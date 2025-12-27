-- 创建字典类型表
CREATE TABLE IF NOT EXISTS dictionary_type (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    type_code VARCHAR(50) NOT NULL COMMENT '字典类型编码',
    type_name VARCHAR(100) NOT NULL COMMENT '字典类型名称',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0：正常，1：停用）',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by VARCHAR(64) COMMENT '创建人',
    update_by VARCHAR(64) COMMENT '更新人',
    UNIQUE KEY uk_type_code (type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

-- 创建字典表
CREATE TABLE IF NOT EXISTS dictionary (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    dict_code VARCHAR(50) NOT NULL COMMENT '字典编码',
    dict_name VARCHAR(100) NOT NULL COMMENT '字典名称',
    dict_type VARCHAR(50) NOT NULL COMMENT '字典类型',
    dict_level INT DEFAULT 1 COMMENT '字典层级',
    parent_id BIGINT DEFAULT 0 COMMENT '父级ID',
    dict_value VARCHAR(100) COMMENT '字典值',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0：正常，1：停用）',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by VARCHAR(64) COMMENT '创建人',
    update_by VARCHAR(64) COMMENT '更新人',
    UNIQUE KEY uk_dict_code (dict_code),
    KEY idx_dict_type (dict_type),
    KEY idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典表';

-- 初始化字典类型数据
INSERT IGNORE INTO dictionary_type (type_code, type_name, status, remark, create_time, create_by)
VALUES 
('sys_user_sex', '用户性别', '0', '用户性别字典', NOW(), 'system'),
('sys_notice_type', '通知类型', '0', '通知类型字典', NOW(), 'system'),
('sys_notice_status', '通知状态', '0', '通知状态字典', NOW(), 'system'),
('sys_job_status', '任务状态', '0', '任务状态字典', NOW(), 'system'),
('sys_job_group', '任务分组', '0', '任务分组字典', NOW(), 'system');

-- 初始化字典数据
INSERT INTO dictionary (dict_code, dict_name, dict_type, dict_level, parent_id, dict_value, sort_order, status, remark, create_time, create_by)
VALUES 
('1', '男', 'sys_user_sex', 1, 0, '1', 1, '0', '性别男', NOW(), 'system'),
('2', '女', 'sys_user_sex', 1, 0, '2', 2, '0', '性别女', NOW(), 'system'),
('3', '未知', 'sys_user_sex', 1, 0, '3', 3, '0', '性别未知', NOW(), 'system'),
('4', '通知', 'sys_notice_type', 1, 0, '1', 1, '0', '通知', NOW(), 'system'),
('5', '公告', 'sys_notice_type', 1, 0, '2', 2, '0', '公告', NOW(), 'system'),
('6', '正常', 'sys_notice_status', 1, 0, '0', 1, '0', '正常状态', NOW(), 'system'),
('7', '关闭', 'sys_notice_status', 1, 0, '1', 2, '0', '关闭状态', NOW(), 'system'),
('8', '正常', 'sys_job_status', 1, 0, '0', 1, '0', '正常状态', NOW(), 'system'),
('9', '暂停', 'sys_job_status', 1, 0, '1', 2, '0', '暂停状态', NOW(), 'system'),
('10', '默认', 'sys_job_group', 1, 0, 'DEFAULT', 1, '0', '默认分组', NOW(), 'system'),
('11', '系统', 'sys_job_group', 1, 0, 'SYSTEM', 2, '0', '系统分组', NOW(), 'system');
