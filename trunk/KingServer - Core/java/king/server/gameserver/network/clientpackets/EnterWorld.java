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
package king.server.gameserver.network.clientpackets;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javolution.util.FastList;

import king.server.Config;
import king.server.gameserver.Announcements;
import king.server.gameserver.LoginServerThread;
import king.server.gameserver.SevenSigns;
import king.server.gameserver.TaskPriority;
import king.server.gameserver.cache.HtmCache;
import king.server.gameserver.communitybbs.Manager.RegionBBSManager;
import king.server.gameserver.custom.PvPColorSystem;
import king.server.gameserver.datatables.AdminTable;
import king.server.gameserver.datatables.SkillTable;
import king.server.gameserver.datatables.SkillTreesData;
import king.server.gameserver.instancemanager.BotManager;
import king.server.gameserver.instancemanager.CHSiegeManager;
import king.server.gameserver.instancemanager.CastleManager;
import king.server.gameserver.instancemanager.ClanHallManager;
import king.server.gameserver.instancemanager.CoupleManager;
import king.server.gameserver.instancemanager.CursedWeaponsManager;
import king.server.gameserver.instancemanager.DimensionalRiftManager;
import king.server.gameserver.instancemanager.FortManager;
import king.server.gameserver.instancemanager.FortSiegeManager;
import king.server.gameserver.instancemanager.InstanceManager;
import king.server.gameserver.instancemanager.MailManager;
import king.server.gameserver.instancemanager.MapRegionManager;
import king.server.gameserver.instancemanager.PetitionManager;
import king.server.gameserver.instancemanager.QuestManager;
import king.server.gameserver.instancemanager.SiegeManager;
import king.server.gameserver.instancemanager.TerritoryWarManager;
import king.server.gameserver.model.L2Clan;
import king.server.gameserver.model.L2Object;
import king.server.gameserver.model.L2World;
import king.server.gameserver.model.PcCondOverride;
import king.server.gameserver.model.actor.instance.L2ClassMasterInstance;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.entity.Castle;
import king.server.gameserver.model.entity.Couple;
import king.server.gameserver.model.entity.Fort;
import king.server.gameserver.model.entity.FortSiege;
import king.server.gameserver.model.entity.Hitman;
import king.server.gameserver.model.entity.L2Event;
import king.server.gameserver.model.entity.Siege;
import king.server.gameserver.model.entity.TvTEvent;
import king.server.gameserver.model.entity.TvTRoundEvent;
import king.server.gameserver.model.entity.clanhall.AuctionableHall;
import king.server.gameserver.model.entity.clanhall.SiegableHall;
import king.server.gameserver.model.items.instance.L2ItemInstance;
import king.server.gameserver.model.quest.Quest;
import king.server.gameserver.model.quest.QuestState;
import king.server.gameserver.model.zone.ZoneId;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.communityserver.CommunityServerThread;
import king.server.gameserver.network.communityserver.writepackets.WorldInfo;
import king.server.gameserver.network.serverpackets.Die;
import king.server.gameserver.network.serverpackets.EtcStatusUpdate;
import king.server.gameserver.network.serverpackets.ExBasicActionList;
import king.server.gameserver.network.serverpackets.ExGetBookMarkInfoPacket;
import king.server.gameserver.network.serverpackets.ExNevitAdventEffect;
import king.server.gameserver.network.serverpackets.ExNevitAdventPointInfoPacket;
import king.server.gameserver.network.serverpackets.ExNevitAdventTimeChange;
import king.server.gameserver.network.serverpackets.ExNoticePostArrived;
import king.server.gameserver.network.serverpackets.ExNotifyPremiumItem;
import king.server.gameserver.network.serverpackets.ExPCCafePointInfo;
import king.server.gameserver.network.serverpackets.ExShowContactList;
import king.server.gameserver.network.serverpackets.ExShowScreenMessage;
import king.server.gameserver.network.serverpackets.ExStorageMaxCount;
import king.server.gameserver.network.serverpackets.ExVoteSystemInfo;
import king.server.gameserver.network.serverpackets.FriendList;
import king.server.gameserver.network.serverpackets.HennaInfo;
import king.server.gameserver.network.serverpackets.ItemList;
import king.server.gameserver.network.serverpackets.NpcHtmlMessage;
import king.server.gameserver.network.serverpackets.PledgeShowMemberListAll;
import king.server.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import king.server.gameserver.network.serverpackets.PledgeSkillList;
import king.server.gameserver.network.serverpackets.PledgeStatusChanged;
import king.server.gameserver.network.serverpackets.QuestList;
import king.server.gameserver.network.serverpackets.ShortCutInit;
import king.server.gameserver.network.serverpackets.SkillCoolTime;
import king.server.gameserver.network.serverpackets.SystemMessage;
import king.server.gameserver.network.serverpackets.ExRedSky;
import king.server.gameserver.network.serverpackets.PlaySound;
import king.server.gameserver.scripting.scriptengine.listeners.player.PlayerSpawnListener;
import king.server.gameserver.util.Util;
import king.server.util.Base64;

