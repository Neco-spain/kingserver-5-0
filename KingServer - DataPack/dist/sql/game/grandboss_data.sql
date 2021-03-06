DROP TABLE IF EXISTS `grandboss_data`;
CREATE TABLE `grandboss_data` (
  `boss_id` smallint(5) unsigned NOT NULL,
  `loc_x` mediumint(6) NOT NULL,
  `loc_y` mediumint(6) NOT NULL,
  `loc_z` mediumint(6) NOT NULL,
  `heading` mediumint(6) NOT NULL DEFAULT '0',
  `respawn_time` bigint(13) unsigned NOT NULL DEFAULT '0',
  `currentHP` decimal(30,15) NOT NULL,
  `currentMP` decimal(30,15) NOT NULL,
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`boss_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of grandboss_data
-- ----------------------------
INSERT INTO grandboss_data VALUES ('29001', '-21610', '181594', '-5734', '0', '0', '229898.480000000000000', '667.776000000000000', '0');
INSERT INTO grandboss_data VALUES ('29006', '17726', '108915', '-6480', '0', '0', '622493.583880000000000', '3793.536000000000000', '0');
INSERT INTO grandboss_data VALUES ('29014', '55024', '17368', '-5412', '10126', '0', '622493.583880000000000', '3793.536000000000000', '0');
INSERT INTO grandboss_data VALUES ('29019', '185708', '114298', '-8221', '32768', '0', '17850000.000000000000000', '39960.000000000000000', '0');
INSERT INTO grandboss_data VALUES ('29020', '116033', '17447', '10107', '-25348', '0', '4068372.000000000000000', '39960.000000000000000', '0');
INSERT INTO grandboss_data VALUES ('29022', '55312', '219168', '-3223', '0', '0', '858518.360000000000000', '399600.000000000000000', '0');
INSERT INTO grandboss_data VALUES ('29028', '-105200', '-253104', '-15264', '0', '0', '62041918.000000000000000', '2248572.000000000000000', '0');
INSERT INTO grandboss_data VALUES ('29062', '0', '0', '0', '0', '0', '0.000000000000000', '0.000000000000000', '0');
INSERT INTO grandboss_data VALUES ('29065', '0', '0', '0', '0', '0', '1216600.000000000000000', '11100.000000000000000', '0');
INSERT INTO grandboss_data VALUES ('29066', '185708', '114298', '-8221', '32768', '0', '14518000.000000000000000', '3996000.000000000000000', '0');
INSERT INTO grandboss_data VALUES ('29067', '185708', '114298', '-8221', '32768', '0', '16184000.000000000000000', '3996000.000000000000000', '0');
INSERT INTO grandboss_data VALUES ('29068', '185708', '114298', '-8221', '32768', '0', '62802301.000000000000000', '1998000.000000000000000', '0');
INSERT INTO grandboss_data VALUES ('29118', '0', '0', '0', '0', '0', '4109288.000000000000000', '1220547.000000000000000', '0');
