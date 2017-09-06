package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.zone.ZoneId;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.ZeuS.ZeuS;
import ZeuS.procedimientos.opera;
import ZeuS.server.Anunc;

public class captchaPLayer {
	private static final Logger _log = Logger.getLogger(captchaPLayer.class.getName());
	private static HashMap<L2PcInstance, Integer> Player_UnixTime = new HashMap<L2PcInstance, Integer>();
	private static HashMap<L2PcInstance, Integer> PlayerCodigToJail = new HashMap<L2PcInstance, Integer>();
	private static HashMap<L2PcInstance, Integer> PlayerVecesEquivocado = new HashMap<L2PcInstance, Integer>();
	private static int getUnixTime(){
		int unixTime = (int) (System.currentTimeMillis()/1000);
		return unixTime;
	}
	
	private static int blackListedChar(L2PcInstance cha){
		int retorno = 1;

		String NombreChar = cha.getName();
		int IdChar = cha.getObjectId();
		
		String qry = "call sp_antibot_blacklist(?,?)";
		Connection conn = null;
		CallableStatement psqry;
		String Respu ="";
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			psqry.setInt(1, IdChar);
			psqry.setString(2, NombreChar);
			ResultSet rss = psqry.executeQuery();
			rss.next();
			Respu = rss.getString(1);
		}catch(SQLException e){
			_log.warning("-->Antibot blacklist error:"+e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){
			_log.warning("-->Antibot blacklist error:"+a.getMessage());
		}

		return retorno;
	}
	
	private static String getAllPlayerNameOnThisRed(L2PcInstance player){
		String Nombres = "";
		if(!general.ANTIBOT_SEND_JAIL_ALL_DUAL_BOX){
			return "";
		}
		
		Vector<L2PcInstance> PJ = opera.getAllPlayerOnThisIp(player);
		
		if(PJ==null){
			return "";
		}
		
		if(PJ.size()==0){
			return "";
		}
		
		for(L2PcInstance cha : PJ){
			try{
				if(cha != null){
					if(cha.isOnline()){
						if(!cha.getClient().isDetached()){
							if(Nombres.length()>0){
								Nombres += ", " + cha.getName();
							}
						}
					}
				}
			}catch(Exception a){
				
			}
		}
		
		return Nombres;
	}
	

	public static void sendAllPlayerJailByIP(L2PcInstance player){
		if(!general.ANTIBOT_SEND_JAIL_ALL_DUAL_BOX){
			return;
		}
		
		Vector<L2PcInstance> PJ = opera.getAllPlayerOnThisIp(player);
		
		if(PJ==null){
			return;
		}
		
		if(PJ.size()==0){
			return;
		}
		
		for(L2PcInstance cha : PJ){
			try{
				if(cha != null){
					if(cha.isOnline()){
						if(!cha.getClient().isDetached()){
							_log.warning("Antibot System has Jailed "+cha.getName()+" becouse the main " + player.getName()+" are Using Bot.");
							opera.sendToJail(cha, general.ANTIBOT_MINUTOS_JAIL);
						}
					}
				}
			}catch(Exception a){
				
			}
		}
	}
	
	public static void setLastKillTime(L2PcInstance player){
		try{
			Player_UnixTime.put(player, getUnixTime());
		}catch(Exception a){
			_log.warning("ZeuS Error getting last time kill -> " + a.getMessage());
		}
	}

	public static void cancelbotCheck(L2PcInstance player, String parametros){
		if(!player.isGM()){
			return;
		}

		L2PcInstance chaTarget = null;

		if(parametros==null){
			L2Object target = player.getTarget();
			if(target instanceof L2PcInstance){
				chaTarget = (L2PcInstance)target;
			}
		}else{
			try{
				chaTarget = L2World.getInstance().getPlayer(parametros);
			}catch (Exception e){
			}

			if (chaTarget == null){
				player.sendMessage("The player " + parametros + " is not online or not exist");
			}
		}

		if(chaTarget!=null){
			if(chaTarget != player){
				if(general.isBotCheckPlayer(chaTarget)){
					cleanPlayer(chaTarget);
					central.msgbox("GM has remove the antibot check from you", chaTarget);
					central.msgbox("You remove the antibot check from " + chaTarget.getName(), player);
					String HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
					HTML += central.LineaDivisora(1) + central.headFormat("Antibot System") + central.LineaDivisora(1);
					HTML += central.LineaDivisora(1) + central.headFormat("You antibot check was removed By GM.","LEVEL") + central.LineaDivisora(1);
					HTML += central.LineaDivisora(1) + central.getPieHTML() + "</body></html>";
					central.sendHtml(chaTarget, HTML);
				}
			}
		}

	}

