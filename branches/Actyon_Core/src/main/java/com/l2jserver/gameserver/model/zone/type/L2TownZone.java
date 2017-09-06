/*
 * Copyright (C) 2004-2016 L2J Server
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
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;

/**
 * A Town zone
 * @author durgus
 */
public class L2TownZone extends L2ZoneType
{
	private int _townId;
	private int _taxById;
	private boolean _isTWZone = false; // zeus
	
	public L2TownZone(int id)
	{
		super(id);
		
		_taxById = 0;
	}
	
	@Override
	public void setParameter(String name, String value)
	{
		if (name.equals("townId"))
		{
			_townId = Integer.parseInt(value);
		}
		else if (name.equals("taxById"))
		{
			_taxById = Integer.parseInt(value);
		}
		else
		{
			super.setParameter(name, value);
		}
	}
	
	@Override
	protected void onEnter(L2Character character)
	{
		character.setInsideZone(ZoneId.TOWN, true);
		// zeus-------------------------------------
		if (_isTWZone)
		{
			character.setInTownWarEvent(true);
			character.sendMessage("You entered a Town War Event Zone.");
			character.sendPacket(new ExShowScreenMessage(1, -1, ExShowScreenMessage.MIDDLE_CENTER, 1, 1, 0, 0, true, 5000, false, "You entered a Town War Event Zone"));
		}
		// -----------------------------------------
	}
	
	@Override
	protected void onExit(L2Character character)
	{
		// zeus--------------------------------------------
		if (_isTWZone)
		{
			character.setInTownWarEvent(false);
			character.sendMessage("You leave to Town War Event zone.");
			character.sendPacket(new ExShowScreenMessage(1, -1, ExShowScreenMessage.MIDDLE_CENTER, 1, 1, 0, 0, true, 5000, false, "You leave a Town War Event Zone"));
		}
		// ------------------------------------------------
		character.setInsideZone(ZoneId.TOWN, false);
	}
	
	/**
	 * Returns this zones town id (if any)
	 * @return
	 */
	public int getTownId()
	{
		return _townId;
	}
	
	/**
	 * Returns this town zones castle id
	 * @return
	 */
	public final int getTaxById()
	{
		return _taxById;
	}
	
	// zeus--------------------------------------------------------------------------------------------------------
	
	/**
	 * ZeuS
	 */
	public final void setIsTWZone(boolean value)
	{
		_isTWZone = value;
	}
	
	public void updateForCharactersInside()
	{
		for (L2Character character : getCharactersInside())
		{
			if (character != null)
			{
				onUpdate(character);
			}
		}
	}
	
	public void onUpdate(L2Character character)
	{
		if (_isTWZone)
		{
			character.setInTownWarEvent(true);
			character.sendMessage("You entered a Town War Event Zone.");
			character.sendPacket(new ExShowScreenMessage(1, -1, ExShowScreenMessage.MIDDLE_CENTER, 1, 1, 0, 0, true, 5000, false, "You entered a Town War Event Zone"));
		}
		else
		{
			character.setInTownWarEvent(false);
			character.sendMessage("You leave to Town War Event zone.");
			character.sendPacket(new ExShowScreenMessage(1, -1, ExShowScreenMessage.MIDDLE_CENTER, 1, 1, 0, 0, true, 5000, false, "You leave a Town War Event Zone"));
		}
	}
	// ------------------------------------------------------------------------------------------------------------
}
