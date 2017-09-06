package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.ZeuS.ZeuS;
import ZeuS.procedimientos.opera;
import ZeuS.server.httpResp;

public class votereward {

	/*
	 * 1 = Hopzone
	 * 2 = Topzone
	*/

	private static final Logger _log = Logger.getLogger(Config.class.getName());

	protected static int Vhop, Vtop,nextRw_H, nextRw_T;
	
	private static HashMap<String,Integer> VotedTime = new HashMap<String,Integer>();
	private static int SegundosEsperaEntreValidacion = 43200;
	
	
	private static HashMap<Integer,HashMap<String, String>>PLAYER_AFK_POSITION = new HashMap<Integer,HashMap<String, String>>();

	protected static boolean Inicializado = false;
	
	private static HashMap<L2PcInstance, Boolean> IsInVoteAction = new HashMap<L2PcInstance, Boolean>();

	private static boolean canRewardVoto(L2PcInstance player, int Voto, int Web){
		boolean retorno = false;
		String qry = "call sp_zeus_voto(?,?,?)";
		Connection conn = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			CallableStatement psqry = conn.prepareCall(qry);
			psqry.setInt(1, player.getObjectId());
			psqry.setInt(2, Voto);
			psqry.setInt(3, Web);
			ResultSet rss = psqry.executeQuery();
			rss.next();
			if(rss.getString(1).equals("cor")){
				retorno = true;
			}else{
				retorno = false;
			}
		}
		catch (SQLException e){

		}

		try{
			conn.close();
		}catch(SQLException e){

		}
		return retorno;
	}

	public static String MainMenuVoteReward(L2PcInstance player){
				if(!general._activated()){
					return "";
				}
				String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
				MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Vote Reward") + central.LineaDivisora(2);
				MAIN_HTML += central.headFormat(msg.VOTEREWARD_REMEMBER_VOTE_12,"FCAF06") + central.LineaDivisora(2);
				MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.VOTEREWARD_MENSAJE,"WHITE") + central.LineaDivisora(2);
				//MAIN_HTML += central.LineaDivisora(2) + central.headFormat("<button value=\"Instructions\" action=\"bypass -h ZeuSNPC VoteReward 3 0 0\" width=200 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","WHITE") + central.LineaDivisora(2);

				String MAIN_HTML_VOTOS = "";

				if(general.VOTE_SHOW_ZEUS_ONLY_BY_ITEM && general.VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM){
					if(!opera.haveItem(player, general.VOTE_SHOW_ZEUS_ONLY_BY_ITEM_ID, 1) && !opera.haveItem(player, general.VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM_ID, 1)){
						String Parabtn = "Buy " + central.getNombreITEMbyID(general.VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM_ID) + " to used ZeuS Temporally";
						Parabtn = "You need to by "+ central.getNombreITEMbyID(general.VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM_ID) +" for Used this NPC.<br><button value=\""+ central.getNombreITEMbyID(general.VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM_ID) +"\" action=\"bypass -h ZeuSNPC VoteReward 4 1 0\" width=200 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
						MAIN_HTML_VOTOS += central.LineaDivisora(1) + central.headFormat(Parabtn,"LEVEL") + central.LineaDivisora(1);
					}
				}

				MAIN_HTML += central.LineaDivisora(2) + central.headFormat("You have only " + String.valueOf(general.VOTO_REWARD_SEG_ESPERA) +"s. to vote" ,"LEVEL");

				if(general.VOTO_REWARD_ACTIVO_HOPZONE && general.VOTO_REWARD_ACTIVO_TOPZONE){
					//MAIN_HTML_VOTOS += "<button value=\"HOPZONE\" action=\"bypass -h ZeuSNPC VoteReward 0 1 0\" width=200 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
					MAIN_HTML_VOTOS += "<button value=\"Begin\" action=\"bypass -h ZeuSNPC VoteReward_new 0 1 0\" width=200 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";					
				}
