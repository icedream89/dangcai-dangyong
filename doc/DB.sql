-- ==========================================
-- 当才当用 - 数据库初始化脚本
-- 数据库版本: MySQL 8.0+
-- 创建时间: 2026-01-07
-- ==========================================

CREATE DATABASE IF NOT EXISTS `dang_cai_dang_yong` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `dang_cai_dang_yong`;

-- ==========================================
-- 一、系统核心表（6张）
-- ==========================================

-- 1. 用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（BCrypt加密）',
  `real_name` VARCHAR(50) COMMENT '真实姓名',
  `phone` VARCHAR(20) COMMENT '手机号',
  `email` VARCHAR(100) COMMENT '邮箱',
  `avatar` VARCHAR(255) COMMENT '头像',
  `user_type` TINYINT NOT NULL DEFAULT 1 COMMENT '用户类型：1-管理员 2-企业管理员 3-企业员工 4-小程序用户',
  `enterprise_id` BIGINT COMMENT '关联企业ID（企业用户必填）',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
  `last_login_time` DATETIME COMMENT '最后登录时间',
  `last_login_ip` VARCHAR(50) COMMENT '最后登录IP',
  `remark` VARCHAR(500) COMMENT '备注',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_phone` (`phone`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  KEY `idx_user_type` (`user_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 角色表
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
  `data_scope` TINYINT NOT NULL DEFAULT 1 COMMENT '数据权限范围：1-全部数据 2-本企业数据 3-本部门数据 4-本人数据',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `remark` VARCHAR(500) COMMENT '备注',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 3. 菜单权限表
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父菜单ID（0为根菜单）',
  `menu_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
  `menu_type` TINYINT NOT NULL COMMENT '菜单类型：1-目录 2-菜单 3-按钮',
  `path` VARCHAR(200) COMMENT '路由路径',
  `component` VARCHAR(200) COMMENT '组件路径',
  `permission` VARCHAR(100) COMMENT '权限标识',
  `icon` VARCHAR(100) COMMENT '菜单图标',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `visible` TINYINT NOT NULL DEFAULT 1 COMMENT '是否可见：0-隐藏 1-显示',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

-- 4. 用户角色关联表
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 5. 角色菜单关联表
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- 6. 系统配置表
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
  `config_value` VARCHAR(500) COMMENT '配置值',
  `config_type` TINYINT NOT NULL DEFAULT 1 COMMENT '配置类型：1-系统配置 2-业务配置',
  `remark` VARCHAR(500) COMMENT '备注',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- ==========================================
-- 二、企业核心表（3张）
-- ==========================================

-- 7. 企业表
DROP TABLE IF EXISTS `ent_enterprise`;
CREATE TABLE `ent_enterprise` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '企业ID',
  `enterprise_name` VARCHAR(200) NOT NULL COMMENT '企业名称',
  `unified_code` VARCHAR(50) NOT NULL COMMENT '统一社会信用代码',
  `legal_person` VARCHAR(50) NOT NULL COMMENT '法人代表',
  `legal_person_phone` VARCHAR(20) NOT NULL COMMENT '法人电话',
  `registered_capital` DECIMAL(15,2) COMMENT '注册资本（万元）',
  `establish_date` DATE COMMENT '成立日期',
  `address` VARCHAR(500) COMMENT '企业地址',
  `industry` VARCHAR(50) COMMENT '所属行业',
  `enterprise_type` VARCHAR(50) COMMENT '企业类型：国有企业、民营企业、外资企业',
  `scale` VARCHAR(50) COMMENT '企业规模：小型、中型、大型',
  `intro` TEXT COMMENT '企业简介',
  `business_scope` TEXT COMMENT '经营范围',
  `is_recommended` TINYINT NOT NULL DEFAULT 0 COMMENT '是否推荐：0-否 1-是',
  `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-待审核 1-正常 2-禁用',
  `audit_remark` VARCHAR(500) COMMENT '审核备注',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_unified_code` (`unified_code`),
  KEY `idx_industry` (`industry`),
  KEY `idx_status` (`status`),
  KEY `idx_is_recommended` (`is_recommended`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='企业表';

-- 8. 企业员工表
DROP TABLE IF EXISTS `ent_employee`;
CREATE TABLE `ent_employee` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '员工ID',
  `enterprise_id` BIGINT NOT NULL COMMENT '企业ID',
  `user_id` BIGINT NOT NULL COMMENT '关联用户ID',
  `employee_name` VARCHAR(50) NOT NULL COMMENT '员工姓名',
  `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
  `position` VARCHAR(100) COMMENT '职位',
  `department` VARCHAR(100) COMMENT '部门',
  `is_admin` TINYINT NOT NULL DEFAULT 0 COMMENT '是否企业管理员：0-否 1-是',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-离职 1-在职',
  `join_date` DATE COMMENT '入职日期',
  `remark` VARCHAR(500) COMMENT '备注',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_enterprise_id` (`enterprise_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='企业员工表';

