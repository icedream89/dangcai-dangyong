package com.dangcai.system.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 模块名称
     *
     * @return 模块名称
     */
    String module() default "";

    /**
     * 操作类型
     *
     * @return 操作类型
     */
    String operation() default "";

    /**
     * 是否保存请求参数
     *
     * @return true-保存 false-不保存
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存响应参数
     *
     * @return true-保存 false-不保存
     */
    boolean isSaveResponseData() default true;
}
