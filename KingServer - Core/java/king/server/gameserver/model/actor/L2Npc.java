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
package king.server.gameserver.model.actor;

import static king.server.gameserver.ai.CtrlIntention.AI_INTENTION_ACTIVE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import king.server.Config;
import king.server.gameserver.SevenSigns;
import king.server.gameserver.SevenSignsFestival;
import king.server.gameserver.ThreadPoolManager;
import king.server.gameserver.cache.HtmCache;
import king.server.gameserver.datatables.FakePcsTable;
import king.server.gameserver.datatables.ItemTable;
import king.server.gameserver.events.EventsInterface;
import king.server.gameserver.datatables.NpcPersonalAIData;
import king.server.gameserver.handler.BypassHandler;
import king.server.gameserver.handler.IBypassHandler;
import king.server.gameserver.instancemanager.CHSiegeManager;
import king.server.gameserver.instancemanager.CastleManager;
import king.server.gameserver.instancemanager.FortManager;
import king.server.gameserver.instancemanager.TownManager;
import king.server.gameserver.instancemanager.WalkingManager;
import king.server.gameserver.model.L2NpcAIData;
import king.server.gameserver.model.L2Object;
import king.server.gameserver.model.L2Spawn;
import king.server.gameserver.model.L2World;
import king.server.gameserver.model.L2WorldRegion;
import king.server.gameserver.model.ShotType;
import king.server.gameserver.model.actor.instance.L2ClanHallManagerInstance;
import king.server.gameserver.model.actor.instance.L2DoormenInstance;
import king.server.gameserver.model.actor.instance.L2FestivalGuideInstance;
import king.server.gameserver.model.actor.instance.L2FishermanInstance;
import king.server.gameserver.model.actor.instance.L2MerchantInstance;
import king.server.gameserver.model.actor.instance.L2NpcInstance;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.actor.instance.L2TeleporterInstance;
import king.server.gameserver.model.actor.instance.L2TrainerHealersInstance;
import king.server.gameserver.model.actor.instance.L2TrainerInstance;
import king.server.gameserver.model.actor.instance.L2WarehouseInstance;
import king.server.gameserver.model.actor.knownlist.NpcKnownList;
import king.server.gameserver.model.actor.stat.NpcStat;
import king.server.gameserver.model.actor.status.NpcStatus;
import king.server.gameserver.model.actor.templates.L2NpcTemplate;
import king.server.gameserver.model.actor.templates.L2NpcTemplate.AIType;
import king.server.gameserver.model.entity.Castle;
import king.server.gameserver.model.entity.Fort;
import king.server.gameserver.model.entity.clanhall.SiegableHall;
import king.server.gameserver.model.items.L2Item;
import king.server.gameserver.model.items.L2Weapon;
import king.server.gameserver.model.items.instance.L2ItemInstance;
import king.server.gameserver.model.olympiad.Olympiad;
import king.server.gameserver.model.quest.Quest;
import king.server.gameserver.model.quest.Quest.QuestEventType;
import king.server.gameserver.model.skills.L2Skill;
import king.server.gameserver.model.skills.targets.L2TargetType;
import king.server.gameserver.model.zone.type.L2TownZone;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.serverpackets.AbstractNpcInfo;
import king.server.gameserver.network.serverpackets.ActionFailed;
import king.server.gameserver.network.serverpackets.ExChangeNpcState;
import king.server.gameserver.network.serverpackets.MagicSkillUse;
import king.server.gameserver.network.serverpackets.NpcHtmlMessage;
import king.server.gameserver.network.serverpackets.NpcSay;
import king.server.gameserver.network.serverpackets.ServerObjectInfo;
import king.server.gameserver.network.serverpackets.SocialAction;
import king.server.gameserver.taskmanager.DecayTaskManager;
import king.server.gameserver.util.Broadcast;
import king.server.util.Rnd;
import king.server.util.StringUtil;

/**
 * This class represents a Non-Player-Character in the world.<br>
 * It can be a monster or a friendly character.<br>
 * It also uses a template to fetch some static values.<br>
 * The templates are hardcoded in the client, so we can rely on them.<br>
 * L2Npc:
 * <ul>
 * <li>L2Attackable</li>
 * <li>L2BoxInstance</li>
 * </ul>
 */
public class L2Npc extends L2Character
{
	/** The interaction distance of the L2NpcInstance(is used as offset in MovetoLocation method) */
	public static final int INTERACTION_DISTANCE = 150;
	
	/** The L2Spawn object that manage this L2NpcInstance */
	private L2Spawn _spawn;
	
	/** The flag to specify if this L2NpcInstance is busy */
	private boolean _isBusy = false;
	
	/** The busy message for this L2NpcInstance */
	private String _busyMessage = "";
	
	/** True if endDecayTask has already been called */
	volatile boolean _isDecayed = false;
	
	/** The castle index in the array of L2Castle this L2NpcInstance belongs to */
	private int _castleIndex = -2;
	
	/** The fortress index in the array of L2Fort this L2NpcInstance belongs to */
	private int _fortIndex = -2;
	
	private boolean _eventMob = false;
	private boolean _isInTown = false;
	
	/** True if this L2Npc is autoattackable **/
	private boolean _isAutoAttackable = false;
	
	/** Time of last social packet broadcast */
	private long _lastSocialBroadcast = 0;
	
	/** Minimum interval between social packets */
	private final int _minimalSocialInterval = 6000;
	
	/** Support for random animation switching */
	private boolean _isRandomAnimationEnabled = true;
	
	protected RandomAnimationTask _rAniTask = null;
	private int _currentLHandId; // normally this shouldn't change from the template, but there exist exceptions
	private int _currentRHandId; // normally this shouldn't change from the template, but there exist exceptions
	private int _currentEnchant; // normally this shouldn't change from the template, but there exist exceptions
	private double _currentCollisionHeight; // used for npc grow effect skills
	private double _currentCollisionRadius; // used for npc grow effect skills
	private boolean _blocked;
	private int _soulshotamount = 0;
	private int _spiritshotamount = 0;
	private int _displayEffect = 0;
	private Map<Integer, Integer> _scriptVal;
	private FakePc _fakePc = null;
	
	/** The character that summons this NPC. */
	private L2Character _summoner = null;
	
	private final L2NpcAIData _staticAIData = getTemplate().getAIDataStatic();
	
	private int _shotsMask = 0;
	
	public int getSoulShotChance()
	{
		return _staticAIData.getSoulShotChance();
	}
	
	public int getSpiritShotChance()
	{
		return _staticAIData.getSpiritShotChance();
	}
	
	public int getEnemyRange()
	{
		return _staticAIData.getEnemyRange();
	}
	
	public String getEnemyClan()
	{
		return _staticAIData.getEnemyClan();
	}
	
	public int getClanRange()
	{
		return _staticAIData.getClanRange();
	}
	
	public String getClan()
	{
		return _staticAIData.getClan();
	}
	
	/**
	 * @return the primary attack.
	 */
	public int getPrimarySkillId()
	{
		return _staticAIData.getPrimarySkillId();
	}
	
	public int getMinSkillChance()
	{
		return _staticAIData.getMinSkillChance();
	}
	
