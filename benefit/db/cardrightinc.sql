/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50621
Source Host           : localhost:3306
Source Database       : benifit

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2017-03-13 21:24:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cardrightinc
-- ----------------------------
DROP TABLE IF EXISTS `cardrightinc`;
CREATE TABLE `cardrightinc` (
  `RightID` varchar(255) DEFAULT NULL,
  `CardNum` varchar(255) DEFAULT NULL,
  `UpdateTime` datetime DEFAULT NULL,
  `Count` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cardrightinc
-- ----------------------------
INSERT INTO `cardrightinc` VALUES ('35', '370246560052673', '2017-03-13 19:20:00', '1');
