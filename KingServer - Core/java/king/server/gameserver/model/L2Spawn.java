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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastList;

import king.server.Config;
import king.server.gameserver.GeoData;
import king.server.gameserver.ThreadPoolManager;
import king.server.gameserver.datatables.ChampionData;
import king.server.gameserver.datatables.NpcPersonalAIData;
import king.server.gameserver.datatables.TerritoryTable;
import king.server.gameserver.idfactory.IdFactory;
import king.server.gameserver.model.actor.L2Attackable;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.L2Npc;
import king.server.gameserver.model.actor.instance.L2MonsterInstance;
import king.server.gameserver.model.actor.templates.L2NpcTemplate;
import king.server.util.Rnd;

/**
 * This class manages the spawn and respawn of a group of L2NpcInstance that are in the same are and have the same type.<br>
 * <B><U>Concept</U>:</B><br>
 * L2NpcInstance can be spawned either in a random position into a location area (if Lox=0 and Locy=0), either at an exact position.<br>
 * The heading of the L2NpcInstance can be a random heading if not defined (value= -1) or an exact heading (ex : merchant...).
 * @author Nightmare
 */
public class L2Spawn
{
	protected static final Logger _log = Logger.getLogger(L2Spawn.class.getName());
	
	/** The link on the L2NpcTemplate object containing generic and static properties of this spawn (ex : RewardExp, RewardSP, AggroRange...) */
	private L2NpcTemplate _template;
	
	// private String _location = DEFAULT_LOCATION;
	
	/** String identifier of this spawn */
	private String _name;
	
	/** The identifier of the location area where L2NpcInstance can be spawned */
	private int _location;
	
	/** The maximum number of L2NpcInstance that can manage this L2Spawn */
	private int _maximumCount;
	
	/** The current number of L2NpcInstance managed by this L2Spawn */
	private int _currentCount;
	
	/** The current number of SpawnTask in progress or stand by of this L2Spawn */
	protected int _scheduledCount;
	
	/** The X position of the spwan point */
	private int _locX;
	
	/** The Y position of the spwan point */
	private int _locY;
	
	/** The Z position of the spwan point */
	private int _locZ;
	
	/** The heading of L2NpcInstance when they are spawned */
	private int _heading;
	
	/** Minimum respawn delay */
	private int _respawnMinDelay;
	
	/** Maximum respawn delay */
	private int _respawnMaxDelay;
	
	private int _instanceId = 0;
	
	private int _onKillDelay = 5000;
	
	/** The generic constructor of L2NpcInstance managed by this L2Spawn */
	private Constructor<?> _constructor;
	
	/** If True a L2NpcInstance is respawned each time that another is killed */
	private boolean _doRespawn;
	
	/** If true then spawn is custom */
	private boolean _customSpawn;
	
	private L2Npc _lastSpawn;
	private static List<SpawnListener> _spawnListeners = new FastList<>();
 	
	private boolean _isNoRndWalk = false; // Is no random walk
	
	/** The task launching the function doSpawn() */
	class SpawnTask implements Runnable
	{
		private final L2Npc _oldNpc;
		
		public SpawnTask(L2Npc pOldNpc)
		{
			_oldNpc = pOldNpc;
		}
		
		@Override
		public void run()
		{
			try
			{
				// doSpawn();
				respawnNpc(_oldNpc);
			}
			catch (Exception e)
			{
				_log.log(Level.WARNING, "", e);
			}
			
			_scheduledCount--;
		}
	}
	
