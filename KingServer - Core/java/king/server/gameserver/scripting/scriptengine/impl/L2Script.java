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
package king.server.gameserver.scripting.scriptengine.impl;

import java.util.ArrayList;
import java.util.List;

import king.server.gameserver.model.L2Clan;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.quest.Quest;
import king.server.gameserver.scripting.scriptengine.events.AddToInventoryEvent;
import king.server.gameserver.scripting.scriptengine.events.AttackEvent;
import king.server.gameserver.scripting.scriptengine.events.AugmentEvent;
import king.server.gameserver.scripting.scriptengine.events.ChatEvent;
import king.server.gameserver.scripting.scriptengine.events.ClanCreationEvent;
import king.server.gameserver.scripting.scriptengine.events.ClanJoinEvent;
import king.server.gameserver.scripting.scriptengine.events.ClanLeaderChangeEvent;
import king.server.gameserver.scripting.scriptengine.events.ClanLeaveEvent;
import king.server.gameserver.scripting.scriptengine.events.ClanLevelUpEvent;
import king.server.gameserver.scripting.scriptengine.events.ClanWarEvent;
import king.server.gameserver.scripting.scriptengine.events.ClanWarehouseAddItemEvent;
import king.server.gameserver.scripting.scriptengine.events.ClanWarehouseDeleteItemEvent;
import king.server.gameserver.scripting.scriptengine.events.ClanWarehouseTransferEvent;
import king.server.gameserver.scripting.scriptengine.events.DeathEvent;
import king.server.gameserver.scripting.scriptengine.events.DlgAnswerEvent;
import king.server.gameserver.scripting.scriptengine.events.EquipmentEvent;
import king.server.gameserver.scripting.scriptengine.events.FortSiegeEvent;
import king.server.gameserver.scripting.scriptengine.events.HennaEvent;
import king.server.gameserver.scripting.scriptengine.events.ItemCreateEvent;
import king.server.gameserver.scripting.scriptengine.events.ItemDestroyEvent;
import king.server.gameserver.scripting.scriptengine.events.ItemDropEvent;
import king.server.gameserver.scripting.scriptengine.events.ItemPickupEvent;
import king.server.gameserver.scripting.scriptengine.events.ItemTransferEvent;
import king.server.gameserver.scripting.scriptengine.events.PlayerEvent;
import king.server.gameserver.scripting.scriptengine.events.PlayerLevelChangeEvent;
import king.server.gameserver.scripting.scriptengine.events.ProfessionChangeEvent;
import king.server.gameserver.scripting.scriptengine.events.RequestBypassToServerEvent;
import king.server.gameserver.scripting.scriptengine.events.SiegeEvent;
import king.server.gameserver.scripting.scriptengine.events.SkillUseEvent;
import king.server.gameserver.scripting.scriptengine.events.TransformEvent;
import king.server.gameserver.scripting.scriptengine.events.TvtKillEvent;
import king.server.gameserver.scripting.scriptengine.events.impl.L2Event;
import king.server.gameserver.scripting.scriptengine.listeners.character.AttackListener;
import king.server.gameserver.scripting.scriptengine.listeners.character.DeathListener;
import king.server.gameserver.scripting.scriptengine.listeners.character.SkillUseListener;
import king.server.gameserver.scripting.scriptengine.listeners.clan.ClanCreationListener;
import king.server.gameserver.scripting.scriptengine.listeners.clan.ClanMembershipListener;
import king.server.gameserver.scripting.scriptengine.listeners.clan.ClanWarListener;
import king.server.gameserver.scripting.scriptengine.listeners.clan.ClanWarehouseListener;
import king.server.gameserver.scripting.scriptengine.listeners.events.FortSiegeListener;
import king.server.gameserver.scripting.scriptengine.listeners.events.SiegeListener;
import king.server.gameserver.scripting.scriptengine.listeners.events.TvTListener;
import king.server.gameserver.scripting.scriptengine.listeners.player.AugmentListener;
import king.server.gameserver.scripting.scriptengine.listeners.player.DropListener;
import king.server.gameserver.scripting.scriptengine.listeners.player.EquipmentListener;
import king.server.gameserver.scripting.scriptengine.listeners.player.HennaListener;
import king.server.gameserver.scripting.scriptengine.listeners.player.ItemTracker;
import king.server.gameserver.scripting.scriptengine.listeners.player.NewItemListener;
import king.server.gameserver.scripting.scriptengine.listeners.player.PlayerDespawnListener;
import king.server.gameserver.scripting.scriptengine.listeners.player.PlayerLevelListener;
import king.server.gameserver.scripting.scriptengine.listeners.player.PlayerListener;
import king.server.gameserver.scripting.scriptengine.listeners.player.PlayerSpawnListener;
import king.server.gameserver.scripting.scriptengine.listeners.player.ProfessionChangeListener;
import king.server.gameserver.scripting.scriptengine.listeners.player.TransformListener;
import king.server.gameserver.scripting.scriptengine.listeners.talk.ChatFilterListener;
import king.server.gameserver.scripting.scriptengine.listeners.talk.ChatListener;
import king.server.gameserver.scripting.scriptengine.listeners.talk.DlgAnswerListener;
import king.server.gameserver.scripting.scriptengine.listeners.talk.RequestBypassToServerListener;
import king.server.gameserver.util.Util;

