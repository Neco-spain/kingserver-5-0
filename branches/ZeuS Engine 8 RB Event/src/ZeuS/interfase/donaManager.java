package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.data.xml.impl.SkillTreesData;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.L2SkillLearn;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.PledgeSkillList;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.procedimientos.opera;
import ZeuS.server.httpResp;

public class donaManager {

	private static final Logger _log = Logger.getLogger(donaManager.class.getName());

	public static HashMap<Integer,HashMap<Integer,HashMap<String,String>>> shopDona = new HashMap<Integer,HashMap<Integer,HashMap<String,String>>>();
	private static HashMap<L2PcInstance,Integer>seccionAnterior = new HashMap<L2PcInstance,Integer>();

	private static HashMap<L2PcInstance, Integer>DC_Solicita = new HashMap<L2PcInstance, Integer>();
	
	private static void setSeccionAnterior(L2PcInstance player,int SeccionIN){
		seccionAnterior.put(player, SeccionIN);
	}

	private static int getSeccionAnterior(L2PcInstance player){
		if(seccionAnterior!=null){
			if(seccionAnterior.containsKey(player)){
				return seccionAnterior.get(player);
			}
		}
		return -1;
	}

	public static void ByPass(L2PcInstance player, String Parametros){
		//_log.warning(Parametros);
		if(Parametros.endsWith("admin_zeus_dona")){
			getMainConfigWindows(player, "");
		}else if(Parametros.split(" ").length>0){
			String[] Params = Parametros.split(" ");
			if(Params[1].equals("NEW")){

			}
		}
	}

	private static void getMainConfigWindows(L2PcInstance player, String Parametros){
		String HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		HTML += central.LineaDivisora(1) + central.headFormat("Dona Manager Config") + central.LineaDivisora(1);

		String ComboTipoAccion = "BUYLIST;CLAN_LVL;CLAN_REPUTATION;EXEC_MULTISELL;FAMA;HEROE;HTML;LVL_85;MULTISELL;NOBLE;SEC;AIO;NAME_CLAN;NAME_PLAYER";
		String cmbTipoAccion = "<combobox width=120 var=cmbTipoAccion list=" + ComboTipoAccion + ">";
		String txtNombre = "<edit var=\"txtName\" width=120>";
		String txtParam1 = "<edit var=\"txtParam1\" width=120>";
		String txtDCPedir = "<edit var=\"txtDCPedir\" width=120>";
		String btnUsarScheme = "<button value=\"Save\" action=\"bypass -h admin_zeus_dona NEW $cmbTipoAccion $txtName $txtParam1 $txtDCPedir 0 0\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		HTML += central.headFormat("<table width=280>" +
		"<tr><td>Action</td><td>"+ cmbTipoAccion +"</td></tr>" +
		"<tr><td>Name</td><td>"+ txtNombre +"</td></tr>" +
		"<tr><td>Parameters</td><td>"+ txtParam1 +"</td></tr>" +
		"<tr><td>Count</td><td>"+ txtDCPedir +"</td></tr>" +
		"</table><br1>"+btnUsarScheme+"<br>");