	/**
	 * Constructor of L2Spawn.<br>
	 * <B><U>Concept</U>:</B><br>
	 * Each L2Spawn owns generic and static properties (ex : RewardExp, RewardSP, AggroRange...).<br>
	 * All of those properties are stored in a different L2NpcTemplate for each type of L2Spawn. Each template is loaded once in the server cache memory (reduce memory use).<br>
	 * When a new instance of L2Spawn is created, server just create a link between the instance and the template.<br>
	 * This link is stored in <B>_template</B> Each L2NpcInstance is linked to a L2Spawn that manages its spawn and respawn (delay, location...).<br>
	 * This link is stored in <B>_spawn</B> of the L2NpcInstance.<br>
	 * <B><U> Actions</U>:</B><br>
	 * <ul>
	 * <li>Set the _template of the L2Spawn</li>
	 * <li>Calculate the implementationName used to generate the generic constructor of L2NpcInstance managed by this L2Spawn</li>
	 * <li>Create the generic constructor of L2NpcInstance managed by this L2Spawn</li>
	 * </ul>
	 * @param mobTemplate The L2NpcTemplate to link to this L2Spawn
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 */
	public L2Spawn(L2NpcTemplate mobTemplate) throws SecurityException, ClassNotFoundException, NoSuchMethodException
	{
		// Set the _template of the L2Spawn
		_template = mobTemplate;
		
		if (_template == null)
		{
			return;
		}
		
		// Create the generic constructor of L2NpcInstance managed by this L2Spawn
		Class<?>[] parameters =
		{
			int.class,
			Class.forName("king.server.gameserver.model.actor.templates.L2NpcTemplate")
		};
		_constructor = Class.forName("king.server.gameserver.model.actor.instance." + _template.getType() + "Instance").getConstructor(parameters);
	}
	
	/**
	 * @return the maximum number of L2NpcInstance that this L2Spawn can manage.
	 */
	public int getAmount()
	{
		return _maximumCount;
	}
	
	/**
	 * @return the String Identifier of this spawn.
	 */
	public String getName()
	{
		return _name;
	}
	
	/**
	 * @return the Identifier of the location area where L2NpcInstance can be spawned.
	 */
	public int getLocation()
	{
		return _location;
	}
	
	public Location getSpawnLocation()
	{
		return new Location(getLocx(), getLocy(), getLocz(), getHeading());
	}
	
	/**
	 * @return the X position of the spwan point.
	 */
	public int getLocx()
	{
		return _locX;
	}
	
	/**
	 * @return the Y position of the spwan point.
	 */
	public int getLocy()
	{
		return _locY;
	}
	
	/**
	 * @return the Z position of the spwan point.
	 */
	public int getLocz()
	{
		return _locZ;
	}
	
	/**
	 * @return the Identifier of the L2NpcInstance manage by this L2Spawn contained in the L2NpcTemplate.
	 */
	public int getNpcid()
	{
		return _template.getNpcId();
	}
	
	/**
	 * @return the heading of L2NpcInstance when they are spawned.
	 */
	public int getHeading()
	{
		return _heading;
	}
	
	/**
	 * @return min respawn delay.
	 */
	public int getRespawnMinDelay()
	{
		return _respawnMinDelay;
	}
	
	/**
	 * @return max respawn delay.
	 */
	public int getRespawnMaxDelay()
	{
		return _respawnMaxDelay;
	}
	
	/**
	 * Set the maximum number of L2NpcInstance that this L2Spawn can manage.
	 * @param amount
	 */
	public void setAmount(int amount)
	{
		_maximumCount = amount;
	}
	
	/**
	 * Set the String Identifier of this spawn.
	 * @param name
	 */
	public void setName(String name)
	{
		_name = name;
	}
	
	/**
	 * Set the Identifier of the location area where L2NpcInstance can be spawned.
	 * @param location
	 */
	public void setLocation(int location)
	{
		_location = location;
	}
	
	/**
	 * Set Minimum Respawn Delay.
	 * @param date
	 */
	public void setRespawnMinDelay(int date)
	{
		_respawnMinDelay = date;
	}
	
	/**
	 * Set Maximum Respawn Delay.
	 * @param date
	 */
	public void setRespawnMaxDelay(int date)
	{
		_respawnMaxDelay = date;
	}
	
	/**
	 * Set the X position of the spawn point.
	 * @param locx
	 */
	public void setLocx(int locx)
	{
		_locX = locx;
	}
	
	/**
	 * Set the Y position of the spawn point.
	 * @param locy
	 */
	public void setLocy(int locy)
	{
		_locY = locy;
	}
	
	/**
	 * Set the Z position of the spawn point.
	 * @param locz
	 */
	public void setLocz(int locz)
	{
		_locZ = locz;
	}
	