/**
 * Enter World Packet Handler
 * <p>
 * <p>
 * 0000: 03
 * <p>
 * packet format rev87 bddddbdcccccccccccccccccccc
 * <p>
 */
public class EnterWorld extends L2GameClientPacket
{
	private static final String _C__11_ENTERWORLD = "[C] 11 EnterWorld";
	
	private static FastList<PlayerSpawnListener> listeners = new FastList<PlayerSpawnListener>().shared();
	
	long _daysleft;
	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	
	private final int[][] tracert = new int[5][4];

	public TaskPriority getPriority()
	{
		return TaskPriority.PR_URGENT;
	}
	
	@Override
	protected void readImpl()
	{
		readB(new byte[32]); // Unknown Byte Array
		readD(); // Unknown Value
		readD(); // Unknown Value
		readD(); // Unknown Value
		readD(); // Unknown Value
		readB(new byte[32]); // Unknown Byte Array
		readD(); // Unknown Value
		for (int i = 0; i < 5; i++)
		{
			for (int o = 0; o < 4; o++)
			{
				tracert[i][o] = readC();
			}
		}
	}

	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		
		PvPColorSystem pvpcolor = new PvPColorSystem();
		pvpcolor.updateNameColor(activeChar);
		pvpcolor.updateTitleColor(activeChar);
		
		if (activeChar == null)
		{
			_log.warning("EnterWorld failed! activeChar returned 'null'.");
			getClient().closeNow();
			return;
		}
		
		String[] adress = new String[5];
		for (int i = 0; i < 5; i++)
		{
			adress[i] = tracert[i][0] + "." + tracert[i][1] + "." + tracert[i][2] + "." + tracert[i][3];
		}
		
		LoginServerThread.getInstance().sendClientTracert(activeChar.getAccountName(), adress);
		
		getClient().setClientTracert(tracert);
		
		// Restore to instanced area if enabled
		if (Config.RESTORE_PLAYER_INSTANCE)
		{
			activeChar.setInstanceId(InstanceManager.getInstance().getPlayerInstance(activeChar.getObjectId()));
		}
		else
		{
			int instanceId = InstanceManager.getInstance().getPlayerInstance(activeChar.getObjectId());
			if (instanceId > 0)
			{
				InstanceManager.getInstance().getInstance(instanceId).removePlayer(activeChar.getObjectId());
			}
		}
		
		if (L2World.getInstance().findObject(activeChar.getObjectId()) != null)
		{
			if (Config.DEBUG)
			{
				_log.warning("User already exists in Object ID map! User " + activeChar.getName() + " is a character clone.");
			}
		}
		
		if (Config.PROTECT_ENCHANT_ENABLE)
		{
			for (L2ItemInstance i : activeChar.getInventory().getItems())
			{
				if (!activeChar.isGM())
				{       
					if (i.isEquipable())
					{
						if (i.getEnchantLevel() > Config.MAX_ENCHANT_LEVEL_PROTECT)
						{
							//Delete Item Over enchanted
							activeChar.getInventory().destroyItem(null, i, activeChar, null);
							//Message to Player
							activeChar.sendMessage("[Server]:Voce possui itens acima do level permitido!");
							activeChar.sendMessage("[Server]:Esta acao e proibida, voce sera expulso!");
							//If Audit is only a Kick, with this the player goes in Jail for 1.200 minutes
							activeChar.setPunishLevel(L2PcInstance.PunishLevel.JAIL, Config.ENCHANT_PROTECT_PUNISH);
							//Log in console
							_log.info("#### ATTENCTION ####");
							_log.info(i+" item has been removed from player.");
						}
					}
				}
			}
		} 

