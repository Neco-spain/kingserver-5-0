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
package king.server.gameserver.model.skills.l2skills;

import java.util.logging.Level;

import king.server.gameserver.events.EventsInterface;
import king.server.gameserver.instancemanager.GrandBossManager;
import king.server.gameserver.instancemanager.MapRegionManager;
import king.server.gameserver.model.L2Object;
import king.server.gameserver.model.Location;
import king.server.gameserver.model.PcCondOverride;
import king.server.gameserver.model.ShotType;
import king.server.gameserver.model.StatsSet;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.entity.TvTEvent;
import king.server.gameserver.model.entity.TvTRoundEvent;
import king.server.gameserver.model.skills.L2Skill;
import king.server.gameserver.model.skills.L2SkillType;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.serverpackets.ActionFailed;

public class L2SkillTeleport extends L2Skill
{
	private final String _recallType;
	private final Location _loc;
	
	public L2SkillTeleport(StatsSet set)
	{
		super(set);
		
		_recallType = set.getString("recallType", "");
		String coords = set.getString("teleCoords", null);
		if (coords != null)
		{
			String[] valuesSplit = coords.split(",");
			_loc = new Location(Integer.parseInt(valuesSplit[0]), Integer.parseInt(valuesSplit[1]), Integer.parseInt(valuesSplit[2]));
		}
		else
		{
			_loc = null;
		}
	}
	
	@Override
	public void useSkill(L2Character activeChar, L2Object[] targets)
	{
		boolean bss = isMagic() && activeChar.isChargedShot(ShotType.BLESSED_SPIRITSHOTS);
		
		if (activeChar.isPlayer())
		{
			// Thanks nbd
			if (!TvTEvent.onEscapeUse(activeChar.getActingPlayer().getObjectId()))
			{
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if (!TvTRoundEvent.onEscapeUse(((L2PcInstance) activeChar).getObjectId()))
			{
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if (EventsInterface.isParticipating(activeChar.getObjectId()))
			{
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if (activeChar.isAfraid())
			{
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if (activeChar.getActingPlayer().isCombatFlagEquipped())
			{
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if (activeChar.getActingPlayer().isInOlympiadMode())
			{
				activeChar.sendPacket(SystemMessageId.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
				return;
			}
			
			if ((GrandBossManager.getInstance().getZone(activeChar) != null) && !activeChar.canOverrideCond(PcCondOverride.SKILL_CONDITIONS))
			{
				activeChar.sendPacket(SystemMessageId.YOU_MAY_NOT_SUMMON_FROM_YOUR_CURRENT_LOCATION);
				return;
			}
		}
		
		try
		{
			for (L2Character target : (L2Character[]) targets)
			{
				if (target.isPlayer())
				{
					L2PcInstance targetChar = target.getActingPlayer();
					
					// Check to see if player is in a duel
					if (targetChar.isInDuel())
					{
						targetChar.sendMessage("You cannot use escape skills during a duel.");
						continue;
					}
					
					if (targetChar != activeChar)
					{
						if (!TvTEvent.onEscapeUse(targetChar.getObjectId()))
						{
							continue;
						}
						
						if (!TvTRoundEvent.onEscapeUse(targetChar.getObjectId()))
						{
							continue;
						}
						
						if (EventsInterface.isParticipating(targetChar.getObjectId()))
						{
							continue;
						}
						
						if (targetChar.isInOlympiadMode())
						{
							continue;
						}
						
						if (GrandBossManager.getInstance().getZone(targetChar) != null)
						{
							continue;
						}
						
						if (targetChar.isCombatFlagEquipped())
						{
							continue;
						}
					}
				}
				Location loc = null;
				if (getSkillType() == L2SkillType.TELEPORT)
				{
					if (_loc != null)
					{
						// target is not player OR player is not flying or flymounted
						// TODO: add check for gracia continent coords
						if (!(target.isPlayer()) || !(target.isFlying() || (target.getActingPlayer().isFlyingMounted())))
						{
							loc = _loc;
						}
					}
				}
				else
				{
					if (_recallType.equalsIgnoreCase("Castle"))
					{
						loc = MapRegionManager.getInstance().getTeleToLocation(target, MapRegionManager.TeleportWhereType.Castle);
					}
					else if (_recallType.equalsIgnoreCase("ClanHall"))
					{
						loc = MapRegionManager.getInstance().getTeleToLocation(target, MapRegionManager.TeleportWhereType.ClanHall);
					}
					else if (_recallType.equalsIgnoreCase("Fortress"))
					{
						loc = MapRegionManager.getInstance().getTeleToLocation(target, MapRegionManager.TeleportWhereType.Fortress);
					}
					else
					{
						loc = MapRegionManager.getInstance().getTeleToLocation(target, MapRegionManager.TeleportWhereType.Town);
					}
				}
				if (loc != null)
				{
					target.setInstanceId(0);
					if (target.isPlayer())
					{
						target.getActingPlayer().setIsIn7sDungeon(false);
					}
					target.teleToLocation(loc, true);
				}
			}
			
			activeChar.setChargedShot(bss ? ShotType.BLESSED_SPIRITSHOTS : ShotType.SPIRITSHOTS, false);
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "", e);
		}
	}
}