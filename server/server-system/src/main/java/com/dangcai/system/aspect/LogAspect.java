package com.dangcai.system.aspect;

import com.dangcai.common.utils.IpUtils;
import com.dangcai.common.utils.JsonUtils;
import com.dangcai.system.annotation.Log;
import com.dangcai.system.domain.SysOperLog;
import com.dangcai.system.service.SysOperLogService;
import com.dangcai.system.utils.SecurityUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * 操作日志记录切面
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Resource
    private SysOperLogService operLogService;

    /**
     * 线程局部变量，用于存储开始时间
     */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new ThreadLocal<>();

    /**
     * 线程局部变量，用于存储操作日志
     */
    private static final ThreadLocal<SysOperLog> LOG_THREADLOCAL = new ThreadLocal<>();

    /**
     * 前置通知：记录开始时间
     */
    @Before("@annotation(controllerLog)")
    public void doBefore(JoinPoint joinPoint, Log controllerLog) {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    /**
     * 正常返回通知：记录操作日志
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 异常返回通知：记录操作日志
     */
    @AfterThrowing(pointcut = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    /**
     * 处理操作日志
     */
    private void handleLog(JoinPoint joinPoint, Log controllerLog, Exception e, Object jsonResult) {
        try {
            // 1. 获取请求信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return;
            }

            HttpServletRequest request = attributes.getRequest();

            // 2. 创建操作日志对象
            SysOperLog operLog = new SysOperLog();

            // 3. 设置用户信息
            try {
                Long userId = SecurityUtils.getUserId();
                String username = SecurityUtils.getUsername();
                operLog.setUserId(userId);
                operLog.setUserName(username);
            } catch (Exception ex) {
                // 未登录用户
                operLog.setUserName("匿名用户");
            }

            // 4. 设置模块和操作类型
            operLog.setModule(controllerLog.module());
            operLog.setOperation(controllerLog.operation());

            // 5. 设置请求方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = method.getName();
            operLog.setMethod(className + "." + methodName + "()");

            // 6. 设置请求URL
            operLog.setRequestUrl(request.getRequestURI());

            // 7. 设置请求参数
            if (controllerLog.isSaveRequestData()) {
                setRequestParameter(joinPoint, operLog);
            }

            // 8. 设置返回结果
            if (controllerLog.isSaveResponseData() && jsonResult != null) {
                operLog.setResponseResult(JsonUtils.toJsonString(jsonResult));
            }

            // 9. 设置IP和地点
            String ip = IpUtils.getIpAddr(request);
            operLog.setIp(ip);
            operLog.setLocation(IpUtils.isInternalIp(ip) ? "内网IP" : "外网IP");

            // 10. 设置浏览器和操作系统
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            operLog.setBrowser(userAgent.getBrowser().getName());
            operLog.setOs(userAgent.getOperatingSystem().getName());

            // 11. 设置状态和错误信息
            if (e != null) {
                operLog.setStatus(0);
                operLog.setErrorMsg(e.getMessage().substring(0, Math.min(e.getMessage().length(), 2000)));
            } else {
                operLog.setStatus(1);
            }

            // 12. 设置耗时
            Long startTime = TIME_THREADLOCAL.get();
            Long costTime = System.currentTimeMillis() - startTime;
            operLog.setCostTime(costTime);

            // 13. 设置操作时间
            operLog.setOperTime(LocalDateTime.now());

            // 14. 异步保存日志
            operLogService.saveAsync(operLog);

        } catch (Exception exp) {
            log.error("操作日志记录失败：{}", exp.getMessage());
        } finally {
            TIME_THREADLOCAL.remove();
            LOG_THREADLOCAL.remove();
        }
    }

    /**
     * 设置请求参数
     */
    private void setRequestParameter(JoinPoint joinPoint, SysOperLog operLog) {
        try {
            Object[] args = joinPoint.getArgs();
            if (args == null || args.length == 0) {
                return;
            }

            // 过滤掉HttpServletRequest和HttpServletResponse对象
            StringBuilder params = new StringBuilder();
            for (Object arg : args) {
                if (arg != null
                        && !arg.getClass().getName().contains("HttpServletRequest")
                        && !arg.getClass().getName().contains("HttpServletResponse")) {
                    String jsonStr = JsonUtils.toJsonString(arg);
                    params.append(jsonStr).append(" ");
                }
            }

            // 限制长度
            String paramStr = params.toString().trim();
            if (paramStr.length() > 2000) {
                paramStr = paramStr.substring(0, 2000);
            }
            operLog.setRequestParam(paramStr);
        } catch (Exception e) {
            log.error("设置请求参数失败：{}", e.getMessage());
        }
    }
}
