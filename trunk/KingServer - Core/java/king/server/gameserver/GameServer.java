package king.server.gameserver;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.mmocore.network.SelectorConfig;
import org.mmocore.network.SelectorThread;

import king.server.Config;
import king.server.L2DatabaseFactory;
import king.server.Server;
import king.server.gameserver.cache.CrestCache;
import king.server.gameserver.cache.HtmCache;
import king.server.gameserver.custom.AutoVoteRewardManager;
import king.server.gameserver.datatables.AdminTable;
import king.server.gameserver.datatables.ArmorSetsData;
import king.server.gameserver.datatables.AugmentationData;
import king.server.gameserver.datatables.ChampionData;
import king.server.gameserver.datatables.CharNameTable;
import king.server.gameserver.datatables.CharSummonTable;
import king.server.gameserver.datatables.CharTemplateTable;
import king.server.gameserver.datatables.ClanTable;
import king.server.gameserver.datatables.ClassListData;
import king.server.gameserver.datatables.DoorTable;
import king.server.gameserver.datatables.EnchantGroupsData;
import king.server.gameserver.datatables.EnchantHPBonusData;
import king.server.gameserver.datatables.EnchantItemData;
import king.server.gameserver.datatables.EnchantOptionsData;
import king.server.gameserver.datatables.EventDroplist;
import king.server.gameserver.datatables.ExperienceTable;
import king.server.gameserver.datatables.FakePcsTable;
import king.server.gameserver.datatables.FishData;
import king.server.gameserver.datatables.FishingMonstersData;
import king.server.gameserver.datatables.FishingRodsData;
import king.server.gameserver.datatables.HennaData;
import king.server.gameserver.datatables.HerbDropTable;
import king.server.gameserver.datatables.HitConditionBonus;
import king.server.gameserver.datatables.InitialEquipmentData;
import king.server.gameserver.datatables.ItemTable;
import king.server.gameserver.datatables.ManorData;
import king.server.gameserver.datatables.MerchantPriceConfigTable;
import king.server.gameserver.datatables.MultiSell;
import king.server.gameserver.datatables.MultilangMsgData;
import king.server.gameserver.datatables.NpcBufferTable;
import king.server.gameserver.datatables.NpcPersonalAIData;
import king.server.gameserver.datatables.NpcTable;
import king.server.gameserver.datatables.NpcWalkerRoutesData;
import king.server.gameserver.datatables.OfflineTradersTable;
import king.server.gameserver.datatables.PetDataTable;
import king.server.gameserver.datatables.PremiumTable;
import king.server.gameserver.datatables.ProductItemTable;
import king.server.gameserver.datatables.RecipeData;
import king.server.gameserver.datatables.SkillTable;
import king.server.gameserver.datatables.SkillTreesData;
import king.server.gameserver.datatables.SpawnTable;
import king.server.gameserver.datatables.StaticObjects;
import king.server.gameserver.datatables.SummonItemsData;
import king.server.gameserver.datatables.SummonSkillsTable;
import king.server.gameserver.datatables.TeleportLocationTable;
import king.server.gameserver.datatables.UITable;
import king.server.gameserver.events.EventsInterface;
import king.server.gameserver.events.Main;
import king.server.gameserver.fence.FenceBuilderManager;
import king.server.gameserver.fence.MovieMakerManager;
import king.server.gameserver.geoeditorcon.GeoEditorListener;
import king.server.gameserver.handler.EffectHandler;
import king.server.gameserver.idfactory.IdFactory;
import king.server.gameserver.instancemanager.AirShipManager;
import king.server.gameserver.instancemanager.AntiFeedManager;
import king.server.gameserver.instancemanager.AuctionManager;
import king.server.gameserver.instancemanager.BoatManager;
import king.server.gameserver.instancemanager.BonusExpManager;
import king.server.gameserver.instancemanager.CHSiegeManager;
import king.server.gameserver.instancemanager.CastleManager;
import king.server.gameserver.instancemanager.CastleManorManager;
import king.server.gameserver.instancemanager.ClanHallManager;
import king.server.gameserver.instancemanager.CoupleManager;
import king.server.gameserver.instancemanager.CursedWeaponsManager;
import king.server.gameserver.instancemanager.DayNightSpawnManager;
import king.server.gameserver.instancemanager.DimensionalRiftManager;
import king.server.gameserver.instancemanager.ExpirableServicesManager;
import king.server.gameserver.instancemanager.FortManager;
import king.server.gameserver.instancemanager.FortSiegeManager;
import king.server.gameserver.instancemanager.FourSepulchersManager;
import king.server.gameserver.instancemanager.GlobalVariablesManager;
import king.server.gameserver.instancemanager.GraciaSeedsManager;
import king.server.gameserver.instancemanager.GrandBossManager;
import king.server.gameserver.instancemanager.HellboundManager;
import king.server.gameserver.instancemanager.InstanceManager;
import king.server.gameserver.instancemanager.ItemAuctionManager;
import king.server.gameserver.instancemanager.ItemsOnGroundManager;
import king.server.gameserver.instancemanager.MailManager;
import king.server.gameserver.instancemanager.MapRegionManager;
import king.server.gameserver.instancemanager.MercTicketManager;
import king.server.gameserver.instancemanager.PcCafePointsManager;
import king.server.gameserver.instancemanager.PetitionManager;
import king.server.gameserver.instancemanager.QuestManager;
import king.server.gameserver.instancemanager.RaidBossPointsManager;
import king.server.gameserver.instancemanager.RaidBossSpawnManager;
import king.server.gameserver.instancemanager.SiegeManager;
import king.server.gameserver.instancemanager.SoIManager;
import king.server.gameserver.instancemanager.TerritoryWarManager;
import king.server.gameserver.instancemanager.TransformationManager;
import king.server.gameserver.instancemanager.WalkingManager;
import king.server.gameserver.instancemanager.ZoneManager;
import king.server.gameserver.instancemanager.leaderboards.ArenaLeaderboard;
import king.server.gameserver.instancemanager.leaderboards.CraftLeaderboard;
import king.server.gameserver.instancemanager.leaderboards.FishermanLeaderboard;
import king.server.gameserver.instancemanager.leaderboards.TvTLeaderboard;
import king.server.gameserver.model.AutoSpawnHandler;
import king.server.gameserver.model.L2World;
import king.server.gameserver.model.PartyMatchRoomList;
import king.server.gameserver.model.PartyMatchWaitingList;
import king.server.gameserver.model.entity.Hero;
import king.server.gameserver.model.entity.Hitman;
import king.server.gameserver.model.entity.TownWarManager;
import king.server.gameserver.model.entity.TvTManager;
import king.server.gameserver.model.entity.TvTRoundManager;
import king.server.gameserver.model.olympiad.Olympiad;
import king.server.gameserver.network.L2GameClient;
import king.server.gameserver.network.L2GamePacketHandler;
import king.server.gameserver.network.communityserver.CommunityServerThread;
import king.server.gameserver.pathfinding.PathFinding;
import king.server.gameserver.script.faenor.FaenorScriptEngine;
import king.server.gameserver.scripting.L2ScriptEngineManager;
import king.server.gameserver.taskmanager.AutoAnnounceTaskManager;
import king.server.gameserver.taskmanager.KnownListUpdateTaskManager;
import king.server.gameserver.taskmanager.TaskManager;
import king.server.status.Status;
import king.server.util.DeadLockDetector;
import king.server.util.IPv4Filter;

