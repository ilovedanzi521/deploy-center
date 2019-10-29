
--组、机器及其关联表;
CREATE TABLE IF NOT EXISTS dc_group(
	id BIGINT(20) NOT NULL auto_increment COMMENT '主键ID',
	name VARCHAR(32) NOT NULL COMMENT '组名称',
	desc VARCHAR(255) NULL DEFAULT NULL COMMENT '组描述',
	create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE KEY (name)
) COMMENT='组信息表';
CREATE TABLE IF NOT EXISTS dc_device(
	id BIGINT(20) NOT NULL auto_increment COMMENT '主键ID',
	name VARCHAR(32) NOT NULL COMMENT '机器名称',
	alias VARCHAR(32) NULL DEFAULT NULL COMMENT '机器别名',
	ip_address VARCHAR(32) NOT NULL COMMENT 'ip地址：IPV4',
	user_name VARCHAR(32) NOT NULL COMMENT '用户名：SSH/SCP LOGIN USER',
	port int(5) NOT NULL COMMENT '端口号：SSH/SCP PORT',
	os_type VARCHAR(32) NOT NULL COMMENT '操作系统类型',
	status int(2) NOT NULL DEFAULT 0 COMMENT '状态：0-未连接;1-连接成功;2-连接失败',
	desc VARCHAR(255) NULL DEFAULT NULL COMMENT '描述',
	create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE KEY (ip_address)
) COMMENT='机器信息表';
CREATE TABLE IF NOT EXISTS dc_group_device_ref(
	group_id BIGINT(20) NOT NULL COMMENT '组ID',
	device_id BIGINT(20) NOT NULL COMMENT '机器ID'
) COMMENT='组与机器关联表';
CREATE TABLE IF NOT EXISTS dc_device_module_ref(
	device_id BIGINT(20) NOT NULL COMMENT '机器ID',
	module_id BIGINT(20) NOT NULL COMMENT '应用模块id',
	primary key(device_id, module_id)
) COMMENT='机器模块关联表';

--应用模块信息表
CREATE TABLE IF NOT EXISTS dc_app_module(
	id BIGINT(20) NOT NULL auto_increment COMMENT '主键ID',
	name VARCHAR(32) NOT NULL COMMENT '应用模块逻辑名',
	path VARCHAR(200) NOT NULL COMMENT '脚本路径',
	pack_dir VARCHAR(255) NOT NULL COMMENT '安装包相对路径',
	pack_ver VARCHAR(32) NOT NULL COMMENT '安装包版本',
	pack_file VARCHAR(255) NOT NULL COMMENT '安装包文件名',
	help VARCHAR(500) NULL COMMENT '帮助信息',
	desc VARCHAR(255) NULL COMMENT '描述信息',
	allow_delete INT(1) NOT NULL DEFAULT 0 COMMENT '运行删除标记',
	create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE KEY (name)
) COMMENT='应用模块信息表';


--策略及其关联表
CREATE TABLE IF NOT EXISTS dc_strategy(
	id BIGINT(20) NOT NULL auto_increment COMMENT '主键ID',
	name VARCHAR(32) NOT NULL COMMENT '策略逻辑名',
	path VARCHAR(200) NOT NULL COMMENT '存放路径',
	help VARCHAR(500) NULL COMMENT '帮助信息',
	desc VARCHAR(255) NULL COMMENT '描述信息',
	allow_delete INT(1) NOT NULL DEFAULT 0 COMMENT '运行删除标记',
	create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE KEY (name)
) COMMENT='策略信息表';
--任务信息表
CREATE TABLE IF NOT EXISTS dc_task(
	id BIGINT(20) NOT NULL auto_increment COMMENT '主键ID',
	strategy_id BIGINT(20) NOT NULL COMMENT '策略ID',
	group_id BIGINT(20) NOT NULL COMMENT '组ID',
	status int(2) NOT NULL COMMENT '状态：0-未部署;1-部署中;2-部署成功；3-部署失败;4-卸载中；5-卸载成功；6-卸载失败；',
	log_path VARCHAR(500) NULL COMMENT '日志文件路径',
	create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE KEY (strategy_id,group_id)
) COMMENT='任务信息表';

--用户信息表
CREATE TABLE IF NOT EXISTS sys_user(
	id BIGINT(20) NOT NULL auto_increment COMMENT '主键ID',
	name VARCHAR(32) NOT NULL COMMENT '用户名',
	password VARCHAR(100) NOT NULL COMMENT '密码',
	create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE KEY (name)
) COMMENT='用户信息表';

-- 系统日志
CREATE TABLE IF NOT EXISTS `sys_log` (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  username varchar(50) COMMENT '用户名',
  operation varchar(50) COMMENT '用户操作',
  method varchar(200) COMMENT '请求方法',
  params varchar(5000) COMMENT '请求参数',
  time bigint NOT NULL COMMENT '执行时长(毫秒)',
  ip varchar(64) COMMENT 'IP地址',
	create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) COMMENT='系统日志表';