	/**
	 * Set the XYZ position of the spawn point.
	 * @param loc
	 */
	public void setLocation(Location loc)
	{
		_locX = loc.getX();
		_locY = loc.getY();
		_locZ = loc.getZ();
		_heading = loc.getHeading();
	}
	
	/**
	 * Set the heading of L2NpcInstance when they are spawned.
	 * @param heading
	 */
	public void setHeading(int heading)
	{
		_heading = heading;
	}
	
	/**
	 * Set the spawn as custom.<BR>
	 * @param custom
	 */
	public void setCustom(boolean custom)
	{
		_customSpawn = custom;
	}
	
	/**
	 * @return type of spawn.
	 */
	public boolean isCustom()
	{
		return _customSpawn;
	}
	
	/**
	 * Decrease the current number of L2NpcInstance of this L2Spawn and if necessary create a SpawnTask to launch after the respawn Delay. <B><U> Actions</U> :</B> <li>Decrease the current number of L2NpcInstance of this L2Spawn</li> <li>Check if respawn is possible to prevent multiple respawning
	 * caused by lag</li> <li>Update the current number of SpawnTask in progress or stand by of this L2Spawn</li> <li>Create a new SpawnTask to launch after the respawn Delay</li> <FONT COLOR=#FF0000><B> <U>Caution</U> : A respawn is possible ONLY if _doRespawn=True and _scheduledCount +
	 * _currentCount < _maximumCount</B></FONT>
	 * @param oldNpc
	 */
	public void decreaseCount(L2Npc oldNpc)
	{
		// sanity check
		if (_currentCount <= 0)
		{
			return;
		}
		
		// Decrease the current number of L2NpcInstance of this L2Spawn
		_currentCount--;
		
		// Check if respawn is possible to prevent multiple respawning caused by lag
		if (_doRespawn && ((_scheduledCount + _currentCount) < _maximumCount))
		{
			// Update the current number of SpawnTask in progress or stand by of this L2Spawn
			_scheduledCount++;
			
			// Create a new SpawnTask to launch after the respawn Delay
			// ClientScheduler.getInstance().scheduleLow(new SpawnTask(npcId), _respawnDelay);
			ThreadPoolManager.getInstance().scheduleGeneral(new SpawnTask(oldNpc), hasRespawnRandom() ? Rnd.get(_respawnMinDelay, _respawnMaxDelay) : _respawnMinDelay);
		}
	}
	
	/**
	 * Create the initial spawning and set _doRespawn to False, if respawn time set to 0, or set it to True otherwise.
	 * @return The number of L2NpcInstance that were spawned
	 */
	public int init()
	{
		while (_currentCount < _maximumCount)
		{
			doSpawn();
		}
		_doRespawn = _respawnMinDelay != 0;
		
		return _currentCount;
	}
	
	/**
	 * Create a L2NpcInstance in this L2Spawn.
	 * @param val
	 * @return
	 */
	public L2Npc spawnOne(boolean val)
	{
		return doSpawn(val);
	}
	
	/**
	 * @return true if respawn enabled
	 */
	public boolean isRespawnEnabled()
	{
		return _doRespawn;
	}
	
	/**
	 * Set _doRespawn to False to stop respawn in this L2Spawn.
	 */
	public void stopRespawn()
	{
		_doRespawn = false;
	}
	
	/**
	 * Set _doRespawn to True to start or restart respawn in this L2Spawn.
	 */
	public void startRespawn()
	{
		_doRespawn = true;
	}
	
	public L2Npc doSpawn()
	{
		return doSpawn(false);
	}
	
