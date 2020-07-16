-- ----------------------------
-- 创建数据库
-- ----------------------------
DROP DATABASE IF EXISTS  `myblog`;
CREATE DATABASE `myblog` DEFAULT CHARACTER SET utf8mb4;

USE myblog;
-- ----------------------------
-- 日志表
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ip` VARCHAR(20) NOT NULL DEFAULT '0.0.0.0' COMMENT '操作地址的IP',
  `addr` VARCHAR(50) NOT NULL DEFAULT '未知' COMMENT '操作者地址',
  `detail` VARCHAR(50) DEFAULT '暂无' COMMENT '操作者描述',
  `create_by` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `remark` VARCHAR(100) NOT NULL DEFAULT '/' COMMENT '操作内容',
  `operate_url` VARCHAR(100) NOT NULL DEFAULT '/' COMMENT '操作的访问地址',
  `operate_by` VARCHAR(500) NULL DEFAULT '未知' COMMENT '操作的浏览器',
  KEY `ip` (`ip`),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

alter table sys_log add index create_by(create_by);

-- ----------------------------
-- 博文分类与文章对照中间表
-- ----------------------------
DROP TABLE IF EXISTS `tbl_article_category`;
CREATE TABLE `tbl_article_category`  (
  `category_name` VARCHAR(20) NOT NULL COMMENT '分类名称',
  `article_id` BIGINT NOT NULL COMMENT '文章id',
  `create_by` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`category_name`,`article_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 博文信息表
