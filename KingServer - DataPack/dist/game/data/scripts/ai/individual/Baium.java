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
package ai.individual;

import static king.server.gameserver.ai.CtrlIntention.AI_INTENTION_FOLLOW;
import static king.server.gameserver.ai.CtrlIntention.AI_INTENTION_IDLE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import javolution.util.FastList;
import ai.npc.AbstractNpcAI;

import king.server.Config;
import king.server.gameserver.GeoData;
import king.server.gameserver.ThreadPoolManager;
import king.server.gameserver.datatables.SkillTable;
import king.server.gameserver.instancemanager.GrandBossManager;
import king.server.gameserver.model.L2Object;
import king.server.gameserver.model.Location;
import king.server.gameserver.model.StatsSet;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.L2Npc;
import king.server.gameserver.model.actor.instance.L2DecoyInstance;
import king.server.gameserver.model.actor.instance.L2GrandBossInstance;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.effects.L2Effect;
import king.server.gameserver.model.quest.QuestTimer;
import king.server.gameserver.model.skills.L2Skill;
import king.server.gameserver.model.zone.type.L2BossZone;
import king.server.gameserver.network.serverpackets.Earthquake;
import king.server.gameserver.network.serverpackets.MoveToPawn;
import king.server.gameserver.network.serverpackets.PlaySound;
import king.server.gameserver.util.Util;

/**
 * Baium's AI.<br>
 * Note1: if the server gets rebooted while players are still fighting Baium, there is no lock, but players also lose their ability to wake Baium up.<br>
 * However, should another person enter the room and wake him up, the players who had stayed inside may join the raid.<br>
 * This can be helpful for players who became victims of a reboot (they only need 1 new player to enter and wake up Baium) and is not too exploitable since any player wishing to exploit it would have to suffer 5 days of being parked in an empty room.<br>
 * Note2: Neither version of Baium should be a permanent spawn.<br>
 * This script is fully capable of spawning the statue-version when the lock expires and switching it to the mob version promptly.<br>
 * Additional notes ( source http://aleenaresron.blogspot.com/2006_08_01_archive.html ):
 * <ul>
 * <li>Baium only first respawns five days after his last death. And from those five days he will respawn within 1-8 hours of his last death. So, you have to know his last time of death.</li>
 * <li>If by some freak chance you are the only one in Baium's chamber and NO ONE comes in [ha, ha] you or someone else will have to wake Baium.<br>
 * There is a good chance that Baium will automatically kill whoever wakes him.<br>
 * There are some people that have been able to wake him and not die, however if you've already gone through the trouble of getting the bloody fabric and camped him out and researched his spawn time,<br>
 * are you willing to take that chance that you'll wake him and not be able to finish your quest? Doubtful. [ this powerful attack vs the player who wakes him up is NOT yet implemented here]</li>
 * <li>Once someone starts attacking Baium no one else can port into the chamber where he is. Unlike with the other raid bosses, you can just show up at any time as long as you are there when they die.<br>
 * Not true with Baium. Once he gets attacked, the port to Baium closes. byebye, see you in 5 days. If nobody attacks Baium for 30 minutes, he auto-despawns and unlocks the vortex.</li>
 * @author Fulminus version 0.1
 */
public class Baium extends AbstractNpcAI
{
	// NPCs
	private static final int STONE_BAIUM = 29025;
	private static final int ANGELIC_VORTEX = 31862;
	private static final int LIVE_BAIUM = 29020;
	private static final int ARCHANGEL = 29021;
	private static final int TELEPORT_CUBIC = 31842;
	
	// Item
	private static final int BLOODED_FABRIC = 4295;
	
	// Baium status tracking
	private static final byte ASLEEP = 0; // baium is in the stone version, waiting to be woken up. Entry is unlocked
	private static final byte AWAKE = 1; // baium is awake and fighting. Entry is locked.
	private static final byte DEAD = 2; // baium has been killed and has not yet spawned. Entry is locked
	
	// fixed archangel spawnloc
	private final static Location[] ANGEL_LOCATION =
	{
		new Location(114239, 17168, 10080, 63544),
		new Location(115780, 15564, 10080, 13620),
		new Location(114880, 16236, 10080, 5400),
		new Location(115168, 17200, 10080, 0),
		new Location(115792, 16608, 10080, 0)
	};
	
	private long _LastAttackVsBaiumTime = 0;
	protected final List<L2Npc> _Minions = new ArrayList<>(5);
	private L2BossZone _Zone;
	
	private L2Character _target;
	private L2Skill _skill;
	
