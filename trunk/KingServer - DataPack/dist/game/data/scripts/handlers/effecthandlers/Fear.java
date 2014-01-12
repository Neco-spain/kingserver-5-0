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

import king.server.Config;
import king.server.gameserver.GeoData;
import king.server.gameserver.ai.CtrlIntention;
import king.server.gameserver.model.L2CharPosition;
import king.server.gameserver.model.Location;
import king.server.gameserver.model.actor.instance.L2DefenderInstance;
import king.server.gameserver.model.actor.instance.L2FortCommanderInstance;
import king.server.gameserver.model.actor.instance.L2GrandBossInstance;
import king.server.gameserver.model.actor.instance.L2NpcInstance;
import king.server.gameserver.model.actor.instance.L2PetInstance;
import king.server.gameserver.model.actor.instance.L2RaidBossInstance;
import king.server.gameserver.model.actor.instance.L2SiegeFlagInstance;
import king.server.gameserver.model.actor.instance.L2SiegeSummonInstance;
import king.server.gameserver.model.effects.EffectFlag;
import king.server.gameserver.model.effects.EffectTemplate;
import king.server.gameserver.model.effects.L2Effect;
import king.server.gameserver.model.effects.L2EffectType;
import king.server.gameserver.model.stats.Env;

/**
 * Implementation of the Fear Effect
 * @author littlecrow
 */
public class Fear extends L2Effect
{
	public static final int FEAR_RANGE = 500;
	
	private int _dX = -1;
	private int _dY = -1;
	
	public Fear(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.FEAR;
	}
	
	@Override
	public boolean onStart()
	{
		if ((getEffected() instanceof L2NpcInstance) || (getEffected() instanceof L2DefenderInstance) || (getEffected() instanceof L2FortCommanderInstance) || (getEffected() instanceof L2GrandBossInstance) || (getEffected() instanceof L2RaidBossInstance) || (getEffected() instanceof L2SiegeFlagInstance) || (getEffected() instanceof L2SiegeSummonInstance))
		{
			return false;
		}
		
		if (!getEffected().isAfraid())
		{
			if (getEffected().isCastingNow() && getEffected().canAbortCast())
			{
				getEffected().abortCast();
			}
			
			if (getEffected().getX() > getEffector().getX())
			{
				_dX = 1;
			}
			if (getEffected().getY() > getEffector().getY())
			{
				_dY = 1;
			}
			
			getEffected().startFear();
			onActionTime();
			return true;
		}
		return false;
	}
	
	@Override
	public void onExit()
	{
		getEffected().stopFear(false);
	}
	
	@Override
	public boolean onActionTime()
	{
		int posX = getEffected().getX();
		int posY = getEffected().getY();
		int posZ = getEffected().getZ();
		
		if (getEffected().getX() > getEffector().getX())
		{
			_dX = 1;
		}
		if (getEffected().getY() > getEffector().getY())
		{
			_dY = 1;
		}
		
		posX += _dX * FEAR_RANGE;
		posY += _dY * FEAR_RANGE;
		
		if (Config.GEODATA > 0)
		{
			Location destiny = GeoData.getInstance().moveCheck(getEffected().getX(), getEffected().getY(), getEffected().getZ(), posX, posY, posZ, getEffected().getInstanceId());
			posX = destiny.getX();
			posY = destiny.getY();
		}
		
		if (!(getEffected() instanceof L2PetInstance))
		{
			getEffected().setRunning();
		}
		
		getEffected().getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(posX, posY, posZ, 0));
		return true;
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.FEAR.getMask();
	}
}