-- ----------------------------
DROP TABLE IF EXISTS `tbl_article_info`;
CREATE TABLE `tbl_article_info`  (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '文章标题',
  `summary` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '文章简介，默认100个汉字以内',
  `tags` VARCHAR(200) NOT NULL DEFAULT '' COMMENT '文章标签，根据分类信息共同修改',
  `is_top` TINYINT NOT NULL DEFAULT 0 COMMENT '文章是否置顶，0为否，1为是',
  `traffic` INT NOT NULL DEFAULT 0 COMMENT '文章访问量',
  `modified_by` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `create_by` TIMESTAMP NOT NULL COMMENT '创建时间',
  KEY `is_top` (`is_top`),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 博文主体信息存储表
-- ----------------------------
DROP TABLE IF EXISTS `tbl_article_content`;
CREATE TABLE `tbl_article_content` (
  `article_id` BIGINT NOT NULL COMMENT '对应文章ID',
  `content` TEXT NOT NULL COMMENT '博文主体',
  `modifield_by` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` TIMESTAMP NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`article_id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 分类信息表
-- ----------------------------
DROP TABLE IF EXISTS `tbl_category_info`;
CREATE TABLE `tbl_category_info`  (
  `id` BIGINT(40) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` VARCHAR(20) NOT NULL COMMENT '分类名称',
  `number` INT NOT NULL DEFAULT 0 COMMENT '该分类下的文章数量',
  `picture_url` VARCHAR(200) NOT NULL COMMENT '分类图片地址',
  `is_effective` TINYINT NOT NULL DEFAULT 1 COMMENT '是否有效，默认为1有效，为0无效',
  `modified_by` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '分类修改时间',
  `create_by` TIMESTAMP NOT NULL COMMENT '分类创建时间',
  UNIQUE KEY `name` (`name`),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 技术栈展示表
-- ----------------------------
DROP TABLE IF EXISTS `tbl_technology`;
CREATE TABLE `tbl_technology`  (
  `id` BIGINT(40) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` VARCHAR(20) NOT NULL COMMENT '技术名',
  `picture_url` VARCHAR(200) NOT NULL COMMENT '图标地址',
  `modified_by` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` TIMESTAMP NOT NULL COMMENT '创建时间',
  UNIQUE KEY `name` (`name`),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 图库
-- ----------------------------
DROP TABLE IF EXISTS `tbl_picture`;
CREATE TABLE `tbl_picture`  (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '图片id',
  `picture_url` VARCHAR(200) NOT NULL COMMENT '图片url',
  `picture_url_small` VARCHAR(200) DEFAULT NULL COMMENT '小图地址',
  `picture_type` TINYINT NOT NULL COMMENT '图片类型: 0 首页图片，1 文章题图， 2 博客页图片, 3 后台首页图片',
  `create_by` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  KEY `picture_type` (`picture_type`),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 博文评论表
-- ----------------------------
DROP TABLE IF EXISTS `tbl_article_comment`;
CREATE TABLE `tbl_article_comment`  (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `article_id` BIGINT NOT NULL COMMENT '文章ID',
  `parent_comment_id` BIGINT NOT NULL DEFAULT 0 COMMENT '评论的父ID，0代表无父评论',
  `name` VARCHAR(50) NOT NULL COMMENT '用户自己定义的名称',
  `email` VARCHAR(100) NOT NULL COMMENT '邮箱，用于回复消息',
  `content` TEXT NOT NULL COMMENT '评论内容',
  `ip` VARCHAR(20) NOT NULL DEFAULT '0.0.0.0' COMMENT '评论者IP',
  `is_effective` TINYINT NOT NULL DEFAULT 1 COMMENT '是否有效，默认为1为有效，0为无效',
  `create_by` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论创建日期',
  KEY `parent_comment_id` (`parent_comment_id`),
  KEY `article_id` (`article_id`),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 博客留言表
-- ----------------------------
DROP TABLE IF EXISTS `tbl_message`;
CREATE TABLE `tbl_message`  (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '留言id',
  `name` VARCHAR(50) NOT NULL COMMENT '留言者昵称',
  `email` VARCHAR(100) NOT NULL COMMENT '留言者邮箱',
  `message` TEXT NOT NULL COMMENT '留言详细信息',
  `is_show` TINYINT NOT NULL DEFAULT 0 COMMENT '是否展示，若展示则为1',
  `ip` VARCHAR(20) NOT NULL DEFAULT '0.0.0.0' COMMENT '评论者IP',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '留言时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 友链表
-- ----------------------------
DROP TABLE IF EXISTS `tbl_friends`;
CREATE TABLE `tbl_friends`  (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `url` VARCHAR(100) NOT NULL COMMENT '友链地址',
  `detail` VARCHAR(200) NOT NULL COMMENT '友链描述',
  `name` VARCHAR(50) NOT NULL COMMENT '友链名称',
  `photo` VARCHAR(100) NOT NULL COMMENT '友链头像',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '申请人邮箱',
  `state` TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态，通过为1，不通过为0',
  `modified_by` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '友链修改时间',
  `create_by` TIMESTAMP NOT NULL COMMENT '友链创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 优质博文分享表表
-- ----------------------------
DROP TABLE IF EXISTS `tbl_good_article`;
CREATE TABLE `tbl_good_article`  (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(70) NOT NULL COMMENT '文章标题',
  `url` varchar(200) NOT NULL COMMENT '文章地址',
  `state` TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态，通过为1，不通过为0',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '申请人邮箱',
  `modified_by` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '文章链接修改时间',
  `create_by` TIMESTAMP NOT NULL COMMENT '文章链接创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 资源映射表
-- ----------------------------
DROP TABLE IF EXISTS `tbl_resource`;
CREATE TABLE `tbl_resource`  (
  `res_key` VARCHAR(100) NOT NULL COMMENT '资源名',
  `value` TEXT NOT NULL COMMENT '资源值',
  `modified_by` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '资源修改时间',
  `create_by` TIMESTAMP NOT NULL COMMENT '资源创建时间',
  PRIMARY KEY (`res_key`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

-- 为资源表添加默认数据
INSERT INTO `myblog`.`tbl_resource`(`res_key`, `value`, `modified_by`, `create_by`) VALUES ('用户名', 'Vergessen', '2020-07-15 09:10:30', '2020-07-06 11:22:31');
INSERT INTO `myblog`.`tbl_resource`(`res_key`, `value`, `modified_by`, `create_by`) VALUES ('密码', 'root', '2020-07-15 11:42:42', '2020-07-06 11:22:32');
INSERT INTO `myblog`.`tbl_resource`(`res_key`, `value`, `modified_by`, `create_by`) VALUES ('头像', 'https://www.vergessen.top/imgGo/blog/head.png', '2020-07-15 11:48:44', '2020-07-06 11:22:33');
INSERT INTO `myblog`.`tbl_resource`(`res_key`, `value`, `modified_by`, `create_by`) VALUES ('logo', 'https://www.vergessen.top/imgGo/blog/logo.png', '2020-07-15 12:00:40', '2020-07-06 11:22:34');
INSERT INTO `myblog`.`tbl_resource`(`res_key`, `value`, `modified_by`, `create_by`) VALUES ('introduction', 'MyBlog 管理员', '2020-07-06 11:22:31', '2020-07-06 11:22:35');
INSERT INTO `myblog`.`tbl_resource`(`res_key`, `value`, `modified_by`, `create_by`) VALUES ('我的标签', 'Coder.|IT 民工.|ACG.', '2020-07-15 11:32:11', '2020-07-15 10:19:09');
INSERT INTO `myblog`.`tbl_resource`(`res_key`, `value`, `modified_by`, `create_by`) VALUES ('自我介绍', '<h3>Hello! I am VERGESSEN</h3>\r\n<p>Talk is cheap show me the code!</p>\r\n<ul class=\"personal-info list-unstyled\">\r\n    <li class=\"about-item\">\r\n        <h4>Name</h4>\r\n	<span>VERGESSEN</span>\r\n    </li>\r\n    <li class=\"about-item\">\r\n        <h4>QQ</h4>\r\n        <span><a style=\"color: #f9c828;\" href=\"tencent://AddContact/?fromId=50&fromSubid=1&subcmd=all&uin=1258252786\">点我加好友</a>\r\n        </span>\r\n    </li>\r\n    <li class=\"about-item\">\r\n        <h4>GitHub</h4>\r\n         <span><a style=\"color: #f9c828;\" href=\"https://github.com/VERGESSE\">点我访问 \r\n             </a>\r\n        </span>\r\n    </li>\r\n    <li class=\"about-item\">\r\n        <h4>Email</h4>\r\n        <span><a>vergessentop@163.com</a></span>\r\n    </li>\r\n    <li class=\"about-item\">\r\n        <h4>Address</h4>\r\n        <span>Tian Jin, China</span>\r\n    </li>\r\n</ul>', '2020-07-15 11:21:14', '2020-07-15 10:20:51');
INSERT INTO `myblog`.`tbl_resource`(`res_key`, `value`, `modified_by`, `create_by`) VALUES ('关于我', '<div class=\"col-sm-6\">\r\n    <div class=\"service\">\r\n        <div class=\"service-icon\">\r\n            <i class=\"flaticon-headphones\"></i>\r\n        </div>\r\n        <h4>ACG</h4>\r\n        <p>二次元爱好者</p>\r\n    </div>\r\n</div>\r\n<div class=\"col-sm-6\">\r\n    <div class=\"service two\">\r\n        <div class=\"service-icon\">\r\n            <i class=\"flaticon-code\"></i>\r\n        </div>\r\n        <h4>代码整洁之道</h4>\r\n        <p>对代码格式有执着的追求</p>\r\n    </div>\r\n</div>\r\n<div class=\"col-sm-6\">\r\n    <div class=\"service three\">\r\n        <div class=\"service-icon\">\r\n            <i class=\"flaticon-blogging\"></i>\r\n        </div>\r\n        <h4>夜猫子</h4>\r\n        <p>喜欢晚上安静的时候码代码</p>\r\n    </div>\r\n</div>\r\n<div class=\"col-sm-6\">\r\n    <div class=\"service\">\r\n        <div class=\"service-icon\">\r\n            <i class=\"flaticon-lightbulb\"></i>\r\n        </div>\r\n        <h4>Java</h4>\r\n        <p>Java大法好</p>\r\n    </div>\r\n</div>', '2020-07-15 11:24:00', '2020-07-15 10:27:10');
INSERT INTO `myblog`.`tbl_resource`(`res_key`, `value`, `modified_by`, `create_by`) VALUES ('首页左侧介绍', '<a href=\"https://www.vergessen.top/\" target=\"_blank\">我的主页</a> &copy; 2020 made by Vergessen V2.0<i class=\"fa fa-heart\"></i>', '2020-07-15 12:12:37', '2020-07-15 12:03:14');
INSERT INTO `myblog`.`tbl_resource`(`res_key`, `value`, `modified_by`, `create_by`) VALUES ('备案号信息', '© 2020-2020 vergessen.top 版权所有 ICP证：\n<a href=\"http://beian.miit.gov.cn/\" target=\"_blank\" style=\"color: #f9c828\">津ICP备19009700号</a>', '2020-07-15 12:12:50', '2020-07-15 12:11:12');
INSERT INTO `myblog`.`tbl_resource`(`res_key`, `value`, `modified_by`, `create_by`) VALUES ('友链宣言', '<div>友链申请须知: </div>\n<div>1.先添加上本站信息(https优先)</div>\n<div>2.本人尽快审核，通过后会有邮件通知</div>\n<br/>\n<div>本站信息: </div>\n<div>昵称: Vergessen</div>\n<div>自我介绍: 喜欢二次元的编程练习生</div>\n<div>网站地址: https://www.vergessen.top</div>\n<div>头像地址: https://www.vergessen.top/imgGo/blog/head.png</div>', '2020-07-15 12:12:37', '2020-07-15 12:11:15');
INSERT INTO `myblog`.`tbl_resource`(`res_key`, `value`, `modified_by`, `create_by`) VALUES ('博文底部信息', ' ', '2020-07-16 21:33:55', '2020-07-16 21:33:31');


-- 为图库添加一些初始数据
INSERT INTO `myblog`.`tbl_picture`(`id`, `picture_url`, `picture_url_small`, `picture_type`, `create_by`) VALUES (1637826015100, 'https://www.vergessen.top/imgGo/blog/2020/0712/1594511810_1500x1500.png', NULL, 0, '2020-07-12 07:56:50');
INSERT INTO `myblog`.`tbl_picture`(`id`, `picture_url`, `picture_url_small`, `picture_type`, `create_by`) VALUES (1747839704900, 'https://www.vergessen.top/imgGo/blog/2020/0712/1594511920_1500x1500.png', NULL, 0, '2020-07-12 07:58:40');
INSERT INTO `myblog`.`tbl_picture`(`id`, `picture_url`, `picture_url_small`, `picture_type`, `create_by`) VALUES (1831564717400, 'https://www.vergessen.top/imgGo/blog/2020/0712/1594512004_1980x1980.png', NULL, 2, '2020-07-12 08:00:04');
INSERT INTO `myblog`.`tbl_picture`(`id`, `picture_url`, `picture_url_small`, `picture_type`, `create_by`) VALUES (1849902407900, 'https://www.vergessen.top/imgGo/blog/2020/0712/1594512022_1980x1980.png', NULL, 2, '2020-07-12 08:00:22');
INSERT INTO `myblog`.`tbl_picture`(`id`, `picture_url`, `picture_url_small`, `picture_type`, `create_by`) VALUES (4233746570999, 'https://www.vergessen.top/imgGo/blog/2020/0712/1594514406_600x600.png', 'https://www.vergessen.top/imgGo/blog/2020/0712/1594514406_100x100.png', 1, '2020-07-12 08:40:06');
INSERT INTO `myblog`.`tbl_picture`(`id`, `picture_url`, `picture_url_small`, `picture_type`, `create_by`) VALUES (5018063994800, 'https://www.vergessen.top/imgGo/blog/2020/0712/1594515191_600x600.png', 'https://www.vergessen.top/imgGo/blog/2020/0712/1594515191_100x100.png', 1, '2020-07-12 08:53:10');
INSERT INTO `myblog`.`tbl_picture`(`id`, `picture_url`, `picture_url_small`, `picture_type`, `create_by`) VALUES (5030355226300, 'https://www.vergessen.top/imgGo/blog/2020/0712/1594515203_600x600.png', 'https://www.vergessen.top/imgGo/blog/2020/0712/1594515203_100x100.png', 1, '2020-07-12 08:53:23');
INSERT INTO `myblog`.`tbl_picture`(`id`, `picture_url`, `picture_url_small`, `picture_type`, `create_by`) VALUES (5041719023200, 'https://www.vergessen.top/imgGo/blog/2020/0712/1594515214_600x600.png', 'https://www.vergessen.top/imgGo/blog/2020/0712/1594515214_100x100.png', 1, '2020-07-12 08:53:34');
INSERT INTO `myblog`.`tbl_picture`(`id`, `picture_url`, `picture_url_small`, `picture_type`, `create_by`) VALUES (5052169303400, 'https://www.vergessen.top/imgGo/blog/2020/0712/1594515225_600x600.png', 'https://www.vergessen.top/imgGo/blog/2020/0712/1594515225_100x100.png', 1, '2020-07-12 08:53:44');
INSERT INTO `myblog`.`tbl_picture`(`id`, `picture_url`, `picture_url_small`, `picture_type`, `create_by`) VALUES (5071490993500, 'https://www.vergessen.top/imgGo/blog/2020/0712/1594515244_600x600.png', 'https://www.vergessen.top/imgGo/blog/2020/0712/1594515244_100x100.png', 1, '2020-07-12 08:54:04');
INSERT INTO `myblog`.`tbl_picture`(`id`, `picture_url`, `picture_url_small`, `picture_type`, `create_by`) VALUES (52444654067700, 'https://www.vergessen.top/imgGo/blog/2020/0711/1594476851_1980x1980.png', NULL, 3, '2020-07-11 22:14:11');
INSERT INTO `myblog`.`tbl_picture`(`id`, `picture_url`, `picture_url_small`, `picture_type`, `create_by`) VALUES (52494144453300, 'https://www.vergessen.top/imgGo/blog/2020/0711/1594476901_1980x1980.png', NULL, 3, '2020-07-11 22:15:00');
INSERT INTO `myblog`.`tbl_picture`(`id`, `picture_url`, `picture_url_small`, `picture_type`, `create_by`) VALUES (52518571529400, 'https://www.vergessen.top/imgGo/blog/2020/0711/1594476925_1980x1980.png', NULL, 3, '2020-07-11 22:15:25');
INSERT INTO `myblog`.`tbl_picture`(`id`, `picture_url`, `picture_url_small`, `picture_type`, `create_by`) VALUES (52529165447800, 'https://www.vergessen.top/imgGo/blog/2020/0711/1594476936_1980x1980.png', NULL, 3, '2020-07-11 22:15:35');
