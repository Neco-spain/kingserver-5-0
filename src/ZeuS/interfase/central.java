package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.data.xml.impl.ClassListData;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.SetupGauge;
import com.l2jserver.gameserver.network.serverpackets.TutorialShowHtml;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.procedimientos.opera;

public class central {

	/**ID Desafios**/
	/*
	 *
	 * 1.- Level 85
	 * 2.- Nobles
	 *
	 * */
	/**ID Desafios**/

	private static Logger _log = Logger.getLogger(central.class.getName());
	public static int IntPintarGrilla = -1;

	public static void sendHtml(L2PcInstance player, String ShowHTML){
		sendHtml(player, ShowHTML, false);
	}

	public static void sendHtml(L2PcInstance player, String ShowHTML, boolean tutorial){
		if(!tutorial){
			player.sendPacket(new NpcHtmlMessage(0,ShowHTML));
		}else{
			player.sendPacket(new TutorialShowHtml(ShowHTML));
		}
	}

	public static String getPieHTML(boolean showLink){
		String Retorno ="";
		//<a action="bypass -h npc_%objectId%_Chat 1">Buy/Sell/Refund.</a><br>
		if(showLink){
			Retorno = LineaDivisora(1) + "<br><center><a action=\"bypass -h ZeuSNPC howiam 0 0 0\" >"+ general.PIE_PAGINA() +"</a></center>";
		}else{
			Retorno = LineaDivisora(1) + "<br><center>"+general.PIE_PAGINA()+"</center>";
		}
		return Retorno;
	}

	public static String getPieHTML(){
		return getPieHTML(true);
	}

	public static void msgbox_Lado(String Mensaje, L2PcInstance st, String TITULO_MENSAJE){
		CreatureSay cs = new CreatureSay(0, Say2.PARTYROOM_COMMANDER, "[" + TITULO_MENSAJE + "]", Mensaje);
		st.sendPacket(cs);
		//st.broadcastPacket(new CreatureSay(0, Say2.PARTYROOM_COMMANDER, "[" + TITULO_MENSAJE + "]", Mensaje));
	}
	public static void msgbox_Lado(String Mensaje, L2PcInstance st){
		CreatureSay cs = new CreatureSay(0, Say2.PARTYROOM_COMMANDER, "[" + general.NOMBRE_NPC + "]", Mensaje);
		st.sendPacket(cs);
	}

	public static String ItemShowReward_annoucement(String ItemReward){
		String Retorno ="";

		String[] MatrizPremeios = ItemReward.split(";");
		for(String Premios: MatrizPremeios){
			String[] indPremio = Premios.split(",");
			if(Retorno.length()>0){
				Retorno +=", ";
			}
			Retorno+= opera.getFormatNumbers(indPremio[1]) + " " +  getNombreITEMbyID(Integer.valueOf(indPremio[0]));
		}

		return Retorno;
	}

	public static String ItemNeedShowBox(String ItemNeed){
		return ItemNeedShowBox(ItemNeed, "Cost of service");
	}

	public static String ItemNeedShowBox(String ItemNeed,String Titulo){
		String Retorno = ItemNeedShow(ItemNeed);
		Retorno = headFormat("<font color=FE9A2E>"+Titulo+"</font><br1>" + Retorno);
		return Retorno;
	}

	public static String ItemNeedShow_line(String ItemNeed){
		String Retorno ="";

		String[] MatrizPremeios = ItemNeed.split(";");
		for(String Premios: MatrizPremeios){
			String[] indPremio = Premios.split(",");
			if(Retorno.length()>0){
				Retorno +=", ";
			}
			try{
				Retorno+= "<font color=LEVEL>" + indPremio[1] + " " +  getNombreITEMbyID(Integer.valueOf(indPremio[0])) + "</font>";
			}catch(Exception a){
				_log.warning("CHECK ITEM-> Error. ItemID not exist: " + indPremio[0]);
			}
		}

		return Retorno;
	}


