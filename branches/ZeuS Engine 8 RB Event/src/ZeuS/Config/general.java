package ZeuS.Config;

import java.io.File;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.l2jserver.Config;
import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.data.xml.impl.NpcData;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.model.effects.L2EffectType;

import ZeuS.Comunidad.Comunidad;
import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Comunidad.EngineForm.C_oly_buff;
import ZeuS.Comunidad.EngineForm.v_auction_house;
//import ZeuS.Comunidad.EngineForm.v_dropsearch;
import ZeuS.Comunidad.EngineForm.v_partymatching;
import ZeuS.Comunidad.EngineForm.v_pvppkLog;
import ZeuS.ZeuS.BD;
import ZeuS.event.TownWarEvent;
import ZeuS.interfase.ManagerAIONpc;
import ZeuS.interfase.bufferChar;
import ZeuS.interfase.buffer_zeus;
import ZeuS.interfase.captchaPLayer;
import ZeuS.interfase.central;
import ZeuS.interfase.donaManager;
import ZeuS.interfase.pinCode;
import ZeuS.interfase.sellBuff;
import ZeuS.interfase.votereward;
import ZeuS.procedimientos.adminHandler;
import ZeuS.procedimientos.handler;
import ZeuS.procedimientos.opera;
import ZeuS.server.httpResp;

public final class general {
	private static boolean _isValid=false;
	private static String SERIAL_;
	private static HashMap<Integer,String> classList = new HashMap<Integer, String>();
	public static HashMap<Integer,HashMap<String,String>> classData = new HashMap<Integer, HashMap<String,String>>();
	private static String SCRIPT_BORRADO ="DELETE FROM zeus_connection WHERE NOW() > DATE_ADD(zeus_connection.date,INTERVAL 10 DAY)";

	private static HashMap<Integer,premiumsystem> PREMIUM_SERVICES = new HashMap<Integer, premiumsystem>();
	private static HashMap<String,premiumPersonalData> PREMIUM_SERVICES_CLAN_CHAR = new HashMap<String, premiumPersonalData>();
	
	public static void setNewPremiumPersonalData(String DataToSave, premiumPersonalData Pm_data){
		if(PREMIUM_SERVICES_CLAN_CHAR!=null){
			if(PREMIUM_SERVICES_CLAN_CHAR.containsKey(DataToSave)){
				PREMIUM_SERVICES_CLAN_CHAR.remove(DataToSave);
			}
		}
		PREMIUM_SERVICES_CLAN_CHAR.put(DataToSave, Pm_data);
	}
	
	public static Vector<Integer> COMMUNITY_MAIN_ACCESS = new Vector<Integer>();
	
	public static HashMap<Integer,Long>_byPassVoice = new HashMap<Integer,Long>();
	
	private static HashMap<Integer,Boolean> BLOCKSAVEBD = new HashMap<Integer, Boolean>();
	
	public static void setStatusCanSave(L2PcInstance player, boolean b){
		BLOCKSAVEBD.put(player.getObjectId(), b);
	}
	
	public static boolean canSaveInBD(L2PcInstance player){
		if(BLOCKSAVEBD!=null){
			if(BLOCKSAVEBD.containsKey(player.getObjectId())){
				return BLOCKSAVEBD.get(player.getObjectId()); 
			}
		}
		BLOCKSAVEBD.put(player.getObjectId(), true);
		return true;
	}
	
	
	private static boolean LOAD_START = false;
	private static boolean LOAD_START_ONE_TIME = false;
	private static HashMap<String,HashMap<String,String>> CHARINFO = new HashMap<String,HashMap<String,String>>();
	private static HashMap<Integer,String> ID_TO_CHAR_NAME = new HashMap<Integer,String>();
	private static HashMap<Integer,Integer>CLASES_COUNT = new HashMap<Integer, Integer>();
	private static HashMap<String,Integer>RACE_COUNT = new HashMap<String, Integer>();
	
	private static Vector<Integer> TopSearch_Item = new Vector<Integer>();
	private static Vector<Integer> TopSearch_Monster = new Vector<Integer>();
	private static Vector<Integer> ClassUltimate = new Vector<Integer>();
	private static HashMap<String,HashMap<Integer,HashMap<Integer,String>>> HeroesList = new HashMap<String,HashMap<Integer,HashMap<Integer,String>>>();
	
	private static HashMap<Integer, HashMap<Integer, HashMap<Integer,String>>> TopPlayersToShow = new HashMap<Integer, HashMap<Integer,HashMap<Integer,String>>>();
	
	private static String TITULO_ENGINE = "ZeuS Engine";
	
	public static String STAFF_DATA;
	
	public static String getTituloEngine(){
		return TITULO_ENGINE;
	}
	

	protected static String HOST_ ="";
	protected static String USER_ ="";
	
	private static String BeginTime = "";
	private static int unixTimeBeginServer = 0;

	private static int CB_IDObjectMainZeuSNPC = 0;

	public static String getHost(){
		return HOST_;
	}

	public static String getUser(){
		return USER_;
	}

	public static String getSerial(){
		return SERIAL_;
	}

	public static boolean _activated(){
		return _isValid;
		//return true;
	}

	protected static void checkValidate(){
		//String respuesta = httpResp.getInfoSerial();
		//try{
		//	_isValid = respuesta.equals("cor")?true:false;
		//}catch(Exception a ){
		//	_isValid = false;
		//	return;
		//}
		_isValid = true;
	}

	protected static int[] AccessConfig;



	public static final Logger _log = Logger.getLogger(general.class.getName());
	private static final String ZEUS_CONFIG = "./config/zeus.properties";
	private static final String ZEUS_DONATION_CONFIG = "./config/zeus_donation.properties";
	private static final String ZEUS_CONFIG_LEN = "./config/zeus_leng.txt";
	public static int ServerStartUnixTime = 0;
	//public static final String QuestId = "";



	public static final String VERSION = "10.2";
	public static final String EMAIL = "adm.jabberwock@gmail.com";
	
	private static HashMap<Integer,HashMap<Integer,HashMap<String,String>>>LastAccess = new HashMap<Integer,HashMap<Integer,HashMap<String,String>>>();


	public static int PLAYER_BASE_TO_SHOW = 0;

	public static boolean DEBUG_CONSOLA_ENTRADAS;
	public static boolean DEBUG_CONSOLA_ENTRADAS_TO_USER;

	public static boolean onLine = false;

	//public static String CLASSMASTER_GIFT;
	//public static String NOBLE_GIFT;

	//public static int NOBLE_LEVEL_FOR_FREE;
	public static int NOBLESS_TIARA = 7694;
	//public static boolean ALLOW_KARMA_PLAYER;
	public static boolean FREE_TELEPORT;
	public static String TELEPORT_PRICE;
	public static Boolean TELEPORT_BD;
	public static Boolean TELEPORT_CAN_USE_IN_COMBAT_MODE;
	public static Boolean TELEPORT_FOR_FREE_UP_TO_LEVEL;
	public static int TELEPORT_FOR_FREE_UP_TO_LEVEL_LV;

	public static boolean DROP_SEARCH_TELEPORT_FOR_FREE;
	public static boolean DROP_SEARCH_OBSERVE_MODE;
	public static boolean DROP_SEARCH_SHOW_IDITEM_TO_PLAYER;
	public static String DROP_TELEPORT_COST;
	public static int DROP_SEARCH_MOSTRAR_LISTA;
	public static Vector<Integer> DROP_SEARCH_MOB_BLOCK_TELEPORT = new Vector<Integer>();
	public static boolean DROP_SEARCH_CAN_USE_TELEPORT;

	public static String Server_Name = "";

	public static int RADIO_PLAYER_NPC_MAXIMO;
	
	
	public static Vector<Integer> BUFF_STORE_BUFFPROHIBITED = new Vector<Integer>();
	public static Vector<String> BUFFSTORE_ITEMS_REQUEST = new Vector<String>();
	
	public static Vector<String> AUCTIONSHOUSE_ITEM_REQUEST = new Vector<String>();
	
	public static int AUCTIONSHOUSE_PERCENT_FEED;
	public static int AUCTIONSHOUSE_FEED_MASTER;

	/*Val Interno*///public static final int DROP_LISTA_ACTUAL = 0;

	//public static String[][] TELEPORT_MAIN = new String[500][15];
	public static HashMap<Integer,HashMap<String,String>> TELEPORT_DATA = new HashMap<Integer, HashMap<String,String>>();
	public static HashMap<Integer,HashMap<String,String>> SHOP_DATA = new HashMap<Integer, HashMap<String,String>>();	
	//public static String[][] SHOP_MAIN = new String[500][15];
	public static HashMap<Integer, Boolean> isSellMerchant = new HashMap<Integer, Boolean>();
	public static HashMap<Integer, Boolean> IS_USING_NPC = new HashMap<Integer,Boolean>();
	public static HashMap<Integer, Boolean> IS_USING_CB = new HashMap<Integer,Boolean>();


	/*PLUS*/
	public static HashMap<String, HashMap<String,String>> PLUS_C = new HashMap<String, HashMap<String,String>>();
	public static String IP_PLUS_C = "";
	public static Vector<String> CHAR_PLUS = new Vector<String>();
	public static HashMap<String,String> CHAR_PLUS_IP = new HashMap<String, String>();
	/*PLUS*/

	public static String PARTY_FINDER_PRICE;
	public static boolean PARTY_FINDER_GO_LEADER_NOBLE;
	public static boolean PARTY_FINDER_GO_LEADER_DEATH;
	public static boolean PARTY_FINDER_GO_LEADER_FLAGPK;
	public static boolean PARTY_FINDER_CAN_USE_FLAG;
	public static boolean PARTY_FINDER_CAN_USE_PK;
	public static int PARTY_FINDER_CAN_USE_LVL;
	public static boolean PARTY_FINDER_USE_NO_SUMMON_RULEZ;
	public static boolean PARTY_FINDER_GO_LEADER_WHEN_ARE_INSTANCE;
	public static boolean PARTY_FINDER_GO_LEADER_ON_ASEDIO;
	public static boolean PARTY_FINDER_CAN_USE_ONLY_NOBLE;

	public static boolean FLAG_FINDER_CAN_USE_FLAG;
	public static boolean FLAG_FINDER_CAN_USE_PK;
	public static boolean FLAG_FINDER_CAN_NOBLE;
	public static String FLAG_FINDER_PRICE;
	public static int FLAG_FINDER_LVL;
	public static int FLAG_PVP_PK_LVL_MIN;
	public static int FLAG_FINDER_MIN_PVP_FROM_TARGET;
	public static boolean FLAG_FINDER_CAN_GO_IS_INSIDE_CASTLE;
	public static boolean FLAG_FINDER_PK_PRIORITY;
	public static boolean FLAG_FINDER_CHECK_CLAN;

	public static String PINTAR_PRICE;
	public static String PINTAR_MATRIZ;

	public static String DELEVEL_PRICE;
	public static int DELEVEL_LVL_MAX;
	public static boolean DELEVEL_NOBLE;
	public static boolean DELEVEL_CHECK_SKILL;

	public static boolean DRESSME_STATUS;
	public static boolean DRESSME_TARGET_STATUS;
	public static boolean DRESSME_CAN_USE_IN_OLYS;
	public static boolean DRESSME_CAN_CHANGE_IN_OLYS;
	public static boolean DRESSME_NEW_DRESS_IS_FREE;
	public static String DRESSME_NEW_DRESS_COST;
	private static Vector<Integer> DRESSME_ID_BLOCK = new Vector<Integer>();


	public static String AUGMENT_ITEM_PRICE;
	public static int AUGMENT_SPECIAL_x_PAGINA;
	public static boolean AUGMENT_SPECIAL_NOBLE;
	public static int AUGMENT_SPECIAL_LVL;
	public static String AUGMENT_SPECIAL_PRICE;
	public static String AUGMENT_SPECIAL_PRICE_ACTIVE;
	public static String AUGMENT_SPECIAL_PRICE_PASSIVE;
	public static String AUGMENT_SPECIAL_PRICE_CHANCE;
	
	public static HashMap<String,HashMap<Integer,HashMap<String, String>>> AUGMENT_DATA = new HashMap<String,HashMap<Integer,HashMap<String, String>>>();

	public static int DONA_ID_ITEM;
	public static String LOGINSERVERNAME;

	public static String VOTO_REWARD_TOPZONE;// = "3470,5;57,50000;44000,5"
	public static String VOTO_REWARD_HOPZONE;// = "57,5000000;44002,5"
	public static boolean VOTO_REWARD_ACTIVO_TOPZONE;// = False
	public static boolean VOTO_REWARD_ACTIVO_HOPZONE;// = True
	public static int VOTO_REWARD_SEG_ESPERA;
	public static String VOTO_ITEM_BUFF_ENCHANT_PRICE;
	public static final String VOTO_WEB_GET_VOTOS = "http://p-venta.cl/pv/getvotos.php";
	public static String VOTO_HOPZONE_IDENTIFICACION = "";
	public static String VOTO_TOPZONE_IDENTIFICACION = "";

	public static boolean VOTO_AUTOREWARD;
	public static int VOTO_REWARD_AUTO_MINUTE_TIME_TO_CHECK;
	public static int VOTO_REWARD_AUTO_RANGO_PREMIAR;
	public static String VOTO_REWARD_AUTO_MENSAJE_FALTA;
	public static String VOTO_REWARD_AUTO_MENSAJE_META_COMPLIDA;
	public static String VOTO_REWARD_AUTO_REWARD_META_HOPZONE;
	public static String VOTO_REWARD_AUTO_REWARD_META_TOPZONE;
	public static boolean VOTO_REWARD_AUTO_AFK_CHECK;

	public static boolean ANTI_OVER_ENCHANT_ACT;
	public static boolean ANTI_OVER_ENCHANT_CHECK_BEFORE_ATTACK;
	public static int ANTI_OVER_ENCHANT_SECOND_BETWEEN_CHECK_BEFORE_ATTACK;
	public static String ANTI_OVER_ENCHANT_MESJ_PUNISH;
	public static String ANTI_OVER_TYPE_PUNISH;
	public static String[] _ANTI_OVER_TYPE_PUNISH = {"JAIL","BAN_CHAR","BAN_ACC"};
	public static boolean ANTI_OVER_ENCHANT_ANNOUCEMENT_ALL;
	public static boolean ANTI_OVER_ENCHANT_PUNISH_ALL_SAME_IP;
	public static int ANTI_OVER_ENCHANT_MAX_WEAPON;
	public static int ANTI_OVER_ENCHANT_MAX_ARMOR;


	public static boolean ENCHANT_NOBLE;
	public static int ENCHANT_LVL;
	public static String ENCHANT_ITEM_PRICE;
	public static int ENCHANT_MIN_ENCHANT;
	public static int ENCHANT_MAX_ENCHANT;
	public static int ENCHANT_x_VEZ;

	public static boolean ELEMENTAL_NOBLE;
	public static int ELEMENTAL_LVL;
	public static String ELEMENTAL_ITEM_PRICE;
	public static int ELEMENTAL_LVL_ENCHANT_MAX_WEAPON;
	public static int ELEMENTAL_LVL_ENCHANT_MAX_ARMOR;


	public static String OPCIONES_CHAR_SEXO_ITEM_PRICE;
	public static String OPCIONES_CHAR_NOBLE_ITEM_PRICE;
	public static String OPCIONES_CHAR_LVL85_ITEM_PRICE;
	public static String OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE;
	public static String OPCIONES_CHAR_BUFFER_AIO_PRICE;
	public static String OPCIONES_CHAR_BUFFER_AIO_PRICE_30;
	public static int OPCIONES_CHAR_BUFFER_AIO_LVL;
	public static boolean OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE;
	public static int OPCIONES_CHAR_CAMBIO_NOMBRE_LVL;
	public static boolean OPCIONES_CHAR_CAMBIO_NOMBRE;
	public static boolean OPCIONES_CLAN_CAMBIO_NOMBRE;
	public static boolean OPCIONES_CHAR_BUFFER_AIO_30;
	
	public static int MAX_IP_COUNT;
	public static boolean MAX_IP_CHECK;
	public static boolean MAX_IP_RECORD_DATA;
	public static int MAX_IP_VIP_COUNT;


	public static boolean OPCIONES_CHAR_SEXO;
	public static boolean OPCIONES_CHAR_NOBLE;
	public static boolean OPCIONES_CHAR_LVL85;
	public static boolean OPCIONES_CHAR_BUFFER_AIO;
	public static boolean OPCIONES_CHAR_FAME;

	public static String OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE;
	public static int OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL;

	public static String OPCIONES_CHAR_FAME_PRICE;
	public static boolean OPCIONES_CHAR_FAME_NOBLE;
	public static int OPCIONES_CHAR_FAME_LVL;
	public static int OPCIONES_CHAR_FAME_GIVE;

	public static int RAIDBOSS_INFO_LISTA_X_HOJA;
	public static boolean RAIDBOSS_INFO_TELEPORT;
	public static String RAIDBOSS_INFO_TELEPORT_PRICE;
	public static boolean RAIDBOSS_INFO_NOBLE;
	public static int RAIDBOSS_INFO_LVL;
	public static Vector<Integer> RAIDBOSS_ID_MOB_NO_TELEPORT = new Vector<Integer>();
	public static boolean RAIDBOSS_OBSERVE_MODE;

	public static boolean RETURN_BUFF;
	public static int RETURN_BUFF_SECONDS_TO_RETURN;
	public static boolean RETURN_BUFF_IN_OLY;
	public static int RETURN_BUFF_IN_OLY_MINUTES_TO_RETURN;
	public static Vector<Integer> RETURN_BUFF_NOT_STEALING = new Vector<Integer>();


	public static String GET_NAME_VAR_TYPE;
	public static String GET_NAME_VAR_EMAIL;
	public static String GET_NAME_VAR_CODE;
	public static String GET_NAME_VAR_DIR_WEB;
	public static String GET_NAME_VAR_IDDONACION;
	public static String GET_NAME_VAR_SERVER_ID;	
	public static String WEB_HOP_ZONE_SERVER;
	public static String WEB_TOP_ZONE_SERVER;


	public static HashMap<L2PcInstance, ArrayList<L2EffectType>> BUFF_REMOVED = new HashMap<L2PcInstance, ArrayList<L2EffectType>>();
	public static HashMap<L2PcInstance, Boolean> CANCEL_TASK = new HashMap<L2PcInstance, Boolean>();

	public static void resetBuffRemoved(L2PcInstance player){
		if(BUFF_REMOVED.containsKey(player)){
			BUFF_REMOVED.remove(player);
			CANCEL_TASK.put(player, false);
		}
	}


	public static boolean TRADE_WHILE_FLAG;
	public static boolean TRADE_WHILE_PK;

	public static boolean BTN_SHOW_VOTE;
	public static boolean BTN_SHOW_BUFFER;
	public static boolean BTN_SHOW_TELEPORT;
	public static boolean BTN_SHOW_SHOP;
	public static boolean BTN_SHOW_WAREHOUSE;
	public static boolean BTN_SHOW_AUGMENT;
	public static boolean BTN_SHOW_SUBCLASES;
	public static boolean BTN_SHOW_CLASS_TRANSFER;
	public static boolean BTN_SHOW_CONFIG_PANEL;
	public static boolean BTN_SHOW_DROP_SEARCH;
	public static boolean BTN_SHOW_PVPPK_LIST;
	public static boolean BTN_SHOW_LOG_PELEAS;
	public static boolean BTN_SHOW_CASTLE_MANAGER;
	public static boolean BTN_SHOW_DESAFIO;
	public static boolean BTN_SHOW_SYMBOL_MARKET;
	public static boolean BTN_SHOW_CLANALLY;
	public static boolean BTN_SHOW_PARTYFINDER;
	public static boolean BTN_SHOW_FLAGFINDER;
	public static boolean BTN_SHOW_COLORNAME;
	public static boolean BTN_SHOW_DELEVEL;
	public static boolean BTN_SHOW_REMOVE_ATRIBUTE;
	public static boolean BTN_SHOW_BUG_REPORT;
	public static boolean BTN_SHOW_DONATION;
	public static boolean BTN_SHOW_CAMBIO_NOMBRE_PJ;
	public static boolean BTN_SHOW_CAMBIO_NOMBRE_CLAN;
	public static boolean BTN_SHOW_VARIAS_OPCIONES;
	public static boolean BTN_SHOW_ELEMENT_ENHANCED;
	public static boolean BTN_SHOW_ENCANTAMIENTO_ITEM;
	public static boolean BTN_SHOW_AUGMENT_SPECIAL;
	public static boolean BTN_SHOW_GRAND_BOSS_STATUS;
	public static boolean BTN_SHOW_RAIDBOSS_INFO;
	public static boolean BTN_SHOW_TRANSFORMATION;


	public static boolean BTN_SHOW_VOTE_CH;
	public static boolean BTN_SHOW_BUFFER_CH;
	public static boolean BTN_SHOW_TELEPORT_CH;
	public static boolean BTN_SHOW_SHOP_CH;
	public static boolean BTN_SHOW_WAREHOUSE_CH;
	public static boolean BTN_SHOW_AUGMENT_CH;
	public static boolean BTN_SHOW_SUBCLASES_CH;
	public static boolean BTN_SHOW_CLASS_TRANSFER_CH;
	public static boolean BTN_SHOW_CONFIG_PANEL_CH;
	public static boolean BTN_SHOW_DROP_SEARCH_CH;
	public static boolean BTN_SHOW_PVPPK_LIST_CH;
	public static boolean BTN_SHOW_LOG_PELEAS_CH;
	public static boolean BTN_SHOW_CASTLE_MANAGER_CH;
	public static boolean BTN_SHOW_DESAFIO_CH;
	public static boolean BTN_SHOW_SYMBOL_MARKET_CH;
	public static boolean BTN_SHOW_CLANALLY_CH;
	public static boolean BTN_SHOW_PARTYFINDER_CH;
	public static boolean BTN_SHOW_FLAGFINDER_CH;
	public static boolean BTN_SHOW_COLORNAME_CH;
	public static boolean BTN_SHOW_DELEVEL_CH;
	public static boolean BTN_SHOW_REMOVE_ATRIBUTE_CH;
	public static boolean BTN_SHOW_BUG_REPORT_CH;
	public static boolean BTN_SHOW_DONATION_CH;
	public static boolean BTN_SHOW_CAMBIO_NOMBRE_PJ_CH;
	public static boolean BTN_SHOW_CAMBIO_NOMBRE_CLAN_CH;
	public static boolean BTN_SHOW_VARIAS_OPCIONES_CH;
	public static boolean BTN_SHOW_ELEMENT_ENHANCED_CH;
	public static boolean BTN_SHOW_ENCANTAMIENTO_ITEM_CH;
	public static boolean BTN_SHOW_AUGMENT_SPECIAL_CH;
	public static boolean BTN_SHOW_GRAND_BOSS_STATUS_CH;
	public static boolean BTN_SHOW_RAIDBOSS_INFO_CH;
	public static boolean BTN_SHOW_TRANSFORMATION_CH;


	public static boolean BTN_SHOW_VOTE_CBE;
	public static boolean BTN_SHOW_BUFFER_CBE;
	public static boolean BTN_SHOW_TELEPORT_CBE;
	public static boolean BTN_SHOW_SHOP_CBE;
	public static boolean BTN_SHOW_WAREHOUSE_CBE;
	public static boolean BTN_SHOW_AUGMENT_CBE;
	public static boolean BTN_SHOW_SUBCLASES_CBE;
	public static boolean BTN_SHOW_CLASS_TRANSFER_CBE;
	public static boolean BTN_SHOW_CONFIG_PANEL_CBE;
	public static boolean BTN_SHOW_DROP_SEARCH_CBE;
	public static boolean BTN_SHOW_PVPPK_LIST_CBE;
	public static boolean BTN_SHOW_LOG_PELEAS_CBE;
	public static boolean BTN_SHOW_CASTLE_MANAGER_CBE;
	public static boolean BTN_SHOW_DESAFIO_CBE;
	public static boolean BTN_SHOW_SYMBOL_MARKET_CBE;
	public static boolean BTN_SHOW_CLANALLY_CBE;
	public static boolean BTN_SHOW_PARTYFINDER_CBE;
	public static boolean BTN_SHOW_FLAGFINDER_CBE;
	public static boolean BTN_SHOW_COLORNAME_CBE;
	public static boolean BTN_SHOW_DELEVEL_CBE;
	public static boolean BTN_SHOW_REMOVE_ATRIBUTE_CBE;
	public static boolean BTN_SHOW_BUG_REPORT_CBE;
	public static boolean BTN_SHOW_DONATION_CBE;
	public static boolean BTN_SHOW_CAMBIO_NOMBRE_PJ_CBE;
	public static boolean BTN_SHOW_CAMBIO_NOMBRE_CLAN_CBE;
	public static boolean BTN_SHOW_VARIAS_OPCIONES_CBE;
	public static boolean BTN_SHOW_ELEMENT_ENHANCED_CBE;
	public static boolean BTN_SHOW_ENCANTAMIENTO_ITEM_CBE;
	public static boolean BTN_SHOW_AUGMENT_SPECIAL_CBE;
	public static boolean BTN_SHOW_GRAND_BOSS_STATUS_CBE;
	public static boolean BTN_SHOW_RAIDBOSS_INFO_CBE;
	public static boolean BTN_SHOW_TRANSFORMATION_CBE;
	public static boolean BTN_SHOW_BLACKSMITH_CBE;
	public static boolean BTN_SHOW_PARTYMATCHING_CBE;
	public static boolean BTN_SHOW_AUCTIONHOUSE_CBE;


	public static String BTN_SHOW_EXPLICA_VOTE;
	public static String BTN_SHOW_EXPLICA_BUFFER;
	public static String BTN_SHOW_EXPLICA_TELEPORT;
	public static String BTN_SHOW_EXPLICA_SHOP;
	public static String BTN_SHOW_EXPLICA_WAREHOUSE;
	public static String BTN_SHOW_EXPLICA_AUGMENT;
	public static String BTN_SHOW_EXPLICA_SUBCLASES;
	public static String BTN_SHOW_EXPLICA_CLASS_TRANSFER;
	public static String BTN_SHOW_EXPLICA_CONFIG_PANEL;
	public static String BTN_SHOW_EXPLICA_DROP_SEARCH;
	public static String BTN_SHOW_EXPLICA_PVPPK_LIST;
	public static String BTN_SHOW_EXPLICA_LOG_PELEAS;
	public static String BTN_SHOW_EXPLICA_CASTLE_MANAGER;
	public static String BTN_SHOW_EXPLICA_DESAFIO;
	public static String BTN_SHOW_EXPLICA_SYMBOL_MARKET;
	public static String BTN_SHOW_EXPLICA_CLANALLY;
	public static String BTN_SHOW_EXPLICA_PARTYFINDER;
	public static String BTN_SHOW_EXPLICA_FLAGFINDER;
	public static String BTN_SHOW_EXPLICA_COLORNAME;
	public static String BTN_SHOW_EXPLICA_DELEVEL;
	public static String BTN_SHOW_EXPLICA_REMOVE_ATRIBUTE;
	public static String BTN_SHOW_EXPLICA_BUG_REPORT;
	public static String BTN_SHOW_EXPLICA_DONATION;
	public static String BTN_SHOW_EXPLICA_CAMBIO_NOMBRE_PJ;
	public static String BTN_SHOW_EXPLICA_CAMBIO_NOMBRE_CLAN;
	public static String BTN_SHOW_EXPLICA_VARIAS_OPCIONES;
	public static String BTN_SHOW_EXPLICA_ELEMENT_ENHANCED;
	public static String BTN_SHOW_EXPLICA_ENCANTAMIENTO_ITEM;
	public static String BTN_SHOW_EXPLICA_AUGMENT_SPECIAL;
	public static String BTN_SHOW_EXPLICA_GRAND_BOSS_STATUS;
	public static String BTN_SHOW_EXPLICA_RAIDBOSS_INFO;
	public static String BTN_SHOW_EXPLICA_TRANSFORMATION;
	public static String BTN_SHOW_EXPLICA_VOTE_CB;
	public static String BTN_SHOW_EXPLICA_BUFFER_CB;
	public static String BTN_SHOW_EXPLICA_TELEPORT_CB;
	public static String BTN_SHOW_EXPLICA_SHOP_CB;
	public static String BTN_SHOW_EXPLICA_WAREHOUSE_CB;
	public static String BTN_SHOW_EXPLICA_AUGMENT_CB;
	public static String BTN_SHOW_EXPLICA_SUBCLASES_CB;
	public static String BTN_SHOW_EXPLICA_CLASS_TRANSFER_CB;
	public static String BTN_SHOW_EXPLICA_CONFIG_PANEL_CB;
	public static String BTN_SHOW_EXPLICA_DROP_SEARCH_CB;
	public static String BTN_SHOW_EXPLICA_PVPPK_LIST_CB;
	public static String BTN_SHOW_EXPLICA_LOG_PELEAS_CB;
	public static String BTN_SHOW_EXPLICA_CASTLE_MANAGER_CB;
	public static String BTN_SHOW_EXPLICA_DESAFIO_CB;
	public static String BTN_SHOW_EXPLICA_SYMBOL_MARKET_CB;
	public static String BTN_SHOW_EXPLICA_CLANALLY_CB;
	public static String BTN_SHOW_EXPLICA_PARTYFINDER_CB;
	public static String BTN_SHOW_EXPLICA_FLAGFINDER_CB;
	public static String BTN_SHOW_EXPLICA_COLORNAME_CB;
	public static String BTN_SHOW_EXPLICA_DELEVEL_CB;
	public static String BTN_SHOW_EXPLICA_REMOVE_ATRIBUTE_CB;
	public static String BTN_SHOW_EXPLICA_BUG_REPORT_CB;
	public static String BTN_SHOW_EXPLICA_DONATION_CB;
	public static String BTN_SHOW_EXPLICA_CAMBIO_NOMBRE_PJ_CB;
	public static String BTN_SHOW_EXPLICA_CAMBIO_NOMBRE_CLAN_CB;
	public static String BTN_SHOW_EXPLICA_VARIAS_OPCIONES_CB;
	public static String BTN_SHOW_EXPLICA_ELEMENT_ENHANCED_CB;
	public static String BTN_SHOW_EXPLICA_ENCANTAMIENTO_ITEM_CB;
	public static String BTN_SHOW_EXPLICA_AUGMENT_SPECIAL_CB;
	public static String BTN_SHOW_EXPLICA_GRAND_BOSS_STATUS_CB;
	public static String BTN_SHOW_EXPLICA_RAIDBOSS_INFO_CB;
	public static String BTN_SHOW_EXPLICA_TRANSFORMATION_CB;
	public static String BTN_SHOW_EXPLICA_PARTYMATCHING_CB;
	public static String BTN_SHOW_EXPLICA_AUCTIONSHOUSE_CB;

	/**/
	public static String DESAFIO_85_PREMIO;
	public static String DESAFIO_NOBLE_PREMIO;
	public static Vector<Integer> DESAFIO_NPC_BUSQUEDAS = new Vector<Integer>();
	public static int DESAFIO_MAX_LVL85;
	public static int DESAFIO_MAX_NOBLE;
	public static boolean DESAFIO_LVL85;
	public static boolean DESAFIO_NOBLE;
	public static boolean DESAFIO_NPC;

	/**/

	public static boolean VOTO_SERVER_HOPZONE;
	public static boolean VOTO_SERVER_TOPZONE;



	public static boolean GIRAN;
	public static boolean ADEN;
	public static boolean RUNE;
	public static boolean OREN;
	public static boolean DION;
	public static boolean GLUDIO;
	public static boolean GODDARD;
	public static boolean SCHUTTGART;
	public static boolean INNADRIL;


	/*BUFFER*/

	public static int BUFFER_ID_ITEM;// = 57
	public static boolean BUFFER_CON_KARMA;// = True
	public static int BUFFER_LVL_MIN;// = 1
	public static int BUFFER_TIME_WAIT;// = 10
	public static boolean BUFF_GRATIS;// = False
	public static boolean RICARICA_SCRIPT;// = True
	public static boolean BUFFER_GM_ONLY;// = False
	public static int BUFFER_ID_ACCESO_GM;// = 8
	public static int BUFFER_ID_ACCESO_ADMIN;// = 8
	public static boolean BUFFER_SINGLE_BUFF_CHOICE;// = True// Set to true to enable single buff choice section.
	public static boolean BUFFER_SCHEME_SYSTEM;// = True	// Set to true to enable scheme system section.
	public static boolean BUFFER_IMPROVED_SECTION;// = True If true the relative button in the main window is enabled, otherwise the button is hidden.
	public static int BUFFER_IMPROVED_VALOR;// = 1000 If the above option is set to false, the follow is ignored, otherwise this is the price for every single buff in the section.
	public static boolean BUFFER_BUFF_SECTION;// = True  If true the relative button in the main window is enabled, otherwise the button is hidden.
	public static int BUFFER_BUFF_VALOR;// = 1000 If the above option is set to false, the follow is ignored, otherwise this is the price for every single buff in the section.
	public static boolean BUFFER_CHANT_SECTION;// = True If true the relative button in the main window is enabled, otherwise the button is hidden.
	public static int BUFFER_CHANT_VALOR;// = 1000 If the above option is set to false, the follow is ignored, otherwise this is the price for every single buff in the section.
	public static boolean BUFFER_DANCE_SECTION;// = True If true the relative button in the main window is enabled, otherwise the button is hidden.
	public static int BUFFER_DANCE_VALOR;// = 1000 If the above option is set to false, the follow is ignored, otherwise this is the price for every single buff in the section.
	public static boolean BUFFER_SONG_SECTION;// = True true the relative button in the main window is enabled, otherwise the button is hidden.
	public static int BUFFER_SONG_VALOR;// = 1000 If the above option is set to false, the follow is ignored, otherwise this is the price for every single buff in the section.
	public static boolean BUFFER_RESIST_SECTION;// = True If true the relative button in the main window is enabled, otherwise the button is hidden.
	public static int BUFFER_RESIST_VALOR;// = 1000 If the above option is set to false, the follow is ignored, otherwise this is the price for every single buff in the section.
	public static boolean BUFFER_CUBIC_SECTION;// = True If true the relative button in the main window is enabled, otherwise the button is hidden.
	public static int BUFFER_CUBIC_VALOR;// = 1000 If the above option is set to false, the follow is ignored, otherwise this is the price for every single buff in the section.
	public static boolean BUFFER_PROPHECY_SECTION;// = True If true the relative button in the main window is enabled, otherwise the button is hidden.
	public static int BUFFER_PROHECY_VALOR;// = 1000  If the above option is set to false, the follow is ignored, otherwise this is the price for every single buff in the section.
	public static boolean BUFFER_SPECIAL_SECTION;// = True If true the relative button in the main window is enabled, otherwise the button is hidden.
	public static int BUFFER_SPECIAL_VALOR;// = 1000  If the above option is set to false, the follow is ignored, otherwise this is the price for every single buff in the section.
	public static boolean BUFFER_OTROS_SECTION;// = True 	// If true the relative button in the main window is enabled, otherwise the button is hidden.
	public static int BUFFER_OTROS_VALOR;// = 1000 	// If the above option is set to false, the follow is ignored, otherwise this is the price for every single buff in the section.
	public static boolean BUFFER_AUTOBUFF;// = True
	public static boolean BUFFER_HEAL;// = True
	public static int BUFFER_HEAL_VALOR;// = 1000
	public static boolean BUFFER_REMOVE_BUFF;// = True
	public static int BUFFER_REMOVE_BUFF_VALOR;// = 1000
	public static int BUFFER_SCHEMA_X_PLAYER;// = 4
	public static boolean BUFFER_HEAL_CAN_FLAG;
	public static boolean BUFFER_HEAL_CAN_IN_COMBAT;
	/*BUFFER*/


	/*Configuracion del Buffer no Modificable por el usuario*/
		/*Ancho de la Páginas de Buff*/
		public static int WIDTH_GRAFI_IMPROVED = 275;
		public static int WIDTH_GRAFI_BUFF= 273;
		public static int WIDTH_GRAFI_CHANT = 275;
		public static int WIDTH_GRAFI_DANCE = 275;
		public static int WIDTH_GRAFI_SONG = 273;
		public static int WIDTH_GRAFI_RESIST = 275;
		public static int WIDTH_GRAFI_CUBIC = 274;
		public static int WIDTH_GRAFI_PROPHECY  = 275;
		public static int WIDTH_GRAFI_SPECIALI = 275;
		public static int WIDTH_GRAFI_ALTRI = 275;
		/*Capacidades de Buff*/
		public static int BUFF_PER_SCHEMA = Config.BUFFS_MAX_AMOUNT;
		public static int DANCE_PER_SCHEMA = Config.DANCES_MAX_AMOUNT;
		/*Tiempo espera entre Operaciones en el buffer*/
		public static boolean TIME_OUT = true;
		/*Grafica*/
		// Set this to 1 to see table borders in the chat windows. Usefull to edit the following widths. In live servers, default is '0'.
		public static int BORDO_TABELLA = 0;
	/*Configuracion del Buffer no Modificable por el usuario*/



