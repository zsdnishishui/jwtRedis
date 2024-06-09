/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.56.110
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 192.168.56.110:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 09/06/2024 11:51:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_auth
-- ----------------------------
DROP TABLE IF EXISTS `user_auth`;
CREATE TABLE `user_auth`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `auth` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_auth
-- ----------------------------
INSERT INTO `user_auth` VALUES (1, 'dfdf', '001');
INSERT INTO `user_auth` VALUES (2, 'rtrt', '001');
INSERT INTO `user_auth` VALUES (3, '121', '001');

SET FOREIGN_KEY_CHECKS = 1;