		HTML += central.LineaDivisora(1) + central.getPieHTML(false) + "</body></html>";
		//_log.warning(HTML);
		opera.enviarHTML(player, HTML);
	}

	public static void loadShop(){
		shopDona.clear();
		String Consulta = "call sp_dona_shop(1,'','','','',-1)";
		Connection conn = null;
		CallableStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(Consulta);
			rss = psqry.executeQuery();
				while (rss.next()){
					if(shopDona == null){
						shopDona.put(rss.getInt(6), new HashMap<Integer, HashMap<String,String>>());
					}else{
						if(!shopDona.containsKey(rss.getInt(6))){
							shopDona.put(rss.getInt(6), new HashMap<Integer, HashMap<String,String>>());
						}
					}
					if(!shopDona.get(rss.getInt(6)).containsKey(rss.getInt(7))){
						shopDona.get(rss.getInt(6)).put(rss.getInt(7), new HashMap<String,String>());
					}
					shopDona.get(rss.getInt(6)).get(rss.getInt(7)).put("NOMBRE", rss.getString(1));
					shopDona.get(rss.getInt(6)).get(rss.getInt(7)).put("ACCION", rss.getString(2));
					shopDona.get(rss.getInt(6)).get(rss.getInt(7)).put("PARM_1", rss.getString(3));
					shopDona.get(rss.getInt(6)).get(rss.getInt(7)).put("PARM_2", rss.getString(4));
					shopDona.get(rss.getInt(6)).get(rss.getInt(7)).put("DC", String.valueOf(rss.getInt(5)));
					shopDona.get(rss.getInt(6)).get(rss.getInt(7)).put("ID", String.valueOf(rss.getInt(7)));
					shopDona.get(rss.getInt(6)).get(rss.getInt(7)).put("POSI", String.valueOf(rss.getInt(8)));
				}
		}catch(SQLException a){

		}

		try{
			conn.close();
		}catch(SQLException a){

		}
	}

	public static String ExplicaComoDonar(){
		String BOTON_ATRAS = "<button value=\"Back\" action=\"bypass -h ZeuSNPC MenuDonation 0 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(1) +  central.headFormat("Donación") + central.LineaDivisora(1);
		MAIN_HTML += central.LineaDivisora(1) +  central.headFormat(msg.DONATION_WINDOWS_INFO) + central.LineaDivisora(1);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(BOTON_ATRAS,"LEVEL") + central.LineaDivisora(2);
		MAIN_HTML += "</body></html>";
		return MAIN_HTML;
}
	public static String MainMenuDonation(L2PcInstance st){
		return MainMenuDonation(st, -1);
	}

	public static String MainMenuDonation(L2PcInstance st,int seccion){
		return MainMenuDonation(st,seccion,false);
	}

	private static void getMenuDonaciones(L2PcInstance player, int Seccion, String Parametros){
		String BotonMainDonation = "<button value=\"Donation\" action=\"bypass -h ZeuSNPC MenuDonation 0 0 0\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String MAIN_HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		MAIN_HTML+= central.LineaDivisora(1) + central.headFormat("DONATION MENU") + central.LineaDivisora(1);
		if(shopDona!=null){
			if(shopDona.size()!=0){
				MAIN_HTML += getBtnDonacionReward(Seccion,player);
			}
		}
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(BotonMainDonation) + central.LineaDivisora(1) + central.getPieHTML() + "</body></html>";
		opera.enviarHTML(player, MAIN_HTML);
	}

	public static String MainMenuDonation(L2PcInstance st,int seccion, boolean Inicio){
		return "";/*
		if(Inicio){
			setSeccionAnterior(st, -1);
		}
		if(!general._activated()){
			return "";
		}
		String npcid = general.npcGlobal(st);
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Donation Reward") + central.LineaDivisora(1);
		int CreditosDonados = opera.haveDonaCreditCoin(st,-1);
		if (CreditosDonados > 0) {
			MAIN_HTML += central.LineaDivisora(1) + "<table width=280 border=0 bgcolor=151515>" +
					"<tr><td width=280 align=center>" +
							msg.DONATION_YOU_HAVE_$donationCount_ON_YOU_ACCOUNT.replace("$donationCount","<font color =\"LEVEL\">" + String.valueOf(CreditosDonados) + "</font>")+
					"</td></tr>"+
					"<tr><td width=280 align=center>"+
						"<button value=\""+ msg.DONATION_GIVE_DC_BUTTON +"\" action=\"bypass -h ZeuSNPC MenuDonation "+npcid+" 0 1\" width=175 height=38 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" +
					"</td></tr></table>" + central.LineaDivisora(1);

		}

		MAIN_HTML += central.headFormat("<button value=\""+ msg.DONATION_BTN_HOW_CAN_I_MAKE_A_DONATION +"\" action=\"bypass -h ZeuSNPC MenuDonation 0 4 0\" width=200 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL");
		MAIN_HTML += central.headFormat("<button value=\""+ msg.DONATION_BTN_NOTIFY_DONATION +"\" action=\"bypass -h ZeuSNPC NOTIFICA_DONACION 0 0 0\" width=200 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL") + central.LineaDivisora(1);
 		MAIN_HTML += central.headFormat("<button value=\""+ msg.DONATION_BTN_REWARD +"\" action=\"bypass -h ZeuSNPC ShopDona 0 0 0\" width=200 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL");

		if(general.PREMIUM_CHAR || general.PREMIUM_CLAN){
			String btnPremiumChar = "<button value=\"Buy Premium Account\" action=\"bypass -h ZeuSNPC ShopDona PREMIUM CHAR 0\" width=180 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			String btnPremiumClan = "<button value=\"Buy Premium Clan\" action=\"bypass -h ZeuSNPC ShopDona PREMIUM CLAN 0\" width=180 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			String txtPremium = "";
			String FechaInicio = "", FechaTermino ="";
			if(general.PREMIUM_CHAR){
				if(general.isPremium(st, true)){
					if(general.ZeusPremium!=null){
						if(general.ZeusPremium.containsKey("CHAR")){
							if(general.ZeusPremium.get("CHAR").containsKey(st.getAccountName())){
								FechaInicio = opera.getDateFromUnixTime(Integer.valueOf(general.ZeusPremium.get("CHAR").get(st.getAccountName()).get("START_DATE")));
								FechaTermino = opera.getDateFromUnixTime(Integer.valueOf(general.ZeusPremium.get("CHAR").get(st.getAccountName()).get("END_DATE")));
								txtPremium = "Start Date:" + FechaInicio + "<br1>" + "End Date:" + FechaTermino + "<br>";
							}
						}
					}
				}
				txtPremium += "Cost: " + String.valueOf(general.PREMIUM_DAYS_GIVE) + " days for " + String.valueOf(general.PREMIUM_CHAR_COST_DONATION) + " " + central.getNombreITEMbyID(general.DONA_ID_ITEM) + "<br1>" + btnPremiumChar;
				if(txtPremium.length()>0){
					MAIN_HTML += central.LineaDivisora(2) + central.LineaDivisora(1) + central.headFormat("Premium Account","FF8000") + central.LineaDivisora(1);
					MAIN_HTML += central.headFormat(txtPremium,"LEVEL") + central.LineaDivisora(1) + central.LineaDivisora(2);
				}
				txtPremium = "";
			}
			if(general.PREMIUM_CLAN){
				if(st.getClan()!=null){
					if(general.isPremium(st, false)){
						if(general.ZeusPremium!=null){
							if(general.ZeusPremium.containsKey("CLAN")){
								if(general.ZeusPremium.get("CLAN").containsKey(String.valueOf(st.getClan().getId()))){
									FechaInicio = opera.getDateFromUnixTime(Integer.valueOf(general.ZeusPremium.get("CLAN").get(String.valueOf(st.getClan().getId())).get("START_DATE")));
									FechaTermino = opera.getDateFromUnixTime(Integer.valueOf(general.ZeusPremium.get("CLAN").get(String.valueOf(st.getClan().getId())).get("END_DATE")));
									txtPremium = "Start Date:" + FechaInicio + "<br1>" + "End Date:" + FechaTermino + "<br>";
								}
							}
						}
					}
					txtPremium += "Cost: " + String.valueOf(general.PREMIUM_DAYS_GIVE) + " days for " + String.valueOf(general.PREMIUM_CLAN_COST_DONATION) + " " + central.getNombreITEMbyID(general.DONA_ID_ITEM) + "<br1>" + btnPremiumClan;
				}
				if(txtPremium.length()>0){
					MAIN_HTML += central.LineaDivisora(2) + central.LineaDivisora(1) + central.headFormat("Premium Clan Account","FF8000") + central.LineaDivisora(1);
					MAIN_HTML += central.headFormat(txtPremium,"LEVEL") + central.LineaDivisora(1) + central.LineaDivisora(2);
				}
			}
		}

		MAIN_HTML += central.BotonGOBACKZEUS() + central.getPieHTML()+"</center></body></html>";
		return MAIN_HTML;*/
	}