public class GameServer
{
	private static final Logger _log = Logger.getLogger(GameServer.class.getName());
	
	private final SelectorThread<L2GameClient> _selectorThread;
	private final L2GamePacketHandler _gamePacketHandler;
	private final DeadLockDetector _deadDetectThread;
	private final IdFactory _idFactory;
	public static GameServer gameServer;
	private final LoginServerThread _loginThread;
	private static Status _statusServer;
	public static final Calendar dateTimeServerStarted = Calendar.getInstance();
	
	public long getUsedMemoryMB()
	{
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576; // ;
	}
	
	public SelectorThread<L2GameClient> getSelectorThread()
	{
		return _selectorThread;
	}
	
	public L2GamePacketHandler getL2GamePacketHandler()
	{
		return _gamePacketHandler;
	}
	
	public DeadLockDetector getDeadLockDetectorThread()
	{
		return _deadDetectThread;
	}
	
	public GameServer() throws Exception
	{
		long serverLoadStart = System.currentTimeMillis();
		
		gameServer = this;
		_log.finest(getClass().getSimpleName() + ": Memoria usada:" + getUsedMemoryMB() + "MB");
		
		if (Config.SERVER_VERSION != null)
		{
			_log.info(getClass().getSimpleName() + ": KingServer Server Version:    " + Config.SERVER_VERSION);
		}
		if (Config.DATAPACK_VERSION != null)
		{
			_log.info(getClass().getSimpleName() + ": KingServer Datapack Version:  " + Config.DATAPACK_VERSION);
		}
		
		_idFactory = IdFactory.getInstance();
		
		if (!_idFactory.isInitialized())
		{
			_log.severe(getClass().getSimpleName() + ": Could not read object IDs from DB. Please Check Your Data.");
			throw new Exception("Could not initialize the ID factory");
		}
		
		ThreadPoolManager.getInstance();
		
		new File(Config.DATAPACK_ROOT, "data/crests").mkdirs();
		new File("log/game").mkdirs();
		
		// load script engines
		printSection("Engines");
		L2ScriptEngineManager.getInstance();
		
		printSection("Mundo");
		// start game time control early
		GameTimeController.getInstance();
		InstanceManager.getInstance();
		L2World.getInstance();
		MapRegionManager.getInstance();
		Announcements.getInstance();
		GlobalVariablesManager.getInstance();
		
		printSection("Skills");
		EffectHandler.getInstance().executeScript();
		EnchantGroupsData.getInstance();
		SkillTreesData.getInstance();
		SkillTable.getInstance();
		SummonSkillsTable.getInstance();
		
		printSection("Items");
		ItemTable.getInstance();
		EnchantItemData.getInstance();
		EnchantOptionsData.getInstance();
		SummonItemsData.getInstance();
		EnchantHPBonusData.getInstance();
		MerchantPriceConfigTable.getInstance().loadInstances();
		TradeController.getInstance();
		MultiSell.getInstance();
		ProductItemTable.getInstance();
		RecipeData.getInstance();
		ArmorSetsData.getInstance();
		FishData.getInstance();
		FishingMonstersData.getInstance();
		FishingRodsData.getInstance();
		HennaData.getInstance();
		
		printSection("Characters");
		ClassListData.getInstance();
		InitialEquipmentData.getInstance();
		ExperienceTable.getInstance();
		HitConditionBonus.getInstance();
		CharTemplateTable.getInstance();
		CharNameTable.getInstance();
		AdminTable.getInstance();
		GmListTable.getInstance();
		RaidBossPointsManager.getInstance();
		PetDataTable.getInstance();
		CharSummonTable.getInstance().init();
		
		PremiumTable.getInstance();
		ExpirableServicesManager.getInstance();
		
		printSection("Clans");
		ClanTable.getInstance();
		CHSiegeManager.getInstance();
		ClanHallManager.getInstance();
		AuctionManager.getInstance();
		
		printSection("Geodata");
		GeoData.getInstance();
		if (Config.GEODATA == 2)
		{
			PathFinding.getInstance();
		}
		
		printSection("NPCs");
		HerbDropTable.getInstance();
		NpcTable.getInstance();
		if (Config.L2JMOD_CHAMPION_ENABLE)
		{
			ChampionData.getInstance();
		}
		NpcWalkerRoutesData.getInstance();
		WalkingManager.getInstance();
		NpcPersonalAIData.getInstance();
		StaticObjects.getInstance();
		ZoneManager.getInstance();
		DoorTable.getInstance();
		if (Config.FENCE_MOVIE_BUILDER)
		{
			MovieMakerManager.getInstance();
			FenceBuilderManager.getInstance();
		}
		ItemAuctionManager.getInstance();
		CastleManager.getInstance().loadInstances();
		FortManager.getInstance().loadInstances();
		NpcBufferTable.getInstance();
		FakePcsTable.getInstance();
		SpawnTable.getInstance();
		HellboundManager.getInstance();
		
		if (Config.ENABLE_BONUS_MANAGER)
		{
			BonusExpManager.getInstance();
		}
		
		RaidBossSpawnManager.getInstance();
		DayNightSpawnManager.getInstance().trim().notifyChangeMode();
		GrandBossManager.getInstance().initZones();
		FourSepulchersManager.getInstance().init();
		DimensionalRiftManager.getInstance();
		EventDroplist.getInstance();
		
		printSection("Siege");
		SiegeManager.getInstance().getSieges();
		FortSiegeManager.getInstance();
		TerritoryWarManager.getInstance();
		CastleManorManager.getInstance();
		MercTicketManager.getInstance();
		PcCafePointsManager.getInstance();
		ManorData.getInstance();
		
		printSection("Olimpiadas");
		Olympiad.getInstance();
		Hero.getInstance();
		
		// Call to load caches
		printSection("Cache");
		HtmCache.getInstance();
		CrestCache.getInstance();
		TeleportLocationTable.getInstance();
		UITable.getInstance();
		PartyMatchWaitingList.getInstance();
		PartyMatchRoomList.getInstance();
		PetitionManager.getInstance();
		AugmentationData.getInstance();
		CursedWeaponsManager.getInstance();
		
		printSection("Scripts");
		QuestManager.getInstance();
		MultilangMsgData.getInstance();
		TransformationManager.getInstance();
		BoatManager.getInstance();
		AirShipManager.getInstance();
		Hitman.start();
		GraciaSeedsManager.getInstance();
		SoIManager.getInstance();
		CastleManager.getInstance().activateInstances();
		FortManager.getInstance().activateInstances();
		MerchantPriceConfigTable.getInstance().updateReferences();
		
		if (Config.VOTE_SYSTEM_ENABLE == true)
		{
			AutoVoteRewardManager.getInstance();
		}
		
		try
		{
			_log.info(getClass().getSimpleName() + ": Carregando Scripts");
			File scripts = new File(Config.DATAPACK_ROOT, "data/scripts.cfg");
			if (!Config.ALT_DEV_NO_HANDLERS || !Config.ALT_DEV_NO_QUESTS)
			{
				L2ScriptEngineManager.getInstance().executeScriptList(scripts);
			}
		}
		catch (IOException ioe)
		{
			_log.severe(getClass().getSimpleName() + ": Failed loading scripts.cfg, no script going to be loaded");
		}
		
		QuestManager.getInstance().report();
		TransformationManager.getInstance().report();
		
		if (Config.SAVE_DROPPED_ITEM)
		{
			ItemsOnGroundManager.getInstance();
			
		}
		
		if ((Config.AUTODESTROY_ITEM_AFTER > 0) || (Config.HERB_AUTO_DESTROY_TIME > 0))
		{
			ItemsAutoDestroy.getInstance();
		}
		
		MonsterRace.getInstance();
		
		SevenSigns.getInstance().spawnSevenSignsNPC();
		SevenSignsFestival.getInstance();
		AutoSpawnHandler.getInstance();
		
		FaenorScriptEngine.getInstance();
		// Init of a cursed weapon manager
		
		_log.info("AutoSpawnHandler: " + AutoSpawnHandler.getInstance().size() + " handlers no total.");
		
		if (Config.L2JMOD_ALLOW_WEDDING)
		{
			CoupleManager.getInstance();
		}
		
		if (Config.RANK_ARENA_ENABLED)
		{
			ArenaLeaderboard.getInstance();
		}
		
		if (Config.RANK_FISHERMAN_ENABLED)
		{
			FishermanLeaderboard.getInstance();
		}
		
		if (Config.RANK_CRAFT_ENABLED)
		{
			CraftLeaderboard.getInstance();
		}
		
		if (Config.RANK_TVT_ENABLED)
		{
			TvTLeaderboard.getInstance();
		}
		
		TaskManager.getInstance();
		
		AntiFeedManager.getInstance().registerEvent(AntiFeedManager.GAME_ID);
		
		if (Config.ALLOW_MAIL)
		{
			MailManager.getInstance();
		}
		
		if (Config.ACCEPT_GEOEDITOR_CONN)
		{
			GeoEditorListener.getInstance();
		}
		
		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());
		
