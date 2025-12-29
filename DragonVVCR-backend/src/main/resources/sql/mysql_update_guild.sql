-- 增量更新SQL文件 - 添加工会字段
-- 适用于已部署的DragonVVCR项目

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 为users表添加guild字段
ALTER TABLE users ADD COLUMN guild VARCHAR(255) DEFAULT '' COMMENT '玩家工会';

-- 为users表的guild字段添加索引
ALTER TABLE users ADD INDEX idx_guild (guild);

-- 为complaints表添加target_guild字段
ALTER TABLE complaints ADD COLUMN target_guild VARCHAR(255) DEFAULT '' COMMENT '目标玩家工会';

-- 为complaints表的target_guild字段添加索引
ALTER TABLE complaints ADD INDEX idx_target_guild (target_guild);

SET FOREIGN_KEY_CHECKS = 1;