/*
	private static void registrarPremium(L2PcInstance player, boolean checkPlayer){

		if(!opera.haveItem(player, general.DONA_ID_ITEM, checkPlayer ? general.PREMIUM_CHAR_COST_DONATION : general.PREMIUM_CLAN_COST_DONATION  )){
			opera.enviarHTML(player, MainMenuDonation(player));
			return;
		}

		String Consulta = "";
		int DiasUnixDar = general.PREMIUM_DAYS_GIVE * 86400;
		int CantidadRemove = 0;
		if(checkPlayer){
			if(general.isPremium(player, true)){
				Consulta = "UPDATE zeus_premium SET zeus_premium.end_date = zeus_premium.end_date + " + String.valueOf(DiasUnixDar) + " WHERE id='"+ player.getAccountName() + "'";
			}else{
				if(general.havePremium(player, true)){
					Consulta = "UPDATE zeus_premium SET zeus_premium.end_date = " + String.valueOf(DiasUnixDar + opera.getUnixTimeNow()) + " WHERE id='"+ player.getAccountName() + "'";
				}else{
					Consulta = "INSERT INTO zeus_premium() values('"+ player.getAccountName() +"',"+ String.valueOf(opera.getUnixTimeNow()) +","+ String.valueOf(opera.getUnixTimeNow() + DiasUnixDar) +",'CHAR')";
				}
			}
			CantidadRemove = general.PREMIUM_CHAR_COST_DONATION;
		}else{
			if(general.isPremium(player, false)){
				Consulta = "UPDATE zeus_premium SET zeus_premium.end_date = zeus_premium.end_date + " + String.valueOf(DiasUnixDar) + " WHERE id='"+ String.valueOf(player.getClanId()) + "'";
			}else{
				if(general.havePremium(player, false)){
					Consulta = "UPDATE zeus_premium SET zeus_premium.end_date = " + String.valueOf(DiasUnixDar + opera.getUnixTimeNow()) + " WHERE id='"+ String.valueOf(player.getClanId()) + "'";
				}else{
					Consulta = "INSERT INTO zeus_premium() values('"+ String.valueOf(player.getClanId()) +"',"+ String.valueOf(opera.getUnixTimeNow()) +","+ String.valueOf(opera.getUnixTimeNow() + DiasUnixDar) +",'CLAN')";
				}
			}
			CantidadRemove = general.PREMIUM_CLAN_COST_DONATION;
		}
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn=ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(Consulta);
			try{
				psqry.executeUpdate();
				psqry.close();
				conn.close();
			}catch (SQLException e) {
			}
		}catch(SQLException a){
		}
		try{
			conn.close();
		}catch(SQLException a){

		}
		opera.removeItem(general.DONA_ID_ITEM, CantidadRemove, player);
		general.loadZeuSPremium();
		opera.enviarHTML(player, MainMenuDonation(player));
	}*/
	
	public static int DC_Solicita(L2PcInstance player){
		return DC_Solicita.get(player);
	}

	public static void delegar(L2PcInstance player, String P1, String P2, String P3){

		//_log.warning(P1+" "+P2+" "+P3);

		if(P1.equals("-99") && P2.equals("-99") && P3.equals("-99")){
			opera.enviarHTML(player, MainMenuDonation(player,-1));
			return;
		}


		/*if(P1.equals("PREMIUM")){
			if(P2.equals("CHAR")){
				registrarPremium(player,true);
			}else if(P2.equals("CLAN")){
				registrarPremium(player,false);
			}
			return;
		}else */if(P1.equals("0") && P2.equals("0") && P3.equals("0")){
			getMenuDonaciones(player,0,P1+" "+P2+" "+P3);
			return;
		}else if(P1.equals("0") && P3.equals("-99")){
			getMenuDonaciones(player, Integer.valueOf(P2) ,P1+" "+P2+" "+P3);
			return;
		}

		String Operacion = shopDona.get(Integer.valueOf(P1)).get(Integer.valueOf(P2)).get("ACCION");
		String Cantidad = shopDona.get(Integer.valueOf(P1)).get(Integer.valueOf(P2)).get("DC");
		String PARM_1 = shopDona.get(Integer.valueOf(P1)).get(Integer.valueOf(P2)).get("PARM_1");
		
		DC_Solicita.put(player, Integer.valueOf(Cantidad));
		
		switch(Operacion){
			case "MAIN":
				getMenuDonaciones(player,0,P1+" "+P2+" "+P3);
				break;
			case "AIO":
				opera.enviarHTML(player, aioChar.explicaAIO(player));
				return;
			case "NOBLE":
				if(player.isNoble()){
					central.msgbox(msg.EL_PLAYER_$player_ES_NOBLE.replace("$player", player.getName()), player);
					break;
				}
				if(!opera.haveItem(player, general.DONA_ID_ITEM, Integer.valueOf(Cantidad))) {
					break;
				}
				opera.removeItem(general.DONA_ID_ITEM, Integer.valueOf(Cantidad), player);
				opera.giveReward(player, 7694, 1);
				player.setNoble(true);
				central.msgbox("Your are noble now", player);
				break;
			case "HEROE":
				if(player.isHero()){
					central.msgbox(msg.EL_PLAYER_$player_ES_HERO.replace("$player", player.getName()), player);
					break;
				}
				if(!opera.haveItem(player, general.DONA_ID_ITEM, Integer.valueOf(Cantidad))) {
					break;
				}
				opera.removeItem(general.DONA_ID_ITEM, Integer.valueOf(Cantidad), player);
				PlayerConfigPanel.setHeroDona(player, true);
				player.setHero(true);
				central.msgbox("Your are hero now", player);
				break;
			case "FAMA":
				if(!opera.haveItem(player, general.DONA_ID_ITEM, Integer.valueOf(Cantidad))) {
					break;
				}
				central.msgbox("You have acquired "+ PARM_1 +" of fame", player);
				opera.removeItem(general.DONA_ID_ITEM, Integer.valueOf(Cantidad), player);
				player.setFame( player.getFame() + Integer.valueOf(PARM_1));
				central.msgbox("Now you have "+ String.valueOf(player.getFame()) +" of fame", player);
				player.broadcastUserInfo();
				break;
			case "CLAN_GIVE_SKILL":
				if(!opera.haveItem(player, general.DONA_ID_ITEM, Integer.valueOf(Cantidad))) {
					break;
				}				
				if(GiveClanSkills(player,true)){
					opera.removeItem(general.DONA_ID_ITEM, Integer.valueOf(Cantidad), player);					
				}
				break;
			case "CLAN_REPUTATION":
				if(!opera.haveItem(player, general.DONA_ID_ITEM, Integer.valueOf(Cantidad))) {
					break;
				}
				if(player.getClan().getLevel()<5){
					central.msgbox("Only clans of level 5 or above may receive reputation points.", player);
					break;
				}
				opera.removeItem(general.DONA_ID_ITEM, Integer.valueOf(Cantidad), player);
				player.getClan().addReputationScore(Integer.valueOf(PARM_1), true);
				central.msgbox("You have acquired "+ PARM_1 +" of Clan Reputation", player);
				break;
			case "LVL_85":
				if(player.getLevel()==85){
					central.msgbox(msg.USTED_YA_ES_85_NO_PUEDE_CONTINUAR, player);
					break;
				}
				if(!opera.haveItem(player, general.DONA_ID_ITEM, Integer.valueOf(Cantidad))) {
					break;
				}
				opera.removeItem(general.DONA_ID_ITEM, Integer.valueOf(Cantidad), player);
				opera.set85(player);
				break;
			case "CLAN_LVL":
				int LevelOtorgar = Integer.valueOf(PARM_1);
				if(player.getClan().getLevel()>=LevelOtorgar){
					central.msgbox("Clan level must be less than " + PARM_1, player);
					break;
				}
				if(!opera.haveItem(player, general.DONA_ID_ITEM, Integer.valueOf(Cantidad))){
					break;
				}
				opera.removeItem(general.DONA_ID_ITEM, Integer.valueOf(Cantidad), player);
				central.msgbox("You Clan level is now "+ PARM_1 , player);
				player.getClan().setLevel(LevelOtorgar);
				player.getClan().changeLevel(LevelOtorgar);				
				player.getClan().updateClanInDB();
				player.getClan().broadcastClanStatus();
				break;
			case "SEC":
				setSeccionAnterior(player,Integer.valueOf(P1));
				central.sendHtml(player, MainMenuDonation(player,Integer.valueOf(P2)));
				return;
			case "HTML":
				return;
		}

		if(P1.equals("-1")){
			opera.enviarHTML(player, MainMenuDonation(player));
		}else{
			getMenuDonaciones(player, Integer.valueOf(P1) ,P1+" "+P2+" "+P3);
		}
	}
	
	
	private static boolean GiveClanSkills(L2PcInstance activeChar, boolean includeSquad)
	{
		
		final L2PcInstance player = activeChar;
		final L2Clan clan = player.getClan();
		if (clan == null)
		{
			central.msgbox("You must have a clan", activeChar);
			return false;
		}
		
		if (!player.isClanLeader())
		{
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_IS_NOT_A_CLAN_LEADER);
			sm.addString(player.getName());
			activeChar.sendPacket(sm);
			return false;
		}
		
		final Map<Integer, L2SkillLearn> skills = SkillTreesData.getInstance().getMaxPledgeSkills(clan, includeSquad);
		for (L2SkillLearn s : skills.values())
		{
			Skill sk = SkillData.getInstance().getSkill(s.getSkillId(), s.getSkillLevel());
			clan.addNewSkill(sk);
		}
		
		// Notify target and active char
		clan.broadcastToOnlineMembers(new PledgeSkillList(clan));
		for (L2PcInstance member : clan.getOnlineMembers(0))
		{
			member.sendSkillList();
		}
		player.sendMessage("Your clan received " + skills.size() + " skills.");
		player.getClan().broadcastClanStatus();
		return true;
	}	

	private static String getBtnDonacionReward(int idSeccion, L2PcInstance player){
		String MAIN_HTML = central.LineaDivisora(3) + central.LineaDivisora(1);
		MAIN_HTML += central.headFormat("Donation SHOP","LEVEL");

		String ByPass = "";
		String btnAtras = "";
		if(idSeccion>0){
			btnAtras = central.LineaDivisora(2) + "<br><center><button value=\"DONATION MAIN\" action=\"bypass -h ZeuSNPC ShopDona -99 -99 -99\" width=128 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		}

		//_log.warning(String.valueOf(idSeccion));

		if(shopDona.containsKey(idSeccion)){
			MAIN_HTML += "<table width=280 align=CENTER>";
			//Iterator itr = general.BUFF_CHAR_DATA.get("CATE").entrySet().iterator();
			Iterator itr = shopDona.get(idSeccion).entrySet().iterator();
		    while(itr.hasNext()){
		    	Map.Entry Entrada = (Map.Entry)itr.next();
		    	HashMap<String,String> datos = shopDona.get(idSeccion).get((int)Entrada.getKey());
		    	String color = "F5DA81";
		    	String ItemDonaQuePide = "";
		    	if(!datos.get("DC").equals("0")){
		    		ItemDonaQuePide = "<center><font color="+color+">Required "+datos.get("DC")+" " + central.getNombreITEMbyID(general.DONA_ID_ITEM)  + "</font></center><br1>";
		    	}
		    	//MENUDONA_VARIOS
		    	switch(datos.get("ACCION")){
		    		case "NAME_PLAYER":
		    			ByPass = datos.get("NOMBRE") +"\" action=\"bypass -h ZeuSNPC MENUDONA_VARIOS 1 0 0";
		    			break;
		    		case "NAME_CLAN":
		    			ByPass = datos.get("NOMBRE") +"\" action=\"bypass -h ZeuSNPC MENUDONA_VARIOS 2 0 0";
		    			break;
					case "AIO":
		    			//ByPass = datos.get("NOMBRE") +"\" action=\"bypass -h ZeuSNPC ShopDona "+ String.valueOf(idSeccion) + " " + datos.get("ID") +" " + datos.get("DC");
		    			ByPass = datos.get("NOMBRE") +"\" action=\"bypass -h ZeuSNPC ShopDona "+ String.valueOf(idSeccion) + " " + datos.get("ID") +" 0";		    			
						//shopDona.get(rss.getInt(6)).get(rss.getInt(7)).put("DC", String.valueOf(rss.getInt(5)));
		    			break;
		    		case "NOBLE":
		    		case "HEROE":
		    		case "FAMA":
		    		case "CLAN_REPUTATION":
		    		case "CLAN_GIVE_SKILL":
		    		case "CLAN_LVL":
		    		case "LVL_85":
		    			ByPass = datos.get("NOMBRE") +"\" action=\"bypass -h ZeuSNPC ShopDona "+ String.valueOf(idSeccion) + " " + datos.get("ID") +" 0";
		    			break;
		    		case "SEC":
		    			ByPass = datos.get("NOMBRE") +"\" action=\"bypass -h ZeuSNPC ShopDona "+ String.valueOf(idSeccion) + " " + datos.get("ID") + " -99";
		    			break;
		    		case "MULTISELL":
		    			ByPass = datos.get("NOMBRE") +"\" action=\"bypass -h npc_%objectId%_multisell " + datos.get("PARM_1");
		    			break;
		    		case "EXEC_MULTISELL":
		    			ByPass = datos.get("NOMBRE") +"\" action=\"bypass -h npc_%objectId%_exc_multisell " + datos.get("PARM_1");
		    			break;
		    		case "HTML":
		    			if(datos.get("PARM_1").endsWith(".htm") || datos.get("PARM_1").endsWith(".html")){
		    				ByPass = datos.get("NOMBRE") +"\" action=\"bypass -h npc_%objectId%_Link merchant/AIONPC/DONA/" + datos.get("PARM_1");
		    			}else{
		    				ByPass = datos.get("NOMBRE") +"\" action=\"bypass -h npc_%objectId%_Link merchant/AIONPC/DONA/" + datos.get("PARM_1") + ".htm";
		    			}
		    			break;
		    		case "BUYLIST":
		    			ByPass = datos.get("NOMBRE") +"\" action=\"bypass -h npc_%objectId%_Buy " + datos.get("PARM_1");
		    			//npc_%objectId%_Buy
		    			break;
		    	}
		    	MAIN_HTML += "<tr><td>"+central.headFormat(ItemDonaQuePide + "<button value=\"" + ByPass.replace("%objectId%", general.npcGlobal(player))  + "\" width=180 height=23 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL") + "</td></tr>";
		    }
			MAIN_HTML += "</table><br1>";
		}
		MAIN_HTML += central.LineaDivisora(1) + central.LineaDivisora(2) + btnAtras + central.LineaDivisora(1) + central.LineaDivisora(3);
		return MAIN_HTML;
	}

	public static String MainHtmlNotificacionDonacion(L2PcInstance player){
		if(!general._activated()){
			return "";
		}
		
		if(!EmailRegistration.hasEmailRegister(player)){
			central.msgbox(msg.DONATION_YOU_NEED_EMAIL_REGISTRATION_INPUT_$command.replace("$command", ".acc_register"), player);
			return "";
		}
		
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() +"</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.DONATION_BTN_NOTIFY_DONATION) + central.LineaDivisora(2);
		
		

		String EmailUsuario = opera.getUserMail(player.getAccountName());
		
		String MensajePublicar = msg.DONATION_WINDOWS_THANKS.replace("%EMAIL", EmailUsuario != null ? EmailUsuario : "NO HAVE" ) ;
		
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(MensajePublicar,"LEVEL") + central.LineaDivisora(1);

		String TEXTO_DESCRIP = "<multiedit var=\"MEMOSTR\" width=250 height=80><br>";
		String COMBO_TIPO_DONACION = "<combobox width=200 var=TIPODONACION list=PAYPAL;DEPOSITO_CAJA;CAJA_VECINA;TRANF_ELECTR;WESTERN_UNION;OTROS><br>";
		String TEXTO_MONTO_DONADO = "<edit type=\"text\" var=\"MONTODONADO\" width=150><br>";

		MAIN_HTML += central.LineaDivisora(1) + central.headFormat("Seleccione Medio de Donación" + COMBO_TIPO_DONACION,"WHITE") + central.LineaDivisora(1);
		MAIN_HTML += central.headFormat("Ingrese el Monto Donado" + TEXTO_MONTO_DONADO,"WHITE") + central.LineaDivisora(1);
		MAIN_HTML += central.headFormat("Ingrese datos como Número de transacción,<br1>hora, fecha. Mientras más Datos Informados<br1>Más rapido serán liberados las Donation Coin." + TEXTO_DESCRIP,"WHITE") + central.LineaDivisora(1);

		String BtnAtras = central.LineaDivisora(2) + central.headFormat("<button value=\"Back\" action=\"bypass -h ZeuSNPC MenuDonation "+general.npcGlobal(player)+" 0 0\" width=95 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">") + central.LineaDivisora(2);

		MAIN_HTML += "<center><button value=\"Notificar\" action=\"bypass -h ZeuSNPC ENVIAR_NOTIFICACION $MONTODONADO $TIPODONACION $MEMOSTR\" width=128 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		MAIN_HTML += "<br><br><br>"+BtnAtras+"</center>"+central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}


	private static void htmlConfigDonacion(L2PcInstance player){

	}



	@SuppressWarnings("deprecation")
	public static String EnviarNotificacionBD(L2PcInstance st, String Monto, String Medio, String Descrip){
		String EmailPlayer = opera.getUserMail(st.getAccountName());
		if (!central.isNumeric(Monto)){
			central.msgbox(msg.INGRESE_SOLO_NUMEROS,st);
			return MainHtmlNotificacionDonacion(st);
		}

		String Consulta = "call sp_ingresa_dona(1,-1," + Monto + ",'" + st.getObjectId() +"',?,'"+EmailPlayer+"',?)";
		Connection conn;
		String IDWEB = "";
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			CallableStatement psqry = conn.prepareCall(Consulta);
			psqry.setString(1,Medio);
			psqry.setString(2,Descrip);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				if(rss.getString(1).equals("cor")) {
					IDWEB = rss.getString(3);
				} else
					if(rss.getString(1).equals("err")) {
						central.msgbox(rss.getString(2),st);
					}
			}
			try{
				conn.close();
			}catch(Exception a){
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}


		boolean Respuesta= httpResp.sendEmailDonation(st,IDWEB);
		if (Respuesta) {
			central.msgbox_Lado(msg.ENVIANDO_COMPROBANTE_OK,st);
		} else {
			central.msgbox_Lado("Error to send notificacion, Please contact any GM or Admin",st);
		}
		return "";
	}

}
