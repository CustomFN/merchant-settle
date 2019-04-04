/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50716
Source Host           : localhost:3306
Source Database       : merchant_settled

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2019-04-02 21:36:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for audit
-- ----------------------------
DROP TABLE IF EXISTS `audit`;
CREATE TABLE `audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customerId` int(11) DEFAULT NULL COMMENT '客户ID',
  `poiId` int(11) DEFAULT NULL COMMENT '门店ID',
  `auditType` int(11) NOT NULL COMMENT '审核类型',
  `auditApplicationType` int(11) NOT NULL COMMENT '审核申请类型',
  `auditData` text NOT NULL COMMENT '审核数据',
  `auditStatus` int(11) NOT NULL COMMENT '审核状态',
  `submitterId` varchar(20) NOT NULL COMMENT '提交人ID',
  `transactor` varchar(20) DEFAULT '' COMMENT '处理人',
  `auditResult` varchar(128) DEFAULT NULL COMMENT '审核结果',
  `ctime` bigint(20) NOT NULL COMMENT '审核任务创建时间',
  `utime` bigint(20) NOT NULL COMMENT '审核任务更新时间',
  `completed` int(11) NOT NULL DEFAULT '0' COMMENT '是否审核完成',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customerId`),
  KEY `idx_poi_id` (`poiId`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for audit_log
-- ----------------------------
DROP TABLE IF EXISTS `audit_log`;
CREATE TABLE `audit_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `auditTaskId` int(11) NOT NULL COMMENT '审核任务ID',
  `logMsg` varchar(255) NOT NULL COMMENT '审核日志内容',
  `auditStatus` int(11) NOT NULL COMMENT '审核状态',
  `opUserId` varchar(20) NOT NULL COMMENT '操作人ID',
  `opTime` bigint(20) NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_audit_task_id` (`auditTaskId`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for banks
-- ----------------------------
DROP TABLE IF EXISTS `banks`;
CREATE TABLE `banks` (
  `subBranchName` varchar(255) NOT NULL,
  `subBranchId` varchar(50) DEFAULT NULL,
  `provinceId` int(11) DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `cityId` int(11) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `bankName` varchar(255) NOT NULL,
  `bankId` int(11) DEFAULT NULL,
  KEY `idx_province` (`provinceId`),
  KEY `idx_city` (`city`),
  KEY `idx_bank_id` (`bankId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `categoryId` int(11) NOT NULL COMMENT '品类ID',
  `categoryName` varchar(64) NOT NULL COMMENT '品类名称',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`categoryId`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customerType` int(11) NOT NULL COMMENT '客户类型',
  `customerCertificatesPic` varchar(255) NOT NULL COMMENT '客户证件类型',
  `customerCertificatesNum` varchar(128) NOT NULL COMMENT '客户证件编号',
  `customerName` varchar(64) NOT NULL COMMENT '客户名称',
  `customerLegalPerson` varchar(32) NOT NULL COMMENT '客户法人',
  `customerCertificatesAddress` varchar(128) NOT NULL COMMENT '客户证件地址',
  `status` int(11) NOT NULL COMMENT '客户状态',
  `customerValidTime` bigint(11) NOT NULL COMMENT '客户有效期',
  `customerPrincipal` varchar(128) DEFAULT NULL COMMENT '客户责任人',
  `auditResult` varchar(255) DEFAULT NULL COMMENT '审核结果',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `idx_customer_principal_id` (`customerPrincipal`),
  KEY `idx_customer_name` (`customerName`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for customer_audited
-- ----------------------------
DROP TABLE IF EXISTS `customer_audited`;
CREATE TABLE `customer_audited` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customerType` int(11) NOT NULL COMMENT '客户类型',
  `customerCertificatesPic` varchar(255) NOT NULL COMMENT '客户证件类型',
  `customerCertificatesNum` varchar(128) NOT NULL COMMENT '客户证件编号',
  `customerName` varchar(64) NOT NULL COMMENT '客户名称',
  `customerLegalPerson` varchar(32) NOT NULL COMMENT '客户法人',
  `customerCertificatesAddress` varchar(128) NOT NULL COMMENT '客户证件地址',
  `status` int(11) NOT NULL COMMENT '客户状态',
  `customerValidTime` bigint(11) NOT NULL COMMENT '客户有效期',
  `customerPrincipal` varchar(128) NOT NULL COMMENT '客户责任人',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `idx_customer_principal_id` (`customerPrincipal`),
  KEY `idx_customer_name` (`customerName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for customer_contract
-- ----------------------------
DROP TABLE IF EXISTS `customer_contract`;
CREATE TABLE `customer_contract` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customerContractType` int(11) NOT NULL COMMENT '合同类型',
  `customerContractNum` varchar(64) NOT NULL COMMENT '合同编号',
  `contractEndTime` bigint(20) NOT NULL COMMENT '合同有效期',
  `contractScan` varchar(255) NOT NULL COMMENT '合同扫描件',
  `status` int(11) NOT NULL COMMENT '合同状态',
  `auditResult` varchar(255) DEFAULT NULL COMMENT '审核结果',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  `customerId` int(11) DEFAULT NULL COMMENT '客户ID',
  PRIMARY KEY (`id`),
  KEY `idx_contract_num_id` (`customerContractNum`),
  KEY `idx_customer_id` (`customerId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for customer_contract_audited
-- ----------------------------
DROP TABLE IF EXISTS `customer_contract_audited`;
CREATE TABLE `customer_contract_audited` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customerContractType` int(11) NOT NULL COMMENT '合同类型',
  `customerContractNum` varchar(64) NOT NULL COMMENT '合同编号',
  `contractEndTime` bigint(20) NOT NULL COMMENT '合同有效期',
  `contractScan` varchar(255) NOT NULL COMMENT '合同扫描件',
  `status` int(11) NOT NULL COMMENT '合同状态',
  `auditResult` varchar(255) DEFAULT NULL COMMENT '审核结果',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  `customerId` int(11) NOT NULL COMMENT '客户ID',
  PRIMARY KEY (`id`),
  KEY `idx_contract_num_id` (`customerContractNum`),
  KEY `idx_customer_id` (`customerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for customer_kp
-- ----------------------------
DROP TABLE IF EXISTS `customer_kp`;
CREATE TABLE `customer_kp` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `kpType` int(11) NOT NULL COMMENT 'KP类型',
  `kpSiginType` int(11) NOT NULL COMMENT 'KP签约',
  `kpAuthorizationPic` varchar(255) DEFAULT NULL COMMENT 'KP授权书',
  `kpCertificatesPic` varchar(255) NOT NULL COMMENT 'KP证件图片',
  `kpName` varchar(64) NOT NULL COMMENT 'KP姓名',
  `kpCertificatesType` int(11) NOT NULL COMMENT 'KP证件类型',
  `kpCertificatesNum` varchar(64) NOT NULL COMMENT 'KP证件号码',
  `kpPhoneNum` varchar(20) NOT NULL COMMENT 'KP手机号',
  `bankId` int(11) DEFAULT NULL COMMENT '银行ID',
  `bankNum` varchar(64) DEFAULT NULL COMMENT '银行卡号',
  `status` int(11) NOT NULL COMMENT 'KP状态',
  `auditResult` varchar(255) DEFAULT NULL COMMENT '审核结果',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  `customerId` int(11) DEFAULT NULL COMMENT '客户ID',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customerId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for customer_kp_audited
-- ----------------------------
DROP TABLE IF EXISTS `customer_kp_audited`;
CREATE TABLE `customer_kp_audited` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `kpType` int(11) NOT NULL COMMENT 'KP类型',
  `kpSiginType` int(11) NOT NULL COMMENT 'KP签约',
  `kpAuthorizationPic` varchar(255) DEFAULT NULL COMMENT 'KP授权书',
  `kpCertificatesPic` varchar(255) NOT NULL COMMENT 'KP证件图片',
  `kpName` varchar(64) NOT NULL COMMENT 'KP姓名',
  `kpCertificatesType` int(11) NOT NULL COMMENT 'KP证件类型',
  `kpCertificatesNum` varchar(64) NOT NULL COMMENT 'KP证件号码',
  `kpPhoneNum` varchar(20) NOT NULL COMMENT 'KP手机号',
  `bankId` int(11) DEFAULT NULL COMMENT '银行ID',
  `bankNum` varchar(64) DEFAULT NULL COMMENT '银行卡号',
  `status` int(11) NOT NULL COMMENT 'KP状态',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  `customerId` int(11) NOT NULL COMMENT '客户ID',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for customer_oplog
-- ----------------------------
DROP TABLE IF EXISTS `customer_oplog`;
CREATE TABLE `customer_oplog` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customerId` int(11) NOT NULL COMMENT '客户ID',
  `module` varchar(20) NOT NULL COMMENT '模块',
  `content` text NOT NULL COMMENT '日志内容',
  `opUserId` varchar(20) NOT NULL COMMENT '操作人',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customerId`),
  KEY `idx_module` (`module`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for customer_poi
-- ----------------------------
DROP TABLE IF EXISTS `customer_poi`;
CREATE TABLE `customer_poi` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customerId` int(11) NOT NULL COMMENT '客户ID',
  `wmPoiId` int(11) NOT NULL COMMENT '门店ID',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customerId`),
  KEY `idx_wmpoi_id` (`wmPoiId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for customer_poi_audited
-- ----------------------------
DROP TABLE IF EXISTS `customer_poi_audited`;
CREATE TABLE `customer_poi_audited` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customerId` int(11) NOT NULL COMMENT '客户ID',
  `wmPoiId` int(11) NOT NULL COMMENT '门店ID',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customerId`),
  KEY `idx_wmpoi_id` (`wmPoiId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for customer_settle
-- ----------------------------
DROP TABLE IF EXISTS `customer_settle`;
CREATE TABLE `customer_settle` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `settleAccType` int(11) NOT NULL COMMENT '结算账户类型',
  `settleAccName` varchar(128) NOT NULL COMMENT '结算账户名称',
  `settleAccNo` varchar(128) DEFAULT NULL COMMENT '结算账号',
  `province` int(11) NOT NULL COMMENT '省',
  `city` int(11) NOT NULL COMMENT '市',
  `bankId` int(11) NOT NULL COMMENT '银行ID',
  `branchId` int(11) NOT NULL COMMENT '支行ID',
  `branchName` varchar(32) NOT NULL COMMENT '支行名称',
  `reservePhoneNum` varchar(32) DEFAULT NULL COMMENT '银行预留手机号',
  `financialOfficer` varchar(20) DEFAULT NULL COMMENT '财务负责人',
  `financialOfficerPhone` varchar(20) DEFAULT NULL COMMENT '财务负责人手机号',
  `financialOfficerCertificatesType` int(11) DEFAULT NULL COMMENT '财务负责人证件类型',
  `financialOfficerCertificatesNum` varchar(64) DEFAULT NULL COMMENT '财务负责人证件编号',
  `settleType` int(11) DEFAULT NULL COMMENT '结算方式',
  `settleCycle` int(11) DEFAULT NULL COMMENT '结算周期',
  `settleMinAmount` double(16,2) DEFAULT NULL COMMENT '最低结算金额',
  `status` int(11) NOT NULL COMMENT '结算状态',
  `auditResult` varchar(255) DEFAULT NULL COMMENT '审核结果',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  `customerId` int(11) DEFAULT NULL COMMENT '客户ID',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customerId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for customer_settle_audited
-- ----------------------------
DROP TABLE IF EXISTS `customer_settle_audited`;
CREATE TABLE `customer_settle_audited` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `settleAccType` int(11) NOT NULL COMMENT '结算账户类型',
  `settleAccName` varchar(128) NOT NULL COMMENT '结算账户名称',
  `settleAccNo` varchar(128) DEFAULT NULL COMMENT '结算账号',
  `province` int(11) NOT NULL COMMENT '省',
  `city` int(11) NOT NULL COMMENT '市',
  `bankId` int(11) NOT NULL COMMENT '银行ID',
  `branchId` int(11) NOT NULL COMMENT '支行ID',
  `branchName` varchar(32) NOT NULL COMMENT '支行名称',
  `reservePhoneNum` varchar(32) DEFAULT NULL COMMENT '银行预留手机号',
  `financialOfficer` varchar(20) DEFAULT NULL COMMENT '财务负责人',
  `financialOfficerPhone` varchar(20) DEFAULT NULL COMMENT '财务负责人手机号',
  `financialOfficerCertificatesType` int(11) DEFAULT NULL COMMENT '财务负责人证件类型',
  `financialOfficerCertificatesNum` varchar(64) DEFAULT NULL COMMENT '财务负责人证件编号',
  `settleType` int(11) DEFAULT NULL COMMENT '结算方式',
  `settleCycle` int(11) DEFAULT NULL COMMENT '结算周期',
  `settleMinAmount` double(16,2) DEFAULT NULL COMMENT '最低结算金额',
  `status` int(11) NOT NULL COMMENT '结算状态',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  `customerId` int(11) NOT NULL COMMENT '客户ID',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for customer_signer
-- ----------------------------
DROP TABLE IF EXISTS `customer_signer`;
CREATE TABLE `customer_signer` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `contractId` int(11) NOT NULL COMMENT '合同ID',
  `signerLabel` varchar(10) NOT NULL,
  `party` varchar(20) NOT NULL COMMENT '合作方',
  `partyContactPerson` varchar(20) DEFAULT NULL COMMENT '合作方联系人',
  `partyContactPersonPhone` varchar(20) NOT NULL COMMENT '合作方联系人手机号',
  `signTime` bigint(11) NOT NULL COMMENT '签约时间',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `idx_contract_id` (`contractId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for customer_signer_audited
-- ----------------------------
DROP TABLE IF EXISTS `customer_signer_audited`;
CREATE TABLE `customer_signer_audited` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `contractId` int(11) NOT NULL COMMENT '合同ID',
  `signerLabel` varchar(10) NOT NULL,
  `party` varchar(20) NOT NULL COMMENT '合作方',
  `partyContactPerson` varchar(20) DEFAULT NULL COMMENT '合作方联系人',
  `partyContactPersonPhone` varchar(20) NOT NULL COMMENT '合作方联系人手机号',
  `signTime` bigint(11) NOT NULL COMMENT '签约时间',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `idx_contract_id` (`contractId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for physical_poi
-- ----------------------------
DROP TABLE IF EXISTS `physical_poi`;
CREATE TABLE `physical_poi` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `physicalCityId` int(11) NOT NULL COMMENT '城市ID',
  `physicalRegionId` int(20) NOT NULL COMMENT '区县',
  `physicalPoiName` varchar(20) DEFAULT NULL COMMENT '物理门店名称',
  `physicalPoiPhone` varchar(20) NOT NULL COMMENT '物理门店电话',
  `physicalPoiCategory` bigint(11) NOT NULL COMMENT '物理门店品类',
  `physicalPoiAddress` varchar(20) NOT NULL COMMENT '物理门店地址',
  `physicalPoiLongitude` varchar(20) NOT NULL COMMENT '物理门店经度',
  `physicalPoiLatitude` varchar(20) NOT NULL COMMENT '物理门店维度',
  `physicalPoiPrincipal` varchar(20) NOT NULL COMMENT '物理门店负责人',
  `status` int(20) NOT NULL COMMENT '状态',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  `claimed` int(11) NOT NULL DEFAULT '0' COMMENT '是否被认领',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`physicalPoiName`),
  KEY `idx_principal` (`physicalPoiPrincipal`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for resource
-- ----------------------------
DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `resourceId` varchar(32) NOT NULL COMMENT '资源id',
  `resourcePath` varchar(64) NOT NULL COMMENT '资源路径',
  `description` text NOT NULL COMMENT '资源描述',
  `resourceName` varchar(255) NOT NULL COMMENT '资源名称',
  `parentId` varchar(32) DEFAULT NULL COMMENT '父级资源id',
  `sortOrder` decimal(10,2) NOT NULL COMMENT '资源排序',
  `level` int(11) NOT NULL COMMENT '资源层级',
  `status` int(11) NOT NULL COMMENT '资源状态',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL,
  `valid` int(11) NOT NULL COMMENT '是否有效.0:无效,1:有效',
  `resourceType` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_resource_id` (`resourceId`),
  KEY `idx_resource_name` (`resourcePath`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `roleId` varchar(32) NOT NULL COMMENT '角色id',
  `roleName` varchar(20) NOT NULL COMMENT '角色名',
  `description` varchar(256) DEFAULT NULL COMMENT '角色描述',
  `status` int(11) NOT NULL COMMENT '角色状态',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL,
  `valid` int(2) NOT NULL COMMENT '是否有效.0:无效,1:有效',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`roleId`),
  KEY `idx_role_name` (`roleName`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for role_resource
-- ----------------------------
DROP TABLE IF EXISTS `role_resource`;
CREATE TABLE `role_resource` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `roleId` varchar(32) NOT NULL COMMENT '角色id',
  `resourceId` varchar(32) NOT NULL COMMENT '资源id',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL,
  `valid` int(2) NOT NULL COMMENT '是否有效.0:无效,1:有效',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`roleId`),
  KEY `idx_resource_id` (`resourceId`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for settle_poi
-- ----------------------------
DROP TABLE IF EXISTS `settle_poi`;
CREATE TABLE `settle_poi` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `settleId` int(11) NOT NULL COMMENT '结算ID',
  `wmPoiId` int(11) NOT NULL COMMENT '门店ID',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `idx_settle_id` (`settleId`),
  KEY `idx_wmpoi_id` (`wmPoiId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for settle_poi_audited
-- ----------------------------
DROP TABLE IF EXISTS `settle_poi_audited`;
CREATE TABLE `settle_poi_audited` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `settleId` int(11) NOT NULL COMMENT '结算ID',
  `wmPoiId` int(11) NOT NULL COMMENT '门店ID',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `idx_settle_id` (`settleId`),
  KEY `idx_wmpoi_id` (`wmPoiId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `userId` varchar(20) NOT NULL COMMENT '用户id',
  `userNameSpell` varchar(20) NOT NULL,
  `userName` varchar(20) NOT NULL COMMENT '用户姓名',
  `userPassword` varchar(256) NOT NULL COMMENT '用户密码',
  `status` int(2) NOT NULL COMMENT '用户头像',
  `salt` varchar(20) NOT NULL COMMENT '盐',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(2) NOT NULL COMMENT '是否有效.0:无效,1:有效',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`userId`),
  KEY `idx_user_name_spell` (`userNameSpell`),
  KEY `idx_user_name` (`userName`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `userId` varchar(20) NOT NULL COMMENT '用户id',
  `roleId` varchar(32) NOT NULL COMMENT '角色id',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL,
  `valid` int(2) NOT NULL COMMENT '是否有效.0:无效,1:有效',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`userId`),
  KEY `idx_role_id` (`roleId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for wm_poi_base_info
-- ----------------------------
DROP TABLE IF EXISTS `wm_poi_base_info`;
CREATE TABLE `wm_poi_base_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customerId` int(11) NOT NULL COMMENT '客户ID',
  `wmPoiName` varchar(64) NOT NULL COMMENT '门店名称',
  `wmPoiLinkMan` varchar(20) NOT NULL COMMENT '门店联系人',
  `wmPoiPhone` varchar(20) DEFAULT NULL COMMENT '门店电话',
  `wmPoiCategory` int(11) NOT NULL COMMENT '门店品类',
  `wmPoiCityId` int(11) NOT NULL COMMENT '城市ID',
  `wmPoiRegionId` int(11) NOT NULL COMMENT '区县ID',
  `wmPoiAddress` varchar(128) NOT NULL COMMENT '门店地址',
  `wmPoiLongitude` varchar(32) NOT NULL COMMENT '门店经度',
  `wmPoiLatitude` varchar(32) DEFAULT NULL COMMENT '门店维度',
  `wmPoiLogo` varchar(255) DEFAULT NULL COMMENT '门店LOGO',
  `wmPoiEnvironmentPic` varchar(255) DEFAULT NULL COMMENT '门店环境图片',
  `wmPoiPrincipal` varchar(32) DEFAULT NULL COMMENT '门店责任人',
  `status` int(11) NOT NULL COMMENT '状态',
  `auditResult` varchar(255) DEFAULT NULL COMMENT '审核结果',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  `orderMealDate` varchar(64) DEFAULT NULL COMMENT '订餐日期',
  `orderMealStartTime` varchar(32) NOT NULL COMMENT '订餐开始时间',
  `orderMealEndTime` varchar(32) NOT NULL COMMENT '订餐结束时间',
  `orderMealTel` varchar(20) DEFAULT NULL COMMENT '订餐电话',
  `businessInfoStatus` int(11) DEFAULT NULL COMMENT '营业信息状态',
  `businessInfoAuditResult` varchar(255) DEFAULT NULL COMMENT '营业信息审核结果',
  `coopState` int(11) NOT NULL DEFAULT '1' COMMENT '上单状态',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customerId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for wm_poi_base_info_audited
-- ----------------------------
DROP TABLE IF EXISTS `wm_poi_base_info_audited`;
CREATE TABLE `wm_poi_base_info_audited` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customerId` int(11) NOT NULL COMMENT '客户ID',
  `wmPoiName` varchar(64) NOT NULL COMMENT '门店名称',
  `wmPoiLinkMan` varchar(20) NOT NULL COMMENT '门店联系人',
  `wmPoiPhone` varchar(20) DEFAULT NULL COMMENT '门店电话',
  `wmPoiCategory` int(11) NOT NULL COMMENT '门店品类',
  `wmPoiCityId` int(11) NOT NULL COMMENT '城市ID',
  `wmPoiRegionId` int(11) NOT NULL COMMENT '区县ID',
  `wmPoiAddress` varchar(128) NOT NULL COMMENT '门店地址',
  `wmPoiLongitude` varchar(32) NOT NULL COMMENT '门店经度',
  `wmPoiLatitude` varchar(32) DEFAULT NULL COMMENT '门店维度',
  `wmPoiLogo` varchar(255) DEFAULT NULL COMMENT '门店LOGO',
  `wmPoiEnvironmentPic` varchar(255) DEFAULT NULL COMMENT '门店环境图片',
  `wmPoiPrincipal` varchar(32) DEFAULT NULL COMMENT '门店责任人',
  `status` int(11) NOT NULL COMMENT '状态',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  `orderMealDate` varchar(64) DEFAULT NULL COMMENT '订餐日期',
  `orderMealStartTime` varchar(32) NOT NULL COMMENT '订餐开始时间',
  `orderMealEndTime` varchar(32) NOT NULL COMMENT '订餐结束时间',
  `orderMealTime` varchar(64) DEFAULT NULL COMMENT '订餐时间',
  `orderMealTel` varchar(20) DEFAULT NULL COMMENT '订餐电话',
  `businessInfoStatus` int(11) DEFAULT NULL COMMENT '营业信息状态',
  `coopState` int(11) NOT NULL DEFAULT '1' COMMENT '上单状态',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customerId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for wm_poi_delivery
-- ----------------------------
DROP TABLE IF EXISTS `wm_poi_delivery`;
CREATE TABLE `wm_poi_delivery` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `wmPoiId` int(11) NOT NULL COMMENT '客户ID',
  `wmDeliveryType` varchar(255) NOT NULL COMMENT '配送方式',
  `poportion` int(11) NOT NULL COMMENT '分成比例',
  `minMoney` varchar(20) NOT NULL COMMENT '保底收入',
  `wmPoiProjects` text NOT NULL COMMENT '配送方案',
  `status` int(11) NOT NULL COMMENT '状态',
  `auditResult` varchar(255) DEFAULT NULL COMMENT '审核结果',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `idx_wmpoi_id` (`wmPoiId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for wm_poi_delivery_audited
-- ----------------------------
DROP TABLE IF EXISTS `wm_poi_delivery_audited`;
CREATE TABLE `wm_poi_delivery_audited` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `wmPoiId` int(11) NOT NULL COMMENT '客户ID',
  `wmDeliveryType` varchar(255) NOT NULL COMMENT '配送方式',
  `poportion` int(11) NOT NULL COMMENT '分成比例',
  `minMoney` varchar(20) NOT NULL COMMENT '保底收入',
  `wmPoiProjects` text NOT NULL COMMENT '配送方案',
  `status` int(11) NOT NULL COMMENT '状态',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `idx_wmpoi_id` (`wmPoiId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for wm_poi_oplog
-- ----------------------------
DROP TABLE IF EXISTS `wm_poi_oplog`;
CREATE TABLE `wm_poi_oplog` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `wmPoiId` int(11) NOT NULL COMMENT '门店ID',
  `module` varchar(20) NOT NULL COMMENT '模块',
  `content` text NOT NULL COMMENT '日志内容',
  `opUser` varchar(20) NOT NULL COMMENT '操作人',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_wmpoi_id` (`wmPoiId`),
  KEY `idx_module` (`module`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for wm_poi_qua
-- ----------------------------
DROP TABLE IF EXISTS `wm_poi_qua`;
CREATE TABLE `wm_poi_qua` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `wmPoiId` int(11) NOT NULL COMMENT '客户ID',
  `wmPoiLinkManIDCardPic` varchar(255) NOT NULL COMMENT '门店联系人证件图片',
  `wmPoiLinkManIDCardType` int(11) NOT NULL COMMENT '门店联系人证件类型',
  `wmPoiLinkManName` varchar(20) NOT NULL COMMENT '门店联系人姓名',
  `wmPoiLinkManIDCardNo` varchar(64) NOT NULL COMMENT '门店联系人证件编号',
  `wmPoiBusinessLicencePic` varchar(255) NOT NULL COMMENT '门店营业执照图片',
  `wmPoiBusinessLicenceNo` varchar(64) NOT NULL COMMENT '门店营业执照编号',
  `wmPoiOperatorName` varchar(20) NOT NULL COMMENT '门店营业者姓名',
  `wmPoiBusinessLicenceName` varchar(64) NOT NULL COMMENT '门店营业执照名称',
  `wmPoiBusinessLicenceAddress` varchar(128) NOT NULL COMMENT '门店营业执照地址',
  `wmPoiRegistrationDate` varchar(64) NOT NULL COMMENT '门店营业执照注册日期',
  `wmPoiRegisterDepartment` varchar(64) NOT NULL COMMENT '门店营业执照登记机关',
  `wmPoiBusinessLicenceValidTime` bigint(20) NOT NULL COMMENT '门店营业执照有效期',
  `status` int(11) NOT NULL COMMENT '状态',
  `auditResult` varchar(255) DEFAULT NULL COMMENT '审核结果',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `idx_wmpoi_id` (`wmPoiId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for wm_poi_qua_audited
-- ----------------------------
DROP TABLE IF EXISTS `wm_poi_qua_audited`;
CREATE TABLE `wm_poi_qua_audited` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `wmPoiId` int(11) NOT NULL COMMENT '客户ID',
  `wmPoiLinkManIDCardPic` varchar(255) NOT NULL COMMENT '门店联系人证件图片',
  `wmPoiLinkManIDCardType` int(11) NOT NULL COMMENT '门店联系人证件类型',
  `wmPoiLinkManName` varchar(20) NOT NULL COMMENT '门店联系人姓名',
  `wmPoiLinkManIDCardNo` varchar(64) NOT NULL COMMENT '门店联系人证件编号',
  `wmPoiBusinessLicencePic` varchar(255) NOT NULL COMMENT '门店营业执照图片',
  `wmPoiBusinessLicenceNo` varchar(64) NOT NULL COMMENT '门店营业执照编号',
  `wmPoiOperatorName` varchar(20) NOT NULL COMMENT '门店营业者姓名',
  `wmPoiBusinessLicenceName` varchar(64) NOT NULL COMMENT '门店营业执照名称',
  `wmPoiBusinessLicenceAddress` varchar(128) NOT NULL COMMENT '门店营业执照地址',
  `wmPoiRegistrationDate` varchar(64) NOT NULL COMMENT '门店营业执照注册日期',
  `wmPoiRegisterDepartment` varchar(64) NOT NULL COMMENT '门店营业执照登记机关',
  `wmPoiBusinessLicenceValidTime` bigint(20) NOT NULL COMMENT '门店营业执照有效期',
  `status` int(11) NOT NULL COMMENT '状态',
  `ctime` bigint(20) NOT NULL COMMENT '创建时间',
  `utime` bigint(20) NOT NULL COMMENT '更新时间',
  `valid` int(11) NOT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `idx_wmpoi_id` (`wmPoiId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