	private static void sendAllCharInZone(L2PcInstance player){
		//opera.isInGeoZone
		int contador = 0;
		Collection<L2PcInstance> pls = opera.getAllPlayerOnWorld();
		for(L2PcInstance onlinePlayer : pls){
			if(opera.isInGeoZone(onlinePlayer, player)){
				if(!onlinePlayer.isGM()){
					if(isPlayerValid(onlinePlayer)){
						PlayerVecesEquivocado.put(player, 0);
						contador++;
						setCheckBot(onlinePlayer, player.getName(),0);
						central.msgbox(msg.BOT_VERIFICATION_SEND_TO_$player.replace("$player",onlinePlayer.getName()), player);
					}
				}
			}
		}
		central.msgbox("Checkbot send to " + String.valueOf(contador) + " player in you zone." , player);
	}

	public static void sendCheckBootZone(L2PcInstance player){
		sendAllCharInZone(player);
	}


	public static void checkSendAntibot(L2PcInstance player){
		int getMobKill = general.getMobKillAntibot(player);
		if(getMobKill >= general.ANTIBOT_MOB_DEAD_TO_ACTIVATE){
			PlayerVecesEquivocado.put(player, 0);
			checkboot(player,true);
		}
	}

	protected static boolean isPlayerValid(L2PcInstance player){
		return isPlayerValid(player, true, true);
	}


