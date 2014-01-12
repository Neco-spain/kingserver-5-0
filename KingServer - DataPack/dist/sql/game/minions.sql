DROP TABLE IF EXISTS `minions`;
CREATE TABLE `minions` (
  `boss_id` smallint(5) unsigned NOT NULL DEFAULT '0',
  `minion_id` smallint(5) unsigned NOT NULL DEFAULT '0',
  `amount_min` tinyint(2) unsigned NOT NULL DEFAULT '0',
  `amount_max` tinyint(2) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`boss_id`,`minion_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of minions
-- ----------------------------
INSERT INTO minions VALUES ('18484', '22422', '4', '4');
INSERT INTO minions VALUES ('18491', '18493', '1', '1');
INSERT INTO minions VALUES ('18908', '22779', '2', '2');
INSERT INTO minions VALUES ('20117', '20118', '1', '3');
INSERT INTO minions VALUES ('20376', '20377', '1', '2');
INSERT INTO minions VALUES ('20398', '20399', '1', '2');
INSERT INTO minions VALUES ('20520', '20445', '3', '5');
INSERT INTO minions VALUES ('20522', '20524', '2', '4');
INSERT INTO minions VALUES ('20738', '20739', '3', '5');
INSERT INTO minions VALUES ('20745', '20746', '1', '2');
INSERT INTO minions VALUES ('20747', '20748', '1', '2');
INSERT INTO minions VALUES ('20749', '20750', '1', '2');
INSERT INTO minions VALUES ('20751', '20752', '3', '3');
INSERT INTO minions VALUES ('20753', '21040', '4', '4');
INSERT INTO minions VALUES ('20758', '20759', '1', '1');
INSERT INTO minions VALUES ('20758', '20760', '1', '1');
INSERT INTO minions VALUES ('20763', '20764', '1', '1');
INSERT INTO minions VALUES ('20763', '20765', '1', '1');
INSERT INTO minions VALUES ('20763', '20766', '1', '1');
INSERT INTO minions VALUES ('20771', '20772', '1', '3');
INSERT INTO minions VALUES ('20773', '20774', '2', '4');
INSERT INTO minions VALUES ('20779', '20750', '1', '3');
INSERT INTO minions VALUES ('20936', '20937', '1', '1');
INSERT INTO minions VALUES ('20936', '20938', '1', '1');
INSERT INTO minions VALUES ('20936', '20939', '1', '1');
INSERT INTO minions VALUES ('20941', '20940', '3', '3');
INSERT INTO minions VALUES ('20944', '20942', '1', '1');
INSERT INTO minions VALUES ('20944', '20943', '2', '2');
INSERT INTO minions VALUES ('20947', '20945', '1', '2');
INSERT INTO minions VALUES ('20947', '20946', '1', '2');
INSERT INTO minions VALUES ('20950', '20948', '1', '2');
INSERT INTO minions VALUES ('20950', '20949', '1', '2');
INSERT INTO minions VALUES ('20953', '20951', '1', '2');
INSERT INTO minions VALUES ('20953', '20952', '1', '2');
INSERT INTO minions VALUES ('20956', '20954', '1', '2');
INSERT INTO minions VALUES ('20956', '20955', '1', '2');
INSERT INTO minions VALUES ('20959', '20957', '1', '2');
INSERT INTO minions VALUES ('20959', '20958', '1', '2');
INSERT INTO minions VALUES ('20963', '20960', '1', '1');
INSERT INTO minions VALUES ('20963', '20961', '1', '1');
INSERT INTO minions VALUES ('20963', '20962', '1', '1');
INSERT INTO minions VALUES ('20974', '20975', '1', '2');
INSERT INTO minions VALUES ('20974', '20976', '1', '2');
INSERT INTO minions VALUES ('20977', '20978', '1', '1');
INSERT INTO minions VALUES ('20977', '20979', '1', '1');
INSERT INTO minions VALUES ('20980', '20981', '1', '1');
INSERT INTO minions VALUES ('20980', '20982', '1', '1');
INSERT INTO minions VALUES ('20986', '20987', '1', '2');
INSERT INTO minions VALUES ('20986', '20988', '1', '2');
INSERT INTO minions VALUES ('20989', '20990', '1', '1');
INSERT INTO minions VALUES ('20991', '20992', '1', '2');
INSERT INTO minions VALUES ('20991', '20993', '1', '2');
INSERT INTO minions VALUES ('20994', '20995', '3', '4');
INSERT INTO minions VALUES ('21058', '21059', '1', '2');
INSERT INTO minions VALUES ('21058', '21060', '1', '2');
INSERT INTO minions VALUES ('21075', '21076', '1', '1');
INSERT INTO minions VALUES ('21075', '21077', '1', '2');
INSERT INTO minions VALUES ('21078', '21079', '1', '1');
INSERT INTO minions VALUES ('21078', '21080', '1', '2');
INSERT INTO minions VALUES ('21081', '21082', '1', '1');
INSERT INTO minions VALUES ('21081', '21083', '1', '3');
INSERT INTO minions VALUES ('21312', '21313', '2', '2');
INSERT INTO minions VALUES ('21343', '21344', '2', '2');
INSERT INTO minions VALUES ('21345', '21346', '2', '2');
INSERT INTO minions VALUES ('21347', '21348', '1', '1');
INSERT INTO minions VALUES ('21347', '21349', '1', '1');
INSERT INTO minions VALUES ('21369', '21370', '2', '2');
INSERT INTO minions VALUES ('21371', '21372', '2', '2');
INSERT INTO minions VALUES ('21373', '21374', '1', '1');
INSERT INTO minions VALUES ('21373', '21375', '1', '1');
INSERT INTO minions VALUES ('21596', '21597', '1', '1');
INSERT INTO minions VALUES ('21596', '21598', '1', '1');
INSERT INTO minions VALUES ('21599', '21600', '1', '1');
INSERT INTO minions VALUES ('21599', '21601', '1', '1');
INSERT INTO minions VALUES ('22028', '22027', '3', '4');
INSERT INTO minions VALUES ('22080', '22079', '3', '3');
INSERT INTO minions VALUES ('22084', '22083', '3', '3');
INSERT INTO minions VALUES ('22092', '22091', '3', '3');
INSERT INTO minions VALUES ('22096', '22095', '3', '3');
INSERT INTO minions VALUES ('22100', '22099', '8', '8');
INSERT INTO minions VALUES ('22102', '22101', '8', '8');
INSERT INTO minions VALUES ('22104', '22103', '8', '8');
INSERT INTO minions VALUES ('22123', '22122', '2', '3');
INSERT INTO minions VALUES ('22135', '22130', '1', '1');
INSERT INTO minions VALUES ('22135', '22131', '1', '1');
INSERT INTO minions VALUES ('22320', '22322', '1', '1');
INSERT INTO minions VALUES ('22320', '22323', '1', '1');
INSERT INTO minions VALUES ('22321', '22322', '1', '1');
INSERT INTO minions VALUES ('22321', '22323', '1', '1');
INSERT INTO minions VALUES ('22346', '22347', '2', '2');
INSERT INTO minions VALUES ('22363', '22364', '4', '4');
INSERT INTO minions VALUES ('22370', '22371', '4', '4');
INSERT INTO minions VALUES ('22377', '22378', '2', '2');
INSERT INTO minions VALUES ('22377', '22379', '2', '2');
INSERT INTO minions VALUES ('22390', '22391', '2', '2');
INSERT INTO minions VALUES ('22448', '22451', '5', '5');
INSERT INTO minions VALUES ('22449', '22450', '10', '10');
INSERT INTO minions VALUES ('25542', '32305', '4', '4');
INSERT INTO minions VALUES ('25544', '25545', '3', '3');
INSERT INTO minions VALUES ('27021', '20492', '6', '8');
INSERT INTO minions VALUES ('27022', '20367', '1', '3');
INSERT INTO minions VALUES ('27036', '27037', '2', '3');
INSERT INTO minions VALUES ('27110', '27111', '3', '5');
INSERT INTO minions VALUES ('27113', '27111', '3', '6');
INSERT INTO minions VALUES ('25001', '25002', '3', '3');
INSERT INTO minions VALUES ('25001', '25003', '1', '1');
INSERT INTO minions VALUES ('25004', '25005', '3', '3');
INSERT INTO minions VALUES ('25004', '25006', '2', '2');
INSERT INTO minions VALUES ('25007', '25008', '2', '2');
INSERT INTO minions VALUES ('25007', '25009', '2', '2');
INSERT INTO minions VALUES ('25010', '25011', '3', '3');
INSERT INTO minions VALUES ('25010', '25012', '2', '2');
INSERT INTO minions VALUES ('25013', '25014', '1', '1');
INSERT INTO minions VALUES ('25013', '25015', '4', '4');
INSERT INTO minions VALUES ('25016', '25017', '2', '2');
INSERT INTO minions VALUES ('25016', '25018', '2', '2');
INSERT INTO minions VALUES ('25020', '25021', '2', '2');
INSERT INTO minions VALUES ('25020', '25022', '2', '2');
INSERT INTO minions VALUES ('25023', '25024', '1', '1');
INSERT INTO minions VALUES ('25023', '25025', '4', '4');
INSERT INTO minions VALUES ('25026', '25027', '3', '3');
INSERT INTO minions VALUES ('25026', '25028', '2', '2');
INSERT INTO minions VALUES ('25029', '25030', '2', '2');
INSERT INTO minions VALUES ('25029', '25031', '2', '2');
INSERT INTO minions VALUES ('25032', '25033', '3', '3');
INSERT INTO minions VALUES ('25032', '25034', '1', '1');
INSERT INTO minions VALUES ('25035', '25036', '3', '3');
INSERT INTO minions VALUES ('25035', '25037', '2', '2');
INSERT INTO minions VALUES ('25038', '25039', '1', '1');
INSERT INTO minions VALUES ('25038', '25040', '4', '4');
INSERT INTO minions VALUES ('25041', '25042', '3', '3');
INSERT INTO minions VALUES ('25041', '25043', '2', '2');
INSERT INTO minions VALUES ('25044', '25045', '2', '2');
INSERT INTO minions VALUES ('25044', '25046', '2', '2');
INSERT INTO minions VALUES ('25047', '25048', '1', '1');
INSERT INTO minions VALUES ('25047', '25049', '4', '4');
INSERT INTO minions VALUES ('25051', '25052', '2', '2');
INSERT INTO minions VALUES ('25051', '25053', '2', '2');
INSERT INTO minions VALUES ('25054', '25055', '1', '1');
INSERT INTO minions VALUES ('25054', '25056', '4', '4');
INSERT INTO minions VALUES ('25057', '25058', '3', '3');
INSERT INTO minions VALUES ('25057', '25059', '2', '2');
INSERT INTO minions VALUES ('25060', '25061', '3', '3');
INSERT INTO minions VALUES ('25060', '25062', '1', '1');
INSERT INTO minions VALUES ('25064', '25065', '1', '1');
INSERT INTO minions VALUES ('25064', '25066', '4', '4');
INSERT INTO minions VALUES ('25067', '25068', '3', '3');
INSERT INTO minions VALUES ('25067', '25069', '2', '2');
INSERT INTO minions VALUES ('25070', '25071', '3', '3');
INSERT INTO minions VALUES ('25070', '25072', '1', '1');
INSERT INTO minions VALUES ('25076', '25077', '1', '1');
INSERT INTO minions VALUES ('25076', '25078', '4', '4');
INSERT INTO minions VALUES ('25079', '25080', '2', '2');
INSERT INTO minions VALUES ('25079', '25081', '2', '2');
INSERT INTO minions VALUES ('25082', '25083', '3', '3');
INSERT INTO minions VALUES ('25082', '25084', '1', '1');
INSERT INTO minions VALUES ('25085', '25086', '3', '3');
INSERT INTO minions VALUES ('25085', '25087', '2', '2');
INSERT INTO minions VALUES ('25089', '25091', '3', '3');
INSERT INTO minions VALUES ('25089', '25090', '1', '1');
INSERT INTO minions VALUES ('25092', '25093', '1', '1');
INSERT INTO minions VALUES ('25092', '25094', '4', '4');
INSERT INTO minions VALUES ('25095', '25096', '3', '3');
INSERT INTO minions VALUES ('25095', '25097', '1', '1');
INSERT INTO minions VALUES ('25099', '25100', '2', '2');
INSERT INTO minions VALUES ('25099', '25101', '2', '2');
INSERT INTO minions VALUES ('25103', '25104', '1', '1');
INSERT INTO minions VALUES ('25103', '25105', '4', '4');
INSERT INTO minions VALUES ('25106', '25107', '3', '3');
INSERT INTO minions VALUES ('25106', '25108', '2', '2');
INSERT INTO minions VALUES ('25112', '25113', '3', '3');
INSERT INTO minions VALUES ('25112', '25114', '1', '1');
INSERT INTO minions VALUES ('25115', '25116', '3', '3');
INSERT INTO minions VALUES ('25115', '25117', '2', '2');
INSERT INTO minions VALUES ('25119', '25120', '3', '3');
INSERT INTO minions VALUES ('25119', '25121', '1', '1');
INSERT INTO minions VALUES ('25122', '25123', '1', '1');
INSERT INTO minions VALUES ('25122', '25124', '4', '4');
INSERT INTO minions VALUES ('25128', '25129', '3', '3');
INSERT INTO minions VALUES ('25128', '25130', '1', '1');
INSERT INTO minions VALUES ('25131', '25132', '3', '3');
INSERT INTO minions VALUES ('25131', '25133', '2', '2');
INSERT INTO minions VALUES ('25134', '25135', '2', '2');
INSERT INTO minions VALUES ('25134', '25136', '2', '2');
INSERT INTO minions VALUES ('25137', '25138', '3', '3');
INSERT INTO minions VALUES ('25137', '25139', '1', '1');
INSERT INTO minions VALUES ('25140', '25141', '1', '1');
INSERT INTO minions VALUES ('25140', '25142', '4', '4');
INSERT INTO minions VALUES ('25143', '25144', '2', '2');
INSERT INTO minions VALUES ('25143', '25145', '2', '2');
INSERT INTO minions VALUES ('25146', '25147', '3', '3');
INSERT INTO minions VALUES ('25146', '25148', '2', '2');
INSERT INTO minions VALUES ('25149', '25150', '3', '3');
INSERT INTO minions VALUES ('25149', '25151', '2', '2');
INSERT INTO minions VALUES ('25152', '25153', '1', '1');
INSERT INTO minions VALUES ('25152', '25154', '4', '4');
INSERT INTO minions VALUES ('25155', '25156', '3', '3');
INSERT INTO minions VALUES ('25155', '25157', '1', '1');
INSERT INTO minions VALUES ('25159', '25160', '2', '2');
INSERT INTO minions VALUES ('25159', '25161', '2', '2');
INSERT INTO minions VALUES ('25166', '25167', '2', '2');
INSERT INTO minions VALUES ('25166', '25168', '2', '2');
INSERT INTO minions VALUES ('25170', '25171', '3', '3');
INSERT INTO minions VALUES ('25170', '25172', '2', '2');
INSERT INTO minions VALUES ('25173', '25174', '2', '2');
INSERT INTO minions VALUES ('25173', '25175', '2', '2');
INSERT INTO minions VALUES ('25179', '25180', '3', '3');
INSERT INTO minions VALUES ('25179', '25181', '1', '1');
INSERT INTO minions VALUES ('25182', '25183', '2', '2');
INSERT INTO minions VALUES ('25182', '25184', '2', '2');
INSERT INTO minions VALUES ('25185', '25186', '3', '3');
INSERT INTO minions VALUES ('25185', '25187', '2', '2');
INSERT INTO minions VALUES ('25189', '25190', '3', '3');
INSERT INTO minions VALUES ('25189', '25191', '1', '1');
INSERT INTO minions VALUES ('25192', '25193', '1', '1');
INSERT INTO minions VALUES ('25192', '25194', '4', '4');
INSERT INTO minions VALUES ('25199', '25200', '3', '3');
INSERT INTO minions VALUES ('25199', '25201', '1', '1');
INSERT INTO minions VALUES ('25202', '25203', '2', '2');
INSERT INTO minions VALUES ('25202', '25204', '2', '2');
INSERT INTO minions VALUES ('25205', '25206', '1', '1');
INSERT INTO minions VALUES ('25205', '25207', '4', '4');
INSERT INTO minions VALUES ('25208', '25209', '3', '3');
INSERT INTO minions VALUES ('25208', '25210', '1', '1');
INSERT INTO minions VALUES ('25211', '25212', '3', '3');
INSERT INTO minions VALUES ('25211', '25213', '2', '2');
INSERT INTO minions VALUES ('25214', '25215', '3', '3');
INSERT INTO minions VALUES ('25214', '25216', '1', '1');
INSERT INTO minions VALUES ('25220', '25221', '3', '3');
INSERT INTO minions VALUES ('25220', '25222', '1', '1');
INSERT INTO minions VALUES ('25223', '25224', '3', '3');
INSERT INTO minions VALUES ('25223', '25225', '1', '1');
INSERT INTO minions VALUES ('25226', '25227', '2', '2');
INSERT INTO minions VALUES ('25226', '25228', '2', '2');
INSERT INTO minions VALUES ('25230', '25231', '1', '1');
INSERT INTO minions VALUES ('25230', '25232', '4', '4');
INSERT INTO minions VALUES ('25235', '25236', '2', '2');
INSERT INTO minions VALUES ('25235', '25237', '2', '2');
INSERT INTO minions VALUES ('25238', '25239', '2', '2');
INSERT INTO minions VALUES ('25238', '25240', '2', '2');
INSERT INTO minions VALUES ('25241', '25242', '3', '3');
INSERT INTO minions VALUES ('25241', '25243', '2', '2');
INSERT INTO minions VALUES ('25245', '25246', '1', '1');
INSERT INTO minions VALUES ('25245', '25247', '4', '4');
INSERT INTO minions VALUES ('25249', '25250', '3', '3');
INSERT INTO minions VALUES ('25249', '25251', '1', '1');
INSERT INTO minions VALUES ('25252', '25253', '3', '3');
INSERT INTO minions VALUES ('25252', '25254', '1', '1');
INSERT INTO minions VALUES ('25256', '25257', '3', '3');
INSERT INTO minions VALUES ('25256', '25258', '2', '2');
INSERT INTO minions VALUES ('25260', '25261', '3', '3');
INSERT INTO minions VALUES ('25260', '25262', '2', '2');
INSERT INTO minions VALUES ('25263', '25264', '3', '3');
INSERT INTO minions VALUES ('25263', '25265', '1', '1');
INSERT INTO minions VALUES ('25266', '25267', '4', '4');
INSERT INTO minions VALUES ('25266', '25268', '1', '1');
INSERT INTO minions VALUES ('25269', '25270', '3', '3');
INSERT INTO minions VALUES ('25269', '25271', '2', '2');
INSERT INTO minions VALUES ('25273', '25274', '3', '3');
INSERT INTO minions VALUES ('25273', '25275', '1', '1');
INSERT INTO minions VALUES ('25277', '25278', '2', '2');
INSERT INTO minions VALUES ('25277', '25279', '2', '2');
INSERT INTO minions VALUES ('25283', '25284', '4', '4');
INSERT INTO minions VALUES ('25283', '25285', '2', '2');
INSERT INTO minions VALUES ('25286', '25287', '3', '3');
INSERT INTO minions VALUES ('25286', '25288', '2', '2');
INSERT INTO minions VALUES ('25286', '25289', '2', '2');
INSERT INTO minions VALUES ('25290', '25291', '3', '3');
INSERT INTO minions VALUES ('25290', '25292', '1', '1');
INSERT INTO minions VALUES ('25293', '25294', '1', '1');
INSERT INTO minions VALUES ('25293', '25295', '4', '4');
INSERT INTO minions VALUES ('25296', '25297', '3', '3');
INSERT INTO minions VALUES ('25296', '25298', '1', '1');
INSERT INTO minions VALUES ('25299', '25300', '3', '3');
INSERT INTO minions VALUES ('25299', '25301', '2', '2');
INSERT INTO minions VALUES ('25302', '25303', '3', '3');
INSERT INTO minions VALUES ('25302', '25304', '1', '1');
INSERT INTO minions VALUES ('25306', '25307', '1', '1');
INSERT INTO minions VALUES ('25306', '25308', '4', '4');
INSERT INTO minions VALUES ('25309', '25310', '1', '1');
INSERT INTO minions VALUES ('25309', '25311', '4', '4');
INSERT INTO minions VALUES ('25312', '25313', '2', '2');
INSERT INTO minions VALUES ('25312', '25314', '2', '2');
INSERT INTO minions VALUES ('25316', '25317', '1', '1');
INSERT INTO minions VALUES ('25316', '25318', '4', '4');
INSERT INTO minions VALUES ('25319', '25320', '3', '3');
INSERT INTO minions VALUES ('25319', '25321', '1', '1');
INSERT INTO minions VALUES ('25322', '25323', '3', '3');
INSERT INTO minions VALUES ('25322', '25324', '1', '1');
INSERT INTO minions VALUES ('25325', '25326', '1', '1');
INSERT INTO minions VALUES ('25325', '25327', '4', '4');
INSERT INTO minions VALUES ('25328', '25329', '1', '1');
INSERT INTO minions VALUES ('25328', '25330', '1', '1');
INSERT INTO minions VALUES ('25328', '25331', '1', '1');
INSERT INTO minions VALUES ('25328', '25332', '1', '1');
INSERT INTO minions VALUES ('25339', '25340', '2', '2');
INSERT INTO minions VALUES ('25339', '25341', '2', '2');
INSERT INTO minions VALUES ('25342', '25343', '1', '1');
INSERT INTO minions VALUES ('25342', '25344', '1', '1');
INSERT INTO minions VALUES ('25342', '25345', '2', '2');
INSERT INTO minions VALUES ('25346', '25347', '3', '3');
INSERT INTO minions VALUES ('25346', '25348', '1', '1');
INSERT INTO minions VALUES ('25349', '25350', '3', '3');
INSERT INTO minions VALUES ('25349', '25351', '1', '1');
INSERT INTO minions VALUES ('25352', '25353', '3', '3');
INSERT INTO minions VALUES ('25354', '25355', '4', '4');
INSERT INTO minions VALUES ('25354', '25356', '1', '1');
INSERT INTO minions VALUES ('25357', '25358', '2', '2');
INSERT INTO minions VALUES ('25357', '25359', '1', '1');
INSERT INTO minions VALUES ('25360', '25361', '3', '3');
INSERT INTO minions VALUES ('25362', '25363', '2', '2');
INSERT INTO minions VALUES ('25362', '25364', '3', '3');
INSERT INTO minions VALUES ('25366', '25367', '2', '2');
INSERT INTO minions VALUES ('25366', '25368', '1', '1');
INSERT INTO minions VALUES ('25369', '25370', '1', '1');
INSERT INTO minions VALUES ('25369', '25371', '4', '4');
INSERT INTO minions VALUES ('25373', '25374', '3', '3');
INSERT INTO minions VALUES ('25375', '25376', '2', '2');
INSERT INTO minions VALUES ('25375', '25377', '1', '1');
INSERT INTO minions VALUES ('25378', '25379', '3', '3');
INSERT INTO minions VALUES ('25380', '25381', '1', '1');
INSERT INTO minions VALUES ('25380', '25382', '4', '4');
INSERT INTO minions VALUES ('25383', '25384', '3', '3');
INSERT INTO minions VALUES ('25385', '25386', '1', '1');
INSERT INTO minions VALUES ('25385', '25387', '4', '4');
INSERT INTO minions VALUES ('25388', '25389', '3', '3');
INSERT INTO minions VALUES ('25388', '25390', '2', '2');
INSERT INTO minions VALUES ('25392', '25393', '3', '3');
INSERT INTO minions VALUES ('25395', '25396', '1', '1');
INSERT INTO minions VALUES ('25395', '25397', '2', '2');
INSERT INTO minions VALUES ('25398', '25399', '3', '3');
INSERT INTO minions VALUES ('25398', '25400', '1', '1');
INSERT INTO minions VALUES ('25401', '25402', '1', '1');
INSERT INTO minions VALUES ('25401', '25403', '3', '3');
INSERT INTO minions VALUES ('25404', '25405', '2', '2');
INSERT INTO minions VALUES ('25404', '25406', '1', '1');
INSERT INTO minions VALUES ('25410', '25411', '3', '3');
INSERT INTO minions VALUES ('25415', '25416', '3', '3');
INSERT INTO minions VALUES ('25415', '25417', '1', '1');
INSERT INTO minions VALUES ('25418', '25419', '3', '3');
INSERT INTO minions VALUES ('25420', '25421', '3', '3');
INSERT INTO minions VALUES ('25420', '25422', '1', '1');
INSERT INTO minions VALUES ('25423', '25424', '4', '4');
INSERT INTO minions VALUES ('25423', '25425', '1', '1');
INSERT INTO minions VALUES ('25426', '25427', '2', '2');
INSERT INTO minions VALUES ('25426', '25428', '1', '1');
INSERT INTO minions VALUES ('25429', '25430', '3', '3');
INSERT INTO minions VALUES ('25431', '25432', '2', '2');
INSERT INTO minions VALUES ('25431', '25433', '2', '2');
INSERT INTO minions VALUES ('25434', '25435', '1', '1');
INSERT INTO minions VALUES ('25434', '25436', '3', '3');
INSERT INTO minions VALUES ('25438', '25439', '1', '1');
INSERT INTO minions VALUES ('25438', '25440', '3', '3');
INSERT INTO minions VALUES ('25441', '25442', '4', '4');
INSERT INTO minions VALUES ('25441', '25443', '1', '1');
INSERT INTO minions VALUES ('25444', '25445', '3', '3');
INSERT INTO minions VALUES ('25444', '25446', '2', '2');
INSERT INTO minions VALUES ('25447', '25448', '2', '2');
INSERT INTO minions VALUES ('25447', '25449', '2', '2');
INSERT INTO minions VALUES ('25450', '25451', '3', '3');
INSERT INTO minions VALUES ('25450', '25452', '1', '1');
INSERT INTO minions VALUES ('25453', '25454', '3', '3');
INSERT INTO minions VALUES ('25453', '25455', '1', '1');
INSERT INTO minions VALUES ('25456', '25457', '1', '1');
INSERT INTO minions VALUES ('25456', '25458', '2', '2');
INSERT INTO minions VALUES ('25456', '25459', '1', '1');
INSERT INTO minions VALUES ('25460', '25461', '2', '2');
INSERT INTO minions VALUES ('25460', '25462', '1', '1');
INSERT INTO minions VALUES ('25463', '25464', '1', '1');
INSERT INTO minions VALUES ('25463', '25465', '1', '1');
INSERT INTO minions VALUES ('25463', '25466', '1', '1');
INSERT INTO minions VALUES ('25467', '25468', '4', '4');
INSERT INTO minions VALUES ('25467', '25469', '1', '1');
INSERT INTO minions VALUES ('25470', '25471', '3', '3');
INSERT INTO minions VALUES ('25470', '25472', '1', '1');
INSERT INTO minions VALUES ('25473', '25474', '3', '3');
INSERT INTO minions VALUES ('25475', '25476', '3', '3');
INSERT INTO minions VALUES ('25475', '25477', '2', '2');
INSERT INTO minions VALUES ('25478', '25479', '3', '3');
INSERT INTO minions VALUES ('25478', '25480', '1', '1');
INSERT INTO minions VALUES ('25481', '25482', '2', '2');
INSERT INTO minions VALUES ('25481', '25483', '1', '1');
INSERT INTO minions VALUES ('25484', '25485', '1', '1');
INSERT INTO minions VALUES ('25484', '25486', '2', '2');
INSERT INTO minions VALUES ('25487', '25488', '3', '3');
INSERT INTO minions VALUES ('25487', '25489', '1', '1');
INSERT INTO minions VALUES ('25490', '25491', '3', '3');
INSERT INTO minions VALUES ('25490', '25492', '2', '2');
INSERT INTO minions VALUES ('25493', '25494', '1', '1');
INSERT INTO minions VALUES ('25493', '25495', '4', '4');
INSERT INTO minions VALUES ('25496', '25497', '3', '3');
INSERT INTO minions VALUES ('25498', '25499', '1', '1');
INSERT INTO minions VALUES ('25498', '25500', '4', '4');
INSERT INTO minions VALUES ('25501', '25502', '3', '3');
INSERT INTO minions VALUES ('25501', '25503', '2', '2');
INSERT INTO minions VALUES ('25504', '25505', '3', '3');
INSERT INTO minions VALUES ('25506', '25507', '1', '1');
INSERT INTO minions VALUES ('25506', '25508', '3', '3');
INSERT INTO minions VALUES ('25509', '25510', '1', '1');
INSERT INTO minions VALUES ('25509', '25511', '3', '3');
INSERT INTO minions VALUES ('25514', '25515', '1', '1');
INSERT INTO minions VALUES ('25514', '25516', '3', '3');
INSERT INTO minions VALUES ('25623', '25633', '4', '6');
INSERT INTO minions VALUES ('25625', '25629', '3', '3');
INSERT INTO minions VALUES ('25625', '25630', '3', '3');
INSERT INTO minions VALUES ('25671', '25672', '3', '3');
INSERT INTO minions VALUES ('25671', '25673', '1', '1');
INSERT INTO minions VALUES ('25674', '25675', '3', '3');
INSERT INTO minions VALUES ('25674', '25676', '2', '2');
INSERT INTO minions VALUES ('25677', '25678', '3', '3');
INSERT INTO minions VALUES ('25677', '25679', '1', '1');
INSERT INTO minions VALUES ('25681', '25682', '3', '3');
INSERT INTO minions VALUES ('25684', '25685', '3', '3');
INSERT INTO minions VALUES ('25687', '25688', '1', '1');
INSERT INTO minions VALUES ('25687', '25689', '2', '2');
INSERT INTO minions VALUES ('25572', '25573', '2', '2');
INSERT INTO minions VALUES ('25572', '25574', '2', '2');
INSERT INTO minions VALUES ('25575', '25576', '4', '4');
INSERT INTO minions VALUES ('25575', '25577', '1', '1');
INSERT INTO minions VALUES ('25579', '25580', '4', '4');
INSERT INTO minions VALUES ('25579', '25581', '1', '1');
INSERT INTO minions VALUES ('25582', '25583', '2', '2');
INSERT INTO minions VALUES ('25582', '25584', '2', '2');
INSERT INTO minions VALUES ('25585', '25586', '3', '3');
INSERT INTO minions VALUES ('25585', '25587', '1', '1');
INSERT INTO minions VALUES ('25589', '25590', '1', '1');
INSERT INTO minions VALUES ('25589', '25591', '4', '4');
INSERT INTO minions VALUES ('25593', '25594', '3', '3');
INSERT INTO minions VALUES ('25593', '25595', '2', '2');
INSERT INTO minions VALUES ('27108', '27109', '1', '1');
INSERT INTO minions VALUES ('29096', '29097', '2', '2');
INSERT INTO minions VALUES ('29096', '29098', '3', '4');
INSERT INTO minions VALUES ('25524', '25525', '2', '2');
INSERT INTO minions VALUES ('25524', '25526', '2', '2');
INSERT INTO minions VALUES ('29040', '29041', '1', '1');
INSERT INTO minions VALUES ('29040', '29042', '1', '1');
INSERT INTO minions VALUES ('29040', '29043', '1', '1');
INSERT INTO minions VALUES ('29040', '29044', '1', '1');
INSERT INTO minions VALUES ('29056', '29057', '2', '2');
INSERT INTO minions VALUES ('29056', '29058', '2', '2');
INSERT INTO minions VALUES ('22742', '22744', '3', '3');
INSERT INTO minions VALUES ('22743', '22745', '3', '4');
INSERT INTO minions VALUES ('22202', '22201', '2', '3');
INSERT INTO minions VALUES ('22205', '22204', '4', '4');
INSERT INTO minions VALUES ('22213', '22209', '1', '1');
INSERT INTO minions VALUES ('22213', '22210', '1', '1');
INSERT INTO minions VALUES ('22213', '22212', '2', '2');
INSERT INTO minions VALUES ('22198', '22197', '4', '4');
INSERT INTO minions VALUES ('22223', '22197', '1', '1');
INSERT INTO minions VALUES ('20983', '20984', '1', '1');
INSERT INTO minions VALUES ('20983', '20985', '1', '1');
INSERT INTO minions VALUES ('20983', '21074', '1', '1');
INSERT INTO minions VALUES ('21544', '21545', '1', '1');
INSERT INTO minions VALUES ('21544', '21546', '1', '1');
INSERT INTO minions VALUES ('22305', '22308', '1', '1');
INSERT INTO minions VALUES ('22306', '22308', '2', '2');
INSERT INTO minions VALUES ('22306', '22309', '2', '2');
INSERT INTO minions VALUES ('22307', '22308', '1', '1');
INSERT INTO minions VALUES ('22307', '22309', '2', '2');
INSERT INTO minions VALUES ('22307', '22310', '1', '1');
INSERT INTO minions VALUES ('22416', '22308', '1', '1');
INSERT INTO minions VALUES ('22416', '22309', '1', '1');
INSERT INTO minions VALUES ('22416', '22310', '1', '1');
INSERT INTO minions VALUES ('22416', '22417', '1', '1');
INSERT INTO minions VALUES ('22299', '22300', '2', '2');
INSERT INTO minions VALUES ('22301', '22290', '2', '2');
INSERT INTO minions VALUES ('22423', '22424', '1', '1');
INSERT INTO minions VALUES ('22423', '22425', '1', '1');
INSERT INTO minions VALUES ('22423', '22426', '1', '1');
INSERT INTO minions VALUES ('22423', '22427', '1', '1');
INSERT INTO minions VALUES ('22423', '22428', '1', '1');
INSERT INTO minions VALUES ('22423', '22429', '1', '1');
INSERT INTO minions VALUES ('22423', '22430', '1', '1');
INSERT INTO minions VALUES ('22431', '22432', '1', '1');
INSERT INTO minions VALUES ('22431', '22433', '1', '1');
INSERT INTO minions VALUES ('22431', '22434', '1', '1');
INSERT INTO minions VALUES ('22431', '22435', '1', '1');
INSERT INTO minions VALUES ('22431', '22436', '1', '1');
INSERT INTO minions VALUES ('22431', '22437', '1', '1');
INSERT INTO minions VALUES ('22431', '22438', '1', '1');
INSERT INTO minions VALUES ('18555', '18556', '2', '2');
INSERT INTO minions VALUES ('18559', '18560', '2', '2');
INSERT INTO minions VALUES ('18562', '18563', '2', '2');
INSERT INTO minions VALUES ('18566', '18567', '2', '2');
INSERT INTO minions VALUES ('18568', '18569', '1', '1');
INSERT INTO minions VALUES ('18571', '18572', '2', '2');
INSERT INTO minions VALUES ('18573', '18574', '2', '2');
INSERT INTO minions VALUES ('18577', '18578', '2', '2');
INSERT INTO minions VALUES ('29129', '29130', '2', '2');
INSERT INTO minions VALUES ('29129', '29131', '2', '2');
INSERT INTO minions VALUES ('29132', '29133', '2', '2');
INSERT INTO minions VALUES ('29132', '29134', '2', '2');
INSERT INTO minions VALUES ('29135', '29136', '2', '2');
INSERT INTO minions VALUES ('29135', '29137', '2', '2');
INSERT INTO minions VALUES ('29138', '29139', '2', '2');
INSERT INTO minions VALUES ('29138', '29140', '2', '2');
INSERT INTO minions VALUES ('29141', '29142', '2', '2');
INSERT INTO minions VALUES ('29141', '29143', '2', '2');
INSERT INTO minions VALUES ('29144', '29145', '2', '2');
INSERT INTO minions VALUES ('29144', '29146', '2', '2');
INSERT INTO minions VALUES ('29147', '29148', '2', '2');
INSERT INTO minions VALUES ('29147', '29149', '2', '2');
INSERT INTO minions VALUES ('25710', '25711', '2', '2');
INSERT INTO minions VALUES ('25710', '25712', '2', '2');
INSERT INTO minions VALUES ('22666', '22667', '1', '4');
INSERT INTO minions VALUES ('22670', '22671', '1', '2');
INSERT INTO minions VALUES ('22670', '22672', '1', '3');
INSERT INTO minions VALUES ('22625', '22624', '1', '1');
INSERT INTO minions VALUES ('22630', '22632', '1', '1');
INSERT INTO minions VALUES ('22621', '22620', '1', '1');
INSERT INTO minions VALUES ('25528', '25529', '2', '2');
INSERT INTO minions VALUES ('25528', '25530', '2', '4');
INSERT INTO minions VALUES ('29001', '29003', '5', '5');
INSERT INTO minions VALUES ('29001', '29005', '7', '7');
INSERT INTO minions VALUES ('29002', '29003', '2', '2');
INSERT INTO minions VALUES ('32070', '32071', '1', '1');
INSERT INTO minions VALUES ('22829', '22860', '1', '12');
INSERT INTO minions VALUES ('22827', '22828', '1', '8');
INSERT INTO minions VALUES ('22861', '22826', '1', '4');
INSERT INTO minions VALUES ('25763', '25764', '2', '2');
INSERT INTO minions VALUES ('25763', '25765', '2', '2');
INSERT INTO minions VALUES ('25790', '25792', '3', '3');
INSERT INTO minions VALUES ('25790', '25791', '1', '1');
INSERT INTO minions VALUES ('25787', '25788', '3', '3');
INSERT INTO minions VALUES ('25787', '25789', '2', '2');
INSERT INTO minions VALUES ('25794', '25796', '4', '4');
INSERT INTO minions VALUES ('25794', '25795', '1', '1');
INSERT INTO minions VALUES ('25797', '25798', '3', '3');
INSERT INTO minions VALUES ('25797', '25799', '1', '1');
INSERT INTO minions VALUES ('25757', '25758', '3', '3');
INSERT INTO minions VALUES ('25757', '25759', '1', '1');
INSERT INTO minions VALUES ('25754', '25755', '1', '1');
INSERT INTO minions VALUES ('25754', '25756', '4', '4');
INSERT INTO minions VALUES ('25750', '25751', '2', '2');
INSERT INTO minions VALUES ('25750', '25752', '1', '1');
INSERT INTO minions VALUES ('25784', '25785', '1', '1');
INSERT INTO minions VALUES ('25784', '25786', '3', '3');
INSERT INTO minions VALUES ('25738', '25739', '2', '2');
INSERT INTO minions VALUES ('25738', '25740', '3', '3');
INSERT INTO minions VALUES ('25735', '25736', '3', '3');
INSERT INTO minions VALUES ('25735', '25737', '1', '1');
INSERT INTO minions VALUES ('25744', '25745', '2', '2');
INSERT INTO minions VALUES ('25744', '25746', '2', '2');
INSERT INTO minions VALUES ('25773', '25774', '3', '3');
INSERT INTO minions VALUES ('25773', '25775', '2', '2');
INSERT INTO minions VALUES ('25776', '25777', '1', '1');
INSERT INTO minions VALUES ('25776', '25778', '4', '4');
INSERT INTO minions VALUES ('25747', '25748', '3', '3');
INSERT INTO minions VALUES ('25747', '25749', '2', '2');
INSERT INTO minions VALUES ('25779', '25780', '2', '2');
INSERT INTO minions VALUES ('25779', '25781', '1', '1');
INSERT INTO minions VALUES ('25770', '25771', '3', '3');
INSERT INTO minions VALUES ('25770', '25772', '2', '2');
INSERT INTO minions VALUES ('25767', '25768', '3', '3');
INSERT INTO minions VALUES ('25767', '25769', '2', '2');
INSERT INTO minions VALUES ('25800', '25801', '3', '3');
INSERT INTO minions VALUES ('25782', '25783', '3', '3');