	public int getMaxSkillChance()
	{
		return _staticAIData.getMaxSkillChance();
	}
	
	public int getCanMove()
	{
		return _staticAIData.getCanMove();
	}
	
	public int getIsChaos()
	{
		return _staticAIData.getIsChaos();
	}
	
	public int getCanDodge()
	{
		return _staticAIData.getDodge();
	}
	
	public int getSSkillChance()
	{
		return _staticAIData.getShortRangeChance();
	}
	
	public int getLSkillChance()
	{
		return _staticAIData.getLongRangeChance();
	}
	
	public int getSwitchRangeChance()
	{
		return _staticAIData.getSwitchRangeChance();
	}
	
	public boolean hasLSkill()
	{
		if (_staticAIData.getLongRangeSkill() == 0)
		{
			return false;
		}
		return true;
	}
	
	public boolean hasSSkill()
	{
		if (_staticAIData.getShortRangeSkill() == 0)
		{
			return false;
		}
		return true;
	}
	
	public List<L2Skill> getLongRangeSkill()
	{
		final List<L2Skill> skilldata = new ArrayList<>();
		if ((_staticAIData == null) || (_staticAIData.getLongRangeSkill() == 0))
		{
			return skilldata;
		}
		
		switch (_staticAIData.getLongRangeSkill())
		{
			case -1:
			{
				Collection<L2Skill> skills = getAllSkills();
				if (skills != null)
				{
					for (L2Skill sk : skills)
					{
						if ((sk == null) || sk.isPassive() || (sk.getTargetType() == L2TargetType.TARGET_SELF))
						{
							continue;
						}
						
						if (sk.getCastRange() >= 200)
						{
							skilldata.add(sk);
						}
					}
				}
				break;
			}
			case 1:
			{
				if (getTemplate().getUniversalSkills() != null)
				{
					for (L2Skill sk : getTemplate().getUniversalSkills())
					{
						if (sk.getCastRange() >= 200)
						{
							skilldata.add(sk);
						}
					}
				}
				break;
			}
			default:
			{
				for (L2Skill sk : getAllSkills())
				{
					if (sk.getId() == _staticAIData.getLongRangeSkill())
					{
						skilldata.add(sk);
					}
				}
			}
		}
		return skilldata;
	}
	
	public List<L2Skill> getShortRangeSkill()
	{
		final List<L2Skill> skilldata = new ArrayList<>();
		if ((_staticAIData == null) || (_staticAIData.getShortRangeSkill() == 0))
		{
			return skilldata;
		}
		
		switch (_staticAIData.getShortRangeSkill())
		{
			case -1:
			{
				Collection<L2Skill> skills = getAllSkills();
				if (skills != null)
				{
					for (L2Skill sk : skills)
					{
						if ((sk == null) || sk.isPassive() || (sk.getTargetType() == L2TargetType.TARGET_SELF))
						{
							continue;
						}
						if (sk.getCastRange() <= 200)
						{
							skilldata.add(sk);
						}
					}
				}
				break;
			}
			case 1:
			{
				if (getTemplate().getUniversalSkills() != null)
				{
					for (L2Skill sk : getTemplate().getUniversalSkills())
					{
						if (sk.getCastRange() <= 200)
						{
							skilldata.add(sk);
						}
					}
				}
				break;
			}
			default:
			{
				for (L2Skill sk : getAllSkills())
				{
					if (sk.getId() == _staticAIData.getShortRangeSkill())
					{
						skilldata.add(sk);
					}
				}
			}
		}
		return skilldata;
	}
	
	/** Task launching the function onRandomAnimation() */
	protected class RandomAnimationTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				if (this != _rAniTask)
				{
					return; // Shouldn't happen, but who knows... just to make sure every active npc has only one timer.
				}
				if (isMob())
				{
					// Cancel further animation timers until intention is changed to ACTIVE again.
					if (getAI().getIntention() != AI_INTENTION_ACTIVE)
					{
						return;
					}
				}
				else
				{
					if (!isInActiveRegion())
					{
						return;
					}
				}
				
				if (!(isDead() || isStunned() || isSleeping() || isParalyzed()))
				{
					onRandomAnimation(Rnd.get(2, 3));
				}
				