	protected static boolean isPlayerValid(L2PcInstance player, boolean ShowMensaje,boolean respetarConfigZeuS){
		Object target = player.getTarget();


		if(!(target instanceof L2PcInstance)){
			central.msgbox(msg.THE_SELECT_OBJECT_IS_NOT_A_PLAYER, player);
			return false;
		}
		L2PcInstance targetChar = (L2PcInstance)target;

		if(!player.isGM()){
			if(Player_UnixTime!=null){
				if(Player_UnixTime.containsKey(target)){
					int ActualUnix = getUnixTime();
					int TiempoTranscurrido = ActualUnix - Player_UnixTime.get(target);
					if(TiempoTranscurrido >= ( general.ANTIBOT_INACTIVE_MINUTES * 60 ) ){
						if(ShowMensaje){
							central.msgbox(msg.BOT_INACTIVE_X_THAN_$minutes.replace("$minutes", String.valueOf(general.ANTIBOT_INACTIVE_MINUTES)), player);
						}
						return false;
					}
				}else{
					if(ShowMensaje){
						central.msgbox(msg.BOT_INACTIVE_X_THAN_$minutes.replace("$minutes", String.valueOf(general.ANTIBOT_INACTIVE_MINUTES)), player);
					}
					return false;
				}
			}else{
				if(ShowMensaje){
					central.msgbox(msg.BOT_INACTIVE_X_THAN_$minutes.replace("$minutes", String.valueOf(general.ANTIBOT_INACTIVE_MINUTES)), player);
				}
				return false;
			}
		}

		if(targetChar.isSitting()){
			central.msgbox("Player is Sitting", player);
			return false;
		}
		
		if(targetChar.isOlympiadStart() || player.isOlympiadStart() || targetChar.isInOlympiadMode() || player.isInOlympiadMode()){
			if(ShowMensaje){
				central.msgbox(msg.BOT_CAN_NOT_SEND_IN_OLY, player);
			}
			return false;
		}

		if(targetChar.equals(player)){
			if(ShowMensaje){
				central.msgbox(msg.BOT_CAN_NOT_SEND_IN_YOUR_SELF, player);
			}
			return false;
		}

		if(targetChar.isGM()){
			if(ShowMensaje){
				central.msgbox("This command not work on adm/gm", player);
			}
			return false;
		}

		if(opera.isMaster(player)){
			return true;
		}



		if(general.ANTIBOT_CHECK_DUALBOX){
			if(ZeuS.isDualBox_pc(player, targetChar)){
				if(ShowMensaje){
					central.msgbox(msg.BOT_CAN_NOT_SEND_IN_YOUR_SELF, player);
				}
				return false;
			}
		}

		if(!general.ANTIBOT_COMANDO_STATUS){
			if(ShowMensaje){
				central.msgbox("This command it is disabled by Admin", player);
			}
			return false;
		}

		if(targetChar.isGM()){
			if(ShowMensaje){
				central.msgbox("This command not work on adm/gm", player);
			}
			return false;
		}

		if(respetarConfigZeuS){
			if(general.getTime_antibot(targetChar) > opera.getUnixTimeNow()){
				if(ShowMensaje){
					int NextCheck = general.getTime_antibot(targetChar) - opera.getUnixTimeNow();
					int Minutos = NextCheck/60;
					String strNextCheck = String.valueOf(Minutos) + ":" + String.valueOf( NextCheck - (Minutos * 60) );
					central.msgbox(msg.BOT_RECENTLY_BEEN_SENT_TO_THIS_PLAYER_VERIFICATION_EVERY_$timeEvery_NEXT_CHECK_IN_$timeNextCheck.replace("$timeEvery", String.valueOf(general.ANTIBOT_MINUTE_VERIF_AGAIN).replace("$timeNextCheck", strNextCheck) ), player);
				}
				return false;
			}
			if(general.ANTIBOT_NOBLE_ONLY && !player.isNoble()){
				if(ShowMensaje){
					central.msgbox(msg.BOT_THIS_COMMAND_ONLY_NOBLE, player);
				}
				return false;
			}

			if(general.ANTIBOT_HERO_ONLY && !player.isHero()){
				if(ShowMensaje){
					central.msgbox(msg.BOT_THIS_COMMAND_ONLY_HERO, player);
				}
				return false;
			}

			if(general.ANTIBOT_GM_ONLY && !player.isGM()){
				if(ShowMensaje){
					central.msgbox("This command is GM Only", player);
				}
				return false;
			}

			if(general.ANTIBOT_MIN_LVL>player.getLevel()){
				if(ShowMensaje){
					central.msgbox(msg.BOT_COMMAND_IS_ONLY_FOR_PLAYER_WHITH_$level.replace("$level", String.valueOf(general.ANTIBOT_MIN_LVL)), player);
				}
				return false;
			}

			if(general.ANTIBOT_ANTIGUEDAD_MINUTOS!=0){
				if (opera.getMinutosVida(player) < general.ANTIBOT_ANTIGUEDAD_MINUTOS){
					if(ShowMensaje){
						central.msgbox(msg.BOT_COMMAND_IS_ONLY_WITH_LIFETIME_OVER_$lifetime.replace("$lifetime", String.valueOf(general.ANTIBOT_ANTIGUEDAD_MINUTOS)), player);
					}
					return false;
				}
			}
		}

		Object targetFromTarget = targetChar.getTarget();

		if(targetFromTarget instanceof L2Npc){
			L2Npc tfTarget = (L2Npc)targetFromTarget;
			if(tfTarget.isRaid()){
				if(ShowMensaje){
					central.msgbox(msg.BOT_THIS_PLAYER_ARE_KILLING_A_RAID_BOSS, player);
				}
				return false;
			}
		}


		if(targetChar.getKarma()>0){
			if(ShowMensaje){
				central.msgbox(msg.BOT_THIS_PLAYER_HAVE_KARMA, player);
			}
			return false;
		}

		if(targetChar.isInSiege()){
			if(ShowMensaje){
				central.msgbox("The selected player is in Siege, can not send the bot verification", player);
			}
			return false;
		}

		if (targetChar.isInsideZone(ZoneId.TOWN)){
			if(ShowMensaje){
				central.msgbox("The selected player is in Town, can not send the bot verification", player);
			}
			return false;
		}

		if(targetChar.isInsideZone(ZoneId.PEACE) && !general.ANTIBOT_CHECK_INPEACE_ZONE){
			if(ShowMensaje){
				central.msgbox("The selected player is in Peace Zone, can not send the bot verification", player);
			}
			return false;
		}

		if((targetChar.getPvpFlag() != 0) || targetChar.isCombatFlagEquipped()){
			if(ShowMensaje){
				central.msgbox("The selected player are in PVP Mode, can not send the bot verification", player);
			}
			return false;
		}

		return true;
	}