		_log.info("IdFactory: Free ObjectID's remaining: " + IdFactory.getInstance().size());
		
		KnownListUpdateTaskManager.getInstance();

		printSection("Events Manager");
		Main.main();
		EventsInterface.start();
		TvTManager.getInstance();
		TownWarManager.getInstance();
		TvTRoundManager.getInstance();

		
		if ((Config.OFFLINE_TRADE_ENABLE || Config.OFFLINE_CRAFT_ENABLE) && Config.RESTORE_OFFLINERS)
		{
			OfflineTradersTable.getInstance().restoreOfflineTraders();
		}
		
		if (Config.DEADLOCK_DETECTOR)
		{
			_deadDetectThread = new DeadLockDetector();
			_deadDetectThread.setDaemon(true);
			_deadDetectThread.start();
		}
		else
		{
			_deadDetectThread = null;
		}
		System.gc();

		if (Config.AUTO_RESTART_ENABLE)
		{
			GameServerRestart.getInstance().StartCalculationOfNextRestartTime();
		}
		else
		{
			_log.info("[Auto Restart]: System is disabled.");
		}
				
		// maxMemory is the upper limit the jvm can use, totalMemory the size of
		// the current allocation pool, freeMemory the unused memory in the
		// allocation pool
		long freeMem = ((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()) + Runtime.getRuntime().freeMemory()) / 1048576;
		long totalMem = Runtime.getRuntime().maxMemory() / 1048576;
		_log.info(getClass().getSimpleName() + ": Iniciado, memoria livre " + freeMem + " Mb de " + totalMem + " Mb");
		Toolkit.getDefaultToolkit().beep();
		
