SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `clan_search`
-- ----------------------------
DROP TABLE IF EXISTS `clan_search`;
CREATE TABLE `clan_search` (
  `visible` tinyint(1) NOT NULL,
  `clanId` int(11) NOT NULL,
  `message` varchar(999) DEFAULT NULL,
  `timeleft` bigint(13) NOT NULL,
  `adenas` int(9) NOT NULL,
  PRIMARY KEY (`clanId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;