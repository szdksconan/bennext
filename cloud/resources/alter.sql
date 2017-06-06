ALTER TABLE `card_bin` ADD COLUMN `updateTime`  datetime NULL DEFAULT now() COMMENT '修改时间',ADD COLUMN `createTime`  datetime NULL DEFAULT now() COMMENT '创建时间';

ALTER TABLE `card_bin` ADD COLUMN `id`  int NOT NULL AUTO_INCREMENT,ADD PRIMARY KEY (`id`);

ALTER TABLE `park`

ADD COLUMN `parkId`  varchar(30) NULL AFTER `useTag`,

ADD COLUMN `clientName`  varchar(100) NULL AFTER `parkId`;


CREATE TABLE `rights_count` (
  `rightsId` varchar(10) DEFAULT NULL COMMENT '权益ID',
  `rank` varchar(10) DEFAULT NULL COMMENT '卡级别',
  `count` int(10) DEFAULT NULL COMMENT '上限次数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权益类型-卡级别信息表';

CREATE TABLE `service_info` (
`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
`name`  varchar(255) NULL COMMENT '名称' ,
`ip`  varchar(255) NULL COMMENT 'ip' ,
`port`  varchar(255) NULL DEFAULT '' COMMENT '端口',
`updateTime` datetime DEFAULT NULL,
PRIMARY KEY (`id`)
)
COMMENT='终端信息表'
;
CREATE TABLE `service_log` (
`ip`  varchar(255) NULL COMMENT 'ip' ,
`info`  varchar(255) NULL COMMENT '信息' ,
`createTime`  datetime NULL COMMENT '时间',
)
COMMENT='终端log'
;

