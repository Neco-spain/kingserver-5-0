package king.server.gameserver.model.entity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import javolution.util.FastMap;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import king.server.Config;
import king.server.gameserver.Announcements;
import king.server.gameserver.ThreadPoolManager;
import king.server.gameserver.datatables.DoorTable;
import king.server.gameserver.datatables.NpcTable;
import king.server.gameserver.idfactory.IdFactory;
import king.server.gameserver.instancemanager.InstanceManager;
import king.server.gameserver.instancemanager.MapRegionManager;
import king.server.gameserver.model.L2Spawn;
import king.server.gameserver.model.L2World;
import king.server.gameserver.model.L2WorldRegion;
import king.server.gameserver.model.Location;
import king.server.gameserver.model.StatsSet;
import king.server.gameserver.model.actor.L2Attackable;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.L2Npc;
import king.server.gameserver.model.actor.instance.L2DoorInstance;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.actor.templates.L2DoorTemplate;
import king.server.gameserver.model.actor.templates.L2NpcTemplate;
import king.server.gameserver.model.instancezone.InstanceWorld;
import king.server.gameserver.model.interfaces.IL2Procedure;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.clientpackets.Say2;
import king.server.gameserver.network.serverpackets.CreatureSay;
import king.server.gameserver.network.serverpackets.L2GameServerPacket;
import king.server.gameserver.network.serverpackets.SystemMessage;
import king.server.util.L2FastList;
import king.server.util.L2FastMap;

public final class Instance
{
	private static final Logger _log = Logger.getLogger(Instance.class.getName());
	
	private final int _id;
	private String _name;
	private int _ejectTime = Config.EJECT_DEAD_PLAYER_TIME;
	/** Allow random walk for NPCs, global parameter. */
	private boolean _allowRandomWalk = true;
	private final L2FastList<Integer> _players = new L2FastList<>(true);
	private final List<L2Npc> _npcs = new L2FastList<>(true);
	private final Map<Integer, L2DoorInstance> _doors = new L2FastMap<>(true);
	private final Map<String, List<L2Spawn>> _manualSpawn = new HashMap<>();
	private Location _spawnLoc = null;
	private boolean _allowSummon = true;
	private long _emptyDestroyTime = -1;
	private long _lastLeft = -1;
	private long _instanceStartTime = -1;
	private long _instanceEndTime = -1;
	private boolean _isPvPInstance = false;
	private boolean _showTimer = false;
	private boolean _isTimerIncrease = true;
	private String _timerText = "";
	
	protected ScheduledFuture<?> _checkTimeUpTask = null;
	protected final Map<Integer, ScheduledFuture<?>> _ejectDeadTasks = new FastMap<>();
	
	public Instance(int id)
	{
		_id = id;
		_instanceStartTime = System.currentTimeMillis();
	}
	
	public Instance(int id, String name)
	{
		_id = id;
		_name = name;
		_instanceStartTime = System.currentTimeMillis();
	}
	
