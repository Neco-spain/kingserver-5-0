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
 * this program. If not, see <http://king.server.ru/>.
 */
package ai.group_template;

import ai.npc.AbstractNpcAI;

import king.server.gameserver.ai.CtrlIntention;
import king.server.gameserver.model.actor.L2Attackable;
import king.server.gameserver.model.actor.L2Npc;
import king.server.gameserver.model.actor.instance.L2MonsterInstance;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.util.Rnd;

public class SpikedStakatoNurse extends AbstractNpcAI
{
	private static final int SPIKED_STAKATO_BABY = 22632;
	private static final int SPIKED_STAKATO_NURSE_2ND_FORM = 22631;
	
	public SpikedStakatoNurse(int questId, String name, String descr)
	{
		super(name, descr);
		
		addKillId(SPIKED_STAKATO_BABY);
		addKillId(SPIKED_STAKATO_NURSE_2ND_FORM);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		final L2Npc nurse = getNurse(npc);
		if ((nurse != null) && !nurse.isDead())
		{
			getNurse(npc).doDie(getNurse(npc));
			final L2Npc newForm = addSpawn(SPIKED_STAKATO_NURSE_2ND_FORM, npc.getX() + Rnd.get(10, 50), npc.getY() + Rnd.get(10, 50), npc.getZ(), 0, false, 0, true);
			newForm.setRunning();
			((L2Attackable) newForm).addDamageHate(killer, 1, 99999);
			newForm.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, killer);
		}
		return super.onKill(npc, killer, isPet);
	}
	
	public L2Npc getNurse(L2Npc couple)
	{
		return ((L2MonsterInstance) couple).getLeader();
	}
	
	public static void main(String[] args)
	{
		new SpikedStakatoNurse(-1, "SpikedStakatoNurse", "ai");
	}
}