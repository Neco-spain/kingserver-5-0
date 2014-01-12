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
package king.server.gameserver.model.actor.instance;

import king.server.gameserver.ai.CtrlEvent;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.templates.L2NpcTemplate;
import king.server.gameserver.network.NpcStringId;
import king.server.gameserver.network.clientpackets.Say2;
import king.server.gameserver.network.serverpackets.CreatureSay;
import king.server.util.Rnd;

public class L2PenaltyMonsterInstance extends L2MonsterInstance
{
	private L2PcInstance _ptk;
	
	public L2PenaltyMonsterInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
		setInstanceType(InstanceType.L2PenaltyMonsterInstance);
	}
	
	@Override
	public L2Character getMostHated()
	{
		if (_ptk != null)
		{
			return _ptk; // always attack only one person
		}
		return super.getMostHated();
	}
	
	public void setPlayerToKill(L2PcInstance ptk)
	{
		if (Rnd.get(100) <= 80)
		{
			CreatureSay cs = new CreatureSay(getObjectId(), Say2.NPC_ALL, getName(), NpcStringId.YOUR_BAIT_WAS_TOO_DELICIOUS_NOW_I_WILL_KILL_YOU);
			this.broadcastPacket(cs);
		}
		_ptk = ptk;
		addDamageHate(ptk, 0, 10);
		getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, ptk);
		addAttackerToAttackByList(ptk);
	}
	
	@Override
	public boolean doDie(L2Character killer)
	{
		if (!super.doDie(killer))
		{
			return false;
		}
		
		if (Rnd.get(100) <= 75)
		{
			CreatureSay cs = new CreatureSay(getObjectId(), Say2.NPC_ALL, getName(), NpcStringId.I_WILL_TELL_FISH_NOT_TO_TAKE_YOUR_BAIT);
			this.broadcastPacket(cs);
		}
		return true;
	}
}