		_loginThread = LoginServerThread.getInstance();
		_loginThread.start();
		
		CommunityServerThread.initialize();
		
		final SelectorConfig sc = new SelectorConfig();
		sc.MAX_READ_PER_PASS = Config.MMO_MAX_READ_PER_PASS;
		sc.MAX_SEND_PER_PASS = Config.MMO_MAX_SEND_PER_PASS;
		sc.SLEEP_TIME = Config.MMO_SELECTOR_SLEEP_TIME;
		sc.HELPER_BUFFER_COUNT = Config.MMO_HELPER_BUFFER_COUNT;
		sc.TCP_NODELAY = Config.MMO_TCP_NODELAY;
		
		_gamePacketHandler = new L2GamePacketHandler();
		_selectorThread = new SelectorThread<>(sc, _gamePacketHandler, _gamePacketHandler, _gamePacketHandler, new IPv4Filter());
		
		InetAddress bindAddress = null;
		if (!Config.GAMESERVER_HOSTNAME.equals("*"))
		{
			try
			{
				bindAddress = InetAddress.getByName(Config.GAMESERVER_HOSTNAME);
			}
			catch (UnknownHostException e1)
			{
				_log.log(Level.SEVERE, getClass().getSimpleName() + ": WARNING: The GameServer bind address is invalid, using all avaliable IPs. Reason: " + e1.getMessage(), e1);
			}
		}
		
