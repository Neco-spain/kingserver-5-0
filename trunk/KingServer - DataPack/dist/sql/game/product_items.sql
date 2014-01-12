DROP TABLE IF EXISTS `product_items`;
CREATE TABLE `product_items` (
  `product_id` int(11) NOT NULL,
  `name` text CHARACTER SET utf8,
  `category` int(11) NOT NULL DEFAULT '5',
  `points` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of product_items
-- ----------------------------
INSERT INTO `product_items` VALUES ('1050021', 'Powerful Healing Potion', '2', '3');
INSERT INTO `product_items` VALUES ('1050022', 'High-grade Healing Potion', '2', '1');
INSERT INTO `product_items` VALUES ('1080001', 'Small fortuna box', '2', '200');
INSERT INTO `product_items` VALUES ('1080002', 'Middle fortuna box', '2', '270');
INSERT INTO `product_items` VALUES ('1080003', 'Large fortuna box', '2', '405');
INSERT INTO `product_items` VALUES ('1080004', 'Small fortuna cube', '2', '81');
INSERT INTO `product_items` VALUES ('1080005', 'Middle fortuna cube', '2', '216');
INSERT INTO `product_items` VALUES ('1080006', 'Large fortuna cube', '2', '324');
INSERT INTO `product_items` VALUES ('1080009', 'Secret medicine of Will - D grade', '2', '4');
INSERT INTO `product_items` VALUES ('1080010', 'Secret medicine of Will - C grade', '2', '13');
INSERT INTO `product_items` VALUES ('1080011', 'Secret medicine of Will - B grade', '2', '22');
INSERT INTO `product_items` VALUES ('1080012', 'Secret medicine of Will - A grade', '2', '34');
INSERT INTO `product_items` VALUES ('1080013', 'Secret medicine of Will - S grade', '2', '49');
INSERT INTO `product_items` VALUES ('1080014', 'Secret medicine of Life - D grade', '2', '10');
INSERT INTO `product_items` VALUES ('1080015', 'Secret medicine of Life - C grade', '2', '30');
INSERT INTO `product_items` VALUES ('1080016', 'Secret medicine of Life - B grade', '2', '54');
INSERT INTO `product_items` VALUES ('1080017', 'Secret medicine of Life - A grade', '2', '85');
INSERT INTO `product_items` VALUES ('1080018', 'Secret medicine of Life - S grade', '2', '122');
INSERT INTO `product_items` VALUES ('1080019', 'Potion of Will', '2', '4');
INSERT INTO `product_items` VALUES ('1080021', 'Wind Walk Scroll', '5', '4');
INSERT INTO `product_items` VALUES ('1080022', 'Haste Scroll', '2', '8');
INSERT INTO `product_items` VALUES ('1080023', 'Might Scroll', '2', '4');
INSERT INTO `product_items` VALUES ('1080024', 'Shield Scroll', '2', '4');
INSERT INTO `product_items` VALUES ('1080025', 'Death Whisper Scroll', '2', '8');
INSERT INTO `product_items` VALUES ('1080026', 'Guidance Scroll', '2', '8');
INSERT INTO `product_items` VALUES ('1080027', 'Empower Scroll', '2', '8');
INSERT INTO `product_items` VALUES ('1080028', 'Grater Acumen Scroll', '2', '8');
INSERT INTO `product_items` VALUES ('1080029', 'Vampiric Rage Scroll', '2', '8');
INSERT INTO `product_items` VALUES ('1080030', 'Bless the Body Scroll', '2', '8');
INSERT INTO `product_items` VALUES ('1080031', 'Berserker Spirit Scroll', '2', '8');
INSERT INTO `product_items` VALUES ('1080032', 'Magic Barrier Scroll', '2', '4');
INSERT INTO `product_items` VALUES ('1080033', 'Rune of SP - 336 Hour Expiration Period', '2', '8');
INSERT INTO `product_items` VALUES ('1080034', 'Rune of SP - 720 Hour Expiration Period', '2', '8');
INSERT INTO `product_items` VALUES ('1080035', 'Crystal form Rune - 24 Hour Expiration Period', '2', '8');

-- Faltou na tabela
INSERT INTO `product_items` VALUES ('1080036', 'Rune of Experience Points', '1', '8');
INSERT INTO `product_items` VALUES ('1080037', 'Rune of Experience Points', '1', '8');
INSERT INTO `product_items` VALUES ('1080038', 'Rune of Experience Points', '1', '8');
INSERT INTO `product_items` VALUES ('1080039', 'Rune of Experience Points', '1', '8');
INSERT INTO `product_items` VALUES ('1080040', 'Rune of SP', '1', '8');
INSERT INTO `product_items` VALUES ('1080041', 'Rune of SP', '1', '8');
INSERT INTO `product_items` VALUES ('1080042', 'Rune of SP', '1', '8');
INSERT INTO `product_items` VALUES ('1080043', 'Rune of SP', '1', '8');
INSERT INTO `product_items` VALUES ('1080044', 'Crystal form Rune', '1', '3');
INSERT INTO `product_items` VALUES ('1080045', 'Crystal form Rune', '1', '3');
INSERT INTO `product_items` VALUES ('1080046', 'Crystal form Rune', '1', '3');
INSERT INTO `product_items` VALUES ('1080047', 'Crystal form Rune', '1', '3');
-- Final

INSERT INTO `product_items` VALUES ('1080048', 'Rune of Feather - 24 Hour Expiration Period', '1', '68');
INSERT INTO `product_items` VALUES ('1080049', 'A Scroll Bundle of Fighter', '4', '52');
INSERT INTO `product_items` VALUES ('1080050', 'A Scroll Bundle of Mage', '4', '59');
INSERT INTO `product_items` VALUES ('1080051', 'Bone Quiver', '4', '21');
INSERT INTO `product_items` VALUES ('1080052', 'Steel Quiver', '4', '34');
INSERT INTO `product_items` VALUES ('1080053', 'Silver Quiver', '4', '48');
INSERT INTO `product_items` VALUES ('1080054', 'Mithril Quiver', '4', '54');
INSERT INTO `product_items` VALUES ('1080055', 'uiver of Light', '4', '68');
INSERT INTO `product_items` VALUES ('1080056', 'Bone Bolt Container', '4', '21');
INSERT INTO `product_items` VALUES ('1080057', 'Steel Bolt Container', '4', '34');
INSERT INTO `product_items` VALUES ('1080058', 'Silver Bolt Container', '4', '48');
INSERT INTO `product_items` VALUES ('1080059', 'Mithril Bolt Container', '4', '54');
INSERT INTO `product_items` VALUES ('1080060', 'Bolt Container of Light', '4', '68');
INSERT INTO `product_items` VALUES ('1080061', 'Blessed Spiritshot Pack - D grade', '4', '31');
INSERT INTO `product_items` VALUES ('1080062', 'Blessed Spiritshot Pack - C grade', '4', '61');
INSERT INTO `product_items` VALUES ('1080063', 'Blessed Spiritshot Pack - B grade', '4', '166');
INSERT INTO `product_items` VALUES ('1080064', 'Blessed Spiritshot Pack - A grade', '4', '196');
INSERT INTO `product_items` VALUES ('1080065', 'Blessed Spiritshot Pack - S grade', '4', '237');
INSERT INTO `product_items` VALUES ('1080066', 'Spiritshot Pack - D grade', '4', '12');
INSERT INTO `product_items` VALUES ('1080067', 'Spiritshot Pack - C grade', '4', '24');
INSERT INTO `product_items` VALUES ('1080068', 'Spiritshot Pack - B grade', '4', '68');
INSERT INTO `product_items` VALUES ('1080069', 'Spiritshot Pack - A grade', '4', '81');
INSERT INTO `product_items` VALUES ('1080070', 'Spiritshot Pack - S grade', '4', '102');
INSERT INTO `product_items` VALUES ('1080071', 'Soulshot Pack - D grade', '4', '8');
INSERT INTO `product_items` VALUES ('1080072', 'Soulshot Pack - C grade', '4', '10');
INSERT INTO `product_items` VALUES ('1080073', 'Soulshot Pack - B grade', '4', '34');
INSERT INTO `product_items` VALUES ('1080074', 'Soulshot Pack - A grade', '4', '54');
INSERT INTO `product_items` VALUES ('1080075', 'Soulshot Pack - S grade', '4', '68');
INSERT INTO `product_items` VALUES ('1080076', 'Blessed Spiritshot Large Pack - D grade', '4', '61');
INSERT INTO `product_items` VALUES ('1080077', 'Blessed Spiritshot Large Pack - C grade', '4', '122');
INSERT INTO `product_items` VALUES ('1080078', 'Blessed Spiritshot Large Pack - B grade', '4', '331');
INSERT INTO `product_items` VALUES ('1080079', 'Blessed Spiritshot Large Pack - A grade', '4', '392');
INSERT INTO `product_items` VALUES ('1080080', 'Blessed Spiritshot Large Pack - S grade', '4', '473');
INSERT INTO `product_items` VALUES ('1080081', 'Spiritshot Large Pack - D grade', '4', '24');
INSERT INTO `product_items` VALUES ('1080082', 'Spiritshot Large Pack - C grade', '4', '48');
INSERT INTO `product_items` VALUES ('1080083', 'Spiritshot Large Pack - B grade', '4', '135');
INSERT INTO `product_items` VALUES ('1080084', 'Spiritshot Large Pack - A grade', '4', '162');
INSERT INTO `product_items` VALUES ('1080085', 'Spiritshot Large Pack - S grade', '4', '203');
INSERT INTO `product_items` VALUES ('1080086', 'Soulshot Large Pack - D grade', '4', '14');
INSERT INTO `product_items` VALUES ('1080087', 'Soulshot Large Pack - C grade', '4', '21');
INSERT INTO `product_items` VALUES ('1080088', 'Soulshot Large Pack - B grade', '4', '68');
INSERT INTO `product_items` VALUES ('1080089', 'Soulshot Large Pack - A grade', '4', '108');
INSERT INTO `product_items` VALUES ('1080090', 'Soulshot Large Pack - S grade', '4', '135');
INSERT INTO `product_items` VALUES ('1080091', 'Wrapped daisy hairpin', '3', '338');
INSERT INTO `product_items` VALUES ('1080092', 'Wrapped forget-me-not hairpin', '3', '338');
INSERT INTO `product_items` VALUES ('1080093', 'Wrapped outlaws eyepatch', '3', '338');
INSERT INTO `product_items` VALUES ('1080094', 'Wrapped pirates eyepatch', '3', '338');
INSERT INTO `product_items` VALUES ('1080095', 'Wrapped Monocle', '3', '338');
INSERT INTO `product_items` VALUES ('1080096', 'Wrapped Red Mask of Victory', '3', '338');
INSERT INTO `product_items` VALUES ('1080097', 'Wrapped Red Horn of Victory', '3', '338');
INSERT INTO `product_items` VALUES ('1080098', 'Wrapped Party Mask', '3', '338');
INSERT INTO `product_items` VALUES ('1080099', 'Wrapped Red Party Mask', '3', '338');
INSERT INTO `product_items` VALUES ('1080100', 'Wrapped Cat Ear', '3', '338');
INSERT INTO `product_items` VALUES ('1080101', 'Wrapped Noblewomans Hairpin', '3', '338');
INSERT INTO `product_items` VALUES ('1080102', 'Wrapped Raccoon Ear', '3', '338');
INSERT INTO `product_items` VALUES ('1080103', 'Wrapped Rabbit Ear', '3', '338');
INSERT INTO `product_items` VALUES ('1080104', 'Wrapped Little Angels Wings', '3', '338');
INSERT INTO `product_items` VALUES ('1080105', 'Wrapped Fairys Tentacle', '3', '338');
INSERT INTO `product_items` VALUES ('1080106', 'Wrapped Dandys Chapeau', '3', '338');
INSERT INTO `product_items` VALUES ('1080107', 'Wrapped Artisans Goggles', '3', '338');
INSERT INTO `product_items` VALUES ('1080112', 'Rune of Experience: 30% - 5 hour limited time', '1', '33');
INSERT INTO `product_items` VALUES ('1080113', 'Rune of Exp. Points 50% - 5 Hour Expiration Period', '1', '54');
INSERT INTO `product_items` VALUES ('1080114', 'Rune of Exp. Points 30% - 10 Hour Expiration Period', '1', '52');
INSERT INTO `product_items` VALUES ('1080115', 'Rune of Exp. Points 50% - 10 Hour Expiration Period', '1', '87');
INSERT INTO `product_items` VALUES ('1080116', 'Rune of Exp. Points 30% - 7 Day Expiration Period', '1', '697');
INSERT INTO `product_items` VALUES ('1080117', 'Rune of Exp. Points 50% - 7 Day Expiration Period', '1', '1161');
INSERT INTO `product_items` VALUES ('1080118', 'Rune of SP 30% - 5 Hour Expiration Period', '1', '17');
INSERT INTO `product_items` VALUES ('1080119', 'Rune of SP 50% - 5 Hour Expiration Period', '1', '27');
INSERT INTO `product_items` VALUES ('1080120', 'Rune of SP 30% - 10 Hour Expiration Period', '1', '26');
INSERT INTO `product_items` VALUES ('1080121', 'Rune of SP 50% - 10 Hour Expiration Period', '1', '44');
INSERT INTO `product_items` VALUES ('1080122', 'Rune of SP 30% - 7 Day Expiration Period', '1', '349');
INSERT INTO `product_items` VALUES ('1080123', 'Rune of SP 50% - 7 Day Expiration Period', '1', '581');
INSERT INTO `product_items` VALUES ('1080124', 'Rune of Crystal level 3 - 5 Hour Expiration Period', '1', '33');
INSERT INTO `product_items` VALUES ('1080125', 'Rune of Crystal level 5 - 5 Hour Expiration Period', '1', '54');
INSERT INTO `product_items` VALUES ('1080126', 'Rune of Crystal level 3 - 10 Hour Expiration Period', '1', '52');
INSERT INTO `product_items` VALUES ('1080127', 'Rune of Crystal level 5 - 10 Hour Expiration Period', '1', '87');
INSERT INTO `product_items` VALUES ('1080128', 'Rune of Crystal level 3 - 7 Day Expiration Period', '1', '697');
INSERT INTO `product_items` VALUES ('1080129', 'Rune of Crystal level 5 - 7 Day Expiration Period', '1', '1161');
INSERT INTO `product_items` VALUES ('1080130', 'Weapon-Type Enhance Backup Stone (D-Grade)', '1', '21');
INSERT INTO `product_items` VALUES ('1080131', 'Weapon-Type Enhance Backup Stone (C-Grade)', '1', '45');
INSERT INTO `product_items` VALUES ('1080132', 'Weapon-Type Enhance Backup Stone (B-Grade)', '1', '203');
INSERT INTO `product_items` VALUES ('1080133', 'Weapon-Type Enhance Backup Stone (A-Grade)', '1', '729');
INSERT INTO `product_items` VALUES ('1080134', 'Weapon-Type Enhance Backup Stone (S-Grade)', '1', '2025');
INSERT INTO `product_items` VALUES ('1080135', 'Armor-Type Enhance Backup Stone (D-Grade)', '1', '4');
INSERT INTO `product_items` VALUES ('1080136', 'Armor-Type Enhance Backup Stone (C-Grade)', '1', '7');
INSERT INTO `product_items` VALUES ('1080137', 'Armor-Type Enhance Backup Stone (B-Grade)', '1', '29');
INSERT INTO `product_items` VALUES ('1080138', 'Armor-Type Enhance Backup Stone (A-Grade)', '1', '104');
INSERT INTO `product_items` VALUES ('1080139', 'Armor-Type Enhance Backup Stone (S-Grade)', '1', '290');
INSERT INTO `product_items` VALUES ('1080140', 'Beast Soulshot Pack', '4', '14');
INSERT INTO `product_items` VALUES ('1080141', 'Beast Spiritshot Pack', '4', '11');
INSERT INTO `product_items` VALUES ('1080142', 'Blessed Beast Spiritshot Pack', '4', '68');
INSERT INTO `product_items` VALUES ('1080143', 'Beast Soulshot Large Pack', '4', '27');
INSERT INTO `product_items` VALUES ('1080144', 'Beast Spiritshot Large Pack', '4', '22');
INSERT INTO `product_items` VALUES ('1080145', 'Blessed Beast Spiritshot Large Pack', '4', '135');
INSERT INTO `product_items` VALUES ('1080146', 'Omen Beast Transformation Scroll', '5', '30');
INSERT INTO `product_items` VALUES ('1080147', 'Death Blader Transformation Scroll', '5', '30');
INSERT INTO `product_items` VALUES ('1080148', 'Grail Apostle Transformation Scroll', '5', '30');
INSERT INTO `product_items` VALUES ('1080149', 'Unicorn Transformation Scroll', '5', '30');
INSERT INTO `product_items` VALUES ('1080150', 'Lilim Knight Transformation Scroll', '5', '30');
INSERT INTO `product_items` VALUES ('1080151', 'Golem Guardian Transformation Scroll', '5', '30');
INSERT INTO `product_items` VALUES ('1080152', 'Inferno Drake Transformation Scroll', '5', '30');
INSERT INTO `product_items` VALUES ('1080153', 'Dragon Bomber Transformation Scroll', '5', '30');
INSERT INTO `product_items` VALUES ('1080154', 'Escape - Talking Island Village', '5', '27');
INSERT INTO `product_items` VALUES ('1080155', 'Escape - Elven Village', '5', '27');
INSERT INTO `product_items` VALUES ('1080156', 'Escape - Dark Elven Village', '5', '27');
INSERT INTO `product_items` VALUES ('1080157', 'Escape - Orc Village', '5', '27');
INSERT INTO `product_items` VALUES ('1080158', 'Escape - Dwarven Village', '5', '27');
INSERT INTO `product_items` VALUES ('1080159', 'Escape - Gludin Village', '5', '27');
INSERT INTO `product_items` VALUES ('1080160', 'Escape - Town of Gludio', '5', '27');
INSERT INTO `product_items` VALUES ('1080161', 'Escape - Town of Dion', '5', '27');
INSERT INTO `product_items` VALUES ('1080162', 'Escape - Floran Village', '5', '27');
INSERT INTO `product_items` VALUES ('1080163', 'Escape - Giran Castle Town', '5', '27');
INSERT INTO `product_items` VALUES ('1080164', 'Escape - Hardins Academy', '5', '27');
INSERT INTO `product_items` VALUES ('1080165', 'Escape - Heine', '5', '27');
INSERT INTO `product_items` VALUES ('1080166', 'Escape - Town of Oren', '5', '27');
INSERT INTO `product_items` VALUES ('1080167', 'Escape - Ivory Tower', '5', '27');
INSERT INTO `product_items` VALUES ('1080168', 'Escape - Hunters Village', '5', '27');
INSERT INTO `product_items` VALUES ('1080169', 'Escape - Town of Aden', '5', '27');
INSERT INTO `product_items` VALUES ('1080170', 'Escape - Town of Goddard', '5', '27');
INSERT INTO `product_items` VALUES ('1080171', 'Escape - Rune Township', '5', '27');
INSERT INTO `product_items` VALUES ('1080172', 'Escape - Town of Schuttgart', '5', '27');
INSERT INTO `product_items` VALUES ('1080173', 'My Teleport Spellbook', '5', '675');
INSERT INTO `product_items` VALUES ('1080174', 'My Teleport Scroll', '5', '135');
INSERT INTO `product_items` VALUES ('1080175', 'My Teleport Scroll', '5', '270');
INSERT INTO `product_items` VALUES ('1080176', 'My Teleport Flag', '5', '338');
INSERT INTO `product_items` VALUES ('1080177', 'My Teleport Flag', '5', '675');
INSERT INTO `product_items` VALUES ('1080178', 'Extra Entrance Pass - Kamaloka (Hall of the Abyss)', '5', '338');
INSERT INTO `product_items` VALUES ('1080179', 'Extra Entrance Pass - Kamaloka (Hall of the Abyss)', '5', '675');
INSERT INTO `product_items` VALUES ('1080180', 'Extra Entrance Pass - Near Kamaloka', '5', '338');
INSERT INTO `product_items` VALUES ('1080181', 'Extra Entrance Pass - Near Kamaloka', '5', '675');
INSERT INTO `product_items` VALUES ('1080182', 'Extra Entrance Pass - Kamaloka (Labyrinth of the Abyss)', '5', '338');
INSERT INTO `product_items` VALUES ('1080183', 'Extra Entrance Pass - Kamaloka (Labyrinth of the Abyss)', '5', '675');
INSERT INTO `product_items` VALUES ('1080185', 'Color Name', '5', '268');
INSERT INTO `product_items` VALUES ('1080186', 'Greater CP Potion', '3', '14');
INSERT INTO `product_items` VALUES ('1080197', 'Potion of Energy Maintenance', '3', '142');
INSERT INTO `product_items` VALUES ('1080198', 'Potion of Vitality Replenishin', '3', '68');
INSERT INTO `product_items` VALUES ('1080199', 'Sweet Fruit Cocktail', '5', '79');
INSERT INTO `product_items` VALUES ('1080200', 'Fresh Fruit Cocktail', '5', '91');
INSERT INTO `product_items` VALUES ('1080201', 'Sudden Agathion 7 Day Pack', '3', '338');
INSERT INTO `product_items` VALUES ('1080202', 'Shiny Agathion 7 Day Pac', '3', '338');
INSERT INTO `product_items` VALUES ('1080203', 'Sobbing Agathion 7 Day Pack', '3', '338');
INSERT INTO `product_items` VALUES ('1080205', 'Pumpkin Transformation Stick 7-Day Pack (Event)', '3', '254');
INSERT INTO `product_items` VALUES ('1080206', 'Kat the Cat Hat 7-Day Pack (Event)', '3', '169');
INSERT INTO `product_items` VALUES ('1080207', 'Feline Queen Hat 7-Day Pack (Event)', '3', '169');
INSERT INTO `product_items` VALUES ('1080208', 'Monster Eye Hat 7-Day Pack (Event)', '3', '169');
INSERT INTO `product_items` VALUES ('1080209', 'Brown Bear Hat 7-Day Pack (Event)', '3', '169');
INSERT INTO `product_items` VALUES ('1080210', 'Fungus Hat 7-Day Pack (Event)', '3', '169');
INSERT INTO `product_items` VALUES ('1080211', 'Skull Hat 7-Day Pack (Event)', '3', '169');
INSERT INTO `product_items` VALUES ('1080212', 'Ornithomimus Hat 7-Day Pack (Event)', '3', '169');
INSERT INTO `product_items` VALUES ('1080213', 'Feline King Hat 7-Day Pack (Event)', '3', '169');
INSERT INTO `product_items` VALUES ('1080214', 'Kai the Cat Hat 7-Day Pack (Event)', '3', '169');
INSERT INTO `product_items` VALUES ('1080229', 'OX Stick 7-Day Pack (Event)', '3', '169');
INSERT INTO `product_items` VALUES ('1080230', 'Rock-Paper-Scissors Stick 7-Day Pack (Event)', '3', '506');
INSERT INTO `product_items` VALUES ('1080236', 'Mounting Item 3 Pack', '5', '199');
INSERT INTO `product_items` VALUES ('1080238', 'Steam Beatle Mounting Bracelet - 7-day Limited Period', '5', '89');
INSERT INTO `product_items` VALUES ('1080239', 'Light Purple-Maned Horse Mounting Bracelet - 7 day limited period', '5', '89');
INSERT INTO `product_items` VALUES ('1080240', '10 minute Energy Maintaining Potion', '5', '18');
INSERT INTO `product_items` VALUES ('1080241', 'Vitality Maintenance Potion - 30 minutes', '5', '54');
INSERT INTO `product_items` VALUES ('1080242', 'Rune of Exp. Points 30% - 3 hours limited time', '5', '24');
INSERT INTO `product_items` VALUES ('1080243', 'Rune of Exp. Points 30%', '5', '9');
INSERT INTO `product_items` VALUES ('1080244', 'Rune of SP 30%', '5', '5');
INSERT INTO `product_items` VALUES ('1080245', 'Hardins Divine Protection', '5', '15');
INSERT INTO `product_items` VALUES ('1080246', 'Hardins Blessing', '5', '15');
INSERT INTO `product_items` VALUES ('1080247', 'Silpeeds Wing', '5', '5');
INSERT INTO `product_items` VALUES ('1080248', 'Silpeeds Blessing', '5', '92');
INSERT INTO `product_items` VALUES ('1080249', 'Potion of a Hero', '5', '1');

DROP TABLE IF EXISTS `product_item_components`;
CREATE TABLE `product_item_components` (
  `product_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `count` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`product_id`,`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of product_item_components
-- ----------------------------
INSERT INTO `product_item_components` VALUES ('1050021', '22025', '1');
INSERT INTO `product_item_components` VALUES ('1050022', '22026', '1');
INSERT INTO `product_item_components` VALUES ('1080001', '22000', '1');
INSERT INTO `product_item_components` VALUES ('1080002', '22001', '1');
INSERT INTO `product_item_components` VALUES ('1080003', '22002', '1');
INSERT INTO `product_item_components` VALUES ('1080004', '22003', '1');
INSERT INTO `product_item_components` VALUES ('1080005', '22004', '1');
INSERT INTO `product_item_components` VALUES ('1080006', '22005', '1');
INSERT INTO `product_item_components` VALUES ('1080009', '22027', '1');
INSERT INTO `product_item_components` VALUES ('1080010', '22028', '1');
INSERT INTO `product_item_components` VALUES ('1080011', '22029', '1');
INSERT INTO `product_item_components` VALUES ('1080012', '22030', '1');
INSERT INTO `product_item_components` VALUES ('1080013', '22031', '1');
INSERT INTO `product_item_components` VALUES ('1080014', '22032', '1');
INSERT INTO `product_item_components` VALUES ('1080015', '22033', '1');
INSERT INTO `product_item_components` VALUES ('1080016', '22034', '1');
INSERT INTO `product_item_components` VALUES ('1080017', '22035', '1');
INSERT INTO `product_item_components` VALUES ('1080018', '22036', '1');
INSERT INTO `product_item_components` VALUES ('1080019', '22037', '1');
INSERT INTO `product_item_components` VALUES ('1080021', '22039', '1');
INSERT INTO `product_item_components` VALUES ('1080022', '22040', '1');
INSERT INTO `product_item_components` VALUES ('1080023', '22041', '1');
INSERT INTO `product_item_components` VALUES ('1080024', '22042', '1');
INSERT INTO `product_item_components` VALUES ('1080025', '22043', '1');
INSERT INTO `product_item_components` VALUES ('1080026', '22044', '1');
INSERT INTO `product_item_components` VALUES ('1080027', '22045', '1');
INSERT INTO `product_item_components` VALUES ('1080028', '22046', '1');
INSERT INTO `product_item_components` VALUES ('1080029', '22047', '1');
INSERT INTO `product_item_components` VALUES ('1080030', '22048', '1');
INSERT INTO `product_item_components` VALUES ('1080031', '22049', '1');
INSERT INTO `product_item_components` VALUES ('1080032', '22050', '1');
INSERT INTO `product_item_components` VALUES ('1080033', '22060', '1');
INSERT INTO `product_item_components` VALUES ('1080034', '22061', '1');
INSERT INTO `product_item_components` VALUES ('1080035', '22062', '1');

-- faltou na tabela

INSERT INTO `product_item_components` VALUES ('1080036', '22054', '1');
INSERT INTO `product_item_components` VALUES ('1080037', '22055', '1');
INSERT INTO `product_item_components` VALUES ('1080038', '22056', '1');
INSERT INTO `product_item_components` VALUES ('1080039', '22057', '1');
INSERT INTO `product_item_components` VALUES ('1080040', '22058', '1');
INSERT INTO `product_item_components` VALUES ('1080041', '22059', '1');
INSERT INTO `product_item_components` VALUES ('1080042', '22060', '1');
INSERT INTO `product_item_components` VALUES ('1080043', '22061', '1');
INSERT INTO `product_item_components` VALUES ('1080044', '22062', '1');
INSERT INTO `product_item_components` VALUES ('1080045', '22063', '1');
INSERT INTO `product_item_components` VALUES ('1080046', '22064', '1');
INSERT INTO `product_item_components` VALUES ('1080057', '22065', '1');

-- fim

INSERT INTO `product_item_components` VALUES ('1080048', '22066', '1');
INSERT INTO `product_item_components` VALUES ('1080049', '22087', '1');
INSERT INTO `product_item_components` VALUES ('1080050', '22088', '1');
INSERT INTO `product_item_components` VALUES ('1080051', '22089', '1');
INSERT INTO `product_item_components` VALUES ('1080052', '22090', '1');
INSERT INTO `product_item_components` VALUES ('1080053', '22091', '1');
INSERT INTO `product_item_components` VALUES ('1080054', '22092', '1');
INSERT INTO `product_item_components` VALUES ('1080055', '22093', '1');
INSERT INTO `product_item_components` VALUES ('1080056', '22149', '1');
INSERT INTO `product_item_components` VALUES ('1080057', '22150', '1');
INSERT INTO `product_item_components` VALUES ('1080058', '22151', '1');
INSERT INTO `product_item_components` VALUES ('1080059', '22152', '1');
INSERT INTO `product_item_components` VALUES ('1080060', '22153', '1');
INSERT INTO `product_item_components` VALUES ('1080061', '22094', '1');
INSERT INTO `product_item_components` VALUES ('1080062', '22095', '1');
INSERT INTO `product_item_components` VALUES ('1080063', '22096', '1');
INSERT INTO `product_item_components` VALUES ('1080064', '22097', '1');
INSERT INTO `product_item_components` VALUES ('1080065', '22098', '1');
INSERT INTO `product_item_components` VALUES ('1080066', '22099', '1');
INSERT INTO `product_item_components` VALUES ('1080067', '22100', '1');
INSERT INTO `product_item_components` VALUES ('1080068', '22101', '1');
INSERT INTO `product_item_components` VALUES ('1080069', '22102', '1');
INSERT INTO `product_item_components` VALUES ('1080070', '22103', '1');
INSERT INTO `product_item_components` VALUES ('1080071', '22104', '1');
INSERT INTO `product_item_components` VALUES ('1080072', '22105', '1');
INSERT INTO `product_item_components` VALUES ('1080073', '22106', '1');
INSERT INTO `product_item_components` VALUES ('1080074', '22107', '1');
INSERT INTO `product_item_components` VALUES ('1080075', '22108', '1');
INSERT INTO `product_item_components` VALUES ('1080076', '22109', '1');
INSERT INTO `product_item_components` VALUES ('1080077', '22110', '1');
INSERT INTO `product_item_components` VALUES ('1080078', '22111', '1');
INSERT INTO `product_item_components` VALUES ('1080079', '22112', '1');
INSERT INTO `product_item_components` VALUES ('1080080', '22113', '1');
INSERT INTO `product_item_components` VALUES ('1080081', '22114', '1');
INSERT INTO `product_item_components` VALUES ('1080082', '22115', '1');
INSERT INTO `product_item_components` VALUES ('1080083', '22116', '1');
INSERT INTO `product_item_components` VALUES ('1080084', '22117', '1');
INSERT INTO `product_item_components` VALUES ('1080085', '22118', '1');
INSERT INTO `product_item_components` VALUES ('1080086', '22119', '1');
INSERT INTO `product_item_components` VALUES ('1080087', '22120', '1');
INSERT INTO `product_item_components` VALUES ('1080088', '22121', '1');
INSERT INTO `product_item_components` VALUES ('1080089', '22122', '1');
INSERT INTO `product_item_components` VALUES ('1080090', '22123', '1');
INSERT INTO `product_item_components` VALUES ('1080091', '22124', '1');
INSERT INTO `product_item_components` VALUES ('1080092', '22125', '1');
INSERT INTO `product_item_components` VALUES ('1080093', '22126', '1');
INSERT INTO `product_item_components` VALUES ('1080094', '22127', '1');
INSERT INTO `product_item_components` VALUES ('1080095', '22128', '1');
INSERT INTO `product_item_components` VALUES ('1080096', '22129', '1');
INSERT INTO `product_item_components` VALUES ('1080097', '22130', '1');
INSERT INTO `product_item_components` VALUES ('1080098', '22131', '1');
INSERT INTO `product_item_components` VALUES ('1080099', '22132', '1');
INSERT INTO `product_item_components` VALUES ('1080100', '22133', '1');
INSERT INTO `product_item_components` VALUES ('1080101', '22134', '1');
INSERT INTO `product_item_components` VALUES ('1080102', '22135', '1');
INSERT INTO `product_item_components` VALUES ('1080103', '22136', '1');
INSERT INTO `product_item_components` VALUES ('1080104', '22137', '1');
INSERT INTO `product_item_components` VALUES ('1080105', '22138', '1');
INSERT INTO `product_item_components` VALUES ('1080106', '22139', '1');
INSERT INTO `product_item_components` VALUES ('1080107', '22140', '1');
INSERT INTO `product_item_components` VALUES ('1080112', '20335', '1');
INSERT INTO `product_item_components` VALUES ('1080113', '20336', '1');
INSERT INTO `product_item_components` VALUES ('1080114', '20337', '1');
INSERT INTO `product_item_components` VALUES ('1080115', '20338', '1');
INSERT INTO `product_item_components` VALUES ('1080116', '20339', '1');
INSERT INTO `product_item_components` VALUES ('1080117', '20340', '1');
INSERT INTO `product_item_components` VALUES ('1080118', '20341', '1');
INSERT INTO `product_item_components` VALUES ('1080119', '20342', '1');
INSERT INTO `product_item_components` VALUES ('1080120', '20343', '1');
INSERT INTO `product_item_components` VALUES ('1080121', '20344', '1');
INSERT INTO `product_item_components` VALUES ('1080122', '20345', '1');
INSERT INTO `product_item_components` VALUES ('1080123', '20346', '1');
INSERT INTO `product_item_components` VALUES ('1080124', '20347', '1');
INSERT INTO `product_item_components` VALUES ('1080125', '20348', '1');
INSERT INTO `product_item_components` VALUES ('1080126', '20349', '1');
INSERT INTO `product_item_components` VALUES ('1080127', '20350', '1');
INSERT INTO `product_item_components` VALUES ('1080128', '20351', '1');
INSERT INTO `product_item_components` VALUES ('1080129', '20352', '1');
INSERT INTO `product_item_components` VALUES ('1080130', '12362', '1');
INSERT INTO `product_item_components` VALUES ('1080131', '12363', '1');
INSERT INTO `product_item_components` VALUES ('1080132', '12364', '1');
INSERT INTO `product_item_components` VALUES ('1080133', '12365', '1');
INSERT INTO `product_item_components` VALUES ('1080134', '12366', '1');
INSERT INTO `product_item_components` VALUES ('1080135', '12367', '1');
INSERT INTO `product_item_components` VALUES ('1080136', '12368', '1');
INSERT INTO `product_item_components` VALUES ('1080137', '12369', '1');
INSERT INTO `product_item_components` VALUES ('1080138', '12370', '1');
INSERT INTO `product_item_components` VALUES ('1080139', '12371', '1');
INSERT INTO `product_item_components` VALUES ('1080140', '20326', '1');
INSERT INTO `product_item_components` VALUES ('1080141', '20327', '1');
INSERT INTO `product_item_components` VALUES ('1080142', '20328', '1');
INSERT INTO `product_item_components` VALUES ('1080143', '20329', '1');
INSERT INTO `product_item_components` VALUES ('1080144', '20330', '1');
INSERT INTO `product_item_components` VALUES ('1080145', '20331', '1');
INSERT INTO `product_item_components` VALUES ('1080146', '20364', '1');
INSERT INTO `product_item_components` VALUES ('1080147', '20365', '1');
INSERT INTO `product_item_components` VALUES ('1080148', '20366', '1');
INSERT INTO `product_item_components` VALUES ('1080149', '20367', '1');
INSERT INTO `product_item_components` VALUES ('1080150', '20368', '1');
INSERT INTO `product_item_components` VALUES ('1080151', '20369', '1');
INSERT INTO `product_item_components` VALUES ('1080152', '20370', '1');
INSERT INTO `product_item_components` VALUES ('1080153', '20371', '1');
INSERT INTO `product_item_components` VALUES ('1080154', '20372', '1');
INSERT INTO `product_item_components` VALUES ('1080155', '20373', '1');
INSERT INTO `product_item_components` VALUES ('1080156', '20374', '1');
INSERT INTO `product_item_components` VALUES ('1080157', '20375', '1');
INSERT INTO `product_item_components` VALUES ('1080158', '20376', '1');
INSERT INTO `product_item_components` VALUES ('1080159', '20377', '1');
INSERT INTO `product_item_components` VALUES ('1080160', '20378', '1');
INSERT INTO `product_item_components` VALUES ('1080161', '20379', '1');
INSERT INTO `product_item_components` VALUES ('1080162', '20380', '1');
INSERT INTO `product_item_components` VALUES ('1080163', '20381', '1');
INSERT INTO `product_item_components` VALUES ('1080164', '20382', '1');
INSERT INTO `product_item_components` VALUES ('1080165', '20383', '1');
INSERT INTO `product_item_components` VALUES ('1080166', '20384', '1');
INSERT INTO `product_item_components` VALUES ('1080167', '20385', '1');
INSERT INTO `product_item_components` VALUES ('1080168', '20386', '1');
INSERT INTO `product_item_components` VALUES ('1080169', '20387', '1');
INSERT INTO `product_item_components` VALUES ('1080170', '20388', '1');
INSERT INTO `product_item_components` VALUES ('1080171', '20389', '1');
INSERT INTO `product_item_components` VALUES ('1080172', '20390', '1');
INSERT INTO `product_item_components` VALUES ('1080173', '13015', '1');
INSERT INTO `product_item_components` VALUES ('1080174', '13016', '5');
INSERT INTO `product_item_components` VALUES ('1080175', '13016', '10');
INSERT INTO `product_item_components` VALUES ('1080176', '20033', '5');
INSERT INTO `product_item_components` VALUES ('1080177', '20033', '10');
INSERT INTO `product_item_components` VALUES ('1080178', '13010', '5');
INSERT INTO `product_item_components` VALUES ('1080179', '13010', '10');
INSERT INTO `product_item_components` VALUES ('1080180', '13011', '5');
INSERT INTO `product_item_components` VALUES ('1080181', '13011', '10');
INSERT INTO `product_item_components` VALUES ('1080182', '13012', '5');
INSERT INTO `product_item_components` VALUES ('1080183', '13012', '10');
INSERT INTO `product_item_components` VALUES ('1080185', '13021', '1');
INSERT INTO `product_item_components` VALUES ('1080186', '5592', '1');
INSERT INTO `product_item_components` VALUES ('1080197', '20391', '1');
INSERT INTO `product_item_components` VALUES ('1080198', '20392', '1');
INSERT INTO `product_item_components` VALUES ('1080199', '20393', '1');
INSERT INTO `product_item_components` VALUES ('1080200', '20394', '1');
INSERT INTO `product_item_components` VALUES ('1080201', '139', '1');
INSERT INTO `product_item_components` VALUES ('1080202', '140', '1');
INSERT INTO `product_item_components` VALUES ('1080203', '141', '1');
INSERT INTO `product_item_components` VALUES ('1080205', '13370', '1');
INSERT INTO `product_item_components` VALUES ('1080206', '13371', '1');
INSERT INTO `product_item_components` VALUES ('1080207', '13372', '1');
INSERT INTO `product_item_components` VALUES ('1080208', '13373', '1');
INSERT INTO `product_item_components` VALUES ('1080209', '13374', '1');
INSERT INTO `product_item_components` VALUES ('1080210', '13375', '1');
INSERT INTO `product_item_components` VALUES ('1080211', '13376', '1');
INSERT INTO `product_item_components` VALUES ('1080212', '13377', '1');
INSERT INTO `product_item_components` VALUES ('1080213', '13378', '1');
INSERT INTO `product_item_components` VALUES ('1080214', '13379', '1');
INSERT INTO `product_item_components` VALUES ('1080229', '13380', '1');
INSERT INTO `product_item_components` VALUES ('1080230', '13381', '1');
INSERT INTO `product_item_components` VALUES ('1080236', '17019', '1');
INSERT INTO `product_item_components` VALUES ('1080238', '14054', '1');
INSERT INTO `product_item_components` VALUES ('1080239', '13022', '1');
INSERT INTO `product_item_components` VALUES ('1080240', '15438', '1');
INSERT INTO `product_item_components` VALUES ('1080241', '15440', '1');
INSERT INTO `product_item_components` VALUES ('1080242', '20572', '1');
INSERT INTO `product_item_components` VALUES ('1080243', '21084', '1');
INSERT INTO `product_item_components` VALUES ('1080244', '21086', '1');
INSERT INTO `product_item_components` VALUES ('1080245', '21030', '1');
INSERT INTO `product_item_components` VALUES ('1080246', '21031', '1');
INSERT INTO `product_item_components` VALUES ('1080247', '21032', '1');
INSERT INTO `product_item_components` VALUES ('1080248', '21033', '1');
INSERT INTO `product_item_components` VALUES ('1080249', '21038', '1');