		/*ZeuS SERVER*/
		
		
		public static boolean CHAT_SHOUT_BLOCK;
		public static boolean CHAT_TRADE_BLOCK;
		public static boolean CHAT_WISP_BLOCK;
		
		public static int CHAT_SHOUT_NEED_PVP;
		public static int CHAT_SHOUT_NEED_LEVEL;
		public static int CHAT_SHOUT_NEED_LIFETIME;
		
		public static int CHAT_TRADE_NEED_PVP;
		public static int CHAT_TRADE_NEED_LEVEL;
		public static int CHAT_TRADE_NEED_LIFETIME;

		public static int CHAT_WISP_NEED_PVP;
		public static int CHAT_WISP_NEED_LEVEL;
		public static int CHAT_WISP_NEED_LIFETIME;
		
		
		public static boolean PVP_COLOR_SYSTEM_ENABLED;
		public static boolean PK_COLOR_SYSTEM_ENABLED;
		public static boolean ANNOUCE_RAID_BOS_STATUS;

		public static boolean MENSAJE_PVP_PK_CICLOS;

		public static boolean ANNOUCE_TOP_PPVPPK_ENTER;
		public static boolean ANNOUCE_PJ_KARMA;
		public static boolean ANNOUCE_CLASS_OPONENT_OLY;

		public static boolean ALLOW_BLESSED_ESCAPE_PVP;
		public static boolean CAN_USE_BSOE_PK;
		public static boolean RATE_EXP_OFF;
		public static boolean SHOW_MY_STAT;
		public static boolean LOG_FIGHT_PVP_PK;
		public static boolean PVP_PK_GRAFICAL_EFFECT;

		public static String MENSAJE_ENTRADA_PJ_KARMA;
		public static String MENSAJE_ENTRADA_PJ_TOPPVPPK;

		public static String CICLO_MENSAJE_PVP_1;
		public static String CICLO_MENSAJE_PVP_2;
		public static String CICLO_MENSAJE_PVP_3;
		public static String CICLO_MENSAJE_PVP_4;
		public static String CICLO_MENSAJE_PVP_5;

		public static String CICLO_MENSAJE_PK_1;
		public static String CICLO_MENSAJE_PK_2;
		public static String CICLO_MENSAJE_PK_3;
		public static String CICLO_MENSAJE_PK_4;
		public static String CICLO_MENSAJE_PK_5;

		public static int CANTIDAD_CICLO_MENSAJE_PVP_1;
		public static int CANTIDAD_CICLO_MENSAJE_PVP_2;
		public static int CANTIDAD_CICLO_MENSAJE_PVP_3;
		public static int CANTIDAD_CICLO_MENSAJE_PVP_4;
		public static int CANTIDAD_CICLO_MENSAJE_PVP_5;

		public static int CANTIDAD_CICLO_MENSAJE_PK_1;
		public static int CANTIDAD_CICLO_MENSAJE_PK_2;
		public static int CANTIDAD_CICLO_MENSAJE_PK_3;
		public static int CANTIDAD_CICLO_MENSAJE_PK_4;
		public static int CANTIDAD_CICLO_MENSAJE_PK_5;

		public static int PVP_AMOUNT_1;
		public static int PVP_AMOUNT_2;
		public static int PVP_AMOUNT_3;
		public static int PVP_AMOUNT_4;
		public static int PVP_AMOUNT_5;
		public static int PVP_AMOUNT_6;
		public static int PVP_AMOUNT_7;
		public static int PVP_AMOUNT_8;
		public static int PVP_AMOUNT_9;
		public static int PVP_AMOUNT_10;
		public static int NAME_COLOR_FOR_PVP_AMOUNT_1;
		public static int NAME_COLOR_FOR_PVP_AMOUNT_2;
		public static int NAME_COLOR_FOR_PVP_AMOUNT_3;
		public static int NAME_COLOR_FOR_PVP_AMOUNT_4;
		public static int NAME_COLOR_FOR_PVP_AMOUNT_5;
		public static int NAME_COLOR_FOR_PVP_AMOUNT_6;
		public static int NAME_COLOR_FOR_PVP_AMOUNT_7;
		public static int NAME_COLOR_FOR_PVP_AMOUNT_8;
		public static int NAME_COLOR_FOR_PVP_AMOUNT_9;
		public static int NAME_COLOR_FOR_PVP_AMOUNT_10;
		public static String[] NAME_COLOR_FOR_ALL = new String[10];
		public static int PK_AMOUNT_1;
		public static int PK_AMOUNT_2;
		public static int PK_AMOUNT_3;
		public static int PK_AMOUNT_4;
		public static int PK_AMOUNT_5;
		public static int PK_AMOUNT_6;
		public static int PK_AMOUNT_7;
		public static int PK_AMOUNT_8;
		public static int PK_AMOUNT_9;
		public static int PK_AMOUNT_10;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_1;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_2;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_3;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_4;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_5;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_6;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_7;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_8;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_9;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_10;
		public static String[] TITLE_COLOR_FOR_ALL = new String[10];
		public static String RAID_ANNOUCEMENT_DIED;
		public static String RAID_ANNOUCEMENT_LIFE;
		public static int RAID_ANNOUCEMENT_ID_ANNOUCEMENT;

		public static boolean TRANSFORMATION_NOBLE;
		public static int TRANSFORMATION_LVL;
		public static String TRANSFORMATION_PRICE;
		public static boolean TRANSFORMATION_ESPECIALES;
		public static boolean TRANSFORMATION_RAIDBOSS;
		public static String TRANSFORMATION_ESPECIALES_PRICE;
		public static String TRANSFORMATION_RAIDBOSS_PRICE;
		public static boolean TRANSFORM_TIME;
		public static int TRANSFORM_TIME_MINUTES;
		public static int TRANSFORM_REUSE_TIME_MINUTES;

		public static boolean OLY_ANTIFEED_CHANGE_TEMPLATE;
		public static boolean OLY_ANTIFEED_SHOW_NAME_NPC;
		public static boolean OLY_ANTIFEED_SHOW_NAME_OPPO;
		public static boolean OLY_ANTIFEED_SHOW_IN_NAME_CLASS;

		public static int[] OLY_SECOND_SHOW_OPPONET;
		public static int[] OLY_ACCESS_ID_MODIFICAR_POINT;
		
		public static boolean OLY_DUAL_BOX_CONTROL;
		
		public static boolean OLY_CAN_USE_SCHEME_BUFFER;
		
		public static int[] ENCHANT_ANNOUCEMENT;
		public static int PVP_PK_PROTECTION_LVL;
		public static int PVP_PK_PROTECTION_LIFETIME_MINUTES;
		
		
		public static int ANNOUCE_PJ_KARMA_CANTIDAD;
		public static boolean SHOP_USE_BD;

		public static boolean PVP_REWARD;
		public static boolean PVP_PARTY_REWARD;
		public static String PVP_REWARD_ITEMS;
		public static String PVP_PARTY_REWARD_ITEMS;
		public static boolean PVP_REWARD_CHECK_DUALBOX;
		public static int PVP_REWARD_RANGE;

		public static boolean ANNOUNCE_KARMA_PLAYER_WHEN_KILL;
		public static String ANNOUNCE_KARMA_PLAYER_WHEN_KILL_MSN;

		//public static Vector<Integer> DESAFIO_NPC_BUSQUEDAS = new Vector<Integer>();
		protected static Vector<Integer> BOT_PLAYER = new Vector<Integer>();

		public static boolean SHOW_ZEUS_ENTER_GAME;


		public static String[][] PREGUNTAS_BOT = new String[800][2];
		public static int PREGUNTAS_BOT_CANT = 0;

		public static boolean ANTIBOT_COMANDO_STATUS;
		public static int ANTIBOT_MINUTOS_ESPERA;
		public static int ANTIBOT_OPORTUNIDADES;
		public static int ANTIBOT_MINUTOS_JAIL;
		public static int ANTIBOT_MOB_DEAD_TO_ACTIVATE;
		public static int ANTIBOT_MINUTE_VERIF_AGAIN;
		public static int ANTIBOT_MIN_LVL;
		public static int ANTIBOT_ANTIGUEDAD_MINUTOS;
		public static boolean ANTIBOT_NOBLE_ONLY;
		public static boolean ANTIBOT_HERO_ONLY;
		public static boolean ANTIBOT_GM_ONLY;
		public static boolean ANTIBOT_ANNOU_JAIL;
		public static boolean ANTIBOT_AUTO;
		public static boolean ANTIBOT_RESET_COUNT;
		public static boolean ANTIBOT_BORRAR_ITEM;
		public static int ANTIBOT_BORRAR_ITEM_PORCENTAJE;
		public static String ANTIBOT_BORRAR_ITEM_ID;
		public static boolean ANTIBOT_CHECK_INPEACE_ZONE;
		public static int ANTIBOT_INACTIVE_MINUTES;
		public static boolean ANTIBOT_SEND_ALL_IP;
		public static int ANTIBOT_SECONDS_TO_RESEND_ANTIBOT;
		public static boolean ANTIBOT_CHECK_DUALBOX;
		public static boolean ANTIBOT_SEND_JAIL_ALL_DUAL_BOX;
		public static boolean ANTIBOT_BLACK_LIST;
		public static int ANTIBOT_BLACK_LIST_MULTIPLIER;


		public static boolean BANIP_CHECK_IP_INTERNET;
		public static boolean BANIP_CHECK_IP_RED;
		public static boolean BANIP_STATUS;
		public static boolean BANIP_DISCONNECT_ALL_PLAYER;

		public static boolean BUFFCHAR_ACT;
		public static boolean BUFFCHAR_CAN_USE_FLAG;
		public static boolean BUFFCHAR_CAN_USE_PK;
		public static boolean BUFFCHAR_CAN_USE_COMBAT_MODE;
		public static boolean BUFFCHAR_CAN_USE_SIEGE_ZONE;
		public static boolean BUFFCHAR_CAN_USE_INDIVIDUAL_BUFF;
		public static boolean BUFFCHAR_FOR_FREE;
		public static boolean BUFFCHAR_INDIVIDUAL_FOR_FREE;
		public static boolean BUFFCHAR_HEAL_FOR_FREE;
		public static boolean BUFFCHAR_CANCEL_FOR_FREE;
		public static String BUFFCHAR_COST_USE;
		public static String BUFFCHAR_COST_HEAL;
		public static String BUFFCHAR_COST_CANCEL;
		public static String BUFFCHAR_COST_INDIVIDUAL;
		public static String BUFFCHAR_DONATION_SECCION;
		public static String BUFFCHAR_DONATION_SECCION_COST;
		public static boolean BUFFCHAR_DONATION_SECCION_REMOVE_ITEM;
		public static boolean BUFFCHAR_DONATION_SECCION_ACT;
		public static boolean BUFFCHAR_PET;
		public static boolean BUFFCHAR_EDIT_SCHEME_ON_MAIN_WINDOWS_BUFFER;

		public static HashMap<Integer, Float> PREMIUM_ITEM_CHAR_DROP_LIST = new HashMap<Integer, Float>();
		public static HashMap<Integer, Float> PREMIUM_ITEM_CLAN_DROP_LIST = new HashMap<Integer, Float>();		
		public static boolean PREMIUM_MESSAGE;
		
		
		public static String DONATION_EASY_NOTIFICATION;
		public static String DONATION_TYPE_LIST;
		


		public static boolean CHAR_PANEL;

		public static boolean COMMUNITY_BOARD;
		public static boolean COMMUNITY_BOARD_REGION;
		public static boolean COMMUNITY_BOARD_ENGINE;
		public static boolean COMMUNITY_BOARD_CLAN;
		public static boolean COMMUNITY_BOARD_GRAND_RB;
		public static boolean COMMUNITY_BOARD_PARTYFINDER;
		
		public static String COMMUNITY_BOARD_PART_EXEC;
		public static String COMMUNITY_BOARD_REGION_PART_EXEC;
		public static String COMMUNITY_BOARD_ENGINE_PART_EXEC;
		public static String COMMUNITY_BOARD_CLAN_PART_EXEC;
		public static String COMMUNITY_BOARD_GRAND_RB_EXEC;
		public static String COMMUNITY_BOARD_PARTYFINDER_EXEC;
		
		public static int COMMUNITY_BOARD_ROWS_FOR_PAGE;
		public static int COMMUNITY_BOARD_TOPPLAYER_LIST;
		public static int COMMUNITY_BOARD_CLAN_LIST;
		public static int COMMUNITY_BOARD_MERCHANT_LIST;
		public static int COMMUNITY_BOARD_CLAN_ROWN_LIST;

		public static boolean COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT;
		public static boolean COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_SIEGE;
		public static boolean COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CASTLE;
		public static boolean COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_ONLY_PEACEZONE;
		public static boolean COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CLANHALL_WITH_ANOTHER_CLAN;
		public static int COMMUNITY_BOARD_REGION_PLAYER_ON_LIST ; //481
		public static Vector<String> COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION = new Vector<String>();
		
		public static Vector<String>COMMUNITY_REGION_LEGEND = new Vector<String>();

		private static Vector<String> COMMUNITY_BOARD_SV_CONFIG = new Vector<String>();

		public static int COMMUNITY_BOARD_PARTYFINDER_SEC_BETWEEN_MSJE;
		public static int COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST;
		public static int COMMUNITY_BOARD_PARTYFINDER_SEC_ON_BOARD;
		public static boolean COMMUNITY_BOARD_PARTYFINDER_COMMAND_ENABLE;
		
		
		/*'590','COMMUNITY_BOARD_PARTYFINDER_SEC_BETWEEN_MSJE','40'
		'591','COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST','60'
		'592','COMMUNITY_BOARD_PARTYFINDER_SEC_ON_BOARD','360'
		'593','COMMUNITY_BOARD_PARTYFINDER_COMMAND_ENABLE','true'*/		
		
		
		
		
		
		//('572','DONATION_LV_85_COST','0'),('573','DONATION_NOBLE_COST','0'),('574','DONATION_FAME_COST','0'), ('575','DONATION_FAME_AMOUNT','200'),('576','DONATION_CLAN_LV_COST','0'),('577','DONATION_CLAN_LV_LV','5'),('578','DONATION_REDUCE_PK_COST','0'),('579','DONATION_CHANGE_SEX_COST','0'),('580','DONATION_AIO_CHAR_SIMPLE_COSTO','0'), ('581','DONATION_AIO_CHAR_30_COSTO','0'),('582','DONATION_AIO_CHAR_LV_REQUEST','72'),('583','DONATION_CHANGE_CHAR_NAME_COST','0'),('584','DONATION_CHANGE_CLAN_NAME_COST','0')
		
		
		public static int DONATION_NOBLE_COST;
		public static int DONATION_CHANGE_SEX_COST;
		public static int DONATION_AIO_CHAR_SIMPLE_COSTO;
		public static int DONATION_AIO_CHAR_30_COSTO;
		public static int DONATION_AIO_CHAR_LV_REQUEST;
		public static int DONATION_CHANGE_CHAR_NAME_COST;
		public static int DONATION_CHANGE_CLAN_NAME_COST;
		public static String DONATION_EXPLAIN_HOW_DO_IT;
		public static int DONATION_255_RECOMMENDS;
		public static HashMap<Integer,Integer> DONATION_CHARACTERS_LEVEL = new HashMap<Integer, Integer>();
		public static HashMap<Integer,Integer> DONATION_CLAN_REPUTATION = new HashMap<Integer, Integer>();
		public static HashMap<Integer,Integer> DONATION_CLAN_SKILL = new HashMap<Integer, Integer>();
		public static HashMap<Integer,Integer> DONATION_CLAN_LEVEL = new HashMap<Integer, Integer>();
		public static HashMap<Integer,Integer> DONATION_CHARACTERS_FAME_POINT = new HashMap<Integer, Integer>();
		public static HashMap<Integer,Integer> DONATION_CHARACTERS_PK_POINT = new HashMap<Integer, Integer>();
		public static HashMap<Integer,Integer> DONATION_ENCHANT_ITEM_ARMOR = new HashMap<Integer,Integer>();
		public static HashMap<Integer,Integer> DONATION_ENCHANT_ITEM_WEAPON = new HashMap<Integer,Integer>();
		public static HashMap<Integer,Integer> DONATION_ELEMENTAL_ITEM_ARMOR = new HashMap<Integer,Integer>();
		public static HashMap<Integer,Integer> DONATION_ELEMENTAL_ITEM_WEAPON = new HashMap<Integer,Integer>();

		public static boolean PREMIUM_CHAR;
		public static boolean PREMIUM_CLAN;

		
		public static boolean EVENT_TOWN_WAR_AUTOEVENT;
		public static int EVENT_TOWN_WAR_MINUTES_START_SERVER;
		public static int EVENT_TOWN_WAR_MINUTES_INTERVAL;
		public static String EVENT_TOWN_WAR_CITY_ON_WAR;
		public static int EVENT_TOWN_WAR_MINUTES_EVENT;
		public static int EVENT_TOWN_WAR_JOIN_TIME;
		public static boolean EVENT_TOWN_WAR_DUAL_BOX_CHECK;
		
		public static boolean EVENT_TOWN_WAR_GIVE_PVP_REWARD;
		public static boolean EVENT_TOWN_WAR_GIVE_REWARD_TO_TOP_PPL_KILLER;
		public static String EVENT_TOWN_WAR_REWARD_GENERAL;
		public static String EVENT_TOWN_WAR_REWARD_TOP_PLAYER;
		public static boolean EVENT_TOWN_WAR_HIDE_NPC;
		public static boolean EVENT_TOWN_WAR_RANDOM_CITY;
		public static String EVENT_TOWN_WAR_NPC_ID_HIDE;
		public static boolean EVENT_TOWN_WAR_CAN_USE_BUFFER;
		
		
		
		public static boolean EVENT_RAIDBOSS_AUTOEVENT;
		public static Vector<Integer> EVENT_RAIDBOSS_RAID_ID = new Vector<Integer>();
		public static String EVENT_RAIDBOSS_RAID_POSITION;
		public static String EVENT_RAIDBOSS_PLAYER_POSITION;
		public static boolean EVENT_RAIDBOSS_PLAYER_INMOBIL;
		public static boolean EVENT_RAIDBOSS_CHECK_DUALBOX;
		public static String EVENT_RAIDBOSS_REWARD_WIN;
		public static String EVENT_RAIDBOSS_REWARD_LOOSER;
		public static int EVENT_RAIDBOSS_PLAYER_MIN_LEVEL;
		public static int EVENT_RAIDBOSS_PLAYER_MAX_LEVEL;
		public static int EVENT_RAIDBOSS_PLAYER_MIN_REGIS;
		public static int EVENT_RAIDBOSS_PLAYER_MAX_REGIS;
		public static int EVENT_RAIDBOSS_SECOND_TO_BACK;
		public static int EVENT_RAIDBOSS_JOINTIME = 0;
		public static int EVENT_RAIDBOSS_EVENT_TIME = 0;
		public static String EVENT_RAIDBOSS_COLORNAME;
		public static boolean EVENT_RAIDBOSS_CANCEL_BUFF;
		public static boolean EVENT_RAIDBOSS_UNSUMMON_PET;
		public static int EVENT_RAIDBOSS_SECOND_TO_REVIVE;
		public static int EVENT_RAIDBOSS_SECOND_TO_TELEPORT_RADIBOSS;
		public static String EVENT_RAIDBOSS_HOUR_TO_START;
		public static int EVENT_RAIDBOSS_MINUTE_INTERVAL;
		public static int EVENT_RAIDBOSS_MINUTE_INTERVAL_START_SERVER;		

		public static boolean REGISTER_EMAIL_ONLINE;
		public static int REGISTER_NEW_PLAYER_WAITING_TIME;
		public static int REGISTER_NEW_PLAYER_RE_ENTER_WAITING_TIME;
		public static int REGISTER_NEW_PlAYER_TRIES;
		public static boolean REGISTER_NEW_PLAYER_BLOCK_CHAT;
		public static boolean REGISTER_NEW_PLAYER_CHECK_BANNED_ACCOUNT;
		
		
		public static boolean EVENT_REPUTATION_CLAN;		
		public static int EVENT_REPUTATION_CLAN_ID_NPC;
		public static int EVENT_REPUTATION_LVL_TO_GIVE;
		public static int EVENT_REPUTATION_REPU_TO_GIVE;
		public static int EVENT_REPUTATION_MIN_PLAYER;
		public static boolean EVENT_REPUTATION_NEED_ALL_MEMBER_ONLINE;
		
		

		//public static HashMap<String,HashMap<String, HashMap<String, String>>> ZeusPremium = new HashMap<String, HashMap<String,HashMap<String,String>>>();


		public static boolean VOTE_SHOW_ZEUS_ONLY_BY_ITEM;
		public static int VOTE_SHOW_ZEUS_ONLY_BY_ITEM_ID;

		public static boolean VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM;
		public static int VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM_ID;
		public static String VOTE_SHOW_ZEUS_TEMPORAL_PRICE;
		public static boolean VOTE_REWARD_PERSONAL_VOTE_ALL_IP_PLAYER;
		public static boolean VOTE_EVERY_12_HOURS;

		public static int EVENT_COLISEUM_NPC_ID;
		
		public static Integer ANTIFEED_ENCHANT_SKILL_REUSE;

		public static boolean SHOW_NEW_MAIN_WINDOWS;
		public static boolean SHOW_FIXME_WINDOWS;

		protected static HashMap<Integer, Integer> ANTIBOT_PLAYER_MOB_KILL = new HashMap<Integer, Integer>();
		protected static HashMap<Integer, Integer> ANTIBOT_PLAYER_TIME = new HashMap<Integer, Integer>();

		protected static HashMap<L2PcInstance,HashMap<Integer,String>> CHAR_CONEX = new HashMap<L2PcInstance,HashMap<Integer,String>>();

		protected static Vector<String> IPBAN_INTERNET = new Vector<String>();
		protected static Vector<String> IPBAN_MAQUINA = new Vector<String>();
		//IDITEM, IDNPC,Nombre


		public static HashMap<Integer,HashMap<Integer,HashMap<String,String>>> DROP_LIST_ITEM = new HashMap<Integer,HashMap<Integer,HashMap<String,String>>>();

		public static HashMap<L2PcInstance,HashMap<Integer,HashMap<String,Integer>>> DRESSME_PLAYER = new HashMap<L2PcInstance,HashMap<Integer,HashMap<String,Integer>>>();
		public static HashMap<L2PcInstance,Boolean> DRESSME_SHARE = new HashMap<L2PcInstance,Boolean>();


		private static HashMap<L2PcInstance,HashMap<String,Boolean>> playerConfig = new HashMap<L2PcInstance,HashMap<String,Boolean>>();
		private static HashMap<L2PcInstance, Integer> countPinCode = new HashMap<L2PcInstance, Integer>();
		public static HashMap<L2PcInstance,Integer> charPVPCOUNT = new HashMap<L2PcInstance, Integer>();
		public static HashMap<L2PcInstance,Integer> charPKCOUNT = new HashMap<L2PcInstance, Integer>();
		public static HashMap<L2PcInstance,Integer> charBufferTime = new HashMap<L2PcInstance, Integer>();
		public static HashMap<L2PcInstance,Integer> havePetSum = new HashMap<L2PcInstance, Integer>();
		public static HashMap<L2PcInstance,Integer> blockUntilTime = new HashMap<L2PcInstance, Integer>();
		public static HashMap<L2PcInstance,Integer> BLOQUEO_ACCION = new HashMap<L2PcInstance, Integer>();
		//public static HashMap<L2PcInstance,Boolean> EXP_BLOCK = new HashMap<L2PcInstance, Boolean>();

		private static HashMap<L2PcInstance,Integer> OnlineTimeToday = new HashMap<L2PcInstance,Integer>();
		
		public static void setNewTimeLife(L2PcInstance player){
			OnlineTimeToday.put(player, opera.getUnixTimeNow());
		}
		
		public static String getLifeToday(L2PcInstance player){
			if(OnlineTimeToday!=null){
				if(!OnlineTimeToday.containsKey(player)){
					OnlineTimeToday.put(player,unixTimeBeginServer);
				}
			}
			int unixNow = opera.getUnixTimeNow();
			int Resultado = unixNow - OnlineTimeToday.get(player);
			String RetornoStr = opera.getTiempoON(Resultado);
			return RetornoStr;
		}

		public static HashMap<String,HashMap<Integer,HashMap<String,String>>> BUFF_CHAR_DATA = new HashMap<String,HashMap<Integer,HashMap<String,String>>>();

		private static void loadbuffData_buffList(int idCat, String NomCat){
			Connection conn = null;
			try{
				conn = ConnectionFactory.getInstance().getConnection();
				String qry = "SELECT id,idBuff,level,act,secc,descrip,nom FROM zeus_buff_char_buff WHERE secc = " + String.valueOf(idCat);
				PreparedStatement psqry = conn.prepareStatement(qry);
				ResultSet rss = psqry.executeQuery();
					while (rss.next()){
						try{
							if(!BUFF_CHAR_DATA.containsKey(NomCat)){
								BUFF_CHAR_DATA.put(NomCat, new HashMap<Integer,HashMap<String,String>>());
							}

							if(!BUFF_CHAR_DATA.get(NomCat).containsKey(rss.getInt(2))){
								BUFF_CHAR_DATA.get(NomCat).put(rss.getInt(2),new HashMap<String, String>());
							}
							BUFF_CHAR_DATA.get(NomCat).get(rss.getInt(2)).put("ID", String.valueOf(rss.getInt(2)));
							BUFF_CHAR_DATA.get(NomCat).get(rss.getInt(2)).put("LEVEL", String.valueOf(rss.getInt(3)));
							BUFF_CHAR_DATA.get(NomCat).get(rss.getInt(2)).put("SEC", String.valueOf(rss.getInt(5)));
							BUFF_CHAR_DATA.get(NomCat).get(rss.getInt(2)).put("NOM", String.valueOf(rss.getString(7)));
							BUFF_CHAR_DATA.get(NomCat).get(rss.getInt(2)).put("DESCR", String.valueOf(rss.getString(6)));
							BUFF_CHAR_DATA.get(NomCat).get(rss.getInt(2)).put("ACT", rss.getString(4));
						}catch(SQLException e){

						}
					}
				//conn.close();
				}catch(SQLException e){

				}
			try{
				//conn.close();
			}catch (Exception e) {

			}
		}

		public static void loadBuffData(){
			loadBuffDataChar();
		}

		private static void loadBuffDataChar(){
			BUFF_CHAR_DATA.clear();
			Connection conn = null;
			try{
				conn = ConnectionFactory.getInstance().getConnection();
				String qry = "SELECT id,nomcat,posi FROM zeus_buff_char_cate order by posi";
				PreparedStatement psqry = conn.prepareStatement(qry);
				ResultSet rss = psqry.executeQuery();
					while (rss.next()){
						try{
							if(!BUFF_CHAR_DATA.containsKey("CATE")){
								BUFF_CHAR_DATA.put("CATE", new HashMap<Integer,HashMap<String,String>>());
							}
							BUFF_CHAR_DATA.get("CATE").put(rss.getInt(1), new HashMap<String, String>());
							BUFF_CHAR_DATA.get("CATE").get(rss.getInt(1)).put("IDCATE", String.valueOf(rss.getInt(1)));
							BUFF_CHAR_DATA.get("CATE").get(rss.getInt(1)).put("NOMCATE", rss.getString(2));
							BUFF_CHAR_DATA.get("CATE").get(rss.getInt(1)).put("POSI", String.valueOf(rss.getInt(3)));
							loadbuffData_buffList(rss.getInt(1),rss.getString(2));
						}catch(SQLException e){

						}
					}
				//conn.close();
				}catch(SQLException e){

				}
			try{
			conn.close();
			}catch (Exception e) {
				// TODO: handle exception
			}
		}


		private static HashMap<L2PcInstance,Vector<String>> CHAR_BUFF_SCH = new HashMap<L2PcInstance, Vector<String>>();

		public static void deleteSchemme(L2PcInstance player, String nomSch){
			CHAR_BUFF_SCH.get(player).remove(nomSch);
		}

		public static Vector<String> getNameBuffSch (L2PcInstance player){
			if(!CHAR_BUFF_SCH.containsKey(player)){
				return null;
			}
			return CHAR_BUFF_SCH.get(player);
		}


		public static void setCharVariables(L2PcInstance cha){
			general.getInstance().loadCharConfig(cha);
			general.getInstance().loadCharBuffer(cha);
			bufferChar.getSchemmeToChar(cha);
			charPVPCOUNT.put(cha, 0);
			charPKCOUNT.put(cha, 0);

			general.getInstance().setConfigExpSp(cha, true);

			pinCode.setPinStatusGeneral(cha,false);

			if(!charBufferTime.containsKey(cha)){
				charBufferTime.put(cha, 0);
			}
			if(!havePetSum.containsKey(cha)){
				havePetSum.put(cha, 0);
			}
			if(!blockUntilTime.containsKey(cha)){
				blockUntilTime.put(cha, 0);
			}
			if(!BLOQUEO_ACCION.containsKey(cha)){
				BLOQUEO_ACCION.put(cha, 0);
			}

			if(!getCharConfigPIN(cha)){
				pinCode.setPinStatusGeneral(cha, true);
			}

		}

		public static int getPinCountChar(L2PcInstance cha){
			if(countPinCode.containsKey(cha)){
				return countPinCode.get(cha);
			}
			return 0;
		}

		public static void setPinCountChar(L2PcInstance cha,int contador){
			if(!countPinCode.containsKey(cha)){
				countPinCode.put(cha, contador);
				return;
			}
			countPinCode.put(cha, contador);
		}

		public static void setPinCountChar(L2PcInstance cha){
			if(!countPinCode.containsKey(cha)){
				countPinCode.put(cha, 1);
				return;
			}
			countPinCode.put(cha, countPinCode.get(cha) + 1);
		}

		public static Vector<Integer> getDressmeTargetBlockList(){
			return DRESSME_ID_BLOCK;
		}


		public static void loadDropList(){
//			loadAllDropList();
		}

		public static void setNewDataTableDropList(){
			Connection conn = null;
			String SQL_INSERT = "call zeus_crear_table_drop_search()";
			try {
		  		  conn = ConnectionFactory.getInstance().getConnection();
		  		  CallableStatement psqry = conn.prepareCall(SQL_INSERT);
		  		  ResultSet rss = psqry.executeQuery();
		  		  if (rss.next()){
		  		  }
		  	}catch (SQLException e) {
		  		  e.printStackTrace();
		  	}

	  		try{
	  		//  conn.close();
	  		}catch(Exception a){

	  		}

//	  		loadAllDropList();
		}

		public void setConfigREFUSAL(L2PcInstance player,boolean value){
			setConfigPlayer(player,"REFUSAL",value);
		}
		public static boolean getCharConfigREFUSAL(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("REFUSAL");
		}

		public void setConfigPartyMatching(L2PcInstance player,boolean value){
			setConfigPlayer(player,"PARTYMATCHING",value);
		}
		public static boolean getCharConfigPartyMatching(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("PARTYMATCHING");
		}
		
		public void setConfigHideStore(L2PcInstance player,boolean value){
			setConfigPlayer(player,"HIDESTORE",value);
		}
		public static boolean getCharConfigHIDESTORE(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("HIDESTORE");
		}

		public void setConfigBadBuff(L2PcInstance player,boolean value){
			setConfigPlayer(player,"BADBUFF",value);
		}
		public static boolean getCharConfigBADBUFF(L2PcInstance player){
			try{
				if(!playerConfig.containsKey(player)){
					return false;
				}
				return playerConfig.get(player).get("BADBUFF");
			}catch(Exception a){
				return false;
			}
		}

		public void setConfigTrade(L2PcInstance player,boolean value){
			setConfigPlayer(player,"TRADE",value);
		}
		public static boolean getCharConfigTRADE(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("TRADE");
		}

		public void setConfigExpSp(L2PcInstance player, boolean value){
			setConfigPlayer(player,"EXPSP",value);
		}
		public static boolean getCharConfigEXPSP(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("EXPSP");
		}

		public void setConfigHeroPlayer(L2PcInstance player, boolean value){
			setConfigPlayer(player,"HERO",value);
		}

		public static boolean getCharConfigHERO(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("HERO");
		}

		public void setConfigPIN(L2PcInstance player, boolean value){
			setConfigPlayer(player, "PIN", value);
		}

		public static boolean getCharConfigPIN(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("PIN");
		}

		public void setConfigEFFECT(L2PcInstance player, boolean value){
			setConfigPlayer(player, "EFFECT", value);
		}

		public static boolean getCharConfigEFFECT(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("EFFECT");
		}

		public void setConfigANNOU(L2PcInstance player, boolean value){
			setConfigPlayer(player, "ANNOU", value);
		}

		public static boolean getCharConfigANNOU(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("ANNOU");
		}

		public void setConfigSHOWSTAT(L2PcInstance player, boolean value){
			setConfigPlayer(player,"SHOWSTAT",value);
		}

		public static boolean getCharConfigSHOWSTAT(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("SHOWSTAT");
		}

		public void setConfigBANOLY(L2PcInstance player, boolean value){
			setConfigPlayer(player, "BANOLY", value);
		}

		public static boolean getCharConfigBANOLY(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("BANOLY");
		}

	/*	public static void loadCharConfigAll(L2PcInstance player){
			//"sp_char_config" IN tipo SMALLINT, IN SECIN INTEGER, IN idChar INTEGER, IN ValueIN VARCHAR(20)
			String Consulta = "call sp_char_config(?,?,?,?)";
			try (Connection con = ConnectionFactory.getInstance().getConnection();
					PreparedStatement ps = con.prepareStatement(Consulta))
					{
					ps.setInt(1, 1);
					ps.setInt(2, 1);
					ps.setInt(3, player.getObjectId());
					ps.setString(4, "");
					try (ResultSet rs = ps.executeQuery())
					{
						while (rs.next())
						{
							setConfigANNOU(player, rs.getInt(2)==0?false:true);
							setConfigEFFECT(player, rs.getInt(3)==0?false:true);
							setConfigSHOWSTAT(player, rs.getInt(4)==0?false:true);
							setConfigPIN(player, rs.getInt(5)==0?false:true);
							setConfigBANOLY(player, rs.getInt(7)==0?false:true);
						}
					}
					}
				catch (SQLException e)
				{
					_log.severe("Error Consulta PIN: " + e.getMessage());
				}
		}
*/

		public void loadCharBuffer(L2PcInstance player){
			String Consulta = "call sp_buff_char_sch(?,?,?,0,0,'')";

			try{
				CHAR_BUFF_SCH.get(player).clear();
			}catch(Exception a){

			}

			try (Connection con = ConnectionFactory.getInstance().getConnection();
					PreparedStatement ps = con.prepareStatement(Consulta))
					{
					ps.setInt(1, 1);
					ps.setInt(2, player.getObjectId());
					ps.setString(3, "");
					try (ResultSet rs = ps.executeQuery())
					{
						while (rs.next())
						{
							if(rs.getString(1).equals("cor")){
								if(!CHAR_BUFF_SCH.containsKey(player)){
									CHAR_BUFF_SCH.put(player, new Vector<String>());
								}
								CHAR_BUFF_SCH.get(player).add(rs.getString(2));
							}
						}
					}
					}
				catch (SQLException e)
				{
					_log.severe("Error Consulta PIN: " + e.getMessage());
				}
		}

		public static boolean havePremium(L2PcInstance player, boolean checkplayer){
			boolean retorno = false;
			if(!checkplayer){
				if(player.getClan()==null){
					return false;
				}
			}
			String idBusqueda = checkplayer ? player.getAccountName() : String.valueOf(player.getClan().getId());
			if(PREMIUM_SERVICES_CLAN_CHAR != null){
				if(PREMIUM_SERVICES_CLAN_CHAR.containsKey(idBusqueda)){
					retorno = PREMIUM_SERVICES_CLAN_CHAR.get(idBusqueda).ISACTIVE;
				}
			}

			return retorno;
		}

		public static boolean isPremium(L2PcInstance player, boolean checkplayer){
			return havePremium(player,checkplayer);
		}

