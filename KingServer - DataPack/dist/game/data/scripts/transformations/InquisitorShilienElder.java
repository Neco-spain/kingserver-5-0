/*
 * Copyright (C) 2004-2013 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package transformations;

import king.server.gameserver.datatables.SkillTable;
import king.server.gameserver.instancemanager.TransformationManager;
import king.server.gameserver.model.L2Transformation;

public class InquisitorShilienElder extends L2Transformation
{
	public InquisitorShilienElder()
	{
		// id
		super(318);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 318) || getPlayer().isCursedWeaponEquipped())
		{
			return;
		}
		
		transformedSkills();
	}
	
	@Override
	public void onUntransform()
	{
		removeSkills();
	}
	
	public void removeSkills()
	{
		int lvl = 1;
		if (getPlayer().getLevel() > 43)
		{
			lvl = (getPlayer().getLevel() - 43);
		}
		
		// Divine Punishment
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(1523, lvl), false);
		// Divine Flash
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(1528, lvl), false);
		// Holy Weapon
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(1043, 1), false, false);
		// Surrender to the Holy
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(1524, lvl), false);
		// Divine Curse
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(1525, lvl), false);
		// Switch Stance
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(838, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		int lvl = 1;
		if (getPlayer().getLevel() > 43)
		{
			lvl = (getPlayer().getLevel() - 43);
		}
		
		// Divine Punishment
		getPlayer().addSkill(SkillTable.getInstance().getInfo(1523, lvl), false);
		// Divine Flash
		getPlayer().addSkill(SkillTable.getInstance().getInfo(1528, lvl), false);
		// Holy Weapon
		getPlayer().addSkill(SkillTable.getInstance().getInfo(1043, 1), false);
		// Surrender to the Holy
		getPlayer().addSkill(SkillTable.getInstance().getInfo(1524, lvl), false);
		// Divine Curse
		getPlayer().addSkill(SkillTable.getInstance().getInfo(1525, lvl), false);
		// Set allowed skills
		getPlayer().setTransformAllowedSkills(new int[]
		{
			838,
			1523,
			1528,
			1524,
			1525,
			1430,
			1303,
			1059,
			1043
		});
		// Switch Stance
		getPlayer().addSkill(SkillTable.getInstance().getInfo(838, 1), false);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new InquisitorShilienElder());
	}
}
