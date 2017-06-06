

ALTER TABLE `client_rights`
MODIFY COLUMN `id`  int(11) NOT NULL AUTO_INCREMENT COMMENT 'id' FIRST ;



ALTER TABLE `client_rights`
ADD COLUMN `price`  double(10,0) NULL COMMENT '扣费金额' AFTER `deductionHours`;

 /*面板信息添加ip字段 */
ALTER TABLE `pay_panel`
ADD COLUMN `ip`  varchar(100) NULL AFTER `payPanelCode`;

ALTER TABLE `park`
ADD COLUMN `charge`  double NULL COMMENT '收费标准' AFTER `creator`;

ALTER TABLE `rights`
MODIFY COLUMN `cardNo`  varchar(255) NULL DEFAULT NULL COMMENT '卡号' AFTER `id`;

ALTER TABLE `client_rights`
MODIFY COLUMN `price`  double(10,2) NULL DEFAULT NULL COMMENT '扣费金额' AFTER
`deductionHours`;

ALTER TABLE `exit_way`
MODIFY COLUMN `id`  varchar(255) NOT NULL COMMENT 'id' FIRST ;