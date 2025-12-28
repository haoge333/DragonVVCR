-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS dragonvvcr DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建应用用户并授权
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON dragonvvcr.* TO 'root'@'%';
FLUSH PRIVILEGES;

USE dragonvvcr;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    region VARCHAR(255) NOT NULL,
    server VARCHAR(255) NOT NULL,
    game_id VARCHAR(255) NOT NULL,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0,
    INDEX idx_username (username),
    INDEX idx_game_id (game_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建吐槽表
CREATE TABLE IF NOT EXISTS complaints (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    target_player_id VARCHAR(255) NOT NULL,
    dungeon_type VARCHAR(255) NOT NULL,
    description TEXT,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0,
    INDEX idx_user_id (user_id),
    INDEX idx_target_player_id (target_player_id),
    INDEX idx_dungeon_type (dungeon_type),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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


-- 初始化admin用户
INSERT INTO users (username, password, region, server, game_id) 
VALUES ('admin', '21232f297a57a5a743894a0e4a801fc3', '怀旧服', '管理员服务器', '龙之谷怀旧服GM')
ON DUPLICATE KEY UPDATE username = username;

-- 初始化字典类型数据
INSERT IGNORE INTO dictionary_type (type_code, type_name, status, remark, create_time, create_by)
VALUES
    ('sys_user_sex', '用户性别', '0', '用户性别字典', NOW(), 'system'),
    ('sys_notice_type', '通知类型', '0', '通知类型字典', NOW(), 'system'),
    ('sys_notice_status', '通知状态', '0', '通知状态字典', NOW(), 'system'),
    ('sys_job_status', '任务状态', '0', '任务状态字典', NOW(), 'system'),
    ('sys_job_group', '任务分组', '0', '任务分组字典', NOW(), 'system'),
    ('sys_server_type', '服务器', '0', '怀旧服服务器', NOW(), 'system'),
    ('sys_nest_type', '副本类型', '0', '副本类型字典', NOW(), 'system'),
    ('sys_daqu_type', '大区类型', '0', '大区类型字典', NOW(), 'system');

-- 初始化字典数据
INSERT INTO dictionary (dict_code, dict_name, dict_type, dict_level, parent_id, dict_value, sort_order, status, remark, create_time, create_by)
VALUES
    ('male', '男', 'sys_user_sex', 1, 0, '1', 1, '0', '性别男', NOW(), 'system'),
    ('female', '女', 'sys_user_sex', 1, 0, '2', 2, '0', '性别女', NOW(), 'system'),
    ('unknown', '未知', 'sys_user_sex', 1, 0, '3', 3, '0', '性别未知', NOW(), 'system'),
    ('notice', '通知', 'sys_notice_type', 1, 0, '1', 1, '0', '通知', NOW(), 'system'),
    ('announcement', '公告', 'sys_notice_type', 1, 0, '2', 2, '0', '公告', NOW(), 'system'),
    ('normal', '正常', 'sys_notice_status', 1, 0, '0', 1, '0', '正常状态', NOW(), 'system'),
    ('closed', '关闭', 'sys_notice_status', 1, 0, '1', 2, '0', '关闭状态', NOW(), 'system'),
    ('job_normal', '正常', 'sys_job_status', 1, 0, '0', 1, '0', '正常状态', NOW(), 'system'),
    ('paused', '暂停', 'sys_job_status', 1, 0, '1', 2, '0', '暂停状态', NOW(), 'system'),
    ('default', '默认', 'sys_job_group', 1, 0, 'DEFAULT', 1, '0', '默认分组', NOW(), 'system'),
    ('system', '系统', 'sys_job_group', 1, 0, 'SYSTEM', 2, '0', '系统分组', NOW(), 'system'),
    ('salon_hardcore', '沙龙硬核', 'sys_nest_type', 1, 0, '沙龙硬核', 1, '0', '副本类型-沙龙硬核', NOW(), 'system'),
    ('green_dragon_classic', '绿龙经典', 'sys_nest_type', 1, 0, '绿龙经典', 3, '0', '副本类型-绿龙经典', NOW(), 'system'),
    ('green_dragon_hardcore', '绿龙硬核', 'sys_nest_type', 1, 0, '绿龙硬核', 2, '0', '副本类型-绿龙硬核', NOW(), 'system'),
    ('nostalgic_region', '怀旧大区', 'sys_daqu_type', 1, 0, '怀旧大区', 0, '0', '', NOW(), 'system'),
    ('server1_holy_paradise', '1服-神圣天堂', 'sys_server_type', 1, 0, '神圣天堂', 0, '0', '怀旧服大区-1服-神圣天堂', NOW(), 'system'),
    ('server2_theresia', '2服-特蕾西亚', 'sys_server_type', 1, 0, '2服-特蕾西亚', 1, '0', '怀旧服大区-2服-特蕾西亚', NOW(), 'system'),
    ('server3_althaea', '3服-阿尔泰亚', 'sys_server_type', 1, 0, '3服-阿尔泰亚', 2, '0', '怀旧服大区-3服-阿尔泰亚', NOW(), 'system'),
    ('server4_saffron', '4服-萨芬特拉', 'sys_server_type', 1, 0, '4服-萨芬特拉', 0, '0', '怀旧服大区-4服-萨芬特拉', NOW(), 'system');