	public static void checkbootenterW(L2PcInstance player){
		if(!isPlayerValid(player)){
			return;
		}
		sendJail(player);
		central.msgbox(msg.BOT_SYSTEM_SEND_TO_JAIL_FOR_$time_MINUTES_FOR_LOGOUT.replace("$time", String.valueOf(general.ANTIBOT_MINUTOS_JAIL)), player);
	}

	public static void checkboot(L2PcInstance player){
		checkboot(player,false);
	}


	public static void checkboot(L2PcInstance player, boolean isAuto){
		if(!isPlayerValid(player) && !isAuto){
			return;
		}

		L2PcInstance PlayerCheck = null;

		if(isAuto){
			PlayerCheck = player;
		}else{
			L2Object target = player.getTarget();
			if(target instanceof L2PcInstance){
				PlayerCheck = (L2PcInstance)target;
			}else{
				return;
			}
		}
		PlayerVecesEquivocado.put(player, 0);
		setCheckBot(PlayerCheck, player.getName(),0);
		if(!isAuto){
			central.msgbox(msg.BOT_VERIFICATION_SEND_TO_$player.replace("$player", PlayerCheck.getName()), player);
		}else{
			general.resetKillAntibot(PlayerCheck);
		}
	}

	protected static int getPosi_ID(String idPregunta){
		int retorno = 0;
		int Contador = 0;
		for(String[] Pregunta : general.PREGUNTAS_BOT){
			if(Pregunta[0]!=null){
				if(Pregunta[0].equals(idPregunta)){
					return Contador;
				}
				Contador++;
			}
		}

		return retorno;
	}

	public static void bypass(L2PcInstance player, String command){

		if(PlayerVecesEquivocado ==null){
			PlayerVecesEquivocado.put(player, 0);
		}else if(!PlayerVecesEquivocado.containsKey(player)){
			PlayerVecesEquivocado.put(player, 0);
		}

		if(command.startsWith("checkbot")){
			if(!isPlayerValid(player)){
				return;
			}
			L2Object target = player.getTarget();
			if(target instanceof L2PcInstance){
				PlayerVecesEquivocado.put(player, 0);
				setCheckBot((L2PcInstance)target, player.getName(),0);
			}
			central.msgbox(msg.BOT_VERIFICATION_SEND_TO_$player.replace("$player", ((L2PcInstance)target).getName()), player);
		}else if(command.startsWith("zeusBoot")) {
			String Comando[] = command.split(" ");
			if(Comando.length==5){
				String idPregunta = Comando[3];
				String Respuest_dada = Comando[4];
				if(general.PREGUNTAS_BOT[getPosi_ID(idPregunta)][2].equals(Respuest_dada)){
					cleanPlayer(player);
					central.msgbox(msg.BOT_THE_PLAYER_IS_NOT_A_BOT, player);
				}else{
					PlayerVecesEquivocado.put(player, PlayerVecesEquivocado.get(player) + 1);
					setCheckBot(player, Comando[2], PlayerVecesEquivocado.get(player)) ;
				}
			}else{
				setCheckBot(player, Comando[2], PlayerVecesEquivocado.get(player) + 1 );
			}
		}
		//setCheckBot((L2PcInstance)player.getTarget(),player);
	}