		try
		{
			_selectorThread.openServerSocket(bindAddress, Config.PORT_GAME);
		}
		catch (IOException e)
		{
			_log.log(Level.SEVERE, getClass().getSimpleName() + ": FATAL: Failed to open server socket. Reason: " + e.getMessage(), e);
			System.exit(1);
		}
		_selectorThread.start();
		_log.info("Numero maximo de players conectados: " + Config.MAXIMUM_ONLINE_USERS);
		long serverLoadEnd = System.currentTimeMillis();
		_log.info("Servidor carregado em " + ((serverLoadEnd - serverLoadStart) / 1000) + " segundos");
		
		AutoAnnounceTaskManager.getInstance();
	}
	
	public static void main(String[] args) throws Exception
	{
		Server.serverMode = Server.MODE_GAMESERVER;
		// Local Constants
		final String LOG_FOLDER = "log"; // Name of folder for log file
		final String LOG_NAME = "./log.cfg"; // Name of log file
		
		/*** Main ***/
		// Create log folder
		File logFolder = new File(Config.DATAPACK_ROOT, LOG_FOLDER);
		logFolder.mkdir();
		
		// Create input stream for log file -- or store file data into memory
		try (InputStream is = new FileInputStream(new File(LOG_NAME)))
		{
			LogManager.getLogManager().readConfiguration(is);
		}
		
		// Initialize config
		Config.load();
		printSection("Database");
		L2DatabaseFactory.getInstance();
		gameServer = new GameServer();
		
		if (Config.IS_TELNET_ENABLED)
		{
			_statusServer = new Status(Server.serverMode);
			_statusServer.start();
		}
		else
		{
			_log.info(GameServer.class.getSimpleName() + ": Telnet desativado.");
		}
	}
	
	public static void printSection(String s)
	{
		s = "=[ " + s + " ]";
		while (s.length() < 78)
		{
			s = "-" + s;
		}
		_log.info(s);
	}
}