/**
 * L2Script is an extension of Quest.java which makes use of the L2J listeners.<br>
 * It is much more in-depth than its predecessor,<br>
 * It is strongly recommended for the more advanced developers.<br>
 * Methods with boolean return values can be used as code blockers.<br>
 * This means that if the return is false, the action for which the listener was fired does not happen.<br>
 * New in this version: profession change + player level change.<br>
 * TODO: pet item use listeners.<br>
 * TODO: player subclass listeners ?? (needed?)
 * @author TheOne
 */
public abstract class L2Script extends Quest
{
	private final List<L2JListener> _listeners = new ArrayList<>();
	
	/**
	 * constructor
	 * @param name
	 * @param descr
	 */
	public L2Script(String name, String descr)
	{
		super(-1, name, descr);
	}
	
	/**
	 * constructor
	 * @param questId
	 * @param name
	 * @param descr
	 */
	public L2Script(int questId, String name, String descr)
	{
		super(questId, name, descr);
	}
	
	/**
	 * Unloads the script
	 */
	@Override
	public boolean unload()
	{
		for (L2JListener listener : _listeners)
		{
			listener.unregister();
		}
		_listeners.clear();
		return super.unload();
	}
	
	/**
	 * Unregisters the listeners and removes them from the listeners list
	 * @param removeList
	 */
	private void removeListeners(List<L2JListener> removeList)
	{
		for (L2JListener listener : removeList)
		{
			listener.unregister();
			_listeners.remove(listener);
		}
	}
	
	/**
	 * Used locally to call onDeath()
	 * @param event
	 * @return
	 */
	protected boolean notifyDeath(DeathEvent event)
	{
		return onDeath(event);
	}
	
	/**
	 * Used locally to call onAttack(L2Character,L2Character)
	 * @param event
	 * @return
	 */
	protected boolean notifyAttack(AttackEvent event)
	{
		return onAttack(event);
	}
	
	// Register for event notification
	/**
	 * Will notify the script when this L2Character is killed<br>
	 * Can be used for Npc or Player<br>
	 * When the L2Character is killed, the onDeath(L2Character,L2Character) method will be fired<br>
	 * To set a global notifier (for all L2Character) set character to null!
	 * @param character
	 */
	public void addDeathNotify(final L2Character character)
	{
		DeathListener listener = new DeathListener(character)
		{
			@Override
			public boolean onKill(DeathEvent event)
			{
				return notifyDeath(event);
			}
			
			@Override
			public boolean onDeath(DeathEvent event)
			{
				return notifyDeath(event);
			}
		};
		_listeners.add(listener);
	}
	