		private static void updatePremiumTable(){
			String DeleteInfo = "DELETE FROM zeus_premium WHERE end_date <= ?";
			try (Connection conn = ConnectionFactory.getInstance().getConnection();
					PreparedStatement statementt = conn.prepareStatement(DeleteInfo))
				{
					// Remove or update a L2PcInstance skill from the character_skills table of the database
					statementt.setInt(1, opera.getUnixTimeNow());
					statementt.execute();
				}
				catch (Exception e)
				{
					
				}	
		}
		
		public static premiumPersonalData getPremiumDataFromPlayerOrClan(String DataBuscar){
			return PREMIUM_SERVICES_CLAN_CHAR.get(DataBuscar);
		}
		
		public static void loadZeuSPremium(){
			updatePremiumTable();
			if(PREMIUM_SERVICES_CLAN_CHAR!=null){
				PREMIUM_SERVICES_CLAN_CHAR.clear();
			}
			String Consulta ="Select * from zeus_premium";
			try (Connection con = ConnectionFactory.getInstance().getConnection();
					PreparedStatement ps = con.prepareStatement(Consulta))
					{
					try (ResultSet rs = ps.executeQuery())
					{
						while (rs.next())
						{
							//PREMIUM_SERVICES_CLAN_CHAR // String, premiumPersonalData
							premiumPersonalData t = new premiumPersonalData(rs.getString("ID"),rs.getInt("start_date"),rs.getInt("end_date"), ( rs.getString("tip").equals("ACCOUNT") ? true : false ), rs.getInt("idPremium"));
							PREMIUM_SERVICES_CLAN_CHAR.put(rs.getString("ID"), t);
						}
					}
					}
				catch (SQLException e)
				{
					_log.severe("Error Premium Load: " + e.getMessage());
				}
		}

		public void loadCharConfig(L2PcInstance player){
			String Consulta = "call sp_char_config(?,?,?,?)";
			try (Connection con = ConnectionFactory.getInstance().getConnection();
					PreparedStatement ps = con.prepareStatement(Consulta))
					{
					ps.setInt(1, 1);
					ps.setInt(2, 1);
					ps.setInt(3, player.getObjectId());
					ps.setString(4, "");
					try (ResultSet rs = ps.executeQuery())
					{
						while (rs.next())
						{
							setConfigANNOU(player, rs.getInt(2)==0?false:true);
							setConfigEFFECT(player, rs.getInt(3)==0?false:true);
							setConfigSHOWSTAT(player, rs.getInt(4)==0?false:true);
							setConfigPIN(player, rs.getInt(5)==0?false:true);
							setConfigBANOLY(player, rs.getInt(7)==0?false:true);
							setConfigHeroPlayer(player, rs.getInt(8)==0?false:true);
							setConfigExpSp(player, rs.getInt(9)==0?false:true);
							setConfigTrade(player, rs.getInt(10)==0?false:true);
							setConfigBadBuff(player, rs.getInt(11)==0?false:true);
							setConfigHideStore(player, rs.getInt(12)==0?false:true);
							setConfigREFUSAL(player, rs.getInt(13)==0?false:true);
							setConfigPartyMatching(player, rs.getInt(14)==0?false:true);
						}
					}
					}
				catch (SQLException e)
				{
					_log.severe("ZeuS Error-> Load Personal Config '"+ player.getName() +"': " + e.getMessage());
				}
		}

		private void setConfigPlayer(L2PcInstance player,String Parametro, boolean valor){
			if(!playerConfig.containsKey(player)){
				playerConfig.put(player, new HashMap<String,Boolean>());
			}
			playerConfig.get(player).put(Parametro, valor);
		}

		public static HashMap<L2PcInstance,HashMap<String,Boolean>> getConfigChar(){
			return playerConfig;
		}

/*
		private static boolean _loadAllDropList(){
			//DROP_LIST_ITEM
			//SpawnTable.getInstance().getSpawnTable()
			
			Vector<String> TiposNPC = new Vector<String>();

			TiposNPC.add("L2FlyMonster");
			TiposNPC.add("L2FlyRaidBoss");
			TiposNPC.add("L2Monster");
			TiposNPC.add("L2RaidBoss");
			
			
			/*if(!DROP_LIST_ITEM.containsKey(IdDrop)){
				DROP_LIST_ITEM.put(IdDrop,new HashMap<Integer,HashMap<String,String>>());
			}
			int IdMob = rss.getInt(6);
			if(!DROP_LIST_ITEM.get(IdDrop).containsKey(IdMob)){
				DROP_LIST_ITEM.get(IdDrop).put(IdMob,new HashMap<String,String>());
			}
			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("NAME", rss.getString(7));
			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("TYPE", rss.getString(9));
			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("LEVEL", String.valueOf(rss.getInt(8)));
			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("CATEGORY", String.valueOf(rss.getInt(4)));
			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("MIN", String.valueOf(rss.getInt(2)));
			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("MAX", String.valueOf(rss.getInt(3)));
			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("CHANCE", String.valueOf(  (100 * rss.getInt(5)) / 100000.0    ) );
			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("TELEPORT", rss.getString(10) + ","+rss.getString(11)+","+rss.getString(12));
			//Nombre:Level:ID:
			String NpcForSearch = rss.getString(7) + ":" + String.valueOf(rss.getInt(8)) + ":" + String.valueOf(IdMob);
			v_dropsearch.setNPCInfoForSearch(NpcForSearch);*/
/*
			int IdDrop = 0;
			int IdMob = 0;
			
			Comparator<L2NpcTemplate> NPC_NAME_COMPARATOR_ = (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName());
			
			//Iterator itr = SpawnTable.getInstance().getSpawnTable().entrySet().iterator();
			for(String TipoClaseBuscando: TiposNPC){
				List<L2NpcTemplate> L2NpcTemplateSort = NpcData.getInstance().getAllNpcOfClassType(TipoClaseBuscando);
				L2NpcTemplateSort.sort(NPC_NAME_COMPARATOR_);
				for(L2NpcTemplate tmpl : L2NpcTemplateSort){
					int IdNpc = tmpl.getId();
					if(tmpl.getDropData() != null){
						for(L2Spawn NpcSpawn : SpawnTable.getInstance().getSpawns(IdNpc)){
							for (L2DropCategory cat : tmpl.getDropData()){
			    				for(L2DropData DropIn : cat.getAllDrops()){
					    			if(!DROP_LIST_ITEM.containsKey(DropIn.getId())){
					    				DROP_LIST_ITEM.put(DropIn.getId(),new HashMap<Integer,HashMap<String,String>>());
					    			}
					    			if(!DROP_LIST_ITEM.get(DropIn.getId()).containsKey(tmpl.getId())){
					    				DROP_LIST_ITEM.get(DropIn.getId()).put(tmpl.getId(),new HashMap<String,String>());
					    			}				    			
					    			IdDrop = DropIn.getId();
					    			IdMob = tmpl.getId();
					    			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("NAME", tmpl.getName());
					    			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("TYPE", tmpl.getType());
					    			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("LEVEL", String.valueOf(tmpl.getLevel()));
					    			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("CATEGORY", String.valueOf(cat.getCategoryType()));
					    			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("MIN", String.valueOf(DropIn.getMinDrop()));
					    			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("MAX", String.valueOf(DropIn.getMaxDrop()));
					    			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("CHANCE", opera.getChancePorcentaje(String.valueOf(DropIn.getChance())));
					    			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("TELEPORT", String.valueOf(NpcSpawn.getLocation().getX()) + ","+String.valueOf(NpcSpawn.getLocation().getY())+","+String.valueOf(NpcSpawn.getLocation().getZ()));
					    			String NpcForSearch = tmpl.getName() + ":" + String.valueOf(tmpl.getLevel()) + ":" + String.valueOf(IdMob);
									v_dropsearch.setNPCInfoForSearch(NpcForSearch);
			    				}
			    				//Collections.sort(DROP_LIST_ITEM.get(IdDrop).get(IdMob));
			    				//Collections.so
			    				Map<String, String> treeMap = new TreeMap<String, String>(DROP_LIST_ITEM.get(IdDrop).get(IdMob));
			    				
			    			}					
						}
					}
				}
			}
		    _log.warning("--- ZeuS Drop List Has been loaded "+ String.valueOf(DROP_LIST_ITEM.size()) +" items from Drop List ---");
		    
		    
		    return true;
		}


		protected static void loadAllDropList2(){
			if(_loadAllDropList()){
				return;
			}
			//IDITEM, IDNPC,Nombre, "Categoria", "Valor"
			//DROP_LIST_ITEM
			String TiposProhibidos = "'L2Npc','L2FestivalMonster','L2Chest'";

			Vector<String>SQLConsulta = new Vector<String>();
			
			
			SQLConsulta.add("SELECT "+
				"droplist.itemId,"+
				"droplist.min,"+
				"droplist.max,"+
				"droplist.category,"+
				"droplist.chance,"+
				"droplist.mobId,"+
				"npc.`name`,"+
				"npc.`level`,"+
				"npc.type,"+
				"custom_spawnlist.locx,"+
				"custom_spawnlist.locy,"+
				"custom_spawnlist.locz"+
				" FROM " + 
				" droplist "+
				" INNER JOIN npc ON droplist.mobId = npc.id " + 
				" INNER JOIN custom_spawnlist ON npc.id = custom_spawnlist.npc_templateid"+
				" WHERE "+
				" npc.type NOT IN ("+ TiposProhibidos +")");

			SQLConsulta.add("SELECT "+
					"custom_droplist.itemId,"+
					"custom_droplist.min,"+
					"custom_droplist.max,"+
					"custom_droplist.category,"+
					"custom_droplist.chance,"+
					"custom_droplist.mobId,"+
					"npc.`name`,"+
					"npc.`level`,"+
					"npc.type,"+
					"grandboss_data.loc_x,"+
					"grandboss_data.loc_y,"+
					"grandboss_data.loc_z"+
					" FROM "+
					"custom_droplist "+
					"INNER JOIN npc ON custom_droplist.mobId = npc.id "+
					"INNER JOIN grandboss_data ON npc.id = grandboss_data.boss_id "+
					" WHERE "+
					"npc.type NOT IN ("+ TiposProhibidos +")");

			SQLConsulta.add("SELECT "+
					"custom_droplist.itemId,"+
					"custom_droplist.min,"+
					"custom_droplist.max,"+
					"custom_droplist.category,"+
					"custom_droplist.chance,"+
					"custom_droplist.mobId,"+
					"npc.`name`,"+
					"npc.`level`,"+
					"npc.type,"+
					"raidboss_spawnlist.loc_x,"+
					"raidboss_spawnlist.loc_y,"+
					"raidboss_spawnlist.loc_z"+
					" FROM "+
					"custom_droplist "+
					"INNER JOIN npc ON custom_droplist.mobId = npc.id "+
					"INNER JOIN raidboss_spawnlist ON npc.id = raidboss_spawnlist.boss_id "+
					" WHERE "+
					"npc.type NOT IN ("+ TiposProhibidos +")");

			SQLConsulta.add("SELECT "+
							"custom_droplist.itemId,"+
							"custom_droplist.min,"+
							"custom_droplist.max,"+
							"custom_droplist.category,"+
							"custom_droplist.chance,"+
							"custom_droplist.mobId,"+
							"npc.`name`,"+
							"npc.`level`,"+
							"npc.type,"+
							"spawnlist.locx,"+
							"spawnlist.locy,"+
							"spawnlist.locz"+
							" FROM "+
							"custom_droplist "+
							"INNER JOIN npc ON custom_droplist.mobId = npc.id "+
							"INNER JOIN spawnlist ON npc.id = spawnlist.npc_templateid "+
							" WHERE "+
							"npc.type NOT IN ("+ TiposProhibidos +")");




			SQLConsulta.add("SELECT custom_droplist.itemId,"+
							"custom_droplist.min,"+
							"custom_droplist.max,"+
							"custom_droplist.category,"+
							"custom_droplist.chance,"+
							"custom_droplist.mobId,"+
							"custom_npc.`name`,"+
							"custom_npc.`level`,"+
							"custom_npc.type, "+
							"custom_spawnlist.locx,"+
							"custom_spawnlist.locy,"+
							"custom_spawnlist.locz "+
							"FROM "+
							"custom_droplist "+
							"INNER JOIN custom_npc ON custom_droplist.mobId = custom_npc.id "+
							"INNER JOIN custom_spawnlist ON custom_npc.id = custom_spawnlist.npc_templateid "+
							" WHERE "+
							"custom_npc.id IN (SELECT custom_spawnlist.npc_templateid FROM custom_spawnlist) AND custom_npc.type NOT IN ("+ TiposProhibidos +")");

			SQLConsulta.add("SELECT droplist.itemId,"+
					"droplist.min,"+
					"droplist.max,"+
					"droplist.category,"+
					"droplist.chance,"+
					"droplist.mobId,"+
					"npc.`name`,"+
					"npc.`level`,"+
					"npc.type, "+
					"spawnlist.locx,"+
					"spawnlist.locy,"+
					"spawnlist.locz "+
					"FROM "+
					"droplist "+
					"INNER JOIN npc ON droplist.mobId = npc.id "+
					"INNER JOIN spawnlist ON npc.id = spawnlist.npc_templateid "+
					" WHERE "+
					"npc.id IN (SELECT spawnlist.npc_templateid FROM spawnlist) AND npc.type NOT IN ("+ TiposProhibidos +")");


			SQLConsulta.add("SELECT custom_droplist.itemId,"+
					"custom_droplist.min,"+
					"custom_droplist.max,"+
					"custom_droplist.category,"+
					"custom_droplist.chance,"+
					"custom_droplist.mobId,"+
					"custom_npc.`name`,"+
					"custom_npc.`level`,"+
					"custom_npc.type, "+
					"raidboss_spawnlist.loc_x,"+
					"raidboss_spawnlist.loc_y,"+
					"raidboss_spawnlist.loc_z"+
					" FROM "+
					" custom_droplist "+
					"INNER JOIN custom_npc ON custom_droplist.mobId = custom_npc.id "+
					"INNER JOIN raidboss_spawnlist ON custom_npc.id = raidboss_spawnlist.boss_id "+
					" WHERE "+
					"custom_npc.id IN (SELECT raidboss_spawnlist.boss_id FROM  raidboss_spawnlist) AND custom_npc.type NOT IN ("+ TiposProhibidos +")");


			SQLConsulta.add("SELECT droplist.itemId,"+
					"droplist.min,"+
					"droplist.max,"+
					"droplist.category,"+
					"droplist.chance,"+
					"droplist.mobId,"+
					"npc.`name`,"+
					"npc.`level`,"+
					"npc.type, "+
					"raidboss_spawnlist.loc_x,"+
					"raidboss_spawnlist.loc_y,"+
					"raidboss_spawnlist.loc_z"+
					" FROM "+
					" droplist "+
					"INNER JOIN npc ON droplist.mobId = npc.id "+
					"INNER JOIN raidboss_spawnlist ON npc.id = raidboss_spawnlist.boss_id "+
					" WHERE "+
					"npc.id IN (SELECT raidboss_spawnlist.boss_id FROM  raidboss_spawnlist) AND npc.type NOT IN ("+ TiposProhibidos +")");
			Connection conn = null;
			PreparedStatement psqry = null;
			ResultSet rss = null;
			for(String sqlConsulta: SQLConsulta){
						try{
							conn = ConnectionFactory.getInstance().getConnection();
							psqry = conn.prepareStatement(sqlConsulta);
							rss = psqry.executeQuery();
							while(rss.next()){
								int IdDrop = rss.getInt(1);
								if(!DROP_LIST_ITEM.containsKey(IdDrop)){
									DROP_LIST_ITEM.put(IdDrop,new HashMap<Integer,HashMap<String,String>>());
								}
								int IdMob = rss.getInt(6);
								if(!DROP_LIST_ITEM.get(IdDrop).containsKey(IdMob)){
									DROP_LIST_ITEM.get(IdDrop).put(IdMob,new HashMap<String,String>());
								}
								DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("NAME", rss.getString(7));
								DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("TYPE", rss.getString(9));
								DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("LEVEL", String.valueOf(rss.getInt(8)));
								DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("CATEGORY", String.valueOf(rss.getInt(4)));
								DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("MIN", String.valueOf(rss.getInt(2)));
								DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("MAX", String.valueOf(rss.getInt(3)));
								DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("CHANCE", String.valueOf(  (100 * rss.getInt(5)) / 100000.0    ) );
								DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("TELEPORT", rss.getString(10) + ","+rss.getString(11)+","+rss.getString(12));
								//Nombre:Level:ID:
								String NpcForSearch = rss.getString(7) + ":" + String.valueOf(rss.getInt(8)) + ":" + String.valueOf(IdMob);
								v_dropsearch.setNPCInfoForSearch(NpcForSearch);
							}
						}catch(SQLException a){
							_log.warning("ZEUS DROPLIST ERROR->" + a.getMessage());
						}
						try{
							conn.close();
							psqry.close();
							rss.close();
						}catch(Exception a){

						}
			}

			_log.warning("--- ZeuS Drop List Has been loaded "+ String.valueOf(DROP_LIST_ITEM.size()) +" items from Drop List ---");

		}

		protected static void loadAllDropList(){
			loadAllDropList2();

			if((DROP_LIST_ITEM.size()>0) || (DROP_LIST_ITEM==null)){
				return;
			}

			boolean conerror = false;
			//IDITEM, IDNPC,Nombre, "Categoria", "Valor"
			//DROP_LIST_ITEM
			String TiposProhibidos = "'L2Npc','L2FestivalMonster','L2Chest'";

			String SQLConsulta = "SELECT *,get_concatenaciones(3,zeus_droplist.modID) FROM zeus_droplist WHERE TypeNPC NOT IN ("+TiposProhibidos+") AND ( (zeus_droplist.modID IN ( SELECT spawnlist.npc_templateid FROM spawnlist ) ) 		OR (zeus_droplist.modID IN ( SELECT custom_spawnlist.npc_templateid FROM custom_spawnlist ) ) 		OR (zeus_droplist.modID IN (SELECT raidboss_spawnlist.boss_id FROM raidboss_spawnlist)))";
			Connection conn = null;
			PreparedStatement psqry = null;
			ResultSet rss = null;
			try{
				conn = ConnectionFactory.getInstance().getConnection();
				psqry = conn.prepareStatement(SQLConsulta);
				rss = psqry.executeQuery();
				while(rss.next()){
					for(String DatosDrop : rss.getString(2).split(";")){
						String DROP[] = DatosDrop.split(",");
						int IdDrop = Integer.valueOf(DROP[0].replace("-", ""));
						if(!DROP_LIST_ITEM.containsKey(IdDrop)){
							DROP_LIST_ITEM.put(IdDrop,new HashMap<Integer,HashMap<String,String>>());
						}
						int IdMob = rss.getInt(1);
						if(!DROP_LIST_ITEM.get(IdDrop).containsKey(IdMob)){
							DROP_LIST_ITEM.get(IdDrop).put(IdMob,new HashMap<String,String>());
						}
						DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("NAME", rss.getString(4));
						DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("TYPE", rss.getString(1));
						DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("LEVEL", String.valueOf(rss.getInt(5)));
						DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("CATEGORY", DROP[1]);
						DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("MIN", DROP[2]);
						DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("MAX", DROP[3]);
						DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("CHANCE", DROP[4]);
						DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("TELEPORT", rss.getString(7));
					}
				}
			}catch(SQLException a){
				_log.warning("ZEUS DROPLIST ERROR->" + a.getMessage());
				conerror=true;
			}
			try{
				conn.close();
				psqry.close();
				rss.close();
			}catch(Exception a){

			}

			if(!conerror){
				_log.warning("DropList Cargado con " + String.valueOf(DROP_LIST_ITEM.size()));
			}
		}
*/
		private static void cleanBD(){
			Vector<String> SQL_Clean = new Vector<String>();
			SQL_Clean.add("DELETE FROM zeus_buffer_scheme_list WHERE playerId NOT IN (SELECT characters.charId FROM characters)");
			SQL_Clean.add("DELETE FROM zeus_buffer_scheme_contents WHERE schemeId NOT IN (SELECT zeus_buffer_scheme_list.playerId FROM zeus_buffer_scheme_list)");
			SQL_Clean.add("DELETE FROM zeus_buff_char_sch WHERE idChar NOT IN (SELECT characters.charId FROM characters)");
			SQL_Clean.add("DELETE FROM zeus_buff_char_sch_buff WHERE zeus_buff_char_sch_buff.idSch NOT IN (SELECT zeus_buff_char_sch.id FROM zeus_buff_char_sch)");
			SQL_Clean.add("DELETE FROM zeus_char_config WHERE zeus_char_config.idchar NOT IN (SELECT characters.charId FROM characters)");

		}


		public static void RegistrarPlayerIPs(L2PcInstance player, String IPChar, int idSeccion){
			if(!CHAR_CONEX.containsKey(player)){
				CHAR_CONEX.put(player,new HashMap<Integer, String>());
			}
			CHAR_CONEX.get(player).put(idSeccion, IPChar);
		}

		public static String getIPPlayer(L2PcInstance player, int idSeccion){
			return CHAR_CONEX.get(player).get(idSeccion);
		}


		public static void setTime_antibot(L2PcInstance player){
			int nextCheck = opera.getUnixTimeNow() + (ANTIBOT_MINUTE_VERIF_AGAIN * 60);
			ANTIBOT_PLAYER_TIME.put(player.getObjectId(), nextCheck);
		}

		public static int getTime_antibot(L2PcInstance player){
			if(ANTIBOT_PLAYER_TIME.containsKey(player.getObjectId())){
				return ANTIBOT_PLAYER_TIME.get(player.getObjectId());
			}
			return 0;
		}

		public static HashMap<Integer, Integer> getANTIBOT_PLAYER(){
			return ANTIBOT_PLAYER_MOB_KILL;
		}

		public static int getMobKillAntibot(L2PcInstance player){
			return ANTIBOT_PLAYER_MOB_KILL.get(player.getObjectId());
		}

		public static void resetKillAntibot(L2PcInstance player){
			try{
				ANTIBOT_PLAYER_MOB_KILL.put(player.getObjectId(), 0);
			}catch(Exception a){

			}
		}

		public static void addKillAntibot(L2PcInstance player){
			ANTIBOT_PLAYER_MOB_KILL.put(player.getObjectId(), ANTIBOT_PLAYER_MOB_KILL.get(player.getObjectId()) + 1);
			captchaPLayer.setLastKillTime(player);
		}

		public static void addPlayerAntibot(L2PcInstance player){
			if(!general.ANTIBOT_RESET_COUNT){
				if(ANTIBOT_PLAYER_MOB_KILL.containsKey(player.getObjectId())){
					return;
				}
			}
			ANTIBOT_PLAYER_MOB_KILL.put(player.getObjectId(), 0);
		}


		public static Vector<Integer> getVectorBotPlayer(){
			return BOT_PLAYER;
		}

		public static void addBotPlayer(L2PcInstance player){
			BOT_PLAYER.add(player.getObjectId());
		}
		public static void removeBotPlayer(L2PcInstance player){
			if(BOT_PLAYER.contains(player.getObjectId())){
				BOT_PLAYER.removeElement(player.getObjectId());
			}
		}
		public static boolean isBotCheckPlayer(L2PcInstance player){
			return BOT_PLAYER.contains(player.getObjectId());
		}


		/*ZeuS SERVER*/


	public static int[] get_AccessConfig(){
		return AccessConfig;
	}



	protected static final String TITULO_NPC = "ZeuS Engine";


	public static String TITULO_NPC(){
		return TITULO_NPC + " - " + Server_Name;
	}

	public static String PIE_PAGINA_COMMUNIDAD(){
		return "<font color=333333>Zeus Community Engine<br1>By JabberWock 2013 - 2014</font>";
	}
	public static String PIE_PAGINA(){
		return "<font color=333333>Zeus Engine<br1>By JabberWock 2013 - 2015</font>";
	}

	public static String QUEST_INFO = "";

	public static String []BUFF_ENCHANT_PIDE;
	public static String PIE_DE_PAGINA = "";
	public static String NOMBRE_NPC = "ZeuS";
	public static int ID_NPC;
	public static int ID_NPC_CH;

	public static int MAX_LISTA_PVP;
	public static int MAX_LISTA_PVP_LOG;

	public static int IntPintarGrilla;
	//public static String npcGlobal="";

	public static String npcGlobal(L2PcInstance player){

		boolean buscar = false;

		if(general.IS_USING_NPC.get(player.getObjectId()) && !general.IS_USING_CB.get(player.getObjectId())){
			buscar = false;
		}else if(!general.IS_USING_NPC.get(player.getObjectId()) && general.IS_USING_CB.get(player.getObjectId())){
			buscar = true;
		}

		return npcGlobal(player,buscar);
	}

	public static L2Npc getNpcGlobal(){
		for(L2Spawn SpawnLo : SpawnTable.getInstance().getSpawns(ID_NPC)){
			L2Npc npcLocal = SpawnLo.getLastSpawn();
			CB_IDObjectMainZeuSNPC = npcLocal.getObjectId();
			return npcLocal;
		}
		
		return null;
	}
	
	public static String npcGlobal(L2PcInstance player,boolean isComunidad){
		if(!_isValid){
			return "";
		}
		if(!isComunidad){
			if(player.getTarget() instanceof L2Npc){
				L2Npc npc = (L2Npc) player.getTarget();
				if(!ManagerAIONpc.isNpcFromZeus(opera.getIDNPCTarget( player ))){
					return "0";
				}
				String idObjeto = String.valueOf(npc.getObjectId());
				return idObjeto;
			}
		}else{
			if(CB_IDObjectMainZeuSNPC==0){
				for(L2Spawn SpawnLo : SpawnTable.getInstance().getSpawns(ID_NPC)){
					if(CB_IDObjectMainZeuSNPC==0){
						L2Npc npcLocal = SpawnLo.getLastSpawn();
						CB_IDObjectMainZeuSNPC = npcLocal.getObjectId();
						return String.valueOf(CB_IDObjectMainZeuSNPC);
					}
				}

			}else{
				return String.valueOf(CB_IDObjectMainZeuSNPC);
			}
		}
		return "";
	}


	public static void loadConfigs(){
		loadConfigs(true);
	}

