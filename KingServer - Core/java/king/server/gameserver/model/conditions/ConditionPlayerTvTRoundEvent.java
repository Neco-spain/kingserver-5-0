/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package king.server.gameserver.model.conditions;

import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.entity.TvTRoundEvent;
import king.server.gameserver.model.stats.Env;

/**
 * The Class ConditionPlayerTvTRoundEvent.
 */
public class ConditionPlayerTvTRoundEvent extends Condition
{
	private final boolean _val;
	
	/**
	 * Instantiates a new condition player tvt round event.
	 *
	 * @param val the val
	 */
	public ConditionPlayerTvTRoundEvent(boolean val)
	{
		_val = val;
	}
	
	@Override
	public boolean testImpl(Env env)
	{
		final L2PcInstance player = env.getPlayer();
		if (player == null || !TvTRoundEvent.isStarted())
			return !_val;
		
		return (TvTRoundEvent.isPlayerParticipant(player.getObjectId()) == _val);
	}
}