		// Apply special GM properties to the GM when entering
		if (activeChar.isGM())
		{
			if (Config.GM_STARTUP_INVULNERABLE && AdminTable.getInstance().hasAccess("admin_invul", activeChar.getAccessLevel()))
			{
				activeChar.setIsInvul(true);
			}
			
			if(Config.GM_SUPER_HASTE)
			{
				SkillTable.getInstance().getInfo(7029, 3).getEffects(activeChar, activeChar);
			}
			
			if (Config.GM_STARTUP_INVISIBLE && AdminTable.getInstance().hasAccess("admin_invisible", activeChar.getAccessLevel()))
			{
				activeChar.getAppearance().setInvisible();
			}
			
			if (Config.GM_STARTUP_SILENCE && AdminTable.getInstance().hasAccess("admin_silence", activeChar.getAccessLevel()))
			{
				activeChar.setSilenceMode(true);
			}
			
			if (Config.GM_STARTUP_DIET_MODE && AdminTable.getInstance().hasAccess("admin_diet", activeChar.getAccessLevel()))
			{
				activeChar.setDietMode(true);
				activeChar.refreshOverloaded();
			}
			
			if (Config.GM_STARTUP_AUTO_LIST && AdminTable.getInstance().hasAccess("admin_gmliston", activeChar.getAccessLevel()))
			{
				AdminTable.getInstance().addGm(activeChar, false);
			}
			else
			{
				AdminTable.getInstance().addGm(activeChar, true);
			}
			
			if (Config.GM_GIVE_SPECIAL_SKILLS)
			{
				SkillTreesData.getInstance().addSkills(activeChar, false);
			}
			
			if (Config.GM_GIVE_SPECIAL_AURA_SKILLS)
			{
				SkillTreesData.getInstance().addSkills(activeChar, true);
			}
		}
		
		// Set dead status if applies
		if (activeChar.getCurrentHp() < 0.5)
		{
			activeChar.setIsDead(true);
			
            // Make Sky Red For 7 Seconds.
            ExRedSky packet = new ExRedSky(7);
            sendPacket(packet);
            
            // Play Custom Game Over Music
            PlaySound death_music = new PlaySound(1, "Game_Over", 0, 0, 0, 0, 0);
            sendPacket(death_music);
		}
		
		boolean showClanNotice = false;
		
		// Clan related checks are here
		if (activeChar.getClan() != null)
		{
			activeChar.sendPacket(new PledgeSkillList(activeChar.getClan()));
			
			notifyClanMembers(activeChar);
			
			notifySponsorOrApprentice(activeChar);
			
			AuctionableHall clanHall = ClanHallManager.getInstance().getClanHallByOwner(activeChar.getClan());
			
			if (clanHall != null)
			{
				if (!clanHall.getPaid())
				{
					activeChar.sendPacket(SystemMessageId.PAYMENT_FOR_YOUR_CLAN_HALL_HAS_NOT_BEEN_MADE_PLEASE_MAKE_PAYMENT_TO_YOUR_CLAN_WAREHOUSE_BY_S1_TOMORROW);
				}
			}
			
			for (Siege siege : SiegeManager.getInstance().getSieges())
			{
				if (!siege.getIsInProgress())
				{
					continue;
				}
				
				if (siege.checkIsAttacker(activeChar.getClan()))
				{
					activeChar.setSiegeState((byte) 1);
					activeChar.setSiegeSide(siege.getCastle().getCastleId());
				}
				
				else if (siege.checkIsDefender(activeChar.getClan()))
				{
					activeChar.setSiegeState((byte) 2);
					activeChar.setSiegeSide(siege.getCastle().getCastleId());
				}
			}
			
			for (FortSiege siege : FortSiegeManager.getInstance().getSieges())
			{
				if (!siege.getIsInProgress())
				{
					continue;
				}
				
				if (siege.checkIsAttacker(activeChar.getClan()))
				{
					activeChar.setSiegeState((byte) 1);
					activeChar.setSiegeSide(siege.getFort().getFortId());
				}
				
				else if (siege.checkIsDefender(activeChar.getClan()))
				{
					activeChar.setSiegeState((byte) 2);
					activeChar.setSiegeSide(siege.getFort().getFortId());
				}
			}
			
			for (SiegableHall hall : CHSiegeManager.getInstance().getConquerableHalls().values())
			{
				if (!hall.isInSiege())
				{
					continue;
				}
				
				if (hall.isRegistered(activeChar.getClan()))
				{
					activeChar.setSiegeState((byte) 1);
					activeChar.setSiegeSide(hall.getId());
					activeChar.setIsInHideoutSiege(true);
				}
			}
			
			sendPacket(new PledgeShowMemberListAll(activeChar.getClan(), activeChar));
			sendPacket(new PledgeStatusChanged(activeChar.getClan()));
			
			// Residential skills support
			if (activeChar.getClan().getCastleId() > 0)
			{
				CastleManager.getInstance().getCastleByOwner(activeChar.getClan()).giveResidentialSkills(activeChar);
			}
			
			if (activeChar.getClan().getFortId() > 0)
			{
				FortManager.getInstance().getFortByOwner(activeChar.getClan()).giveResidentialSkills(activeChar);
			}
			
			showClanNotice = activeChar.getClan().isNoticeEnabled();
		}
		