	/**
	 * Removes the death listener from this L2Character
	 * @param character
	 */
	public void removeDeathNotify(L2Character character)
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if (listener instanceof DeathListener)
			{
				if (((DeathListener) listener).getCharacter() == character)
				{
					removeList.add(listener);
				}
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * When a player logs in it will call the onPlayerLogin(L2PcInstance player) method<br>
	 * When a player logs out it will call the onPlayerLogout(L2PcInstance player) method<br>
	 */
	public void addLoginLogoutNotify()
	{
		PlayerSpawnListener spawn = new PlayerSpawnListener()
		{
			@Override
			public void onSpawn(L2PcInstance player)
			{
				onPlayerLogin(player);
			}
		};
		PlayerDespawnListener despawn = new PlayerDespawnListener()
		{
			@Override
			public void onDespawn(L2PcInstance player)
			{
				onPlayerLogout(player);
			}
		};
		_listeners.add(spawn);
		_listeners.add(despawn);
	}
	
	/**
	 * Removes the login and logout notifications
	 */
	public void removeLoginLogoutNotify()
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if ((listener instanceof PlayerSpawnListener) || (listener instanceof PlayerDespawnListener))
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Adds an L2Character-specific attack listener Fires onAttack(L2Character target, L2Character attacker) when this character is attacked AND when it gets attacked
	 * @param character
	 */
	public void addAttackNotify(final L2Character character)
	{
		if (character != null)
		{
			AttackListener listener = new AttackListener(character)
			{
				@Override
				public boolean onAttack(AttackEvent event)
				{
					return notifyAttack(event);
				}
				
				@Override
				public boolean isAttacked(AttackEvent event)
				{
					return notifyAttack(event);
				}
			};
			_listeners.add(listener);
		}
	}
	
	/**
	 * Removes the notifications for attacks from/on this L2Character
	 * @param character
	 */
	public void removeAttackNotify(L2Character character)
	{
		if (character != null)
		{
			List<L2JListener> removeList = new ArrayList<>();
			for (L2JListener listener : _listeners)
			{
				if ((listener instanceof AttackListener) && (((AttackListener) listener).getCharacter() == character))
				{
					removeList.add(listener);
				}
			}
			removeListeners(removeList);
		}
	}
	
	/**
	 * NPC specific, will only be triggered when npc with the given ID uses the correct skill Use skillId = -1 to be notified of all skills used Use npcId = -1 to be notified for all NPCs use npcId = -2 to be notified for all players use npcId = -3 to be notified for all L2Characters
	 * @param npcId
	 * @param skillId
	 */
	public void addSkillUseNotify(int npcId, int skillId)
	{
		SkillUseListener listener = new SkillUseListener(npcId, skillId)
		{
			@Override
			public boolean onSkillUse(SkillUseEvent event)
			{
				return onUseSkill(event);
			}
		};
		_listeners.add(listener);
	}
	
	/**
	 * L2Character specific, will only be fired when this L2Character uses the specified skill Use skillId = -1 to be notified of all skills used
	 * @param character
	 * @param skillId
	 */
	public void addSkillUseNotify(L2Character character, int skillId)
	{
		if (character != null)
		{
			SkillUseListener listener = new SkillUseListener(character, skillId)
			{
				@Override
				public boolean onSkillUse(SkillUseEvent event)
				{
					return onUseSkill(event);
				}
			};
			_listeners.add(listener);
		}
	}
	
	/**
	 * Removes a skill use listener
	 * @param character
	 */
	public void removeSkillUseNotify(L2Character character)
	{
		if (character != null)
		{
			List<L2JListener> removeList = new ArrayList<>();
			for (L2JListener listener : _listeners)
			{
				if ((listener instanceof SkillUseListener) && (((SkillUseListener) listener).getCharacter() == character))
				{
					removeList.add(listener);
				}
			}
			removeListeners(removeList);
		}
	}
	
	/**
	 * Removes a skill use listener
	 * @param npcId
	 */
	public void removeSkillUseNotify(int npcId)
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if ((listener instanceof SkillUseListener) && (((SkillUseListener) listener).getNpcId() == npcId))
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Adds a notification for when a clan is created or levels up
	 */
	public void addClanCreationLevelUpNotify()
	{
		ClanCreationListener listener = new ClanCreationListener()
		{
			@Override
			public void onClanCreate(ClanCreationEvent event)
			{
				onClanCreated(event);
			}
			
			@Override
			public boolean onClanLevelUp(ClanLevelUpEvent event)
			{
				return onClanLeveledUp(event);
			}
		};
		_listeners.add(listener);
	}
	
	/**
	 * Removes the clan creation and level up notifications
	 */
	public void removeClanCreationLevelUpNotify()
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if (listener instanceof ClanCreationListener)
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Adds a notification for players joining and leaving a clan as well as clan leadership changes
	 */
	public void addClanJoinLeaveNotify()
	{
		ClanMembershipListener listener = new ClanMembershipListener()
		{
			@Override
			public boolean onJoin(ClanJoinEvent event)
			{
				return onClanJoin(event);
			}
			
			@Override
			public boolean onLeaderChange(ClanLeaderChangeEvent event)
			{
				return onClanLeaderChange(event);
			}
			
			@Override
			public boolean onLeave(ClanLeaveEvent event)
			{
				return onClanLeave(event);
			}
		};
		_listeners.add(listener);
	}
	
	/**
	 * Removes the notification for players joining and leaving a clan as well as clan leadership changes
	 */
	public void removeClanJoinLeaveNotify()
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if (listener instanceof ClanMembershipListener)
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Adds a notification for when an item from a clanwarehouse is added, deleted or transfered
	 * @param clan
	 */
	public void addClanWarehouseNotify(L2Clan clan)
	{
		if (clan != null)
		{
			ClanWarehouseListener listener = new ClanWarehouseListener(clan)
			{
				@Override
				public boolean onAddItem(ClanWarehouseAddItemEvent event)
				{
					return onClanWarehouseAddItem(event);
				}
				
				@Override
				public boolean onDeleteItem(ClanWarehouseDeleteItemEvent event)
				{
					return onClanWarehouseDeleteItem(event);
				}
				
				@Override
				public boolean onTransferItem(ClanWarehouseTransferEvent event)
				{
					return onClanWarehouseTransferItem(event);
				}
			};
			_listeners.add(listener);
		}
	}
	
	/**
	 * Removes a clan warehouse notifier
	 * @param clan
	 */
	public void removeClanWarehouseNotify(L2Clan clan)
	{
		if (clan != null)
		{
			List<L2JListener> removeList = new ArrayList<>();
			for (L2JListener listener : _listeners)
			{
				if ((listener instanceof ClanWarehouseListener) && (((ClanWarehouseListener) listener).getWarehouse() == clan.getWarehouse()))
				{
					removeList.add(listener);
				}
			}
			removeListeners(removeList);
		}
	}
	
	/**
	 * Adds a notifier for when clan wars start and end
	 */
	public void addClanWarNotify()
	{
		ClanWarListener listener = new ClanWarListener()
		{
			@Override
			public boolean onWarStart(ClanWarEvent event)
			{
				event.setStage(EventStage.START);
				return onClanWarEvent(event);
			}
			
			@Override
			public boolean onWarEnd(ClanWarEvent event)
			{
				event.setStage(EventStage.END);
				return onClanWarEvent(event);
			}
		};
		_listeners.add(listener);
	}
	
	/**
	 * Removes the notification for start/end of clan wars
	 */
	public void removeClanWarNotify()
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if (listener instanceof ClanWarListener)
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Notifies when fort sieges start and end
	 */
	public void addFortSiegeNotify()
	{
		FortSiegeListener listener = new FortSiegeListener()
		{
			@Override
			public boolean onStart(FortSiegeEvent event)
			{
				event.setStage(EventStage.START);
				return onFortSiegeEvent(event);
			}
			
			@Override
			public void onEnd(FortSiegeEvent event)
			{
				event.setStage(EventStage.END);
				onFortSiegeEvent(event);
			}
		};
		_listeners.add(listener);
	}
	
	/**
	 * Removes the notification for fort sieges
	 */
	public void removeFortSiegeNotify()
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if (listener instanceof FortSiegeListener)
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Adds a notifier for when castle sieges start and end
	 */
	public void addSiegeNotify()
	{
		SiegeListener listener = new SiegeListener()
		{
			@Override
			public boolean onStart(SiegeEvent event)
			{
				event.setStage(EventStage.START);
				return onSiegeEvent(event);
			}
			
			@Override
			public void onEnd(SiegeEvent event)
			{
				event.setStage(EventStage.END);
				onSiegeEvent(event);
			}
			
			@Override
			public void onControlChange(SiegeEvent event)
			{
				onCastleControlChange(event);
			}
		};
		_listeners.add(listener);
	}
	
	/**
	 * Removes notification for castle sieges
	 */
	public void removeSiegeNotify()
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if (listener instanceof SiegeListener)
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Notifies of events on TvT:<br>
	 * start<br>
	 * end<br>
	 * registion begin<br>
	 * event stopped<br>
	 * player killed<br>
	 */
	public void addTvTNotify()
	{
		TvTListener listener = new TvTListener()
		{
			@Override
			public void onBegin()
			{
				onTvtEvent(EventStage.START);
			}
			
			@Override
			public void onKill(TvtKillEvent event)
			{
				onTvtKill(event);
			}
			
			@Override
			public void onEnd()
			{
				onTvtEvent(EventStage.END);
			}
			
			@Override
			public void onRegistrationStart()
			{
				onTvtEvent(EventStage.REGISTRATION_BEGIN);
			}
		};
		_listeners.add(listener);
	}
	
	/**
	 * Removes the TvT notifications
	 */
	public void removeTvtNotify()
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if (listener instanceof TvTListener)
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Adds a notifier for when items get augmented
	 */
	public void addItemAugmentNotify()
	{
		AugmentListener listener = new AugmentListener()
		{
			@Override
			public boolean onAugment(AugmentEvent event)
			{
				return onItemAugment(event);
			}
			
			@Override
			public boolean onRemoveAugment(AugmentEvent event)
			{
				return onItemAugment(event);
			}
		};
		_listeners.add(listener);
	}
	
	/**
	 * Removes the item augmentation listener
	 */
	public void removeItemAugmentNotify()
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if (listener instanceof AugmentListener)
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Adds a listener for items dropped and picked up by players
	 */
	public void addItemDropPickupNotify()
	{
		DropListener listener = new DropListener()
		{
			
			@Override
			public boolean onDrop(ItemDropEvent event)
			{
				return onItemDrop(event);
			}
			
			@Override
			public boolean onPickup(ItemPickupEvent event)
			{
				return onItemPickup(event);
			}
		};
		_listeners.add(listener);
	}
	
	/**
	 * Removes the item drop and pickup listeners
	 */
	public void removeItemDropPickupNotify()
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if (listener instanceof DropListener)
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Adds a player level change listener<br>
	 * Set player to null if you wish to be notified for all the players on the server.
	 * @param player
	 */
	public void addPlayerLevelNotify(L2PcInstance player)
	{
		PlayerLevelListener listener = new PlayerLevelListener(player)
		{
			@Override
			public void levelChanged(PlayerLevelChangeEvent event)
			{
				onPlayerLevelChange(event);
			}
		};
		_listeners.add(listener);
	}
	
	/**
	 * Removes the player level notification for the given player<br>
	 * Removes all global notifications if player = null
	 * @param player
	 */
	public void removePlayerLevelNotify(L2PcInstance player)
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if ((listener instanceof PlayerLevelListener) && (listener.getPlayer() == player))
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Adds a player profession change listener.<br>
	 * Set player to null if you wish to be notified for all the players on the server.
	 * @param player
	 */
	public void addProfessionChangeNotify(L2PcInstance player)
	{
		ProfessionChangeListener listener = new ProfessionChangeListener(player)
		{
			@Override
			public void professionChanged(ProfessionChangeEvent event)
			{
				onProfessionChange(event);
			}
		};
		_listeners.add(listener);
	}
	
	/**
	 * Removes the profession change notification for the given player<br>
	 * Removes all global notifications if player = null
	 * @param player
	 */
	public void removeProfessionChangeNotify(L2PcInstance player)
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if ((listener instanceof ProfessionChangeListener) && (listener.getPlayer() == player))
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Adds an item equip/unequip listener.<br>
	 * Set player to null if you wish to be notified for all the players on server
	 * @param player
	 */
	public void addEquipmentNotify(L2PcInstance player)
	{
		EquipmentListener listener = new EquipmentListener(player)
		{
			@Override
			public boolean onEquip(EquipmentEvent event)
			{
				return onItemEquip(event);
			}
		};
		_listeners.add(listener);
	}
	
	/**
	 * Removes an equip/unequip listener<br>
	 * Set player to null if you wish to remove a global listener
	 * @param player
	 */
	public void removeEquipmentNotify(L2PcInstance player)
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if ((listener instanceof EquipmentListener) && (((EquipmentListener) listener).getPlayer() == player))
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Adds a Henna add/remove notifier
	 */
	public void addHennaNotify()
	{
		HennaListener listener = new HennaListener()
		{
			@Override
			public boolean onAddHenna(HennaEvent event)
			{
				return onHennaModify(event);
			}
			
			@Override
			public boolean onRemoveHenna(HennaEvent event)
			{
				return onHennaModify(event);
			}
		};
		_listeners.add(listener);
	}
	
	/**
	 * Removes the henna add/remove notifier
	 */
	public void removeHennaNotify()
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if (listener instanceof HennaListener)
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Adds an item tracker notifier.<br>
	 * It will keep track of all movements for the items with the given IDs
	 * @param itemIds
	 */
	public void addItemTracker(final List<Integer> itemIds)
	{
		if (itemIds != null)
		{
			ItemTracker listener = new ItemTracker(itemIds)
			{
				@Override
				public void onDrop(ItemDropEvent event)
				{
					onItemTrackerEvent(event);
				}
				
				@Override
				public void onAddToInventory(AddToInventoryEvent event)
				{
					onItemTrackerEvent(event);
				}
				
				@Override
				public void onDestroy(ItemDestroyEvent event)
				{
					onItemTrackerEvent(event);
				}
				
				@Override
				public void onTransfer(ItemTransferEvent event)
				{
					onItemTrackerEvent(event);
				}
			};
			_listeners.add(listener);
		}
	}
	
	/**
	 * Removes all the item trackers
	 */
	public void removeItemTrackers()
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if (listener instanceof ItemTracker)
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Adds an item creation notifier
	 * @param itemIds
	 */
	public void addNewItemNotify(List<Integer> itemIds)
	{
		if (itemIds != null)
		{
			NewItemListener listener = new NewItemListener(itemIds)
			{
				
				@Override
				public boolean onCreate(ItemCreateEvent event)
				{
					return onItemCreate(event);
				}
			};
			_listeners.add(listener);
		}
	}
	
	/**
	 * Removes all new items notifiers
	 */
	public void removeNewItemNotify()
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if (listener instanceof NewItemListener)
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Adds a player transformation notifier
	 * @param player
	 */
	public void addTransformNotify(final L2PcInstance player)
	{
		if (player != null)
		{
			TransformListener listener = new TransformListener(player)
			{
				@Override
				public boolean onTransform(TransformEvent event)
				{
					event.setTransforming(true);
					return onPlayerTransform(event);
				}
				
				@Override
				public boolean onUntransform(TransformEvent event)
				{
					return onPlayerTransform(event);
				}
			};
			_listeners.add(listener);
		}
	}
	
	/**
	 * Removes a player transform notifier
	 * @param player
	 */
	public void removeTransformNotify(L2PcInstance player)
	{
		if (player != null)
		{
			List<L2JListener> removeList = new ArrayList<>();
			for (L2JListener listener : _listeners)
			{
				if ((listener instanceof TransformListener) && (listener.getPlayer() == player))
				{
					removeList.add(listener);
				}
			}
			removeListeners(removeList);
		}
	}
	
	/**
	 * Adds a chat filter
	 */
	public void addPlayerChatFilter()
	{
		ChatFilterListener listener = new ChatFilterListener()
		{
			@Override
			public String onTalk(ChatEvent event)
			{
				return filterChat(event);
			}
		};
		_listeners.add(listener);
	}
	
	/**
	 * Removes a chat filter
	 */
	public void removePlayerChatFilter()
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if (listener instanceof ChatFilterListener)
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Adds a player chat notifier
	 */
	public void addPlayerTalkNotify()
	{
		ChatListener listener = new ChatListener()
		{
			@Override
			public void onTalk(ChatEvent event)
			{
				onPlayerTalk(event);
			}
		};
		_listeners.add(listener);
	}
	
	/**
	 * Removes all player chat notifiers
	 */
	public void removePlayerTalkNotify()
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if (listener instanceof ChatListener)
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * You can use -1 to listen for all kinds of message id's
	 * @param messageIds
	 */
	public void addDlgAnswerNotify(int... messageIds)
	{
		for (int messageId : messageIds)
		{
			DlgAnswerListener dlgAnswer = new DlgAnswerListener(messageId)
			{
				@Override
				public void onDlgAnswer(DlgAnswerEvent event)
				{
					L2Script.this.onDlgAnswer(event);
				}
			};
			
			_listeners.add(dlgAnswer);
		}
	}
	
	/**
	 * Removes all Dlg Answer Listeners
	 */
	public void removeDlgAnswerNotify()
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if (listener instanceof DlgAnswerListener)
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Removes specified Dlg Answer Listeners
	 * @param messageIds
	 */
	public void removeDlgAnswerNotify(int... messageIds)
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if ((listener instanceof DlgAnswerListener) && Util.contains(messageIds, ((DlgAnswerListener) listener).getMessageId()))
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Notify when RequestBypassToServer packet is received from client.
	 */
	public void addRequestBypassToServerNotify()
	{
		RequestBypassToServerListener bypass = new RequestBypassToServerListener()
		{
			@Override
			public void onRequestBypassToServer(RequestBypassToServerEvent event)
			{
				L2Script.this.onRequestBypassToServer(event);
			}
		};
		
		_listeners.add(bypass);
	}
	
	/**
	 * Removes all RequestBypassToServer Listeners
	 */
	public void removeRequestBypassToServerNotify()
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if (listener instanceof DlgAnswerListener)
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	/**
	 * Notify on player creation/delete/restore/select action.
	 */
	public void addPlayerNotify()
	{
		PlayerListener bypass = new PlayerListener()
		{
			@Override
			public void onCharCreate(PlayerEvent event)
			{
				L2Script.this.onCharCreate(event);
				
			}
			
			@Override
			public void onCharDelete(PlayerEvent event)
			{
				L2Script.this.onCharDelete(event);
			}
			
			@Override
			public void onCharRestore(PlayerEvent event)
			{
				L2Script.this.onCharRestore(event);
			}
			
			@Override
			public void onCharSelect(PlayerEvent event)
			{
				L2Script.this.onCharSelect(event);
			}
		};
		
		_listeners.add(bypass);
	}
	
	/**
	 * Removes all player creation/delete/restore/select Listeners
	 */
	public void removePlayerNotify()
	{
		List<L2JListener> removeList = new ArrayList<>();
		for (L2JListener listener : _listeners)
		{
			if (listener instanceof PlayerListener)
			{
				removeList.add(listener);
			}
		}
		removeListeners(removeList);
	}
	
	// Script notifications
	/**
	 * Fired when a player logs in
	 * @param player
	 */
	public void onPlayerLogin(L2PcInstance player)
	{
		
	}
	
	/**
	 * Fired when a player logs out
	 * @param player
	 */
	public void onPlayerLogout(L2PcInstance player)
	{
		
	}
	
	/**
	 * Fired when a L2Character registered with addAttackNotify is either attacked or attacks another L2Character
	 * @param event
	 * @return
	 */
	public boolean onAttack(AttackEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when a L2Character registered with addNotifyDeath is either killed or kills another L2Character
	 * @param event
	 * @return
	 */
	public boolean onDeath(DeathEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when a SKillUseListener gets triggered.<br>
	 * Register using addSkillUseNotify()
	 * @param event
	 * @return
	 */
	public boolean onUseSkill(SkillUseEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when a clan is created Register the listener using addClanCreationLevelUpNotify()
	 * @param event
	 */
	public void onClanCreated(ClanCreationEvent event)
	{
		
	}
	
	/**
	 * Fired when a clan levels up<br>
	 * Register the listener using addClanCreationLevelUpListener()
	 * @param event
	 * @return
	 */
	public boolean onClanLeveledUp(ClanLevelUpEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when a player joins a clan<br>
	 * Register the listener with addClanJoinLeaveNotify()<br>
	 * @param event
	 * @return
	 */
	public boolean onClanJoin(ClanJoinEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when a player leaves a clan<br>
	 * Register the listener with addClanJoinLeaveNotify()<br>
	 * @param event
	 * @return
	 */
	public boolean onClanLeave(ClanLeaveEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when a clan leader is changed for another<br>
	 * Register the listener with addClanJoinLeaveNotify()<br>
	 * @param event
	 * @return
	 */
	public boolean onClanLeaderChange(ClanLeaderChangeEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when an item is added to a clan warehouse<br>
	 * Register the listener with addClanWarehouseNotify(L2Clan)
	 * @param event
	 * @return
	 */
	public boolean onClanWarehouseAddItem(ClanWarehouseAddItemEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when an item is deleted from a clan warehouse<br>
	 * Register the listener with addClanWarehouseNotify(L2Clan)
	 * @param event
	 * @return
	 */
	public boolean onClanWarehouseDeleteItem(ClanWarehouseDeleteItemEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when an item is transfered from/to a clan warehouse<br>
	 * Register the listener with addClanWarehouseNotify(L2Clan)
	 * @param event
	 * @return
	 */
	public boolean onClanWarehouseTransferItem(ClanWarehouseTransferEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when a clan war starts or ends<br>
	 * Register the listener witn addClanWarNotify()
	 * @param event
	 * @return
	 */
	public boolean onClanWarEvent(ClanWarEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when a fort siege starts or ends<br>
	 * Register using addFortSiegeNotify()
	 * @param event
	 * @return
	 */
	public boolean onFortSiegeEvent(FortSiegeEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when a castle siege starts or ends<br>
	 * Register using addSiegeNotify()
	 * @param event
	 * @return
	 */
	public boolean onSiegeEvent(SiegeEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when the control of a castle changes during a siege<br>
	 * Register using addSiegeNotify()
	 * @param event
	 */
	public void onCastleControlChange(SiegeEvent event)
	{
		
	}
	
	/**
	 * Notifies of TvT events<br>
	 * Register using addTvtNotify()
	 * @param stage
	 */
	public void onTvtEvent(EventStage stage)
	{
		
	}
	
	/**
	 * Notifies that a player was killed during TvT<br>
	 * Register using addTvtNotify()
	 * @param event
	 */
	public void onTvtKill(TvtKillEvent event)
	{
		
	}
	
	/**
	 * triggered when an item is augmented or when the augmentation is removed<br>
	 * Register using addItemAugmentNotify()
	 * @param event
	 * @return
	 */
	public boolean onItemAugment(AugmentEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when an item is dropped by a player<br>
	 * Register using addItemDropPickupNotify()
	 * @param event
	 * @return
	 */
	public boolean onItemDrop(ItemDropEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when an item is picked up by a player<br>
	 * Register using addItemDropPickupNotify()
	 * @param event
	 * @return
	 */
	public boolean onItemPickup(ItemPickupEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when an item is equipped or unequipped<br>
	 * Register using addEquipmentNotify()
	 * @param event
	 * @return
	 */
	public boolean onItemEquip(EquipmentEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when a player's level changes<br>
	 * Register using addPlayerLevelNotify(player)
	 * @param event
	 */
	public void onPlayerLevelChange(PlayerLevelChangeEvent event)
	{
		
	}
	
	/**
	 * Fired when a player changes profession<br>
	 * Register using addProfessionChangeNotify(player)
	 * @param event
	 */
	public void onProfessionChange(ProfessionChangeEvent event)
	{
		
	}
	
	/**
	 * Fired when a player's henna changes (add/remove)<br>
	 * Register using addHennaNotify()
	 * @param event
	 * @return
	 */
	public boolean onHennaModify(HennaEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when an item on the item tracker list has an event<br>
	 * Register using addItemTracker(itemIds)
	 * @param event
	 */
	public void onItemTrackerEvent(L2Event event)
	{
		
	}
	
	/**
	 * Fired when an item is created<br>
	 * Register using addNewItemNotify(itemIds)
	 * @param event
	 * @return
	 */
	public boolean onItemCreate(ItemCreateEvent event)
	{
		return true;
	}
	
	/**
	 * Fired when a player transforms/untransforms<br>
	 * Register using addTransformNotify(player)
	 * @param event
	 * @return
	 */
	public boolean onPlayerTransform(TransformEvent event)
	{
		return true;
	}
	
	/**
	 * Allows for custom chat filtering<br>
	 * Fired each time a player writes something in any form of chat<br>
	 * Register using addPlayerChatFilter()
	 * @param event
	 * @return
	 */
	public String filterChat(ChatEvent event)
	{
		return "";
	}
	
	/**
	 * Fired when a player writes some text in chat<br>
	 * Register using addPlayerTalkNotify()
	 * @param event
	 */
	public void onPlayerTalk(ChatEvent event)
	{
		
	}
	
	/**
	 * Fired when client answer on dialog request<br>
	 * Register using addDlgAnswerNotify()
	 * @param event
	 */
	public void onDlgAnswer(DlgAnswerEvent event)
	{
		
	}
	
	/**
	 * Fired when client answer on dialog request<br>
	 * Register using addRequestBypassToServerNotify()
	 * @param event
	 */
	protected void onRequestBypassToServer(RequestBypassToServerEvent event)
	{
		
	}
	
	/**
	 * Fired when client select a player<br>
	 * Register using addPlayerNotify()
	 * @param event
	 */
	protected void onCharSelect(PlayerEvent event)
	{
		
	}
	
	/**
	 * Fired when client create a character<br>
	 * Register using addPlayerNotify()
	 * @param event
	 */
	protected void onCharCreate(PlayerEvent event)
	{
		
	}
	
	/**
	 * Fired when client select a character for delete<br>
	 * Register using addPlayerNotify()
	 * @param event
	 */
	protected void onCharDelete(PlayerEvent event)
	{
		
	}
	
	/**
	 * Fired when client select a character for restore<br>
	 * Register using addPlayerNotify()
	 * @param event
	 */
	protected void onCharRestore(PlayerEvent event)
	{
		
	}
	
	// Enums
	
	public enum EventStage
	{
		START,
		END,
		EVENT_STOPPED,
		REGISTRATION_BEGIN,
		CONTROL_CHANGE
	}
	
	public enum ItemTrackerEvent
	{
		DROP,
		ADD_TO_INVENTORY,
		DESTROY,
		TRANSFER
	}
}