	public static void loadConfigs(boolean showmessage){
		if(!onLine){
			loadSerial();
			checkValidate();
			if(httpResp.isConect){
				if(!_isValid){
					_log.warning(":::::: Wrog ZeuS Serial ::::::");
					return;
				}

				_log.warning(":::::: ZeuS Serial OK ::::::");
			}else{
				return;
			}
			BD.getInstance().checkBD();
		}

		getAllClases();

		if(general.ServerStartUnixTime <= 0){
			general.ServerStartUnixTime = opera.getUnixTimeNow();
		}

		Connection conn = null;
		String qry = "";
		PreparedStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			qry = "SELECT zeus_config_seccion.seccion, zeus_config_seccion.param FROM zeus_config_seccion";
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			while(rss.next()){
				switch(rss.getString(1)){
				case "ID_NPC":
					ID_NPC = Integer.valueOf(rss.getString(2));
					break;
				case "ID_NPC_CH":
					ID_NPC_CH = Integer.valueOf(rss.getString(2));
					break;
				case "MAX_LISTA_PVP":
					MAX_LISTA_PVP = Integer.valueOf(rss.getString(2));
					break;
				case "MAX_LISTA_PVP_LOG":
					MAX_LISTA_PVP_LOG = Integer.valueOf(rss.getString(2));
					break;
				case "DEBUG_CONSOLA_ENTRADAS":
					DEBUG_CONSOLA_ENTRADAS = Boolean.valueOf(rss.getString(2));
					break;
				case "DEBUG_CONSOLA_ENTRADAS_TO_USER":
					DEBUG_CONSOLA_ENTRADAS_TO_USER =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_VOTE":
					BTN_SHOW_VOTE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_BUFFER":
					BTN_SHOW_BUFFER =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_TELEPORT":
					BTN_SHOW_TELEPORT =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SHOP":
					BTN_SHOW_SHOP =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_WAREHOUSE":
					BTN_SHOW_WAREHOUSE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_AUGMENT":
					BTN_SHOW_AUGMENT =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SUBCLASES":
					BTN_SHOW_SUBCLASES =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CLASS_TRANSFER":
					BTN_SHOW_CLASS_TRANSFER =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CONFIG_PANEL":
					BTN_SHOW_CONFIG_PANEL =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DROP_SEARCH":
					BTN_SHOW_DROP_SEARCH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_PVPPK_LIST":
					BTN_SHOW_PVPPK_LIST =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_LOG_PELEAS":
					BTN_SHOW_LOG_PELEAS =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CASTLE_MANAGER":
					BTN_SHOW_CASTLE_MANAGER =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DESAFIO":
					BTN_SHOW_DESAFIO =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SYMBOL_MARKET":
					BTN_SHOW_SYMBOL_MARKET =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CLANALLY":
					BTN_SHOW_CLANALLY =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_PARTYFINDER":
					BTN_SHOW_PARTYFINDER =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_FLAGFINDER":
					BTN_SHOW_FLAGFINDER =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_COLORNAME":
					BTN_SHOW_COLORNAME =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DELEVEL":
					BTN_SHOW_DELEVEL =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_REMOVE_ATRIBUTE":
					BTN_SHOW_REMOVE_ATRIBUTE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_BUG_REPORT":
					BTN_SHOW_BUG_REPORT =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DONATION":
					BTN_SHOW_DONATION =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CAMBIO_NOMBRE_PJ":
					BTN_SHOW_CAMBIO_NOMBRE_PJ =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CAMBIO_NOMBRE_CLAN":
					BTN_SHOW_CAMBIO_NOMBRE_CLAN =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_VARIAS_OPCIONES":
					BTN_SHOW_VARIAS_OPCIONES =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_ELEMENT_ENHANCED":
					BTN_SHOW_ELEMENT_ENHANCED =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_ENCANTAMIENTO_ITEM":
					BTN_SHOW_ENCANTAMIENTO_ITEM =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_AUGMENT_SPECIAL":
					BTN_SHOW_AUGMENT_SPECIAL =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_GRAND_BOSS_STATUS":
					BTN_SHOW_GRAND_BOSS_STATUS =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_RAIDBOSS_INFO":
					BTN_SHOW_RAIDBOSS_INFO =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_TRANSFORMACION":
					BTN_SHOW_TRANSFORMATION = Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_VOTE_CH":
					BTN_SHOW_VOTE_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_BUFFER_CH":
					BTN_SHOW_BUFFER_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_TELEPORT_CH":
					BTN_SHOW_TELEPORT_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SHOP_CH":
					BTN_SHOW_SHOP_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_WAREHOUSE_CH":
					BTN_SHOW_WAREHOUSE_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_AUGMENT_CH":
					BTN_SHOW_AUGMENT_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SUBCLASES_CH":
					BTN_SHOW_SUBCLASES_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CLASS_TRANSFER_CH":
					BTN_SHOW_CLASS_TRANSFER_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CONFIG_PANEL_CH":
					BTN_SHOW_CONFIG_PANEL_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DROP_SEARCH_CH":
					BTN_SHOW_DROP_SEARCH_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_PVPPK_LIST_CH":
					BTN_SHOW_PVPPK_LIST_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_LOG_PELEAS_CH":
					BTN_SHOW_LOG_PELEAS_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CASTLE_MANAGER_CH":
					BTN_SHOW_CASTLE_MANAGER_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DESAFIO_CH":
					BTN_SHOW_DESAFIO_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SYMBOL_MARKET_CH":
					BTN_SHOW_SYMBOL_MARKET_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CLANALLY_CH":
					BTN_SHOW_CLANALLY_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_PARTYFINDER_CH":
					BTN_SHOW_PARTYFINDER_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_FLAGFINDER_CH":
					BTN_SHOW_FLAGFINDER_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_COLORNAME_CH":
					BTN_SHOW_COLORNAME_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DELEVEL_CH":
					BTN_SHOW_DELEVEL_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_REMOVE_ATRIBUTE_CH":
					BTN_SHOW_REMOVE_ATRIBUTE_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_BUG_REPORT_CH":
					BTN_SHOW_BUG_REPORT_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DONATION_CH":
					BTN_SHOW_DONATION_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CAMBIO_NOMBRE_PJ_CH":
					BTN_SHOW_CAMBIO_NOMBRE_PJ_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CAMBIO_NOMBRE_CLAN_CH":
					BTN_SHOW_CAMBIO_NOMBRE_CLAN_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_VARIAS_OPCIONES_CH":
					BTN_SHOW_VARIAS_OPCIONES_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_ELEMENT_ENHANCED_CH":
					BTN_SHOW_ELEMENT_ENHANCED_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_ENCANTAMIENTO_ITEM_CH":
					BTN_SHOW_ENCANTAMIENTO_ITEM_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_AUGMENT_SPECIAL_CH":
					BTN_SHOW_AUGMENT_SPECIAL_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_GRAND_BOSS_STATUS_CH":
					BTN_SHOW_GRAND_BOSS_STATUS_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_RAIDBOSS_INFO_CH":
					BTN_SHOW_RAIDBOSS_INFO_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_TRANSFORMACION_CH":
					BTN_SHOW_TRANSFORMATION_CH = Boolean.valueOf(rss.getString(2));
					break;
				case "VOTO_REWARD_TOPZONE":
					VOTO_REWARD_TOPZONE = rss.getString(2);
					break;
				case "VOTO_REWARD_HOPZONE":
					VOTO_REWARD_HOPZONE = rss.getString(2);
					break;
				case "VOTO_REWARD_ACTIVO_TOPZONE":
					VOTO_REWARD_ACTIVO_TOPZONE =Boolean.valueOf(rss.getString(2));
					break;
				case "VOTO_REWARD_ACTIVO_HOPZONE":
					VOTO_REWARD_ACTIVO_HOPZONE =Boolean.valueOf(rss.getString(2));
					break;
				case "VOTO_REWARD_SEG_ESPERA":
					VOTO_REWARD_SEG_ESPERA =Integer.valueOf(rss.getString(2));
					break;
				case "VOTO_ITEM_BUFF_ENCHANT_PRICE":
					VOTO_ITEM_BUFF_ENCHANT_PRICE =rss.getString(2);
					break;/*
				case "DONA_ID_ITEM":
					DONA_ID_ITEM =Integer.valueOf(rss.getString(2));
					break;
				case "DONA_COST_85":
					DONA_COST_85 =Integer.valueOf(rss.getString(2));
					break;
				case "DONA_COST_NOBLE":
					DONA_COST_NOBLE =Integer.valueOf(rss.getString(2));
					break;
				case "DONA_COST_BUFFER":
					DONA_COST_BUFFER =Integer.valueOf(rss.getString(2));
					break;
				case "DONA_COST_CAMBIO_NOM_PJ":
					DONA_COST_CAMBIO_NOM_PJ =Integer.valueOf(rss.getString(2));
					break;
				case "DONA_COST_CAMBIO_NOM_CLAN":
					DONA_COST_CAMBIO_NOM_CLAN =Integer.valueOf(rss.getString(2));
					break;*/
				case "TELEPORT_PRICE":
					TELEPORT_PRICE =rss.getString(2);
					break;
				case "FREE_TELEPORT":
					FREE_TELEPORT =Boolean.valueOf(rss.getString(2));
					break;
				case "DESAFIO_85_PREMIO":
					DESAFIO_85_PREMIO =rss.getString(2);
					break;
				case "DESAFIO_NOBLE_PREMIO":
					DESAFIO_NOBLE_PREMIO =rss.getString(2);
					break;
				case "DESAFIO_NPC_BUSQUEDAS":
					String[] npcBusq = rss.getString(2).split(",");
					DESAFIO_NPC_BUSQUEDAS.clear();
					for (String NpcID : npcBusq)
					{
						if(opera.isNumeric(NpcID)) {
							DESAFIO_NPC_BUSQUEDAS.add(Integer.parseInt(NpcID));
						}
					}
					Collections.sort(DESAFIO_NPC_BUSQUEDAS);
					break;

				case "DESAFIO_MAX_LVL85":
					DESAFIO_MAX_LVL85 = Integer.valueOf(rss.getString(2));
					break;

				case "DESAFIO_MAX_NOBLE":
					DESAFIO_MAX_NOBLE = Integer.valueOf(rss.getString(2));
					break;

				case "DESAFIO_LVL85":
					DESAFIO_LVL85 = Boolean.valueOf(rss.getString(2));
					break;

				case "DESAFIO_NOBLE":
					DESAFIO_NOBLE = Boolean.valueOf(rss.getString(2));
					break;

				case "DESAFIO_NPC":
					DESAFIO_NPC = Boolean.valueOf(rss.getString(2));

				case "DROP_SEARCH_COBRAR_TELEPORT":
					DROP_SEARCH_TELEPORT_FOR_FREE =Boolean.valueOf(rss.getString(2));
					break;
				case "DROP_TELEPORT_COST":
					DROP_TELEPORT_COST =rss.getString(2);
					break;
				case "DROP_SEARCH_MOSTRAR_LISTA":
					DROP_SEARCH_MOSTRAR_LISTA = Integer.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_PRICE":
					PARTY_FINDER_PRICE =rss.getString(2);
					break;
				case "PARTY_FINDER_GO_LEADER_DEATH":
					PARTY_FINDER_GO_LEADER_DEATH =Boolean.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_GO_LEADER_NOBLE":
					PARTY_FINDER_GO_LEADER_NOBLE =Boolean.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_GO_LEADER_FLAGPK":
					PARTY_FINDER_GO_LEADER_FLAGPK =Boolean.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_CAN_USE_PK":
					PARTY_FINDER_CAN_USE_PK =Boolean.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_CAN_USE_FLAG":
					PARTY_FINDER_CAN_USE_FLAG =Boolean.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_CAN_USE_LVL":
					PARTY_FINDER_CAN_USE_LVL = Integer.valueOf(rss.getString(2));
					break;
				case "FLAG_FINDER_PRICE":
					FLAG_FINDER_PRICE =rss.getString(2);
					break;
				case "FLAG_FINDER_CAN_USE_FLAG":
					FLAG_FINDER_CAN_USE_FLAG =Boolean.valueOf(rss.getString(2));
					break;
				case "FLAG_FINDER_CAN_USE_PK":
					FLAG_FINDER_CAN_USE_PK =Boolean.valueOf(rss.getString(2));
					break;
				case "FLAG_FINDER_CAN_NOBLE":
					FLAG_FINDER_CAN_NOBLE =Boolean.valueOf(rss.getString(2));
					break;
				case "FLAG_FINDER_LVL":
					FLAG_FINDER_LVL =Integer.valueOf(rss.getString(2));
					break;
				case "FLAG_PVP_PK_LVL_MIN":
					FLAG_PVP_PK_LVL_MIN =Integer.valueOf(rss.getString(2));
					break;
				case "FLAG_FINDER_CAN_GO_CASTLE":
					FLAG_FINDER_CAN_GO_IS_INSIDE_CASTLE = Boolean.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_GO_LEADER_ON_ASEDIO":
					PARTY_FINDER_GO_LEADER_ON_ASEDIO = Boolean.valueOf(rss.getString(2));
					break;
				case "PINTAR_PRICE":
					PINTAR_PRICE =rss.getString(2);
					break;
				case "PINTAR_COLORS":
					PINTAR_MATRIZ = rss.getString(2);
					break;
				case "AUGMENT_ITEM_PRICE":
					AUGMENT_ITEM_PRICE =rss.getString(2);
					break;
				case "AUGMENT_SPECIAL_PRICE":
					AUGMENT_SPECIAL_PRICE =rss.getString(2);
					break;
				case "AUGMENT_SPECIAL_x_PAGINA":
					AUGMENT_SPECIAL_x_PAGINA =Integer.valueOf(rss.getString(2));
					break;
				case "AUGMENT_SPECIAL_NOBLE":
					AUGMENT_SPECIAL_NOBLE =Boolean.valueOf(rss.getString(2));
					break;
				case "AUGMENT_SPECIAL_LVL":
					AUGMENT_SPECIAL_LVL =Integer.valueOf(rss.getString(2));
					break;
				case "ENCHANT_ITEM_PRICE":
					ENCHANT_ITEM_PRICE =rss.getString(2);
					break;
				case "ENCHANT_NOBLE":
					ENCHANT_NOBLE =Boolean.valueOf(rss.getString(2));
					break;
				case "ENCHANT_LVL":
					ENCHANT_LVL =Integer.valueOf(rss.getString(2));
					break;
				case "ENCHANT_MIN_ENCHANT":
					ENCHANT_MIN_ENCHANT =Integer.valueOf(rss.getString(2));
					break;
				case "ENCHANT_MAX_ENCHANT":
					ENCHANT_MAX_ENCHANT =Integer.valueOf(rss.getString(2));
					break;
				case "ENCHANT_x_VEZ":
					ENCHANT_x_VEZ =Integer.valueOf(rss.getString(2));
					break;
				case "RAIDBOSS_INFO_TELEPORT_PRICE":
					RAIDBOSS_INFO_TELEPORT_PRICE =rss.getString(2);
					break;
				case "RAIDBOSS_INFO_LISTA_X_HOJA":
					RAIDBOSS_INFO_LISTA_X_HOJA =Integer.valueOf(rss.getString(2));
					break;
				case "RAIDBOSS_INFO_TELEPORT":
					RAIDBOSS_INFO_TELEPORT = Boolean.valueOf(rss.getString(2));
					break;
				case "RAIDBOSS_INFO_NOBLE":
					RAIDBOSS_INFO_NOBLE =Boolean.valueOf(rss.getString(2));
					break;
				case "RAIDBOSS_INFO_LVL":
					RAIDBOSS_INFO_LVL =Integer.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_SEXO_ITEM_PRICE":
					OPCIONES_CHAR_SEXO_ITEM_PRICE = rss.getString(2);
					break;
				case "OPCIONES_CHAR_NOBLE_ITEM_PRICE":
					OPCIONES_CHAR_NOBLE_ITEM_PRICE = rss.getString(2);
					break;
				case "OPCIONES_CHAR_LVL85_ITEM_PRICE":
					OPCIONES_CHAR_LVL85_ITEM_PRICE = rss.getString(2);
					break;
				case "OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE":
					OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE = rss.getString(2);
					break;
				case "OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE":
					OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE = rss.getString(2);
					break;
				case "OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE":
					OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE = Boolean.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_CAMBIO_NOMBRE_LVL":
					OPCIONES_CHAR_CAMBIO_NOMBRE_LVL =Integer.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL":
					OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL = Integer.valueOf(rss.getString(2));
					break;

				case "OPCIONES_CHAR_SEXO":
					OPCIONES_CHAR_SEXO = Boolean.parseBoolean(rss.getString(2));
					break;
				case "OPCIONES_CHAR_NOBLE":
					OPCIONES_CHAR_NOBLE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "OPCIONES_CHAR_LVL85":
					OPCIONES_CHAR_LVL85 = Boolean.parseBoolean(rss.getString(2));
					break;
				case "OPCIONES_CHAR_BUFFER_AIO":
					OPCIONES_CHAR_BUFFER_AIO = Boolean.parseBoolean(rss.getString(2));
					break;
				case "OPCIONES_CHAR_BUFFER_AIO_PRICE":
					OPCIONES_CHAR_BUFFER_AIO_PRICE = rss.getString(2);
					break;
				case "OPCIONES_CHAR_BUFFER_AIO_LVL":
					OPCIONES_CHAR_BUFFER_AIO_LVL = Integer.valueOf(rss.getString(2));
					break;

				case "ELEMENTAL_ITEM_PRICE":
					ELEMENTAL_ITEM_PRICE = rss.getString(2);
					break;
				case "ELEMENTAL_NOBLE":
					ELEMENTAL_NOBLE =Boolean.valueOf(rss.getString(2));
					break;
				case "ELEMENTAL_LVL":
					ELEMENTAL_LVL =Integer.valueOf(rss.getString(2));
					break;
				case "DELEVEL_PRICE":
					DELEVEL_PRICE = rss.getString(2);
					break;
				case "DELEVEL_LVL_MAX":
					DELEVEL_LVL_MAX =Integer.valueOf(rss.getString(2));
					break;
				case "DELEVEL_NOBLE":
					DELEVEL_NOBLE =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_ID_ITEM":
					BUFFER_ID_ITEM = Integer.valueOf(rss.getString(2));
					break;
				case "BUFFER_CON_KARMA":
					BUFFER_CON_KARMA =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_LVL_MIN":
					BUFFER_LVL_MIN =Integer.valueOf(rss.getString(2));
					break;
				case "BUFFER_TIME_WAIT":
					BUFFER_TIME_WAIT = Integer.valueOf(rss.getString(2));
					break;
				case "BUFF_GRATIS":
					BUFF_GRATIS =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_GM_ONLY":
					BUFFER_GM_ONLY =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_ID_ACCESO_GM":
					BUFFER_ID_ACCESO_GM = Integer.valueOf(rss.getString(2));
					break;
				case "BUFFER_ID_ACCESO_ADMIN":
					BUFFER_ID_ACCESO_ADMIN =Integer.valueOf(rss.getString(2));
					break;
				case "BUFFER_SINGLE_BUFF_CHOICE":
					BUFFER_SINGLE_BUFF_CHOICE =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_SCHEME_SYSTEM":
					BUFFER_SCHEME_SYSTEM =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_SCHEMA_X_PLAYER":
					BUFFER_SCHEMA_X_PLAYER = Integer.valueOf(rss.getString(2));
					break;
				case "BUFFER_IMPROVED_SECTION":
					BUFFER_IMPROVED_SECTION =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_IMPROVED_VALOR":
					BUFFER_IMPROVED_VALOR =Integer.valueOf(rss.getString(2));
					break;
				case "BUFFER_BUFF_SECTION":
					BUFFER_BUFF_SECTION =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_BUFF_VALOR":
					BUFFER_BUFF_VALOR =Integer.valueOf(rss.getString(2));
					break;
				case "BUFFER_CHANT_SECTION":
					BUFFER_CHANT_SECTION =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_CHANT_VALOR":
					BUFFER_CHANT_VALOR =Integer.valueOf(rss.getString(2));
					break;
				case "BUFFER_DANCE_SECTION":
					BUFFER_DANCE_SECTION =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_DANCE_VALOR":
					BUFFER_DANCE_VALOR =Integer.valueOf(rss.getString(2));
					break;
				case "BUFFER_SONG_SECTION":
					BUFFER_SONG_SECTION =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_SONG_VALOR":
					BUFFER_SONG_VALOR =Integer.valueOf(rss.getString(2));
					break;
				case "BUFFER_RESIST_SECTION":
					BUFFER_RESIST_SECTION =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_RESIST_VALOR":
					BUFFER_RESIST_VALOR =Integer.valueOf(rss.getString(2));
					break;
				case "BUFFER_CUBIC_SECTION":
					BUFFER_CUBIC_SECTION =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_CUBIC_VALOR":
					BUFFER_CUBIC_VALOR =Integer.valueOf(rss.getString(2));
					break;
				case "BUFFER_PROPHECY_SECTION":
					BUFFER_PROPHECY_SECTION =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_PROHECY_VALOR":
					BUFFER_PROHECY_VALOR =Integer.valueOf(rss.getString(2));
					break;
				case "BUFFER_SPECIAL_SECTION":
					BUFFER_SPECIAL_SECTION =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_SPECIAL_VALOR":
					BUFFER_SPECIAL_VALOR =Integer.valueOf(rss.getString(2));
					break;
				case "BUFFER_OTROS_SECTION":
					BUFFER_OTROS_SECTION =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_OTROS_VALOR":
					BUFFER_OTROS_VALOR =Integer.valueOf(rss.getString(2));
					break;
				case "BUFFER_AUTOBUFF":
					BUFFER_AUTOBUFF =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_HEAL":
					BUFFER_HEAL = Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_HEAL_VALOR":
					BUFFER_HEAL_VALOR =Integer.valueOf(rss.getString(2));
					break;
				case "BUFFER_REMOVE_BUFF":
					BUFFER_REMOVE_BUFF =Boolean.valueOf(rss.getString(2));
					break;
				case "BUFFER_REMOVE_BUFF_VALOR":
					BUFFER_REMOVE_BUFF_VALOR =Integer.valueOf(rss.getString(2));
					break;
				case "RATE_EXP_OFF":
					RATE_EXP_OFF =Boolean.valueOf(rss.getString(2));
					break;
				case "SHOW_MY_STAT":
					SHOW_MY_STAT = Boolean.valueOf(rss.getString(2));
					break;
				case "LOG_FIGHT_PVP_PK":
					LOG_FIGHT_PVP_PK =Boolean.valueOf(rss.getString(2));
					break;
				case "ENCHANT_ANNOUCEMENT":
					final String[] EnchantForAnnoucement = rss.getString(2).split(",");
					ENCHANT_ANNOUCEMENT = null;
					ENCHANT_ANNOUCEMENT = new int[EnchantForAnnoucement.length];
					for (int i = 0; i < EnchantForAnnoucement.length; i++)
					{
						if(opera.isNumeric(EnchantForAnnoucement[i])){
							ENCHANT_ANNOUCEMENT[i] = Integer.parseInt(EnchantForAnnoucement[i]);
						}else{
							_log.warning("ZEUS-> Error en Cargado de Anuncios de ENchant, el Valor " + EnchantForAnnoucement[i] + " No es númerico. Omitido");
						}
					}
					Arrays.sort(ENCHANT_ANNOUCEMENT);
					break;
				case "PVP_PK_PROTECTION_LVL":
					PVP_PK_PROTECTION_LVL =Integer.valueOf(rss.getString(2));
					break;
				case "ALLOW_BLESSED_ESCAPE_PVP":
					ALLOW_BLESSED_ESCAPE_PVP = Boolean.valueOf(rss.getString(2));
					break;
				case "PVP_PK_GRAFICAL_EFFECT":
					PVP_PK_GRAFICAL_EFFECT =Boolean.valueOf(rss.getString(2));
					break;
				case "PVP_COLOR_SYSTEM_ENABLED":
					PVP_COLOR_SYSTEM_ENABLED =Boolean.valueOf(rss.getString(2));
					break;
				case "PK_COLOR_SYSTEM_ENABLED":
					PK_COLOR_SYSTEM_ENABLED =Boolean.valueOf(rss.getString(2));
					break;
				case "PVP_AMOUNT_1":
					PVP_AMOUNT_1 =Integer.valueOf(rss.getString(2));
					break;
				case "PVP_AMOUNT_2":
					PVP_AMOUNT_2 =Integer.valueOf(rss.getString(2));
					break;
				case "PVP_AMOUNT_3":
					PVP_AMOUNT_3 =Integer.valueOf(rss.getString(2));
					break;
				case "PVP_AMOUNT_4":
					PVP_AMOUNT_4 =Integer.valueOf(rss.getString(2));
					break;
				case "PVP_AMOUNT_5":
					PVP_AMOUNT_5 =Integer.valueOf(rss.getString(2));
					break;
				case "PVP_AMOUNT_6":
					PVP_AMOUNT_6 =Integer.valueOf(rss.getString(2));
					break;
				case "PVP_AMOUNT_7":
					PVP_AMOUNT_7 =Integer.valueOf(rss.getString(2));
					break;
				case "PVP_AMOUNT_8":
					PVP_AMOUNT_8 =Integer.valueOf(rss.getString(2));
					break;
				case "PVP_AMOUNT_9":
					PVP_AMOUNT_9 =Integer.valueOf(rss.getString(2));
					break;
				case "PVP_AMOUNT_10":
					PVP_AMOUNT_10 =Integer.valueOf(rss.getString(2));
					break;
				case "NAME_COLOR_FOR_PVP_AMOUNT_1":
					NAME_COLOR_FOR_PVP_AMOUNT_1 = Integer.decode("0x" + rss.getString(2));
					NAME_COLOR_FOR_ALL[0] = rss.getString(2);
					break;
				case "NAME_COLOR_FOR_PVP_AMOUNT_2":
					NAME_COLOR_FOR_PVP_AMOUNT_2 =Integer.decode("0x" + rss.getString(2));
					NAME_COLOR_FOR_ALL[1] = rss.getString(2);
					break;
				case "NAME_COLOR_FOR_PVP_AMOUNT_3":
					NAME_COLOR_FOR_PVP_AMOUNT_3 = Integer.decode("0x" + rss.getString(2));
					NAME_COLOR_FOR_ALL[2] = rss.getString(2);
					break;
				case "NAME_COLOR_FOR_PVP_AMOUNT_4":
					NAME_COLOR_FOR_PVP_AMOUNT_4 = Integer.decode("0x" + rss.getString(2));
					NAME_COLOR_FOR_ALL[3] = rss.getString(2);
					break;
				case "NAME_COLOR_FOR_PVP_AMOUNT_5":
					NAME_COLOR_FOR_PVP_AMOUNT_5 = Integer.decode("0x" + rss.getString(2));
					NAME_COLOR_FOR_ALL[4] = rss.getString(2);
					break;
				case "NAME_COLOR_FOR_PVP_AMOUNT_6":
					NAME_COLOR_FOR_PVP_AMOUNT_6 = Integer.decode("0x" + rss.getString(2));
					NAME_COLOR_FOR_ALL[5] = rss.getString(2);
					break;
				case "NAME_COLOR_FOR_PVP_AMOUNT_7":
					NAME_COLOR_FOR_PVP_AMOUNT_7 = Integer.decode("0x" + rss.getString(2));
					NAME_COLOR_FOR_ALL[6] = rss.getString(2);
					break;
				case "NAME_COLOR_FOR_PVP_AMOUNT_8":
					NAME_COLOR_FOR_PVP_AMOUNT_8 = Integer.decode("0x" + rss.getString(2));
					NAME_COLOR_FOR_ALL[7] = rss.getString(2);
					break;
				case "NAME_COLOR_FOR_PVP_AMOUNT_9":
					NAME_COLOR_FOR_PVP_AMOUNT_9 = Integer.decode("0x" + rss.getString(2));
					NAME_COLOR_FOR_ALL[8] = rss.getString(2);
					break;
				case "NAME_COLOR_FOR_PVP_AMOUNT_10":
					NAME_COLOR_FOR_PVP_AMOUNT_10 = Integer.decode("0x" + rss.getString(2));
					NAME_COLOR_FOR_ALL[9] = rss.getString(2);
					break;
				case "PK_AMOUNT_1":
					PK_AMOUNT_1 =Integer.valueOf(rss.getString(2));
					break;
				case "PK_AMOUNT_2":
					PK_AMOUNT_2 =Integer.valueOf(rss.getString(2));
					break;
				case "PK_AMOUNT_3":
					PK_AMOUNT_3 =Integer.valueOf(rss.getString(2));
					break;
				case "PK_AMOUNT_4":
					PK_AMOUNT_4 =Integer.valueOf(rss.getString(2));
					break;
				case "PK_AMOUNT_5":
					PK_AMOUNT_5 =Integer.valueOf(rss.getString(2));
					break;
				case "PK_AMOUNT_6":
					PK_AMOUNT_6 =Integer.valueOf(rss.getString(2));
					break;
				case "PK_AMOUNT_7":
					PK_AMOUNT_7 =Integer.valueOf(rss.getString(2));
					break;
				case "PK_AMOUNT_8":
					PK_AMOUNT_8 =Integer.valueOf(rss.getString(2));
					break;
				case "PK_AMOUNT_9":
					PK_AMOUNT_9 =Integer.valueOf(rss.getString(2));
					break;
				case "PK_AMOUNT_10":
					PK_AMOUNT_10 =Integer.valueOf(rss.getString(2));
					break;
				case "TITLE_COLOR_FOR_PK_AMOUNT_1":
					TITLE_COLOR_FOR_PK_AMOUNT_1 = Integer.decode("0x" + rss.getString(2));
					TITLE_COLOR_FOR_ALL[0] = rss.getString(2);
					break;
				case "TITLE_COLOR_FOR_PK_AMOUNT_2":
					TITLE_COLOR_FOR_PK_AMOUNT_2 = Integer.decode("0x" + rss.getString(2));
					TITLE_COLOR_FOR_ALL[1] = rss.getString(2);
					break;
				case "TITLE_COLOR_FOR_PK_AMOUNT_3":
					TITLE_COLOR_FOR_PK_AMOUNT_3 = Integer.decode("0x" + rss.getString(2));
					TITLE_COLOR_FOR_ALL[2] = rss.getString(2);
					break;
				case "TITLE_COLOR_FOR_PK_AMOUNT_4":
					TITLE_COLOR_FOR_PK_AMOUNT_4 = Integer.decode("0x" + rss.getString(2));
					TITLE_COLOR_FOR_ALL[3] = rss.getString(2);
					break;
				case "TITLE_COLOR_FOR_PK_AMOUNT_5":
					TITLE_COLOR_FOR_PK_AMOUNT_5 = Integer.decode("0x" + rss.getString(2));
					TITLE_COLOR_FOR_ALL[4] = rss.getString(2);
					break;
				case "TITLE_COLOR_FOR_PK_AMOUNT_6":
					TITLE_COLOR_FOR_PK_AMOUNT_6 = Integer.decode("0x" + rss.getString(2));
					TITLE_COLOR_FOR_ALL[5] = rss.getString(2);
					break;
				case "TITLE_COLOR_FOR_PK_AMOUNT_7":
					TITLE_COLOR_FOR_PK_AMOUNT_7 = Integer.decode("0x" + rss.getString(2));
					TITLE_COLOR_FOR_ALL[6] = rss.getString(2);
					break;
				case "TITLE_COLOR_FOR_PK_AMOUNT_8":
					TITLE_COLOR_FOR_PK_AMOUNT_8 =Integer.decode("0x" + rss.getString(2));
					TITLE_COLOR_FOR_ALL[7] = rss.getString(2);
					break;
				case "TITLE_COLOR_FOR_PK_AMOUNT_9":
					TITLE_COLOR_FOR_PK_AMOUNT_9 = Integer.decode("0x" + rss.getString(2));
					TITLE_COLOR_FOR_ALL[8] = rss.getString(2);
					break;
				case "TITLE_COLOR_FOR_PK_AMOUNT_10":
					TITLE_COLOR_FOR_PK_AMOUNT_10 = Integer.decode("0x" + rss.getString(2));
					TITLE_COLOR_FOR_ALL[9] = rss.getString(2);
					break;
				case "MENSAJE_PVP_PK_CICLOS":
					MENSAJE_PVP_PK_CICLOS =Boolean.valueOf(rss.getString(2));
					break;
				case "CANTIDAD_CICLO_MENSAJE_PVP_1":
					CANTIDAD_CICLO_MENSAJE_PVP_1 =Integer.valueOf(rss.getString(2));
					break;
				case "CANTIDAD_CICLO_MENSAJE_PVP_2":
					CANTIDAD_CICLO_MENSAJE_PVP_2 =Integer.valueOf(rss.getString(2));
					break;
				case "CANTIDAD_CICLO_MENSAJE_PVP_3":
					CANTIDAD_CICLO_MENSAJE_PVP_3 =Integer.valueOf(rss.getString(2));
					break;
				case "CANTIDAD_CICLO_MENSAJE_PVP_4":
					CANTIDAD_CICLO_MENSAJE_PVP_4 =Integer.valueOf(rss.getString(2));
					break;
				case "CANTIDAD_CICLO_MENSAJE_PVP_5":
					CANTIDAD_CICLO_MENSAJE_PVP_5 =Integer.valueOf(rss.getString(2));
					break;
				case "CANTIDAD_CICLO_MENSAJE_PK_1":
					CANTIDAD_CICLO_MENSAJE_PK_1 =Integer.valueOf(rss.getString(2));
					break;
				case "CANTIDAD_CICLO_MENSAJE_PK_2":
					CANTIDAD_CICLO_MENSAJE_PK_2 =Integer.valueOf(rss.getString(2));
					break;
				case "CANTIDAD_CICLO_MENSAJE_PK_3":
					CANTIDAD_CICLO_MENSAJE_PK_3 =Integer.valueOf(rss.getString(2));
					break;
				case "CANTIDAD_CICLO_MENSAJE_PK_4":
					CANTIDAD_CICLO_MENSAJE_PK_4 =Integer.valueOf(rss.getString(2));
					break;
				case "CANTIDAD_CICLO_MENSAJE_PK_5":
					CANTIDAD_CICLO_MENSAJE_PK_5 =Integer.valueOf(rss.getString(2));
					break;
				case "CICLO_MENSAJE_PVP_1":
					CICLO_MENSAJE_PVP_1 =rss.getString(2);
					break;
				case "CICLO_MENSAJE_PVP_2":
					CICLO_MENSAJE_PVP_2 =rss.getString(2);
					break;
				case "CICLO_MENSAJE_PVP_3":
					CICLO_MENSAJE_PVP_3 =rss.getString(2);
					break;
				case "CICLO_MENSAJE_PVP_4":
					CICLO_MENSAJE_PVP_4 =rss.getString(2);
					break;
				case "CICLO_MENSAJE_PVP_5":
					CICLO_MENSAJE_PVP_5 =rss.getString(2);
					break;
				case "CICLO_MENSAJE_PK_1":
					CICLO_MENSAJE_PK_1 =rss.getString(2);
					break;
				case "CICLO_MENSAJE_PK_2":
					CICLO_MENSAJE_PK_2 =rss.getString(2);
					break;
				case "CICLO_MENSAJE_PK_3":
					CICLO_MENSAJE_PK_3 =rss.getString(2);
					break;
				case "CICLO_MENSAJE_PK_4":
					CICLO_MENSAJE_PK_4 =rss.getString(2);
					break;
				case "CICLO_MENSAJE_PK_5":
					CICLO_MENSAJE_PK_5 =rss.getString(2);
					break;
				case "ANNOUCE_RAID_BOS_STATUS":
					ANNOUCE_RAID_BOS_STATUS =Boolean.valueOf(rss.getString(2));
					break;
				case "RAID_ANNOUCEMENT_DIED":
					RAID_ANNOUCEMENT_DIED =rss.getString(2);
					break;
				case "RAID_ANNOUCEMENT_LIFE":
					RAID_ANNOUCEMENT_LIFE =rss.getString(2);
					break;
				case "RAID_ANNOUCEMENT_ID_ANNOUCEMENT":
					RAID_ANNOUCEMENT_ID_ANNOUCEMENT = Integer.valueOf(rss.getString(2));
					break;
				case "ANNOUCE_TOP_PPVPPK_ENTER":
					ANNOUCE_TOP_PPVPPK_ENTER =Boolean.valueOf(rss.getString(2));
					break;
				case "MENSAJE_ENTRADA_PJ_TOPPVPPK":
					MENSAJE_ENTRADA_PJ_TOPPVPPK =rss.getString(2);
					break;
				case "ANNOUCE_PJ_KARMA":
					ANNOUCE_PJ_KARMA =Boolean.valueOf(rss.getString(2));
					break;
				case "MENSAJE_ENTRADA_PJ_KARMA":
					MENSAJE_ENTRADA_PJ_KARMA =rss.getString(2);
					break;
				case "ANNOUCE_PJ_KARMA_CANTIDAD":
					ANNOUCE_PJ_KARMA_CANTIDAD =Integer.valueOf(rss.getString(2));
					break;
				case "ANNOUCE_CLASS_OPONENT_OLY":
					ANNOUCE_CLASS_OPONENT_OLY =Boolean.valueOf(rss.getString(2));
					break;
				case "TRANSFORM_NOBLE":
					TRANSFORMATION_NOBLE = Boolean.valueOf(rss.getString(2));
					break;
				case "TRANSFORM_LVL":
					TRANSFORMATION_LVL = Integer.valueOf(rss.getString(2));
					break;
				case "TRANSFORM_PRICE":
					TRANSFORMATION_PRICE = rss.getString(2);
					break;
				case "TRANSFORM_ESPECIALES":
					TRANSFORMATION_ESPECIALES = Boolean.valueOf(rss.getString(2));
					break;
				case "TRANSFORM_RAIDBOSS":
					TRANSFORMATION_RAIDBOSS = Boolean.valueOf(rss.getString(2));
					break;
				case "TRANSFORM_ESPECIALES_PRICE":
					TRANSFORMATION_ESPECIALES_PRICE = rss.getString(2);
					break;
				case "TRANSFORM_RAIDBOSS_PRICE":
					TRANSFORMATION_RAIDBOSS_PRICE = rss.getString(2);
					break;
				case "OPCIONES_CHAR_FAME":
					OPCIONES_CHAR_FAME = Boolean.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_FAME_PRICE":
					OPCIONES_CHAR_FAME_PRICE = rss.getString(2);
					break;
				case "OPCIONES_CHAR_FAME_NOBLE":
					OPCIONES_CHAR_FAME_NOBLE = Boolean.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_FAME_LVL":
					OPCIONES_CHAR_FAME_LVL = Integer.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_FAME_GIVE":
					OPCIONES_CHAR_FAME_GIVE = Integer.valueOf(rss.getString(2));
					break;
				case "OLY_ANTIFEED_CHANGE_TEMPLATE":
					OLY_ANTIFEED_CHANGE_TEMPLATE = Boolean.valueOf(rss.getString(2));
					break;
				case "OLY_ANTIFEED_NO_SHOW_NAME_NPC":
					OLY_ANTIFEED_SHOW_NAME_NPC = Boolean.valueOf(rss.getString(2));
					break;
				case "OLY_SECOND_SHOW_OPONENTES":
					final String[] SegundosOpoentes = rss.getString(2).split(",");
					OLY_SECOND_SHOW_OPPONET = null;
					OLY_SECOND_SHOW_OPPONET = new int[SegundosOpoentes.length];
					for (int i = 0; i < SegundosOpoentes.length; i++)
					{
						if(opera.isNumeric(SegundosOpoentes[i])){
							OLY_SECOND_SHOW_OPPONET[i] = Integer.parseInt(SegundosOpoentes[i]);
						}else{
							_log.warning("ZEUS-> Error en Cargado de Segundos de anucios Oponenetes, el Valor " + SegundosOpoentes[i] + " No es númerico. Omitido");
						}
					}
					Arrays.sort(OLY_SECOND_SHOW_OPPONET);
					break;
				case "OLY_SHOW_NAME_OPONENTES":
					OLY_ANTIFEED_SHOW_NAME_OPPO = Boolean.valueOf(rss.getString(2));
					break;
				case "OLY_ID_ACCESS_POINT_MODIF":
					final String[] ID_ACCESS = rss.getString(2).split(",");
					OLY_ACCESS_ID_MODIFICAR_POINT = null;
					OLY_ACCESS_ID_MODIFICAR_POINT = new int[ID_ACCESS.length];
					for (int i = 0; i < ID_ACCESS.length; i++)
					{
						if(opera.isNumeric(ID_ACCESS[i])){
							OLY_ACCESS_ID_MODIFICAR_POINT[i] = Integer.parseInt(ID_ACCESS[i]);
						}else{
							_log.warning("ZEUS-> Error en Cargado de Segundos de anucios Oponenetes, el Valor " + ID_ACCESS[i] + " No es númerico. Omitido");
						}
					}
					Arrays.sort(OLY_ACCESS_ID_MODIFICAR_POINT);
					break;

				case "ACCESS_ID":
					String [] AccSplit =  rss.getString(2).split(",");
					AccessConfig = new int[AccSplit.length];
					int Cont = 0;
					for (String Accs : AccSplit){
						AccessConfig[Cont] = Integer.valueOf(Accs);
						Cont++;
					}
					break;
				case "TELEPORT_BD":
					TELEPORT_BD = Boolean.valueOf(rss.getString(2));
					break;
				case "SERVER_NAME":
					Server_Name = rss.getString(2);
					break;
				case "SHOP_USE_BD":
					SHOP_USE_BD = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_COMANDO_STATUS":
					ANTIBOT_COMANDO_STATUS = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_OPORTUNIDADES":
					ANTIBOT_OPORTUNIDADES = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_MINUTOS_JAIL":
					ANTIBOT_MINUTOS_JAIL = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_MOB_DEAD_TO_ACTIVATE":
					ANTIBOT_MOB_DEAD_TO_ACTIVATE = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_MINUTE_VERIF_AGAIN":
					ANTIBOT_MINUTE_VERIF_AGAIN = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_MINUTOS_ESPERA":
					ANTIBOT_MINUTOS_ESPERA = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_MIN_LVL":
					ANTIBOT_MIN_LVL = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_ONLY_NOBLE":
					ANTIBOT_NOBLE_ONLY = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_ONLY_HERO":
					ANTIBOT_HERO_ONLY = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_ONLY_GM":
					ANTIBOT_GM_ONLY = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_ANTIGUEDAD_MINUTOS_MIN":
					ANTIBOT_ANTIGUEDAD_MINUTOS = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_ANNOU_JAIL":
					ANTIBOT_ANNOU_JAIL = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_AUTO":
					ANTIBOT_AUTO = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_RESET_COUNT":
					ANTIBOT_RESET_COUNT = Boolean.valueOf(rss.getString(2));
					break;
				case "BANIP_CHECK_IP_INTERNET":
					BANIP_CHECK_IP_INTERNET = Boolean.valueOf(rss.getString(2));
					break;
				case "BANIP_CHECK_IP_RED":
					BANIP_CHECK_IP_RED = Boolean.valueOf(rss.getString(2));
					break;
				case "BANIP_STATUS":
					BANIP_STATUS = Boolean.valueOf(rss.getString(2));
					break;
				case "BANIP_DISCONNECT_ALL_PLAYER":
					BANIP_DISCONNECT_ALL_PLAYER = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_COLISEUM_NPC_ID":
					EVENT_COLISEUM_NPC_ID = Integer.valueOf(rss.getString(2));
					break;
				case "VOTE_SHOW_ONLY_ZEUS_ITEM":
					VOTE_SHOW_ZEUS_ONLY_BY_ITEM = Boolean.valueOf(rss.getString(2));
					break;
				case "VOTE_SHOW_ONLY_ZEUS_ITEM_ID":
					VOTE_SHOW_ZEUS_ONLY_BY_ITEM_ID = Integer.valueOf(rss.getString(2));
					break;
				case "VOTE_SHOW_ONLY_ZEUS_ITEM_GIVE_TEMPORAL":
					VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM = Boolean.valueOf(rss.getString(2));
					break;
				case "VOTE_SHOW_ONLY_ZEUS_ITEM_ID_TEMPORTAL":
					VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM_ID = Integer.valueOf(rss.getString(2));
					break;
				case "VOTE_SHOW_ONLY_ZEUS_ITEM_TEMPORAL_PRICE":
					VOTE_SHOW_ZEUS_TEMPORAL_PRICE = rss.getString(2);
					break;
				case "ANTIBOT_BORRAR_ITEM":
					ANTIBOT_BORRAR_ITEM = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_PORCENTAJE":
					ANTIBOT_BORRAR_ITEM_PORCENTAJE = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_ID_BORRAR":
					ANTIBOT_BORRAR_ITEM_ID = rss.getString(2);
					break;
				case "ANTIBOT_CHECK_INPEACE_ZONE":
					ANTIBOT_CHECK_INPEACE_ZONE = Boolean.valueOf(rss.getString(2));
					break;
				case "ANNOUNCE_KARMA_PLAYER_WHEN_KILL":
					ANNOUNCE_KARMA_PLAYER_WHEN_KILL = Boolean.valueOf(rss.getString(2));
					break;
				case "ANNOUNCE_KARMA_PLAYER_WHEN_KILL_MSN":
					ANNOUNCE_KARMA_PLAYER_WHEN_KILL_MSN = rss.getString(2);
					break;
				case "PARTY_FINDER_USE_NO_SUMMON_RULEZ":
					PARTY_FINDER_USE_NO_SUMMON_RULEZ = Boolean.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_GO_LEADER_WHEN_ARE_INSTANCE":
					PARTY_FINDER_GO_LEADER_WHEN_ARE_INSTANCE = Boolean.valueOf(rss.getString(2));
					break;
				case "VOTO_REWARD_AUTO_MINUTE_TIME_TO_CHECK":
					if(!rss.getString(2).equals("")){
						VOTO_REWARD_AUTO_MINUTE_TIME_TO_CHECK = Integer.valueOf(rss.getString(2));
					}else{
						VOTO_REWARD_AUTO_MINUTE_TIME_TO_CHECK = 10;
					}
					break;
				case "VOTO_REWARD_AUTO_RANGO_PREMIAR":
					if(rss.getString(2).equals("")){
						VOTO_REWARD_AUTO_RANGO_PREMIAR = 10;
					}else{
						VOTO_REWARD_AUTO_RANGO_PREMIAR = Integer.valueOf(rss.getString(2));
					}
					break;
				case "VOTO_REWARD_AUTO_MENSAJE_FALTA":
					VOTO_REWARD_AUTO_MENSAJE_FALTA = rss.getString(2);
					break;
				case "VOTO_REWARD_AUTO_MENSAJE_META_COMPLIDA":
					VOTO_REWARD_AUTO_MENSAJE_META_COMPLIDA = rss.getString(2);
					break;
				case "VOTE_AUTOREWARD":
					if(onLine){
						boolean tempo = Boolean.parseBoolean(rss.getString(2));
						if(!VOTO_AUTOREWARD && tempo){
							VOTO_AUTOREWARD = Boolean.parseBoolean(rss.getString(2));
							votereward.getInstance().inicializar();
							break;
						}
					}
					VOTO_AUTOREWARD = Boolean.parseBoolean(rss.getString(2));
					break;
				case "":

				case "CASTLE_MANAGER_SHOW_GIRAN":
					GIRAN = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CASTLE_MANAGER_SHOW_ADEN":
					ADEN = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CASTLE_MANAGER_SHOW_RUNE":
					RUNE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CASTLE_MANAGER_SHOW_OREN":
					OREN = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CASTLE_MANAGER_SHOW_DION":
					DION = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CASTLE_MANAGER_SHOW_GLUDIO":
					GLUDIO = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CASTLE_MANAGER_SHOW_GODDARD":
					GODDARD = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CASTLE_MANAGER_SHOW_SCHUTTGART":
					SCHUTTGART = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CASTLE_MANAGER_SHOW_INNADRIL":
					INNADRIL = Boolean.parseBoolean(rss.getString(2));
					break;
				case "DROP_SEARCH_SHOW_IDITEM_TO_PLAYER":
					DROP_SEARCH_SHOW_IDITEM_TO_PLAYER =Boolean.parseBoolean(rss.getString(2));
					break;
				case "SHOW_NEW_MAIN_WINDOWS":
					SHOW_NEW_MAIN_WINDOWS = Boolean.parseBoolean(rss.getString(2));
					break;
				case "FLAG_FINDER_PK_PRIORITY":
					FLAG_FINDER_PK_PRIORITY = Boolean.parseBoolean(rss.getString(2));
					break;
				case "AUGMENT_SPECIAL_PRICE_PAS":
					AUGMENT_SPECIAL_PRICE_PASSIVE = rss.getString(2);
					break;
				case "AUGMENT_SPECIAL_PRICE_CHA":
					AUGMENT_SPECIAL_PRICE_CHANCE = rss.getString(2);
					break;
				case "AUGMENT_SPECIAL_PRICE_ACT":
					AUGMENT_SPECIAL_PRICE_ACTIVE = rss.getString(2);
					break;
				case "DRESSME":
					DRESSME_STATUS = Boolean.parseBoolean(rss.getString(2));
					break;
				case "DRESSME_CAN_USE_IN_OLYS":
					DRESSME_CAN_USE_IN_OLYS = Boolean.parseBoolean(rss.getString(2));
					break;
				case "DRESSME_CAN_CHANGE_DRESS_IN_OLY":
					DRESSME_CAN_CHANGE_IN_OLYS = Boolean.parseBoolean(rss.getString(2));
					break;
				case "DRESSME_NEW_DRESS_IS_FREE":
					DRESSME_NEW_DRESS_IS_FREE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "DRESSME_NEW_DRESS_COST":
					DRESSME_NEW_DRESS_COST = rss.getString(2);
					break;
				case "DRESSME_TARGET":
					DRESSME_TARGET_STATUS = Boolean.parseBoolean(rss.getString(2));
					break;
				case "DROP_SEARCH_ID_MOB_NO_TELEPORT":
					DROP_SEARCH_MOB_BLOCK_TELEPORT.clear();
					if(rss.getString(2).length()>0){
						for(String blockNPC : rss.getString(2).split(",")){
							if(opera.isNumeric(blockNPC)){
								DROP_SEARCH_MOB_BLOCK_TELEPORT.add(Integer.valueOf(blockNPC));
							}else{
								_log.warning("Error loading NPC Block to used Teleport in Drop Search. Data: " + blockNPC);
							}
						}
						Collections.sort(DROP_SEARCH_MOB_BLOCK_TELEPORT);
					}
					break;
				case "RAIDBOSS_ID_MOB_NO_TELEPORT":
					RAIDBOSS_ID_MOB_NO_TELEPORT.clear();
					if(rss.getString(2).length()>0){
						for(String blockNPC : rss.getString(2).split(",")){
							if(opera.isNumeric(blockNPC)){
								RAIDBOSS_ID_MOB_NO_TELEPORT.add(Integer.valueOf(blockNPC));
							}else{
								_log.warning("Error loading NPC Block to used Teleport in Raidboss Info. Data: " + blockNPC);
							}
						}
						Collections.sort(RAIDBOSS_ID_MOB_NO_TELEPORT);
					}
					break;
				case "DROP_SEARCH_CAN_USE_TELEPORT":
					DROP_SEARCH_CAN_USE_TELEPORT = Boolean.parseBoolean(rss.getString(2));
					break;
				case "RETURN_BUFF":
					RETURN_BUFF = Boolean.parseBoolean(rss.getString(2));
					break;
				case "RETURN_BUFF_MINUTES":
					RETURN_BUFF_SECONDS_TO_RETURN = Integer.valueOf(rss.getString(2));
					break;
				case "RETURN_BUFF_IN_OLY":
					RETURN_BUFF_IN_OLY = Boolean.parseBoolean(rss.getString(2));
					break;
				case "RETURN_BUFF_IN_OLY_MINUTES_TO_RETURN":
					RETURN_BUFF_IN_OLY_MINUTES_TO_RETURN = Integer.valueOf(rss.getString(2));
					break;
				case "RETURN_CANCEL_BUFF_NOT_STEALING":
					String NoRobar = rss.getString(2);
					if(RETURN_BUFF_NOT_STEALING.size()>0){
						RETURN_BUFF_NOT_STEALING.clear();
					}
					if(NoRobar.length()<=0){
						break;
					}
					for(String NoRobaSplit : NoRobar.split(",")){
						if(opera.isNumeric(NoRobaSplit)){
							RETURN_BUFF_NOT_STEALING.add(Integer.valueOf(NoRobaSplit));
						}
					}
					break;
				case "TRADE_WHILE_FLAG":
					TRADE_WHILE_FLAG = Boolean.parseBoolean(rss.getString(2));
					break;
				case "TRADE_WHILE_PK":
					TRADE_WHILE_PK = Boolean.parseBoolean(rss.getString(2));
					break;
				case "TELEPORT_CAN_USE_IN_COMBAT_MODE":
					TELEPORT_CAN_USE_IN_COMBAT_MODE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "BUFFCHAR_ACT":
					BUFFCHAR_ACT = Boolean.parseBoolean(rss.getString(2));
					break;
				case "BUFFCHAR_CAN_USE_FLAG":
					BUFFCHAR_CAN_USE_FLAG = Boolean.parseBoolean(rss.getString(2));
					break;
				case "BUFFCHAR_CAN_USE_PK":
					BUFFCHAR_CAN_USE_PK = Boolean.parseBoolean(rss.getString(2));
					break;
				case "BUFFCHAR_CAN_USE_COMBAT_MODE":
					BUFFCHAR_CAN_USE_COMBAT_MODE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "BUFFCHAR_CAN_USE_SIEGE_ZONE":
					BUFFCHAR_CAN_USE_SIEGE_ZONE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "BUFFCHAR_CAN_USE_INDIVIDUAL_BUFF":
					BUFFCHAR_CAN_USE_INDIVIDUAL_BUFF = Boolean.parseBoolean(rss.getString(2));
					break;
				case "BUFFCHAR_FOR_FREE":
					BUFFCHAR_FOR_FREE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "BUFFCHAR_HEAL_FOR_FREE":
					BUFFCHAR_HEAL_FOR_FREE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "BUFFCHAR_CANCEL_FOR_FREE":
					BUFFCHAR_CANCEL_FOR_FREE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "BUFFCHAR_COST_USE":
					BUFFCHAR_COST_USE = rss.getString(2);
					break;
				case "BUFFCHAR_COST_HEAL":
					BUFFCHAR_COST_HEAL = rss.getString(2);
					break;
				case "BUFFCHAR_COST_CANCEL":
					BUFFCHAR_COST_CANCEL = rss.getString(2);
					break;
				case "BUFFCHAR_COST_INDIVIDUAL":
					BUFFCHAR_COST_INDIVIDUAL = rss.getString(2);
					break;
				case "BUFFCHAR_INDIVIDUAL_FOR_FREE":
					BUFFCHAR_INDIVIDUAL_FOR_FREE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "BUFFCHAR_DONATION_SECCION":
					BUFFCHAR_DONATION_SECCION = rss.getString(2);
					break;
				case "BUFFCHAR_DONATION_SECCION_COST":
					BUFFCHAR_DONATION_SECCION_COST = rss.getString(2);
					break;
				case "BUFFCHAR_DONATION_SECCION_REMOVE_ITEM":
					BUFFCHAR_DONATION_SECCION_REMOVE_ITEM = Boolean.parseBoolean(rss.getString(2));
					break;
				case "BUFFCHAR_DONATION_SECCION_ACT":
					BUFFCHAR_DONATION_SECCION_ACT = Boolean.parseBoolean(rss.getString(2));
					break;
				case "BUFFCHAR_PET":
					BUFFCHAR_PET = Boolean.parseBoolean(rss.getString(2));
					break;
				case "ANTIBOT_INACTIVE_MINUTES":
					ANTIBOT_INACTIVE_MINUTES = Integer.parseInt(rss.getString(2));
					break;
				case "OLY_ANTIFEED_SHOW_IN_NAME_CLASS":
					OLY_ANTIFEED_SHOW_IN_NAME_CLASS = Boolean.parseBoolean(rss.getString(2));
					break;
				case "RADIO_PLAYER_NPC_MAXIMO":
					RADIO_PLAYER_NPC_MAXIMO = Integer.valueOf(rss.getString(2));
					break;
				case "SHOW_ZEUS_ENTER_GAME":
					SHOW_ZEUS_ENTER_GAME = Boolean.parseBoolean(rss.getString(2));
					break;
				case "PVP_REWARD":
					PVP_REWARD = Boolean.parseBoolean(rss.getString(2));
					break;
				case "PVP_PARTY_REWARD":
					PVP_PARTY_REWARD = Boolean.parseBoolean(rss.getString(2));
					break;
				case "PVP_REWARD_ITEMS":
					PVP_REWARD_ITEMS = rss.getString(2);
					break;
				case "PVP_PARTY_REWARD_ITEMS":
					PVP_PARTY_REWARD_ITEMS = rss.getString(2);
					break;
				/*case "PREMIUM_CHAR":
					PREMIUM_CHAR = Boolean.parseBoolean(rss.getString(2));
					break;
				case "PREMIUM_CLAN":
					PREMIUM_CLAN = Boolean.parseBoolean(rss.getString(2));
					break;
				case "PREMIUM_CHAR_EXP_PORCEN":
					PREMIUM_CHAR_EXP_PORCEN = Integer.valueOf(rss.getString(2));
					break;
				case "PREMIUM_CHAR_SP_PORCEN":
					PREMIUM_CHAR_SP_PORCEN = Integer.valueOf(rss.getString(2));
					break;
				case "PREMIUM_CLAN_EXP_PORCEN":
					PREMIUM_CLAN_EXP_PORCEN = Integer.valueOf(rss.getString(2));
					break;
				case "PREMIUM_CLAN_SP_PORCEN":
					PREMIUM_CLAN_SP_PORCEN = Integer.valueOf(rss.getString(2));
					break;
				case "PREMIUM_CHAR_DROP_PORCEN":
					PREMIUM_CHAR_DROP_ADENA_PORCEN = Long.valueOf(rss.getString(2));
					break;
				case "PREMIUM_CLAN_DROP_PORCEN":
					PREMIUM_CLAN_DROP_ADENA_PORCEN = Long.valueOf(rss.getString(2));
					break;
				case "PREMIUM_DAYS_GIVE":
					PREMIUM_DAYS_GIVE = Integer.valueOf(rss.getString(2));
					break;
				case "PREMIUM_CHAR_COST_DONATION":
					PREMIUM_CHAR_COST_DONATION = Integer.valueOf(rss.getString(2));
					break;
				case "PREMIUM_CLAN_COST_DONATION":
					PREMIUM_CLAN_COST_DONATION = Integer.valueOf(rss.getString(2));
					break;*/
				case "DELEVEL_CHECK_SKILL":
					DELEVEL_CHECK_SKILL = Boolean.parseBoolean(rss.getString(2));
					break;
				case "MAX_IP_CHECK":
					MAX_IP_CHECK = Boolean.parseBoolean(rss.getString(2));
					break;
				case "MAX_IP_COUNT":
					MAX_IP_COUNT = Integer.valueOf(rss.getString(2));
					break;
				case "MAX_IP_RECORD_DATA":
					MAX_IP_RECORD_DATA = Boolean.parseBoolean(rss.getString(2));
					break;
				case "MAX_IP_VIP_COUNT":
					MAX_IP_VIP_COUNT = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_SEND_ALL_IP":
					ANTIBOT_SEND_ALL_IP = Boolean.parseBoolean(rss.getString(2));
					break;
				case "ELEMENTAL_LVL_ENCHANT_MAX_WEAPON":
					ELEMENTAL_LVL_ENCHANT_MAX_WEAPON = Integer.valueOf(rss.getString(2));
					break;
				case "ELEMENTAL_LVL_ENCHANT_MAX_ARMOR":
					ELEMENTAL_LVL_ENCHANT_MAX_ARMOR = Integer.valueOf(rss.getString(2));
					break;
				case "CHAR_PANEL":
					CHAR_PANEL = Boolean.parseBoolean(rss.getString(2));
					break;
				case "SHOW_FIXME_WINDOWS":
					SHOW_FIXME_WINDOWS = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD":
					COMMUNITY_BOARD = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PART_EXEC":
					COMMUNITY_BOARD_PART_EXEC = rss.getString(2);
					break;
				case "COMMUNITY_BOARD_REGION":
					general.COMMUNITY_BOARD_REGION= Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_REGION_PART_EXEC":
					general.COMMUNITY_BOARD_REGION_PART_EXEC = rss.getString(2);
					break;
				case "COMMUNITY_BOARD_ENGINE":
					general.COMMUNITY_BOARD_ENGINE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_ENGINE_PART_EXEC":
					general.COMMUNITY_BOARD_ENGINE_PART_EXEC = rss.getString(2);
					break;
				case "COMMUNITY_BOARD_ROWS_FOR_PAGE":
					general.COMMUNITY_BOARD_ROWS_FOR_PAGE = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_TOPPLAYER_LIST":
					general.COMMUNITY_BOARD_TOPPLAYER_LIST = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_CLAN_LIST":
					general.COMMUNITY_BOARD_CLAN_LIST = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_MERCHANT_LIST":
					general.COMMUNITY_BOARD_MERCHANT_LIST = Integer.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_VOTE_CBE":
					BTN_SHOW_VOTE_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_BUFFER_CBE":
					BTN_SHOW_BUFFER_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_TELEPORT_CBE":
					BTN_SHOW_TELEPORT_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SHOP_CBE":
					BTN_SHOW_SHOP_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_WAREHOUSE_CBE":
					BTN_SHOW_WAREHOUSE_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_AUGMENT_CBE":
					BTN_SHOW_AUGMENT_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SUBCLASES_CBE":
					BTN_SHOW_SUBCLASES_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CLASS_TRANSFER_CBE":
					BTN_SHOW_CLASS_TRANSFER_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CONFIG_PANEL_CBE":
					BTN_SHOW_CONFIG_PANEL_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DROP_SEARCH_CBE":
					BTN_SHOW_DROP_SEARCH_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_PVPPK_LIST_CBE":
					BTN_SHOW_PVPPK_LIST_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_LOG_PELEAS_CBE":
					BTN_SHOW_LOG_PELEAS_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CASTLE_MANAGER_CBE":
					BTN_SHOW_CASTLE_MANAGER_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DESAFIO_CBE":
					BTN_SHOW_DESAFIO_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SYMBOL_MARKET_CBE":
					BTN_SHOW_SYMBOL_MARKET_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CLANALLY_CBE":
					BTN_SHOW_CLANALLY_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_PARTYFINDER_CBE":
					BTN_SHOW_PARTYFINDER_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_FLAGFINDER_CBE":
					BTN_SHOW_FLAGFINDER_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_COLORNAME_CBE":
					BTN_SHOW_COLORNAME_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DELEVEL_CBE":
					BTN_SHOW_DELEVEL_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_REMOVE_ATRIBUTE_CBE":
					BTN_SHOW_REMOVE_ATRIBUTE_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_BUG_REPORT_CBE":
					BTN_SHOW_BUG_REPORT_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DONATION_CBE":
					BTN_SHOW_DONATION_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CAMBIO_NOMBRE_PJ_CBE":
					BTN_SHOW_CAMBIO_NOMBRE_PJ_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CAMBIO_NOMBRE_CLAN_CBE":
					BTN_SHOW_CAMBIO_NOMBRE_CLAN_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_VARIAS_OPCIONES_CBE":
					BTN_SHOW_VARIAS_OPCIONES_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_ELEMENT_ENHANCED_CBE":
					BTN_SHOW_ELEMENT_ENHANCED_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_ENCANTAMIENTO_ITEM_CBE":
					BTN_SHOW_ENCANTAMIENTO_ITEM_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_AUGMENT_SPECIAL_CBE":
					BTN_SHOW_AUGMENT_SPECIAL_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_GRAND_BOSS_STATUS_CBE":
					BTN_SHOW_GRAND_BOSS_STATUS_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_RAIDBOSS_INFO_CBE":
					BTN_SHOW_RAIDBOSS_INFO_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_TRANSFORMACION_CBE":
					BTN_SHOW_TRANSFORMATION_CBE = Boolean.valueOf(rss.getString(2));
					break;/*
				case "PREMIUM_CHAR_DROP_SPOIL":
					PREMIUM_CHAR_DROP_SPOIL = Float.valueOf(rss.getString(2));
					break;
				case "PREMIUM_CLAN_DROP_SPOIL":
					PREMIUM_CLAN_DROP_SPOIL = Float.valueOf(rss.getString(2));
					break;
				case "PREMIUM_CHAR_DROP_RAID":
					PREMIUM_CHAR_DROP_RAID = Float.valueOf(rss.getString(2));
					break;
				case "PREMIUM_CLAN_DROP_RAID":
					PREMIUM_CLAN_DROP_RAID = Float.valueOf(rss.getString(2));
					break;*/
				case "ANTI_OVER_ENCHANT_ACT":
					ANTI_OVER_ENCHANT_ACT = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTI_OVER_ENCHANT_CHECK_BEFORE_ATTACK":
					ANTI_OVER_ENCHANT_CHECK_BEFORE_ATTACK = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTI_OVER_ENCHANT_SECOND_BETWEEN_CHECK_BEFORE_ATTACK":
					ANTI_OVER_ENCHANT_SECOND_BETWEEN_CHECK_BEFORE_ATTACK = Integer.valueOf(rss.getString(2));
					break;
				case "ANTI_OVER_ENCHANT_MESJ_PUNISH":
					ANTI_OVER_ENCHANT_MESJ_PUNISH = rss.getString(2);
					break;
				case "ANTI_OVER_TYPE_PUNISH":
					ANTI_OVER_TYPE_PUNISH = rss.getString(2);
					break;
				case "ANTI_OVER_ENCHANT_ANNOUCEMENT_ALL":
					ANTI_OVER_ENCHANT_ANNOUCEMENT_ALL = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTI_OVER_ENCHANT_PUNISH_ALL_SAME_IP":
					ANTI_OVER_ENCHANT_PUNISH_ALL_SAME_IP = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTI_OVER_ENCHANT_MAX_WEAPON":
					ANTI_OVER_ENCHANT_MAX_WEAPON = Integer.valueOf(rss.getString(2));
					break;
				case "ANTI_OVER_ENCHANT_MAX_ARMOR":
					ANTI_OVER_ENCHANT_MAX_ARMOR = Integer.valueOf(rss.getString(2));
					break;
				case "REGISTER_EMAIL_ONLINE":
					REGISTER_EMAIL_ONLINE = Boolean.valueOf(rss.getString(2));
					break;
				case "REGISTER_NEW_PLAYER_WAITING_TIME":
					REGISTER_NEW_PLAYER_WAITING_TIME = Integer.valueOf(rss.getString(2));
					break;
				case "REGISTER_NEW_PLAYER_RE_ENTER_WAITING_TIME":
					REGISTER_NEW_PLAYER_RE_ENTER_WAITING_TIME = Integer.valueOf(rss.getString(2));
					break;
				case "REGISTER_NEW_PlAYER_TRIES":
					REGISTER_NEW_PlAYER_TRIES = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_CLAN_PART_EXEC":
					COMMUNITY_BOARD_CLAN_PART_EXEC = rss.getString(2);
					break;
				case "COMMUNITY_BOARD_CLAN":
					COMMUNITY_BOARD_CLAN = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_CLAN_ROWN_LIST":
					COMMUNITY_BOARD_CLAN_ROWN_LIST = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT":
					COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_SIEGE":
					COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_SIEGE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CASTLE":
					COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CASTLE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_ONLY_PEACEZONE":
					COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_ONLY_PEACEZONE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CLANHALL_WITH_ANOTHER_CLAN":
					COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CLANHALL_WITH_ANOTHER_CLAN = Boolean.parseBoolean(rss.getString(2));
					break;
				case "REGISTER_NEW_PLAYER_BLOCK_CHAT":
					REGISTER_NEW_PLAYER_BLOCK_CHAT = Boolean.parseBoolean(rss.getString(2));
					break;
				case "REGISTER_NEW_PLAYER_CHECK_BANNED_ACCOUNT":
					REGISTER_NEW_PLAYER_CHECK_BANNED_ACCOUNT = Boolean.parseBoolean(rss.getString(2));
					break;
				case "OPCIONES_CHAR_CAMBIO_NOMBRE":
					OPCIONES_CHAR_CAMBIO_NOMBRE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "OPCIONES_CLAN_CAMBIO_NOMBRE":
					OPCIONES_CLAN_CAMBIO_NOMBRE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_REGION_PLAYER_ON_LIST":
					COMMUNITY_BOARD_REGION_PLAYER_ON_LIST = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_SECONDS_TO_RESEND_ANTIBOT":
					ANTIBOT_SECONDS_TO_RESEND_ANTIBOT = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_CHECK_DUALBOX":
					ANTIBOT_CHECK_DUALBOX = Boolean.valueOf(rss.getString(2));
					break;
				/*case "PREMIUM_CHAR_DROP_ITEM":
					PREMIUM_CHAR_DROP_ITEM = Integer.valueOf(rss.getString(2));
					break;
				case "PREMIUM_CLAN_DROP_ITEM":
					PREMIUM_CLAN_DROP_ITEM = Integer.valueOf(rss.getString(2));
					break;*/
					
				case "EVENT_RAIDBOSS_RAID_ID":
					String tempEVENT_RAIDBOSS_RAID_ID = rss.getString(2);
					if(tempEVENT_RAIDBOSS_RAID_ID.length()>0){
						EVENT_RAIDBOSS_RAID_ID.clear();
						for(String strNpcId : tempEVENT_RAIDBOSS_RAID_ID.split(",")){
							try{
								EVENT_RAIDBOSS_RAID_ID.add(Integer.valueOf(strNpcId));
							}catch(Exception a){
								
							}
						}
					}
					break;
				case "EVENT_RAIDBOSS_RAID_POSITION":
					EVENT_RAIDBOSS_RAID_POSITION = rss.getString(2);
					break;
				case "EVENT_RAIDBOSS_PLAYER_POSITION":
					EVENT_RAIDBOSS_PLAYER_POSITION = rss.getString(2);
					break;
				case "EVENT_RAIDBOSS_PLAYER_INMOBIL":
					EVENT_RAIDBOSS_PLAYER_INMOBIL = Boolean.parseBoolean(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_REWARD_WIN":
					EVENT_RAIDBOSS_REWARD_WIN = rss.getString(2);
					break;
				case "EVENT_RAIDBOSS_REWARD_LOOSER":
					EVENT_RAIDBOSS_REWARD_LOOSER = rss.getString(2);
					break;
				case "EVENT_RAIDBOSS_PLAYER_MIN_LEVEL":
					EVENT_RAIDBOSS_PLAYER_MIN_LEVEL = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_PLAYER_MAX_LEVEL":
					EVENT_RAIDBOSS_PLAYER_MAX_LEVEL = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_PLAYER_MIN_REGIS":
					EVENT_RAIDBOSS_PLAYER_MIN_REGIS = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_PLAYER_MAX_REGIS":
					EVENT_RAIDBOSS_PLAYER_MAX_REGIS = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_SECOND_TO_BACK":
					EVENT_RAIDBOSS_SECOND_TO_BACK = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_JOINTIME":
					EVENT_RAIDBOSS_JOINTIME = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_EVENT_TIME":
					EVENT_RAIDBOSS_EVENT_TIME = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_COLORNAME":
					EVENT_RAIDBOSS_COLORNAME = rss.getString(2);
					break;
				case "EVENT_RAIDBOSS_CHECK_DUALBOX":
					EVENT_RAIDBOSS_CHECK_DUALBOX = Boolean.parseBoolean(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_CANCEL_BUFF":
					EVENT_RAIDBOSS_CANCEL_BUFF = Boolean.parseBoolean(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_UNSUMMON_PET":
					EVENT_RAIDBOSS_UNSUMMON_PET = Boolean.parseBoolean(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_SECOND_TO_REVIVE":
					EVENT_RAIDBOSS_SECOND_TO_REVIVE = Integer.parseInt(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_SECOND_TO_TELEPORT_RADIBOSS":
					EVENT_RAIDBOSS_SECOND_TO_TELEPORT_RADIBOSS = Integer.parseInt(rss.getString(2));
					break;
				case "ANTIBOT_SEND_JAIL_ALL_DUAL_BOX":
					ANTIBOT_SEND_JAIL_ALL_DUAL_BOX = Boolean.parseBoolean(rss.getString(2));
					break;
				case "ANTIFEED_ENCHANT_SKILL_REUSE":
					ANTIFEED_ENCHANT_SKILL_REUSE = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_HOUR_TO_START":
					EVENT_RAIDBOSS_HOUR_TO_START = rss.getString(2);
					break;
				case "EVENT_RAIDBOSS_AUTOEVENT":
					EVENT_RAIDBOSS_AUTOEVENT = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CAN_USE_BSOE_PK":
					CAN_USE_BSOE_PK = Boolean.parseBoolean(rss.getString(2));
					break;
				case "FLAG_FINDER_MIN_PVP_FROM_TARGET":
					FLAG_FINDER_MIN_PVP_FROM_TARGET = Integer.valueOf(rss.getString(2));
					break;
				case "PREMIUM_MESSAGE":
					PREMIUM_MESSAGE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "VOTO_REWARD_AUTO_REWARD_META_HOPZONE":
					VOTO_REWARD_AUTO_REWARD_META_HOPZONE = rss.getString(2);
					break;
				case "VOTO_REWARD_AUTO_REWARD_META_TOPZONE":
					VOTO_REWARD_AUTO_REWARD_META_TOPZONE = rss.getString(2);
					break;
				case "VOTO_REWARD_AUTO_AFK_CHECK":
					VOTO_REWARD_AUTO_AFK_CHECK = Boolean.parseBoolean(rss.getString(2));
					break;
				case "TRANSFORM_TIME":
					TRANSFORM_TIME = Boolean.parseBoolean(rss.getString(2));
					break;
				case "TRANSFORM_TIME_MINUTES":
					TRANSFORM_TIME_MINUTES = Integer.valueOf(rss.getString(2));
					break;
				case "TRANSFORM_REUSE_TIME_MINUTES":
					TRANSFORM_REUSE_TIME_MINUTES = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_REGION_LEGEND":
					COMMUNITY_REGION_LEGEND.clear();
					String RegionLegend = rss.getString(2);
					for(String parte : RegionLegend.split(",")){
						COMMUNITY_REGION_LEGEND.add(parte.split(":")[0]);
					}
					break;
				case "PVP_PK_PROTECTION_LIFETIME_MINUTES":
					PVP_PK_PROTECTION_LIFETIME_MINUTES = Integer.valueOf(rss.getString(2));
					break;
				case "PVP_REWARD_CHECK_DUALBOX":
					PVP_REWARD_CHECK_DUALBOX = Boolean.parseBoolean(rss.getString(2));
					break;
				case "BUFFER_HEAL_CAN_FLAG":
					BUFFER_HEAL_CAN_FLAG = Boolean.parseBoolean(rss.getString(2));
					break;
				case "BUFFER_HEAL_CAN_IN_COMBAT":
					BUFFER_HEAL_CAN_IN_COMBAT = Boolean.parseBoolean(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_MINUTE_INTERVAL":
					EVENT_RAIDBOSS_MINUTE_INTERVAL = Integer.valueOf(rss.getString(2));
					break;
				//'524','EVENT_RAIDBOSS_MINUTE_INTERVAL_START_SERVER'
				case "EVENT_RAIDBOSS_MINUTE_INTERVAL_START_SERVER":
					EVENT_RAIDBOSS_MINUTE_INTERVAL_START_SERVER = Integer.valueOf(rss.getString(2));
					break;
				case "BUFFCHAR_EDIT_SCHEME_ON_MAIN_WINDOWS_BUFFER":
					BUFFCHAR_EDIT_SCHEME_ON_MAIN_WINDOWS_BUFFER = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_BLACK_LIST":
					ANTIBOT_BLACK_LIST = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_BLACK_LIST_MULTIPLIER":
					ANTIBOT_BLACK_LIST_MULTIPLIER = Integer.valueOf(rss.getString(2));
					break;
				case "OLY_DUAL_BOX_CONTROL":
					OLY_DUAL_BOX_CONTROL = Boolean.valueOf(rss.getString(2));
					break;
				case "PVP_REWARD_RANGE":
					PVP_REWARD_RANGE = Integer.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_CAN_USE_ONLY_NOBLE":
					PARTY_FINDER_CAN_USE_ONLY_NOBLE = Boolean.valueOf(rss.getString(2));
					break;
				case "TELEPORT_FOR_FREE_UP_TO_LEVEL":
					TELEPORT_FOR_FREE_UP_TO_LEVEL = Boolean.valueOf(rss.getString(2));
					break;
				case "TELEPORT_FOR_FREE_UP_TO_LEVEL_LV":
					TELEPORT_FOR_FREE_UP_TO_LEVEL_LV = Integer.valueOf(rss.getString(2));
					break;
				case "VOTE_REWARD_PERSONAL_VOTE_ALL_IP_PLAYER":
					VOTE_REWARD_PERSONAL_VOTE_ALL_IP_PLAYER = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_REPUTATION_CLAN":
					EVENT_REPUTATION_CLAN = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_REPUTATION_CLAN_ID_NPC":
					EVENT_REPUTATION_CLAN_ID_NPC = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_REPUTATION_LVL_TO_GIVE":
					EVENT_REPUTATION_LVL_TO_GIVE = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_REPUTATION_MIN_PLAYER":
					EVENT_REPUTATION_MIN_PLAYER = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_REPUTATION_NEED_ALL_MEMBER_ONLINE":
					EVENT_REPUTATION_NEED_ALL_MEMBER_ONLINE = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_REPUTATION_REPU_TO_GIVE":
					EVENT_REPUTATION_REPU_TO_GIVE = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_SHOUT_BLOCK":
					CHAT_SHOUT_BLOCK = Boolean.valueOf(rss.getString(2));
					break;
				case "CHAT_TRADE_BLOCK":
					CHAT_TRADE_BLOCK = Boolean.valueOf(rss.getString(2));
					break;
				case "CHAT_WISP_BLOCK":
					CHAT_WISP_BLOCK = Boolean.valueOf(rss.getString(2));
					break;
				case "CHAT_SHOUT_NEED_PVP":
					CHAT_SHOUT_NEED_PVP = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_SHOUT_NEED_LEVEL":
					CHAT_SHOUT_NEED_LEVEL = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_SHOUT_NEED_LIFETIME":
					CHAT_SHOUT_NEED_LIFETIME = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_TRADE_NEED_PVP":
					CHAT_TRADE_NEED_PVP = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_TRADE_NEED_LEVEL":
					CHAT_TRADE_NEED_LEVEL = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_TRADE_NEED_LIFETIME":
					CHAT_TRADE_NEED_LIFETIME = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_WISP_NEED_PVP":
					CHAT_WISP_NEED_PVP = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_WISP_NEED_LEVEL":
					CHAT_WISP_NEED_LEVEL = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_WISP_NEED_LIFETIME":
					CHAT_WISP_NEED_LIFETIME = Integer.valueOf(rss.getString(2));
					break;
				case "FLAG_FINDER_CHECK_CLAN":
					FLAG_FINDER_CHECK_CLAN = Boolean.valueOf(rss.getBoolean(2));
					break;
					
				case "EVENT_TOWN_WAR_AUTOEVENT":
					EVENT_TOWN_WAR_AUTOEVENT = Boolean.valueOf(rss.getBoolean(2));
					break;
				case "EVENT_TOWN_WAR_MINUTES_START_SERVER":
					EVENT_TOWN_WAR_MINUTES_START_SERVER = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_MINUTES_INTERVAL":
					EVENT_TOWN_WAR_MINUTES_INTERVAL = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_CITY_ON_WAR":
					EVENT_TOWN_WAR_CITY_ON_WAR = rss.getString(2);
					break;
				case "EVENT_TOWN_WAR_MINUTES_EVENT":
					EVENT_TOWN_WAR_MINUTES_EVENT = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_JOIN_TIME":
					EVENT_TOWN_WAR_JOIN_TIME = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_GIVE_PVP_REWARD":
					EVENT_TOWN_WAR_GIVE_PVP_REWARD = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_GIVE_REWARD_TO_TOP_PPL_KILLER":
					EVENT_TOWN_WAR_GIVE_REWARD_TO_TOP_PPL_KILLER = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_REWARD_GENERAL":
					EVENT_TOWN_WAR_REWARD_GENERAL = rss.getString(2);
					break;
				case "EVENT_TOWN_WAR_REWARD_TOP_PLAYER":
					EVENT_TOWN_WAR_REWARD_TOP_PLAYER = rss.getString(2);
					break;
				case "EVENT_TOWN_WAR_RANDOM_CITY":
					EVENT_TOWN_WAR_RANDOM_CITY = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_DUAL_BOX_CHECK":
					EVENT_TOWN_WAR_DUAL_BOX_CHECK = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_HIDE_NPC":
					EVENT_TOWN_WAR_HIDE_NPC = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_NPC_ID_HIDE":
					EVENT_TOWN_WAR_NPC_ID_HIDE = rss.getString(2);
					break;
				case "EVENT_TOWN_WAR_CAN_USE_BUFFER":
					EVENT_TOWN_WAR_CAN_USE_BUFFER = Boolean.valueOf(rss.getString(2));
					break;
				case "VOTE_EVERY_12_HOURS":
					VOTE_EVERY_12_HOURS = Boolean.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_GRAND_RB_EXEC":
					COMMUNITY_BOARD_GRAND_RB_EXEC = rss.getString(2);
					break;
				case "COMMUNITY_BOARD_GRAND_RB":
					COMMUNITY_BOARD_GRAND_RB = Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_BLACKSMITH_CBE":
					BTN_SHOW_BLACKSMITH_CBE = Boolean.valueOf(rss.getString(2));
					break;
				/*case "DONATION_LV_85_COST":
					DONATION_LV_85_COST = Integer.valueOf(rss.getString(2));
					break;
				case "DONATION_FAME_COST":
					DONATION_FAME_COST =Integer.valueOf(rss.getString(2));
					break;
				case "DONATION_NOBLE_COST":
					DONATION_NOBLE_COST =Integer.valueOf(rss.getString(2));
					break;
				case "DONATION_FAME_AMOUNT":
					DONATION_FAME_AMOUNT = Integer.valueOf(rss.getString(2));
					break;
				case "DONATION_CLAN_LV_COST":
					DONATION_CLAN_LV_COST =Integer.valueOf(rss.getString(2));
					break;
				case "DONATION_CLAN_LV_LV":
					DONATION_CLAN_LV_LV =Integer.valueOf(rss.getString(2));
					break;
				case "DONATION_REDUCE_PK_COST":
					DONATION_REDUCE_PK_COST = Integer.valueOf(rss.getString(2));
					break;
				case "DONATION_CHANGE_SEX_COST":
					DONATION_CHANGE_SEX_COST = Integer.valueOf(rss.getString(2));
					break;
				case "DONATION_AIO_CHAR_SIMPLE_COSTO":
					DONATION_AIO_CHAR_SIMPLE_COSTO =Integer.valueOf(rss.getString(2));
					break;
				case "DONATION_AIO_CHAR_30_COSTO":
					DONATION_AIO_CHAR_30_COSTO =Integer.valueOf(rss.getString(2));
					break;
				case "DONATION_AIO_CHAR_LV_REQUEST":
					DONATION_AIO_CHAR_LV_REQUEST =Integer.valueOf(rss.getString(2));
					break;
				case "DONATION_CHANGE_CHAR_NAME_COST":
					DONATION_CHANGE_CHAR_NAME_COST = Integer.valueOf(rss.getString(2));
					break;
				case "DONATION_CHANGE_CLAN_NAME_COST":
					DONATION_CHANGE_CLAN_NAME_COST = Integer.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_BUFFER_AIO_30":
					OPCIONES_CHAR_BUFFER_AIO_30 = Boolean.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_BUFFER_AIO_PRICE_30":
					OPCIONES_CHAR_BUFFER_AIO_PRICE_30 = rss.getString(2);
					break;*/
				case "BTN_SHOW_PARTYMATCHING_CBE":
					BTN_SHOW_PARTYMATCHING_CBE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PARTYFINDER":
					COMMUNITY_BOARD_PARTYFINDER = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PARTYFINDER_EXEC":
					COMMUNITY_BOARD_PARTYFINDER_EXEC = rss.getString(2);
					break;
				case "COMMUNITY_BOARD_PARTYFINDER_SEC_BETWEEN_MSJE":
					COMMUNITY_BOARD_PARTYFINDER_SEC_BETWEEN_MSJE = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST":
					COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PARTYFINDER_SEC_ON_BOARD":
					COMMUNITY_BOARD_PARTYFINDER_SEC_ON_BOARD = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PARTYFINDER_COMMAND_ENABLE":
					COMMUNITY_BOARD_PARTYFINDER_COMMAND_ENABLE = Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_AUCTIONHOUSE_CBE":
					BTN_SHOW_AUCTIONHOUSE_CBE = Boolean.valueOf(rss.getString(2));
					break;
				case "RAIDBOSS_OBSERVE_MODE":
					RAIDBOSS_OBSERVE_MODE = Boolean.valueOf(rss.getString(2));
					break;
				case "DROP_SEARCH_OBSERVE_MODE":
					DROP_SEARCH_OBSERVE_MODE = Boolean.valueOf(rss.getString(2));
					break;
				case "OLY_CAN_USE_SCHEME_BUFFER":
					OLY_CAN_USE_SCHEME_BUFFER = Boolean.valueOf(rss.getString(2));
					break;
				}
				
			}
		}catch(SQLException a){
			_log.warning("ZeuS Error CARGADO->"+a.getMessage());
		}finally{
			
		}
		
		try{
		conn.close();
		}catch(Exception a){
			
		}
		
		loadPropertyWeb();
		if(!onLine){
//			loadAllDropList();
			try{
				TownWarEvent.getInstance().AutoEventStart(true);
			}catch(Exception e){
				
			}
		}
		
		try{
			loadDonationConfig();
		}catch(Exception a){
			
		}
		
		try{
			loadTeleportMain();
		}catch(Exception a){

		}
		try{
			loadShopMain();
		}catch(Exception a){

		}
		try{
			loadPreguntasBot();
		}catch(Exception a){

		}

		try{
			loadIPBLOCK();
		}catch(Exception a){

		}

		try{
			loadBuffDataChar();
		}catch(Exception a){

		}

		try{
			donaManager.loadShop();
		}catch(Exception a){

		}

		try{
			buffer_zeus.loadBuff();
		}catch(Exception a){

		}

		try{
			loadZeuSPremium();
		}catch(Exception a){

		}
/*
		try{
			httpResp.getPJPlus();
		}catch(Exception a){

		}
*/
		try{
			checkDualBoxData();
		}catch(Exception a){

		}
		
		try{
			loadPremiumServices();
		}catch(Exception a){
			_log.warning("Error loading premium data->" + a.getMessage());
		}
		
		if(!onLine){
			try{
				handler.registerHandler();
			}catch(Exception a){
				_log.warning("Error al cargar los ZeuS Voice Handler");
			}

			try{
				adminHandler.registerHandler();
			}catch(Exception a){
				_log.warning("Error al cargar los ZeuS Voide Handler");
			}

			System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::");
			System.out.println("---------ZeuS Service initialized---------------");
			System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::");

			try{
				loadLenguaje();
				System.out.println("---------ZeuS Lenguaje initialized---------------");
				System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::");
			}catch(Exception e){

			}

			onLine = true;
			
			BeginTime = opera.getFechaActual();
			unixTimeBeginServer = opera.getUnixTimeNow();
			
		}else{
			try{
				loadLenguaje();
			}catch(Exception a){
				_log.warning("ZEUS LENGUAJE ERROR->" + a.getMessage());
			}
			try{
				loadPremiumServices();
			}catch(Exception a){
				_log.warning("Error loading premium data->" + a.getMessage());
			}			
			if(showmessage){
				System.out.println("---------ZeuS Config Change ---------------");
			}
		}
		
		try{
			getAllAutoUpdate();
		}catch(Exception a){
			
		}		


	}
	
	public static HashMap<String,HashMap<String,String>> getCharInfo(){
		return CHARINFO;
	}
	
	public static HashMap<Integer,Integer> getClasesCount(){
		return CLASES_COUNT;
	}
	
	public static HashMap<Integer,String> getIdToCharName(){
		return ID_TO_CHAR_NAME;
	}
	
	public static Vector<Integer> getTopSearchItem(){
		return TopSearch_Item;
	} 
	public static Vector<Integer> getTopSearchMonster(){
		return TopSearch_Monster;
	} 
	
	private static void _getMostSearch(){
		
		if(TopSearch_Item!=null){
			TopSearch_Item.clear();
		}
		if(TopSearch_Monster!=null){
			TopSearch_Monster.clear();
		}
		String consulta = "SELECT * FROM zeus_rank_acc WHERE zeus_rank_acc.tip=\"SEARCH_DROP\" ORDER BY zeus_rank_acc.cant DESC LIMIT 5";
		Connection conn = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					try{
						TopSearch_Item.add(rss.getInt("id"));
					}catch(SQLException e){

					}
				}
		conn.close();
			}catch(SQLException e){

			}
		try{
		conn.close();
		}catch (Exception e) {

		}
		
		consulta = "SELECT * FROM zeus_rank_acc WHERE zeus_rank_acc.tip=\"SEARCH_NPC\" ORDER BY zeus_rank_acc.cant DESC LIMIT 5";
		conn = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					try{
						TopSearch_Monster.add(rss.getInt("id"));
					}catch(SQLException e){

					}
				}
		conn.close();
			}catch(SQLException e){

			}
		try{
		conn.close();
		}catch (Exception e) {

		}
	}
	
	
	
	
	
	public static HashMap<String,Integer> getRaceCount(){
		return RACE_COUNT;
	}
	
	
	
	public static void _getAllChar(){
		
		if(CHARINFO!=null){
			CHARINFO.clear();
		}
		if(ID_TO_CHAR_NAME !=null){
			ID_TO_CHAR_NAME.clear();
		}
		if(CLASES_COUNT!=null){
			CLASES_COUNT.clear();
		}
		if(RACE_COUNT!=null){
			RACE_COUNT.clear();
		}
		
		String Consulta = "SELECT characters.char_name, characters.charId, characters.`level`, characters.race, characters.base_class, characters.pvpkills, characters.pkkills FROM characters WHERE characters.accesslevel = 0 ORDER BY characters.char_name ASC";
		
		Connection conn = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Consulta);
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					try{
						
						CHARINFO.put(rss.getString("char_name"), new HashMap<String, String>());
						CHARINFO.get(rss.getString("char_name")).put("NOM", rss.getString("char_name"));
						CHARINFO.get(rss.getString("char_name")).put("LEVEL", String.valueOf(rss.getInt("level")));
						CHARINFO.get(rss.getString("char_name")).put("PVP", String.valueOf(rss.getInt("pvpkills")));
						CHARINFO.get(rss.getString("char_name")).put("PK", String.valueOf(rss.getInt("pkkills")));
						CHARINFO.get(rss.getString("char_name")).put("BASE", String.valueOf(rss.getInt("base_class")));
						CHARINFO.get(rss.getString("char_name")).put("ID", String.valueOf(rss.getInt("charId")));
						ID_TO_CHAR_NAME.put(rss.getInt("charId"), rss.getString("char_name"));
						if(CLASES_COUNT.containsKey(rss.getInt("base_class"))){
							CLASES_COUNT.put(rss.getInt("base_class"), CLASES_COUNT.get(rss.getInt("base_class")) +1);
						}else{
							CLASES_COUNT.put(rss.getInt("base_class"), 1);
						}
						String RaceTmp = general.classData.get(rss.getInt("base_class")).get("CLASSBASE");
						
						if(RACE_COUNT.containsKey(RaceTmp)){
							RACE_COUNT.put(RaceTmp, RACE_COUNT.get(RaceTmp)+1);
						}else{
							RACE_COUNT.put(RaceTmp, 1);
						}
						
					}catch(SQLException e){

					}
				}
		conn.close();
			}catch(SQLException e){

			}
		try{
		conn.close();
		}catch (Exception e) {

		}
	}	
	
	public static HashMap<Integer, premiumsystem> getPremiumServices(){
		return PREMIUM_SERVICES;
	}
	
	private static void loadPremiumServices(){
		//PREMIUM_SERVICES
		//InfoAuction V = new InfoAuction(item.getObjectId(),item);

		PREMIUM_SERVICES.clear();
		
		File dir = new File("./config/zeus_premium.xml");
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dir);
			
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
			{
				if ("list".equalsIgnoreCase(n.getNodeName()))
				{
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()){
						if ("premium".equalsIgnoreCase(d.getNodeName()))
						{
							int IDPremiun = Integer.valueOf(d.getAttributes().getNamedItem("id").getNodeValue());
							String NombrePremiun = d.getAttributes().getNamedItem("name").getNodeValue();
							NamedNodeMap n2 = d.getAttributes();
							Node first = d.getFirstChild();
							int exp=0, sp=0, adena=0, spoil=0, drop=0, epaulette=0,craft=0,mwcraft=0,days=0,cost=0;
							boolean isAcc=true;
							boolean isEnabled= false;
							String Icono="";							
							Vector<String> DataToShow = new Vector<String>();
							for (Node dd = first; dd != null; dd = dd.getNextSibling())
							{
								if ("set".equalsIgnoreCase(dd.getNodeName()))
								{
									String valor = dd.getAttributes().getNamedItem("val").getNodeValue();
									boolean grabar = true;
									switch(dd.getAttributes().getNamedItem("name").getNodeValue().toLowerCase()){
										case "exp":
											exp = Integer.valueOf(valor);
											break;
										case "sp":
											sp = Integer.valueOf(valor);
											break;
										case "adena":
											adena = Integer.valueOf(valor);
											break;
										case "spoil":
											spoil = Integer.valueOf(valor);
											break;
										case "drop":
											drop = Integer.valueOf(valor);
											break;
										case "epaulette":
											epaulette = Integer.valueOf(valor);
											break;
										case "craft":
											craft = Integer.valueOf(valor);
											break;
										case "mw_craft":
											mwcraft = Integer.valueOf(valor);
											break;
										case "days":
											grabar=false;
											days = Integer.valueOf(valor);
											break;
										case "cost":
											grabar=false;
											cost = Integer.valueOf(valor);
											break;
										case "tipo":
											grabar=false;
											isAcc = valor.equalsIgnoreCase("account");
											break;
										case "ima":
											grabar=false;
											Icono = valor;
											break;
										case "active":
											isEnabled = Boolean.parseBoolean(valor);
											grabar=false;
											break;
									}
									if(grabar){
										DataToShow.add(dd.getAttributes().getNamedItem("name").getNodeValue().toLowerCase() + ":" + valor);
									}
								}
							}
							premiumsystem V = new premiumsystem(IDPremiun,exp,sp,drop,adena,spoil,epaulette,craft,mwcraft,days,cost,isAcc,NombrePremiun,Icono,DataToShow,isEnabled);
							PREMIUM_SERVICES.put(IDPremiun, V);
						}
					}
				}
			}
		}catch(Exception a){
			//System.out.print(a.getMessage());
		}	
		
	}
	

	protected static boolean loadSerial(){
		try{
			Properties propZ = new Properties();
			propZ.load(new FileInputStream(new File(ZEUS_CONFIG)));
			SERIAL_ = String.valueOf(propZ.getProperty("SERIAL"));
			HOST_ = String.valueOf(propZ.getProperty("HOST"));
			USER_ = String.valueOf(propZ.getProperty("USER"));
			return true;
		}catch(Exception a){
			_log.warning(":::::::: ERROR AL CARGAR SERIAL DE ZEUS ::::::::::");
			_isValid=false;
			return false;
		}
	}
	
	protected static boolean loadDonationConfig(){
		try{
			Properties propZ = new Properties();
			propZ.load(new FileInputStream(new File(ZEUS_DONATION_CONFIG)));
			DONA_ID_ITEM = Integer.valueOf(propZ.getProperty("ID_DONATION_COIN"));
			DONATION_EXPLAIN_HOW_DO_IT = propZ.getProperty("DONATION_EXPLAIN_ABOUT_IT","");
			DONATION_CHANGE_CHAR_NAME_COST = Integer.valueOf(propZ.getProperty("DONATION_CHARACTER_RENAME","0"));
			DONATION_CHANGE_CLAN_NAME_COST = Integer.valueOf(propZ.getProperty("DONATION_CLAN_RENAME","0"));
			DONATION_255_RECOMMENDS = Integer.valueOf(propZ.getProperty("DONATION_255_RECOMMENDS","0"));
			DONATION_NOBLE_COST = Integer.valueOf(propZ.getProperty("DONATION_NOBLE","0"));
			DONATION_CHANGE_SEX_COST = Integer.valueOf(propZ.getProperty("DONATION_SEX_CHANGE","0"));
			DONATION_AIO_CHAR_SIMPLE_COSTO = Integer.valueOf(propZ.getProperty("DONATION_AIO_NORMAL","0"));
			DONATION_AIO_CHAR_30_COSTO = Integer.valueOf(propZ.getProperty("DONATION_AIO_30","0"));
			DONATION_AIO_CHAR_LV_REQUEST = Integer.valueOf(propZ.getProperty("DONATION_MIN_LVL_REQUEST","0"));
			
			PREMIUM_CHAR = Boolean.valueOf(propZ.getProperty("PREMIUM_CHAR","false"));
			PREMIUM_CLAN = Boolean.valueOf(propZ.getProperty("PREMIUM_CLAN","false"));
			
			try{
				String ch_lv = propZ.getProperty("DONATION_CHARACTER_LEVEL","0,0");
				DONATION_CHARACTERS_LEVEL.clear();
				for(String p : ch_lv.split(";")){
					DONATION_CHARACTERS_LEVEL.put(Integer.valueOf(p.split(",")[0]),Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Character Level " + a.getMessage());
			}

			try{
				String c_repu = propZ.getProperty("DONATION_CLAN_REPUTATION","0,0");
				DONATION_CLAN_REPUTATION.clear();
				for(String p : c_repu.split(";")){
					DONATION_CLAN_REPUTATION.put(Integer.valueOf(p.split(",")[0]),Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Clan Reputation " + a.getMessage());
			}
			
			try{
				String c_skill = propZ.getProperty("DONATION_CLAN_SKILL","0,0");
				DONATION_CLAN_SKILL.clear();
				for(String p : c_skill.split(";")){
					DONATION_CLAN_SKILL.put(Integer.valueOf(p.split(",")[0]),Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Clan Skill " + a.getMessage());
			}
			
			try{
				String c_skill = propZ.getProperty("DONATION_CLAN_LEVEL","0,0");
				DONATION_CLAN_LEVEL.clear();
				for(String p : c_skill.split(";")){
					DONATION_CLAN_LEVEL.put(Integer.valueOf(p.split(",")[0]),Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Clan Level " + a.getMessage());
			}
			
			try{
				String c_fame = propZ.getProperty("DONATION_FAME_POINT","0,0");
				DONATION_CHARACTERS_FAME_POINT.clear();
				for(String p : c_fame.split(";")){
					DONATION_CHARACTERS_FAME_POINT.put(Integer.valueOf(p.split(",")[0]),Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Fame Point " + a.getMessage());
			}				
			
			try{
				String c_pk_point = propZ.getProperty("DONATION_PK_POINT","0,0");
				DONATION_CHARACTERS_PK_POINT.clear();
				for(String p : c_pk_point.split(";")){
					DONATION_CHARACTERS_PK_POINT.put(Integer.valueOf(p.split(",")[0]),Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Pk Point" + a.getMessage());
			}
			
			try{
				String enchantItem = propZ.getProperty("DONATION_ENCHANT_ITEM_ARMOR","0,0");
				DONATION_ENCHANT_ITEM_ARMOR.clear();
				for(String p : enchantItem.split(";")){
					DONATION_ENCHANT_ITEM_ARMOR.put(Integer.valueOf(p.split(",")[0]), Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Enchant Item" + a.getMessage());
			}
			
			try{
				String enchantItemW = propZ.getProperty("DONATION_ENCHANT_ITEM_WEAPON","0,0");
				DONATION_ENCHANT_ITEM_WEAPON.clear();
				for(String p : enchantItemW.split(";")){
					DONATION_ENCHANT_ITEM_WEAPON.put(Integer.valueOf(p.split(",")[0]), Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Enchant Item" + a.getMessage());
			}			

			try{
				String enchantItem_E_A = propZ.getProperty("DONATION_ELEMENTAL_ITEM_ARMOR","0,0");
				DONATION_ELEMENTAL_ITEM_ARMOR.clear();
				for(String p : enchantItem_E_A.split(";")){
					DONATION_ELEMENTAL_ITEM_ARMOR.put(Integer.valueOf(p.split(",")[0]), Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Elemental Item Armor" + a.getMessage());
			}
			
			try{
				String enchantItem_E_W = propZ.getProperty("DONATION_ELEMENTAL_ITEM_WEAPON","0,0");
				DONATION_ELEMENTAL_ITEM_WEAPON.clear();
				for(String p : enchantItem_E_W.split(";")){
					DONATION_ELEMENTAL_ITEM_WEAPON.put(Integer.valueOf(p.split(",")[0]), Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Elemental Item Armor" + a.getMessage());
			}
			
			
			String itemCharPremium = String.valueOf(propZ.getProperty("PREMIUM_ITEM_DROP_CHAR"));
			if(itemCharPremium!=null){
				if(itemCharPremium.length()>0){
					for(String itemId : itemCharPremium.split(";")){
						String itemChance[] = itemId.split(":");
						if(opera.isNumeric(itemChance[0])){
							try{
								PREMIUM_ITEM_CHAR_DROP_LIST.put(Integer.valueOf(itemChance[0]), Float.parseFloat(itemChance[1]));
							}catch(Exception a ){
								_log.warning("ZeuS-> Error load Premium Char Item = " + itemId);
							}
						}
					}
				}
			}
			
			String itemClanPremium = String.valueOf(propZ.getProperty("PREMIUM_ITEM_DROP_CLAN"));
			if(itemClanPremium!=null){
				if(itemClanPremium.length()>0){
					for(String itemId : itemCharPremium.split(";")){
						String itemChance[] = itemId.split(":");
						if(opera.isNumeric(itemChance[0])){
							try{
								PREMIUM_ITEM_CLAN_DROP_LIST.put(Integer.valueOf(itemChance[0]), Float.parseFloat(itemChance[1]));
							}catch(Exception a ){
								_log.warning("ZeuS-> Error load Premium Clan Item = " + itemId);
							}
						}
					}
				}
			}
			
			DONATION_EASY_NOTIFICATION = propZ.getProperty("DONATION_EASY_NOTIFICATION","");
			
			DONATION_TYPE_LIST = propZ.getProperty("DONATION_TYPE_LIST","");
			
			
			
			
			return true;
		}catch(Exception a){
			_log.warning(":::::::: ERROR AL CARGAR Donation Info ::::::::::");
			_log.warning(a.getMessage());
			return false;
		}
	}

	public static boolean banIPInterner(String ip){
		return IPBAN_INTERNET.contains(ip);
	}

	public static boolean banIPMaquina(String ip){
		return IPBAN_MAQUINA.contains(ip);
	}

	public static Vector<String> getBANIPINTERNET(){
		return IPBAN_INTERNET;
	}

	public static Vector<String> getBANIPMAQUINA(){
		return IPBAN_MAQUINA;
	}

	protected static void loadIPBLOCK(){
		IPBAN_INTERNET.clear();
		IPBAN_MAQUINA.clear();

		Connection conn = null;
		String qry = "";
		PreparedStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			qry = "SELECT * FROM zeus_ipblock";
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			while(rss.next()){
				if(!banIPInterner(rss.getString("ipWAN"))) {
					IPBAN_INTERNET.add(rss.getString("ipWAN"));
				}

				if(!banIPMaquina(rss.getString("ipRED"))) {
					IPBAN_MAQUINA.add(rss.getString("ipRED"));
				}
			}
		}catch(SQLException a){
			_log.warning("ipblock Load Error->"+a.getMessage());
		}

		try{
			conn.close();
		}catch(Exception a){
			
		}

	}

	protected static void loadPreguntasBot(){
		PREGUNTAS_BOT = null;
		PREGUNTAS_BOT = new String[800][3];
		Connection conn = null;
		String qry = "";
		PreparedStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			qry = "SELECT * FROM zeus_antibot";
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			int Contador = 0;
			while(rss.next()){
				PREGUNTAS_BOT_CANT = Contador;
				PREGUNTAS_BOT[Contador][0] = String.valueOf(rss.getInt("id"));
				PREGUNTAS_BOT[Contador][1] = rss.getString("ask");
				PREGUNTAS_BOT[Contador][2] = rss.getString("answer");
				Contador++;
			}
		}catch(SQLException a){
			_log.warning("AntiBot Question Load Error->"+a.getMessage());
		}

		try{
			conn.close();
		}catch(Exception a){
			
		}
	}
	
	public static enum shopData{
		ID,NOM,DESCRIP,IMA,TIPO,ID_ARCHI,ID_SEC,POS,ID_ITEMSHOW
	}
	
	protected static void loadShopData(){
		SHOP_DATA.clear();
		Connection conn = null;
		String qry = "";
		PreparedStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			qry = "SELECT * FROM zeus_shop ORDER BY zeus_shop.idsec, zeus_shop.pos ASC";
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			while(rss.next()){
				SHOP_DATA.put(rss.getInt("id"), new HashMap<String,String>());
				SHOP_DATA.get(rss.getInt("id")).put(shopData.ID.name() , String.valueOf(rss.getInt("id")));
				SHOP_DATA.get(rss.getInt("id")).put(shopData.NOM.name() , rss.getString("nom"));
				SHOP_DATA.get(rss.getInt("id")).put(shopData.DESCRIP.name() , rss.getString("descrip"));
				SHOP_DATA.get(rss.getInt("id")).put(shopData.IMA.name() , rss.getString("ima"));
				SHOP_DATA.get(rss.getInt("id")).put(shopData.TIPO.name() , rss.getString("tip"));
				SHOP_DATA.get(rss.getInt("id")).put(shopData.ID_ARCHI.name() , rss.getString("idarch"));
				SHOP_DATA.get(rss.getInt("id")).put(shopData.ID_SEC.name() , String.valueOf(rss.getInt("idsec")));
				SHOP_DATA.get(rss.getInt("id")).put(shopData.POS.name() , String.valueOf(rss.getInt("pos")));
				SHOP_DATA.get(rss.getInt("id")).put(shopData.ID_ITEMSHOW.name() , String.valueOf(rss.getInt("idItemShow")));
			}
		}catch(SQLException a){
			_log.warning("Shop Load Error->"+a.getMessage());
		}

		try{
			conn.close();
		}catch(Exception a){
			
		}		
	}


	protected static void loadShopMain(){
		loadShopData();/*
		SHOP_MAIN = null;
		SHOP_MAIN = new String[800][15];
		Connection conn = null;
		String qry = "";
		PreparedStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			qry = "SELECT * FROM zeus_shop ORDER BY zeus_shop.idsec, zeus_shop.pos ASC";
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			int Contador = 0;
			while(rss.next()){
				SHOP_MAIN[Contador][0] = String.valueOf(rss.getInt("id"));
				SHOP_MAIN[Contador][1] = rss.getString("nom");
				SHOP_MAIN[Contador][2] = rss.getString("descrip");
				SHOP_MAIN[Contador][3] = rss.getString("ima");
				SHOP_MAIN[Contador][4] = rss.getString("tip");
				SHOP_MAIN[Contador][5] = rss.getString("idarch");
				SHOP_MAIN[Contador][6] = String.valueOf(rss.getInt("idsec"));
				SHOP_MAIN[Contador][7] = String.valueOf(rss.getInt("pos"));
				SHOP_MAIN[Contador][8] = String.valueOf(rss.getInt("idItemShow"));
				Contador++;
			}
		}catch(SQLException a){
			_log.warning("Shop Load Error->"+a.getMessage());
		}

		try{
			conn.close();
		}catch(Exception a){
			
		}*/
	}
	
	public enum teleportType{
		ID,
		NOM,
		DESCRIP,
		TIP,
		COORDE,
		ID_SECC,
		NOBLE_ONLY,
		USE_FLAG,
		USE_KARMA,
		LEVEL,
		POSI,
		USE_DUALBOX
	}
	
	private static void loadTeleportData(){
		TELEPORT_DATA.clear();
		Connection conn = null;
		String qry = "";
		PreparedStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			qry = "SELECT * FROM zeus_teleport ORDER BY zeus_teleport.idsec, zeus_teleport.pos ASC";
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			while(rss.next()){
				TELEPORT_DATA.put(rss.getInt("id"), new HashMap<String,String>());
				TELEPORT_DATA.get(rss.getInt("id")).put("ID", String.valueOf(rss.getInt("id")));//0
				TELEPORT_DATA.get(rss.getInt("id")).put("NOM", rss.getString("nom"));//1
				TELEPORT_DATA.get(rss.getInt("id")).put("DESCRIP", rss.getString("descrip"));//2
				TELEPORT_DATA.get(rss.getInt("id")).put("TIP", rss.getString("tip"));//3
				TELEPORT_DATA.get(rss.getInt("id")).put("COORDE", String.valueOf(rss.getInt("x") + " " + rss.getInt("y") + " " + rss.getInt("z")));//4
				TELEPORT_DATA.get(rss.getInt("id")).put("ID_SECC", rss.getString("idsec"));//5
				TELEPORT_DATA.get(rss.getInt("id")).put("NOBLE_ONLY", rss.getString("fornoble"));//6
				TELEPORT_DATA.get(rss.getInt("id")).put("USE_FLAG", rss.getString("cangoflag"));//7
				TELEPORT_DATA.get(rss.getInt("id")).put("USE_KARMA", rss.getString("cangoKarma"));//8
				TELEPORT_DATA.get(rss.getInt("id")).put("LEVEL", String.valueOf(rss.getInt("lvlup")));//9
				TELEPORT_DATA.get(rss.getInt("id")).put("POSI", String.valueOf(rss.getInt("pos")));//10
				TELEPORT_DATA.get(rss.getInt("id")).put("USE_DUALBOX", rss.getString("dualbox"));//11
			}
		}catch(SQLException a){
			_log.warning("Telepot Load error->" + a.getMessage());
		}

		try{
			conn.close();
		}catch(Exception a){
			
		}				
	}

	protected static void loadTeleportMain(){
		loadTeleportData();
		/*
		TELEPORT_MAIN = null;
		TELEPORT_MAIN = new String[500][15];
		Connection conn = null;
		String qry = "";
		PreparedStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			qry = "SELECT * FROM zeus_teleport ORDER BY zeus_teleport.idsec, zeus_teleport.pos ASC";
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			int Contador = 0;
			while(rss.next()){
				TELEPORT_MAIN[Contador][0] = String.valueOf(rss.getInt("id"));
				TELEPORT_MAIN[Contador][1] = rss.getString("nom");
				TELEPORT_MAIN[Contador][2] = rss.getString("descrip");
				TELEPORT_MAIN[Contador][3] = rss.getString("tip");
				TELEPORT_MAIN[Contador][4] = String.valueOf(rss.getInt("x") + " " + rss.getInt("y") + " " + rss.getInt("z"));
				TELEPORT_MAIN[Contador][5] = rss.getString("idsec");
				TELEPORT_MAIN[Contador][6] = rss.getString("forNoble");
				TELEPORT_MAIN[Contador][7] = rss.getString("cangoFlag");
				TELEPORT_MAIN[Contador][8] = rss.getString("cangoKarma");
				TELEPORT_MAIN[Contador][9] = String.valueOf(rss.getInt("lvlup"));
				TELEPORT_MAIN[Contador][10] = String.valueOf(rss.getInt("pos"));
				TELEPORT_MAIN[Contador][11] = rss.getString("dualbox");
				Contador++;
			}
		}catch(SQLException a){
			_log.warning("Telepot Load error->" + a.getMessage());
		}

		try{
			conn.close();
		}catch(Exception a){
			
		}*/
	}

	protected static boolean loadLenguaje(){
		try{
			Properties propZ = new Properties();
			propZ.load(new FileInputStream(new File(ZEUS_CONFIG_LEN)));
			msg.NECESITAS_SER_NOBLE = String.valueOf(propZ.getProperty("NECESITAS_SER_NOBLE"));
			msg.NECESITAS_SER_NOBLE_PARA_ESTA_OPERACION = String.valueOf(propZ.getProperty("NECESITAS_SER_NOBLE_PARA_ESTA_OPERACION"));
			msg.NECESITAS_TENER_UN_LEVEL_MAYOR_A_$level = String.valueOf(propZ.getProperty("NECESITAS_TENER_UN_LEVEL_MAYOR_A_$level"));
			msg.NECESITAS_SER_LVL_$level_PARA_ESTA_OPERACION = String.valueOf(propZ.getProperty("NECESITAS_SER_LVL_$level_PARA_ESTA_OPERACION"));
			msg.NECESITAS_TENER_UN_LEVEL_DE_CLAN_IGUAL_O_SUPERIOR_A_$level = String.valueOf(propZ.getProperty("NECESITAS_TENER_UN_LEVEL_DE_CLAN_IGUAL_O_SUPERIOR_A_$level"));
			msg.NECESITAS_ESTAR_EN_PARTY = String.valueOf(propZ.getProperty("NECESITAS_ESTAR_EN_PARTY"));
			msg.NO_TIENES_LOS_ITEM_NECESARIOS_PARA_ESTA_OPERACION = String.valueOf(propZ.getProperty("NO_TIENES_LOS_ITEM_NECESARIOS_PARA_ESTA_OPERACION"));
			msg.NO_PUEDES_VOTAR_EN_ESTE_MOMENTO = String.valueOf(propZ.getProperty("NO_PUEDES_VOTAR_EN_ESTE_MOMENTO"));
			msg.TU_VOTO_NO_FUE_CURSADO = String.valueOf(propZ.getProperty("TU_VOTO_NO_FUE_CURSADO"));
			msg.USTED_NO_PUEDE_INGRESAR_A_ESTA_SECCION = String.valueOf(propZ.getProperty("USTED_NO_PUEDE_INGRESAR_A_ESTA_SECCION"));
			msg.DEBES_INGREGAR_LOS_DATOS_SOLICITADOS = String.valueOf(propZ.getProperty("DEBES_INGREGAR_LOS_DATOS_SOLICITADOS"));
			msg.INGRESE_SOLO_NUMEROS = String.valueOf(propZ.getProperty("INGRESE_SOLO_NUMEROS"));
			msg.NO_DEJE_CAMPOS_VACIOS = String.valueOf(propZ.getProperty("NO_DEJE_CAMPOS_VACIOS"));
			msg.EL_NOMBRE_YA_EXISTS_COMO_AIO = String.valueOf(propZ.getProperty("EL_NOMBRE_YA_EXISTS_COMO_AIO"));
			msg.ESTE_PROCESO_PUEDE_DEMORAR_UNOS_SEGUNDOS = String.valueOf(propZ.getProperty("ESTE_PROCESO_PUEDE_DEMORAR_UNOS_SEGUNDOS"));
			msg.LA_CREACION_DEL_AIO_HA_COMENZADO = String.valueOf(propZ.getProperty("LA_CREACION_DEL_AIO_HA_COMENZADO"));
			msg.AIO_CREADO_CON_EXITO = String.valueOf(propZ.getProperty("AIO_CREADO_CON_EXITO"));
			msg.SOLO_LIDER_DE_CLAN_PUEDE_REALIZAR_ESTA_OPERACION = String.valueOf(propZ.getProperty("SOLO_LIDER_DE_CLAN_PUEDE_REALIZAR_ESTA_OPERACION"));
			msg.NO_PUEDES_USAR_MIS_SERVICIOS_ESTANDO_FLAG = String.valueOf(propZ.getProperty("NO_PUEDES_USAR_MIS_SERVICIOS_ESTANDO_FLAG"));
			msg.NO_PUEDES_USAR_MIS_SERVICIOS_ESTANDO_PK = String.valueOf(propZ.getProperty("NO_PUEDES_USAR_MIS_SERVICIOS_ESTANDO_PK"));
			msg.NO_PUEDES_USAR_MIS_SERVICIOS_ESTANDO_MUERTO = String.valueOf(propZ.getProperty("NO_PUEDES_USAR_MIS_SERVICIOS_ESTANDO_MUERTO"));
			msg.BTN_ACTIVAR = String.valueOf(propZ.getProperty("BTN_ACTIVAR"));
			msg.BTN_DESACTIVAR = String.valueOf(propZ.getProperty("BTN_DESACTIVAR"));
			msg.USTED_YA_ES_NOBLE_NO_PUEDE_CONTINUAR = String.valueOf(propZ.getProperty("USTED_YA_ES_NOBLE_NO_PUEDE_CONTINUAR"));
			msg.USTED_YA_ES_85_NO_PUEDE_CONTINUAR = String.valueOf(propZ.getProperty("USTED_YA_ES_85_NO_PUEDE_CONTINUAR"));
			msg.NOMBRE_INGRESADO_NO_ES_VALIDO = String.valueOf(propZ.getProperty("NOMBRE_INGRESADO_NO_ES_VALIDO"));
			msg.CAMBIO_DE_NOMBRE_EXITOSO = String.valueOf(propZ.getProperty("CAMBIO_DE_NOMBRE_EXITOSO"));
			msg.ERROR_EN_ESTE_PROCESO = String.valueOf(propZ.getProperty("ERROR_EN_ESTE_PROCESO"));
			msg.ERROR_TIPEO = String.valueOf(propZ.getProperty("ERROR_TIPEO"));
			msg.MENSAJE_BIENVENIDA = String.valueOf(propZ.getProperty("MENSAJE_BIENVENIDA"));
			msg.PROCESO_DE_ELIMINACION_DE_CLAN_CANCELADA = String.valueOf(propZ.getProperty("PROCESO_DE_ELIMINACION_DE_CLAN_CANCELADA"));
			msg.INGRESE_NOMBRE_DEL_MIEMBRO_A_POSEER_EL_CLAN = String.valueOf(propZ.getProperty("INGRESE_NOMBRE_DEL_MIEMBRO_A_POSEER_EL_CLAN"));
			msg.NO_HAY_SKILL_PARA_APRENDER = String.valueOf(propZ.getProperty("NO_HAY_SKILL_PARA_APRENDER"));
			msg.DEBE_INRGESAR_EL_NOMBRE_DE_LA_ALIANZA = String.valueOf(propZ.getProperty("DEBE_INRGESAR_EL_NOMBRE_DE_LA_ALIANZA"));
			msg.NO_TIENES_CLAN = String.valueOf(propZ.getProperty("NO_TIENES_CLAN"));
			msg.ENVIANDO_COMPROBANTE_AL_ADMIN  = String.valueOf(propZ.getProperty("ENVIANDO_COMPROBANTE_AL_ADMIN"));
			msg.ENVIANDO_COMPROBANTE_OK = String.valueOf(propZ.getProperty("ENVIANDO_COMPROBANTE_OK"));
			msg.NOMBRE_INCORRECTO = String.valueOf(propZ.getProperty("NOMBRE_INCORRECTO"));
			msg.DESAFIO_GANO = String.valueOf(propZ.getProperty("DESAFIO_GANO"));
			msg.DESAFIO_PERDIO = String.valueOf(propZ.getProperty("DESAFIO_PERDIO"));
			msg.ITEM_ID_NO_ENCONTRADO = String.valueOf(propZ.getProperty("ITEM_ID_NO_ENCONTRADO"));
			msg.EL_TARGET_NO_ES_PLAYER = String.valueOf(propZ.getProperty("EL_TARGET_NO_ES_PLAYER"));
			msg.EL_PLAYER_$player_ES_NOBLE = String.valueOf(propZ.getProperty("EL_PLAYER_$player_ES_NOBLE"));
			msg.EL_PLAYER_$player_YA_NO_ES_NOBLE = String.valueOf(propZ.getProperty("EL_PLAYER_$player_YA_NO_ES_NOBLE"));
			msg.TU_ERES_NOBLE = String.valueOf(propZ.getProperty("TU_ERES_NOBLE"));
			msg.TU_NO_ERES_NOBLE = String.valueOf(propZ.getProperty("TU_NO_ERES_NOBLE"));
			msg.GM_RESET_PIN = String.valueOf(propZ.getProperty("GM_RESET_PIN"));
			msg.BOTON_ATRAS = String.valueOf(propZ.getProperty("BOTON_ATRAS"));
			msg.BOTON_SIGUENTE = String.valueOf(propZ.getProperty("BOTON_SIGUENTE"));
			msg.QUE_ES_AIO_BUFFER = String.valueOf(propZ.getProperty("QUE_ES_AIO_BUFFER"));
			msg.REQUERIMIENTOS_BUFFER = String.valueOf(propZ.getProperty("REQUERIMIENTOS_BUFFER"));
			msg.BUG_REPORT_MENSAJE_$player = String.valueOf(propZ.getProperty("BUG_REPORT_MENSAJE_$player"));
			msg.BUG_REPORT_LISTA_REPORTES = String.valueOf(propZ.getProperty("BUG_REPORT_LISTA_REPORTES"));
			msg.BUG_REPORT_MENSAJE_INICIAL = String.valueOf(propZ.getProperty("BUG_REPORT_MENSAJE_INICIAL"));
			msg.CHALLENGE_EL_$player_YA_ES_85_POSICION_$posi_DE_$total = String.valueOf(propZ.getProperty("CHALLENGE_EL_$player_YA_ES_85_POSICION_$posi_DE_$total"));
			msg.CHALLENGE_EL_$player_YA_ES_NOBLE_POSICION_$posi_DE_$total = String.valueOf(propZ.getProperty("CHALLENGE_EL_$player_YA_ES_NOBLE_POSICION_$posi_DE_$total"));
			msg.CHALLENGE_MENSAJE_85_$total85 = String.valueOf(propZ.getProperty("CHALLENGE_MENSAJE_85_$total85"));
			msg.CHALLENGE_MENSAJE_NOBLE_$totalnoble = String.valueOf(propZ.getProperty("CHALLENGE_MENSAJE_NOBLE_$totalnoble"));
			msg.CHALLENGE_MENSAJE_NPC_ESCONDIDOS_$total = String.valueOf(propZ.getProperty("CHALLENGE_MENSAJE_NPC_ESCONDIDOS_$total"));
			msg.CHALLENGE_MENSAJE_NPC_HIDDEN = String.valueOf(propZ.getProperty("CHALLENGE_MENSAJE_NPC_HIDDEN"));
			msg.CHALLENGE_MENSAJE_NPC_HIDDEN_GANADOR = String.valueOf(propZ.getProperty("CHALLENGE_MENSAJE_NPC_HIDDEN_GANADOR"));
			msg.CHALLENGE_MENSAJE_NPC_HIDDEN_ANNOUCEMENT_$player = String.valueOf(propZ.getProperty("CHALLENGE_MENSAJE_NPC_HIDDEN_ANNOUCEMENT_$player"));
			msg.CHALLENGE_MENSAJE_NPC_HIDDEN_ENCONTRADO_NPC_$player = String.valueOf(propZ.getProperty("CHALLENGE_MENSAJE_NPC_HIDDEN_ENCONTRADO_NPC_$player"));
			msg.CHALLENGE_MENSAJE_NPC_HIDDEN_ENCONTRADO_PLAYER = String.valueOf(propZ.getProperty("CHALLENGE_MENSAJE_NPC_HIDDEN_ENCONTRADO_PLAYER"));
			msg.CHALLENGE_MENSAJE_NPC_HIDDEN_ENCONTRADO_REWARD_IT = String.valueOf(propZ.getProperty("CHALLENGE_MENSAJE_NPC_HIDDEN_ENCONTRADO_REWARD_IT"));
			msg.CLAN_MENSAJE_INCREMENTAR = String.valueOf(propZ.getProperty("CLAN_MENSAJE_INCREMENTAR"));
			msg.CLAN_MENSAJE_ELIMINAR = String.valueOf(propZ.getProperty("CLAN_MENSAJE_ELIMINAR"));
			msg.CLAN_MENSAJE_RESTORE = String.valueOf(propZ.getProperty("CLAN_MENSAJE_RESTORE"));
			msg.CLAN_CHANGE_CLAN_LEADER = String.valueOf(propZ.getProperty("CLAN_CHANGE_CLAN_LEADER"));
			msg.CLAN_NOMBRE_CLAN_CREAR = String.valueOf(propZ.getProperty("CLAN_NOMBRE_CLAN_CREAR"));
			msg.CLAN_CREAR_ALIANZA = String.valueOf(propZ.getProperty("CLAN_CREAR_ALIANZA"));
			msg.CLAN_NAME_$name_YA_EXISTE = String.valueOf(propZ.getProperty("CLAN_NAME_$name_YA_EXISTE"));
			msg.CLAN_NAME_CAMBIADO_CORRECTO_A_$name = String.valueOf(propZ.getProperty("CLAN_NAME_CAMBIADO_CORRECTO_A_$name"));
			msg.CLASS_MASTER_FELICIDADES_$profesion = String.valueOf(propZ.getProperty("CLASS_MASTER_FELICIDADES_$profesion"));
			msg.CLASS_MASTER_MENSAJE_ELECCION = String.valueOf(propZ.getProperty("CLASS_MASTER_MENSAJE_ELECCION"));
			msg.COLOR_MANAGER_MENSAJE = String.valueOf(propZ.getProperty("COLOR_MANAGER_MENSAJE"));
			msg.DELEVEL_MANAGER_MENSAJE_HASTA_$level = String.valueOf(propZ.getProperty("DELEVEL_MANAGER_MENSAJE_HASTA_$level"));
			msg.DROP_SEARCH_MENSAJE_ESPACIO = String.valueOf(propZ.getProperty("DROP_SEARCH_MENSAJE_ESPACIO"));
			msg.DROP_SEARCH_MENSAJE = String.valueOf(propZ.getProperty("DROP_SEARCH_MENSAJE"));
			msg.FLAG_FINDER_MENSAJE = String.valueOf(propZ.getProperty("FLAG_FINDER_MENSAJE"));
			msg.FLAG_FINDER_BTN_MENSAJE = String.valueOf(propZ.getProperty("FLAG_FINDER_BTN_MENSAJE"));
			msg.FLAG_FINDER_MENSAJE_NO_PVP = String.valueOf(propZ.getProperty("FLAG_FINDER_MENSAJE_NO_PVP"));
			msg.FLAG_FINDER_ENCONTRADO_$player = String.valueOf(propZ.getProperty("FLAG_FINDER_ENCONTRADO_$player"));
			msg.FLAG_FINDER_VIENEN_POR_TI_$player = String.valueOf(propZ.getProperty("FLAG_FINDER_VIENEN_POR_TI_$player"));
			msg.CONFIG_PANEL_ANUN_ENRADA = String.valueOf(propZ.getProperty("CONFIG_PANEL_ANUN_ENRADA"));
			msg.CONFIG_PANEL_EFECTOS_TOP_PVPPK = String.valueOf(propZ.getProperty("CONFIG_PANEL_EFECTOS_TOP_PVPPK"));
			msg.CONFIG_PANEL_STAT = String.valueOf(propZ.getProperty("CONFIG_PANEL_STAT"));
			msg.CONFIG_PANEL_EXPICA_STAT = String.valueOf(propZ.getProperty("CONFIG_PANEL_EXPICA_STAT"));
			msg.CONFIG_PANEL_EXPLICA_PIN = String.valueOf(propZ.getProperty("CONFIG_PANEL_EXPLICA_PIN"));
			msg.LOGPVP_MENSAJE = String.valueOf(propZ.getProperty("LOGPVP_MENSAJE"));
			msg.LOGPVP_INGRESE_NOMBRE_O_PARCIAL = String.valueOf(propZ.getProperty("LOGPVP_INGRESE_NOMBRE_O_PARCIAL"));
			msg.PARTY_FINDER_MENSAJE_CENTRAL = String.valueOf(propZ.getProperty("PARTY_FINDER_MENSAJE_CENTRAL"));
			msg.PARTY_FINDER_NO_PARTY_LEADER_DEATH = String.valueOf(propZ.getProperty("PARTY_FINDER_NO_PARTY_LEADER_DEATH"));
			msg.PARTY_FINDER_NO_PARTY_LEADER_FLAG = String.valueOf(propZ.getProperty("PARTY_FINDER_NO_PARTY_LEADER_FLAG"));
			msg.PARTY_FINDER_NO_PARTY_LEADER_NOBLE = String.valueOf(propZ.getProperty("PARTY_FINDER_NO_PARTY_LEADER_NOBLE"));
			msg.PARTY_FINDER_NO_PARTY_LEADER_ISIN_INSTANCE = String.valueOf(propZ.getProperty("PARTY_FINDER_NO_PARTY_LEADER_ISIN_INSTANCE"));
			msg.PARTY_FINDER_NO_PARTY_LEADER_NO_SUMMON_ZONE = String.valueOf(propZ.getProperty("PARTY_FINDER_NO_PARTY_LEADER_NO_SUMMON_ZONE"));
			msg.PIN_ONLY_NUMERIC = String.valueOf(propZ.getProperty("PIN_ONLY_NUMERIC"));
			msg.PIN_LENGTH_4 = String.valueOf(propZ.getProperty("PIN_LENGTH_4"));
			msg.PIN_NO_COINCIDEN = String.valueOf(propZ.getProperty("PIN_NO_COINCIDEN"));
			msg.AUGMENT_SPECIAL_REMOVE_AUGMENT = String.valueOf(propZ.getProperty("AUGMENT_SPECIAL_REMOVE_AUGMENT"));
			msg.VOTEREWARD_REMEMBER_VOTE_12 = String.valueOf(propZ.getProperty("VOTEREWARD_REMEMBER_VOTE_12"));
			msg.VOTEREWARD_MENSAJE = String.valueOf(propZ.getProperty("VOTEREWARD_MENSAJE"));
			msg.VOTEREWARD_AGOTASTE_EL_TIEMPO_$time = String.valueOf(propZ.getProperty("VOTEREWARD_AGOTASTE_EL_TIEMPO_$time"));
			msg.VOTEREWARD_MENSAJE_ESPERAR = String.valueOf(propZ.getProperty("VOTEREWARD_MENSAJE_ESPERAR"));
			msg.VOTEREWARD_MENSAJE_ERROR = String.valueOf(propZ.getProperty("VOTEREWARD_MENSAJE_ERROR"));
			msg.VOTOREWARD_MENSAJE_ENGAÑO = String.valueOf(propZ.getProperty("VOTOREWARD_MENSAJE_ENGAÑO"));
			msg.VOTOREWARD_ESPERE_UN_MOMENTO_OBTENIENDO_INFO_$pagina = String.valueOf(propZ.getProperty("VOTOREWARD_ESPERE_UN_MOMENTO_OBTENIENDO_INFO_$pagina"));
			msg.VOTOREWARD_TENEMOS_$votos_EN_$pagina_VOTA_AHORA_MENSAJE = String.valueOf(propZ.getProperty("VOTOREWARD_TENEMOS_$votos_EN_$pagina_VOTA_AHORA_MENSAJE"));

			BTN_SHOW_EXPLICA_VOTE =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_VOTE"));
			BTN_SHOW_EXPLICA_BUFFER =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_BUFFER"));
			BTN_SHOW_EXPLICA_TELEPORT =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_TELEPORT"));
			BTN_SHOW_EXPLICA_SHOP =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_SHOP"));
			BTN_SHOW_EXPLICA_WAREHOUSE =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_WAREHOUSE"));
			BTN_SHOW_EXPLICA_AUGMENT =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_AUGMENT"));
			BTN_SHOW_EXPLICA_SUBCLASES =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_SUBCLASES"));
			BTN_SHOW_EXPLICA_CLASS_TRANSFER =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_CLASS_TRANSFER"));
			BTN_SHOW_EXPLICA_CONFIG_PANEL =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_CONFIG_PANEL"));
			BTN_SHOW_EXPLICA_DROP_SEARCH =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_DROP_SEARCH"));
			BTN_SHOW_EXPLICA_PVPPK_LIST =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_PVPPK_LIST"));
			BTN_SHOW_EXPLICA_LOG_PELEAS =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_LOG_PELEAS"));
			BTN_SHOW_EXPLICA_CASTLE_MANAGER =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_CASTLE_MANAGER"));
			BTN_SHOW_EXPLICA_DESAFIO =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_DESAFIO"));
			BTN_SHOW_EXPLICA_SYMBOL_MARKET =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_SYMBOL_MARKET"));
			BTN_SHOW_EXPLICA_CLANALLY =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_CLANALLY"));
			BTN_SHOW_EXPLICA_PARTYFINDER =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_PARTYFINDER"));
			BTN_SHOW_EXPLICA_FLAGFINDER =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_FLAGFINDER"));
			BTN_SHOW_EXPLICA_COLORNAME =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_COLORNAME"));
			BTN_SHOW_EXPLICA_DELEVEL =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_DELEVEL"));
			BTN_SHOW_EXPLICA_REMOVE_ATRIBUTE =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_REMOVE_ATRIBUTE"));
			BTN_SHOW_EXPLICA_BUG_REPORT =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_BUG_REPORT"));
			BTN_SHOW_EXPLICA_DONATION =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_DONATION"));
			BTN_SHOW_EXPLICA_CAMBIO_NOMBRE_PJ =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_CAMBIO_NOMBRE_PJ"));
			BTN_SHOW_EXPLICA_CAMBIO_NOMBRE_CLAN =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_CAMBIO_NOMBRE_CLAN"));
			BTN_SHOW_EXPLICA_VARIAS_OPCIONES =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_VARIAS_OPCIONES"));
			BTN_SHOW_EXPLICA_ELEMENT_ENHANCED =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_ELEMENT_ENHANCED"));
			BTN_SHOW_EXPLICA_ENCANTAMIENTO_ITEM =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_ENCANTAMIENTO_ITEM"));
			BTN_SHOW_EXPLICA_AUGMENT_SPECIAL =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_AUGMENT_SPECIAL"));
			BTN_SHOW_EXPLICA_GRAND_BOSS_STATUS =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_GRAND_BOSS_STATUS"));
			BTN_SHOW_EXPLICA_RAIDBOSS_INFO =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_RAIDBOSS_INFO"));
			BTN_SHOW_EXPLICA_TRANSFORMATION =  String.valueOf(propZ.getProperty("BOTON_EXPLICA_TRANSFORMATION"));

			msg.TU_NO_PUEDES_CAMBIARTE_EL_TITULO_EN_ESTE_MOMENTO = String.valueOf(propZ.getProperty("TU_NO_PUEDES_CAMBIARTE_EL_TITULO_EN_ESTE_MOMENTO"));
			msg.BOT_VERIFICATION_SEND_TO_$player = String.valueOf(propZ.getProperty("BOT_VERIFICATION_SEND_TO_$player"));
			msg.THE_SELECT_OBJECT_IS_NOT_A_PLAYER = String.valueOf(propZ.getProperty("THE_SELECT_OBJECT_IS_NOT_A_PLAYER"));
			msg.BOT_CAN_NOT_SEND_IN_OLY = String.valueOf(propZ.getProperty("BOT_CAN_NOT_SEND_IN_OLY"));
			msg.BOT_CAN_NOT_SEND_IN_YOUR_SELF = String.valueOf(propZ.getProperty("BOT_CAN_NOT_SEND_IN_YOUR_SELF"));
			msg.BOT_RECENTLY_BEEN_SENT_TO_THIS_PLAYER_VERIFICATION_EVERY_$timeEvery_NEXT_CHECK_IN_$timeNextCheck = String.valueOf(propZ.getProperty("BOT_RECENTLY_BEEN_SENT_TO_THIS_PLAYER_VERIFICATION_EVERY_$time_NEXT_CHECK_IN_$timeNextCheck"));
			msg.BOT_THIS_COMMAND_ONLY_NOBLE = String.valueOf(propZ.getProperty("BOT_THIS_COMMAND_ONLY_NOBLE"));
			msg.BOT_THIS_COMMAND_ONLY_HERO = String.valueOf(propZ.getProperty("BOT_THIS_COMMAND_ONLY_HERO"));
			msg.BOT_COMMAND_IS_ONLY_FOR_PLAYER_WHITH_$level = String.valueOf(propZ.getProperty("BOT_COMMAND_IS_ONLY_FOR_PLAYER_WHITH_$level"));
			msg.BOT_COMMAND_IS_ONLY_WITH_LIFETIME_OVER_$lifetime = String.valueOf(propZ.getProperty("BOT_COMMAND_IS_ONLY_WITH_LIFETIME_OVER_$lifetime"));
			msg.BOT_THIS_PLAYER_ARE_KILLING_A_RAID_BOSS = String.valueOf(propZ.getProperty("BOT_THIS_PLAYER_ARE_KILLING_A_RAID_BOSS"));
			msg.BOT_THIS_PLAYER_HAVE_KARMA = String.valueOf(propZ.getProperty("BOT_THIS_PLAYER_HAVE_KARMA"));
			msg.BOT_SYSTEM_SEND_TO_JAIL_FOR_$time_MINUTES_FOR_LOGOUT = String.valueOf(propZ.getProperty("BOT_SYSTEM_SEND_TO_JAIL_FOR_$time_MINUTES_FOR_LOGOUT"));
			msg.BOT_THE_PLAYER_IS_NOT_A_BOT = String.valueOf(propZ.getProperty("BOT_THE_PLAYER_IS_NOT_A_BOT"));
			msg.BOT_$player_HAVE_SENT_YOU_A_ANTIBOT_VERIFICATION = String.valueOf(propZ.getProperty(""));
			msg.BOT_YOU_HAVE_BEEN_SEND_TO_JAIL_FOR_NOT_ENTER_THE_RIGHT_PASS_INTIME_$time = String.valueOf(propZ.getProperty("BOT_YOU_HAVE_BEEN_SEND_TO_JAIL_FOR_NOT_ENTER_THE_RIGHT_PASS_INTIME_$time"));
			msg.BOT_ANNOUCEMENT_WHEN_$player_IS_SEND_TO_JAIL_FOR_$time_MINUTER = String.valueOf(propZ.getProperty("BOT_ANNOUCEMENT_WHEN_$player_IS_SEND_TO_JAIL_FOR_$time_MINUTER"));
			msg.BOT_ANNOUCEMENT_REMOVE_ITEM = String.valueOf(propZ.getProperty("BOT_ANNOUCEMENT_REMOVE_ITEM"));

			/*2014-03*/
			msg.DRESSME_ONLY_NUMERIC_TO_SHOW_DRESSME = String.valueOf(propZ.getProperty("DRESSME_ONLY_NUMERIC_TO_SHOW_DRESSME"));
			msg.DRESSME_ONLY_HAVE_$maximo_TO_CHOOSE = String.valueOf(propZ.getProperty("DRESSME_ONLY_HAVE_$maximo_TO_CHOOSE"));
			msg.DRESSME_CHOOSE_WRONG = String.valueOf(propZ.getProperty("DRESSME_CHOOSE_WRONG"));
			msg.DRESSME_DISABLED = String.valueOf(propZ.getProperty("DRESSME_DISABLED"));
			msg.DRESSME_YOU_DONT_HAVE_DRESSME_CONFIG = String.valueOf(propZ.getProperty("DRESSME_YOU_DONT_HAVE_DRESSME_CONFIG"));
			msg.DRESSME_HOW_TO_USED_$dressmeSave_$dressmeChoose_$dressme_$dressmeCopy = String.valueOf(propZ.getProperty("DRESSME_HOW_TO_USED_$dressmeSave_$dressmeChoose_$dressme_$dressmeCopy"));
			msg.DRESSME_THE_COST_FOR_NEW_DRESSME = String.valueOf(propZ.getProperty("DRESSME_THE_COST_FOR_NEW_DRESSME"));
			msg.DRESSME_YOU_NEED_TO_HAVE_ANY_DRESSME_ADDED = String.valueOf(propZ.getProperty("DRESSME_YOU_NEED_TO_HAVE_ANY_DRESSME_ADDED"));
			msg.DRESSME_YOU_NEED_TO_PUT_ON_A_DRESSME = String.valueOf(propZ.getProperty("DRESSME_YOU_NEED_TO_PUT_ON_A_DRESSME"));
			msg.CANCEL_BUFF_RETURNED_IN_$timeSeconds = String.valueOf(propZ.getProperty("CANCEL_BUFF_RETURNED_IN_SECONDS"));
			msg.CANCEL_BUFF_YOUR_CANCEL_BUFF_HAVE_RETURNED = String.valueOf(propZ.getProperty("CANCEL_BUFF_YOUR_CANCEL_BUFF_HAVE_RETURNED"));
			msg.YOU_CAN_NOT_TRADE_WHILE_FLAG = String.valueOf(propZ.getProperty("YOU_CAN_NOT_TRADE_WHILE_FLAG"));
			msg.YOU_CAN_NOT_TRADE_WHILE_PK = String.valueOf(propZ.getProperty("YOU_CAN_NOT_TRADE_WHILE_PK"));
			msg.TELEPORT_YOU_CAN_NOT_USE_THE_TELEPORT_IN_COMBAT_MODE = String.valueOf(propZ.getProperty("TELEPORT_YOU_CAN_NOT_USE_THE_TELEPORT_IN_COMBAT_MODE"));
			msg.BUFFVOICE_YOU_CANNOT_USE_ME_IN_COMBAT_MODE = String.valueOf(propZ.getProperty("BUFFVOICE_YOU_CANNOT_USE_ME_IN_COMBAT_MODE"));
			msg.BUFFVOICE_YOU_CANNOT_USE_ME_IN_SIEGE_AND_TW = String.valueOf(propZ.getProperty("BUFFVOICE_YOU_CANNOT_USE_ME_IN_SIEGE_AND_TW"));
			msg.BUFFVOICE_YOU_DONT_HAVE_THE_DONATION_ITEM_TO_USE_THIS_BUFF = String.valueOf(propZ.getProperty("BUFFVOICE_YOU_DONT_HAVE_THE_DONATION_ITEM_TO_USE_THIS_BUFF"));
			msg.BUFFVOICE_YOU_CANNOT_USE_ME_IN_EVENT = String.valueOf(propZ.getProperty("BUFFVOICE_YOU_CANNOT_USE_ME_IN_EVENT"));
			msg.HELP_ZEUS_BUFFER = String.valueOf(propZ.getProperty("HELP_ZEUS_BUFFER"));
			msg.HELP_EXPONOFF = String.valueOf(propZ.getProperty("HELP_EXPONOFF"));
			msg.HELP_DRESSME = String.valueOf(propZ.getProperty("HELP_DRESSME"));
			msg.HELP_CHECKBOT = String.valueOf(propZ.getProperty("HELP_CHECKBOT"));
			msg.HELP_STAT = String.valueOf(propZ.getProperty("HELP_STAT"));
			msg.BOT_INACTIVE_X_THAN_$minutes = String.valueOf(propZ.getProperty("BOT_INACTIVE_X_THAN_$minutes"));
			msg.DRESSME_PART_FROM_YOU_TARGET_IS_NOW_ADDED_TO_YOU_DRESSME = String.valueOf(propZ.getProperty("DRESSME_PART_FROM_YOU_TARGET_IS_NOW_ADDED_TO_YOU_DRESSME"));
			msg.DRESSME_TARGET_SAVE_IT_INTO_DRESSME_ID_$id = String.valueOf(propZ.getProperty("DRESSME_TARGET_SAVE_IT_INTO_DRESSME_ID_$id"));
			msg.DRESSME_TARGET_COMMAND_IS_DISABLED_BY_GM = String.valueOf(propZ.getProperty("DRESSME_TARGET_COMMAND_IS_DISABLED_BY_GM"));
			msg.DRESSME_TARGET_THE_TARGET_PLAYER_$targetName_NOT_SHARE_THEY_DRESSME = String.valueOf(propZ.getProperty("DRESSME_TARGET_THE_TARGET_PLAYER_$targetName_NOT_SHARE_THEY_DRESSME"));
			msg.DRESSME_TARGET_THE_PLAYER_$name_WANTS_YOU_DRESSME = String.valueOf(propZ.getProperty("DRESSME_TARGET_THE_PLAYER_$name_WANTS_YOU_DRESSME"));
			msg.OLY_YOU_HAVE_BEEN_UNBANNED_FROM_OLY_BY_$gmName = String.valueOf(propZ.getProperty("OLY_YOU_HAVE_BEEN_UNBANNED_FROM_OLY_BY_$gmName"));
			msg.OLY_YOU_HAVE_BEEN_BANNED_FROM_OLY_BY_$gmName = String.valueOf(propZ.getProperty("OLY_YOU_HAVE_BEEN_BANNED_FROM_OLY_BY_$gmName"));
			msg.BUFFERCHAR_YOU_DONT_HAVE_DONATION_COIN = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_DONT_HAVE_DONATION_COIN"));
			msg.BUFFERCHAR_LENGTH_OF_THE_NAME_MUST_BE_LESS_THAN_$size = String.valueOf(propZ.getProperty("BUFFERCHAR_LENGTH_OF_THE_NAME_MUST_BE_LESS_THAN_$size"));
			msg.BUFFERCHAR_NAME_ALREADY_EXISTS_IN_YOU_SCHEMES = String.valueOf(propZ.getProperty("BUFFERCHAR_NAME_ALREADY_EXISTS_IN_YOU_SCHEMES"));
			msg.BUFFERCHAR_SCHEME_NAME_SAVED = String.valueOf(propZ.getProperty("BUFFERCHAR_SCHEME_NAME_SAVED"));
			msg.BUFFERCHAR_YOU_CAN_NOT_BUFF_YOU_PET = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_CAN_NOT_BUFF_YOU_PET"));
			msg.BUFFERCHAR_YOU_CAN_NOT_HEAL_YOU_PET = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_CAN_NOT_HEAL_YOU_PET"));
			msg.BUFFERCHAR_YOU_CAN_NOT_CANCEL_YOU_PET_BUFF = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_CAN_NOT_CANCEL_YOU_PET_BUFF"));
			msg.BUFFERCHAR_WROG_SCHEME_NAME = String.valueOf(propZ.getProperty("BUFFERCHAR_WROG_SCHEME_NAME"));
			msg.BUFFERCHAR_YOU_NOT_HAVE_PET = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_NOT_HAVE_PET"));
			msg.PARTY_FINDER_YOU_HAVE_BEEN_SENT_TO_YOUR_PARTY_LEADER = String.valueOf(propZ.getProperty("PARTY_FINDER_YOU_HAVE_BEEN_SENT_TO_YOUR_PARTY_LEADER"));
			msg.PARTY_FINDER_THE_PLAYER_$name_HAS_MOVE_TO_YOU_POSITION = String.valueOf(propZ.getProperty("PARTY_FINDER_THE_PLAYER_$name_HAS_MOVE_TO_YOU_POSITION"));
			msg.TELEPORT_THIS_AREA_DOES_NOT_ALLOW_DUAL_BOX = String.valueOf(propZ.getProperty("TELEPORT_THIS_AREA_DOES_NOT_ALLOW_DUAL_BOX"));
			/*2014-03*/
			/*2014-06*/
			/*2014-06*/
			/*2014-07*/
			msg.EL_PLAYER_$player_ES_HERO = String.valueOf(propZ.getProperty("EL_PLAYER_$player_ES_HERO"));
			msg.DONATION_YOU_HAVE_$donationCount_ON_YOU_ACCOUNT = String.valueOf(propZ.getProperty("DONATION_YOU_HAVE_$donationCount_ON_YOU_ACCOUNT"));
			msg.DONATION_GIVE_DC_BUTTON = String.valueOf(propZ.getProperty("DONATION_GIVE_DC_BUTTON"));
			msg.DELEVEL_REMOVE_INVALID_SKILL_$nameskill_$idlevel_$classname = String.valueOf(propZ.getProperty("DELEVEL_REMOVE_INVALID_SKILL_$nameskill_$idlevel_$classname"));
			msg.CHARPANEL_PANEL_TO_SET_VARIOUS_SETTING_FOR_YOUR_CHARACTER = String.valueOf(propZ.getProperty("CHARPANEL_PANEL_TO_SET_VARIOUS_SETTING_FOR_YOUR_CHARACTER"));
			msg.FIXME_SELECT_THE_CHAR_HOW_NEED_TO_BE_FIX_IT = String.valueOf(propZ.getProperty("FIXME_SELECT_THE_CHAR_HOW_NEED_TO_BE_FIX_IT"));
			msg.FIXME_EXPLAIN = String.valueOf(propZ.getProperty("FIXME_EXPLAIN"));
			msg.ACCOUNT_THE_EMAIL_HAS_BEEN_SUCCESFULLY_UPDATED = String.valueOf(propZ.getProperty("ACCOUNT_THE_EMAIL_HAS_BEEN_SUCCESFULLY_UPDATED"));
			msg.ACCOUNT_REGISTER_EXPLAIN_INPUT_EMAIL = String.valueOf(propZ.getProperty("ACCOUNT_REGISTER_EXPLAIN_INPUT_EMAIL"));
			msg.ACCOUNT_REGISTER_EXPLAIN_INPUT_EMAIL_CODE = String.valueOf(propZ.getProperty("ACCOUNT_REGISTER_EXPLAIN_INPUT_EMAIL_CODE"));
			msg.ACCOUNT_YOUR_ACCOUNT_IS_ALREADY_ASOCIATED_TO_AN_EMAIL_$email = String.valueOf(propZ.getProperty("ACCOUNT_YOUR_ACCOUNT_IS_ALREADY_ASOCIATED_TO_AN_EMAIL_$email"));
			msg.ACCOUNT_BUTTON_SEND_ME_A_VALIDATION_CODE = String.valueOf(propZ.getProperty("ACCOUNT_BUTTON_SEND_ME_A_VALIDATION_CODE"));
			msg.ACCOUNT_BUTTON_CHECK_BUTTON = propZ.getProperty("ACCOUNT_BUTTON_CHECK_BUTTON");
			msg.ACCOUNT_REGISTER_EXPLAIN_INPUT_EMAIL_AGAIN = propZ.getProperty("ACCOUNT_REGISTER_EXPLAIN_INPUT_EMAIL_AGAIN");
			
			
			msg.CB_BTN_ANNOUCEMENT = String.valueOf(propZ.getProperty("CB_BTN_ANNOUCEMENT"));
			msg.CB_BTN_CHANGE_LOG = String.valueOf(propZ.getProperty("CB_BTN_CHANGE_LOG"));
			msg.CB_BTN_RULES = String.valueOf(propZ.getProperty("CB_BTN_RULES"));
			msg.CB_BTN_STAFF = String.valueOf(propZ.getProperty("CB_BTN_STAFF"));
			msg.CB_BTN_TOP_PLAYER = String.valueOf(propZ.getProperty("CB_BTN_TOP_PLAYER"));
			msg.CB_BTN_HEROES = String.valueOf(propZ.getProperty("CB_BTN_HEROES"));
			msg.CB_BTN_CLAN = String.valueOf(propZ.getProperty("CB_BTN_CLAN"));
			msg.CB_BTN_CASTLE = String.valueOf(propZ.getProperty("CB_BTN_CASTLE"));
			msg.CB_BTN_PRIVATE_STORE = String.valueOf(propZ.getProperty("CB_BTN_PRIVATE_STORE"));
			msg.CB_BTN_SV_CONFIG = String.valueOf(propZ.getProperty("CB_BTN_SV_CONFIG"));
			msg.CB_BTN_FEATURES = String.valueOf(propZ.getProperty("CB_BTN_FEATURES"));
			msg.CB_BTN_EVENTS = String.valueOf(propZ.getProperty("CB_BTN_EVENTS"));
			msg.CB_BTN_PLAYGAME = String.valueOf(propZ.getProperty("CB_BTN_PLAYGAME"));
			msg.CHANGE_PASS_EXPLAIN = String.valueOf(propZ.getProperty("PASSWORD_RECOVERY_DESCRIPT"));
			/*2014-07*/
			/*2015*/
			msg.DONATION_WINDOWS_THANKS = String.valueOf(propZ.getProperty("DONATION_WINDOWS_THANKS"));
			msg.DONATION_WINDOWS_INFO = String.valueOf(propZ.getProperty("DONATION_WINDOWS_INFO"));
			/*2015*/
			
			/*2015 2º periodo*/
			
			BTN_SHOW_EXPLICA_VOTE_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_VOTE_CB"));
			BTN_SHOW_EXPLICA_BUFFER_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_BUFFER_CB"));
			BTN_SHOW_EXPLICA_TELEPORT_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_TELEPORT_CB"));
			BTN_SHOW_EXPLICA_SHOP_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_SHOP_CB"));
			BTN_SHOW_EXPLICA_WAREHOUSE_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_WAREHOUSE_CB"));
			BTN_SHOW_EXPLICA_AUGMENT_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_AUGMENT_CB"));
			BTN_SHOW_EXPLICA_SUBCLASES_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_SUBCLASES_CB"));
			BTN_SHOW_EXPLICA_CLASS_TRANSFER_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_CLASS_TRANSFER_CB"));
			BTN_SHOW_EXPLICA_CONFIG_PANEL_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_CONFIG_PANEL_CB"));
			BTN_SHOW_EXPLICA_DROP_SEARCH_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_DROP_SEARCH_CB"));
			BTN_SHOW_EXPLICA_PVPPK_LIST_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_PVPPK_LIST_CB"));
			BTN_SHOW_EXPLICA_LOG_PELEAS_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_LOG_PELEAS_CB"));
			BTN_SHOW_EXPLICA_CASTLE_MANAGER_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_CASTLE_MANAGER_CB"));
			BTN_SHOW_EXPLICA_DESAFIO_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_DESAFIO_CB"));
			BTN_SHOW_EXPLICA_SYMBOL_MARKET_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_SYMBOL_MARKET_CB"));
			BTN_SHOW_EXPLICA_CLANALLY_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_CLANALLY_CB"));
			BTN_SHOW_EXPLICA_PARTYFINDER_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_PARTYFINDER_CB"));
			BTN_SHOW_EXPLICA_FLAGFINDER_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_FLAGFINDER_CB"));
			BTN_SHOW_EXPLICA_COLORNAME_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_COLORNAME_CB"));
			BTN_SHOW_EXPLICA_DELEVEL_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_DELEVEL_CB"));
			BTN_SHOW_EXPLICA_REMOVE_ATRIBUTE_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_REMOVE_ATRIBUTE_CB"));
			BTN_SHOW_EXPLICA_BUG_REPORT_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_BUG_REPORT_CB"));
			BTN_SHOW_EXPLICA_DONATION_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_DONATION_CB"));
			BTN_SHOW_EXPLICA_CAMBIO_NOMBRE_PJ_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_CAMBIO_NOMBRE_PJ_CB"));
			BTN_SHOW_EXPLICA_CAMBIO_NOMBRE_CLAN_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_CAMBIO_NOMBRE_CLAN_CB"));
			BTN_SHOW_EXPLICA_VARIAS_OPCIONES_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_VARIAS_OPCIONES_CB"));
			BTN_SHOW_EXPLICA_ELEMENT_ENHANCED_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_ELEMENT_ENHANCED_CB"));
			BTN_SHOW_EXPLICA_ENCANTAMIENTO_ITEM_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_ENCANTAMIENTO_ITEM_CB"));
			BTN_SHOW_EXPLICA_AUGMENT_SPECIAL_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_AUGMENT_SPECIAL_CB"));
			BTN_SHOW_EXPLICA_GRAND_BOSS_STATUS_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_GRAND_BOSS_STATUS_CB"));
			BTN_SHOW_EXPLICA_RAIDBOSS_INFO_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_RAIDBOSS_INFO_CB"));
			BTN_SHOW_EXPLICA_TRANSFORMATION_CB = String.valueOf(propZ.getProperty("BOTON_EXPLICA_TRANSFORMATION_CB"));			
			BTN_SHOW_EXPLICA_PARTYMATCHING_CB = String.valueOf(propZ.getProperty("BTN_SHOW_EXPLICA_PARTYMATCHING_CB"));
			BTN_SHOW_EXPLICA_AUCTIONSHOUSE_CB = String.valueOf(propZ.getProperty("BTN_SHOW_EXPLICA_AUCTIONSHOUSE_CB"));
			
			/*2015 2º periodo*/
			
			
			return true;
		}catch(Exception a){
			_log.warning(":::::::: ERROR AL CARGAR LENGUAJE ::::::::::");
			_isValid=false;
			return false;
		}
	}

	public static HashMap<Integer, String> getClases(){
		return classList;
	}

	
	private static void getAllClases(){
		classList.put(0, "Fighter");
		classList.put(1, "Warrior");
		classList.put(2, "Gladiator");
		classList.put(3, "Warlord");
		classList.put(4, "Knight");
		classList.put(5, "Paladin");
		classList.put(6, "Dark Avenger");
		classList.put(7, "Rogue");
		classList.put(8, "Treasure Hunter");
		classList.put(9, "Hawkeye");
		classList.put(10, "Mage");
		classList.put(11, "Wizard");
		classList.put(12, "Sorcerer");
		classList.put(13, "Necromancer");
		classList.put(14, "Warlock");
		classList.put(15, "Cleric");
		classList.put(16, "Bishop");
		classList.put(17, "Prophet");
		classList.put(18, "Elven Fighter");
		classList.put(19, "Elven Knight");
		classList.put(20, "Temple Knight");
		classList.put(21, "Swordsinger");
		classList.put(22, "Elven Scout");
		classList.put(23, "Plains Walker");
		classList.put(24, "Silver Ranger");
		classList.put(25, "Elven Mage");
		classList.put(26, "Elven Wizard");
		classList.put(27, "Spellsinger");
		classList.put(28, "Elemental Summoner");
		classList.put(29, "Oracle");
		classList.put(30, "Elder");
		classList.put(31, "Dark Fighter");
		classList.put(32, "Palus Knightr");
		classList.put(33, "Shillien Knight");
		classList.put(34, "Bladedancer");
		classList.put(35, "Assasin");
		classList.put(36, "Abyss Walker");
		classList.put(37, "Phantom Ranger");
		classList.put(38, "Dark Mage");
		classList.put(39, "Dark Wizard");
		classList.put(40, "Spellhowler");
		classList.put(41, "Phantom Summoner");
		classList.put(42, "Shillien Oracle");
		classList.put(43, "Shilien Elder");
		classList.put(44, "Orc Fighter");
		classList.put(45, "Orc Raider");
		classList.put(46, "Destroyer");
		classList.put(47, "Orc Monk");
		classList.put(48, "Tyrant");
		classList.put(49, "Orc Mage");
		classList.put(50, "Orc Shaman");
		classList.put(51, "Overlord");
		classList.put(52, "Warcryer");
		classList.put(53, "Dwarven Fighter");
		classList.put(54, "Scavenger");
		classList.put(55, "Bounty Hunter");
		classList.put(56, "Artisan");
		classList.put(57, "Warsmith");
		classList.put(88, "Duelist");
		classList.put(89, "Dreadnought");
		classList.put(90, "Phoenix Knight");
		classList.put(91, "Hell Knight");
		classList.put(92, "Sagittarius");
		classList.put(93, "Adventurer");
		classList.put(94, "Archmage");
		classList.put(95, "Soultaker");
		classList.put(96, "Arcana Lord");
		classList.put(97, "Cardinal");
		classList.put(98, "Hierophant");
		classList.put(99, "Evas Templar");
		classList.put(100, "Sword Muse");
		classList.put(101, "Wind Rider");
		classList.put(102, "Moonlight Sentinel");
		classList.put(103, "Mystic Muse");
		classList.put(104, "Elemental Master");
		classList.put(105, "Evas Saint");
		classList.put(106, "Shillien Templar");
		classList.put(107, "Spectral Dancer");
		classList.put(108, "Ghost Hunter");
		classList.put(109, "Ghost Sentinel");
		classList.put(110, "Storm Screamer");
		classList.put(111, "Spectral Master");
		classList.put(112, "Shillien Saint");
		classList.put(113, "Titan");
		classList.put(114, "Grand Khavatari");
		classList.put(115, "Dominator");
		classList.put(116, "Doomcryer");
		classList.put(117, "Fortune Seeker");
		classList.put(118, "Maestro");
		classList.put(123, "Male Soldier");
		classList.put(124, "Female Soldier");
		classList.put(125, "Trooper");
		classList.put(126, "Warder");
		classList.put(127, "Berserker");
		classList.put(128, "Male Soulbreaker");
		classList.put(129, "Female Soulbreaker");
		classList.put(130, "Arbalester");
		classList.put(131, "Doombringer");
		classList.put(132, "Male Soulhound");
		classList.put(133, "Female Soulhound");
		classList.put(134, "Trickster");
		classList.put(135, "Inspector");
		classList.put(136, "Judicator");
		for(int i=0;i<=136;i++){
			classData.put(i, new HashMap<String,String>());	
		}
		
		classData.get(0).put("CLASSBASE", "Human");classData.get(0).put("IMAGEN", "icon.skillhuman");
		classData.get(1).put("CLASSBASE", "Human");classData.get(1).put("IMAGEN", "icon.skillhuman");
		classData.get(2).put("CLASSBASE", "Human");classData.get(2).put("IMAGEN", "icon.skillhuman");
		classData.get(3).put("CLASSBASE", "Human");classData.get(3).put("IMAGEN", "icon.skillhuman");
		classData.get(4).put("CLASSBASE", "Human");classData.get(4).put("IMAGEN", "icon.skillhuman");
		classData.get(5).put("CLASSBASE", "Human");classData.get(5).put("IMAGEN", "icon.skillhuman");
		classData.get(6).put("CLASSBASE", "Human");classData.get(6).put("IMAGEN", "icon.skillhuman");
		classData.get(7).put("CLASSBASE", "Human");classData.get(7).put("IMAGEN", "icon.skillhuman");
		classData.get(8).put("CLASSBASE", "Human");classData.get(8).put("IMAGEN", "icon.skillhuman");
		classData.get(9).put("CLASSBASE", "Human");classData.get(9).put("IMAGEN", "icon.skillhuman");
		classData.get(10).put("CLASSBASE", "Human");classData.get(10).put("IMAGEN", "icon.skillhuman");
		classData.get(11).put("CLASSBASE", "Human");classData.get(11).put("IMAGEN", "icon.skillhuman");
		classData.get(12).put("CLASSBASE", "Human");classData.get(12).put("IMAGEN", "icon.skillhuman");
		classData.get(13).put("CLASSBASE", "Human");classData.get(13).put("IMAGEN", "icon.skillhuman");
		classData.get(14).put("CLASSBASE", "Human");classData.get(14).put("IMAGEN", "icon.skillhuman");
		classData.get(15).put("CLASSBASE", "Human");classData.get(15).put("IMAGEN", "icon.skillhuman");
		classData.get(16).put("CLASSBASE", "Human");classData.get(16).put("IMAGEN", "icon.skillhuman");
		classData.get(17).put("CLASSBASE", "Human");classData.get(17).put("IMAGEN", "icon.skillhuman");
		classData.get(88).put("CLASSBASE", "Human");classData.get(88).put("IMAGEN", "icon.skillhuman");
		classData.get(89).put("CLASSBASE", "Human");classData.get(89).put("IMAGEN", "icon.skillhuman");
		classData.get(90).put("CLASSBASE", "Human");classData.get(90).put("IMAGEN", "icon.skillhuman");
		classData.get(91).put("CLASSBASE", "Human");classData.get(91).put("IMAGEN", "icon.skillhuman");
		classData.get(92).put("CLASSBASE", "Human");classData.get(92).put("IMAGEN", "icon.skillhuman");
		classData.get(93).put("CLASSBASE", "Human");classData.get(93).put("IMAGEN", "icon.skillhuman");
		classData.get(94).put("CLASSBASE", "Human");classData.get(94).put("IMAGEN", "icon.skillhuman");
		classData.get(95).put("CLASSBASE", "Human");classData.get(95).put("IMAGEN", "icon.skillhuman");
		classData.get(96).put("CLASSBASE", "Human");classData.get(96).put("IMAGEN", "icon.skillhuman");
		classData.get(97).put("CLASSBASE", "Human");classData.get(97).put("IMAGEN", "icon.skillhuman");
		classData.get(98).put("CLASSBASE", "Human");classData.get(98).put("IMAGEN", "icon.skillhuman");

		classData.get(18).put("CLASSBASE", "Elf");classData.get(18).put("IMAGEN", "icon.skillelf");
		classData.get(19).put("CLASSBASE", "Elf");classData.get(19).put("IMAGEN", "icon.skillelf");
		classData.get(20).put("CLASSBASE", "Elf");classData.get(20).put("IMAGEN", "icon.skillelf");
		classData.get(21).put("CLASSBASE", "Elf");classData.get(21).put("IMAGEN", "icon.skillelf");
		classData.get(22).put("CLASSBASE", "Elf");classData.get(22).put("IMAGEN", "icon.skillelf");
		classData.get(23).put("CLASSBASE", "Elf");classData.get(23).put("IMAGEN", "icon.skillelf");
		classData.get(24).put("CLASSBASE", "Elf");classData.get(24).put("IMAGEN", "icon.skillelf");
		classData.get(25).put("CLASSBASE", "Elf");classData.get(25).put("IMAGEN", "icon.skillelf");
		classData.get(26).put("CLASSBASE", "Elf");classData.get(26).put("IMAGEN", "icon.skillelf");
		classData.get(27).put("CLASSBASE", "Elf");classData.get(27).put("IMAGEN", "icon.skillelf");
		classData.get(28).put("CLASSBASE", "Elf");classData.get(28).put("IMAGEN", "icon.skillelf");
		classData.get(29).put("CLASSBASE", "Elf");classData.get(29).put("IMAGEN", "icon.skillelf");
		classData.get(30).put("CLASSBASE", "Elf");classData.get(30).put("IMAGEN", "icon.skillelf");
		classData.get(99).put("CLASSBASE", "Elf");classData.get(99).put("IMAGEN", "icon.skillelf");
		classData.get(100).put("CLASSBASE", "Elf");classData.get(100).put("IMAGEN", "icon.skillelf");
		classData.get(101).put("CLASSBASE", "Elf");classData.get(101).put("IMAGEN", "icon.skillelf");
		classData.get(102).put("CLASSBASE", "Elf");classData.get(102).put("IMAGEN", "icon.skillelf");
		classData.get(103).put("CLASSBASE", "Elf");classData.get(103).put("IMAGEN", "icon.skillelf");
		classData.get(104).put("CLASSBASE", "Elf");classData.get(104).put("IMAGEN", "icon.skillelf");
		classData.get(105).put("CLASSBASE", "Elf");classData.get(105).put("IMAGEN", "icon.skillelf");

		classData.get(31).put("CLASSBASE", "Dark Elf");classData.get(31).put("IMAGEN", "icon.skilldarkelf");
		classData.get(32).put("CLASSBASE", "Dark Elf");classData.get(32).put("IMAGEN", "icon.skilldarkelf");
		classData.get(33).put("CLASSBASE", "Dark Elf");classData.get(33).put("IMAGEN", "icon.skilldarkelf");
		classData.get(34).put("CLASSBASE", "Dark Elf");classData.get(34).put("IMAGEN", "icon.skilldarkelf");
		classData.get(35).put("CLASSBASE", "Dark Elf");classData.get(35).put("IMAGEN", "icon.skilldarkelf");
		classData.get(36).put("CLASSBASE", "Dark Elf");classData.get(36).put("IMAGEN", "icon.skilldarkelf");
		classData.get(37).put("CLASSBASE", "Dark Elf");classData.get(37).put("IMAGEN", "icon.skilldarkelf");
		classData.get(38).put("CLASSBASE", "Dark Elf");classData.get(38).put("IMAGEN", "icon.skilldarkelf");
		classData.get(39).put("CLASSBASE", "Dark Elf");classData.get(39).put("IMAGEN", "icon.skilldarkelf");
		classData.get(40).put("CLASSBASE", "Dark Elf");classData.get(40).put("IMAGEN", "icon.skilldarkelf");
		classData.get(41).put("CLASSBASE", "Dark Elf");classData.get(41).put("IMAGEN", "icon.skilldarkelf");
		classData.get(42).put("CLASSBASE", "Dark Elf");classData.get(42).put("IMAGEN", "icon.skilldarkelf");
		classData.get(43).put("CLASSBASE", "Dark Elf");classData.get(43).put("IMAGEN", "icon.skilldarkelf");
		classData.get(106).put("CLASSBASE", "Dark Elf");classData.get(106).put("IMAGEN", "icon.skilldarkelf");
		classData.get(107).put("CLASSBASE", "Dark Elf");classData.get(107).put("IMAGEN", "icon.skilldarkelf");
		classData.get(108).put("CLASSBASE", "Dark Elf");classData.get(108).put("IMAGEN", "icon.skilldarkelf");
		classData.get(109).put("CLASSBASE", "Dark Elf");classData.get(109).put("IMAGEN", "icon.skilldarkelf");
		classData.get(110).put("CLASSBASE", "Dark Elf");classData.get(110).put("IMAGEN", "icon.skilldarkelf");
		classData.get(111).put("CLASSBASE", "Dark Elf");classData.get(111).put("IMAGEN", "icon.skilldarkelf");
		classData.get(112).put("CLASSBASE", "Dark Elf");classData.get(112).put("IMAGEN", "icon.skilldarkelf");

		classData.get(44).put("CLASSBASE", "Orc");classData.get(44).put("IMAGEN", "icon.skillorc");
		classData.get(45).put("CLASSBASE", "Orc");classData.get(45).put("IMAGEN", "icon.skillorc");
		classData.get(46).put("CLASSBASE", "Orc");classData.get(46).put("IMAGEN", "icon.skillorc");
		classData.get(47).put("CLASSBASE", "Orc");classData.get(47).put("IMAGEN", "icon.skillorc");
		classData.get(48).put("CLASSBASE", "Orc");classData.get(48).put("IMAGEN", "icon.skillorc");
		classData.get(49).put("CLASSBASE", "Orc");classData.get(49).put("IMAGEN", "icon.skillorc");
		classData.get(50).put("CLASSBASE", "Orc");classData.get(50).put("IMAGEN", "icon.skillorc");
		classData.get(51).put("CLASSBASE", "Orc");classData.get(51).put("IMAGEN", "icon.skillorc");
		classData.get(52).put("CLASSBASE", "Orc");classData.get(52).put("IMAGEN", "icon.skillorc");
		classData.get(113).put("CLASSBASE", "Orc");classData.get(113).put("IMAGEN", "icon.skillorc");
		classData.get(114).put("CLASSBASE", "Orc");classData.get(114).put("IMAGEN", "icon.skillorc");
		classData.get(115).put("CLASSBASE", "Orc");classData.get(115).put("IMAGEN", "icon.skillorc");
		classData.get(116).put("CLASSBASE", "Orc");classData.get(116).put("IMAGEN", "icon.skillorc");
		
		
		classData.get(53).put("CLASSBASE", "Dwarf");classData.get(53).put("IMAGEN", "icon.skilldwarf");
		classData.get(54).put("CLASSBASE", "Dwarf");classData.get(54).put("IMAGEN", "icon.skilldwarf");
		classData.get(55).put("CLASSBASE", "Dwarf");classData.get(55).put("IMAGEN", "icon.skilldwarf");
		classData.get(56).put("CLASSBASE", "Dwarf");classData.get(56).put("IMAGEN", "icon.skilldwarf");
		classData.get(57).put("CLASSBASE", "Dwarf");classData.get(57).put("IMAGEN", "icon.skilldwarf");
		classData.get(117).put("CLASSBASE", "Dwarf");classData.get(117).put("IMAGEN", "icon.skilldwarf");
		classData.get(118).put("CLASSBASE", "Dwarf");classData.get(118).put("IMAGEN", "icon.skilldwarf");

		classData.get(123).put("CLASSBASE", "Kamael");classData.get(123).put("IMAGEN", "icon.skillkamael");
		classData.get(124).put("CLASSBASE", "Kamael");classData.get(124).put("IMAGEN", "icon.skillkamael");
		classData.get(125).put("CLASSBASE", "Kamael");classData.get(125).put("IMAGEN", "icon.skillkamael");
		classData.get(126).put("CLASSBASE", "Kamael");classData.get(126).put("IMAGEN", "icon.skillkamael");
		classData.get(127).put("CLASSBASE", "Kamael");classData.get(127).put("IMAGEN", "icon.skillkamael");
		classData.get(128).put("CLASSBASE", "Kamael");classData.get(128).put("IMAGEN", "icon.skillkamael");
		classData.get(129).put("CLASSBASE", "Kamael");classData.get(129).put("IMAGEN", "icon.skillkamael");
		classData.get(130).put("CLASSBASE", "Kamael");classData.get(130).put("IMAGEN", "icon.skillkamael");
		classData.get(131).put("CLASSBASE", "Kamael");classData.get(131).put("IMAGEN", "icon.skillkamael");
		classData.get(132).put("CLASSBASE", "Kamael");classData.get(132).put("IMAGEN", "icon.skillkamael");
		classData.get(133).put("CLASSBASE", "Kamael");classData.get(133).put("IMAGEN", "icon.skillkamael");
		classData.get(134).put("CLASSBASE", "Kamael");classData.get(134).put("IMAGEN", "icon.skillkamael");
		classData.get(135).put("CLASSBASE", "Kamael");classData.get(135).put("IMAGEN", "icon.skillkamael");
		classData.get(136).put("CLASSBASE", "Kamael");classData.get(136).put("IMAGEN", "icon.skillkamael");
		
		ClassUltimate.add(88); //Duelist
		ClassUltimate.add(89); //Dreadnought
		ClassUltimate.add(90); //Phoenix Knight
		ClassUltimate.add(91); //Hell Knight
		ClassUltimate.add(92); //Sagittarius
		ClassUltimate.add(93); //Adventurer
		ClassUltimate.add(94); //Archmage
		ClassUltimate.add(95); //Soultaker
		ClassUltimate.add(96); //Arcana Lord
		ClassUltimate.add(97); //Cardinal
		ClassUltimate.add(98); //Hierophant
		ClassUltimate.add(99); //Eva's Templar
		ClassUltimate.add(100); //Sword Muse
		ClassUltimate.add(101); //Wind Rider
		ClassUltimate.add(102); //Moonlight Sentinel
		ClassUltimate.add(103); //Mystic Muse
		ClassUltimate.add(104); //Elemental Master
		ClassUltimate.add(105); //Eva's Saint
		ClassUltimate.add(106); //Shilien Templar
		ClassUltimate.add(107); //Spectral Dancer
		ClassUltimate.add(108); //Ghost Hunter
		ClassUltimate.add(109); //Ghost Sentinel
		ClassUltimate.add(110); //Storm Screamer
		ClassUltimate.add(111); //Spectral Master
		ClassUltimate.add(112); //Shilien Saint
		ClassUltimate.add(113); //Titan
		ClassUltimate.add(114); //Grand Khavatari
		ClassUltimate.add(115); //Dominator
		ClassUltimate.add(116); //Doomcryer
		ClassUltimate.add(117); //Fortune Seeker
		ClassUltimate.add(118); //Maestro
		ClassUltimate.add(131); //Doombringer
		ClassUltimate.add(132); //Soul Hound
		ClassUltimate.add(134); //Trickster

	}
	
	public static HashMap<Integer,HashMap<String, String>> getLastConnections(L2PcInstance player){
		return LastAccess.get(player.getObjectId());
	}
	
	public static void loadConnections(L2PcInstance player){
		String consulta = "SELECT * FROM zeus_connection WHERE zeus_connection.charID = ? ORDER BY zeus_connection.date DESC LIMIT 0,10";
		Connection conn = null;
		PreparedStatement psqry = null;
		int Contador = 1;
		if(LastAccess != null){
			if(LastAccess.containsKey(player.getObjectId())){
				LastAccess.get(player.getObjectId()).clear();
			}
		}
		
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			psqry.setInt(1, player.getObjectId());
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				if(LastAccess==null){
					LastAccess.put(player.getObjectId(), new HashMap<Integer, HashMap<String,String>>());
				}else if(!LastAccess.containsKey(player.getObjectId())){
					LastAccess.put(player.getObjectId(), new HashMap<Integer, HashMap<String,String>>());
				}
				LastAccess.get(player.getObjectId()).put(Contador, new HashMap<String, String>());
				LastAccess.get(player.getObjectId()).get(Contador).put("WAN", rss.getString(1));
				LastAccess.get(player.getObjectId()).get(Contador).put("LAN", rss.getString(2));
				LastAccess.get(player.getObjectId()).get(Contador).put("DATE", rss.getString(6).replace(".0", ""));
				Contador++;
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}		
		
	}
	

	public static void loadPropertyWeb(){
		COMMUNITY_BOARD_SV_CONFIG.clear();
		DRESSME_ID_BLOCK.clear();
		try{
			Properties propZ = new Properties();
			propZ.load(new FileInputStream(new File(ZEUS_CONFIG)));
			LOGINSERVERNAME = String.valueOf(propZ.getProperty("L2JLOGINSERVER_NAME"));
			WEB_HOP_ZONE_SERVER = String.valueOf(propZ.getProperty("WEB_HOPZONE_SERVER"));
			WEB_TOP_ZONE_SERVER = String.valueOf(propZ.getProperty("WEB_TOPZONE_SERVER"));
			GET_NAME_VAR_TYPE = String.valueOf(propZ.getProperty("GET_NAME_VAR_TYPE"));
			GET_NAME_VAR_EMAIL = String.valueOf(propZ.getProperty("GET_NAME_VAR_EMAIL"));
			GET_NAME_VAR_CODE = String.valueOf(propZ.getProperty("GET_NAME_VAR_CODE"));
			GET_NAME_VAR_DIR_WEB = String.valueOf(propZ.getProperty("GET_NAME_VAR_DIR_WEB"));
			GET_NAME_VAR_IDDONACION = String.valueOf(propZ.getProperty("GET_NAME_VAR_IDDONACION"));
			GET_NAME_VAR_SERVER_ID = String.valueOf(propZ.getProperty("GET_NAME_VAR_SERVER_ID"));
			String prop_desde_txt = String.valueOf(propZ.getProperty("COMMUNITY_SV_CONFIG_MANUAL_INPUT"));
			if(prop_desde_txt!=null){
				if(prop_desde_txt.length()>0){
					for(String proP_Indi : prop_desde_txt.split(";")){
						if(proP_Indi.split(":").length>1){
							COMMUNITY_BOARD_SV_CONFIG.add(proP_Indi);
						}else{
							_log.warning("ZeuS error loading Server Config for CB->" + proP_Indi);
						}
					}
				}
			}
			String DRESSME_BLOCK = String.valueOf(propZ.getProperty("DRESSME_TARGET_BLOCK_ITEM"));

			if(DRESSME_BLOCK !=null){
				if(DRESSME_BLOCK.length()>0){
					for(String idItem : DRESSME_BLOCK.split(";")){
						try{
							DRESSME_ID_BLOCK.add(Integer.valueOf(idItem));
						}catch(Exception a){
							_log.warning("ZeuS erro-> Loading dressme block ->" + a.getMessage() +" = " + idItem);
						}
					}
					if(DRESSME_ID_BLOCK.size()>0){
						_log.warning("==== Loading " + String.valueOf(DRESSME_ID_BLOCK.size()) + " blocked item for Dressme Target ====");
					}
				}
			}
			
			try{
				PLAYER_BASE_TO_SHOW = Integer.valueOf(propZ.getProperty("ONLINE_BASE_PLAYER"));	
			}catch(Exception Numero){
				_log.warning("Error in load Property ONLINE_BASE_PLAYER");
				PLAYER_BASE_TO_SHOW = 20;
			}
			
			String NoShow = propZ.getProperty("NO_SHOW_ON_PLAYER_INFO","");

			COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.clear();
			
			if(NoShow.length()>0){
				for(String PartesNosShow : NoShow.split(",") ){
					COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.add(PartesNosShow + ":");
				}
			}else{
				COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.add("NONE");
			}
			
			if(BUFF_STORE_BUFFPROHIBITED!=null){
				BUFF_STORE_BUFFPROHIBITED.clear();
			}
			
			String BuffProhibited = propZ.getProperty("BUFFSTORE_BUFF_PROHIBITED");
			for(String Parte : BuffProhibited.split(",")){
				if(opera.isNumeric(Parte)){
					BUFF_STORE_BUFFPROHIBITED.add(Integer.valueOf(Parte));
				}
			}
			
			String ITEMS_REQUEST = propZ.getProperty("BUFFSTORE_ITEMS_REQUEST","57");
			
			
			if(BUFFSTORE_ITEMS_REQUEST!=null){
				BUFFSTORE_ITEMS_REQUEST.clear();
			}
			
			
			//ID:NOMBRE_ITEM
			
			for(String t : ITEMS_REQUEST.split(",")){
				if(opera.exitIDItem(Integer.valueOf(t))){
					BUFFSTORE_ITEMS_REQUEST.add(t+":"+central.getNombreITEMbyID(Integer.valueOf(t)).replace(" ", "_") );
				}
			}
			
			ITEMS_REQUEST = propZ.getProperty("AUCTIONSHOUSE_ITEMS_REQUEST","57");
			
			for(String t : ITEMS_REQUEST.split(",")){
				if(opera.exitIDItem(Integer.valueOf(t))){
					AUCTIONSHOUSE_ITEM_REQUEST.add(t+":"+central.getNombreITEMbyID(Integer.valueOf(t)).replace(" ", "_") );
				}
			}
			
			AUCTIONSHOUSE_PERCENT_FEED = Integer.valueOf(propZ.getProperty("AUCTIONSHOUSE_PERCENT_FEED","5"));

			AUCTIONSHOUSE_FEED_MASTER = Integer.valueOf(propZ.getProperty("AUCTIONSHOUSE_FEED_MASTER","10000"));
			
			
			STAFF_DATA = propZ.getProperty("STAFF_DATA");
			
			String strCBAccess = propZ.getProperty("COMMUNITY_MAIN_ACCESS","0,1,2,3,4,5,6");
			
			COMMUNITY_MAIN_ACCESS.clear();
			
			int ContadorMaximo = 0;
			
			for(String M : strCBAccess.split(",")){
				if(ContadorMaximo>7){
					break;
				}
				try{
					int ID = Integer.valueOf(M);
					COMMUNITY_MAIN_ACCESS.add(ID);
				}catch(Exception a){
					_log.warning("Wrog input on Community main Acess->" + a.getMessage());
					_log.warning("Try tro create a Random Access");
					try{
						int RandInt = -1;
						int RandQ = 0;
						Random aleatorio = new Random();
						while(RandInt<0){
							RandQ = aleatorio.nextInt(Comunidad.getSizeMainOption());
							if(COMMUNITY_MAIN_ACCESS==null){
								COMMUNITY_MAIN_ACCESS.add(RandQ);
								RandInt = RandQ;
							}else{
								if(!COMMUNITY_MAIN_ACCESS.contains(RandQ)){
									COMMUNITY_MAIN_ACCESS.add(RandQ);
									RandInt = RandQ;								
								}
							}
						}
					}catch(Exception b){
						_log.warning("Error on get a Random Option -> " + b.getMessage());
					}
				}
			}
			
		}catch(Exception a){
			_log.warning("Error loading ZeuS.properties: -> " + a.getMessage());
		}
	}

	public static Vector<String> getCommunityServerInfo() {
		return COMMUNITY_BOARD_SV_CONFIG;
	}


	public static void loadConfigsTele() {
		try{
			loadTeleportMain();
		}catch(Exception a){
			_log.warning("Load ZeuS Teleport->"+a.getMessage());
		}
	}

	public static void loadConfigsShop() {
		try{
			loadShopMain();
		}catch(Exception a){
			_log.warning("Load ZeuS Shop->"+a.getMessage());
		}

	}

	public static void loadConfigBanIP() {
		try{
			loadIPBLOCK();
		}catch(Exception a){

		}

	}

	public static void loadAntiBotQuestion() {
		try{
			loadPreguntasBot();
		}catch(Exception a){

		}
	}

  	public static general getInstance()
	{
	    return SingletonHolder._instance;
	}

  	private static void checkDualBoxData(){
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = ConnectionFactory.getInstance().getConnection();
			ins = con.prepareStatement(SCRIPT_BORRADO);
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
		}catch(SQLException a){

		}
		try{
			con.close();
		}catch(SQLException a){

		}
	}
  	
  	public static HashMap<Integer,HashMap<Integer,String>> getTopPpl(int idFind){
  		return TopPlayersToShow.get(idFind);
  	}
  	
  	
  	public static HashMap<Integer,HashMap<Integer,String>> getPlayerHeroes(String Raza){
  		return HeroesList.get(Raza);
  	}
  	
  	private static void _getAllHeroesPlayers(){
  		//HeroesList
  		
  		String Consulta = "SELECT characters.char_name," +
				"characters.level," +
				"characters.base_class," +
				"characters.pvpkills," +
				"characters.pkkills,"+
				"IFNULL((SELECT clan_data.clan_name FROM clan_data WHERE clan_data.clan_id = characters.clanid),\"No Clan\"),"+
				"characters.online,"+
				"characters.onlinetime"+
				" FROM characters WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.char_name";

  		Connection conn;
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				//classData.get(136).put("CLASSBASE",
				int IdClass = rss.getInt(3);
				String BaseClase = classData.get(IdClass).get("CLASSBASE");
				if(!HeroesList.containsKey(BaseClase)){
					HeroesList.put(BaseClase, new HashMap<Integer,HashMap<Integer,String>>());
				}
				HeroesList.get(BaseClase).put(IdClass, new HashMap<Integer,String>());
				HeroesList.get(BaseClase).get(IdClass).put(1, rss.getString(1));
				HeroesList.get(BaseClase).get(IdClass).put(2, String.valueOf(rss.getInt(2)));
				HeroesList.get(BaseClase).get(IdClass).put(3, String.valueOf(rss.getInt(3)));
				HeroesList.get(BaseClase).get(IdClass).put(4, String.valueOf(rss.getInt(4)));
				HeroesList.get(BaseClase).get(IdClass).put(5, String.valueOf(rss.getInt(5)));
				HeroesList.get(BaseClase).get(IdClass).put(6, rss.getString(6));
				HeroesList.get(BaseClase).get(IdClass).put(7, String.valueOf(rss.getInt(7)));
				HeroesList.get(BaseClase).get(IdClass).put(8, String.valueOf(rss.getInt(8)));
			}			
		} catch (SQLException e) {

		}
  	}
  	
  	
  	private static void _getAllAugment(){
  		if(AUGMENT_DATA!=null){
  			AUGMENT_DATA.clear();
  		}
  		
  		String Consulta = "SELECT * FROM zeus_augment_data ORDER BY skill_descrip";
		Connection conn = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			CallableStatement psqry = conn.prepareCall(Consulta);
			ResultSet rss = psqry.executeQuery();
			try{
				while (rss.next()){
					if(!AUGMENT_DATA.containsKey(rss.getString("tipo"))){
						AUGMENT_DATA.put(rss.getString("tipo"), new HashMap<Integer, HashMap<String,String>>());
					}
					AUGMENT_DATA.get(rss.getString("tipo")).put(rss.getInt("id"), new HashMap<String,String>());
					AUGMENT_DATA.get(rss.getString("tipo")).get(rss.getInt("id")).put("ID_AUGMT_SERVER", String.valueOf(rss.getInt("idaugmentGame")));
					AUGMENT_DATA.get(rss.getString("tipo")).get(rss.getInt("id")).put("AUGMENT_DESCRIPT", String.valueOf(rss.getString("aug_descrip")));
					AUGMENT_DATA.get(rss.getString("tipo")).get(rss.getInt("id")).put("AUGMENT_SKILL", String.valueOf(rss.getInt("aug_skill")));
					AUGMENT_DATA.get(rss.getString("tipo")).get(rss.getInt("id")).put("AUGMENT_SKILL_DESCRIP", String.valueOf(rss.getString("skill_descrip")));
					AUGMENT_DATA.get(rss.getString("tipo")).get(rss.getInt("id")).put("AUGMENT_SKILL_LEVEL", String.valueOf(rss.getInt("skill_level")));
				}
			}catch(SQLException a){
				conn.close();
			}
			conn.close();
		}catch(SQLException a){

		}
  		
  		
  		
  		//AUGMENT_DATA
  	}
  	
  	
  	private static void _getAllTopPlayers(){
		
		if(TopPlayersToShow!=null){
			TopPlayersToShow.clear();
		}
		
		
		HashMap<Integer,String> TipoBusquedas = new HashMap<Integer, String>();
		TipoBusquedas.put(0, " WHERE characters.accesslevel = 0 ORDER BY characters.pkkills DESC LIMIT " + String.valueOf(general.COMMUNITY_BOARD_TOPPLAYER_LIST));//Mayor a Menor
		TipoBusquedas.put(1, " WHERE characters.accesslevel = 0 ORDER BY characters.pkkills ASC LIMIT " + String.valueOf(general.COMMUNITY_BOARD_TOPPLAYER_LIST));//Menor a Mayor
		TipoBusquedas.put(2, " WHERE characters.accesslevel = 0 ORDER BY characters.pvpkills DESC LIMIT " + String.valueOf(general.COMMUNITY_BOARD_TOPPLAYER_LIST));//Mayor a Menor
		TipoBusquedas.put(3, " WHERE characters.accesslevel = 0 ORDER BY characters.pvpkills ASC LIMIT " + String.valueOf(general.COMMUNITY_BOARD_TOPPLAYER_LIST));//Menor a Mayor

		String Consulta = "SELECT characters.char_name," +
				"characters.level," +
				"characters.base_class," +
				"characters.pvpkills," +
				"characters.pkkills,"+
				"IFNULL((SELECT clan_data.clan_name FROM clan_data WHERE clan_data.clan_id = characters.clanid),\"No Clan\"),"+
				"characters.online,"+
				"characters.onlinetime"+
				" FROM characters ";
		
		for(int i=0;i<TipoBusquedas.size();i++){
			TopPlayersToShow.put(i, new HashMap<Integer,HashMap<Integer,String>>());
			Connection conn = null;
			String strConsulta = Consulta + TipoBusquedas.get(i);
			try{
				conn  = ConnectionFactory.getInstance().getConnection();
				PreparedStatement psqry = conn.prepareStatement(strConsulta);
				ResultSet rss = psqry.executeQuery();
				int contador = 0;
				while (rss.next()){
					TopPlayersToShow.get(i).put(contador, new HashMap<Integer,String>());
					TopPlayersToShow.get(i).get(contador).put(1, rss.getString(1));
					TopPlayersToShow.get(i).get(contador).put(2, String.valueOf(rss.getInt(2)));
					TopPlayersToShow.get(i).get(contador).put(3, String.valueOf(rss.getInt(3)));
					TopPlayersToShow.get(i).get(contador).put(4, String.valueOf(rss.getInt(4)));
					TopPlayersToShow.get(i).get(contador).put(5, String.valueOf(rss.getInt(5)));
					TopPlayersToShow.get(i).get(contador).put(6, rss.getString(6));
					TopPlayersToShow.get(i).get(contador).put(7, String.valueOf(rss.getInt(7)));
					TopPlayersToShow.get(i).get(contador).put(8, String.valueOf(rss.getInt(8)));
					contador++;
				}
			}catch(Exception a){
				
			}
		}
		
		//Tipo,Posicion
		
		//private static HashMap<Integer, HashMap<Integer, HashMap<String,String>>> TopPlayersToShow = new HashMap<Integer, HashMap<Integer,HashMap<String,String>>>();
		
		
	}
  	
  	public static String getBeginDate(){
  		return BeginTime;
  	}
  	

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder{
	    protected static final general _instance = new general();
	}
	
	public static void _getAllTopPlayersConfig(){
		_getAllTopPlayers();
	}
	
	private static void getAllAutoUpdate(){
		if(LOAD_START){
			return;
		}
		_getAllChar();
		_getMostSearch();
		_getAllTopPlayers();
		_getAllHeroesPlayers();
		v_auction_house.getAllItem();
		C_oly_buff.getSchFromOlys();
		if(!LOAD_START_ONE_TIME){
			Engine._Load();
			_getAllAugment();
			cbManager.loadColores();
			sellBuff.restoreOffline();
			v_partymatching.setTipoBusquedas();
			LOAD_START_ONE_TIME=true;
		}
		int MinutosParaCargar = 5;
		ThreadPoolManager.getInstance().scheduleGeneral(new ReloadTopInfo(), (MinutosParaCargar * 60) * 1000);
		LOAD_START=true;
		_log.info("AutoLoad ZeuS");
	}
	
	
	public static class ReloadTopInfo implements Runnable{
		public ReloadTopInfo(){

		}
		@Override
		public void run(){
			try{
				LOAD_START=false;
				getAllAutoUpdate();
			}catch(Exception a){

			}

		}
	}
	
	
	
	
}