		if (TerritoryWarManager.getInstance().getRegisteredTerritoryId(activeChar) > 0)
		{
			if (TerritoryWarManager.getInstance().isTWInProgress())
			{
				activeChar.setSiegeState((byte) 1);
			}
			activeChar.setSiegeSide(TerritoryWarManager.getInstance().getRegisteredTerritoryId(activeChar));
		}
		
		// Updating Seal of Strife Buff/Debuff
		if (SevenSigns.getInstance().isSealValidationPeriod() && (SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_STRIFE) != SevenSigns.CABAL_NULL))
		{
			int cabal = SevenSigns.getInstance().getPlayerCabal(activeChar.getObjectId());
			if (cabal != SevenSigns.CABAL_NULL)
			{
				if (cabal == SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_STRIFE))
				{
					activeChar.addSkill(SkillTable.FrequentSkill.THE_VICTOR_OF_WAR.getSkill());
				}
				else
				{
					activeChar.addSkill(SkillTable.FrequentSkill.THE_VANQUISHED_OF_WAR.getSkill());
				}
			}
		}
		else
		{
			activeChar.removeSkill(SkillTable.FrequentSkill.THE_VICTOR_OF_WAR.getSkill());
			activeChar.removeSkill(SkillTable.FrequentSkill.THE_VANQUISHED_OF_WAR.getSkill());
		}
		
		if (Config.ENABLE_VITALITY && Config.RECOVER_VITALITY_ON_RECONNECT)
		{
			float points = (Config.RATE_RECOVERY_ON_RECONNECT * (System.currentTimeMillis() - activeChar.getLastAccess())) / 60000;
			if (points > 0)
			{
				activeChar.updateVitalityPoints(points, false, true);
			}
		}
		
		activeChar.checkRecoBonusTask();
		
		activeChar.broadcastUserInfo();
		
		// Send Macro List
		activeChar.getMacros().sendUpdate();
		
		// Send Item List
		sendPacket(new ItemList(activeChar, false));
		
		// Send GG check
		activeChar.queryGameGuard();
		
		// Send Teleport Bookmark List
		sendPacket(new ExGetBookMarkInfoPacket(activeChar));
		
		// Send Shortcuts
		sendPacket(new ShortCutInit(activeChar));
		
		// Send Action list
		activeChar.sendPacket(ExBasicActionList.getStaticPacket(activeChar));
		
		// Send Skill list
		activeChar.sendSkillList();
		
		// Send Dye Information
		activeChar.sendPacket(new HennaInfo(activeChar));
		
		Quest.playerEnter(activeChar);
		
		if (!Config.DISABLE_TUTORIAL)
		{
			loadTutorial(activeChar);
		}
		
		for (Quest quest : QuestManager.getInstance().getAllManagedScripts())
		{
			if ((quest != null) && quest.getOnEnterWorld())
			{
				quest.notifyEnterWorld(activeChar);
			}
		}
		activeChar.sendPacket(new QuestList());
		
		if (Config.PLAYER_SPAWN_PROTECTION > 0)
		{
			activeChar.setProtection(true);
		}
		
		activeChar.spawnMe(activeChar.getX(), activeChar.getY(), activeChar.getZ());
		
		activeChar.getInventory().applyItemSkills();
		
		if (L2Event.isParticipant(activeChar))
		{
			L2Event.restorePlayerEventStatus(activeChar);
		}
		
		// Wedding Checks
		if (Config.L2JMOD_ALLOW_WEDDING)
		{
			engage(activeChar);
			notifyPartner(activeChar, activeChar.getPartnerId());
		}
		
		if (activeChar.isCursedWeaponEquipped())
		{
			CursedWeaponsManager.getInstance().getCursedWeapon(activeChar.getCursedWeaponEquippedId()).cursedOnLogin();
		}
		
		activeChar.updateEffectIcons();
		
		if (Config.PC_BANG_ENABLED)
		{
			if (activeChar.getPcBangPoints() > 0)
			{
				activeChar.sendPacket(new ExPCCafePointInfo(activeChar.getPcBangPoints(), 0, 1));
			}
			else
			{
				activeChar.sendPacket(new ExPCCafePointInfo());
			}
		}
		
		activeChar.sendPacket(new EtcStatusUpdate(activeChar));
		
		// Expand Skill
		activeChar.sendPacket(new ExStorageMaxCount(activeChar));
		
		sendPacket(new FriendList(activeChar));
		
		SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.FRIEND_S1_HAS_LOGGED_IN);
		sm.addString(activeChar.getName());
		for (int id : activeChar.getFriendList())
		{
			L2Object obj = L2World.getInstance().findObject(id);
			if (obj != null)
			{
				obj.sendPacket(sm);
			}
		}
		
		activeChar.sendPacket(SystemMessageId.WELCOME_TO_LINEAGE);
		SevenSigns.getInstance().sendCurrentPeriodMsg(activeChar);
		Announcements.getInstance().showAnnouncements(activeChar);
		activeChar.sendMessage("Este servidor usa o projeto KingServer");
		activeChar.sendMessage("Desenvolvedor: KingHanker");
		 //Information when character is logged
        activeChar.sendMessage("Seja Bem vindo: " + activeChar.getName());
        activeChar.sendMessage("Sua classe: " + activeChar.getClassId());
        activeChar.sendMessage("PvP Kills: " + activeChar.getPvpKills());
        activeChar.sendMessage("PK Kills: " + activeChar.getPkKills());
        activeChar.sendMessage("Adena em sua bolsa: " + activeChar.getAdena());
        
		if(Config.SHOW_ONLINE_PLAYERS_ON_LOGIN)
		{
			activeChar.sendMessage("==================================");
			activeChar.sendMessage("Temos em todo o mundo: "  + L2World.getInstance().getAllPlayers().size()+" Jogadores(s) OnLine") ;
			activeChar.sendMessage("==================================");
		}
        
		if (showClanNotice)
		{
			NpcHtmlMessage notice = new NpcHtmlMessage(1);
			notice.setFile(activeChar.getHtmlPrefix(), "data/html/clanNotice.htm");
			notice.replace("%clan_name%", activeChar.getClan().getName());
			notice.replace("%notice_text%", activeChar.getClan().getNotice());
			notice.disableValidation();
			sendPacket(notice);
		}
		else if (Config.SERVER_NEWS)
		{
			String serverNews = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/servnews.htm");
			if (serverNews != null)
			{
				sendPacket(new NpcHtmlMessage(1, serverNews));
			}
		}
		
		// Bot manager punishment
		if (Config.ENABLE_BOTREPORT)
		{
			BotManager.getInstance().onEnter(activeChar);
		}
		
		// Clan Leader Color Name
		if (!activeChar.isGM() && ((activeChar.getClan() != null) && activeChar.isClanLeader() && Config.CLAN_LEADER_COLOR_ENABLED && (activeChar.getClan().getLevel() >= Config.CLAN_LEADER_COLOR_CLAN_LEVEL)))
		{
			activeChar.getAppearance().setNameColor(Config.CLAN_LEADER_COLOR);
		}
		
		if (Config.ANNOUNCE_CASTLE_LORDS)
		{
			notifyCastleOwner(activeChar);
		}
		
		if (Config.PETITIONING_ALLOWED)
		{
			PetitionManager.getInstance().checkPetitionMessages(activeChar);
		}
		
		if (activeChar.isAlikeDead()) // dead or fake dead
		{
			// no broadcast needed since the player will already spawn dead to others
			sendPacket(new Die(activeChar));
		}
		
		activeChar.onPlayerEnter();
		
		sendPacket(new SkillCoolTime(activeChar));
		sendPacket(new ExVoteSystemInfo(activeChar));
		sendPacket(new ExNevitAdventEffect(0));
		sendPacket(new ExNevitAdventPointInfoPacket(activeChar));
		sendPacket(new ExNevitAdventTimeChange(activeChar.getAdventTime(), true));
		sendPacket(new ExShowContactList(activeChar));
		
		activeChar.sendAdventPointMsg();
		
		for (L2ItemInstance i : activeChar.getInventory().getItems())
		{
			if (i.isTimeLimitedItem())
			{
				i.scheduleLifeTimeTask();
			}
			if (i.isShadowItem() && i.isEquipped())
			{
				i.decreaseMana(false);
			}
		}
		for (L2ItemInstance i : activeChar.getWarehouse().getItems())
		{
			if (i.isTimeLimitedItem())
			{
				i.scheduleLifeTimeTask();
			}
		}
		for (L2ItemInstance items : activeChar.getInventory().getItems())
		{
			if (!activeChar.isGM() && items.isEquipable() && (items.getEnchantLevel() > Config.MAX_ENCHANT_LEVEL))
			{
				activeChar.getInventory().destroyItem(null, items, activeChar, null);
				Util.handleIllegalPlayerAction(activeChar, "Jogador " + activeChar.getName() + " tem item encantado acima do permitido! ", Config.DEFAULT_PUNISH);
				_log.info(items + " item foi removido " + activeChar);
			}
		}
		
		if (DimensionalRiftManager.getInstance().checkIfInRiftZone(activeChar.getX(), activeChar.getY(), activeChar.getZ(), false))
		{
			DimensionalRiftManager.getInstance().teleportToWaitingRoom(activeChar);
		}
		
		if (activeChar.getClanJoinExpiryTime() > System.currentTimeMillis())
		{
			activeChar.sendPacket(SystemMessageId.CLAN_MEMBERSHIP_TERMINATED);
		}
		
		// remove combat flag before teleporting
		if (activeChar.getInventory().getItemByItemId(9819) != null)
		{
			Fort fort = FortManager.getInstance().getFort(activeChar);
			
			if (fort != null)
			{
				FortSiegeManager.getInstance().dropCombatFlag(activeChar, fort.getFortId());
			}
			else
			{
				int slot = activeChar.getInventory().getSlotFromItem(activeChar.getInventory().getItemByItemId(9819));
				activeChar.getInventory().unEquipItemInBodySlot(slot);
				activeChar.destroyItem("CombatFlag", activeChar.getInventory().getItemByItemId(9819), null, true);
			}
		}
		
		// Attacker or spectator logging in to a siege zone.
		// Actually should be checked for inside castle only?
		if (!activeChar.canOverrideCond(PcCondOverride.ZONE_CONDITIONS) && activeChar.isInsideZone(ZoneId.SIEGE) && (!activeChar.isInSiege() || (activeChar.getSiegeState() < 2)))
		{
			activeChar.teleToLocation(MapRegionManager.TeleportWhereType.Town);
		}
		
		if (Config.ALLOW_MAIL)
		{
			if (MailManager.getInstance().hasUnreadPost(activeChar))
			{
				sendPacket(ExNoticePostArrived.valueOf(false));
			}
		}
		
		RegionBBSManager.getInstance().changeCommunityBoard();
		CommunityServerThread.getInstance().sendPacket(new WorldInfo(activeChar, null, WorldInfo.TYPE_UPDATE_PLAYER_STATUS));
		
		if (activeChar.isAio())
		{
			onEnterAio(activeChar);
		}
		
		if (Config.ALLOW_AIO_NCOLOR && activeChar.isAio())
		{
			activeChar.getAppearance().setNameColor(Config.AIO_NCOLOR);
		}
		
		if (Config.ALLOW_AIO_TCOLOR && activeChar.isAio())
		{
			activeChar.getAppearance().setTitleColor(Config.AIO_TCOLOR);
		}
		
		if(activeChar.isVip())
		{
			onEnterVip(activeChar);
		}
		
		if(Config.ALLOW_VIP_NCOLOR && activeChar.isVip())
			activeChar.getAppearance().setNameColor(Config.VIP_NCOLOR);
			               
		if(Config.ALLOW_VIP_TCOLOR && activeChar.isVip())
			activeChar.getAppearance().setTitleColor(Config.VIP_TCOLOR);

		TvTEvent.onLogin(activeChar);
		TvTRoundEvent.onLogin(activeChar);
		
		if (Config.WELCOME_MESSAGE_ENABLED)
		{
			activeChar.sendPacket(new ExShowScreenMessage(Config.WELCOME_MESSAGE_TEXT, Config.WELCOME_MESSAGE_TIME));
		}
		
		L2ClassMasterInstance.showQuestionMark(activeChar);
		if (Config.ALLOW_HITMAN_GDE)
		{
			Hitman.getInstance().onEnterWorld(activeChar);
		}
		
		int birthday = activeChar.checkBirthDay();
		if (birthday == 0)
		{
			activeChar.sendPacket(SystemMessageId.YOUR_BIRTHDAY_GIFT_HAS_ARRIVED);
			// activeChar.sendPacket(new ExBirthdayPopup()); Removed in H5?
		}
		else if (birthday != -1)
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.THERE_ARE_S1_DAYS_UNTIL_YOUR_CHARACTERS_BIRTHDAY);
			sm.addString(Integer.toString(birthday));
			activeChar.sendPacket(sm);
		}
		
		if (!activeChar.getPremiumItemList().isEmpty())
		{
			activeChar.sendPacket(ExNotifyPremiumItem.STATIC_PACKET);
		}
		
		for (PlayerSpawnListener listener : listeners)
		{
			listener.onSpawn(activeChar);
		}
	}
	
	/**
	 * @param cha
	 */
	private void engage(L2PcInstance cha)
	{
		int _chaid = cha.getObjectId();
		
		for (Couple cl : CoupleManager.getInstance().getCouples())
		{
			if ((cl.getPlayer1Id() == _chaid) || (cl.getPlayer2Id() == _chaid))
			{
				if (cl.getMaried())
				{
					cha.setMarried(true);
				}
				
				cha.setCoupleId(cl.getId());
				
				if (cl.getPlayer1Id() == _chaid)
				{
					cha.setPartnerId(cl.getPlayer2Id());
				}
				else
				{
					cha.setPartnerId(cl.getPlayer1Id());
				}
			}
		}
	}
	
	/**
	 * @param cha
	 * @param partnerId
	 */
	private void notifyPartner(L2PcInstance cha, int partnerId)
	{
		if (cha.getPartnerId() != 0)
		{
			int objId = cha.getPartnerId();
			
			try
			{
				L2PcInstance partner = L2World.getInstance().getPlayer(objId);
				
				if (partner != null)
				{
					partner.sendMessage("Your Partner has logged in.");
				}
				
				partner = null;
			}
			catch (ClassCastException cce)
			{
				_log.warning("Wedding Error: ID " + objId + " is now owned by a(n) " + L2World.getInstance().findObject(objId).getClass().getSimpleName());
			}
		}
	}
	

	private void onEnterAio(L2PcInstance activeChar)
	{
		long now = Calendar.getInstance().getTimeInMillis();
		long endDay = activeChar.getAioEndTime();
		if (now > endDay)
		{
			activeChar.setAio(false);
			activeChar.setAioEndTime(0);
			activeChar.lostAioSkills();
			activeChar.sendMessage("[Aio System]: Removido seu status de AIO... periodo terminou ");
		}
		else
		{
			Date dt = new Date(endDay);
			_daysleft = (endDay - now) / 86400000;
			if (_daysleft > 30)
			{
				activeChar.sendMessage("[Aio System]: Periodo termina em " + df.format(dt) + ". aproveite o jogo");
			}
			else if (_daysleft > 0)
			{
				activeChar.sendMessage("[Aio System]: deixou " + (int) _daysleft + " para o perido de Aio terminar");
			}
			else if (_daysleft < 1)
			{
				long hour = (endDay - now) / 3600000;
				activeChar.sendMessage("[Aio System]: deixou " + (int) hour + " horas para o periodo de Aio terminar");
			}
		}
	}

       private void onEnterVip(L2PcInstance activeChar)
       {
               long now = Calendar.getInstance().getTimeInMillis();
               long endDay = activeChar.getVipEndTime();
               if(now > endDay)
               {
                       activeChar.setVip(false);
                       activeChar.setVipEndTime(0);
                       activeChar.sendMessage("[Vip System]: Removido seu status de VIP... periodo terminou  ");
               }
               else
               {
                       Date dt = new Date(endDay);
                       _daysleft = (endDay - now)/86400000;
                       if(_daysleft > 30)
                               activeChar.sendMessage("[Vip System]: Periodo termina em " + df.format(dt) + ". aproveite o jogo");
                       else if(_daysleft > 0)
                               activeChar.sendMessage("deixou " + (int)_daysleft + " para o perido de Vip terminar");
                       else if(_daysleft < 1)
                       {
                               long hour = (endDay - now)/3600000;
                               activeChar.sendMessage("deixou " + (int)hour + " horas para o periodo de Vip terminar");
                       }
               }
       }

	/**
	 * @param activeChar
	 */
	private void notifyClanMembers(L2PcInstance activeChar)
	{
		L2Clan clan = activeChar.getClan();
		
		// This null check may not be needed anymore since notifyClanMembers is called from within a null check already. Please remove if we're certain it's ok to do so.
		if (clan != null)
		{
			clan.getClanMember(activeChar.getObjectId()).setPlayerInstance(activeChar);
			SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.CLAN_MEMBER_S1_LOGGED_IN);
			msg.addString(activeChar.getName());
			clan.broadcastToOtherOnlineMembers(msg, activeChar);
			msg = null;
			clan.broadcastToOtherOnlineMembers(new PledgeShowMemberListUpdate(activeChar), activeChar);
		}
	}
	
	/**
	 * @param activeChar
	 */
	private void notifySponsorOrApprentice(L2PcInstance activeChar)
	{
		if (activeChar.getSponsor() != 0)
		{
			L2PcInstance sponsor = L2World.getInstance().getPlayer(activeChar.getSponsor());
			
			if (sponsor != null)
			{
				SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.YOUR_APPRENTICE_S1_HAS_LOGGED_IN);
				msg.addString(activeChar.getName());
				sponsor.sendPacket(msg);
			}
		}
		else if (activeChar.getApprentice() != 0)
		{
			L2PcInstance apprentice = L2World.getInstance().getPlayer(activeChar.getApprentice());
			
			if (apprentice != null)
			{
				SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.YOUR_SPONSOR_C1_HAS_LOGGED_IN);
				msg.addString(activeChar.getName());
				apprentice.sendPacket(msg);
			}
		}
	}
	
	/**
	 * @param string
	 * @return
	 */
	private String getText(String string)
	{
		try
		{
			String result = new String(Base64.decode(string), "UTF-8");
			return result;
		}
		catch (UnsupportedEncodingException e)
		{
			return null;
		}
	}
	
	private void loadTutorial(L2PcInstance player)
	{
		QuestState qs = player.getQuestState("255_Tutorial");
		
		if (qs != null)
		{
			qs.getQuest().notifyEvent("UC", null, player);
		}
		
	}
	
	@Override
	public String getType()
	{
		return _C__11_ENTERWORLD;
	}
	
	@Override
	protected boolean triggersOnActionRequest()
	{
		return false;
	}
	
	// Player spawn listeners
	/**
	 * Adds a spawn listener
	 * @param listener
	 */
	public static void addSpawnListener(PlayerSpawnListener listener)
	{
		if (!listeners.contains(listener))
		{
			listeners.add(listener);
		}
	}
	
	/**
	 * Removes a spawn listener
	 * @param listener
	 */
	public static void removeSpawnListener(PlayerSpawnListener listener)
	{
		listeners.remove(listener);
	}
	
	private void notifyCastleOwner(L2PcInstance activeChar)
	{
		L2Clan clan = activeChar.getClan();
		
		if (clan != null)
		{
			if (clan.getCastleId() > 0)
			{
				Castle castle = CastleManager.getInstance().getCastleById(clan.getCastleId());
				if ((castle != null) && (activeChar.getObjectId() == clan.getLeaderId()))
				{
					Announcements.getInstance().announceToAll("Lord " + activeChar.getName() + " Ruler Of " + castle.getName() + " Castle is Now Online!");
				}
			}
		}
	}
}
