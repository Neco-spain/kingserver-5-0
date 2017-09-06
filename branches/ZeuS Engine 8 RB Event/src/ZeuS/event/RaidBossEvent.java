/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ZeuS.event;

/**
 * 
 * @author HanWik & Blazer. Created for L2Blaze Server.
 *
 */

import java.awt.TrayIcon.MessageType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.data.xml.impl.NpcData;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.enums.Team;
import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.L2Party.messageType;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.model.items.L2Item;
import com.l2jserver.gameserver.network.serverpackets.ActionFailed;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.util.Broadcast;
import com.l2jserver.util.StringUtil;

import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.central;
import ZeuS.procedimientos.opera;

public class RaidBossEvent
{
	private static boolean RaidBoss_Auto_Event = false;
	
	private final static int searchNextSpawnOnMinute = 30;
	
	private static int _npcX, _npcY, _npcZ, _bossX, _bossY, _bossZ, _startX, _startY, _startZ;
	private static int _bossId;
	private static boolean _playersWon = false;
	
	private static int KeyToStart = 0;
	private static int NextUnixEvent = 0;
	private static boolean isStartServerEvent = true;
	//EVENT_RAIDBOSS_MINUTE_INTERVAL_START_SERVER
	
	private static HashMap<Integer,String> PLAYER_REGISTER = new HashMap<Integer,String>();
	private static Vector<Integer> PLAYER_TO_HIT_THE_RAID = new Vector<Integer>();
	private static HashMap<Integer, HashMap<String,String>> PLAYER_INFO = new HashMap<Integer, HashMap<String,String>>();
	private static Vector<String> DROP_RAID = new Vector<String>();
	private static enum FormatosVaribles{
		TELE,
		COLOR
	}
	
	public static boolean isJoin(){
		return _joining;
	}
	
	public static void isEventModRaid(L2Npc raid,L2PcInstance player){
		if(PLAYER_REGISTER==null){
			return;
		}
		if(PLAYER_REGISTER.size()==0){
			return;
		}
		if(!_started){
			return;
		}
		
		if(raid.getTemplate().getId() != _bossId){
			return;
		}
		
		raidKilled();
	}

	public static void setPlayerHitRaid(L2PcInstance player){
		if(player==null){
			return;
		}
		
		if(!PLAYER_REGISTER.containsKey(player.getObjectId())){
			return;
		}
		
		if(PLAYER_TO_HIT_THE_RAID!=null){
			if(!PLAYER_TO_HIT_THE_RAID.contains(player.getObjectId())){
				PLAYER_TO_HIT_THE_RAID.add(player.getObjectId());
				player.setTeam(Team.RED);
			}
		}else{
			PLAYER_TO_HIT_THE_RAID.add(player.getObjectId());
		}
	}
	
	public static boolean isPlayerOnRBEvent(L2PcInstance player){
		
		if(PLAYER_REGISTER==null){
			return false;
		}
		
		if(PLAYER_REGISTER.size()==0){
			return false;
		}
		
		try{
			return PLAYER_REGISTER.containsKey(player.getObjectId());
		}catch(Exception a){
			return false;
		}
	}
	
