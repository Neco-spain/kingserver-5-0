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
package handlers.effecthandlers;

import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.effects.EffectTemplate;
import king.server.gameserver.model.effects.L2Effect;
import king.server.gameserver.model.effects.L2EffectType;
import king.server.gameserver.model.stats.Env;
import king.server.gameserver.network.SystemMessageId;

/**
 * Summon Agathion effect.
 * @author Zoey76
 */
public class SummonAgathion extends L2Effect
{
	public SummonAgathion(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public boolean onStart()
	{
		if ((getEffector() == null) || (getEffected() == null) || !getEffector().isPlayer() || !getEffected().isPlayer() || getEffected().isAlikeDead())
		{
			return false;
		}
		
		final L2PcInstance player = getEffector().getActingPlayer();
		if (player.isInOlympiadMode())
		{
			player.sendPacket(SystemMessageId.THIS_SKILL_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
			return false;
		}
		
		setAgathionId(player);
		player.broadcastUserInfo();
		return true;
	}
	
	@Override
	public void onExit()
	{
		super.onExit();
		final L2PcInstance player = getEffector().getActingPlayer();
		if (player != null)
		{
			player.setAgathionId(0);
			player.broadcastUserInfo();
		}
	}
	
	/**
	 * Set the player's agathion Id.
	 * @param player the player to set the agathion Id.
	 */
	protected void setAgathionId(L2PcInstance player)
	{
		player.setAgathionId((getSkill() == null) ? 0 : getSkill().getNpcId());
	}
	
	@Override
	public boolean onActionTime()
	{
		return true;
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.SUMMON_AGATHION;
	}
}
