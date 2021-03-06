/*
 * Copyright (C) 2004-2013 L2J Server
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
package king.server.gameserver.model.actor.position;

import king.server.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author Erb
 */
public class PcPosition extends CharPosition
{
	public PcPosition(L2PcInstance activeObject)
	{
		super(activeObject);
	}
	
	@Override
	public L2PcInstance getActiveObject()
	{
		return ((L2PcInstance) super.getActiveObject());
	}
	
	@Override
	protected void badCoords()
	{
		getActiveObject().teleToLocation(0, 0, 0, false);
		getActiveObject().sendMessage("Error with your coords, Please ask a GM for help!");
	}
}
