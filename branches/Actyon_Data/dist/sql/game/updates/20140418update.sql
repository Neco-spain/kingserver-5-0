INSERT INTO character_variables (charId, var, val) SELECT charId, REPLACE(var, 'SkillTransfer', 'HOLY_POMANDER_') AS var, 'true' AS val FROM character_quest_global_data WHERE var LIKE "SkillTransfer%";
DELETE FROM character_quest_global_data WHERE var LIKE "SkillTransfer%";