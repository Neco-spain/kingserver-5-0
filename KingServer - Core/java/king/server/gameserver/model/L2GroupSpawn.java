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
package king.server.gameserver.model;

import java.lang.reflect.Constructor;
import java.util.logging.Level;

import king.server.Config;
import king.server.gameserver.datatables.TerritoryTable;
import king.server.gameserver.idfactory.IdFactory;
import king.server.gameserver.model.actor.L2Npc;
import king.server.gameserver.model.actor.templates.L2NpcTemplate;
import king.server.util.Rnd;

/**
 * @author littlecrow A special spawn implementation to spawn controllable mob
 */
public class L2GroupSpawn extends L2Spawn
{
	private final Constructor<?> _constructor;
	private final L2NpcTemplate _template;
	
	public L2GroupSpawn(L2NpcTemplate mobTemplate) throws SecurityException, ClassNotFoundException, NoSuchMethodException
	{
		super(mobTemplate);
		_constructor = Class.forName("king.server.gameserver.model.actor.instance.L2ControllableMobInstance").getConstructors()[0];
		_template = mobTemplate;
		
		setAmount(1);
	}
	
	public L2Npc doGroupSpawn()
	{
		L2Npc mob = null;
		
		try
		{
			if (_template.isType("L2Pet") || _template.isType("L2Minion"))
			{
				return null;
			}
			
			Object[] parameters =
			{
				IdFactory.getInstance().getNextId(),
				_template
			};
			Object tmp = _constructor.newInstance(parameters);
			
			if (!(tmp instanceof L2Npc))
			{
				return null;
			}
			
			mob = (L2Npc) tmp;
			
			int newlocx, newlocy, newlocz;
			
			if ((getLocx() == 0) && (getLocy() == 0))
			{
				if (getLocation() == 0)
				{
					return null;
				}
				
				int p[] = TerritoryTable.getInstance().getRandomPoint(getLocation());
				newlocx = p[0];
				newlocy = p[1];
				newlocz = p[2];
			}
			else
			{
				newlocx = getLocx();
				newlocy = getLocy();
				newlocz = getLocz();
			}
			
			mob.setCurrentHpMp(mob.getMaxHp(), mob.getMaxMp());
			
			if (getHeading() == -1)
			{
				mob.setHeading(Rnd.nextInt(61794));
			}
			else
			{
				mob.setHeading(getHeading());
			}
			
			mob.setSpawn(this);
			mob.spawnMe(newlocx, newlocy, newlocz);
			mob.onSpawn();
			
			if (Config.DEBUG)
			{
				_log.finest("Spawned Mob Id: " + _template.getNpcId() + " ,at: X: " + mob.getX() + " Y: " + mob.getY() + " Z: " + mob.getZ());
			}
			return mob;
			
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "NPC class not found: " + e.getMessage(), e);
			return null;
		}
	}
}