	private Baium(String name, String descr)
	{
		super(name, descr);
		registerMobs(LIVE_BAIUM);
		
		// Quest NPC starter initialization
		addStartNpc(STONE_BAIUM, ANGELIC_VORTEX, TELEPORT_CUBIC);
		addTalkId(STONE_BAIUM, ANGELIC_VORTEX, TELEPORT_CUBIC);
		
		_Zone = GrandBossManager.getInstance().getZone(113100, 14500, 10077);
		StatsSet info = GrandBossManager.getInstance().getStatsSet(LIVE_BAIUM);
		int status = GrandBossManager.getInstance().getBossStatus(LIVE_BAIUM);
		if (status == DEAD)
		{
			// load the unlock date and time for baium from DB
			long temp = (info.getLong("respawn_time") - System.currentTimeMillis());
			if (temp > 0)
			{
				// the unlock time has not yet expired. Mark Baium as currently locked (dead). Setup a timer
				// to fire at the correct time (calculate the time between now and the unlock time,
				// setup a timer to fire after that many msec)
				startQuestTimer("baium_unlock", temp, null, null);
			}
			else
			{
				// the time has already expired while the server was offline. Delete the saved time and
				// immediately spawn the stone-baium. Also the state need not be changed from ASLEEP
				addSpawn(STONE_BAIUM, 116033, 17447, 10107, -25348, false, 0);
				GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM, ASLEEP);
			}
		}
		else if (status == AWAKE)
		{
			int loc_x = info.getInteger("loc_x");
			int loc_y = info.getInteger("loc_y");
			int loc_z = info.getInteger("loc_z");
			int heading = info.getInteger("heading");
			final int hp = info.getInteger("currentHP");
			final int mp = info.getInteger("currentMP");
			L2GrandBossInstance baium = (L2GrandBossInstance) addSpawn(LIVE_BAIUM, loc_x, loc_y, loc_z, heading, false, 0);
			GrandBossManager.getInstance().addBoss(baium);
			final L2Npc _baium = baium;
			ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						_baium.setCurrentHpMp(hp, mp);
						_baium.setIsInvul(true);
						_baium.setIsImmobilized(true);
						_baium.setRunning();
						_baium.broadcastSocialAction(2);
						startQuestTimer("baium_wakeup", 15000, _baium, null);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}, 100L);
		}
		else
		{
			addSpawn(STONE_BAIUM, 116033, 17447, 10107, -25348, false, 0);
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("baium_unlock"))
		{
			GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM, ASLEEP);
			addSpawn(STONE_BAIUM, 116033, 17447, 10107, -25348, false, 0);
		}
		else if (event.equalsIgnoreCase("skill_range") && (npc != null))
		{
			callSkillAI(npc);
		}
		else if (event.equalsIgnoreCase("clean_player"))
		{
			_target = getRandomTarget(npc);
		}
		else if (event.equalsIgnoreCase("baium_wakeup") && (npc != null))
		{
			if (npc.getNpcId() == LIVE_BAIUM)
			{
				npc.broadcastSocialAction(1);
				npc.broadcastPacket(new Earthquake(npc.getX(), npc.getY(), npc.getZ(), 40, 5));
				// start monitoring baium's inactivity
				_LastAttackVsBaiumTime = System.currentTimeMillis();
				startQuestTimer("baium_despawn", 60000, npc, null, true);
				startQuestTimer("skill_range", 500, npc, null, true);
				final L2Npc baium = npc;
				ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							baium.setIsInvul(false);
							baium.setIsImmobilized(false);
							for (L2Npc minion : _Minions)
							{
								minion.setShowSummonAnimation(false);
							}
						}
						catch (Exception e)
						{
							_log.log(Level.WARNING, "", e);
						}
					}
				}, 11100L);
				// TODO: the person who woke baium up should be knocked across the room, onto a wall, and
				// lose massive amounts of HP.
				for (Location element : ANGEL_LOCATION)
				{
					L2Npc angel = addSpawn(ARCHANGEL, element, false, 0, true);
					angel.setIsInvul(false);
					_Minions.add(angel);
				}
			}
			// despawn the live baium after 30 minutes of inactivity
			// also check if the players are cheating, having pulled Baium outside his zone...
		}
		else if (event.equalsIgnoreCase("baium_despawn") && (npc != null))
		{
			if (npc.getNpcId() == LIVE_BAIUM)
			{
				// just in case the zone reference has been lost (somehow...), restore the reference
				if (_Zone == null)
				{
					_Zone = GrandBossManager.getInstance().getZone(113100, 14500, 10077);
				}
				if ((_LastAttackVsBaiumTime + 1800000) < System.currentTimeMillis())
				{
					npc.deleteMe(); // despawn the live-baium
					for (L2Npc minion : _Minions)
					{
						if (minion != null)
						{
							minion.getSpawn().stopRespawn();
							minion.deleteMe();
						}
					}
					_Minions.clear();
					addSpawn(STONE_BAIUM, 116033, 17447, 10107, -25348, false, 0); // spawn stone-baium
					GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM, ASLEEP); // mark that Baium is not awake any more
					_Zone.oustAllPlayers();
					cancelQuestTimer("baium_despawn", npc, null);
				}
				else if (((_LastAttackVsBaiumTime + 300000) < System.currentTimeMillis()) && (npc.getCurrentHp() < ((npc.getMaxHp() * 3) / 4.0)))
				{
					npc.setIsCastingNow(false); // just in case
					npc.setTarget(npc);
					L2Skill skill = SkillTable.getInstance().getInfo(4135, 1);
					if (skill.isMagic())
					{
						if (npc.isMuted())
						{
							return super.onAdvEvent(event, npc, player);
						}
					}
					else
					{
						if (npc.isPhysicalMuted())
						{
							return super.onAdvEvent(event, npc, player);
						}
					}
					npc.doCast(skill);
					npc.setIsCastingNow(true);
				}
				else if (!_Zone.isInsideZone(npc))
				{
					npc.teleToLocation(116033, 17447, 10104);
				}
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		int npcId = npc.getNpcId();
		String htmltext = "";
		if (_Zone == null)
		{
			_Zone = GrandBossManager.getInstance().getZone(113100, 14500, 10077);
		}
		if (_Zone == null)
		{
			return "<html><body>Angelic Vortex:<br>You may not enter while admin disabled this zone</body></html>";
		}
		if ((npcId == STONE_BAIUM) && (GrandBossManager.getInstance().getBossStatus(LIVE_BAIUM) == ASLEEP))
		{
			if (_Zone.isPlayerAllowed(player))
			{
				// once Baium is awaken, no more people may enter until he dies, the server reboots, or
				// 30 minutes pass with no attacks made against Baium.
				GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM, AWAKE);
				npc.deleteMe();
				L2GrandBossInstance baium = (L2GrandBossInstance) addSpawn(LIVE_BAIUM, npc, true);
				GrandBossManager.getInstance().addBoss(baium);
				final L2Npc _baium = baium;
				ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							_baium.setIsInvul(true);
							_baium.setRunning();
							_baium.broadcastSocialAction(2);
							startQuestTimer("baium_wakeup", 15000, _baium, null);
							_baium.setShowSummonAnimation(false);
						}
						catch (Throwable e)
						{
							_log.log(Level.WARNING, "", e);
						}
					}
				}, 100L);
			}
			else
			{
				htmltext = "Conditions are not right to wake up Baium";
			}
		}
		else if (npcId == ANGELIC_VORTEX)
		{
			if (player.isFlying())
			{
				// print "Player "+player.getName()+" attempted to enter Baium's lair while flying!";
				return "<html><body>Angelic Vortex:<br>You may not enter while flying a wyvern</body></html>";
			}
			
			if ((GrandBossManager.getInstance().getBossStatus(LIVE_BAIUM) == ASLEEP) && hasQuestItems(player, BLOODED_FABRIC))
			{
				takeItems(player, BLOODED_FABRIC, 1);
				// allow entry for the player for the next 30 secs (more than enough time for the TP to happen)
				// Note: this just means 30secs to get in, no limits on how long it takes before we get out.
				_Zone.allowPlayerEntry(player, 30);
				player.teleToLocation(113100, 14500, 10077);
			}
			else
			{
				npc.showChatWindow(player, 1);
			}
		}
		else if (npc.getNpcId() == TELEPORT_CUBIC)
		{
			int chance = getRandom(3);
			int x, y, z;
			
			switch (chance)
			{
				case 0:
					x = 108784 + getRandom(100);
					y = 16000 + getRandom(100);
					z = -4928;
					break;
				
				case 1:
					x = 113824 + getRandom(100);
					y = 10448 + getRandom(100);
					z = -5164;
					break;
				
				default:
					x = 115488 + getRandom(100);
					y = 22096 + getRandom(100);
					z = -5168;
					break;
			}
			
			player.teleToLocation(x, y, z);
		}
		return htmltext;
	}
	
	@Override
	public String onSpellFinished(L2Npc npc, L2PcInstance player, L2Skill skill)
	{
		if (npc.isInvul())
		{
			npc.getAI().setIntention(AI_INTENTION_IDLE);
			return null;
		}
		else if ((npc.getNpcId() == LIVE_BAIUM) && !npc.isInvul())
		{
			callSkillAI(npc);
		}
		return super.onSpellFinished(npc, player, skill);
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		npc.disableCoreAI(true);
		return super.onSpawn(npc);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon)
	{
		if (!_Zone.isInsideZone(attacker))
		{
			attacker.reduceCurrentHp(attacker.getCurrentHp(), attacker, false, false, null);
			return super.onAttack(npc, attacker, damage, isSummon);
		}
		if (npc.isInvul())
		{
			npc.getAI().setIntention(AI_INTENTION_IDLE);
			return super.onAttack(npc, attacker, damage, isSummon);
		}
		else if ((npc.getNpcId() == LIVE_BAIUM) && !npc.isInvul())
		{
			if (attacker.getMountType() == 1)
			{
				int sk_4258 = 0;
				L2Effect[] effects = attacker.getAllEffects();
				if ((effects != null) && (effects.length != 0))
				{
					for (L2Effect e : effects)
					{
						if (e.getSkill().getId() == 4258)
						{
							sk_4258 = 1;
						}
					}
				}
				if (sk_4258 == 0)
				{
					npc.setTarget(attacker);
					L2Skill skill = SkillTable.getInstance().getInfo(4258, 1);
					if (skill.isMagic())
					{
						if (npc.isMuted())
						{
							return super.onAttack(npc, attacker, damage, isSummon);
						}
					}
					else
					{
						if (npc.isPhysicalMuted())
						{
							return super.onAttack(npc, attacker, damage, isSummon);
						}
					}
					npc.doCast(skill);
				}
			}
			// update a variable with the last action against baium
			_LastAttackVsBaiumTime = System.currentTimeMillis();
			callSkillAI(npc);
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		cancelQuestTimer("baium_despawn", npc, null);
		npc.broadcastPacket(new PlaySound(1, "BS01_D", 1, npc.getObjectId(), npc.getX(), npc.getY(), npc.getZ()));
		// spawn the "Teleportation Cubic" for 15 minutes (to allow players to exit the lair)
		addSpawn(TELEPORT_CUBIC, 115017, 15549, 10090, 0, false, 900000);
		// Calculate Min and Max respawn times randomly.
		long respawnTime = getRandom((Config.Interval_Of_Baium_Spawn - Config.Random_Of_Baium_Spawn), (Config.Interval_Of_Baium_Spawn + Config.Random_Of_Baium_Spawn));
		GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM, DEAD);
		startQuestTimer("baium_unlock", respawnTime, null, null);
		// also save the respawn time so that the info is maintained past reboots
		StatsSet info = GrandBossManager.getInstance().getStatsSet(LIVE_BAIUM);
		info.set("respawn_time", (System.currentTimeMillis()) + respawnTime);
		GrandBossManager.getInstance().setStatsSet(LIVE_BAIUM, info);
		for (L2Npc minion : _Minions)
		{
			if (minion != null)
			{
				minion.getSpawn().stopRespawn();
				minion.deleteMe();
			}
		}
		_Minions.clear();
		final QuestTimer timer = getQuestTimer("skill_range", npc, null);
		if (timer != null)
		{
			timer.cancelAndRemove();
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public L2Character getRandomTarget(L2Npc npc)
	{
		FastList<L2Character> result = FastList.newInstance();
		Collection<L2Object> objs = npc.getKnownList().getKnownObjects().values();
		{
			for (L2Object obj : objs)
			{
				if (obj.isPlayable() || (obj instanceof L2DecoyInstance))
				{
					if (obj.isPlayer())
					{
						if (obj.getActingPlayer().getAppearance().getInvisible())
						{
							continue;
						}
					}
					
					if (((((L2Character) obj).getZ() < (npc.getZ() - 100)) && (((L2Character) obj).getZ() > (npc.getZ() + 100))) || !(GeoData.getInstance().canSeeTarget(((L2Character) obj).getX(), ((L2Character) obj).getY(), ((L2Character) obj).getZ(), npc.getX(), npc.getY(), npc.getZ())))
					{
						continue;
					}
				}
				if (obj.isPlayable() || (obj instanceof L2DecoyInstance))
				{
					if (Util.checkIfInRange(9000, npc, obj, true) && !((L2Character) obj).isDead())
					{
						result.add((L2Character) obj);
					}
				}
			}
		}
		if (result.isEmpty())
		{
			for (L2Npc minion : _Minions)
			{
				if (minion != null)
				{
					result.add(minion);
				}
			}
		}
		
		if (result.isEmpty())
		{
			FastList.recycle(result);
			return null;
		}
		
		Object[] characters = result.toArray();
		QuestTimer timer = getQuestTimer("clean_player", npc, null);
		if (timer != null)
		{
			timer.cancelAndRemove();
		}
		startQuestTimer("clean_player", 20000, npc, null);
		L2Character target = (L2Character) characters[getRandom(characters.length)];
		FastList.recycle(result);
		return target;
		
	}
	
	public synchronized void callSkillAI(L2Npc npc)
	{
		if (npc.isInvul() || npc.isCastingNow())
		{
			return;
		}
		
		if ((_target == null) || _target.isDead() || !(_Zone.isInsideZone(_target)))
		{
			_target = getRandomTarget(npc);
			if (_target != null)
			{
				_skill = SkillTable.getInstance().getInfo(getRandomSkill(npc), 1);
			}
		}
		
		L2Character target = _target;
		L2Skill skill = _skill;
		if (skill == null)
		{
			skill = SkillTable.getInstance().getInfo(getRandomSkill(npc), 1);
		}
		
		if (skill.isMagic())
		{
			if (npc.isMuted())
			{
				return;
			}
		}
		else
		{
			if (npc.isPhysicalMuted())
			{
				return;
			}
		}
		
		if ((target == null) || target.isDead() || !(_Zone.isInsideZone(target)))
		{
			npc.setIsCastingNow(false);
			return;
		}
		
		if (Util.checkIfInRange(skill.getCastRange(), npc, target, true))
		{
			npc.getAI().setIntention(AI_INTENTION_IDLE);
			npc.setTarget(target);
			npc.setIsCastingNow(true);
			_target = null;
			_skill = null;
			if (getDist(skill.getCastRange()) > 0)
			{
				npc.broadcastPacket(new MoveToPawn(npc, target, getDist(skill.getCastRange())));
			}
			try
			{
				Thread.sleep(1000);
				npc.stopMove(null);
				npc.doCast(skill);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			npc.getAI().setIntention(AI_INTENTION_FOLLOW, target, null);
			npc.setIsCastingNow(false);
		}
	}
	
	public int getRandomSkill(L2Npc npc)
	{
		int skill;
		if (npc.getCurrentHp() > ((npc.getMaxHp() * 3) / 4.0))
		{
			if (getRandom(100) < 10)
			{
				skill = 4128;
			}
			else if (getRandom(100) < 10)
			{
				skill = 4129;
			}
			else
			{
				skill = 4127;
			}
		}
		else if (npc.getCurrentHp() > ((npc.getMaxHp() * 2) / 4.0))
		{
			if (getRandom(100) < 10)
			{
				skill = 4131;
			}
			else if (getRandom(100) < 10)
			{
				skill = 4128;
			}
			else if (getRandom(100) < 10)
			{
				skill = 4129;
			}
			else
			{
				skill = 4127;
			}
		}
		else if (npc.getCurrentHp() > ((npc.getMaxHp() * 1) / 4.0))
		{
			if (getRandom(100) < 10)
			{
				skill = 4130;
			}
			else if (getRandom(100) < 10)
			{
				skill = 4131;
			}
			else if (getRandom(100) < 10)
			{
				skill = 4128;
			}
			else if (getRandom(100) < 10)
			{
				skill = 4129;
			}
			else
			{
				skill = 4127;
			}
		}
		else if (getRandom(100) < 10)
		{
			skill = 4130;
		}
		else if (getRandom(100) < 10)
		{
			skill = 4131;
		}
		else if (getRandom(100) < 10)
		{
			skill = 4128;
		}
		else if (getRandom(100) < 10)
		{
			skill = 4129;
		}
		else
		{
			skill = 4127;
		}
		return skill;
	}
	
	@Override
	public String onSkillSee(L2Npc npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isSummon)
	{
		if (npc.isInvul())
		{
			npc.getAI().setIntention(AI_INTENTION_IDLE);
			return null;
		}
		npc.setTarget(caster);
		return super.onSkillSee(npc, caster, skill, targets, isSummon);
	}
	
	public int getDist(int range)
	{
		int dist = 0;
		switch (range)
		{
			case -1:
				break;
			case 100:
				dist = 85;
				break;
			default:
				dist = range - 85;
				break;
		}
		return dist;
	}
	
	public static void main(String[] args)
	{
		new Baium(Baium.class.getSimpleName(), "ai");
	}
}