				startRandomAnimationTimer();
			}
			catch (Exception e)
			{
				_log.log(Level.SEVERE, "", e);
			}
		}
	}
	
	/**
	 * Send a packet SocialAction to all L2PcInstance in the _KnownPlayers of the L2NpcInstance and create a new RandomAnimation Task.<BR>
	 * <BR>
	 * @param animationId
	 */
	public void onRandomAnimation(int animationId)
	{
		// Send a packet SocialAction to all L2PcInstance in the _KnownPlayers of the L2NpcInstance
		long now = System.currentTimeMillis();
		if ((now - _lastSocialBroadcast) > _minimalSocialInterval)
		{
			_lastSocialBroadcast = now;
			broadcastPacket(new SocialAction(getObjectId(), animationId));
		}
	}
	
	/**
	 * Create a RandomAnimation Task that will be launched after the calculated delay.<BR>
	 * <BR>
	 */
	public void startRandomAnimationTimer()
	{
		if (!hasRandomAnimation())
		{
			return;
		}
		
		int minWait = isMob() ? Config.MIN_MONSTER_ANIMATION : Config.MIN_NPC_ANIMATION;
		int maxWait = isMob() ? Config.MAX_MONSTER_ANIMATION : Config.MAX_NPC_ANIMATION;
		
		// Calculate the delay before the next animation
		int interval = Rnd.get(minWait, maxWait) * 1000;
		
		// Create a RandomAnimation Task that will be launched after the calculated delay
		_rAniTask = new RandomAnimationTask();
		ThreadPoolManager.getInstance().scheduleGeneral(_rAniTask, interval);
	}
	
	/**
	 * @return true if the server allows Random Animation.
	 */
	public boolean hasRandomAnimation()
	{
		return ((Config.MAX_NPC_ANIMATION > 0) && _isRandomAnimationEnabled && !getAiType().equals(AIType.CORPSE));
	}
	
 	/**
 	 * Switches random Animation state into val.
	 * @param val needed state of random animation
 	 */
	public void setRandomAnimationEnabled(boolean val)
	{
		_isRandomAnimationEnabled = val;
	}
	
	/**
	 * Returns Random Animation state.<BR>
	 * <BR>
	 * @return 
	 */
	public boolean isRandomAnimationEnabled()
	{
		return _isRandomAnimationEnabled;
	}
	
	/**
	 * Constructor of L2NpcInstance (use L2Character constructor).<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Call the L2Character constructor to set the _template of the L2Character (copy skills from template to object and link _calculators to NPC_STD_CALCULATOR)</li> <li>Set the name of the L2Character</li> <li>Create a RandomAnimation Task that will be launched after the calculated delay if
	 * the server allow it</li><BR>
	 * <BR>
	 * @param objectId Identifier of the object to initialized
	 * @param template The L2NpcTemplate to apply to the NPC
	 */
	public L2Npc(int objectId, L2NpcTemplate template)
	{
		// Call the L2Character constructor to set the _template of the L2Character, copy skills from template to object
		// and link _calculators to NPC_STD_CALCULATOR
		super(objectId, template);
		setInstanceType(InstanceType.L2Npc);
		initCharStatusUpdateValues();
		
		// initialize the "current" equipment
		_currentLHandId = getTemplate().getLeftHand();
		_currentRHandId = getTemplate().getRightHand();
		_currentEnchant = Config.ENABLE_RANDOM_ENCHANT_EFFECT ? Rnd.get(4, 21) : getTemplate().getEnchantEffect();
		// initialize the "current" collisions
		_currentCollisionHeight = getTemplate().getfCollisionHeight();
		_currentCollisionRadius = getTemplate().getfCollisionRadius();
		
		if (template == null)
		{
			_log.severe("No template for Npc. Please check your datapack is setup correctly.");
			return;
		}
		
		_fakePc = FakePcsTable.getInstance().getFakePc(template.getNpcId());
		
		// Set the name of the L2Character
		setName(template.getName());
	}
	
	@Override
	public NpcKnownList getKnownList()
	{
		return (NpcKnownList) super.getKnownList();
	}
	
	@Override
	public void initKnownList()
	{
		setKnownList(new NpcKnownList(this));
	}
	
	@Override
	public NpcStat getStat()
	{
		return (NpcStat) super.getStat();
	}
	
	@Override
	public void initCharStat()
	{
		setStat(new NpcStat(this));
	}
	
	@Override
	public NpcStatus getStatus()
	{
		return (NpcStatus) super.getStatus();
	}
	
	@Override
	public void initCharStatus()
	{
		setStatus(new NpcStatus(this));
	}
	
	/** Return the L2NpcTemplate of the L2NpcInstance. */
	@Override
	public final L2NpcTemplate getTemplate()
	{
		return (L2NpcTemplate) super.getTemplate();
	}
	
	/**
	 * @return the generic Identifier of this L2NpcInstance contained in the L2NpcTemplate.
	 */
	public int getNpcId()
	{
		return getTemplate().getNpcId();
	}
	
	@Override
	public boolean isAttackable()
	{
		return Config.ALT_ATTACKABLE_NPCS;
	}
	
	/**
	 * <B><U> Concept</U> :</B><BR>
	 * <BR>
	 * If a NPC belongs to a Faction, other NPC of the faction inside the Faction range will help it if it's attacked<BR>
	 * <BR>
	 * @return the faction Identifier of this L2NpcInstance contained in the L2NpcTemplate.
	 */
	// @Deprecated
	public final String getFactionId()
	{
		return getClan();
	}
	
	/**
	 * Return the Level of this L2NpcInstance contained in the L2NpcTemplate.<BR>
	 * <BR>
	 */
	@Override
	public final int getLevel()
	{
		return getTemplate().getLevel();
	}
	
	/**
	 * @return True if the L2NpcInstance is aggressive (ex : L2MonsterInstance in function of aggroRange).
	 */
	public boolean isAggressive()
	{
		return false;
	}
	
	/**
	 * @return the Aggro Range of this L2NpcInstance contained in the L2NpcTemplate.
	 */
	public int getAggroRange()
	{
		return hasAIValue("aggroRange") ? getAIValue("aggroRange") : _staticAIData.getAggroRange();
	}
	
	/**
	 * @return the Faction Range of this L2NpcInstance contained in the L2NpcTemplate.
	 */
	// @Deprecated
	public int getFactionRange()
	{
		return getClanRange();
	}
	
	/**
	 * Return True if this L2NpcInstance is undead in function of the L2NpcTemplate.<BR>
	 * <BR>
	 */
	@Override
	public boolean isUndead()
	{
		return getTemplate().isUndead();
	}
	
	/**
	 * Send a packet NpcInfo with state of abnormal effect to all L2PcInstance in the _KnownPlayers of the L2NpcInstance.<BR>
	 * <BR>
	 */
	@Override
	public void updateAbnormalEffect()
	{
		// Send a Server->Client packet NpcInfo with state of abnormal effect to all L2PcInstance in the _KnownPlayers of the L2NpcInstance
		Collection<L2PcInstance> plrs = getKnownList().getKnownPlayers().values();
		for (L2PcInstance player : plrs)
		{
			if (player == null)
			{
				continue;
			}
			if (getRunSpeed() == 0)
			{
				player.sendPacket(new ServerObjectInfo(this, player));
			}
			else
			{
				player.sendPacket(new AbstractNpcInfo.NpcInfo(this, player));
			}
		}
	}
	
	/**
	 * <B><U>Values </U>:</B>
	 * <ul>
	 * <li>object is a L2FolkInstance : 0 (don't remember it)</li>
	 * <li>object is a L2Character : 0 (don't remember it)</li>
	 * <li>object is a L2Playable : 1500</li>
	 * <li>others : 500</li>
	 * <ul>
	 * @param object The Object to add to _knownObject
	 * @return the distance under which the object must be add to _knownObject in function of the object type.
	 */
	public int getDistanceToWatchObject(L2Object object)
	{
		if (object instanceof L2FestivalGuideInstance)
		{
			return 10000;
			
		}
		
		if ((object instanceof L2NpcInstance) || !(object instanceof L2Character))
		{
			return 0;
		}
		
		if (object instanceof L2Playable)
		{
			return 1500;
		}
		
		return 500;
	}
	
	/**
	 * <B><U>Values</U>:</B>
	 * <ul>
	 * <li>object is not a L2Character : 0 (don't remember it)</li>
	 * <li>object is a L2FolkInstance : 0 (don't remember it)</li>
	 * <li>object is a L2Playable : 3000</li>
	 * <li>others : 1000</li>
	 * </ul>
	 * @param object The Object to remove from _knownObject
	 * @return the distance after which the object must be remove from _knownObject in function of the object type.
	 */
	public int getDistanceToForgetObject(L2Object object)
	{
		return 2 * getDistanceToWatchObject(object);
	}
	
	public boolean isEventMob()
	{
		return _eventMob;
	}
	
	public void setEventMob(boolean val)
	{
		_eventMob = val;
	}
	
	@Override
	public boolean isAutoAttackable(L2Character attacker)
	{
		return _isAutoAttackable;
	}
	
	public void setAutoAttackable(boolean flag)
	{
		_isAutoAttackable = flag;
	}
	
	/**
	 * @return the Identifier of the item in the left hand of this L2NpcInstance contained in the L2NpcTemplate.
	 */
	public int getLeftHandItem()
	{
		return _currentLHandId;
	}
	
	/**
	 * @return the Identifier of the item in the right hand of this L2NpcInstance contained in the L2NpcTemplate.
	 */
	public int getRightHandItem()
	{
		return _currentRHandId;
	}
	
	public int getEnchantEffect()
	{
		return _currentEnchant;
	}
	
	/**
	 * @return the busy status of this L2NpcInstance.
	 */
	public final boolean isBusy()
	{
		return _isBusy;
	}
	
	/**
	 * @param isBusy the busy status of this L2Npc
	 */
	public void setBusy(boolean isBusy)
	{
		_isBusy = isBusy;
	}
	
	/**
	 * @return the busy message of this L2NpcInstance.
	 */
	public final String getBusyMessage()
	{
		return _busyMessage;
	}
	
	/**
	 * @param message the busy message of this L2Npc.
	 */
	public void setBusyMessage(String message)
	{
		_busyMessage = message;
	}
	
	/**
	 * @return true if this L2Npc instance can be warehouse manager.
	 */
	public boolean isWarehouse()
	{
		return false;
	}
	
	public boolean canTarget(L2PcInstance player)
	{
		if (player.isOutOfControl())
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		if (player.isLockedTarget() && (player.getLockedTarget() != this))
		{
			player.sendPacket(SystemMessageId.FAILED_CHANGE_TARGET);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		// TODO: More checks...
		
		return true;
	}
	
	public boolean canInteract(L2PcInstance player)
	{
		if (player.isCastingNow() || player.isCastingSimultaneouslyNow())
		{
			return false;
		}
		if (player.isDead() || player.isFakeDeath())
		{
			return false;
		}
		if (player.isSitting())
		{
			return false;
		}
		if (player.getPrivateStoreType() != L2PcInstance.STORE_PRIVATE_NONE)
		{
			return false;
		}
		if (!isInsideRadius(player, INTERACTION_DISTANCE, true, false))
		{
			return false;
		}
		if ((player.getInstanceId() != getInstanceId()) && (player.getInstanceId() != -1))
		{
			return false;
		}
		if (isBusy())
		{
			return false;
		}
		return true;
	}
	
	/**
	 * @return the L2Castle this L2NpcInstance belongs to.
	 */
	public final Castle getCastle()
	{
		// Get castle this NPC belongs to (excluding L2Attackable)
		if (_castleIndex < 0)
		{
			L2TownZone town = TownManager.getTown(getX(), getY(), getZ());
			
			if (town != null)
			{
				_castleIndex = CastleManager.getInstance().getCastleIndex(town.getTaxById());
			}
			
			if (_castleIndex < 0)
			{
				_castleIndex = CastleManager.getInstance().findNearestCastleIndex(this);
			}
			else
			{
				_isInTown = true; // Npc was spawned in town
			}
		}
		
		if (_castleIndex < 0)
		{
			return null;
		}
		
		return CastleManager.getInstance().getCastles().get(_castleIndex);
	}
	
	/**
	 * Verify if the given player is this NPC's lord.
	 * @param player the player to check
	 * @return {@code true} if the player is clan leader and owner of a castle of fort that this NPC belongs to, {@code false} otherwise
	 */
	public boolean isMyLord(L2PcInstance player)
	{
		if (player.isClanLeader())
		{
			final int castleId = getCastle() != null ? getCastle().getCastleId() : -1;
			final int fortId = getFort() != null ? getFort().getFortId() : -1;
			return (player.getClan().getCastleId() == castleId) || (player.getClan().getFortId() == fortId);
		}
		return false;
	}
	
	public final SiegableHall getConquerableHall()
	{
		return CHSiegeManager.getInstance().getNearbyClanHall(getX(), getY(), 10000);
	}
	
	/**
	 * Return closest castle in defined distance
	 * @param maxDistance long
	 * @return Castle
	 */
	public final Castle getCastle(long maxDistance)
	{
		int index = CastleManager.getInstance().findNearestCastleIndex(this, maxDistance);
		
		if (index < 0)
		{
			return null;
		}
		
		return CastleManager.getInstance().getCastles().get(index);
	}
	
	/**
	 * @return the L2Fort this L2NpcInstance belongs to.
	 */
	public final Fort getFort()
	{
		// Get Fort this NPC belongs to (excluding L2Attackable)
		if (_fortIndex < 0)
		{
			Fort fort = FortManager.getInstance().getFort(getX(), getY(), getZ());
			if (fort != null)
			{
				_fortIndex = FortManager.getInstance().getFortIndex(fort.getFortId());
			}
			
			if (_fortIndex < 0)
			{
				_fortIndex = FortManager.getInstance().findNearestFortIndex(this);
			}
		}
		
		if (_fortIndex < 0)
		{
			return null;
		}
		
		return FortManager.getInstance().getForts().get(_fortIndex);
	}
	
	/**
	 * Return closest Fort in defined distance
	 * @param maxDistance long
	 * @return Fort
	 */
	public final Fort getFort(long maxDistance)
	{
		int index = FortManager.getInstance().findNearestFortIndex(this, maxDistance);
		
		if (index < 0)
		{
			return null;
		}
		
		return FortManager.getInstance().getForts().get(index);
	}
	
	public final boolean getIsInTown()
	{
		if (_castleIndex < 0)
		{
			getCastle();
		}
		
		return _isInTown;
	}
	
	/**
	 * Open a quest or chat window on client with the text of the L2NpcInstance in function of the command.<br>
	 * <B><U> Example of use </U> :</B>
	 * <ul>
	 * <li>Client packet : RequestBypassToServer</li>
	 * </ul>
	 * @param player
	 * @param command The command string received from client
	 */
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		// if (canInteract(player))
		{
			if (isBusy() && (getBusyMessage().length() > 0))
			{
				player.sendPacket(ActionFailed.STATIC_PACKET);
				
				NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile(player.getHtmlPrefix(), "data/html/npcbusy.htm");
				html.replace("%busymessage%", getBusyMessage());
				html.replace("%npcname%", getName());
				html.replace("%playername%", player.getName());
				player.sendPacket(html);
			}
			else
			{
				IBypassHandler handler = BypassHandler.getInstance().getHandler(command);
				if (handler != null)
				{
					handler.useBypass(command, player, this);
				}
				else
				{
					_log.info(getClass().getSimpleName() + ": Unknown NPC bypass: \"" + command + "\" NpcId: " + getNpcId());
				}
			}
		}
	}
	
	/**
	 * Return null (regular NPCs don't have weapons instancies).
	 */
	@Override
	public L2ItemInstance getActiveWeaponInstance()
	{
		// regular NPCs dont have weapons instancies
		return null;
	}
	
	/**
	 * Return the weapon item equiped in the right hand of the L2NpcInstance or null.<BR>
	 * <BR>
	 */
	@Override
	public L2Weapon getActiveWeaponItem()
	{
		// Get the weapon identifier equiped in the right hand of the L2NpcInstance
		int weaponId = getTemplate().getRightHand();
		
		if (weaponId < 1)
		{
			return null;
		}
		
		// Get the weapon item equiped in the right hand of the L2NpcInstance
		L2Item item = ItemTable.getInstance().getTemplate(getTemplate().getRightHand());
		
		if (!(item instanceof L2Weapon))
		{
			return null;
		}
		
		return (L2Weapon) item;
	}
	
	/**
	 * Return null (regular NPCs don't have weapons instancies).<BR>
	 * <BR>
	 */
	@Override
	public L2ItemInstance getSecondaryWeaponInstance()
	{
		// regular NPCs dont have weapons instancies
		return null;
	}
	
	/**
	 * Return the weapon item equiped in the left hand of the L2NpcInstance or null.<BR>
	 * <BR>
	 */
	@Override
	public L2Weapon getSecondaryWeaponItem()
	{
		// Get the weapon identifier equiped in the right hand of the L2NpcInstance
		int weaponId = getTemplate().getLeftHand();
		
		if (weaponId < 1)
		{
			return null;
		}
		
		// Get the weapon item equiped in the right hand of the L2NpcInstance
		L2Item item = ItemTable.getInstance().getTemplate(getTemplate().getLeftHand());
		
		if (!(item instanceof L2Weapon))
		{
			return null;
		}
		
		return (L2Weapon) item;
	}
	
	/**
	 * Send a Server->Client packet NpcHtmlMessage to the L2PcInstance in order to display the message of the L2NpcInstance.
	 * @param player The L2PcInstance who talks with the L2NpcInstance
	 * @param content The text of the L2NpcMessage
	 */
	public void insertObjectIdAndShowChatWindow(L2PcInstance player, String content)
	{
		// Send a Server->Client packet NpcHtmlMessage to the L2PcInstance in order to display the message of the L2NpcInstance
		content = content.replaceAll("%objectId%", String.valueOf(getObjectId()));
		NpcHtmlMessage npcReply = new NpcHtmlMessage(getObjectId());
		npcReply.setHtml(content);
		player.sendPacket(npcReply);
	}
	
	/**
	 * <B><U Format of the pathfile</U>:</B>
	 * <ul>
	 * <li>if the file exists on the server (page number = 0) : <B>data/html/default/12006.htm</B> (npcId-page number)</li>
	 * <li>if the file exists on the server (page number > 0) : <B>data/html/default/12006-1.htm</B> (npcId-page number)</li>
	 * <li>if the file doesn't exist on the server : <B>data/html/npcdefault.htm</B> (message : "I have nothing to say to you")</li>
	 * </ul>
	 * @param npcId The Identifier of the L2NpcInstance whose text must be display
	 * @param val The number of the page to display
	 * @return the pathfile of the selected HTML file in function of the npcId and of the page number.
	 */
	public String getHtmlPath(int npcId, int val)
	{
		String pom = "";
		
		if (val == 0)
		{
			pom = "" + npcId;
		}
		else
		{
			pom = npcId + "-" + val;
		}
		
		String temp = "data/html/default/" + pom + ".htm";
		
		if (!Config.LAZY_CACHE)
		{
			// If not running lazy cache the file must be in the cache or it doesnt exist
			if (HtmCache.getInstance().contains(temp))
			{
				return temp;
			}
		}
		else
		{
			if (HtmCache.getInstance().isLoadable(temp))
			{
				return temp;
			}
		}
		
		// If the file is not found, the standard message "I have nothing to say to you" is returned
		return "data/html/npcdefault.htm";
	}
	
	public void showChatWindow(L2PcInstance player)
	{
		showChatWindow(player, 0);
	}
	
	/**
	 * Returns true if html exists
	 * @param player
	 * @param type
	 * @return boolean
	 */
	private boolean showPkDenyChatWindow(L2PcInstance player, String type)
	{
		String html = HtmCache.getInstance().getHtm(player.getHtmlPrefix(), "data/html/" + type + "/" + getNpcId() + "-pk.htm");
		
		if (html != null)
		{
			NpcHtmlMessage pkDenyMsg = new NpcHtmlMessage(getObjectId());
			pkDenyMsg.setHtml(html);
			player.sendPacket(pkDenyMsg);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Open a chat window on client with the text of the L2NpcInstance.<br>
	 * <B><U>Actions</U>:</B>
	 * <ul>
	 * <li>Get the text of the selected HTML file in function of the npcId and of the page number</li>
	 * <li>Send a Server->Client NpcHtmlMessage containing the text of the L2NpcInstance to the L2PcInstance</li>
	 * <li>Send a Server->Client ActionFailed to the L2PcInstance in order to avoid that the client wait another packet</li>
	 * </ul>
	 * @param player The L2PcInstance that talk with the L2NpcInstance
	 * @param val The number of the page of the L2NpcInstance to display
	 */
	public void showChatWindow(L2PcInstance player, int val)
	{
		if (Config.NON_TALKING_NPCS.contains(getNpcId()))
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		if (player.isCursedWeaponEquipped() && (!(player.getTarget() instanceof L2ClanHallManagerInstance) || !(player.getTarget() instanceof L2DoormenInstance)))
		{
			player.setTarget(player);
			return;
		}
		if (player.getKarma() > 0)
		{
			if (!Config.ALT_GAME_KARMA_PLAYER_CAN_SHOP && (this instanceof L2MerchantInstance))
			{
				if (showPkDenyChatWindow(player, "merchant"))
				{
					return;
				}
			}
			else if (!Config.ALT_GAME_KARMA_PLAYER_CAN_USE_GK && (this instanceof L2TeleporterInstance))
			{
				if (showPkDenyChatWindow(player, "teleporter"))
				{
					return;
				}
			}
			else if (!Config.ALT_GAME_KARMA_PLAYER_CAN_USE_WAREHOUSE && (this instanceof L2WarehouseInstance))
			{
				if (showPkDenyChatWindow(player, "warehouse"))
				{
					return;
				}
			}
			else if (!Config.ALT_GAME_KARMA_PLAYER_CAN_SHOP && (this instanceof L2FishermanInstance))
			{
				if (showPkDenyChatWindow(player, "fisherman"))
				{
					return;
				}
			}
			else if (player.isInCombat())
			{
				if (showPkDenyChatWindow(player, "You cannot use gatekeeper while you are in combat."))
				{
					return;
				}
			}
		}
		
		if (getTemplate().isType("L2Auctioneer") && (val == 0))
		{
			return;
		}
		
		int npcId = getTemplate().getNpcId();
		
		/* For use with Seven Signs implementation */
		String filename = SevenSigns.SEVEN_SIGNS_HTML_PATH;
		int sealAvariceOwner = SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_AVARICE);
		int sealGnosisOwner = SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_GNOSIS);
		int playerCabal = SevenSigns.getInstance().getPlayerCabal(player.getObjectId());
		int compWinner = SevenSigns.getInstance().getCabalHighestScore();
		
		switch (npcId)
		{
			case 31127: //
			case 31128: //
			case 31129: // Dawn Festival Guides
			case 31130: //
			case 31131: //
				filename += "festival/dawn_guide.htm";
				break;
			case 31137: //
			case 31138: //
			case 31139: // Dusk Festival Guides
			case 31140: //
			case 31141: //
				filename += "festival/dusk_guide.htm";
				break;
			case 31092: // Black Marketeer of Mammon
				filename += "blkmrkt_1.htm";
				break;
			case 31113: // Merchant of Mammon
				if (Config.ALT_STRICT_SEVENSIGNS)
				{
					switch (compWinner)
					{
						case SevenSigns.CABAL_DAWN:
							if ((playerCabal != compWinner) || (playerCabal != sealAvariceOwner))
							{
								player.sendPacket(SystemMessageId.CAN_BE_USED_BY_DAWN);
								player.sendPacket(ActionFailed.STATIC_PACKET);
								return;
							}
							break;
						case SevenSigns.CABAL_DUSK:
							if ((playerCabal != compWinner) || (playerCabal != sealAvariceOwner))
							{
								player.sendPacket(SystemMessageId.CAN_BE_USED_BY_DUSK);
								player.sendPacket(ActionFailed.STATIC_PACKET);
								return;
							}
							break;
						default:
							player.sendPacket(SystemMessageId.SSQ_COMPETITION_UNDERWAY);
							return;
					}
				}
				filename += "mammmerch_1.htm";
				break;
			case 31126: // Blacksmith of Mammon
				if (Config.ALT_STRICT_SEVENSIGNS)
				{
					switch (compWinner)
					{
						case SevenSigns.CABAL_DAWN:
							if ((playerCabal != compWinner) || (playerCabal != sealGnosisOwner))
							{
								player.sendPacket(SystemMessageId.CAN_BE_USED_BY_DAWN);
								player.sendPacket(ActionFailed.STATIC_PACKET);
								return;
							}
							break;
						case SevenSigns.CABAL_DUSK:
							if ((playerCabal != compWinner) || (playerCabal != sealGnosisOwner))
							{
								player.sendPacket(SystemMessageId.CAN_BE_USED_BY_DUSK);
								player.sendPacket(ActionFailed.STATIC_PACKET);
								return;
							}
							break;
						default:
							player.sendPacket(SystemMessageId.SSQ_COMPETITION_UNDERWAY);
							return;
					}
				}
				filename += "mammblack_1.htm";
				break;
			case 31132:
			case 31133:
			case 31134:
			case 31135:
			case 31136: // Festival Witches
			case 31142:
			case 31143:
			case 31144:
			case 31145:
			case 31146:
				filename += "festival/festival_witch.htm";
				break;
			case 31688:
				if (player.isNoble())
				{
					filename = Olympiad.OLYMPIAD_HTML_PATH + "noble_main.htm";
				}
				else
				{
					filename = (getHtmlPath(npcId, val));
				}
				break;
			case 31690:
			case 31769:
			case 31770:
			case 31771:
			case 31772:
				if (player.isHero() || player.isNoble())
				{
					filename = Olympiad.OLYMPIAD_HTML_PATH + "hero_main.htm";
				}
				else
				{
					filename = (getHtmlPath(npcId, val));
				}
				break;
			case 36402:
				if (player.olyBuff > 0)
				{
					filename = (player.olyBuff == 5 ? Olympiad.OLYMPIAD_HTML_PATH + "olympiad_buffs.htm" : Olympiad.OLYMPIAD_HTML_PATH + "olympiad_5buffs.htm");
				}
				else
				{
					filename = Olympiad.OLYMPIAD_HTML_PATH + "olympiad_nobuffs.htm";
				}
				break;
			case 30298: // Blacksmith Pinter
				if (player.isAcademyMember())
				{
					filename = (getHtmlPath(npcId, 1));
				}
				else
				{
					filename = (getHtmlPath(npcId, val));
				}
				break;
			default:
				if ((npcId >= 31865) && (npcId <= 31918))
				{
					if (val == 0)
					{
						filename += "rift/GuardianOfBorder.htm";
					}
					else
					{
						filename += "rift/GuardianOfBorder-" + val + ".htm";
					}
					break;
				}
				if (((npcId >= 31093) && (npcId <= 31094)) || ((npcId >= 31172) && (npcId <= 31201)) || ((npcId >= 31239) && (npcId <= 31254)))
				{
					return;
				}
				// Get the text of the selected HTML file in function of the npcId and of the page number
				filename = (getHtmlPath(npcId, val));
				break;
		}
		
		if (EventsInterface.talkNpc(player.getObjectId(), getObjectId()))
		{
			return;
		}
		
		if (npcId == EventsInterface.getInt("managerNpcId", 0))
		{
			EventsInterface.showFirstHtml(player.getObjectId(), getObjectId());
			return;
		}
		
		if (EventsInterface.isParticipating(player.getObjectId()))
		{
			if (EventsInterface.onTalkNpc(getObjectId(), player.getObjectId()))
			{
				return;
			}
		}
		
		// Send a Server->Client NpcHtmlMessage containing the text of the L2NpcInstance to the L2PcInstance
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(player.getHtmlPrefix(), filename);
		
		if (this instanceof L2MerchantInstance)
		{
			if (Config.LIST_PET_RENT_NPC.contains(npcId))
			{
				html.replace("_Quest", "_RentPet\">Rent Pet</a><br><a action=\"bypass -h npc_%objectId%_Quest");
			}
		}
		
		html.replace("%name%", getName());
		html.replace("%player_name%", player.getName());
		html.replace("%objectId%", String.valueOf(getObjectId()));
		html.replace("%festivalMins%", SevenSignsFestival.getInstance().getTimeToNextFestivalStr());
		player.sendPacket(html);
		
		// Send a Server->Client ActionFailed to the L2PcInstance in order to avoid that the client wait another packet
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	/**
	 * Open a chat window on client with the text specified by the given file name and path, relative to the datapack root.
	 * @param player The L2PcInstance that talk with the L2NpcInstance
	 * @param filename The filename that contains the text to send
	 */
	public void showChatWindow(L2PcInstance player, String filename)
	{
		// Send a Server->Client NpcHtmlMessage containing the text of the L2NpcInstance to the L2PcInstance
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(player.getHtmlPrefix(), filename);
		html.replace("%objectId%", String.valueOf(getObjectId()));
		player.sendPacket(html);
		
		// Send a Server->Client ActionFailed to the L2PcInstance in order to avoid that the client wait another packet
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	/**
	 * @return the Exp Reward of this L2NpcInstance contained in the L2NpcTemplate (modified by RATE_XP).
	 */
	public int getExpReward()
	{
		return (int) (getTemplate().getRewardExp() * Config.RATE_XP);
	}
	
	/**
	 * @return the SP Reward of this L2NpcInstance contained in the L2NpcTemplate (modified by RATE_SP).
	 */
	public int getSpReward()
	{
		return (int) (getTemplate().getRewardSp() * Config.RATE_SP);
	}
	
	/**
	 * Kill the L2NpcInstance (the corpse disappeared after 7 seconds).<br>
	 * <B><U>Actions</U>:</B>
	 * <ul>
	 * <li>Create a DecayTask to remove the corpse of the L2NpcInstance after 7 seconds</li>
	 * <li>Set target to null and cancel Attack or Cast</li>
	 * <li>Stop movement</li>
	 * <li>Stop HP/MP/CP Regeneration task</li>
	 * <li>Stop all active skills effects in progress on the L2Character</li>
	 * <li>Send the Server->Client packet StatusUpdate with current HP and MP to all other L2PcInstance to inform</li>
	 * <li>Notify L2Character AI</li>
	 * </ul>
	 * @param killer The L2Character who killed it
	 */
	@Override
	public boolean doDie(L2Character killer)
	{
		if (!super.doDie(killer))
		{
			return false;
		}
		
		// normally this wouldn't really be needed, but for those few exceptions,
		// we do need to reset the weapons back to the initial template weapon.
		_currentLHandId = getTemplate().getLeftHand();
		_currentRHandId = getTemplate().getRightHand();
		_currentCollisionHeight = getTemplate().getfCollisionHeight();
		_currentCollisionRadius = getTemplate().getfCollisionRadius();
		DecayTaskManager.getInstance().addDecayTask(this);
		return true;
	}
	
	/**
	 * Set the spawn of the L2NpcInstance.
	 * @param spawn The L2Spawn that manage the L2NpcInstance
	 */
	public void setSpawn(L2Spawn spawn)
	{
		_spawn = spawn;
	}
	
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		
		// Recharge shots
		_soulshotamount = getTemplate().getAIDataStatic().getSoulShot();
		_spiritshotamount = getTemplate().getAIDataStatic().getSpiritShot();
		
		if (getTemplate().getEventQuests(QuestEventType.ON_SPAWN) != null)
		{
			for (Quest quest : getTemplate().getEventQuests(QuestEventType.ON_SPAWN))
			{
				quest.notifySpawn(this);
			}
		}
		
		if (!isTeleporting())
		{
			WalkingManager.getInstance().onSpawn(this);
		}
	}
	
	/**
	 * Remove the L2NpcInstance from the world and update its spawn object (for a complete removal use the deleteMe method).<br>
	 * <B><U>Actions</U>:</B>
	 * <ul>
	 * <li>Remove the L2NpcInstance from the world when the decay task is launched</li>
	 * <li>Decrease its spawn counter</li>
	 * <li>Manage Siege task (killFlag, killCT)</li>
	 * </ul>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method DOESN'T REMOVE the object from _allObjects of L2World </B></FONT><BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method DOESN'T SEND Server->Client packets to players</B></FONT>
	 */
	@Override
	public void onDecay()
	{
		if (isDecayed())
		{
			return;
		}
		setDecayed(true);
		
		// Remove the L2NpcInstance from the world when the decay task is launched
		super.onDecay();
		
		// Decrease its spawn counter
		if (_spawn != null)
		{
			_spawn.decreaseCount(this);
		}
		
		// Notify Walking Manager
		WalkingManager.getInstance().onDeath(this);
	}
	
	/**
	 * Remove PROPERLY the L2NpcInstance from the world.<br>
	 * <B><U>Actions</U>:</B>
	 * <ul>
	 * <li>Remove the L2NpcInstance from the world and update its spawn object</li>
	 * <li>Remove all L2Object from _knownObjects and _knownPlayer of the L2NpcInstance then cancel Attack or Cast and notify AI</li>
	 * <li>Remove L2Object object from _allObjects of L2World</li>
	 * </ul>
	 * <FONT COLOR=#FF0000><B><U>Caution</U>: This method DOESN'T SEND Server->Client packets to players</B></FONT><br>
	 * UnAfraid: TODO: Add Listener here
	 */
	@Override
	public void deleteMe()
	{
		L2WorldRegion oldRegion = getWorldRegion();
		
		try
		{
			onDecay();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Failed decayMe().", e);
		}
		try
		{
			if (_fusionSkill != null)
			{
				abortCast();
			}
			
			for (L2Character character : getKnownList().getKnownCharacters())
			{
				if ((character.getFusionSkill() != null) && (character.getFusionSkill().getTarget() == this))
				{
					character.abortCast();
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "deleteMe()", e);
		}
		if (oldRegion != null)
		{
			oldRegion.removeFromZones(this);
		}
		
		// Remove all L2Object from _knownObjects and _knownPlayer of the L2Character then cancel Attack or Cast and notify AI
		try
		{
			getKnownList().removeAllKnownObjects();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Failed removing cleaning knownlist.", e);
		}
		
		// Remove L2Object object from _allObjects of L2World
		L2World.getInstance().removeObject(this);
		
		super.deleteMe();
	}
	
	/**
	 * @return the L2Spawn object that manage this L2NpcInstance.
	 */
	public L2Spawn getSpawn()
	{
		return _spawn;
	}
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + ":" + getName() + "(" + getNpcId() + ")" + "[" + getObjectId() + "]";
	}
	
	public boolean isDecayed()
	{
		return _isDecayed;
	}
	
	public void setDecayed(boolean decayed)
	{
		_isDecayed = decayed;
	}
	
	public void endDecayTask()
	{
		if (!isDecayed())
		{
			DecayTaskManager.getInstance().cancelDecayTask(this);
			onDecay();
		}
	}
	
	public boolean isMob() // rather delete this check
	{
		return false; // This means we use MAX_NPC_ANIMATION instead of MAX_MONSTER_ANIMATION
	}
	
	// Two functions to change the appearance of the equipped weapons on the NPC
	// This is only useful for a few NPCs and is most likely going to be called from AI
	public void setLHandId(int newWeaponId)
	{
		_currentLHandId = newWeaponId;
		updateAbnormalEffect();
	}
	
	public void setRHandId(int newWeaponId)
	{
		_currentRHandId = newWeaponId;
		updateAbnormalEffect();
	}
	
	public void setLRHandId(int newLWeaponId, int newRWeaponId)
	{
		_currentRHandId = newRWeaponId;
		_currentLHandId = newLWeaponId;
		updateAbnormalEffect();
	}
	
	public void setEnchant(int newEnchantValue)
	{
		_currentEnchant = newEnchantValue;
		updateAbnormalEffect();
	}
	
	public boolean isShowName()
	{
		return _staticAIData.showName();
	}
	
	@Override
	public boolean isTargetable()
	{
		return _staticAIData.isTargetable();
	}
	
	public void setCollisionHeight(double height)
	{
		_currentCollisionHeight = height;
	}
	
	public void setCollisionRadius(double radius)
	{
		_currentCollisionRadius = radius;
	}
	
	public double getCollisionHeight()
	{
		return _currentCollisionHeight;
	}
	
	public double getCollisionRadius()
	{
		return _currentCollisionRadius;
	}
	
	@Override
	public void sendInfo(L2PcInstance activeChar)
	{
		if (Config.CHECK_KNOWN && activeChar.isGM())
		{
			activeChar.sendMessage("Added NPC: " + getName());
		}
		
		if (getRunSpeed() == 0)
		{
			activeChar.sendPacket(new ServerObjectInfo(this, activeChar));
		}
		else
		{
			activeChar.sendPacket(new AbstractNpcInfo.NpcInfo(this, activeChar));
		}
	}
	
	public void showNoTeachHtml(L2PcInstance player)
	{
		int npcId = getNpcId();
		String html = "";
		
		if (this instanceof L2WarehouseInstance)
		{
			html = HtmCache.getInstance().getHtm(player.getHtmlPrefix(), "data/html/warehouse/" + npcId + "-noteach.htm");
		}
		else if (this instanceof L2TrainerHealersInstance)
		{
			html = HtmCache.getInstance().getHtm(player.getHtmlPrefix(), "data/html/trainer/skilltransfer/" + npcId + "-noteach.htm");
		}
		else if (this instanceof L2TrainerInstance)
		{
			html = HtmCache.getInstance().getHtm(player.getHtmlPrefix(), "data/html/trainer/" + npcId + "-noteach.htm");
		}
		
		if (html == null)
		{
			_log.warning("Npc " + npcId + " missing noTeach html!");
			NpcHtmlMessage msg = new NpcHtmlMessage(getObjectId());
			final String sb = StringUtil.concat("<html><body>" + "I cannot teach you any skills.<br>You must find your current class teachers.", "</body></html>");
			msg.setHtml(sb);
			player.sendPacket(msg);
			return;
		}
		
		final NpcHtmlMessage noTeachMsg = new NpcHtmlMessage(getObjectId());
		noTeachMsg.setHtml(html);
		noTeachMsg.replace("%objectId%", String.valueOf(getObjectId()));
		player.sendPacket(noTeachMsg);
	}
	
	public L2Npc scheduleDespawn(long delay)
	{
		ThreadPoolManager.getInstance().scheduleGeneral(new DespawnTask(), delay);
		return this;
	}
	
	protected class DespawnTask implements Runnable
	{
		@Override
		public void run()
		{
			if (!isDecayed())
			{
				deleteMe();
			}
		}
	}
	
	@Override
	protected final void notifyQuestEventSkillFinished(L2Skill skill, L2Object target)
	{
		try
		{
			if (getTemplate().getEventQuests(QuestEventType.ON_SPELL_FINISHED) != null)
			{
				L2PcInstance player = null;
				if (target != null)
				{
					player = target.getActingPlayer();
				}
				for (Quest quest : getTemplate().getEventQuests(QuestEventType.ON_SPELL_FINISHED))
				{
					quest.notifySpellFinished(this, player, skill);
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "", e);
		}
	}
	
	@Override
	public boolean isMovementDisabled()
	{
		return super.isMovementDisabled() || (getCanMove() == 0) || getAiType().equals(AIType.CORPSE);
	}
	
	public AIType getAiType()
	{
		return _staticAIData.getAiType();
	}
	
	public void setDisplayEffect(int val)
	{
		if (val != _displayEffect)
		{
			_displayEffect = val;
			broadcastPacket(new ExChangeNpcState(getObjectId(), val));
		}
	}
	
	public int getDisplayEffect()
	{
		return _displayEffect;
	}
	
	public int getColorEffect()
	{
		return 0;
	}
	
	/**
	 * @return the character that summoned this NPC.
	 */
	public L2Character getSummoner()
	{
		return _summoner;
	}
	
	/**
	 * @param summoner the summoner of this NPC.
	 */
	public void setSummoner(L2Character summoner)
	{
		_summoner = summoner;
	}
	
	@Override
	public boolean isNpc()
	{
		return true;
	}
	
	public void broadcastNpcSay(String text)
	{
		broadcastNpcSay(0, text);
	}
	
	public void broadcastNpcSay(int messageType, String text)
	{
		broadcastPacket(new NpcSay(getObjectId(), messageType, getNpcId(), text));
	}
	
	// This sets mobs that not do any animation
	private boolean _isNoAnimation = false;
	
	public final boolean isNoAnimation()
	{
		return _isNoAnimation;
	}
	
	public final void setIsNoAnimation(boolean value)
	{
		_isNoAnimation = value;
	}
	
	@Override
	public void setTeam(int id)
	{
		super.setTeam(id);
		for (L2PcInstance player : getKnownList().getKnownPlayers().values())
		{
			player.sendPacket(new AbstractNpcInfo.NpcInfo(this, player));
		}
	}
	
	/**
	 * @return {@code true} if this L2Npc is registered in WalkingManager
	 */
	@Override
	public boolean isWalker()
	{
		return WalkingManager.getInstance().isRegistered(this);
	}
	
	public FakePc getFakePc()
	{
		return _fakePc;
	}
	
	@Override
	public boolean isChargedShot(ShotType type)
	{
		return (_shotsMask & type.getMask()) == type.getMask();
	}
	
	@Override
	public void setChargedShot(ShotType type, boolean charged)
	{
		if (charged)
		{
			_shotsMask |= type.getMask();
		}
		else
		{
			_shotsMask &= ~type.getMask();
		}
	}
	
	@Override
	public void rechargeShots(boolean physical, boolean magic)
	{
		if ((_soulshotamount > 0) || (_spiritshotamount > 0))
		{
			if (physical)
			{
				if (_soulshotamount == 0)
				{
					return;
				}
				else if (Rnd.get(100) > getSoulShotChance())
				{
					return;
				}
				_soulshotamount--;
				Broadcast.toSelfAndKnownPlayersInRadius(this, new MagicSkillUse(this, this, 2154, 1, 0, 0), 600);
				setChargedShot(ShotType.SOULSHOTS, true);
			}
			if (magic)
			{
				if (_spiritshotamount == 0)
				{
					return;
				}
				else if (Rnd.get(100) > getSpiritShotChance())
				{
					return;
				}
				_spiritshotamount--;
				Broadcast.toSelfAndKnownPlayersInRadius(this, new MagicSkillUse(this, this, 2061, 1, 0, 0), 600);
				setChargedShot(ShotType.SPIRITSHOTS, true);
			}
		}
	}
	
	/**
	 * Send an "event" to all NPC's within given radius
	 * @param eventName - name of event
	 * @param radius - radius to send event
	 * @param reference - L2Object to pass, if needed
	 */
	public void broadcastEvent(String eventName, int radius, L2Object reference)
	{
		for (L2Object obj : L2World.getInstance().getVisibleObjects(this, radius))
		{
			if (obj.isNpc() && (((L2Npc) obj).getTemplate().getEventQuests(QuestEventType.ON_EVENT_RECEIVED) != null))
			{
				for (Quest quest : ((L2Npc) obj).getTemplate().getEventQuests(QuestEventType.ON_EVENT_RECEIVED))
				{
					quest.notifyEventReceived(eventName, this, (L2Npc) obj, reference);
				}
			}
		}
	}
	
	public int getScriptValue(int index)
	{
		return ((_scriptVal == null) || !_scriptVal.containsKey(index)) ? 0 : _scriptVal.get(index);
	}
	
	public int getScriptValue()
	{
		return getScriptValue(0);
	}
	
	public void setScriptValue(int index, int val)
	{
		if (_scriptVal == null)
		{
			_scriptVal = new HashMap<>();
		}
		
		_scriptVal.put(index, val);
	}
	
	public void setScriptValue(int val)
	{
		setScriptValue(0, val);
	}
	
	public boolean isScriptValue(int index, int val)
	{
		return getScriptValue(index) == val;
	}
	
	public boolean isScriptValue(int val)
	{
		return isScriptValue(0, val);
	}
	
	public void clearScriptValues()
	{
		if (_scriptVal != null)
		{
			_scriptVal.clear();
		}
	}
	
	public int getAIValue(final String paramName)
	{
		return hasAIValue(paramName) ? NpcPersonalAIData.getInstance().getAIValue(getSpawn().getName(), paramName) : -1;
	}
	
	public boolean hasAIValue(final String paramName)
	{
		return (getSpawn() != null) && (getSpawn().getName() != null) && NpcPersonalAIData.getInstance().hasAIValue(getSpawn().getName(), paramName);
	}
	
	public void block()
	{
		_blocked = true;
	}
	
	public void unblock()
	{
		_blocked = false;
	}
	
	public boolean isBlocked()
	{
		return _blocked;
	}
}
