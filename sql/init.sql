-- ============================================================
-- 校园活动管理系统 - 数据库初始化脚本
-- 适用：MySQL 8.x
-- 使用前请确认 DBUtil 中的账号密码与本机 MySQL 一致
-- ============================================================
CREATE DATABASE IF NOT EXISTS campus_activity
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE campus_activity;
-- ---------- 用户表 ----------
CREATE TABLE IF NOT EXISTS users (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  username    VARCHAR(50)  NOT NULL UNIQUE COMMENT '登录用户名',
  password    VARCHAR(64)  NOT NULL COMMENT 'SHA-256 密码摘要',
  nickname    VARCHAR(100) DEFAULT NULL COMMENT '昵称',
  role        VARCHAR(20)  NOT NULL DEFAULT 'user' COMMENT '角色：admin / leader / user',
  create_time TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户';

-- 默认管理员账号：admin / 123456
INSERT INTO users (username, password, nickname, role)
SELECT 'admin', '8e4f1076757a59853aa7341fad475bb0ece7dd1792b5af8294bfea7f414120d9', '管理员', 'admin'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- 示例负责人账号：leader / 123456
INSERT INTO users (username, password, nickname, role)
SELECT 'leader', '8e4f1076757a59853aa7341fad475bb0ece7dd1792b5af8294bfea7f414120d9', '社团负责人', 'leader'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'leader');

-- ---------- 讲座表 ----------
CREATE TABLE IF NOT EXISTS lecture (
  id      INT AUTO_INCREMENT PRIMARY KEY,
  title   VARCHAR(200) NOT NULL COMMENT '讲座标题',
  time    VARCHAR(100) DEFAULT NULL COMMENT '举办时间',
  address VARCHAR(200) DEFAULT NULL COMMENT '地点',
  content TEXT         DEFAULT NULL COMMENT '讲座内容',
  userId  INT          DEFAULT NULL COMMENT '发布人ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='讲座信息';

-- ---------- 社团活动表 ----------
CREATE TABLE IF NOT EXISTS club (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  title         VARCHAR(200) NOT NULL COMMENT '活动名称',
  time          VARCHAR(100) DEFAULT NULL COMMENT '活动时间',
  content       TEXT         DEFAULT NULL COMMENT '活动介绍',
  has_cert      VARCHAR(20)  DEFAULT NULL COMMENT '是否颁发证书',
  has_volunteer VARCHAR(20)  DEFAULT NULL COMMENT '是否计入志愿时长',
  has_prize     VARCHAR(20)  DEFAULT NULL COMMENT '是否可评优获奖',
  userId        INT          DEFAULT NULL COMMENT '发布人ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团活动';

-- ---------- 竞赛表 ----------
CREATE TABLE IF NOT EXISTS competition (
  id       INT AUTO_INCREMENT PRIMARY KEY,
  title    VARCHAR(200) NOT NULL COMMENT '竞赛标题',
  time     VARCHAR(100) DEFAULT NULL COMMENT '竞赛时间',
  content  TEXT         DEFAULT NULL COMMENT '竞赛详情',
  location VARCHAR(200) DEFAULT NULL COMMENT '竞赛地点',
  userId   INT          DEFAULT NULL COMMENT '发布人ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='竞赛信息';
-- ---------- 评优表 ----------
CREATE TABLE IF NOT EXISTS award (
  id           INT AUTO_INCREMENT PRIMARY KEY,
  title        VARCHAR(200) NOT NULL COMMENT '评优标题',
  file_name    VARCHAR(255) DEFAULT NULL COMMENT '附件原始文件名',
  file_path    VARCHAR(500) DEFAULT NULL COMMENT '附件存储路径',
  publish_time VARCHAR(50)  DEFAULT NULL COMMENT '发布时间',
  details      TEXT         DEFAULT NULL COMMENT '评优详情',
  userId       INT          DEFAULT NULL COMMENT '发布人ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评优信息';
-- ---------- 用户收藏表 ----------
CREATE TABLE IF NOT EXISTS favorites (
  id             INT AUTO_INCREMENT PRIMARY KEY,
  user_id        INT          NOT NULL COMMENT '用户ID',
  activity_type  VARCHAR(20)  NOT NULL COMMENT '活动类型：lecture/club/competition/award',
  activity_id    INT          NOT NULL COMMENT '活动ID',
  activity_title VARCHAR(200) NOT NULL COMMENT '活动标题快照',
  create_time    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  UNIQUE KEY uk_user_activity (user_id, activity_type, activity_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏';
-- ---------- 浏览记录表 ----------
CREATE TABLE IF NOT EXISTS browse_record (
  id             INT AUTO_INCREMENT PRIMARY KEY,
  user_id        INT          NOT NULL COMMENT '用户ID',
  activity_type  VARCHAR(20)  NOT NULL COMMENT '活动类型',
  activity_id    INT          NOT NULL DEFAULT 0 COMMENT '活动ID，0表示列表浏览',
  activity_title VARCHAR(200) NOT NULL COMMENT '浏览标题',
  browse_time    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
  KEY idx_user_time (user_id, browse_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='浏览记录';
-- ---------- 消息中心表 ----------
CREATE TABLE IF NOT EXISTS messages (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  user_id     INT          NOT NULL COMMENT '接收用户ID',
  title       VARCHAR(200) NOT NULL COMMENT '消息标题',
  content     TEXT         NOT NULL COMMENT '消息内容',
  type        VARCHAR(20)  NOT NULL DEFAULT 'system' COMMENT '类型：system/activity',
  is_read     TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否已读',
  create_time TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  KEY idx_user_read (user_id, is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户消息';
-- ALTER TABLE users MODIFY COLUMN role VARCHAR(20) NOT NULL DEFAULT 'user' COMMENT '角色：admin / leader / user';
-- ALTER TABLE lecture ADD COLUMN userId INT DEFAULT NULL COMMENT '发布人ID';
-- ALTER TABLE club ADD COLUMN userId INT DEFAULT NULL COMMENT '发布人ID';
-- ALTER TABLE competition ADD COLUMN userId INT DEFAULT NULL COMMENT '发布人ID';
-- ALTER TABLE award ADD COLUMN userId INT DEFAULT NULL COMMENT '发布人ID';
