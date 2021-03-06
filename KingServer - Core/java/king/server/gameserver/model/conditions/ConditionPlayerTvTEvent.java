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

import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.entity.TvTEvent;
import king.server.gameserver.model.stats.Env;

/**
 * The Class ConditionPlayerTvTEvent.
 */
public class ConditionPlayerTvTEvent extends Condition
{
	private final boolean _val;
	
	/**
	 * Instantiates a new condition player tv t event.
	 * @param val the val
	 */
	public ConditionPlayerTvTEvent(boolean val)
	{
		_val = val;
	}
	
	@Override
	public boolean testImpl(Env env)
	{
		final L2PcInstance player = env.getPlayer();
		if ((player == null) || !TvTEvent.isStarted())
		{
			return !_val;
		}
		return (TvTEvent.isPlayerParticipant(player.getObjectId()) == _val);
	}
}
