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
package king.server.gameserver.model.conditions;

import king.server.gameserver.model.stats.Env;
import king.server.gameserver.model.zone.ZoneId;

/**
 * The Class ConditionPlayerLandingZone.
 * @author kerberos
 */
public class ConditionPlayerLandingZone extends Condition
{
	
	private final boolean _val;
	
	/**
	 * Instantiates a new condition player landing zone.
	 * @param val the val
	 */
	public ConditionPlayerLandingZone(boolean val)
	{
		_val = val;
	}
	
	@Override
	public boolean testImpl(Env env)
	{
		return env.getCharacter().isInsideZone(ZoneId.LANDING) == _val;
	}
}