	/**
	 * Create the L2NpcInstance, add it to the world and lauch its OnSpawn action.<br>
	 * <B><U>Concept</U>:</B><br>
	 * L2NpcInstance can be spawned either in a random position into a location area (if Lox=0 and Locy=0), either at an exact position.<br>
	 * The heading of the L2NpcInstance can be a random heading if not defined (value= -1) or an exact heading (ex : merchant...).<br>
	 * <B><U>Actions for an random spawn into location area</U>:<I> (if Locx=0 and Locy=0)</I></B>
	 * <ul>
	 * <li>Get L2NpcInstance Init parameters and its generate an Identifier</li>
	 * <li>Call the constructor of the L2NpcInstance</li>
	 * <li>Calculate the random position in the location area (if Locx=0 and Locy=0) or get its exact position from the L2Spawn</li>
	 * <li>Set the position of the L2NpcInstance</li>
	 * <li>Set the HP and MP of the L2NpcInstance to the max</li>
	 * <li>Set the heading of the L2NpcInstance (random heading if not defined : value=-1)</li>
	 * <li>Link the L2NpcInstance to this L2Spawn</li>
	 * <li>Init other values of the L2NpcInstance (ex : from its L2CharTemplate for INT, STR, DEX...) and add it in the world</li>
	 * <li>Launch the action OnSpawn fo the L2NpcInstance</li>
	 * <li>Increase the current number of L2NpcInstance managed by this L2Spawn</li>
	 * </ul>
	 * @param isSummonSpawn
	 * @return
	 */
	public L2Npc doSpawn(boolean isSummonSpawn)
	{
		L2Npc mob = null;
		try
		{
			// Check if the L2Spawn is not a L2Pet or L2Minion or L2Decoy spawn
			if (_template.isType("L2Pet") || _template.isType("L2Decoy") || _template.isType("L2Trap") || _template.isType("L2EffectPoint"))
			{
				_currentCount++;
				
				return mob;
			}
			
			// Get L2NpcInstance Init parameters and its generate an Identifier
			Object[] parameters =
			{
				IdFactory.getInstance().getNextId(),
				_template
			};
			
			// Call the constructor of the L2NpcInstance
			// (can be a L2ArtefactInstance, L2FriendlyMobInstance, L2GuardInstance, L2MonsterInstance, L2SiegeGuardInstance, L2BoxInstance,
			// L2FeedableBeastInstance, L2TamedBeastInstance, L2FolkInstance, L2TvTEventNpcInstance or L2TvTRoundEventNpcInstance)
			Object tmp = _constructor.newInstance(parameters);
			((L2Object) tmp).setInstanceId(_instanceId); // Must be done before object is spawned into visible world
			if (isSummonSpawn && (tmp instanceof L2Character))
			{
				((L2Character) tmp).setShowSummonAnimation(isSummonSpawn);
			}
			// Check if the Instance is a L2NpcInstance
			if (!(tmp instanceof L2Npc))
			{
				return mob;
			}
			mob = (L2Npc) tmp;
			if (_name != null)
			{
				NpcPersonalAIData.getInstance().initializeNpcParameters(mob, _name);
			}
			return initializeNpcInstance(mob);
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "NPC " + _template.getNpcId() + " class not found", e);
		}
		return mob;
	}
	
	/**
	 * @param mob
	 * @return
	 */
	private L2Npc initializeNpcInstance(L2Npc mob)
	{
		int newlocx, newlocy, newlocz;
		
		// If Locx=0 and Locy=0, the L2NpcInstance must be spawned in an area defined by location
		if ((getLocx() == 0) && (getLocy() == 0))
		{
			if (getLocation() == 0)
			{
				return mob;
			}
			
			// Calculate the random position in the location area
			int p[] = TerritoryTable.getInstance().getRandomPoint(getLocation());
			
			// Set the calculated position of the L2NpcInstance
			newlocx = p[0];
			newlocy = p[1];
			newlocz = GeoData.getInstance().getSpawnHeight(newlocx, newlocy, p[2], p[3], this);
		}
		else
		{
			// The L2NpcInstance is spawned at the exact position (Lox, Locy, Locz)
			newlocx = getLocx();
			newlocy = getLocy();
			if (Config.GEODATA > 0)
			{
				newlocz = GeoData.getInstance().getSpawnHeight(newlocx, newlocy, getLocz(), getLocz(), this);
			}
			else
			{
				newlocz = getLocz();
			}
		}
		
		mob.stopAllEffects();
		
		mob.setIsDead(false);
		// Reset decay info
		mob.setDecayed(false);
		// Set the HP and MP of the L2NpcInstance to the max
		mob.setCurrentHpMp(mob.getMaxHp(), mob.getMaxMp());
		// Set default value
		mob.clearScriptValues();
		
		// Set the heading of the L2NpcInstance (random heading if not defined)
		if (getHeading() == -1)
		{
			mob.setHeading(Rnd.nextInt(61794));
		}
		else
		{
			mob.setHeading(getHeading());
		}
		
		if (mob instanceof L2Attackable)
		{
			((L2Attackable) mob).setChampionType(-1);
		}
		
		// Set champion on next spawn
		if (Config.L2JMOD_CHAMPION_ENABLE && mob.isMonster())
		{
			((L2MonsterInstance) mob).setChampionType(ChampionData.getInstance().calculateChampionType((L2MonsterInstance) mob));
		}
		
		// Link the L2NpcInstance to this L2Spawn
		mob.setSpawn(this);
		
		// Init other values of the L2NpcInstance (ex : from its L2CharTemplate for INT, STR, DEX...) and add it in the world as a visible object
		mob.spawnMe(newlocx, newlocy, newlocz);
		
		L2Spawn.notifyNpcSpawned(mob);
		
		_lastSpawn = mob;
		
		if (Config.DEBUG)
		{
			_log.finest("Spawned Mob Id: " + _template.getNpcId() + " , at: X: " + mob.getX() + " Y: " + mob.getY() + " Z: " + mob.getZ());
		}
		// Increase the current number of L2NpcInstance managed by this L2Spawn
		_currentCount++;
		return mob;
	}
	
	public static void addSpawnListener(SpawnListener listener)
	{
		synchronized (_spawnListeners)
		{
			_spawnListeners.add(listener);
		}
	}
	
	public static void removeSpawnListener(SpawnListener listener)
	{
		synchronized (_spawnListeners)
		{
			_spawnListeners.remove(listener);
		}
	}
	
	public static void notifyNpcSpawned(L2Npc npc)
	{
		synchronized (_spawnListeners)
		{
			for (SpawnListener listener : _spawnListeners)
			{
				listener.npcSpawned(npc);
			}
		}
	}
	
	/**
	 * Set bounds for random calculation and delay for respawn
	 * @param delay delay in seconds
	 * @param randomInterval random interval in seconds
	 */
	public void setRespawnDelay(int delay, int randomInterval)
	{
		if (delay != 0)
		{
			if (delay < 0)
			{
				_log.warning("respawn delay is negative for spawn:" + this);
			}
			
			int minDelay = delay - randomInterval;
			int maxDelay = delay + randomInterval;
			
			_respawnMinDelay = Math.max(10, minDelay) * 1000;
			_respawnMaxDelay = Math.max(10, maxDelay) * 1000;
		}
		
		else
		{
			_respawnMinDelay = 0;
			_respawnMaxDelay = 0;
		}
	}
	
	public void setRespawnDelay(int delay)
	{
		setRespawnDelay(delay, 0);
	}
	
	public int getRespawnDelay()
	{
		return (_respawnMinDelay + _respawnMaxDelay) / 2;
	}
	
	public boolean hasRespawnRandom()
	{
		return _respawnMinDelay != _respawnMaxDelay;
	}
	
	public L2Npc getLastSpawn()
	{
		return _lastSpawn;
	}
	
	/**
	 * @param oldNpc
	 */
	public void respawnNpc(L2Npc oldNpc)
	{
		if (_doRespawn)
		{
			oldNpc.refreshID();
			initializeNpcInstance(oldNpc);
		}
	}
	
	public L2NpcTemplate getTemplate()
	{
		return _template;
	}
	
	public int getInstanceId()
	{
		return _instanceId;
	}
	
	public void setInstanceId(int instanceId)
	{
		_instanceId = instanceId;
	}
	
	public void setOnKillDelay(int delay)
	{
		this._onKillDelay = delay;
	}
	
	@Override
	public String toString()
	{
		return "L2Spawn [_template=" + getNpcid() + ", _locX=" + _locX + ", _locY=" + _locY + ", _locZ=" + _locZ + ", _heading=" + _heading + "]";
	}
	
	public final boolean isNoRndWalk()
	{
		return _isNoRndWalk;
	}
	
	public final void setIsNoRndWalk(boolean value)
	{
		_isNoRndWalk = value;
	}
}