/*
				if (general.VOTO_REWARD_ACTIVO_TOPZONE){
					MAIN_HTML_VOTOS += "<button value=\"TOPZONE\" action=\"bypass -h ZeuSNPC VoteReward 0 2 0\" width=200 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				}*/

				if(MAIN_HTML_VOTOS.length()>0){
					MAIN_HTML += central.headFormat(MAIN_HTML_VOTOS,"") + central.LineaDivisora(2);
				}

				//MAIN_HTML += central.LineaDivisora(2) + central.headFormat("<button value=\"Premios\" action=\"bypass -h ZeuSNPC VoteReward 0 3 0\" width=200 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL") + central.LineaDivisora(2);


				MAIN_HTML += central.BotonGOBACKZEUS() + central.getPieHTML() + "</body></html>";

				return MAIN_HTML;
	}
/*
	private static String getMenuVoteHopzone(L2PcInstance player){
		String Milesegundos = String.valueOf(System.currentTimeMillis() / 1000);
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Vote Reward HOPZONE") + central.LineaDivisora(2);
		int VotosEntrada = httpResp.getVotes(1);
		if(VotosEntrada > 0){
			central.SetBlockTimeVote(player);
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Tenemos <font color \"LEVEL\">"+ String.valueOf(VotosEntrada) +"</font> Votos en H.Z., tu tienes que<br1>votar ahora y si tu voto es valido, tendras<br1>que precionar <font color = \"FF8A02\">\"I voted, check it\"</font> para ver<br1>tu suerte","WHITE") + central.LineaDivisora(2);
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat("<button value=\"I voted, check it\" action=\"bypass -h ZeuSNPC VoteReward 1 "+String.valueOf(VotosEntrada) + " hopzone " + Milesegundos + "\" width=180 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL") + central.LineaDivisora(2);
		}else{
			MAIN_HTML += "<br>";
		}
		MAIN_HTML += central.LineaDivisora(2) + central.BotonGOBACKZEUS() + central.getPieHTML() + "</center></body></html>";
		return MAIN_HTML;
	}

	private static String getMenuVoteTopzone(L2PcInstance player){
		String Milesegundos = String.valueOf(System.currentTimeMillis() / 1000);
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Vote Reward TOPZONE") + central.LineaDivisora(2);
		int VotosEntrada = httpResp.getVotes(2);
		if(VotosEntrada > 0){
			central.SetBlockTimeVote(player);
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Tenemos <font color \"LEVEL\">"+ String.valueOf(VotosEntrada) +"</font> Votos en H.Z., tu tienes que<br1>votar ahora y si tu voto es valido, tendras<br1>que precionar <font color = \"FF8A02\">\"I voted, check it\"</font> para ver<br1>tu suerte","WHITE") + central.LineaDivisora(2);
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat("<button value=\"I voted, check it\" action=\"bypass -h ZeuSNPC VoteReward 1 "+String.valueOf(VotosEntrada) + " topzone " + Milesegundos + "\" width=180 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL") + central.LineaDivisora(2);
		}else{
			MAIN_HTML += "<br>";
		}
		MAIN_HTML += central.LineaDivisora(2) + central.BotonGOBACKZEUS() + central.getPieHTML() + "</center></body></html>";
		return MAIN_HTML;
	}

*/
	public static void getVoteWindows(L2PcInstance player, boolean isCommand){
		int PVotoHopzone = -1;
		int PVotoTopzone = -1;
		
		if(IsInVoteAction!=null){
			if(IsInVoteAction.containsKey(player)){
				if(IsInVoteAction.get(player)){
					central.msgbox("You are in voting process, please wait.", player);
					return;
				}
			}
		}
		
		String IPWanFromVotePpl = ZeuS.getIp_Wan(player);
		int UnixNow = opera.getUnixTimeNow();
		
		if(VotedTime!=null){
			if(VotedTime.containsKey(IPWanFromVotePpl)){
				int Inicio_A = VotedTime.get(IPWanFromVotePpl);
				if( (Inicio_A + SegundosEsperaEntreValidacion) > UnixNow ){
					int Esperar = (Inicio_A + SegundosEsperaEntreValidacion) - UnixNow;
					String Horas = opera.getTiempoON(Esperar);
					central.msgbox("You have already Voted. U can Vote Every 12 Hours. You need to wait " + Horas + " to vote again", player);
					return;
				}
			}
		}
		
		central.msgbox("Capturing information votes. Please wait", player);
		
		if(httpResp.haveVoteHopZone() && general.VOTO_REWARD_ACTIVO_HOPZONE){
			PVotoHopzone = httpResp.getHopZoneVote();
		}
		if(httpResp.haveVoteTopZone() && general.VOTO_REWARD_ACTIVO_TOPZONE){
			PVotoTopzone = httpResp.getTopZoneVote();
		}
		
		String mensaje = PVotoHopzone>=0 ? "We have <font color=LEVEL>"+ String.valueOf(PVotoHopzone) +"</font> on <font color=FF8000>Hopzone</font>" : "";
		mensaje += mensaje.length()>0 ? ( PVotoTopzone>=0  ? "<br1> and <br1>" : "" ) : "";
		mensaje += PVotoTopzone>=0 ? "We have <font color=LEVEL>"+ String.valueOf(PVotoTopzone) +"</font> on <font color=FF8000>Topzone</font>" : "";
		central.SetBlockTimeVote(player);
		IsInVoteAction.put(player, true);
		
		
		if(general.VOTE_EVERY_12_HOURS){
			VotedTime.put(IPWanFromVotePpl, UnixNow);
		}
		
		ThreadPoolManager.getInstance().scheduleGeneral(new autoCheckWindows(player,PVotoHopzone,PVotoTopzone), (general.VOTO_REWARD_SEG_ESPERA) * 1000);
		String html = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		html += central.LineaDivisora(1) + central.headFormat("Vote Reward") + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.LineaDivisora(3) + central.headFormat(mensaje) + central.LineaDivisora(3) + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat("You have only <font color=E1F5A9>" + String.valueOf(general.VOTO_REWARD_SEG_ESPERA) + "</font> second's to vote") + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat("If the vote is valid you will be reward") + central.LineaDivisora(1);
		html += central.getPieHTML(false) + "</body></html>";
		opera.enviarHTML(player, html);
	}
	
	
	private static void getCheckVotedWindows(L2PcInstance player, int HZ_Antes, int TZ_Antes){
		int DIFERECIAACEPTABLE = 6;
		int actualHZ = 0, actualTZ = 0;
		int diferencia = 0;
		int checkVeces = 5;
		boolean premiarHZ = false, premiarTZ = false;
		if(httpResp.haveVoteHopZone()){
			int Contador = 0;
			while(Contador < checkVeces ){
				actualHZ = httpResp.getHopZoneVoteNow();
				if(actualHZ > HZ_Antes){
					Contador=100;
				}
				Contador++;
			}
			//actualHZ = httpResp.getHopZoneVote();
			diferencia = actualHZ - HZ_Antes;
			if(diferencia >= 1 && diferencia <= DIFERECIAACEPTABLE){
				premiarHZ = true;
			}
		}
		if(httpResp.haveVoteTopZone()){
			int Contador = 0;
			while(Contador < checkVeces){
				actualTZ = httpResp.getVoteTopZoneNow();
				if(actualTZ > TZ_Antes){
					Contador = 100;
				}
				Contador++;
			}
			actualTZ = httpResp.getTopZoneVote();
			diferencia = actualTZ - TZ_Antes;
			if(diferencia >= 1 && diferencia <= DIFERECIAACEPTABLE){
				premiarTZ = true;
			}			
		}
		
		central.msgbox("Validation process initiated. Please Wait", player);
		
		IsInVoteAction.put(player, false);
		
		if(premiarHZ){
			opera.giveReward(player, general.VOTO_REWARD_HOPZONE);
			if(general.VOTE_REWARD_PERSONAL_VOTE_ALL_IP_PLAYER){
				central.msgbox("(Hopzone)All player on ur Connection was Rewarded", player);
				opera.GiveRewardAllConnect(player,general.VOTO_REWARD_HOPZONE);
			}
		}
		if(premiarTZ){
			opera.giveReward(player, general.VOTO_REWARD_TOPZONE);
			if(general.VOTE_REWARD_PERSONAL_VOTE_ALL_IP_PLAYER){
				central.msgbox("(Topzone)All player on ur Connection was Rewarded", player);
				opera.GiveRewardAllConnect(player,general.VOTO_REWARD_TOPZONE);
			}
		}
		String html = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		html += central.LineaDivisora(1) + central.headFormat("Vote Reward Check") + central.LineaDivisora(1);
		String mensaje = "";
		mensaje = "<font color=FF8000>Hopzone</font><br1>" + (premiarHZ ? "Congratulations!!<br1>Your vote was completed successfully" : "Your vote was not completed.<br1>Before: <font color=F7D358>"+ String.valueOf(HZ_Antes) +"</font><br1>After: <font color=F7D358>"+ String.valueOf(actualHZ) +"</font><br>");
		html += mensaje.length()>0 ? central.LineaDivisora(1) + central.headFormat(mensaje) + central.LineaDivisora(1) : "";
		
		mensaje = "<font color=FF8000>Topzone</font><br1>" + (premiarTZ ? "Congratulations!!<br1>Your vote was completed successfully" : "Your vote was not completed.<br1>Before: <font color=F7D358>"+ String.valueOf(TZ_Antes) +"</font><br1>After: <font color=F7D358>"+ String.valueOf(actualTZ) +"</font><br>");
		html += mensaje.length()>0 ? central.LineaDivisora(1) + central.headFormat(mensaje) + central.LineaDivisora(1) : "";		
		html += central.getPieHTML(false) + "</body></html>";
		opera.enviarHTML(player, html);
	}
	
	
	public static String MainMenuVoteReward_Pide(L2PcInstance st){
		if(!general._activated()){
			return "";
		}
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Voto Recompenza") + central.LineaDivisora(2);
		String BOTON = "<button value=\"ITEM REWARD\" action=\"bypass -h npc_"+general.npcGlobal(st)+"_multisell 300\" width=200 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Puedes escojer un Item ingresando aquí"+BOTON,"WHITE") + central.LineaDivisora(2) + central.LineaDivisora(2);

		BOTON = "<font color = \"5858FA\">Item Necesario: " + central.ItemNeedShow(general.VOTO_ITEM_BUFF_ENCHANT_PRICE) + " </font>";
		BOTON += "<button value=\"Chant of Victory +15\" action=\"bypass -h ZeuSNPC VoteReward 0 3 1363\" width=200 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		BOTON += "<button value=\"Prophecy of Fire +15\" action=\"bypass -h ZeuSNPC VoteReward 0 3 1356\" width=200 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		BOTON += "<button value=\"Prophecy of Water +15\" action=\"bypass -h ZeuSNPC VoteReward 0 3 1355\" width=200 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		BOTON += "<button value=\"Prophecy of Wind +15\" action=\"bypass -h ZeuSNPC VoteReward 0 3 1357\" width=200 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Puedes escojer un Buff de la Siguente lista.<br1>"+BOTON,"WHITE") + central.LineaDivisora(2) + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("<button value=\"Back\" action=\"bypass -h ZeuSNPC VoteReward 0 0 0\" width=80 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","WHITE") + central.LineaDivisora(2);

		MAIN_HTML += central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}

	public static boolean checkVoto(int VotoEntrada, String paginaVoto, int unixTime, L2PcInstance st, int Web){
		if(!general._activated()){
			return false;
		}
		int UnixCheckLimite = unixTime + general.VOTO_REWARD_SEG_ESPERA;
		int UnixCheck = (int) (System.currentTimeMillis() / 1000);

		if(UnixCheck >= UnixCheckLimite){
			central.msgbox( msg.VOTEREWARD_AGOTASTE_EL_TIEMPO_$time.replace("$time", String.valueOf(general.VOTO_REWARD_SEG_ESPERA)), st);
			return false;
		}

		central.msgbox(msg.VOTEREWARD_MENSAJE_ESPERAR, st);
		int VotosActual = 0;

		if(Web == 1){
			if(httpResp.haveVoteHopZone()){
				VotosActual = httpResp.getHopZoneVote();
			}else{
				central.msgbox("We have problem to get Hopzone votes", st);
				return false;
			}
		}else{
			if(httpResp.haveVoteTopZone()){
				VotosActual = httpResp.getTopZoneVote();
			}else{
				central.msgbox("We have problem to get Topzone votes", st);
				return false;
			}
		}

		int VotosDiferencia = VotosActual - VotoEntrada;

		if((VotosActual > VotoEntrada) && (VotosDiferencia <=3) && canRewardVoto(st,VotoEntrada, Web) ){
			return true;
		}else{
			central.msgbox_Lado(msg.VOTEREWARD_MENSAJE_ERROR, st);
			if(VotosActual == VotoEntrada){
				central.msgbox("vote before checking " + String.valueOf(VotoEntrada) + ", vote at the moment " + String.valueOf(VotosActual), st);
			}else if((VotosActual - VotoEntrada) > 3){
				central.msgbox(msg.VOTOREWARD_MENSAJE_ENGAÑO,st);
			}
			return false;
		}
	}


	public static String HTMLComoVotar(L2PcInstance st){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Vote Reward") + central.LineaDivisora(2);
		MAIN_HTML += central.headFormat("Pasos a Seguir para Votar","FCAF06") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("1.- Debes ingresar a la Página central del servidor y pinchar el Logotipo de uno de los Sitios para Votar, ya sea HopZone o TopZone.","WHITE") + central.LineaDivisora(2);
		MAIN_HTML += central.headFormat("2.- Al momento de Abrirse la Página para Cusar el Voto verás un Captcha, como consejo y con Mucho cuidado Deja escrito el Captcha sin Aceptar para Cursar el Voto aun. Esto es Importante para que reconosca el Voto Cusado.","LEVEL") + central.LineaDivisora(2);
		MAIN_HTML += central.headFormat("3.- Cuando ya tengamos los pasos 1 y 2 Completados debemos entrar a la Sección Vote Reward de este Mismo NPC, y Pinchar el Boton Correspondiente a sitio a votar (Del paso 1). Ejemplo, si voto en el Logo de Hopzone, debe Ingresar al Voton HOPZONE.","WHITE") + central.LineaDivisora(2);
		MAIN_HTML += central.headFormat("4.- Al Ingresar saldrá una Nueva ventana Informando de los Votos Actuales de la Página de Votación seleccionada. Ademas, abajo del nombre de su Char aparecera una Barra de Color Amarillo mostrando el tiempo que le queda para Validar su Voto. En este paso usted debe volver a la Página de Votación donde escribio el Captcha y Cursar el voto antes de que el tiempo Pase.","LEVEL") + central.LineaDivisora(2);
		MAIN_HTML += central.headFormat("5.- Una vez Cursado el Voto y contabilizado en la Página de Votación, deberá regresar al L2 y presionar el boton, \"Ya Vote, Valida\". Si el Voto fue Contabilizado por la Página de Votación, recibirás el Premio Correspondiente.","WHITE") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Si aun tienes Dudas de como cursar tu voto,<br1>puedes ingresar a Nuestro Facebook y ver<br1>el Video de Como Votar.","FE9A2E") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Recuerda que Hopzone y Topzone permite votar<br1>cada 12 Horas con el Mismo IP","FE9A2E") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("<button value=\"Back\" action=\"bypass -h ZeuSNPC VoteReward 0 0 0\" width=80 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","WHITE") + central.LineaDivisora(2);
		MAIN_HTML += central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}

	public static String MainMenuVoteRewardTopzone(L2PcInstance st){
		if(!general._activated()){
			return "";
		}
		String Milesegundos = String.valueOf(System.currentTimeMillis() / 1000);
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Vote Reward TOPZONE") + central.LineaDivisora(2);
		central.msgbox(msg.VOTOREWARD_ESPERE_UN_MOMENTO_OBTENIENDO_INFO_$pagina.replace("$pagina", "TopZone"), st);
		int VotosEntrada = -1;
		if(httpResp.haveVoteTopZone()){
			VotosEntrada = httpResp.getTopZoneVote();
		}
		if(VotosEntrada > 0){
			central.SetBlockTimeVote(st);
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.VOTOREWARD_TENEMOS_$votos_EN_$pagina_VOTA_AHORA_MENSAJE.replace("$votos", "<font color=LEVEL>" + String.valueOf(VotosEntrada) + "</font>").replace("$pagina", "Topzone"),"WHITE") + central.LineaDivisora(2);
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat("<button value=\"I voted, check it\" action=\"bypass -h ZeuSNPC VoteReward 1 "+String.valueOf(VotosEntrada) + " topzone " + Milesegundos + "\" width=180 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL") + central.LineaDivisora(2);
		} else {
			MAIN_HTML += "<br>";
		}

		String BotonAtras = "<button value=\"Back\" action=\"bypass -h ZeuSNPC VoteReward 0 0 0\" width=65 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(BotonAtras) + central.LineaDivisora(2);

		MAIN_HTML += central.getPieHTML() + "</center></body></html>";
		return MAIN_HTML;
	}

	public static String MainMenuVoteRewardHopzone(L2PcInstance st){
		if(!general._activated()){
			return "";
		}
		String Milesegundos = String.valueOf(System.currentTimeMillis() / 1000);
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Vote Reward HOPZONE") + central.LineaDivisora(2);
		central.msgbox(msg.VOTOREWARD_ESPERE_UN_MOMENTO_OBTENIENDO_INFO_$pagina.replace("$pagina", "HopZone"), st);
		int VotosEntrada = -1;
		if(httpResp.haveVoteHopZone()){
			VotosEntrada = httpResp.getHopZoneVote();
		}
		if(VotosEntrada > 0){
			central.SetBlockTimeVote(st);
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.VOTOREWARD_TENEMOS_$votos_EN_$pagina_VOTA_AHORA_MENSAJE.replace("$votos", "<font color=LEVEL>" + String.valueOf(VotosEntrada) + "</font>").replace("$pagina", "Hopzone"),"WHITE") + central.LineaDivisora(2);
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat("<button value=\"I voted, check it\" action=\"bypass -h ZeuSNPC VoteReward 1 "+String.valueOf(VotosEntrada) + " hopzone " + Milesegundos + "\" width=180 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL") + central.LineaDivisora(2);
		} else {
			MAIN_HTML += "<br>";
		}

		String BotonAtras = "<button value=\"Back\" action=\"bypass -h ZeuSNPC VoteReward 0 0 0\" width=65 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(BotonAtras) + central.LineaDivisora(2);

		MAIN_HTML += central.getPieHTML() + "</center></body></html>";
		return MAIN_HTML;
	}


	public void inicializar(){
		if(!Inicializado){
			if(general.VOTO_AUTOREWARD){
				ThreadPoolManager.getInstance().scheduleGeneral(new AutoReward(), 20 * 1000);// 20 segundos
				_log.warning("ZeuS Autovote reward: Activated");
				Inicializado = true;
			}
		}
	}


	@SuppressWarnings("unused")
	private static void getStatus(){

		int VotoTopZone=-1, VotoHopZone=-1;

		if(general.VOTO_REWARD_ACTIVO_HOPZONE){
			httpResp.haveVoteHopZone();
			VotoHopZone = httpResp.getHopZoneVote();
		}
		if(general.VOTO_REWARD_ACTIVO_TOPZONE){
			httpResp.haveVoteTopZone();
			VotoTopZone = httpResp.getTopZoneVote();
		}

		if((Vhop <= 0) && (Vtop <= 0)){
			Vhop = VotoHopZone;
			Vtop = VotoTopZone;
			nextRw_H = Vhop + general.VOTO_REWARD_AUTO_RANGO_PREMIAR;
			nextRw_T = Vtop + general.VOTO_REWARD_AUTO_RANGO_PREMIAR;
		}else{
			if(VotoHopZone>0){
				if(VotoHopZone>=nextRw_H){
					rewardAll("Hopzone");
					nextRw_H = VotoHopZone + general.VOTO_REWARD_AUTO_RANGO_PREMIAR;
				}
			}
			if(VotoTopZone>0){
				if(VotoTopZone>=nextRw_T){
					rewardAll("Topzone");
					nextRw_T = VotoTopZone + general.VOTO_REWARD_AUTO_RANGO_PREMIAR;
				}
			}
		}
		anunciarVotos(VotoHopZone,VotoTopZone);
		ThreadPoolManager.getInstance().scheduleGeneral(new AutoReward(), ( general.VOTO_REWARD_AUTO_MINUTE_TIME_TO_CHECK * 60 ) * 1000);
	}

	protected static void anunciarVotos(int VH, int VT){
		String Mensaje = "";
		int Faltan = 0;
		if(VH>0){
			Faltan = nextRw_H - VH;
			if(Faltan > general.VOTO_REWARD_AUTO_RANGO_PREMIAR){
				nextRw_H = VH + general.VOTO_REWARD_AUTO_RANGO_PREMIAR;
				Faltan = nextRw_H - VH;
			}
			Mensaje = general.VOTO_REWARD_AUTO_MENSAJE_FALTA.replace("%VOTEACT%", String.valueOf(VH)).replace("%VOTENEED%", String.valueOf(Faltan)).replace("%VOTETOREWARD%", String.valueOf(nextRw_H)).replace("%SITE%", "Hopzone") ;
		}
		if(VT>0){
			Faltan = nextRw_T - VT;
			if(Faltan > general.VOTO_REWARD_AUTO_RANGO_PREMIAR){
				nextRw_H = VT + general.VOTO_REWARD_AUTO_RANGO_PREMIAR;
				Faltan = nextRw_T - VT;
			}
			if(Mensaje.length()>0){
				Mensaje +=" y ";
			}
			Mensaje += general.VOTO_REWARD_AUTO_MENSAJE_FALTA.replace("%VOTEACT%", String.valueOf(VT)).replace("%VOTENEED%", String.valueOf(Faltan)).replace("%VOTETOREWARD%", String.valueOf(nextRw_T)).replace("%SITE%", "Topzone") ;
		}
		if(Mensaje.length()>0){
			opera.AnunciarTodos("VOTE REWARD", Mensaje);
		}
	}
	
	private static boolean IsAfkPlayer(L2PcInstance player, String Sitio){
		if(!general.VOTO_REWARD_AUTO_AFK_CHECK){
			return false;
		}
		
		String Posicion = String.valueOf(player.getX()) + "-" + String.valueOf(player.getY()) + "-" + String.valueOf(player.getZ());
		if(PLAYER_AFK_POSITION==null){
			PLAYER_AFK_POSITION.put(player.getObjectId(), new HashMap<String,String>());
			PLAYER_AFK_POSITION.get(player.getObjectId()).put(Sitio, Posicion);
			return false;
		}else{
			if(PLAYER_AFK_POSITION.containsKey(player.getObjectId())){
				if(PLAYER_AFK_POSITION.get(player.getObjectId()).containsKey(Sitio)){
					String PosicionFast = PLAYER_AFK_POSITION.get(player.getObjectId()).get(Sitio);
					if(!PosicionFast.equals(Posicion)){
						return false;
					}
				}else{
					PLAYER_AFK_POSITION.get(player.getObjectId()).put(Sitio, Posicion);
					return false;
				}
			}else{
				PLAYER_AFK_POSITION.put(player.getObjectId(), new HashMap<String,String>());
				PLAYER_AFK_POSITION.get(player.getObjectId()).put(Sitio, Posicion);				
				return false;				
			}
		}
		return true;
	}

	protected static void rewardAll(String site){
		   String rewardToGive = "";
		   if(site.equalsIgnoreCase("Hopzone")){
			   rewardToGive = general.VOTO_REWARD_AUTO_REWARD_META_HOPZONE;
		   }else if(site.equalsIgnoreCase("topzone")){
			   rewardToGive = general.VOTO_REWARD_AUTO_REWARD_META_TOPZONE;
		   }		
		   
		   if(rewardToGive.length()>0){
				Collection<L2PcInstance> pls = opera.getAllPlayerOnWorld();
				Vector<String> ip_local = new Vector<String>();
				Vector<String> ip_Interner = new Vector<String>();
			 	for (L2PcInstance onlinePlayer : pls){
		           if(onlinePlayer.isOnline()){
		        	   if(!onlinePlayer.isInStoreMode() && !onlinePlayer.isInCraftMode()){
		        		   if(!onlinePlayer.getClient().isDetached()){
				        	   if((ZeuS.getIp_pc(onlinePlayer).length()>0) && (ZeuS.getIp_Wan(onlinePlayer).length()>0) ){
					        	   if( onlinePlayer.isGM() || (!ip_local.contains(ZeuS.getIp_pc(onlinePlayer)) && !ip_Interner.contains(ZeuS.getIp_Wan(onlinePlayer)))){
					        		   if(!onlinePlayer.isGM()){
					        			   ip_local.add(ZeuS.getIp_pc(onlinePlayer));
					        			   ip_Interner.add(ZeuS.getIp_Wan(onlinePlayer));
					        		   }
					        		   if(rewardToGive.length()>0){
					        			   if(!IsAfkPlayer(onlinePlayer,site)){
					        				   opera.giveReward(onlinePlayer, rewardToGive);			        				   
					        			   }
					        		   }
					        	   }
				        	   }
		        	   		}
		        	   }
		           }
			 	}
			 	opera.AnunciarTodos("V. REWARD", general.VOTO_REWARD_AUTO_MENSAJE_META_COMPLIDA.replace("%SITE%", site));
		   }else{
			   _log.warning("Sitio: " + site + " no presenta Reward's");
		   }
	}

	
	public static class autoCheckWindows implements Runnable{
		L2PcInstance activeChar;
		int _IN_HZ, _IN_TZ;
		public autoCheckWindows(L2PcInstance player, int hopzone, int topzone){
			activeChar = player;
			_IN_HZ = hopzone;
			_IN_TZ = topzone;
		}
		@Override
		public void run(){
			try{
				if(activeChar!=null){
					if(!activeChar.getClient().isDetached()){
						getCheckVotedWindows(activeChar, _IN_HZ, _IN_TZ);
					}
				}
			}catch(Exception a){

			}
		}
	}	
	
	
	

	private static class AutoReward implements Runnable{
		@Override
		public void run(){
			if(general.VOTO_AUTOREWARD){
				getStatus();
			}else{
				Inicializado = false;
			}
		}
	}

	public static votereward getInstance()
	{
	    return SingletonHolder._instance;
	}


	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
	    protected static final votereward _instance = new votereward();
	}


}