	/**
	 * @return the ID of this instance.
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * @return the name of this instance
	 */
	public String getName()
	{
		return _name;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
	
	/**
	 * @return the eject time
	 */
	public int getEjectTime()
	{
		return _ejectTime;
	}
	
	/**
	 * @param ejectTime the player eject time upon death
	 */
	public void setEjectTime(int ejectTime)
	{
		_ejectTime = ejectTime;
	}
	
	/**
	 * @return whether summon friend type skills are allowed for this instance
	 */
	public boolean isSummonAllowed()
	{
		return _allowSummon;
	}
	
	/**
	 * Sets the status for the instance for summon friend type skills
	 * @param b
	 */
	public void setAllowSummon(boolean b)
	{
		_allowSummon = b;
	}
	
	/**
	 * Returns true if entire instance is PvP zone
	 * @return
	 */
	public boolean isPvPInstance()
	{
		return _isPvPInstance;
	}
	
	/**
	 * Sets PvP zone status of the instance
	 * @param b
	 */
	public void setPvPInstance(boolean b)
	{
		_isPvPInstance = b;
	}
	
	/**
	 * Set the instance duration task
	 * @param duration in milliseconds
	 */
	public void setDuration(int duration)
	{
		if (_checkTimeUpTask != null)
		{
			_checkTimeUpTask.cancel(true);
		}
		
		_checkTimeUpTask = ThreadPoolManager.getInstance().scheduleGeneral(new CheckTimeUp(duration), 500);
		_instanceEndTime = System.currentTimeMillis() + duration + 500;
	}
	
	/**
	 * Set time before empty instance will be removed
	 * @param time in milliseconds
	 */
	public void setEmptyDestroyTime(long time)
	{
		_emptyDestroyTime = time;
	}
	
	/**
	 * Checks if the player exists within this instance
	 * @param objectId
	 * @return true if player exists in instance
	 */
	public boolean containsPlayer(int objectId)
	{
		return _players.contains(objectId);
	}
	
	/**
	 * Adds the specified player to the instance
	 * @param objectId Players object ID
	 */
	public void addPlayer(int objectId)
	{
		_players.add(objectId);
	}
	
	/**
	 * Removes the specified player from the instance list.
	 * @param objectId the player's object Id
	 */
	public void removePlayer(Integer objectId)
	{
		_players.remove(objectId);
		if (_players.isEmpty() && (_emptyDestroyTime >= 0))
		{
			_lastLeft = System.currentTimeMillis();
			setDuration((int) (_instanceEndTime - System.currentTimeMillis() - 500));
		}
	}
	
	public void addNpc(L2Npc npc)
	{
		_npcs.add(npc);
	}
	
	public void removeNpc(L2Npc npc)
	{
		if (npc.getSpawn() != null)
		{
			npc.getSpawn().stopRespawn();
		}
		_npcs.remove(npc);
	}
	
	/**
	 * Adds a door into the instance
	 * @param doorId - from doors.xml
	 * @param set - StatsSet for initializing door
	 */
	public void addDoor(int doorId, StatsSet set)
	{
		if (_doors.containsKey(doorId))
		{
			_log.warning("Door ID " + doorId + " already exists in instance " + getId());
			return;
		}
		
		final L2DoorInstance newdoor = new L2DoorInstance(IdFactory.getInstance().getNextId(), new L2DoorTemplate(set));
		newdoor.setInstanceId(getId());
		newdoor.setCurrentHp(newdoor.getMaxHp());
		newdoor.spawnMe(newdoor.getTemplate().getX(), newdoor.getTemplate().getY(), newdoor.getTemplate().getZ());
		_doors.put(doorId, newdoor);
	}
	
	public List<Integer> getPlayers()
	{
		return _players;
	}
	
	public List<L2Npc> getNpcs()
	{
		return _npcs;
	}
	
	public Collection<L2DoorInstance> getDoors()
	{
		return _doors.values();
	}
	
	public L2DoorInstance getDoor(int id)
	{
		return _doors.get(id);
	}
	
	public long getInstanceEndTime()
	{
		return _instanceEndTime;
	}
	
	public long getInstanceStartTime()
	{
		return _instanceStartTime;
	}
	
	public boolean isShowTimer()
	{
		return _showTimer;
	}
	
	public boolean isTimerIncrease()
	{
		return _isTimerIncrease;
	}
	
	public String getTimerText()
	{
		return _timerText;
	}
	
	/**
	 * @return the spawn location for this instance to be used when leaving the instance
	 */
	public Location getSpawnLoc()
	{
		return _spawnLoc;
	}

	/**
	 * Sets the spawn location for this instance to be used when leaving the instance
	 * @param loc
	 */
	public void setSpawnLoc(Location loc)
	{
		_spawnLoc = loc;
	}

	
	public void removePlayers()
	{
		_players.executeForEach(new EjectProcedure());
		_players.clear();
	}
	
	public void removeNpcs()
	{
		for (L2Npc mob : _npcs)
		{
			if (mob != null)
			{
				if (mob.getSpawn() != null)
				{
					mob.getSpawn().stopRespawn();
				}
				mob.deleteMe();
			}
		}
		_npcs.clear();
		_manualSpawn.clear();
	}
	
	public void removeDoors()
	{
		for (L2DoorInstance door : _doors.values())
		{
			if (door != null)
			{
				L2WorldRegion region = door.getWorldRegion();
				door.decayMe();
				
				if (region != null)
				{
					region.removeVisibleObject(door);
				}
				
				door.getKnownList().removeAllKnownObjects();
				L2World.getInstance().removeObject(door);
			}
		}
		_doors.clear();
	}
	
	/**
	 * Spawns group of instance NPC's
	 * @param groupName - name of group from XML definition to spawn
	 * @return list of spawned NPC's
	 */
	public List<L2Npc> spawnGroup(String groupName)
	{
		List<L2Npc> ret = null;
		if (_manualSpawn.containsKey(groupName))
		{
			final List<L2Spawn> manualSpawn = _manualSpawn.get(groupName);
			ret = new ArrayList<>(manualSpawn.size());
			
			for (L2Spawn spawnDat : manualSpawn)
			{
				ret.add(spawnDat.doSpawn());
			}
		}
		else
		{
			_log.warning(getName() + " instance: cannot spawn NPC's, wrong group name: " + groupName);
		}
		
		return ret;
	}
	
	public void loadInstanceTemplate(String filename)
	{
		Document doc = null;
		File xml = new File(Config.DATAPACK_ROOT, "data/instances/" + filename);
		
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			doc = factory.newDocumentBuilder().parse(xml);
			
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
			{
				if ("instance".equalsIgnoreCase(n.getNodeName()))
				{
					parseInstance(n);
				}
			}
		}
		catch (IOException e)
		{
			_log.log(Level.WARNING, "Instance: can not find " + xml.getAbsolutePath() + " ! " + e.getMessage(), e);
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Instance: error while loading " + xml.getAbsolutePath() + " ! " + e.getMessage(), e);
		}
	}
	
