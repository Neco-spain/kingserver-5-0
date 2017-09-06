/*
 * Copyright (C) 2004-2017 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.model.zone.type;

import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.zone.L2ZoneType;
import com.l2jserver.gameserver.model.zone.ZoneId;

public class L2NoZeusZone extends L2ZoneType
{
	public L2NoZeusZone(int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(L2Character character)
	{
		character.setInsideZone(ZoneId.NO_ZEUS, true);
	}
	
	@Override
	protected void onExit(L2Character character)
	{
		character.setInsideZone(ZoneId.NO_ZEUS, false);
	}
	
	@Override
	public void onDieInside(L2Character character)
	{
	}
	
	@Override
	public void onReviveInside(L2Character character)
	{
	}
	
}