-- 9. 企业变更记录表
DROP TABLE IF EXISTS `ent_enterprise_log`;
CREATE TABLE `ent_enterprise_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `enterprise_id` BIGINT NOT NULL COMMENT '企业ID',
  `operation_type` VARCHAR(20) NOT NULL COMMENT '操作类型：新增、修改、审核、状态变更',
  `operation_content` TEXT COMMENT '操作内容（JSON格式）',
  `operator` VARCHAR(50) COMMENT '操作人',
  `operation_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `remark` VARCHAR(500) COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  KEY `idx_operation_time` (`operation_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='企业变更记录表';

-- ==========================================
-- 三、业务核心表（9张）
-- ==========================================

-- 10. 分类表（树形结构）
DROP TABLE IF EXISTS `biz_category`;
CREATE TABLE `biz_category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父分类ID（0为根分类）',
  `category_name` VARCHAR(100) NOT NULL COMMENT '分类名称',
  `category_code` VARCHAR(50) COMMENT '分类编码',
  `icon` VARCHAR(100) COMMENT '图标',
  `image` VARCHAR(255) COMMENT '图片',
  `manage_mode` VARCHAR(20) NOT NULL DEFAULT 'list' COMMENT '管理模式：list-列表 grid-网格 tree-树形 image-图文',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_category_code` (`category_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- 11. 产品表
DROP TABLE IF EXISTS `biz_product`;
CREATE TABLE `biz_product` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '产品ID',
  `enterprise_id` BIGINT NOT NULL COMMENT '企业ID',
  `product_name` VARCHAR(200) NOT NULL COMMENT '产品名称',
  `category` VARCHAR(50) COMMENT '产品分类',
  `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
  `unit` VARCHAR(20) COMMENT '单位',
  `cover_image` VARCHAR(255) COMMENT '封面图片',
  `images` TEXT COMMENT '产品图片（JSON数组）',
  `description` TEXT COMMENT '产品描述',
  `specs` TEXT COMMENT '规格参数（JSON格式）',
  `recommend_weight` INT NOT NULL DEFAULT 0 COMMENT '推荐权重（0-100）',
  `sales_count` INT NOT NULL DEFAULT 0 COMMENT '销量',
  `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-下架 1-上架',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  KEY `idx_category` (`category`),
  KEY `idx_recommend_weight` (`recommend_weight`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品表';

-- 12. 政策表
DROP TABLE IF EXISTS `biz_policy`;
CREATE TABLE `biz_policy` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '政策ID',
  `policy_title` VARCHAR(200) NOT NULL COMMENT '政策标题',
  `policy_no` VARCHAR(50) COMMENT '政策编号',
  `policy_type` VARCHAR(50) COMMENT '政策类型',
  `publish_date` DATE COMMENT '发布日期',
  `publish_dept` VARCHAR(200) COMMENT '发布部门',
  `apply_start_date` DATE COMMENT '申报开始日期',
  `apply_end_date` DATE COMMENT '申报结束日期',
  `cover_image` VARCHAR(255) COMMENT '封面图片',
  `summary` VARCHAR(500) COMMENT '政策摘要',
  `content` TEXT COMMENT '政策内容（富文本）',
  `attachments` TEXT COMMENT '附件（JSON数组）',
  `tags` VARCHAR(500) COMMENT '标签（逗号分隔）',
  `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
  `is_hot` TINYINT NOT NULL DEFAULT 0 COMMENT '是否热门：0-否 1-是',
  `is_top` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶：0-否 1-是',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-下架 1-发布',
  `publish_time` DATETIME COMMENT '发布时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  KEY `idx_policy_type` (`policy_type`),
  KEY `idx_publish_date` (`publish_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='政策表';

-- 13. 企业课堂表
DROP TABLE IF EXISTS `biz_course`;
CREATE TABLE `biz_course` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '课堂ID',
  `course_title` VARCHAR(200) NOT NULL COMMENT '课堂标题',
  `course_type` VARCHAR(50) COMMENT '课堂类型：视频、文档、图文',
  `cover_image` VARCHAR(255) COMMENT '封面图片',
  `category` VARCHAR(50) COMMENT '分类',
  `summary` VARCHAR(500) COMMENT '简介',
  `content` TEXT COMMENT '内容（富文本或视频URL）',
  `attachments` TEXT COMMENT '附件（JSON数组）',
  `duration` INT COMMENT '时长（秒）',
  `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
  `is_recommended` TINYINT NOT NULL DEFAULT 0 COMMENT '是否推荐：0-否 1-是',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-下架 1-发布',
  `publish_time` DATETIME COMMENT '发布时间',
  `create_by` VARCHAR(50) COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) COMMENT '更新人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='企业课堂表';

-- 14. 求助工单表
DROP TABLE IF EXISTS `biz_help_ticket`;
CREATE TABLE `biz_help_ticket` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '工单ID',
  `ticket_no` VARCHAR(50) NOT NULL COMMENT '工单编号',
  `enterprise_id` BIGINT COMMENT '企业ID（企业用户提交必填）',
  `user_id` BIGINT NOT NULL COMMENT '提交人ID',
  `user_name` VARCHAR(50) NOT NULL COMMENT '提交人姓名',
  `user_phone` VARCHAR(20) NOT NULL COMMENT '提交人电话',
  `help_type` VARCHAR(50) NOT NULL COMMENT '求助类型',
  `title` VARCHAR(200) NOT NULL COMMENT '工单标题',
  `content` TEXT NOT NULL COMMENT '工单内容',
  `images` TEXT COMMENT '图片（JSON数组）',
  `priority` TINYINT NOT NULL DEFAULT 2 COMMENT '优先级：1-紧急 2-普通 3-低',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-待处理 2-处理中 3-已完成 4-已关闭',
  `handler_id` BIGINT COMMENT '处理人ID',
  `handle_result` TEXT COMMENT '处理结果',
  `handle_time` DATETIME COMMENT '处理时间',
  `satisfaction` TINYINT COMMENT '满意度评分：1-5分',
  `feedback` TEXT COMMENT '用户反馈',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ticket_no` (`ticket_no`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='求助工单表';

-- 15. 工单流转记录表
DROP TABLE IF EXISTS `biz_help_flow`;
CREATE TABLE `biz_help_flow` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '流转ID',
  `ticket_id` BIGINT NOT NULL COMMENT '工单ID',
  `from_status` TINYINT COMMENT '原状态',
  `to_status` TINYINT NOT NULL COMMENT '新状态',
  `operator_id` BIGINT NOT NULL COMMENT '操作人ID',
  `operator_name` VARCHAR(50) COMMENT '操作人姓名',
  `operation` VARCHAR(100) COMMENT '操作说明',
  `remark` TEXT COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_ticket_id` (`ticket_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工单流转记录表';

-- 16. 采购需求表
DROP TABLE IF EXISTS `biz_purchase_requirement`;
CREATE TABLE `biz_purchase_requirement` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '需求ID',
  `requirement_no` VARCHAR(50) NOT NULL COMMENT '需求编号',
  `enterprise_id` BIGINT COMMENT '发布企业ID',
  `user_id` BIGINT NOT NULL COMMENT '发布人ID',
  `user_name` VARCHAR(50) NOT NULL COMMENT '发布人姓名',
  `user_phone` VARCHAR(20) NOT NULL COMMENT '发布人电话',
  `title` VARCHAR(200) NOT NULL COMMENT '需求标题',
  `category` VARCHAR(50) COMMENT '需求分类',
  `content` TEXT NOT NULL COMMENT '需求描述',
  `budget` DECIMAL(12,2) COMMENT '预算金额',
  `expect_date` DATE COMMENT '期望交付日期',
  `address` VARCHAR(500) COMMENT '需求地址',
  `images` TEXT COMMENT '图片（JSON数组）',
  `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
  `reply_count` INT NOT NULL DEFAULT 0 COMMENT '回复数',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-待匹配 2-已匹配 3-已完成 4-已关闭',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_requirement_no` (`requirement_no`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购需求表';

-- 17. 采购需求回复表
DROP TABLE IF EXISTS `biz_purchase_reply`;
CREATE TABLE `biz_purchase_reply` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '回复ID',
  `requirement_id` BIGINT NOT NULL COMMENT '需求ID',
  `enterprise_id` BIGINT NOT NULL COMMENT '回复企业ID',
  `enterprise_name` VARCHAR(200) NOT NULL COMMENT '企业名称',
  `contact_person` VARCHAR(50) NOT NULL COMMENT '联系人',
  `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
  `reply_content` TEXT NOT NULL COMMENT '回复内容',
  `quote_price` DECIMAL(12,2) COMMENT '报价',
  `delivery_period` INT COMMENT '交付周期（天）',
  `attachments` TEXT COMMENT '附件（JSON数组）',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-已回复 2-已确认 3-已完成交易',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '回复时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_requirement_id` (`requirement_id`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购需求回复表';

-- 18. 需求匹配表
DROP TABLE IF EXISTS `biz_purchase_match`;
CREATE TABLE `biz_purchase_match` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '匹配ID',
  `requirement_id` BIGINT NOT NULL COMMENT '需求ID',
  `enterprise_id` BIGINT NOT NULL COMMENT '匹配企业ID',
  `enterprise_name` VARCHAR(200) NOT NULL COMMENT '企业名称',
  `operator_id` BIGINT NOT NULL COMMENT '操作人ID（管理员）',
  `operator_name` VARCHAR(50) COMMENT '操作人姓名',
  `match_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '匹配时间',
  `has_replied` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已回复：0-否 1-是',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-已匹配 2-已取消',
  `cancel_time` DATETIME COMMENT '取消时间',
  `cancel_reason` VARCHAR(500) COMMENT '取消原因',
  PRIMARY KEY (`id`),
  KEY `idx_requirement_id` (`requirement_id`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  KEY `idx_match_time` (`match_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='需求匹配表';

-- 19. 消息订阅表
DROP TABLE IF EXISTS `biz_message_subscribe`;
CREATE TABLE `biz_message_subscribe` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订阅ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `subscribe_type` VARCHAR(50) NOT NULL COMMENT '订阅类型：policy-政策 ticket-工单 requirement-需求回复',
  `template_id` VARCHAR(100) COMMENT '微信模板ID',
  `subscribe_status` TINYINT NOT NULL DEFAULT 1 COMMENT '订阅状态：0-取消订阅 1-已订阅',
  `last_push_time` DATETIME COMMENT '最后推送时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_type` (`user_id`, `subscribe_type`),
  KEY `idx_subscribe_status` (`subscribe_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息订阅表';

-- ==========================================
-- 四、日志表（4张）
-- ==========================================

-- 20. 操作日志表
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` BIGINT COMMENT '操作用户ID',
  `user_name` VARCHAR(50) COMMENT '操作用户名',
  `module` VARCHAR(50) COMMENT '模块名称',
  `operation` VARCHAR(100) COMMENT '操作类型',
  `method` VARCHAR(200) COMMENT '请求方法',
  `request_url` VARCHAR(500) COMMENT '请求URL',
  `request_param` TEXT COMMENT '请求参数',
  `response_result` TEXT COMMENT '返回结果',
  `ip` VARCHAR(50) COMMENT '操作IP',
  `location` VARCHAR(200) COMMENT '操作地点',
  `browser` VARCHAR(100) COMMENT '浏览器',
  `os` VARCHAR(100) COMMENT '操作系统',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-失败 1-成功',
  `error_msg` TEXT COMMENT '错误信息',
  `cost_time` BIGINT COMMENT '耗时（毫秒）',
  `oper_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_oper_time` (`oper_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- 21. 登录日志表
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` BIGINT COMMENT '用户ID',
  `user_name` VARCHAR(50) COMMENT '用户名',
  `login_type` VARCHAR(20) COMMENT '登录类型：后台登录、小程序登录',
  `ip` VARCHAR(50) COMMENT '登录IP',
  `location` VARCHAR(200) COMMENT '登录地点',
  `browser` VARCHAR(100) COMMENT '浏览器',
  `os` VARCHAR(100) COMMENT '操作系统',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-失败 1-成功',
  `msg` VARCHAR(500) COMMENT '提示信息',
  `login_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志表';

-- 22. 文件记录表
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `file_name` VARCHAR(200) NOT NULL COMMENT '文件名',
  `file_path` VARCHAR(500) NOT NULL COMMENT '文件路径',
  `file_size` BIGINT NOT NULL COMMENT '文件大小（字节）',
  `file_type` VARCHAR(50) NOT NULL COMMENT '文件类型',
  `file_ext` VARCHAR(20) COMMENT '文件扩展名',
  `business_type` VARCHAR(50) COMMENT '业务类型：enterprise-企业 product-产品 policy-政策等',
  `business_id` BIGINT COMMENT '业务ID',
  `upload_user_id` BIGINT COMMENT '上传用户ID',
  `storage_type` VARCHAR(20) NOT NULL DEFAULT 'minio' COMMENT '存储类型：minio-对象存储 local-本地存储',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_business` (`business_type`, `business_id`),
  KEY `idx_upload_user_id` (`upload_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件记录表';

-- ==========================================
-- 初始化数据
-- ==========================================

-- 插入默认管理员账号（密码：123456，已BCrypt加密）
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `user_type`, `status`) VALUES
(1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE/sWlsQhcTlTm', '系统管理员', 1, 1);

-- 插入默认角色
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `data_scope`, `sort_order`) VALUES
(1, '超级管理员', 'SUPER_ADMIN', 1, 1),
(2, '政府管理员', 'GOV_ADMIN', 1, 2),
(3, '企业管理员', 'ENT_ADMIN', 2, 3),
(4, '企业员工', 'ENT_EMPLOYEE', 2, 4),
(5, '小程序用户', 'MINIAPP_USER', 4, 5);

-- 关联管理员角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- 插入默认系统配置
INSERT INTO `sys_config` (`config_key`, `config_value`, `config_type`, `remark`) VALUES
('system.name', '当才当用', 1, '系统名称'),
('system.logo', '/logo.png', 1, '系统Logo'),
('wechat.miniapp.appid', '', 1, '微信小程序AppID'),
('wechat.miniapp.secret', '', 1, '微信小程序AppSecret'),
('file.upload.max_size', '52428800', 1, '文件上传最大大小（字节）默认50MB'),
('file.upload.allow_types', 'jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx', 1, '允许上传的文件类型');

-- ==========================================
-- 创建视图（可选）
-- ==========================================

-- 企业员工详情视图
CREATE OR REPLACE VIEW `v_employee_detail` AS
SELECT
  e.*,
  u.username,
  u.phone AS user_phone,
  u.email,
  ent.enterprise_name
FROM `ent_employee` e
LEFT JOIN `sys_user` u ON e.user_id = u.id
LEFT JOIN `ent_enterprise` ent ON e.enterprise_id = ent.id
WHERE e.del_flag = 0;

-- ==========================================
-- 脚本结束
-- ==========================================