	public static boolean onDieRevive(L2PcInstance character)
	{
		if(!_started){
			return false;
		}
		
		if(PLAYER_REGISTER==null){
			return false;
		}
		
		if(PLAYER_REGISTER.size()==0){
			return false;
		}
		
		if(!PLAYER_REGISTER.containsKey(character.getObjectId())){
			return false;
		}
		
		L2PcInstance activeChar = character;
		activeChar.sendMessage("You will revive on " + String.valueOf(general.EVENT_RAIDBOSS_SECOND_TO_REVIVE) + " Second's");
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
			@Override
			public void run()
			{
				activeChar.doRevive();
				if(PLAYER_TO_HIT_THE_RAID!=null){
					if(PLAYER_REGISTER!=null){
						if(PLAYER_REGISTER.containsKey(activeChar.getObjectId())){
							if(PLAYER_TO_HIT_THE_RAID.contains(activeChar.getObjectId())){
								activeChar.setTeam(Team.RED);
							}else{
								activeChar.setTeam(Team.BLUE);
							}
							try{
								SkillData.getInstance().getSkill(1323,1).applyEffects(activeChar,activeChar);
							}catch(Exception a){
								
							}
						}
					}
				}
			}
		}, general.EVENT_RAIDBOSS_SECOND_TO_REVIVE * 1000);
		return true;
	}
	
	public static void setPlayerInfo(L2PcInstance player){
		String LocActual = String.valueOf(player.getLocation().getX()) + "," + String.valueOf(player.getLocation().getY()) + "," + String.valueOf(player.getLocation().getZ());
		String ColorName = String.valueOf(player.getAppearance().getNameColor());
		PLAYER_INFO.put(player.getObjectId(), new HashMap<String,String>());
		PLAYER_INFO.get(player.getObjectId()).put(FormatosVaribles.TELE.name(), LocActual);
		PLAYER_INFO.get(player.getObjectId()).put(FormatosVaribles.COLOR.name(), ColorName);
		player.setTeam(Team.BLUE);
	}
	
	public static String getPlayerTele(L2PcInstance player){
		return PLAYER_INFO.get(player.getObjectId()).get(FormatosVaribles.TELE.name());
	}

	public static String getPlayerColorName(L2PcInstance player){
		return PLAYER_INFO.get(player.getObjectId()).get(FormatosVaribles.COLOR.name());
	}
	
	private final static Logger _log = Logger.getLogger(RaidBossEvent.class.getName());

	public static String _nameRaid = "";
	
	public static boolean _joining = false;
	public static boolean _teleport = false;
	public static boolean _started = false;
	public static L2Spawn _bossSpawn;
	
	public static int _npcHeading = 0;
	public static int _bossHeading = 0;
	
	
	public static Vector<L2PcInstance> getValidPlayer(boolean LimpiarRegistroPlayer){
		Vector<L2PcInstance> retorno = new Vector<L2PcInstance>();
		HashMap<Integer, String> LimpieadoRegistro = new HashMap<Integer, String>();
		Iterator itr = PLAYER_REGISTER.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	L2PcInstance cha = null;
	    	try{
	    		cha = opera.getPlayerbyName(Entrada.getValue().toString());
	    		if(cha != null){
	    			if(cha.isOnline()){
	    				if(!cha.getClient().isDetached()){
	    					retorno.add(cha);
	    					LimpieadoRegistro.put(cha.getObjectId(), cha.getName());
	    				}
	    			}
	    		}
	    	}catch(Exception a){
	    		
	    	}
	    }
	    if(LimpiarRegistroPlayer){
	    	PLAYER_REGISTER.clear();
	    	PLAYER_REGISTER = LimpieadoRegistro;
	    }
	    return retorno;
	}
	
	
	public static void AnnounceToPlayers(Boolean toall, String announce)
	{
		if (toall) {
			AnunciarTodos(announce);
		}else{
			Iterator itr = PLAYER_REGISTER.entrySet().iterator();
		    while(itr.hasNext()){
		    	Map.Entry Entrada = (Map.Entry)itr.next();
		    	L2PcInstance cha = null;
		    	try{
		    		cha = opera.getPlayerbyName(Entrada.getValue().toString());
		    		if(cha != null){
		    			central.msgbox_Lado(announce, cha, "RAIDBOSS EVENT");
		    		}
		    	}catch(Exception a){
		    		
		    	}
		    }
		}
	}
	
	public static void kickPlayerFromRaid(L2PcInstance playerToKick)
	{
		if (playerToKick == null) {
			return;
		}
		
		if(isPlayerOnRBEvent(playerToKick)){
			PLAYER_REGISTER.remove(playerToKick.getObjectId());
		}
		
		if (_started || _teleport)
		{
			if (playerToKick.isOnline())
			{
				playerToKick.getAppearance().setNameColor( Integer.valueOf(getPlayerColorName(playerToKick)));
				playerToKick.broadcastUserInfo();
				playerToKick.sendMessage("You have been kicked from the Raid.");
				//playerToKick.setTeam(0);
				String LocationToSend = getPlayerTele(playerToKick);
				playerToKick.teleToLocation(Integer.valueOf(LocationToSend.split(",")[0]), Integer.valueOf(LocationToSend.split(",")[1]), Integer.valueOf(LocationToSend.split(",")[2]), false);
			}
		}
	}
	
	private static void setBossPos(){
		_bossX = Integer.valueOf(general.EVENT_RAIDBOSS_RAID_POSITION.split(",")[0]);
		_bossY = Integer.valueOf(general.EVENT_RAIDBOSS_RAID_POSITION.split(",")[1]);
		_bossZ = Integer.valueOf(general.EVENT_RAIDBOSS_RAID_POSITION.split(",")[2]);
	}
	
	public static boolean checkMaxLevel(int maxlvl)
	{
		return general.EVENT_RAIDBOSS_PLAYER_MIN_LEVEL < maxlvl;
	}
	
	public static boolean checkMinLevel(int minlvl)
	{
		return general.EVENT_RAIDBOSS_PLAYER_MAX_LEVEL > minlvl;
	}
	
	public static boolean checkMinPlayers(int players)
	{
		return general.EVENT_RAIDBOSS_PLAYER_MIN_REGIS <= players;
	}
	
	public static boolean checkMaxPlayers(int players)
	{
		return general.EVENT_RAIDBOSS_PLAYER_MAX_REGIS > players;
	}
	
	public static void setTeamPos()
	{
		_startX = Integer.valueOf(general.EVENT_RAIDBOSS_PLAYER_POSITION.split(",")[0]);
		_startY = Integer.valueOf(general.EVENT_RAIDBOSS_PLAYER_POSITION.split(",")[1]);
		_startZ = Integer.valueOf(general.EVENT_RAIDBOSS_PLAYER_POSITION.split(",")[2]);
	}
	
	public static boolean checkTeamOk()
	{
		return !(_started || _teleport || _joining);
	}
	
	public static boolean startJoin()
	{
		setIdBoss();
		setTeamPos();
		setBossPos();
		spawnEventBoss();
		_joining = true;
		MensajeRegistro(true);
		return true;
	}
	
	
	private static void MensajeRegistro(boolean isStart){
		if(isStart){
			AnnounceToPlayers(true, "============================");
			AnnounceToPlayers(true, "Iniciado / Started Use .joinraid to register and .leaveraid to unregister!");
			AnnounceToPlayers(true, "Our Raid for now is " + _nameRaid + ", register and kill it!!!");
			AnnounceToPlayers(true, NombresItemDar());
			AnnounceToPlayers(true, NombresItemDarlooser());
			AnnounceToPlayers(true, "============================");
		}
		AnnounceToPlayers(true, "Recruiting levels " + String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MIN_LEVEL) + " to " + String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MAX_LEVEL));
		AnnounceToPlayers(true, "Min. Player's " + String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MIN_REGIS) + ", Max. Player's " + String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MAX_REGIS));
		if(!isStart){
			AnnounceToPlayers(true, "Players Registered " + String.valueOf(PLAYER_REGISTER.size()));
		}
	}
	
	private static void spawnEventBoss(){
		
		if(_joining){
			return;
		}
		
		L2NpcTemplate tmpl = NpcData.getInstance().getTemplate(_bossId);
		try
		{
			DROP_RAID.clear();
			_bossSpawn = new L2Spawn(tmpl);
			_bossSpawn.setX(_bossX);
			_bossSpawn.setY(_bossY);
			_bossSpawn.setZ(_bossZ);
			_bossSpawn.setAmount(1);
			_bossSpawn.setHeading(_bossHeading);
			_bossSpawn.setRespawnDelay(20);
			_bossSpawn.setAmount(1);;			
			SpawnTable.getInstance().addNewSpawn(_bossSpawn, false);
			_bossSpawn.init();
			_bossSpawn.getLastSpawn().setTitle("RAID BOSS EVENT");
			_bossSpawn.getLastSpawn()._isEventMobRaid = true;
			_bossSpawn.getLastSpawn().isAggressive();
			_bossSpawn.getLastSpawn().isInvisible();
			_bossSpawn.getLastSpawn().decayMe();
			_bossSpawn.getLastSpawn().spawnMe(_bossSpawn.getLastSpawn().getX(), _bossSpawn.getLastSpawn().getY(), _bossSpawn.getLastSpawn().getZ());
			_bossSpawn.getLastSpawn().broadcastPacket(new MagicSkillUse(_bossSpawn.getLastSpawn(), _bossSpawn.getLastSpawn(), 1034, 1, 1, 1));
			_nameRaid = tmpl.getName();
/*			
			if (!(_bossSpawn.getTemplate().getDropData().isEmpty())){
				for (L2DropCategory cat : _bossSpawn.getTemplate().getDropData())
				{
					for (L2DropData drop : cat.getAllDrops())
					{
						final L2Item item = ItemTable.getInstance().getTemplate(drop.getId());
						if (item == null)
						{
							continue;
						}
						final String color;
						if (drop.getChance() >= 500000)
						{
							color = "ff9999";
						}
						else if (drop.getChance() >= 300000)
						{
							color = "00ff00";
						}
						else
						{
							color = "0066ff";
						}
						DROP_RAID.add("<tr>"
								+ "<td><img src=\"" + item.getIcon() + "\" height=32 width=32></td>"
								+ "<td><font color=\"" + color + "\">" + item.getName() + "</font></td>" 
								+ "<td>" + (drop.isQuestDrop() ? "Quest" : (cat.isSweep() ? "Sweep" : "Drop")) 
								+ "</td></tr>");
					}
				}
			}
*/			
			
		}
		catch (Exception e)
		{
			_log.warning("Raid Engine[spawnEventNpc(" + String.valueOf(_bossId) + ")]: exception: " + e.getMessage());
		}
	}
	
	private static boolean teleportStart(){
		if (!_joining || _started || _teleport) {
			return true;
		}
		
		removeOfflinePlayers();
		if (!checkMinPlayers( PLAYER_REGISTER.size()))
		{
			if(_joining){
				AnnounceToPlayers(true, "Not enough players for RB Event. Min Requested : " + String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MIN_REGIS ) + ", Participating : " + PLAYER_REGISTER.size());
				cleanRaid();
			}
			return false;
		}
		
		_joining = false;
		AnnounceToPlayers(false, "Teleport to starting spot in "+ String.valueOf(general.EVENT_RAIDBOSS_SECOND_TO_TELEPORT_RADIBOSS) +" seconds!");
		setUserData();
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
			public void run()
			{
				RaidBossEvent.sit(true);
				
				for (L2PcInstance player : getValidPlayer(false)){
					if (player != null)	{
						// Remove player from his party
						if (player.getParty() != null)
						{
							L2Party party = player.getParty();
							party.removePartyMember(player,messageType.Expelled);
						}
						player.teleToLocation(_startX, _startY, _startZ, true);
					}
				}
			}
		}, general.EVENT_RAIDBOSS_SECOND_TO_TELEPORT_RADIBOSS * 1000);
		_teleport = true;
		return true;
	}
	
	private static void startEvent(L2PcInstance activeChar)
	{
		if (!startEventOk())
		{
			if (Config.DEBUG) {
				_log.fine("Raid Engine[startEvent(" + activeChar.getName() + ")]: startEventOk() = false");
			}
			return;
		}
		_teleport = false;
		sit(false);
		_bossSpawn.getLastSpawn().isVisible();
		AnnounceToPlayers(false,"Started. Go kill the raid!");
		_started = true;
	}
	
	public static Integer getSeconToStartEvent(){
		if(general.EVENT_RAIDBOSS_HOUR_TO_START==null){
			return -1;
		}
		
		if(general.EVENT_RAIDBOSS_HOUR_TO_START.length()==0){
			return -1;
		}
		
		Calendar currentCal = Calendar.getInstance();
		Integer hour = currentCal.get(Calendar.HOUR_OF_DAY);
		Integer mins = currentCal.get(Calendar.MINUTE);
		Integer sec = currentCal.get(Calendar.SECOND);
		String horaCercana = "";
		Vector<String> Tiempos = new Vector<String>();
		for(String horas : general.EVENT_RAIDBOSS_HOUR_TO_START.split(";")){
			Tiempos.add(horas);
		}
		Collections.sort(Tiempos);
		for(String tiempo : Tiempos){
			if(tiempo.split(":").length == 2){
				if(Integer.valueOf(tiempo.split(":")[0])==hour && Integer.valueOf(tiempo.split(":")[1])>=mins && horaCercana.length()==0){
					horaCercana = tiempo.split(":")[0] + ":" + tiempo.split(":")[1] + ":00";
				}else if(Integer.valueOf(tiempo.split(":")[0]) > hour && horaCercana.length()==0){
					horaCercana = tiempo.split(":")[0] + ":" + tiempo.split(":")[1] + ":00";
				}else if(Integer.valueOf(tiempo.split(":")[0])<= hour && Integer.valueOf(tiempo.split(":")[1])>=mins && horaCercana.length()==0){
					horaCercana = tiempo.split(":")[0] + ":" + tiempo.split(":")[1] + ":00";
				}else if((hour - Integer.valueOf(tiempo.split(":")[0]))>12 && horaCercana.length()==0){
					if(horaCercana.length()==0){
						horaCercana = tiempo.split(":")[0] + ":" + tiempo.split(":")[1] + ":00";
					}
				}
			}
		}
		int SegundosDiferencia = 0;
		if(horaCercana.length()>0){
			int SegundosHoraCerca = 0;
			if( Integer.valueOf(horaCercana.split(":")[0]) >= hour ){
				SegundosHoraCerca = (Integer.valueOf(horaCercana.split(":")[0]) * 3600) + (Integer.valueOf(horaCercana.split(":")[1]) * 60) + 60;
				SegundosDiferencia = SegundosHoraCerca - ((hour * 3600) + (mins * 60) + sec);				
			}else{
				SegundosHoraCerca = (Integer.valueOf(horaCercana.split(":")[0]) * 3600) + (Integer.valueOf(horaCercana.split(":")[1]) * 60) + 60;
				int segundosDias = 86400;
				int SegundosHoraActual = (hour * 3600) + (mins * 60) + sec;
				SegundosDiferencia = (segundosDias - SegundosHoraActual) + SegundosHoraCerca;
			}
			_log.warning("RaidBossEvent: Next event in ->" + opera.getTiempoON(SegundosDiferencia, false) + " sec. at " + horaCercana);
		}
		return SegundosDiferencia ;
	}
	
	
	
	private static class RaidBoss_autoSearch implements Runnable{
		int _CodigoEntrada;
		public RaidBoss_autoSearch(){
			
		}
		@Override
		public void run(){
			try{
				if(!RaidBoss_Auto_Event){
					IntervalEventStart();
				}
			}catch(Exception a){

			}

		}
	}	
	
	
	
	public static class RaidBossIntervalRunner implements Runnable{
		int _CodigoEntrada;
		public RaidBossIntervalRunner(int CodEntrada){
			_CodigoEntrada = CodEntrada;
		}
		@Override
		public void run(){
			try{
				if(_CodigoEntrada == KeyToStart){
					if(!_joining && !_started & !_teleport){
						if(general.EVENT_RAIDBOSS_AUTOEVENT){
							KeyToStart = opera.getUnixTimeNow();
							autoEvent();
						}
					}
				}
			}catch(Exception a){

			}

		}
	}	
	
	public static void IntervalEventStart(){

		int IntevarloCorrectoMayor = general.EVENT_RAIDBOSS_JOINTIME + general.EVENT_RAIDBOSS_EVENT_TIME + 10;
		
		NextUnixEvent = 0;
		
		if(general.EVENT_RAIDBOSS_MINUTE_INTERVAL<=0 || general.EVENT_RAIDBOSS_MINUTE_INTERVAL <= IntevarloCorrectoMayor){
			_log.warning("Raidboss event->Problem with Interval : " + general.EVENT_RAIDBOSS_MINUTE_INTERVAL + ". Need to be bigger than "+ String.valueOf(IntevarloCorrectoMayor) +" minutes");
			RaidBoss_Auto_Event = false;
			return;
		}

		if(!general.EVENT_RAIDBOSS_AUTOEVENT){
			return;	
		}
		
		if(general.EVENT_RAIDBOSS_HOUR_TO_START==null){
			return;
		}
		if(general.EVENT_RAIDBOSS_HOUR_TO_START.length()==0){
			return;
		}
		
		
		int minutosParaComenzar = 0;
		
		if(isStartServerEvent){
			isStartServerEvent = false;
			minutosParaComenzar = general.EVENT_RAIDBOSS_MINUTE_INTERVAL_START_SERVER;
		}else{
			minutosParaComenzar = general.EVENT_RAIDBOSS_MINUTE_INTERVAL;
		}
		KeyToStart = opera.getUnixTimeNow();
		NextUnixEvent = KeyToStart + (minutosParaComenzar * 60);
		ThreadPoolManager.getInstance().scheduleGeneral(new RaidBossIntervalRunner(KeyToStart), (minutosParaComenzar * 60) * 1000);
		_log.info("Raiddboss event-> Start: " + minutosParaComenzar + " minutes ("+ opera.getDateFromUnixTime(NextUnixEvent) +")");
		RaidBoss_Auto_Event = true;
	}
	
	public static String getNextEventTimeLess(){
		String retorno = "";
		
		if(general.EVENT_RAIDBOSS_MINUTE_INTERVAL<=0 || !RaidBoss_Auto_Event){
			return "";
		}
		
		int Falta = NextUnixEvent - opera.getUnixTimeNow();
		
		int minutosFalta = Math.round(Falta / 60);
		int Segundos = Falta;
		
		if(minutosFalta>0){
			retorno = "Left "+ String.valueOf(minutosFalta) +" minutes to start";
		}else{
			retorno = "Left "+ String.valueOf(Segundos) +" second to start";
		}
		retorno += "(/time "+ opera.getDateFromUnixTime(NextUnixEvent) +")";
		return retorno;
	}
	
	public static void autoEvent(){
		//INICIO
		if(_joining || _started || _teleport){
			return;
		}
		if (startJoin()){
			if (general.EVENT_RAIDBOSS_JOINTIME > 0) {
				waiter(general.EVENT_RAIDBOSS_JOINTIME * 60 * 1000); // minutes for join event
			}else if (general.EVENT_RAIDBOSS_JOINTIME <= 0){
				abortEvent();
				return;
			}
			if (teleportStart())
			{
				waiter(1 * 40 * 1000); // 30 sec wait time untill start fight after teleported
				if (startAutoEvent())
				{
					waiter(general.EVENT_RAIDBOSS_EVENT_TIME * 60 * 1000); // minutes for event time
					finishEvent();
				}
			}
			else if (!teleportStart())
			{
				abortEvent();
			}
		}
	}
	
	private static void waiter(long interval)
	{
		long startWaiterTime = System.currentTimeMillis();
		int seconds = (int) (interval / 1000);
		
		while (startWaiterTime + interval > System.currentTimeMillis())
		{
			seconds--; // here because we don't want to see two time announce at the same time
			
			if (_joining || _started || _teleport)
			{
				switch (seconds)
				{
					case 3600: // 1 hour left
						if (_joining)
						{
							MensajeRegistro(false);
						}
						else if (_started) {
							AnnounceToPlayers(false, "Raid Event: " + seconds / 60 / 60 + " hour(s) till event ends!");
						}
						
						break;
					case 1800: // 30 minutes left
					case 900: // 15 minutes left
					case 600: // 10 minutes left
					case 300: // 5 minutes left
					case 60: // 1 minute left
						if (_joining)
						{
							removeOfflinePlayers();
							MensajeRegistro(false);
							AnnounceToPlayers(true, seconds / 60 + " minute(s) till registration ends!");
						}
						else if (_started) {
							AnnounceToPlayers(false, seconds / 60 + " minute(s) till event ends!");
						}
						
						break;
					case 30: // 30 seconds left
					case 10: // 10 seconds left
					case 3: // 3 seconds left
					case 2: // 2 seconds left
					case 1: // 1 seconds left
						if (_joining)
						{
							MensajeRegistro(false);
							AnnounceToPlayers(true, seconds + " second(s) till registration ends!");
						}
						else if (_teleport) {
							AnnounceToPlayers(false, seconds + " second(s) till fight starts!");
						} else if (_started) {
							AnnounceToPlayers(false, seconds + " second(s) till event ends!");
						}
						
						break;
				}
			}
			
			long startOneSecondWaiterStartTime = System.currentTimeMillis();
			
			// only the try catch with Thread.sleep(1000) give bad countdown on high wait times
			while (startOneSecondWaiterStartTime + 1000 > System.currentTimeMillis())
			{
				try
				{
					Thread.sleep(1);
				}
				catch (InterruptedException ie)
				{
				}
			}
		}
	}
	
	private static boolean startAutoEvent(){
		if (!startEventOk())
		{
			if (Config.DEBUG) {
				_log.fine("Raid Engine[startEvent]: startEventOk() = false");
			}
			return false;
		}
		_teleport = false;
		sit(false);
		AnnounceToPlayers(false,"Started. Go kill the raid!");
		_started = true;
		return true;
	}	
	
	private static boolean startEventOk()
	{
		if (_joining || !_teleport || _started) {
			return false;
		}
		
		if (PLAYER_REGISTER == null || PLAYER_REGISTER.isEmpty() || PLAYER_REGISTER.size() == 0) {
			return false;
		}
		
		return true;
	}
	
	private static void setUserData()
	{
		for (L2PcInstance player : getValidPlayer(true))
		{
			
			setPlayerInfo(player);
			player.getAppearance().setNameColor(Integer.decode("0x" + "0099ff"));
			player.broadcastUserInfo();
		}
	}
	
	private static void finishEvent()
	{
		if (!finishEventOk())
		{
			if (Config.DEBUG) {
				_log.fine("Raid Engine[finishEvent]: finishEventOk() = false");
			}
			return;
		}
		_started = false;
		
		if (!_playersWon)
		{
			_bossSpawn.getLastSpawn().isInvisible();
			unspawnEventBoss();
			
			AnnounceToPlayers(true, "The event is over and you have failed to kill the raid.");
			for (L2PcInstance player : getValidPlayer(false))
			{
				NpcHtmlMessage nhm = new NpcHtmlMessage(5);
				final StringBuilder replyMSG = StringUtil.startAppend(1000, "<html><body>");
				if (PLAYER_TO_HIT_THE_RAID.contains(player.getObjectId())){
					EntregaPremioLooser(player);
					replyMSG.append("<html><body>Es una pena, no pudieron matar al raid. De igual manera recibirán un premio de Consuelo :D!!!</body></html>");
				}else{
					replyMSG.append("<html><body>Lo sentimos, no ayudaste a matar al raid. Por lo cual no tienes premio consuelo :( </body></html>");
				}
				nhm.setHtml(replyMSG.toString());
				player.sendPacket(nhm);
				player.sendPacket(ActionFailed.STATIC_PACKET);
			}
			
		}else{
			ThreadPoolManager.getInstance().scheduleGeneral(new unSpawnRaidBoss(), 5 * 1000);
		}
		teleportFinish();
		
	}
	
	private static boolean finishEventOk()
	{
		return _started;
	}
	
	private static String NombresItemDarlooser()
	{
		String NombresPremios = "";
		for (String rewardTexto : general.EVENT_RAIDBOSS_REWARD_LOOSER.split(";"))
		{
			if(NombresPremios.length()>0){
				NombresPremios +=", ";
			}
			String[] reward = rewardTexto.split(",");
			NombresPremios += reward[1] + " of " + ItemTable.getInstance().getTemplate(Integer.valueOf(reward[0])).getName();
		}
		return "Reward(s) if your loose:" + NombresPremios;
	}
	
	private static String NombresItemDar()
	{
		String NombresPremios = "";
		for (String rewardTexto : general.EVENT_RAIDBOSS_REWARD_WIN.split(";"))
		{
			if(NombresPremios.length()>0){
				NombresPremios +=", ";
			}
			String[] reward = rewardTexto.split(",");
			NombresPremios += reward[1] + " of " + ItemTable.getInstance().getTemplate(Integer.valueOf(reward[0])).getName();
		}
		return "Reward(s) if your Win:" + NombresPremios;
	}
	
	private static void EntregaPremioLooser(L2PcInstance eventPlayer)
	{
		opera.giveReward(eventPlayer, general.EVENT_RAIDBOSS_REWARD_LOOSER);
	}
	
	private static void EntregaPremio(L2PcInstance eventPlayer)
	{
		opera.giveReward(eventPlayer, general.EVENT_RAIDBOSS_REWARD_WIN);
	}
	
	private static void rewardTeam()
	{
		for (L2PcInstance player : getValidPlayer(false))
		{
			if (player != null && player.isOnline())
			{
				NpcHtmlMessage nhm = new NpcHtmlMessage(5);
				final StringBuilder replyMSG = StringUtil.startAppend(1000, "<html><body>");
				if(PLAYER_TO_HIT_THE_RAID.contains(player.getObjectId())){
					EntregaPremio(player);
					replyMSG.append("<html><body>Felicitaciones, como ayudaste a Matar al Raid Boss Event tendrás tu recompenza, Mira tu Inventario. :D!!!</body></html>");
				}else{
					replyMSG.append("<html><body>Lo sentimos, no ayudaste a matar al raid. Por lo cual no tienes recompenza :( </body></html>");
				}
				nhm.setHtml(replyMSG.toString());
				player.sendPacket(nhm);
				player.sendPacket(ActionFailed.STATIC_PACKET);
			}
		}
	}
	
	private static void raidKilled(){
		_playersWon = true;
		rewardTeam();
		AnnounceToPlayers(true, "The players have been killed the raidboss!");
		finishEvent();
	}
	
	public static void abortEvent()
	{
		if (!_joining && !_teleport && !_started) {
			return;
		}
		if (_joining && !_teleport && !_started)
		{
			unspawnEventBoss();
			cleanRaid();
			_joining = false;
			AnnounceToPlayers(true, "Match aborted!");
			return;
		}
		_joining = false;
		_teleport = false;
		_started = false;
		unspawnEventBoss();
		AnnounceToPlayers(true, "Match aborted!");
		teleportFinish();
	}
	
	private static void sit(Boolean Congelar)
	{
		for (L2PcInstance player : getValidPlayer(false)){
			if (player != null){
				
				if(general.EVENT_RAIDBOSS_CANCEL_BUFF){
					player.stopAllEffects();
					if(player.getSummon()!=null){
						player.getSummon().stopAllEffects();
					}
				}
				
				if(general.EVENT_RAIDBOSS_UNSUMMON_PET){
					if(player.getSummon()!=null){
						player.getSummon().unSummon(player);
					}
				}
				
				if (Congelar){
					if(general.EVENT_RAIDBOSS_PLAYER_INMOBIL){
						opera.setImmbileChar(player, true);
					}
				}else{
					opera.setImmbileChar(player, false);
				}
			}
		}
	}
	
	private static void setIdBoss(){
		Random r = new Random();
		int RanBossSelec = r.nextInt( general.EVENT_RAIDBOSS_RAID_ID.size() );
		_bossId = general.EVENT_RAIDBOSS_RAID_ID.get(RanBossSelec);
		NomRaid();
	}
	
	private static void NomRaid()
	{
		L2NpcTemplate tmpl = NpcData.getInstance().getTemplate(_bossId);
		_nameRaid = tmpl.getName();
		_log.warning("Nombre Raid Boss Seleccionado Para evento: " + _nameRaid);
	}
	
	public static void addPlayer(L2PcInstance player)
	{
		if (!addPlayerOk(player)) {
			return;
		}
		PLAYER_REGISTER.put(player.getObjectId(), player.getName());
		String html = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		html += central.LineaDivisora(1) + central.headFormat("RAIDBOSS EVENT") + central.LineaDivisora(1);
		html += central.headFormat("Inscripción Exitosa, ya estás participando","LEVEL") + central.LineaDivisora(1);
		html += central.headFormat("Successful Join","LEVEL") + central.LineaDivisora(1);
		html += central.headFormat("Player join: <font color=LEVEL>" + String.valueOf(PLAYER_REGISTER.size()) + "</font>","WHITE")+ central.LineaDivisora(1);
		
		String Premios = central.LineaDivisora(1) + central.headFormat("Reward", "LEVEL") + central.LineaDivisora(1);
		Premios += central.headFormat("Winner's", "A9D0F5");
		for(String PremiosWinner : general.EVENT_RAIDBOSS_REWARD_WIN.split(";")){
			Premios += "<center><font color=FFBF00>" + PremiosWinner.split(",")[1] + "</font> - " + central.getNombreITEMbyID(Integer.valueOf(PremiosWinner.split(",")[0])) + "</center><br1>";
		}
		Premios += central.LineaDivisora(1) + central.headFormat("Looser's","A9D0F5") + central.LineaDivisora(1);
		for(String PremiosWinner : general.EVENT_RAIDBOSS_REWARD_LOOSER.split(";")){
			Premios += "<center><font color=FFBF00>" + PremiosWinner.split(",")[1] + "</font> - " + central.getNombreITEMbyID(Integer.valueOf(PremiosWinner.split(",")[0])) + "</center><br1>";
		}
		Premios += central.LineaDivisora(1);

		if(DROP_RAID!=null){
			if(DROP_RAID.size()>0){
				Premios += central.LineaDivisora(1) + central.headFormat("Raid Drop","WHITE") + central.LineaDivisora(1);
				Premios += "<table width=280>%DATA%</table>";
				String Temporal = "";
				for(String Filas : DROP_RAID){
					Temporal += Filas;
				}
				Premios = Premios.replace("%DATA%", Temporal);
				Premios += central.LineaDivisora(1);
			}
		}
		
		html += "<center>" + Premios + "</center>";
		
		html += central.getPieHTML(false) + "</body></html>";
		central.sendHtml(player, html);
	}
	
	private static void removeOfflinePlayers(){
		getValidPlayer(true);
	}
	
	private static boolean addPlayerOk(L2PcInstance eventPlayer)
	{
		if(!_joining){
			String Mensaje = getNextEventTimeLess();
			central.msgbox("Event Inactive. You need to Wait. " + Mensaje, eventPlayer);
			return false;
		}
		if(_started){
			central.msgbox("Event Inactive. Event Started", eventPlayer);
			return false;			
		}
		
		try
		{
			
			if (eventPlayer.getInstanceId() > 0)
			{
				eventPlayer.sendMessage("Your on a Instance, you can not participate");
				return false;
			}
			
			
			if (eventPlayer.isOnEvent()) {
				eventPlayer.sendMessage("You are already participating in another Event");
				return false;
			}
			
			
			if(PLAYER_REGISTER.containsKey(eventPlayer.getObjectId()))
			{
				eventPlayer.sendMessage("You are already participating in the event!");
				return false;
			}
			
			if (eventPlayer.isInOlympiadMode())
			{
				eventPlayer.sendMessage("You cant join if you are registered in olympiads");
				return false;
			}
			
			/*
			 * if (!RegistrarInBd(eventPlayer)) { eventPlayer.sendMessage("Just one Account can registered"); return false; }
			 */
			
			for (L2PcInstance player : getValidPlayer(false))
			{
				if (eventPlayer.getAccessLevel().getLevel() <= 0)
				{
					if(general.EVENT_RAIDBOSS_CHECK_DUALBOX){
						if (ZeuS.isDualBox_pc(player, eventPlayer) && (!player.isGM() || !eventPlayer.isGM()))
						{
							eventPlayer.sendMessage("You are already participating in the event from this IP!");
							return false;
						}
					}
				}
				else if (player.getObjectId() == eventPlayer.getObjectId())
				{
					eventPlayer.sendMessage("You are already participating in the event!");
					return false;
				}
				else if (player.getName() == eventPlayer.getName())
				{
					eventPlayer.sendMessage("You are already participating in the event!");
					return false;
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("Raid Engine exception: " + e.getMessage());
		}
		
		if (PLAYER_REGISTER.size() < general.EVENT_RAIDBOSS_PLAYER_MAX_REGIS) {
			return true;
		}
		
		eventPlayer.sendMessage("Too many players registered");
		return false;
	}
	
	private static synchronized void addDisconnectedPlayer(L2PcInstance player)
	{

	}
	public static void removePlayer(L2PcInstance player)
	{
		if(!_joining){
			return;
		}
		
		if(_started){
			central.msgbox("You can not unregister now", player);
			return;
		}
		
		if(PLAYER_REGISTER==null){
			return;
		}
		if (PLAYER_REGISTER.containsKey(player.getObjectId())){
			if (!_joining){
				player.getAppearance().setNameColor(Integer.valueOf(getPlayerColorName(player)));
				player.broadcastUserInfo();
			}
			PLAYER_REGISTER.remove(player.getObjectId());
			PLAYER_INFO.remove(player.getObjectId());
			
			try{
				PLAYER_TO_HIT_THE_RAID.remove(player.getObjectId());
			}catch(Exception a){
				
			}
			NpcHtmlMessage nhm = new NpcHtmlMessage(5);
			final StringBuilder replyMSG = StringUtil.startAppend(1000, "<html><body>");
			replyMSG.append("<html><body>Te has Eliminado del Evento Raid Boss con Exito.</body></html>");
			nhm.setHtml(replyMSG.toString());
			player.sendPacket(nhm);
			PLAYER_REGISTER.remove(player.getObjectId());
		}
	}
	
	private static void cleanRaid()
	{
		unspawnEventBoss();
		_log.warning("Raid: Cleaning players.");
		PLAYER_INFO.clear();
		PLAYER_REGISTER.clear();
		PLAYER_TO_HIT_THE_RAID.clear();
		_joining = false;
		_started = false;
		_teleport = false;		
		_playersWon = false;
		_log.warning("Cleaning Raid done.");
		if(general.EVENT_RAIDBOSS_AUTOEVENT){
			RaidBoss_Auto_Event = false;
			IntervalEventStart();
		}
	}
	
	public static void setNewSearch(){
		RaidBoss_Auto_Event = false;
	}
	
	private static void unspawnEventBoss()
	{
		try{
			if (_bossSpawn == null) {
				return;
			}
			_bossSpawn.getLastSpawn().deleteMe();
			_bossSpawn.stopRespawn();
			SpawnTable.getInstance().deleteSpawn(_bossSpawn, true);
		}catch(Exception a){
			_log.warning("RaidBoss Event: Error unspawn RB->" + a.getMessage());
		}
	}
	
	public static boolean playerIsRaidEventInEvent(L2PcInstance player){
		if(PLAYER_REGISTER==null){
			return false;
		}
		if(PLAYER_REGISTER.size()==0){
			return false;
		}
		if(!PLAYER_REGISTER.containsKey(player.getObjectId())){
			return false;
		}
		if(!_started){
			return false;
		}
		
		return true;
	}
	
	public static void atacaAlRaid(L2PcInstance jugador, L2Npc raid)
	{
		if(PLAYER_REGISTER==null){
			return;
		}
		
		if(PLAYER_REGISTER.size()==0){
			return;
		}
		
		if(!_started){
			return;
		}
				
		if (jugador == null || raid == null) {
			return;
		}
		
		if(!_started){
			return;
		}
		
		if (raid._isEventMobRaid)
		{
			setPlayerHitRaid(jugador);
		}
	}
	
	private static void AnunciarTodos(String Mensaje)
	{
		CreatureSay strMensaje = new CreatureSay(0, 18, "", "[RB Event]" + Mensaje);
		Broadcast.toAllOnlinePlayers(strMensaje);
	}
	
	
	public static class unSpawnRaidBoss implements Runnable{
		public unSpawnRaidBoss(){

		}
		@Override
		public void run(){
			try{
				_bossSpawn.getLastSpawn().isInvisible();
			}catch(Exception a){

			}
			try{
				unspawnEventBoss();
			}catch(Exception a){
				
			}
		}
	}	
	
	
	private static void teleportFinish()
	{
		AnnounceToPlayers(false, "Teleport back to participation Spot in " + String.valueOf(general.EVENT_RAIDBOSS_SECOND_TO_BACK) + " seconds!");
		
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
			@SuppressWarnings("synthetic-access")
			public void run()
			{
				for (L2PcInstance player : getValidPlayer(false)){
					if (player != null)
					{
						String colorName = "";
						try{
							colorName = getPlayerColorName(player);
							player.getAppearance().setNameColor(Integer.valueOf(colorName));							
						}catch(Exception a){
							player.getAppearance().setNameColor(0);
						}
						player.setTeam(Team.NONE);
						String[] locacion = getPlayerTele(player).split(",");
						if (player.isOnline()) {
							player.teleToLocation(Integer.valueOf(locacion[0]), Integer.valueOf(locacion[1]), Integer.valueOf(locacion[2]), false);
						}else{
							Connection con = null;
							try
							{
								con = ConnectionFactory.getInstance().getConnection();
								
								PreparedStatement statement = con.prepareStatement("UPDATE characters SET x=?, y=?, z=? WHERE char_name=?");
								statement.setInt(1, Integer.valueOf(locacion[0]));
								statement.setInt(2, Integer.valueOf(locacion[1]));
								statement.setInt(3, Integer.valueOf(locacion[2]));
								statement.setString(4, player.getName());
								statement.execute();
								statement.close();
							}
							catch (SQLException se)
							{
								_log.warning("Raid Engine exception: " + se.getMessage());
							}
							finally
							{
								try
								{
									if (con != null) {
										con.close();
									}
								}
								catch (SQLException e)
								{
									e.printStackTrace();
								}
							}
						}
					}
				}
				_log.warning("Raid: Teleport done.");
				cleanRaid();
			}
		}, general.EVENT_RAIDBOSS_SECOND_TO_BACK * 1000);
	}
}