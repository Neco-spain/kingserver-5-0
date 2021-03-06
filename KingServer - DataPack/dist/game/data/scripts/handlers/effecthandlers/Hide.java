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

import king.server.gameserver.ai.CtrlIntention;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.effects.AbnormalEffect;
import king.server.gameserver.model.effects.EffectTemplate;
import king.server.gameserver.model.effects.L2Effect;
import king.server.gameserver.model.effects.L2EffectType;
import king.server.gameserver.model.stats.Env;
import king.server.gameserver.network.serverpackets.DeleteObject;
import king.server.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author ZaKaX, nBd
 */
public class Hide extends L2Effect
{
	public Hide(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	public Hide(Env env, L2Effect effect)
	{
		super(env, effect);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.HIDE;
	}
	
	@Override
	public boolean onStart()
	{
		if (getEffected().isPlayer())
		{
			L2PcInstance activeChar = getEffected().getActingPlayer();
			activeChar.getAppearance().setInvisible();
			activeChar.startAbnormalEffect(AbnormalEffect.STEALTH);
			
			if ((activeChar.getAI().getNextIntention() != null) && (activeChar.getAI().getNextIntention().getCtrlIntention() == CtrlIntention.AI_INTENTION_ATTACK))
			{
				activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
			}
			
			L2GameServerPacket del = new DeleteObject(activeChar);
			for (L2Character target : activeChar.getKnownList().getKnownCharacters())
			{
				try
				{
					if (target.getTarget() == activeChar)
					{
						target.setTarget(null);
						target.abortAttack();
						target.abortCast();
						target.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
					}
					
					if (target.isPlayer())
					{
						target.sendPacket(del);
					}
				}
				catch (NullPointerException e)
				{
				}
			}
		}
		return true;
	}
	
	@Override
	public void onExit()
	{
		if (getEffected().isPlayer())
		{
			L2PcInstance activeChar = getEffected().getActingPlayer();
			if (!activeChar.inObserverMode())
			{
				activeChar.getAppearance().setVisible();
			}
			activeChar.stopAbnormalEffect(AbnormalEffect.STEALTH);
		}
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}