	public static String ItemNeedShow(String ItemNeed){
		String Retorno ="";

		String[] MatrizPremeios = ItemNeed.split(";");
		for(String Premios: MatrizPremeios){
			String[] indPremio = Premios.split(",");
			if(Retorno.length()>0){
				Retorno +="<br1>";
			}
			try{
				Retorno+= "<font color=LEVEL>" + opera.getFormatNumbers(indPremio[1]) + "</font> of <font color=FF8000>" +  getNombreITEMbyID(Integer.valueOf(indPremio[0])) + "</font>";
			}catch(Exception a){
				_log.warning("CHECK ITEM-> Error. ItemID not exist: " + indPremio[0]);
			}
		}

		return Retorno;
	}

	public static void msgbox(String Mensaje,L2PcInstance st){
		st.sendMessage(Mensaje);
	}
	
	public static int getStatusDesafioBusNPC(){
		Connection conn = null;
		int _estadoDesafio = 0;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			String qry = "call sp_evento_inicial(-2,-1)";
			CallableStatement psqry = conn.prepareCall(qry);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()) {
				_estadoDesafio = rss.getInt(1);
			}

		}catch(SQLException e){

		}
		
		try{
			conn.close();
		}catch(Exception a){
			
		}
		return _estadoDesafio;
	}
	

	public static int[] getStatusDesafio(L2PcInstance st){
		Connection conn = null;
		int[] _estadoDesafio = new int[800];
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			String qry = "call sp_evento_inicial(-1,-1)";
			CallableStatement psqry = conn.prepareCall(qry);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()) {
				_estadoDesafio[Integer.valueOf(rss.getString(1))] = _estadoDesafio[Integer.valueOf(rss.getString(1))] + 1;
			}

		}catch(SQLException e){

		}
		
		try{
			conn.close();
		}catch(Exception a){
			
		}

		return _estadoDesafio;
	}

	public static String getNombreITEMbyID(int id, boolean showLogError){
		return getNombreITEMbyID(id, showLogError, false);
	}
	public static String getNombreITEMbyID(int id, boolean showLogError, boolean forDropSearchFilter){
		String Nombre = "";
		try{
			if(!forDropSearchFilter){
				Nombre = ItemTable.getInstance().getTemplate(id).getName();
			}else{
				String NomTemp = ItemTable.getInstance().getTemplate(id).getName();
				//indexOf(nameSearch.toUpperCase())>=0
				if((NomTemp.toUpperCase().indexOf("FOR EVENTS")>0) ||
					(NomTemp.toUpperCase().indexOf("LIMITED PERIOD")>0) ||
					(NomTemp.toUpperCase().indexOf("(EVENT)")>0) ||
					(NomTemp.toUpperCase().indexOf("DAY PACK")>0) ||
					(NomTemp.toUpperCase().indexOf("- EVENT")>0) ||
					(NomTemp.toUpperCase().indexOf("{PVP}")>0)
					){
					Nombre = "NULL";
				}else{
					Nombre = ItemTable.getInstance().getTemplate(id).getName();
				}
			}
		}catch(Exception a){
			Nombre = "NULL";
			if(showLogError){
				_log.warning("CHECK ITEM-> Error. ItemID not exist: " + String.valueOf(id));
			}
		}
		return Nombre;
	}

	public static String getNombreITEMbyID(int id){
		return getNombreITEMbyID(id,true);
	}

	public static String getTipoITEMbyID(int id){
		try{
			return ItemTable.getInstance().getTemplate(id).getItemType().toString();
		}catch(Exception a){
			_log.warning("CHECK ITEM-> Error. ItemID not exist: " + String.valueOf(id));
			return "NULL";
		}
	}

	public static String EjecutarDesafio(L2PcInstance st,String opc){
		String Respuesta="";
		Connection conn = null;
		try{
		conn = ConnectionFactory.getInstance().getConnection();
		String qry = "call sp_evento_inicial(" + opc + "," + String.valueOf(st.getObjectId()) + ")";
		CallableStatement psqry = conn.prepareCall(qry);
		ResultSet rss = psqry.executeQuery();

		while (rss.next()) {
			Respuesta = rss.getString(1);
		}
		}catch(SQLException e){

		}
		
		try{
			conn.close();
		}catch(Exception a){
			
		}

		return Respuesta;
	}

	public static String desafioCheck(L2PcInstance st,String opc){
		st.storeMe();
		String[] splitRespuesta;
		switch (opc){
			case "1":
				if(st.getLevel() == 85){
					splitRespuesta = EjecutarDesafio(st,String.valueOf(1)).split(";");
					if (splitRespuesta[0].equals("cor")){
						msgbox(splitRespuesta[1],st);
						opera.giveReward(st,general.DESAFIO_85_PREMIO);
						opera.AnunciarTodos("Challenge", msg.CHALLENGE_EL_$player_YA_ES_85_POSICION_$posi_DE_$total.replace("$player",st.getName()).replace("$posi", splitRespuesta[2]).replace("$total", splitRespuesta[3]) );
					}else{
						msgbox(splitRespuesta[1],st);
					}
				}else{
					msgbox(msg.NECESITAS_SER_LVL_$level_PARA_ESTA_OPERACION.replace("$level", "85"),st);
				}
			break;
			case "2":
				if(st.isNoble()){
					splitRespuesta = EjecutarDesafio(st,String.valueOf(2)).split(";");
					if(splitRespuesta[0].equals("cor")){
						msgbox(splitRespuesta[1],st);
						opera.giveReward(st,general.DESAFIO_NOBLE_PREMIO);
						opera.AnunciarTodos("Challenge", msg.CHALLENGE_EL_$player_YA_ES_NOBLE_POSICION_$posi_DE_$total.replace("$player",st.getName()).replace("$posi", splitRespuesta[2]).replace("$total", splitRespuesta[3]) );
					}else{
						msgbox(splitRespuesta[1],st);
					}
				}else{
					msgbox(msg.NECESITAS_SER_NOBLE_PARA_ESTA_OPERACION,st);
				}
			break;
		}
		return "";

	}



	public static boolean isNameDisponibleAIO(L2PcInstance st){
		boolean Responder = false;
		Connection conn = null;
		try{
		conn = ConnectionFactory.getInstance().getConnection();
		String qry = "call sp_buffaio(2,'"+ st.getName() +"')";
		CallableStatement psqry = conn.prepareCall(qry);
		ResultSet rss = psqry.executeQuery();
		while (rss.next()){
			if(rss.getInt("A") == 0) {
				Responder = true;
			}
		}
		}catch(SQLException e){

		}
		try{
			conn.close();
		}catch(Exception a){
			
		}
		return Responder;
	}

	public static void healAll(L2PcInstance st, boolean isSummon){
		if(!isSummon){
				st.getStatus().setCurrentHp(st.getStat().getMaxHp());
				st.getStatus().setCurrentMp(st.getStat().getMaxMp());
				st.getStatus().setCurrentCp(st.getStat().getMaxCp());
		}else{
			if(st.getSummon() != null){
				st.getSummon().getStatus().setCurrentHp(st.getSummon().getStat().getMaxHp());
				st.getSummon().getStatus().setCurrentMp(st.getSummon().getStat().getMaxMp());

			}
		}
	}



	public static String _setSkillAIOS(L2PcInstance st){
		return "";
		/*if(!isNameDisponibleAIO(st)) {
			return "The name already exists as AIO Char";
		}

		msgbox("=================",st);
		msgbox(msg.LA_CREACION_DEL_AIO_HA_COMENZADO,st);
		msgbox("=================",st);

		if(!st.isNoble()){
			st.setNoble(true);
			st.addExpAndSp(93836,0);
			opera.giveReward(st,general.NOBLESS_TIARA,1);
		}
		Connection conn = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			String qry = "call sp_buffaio(1,'"+ st.getName() +"')";
			CallableStatement psqry = conn.prepareCall(qry);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				L2Skill newSkill = SkillData.getInstance().getInfo(rss.getInt("idBuff") ,rss.getInt("buffLevel"));
				st.addSkill(newSkill,true);
			}
			opera.removeItem(general.DONA_ID_ITEM,general.DONA_COST_BUFFER,st);
			st.setName( "[BUFF]"+st.getName());
			st.store();
			st.broadcastUserInfo();
			msgbox("=================",st);
			msgbox(msg.AIO_CREADO_CON_EXITO,st);
			msgbox("=================",st);
		}catch(SQLException e){
			msgbox("=================",st);
			msgbox(msg.ERROR_EN_ESTE_PROCESO,st);
			msgbox("=================",st);
			_log.warning("Error en crear AIO: " + e.getMessage() );
		}
		
		try{
			conn.close();
		}catch(Exception a){
			
		}

		return "";*/
	}

	public static String[] getDateCastleRegEnd(L2PcInstance st){
		String ListaDate[] = new String[20];
		String Prelist ="";
		int Contador =0;
		try{
			Connection conn = ConnectionFactory.getInstance().getConnection();
			String qry = "SELECT FROM_UNIXTIME(castle.regTimeEnd /1000) as FechaLimite FROM castle ORDER BY castle.id ASC";
			PreparedStatement psqry = conn.prepareStatement(qry);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
					Prelist = rss.getString("FechaLimite");
					ListaDate[Contador] = Prelist;
					Contador++;
			}
			conn.close();
		}catch(SQLException e){

		}
		return ListaDate;
	}

	public static String generaQuery(){
		int aa = 1;
		int conteggio = 0;
		String qry = "";
		String[] tipiBuff = new String[20];
				if(general.BUFFER_IMPROVED_SECTION){
					tipiBuff[conteggio] = "\"improved\"";
					conteggio++;
				}
				if (general.BUFFER_BUFF_SECTION){
					tipiBuff[conteggio] = "\"buff\"";
					conteggio++;
				}
				if (general.BUFFER_CHANT_SECTION){
					tipiBuff[conteggio] ="\"chant\"";
					conteggio++;
				}
				if (general.BUFFER_DANCE_SECTION){
					tipiBuff[conteggio] = "\"dance\"";
					conteggio ++;
				}
				if (general.BUFFER_SONG_SECTION){
					tipiBuff[conteggio] = "\"song\"";
					conteggio ++;
				}
				if (general.BUFFER_RESIST_SECTION){
					tipiBuff[conteggio] = "\"resist\"";
					conteggio ++;
				}
				if (general.BUFFER_PROPHECY_SECTION){
					tipiBuff[conteggio] = "\"prophecy\"";
					conteggio ++;
				}
				if (general.BUFFER_SPECIAL_SECTION){
					tipiBuff[conteggio] = "\"special\"";
					conteggio ++;
				}
				if (general.BUFFER_OTROS_SECTION){
					tipiBuff[conteggio] = "\"others\"";
					conteggio ++;
				}
				while(aa <= conteggio){
					if(aa == conteggio) {
						qry += tipiBuff[aa - 1];
					} else {
						qry += tipiBuff[aa - 1] + ",";
					}
					aa ++;
				}
				return qry;
	}


	public static void setBlockPJSeg(L2PcInstance Player, int Segundos){
		//long endtime = System.currentTimeMillis()/1000 + general.VOTO_REWARD_SEG_ESPERA;
		Player.sendPacket(new SetupGauge(3, Integer.valueOf((Segundos * 1000) + 300)));
	}

	public static Boolean SetBlockTimeVote(L2PcInstance st){
		long endtime = (System.currentTimeMillis()/1000) + general.VOTO_REWARD_SEG_ESPERA;
		//st.set("blockUntilTime",String.valueOf(endtime));
		st.sendPacket(new SetupGauge(3, Integer.valueOf((general.VOTO_REWARD_SEG_ESPERA * 1000) + 300)));
		Boolean val = true;
		return val;
	}






	public static String LineaDivisora(int Ancho){
		return "<img src=\"L2UI.SquareBlank\" width=280 height=" + String.valueOf(Ancho)  + "><img src=\"L2UI.SquareGray\" width=280 height="+String.valueOf(Ancho)+"><img src=\"L2UI.SquareBlank\" width=270 height="+String.valueOf(Ancho)+">";
	}


	public static String ErrorTipeoEspacio(){
		String HTML_STR = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		HTML_STR += LineaDivisora(2) + headFormat("ERROR") + LineaDivisora(2);
		HTML_STR += LineaDivisora(2) + headFormat(msg.ERROR_TIPEO,"B40404") + LineaDivisora(2);
		HTML_STR += "</body></html>";
		return HTML_STR;
	}

	public static String headFormat(String Titulo){
		String TITLE_NPC;
		if (Titulo == "") {
			TITLE_NPC = msg.MENSAJE_BIENVENIDA;
		} else {
			TITLE_NPC = Titulo;
		}

		String MAIN_HTM = "<center><table width=280 border=0 bgcolor=151515>";
		MAIN_HTM += "<tr><td width=280 align=center fixwidth=280><font color=EBDF6C>"+TITLE_NPC+"</font></td></tr></table></center>";
		return MAIN_HTM;
	}

	public static String headFormat(String Titulo,String Color){
		String TITLE_NPC;
		if (Titulo == "") {
			TITLE_NPC = msg.MENSAJE_BIENVENIDA;
		} else {
			TITLE_NPC = Titulo;
		}
		String MAIN_HTM = "<center><table width=280 border=0 bgcolor=151515>";
		MAIN_HTM += "<tr><td width=280 align=center fixwidth=280><font color="+Color+">"+TITLE_NPC+"</font></td></tr></table></center>";
		return MAIN_HTM;
	}


	public static String BotonClasicCentral(String Boton, String Boton2){
		String MAIN_HTM = "";
		MAIN_HTM = "<table width=280 border=0 bgcolor=151515><tr>";
		MAIN_HTM += "<td width=12 align=left></td>";
		MAIN_HTM += "<td width=128 align=center>" + Boton + "</td>";
		MAIN_HTM += "<td width=128 align=center>"+ Boton2 +"</td>";
		MAIN_HTM += "<td width=12 align=left></td>";
		MAIN_HTM += "</tr>";
		MAIN_HTM += "</table><img src=\"L2UI.SquareBlank\" width=280 height=1>";
		return MAIN_HTM	;
	}


	public static String BotonCentral(String Boton, String Mensaje, int Color){
		String COLORPINTAR;
		if (Color == 0) {
			COLORPINTAR = "C4A23D";
		} else {
			COLORPINTAR = "C4803D";
		}

		String MAIN_HTM = "";

		MAIN_HTM = "<table width=280 border=0 bgcolor=151515><tr>";
		MAIN_HTM += "<td width=2 align=left></td>";
		MAIN_HTM += "<td width=90 align=left>" + Boton + "</td>";
		MAIN_HTM += "<td width=180 align=left><font color="+COLORPINTAR+">" + Mensaje +"</font></td>";
		MAIN_HTM += "<td width=2 align=left></td>";
		MAIN_HTM += "</tr>";
		MAIN_HTM += "</table><img src=\"L2UI.SquareBlank\" width=280 height=1>";
		return MAIN_HTM	;
	}

	public static String BotonCentral(String Boton, String Mensaje){
		String MAIN_HTM = "<table width=280 border=0 bgcolor=151515><tr>";
		MAIN_HTM += "<td width=90 align=left>" + Boton + "</td>";
		MAIN_HTM += "<td width=130 align=right>" + Mensaje + "</td>";
		MAIN_HTM += "</tr></table>";
		MAIN_HTM += LineaDivisora(2);
		return MAIN_HTM	;
	}

	public static String BotonCentral_sinFormato(String Boton, String Mensaje){
		String MAIN_HTM = "<tr><td width=2 align=left></td>";
		MAIN_HTM += "<td width=90 align=left>" + Boton + "</td>";
		MAIN_HTM += "<td width=130 align=right>" + Mensaje + "</td>";
		MAIN_HTM += "</tr>";
		MAIN_HTM += LineaDivisora(1);
		return MAIN_HTM	;
	}

	public static String BotonGOBACKZEUS(){
		return LineaDivisora(2) + headFormat("<button value=\"Main Menu\" action=\"bypass -h ZeuSNPC reloadscript 0 0 0\" width=100 height=28 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","WHITE") + LineaDivisora(2);
	}


	public static boolean isNumeric( String s ){
	    try{
	        double y = Double.parseDouble( s );
	        return true;
	    }
	    catch( NumberFormatException err ){
	        return false;
	    }
	}

	public static int INT_PINTAR_GRILLA(int INICIO){

		if(INICIO >0) {
			IntPintarGrilla = INICIO;
		}
		if(IntPintarGrilla == 1) {
			IntPintarGrilla = 0;
		} else {
			IntPintarGrilla = 1;
		}

		return IntPintarGrilla;
	}

	/* Freya
	public static String getClassName(L2PcInstance st,int idClass){
		return CharTemplateTable.getInstance().getClassNameById(idClass);
	}

	public static String getClassName(L2PcInstance st){
		return CharTemplateTable.getInstance().getClassNameById(st.getClassId().getId());
	}
	*/

	public static String getClassName(L2PcInstance st,int idClass){
		return ClassListData.getInstance().getClass(idClass).getClassName();
	}

	public static String getClassName(L2PcInstance st){
		return ClassListData.getInstance().getClass(st.getClassId().getId()).getClassName();
	}

	public static void setAllConfigCharToBD(L2PcInstance player){
		int _SHOWEFECT = (general.getCharConfigEFFECT(player) ? 1 : 0);
		int _SHOWANNOU = (general.getCharConfigANNOU(player) ? 1 : 0);
		int _SHOWMYSTAT = (general.getCharConfigSHOWSTAT(player) ? 1 : 0);
		int _SHOWPIN = (general.getCharConfigPIN(player) ? 1 : 0);
		int _HERO = (general.getCharConfigHERO(player) ? 1 : 0);
		int _EXPSP = (general.getCharConfigEXPSP(player) ? 1 : 0);
		int _TRADE = (general.getCharConfigTRADE(player) ? 1 : 0);
		int _BADBUFF = (general.getCharConfigBADBUFF(player) ? 1 : 0);
		int _HIDESTORE = (general.getCharConfigHIDESTORE(player) ? 1 : 0);
		int _REFUSAL = (general.getCharConfigREFUSAL(player) ? 1 : 0);
		int _PARTYMATCHING = ( general.getCharConfigPartyMatching(player) ? 1 : 0);
		set_ConfigCHAR(player, _SHOWEFECT, _SHOWANNOU, _SHOWMYSTAT, _SHOWPIN, _HERO, _EXPSP, _TRADE, _BADBUFF, _HIDESTORE, _REFUSAL,_PARTYMATCHING);
	}


	public static void set_ConfigCHAR(L2PcInstance player, Integer _SHOWEFECT, Integer _SHOWANNOU, Integer _SHOWMYSTAT,
										Integer _SHOWPIN, Integer _HERO, Integer _EXPSP, Integer _TRADE, Integer _BADBUFF, Integer _HIDESTORE, Integer _REFUSAL, Integer _PARTYMATCHING)
	{
		String UPDATE_CONFIG_PVPPK = "UPDATE zeus_char_config SET " +
				"annouc=?," +
				"effect=?," +
				"statt=?," +
				"pin=?," +
				"hero=?," +
				"expsp=?," +
				"trade=?,"+
				"badbuff=?,"+
				"hidestore=?, "+
				"refusal=?,"+
				"partymatching=?"+
				" WHERE idchar=?";
		try (Connection con = ConnectionFactory.getInstance().getConnection())
		{
			PreparedStatement statement;
			statement = con.prepareStatement(UPDATE_CONFIG_PVPPK);
			statement.setString(1, _SHOWANNOU.toString());
			statement.setString(2, _SHOWEFECT.toString());
			statement.setString(3, _SHOWMYSTAT.toString());
			statement.setString(4, _SHOWPIN.toString());
			statement.setString(5, _HERO.toString());
			statement.setString(6, _EXPSP.toString());
			statement.setString(7, _TRADE.toString());
			statement.setString(8, _BADBUFF.toString());
			statement.setString(9, _HIDESTORE.toString());
			statement.setString(10, _REFUSAL.toString());
			statement.setString(11, _PARTYMATCHING.toString());
			statement.setInt(12, player.getObjectId());
			statement.execute();
			statement.close();
		}
		catch (SQLException e)
		{
			_log.warning("ZeuS Config-> Error Save personal Config '"+ player.getName() +"': " + e.getMessage());
		}
		general.getInstance().loadCharConfig(player);
	}



}