	protected static void setCheckBot(L2PcInstance targetPlayer, String playerCheck, int VecesIntento){
		central.msgbox("If you close this window, it will appear in "+ String.valueOf(general.ANTIBOT_SECONDS_TO_RESEND_ANTIBOT) +" seconds again.", targetPlayer);
		opera.setImmbileChar(targetPlayer, true);
		Random aleatorio = new Random();
		int RandPregunta = aleatorio.nextInt(general.PREGUNTAS_BOT_CANT);
		String txtRespuesta = "<edit var=\"txt_respu\" width=160>";

		String idPregunta = general.PREGUNTAS_BOT[RandPregunta][0];
		String Pregunta = general.PREGUNTAS_BOT[RandPregunta][1];

		String btnBypass = "bypass -h voice .zeusBoot "+String.valueOf(VecesIntento) + " " + playerCheck + " " +idPregunta + " $txt_respu";
		String BtnAdd = "<button value=\"Check Answer\" action=\""+ btnBypass +"\" width=180 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";


		String HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		HTML += central.LineaDivisora(1) + central.headFormat("Antibot Check player")+central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat( msg.ANTIBOT_CHECK_SENT_BY_$name.replace("$name", playerCheck.replace("_", " ")),"LEVEL") + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat(msg.ANTIBOT_CHECK_ANWER_THIS_ANSWER + "<br1><font color=FF8000>"+Pregunta+"</font>"+txtRespuesta+"<br>"+BtnAdd+"<br>","FFBF00") + central.LineaDivisora(1);
		//
		HTML += central.LineaDivisora(1) + central.headFormat( msg.ANTIBOT_CHECK_ONLY_$minutes_TO_ANSWER.replace("$minutes", "<font color=FF8000>"+String.valueOf(general.ANTIBOT_MINUTOS_ESPERA)+"</font>"),"FFBF00") + central.LineaDivisora(1);
		//HTML += central.LineaDivisora(1) + central.headFormat("You have only <font color=FF8000>"+String.valueOf(general.ANTIBOT_MINUTOS_ESPERA)+"</font> minute to answer.","FFBF00") + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat(msg.ANTIBOT_ATTEMP_NUMBER_$number.replace("$number", "<font color=FF8000>"+String.valueOf(VecesIntento)+"</font>"),"FFBF00") + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat(msg.ANTIBOT_PUNISHMENT_TIME_$minutes_in_jail.replace("$minutes_in_jail",
																"<font color=FF8000>" + String.valueOf(general.ANTIBOT_MINUTOS_JAIL) + (general.ANTIBOT_BORRAR_ITEM ? " " + msg.ANTIBOT_AND_REMOVE_SOME_ITEM :".") + "</font>"),"FFBF00") + central.LineaDivisora(1);
		HTML += central.getPieHTML() + "</center></body></html>";

		central.sendHtml(targetPlayer, HTML);

		if(VecesIntento >= general.ANTIBOT_OPORTUNIDADES){
			sendJail(targetPlayer);
			general.removeBotPlayer(targetPlayer);
			return;
		}

		general.setTime_antibot(targetPlayer);

		if(general.ANTIBOT_SEND_ALL_IP){
			Anunc.Anunciar_All_Char_IP(targetPlayer,"Antibot check was send to " + targetPlayer.getName(),"ANTIBOT",false);
		}

		/**Reenvio de la ventana*/
		ThreadPoolManager.getInstance().scheduleGeneral(new CaptchaSendBackHTML(targetPlayer, VecesIntento), (general.ANTIBOT_SECONDS_TO_RESEND_ANTIBOT) * 1000);
		/**Reenvio de la ventana*/

		if(!general.isBotCheckPlayer(targetPlayer)){
			central.msgbox(msg.BOT_$player_HAVE_SENT_YOU_A_ANTIBOT_VERIFICATION.replace("$player", playerCheck) , targetPlayer);
			general.addBotPlayer(targetPlayer);
			PlayerCodigToJail.put(targetPlayer, getUnixTime());
			ThreadPoolManager.getInstance().scheduleGeneral(new CaptchaTimerPlayer(targetPlayer,PlayerCodigToJail.get(targetPlayer)), (general.ANTIBOT_MINUTOS_ESPERA * 60) * 1000);
		}
	}

	protected static void cleanPlayer(L2PcInstance activeChar){
		opera.setImmbileChar(activeChar, false);
		general.removeBotPlayer(activeChar);
		general.resetKillAntibot(activeChar);
		PlayerCodigToJail.put(activeChar, 0);
	}

	protected static void sendJail(L2PcInstance activeChar){

		if(!general.isBotCheckPlayer(activeChar)){
			return;
		}

		general.removeBotPlayer(activeChar);

		try{
			opera.setImmbileChar(activeChar, false);
		}catch(Exception a ){

		}
		central.msgbox(msg.BOT_YOU_HAVE_BEEN_SEND_TO_JAIL_FOR_NOT_ENTER_THE_RIGHT_PASS_INTIME_$time.replace("$time", String.valueOf(general.ANTIBOT_MINUTOS_ESPERA)), activeChar);
		if (activeChar.isTransformed()) {
			activeChar.untransform();
		}
		
		int tiempoInJail = general.ANTIBOT_MINUTOS_JAIL;
		if(general.ANTIBOT_BLACK_LIST){
			try{
				int vecesRepetidas = blackListedChar( activeChar );
				tiempoInJail = tiempoInJail * vecesRepetidas;
			}catch(Exception a){
				tiempoInJail = general.ANTIBOT_MINUTOS_JAIL;
			}
		}
		
		opera.sendToJail(activeChar, tiempoInJail);
		sendAllPlayerJailByIP(activeChar);
		
		String PlayersName = getAllPlayerNameOnThisRed(activeChar);
		
		PlayersName = PlayersName.length()>0 ? activeChar.getName() +", "+PlayersName : activeChar.getName();
		
		if(general.ANTIBOT_ANNOU_JAIL){
			String Mensaje = msg.BOT_ANNOUCEMENT_WHEN_$player_IS_SEND_TO_JAIL_FOR_$time_MINUTER.replace("$player", PlayersName).replace("$time", String.valueOf(tiempoInJail)) + ( general.ANTIBOT_BORRAR_ITEM ? " " + msg.BOT_ANNOUCEMENT_REMOVE_ITEM : "" ) ;
			opera.AnunciarTodos("Antibot", Mensaje);
		}

		RemoveItemFromInventary(activeChar);

	}

	protected static void RemoveItemFromInventary(L2PcInstance player){
		if((general.ANTIBOT_BORRAR_ITEM_ID.length()==0) || !general.ANTIBOT_BORRAR_ITEM){
			return;
		}

		for(String IdItem : general.ANTIBOT_BORRAR_ITEM_ID.split(",")){
			try{
				long IteminChar = player.getInventory().getInventoryItemCount(Integer.valueOf(IdItem),-1);
				if(IteminChar>0){
					long CantidadRemover = Porcent( IteminChar );
					if(CantidadRemover>0){
						opera.removeItem(Integer.valueOf(IdItem), CantidadRemover, player);
					}
				}
			}catch(Exception a ){

			}
		}

	}

	private static long Porcent(long valor){
		double retorno = 0;
		double multi = valor * (general.ANTIBOT_BORRAR_ITEM_PORCENTAJE / 100.0);
		retorno = multi;
		return (long) retorno;
	}

	public static class CaptchaSendBackHTML implements Runnable{
		L2PcInstance activeChar;
		int _VecesIntentado;
		public CaptchaSendBackHTML(L2PcInstance player, int VecesIntentado){
			activeChar = player;
			_VecesIntentado = VecesIntentado;
		}
		@Override
		public void run(){
			try{
				if(_VecesIntentado == PlayerVecesEquivocado.get(activeChar)){
					if(general.isBotCheckPlayer(activeChar) && !activeChar.isJailed()){
						central.msgbox("RESEND ANTIBOT CHECK WINDOWS", activeChar);
						setCheckBot(activeChar,"Auto_System",_VecesIntentado);
					}
				}
			}catch(Exception a){

			}
		}
	}

	public static class CaptchaTimerPlayer implements Runnable{
			L2PcInstance activeChar;
			int CodigoEntrada;
			public CaptchaTimerPlayer(L2PcInstance player, int CodEntrada){
				activeChar = player;
				CodigoEntrada = CodEntrada;
			}
			@Override
			public void run(){
				try{
					if(general.isBotCheckPlayer(activeChar) && !activeChar.isJailed()){
						if(PlayerCodigToJail.get(activeChar) == CodigoEntrada){
							sendJail(activeChar);
						}
					}
				}catch(Exception a){

				}

			}
		}
}