	private void parseInstance(Node n) throws Exception
	{
		L2Spawn spawnDat;
		L2NpcTemplate npcTemplate;
		_name = n.getAttributes().getNamedItem("name").getNodeValue();
		Node a = n.getAttributes().getNamedItem("ejectTime");
		if (a != null)
		{
			_ejectTime = 1000 * Integer.parseInt(a.getNodeValue());
		}
		a = n.getAttributes().getNamedItem("allowRandomWalk");
		if (a != null)
		{
			_allowRandomWalk = Boolean.parseBoolean(a.getNodeValue());
		}
		Node first = n.getFirstChild();
		for (n = first; n != null; n = n.getNextSibling())
		{
			if ("activityTime".equalsIgnoreCase(n.getNodeName()))
			{
				a = n.getAttributes().getNamedItem("val");
				if (a != null)
				{
					_checkTimeUpTask = ThreadPoolManager.getInstance().scheduleGeneral(new CheckTimeUp(Integer.parseInt(a.getNodeValue()) * 60000), 15000);
					_instanceEndTime = System.currentTimeMillis() + (Long.parseLong(a.getNodeValue()) * 60000) + 15000;
				}
			}
			// @formatter:off
			/*
			else if ("timeDelay".equalsIgnoreCase(n.getNodeName()))
			{
				a = n.getAttributes().getNamedItem("val");
				if (a != null)
				{
					instance.setTimeDelay(Integer.parseInt(a.getNodeValue()));
				}
			}
			*/
			// @formatter:on
			else if ("allowSummon".equalsIgnoreCase(n.getNodeName()))
			{
				a = n.getAttributes().getNamedItem("val");
				if (a != null)
				{
					setAllowSummon(Boolean.parseBoolean(a.getNodeValue()));
				}
			}
			else if ("emptyDestroyTime".equalsIgnoreCase(n.getNodeName()))
			{
				a = n.getAttributes().getNamedItem("val");
				if (a != null)
				{
					_emptyDestroyTime = Long.parseLong(a.getNodeValue()) * 1000;
				}
			}
			else if ("showTimer".equalsIgnoreCase(n.getNodeName()))
			{
				a = n.getAttributes().getNamedItem("val");
				if (a != null)
				{
					_showTimer = Boolean.parseBoolean(a.getNodeValue());
				}
				a = n.getAttributes().getNamedItem("increase");
				if (a != null)
				{
					_isTimerIncrease = Boolean.parseBoolean(a.getNodeValue());
				}
				a = n.getAttributes().getNamedItem("text");
				if (a != null)
				{
					_timerText = a.getNodeValue();
				}
			}
			else if ("PvPInstance".equalsIgnoreCase(n.getNodeName()))
			{
				a = n.getAttributes().getNamedItem("val");
				if (a != null)
				{
					setPvPInstance(Boolean.parseBoolean(a.getNodeValue()));
				}
			}
			else if ("doorlist".equalsIgnoreCase(n.getNodeName()))
			{
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					int doorId = 0;
					if ("door".equalsIgnoreCase(d.getNodeName()))
					{
						doorId = Integer.parseInt(d.getAttributes().getNamedItem("doorId").getNodeValue());
						StatsSet set = new StatsSet();
						set.add(DoorTable.getInstance().getDoorTemplate(doorId));
						for (Node bean = d.getFirstChild(); bean != null; bean = bean.getNextSibling())
						{
							if ("set".equalsIgnoreCase(bean.getNodeName()))
							{
								NamedNodeMap attrs = bean.getAttributes();
								String setname = attrs.getNamedItem("name").getNodeValue();
								String value = attrs.getNamedItem("val").getNodeValue();
								set.set(setname, value);
							}
						}
						addDoor(doorId, set);
					}
				}
			}
			else if ("spawnlist".equalsIgnoreCase(n.getNodeName()))
			{
				for (Node group = n.getFirstChild(); group != null; group = group.getNextSibling())
				{
					if ("group".equalsIgnoreCase(group.getNodeName()))
					{
						String spawnGroup = group.getAttributes().getNamedItem("name").getNodeValue();
						List<L2Spawn> manualSpawn = new ArrayList<>();
						for (Node d = group.getFirstChild(); d != null; d = d.getNextSibling())
						{
							int npcId = 0, x = 0, y = 0, z = 0, heading = 0, respawn = 0, respawnRandom = 0, delay = -1;
							Boolean allowRandomWalk = null;
							if ("spawn".equalsIgnoreCase(d.getNodeName()))
							{
								
								npcId = Integer.parseInt(d.getAttributes().getNamedItem("npcId").getNodeValue());
								x = Integer.parseInt(d.getAttributes().getNamedItem("x").getNodeValue());
								y = Integer.parseInt(d.getAttributes().getNamedItem("y").getNodeValue());
								z = Integer.parseInt(d.getAttributes().getNamedItem("z").getNodeValue());
								heading = Integer.parseInt(d.getAttributes().getNamedItem("heading").getNodeValue());
								respawn = Integer.parseInt(d.getAttributes().getNamedItem("respawn").getNodeValue());
								if (d.getAttributes().getNamedItem("onKillDelay") != null)
								{
									delay = Integer.parseInt(d.getAttributes().getNamedItem("onKillDelay").getNodeValue());
								}
								if (d.getAttributes().getNamedItem("respawnRandom") != null)
								{
									respawnRandom = Integer.parseInt(d.getAttributes().getNamedItem("respawnRandom").getNodeValue());
								}
								if (d.getAttributes().getNamedItem("allowRandomWalk") != null)
								{
									allowRandomWalk = Boolean.valueOf(d.getAttributes().getNamedItem("allowRandomWalk").getNodeValue());
								}
								npcTemplate = NpcTable.getInstance().getTemplate(npcId);
								if (npcTemplate != null)
								{
									spawnDat = new L2Spawn(npcTemplate);
									spawnDat.setLocx(x);
									spawnDat.setLocy(y);
									spawnDat.setLocz(z);
									spawnDat.setAmount(1);
									spawnDat.setHeading(heading);
									spawnDat.setRespawnDelay(respawn, respawnRandom);
									if (respawn == 0)
									{
										spawnDat.stopRespawn();
									}
									else
									{
										spawnDat.startRespawn();
									}
									spawnDat.setInstanceId(getId());
									if (allowRandomWalk == null)
									{
										spawnDat.setIsNoRndWalk(!_allowRandomWalk);
									}
									else
									{
										spawnDat.setIsNoRndWalk(!allowRandomWalk);
									}
									if (spawnGroup.equals("general"))
									{
										L2Npc spawned = spawnDat.doSpawn();
										if ((delay >= 0) && (spawned instanceof L2Attackable))
										{
											((L2Attackable) spawned).setOnKillDelay(delay);
										}
									}
									else
									{
										manualSpawn.add(spawnDat);
									}
								}
								else
								{
									_log.warning("Instance: Data missing in NPC table for ID: " + npcId + " in Instance " + getId());
								}
							}
						}
						if (!manualSpawn.isEmpty())
						{
							_manualSpawn.put(spawnGroup, manualSpawn);
						}
					}
				}
			}
			else if ("spawnpoint".equalsIgnoreCase(n.getNodeName()))
			{
				try
				{
					int x = Integer.parseInt(n.getAttributes().getNamedItem("spawnX").getNodeValue());
					int y = Integer.parseInt(n.getAttributes().getNamedItem("spawnY").getNodeValue());
					int z = Integer.parseInt(n.getAttributes().getNamedItem("spawnZ").getNodeValue());
					_spawnLoc = new Location(x, y, z);
				}
				catch (Exception e)
				{
					_log.log(Level.WARNING, "Error parsing instance xml: " + e.getMessage(), e);
					_spawnLoc = null;
				}
			}
		}
	}
	
	protected void doCheckTimeUp(int remaining)
	{
		CreatureSay cs = null;
		int timeLeft;
		int interval;
		
		if (_players.isEmpty() && (_emptyDestroyTime == 0))
		{
			remaining = 0;
			interval = 500;
		}
		else if (_players.isEmpty() && (_emptyDestroyTime > 0))
		{
			
			Long emptyTimeLeft = (_lastLeft + _emptyDestroyTime) - System.currentTimeMillis();
			if (emptyTimeLeft <= 0)
			{
				interval = 0;
				remaining = 0;
			}
			else if ((remaining > 300000) && (emptyTimeLeft > 300000))
			{
				interval = 300000;
				remaining = remaining - 300000;
			}
			else if ((remaining > 60000) && (emptyTimeLeft > 60000))
			{
				interval = 60000;
				remaining = remaining - 60000;
			}
			else if ((remaining > 30000) && (emptyTimeLeft > 30000))
			{
				interval = 30000;
				remaining = remaining - 30000;
			}
			else
			{
				interval = 10000;
				remaining = remaining - 10000;
			}
		}
		else if (remaining > 300000)
		{
			timeLeft = remaining / 60000;
			interval = 300000;
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.DUNGEON_EXPIRES_IN_S1_MINUTES);
			sm.addString(Integer.toString(timeLeft));
			Announcements.getInstance().announceToInstance(sm, getId());
			remaining = remaining - 300000;
		}
		else if (remaining > 60000)
		{
			timeLeft = remaining / 60000;
			interval = 60000;
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.DUNGEON_EXPIRES_IN_S1_MINUTES);
			sm.addString(Integer.toString(timeLeft));
			Announcements.getInstance().announceToInstance(sm, getId());
			remaining = remaining - 60000;
		}
		else if (remaining > 30000)
		{
			timeLeft = remaining / 1000;
			interval = 30000;
			cs = new CreatureSay(0, Say2.ALLIANCE, "Notice", timeLeft + " seconds left.");
			remaining = remaining - 30000;
		}
		else
		{
			timeLeft = remaining / 1000;
			interval = 10000;
			cs = new CreatureSay(0, Say2.ALLIANCE, "Notice", timeLeft + " seconds left.");
			remaining = remaining - 10000;
		}
		if (cs != null)
		{
			_players.executeForEach(new BroadcastPacket(cs));
		}
		cancelTimer();
		if (remaining >= 10000)
		{
			_checkTimeUpTask = ThreadPoolManager.getInstance().scheduleGeneral(new CheckTimeUp(remaining), interval);
		}
		else
		{
			_checkTimeUpTask = ThreadPoolManager.getInstance().scheduleGeneral(new TimeUp(), interval);
		}
	}
	
	public void cancelTimer()
	{
		if (_checkTimeUpTask != null)
		{
			_checkTimeUpTask.cancel(true);
		}
	}
	
	public void cancelEjectDeadPlayer(L2PcInstance player)
	{
		if (_ejectDeadTasks.containsKey(player.getObjectId()))
		{
			final ScheduledFuture<?> task = _ejectDeadTasks.remove(player.getObjectId());
			if (task != null)
			{
				task.cancel(true);
			}
		}
	}
	
	public void addEjectDeadTask(L2PcInstance player)
	{
		if ((player != null))
		{
			_ejectDeadTasks.put(player.getObjectId(), ThreadPoolManager.getInstance().scheduleGeneral(new EjectPlayer(player), _ejectTime));
		}
	}
	
	/**
	 * @param killer the character that killed the {@code victim}
	 * @param victim the character that was killed by the {@code killer}
	 */
	public final void notifyDeath(L2Character killer, L2Character victim)
	{
		final InstanceWorld instance = InstanceManager.getInstance().getPlayerWorld(victim.getActingPlayer());
		if (instance != null)
		{
			instance.onDeath(killer, victim);
		}
	}
	
	public class CheckTimeUp implements Runnable
	{
		private final int _remaining;
		
		public CheckTimeUp(int remaining)
		{
			_remaining = remaining;
		}
		
		@Override
		public void run()
		{
			doCheckTimeUp(_remaining);
		}
	}
	
	public class TimeUp implements Runnable
	{
		@Override
		public void run()
		{
			InstanceManager.getInstance().destroyInstance(getId());
		}
	}
	
	protected class EjectPlayer implements Runnable
	{
		private final L2PcInstance _player;
		
		public EjectPlayer(L2PcInstance player)
		{
			_player = player;
		}
		
		@Override
		public void run()
		{
			if ((_player != null) && _player.isDead() && (_player.getInstanceId() == getId()))
			{
				_player.setInstanceId(0);
				if (getSpawnLoc() != null)
				{
					_player.teleToLocation(getSpawnLoc(), true);
				}
				else
				{
					_player.teleToLocation(MapRegionManager.TeleportWhereType.Town);
				}
			}
		}
	}
	
	public final class EjectProcedure implements IL2Procedure<Integer>
	{
		@Override
		public boolean execute(Integer objectId)
		{
			final L2PcInstance player = L2World.getInstance().getPlayer(objectId);
			if ((player != null) && (player.getInstanceId() == getId()))
			{
				player.setInstanceId(0);
				if (getSpawnLoc() != null)
				{
					player.teleToLocation(getSpawnLoc(), true);
				}
				else
				{
					player.teleToLocation(MapRegionManager.TeleportWhereType.Town);
				}
			}
			return true;
		}
	}
	
	public final class BroadcastPacket implements IL2Procedure<Integer>
	{
		private final L2GameServerPacket _packet;
		
		public BroadcastPacket(L2GameServerPacket packet)
		{
			_packet = packet;
		}
		
		@Override
		public boolean execute(Integer objectId)
		{
			final L2PcInstance player = L2World.getInstance().getPlayer(objectId);
			if ((player != null) && (player.getInstanceId() == getId()))
			{
				player.sendPacket(_packet);
			}
			return true;
		}
	}
}