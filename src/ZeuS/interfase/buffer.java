package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.GameServer;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.actor.instance.L2CubicInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.serverpackets.SetupGauge;

import ZeuS.Config.general;
import ZeuS.procedimientos.opera;

public class buffer {

	private static Connection con = null;
	private static PreparedStatement ins;

	private static final Logger _log = Logger.getLogger(GameServer.class.getName());

	public static String Delete(L2PcInstance st, String eventParam1){
		if(!general._activated()){
			return "";
		}
		try{
		con=ConnectionFactory.getInstance().getConnection();
		ins=con.prepareStatement("DELETE FROM aionpc_scheme_list WHERE id=? LIMIT 1");
		ins.setString(1, eventParam1);
		try{
			ins.executeUpdate();
		}catch(SQLException e){

		}
		ins=con.prepareStatement("DELETE FROM aionpc_scheme_contents WHERE scheme_id=?");
		ins.setString(1, eventParam1);
		try{
			ins.executeUpdate();
			ins.close();
			ins.close();
		}catch(SQLException e){
			try{
				con.close();
			}catch (SQLException e2) {

			}

		}
		}catch (SQLException e) {
			return rebuildMainHtml(st,"");
		}
		return rebuildMainHtml(st,"");
	}

	public static String Create(L2PcInstance st, String eventParam1){
		String param = eventParam1.replace("."," ");
		try{
		con=ConnectionFactory.getInstance().getConnection();
		if(param.equals("no_name")) {
			return mostraTesto(st, "INFO:", "Please, enter the scheme name!", true, "return", "main", "True");
		} else{
			ins = con.prepareStatement("INSERT INTO aionpc_scheme_list (player_id,scheme_name) VALUES (?,?)");
			ins.setString(1, String.valueOf(st.getObjectId()));
			ins.setString(2, param);
			try{
				ins.executeUpdate();
				ins.close();
				con.close();
			}catch(SQLException e){
				return rebuildMainHtml(st,"");
			}

		}
		}catch(SQLException e1 ){
			return rebuildMainHtml(st,"");
		}
		return rebuildMainHtml(st,"");
	}

	public static void BuffPet(L2PcInstance st,int CurTime, String eventParam1){
		if (CurTime > general.charBufferTime.get(st)){
			try{
			con = ConnectionFactory.getInstance().getConnection();
			if(eventParam1.equals("PetOn")){
				ins = con.prepareStatement("INSERT INTO aionpc_pet_list (player_id,scheme_name) VALUES (?,?)");
				ins.setString(1, String.valueOf(st.getObjectId()));
				ins.setString(2, eventParam1);
				try{
					ins.executeUpdate();
					ins.close();
					con.close();
				}catch (SQLException e) {

				}
			}else{
				ins=con.prepareStatement("DELETE FROM aionpc_pet_list WHERE player_id=? LIMIT 1");
				ins.setString(1,String.valueOf(st.getObjectId()));
				try{
					ins.executeUpdate();
					ins.close();
					con.close();
				}catch(SQLException e){
					try{
						con.close();
					}catch (SQLException e1) {

					}
				}
			}
		}catch(SQLException e){

		}
		}

	}


	public static String rebuildMainHtml(L2PcInstance st, String playerAbilitato){
		if(!general._activated()){
			return "";
		}
		String HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		HTML += central.LineaDivisora(2) + central.headFormat("Buffer Scheme") + central.LineaDivisora(2);
		String CORPO = "";
		String pulsanteA = "Auto Buff";
		String pulsanteB = "Heal Me";
		String pulsanteC = "Rem. Buffs";
		int i = 0;
		int j = 0;
		String Temp = "<tr><td width=140 align=center>,</td>,<td width=140 align=center>,</td></tr>";
		String[] TRS = Temp.split(",");
		if(general.havePetSum.get(st) == 1){
			pulsanteA = "Auto Buff Pet"; pulsanteB = "Heal My Pet";
			pulsanteC = "Rem. Pet Buffs";
			HTML += central.LineaDivisora(2) + central.headFormat("<button value=\"Char Options\" action=\"bypass -h ZeuSNPC pet_buff 0 " + String.valueOf(playerAbilitato) + " 0 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL") + central.LineaDivisora(2);
		}else{
			HTML += central.LineaDivisora(2) + central.headFormat("<button value=\"Pet Options\" action=\"bypass -h ZeuSNPC pet_buff 1 " + String.valueOf(playerAbilitato) + " 0 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL") + central.LineaDivisora(2);
		}

		HTML += "<img src=\"L2UI.SquareGray\" width=280 height=1>";
		String DIVISORIO = "<br1><img src=\"L2UI.SquareGray\" width=280 height=1><br1>";
		if(general.BUFFER_SINGLE_BUFF_CHOICE){
			if(general.BUFFER_IMPROVED_SECTION){
				if(i > 2) {
					i = 0;
				}
				CORPO += TRS[i] + "<button value=\"Improved\" action=\"bypass -h ZeuSNPC indirizza vedi_improved 1 ";
				CORPO += String.valueOf(playerAbilitato) + " 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" + TRS[i + 1];
				i += 2;
				j += 1;
				}
				if(general.BUFFER_BUFF_SECTION){
					if(i > 2) {
						i = 0;
					}
					CORPO += TRS[i] + "<button value=\"Buffs\" action=\"bypass -h ZeuSNPC indirizza vedi_buff 1 " + String.valueOf(playerAbilitato);
					CORPO += " 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" + TRS[i + 1];
					i += 2;
					j += 1;
				}
				if (general.BUFFER_CHANT_SECTION){
					if (i > 2) {
						i = 0;
					}
					CORPO += TRS[i] + "<button value=\"Chants\" action=\"bypass -h ZeuSNPC indirizza vedi_chant 1 " + String.valueOf(playerAbilitato);
					CORPO += " 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" + TRS[i + 1];
					i += 2;
					j += 1;
				}
				if (general.BUFFER_DANCE_SECTION){
					if(i > 2) {
						i = 0;
					}
					CORPO += TRS[i] + "<button value=\"Dances\" action=\"bypass -h ZeuSNPC indirizza vedi_dance 1 " + String.valueOf(playerAbilitato);
					CORPO += " 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" + TRS[i + 1];
					i += 2;
					j += 1;
				}
				if(general.BUFFER_SONG_SECTION){
					if(i > 2) {
						i = 0;
					}
					CORPO += TRS[i] + "<button value=\"Songs\" action=\"bypass -h ZeuSNPC indirizza vedi_song 1 " + String.valueOf(playerAbilitato);
					CORPO += " 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" + TRS[i + 1];
					i += 2;
					j += 1;
				}
				if (general.BUFFER_RESIST_SECTION){
					if(i > 2) {
						i = 0;
					}
					CORPO += TRS[i] + "<button value=\"Resist\" action=\"bypass -h ZeuSNPC indirizza vedi_resist 1 ";
					CORPO += String.valueOf(playerAbilitato) + " 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" + TRS[i + 1];
					i += 2;
					j += 1;
				}
				if (general.BUFFER_CUBIC_SECTION){
					if(i > 2) {
						i = 0;
					}
					CORPO += TRS[i] + "<button value=\"Cubics\" action=\"bypass -h ZeuSNPC indirizza vedi_cubic 1 " + String.valueOf(playerAbilitato);
					CORPO += " 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" + TRS[i + 1];
					i += 2;
					j += 1;
				}
				if(general.BUFFER_PROPHECY_SECTION){
					if (i > 2) {
						i = 0;
					}
					CORPO += TRS[i] + "<button value=\"Prophecies\" action=\"bypass -h ZeuSNPC indirizza vedi_prophecy 1 ";
					CORPO += String.valueOf(playerAbilitato) + " 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" + TRS[i + 1];
					i += 2;
					j += 1;
				}
				if(general.BUFFER_SPECIAL_SECTION){
					if(i > 2) {
						i = 0;
					}
					CORPO += TRS[i] + "<button value=\"Special\" action=\"bypass -h ZeuSNPC indirizza vedi_speciali 1 ";
					CORPO += String.valueOf(playerAbilitato) + " 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" + TRS[i + 1];
					i += 2;
					j += 1;
				}
				if(general.BUFFER_OTROS_SECTION){
					if(i > 2) {
						i = 0;
					}
					CORPO += TRS[i] + "<button value=\"Others\" action=\"bypass -h ZeuSNPC indirizza vedi_altri 1 " + String.valueOf(playerAbilitato);
					CORPO += " 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" + TRS[i + 1];
					i += 2;
					j += 1;
				}
		}
				boolean sezioneBuff,sezioneFunzioni;
				String HEADER = "";
				String CORPO2 = "";

				if (j > 0){
					if (i == 2) {
						CORPO += TRS[i] + TRS[i + 1];
					}
					sezioneBuff = true;
					HEADER = central.LineaDivisora(2) + central.headFormat("Buff Individuales.") + central.LineaDivisora(2);
					i = 0;
				}else{
					sezioneBuff = false;
				}

				sezioneFunzioni = false;
				CORPO2 = "";
				if(general.BUFFER_AUTOBUFF){
					sezioneFunzioni = true;
					CORPO2 += "<tr><td width=280><center><button value=\"" + pulsanteA + "\" action=\"bypass -h ZeuSNPC assegna_pacchetto_buff";
					CORPO2 += " " + String.valueOf(playerAbilitato) + " 0 0 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
					CORPO2 += "</td></tr></table><table bgcolor=424141>";
					j += 1;
				}
				if(general.BUFFER_HEAL){
					sezioneFunzioni = true;
					CORPO2 += TRS[i] + "<button value=\"" + pulsanteB + "\" action=\"bypass -h ZeuSNPC pieno " + String.valueOf(playerAbilitato);
					CORPO2 += " 0 0 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" + TRS[i + 1];
					i += 2; j += 1;
				}
				if(general.BUFFER_REMOVE_BUFF){
					sezioneFunzioni = true;
					CORPO2 += TRS[i] + "<button value=\"" + pulsanteC + "\" action=\"bypass -h ZeuSNPC rimuovi_buff ";
					CORPO2 += String.valueOf(playerAbilitato) + " 0 0 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" + TRS[i + 1];
					i += 2;
					j += 1;
				}
				if (j == 0) {
					HTML += "<br>";
				} else{
					if(sezioneBuff) {
						HTML += HEADER + "<br1><table bgcolor=151515 width=280>" + CORPO + "</table>" + DIVISORIO;
					}
					if (sezioneFunzioni) {
						HTML += central.LineaDivisora(2) + central.headFormat("Funciones","LEVEL") + central.LineaDivisora(2) + "<br1><table width=280 bgcolor=424141>" + CORPO2 + "</table>"+ DIVISORIO;
					}
				}
			if(general.BUFFER_SCHEME_SYSTEM) {
				HTML += central.LineaDivisora(2) + central.headFormat("Esquemas de Buff","LEVEL") + central.LineaDivisora(2) + generaSchema(st, playerAbilitato) + DIVISORIO;
			}
			if(playerAbilitato.equals("True")){
				/*Removido y puesto en Configuracion NPC*/
			}
			HTML += central.BotonGOBACKZEUS() + central.getPieHTML() + "</body></html>";
		return HTML;
	}





	public static String generaSchema(L2PcInstance st, String playerAbilitato){
		if(!general._activated()){
			return "";
		}
		Vector<String> elencoSchemi = new Vector<String>();
		String HTML = "";
		String idSchema,perPlayer,nomeSchema;
		String datiSchema="";
		try{
		Connection conn = ConnectionFactory.getInstance().getConnection();
		String qry = "SELECT * FROM zeus_buffer_scheme_list WHERE playerId=" + String.valueOf(st.getObjectId());
		PreparedStatement psqry = conn.prepareStatement(qry);
		ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					idSchema = rss.getString("id");
					perPlayer = rss.getString("buffPlayer");
					nomeSchema = rss.getString("schemeName");
					nomeSchema = nomeSchema.replace(" ","*");
					datiSchema = idSchema + "_" + perPlayer + "_" + nomeSchema;
					elencoSchemi.add(datiSchema);
				}catch(SQLException e){

				}
			}
		conn.close();
		}catch(SQLException e){

		}

		String CORPO = "";
		if(elencoSchemi.size()>0){
			if(elencoSchemi.size() == 1){
				datiSchema = elencoSchemi.get(0);
				datiSchema = datiSchema.replace("_"," ");
				String[] splitSchema = datiSchema.split(" ");
				idSchema = splitSchema[0];
				perPlayer = splitSchema[1];
				nomeSchema = splitSchema[2];
				nomeSchema = nomeSchema.replace("*"," ");
				CORPO = "<tr><td width=280 align=CENTER><button value=\"" + nomeSchema + "\" action=\"bypass -h ZeuSNPC assegna " + idSchema + " ";
				CORPO += perPlayer + " " + String.valueOf(playerAbilitato) + " 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				CORPO += "</td></tr>";
			}else{
				int i = 0;
				int j = 0;
				int k = 0;
				String Temp = "<tr><td width=280 align=CENTER>,</td>,<td width=280 align=CENTER>,</td></tr>";
				String[] TRS = Temp.split(",");
				boolean dispari = false;
				j = elencoSchemi.size() - 1;
				if((elencoSchemi.size() % 2) != 0){
					j--;
					dispari = true;
				}
				while(i <= j){
					if(k > 2){
						k = 0;
					}
					datiSchema = elencoSchemi.get(i);
					datiSchema = datiSchema.replace("_"," ");
					String splitSchema[] = datiSchema.split(" ");
					idSchema = splitSchema[0];
					perPlayer = splitSchema[1];
					nomeSchema = splitSchema[2];
					nomeSchema = nomeSchema.replace("*"," ");
					CORPO += TRS[k] + "<button value=\"" + nomeSchema + "\" action=\"bypass -h ZeuSNPC assegna " + idSchema + " ";
					CORPO += perPlayer + " " + String.valueOf(playerAbilitato) + " 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"";
					CORPO += ">" + TRS[k + 1];
					i += 1;
					k += 2;
				}
				if(dispari){
					datiSchema = elencoSchemi.get(i);
					datiSchema = datiSchema.replace("_"," ");
					String splitSchema[] = datiSchema.split(" ");
					idSchema = splitSchema[0];
					perPlayer = splitSchema[1];
					nomeSchema = splitSchema[2];
					nomeSchema = nomeSchema.replace("*"," ");
					CORPO += "</table><table bgcolor=151515 width=280><tr><td align=CENTER width=280><button value=\"" + nomeSchema + "\" action=\"bypass -h ZeuSNPC assegna ";
					CORPO += idSchema + " " + perPlayer + " " + String.valueOf(playerAbilitato) + " 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"";
					CORPO += "L2UI_ct1.button_df\"></td></tr>";
				}
			}
			HTML += "<table  bgcolor=151515 width=280>" + CORPO + "</table>" + central.LineaDivisora(2);
		}



		if(elencoSchemi.size() < general.BUFFER_SCHEMA_X_PLAYER){
			HTML += "<table bgcolor=151515 width=280><tr><td width=93 align=CENTER><button value=\"Create\" action=\"bypass -h ZeuSNPC crea_1 " + String.valueOf(playerAbilitato) + " 0 0 0\" ";
			HTML += "width=85 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td><td>";
		}else{
			HTML += "<table bgcolor=151515 width=280><tr><td width=93 align=CENTER>";
		}

		if(elencoSchemi.size() > 0){
			HTML += "<button value=\"Edit\" action=\"bypass -h ZeuSNPC edita_schema " + String.valueOf(playerAbilitato) + " 0 0 0\" width=85 ";
			HTML += "height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td><td width=93 align=CENTER><button value=\"Delete\" action=\"bypass -h ";
			HTML += "ZeuSNPC cancella_schema " + String.valueOf(playerAbilitato) + " 0 0 0\" width=85 height=20 back=\"L2UI_ct1.button_df\" ";
			HTML += "fore=\"L2UI_ct1.button_df\"></td></tr></table>";
		}else{
			HTML += "</td></tr></table>";
		}
		return HTML;
	}

	public static String costruisciHtml(String tipoBuff, int pagina, String playerAbilitato){
		if(!general._activated()){
			return "";
		}
		int prezzo = 0;
		int larghezzaCella = 0;
		int larghezza =0;

		switch(tipoBuff){
			case "improved":
				prezzo = general.BUFFER_IMPROVED_VALOR;
				larghezza = general.WIDTH_GRAFI_IMPROVED;
				break;
			case "buff"://elif tipoBuff == "buff" :
				prezzo = general.BUFFER_BUFF_VALOR;
				larghezza = general.WIDTH_GRAFI_BUFF;
				break;
			case "chant":
				prezzo = general.BUFFER_CHANT_VALOR;
				larghezza = general.WIDTH_GRAFI_CHANT;
				break;
			case "dance":
				prezzo = general.BUFFER_DANCE_VALOR;
				larghezza = general.WIDTH_GRAFI_DANCE;
				break;
			case "song":
				prezzo = general.BUFFER_SONG_VALOR;
				larghezza = general.WIDTH_GRAFI_SONG;
				break;
			case "resist":
				prezzo = general.BUFFER_RESIST_VALOR;
				larghezza = general.WIDTH_GRAFI_RESIST;
				break;
			case "cubic":
				prezzo = general.BUFFER_CUBIC_VALOR;
				larghezza = general.WIDTH_GRAFI_CUBIC;
				break;
			case "prophecy":
				prezzo = general.BUFFER_PROHECY_VALOR;
				larghezza = general.WIDTH_GRAFI_PROPHECY;
				break;
			case "special":
				prezzo = general.BUFFER_SPECIAL_VALOR;
				larghezza = general.WIDTH_GRAFI_SPECIALI;
				break;
			case "other":
				prezzo = general.BUFFER_OTROS_VALOR;
				larghezza = general.WIDTH_GRAFI_ALTRI;
				break;
		}
		String destinazione = "";
		String HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		HTML += central.LineaDivisora(2) + central.headFormat("Buffer Schemme ZeuS") + central.LineaDivisora(2);
		String HTML_1 = "";
		String BotonAtras = "<button value=\"Back\" action=\"bypass -h ZeuSNPC indirizza principale " + String.valueOf(playerAbilitato) + " 0 0 \" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br1>";
		if (general.BUFF_GRATIS){
			HTML_1 = central.LineaDivisora(2) + central.headFormat("All buffs are for <font color=\"LEVEL\">free</font>!<br1>","WHITE") + central.LineaDivisora(2);
			HTML += HTML_1;
		}else{
			if(tipoBuff.equals("prophecy")){
				HTML_1 = "All prophecy buff";
			}else{
				HTML_1 += "All " + String.valueOf(tipoBuff) + " buff";
			}
			if(!tipoBuff.equals("others")) {
				HTML_1 = "All buff's";
			}
			HTML_1 += " Cost <br1><font color=\"LEVEL\">" + String.valueOf(prezzo) + "</font> " + central.getNombreITEMbyID(general.BUFFER_ID_ITEM) + "!<br1>";
		}
		HTML_1 += "Click on the Icon Buff for Get it<br1>";

		HTML += central.LineaDivisora(2) + central.headFormat(HTML_1+BotonAtras+"<br1>","WHITE") + central.LineaDivisora(2);

		if (tipoBuff.equals("cubic")) {
			HTML += central.LineaDivisora(2) + central.headFormat("<font color=\"ff0000\">WARNING!!!:</font><br1>check your Cubic Mastery passive<br1>skill to see how many you can summon.<br1><br>If you don't have the skill your limit is 1!", "LEVEL") + central.LineaDivisora(2);
		}

		larghezzaCella = larghezza - 32;

		Vector<String> buffDisponibili = new Vector<String>();

		String stringaBuff,nomeBuff,descrizioneBuff;
		int idBuff,livelloBuff;

		try{
			Connection conn = ConnectionFactory.getInstance().getConnection();
			String qry = "SELECT buffId, buffLevel, buffDesc FROM zeus_buffer_buff_list WHERE buffType=\"" + tipoBuff + "\" AND canUse=1 ORDER BY buffOrder";
			PreparedStatement psqry = conn.prepareStatement(qry);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					idBuff = rss.getInt("buffId");
					livelloBuff = rss.getInt("buffLevel");
					descrizioneBuff = rss.getString("buffDesc");
					descrizioneBuff = descrizioneBuff.replace(" ","*");
					nomeBuff = SkillData.getInstance().getSkill(idBuff,livelloBuff).getName();
					nomeBuff = nomeBuff.replace(" ","*");
					stringaBuff = nomeBuff + "_" + descrizioneBuff + "_" + String.valueOf(idBuff) + "_" + String.valueOf(livelloBuff);
					buffDisponibili.add(stringaBuff);
				}catch(SQLException e){
					stringaBuff = "<font color=\"LEVEL\">Error loading buff. <font color=\"ff0000\">(ID: " + rss.getString("buffId") + ")</font><br1>";
					stringaBuff += "Contact a GM for assistance.</font><br>";
					buffDisponibili.add(stringaBuff);
				}
			}
			conn.close();
		}catch (SQLException e){

		}
		int pagine = 1;
		int elementiInPagine = 25;
		int ContadorPaginas = elementiInPagine;

		while(ContadorPaginas < buffDisponibili.size()){
			pagine++;
			ContadorPaginas = elementiInPagine * pagine;
		}
		String lista="";
		int i=1;
		if (pagine > 1){
			lista = "List=\"Page_" + String.valueOf(pagina) + ";";
			i = 1;
			while(i <= pagine){
				if(i != pagina) {
					lista += "Page_" + String.valueOf(i) + ";";
				}
				i++;
			}
			lista += "\"";
			destinazione = tipoBuff;
			if(destinazione.equals("special")) {
				destinazione = "speciali";
			}
			if(destinazione.equals("others")) {
				destinazione = "altri";
			}
			HTML += "<table><tr><td><combobox var=\"pagina\" width=65 " + lista + "></td><td><button value=\"Go\" action=\"bypass -h ZeuSNPC ";
			HTML += " indirizza vedi_" + destinazione + " $pagina " + String.valueOf(playerAbilitato) + " 0\" width=65 height=20 back=\"";
			HTML += "L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";
		}
		HTML += "<table width=" + String.valueOf(larghezza) + " border=" + general.BORDO_TABELLA + "><tr><td>";
		if (buffDisponibili.size() == 0){
			HTML += "<center>No buffs are available at this moment!</center>";
		}else{
			String formato = "0000";
			i = 0;
			int j = 0;
			i = (pagina - 1) * elementiInPagine;
			j = (pagina * elementiInPagine) - 1;

			String nome = "";
			String descrizione = "";
			int id = 0;
			String livello = "";

			if (j >= (buffDisponibili.size() - 1)){
				j = buffDisponibili.size() - 1;
			}
			while(i <= j){
				boolean errore = false;
				String buff = buffDisponibili.get(i);
				buff = buff.replace("_"," ");
				String[] arrayBuff = buff.split(" ");
				if(arrayBuff[0].equals("<font")) {
					errore = true;
				} else{
					nome = arrayBuff[0];
					descrizione = arrayBuff[1];
					id = Integer.valueOf(arrayBuff[2]);
					livello = arrayBuff[3];
					nome = nome.replace("*"," ");
					descrizione = descrizione.replace("*"," ");
					formato = getFormato(id);
				}
				if(errore){
					HTML += "<table bgcolor=\"330000\" width=" + String.valueOf(larghezza) + "><tr><td><center>" + buff + "</center></td></tr></table>";
				}else{
					if((i % 2) != 0){
						HTML += "<table bgcolor=333333 border=0 width=\"100%\">";
					}else{
						HTML += "<table bgcolor=292929 border=0 width=\"100%\">";
						HTML += "<tr><td width=32><button value=\"\" action=\"bypass -h ZeuSNPC assegna_buff " + String.valueOf(id) + "_";
						HTML += String.valueOf(livello) + " " + tipoBuff + " " + String.valueOf(pagina) + " " + String.valueOf(playerAbilitato) + "\" width=32 height=32 back=\"Icon.skill";
						HTML += formato + "\" fore=\"Icon.skill" + formato + "\"><br></td><td width=" + String.valueOf(larghezzaCella) + "><font color=\"00ff00\"";
						HTML += ">" + nome + "</font><br1>" + descrizione + "<br></td></tr></table>";
					}
				}
				i++;
			}
		HTML += "</td></tr></table><br1>";
		}
		if(pagine > 1){
			HTML += "<table><tr><td><combobox var=\"pagina2\" width=65 " + lista + "></td><td><button value=\"Go\" action=\"bypass -h ZeuSNPC ";
			HTML += " indirizza vedi_" + destinazione + " $pagina2 " + String.valueOf(playerAbilitato) + " 0\" width=65 height=20 back=\"";
			HTML += "L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><br1>";
		}
		HTML += central.LineaDivisora(2) + central.headFormat(BotonAtras,"WHITE") + central.LineaDivisora(2);
		HTML += central.getPieHTML() + central.BotonGOBACKZEUS()+"</body></html>";
		return HTML;
	}

	public static String getFormato(int id){
		String formato;
		if(id == 4) {
			formato = "0004";
		} else if((id > 9) && (id < 100)) {
			formato = "00" + String.valueOf(id);
		} else if((id > 99) && (id < 1000)) {
			formato = "0" + String.valueOf(id);
		} else if(id == 1517) {
			formato = "1536";
		} else if(id == 1518) {
			formato = "1537";
		} else if(id == 1547) {
			formato = "0065";
		} else if(id == 2076) {
			formato = "0195";
		} else if((id > 4550) && (id < 4555)) {
			formato = "5739";
		} else if((id > 4698) && (id < 4701)) {
			formato = "1331";
		} else if((id > 4701) && (id < 4704)) {
			formato = "1332";
		} else if(id == 6049) {
			formato = "0094";
		} else {
			formato = String.valueOf(id);
		}

		return formato;

	}


	public static String creaSchema(String playerAbilitato){
		String HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		HTML += central.LineaDivisora(2) + central.headFormat("Scheme Creation","00ff00") + central.LineaDivisora(2);
		HTML += central.headFormat("<table><tr><td width=100 align=\"center\">Scheme name:<br><edit var=\"nome\" width=100></td><td width=100 align=\"center\">Scheme for:<br><combobox var=\"perPlayer\" width=50 List=\"Me;Pet;\"></td></tr></table>" +
				"<table><tr><td><button value=\"Create Scheme\" action=\"bypass -h ZeuSNPC crea $perPlayer " + String.valueOf(playerAbilitato) + " 0 $nome\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>" +
				"<button value=\"Back\" action=\"bypass -h ZeuSNPC indirizza principale " + String.valueOf(playerAbilitato) + " 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br>");
		HTML += central.LineaDivisora(3) + central.getPieHTML() + "</center></body></html>";
		return HTML;
	}






	public static String editaSchema(L2PcInstance st, String playerAbilitato){
		if(!general._activated()){
			return "";
		}
		String nomeSchema = "";
		String idSchema = "";
		Vector<String> elencoSchemi= new Vector<String>();
		String HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		HTML += central.LineaDivisora(2) + central.headFormat("Scheme Edit Section","00ff00");
		HTML += central.LineaDivisora(2) + central.headFormat("Select a scheme that you would like to manage","LEVEL") + central.LineaDivisora(2) + "<br><br><table height=172>";
		try{
		Connection conn = ConnectionFactory.getInstance().getConnection();
		String qry = "SELECT * FROM zeus_buffer_scheme_list WHERE playerId=" + String.valueOf(st.getObjectId());
		PreparedStatement psqry = conn.prepareStatement(qry);
		ResultSet rss = psqry.executeQuery();
		while (rss.next()){
			idSchema = rss.getString("id");
			nomeSchema = rss.getString("schemeName");
			nomeSchema = nomeSchema.replace(" ","*");
			elencoSchemi.add(String.valueOf(idSchema) + "_" + String.valueOf(nomeSchema));
		}
		conn.close();
		}catch(SQLException e){

		}
		String stringaSchema ="";

		if(elencoSchemi.size() == 1){
			stringaSchema = elencoSchemi.get(0);
			stringaSchema = stringaSchema.replace("_"," ");
			String[] datiSchema = stringaSchema.split(" ");
			idSchema = datiSchema[0];
			nomeSchema = datiSchema[1];
			nomeSchema = nomeSchema.replace("*"," ");
			HTML += "<tr><td><center><button value=\"" + nomeSchema + "\" action=\"bypass -h ZeuSNPC selezione_schemi ";
			HTML += idSchema + " " + String.valueOf(playerAbilitato) + " 0 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			HTML += "</center></td></tr>";
		}else{

			int i = 0;
			int j = 0;
			int k = 0;
			boolean dispari = false;
			String temp = "<tr><td> </td> <td> </td></tr>";
			String[]TSR = temp.split(" ");
			j = elencoSchemi.size() - 1;
			if ((elencoSchemi.size() % 2) != 0){
				 j -= 1;
				dispari = true;
			}
			while (i <= j){
				if (k > 2) {
					k = 0;
				}
				stringaSchema = elencoSchemi.get(i);
				stringaSchema = stringaSchema.replace("_"," ");
				String[] datiSchema = stringaSchema.split(" ");
				idSchema = datiSchema[0];
				nomeSchema = datiSchema[1];
				nomeSchema = nomeSchema.replace("*"," ");
				HTML += TSR[k] + "<button value=\"" + nomeSchema + "\" action=\"bypass -h ZeuSNPC selezione_schemi " + idSchema;
				HTML += " " + String.valueOf(playerAbilitato) + " 0 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" + TSR[k + 1];
				i += 1;
				k += 2;
			}
			if(dispari){
				stringaSchema = elencoSchemi.get(i);
				stringaSchema = stringaSchema.replace("_"," ");
				String[] datiSchema = stringaSchema.split(" ");
				idSchema = datiSchema[0];
				nomeSchema = datiSchema[1];
				nomeSchema = nomeSchema.replace("*"," ");
				HTML += "</table><table><tr><td><center><button value=\"" + nomeSchema + "\" action=\"bypass -h ZeuSNPC ";
				HTML += "selezione_schemi " + idSchema + " " + String.valueOf(playerAbilitato) + " 0 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"";
				HTML += "L2UI_ct1.button_df\"></center></td></tr>";
			}
		}
		HTML += "</table><br><button value=\"Back\" action=\"bypass -h ZeuSNPC indirizza principale " + String.valueOf(playerAbilitato) + " 0 ";
		HTML += "0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br><br><font color=\"333333\">";
		HTML += general.TITULO_NPC() + "</font></center></body></html>";
		return HTML;
	}


	public static String ottieniListaOpzioni(int idSchema, String playerAbilitato){
		String conteggio = contaBuff(idSchema);
		conteggio = conteggio.replace("_"," ");
		String[] datiBuff = conteggio.split(" ");
		String conteggioBuff = datiBuff[0];
		String conteggioDance = datiBuff[1];
		String HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		HTML += central.LineaDivisora(2) + central.headFormat("Scheme Edit Section") + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat("There are <font color=\"LEVEL\">" + String.valueOf(conteggioBuff) + "</font> buffs and <font color=\"LEVEL\">" + String.valueOf(conteggioDance) + "</font> dances/songs in<br1>current scheme!","WHITE") + central.LineaDivisora(1);
		if((Integer.valueOf(conteggioBuff) < general.BUFF_PER_SCHEMA) || (Integer.valueOf(conteggioDance) < general.DANCE_PER_SCHEMA)) {
			HTML += central.headFormat("<button value=\"Add buffs\" action=\"bypass -h ZeuSNPC edita_schemi " + String.valueOf(idSchema) + " 0 aggiungi " + playerAbilitato + "\" width=200 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL") + central.LineaDivisora(1);
		}
		if((Integer.valueOf(conteggioBuff) > 0) || (Integer.valueOf(conteggioDance) > 0)) {
			HTML += central.headFormat("<button value=\"Remove buffs\" action=\"bypass -h ZeuSNPC edita_schemi " + String.valueOf(idSchema) + " 0 rimuovi " + playerAbilitato + "\" width=200 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL") + central.LineaDivisora(1);
		}
		HTML += "<center><button value=\"Back\" action=\"bypass -h ZeuSNPC edita_schema " + String.valueOf(playerAbilitato) + " 0 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><button value=\"Home\" action=\"bypass -h ZeuSNPC indirizza principale " + String.valueOf(playerAbilitato) + " 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		HTML += "<br><br><br><br><br><font color=\"333333\">" + general.TITULO_NPC() + "</font></center></body></html>";
		return HTML;
	}


	public static String cancellaSchema(L2PcInstance st, String playerAbilitato){
		String HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		HTML += central.LineaDivisora(2) + central.headFormat("Delete Scheme Section","ff0000");
		HTML += central.LineaDivisora(2) + central.headFormat("Available schemes","LEVEL") + central.LineaDivisora(2) + "<br><br><table height=184><tr><td>";
		try{
			Connection conn = ConnectionFactory.getInstance().getConnection();
			String qry = "SELECT * FROM zeus_buffer_scheme_list WHERE playerId=" + String.valueOf(st.getObjectId());
			PreparedStatement psqry = conn.prepareStatement(qry);
			ResultSet rss = psqry.executeQuery();
			String id,nome,nome2;
			while (rss.next()){
				id = rss.getString("id");
				nome = rss.getString("schemeName");
				nome2 = nome.replace(" ","*");
				HTML += "<button value=\"" + nome + "\" action=\"bypass -h ZeuSNPC conferma_cancella " + String.valueOf(id) + " " + nome2 + " ";
				HTML += playerAbilitato + " 0\" width=200 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			}
			conn.close();
		}catch(SQLException e){

		}
		HTML += "</td></tr></table><br><button value=\"Back\" action=\"bypass -h ZeuSNPC indirizza principale " + String.valueOf(playerAbilitato) + " 0 0\" ";
		HTML += "width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br><br><font color=\"333333\">" + general.TITULO_NPC();
		HTML += "</font></center></body></html>";
		return HTML;
	}



	public static String confermaCancella(int idSchema, String nomeSchema, String playerAbilitato){
		nomeSchema = nomeSchema.replace("*"," ");
		String BtnYes = "<button value=\"Yes\" action=\"bypass -h ZeuSNPC cancella_definitivo " + String.valueOf(idSchema) + " " + String.valueOf(playerAbilitato) + " 0 0\" width=50 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String BtnNo = "<button value=\"No\" action=\"bypass -h ZeuSNPC cancella_schema " + String.valueOf(playerAbilitato) + " 0 0 0\" width=50 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String MAIN_HTML = "";
		MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Delete Scheme Section","ff0000") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Usted va borrar permanentemente <br1><font color=LEVEL>"+nomeSchema+"</font><br1>scheme.<br1>¿Desea Proceguir?" ,"ff0000") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(BtnYes + BtnNo) + central.LineaDivisora(2);
		MAIN_HTML += "</body></html>";
		return MAIN_HTML;
	}


	public static String visualizzaBuffSchema(int idSchema, String paginaBuff, String azione, String playerAbilitato){
		int conteggio = 0;
		int conteggio_D_S = 0;
		int conteggio_B = 0;
		boolean secondaBuff = false;
		Connection conn = null;
		String qry = "";
		PreparedStatement psqry;
		ResultSet rss;

		int intPaginaBuff = 0;

		if (paginaBuff.length() > 1){
			intPaginaBuff = 1;
			secondaBuff = true;
		}else{
			intPaginaBuff = Integer.valueOf(paginaBuff);
		}
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			qry = "SELECT * FROM zeus_buffer_scheme_contents WHERE schemeId=" + String.valueOf(idSchema);
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			int classeBuff=0;
			while (rss.next()){
				try{
					classeBuff = rss.getInt("buffClass");
					conteggio++;
					if((classeBuff == 6) || (classeBuff == 7)) {
						conteggio_D_S++;
					} else {
						conteggio_B++;
					}
				}catch(SQLException e){
					conteggio = 0; conteggio_D_S = 0; conteggio_B = 0;
				}
			}
		}catch(SQLException e){

		}
		try{
			conn.close();
		}catch(SQLException a){

		}
		int BUFF_TOTALI = conteggio;
		int BUFF = conteggio_B;
		int DANCE_SONG = conteggio_D_S;



		String HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center><br1>";
		qry ="";
		if(azione.equals("aggiungi")){
			if(BUFF_TOTALI < (general.BUFF_PER_SCHEMA + general.DANCE_PER_SCHEMA)){
				if( ((intPaginaBuff <= 4) || (intPaginaBuff >= 8)) && (BUFF == general.BUFF_PER_SCHEMA)) {
					intPaginaBuff = 6;
				}
				if ( (intPaginaBuff >5) && (intPaginaBuff < 8) && (DANCE_SONG == general.DANCE_PER_SCHEMA)) {
					intPaginaBuff = 0;
				}
			}else{
				return ottieniListaOpzioni(idSchema, playerAbilitato);
			}




			HTML += central.LineaDivisora(2) + central.headFormat("You can add <font color=\"LEVEL\">" + String.valueOf(general.BUFF_PER_SCHEMA - BUFF) + "</font> Buffs and <font color=\"LEVEL\">" + String.valueOf(general.DANCE_PER_SCHEMA - DANCE_SONG) + "</font> Dances more<br1>Click on the skill icon to add.<br1>","WHITE") + central.LineaDivisora(2);

			qry = "SELECT * FROM zeus_buffer_buff_list WHERE buffClass=" + String.valueOf(intPaginaBuff) + " AND canUse=1 ORDER BY buffOrder";
		}
		if(azione.equals("rimuovi")){
			HTML += central.LineaDivisora(2) + central.headFormat("You have <font color=\"LEVEL\">" + String.valueOf(BUFF) + "</font> Buffs and <font color=\"LEVEL\">" + String.valueOf(DANCE_SONG) + "</font> Dances/Songs.<br1>Click on the skill icon to remove.<br1>","WHITE") + central.LineaDivisora(2);
			qry = "SELECT * FROM zeus_buffer_scheme_contents WHERE schemeId=" + String.valueOf(idSchema) + " AND buffClass=" + String.valueOf(intPaginaBuff) + " ORDER BY id";
		}
		String listaClassiBuff = "List=\"";
		int width = 0;
		switch(intPaginaBuff){
			case 0:
				if(azione.equals("rimuovi") || (azione.equals("aggiungi") && (BUFF < general.BUFF_PER_SCHEMA))) {
					listaClassiBuff += "Improveds;Buffs;Buffs2;Resists;Prophecies;Chants;";
				}
				if(azione.equals("rimuovi") || (azione.equals("aggiungi") && (DANCE_SONG < general.DANCE_PER_SCHEMA))) {
					listaClassiBuff += "Dances;Songs;";
				}
				if(azione.equals("rimuovi") || (azione.equals("aggiungi") && (BUFF < general.BUFF_PER_SCHEMA))) {
					listaClassiBuff += "Specials;Others;";
				}
				width = general.WIDTH_GRAFI_IMPROVED;
				break;
			case 1:
				if(!secondaBuff){
					if (azione.equals("rimuovi") || (azione.equals("aggiungi") && (BUFF < general.BUFF_PER_SCHEMA))) {
						listaClassiBuff += "Buffs;Buffs2;Resists;Prophecies;Chants;";
					}
					if (azione.equals("rimuovi") || (azione.equals("aggiungi") && (DANCE_SONG < general.DANCE_PER_SCHEMA))) {
						listaClassiBuff += "Dances;Songs;";
					}
					if (azione.equals("rimuovi") || (azione.equals("aggiungi") && (BUFF < general.BUFF_PER_SCHEMA))) {
						listaClassiBuff += "Specials;Others;Improveds;";
					}
				}else{
					if (azione.equals("rimuovi") || (azione.equals("aggiungi") && (BUFF < general.BUFF_PER_SCHEMA))) {
						listaClassiBuff += "Buffs2;Resists;Prophecies;Chants;";
					}
					if (azione.equals("rimuovi") || (azione.equals("aggiungi") && (DANCE_SONG < general.DANCE_PER_SCHEMA))) {
						listaClassiBuff += "Dances;Songs;";
					}
					if (azione.equals("rimuovi") || (azione.equals("aggiungi") && (BUFF < general.BUFF_PER_SCHEMA))) {
						listaClassiBuff += "Specials;Others;Improveds;Buffs;";
					}
				}
				width = general.WIDTH_GRAFI_BUFF;
				break;
			case 2:
				if (azione.equals("rimuovi") || (azione.equals("aggiungi") && (BUFF < general.BUFF_PER_SCHEMA))) {
					listaClassiBuff += "Resists;Prophecies;Chants;";
				}
				if (azione.equals("rimuovi") || (azione.equals("aggiungi") && (DANCE_SONG < general.DANCE_PER_SCHEMA))) {
					listaClassiBuff += "Dances;Songs;";
				}
				if (azione.equals("rimuovi") || (azione.equals("aggiungi") && (BUFF < general.BUFF_PER_SCHEMA))) {
					listaClassiBuff += "Specials;Others;Improveds;Buffs;Buffs2;";
				}
				width = general.WIDTH_GRAFI_RESIST;
				break;
			case 3:
				if (azione.equals("rimuovi") || (azione.equals("aggiungi") && (BUFF < general.BUFF_PER_SCHEMA))) {
					listaClassiBuff += "Prophecies;Chants;";
				}
				if (azione.equals("rimuovi") || (azione.equals("aggiungi") && (DANCE_SONG < general.DANCE_PER_SCHEMA))) {
					listaClassiBuff += "Dances;Songs;";
				}
				if (azione.equals("rimuovi") || (azione.equals("aggiungi") && (BUFF < general.BUFF_PER_SCHEMA))) {
					listaClassiBuff += "Specials;Others;Improveds;Buffs;Buffs2;Resists;";
				}
				width = general.WIDTH_GRAFI_PROPHECY;
				break;
			case 4:
				if(azione.equals("rimuovi") || (azione.equals("aggiungi") && (BUFF < general.BUFF_PER_SCHEMA))) {
					listaClassiBuff += "Chants;";
				}
				if(azione.equals("rimuovi") || (azione.equals("aggiungi") && (DANCE_SONG < general.DANCE_PER_SCHEMA))) {
					listaClassiBuff += "Dances;Songs;";
				}
				if(azione.equals("rimuovi") || (azione.equals("aggiungi") && (BUFF < general.BUFF_PER_SCHEMA))) {
					listaClassiBuff += "Specials;Others;Improveds;Buffs;Buffs2;Resists;";
				}
					listaClassiBuff += "Prophecies;";
					width = general.WIDTH_GRAFI_CHANT;
				break;
			case 6:
				if (azione.equals("rimuovi") || (azione.equals("aggiungi") && (DANCE_SONG < general.DANCE_PER_SCHEMA))) {
					listaClassiBuff += "Dances;Songs;";
				}
				if (azione.equals("rimuovi")|| (azione.equals("aggiungi") && (BUFF < general.BUFF_PER_SCHEMA))) {
					listaClassiBuff += "Specials;Others;Improveds;Buffs;Buffs2;Resists;";
				}
					listaClassiBuff += "Prophecies;Chants;";
					width = general.WIDTH_GRAFI_DANCE;
				break;
			case 7:
				if(azione.equals("rimuovi") || (azione.equals("aggiungi") && (DANCE_SONG < general.DANCE_PER_SCHEMA))) {
					listaClassiBuff += "Songs;";
				}
				if(azione.equals("rimuovi") || (azione.equals("aggiungi") && (BUFF < general.BUFF_PER_SCHEMA))) {
					listaClassiBuff += "Specials;Others;Improveds;Buffs;Buffs2;Resists;Prophecies;Chants;";
				}
				if(azione.equals("rimuovi") || (azione.equals("aggiungi") && (DANCE_SONG < general.DANCE_PER_SCHEMA))) {
					listaClassiBuff += "Dances;";
				}
				width = general.WIDTH_GRAFI_SONG;
				break;
			case 8:
				if(azione.equals("rimuovi") || (azione.equals("aggiungi") && (BUFF < general.BUFF_PER_SCHEMA))) {
					listaClassiBuff += "Specials;Others;Improveds;Buffs;Buffs2;Resists;Prophecies;Chants;";
				}
				if(azione.equals("rimuovi") || (azione.equals("aggiungi") && (DANCE_SONG < general.DANCE_PER_SCHEMA))) {
					listaClassiBuff += "Dances;Songs;";
				}
				width = general.WIDTH_GRAFI_SPECIALI;
				break;
			case 9:
				if(azione.equals("rimuovi") || (azione.equals("aggiungi") && (BUFF < general.BUFF_PER_SCHEMA))) {
					listaClassiBuff += "Others;Improveds;Buffs;Buffs2;Resists;Prophecies;Chants;";
				}
				if(azione.equals("rimuovi") || (azione.equals("aggiungi") && (DANCE_SONG < general.DANCE_PER_SCHEMA))) {
					;
				}
					listaClassiBuff += "Dances;Songs;";
				if(azione.equals("rimuovi") || (azione.equals("aggiungi") && (BUFF < general.BUFF_PER_SCHEMA))) {
					listaClassiBuff += "Specials;";
				}
				width = general.WIDTH_GRAFI_ALTRI;
				break;
		}
		listaClassiBuff += "\"";
		HTML += "<button value=\"Back\" action=\"bypass -h ZeuSNPC selezione_schemi " + String.valueOf(idSchema) + " " + playerAbilitato;
		HTML += " 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br1><button value=\"Home\" action=\"bypass";
		HTML += " -h ZeuSNPC indirizza principale " + String.valueOf(playerAbilitato) + " 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df";
		HTML += "\" fore=\"L2UI_ct1.button_df\"><br1><table width=285 border=0><tr><td align=\"center\" width=95>View list of:</td><td align=\"";
		HTML += "center\" width=95><combobox var=\"classeBuff\" width=90 " + listaClassiBuff + "></td><td align=\"center\" width=95><button ";
		HTML += "value=\"View List\" action=\"bypass -h ZeuSNPC edita_schemi " + String.valueOf(idSchema) + " $classeBuff " + azione + " ";
		HTML += playerAbilitato + "\" width=80 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";

		HTML += "<table border=" + String.valueOf(general.BORDO_TABELLA) + " width=" + String.valueOf(width) + "><tr><td>";
		Vector<String> listaBuff = new Vector<String>();
		Vector<String> listaBuff2 = new Vector<String>();
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			try{
				if (intPaginaBuff == 1){
					conteggio = 0;
					while (rss.next()) {
						conteggio++;
					}
					conn.close();
				}
			}catch(SQLException e){

			}
		}catch(SQLException e){

		}
		try{
			conn.close();
		}catch(SQLException a){

		}



		if(conteggio > 25){
			listaBuff2.clear();
		}

		String classeBuff = "", ordineBuff="", idBuff = "", livelloBuff ="", descrizione ="", nome ="", stringaBuff ="";

		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();

			int i = 0;
			try{
				while (rss.next()){
					classeBuff = rss.getString("buffClass");
					ordineBuff="";
					idBuff = "";
					livelloBuff ="";
					descrizione ="";
					nome ="";
					stringaBuff ="";
					if(azione.equals("aggiungi")) {
						ordineBuff = rss.getString("buffOrder");
					} else {
						ordineBuff = rss.getString("id");
					}
					idBuff = String.valueOf(rss.getInt("buffId"));
					livelloBuff = String.valueOf(rss.getInt("buffLevel"));
					if(azione.equals("aggiungi")) {
						descrizione = rss.getString("buffDesc");
					} else {
						descrizione = recuperaDescrizione(Integer.valueOf(idBuff), Integer.valueOf(livelloBuff));
					}

					descrizione = descrizione.replace(" ","*");
					nome = SkillData.getInstance().getSkill(Integer.valueOf(idBuff),Integer.valueOf(livelloBuff)).getName();
					nome = nome.replace(" ","*");
					if( azione.equals("rimuovi") || (azione.equals("aggiungi") && !buffUsato(idSchema, Integer.valueOf(idBuff), Integer.valueOf(livelloBuff)))){
						stringaBuff = classeBuff + "_" + ordineBuff + "_" + nome + "_" + descrizione + "_" + idBuff + "_" + livelloBuff;
						if(intPaginaBuff == 1){
							if(i <= 24) {
								listaBuff.add(stringaBuff);
							} else {
								listaBuff2.add(stringaBuff);
							}
						}else{
							listaBuff.add(stringaBuff);
						}
					}
					i++;
				}
			}catch(SQLException e){
				_log.warning("BUFF->" + e.getMessage());
			}
		}catch(SQLException e){
			_log.warning("BUFF->" + e.getMessage());
		}
		try {
			conn.close();
		} catch (SQLException e) {
		}
		stringaBuff = "";
		String formato = "0000";
		int i = 0;
		if ((intPaginaBuff == 1) && secondaBuff){
			if(conteggio > 25) {
				listaBuff = listaBuff2;
			} else {
				listaBuff = null;
			}
		}
		if (listaBuff==null){
			HTML += "<table bgcolor=330000 border=0 width=" + String.valueOf(width) + "><tr><td>";
			HTML += "<center>No more ";
			if ((intPaginaBuff == 6) || (intPaginaBuff == 7)) {
				HTML += "dances/songs ";
			} else {
				HTML += "buffs ";
			}
			HTML += "available in this class!</center></td></tr></table>";
		}else{
			Collections.sort(listaBuff);
			String livelloSkill;
			nome = "";
			descrizione = "";
			int idSkill;
			while (i <= (listaBuff.size() - 1)){
				stringaBuff = listaBuff.get(i);
				stringaBuff = stringaBuff.replace("_"," ");
				String[] splitBuff = stringaBuff.split(" ");
				nome = splitBuff[2];
				nome = nome.replace("*"," ");
				descrizione = splitBuff[3];
				descrizione = descrizione.replace("*"," ");
				idSkill = Integer.valueOf(splitBuff[4]);
				livelloSkill = splitBuff[5];
				if(idSkill == 4) {
					formato = "0004";
				} else if ((idSkill > 9) && (idSkill < 100)) {
					formato = "00" + String.valueOf(idSkill);
				} else if ((idSkill > 99) && (idSkill < 1000)) {
					formato = "0" + String.valueOf(idSkill);
				} else if (idSkill == 1517) {
					formato = "1536";
				} else if (idSkill == 1518) {
					formato = "1537";
				} else if (idSkill == 1547) {
					formato = "0065";
				} else if (idSkill == 2076) {
					formato = "0195";
				} else if ((idSkill > 4550) && (idSkill < 4555)) {
					formato = "5739";
				} else if ((idSkill > 4698) && (idSkill < 4701)) {
					formato = "1331";
				} else if ((idSkill > 4701) && (idSkill < 4704)) {
					formato = "1332";
				} else if (idSkill == 6049) {
					formato = "0094";
				} else {
					formato = String.valueOf(idSkill);
				}

				String scatenaEvento;
				if(azione.equals("aggiungi")) {
					scatenaEvento = "aggiungi_buff_schema";
				} else {
					scatenaEvento = "rimuovi_buff_schema";
				}
				String paginaBuff2;
				if(secondaBuff) {
					paginaBuff2 = "1+";
				} else {
					paginaBuff2 = String.valueOf(intPaginaBuff);
				}
				if( (i % 2) != 0) {
					HTML += "<table bgcolor=333333 border=0>";
				} else {
					HTML += "<table bgcolor=292929 border=0>";
				}

				HTML += "<tr><td width=32><button value=\"\" action=\"bypass -h ZeuSNPC " + scatenaEvento + " " + String.valueOf(idSchema);
				HTML += "_" + String.valueOf(idSkill) + "_" + String.valueOf(livelloSkill) + " " + paginaBuff2 + " " + String.valueOf(BUFF_TOTALI) + " " + playerAbilitato;
				HTML += "\" width=32 height=32 back=\"Icon.skill" + formato + "\" fore=\"Icon.skill" + formato + "\"></td><td width=248><font ";
				HTML += "color=\"00ff00\">" + nome + "</font><br1>" + descrizione + "</td></tr></table>";
				i++;
			}
		}
		HTML += "</td></tr></table><table width=285 border=0><tr><td align=\"center\" width=95>View list of:</td><td align=\"center\" width=95>";
		HTML += "<combobox var=\"classeBuff2\" width=90 " + listaClassiBuff + "></td><td align=\"center\" width=95><button value=\"View List\" ";
		HTML += "action=\"bypass -h ZeuSNPC edita_schemi " + String.valueOf(idSchema) + " $classeBuff2 " + azione + " " + playerAbilitato;
		HTML += "\" width=80 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><br1><button value=\"Back\" ";
		HTML += "action=\"bypass -h ZeuSNPC selezione_schemi " + String.valueOf(idSchema) + " " + String.valueOf(playerAbilitato) + " 0 0\" width=100 ";
		HTML += "height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br1><button value=\"Home\" action=\"bypass -h ZeuSNPC ";
		HTML += " indirizza principale " + String.valueOf(playerAbilitato) + " 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"";
		HTML += "L2UI_ct1.button_df\"><br><br><br><br><br><br><br><br><br><br><br><font color=\"333333\">" + general.TITULO_NPC() + "</font>";
		HTML += "</center></body></html>";
		return HTML;
	}



	public static String mostraTesto(L2PcInstance st, String tipo, String testo, boolean abilitaPulsante, String nomePulsante, String locazione, String playerAbilitato){
		if(!general._activated()){
			return "";
		}
		String HTML = "";
		if (tipo.equals("INFO:")) {
			HTML = "<font color=\"LEVEL\">";
		} else if(tipo.equals("SORRY!!")) {
			HTML = "<font color=\"ff0000\">";
		} else {
			HTML = "<font color=\"00ff00\">";
		}

		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(HTML) + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(testo,"WHITE");
		if (abilitaPulsante){
			HTML = "<center><button value=\"" + nomePulsante + "\" action=\"bypass -h ZeuSNPC indirizza " + locazione + " " + String.valueOf(playerAbilitato) + " 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat(testo,"WHITE");
		}
		MAIN_HTML += central.BotonGOBACKZEUS() + "</body></html>";
		return MAIN_HTML;
	}



	public static String ricaricaPannello(String playerAbilitato){
		if(!general._activated()){
			return "";
		}
		String HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br><img src";
		HTML += "=\"L2UI.SquareGray\" width=250 height=1><br><table width=260 border=0 bgcolor=330000><tr><td><br></td></tr><tr><td align=\"";
		HTML += "center\"><font color=\"FFFFFF\">This window can be seen by GMs only and it<br1>allow to update any changes made in the<br1>";
		HTML += "script without the needing of reload all the<br1>scripts (GM command: //reload quests) or<br1> the entire server. When you ";
		HTML += "have finished<br1>to set up the script, you can disable this<br1>option in the 'Configuration Constants'<br1>section within ";
		HTML += "the Script (RICARICA_SCRIPT).<br1><font color=\"LEVEL\">Do you want to update the SCRIPT?</font></font></td></tr><tr><td></td>";
		HTML += "</tr></table><br><img src=\"L2UI.SquareGray\" width=250 height=1><br><br><button value=\"Yes\" action=\"bypass -h ZeuSNPC ";
		HTML += " ricarica_script 1 " + String.valueOf(playerAbilitato) + " 0 0\" width=50 height=20 back=\"L2UI_ct1.button_df\" fore=\"";
		HTML += "L2UI_ct1.button_df\"><button value=\"No\" action=\"bypass -h ZeuSNPC ricarica_script 0 " + String.valueOf(playerAbilitato);
		HTML += " 0 0\" width=50 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br><br><font color=\"333333\">";
		HTML += general.TITULO_NPC() + "</font></center></body></html>";
		return HTML;
	}


	public static String costruisciSezioneAmministrativa(String playerAbilitato){
		if(!general._activated()){
			return "";
		}
		String HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		HTML = central.LineaDivisora(2) + central.headFormat("Configuracion Buffer Buff") + central.LineaDivisora(2);
		HTML += central.LineaDivisora(2) + central.headFormat("Main Sections Status","LEVEL") + central.LineaDivisora(1);
		String HTML_1 = "<table width=271><tr><td width=135>";
		if (!general.BUFFER_SINGLE_BUFF_CHOICE) {
			HTML_1 += "<font color=ff0000>Buff Section Disabled.</font>";
		} else {
			HTML_1 += "<font color=00ff00>Buff Section Enabled.</font>";
		}
		HTML_1 += "</td><td width=1><img src=\"L2UI.SquareGray\" width=1 height=15></td><td align=\"right\" width=135>";
		if (!general.BUFFER_SCHEME_SYSTEM) {
			HTML_1 += "<font color=ff0000>Schemes Disabled.</font>";
		} else {
			HTML_1 += "<font color=00ff00>Schemes Enabled.</font>";
		}
		HTML_1 += "</td></tr><tr><td width=135>";
		int contatoreTrue = 0;
		int contatoreFalse = 0;
		if (general.BUFFER_IMPROVED_SECTION) {
			contatoreTrue++;
		} else {
			contatoreFalse++;
		}
		if(general.BUFFER_BUFF_SECTION) {
			contatoreTrue++;
		} else {
			contatoreFalse++;
		}
		if (general.BUFFER_RESIST_SECTION) {
			contatoreTrue++;
		} else {
			contatoreFalse++;
		}
		if (general.BUFFER_PROPHECY_SECTION) {
			contatoreTrue++;
		} else {
			contatoreFalse++;
		}
		if (general.BUFFER_CHANT_SECTION) {
			contatoreTrue++;
		} else {
			contatoreFalse++;
		}
		if (general.BUFFER_CUBIC_SECTION) {
			contatoreTrue++;
		} else {
			contatoreFalse++;
		}
		if (general.BUFFER_DANCE_SECTION) {
			contatoreTrue++;
		} else {
			contatoreFalse++;
		}
		if (general.BUFFER_SONG_SECTION) {
			contatoreTrue++;
		} else {
			contatoreFalse++;
		}
		if (general.BUFFER_SPECIAL_SECTION) {
			contatoreTrue++;
		} else {
			contatoreFalse++;
		}
		if (general.BUFFER_OTROS_SECTION) {
			contatoreTrue++;
		} else {
			contatoreFalse++;
		}

		if(!general.BUFFER_SINGLE_BUFF_CHOICE){
			contatoreTrue = 0;
			contatoreFalse = 10;
		}
		if (contatoreFalse == 0) {
			HTML_1 += "<font color=00ff00>Single Buff Enabled</font><br1>";
		} else if (contatoreTrue == 0) {
			HTML_1 += "<font color=ff0000>Single Buff Disabled</font><br1>";
		} else {
			HTML_1 += "<font color=\"LEVEL\">Sin. Buff Part. Enabled</font><br1>";
		}
		if (general.BUFFER_SINGLE_BUFF_CHOICE){
			if (general.BUFFER_AUTOBUFF) {
				HTML_1 += "<font color=00ff00>Autobuff Enabled</font><br1>";
			} else {
				HTML_1 += "<font color=ff0000>Autobuff Disabled</font><br1>";
			}
			if (general.BUFFER_HEAL) {
				HTML_1 += "<font color=00ff00>Heal Enabled</font><br1>";
			} else {
				HTML_1 += "<font color=ff0000>Heal Disabled</font><br1>";
			}
			if (general.BUFFER_REMOVE_BUFF) {
				HTML_1 += "<font color=00ff00>Remove Buff Enabled</font>";
			} else {
				HTML_1 += "<font color=ff0000>Remove Buff Disabled</font>";
			}
		}else{
			HTML_1 += "<font color=ff0000>Autobuff Disabled<br1>Heal Disabled<br1>Remove Buff Disabled</font>";
		}
		HTML_1 +="</td><td width=1></td><td align=\"right\" width=135>Schemes per player: " + String.valueOf(general.BUFFER_SCHEMA_X_PLAYER) + "</td></tr></table>";

		HTML += central.LineaDivisora(2) + central.headFormat(HTML_1) + central.LineaDivisora(2);
		HTML += central.LineaDivisora(1) + central.LineaDivisora(2) + central.headFormat("Enable/Disable Section","Level") + central.LineaDivisora(2);


		HTML_1 ="<table width=270><tr><td><button value=\"Improved\" action=\"bypass -h admin_zeus_config edita_lista_buff improved Improveds 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		HTML_1 +="<td><button value=\"Buffs\" action=\"bypass -h admin_zeus_config edita_lista_buff buff Buffs 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
		HTML_1 +="<tr><td><button value=\"Chants\" action=\"bypass -h admin_zeus_config edita_lista_buff chant Chants 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		HTML_1 +="<td><button value=\"Dances\" action=\"bypass -h admin_zeus_config edita_lista_buff dance Dances 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
		HTML_1 +="<tr><td><button value=\"Songs\" action=\"bypass -h admin_zeus_config edita_lista_buff song Songs 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		HTML_1 +="<td><button value=\"Resist Buffs\" action=\"bypass -h admin_zeus_config edita_lista_buff resist Resists 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
		HTML_1 +="<tr><td><button value=\"Cubics\" action=\"bypass -h admin_zeus_config edita_lista_buff cubic Cubics 1 " + playerAbilitato + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		HTML_1 +="<td><button value=\"Prophecies\" action=\"bypass -h admin_zeus_config edita_lista_buff prophecy Prophecies 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
		HTML_1 +="<tr><td><button value=\"Special Buffs\" action=\"bypass -h admin_zeus_config edita_lista_buff special Specials 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		HTML_1 +="<td><button value=\"Others Buffs\" action=\"bypass -h admin_zeus_config edita_lista_buff others Others 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";

		HTML += central.headFormat(HTML_1) + central.LineaDivisora(2);

		HTML += central.LineaDivisora(2) + central.headFormat("Autobuff class setup Section","LEVEL") + central.LineaDivisora(2);

		HTML += central.headFormat("<button value=\"Buff Sets\" action=\"bypass -h admin_zeus_config edita_lista_buff set Buff_Sets 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");

		HTML += central.LineaDivisora(2) + central.headFormat("Group Managing Section","LEVEL") + central.LineaDivisora(2);

		HTML_1 = "<table width=274><tr><td align=\"center\" width=137><button value=\"Manage Groups\" action=\"bypass -h admin_zeus_config edita_gruppi 1 " + String.valueOf(playerAbilitato) + " 0 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		HTML_1 += "<td align=\"center\" width=137><button value=\"Buff Ordering\" action=\"bypass -h admin_zeus_config ordina_buff " + String.valueOf(playerAbilitato) + " 0 0 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";

		HTML += central.headFormat(HTML_1) +central.LineaDivisora(2);


		HTML += central.headFormat("<button value=\"Back\" action=\"bypass -h admin_zeus_config setConfig1 buffer 0 0 0 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");

		HTML += central.getPieHTML()+"</body></html>";
		return HTML;
	}

	public static String vediListaBuff(String tipo, String nomeLista, int pagina, String playerAbilitato){
		if(pagina==0){
			pagina=1;
		}
		String HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		HTML += central.LineaDivisora(2) + central.headFormat("Buff Management") + central.LineaDivisora(1);
		HTML += central.LineaDivisora(2) + central.headFormat(nomeLista + " Page "+String.valueOf(pagina),"WHITE") + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat("<table><tr><td width=100><button value=\"Back\" action=\"bypass -h ZeuSNPC indirizza gestisci_buff " + String.valueOf(playerAbilitato) + " 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td><td width=15></td><td width=100><button value=\"Home\" action=\"bypass -h ZeuSNPC indirizza principale " + String.valueOf(playerAbilitato) + " 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>") +  central.LineaDivisora(1);
		String stringaContatori;
		String[] splitContatori;
		String HTML2="";
		int BuffM=0,DanceM=0,BuffC=0,DanceC=0,BuffS=0,BuffT = 0,DanceT = 0, totaleBuffM = 0,totaleDanceM = 0,totaleBuffC=0,totaleDanceC=0,totaleBuffS=0;
		if(tipo.equals("set")){
			stringaContatori = costruisciContatore();
			stringaContatori = stringaContatori.replace("_"," ");
			splitContatori = stringaContatori.split(" ");
			BuffM = Integer.valueOf(splitContatori[0]); DanceM = Integer.valueOf(splitContatori[1]); BuffC = Integer.valueOf(splitContatori[2]); DanceC = Integer.valueOf(splitContatori[3]);
			BuffS = Integer.valueOf(splitContatori[4]); BuffT = Integer.valueOf(splitContatori[5]); DanceT = Integer.valueOf(splitContatori[6]);
			totaleBuffM = BuffT + BuffM; totaleDanceM = DanceT + DanceM; totaleBuffC = BuffT + BuffC;
			totaleDanceC = DanceT + DanceC; totaleBuffS = totaleBuffC + BuffS;
			HTML2 = "<table width=270><tr><td width=135>Max buffs: <font color=\"LEVEL\">" + String.valueOf(general.BUFF_PER_SCHEMA) + "</font></td><td align=\"";
			HTML2 += "right\" width=135>Max dances/songs: <font color=\"LEVEL\">" + String.valueOf(general.DANCE_PER_SCHEMA) + "</font></td></tr></table><br><table ";
			HTML2 += "width=270><tr><td>For All:</td><td>Buffs: <font color=";
			if(BuffT >= general.BUFF_PER_SCHEMA) {
				HTML2 += "ff0000";
			} else {
				HTML2 += "\"LEVEL\"";
			}
			HTML2 += ">" + String.valueOf(BuffT) + "</font></td><td align=\"right\">Dances/songs: <font color=";
			if (DanceT >= general.DANCE_PER_SCHEMA) {
				HTML2 += "ff0000";
			} else {
				HTML2 += "\"LEVEL\"";
			}
			HTML2 += ">" + String.valueOf(DanceT) + "</font></td></tr><tr><td><font color=00ff00>For Mage:</font></td><td>Buffs: <font color=";
			if (totaleBuffM >= general.BUFF_PER_SCHEMA) {
				HTML2 += "ff0000";
			} else {
				HTML2 += "\"LEVEL\"";
			}
			HTML2 += ">" + String.valueOf(totaleBuffM) + "</font></td><td align=\"right\">Dances/songs: <font color=";
			if (totaleDanceM >= general.DANCE_PER_SCHEMA) {
				HTML2 += "ff0000";
			} else {
				HTML2 += "\"LEVEL\"";
			}
			HTML2 += ">" + String.valueOf(totaleDanceM) + "</font></td></tr><tr><td><font color=00ffff>For Fighters:</font></td><td>Buffs: <font color=";
			if (totaleBuffC >= general.BUFF_PER_SCHEMA) {
				HTML2 += "ff0000";
			} else {
				HTML2 += "\"LEVEL\"";
			}
			HTML2 += ">" + String.valueOf(totaleBuffC) + "</font></td><td align=\"right\">Dances/songs: <font color=";
			if (totaleDanceC >= general.DANCE_PER_SCHEMA) {
				HTML2 += "ff0000";
			} else {
				HTML2 += "\"LEVEL\"";
			}
			HTML2 += ">" + String.valueOf(totaleDanceC) + "</font></td></tr><tr><td><font color=ff00ff>For Schield:</font></td><td>Buffs: <font color=";
			if (totaleBuffS >= general.BUFF_PER_SCHEMA) {
				HTML2 += "ff0000";
			} else {
				HTML2 += "\"LEVEL\"";
			}
			HTML2 += ">" + String.valueOf(totaleBuffS) + "</font></td><td align=\"right\">Dances/songs: <font color=";
			if (totaleDanceC >= general.DANCE_PER_SCHEMA) {
				HTML2 += "ff0000";
			} else {
				HTML2 += "\"LEVEL\"";
			}
			HTML2 += ">" + String.valueOf(totaleDanceC) + "</font></td></tr></table><br>";
		}
		HTML = central.LineaDivisora(2) + central.headFormat(HTML2) + central.LineaDivisora(2);
		Connection conn = null;
		Vector<String> listaBuff = new Vector<String>();
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			nomeLista = nomeLista.replace("_"," ");
			String qry;
			if(tipo.equals("set")) {
				qry = "SELECT * FROM zeus_buffer_buff_list WHERE buffType IN (" + central.generaQuery() + ") AND canUse=1 ORDER BY buffClass, BuffOrder";
			} else {
				qry = "SELECT * FROM zeus_buffer_buff_list WHERE buffType=\"" + tipo + "\" ORDER BY buffOrder";
			}

			PreparedStatement psqry = conn.prepareStatement(qry);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				String nome ="",classeBuff="",tipoBuff="",ordinamento="",idSkill="",livelloSkill="",descrizione="",perClasse="",attiva="",stringaSkill="";
				nome = SkillData.getInstance().getSkill(rss.getInt("buffId"),rss.getInt("buffLevel")).getName();
				nome = nome.replace(" ","*");
				classeBuff = rss.getString("buffClass");
				tipoBuff = rss.getString("buffType");
				ordinamento = rss.getString("buffOrder");
				idSkill = rss.getString("buffId");
				livelloSkill = rss.getString("buffLevel");
				descrizione = rss.getString("buffDesc");
				descrizione = descrizione.replace(" ","*");
				perClasse = rss.getString("forClass");
				attiva = rss.getString("canUse");
				stringaSkill = String.valueOf(classeBuff) + "_" + tipoBuff + "_" + String.valueOf(ordinamento) + "_" + nome + "_" + descrizione + "_" + String.valueOf(perClasse);
				stringaSkill += "_" + String.valueOf(pagina) + "_" + String.valueOf(attiva) + "_" + String.valueOf(idSkill) + "_" + String.valueOf(livelloSkill);
				listaBuff.add(stringaSkill);
			}
		}catch(SQLException e){
			_log.warning("ERROR BUFFER->" + e.getMessage());
		}
		try{
			conn.close();
		}catch (Exception e) {

		}
		nomeLista = nomeLista.replace(" ","_");
		int pagine = 1, elementiInPagine;
		if(tipo.equals("set")) {
			elementiInPagine = 15; //# WARNING!!! SET HERE A HIGHER VALUE MAY CAUSE GRAFICAL PROBLEMS AND IN SOME CASES CRASH OF THE GAME!
		}
		else {
			elementiInPagine = 22; //# WARNING!!! SET HERE A HIGHER VALUE MAY CAUSE GRAFICAL PROBLEMS AND IN SOME CASES CRASH OF THE GAME!
		}
		int ContadorPaginas = elementiInPagine;

		while(ContadorPaginas < listaBuff.size()){
			pagine++;
			ContadorPaginas = elementiInPagine * pagine;
		}
		String lista ="";
		int i=0,j=0;
		String width="";
		if(pagine > 1){
			lista = "List=\"Page_" + String.valueOf(pagina) + ";";
			i = 1;
			while(i <= pagine){
				if (i != pagina) {
					lista += "Page_" + String.valueOf(i) + ";";
				}
				i++;
			}
			lista += "\"";
			//bypass -h admin_zeus_config
			HTML += "<center><table><tr><td><combobox var=\"pagina\" width=70 " + lista + "></td><td><button value=\"Go\" action=\"bypass -h admin_zeus_config ";
			HTML += " edita_lista_buff " + tipo + " " + nomeLista + " $pagina " + String.valueOf(playerAbilitato) + "\" width=70 height=20 back=\"";
			HTML += "L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table></center>";
		}
		width = "275";
		HTML += "<br><table width=" + width + "><tr><td>";
		if(i<0){
			i=1;
		}
		i = (pagina - 1) * elementiInPagine;
		j = (pagina * elementiInPagine) - 1;
		if (j >= (listaBuff.size() - 1)) {
			j = listaBuff.size() - 1;
		}

		if(i<0){
			i=i*-1;
		}


		String stringaBuff,formato;
		String listaClassi ="";
		while (i <= j){
			if(i<=listaBuff.size()){
				stringaBuff = listaBuff.get(i);
				stringaBuff = stringaBuff.replace("_"," ");
				String[] arrayBuff = stringaBuff.split(" ");
				int classeBuff = Integer.valueOf(arrayBuff[0]);
				String tipoBuff = arrayBuff[1];
				String nome = arrayBuff[3];
				String descrizione = arrayBuff[4];
				int perClasse = Integer.valueOf(arrayBuff[5]);
				pagina = Integer.valueOf(arrayBuff[6]);
				int attiva = Integer.valueOf(arrayBuff[7]);
				int skill = Integer.valueOf(arrayBuff[8]);
				int livello = Integer.valueOf(arrayBuff[9]);
				nome = nome.replace("*"," ");
				descrizione = descrizione.replace("*"," ");

				if(descrizione.length()>80){
					descrizione = descrizione.substring(1, 80) +"...";
				}

				if(skill == 4) {
					formato = "0004";
				} else if((skill > 9) && (skill < 100)) {
					formato = "00" + String.valueOf(skill);
				} else if((skill > 99) && (skill < 1000)) {
					formato = "0" + String.valueOf(skill);
				} else if(skill == 1517) {
					formato = "1536";
				} else if(skill == 1518) {
					formato = "1537";
				} else if(skill == 1547) {
					formato = "0065";
				} else if(skill == 2076) {
					formato = "0195";
				} else if((skill > 4550) && (skill < 4555)) {
					formato = "5739";
				} else if((skill > 4698) && (skill < 4701)) {
					formato = "1331";
				} else if((skill > 4701) && (skill < 4704)) {
					formato = "1332";
				} else if(skill == 6049) {
					formato = "0094";
				} else {
					formato = String.valueOf(skill);
				}

				if (attiva == 0) {
					HTML += "<table bgcolor=330000 width=" + String.valueOf(width) + ">";
				} else{
					if((i % 2) != 0) {
						HTML += "<table bgcolor=333333 width=" + String.valueOf(width) + ">";
					} else {
						HTML += "<table bgcolor=292929 width=" + String.valueOf(width) + ">";
					}
				}
				if(tipo.equals("set")){
					listaClassi = "List=\"";
					boolean abilitaBuffM;
					if (totaleBuffM < general.BUFF_PER_SCHEMA) {
						abilitaBuffM = true;
					} else {
						abilitaBuffM = false;
					}
					boolean abilitaDanceM;
					if (totaleDanceM < general.DANCE_PER_SCHEMA) {
						abilitaDanceM = true;
					} else {
						abilitaDanceM = false;
					}
					boolean abilitaBuffC;
					if (totaleBuffC < general.BUFF_PER_SCHEMA) {
						abilitaBuffC = true;
					} else {
						abilitaBuffC = false;
					}
					boolean abilitaDanceC;
					if (totaleDanceC < general.DANCE_PER_SCHEMA) {
						abilitaDanceC = true;
					} else {
						abilitaDanceC = false;
					}
					boolean abilitaBuffS;
					if (totaleBuffS < general.BUFF_PER_SCHEMA) {
						abilitaBuffS = true;
					} else {
						abilitaBuffS = false;
					}
					boolean abilitaBuffT;
					if (BuffT < general.BUFF_PER_SCHEMA) {
						abilitaBuffT = true;
					} else {
						abilitaBuffT = false;
					};
					boolean abilitaDanceT;
					if (DanceT < general.DANCE_PER_SCHEMA) {
						abilitaDanceT = true;
					} else {
						abilitaDanceT = false;
					};
					boolean skillBuff;
					if ((classeBuff < 6) || (classeBuff > 7)) {
						skillBuff = true;
					} else {
						skillBuff = false;
					}

					if(perClasse == 0){
						listaClassi += "None;";
						if ((skillBuff && abilitaBuffM) || (!skillBuff && abilitaDanceM)) {
							listaClassi += "Mage;";
						}
						if ((skillBuff && abilitaBuffC) || (!skillBuff && abilitaDanceC)) {
							listaClassi += "Fighter;";
						}
						if (skillBuff && abilitaBuffS) {
							listaClassi += "Shield;";
						}
						if ((skillBuff && abilitaBuffT) || (!skillBuff && abilitaDanceT)) {
							listaClassi += "All;";
						}
					}else if(perClasse == 1){
						listaClassi += "Mage;";
						if ((skillBuff && abilitaBuffC) || (!skillBuff && abilitaDanceC)) {
							listaClassi += "Fighter;";
						}
						if (skillBuff && abilitaBuffS) {
							listaClassi += "Shield;";
						}
						if ((skillBuff && abilitaBuffT) || (!skillBuff && abilitaDanceT)) {
							listaClassi += "All;";
						}
						listaClassi += "None;";
					}else if( perClasse == 2){
						listaClassi += "Fighter;";
						if (skillBuff && abilitaBuffS) {
							listaClassi += "Shield;";
						}
						if ((skillBuff && abilitaBuffT) || (!skillBuff && abilitaDanceT)) {
							listaClassi += "All;";
						}
						listaClassi += "None;";
						if ((skillBuff && abilitaBuffM) || (!skillBuff && abilitaDanceM)) {
							listaClassi += "Mage;";
						}
					}else if(perClasse == 3) {
						listaClassi += "Shield;";
						if ((skillBuff && abilitaBuffT) || (!skillBuff && abilitaDanceT)) {
							listaClassi += "All;";
						}
						listaClassi += "None;";
						if ((skillBuff && abilitaBuffM) || (!skillBuff && abilitaDanceM)) {
							listaClassi += "Mage;";
						}
						if ((skillBuff && abilitaBuffC) || (!skillBuff && abilitaDanceC)) {
							listaClassi += "Fighter;";
						}
					}else if( perClasse == 4 ) {
						listaClassi += "All;None;";
						if ((skillBuff && abilitaBuffM) || (!skillBuff && abilitaDanceM)) {
							listaClassi += "Mage;";
						}
						if ((skillBuff && abilitaBuffC) || (!skillBuff && abilitaDanceC)) {
							listaClassi += "Fighter;";
						}
						if (skillBuff && abilitaBuffS) {
							listaClassi += "Shield;";
						}
					}
					listaClassi += "\"";
					HTML += "<tr><td align=\"center\"><table width=218><tr><td width=32><img src=\"Icon.skill" + String.valueOf(formato) + "\" width=32 height=";
					HTML += "32><br></td><td align=\"right\"><font color=00ff00>" + nome + "</font><br1><font color=\"LEVEL\">" + tipoBuff + "</font>";
					HTML += "<br></td></tr></table></td></tr><tr><td align=\"center\" width=" + String.valueOf(width) + "\"><img src=\"L2UI.SquareGray\" width=";
					HTML += "250 height=1><br>" + descrizione + "<img src=\"L2UI.SquareGray\" width=250 height=1></td></tr><tr><td width=";
					HTML += String.valueOf(width) + "><table><tr><td align=\"center\" width=" + String.valueOf(Integer.valueOf(width) / 2) + "><br><combobox var=\"nuovoSet" + String.valueOf(i);
					HTML += "\" width=70 " + listaClassi + "></td><td align=\"center\" width=" + String.valueOf(Integer.valueOf(width) / 2) + "><br><button value=\"Update\"";
					HTML += " action=\"bypass -h admin_zeus_config cambia_set " + String.valueOf(skill) + "_" + String.valueOf(livello) + " $nuovoSet" + String.valueOf(i) + " ";
					HTML += pagina + " " + String.valueOf(playerAbilitato) + "\" width=50 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
					HTML +="</tr></table><br></td></tr>";
				}else{
					HTML += "<tr><td align=\"center\"><table width=218><tr><td width=32><img src=\"Icon.skill" + String.valueOf(formato) + "\" width=32 height=";
					HTML += "32><br></td><td align=\"right\"><font color=00ff00>" + nome + "</font></td></tr></table></td></tr><tr><td align=\"center";
					HTML += "\" width=" + String.valueOf(width) + "\"><img src=\"L2UI.SquareGray\" width=250 height=1><br>" + descrizione + "<img src=\"";
					HTML += "L2UI.SquareGray\" width=250 height=1></td></tr><tr><td align=\"center\" width=" + String.valueOf(width) + "><br>";
					if(attiva == 1){
						HTML += "<button value=\"Disable\" action=\"bypass -h admin_zeus_config edita_buff " + String.valueOf(skill) + "_" + String.valueOf(livello);
						HTML += " 0-" + String.valueOf(pagina) + " " + tipo + " " + String.valueOf(playerAbilitato) + "\" width=80 height=20 back=\"L2UI_ct1.button_df\" fore=\"";
						HTML += "L2UI_ct1.button_df\"></td></tr>";
					}else{
						HTML += "<button value=\"Enable\" action=\"bypass -h admin_zeus_config edita_buff " + String.valueOf(skill) + "_" + String.valueOf(livello);
						HTML += " 1-" + String.valueOf(pagina) + " " + tipo + " " + String.valueOf(playerAbilitato) + "\" width=80 height=20 back=\"L2UI_ct1.button_df\" fore=\"";
						HTML += "L2UI_ct1.button_df\"></td></tr>";
					}
				}
				HTML += "</table>";
				i += 1;
			}//end if
		}//while
		HTML += "</td></tr></table><br>";
		if(pagine > 1){
			HTML += "<table><tr><td><combobox var=\"pagina2\" width=70 " + lista + "></td><td><button value=\"Go\" action=\"bypass -h admin_zeus_config ";
			HTML += " edita_lista_buff " + tipo + " " + nomeLista + " $pagina2 " + String.valueOf(playerAbilitato) + "\" width=70 height=20 back=\"";
			HTML += "L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";
		}
		HTML += "<center><table><tr><td width=100><button value=\"Back\" action=\"bypass -h admin_zeus_config indirizza gestisci_buff true 0 0";
		HTML += playerAbilitato + " 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td><td width=15></td>";
		HTML += "<td width=100><button value=\"Home\" action=\"bypass -h ZeuSNPC indirizza principale " + String.valueOf(playerAbilitato) + " ";
		HTML += "0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table></center><br>";
		if(tipo.equals("set")) {
			HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>";
		}
		HTML += "<font color=\"333333\">" + general.TITULO_NPC() + "</font></center></body></html>";
		return HTML;
	}


	public static String editaGruppi(int pagina, String playerAbilitato){
		String HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		HTML = central.LineaDivisora(2) + central.headFormat("Buff Groups Management - Page " + String.valueOf(pagina)) + central.LineaDivisora(2);
		HTML += central.headFormat("<table><tr><td width=100><button value=\"Back\" action=\"bypass -h admin_zeus_config indirizza gestisci_buff " + String.valueOf(playerAbilitato) + " 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td><td width=15></td><td width=100><button value=\"Home\" action=\"bypass -h ZeuSNPC indirizza principale " + String.valueOf(playerAbilitato) + " 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><br>")+central.LineaDivisora(2);

		Connection conn = null;;
		PreparedStatement psqry = null;
		ResultSet rss = null;
		Vector<String> listaBuff = new Vector<String>();
		String nome,tipoBuff,idSkill,livelloSkill,descrizione,stringaSkill;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			String qry = "SELECT * FROM zeus_buffer_buff_list";
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			while (rss.next()){
				nome = SkillData.getInstance().getSkill(rss.getInt("buffId"),rss.getInt("buffLevel")).getName();
				nome = nome.replace(" ","*");
				String classeBuff = rss.getString("buffClass");
				tipoBuff = rss.getString("buffType");
				idSkill = rss.getString("buffId");
				livelloSkill = rss.getString("buffLevel");
				descrizione = rss.getString("buffDesc");
				descrizione = descrizione.replace(" ","*");
				String attiva = rss.getString("canUse");
				stringaSkill = nome + "_" + classeBuff + "_" + tipoBuff + "_" + descrizione + "_" + attiva + "_" + idSkill + "_" + livelloSkill;
				listaBuff.add(stringaSkill);
			}
		}catch(SQLException a){

		}
		try{
		conn.close();
		}catch(SQLException a){

		}
		Collections.sort(listaBuff);
		int pagine = 1;
		int elementiInPagine = 14; //# WARNING!!! SET HERE A HIGHER VALUE MAY CAUSE GRAFICAL PROBLEMS AND IN SOME CASES CRASH OF THE GAME!
		int ContadorPaginas = elementiInPagine;
		while(ContadorPaginas < listaBuff.size()){
			pagine++;
			ContadorPaginas = elementiInPagine * pagine;
		}
		String lista="";
		if(pagine > 1){
			lista= "List=\"Page_" + String.valueOf(pagina) + ";";
			int i = 1;
			while(i <= pagine){
				if(i != pagina) {
					lista += "Page_" + String.valueOf(i) + ";";
				}
				i++;
			}
			lista += "\"";

			HTML += central.headFormat("<table><tr><td><combobox var=\"pagina\" width=75 " + lista + "></td><td><button value=\"Go\" action=\"bypass -h admin_zeus_config edita_gruppi $pagina " + String.valueOf(playerAbilitato) + " 0 0\" width=75 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><br>");
		}
		String width = "275";
		HTML += "<table width=" + width + "><tr><td>";
		int i, j;
		i = (pagina - 1) * elementiInPagine;
		j = (pagina * elementiInPagine) - 1;
		if (j >= (listaBuff.size() - 1)) {
			j = listaBuff.size() - 1;
		}

		String stringaBuff ="";
		String[] arrayBuff;
		while(i <= j){
			stringaBuff = listaBuff.get(i);
			stringaBuff = stringaBuff.replace("_"," ");
			arrayBuff = stringaBuff.split(" ");
			nome = arrayBuff[0];
			int classeBuff = Integer.valueOf(arrayBuff[1]);
			tipoBuff = arrayBuff[2];
			descrizione = arrayBuff[3];
			int attiva = Integer.valueOf(arrayBuff[4]);
			int skill = Integer.valueOf(arrayBuff[5]);
			String livello = arrayBuff[6];
			nome = nome.replace("*"," ");
			descrizione = descrizione.replace("*"," ");
			String formato="";
			if(skill == 4) {
				formato = "0004";
			} else if((skill > 9) && (skill < 100)) {
				formato = "00" + String.valueOf(skill);
			} else if((skill > 99) && (skill < 1000)) {
				formato = "0" + String.valueOf(skill);
			} else if(skill == 1517) {
				formato = "1536";
			} else if(skill == 1518) {
				formato = "1537";
			} else if(skill == 1547) {
				formato = "0065";
			} else if(skill == 2076) {
				formato = "0195";
			} else if((skill > 4550) && (skill < 4555)) {
				formato = "5739";
			} else if((skill > 4698) && (skill < 4701)) {
				formato = "1331";
			} else if((skill > 4701) && (skill < 4704)) {
				formato = "1332";
			} else if(skill == 6049) {
				formato = "0094";
			} else {
				formato = String.valueOf(skill);
			}

			if (attiva == 0) {
				HTML += "<table bgcolor=330000 width=" + String.valueOf(width) + ">";
			} else if((i % 2) != 0) {
				HTML += "<table bgcolor=333333 width=" + String.valueOf(width) + ">";
			} else {
				HTML += "<table bgcolor=292929 width=" + String.valueOf(width) + ">";
			}

			String listaClassi="";
			switch (classeBuff){
				case 0:
					listaClassi = "List=\"Improved;Buff;Resist;Prophecy;Chant;Cubic;Dance;Song;Special;Others;\"";
					break;
				case 1:
					listaClassi = "List=\"Buff;Resist;Prophecy;Chant;Cubic;Dance;Song;Special;Others;Improved;\"";
					break;
				case 2:
					listaClassi = "List=\"Resist;Prophecy;Chant;Cubic;Dance;Song;Special;Others;Improved;Buff;\"";
					break;
				case 3:
					listaClassi = "List=\"Prophecy;Chant;Cubic;Dance;Song;Special;Others;Improved;Buff;Resist;\"";
					break;
				case 4:
					listaClassi = "List=\"Chant;Cubic;Dance;Song;Special;Others;Improved;Buff;Resist;Prophecy;\"";
					break;
				case 5:
					listaClassi = "List=\"Cubic;Dance;Song;Special;Others;Improved;Buff;Resist;Prophecy;Chant;\"";
					break;
				case 6:
					listaClassi = "List=\"Dance;Song;Special;Others;Improved;Buff;Resist;Prophecy;Chant;Cubic;\"";
					break;
				case 7:
					listaClassi = "List=\"Song;Special;Others;Improved;Buff;Resist;Prophecy;Chant;Cubic;Dance;\"";
					break;
				case 8:
					listaClassi = "List=\"Special;Others;Improved;Buff;Resist;Prophecy;Chant;Cubic;Dance;Song;\"";
					break;
				case 9:
					listaClassi = "List=\"Others;Improved;Buff;Resist;Prophecy;Chant;Cubic;Dance;Song;Special;\"";
					break;
			}


			HTML += "<tr><td align=\"center\"><table width=218><tr><td width=32><img src=\"Icon.skill" + String.valueOf(formato) + "\" width=32 height=32>";
			HTML += "<br></td><td align=\"right\"><font color=\"LEVEL\">" + String.valueOf(tipoBuff) + " (";
			if (attiva == 0) {
				HTML += "<font color=ff0000>Disabled</font>";
			} else {
				HTML += "<font color=00ff00>Enabled</font>";
			}

			if(descrizione.length()>80){
				descrizione = descrizione.substring(0, 77) + "...";
			}

			HTML += ")</font><br1><center><font color=00ff00>" + String.valueOf(nome) + "</font></center><br></td></tr></table></td></tr><tr><td align=\"";
			HTML += "center\" width=" + String.valueOf(width) + "\"><img src=\"L2UI.SquareGray\" width=250 height=1><br>" + descrizione + "<img src=\"";
			HTML += "L2UI.SquareGray\" width=250 height=1></td></tr><tr><td width=" + String.valueOf(width) + "><table><tr><td align=\"center\" width=";
			HTML += String.valueOf(Integer.valueOf(width) / 2) + "><br><combobox var=\"nuovoGruppo" + String.valueOf(i) + "\" width=80 " + listaClassi + "></td><td align=\"center";
			HTML += "\" width=" + String.valueOf(Integer.valueOf(width) / 2) + "><br><button value=\"Update\" action=\"bypass -h admin_zeus_config cambia_gruppo";
			HTML += " " + String.valueOf(skill) + "_" + String.valueOf(livello) + " $nuovoGruppo" + String.valueOf(i) + " " + String.valueOf(pagina) + " " + String.valueOf(playerAbilitato) + "\" width=80 ";
			HTML += "height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><br><img src=\"L2UI.SquareGray\" width=";
			HTML += "270 height=3></td></tr></table>";
			i += 1;
		}//end while
		HTML += "</td></tr></table><br>";
		if(pagine > 1){
			HTML += "<table><tr><td><combobox var=\"pagina2\" width=75 " + lista + "></td><td><button value=\"Go\" action=\"bypass -h admin_zeus_config ";
			HTML += " edita_gruppi $pagina2 " + String.valueOf(playerAbilitato) + " 0 0\" width=75 height=20 back=\"L2UI_ct1.button_df\" fore=\"";
			HTML += "L2UI_ct1.button_df\"></td></tr></table>";
		}
		HTML += "<table><tr><td width=100><button value=\"Back\" action=\"bypass -h ZeuSNPC indirizza gestisci_buff ";
		HTML += playerAbilitato + " 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td><td width=15></td>";
		HTML += "<td width=100><button value=\"Home\" action=\"bypass -h ZeuSNPC indirizza principale " + String.valueOf(playerAbilitato) + " ";
		HTML += "0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><br><br><br><br><br><br>";
		HTML += "<font color=\"333333\">" + general.TITULO_NPC() + "</font>";
		HTML += "</center></body></html>";
		return HTML;
	}




	public static String ordinaBuff(String playerAbilitato){
		String MAIN_HTML ="";

		MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Buff Ordering","LEVEL") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Choose the group to order","LEVEL") + central.LineaDivisora(3);

		String HTML = "<table width=270><tr><td><button value=\"Improved\" action=\"bypass -h admin_zeus_config ordina_lista_buff improved Improveds 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		HTML+="<td><button value=\"Buffs\" action=\"bypass -h admin_zeus_config ordina_lista_buff buff Buffs 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
		HTML+="<tr><td><button value=\"Chants\" action=\"bypass -h admin_zeus_config ordina_lista_buff chant Chants 1 " + String.valueOf(playerAbilitato)+ "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		HTML+="<td><button value=\"Dances\" action=\"bypass -h admin_zeus_config ordina_lista_buff dance Dances 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
		HTML+="<tr><td><button value=\"Songs\" action=\"bypass -h admin_zeus_config ordina_lista_buff song Songs 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		HTML+="<td><button value=\"Resist Buffs\" action=\"bypass -h admin_zeus_config ordina_lista_buff resist Resists 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
		HTML+="<tr><td><button value=\"Cubics\" action=\"bypass -h admin_zeus_config ordina_lista_buff cubic Cubics 1 " + playerAbilitato + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		HTML+="<td><button value=\"Prophecies\" action=\"bypass -h admin_zeus_config ordina_lista_buff prophecy Prophecies 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
		HTML+="<tr><td><button value=\"Special Buffs\" action=\"bypass -h admin_zeus_config ordina_lista_buff special Specials 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		HTML+="<td><button value=\"Others Buffs\" action=\"bypass -h admin_zeus_config ordina_lista_buff others Others 1 " + String.valueOf(playerAbilitato) + "\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";

		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(HTML) + central.LineaDivisora(1);

		HTML+="<button value=\"Back\" action=\"bypass -h admin_zeus_config indirizza gestisci_buff " + String.valueOf(playerAbilitato) + " 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br1><button value=\"Home\" action=\"bypass -h ZeuSNPC indirizza principale " + String.valueOf(playerAbilitato) + " 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		//MAIN_HTML += central.LineaDivisora(2) + central.headFormat(HTML) + central.LineaDivisora(1);

		MAIN_HTML += "</body></html>";
		return MAIN_HTML;
	}

	public static String ordinaListaBuff(String gruppo, String nomeGruppo,int pagina,String playerAbilitato){
		String HTML = "<html><title>"+general.TITULO_NPC()+"</titler><body><center>";
		HTML += central.LineaDivisora(2) + central.headFormat("Buff Ordering "+String.valueOf(nomeGruppo)+" Page " + String.valueOf(pagina)) + central.LineaDivisora(2);
		HTML += central.LineaDivisora(1) + "<button value=\"Back\" action=\"bypass -h admin_zeus_config ordina_buff " + String.valueOf(playerAbilitato) + " 0 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br1><button value=\"Home\" action=\"bypass -h ZeuSNPC indirizza principale " + String.valueOf(playerAbilitato) + " 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br>" + central.LineaDivisora(1);
		ordinaGruppo(gruppo);
		Connection conn = null;
		PreparedStatement psqry = null;
		ResultSet rss = null;
		Vector<String> listaBuff = new Vector<String>();
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			String qry = "SELECT * FROM zeus_buffer_buff_list WHERE buffType=\"" + String.valueOf(gruppo) + "\" AND canUse=1 ORDER BY buffOrder";
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			while(rss.next()){
				try{
					String ordine = rss.getString("buffOrder");
					String idSkill = rss.getString("buffId");
					String descrizione = rss.getString("buffDesc");
					int livelloSkill = rss.getInt("buffLevel");
					String nomeSkill = SkillData.getInstance().getSkill(Integer.valueOf(idSkill),livelloSkill).getName();
					nomeSkill = nomeSkill.replace(" ","*");
					descrizione = descrizione.replace(" ","*");
					String stringaBuff = String.valueOf(ordine) + "_" + String.valueOf(nomeSkill) + "_" + String.valueOf(descrizione) + "_" + String.valueOf(idSkill) + "_" + String.valueOf(livelloSkill);
					listaBuff.add(stringaBuff);
				}catch(SQLException b){

				}
			}
		}catch(SQLException a){

		}
		try{
			conn.close();
		}catch(SQLException e){

		}
		int elementiInPagine = 13; //
		int pagine = 1;
		int ContadorPaginas = elementiInPagine;
		while(ContadorPaginas < listaBuff.size()){
			pagine++;
			ContadorPaginas = elementiInPagine * pagine;
		}
		String lista ="";
		int i,j;
		if(pagine > 1){
			lista = "List=\"Page_" + String.valueOf(pagina) + ";";
			i = 1;
			while(i <= pagine){
				if(i != pagina) {
					lista += "Page_" + String.valueOf(i) + ";";
				}
				i++;
			}
			lista += "\"";
			HTML += "<table><tr><td><combobox var=\"pagina\" width=75 " + lista + "></td><td><button value=\"Go\" action=\"bypass -h admin_zeus_config ";
			HTML += "ordina_lista_buff " + String.valueOf(gruppo) + " " + String.valueOf(nomeGruppo) + " $pagina " + String.valueOf(playerAbilitato) + "\" width=75 ";
			HTML += "height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><br>";
		}
		String width = "275";
		HTML += "<table width=" + width + "><tr><td>";
		i = (pagina - 1) * elementiInPagine;
		j = (pagina * elementiInPagine) - 1;
		if (j >= (listaBuff.size() - 1)) {
			j = listaBuff.size() - 1;
		}
		String stringaBuff;
		while(i <= j){
			stringaBuff = listaBuff.get(i);
			stringaBuff = stringaBuff.replace("_"," ");
			String[] arrayBuff = stringaBuff.split(" ");
			String ordine = arrayBuff[0];
			String nome = arrayBuff[1];
			String descrizione = arrayBuff[2];
			int idSkill = Integer.valueOf(arrayBuff[3]);
			String livelloSkill = arrayBuff[4];
			nome = nome.replace("*"," ");
			descrizione = descrizione.replace("*"," ");
			String formato;
			if(idSkill == 4) {
				formato = "0004";
			} else if((idSkill > 9) && (idSkill < 100)) {
				formato = "00" + String.valueOf(idSkill);
			} else if((idSkill > 99) && (idSkill < 1000)) {
				formato = "0" + String.valueOf(idSkill);
			} else if(idSkill == 1517) {
				formato = "1536";
			} else if(idSkill == 1518) {
				formato = "1537";
			} else if(idSkill == 1547) {
				formato = "0065";
			} else if(idSkill == 2076) {
				formato = "0195";
			} else if((idSkill > 4550) && (idSkill < 4555)) {
				formato = "5739";
			} else if((idSkill > 4698) && (idSkill < 4701)) {
				formato = "1331";
			} else if((idSkill > 4701) && (idSkill < 4704)) {
				formato = "1332";
			} else if(idSkill == 6049) {
				formato = "0094";
			} else {
				formato = String.valueOf(idSkill);
			}

			if((i % 2) != 0) {
				HTML += "<table bgcolor=333333 width=" + String.valueOf(width) + ">";
			} else {
				HTML += "<table bgcolor=292929 width=" + String.valueOf(width) + ">";
			}
			HTML += "<tr><td align=\"center\"><table width=218><tr><td width=32><img src=\"Icon.skill" + String.valueOf(formato) + "\" width=32 height=32>";
			HTML += "<br></td><td align=\"right\"><font color=00ff00>" + String.valueOf(nome) + "</font><br></td></tr></table></td></tr><tr><td align=\"";
			HTML += "center\" width=" + String.valueOf(width) + "><img src=\"L2UI.SquareGray\" width=250 height=1><br>" + descrizione + "<img src=\"";
			HTML += "L2UI.SquareGray\" width=250 height=1></td></tr><tr><td align=\"center\" width=" + String.valueOf(width) + "><br><table><tr><td align=\"";
			HTML += "center\" width=" + String.valueOf(Integer.valueOf(width) / 2) + ">Actual Position: <font color=\"LEVEL\">" + String.valueOf(ordine) + "</font></td><td align=";
			HTML += "\"center\" width=" + String.valueOf(Integer.valueOf(width) / 2) + "><table width=90><tr><td align=\"center\" width=30><button value=\"\" action=\"";
			HTML += "bypass -h admin_zeus_config sposta_su " + String.valueOf(idSkill) + "_" + String.valueOf(livelloSkill) + " " + String.valueOf(gruppo) + " ";
			HTML += String.valueOf(pagina) + " " + String.valueOf(playerAbilitato) + "\" width=16 height=16 back=\"L2UI_CH3.ScrollBarUpOnBtn\" fore=\"";
			HTML += "L2UI_CH3.ScrollBarUpOnBtn\"></td><td align=\"center\" width=30>-</td><td align=\"center\" width=30><button value=\"\" ";
			HTML += "action=\"bypass -h admin_zeus_config sposta_giu " + String.valueOf(idSkill) + "_" + String.valueOf(livelloSkill) + " " + String.valueOf(gruppo) + " ";
			HTML += String.valueOf(pagina) + " " + String.valueOf(playerAbilitato) + "\" width=16 height=16 back=\"L2UI_CH3.ScrollBarDownOnBtn\" fore=\"";
			HTML += "L2UI_CH3.ScrollBarDownOnBtn\"></td></tr></table></td></tr></table><br><img src=\"L2UI.SquareGray\" width=270 height=3></td>";
			HTML += "</tr></table>";
			i += 1;
		}//end while
		HTML += "</td></tr></table><br><br>";
		if (pagine > 1){
			HTML += "<table><tr><td><combobox var=\"pagina2\" width=75 " + lista + "></td><td><button value=\"Go\" action=\"bypass -h ZeuSNPC ";
			HTML += " ordina_lista_buff " + String.valueOf(gruppo) + " " + String.valueOf(nomeGruppo) + " $pagina2 " + String.valueOf(playerAbilitato) + "\" width=75 ";
			HTML += "height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><br>";
		}
		HTML += "<button value=\"Back\" action=\"bypass -h ZeuSNPC ordina_buff " + String.valueOf(playerAbilitato) + " 0 0 0\" width";
		HTML += "=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br1><button value=\"Home\" action=\"bypass -h ZeuSNPC ";
		HTML += " indirizza principale " + String.valueOf(playerAbilitato) + " 0 0\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"";
		HTML += "L2UI_ct1.button_df\"><br><br><br><br><font color=\"333333\">" + general.TITULO_NPC() + "</font></center></body></html>";
		return HTML;
	}






	public static String assegnaBuff(L2PcInstance st, int buffaPet, String idSkill, String livelloSkill, String tipoBuff, int pagina, String playerAbilitato){
		int prezzo =0;
		if(tipoBuff.equals("improved")) {
			prezzo = general.BUFFER_IMPROVED_VALOR;
		} else if(tipoBuff.equals("buff")) {
			prezzo = general.BUFFER_BUFF_VALOR;
		} else if(tipoBuff.equals("chant")) {
			prezzo = general.BUFFER_CHANT_VALOR;
		} else if(tipoBuff.equals("dance")) {
			prezzo = general.BUFFER_DANCE_VALOR;
		} else if(tipoBuff.equals("song")) {
			prezzo = general.BUFFER_SONG_VALOR;
		} else if(tipoBuff.equals("resist")) {
			prezzo = general.BUFFER_RESIST_VALOR;
		} else if(tipoBuff.equals("cubic")) {
			prezzo = general.BUFFER_CUBIC_VALOR;
		} else if(tipoBuff.equals("prophecy")) {
			prezzo = general.BUFFER_PROHECY_VALOR;
		} else if(tipoBuff.equals("special")) {
			prezzo = general.BUFFER_SPECIAL_VALOR;
		} else if(tipoBuff.equals("others")) {
			prezzo = general.BUFFER_OTROS_VALOR;
		}

		String CORPO="";
		if (Integer.valueOf((int) (System.currentTimeMillis()/1000)) > general.charBufferTime.get(st)){
			if (!general.BUFF_GRATIS){
				if(!opera.haveItem(st, general.BUFFER_ID_ITEM, prezzo)){
					CORPO = "You don't have enough items:<br>You need: <font color =\"LEVEL\">" + String.valueOf(prezzo) + " ";
					CORPO += String.valueOf(central.getNombreITEMbyID(general.BUFFER_ID_ITEM)) + "!";
					return mostraTesto(st, "SORRY!!", CORPO, true, "Return", "principale", playerAbilitato);
				}
			}
			Skill skill = SkillData.getInstance().getSkill(Integer.valueOf(idSkill),Integer.valueOf(livelloSkill));
			if (String.valueOf(skill.getAbnormalType()).equals("SUMMON")){
				if(!opera.haveItem(st, skill.getItemConsumeId(), skill.getItemConsumeId())){
					CORPO = "You don't have enough items:<br>You need: <font color =\"LEVEL\">" + String.valueOf(skill.getItemConsumeId()) + " ";
					CORPO += String.valueOf(central.getNombreITEMbyID(skill.getItemConsumeId())) + "!";
					return mostraTesto(st, "SORRY!!", CORPO, true, "Return", "principale", playerAbilitato);
				}
			}
			if(buffaPet == 0){
				if(tipoBuff.equals("cubic")) {
					st.useMagic(SkillData.getInstance().getSkill(Integer.valueOf(idSkill),Integer.valueOf(livelloSkill)),false,false);
				} else {
					SkillData.getInstance().getSkill(Integer.valueOf(idSkill),Integer.valueOf(livelloSkill)).applyEffects(st,st);
				}
			}else{
				if(tipoBuff.equals("cubic")) {
					st.useMagic(SkillData.getInstance().getSkill(Integer.valueOf(idSkill),Integer.valueOf(livelloSkill)),false,false);
				} else
					if(st.getSummon() != null) {
						SkillData.getInstance().getSkill(Integer.valueOf(idSkill),Integer.valueOf(livelloSkill)).applyEffects(st.getSummon(),st.getSummon());
					} else{
						CORPO = "You can't use the Pet's options.<br>Summon your pet first!";
						return mostraTesto(st, "INFO:", CORPO, true, "Return", "principale", playerAbilitato);
					}
			}
			if(!general.BUFF_GRATIS) {
				opera.removeItem(general.BUFFER_ID_ITEM,prezzo,st);
			}
			if(general.TIME_OUT) {
				barraTempo(st, 3, general.BUFFER_TIME_WAIT / 10, 600);
			}
			return costruisciHtml(tipoBuff, Integer.valueOf(pagina), playerAbilitato);
		}else{
			return costruisciHtml(tipoBuff, Integer.valueOf(pagina), playerAbilitato);
		}
	}

	public static String assegnaPacchettoBuff(L2PcInstance st, int buffaPet, String playerAbilitato){
		if(Integer.valueOf((int) (System.currentTimeMillis()/1000)) > general.charBufferTime.get(st)){
			Vector<String> setBuff = new Vector<String>();
			int i = 0;
			int prezzo = 0;
			int classePlayer;
			if(st.isMageClass()) {
				classePlayer = 1;
			} else {
				classePlayer = 2;
			}

			Connection conn = null;
			PreparedStatement psqry = null;
			ResultSet rss = null;
			String qry = "",CORPO="";
			String stringaSkill = "";
			if(buffaPet == 0){
				qry = "SELECT buffType, buffId, buffLevel FROM zeus_buffer_buff_list WHERE forClass IN (" + String.valueOf(classePlayer) + ", ";
				int manoSinistra = st.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LHAND);
				if(manoSinistra != 0){
					String tipoOggetto = central.getTipoITEMbyID(manoSinistra);
					if(String.valueOf(tipoOggetto).equals("Shield")) {
						qry += "3, ";
					}
				}
				qry += "4) AND canUse=1 ORDER BY buffClass, buffOrder";
			}else{
				if(st.getSummon() != null){
					qry = "SELECT buffType, buffId, buffLevel FROM zeus_buffer_buff_list WHERE forClass IN (2, 4) AND canUse=1 ORDER BY buffClass, ";
					qry += "buffOrder";
				}else{
					CORPO = "You can't use the Pet's options.<br>Summon your pet first!";
					return mostraTesto(st, "INFO:", CORPO, true, "Return", "principale", playerAbilitato);
				}
			}
			try{
				conn = ConnectionFactory.getInstance().getConnection();
				psqry = conn.prepareStatement(qry);
				rss = psqry.executeQuery();
				boolean assegnaBuff = false;
				int skill = 0;
				int livello = 0;
				while (rss.next()){
					try{
						String tipo = rss.getString("buffType");
						skill = rss.getInt("buffId");
						livello = rss.getInt("buffLevel");
						if(tipo.equals("improved") && general.BUFFER_IMPROVED_SECTION) {
							assegnaBuff = true;
							prezzo += general.BUFFER_IMPROVED_VALOR;
						}

						if(tipo.equals("buff") && general.BUFFER_BUFF_SECTION) {
							assegnaBuff = true;
							prezzo += general.BUFFER_BUFF_VALOR;
						}

						if(tipo.equals("chant") && general.BUFFER_CHANT_SECTION) {
							assegnaBuff = true;
							prezzo += general.BUFFER_CHANT_VALOR;
						}

						if(tipo.equals("dance") && general.BUFFER_DANCE_SECTION) {
							assegnaBuff = true;
							prezzo += general.BUFFER_DANCE_VALOR;
						}

						if(tipo.equals("song") && general.BUFFER_SONG_SECTION) {
							assegnaBuff = true;
							prezzo += general.BUFFER_SONG_VALOR;
						}

						if(tipo.equals("resist") && general.BUFFER_RESIST_SECTION) {
							assegnaBuff = true;
							prezzo += general.BUFFER_RESIST_VALOR;
						}

						if(tipo.equals("prophecy") && general.BUFFER_PROPHECY_SECTION) {
							assegnaBuff = true;
							prezzo += general.BUFFER_PROHECY_VALOR;
						}

						if(tipo.equals("special") && general.BUFFER_SPECIAL_SECTION) {
							assegnaBuff = true;
							prezzo += general.BUFFER_SPECIAL_VALOR;
						}

						if(tipo.equals("others") && general.BUFFER_OTROS_SECTION) {
							assegnaBuff = true;
							prezzo += general.BUFFER_OTROS_VALOR;
						}

						if(assegnaBuff){
							stringaSkill = String.valueOf(skill) + "_" + String.valueOf(livello);
							setBuff.add(stringaSkill);
						}
						assegnaBuff = false;
					}catch(SQLException e){
						_log.warning("Buffer->" + e.getMessage());
					}
				}
			}catch(SQLException a){
				_log.warning("Buffer->" + a.getMessage());
			}
			try{
				conn.close();
			}catch(SQLException b){

			}
			prezzo += general.BUFFER_HEAL_VALOR;
			if (!general.BUFF_GRATIS && !opera.haveItem(st, general.BUFFER_ID_ITEM,prezzo)){
				CORPO = "You don't have enough items:<br>You need: <font color=\"LEVEL\">" + String.valueOf(prezzo) + " ";
				CORPO += central.getNombreITEMbyID(general.BUFFER_ID_ITEM) + "!";
				return mostraTesto(st, "SORRY!!", CORPO, true, "Return", "principale", playerAbilitato);
			}
			//while(i <= setBuff.size() - 1){
			String[] skillSplit;int skill=0;int level=0;
			for(String stringaSkill2 : setBuff){
				//String stringaSkill = setBuff.get(i);
				stringaSkill = stringaSkill2.replace("_"," ");
				skillSplit = stringaSkill.split(" ");
				skill = Integer.parseInt(skillSplit[0]);
				level = Integer.parseInt(skillSplit[1]);
				if(buffaPet == 0) {
					SkillData.getInstance().getSkill(skill,level).applyEffects(st,st);
				} else {
					SkillData.getInstance().getSkill(skill,level).applyEffects(st.getSummon(),st.getSummon());
				}
			}
			boolean ispet = buffaPet==0?false:true;
			central.healAll(st, ispet);
			if(!general.BUFF_GRATIS) {
				opera.removeItem(general.BUFFER_ID_ITEM,prezzo,st);
			}
			if(general.TIME_OUT) {
				barraTempo(st, 3, general.BUFFER_TIME_WAIT, 600);
			}
			return rebuildMainHtml(st, playerAbilitato);
		}else{
			String CORPO = "You have to wait a while if you wish to use my services!";
			return mostraTesto(st, "SORRY!!", CORPO, true, "Retry", "principale", playerAbilitato);
		}
	}

	public static String funzionePieno (L2PcInstance st, int buffaPet, String playerAbilitato){
		String CORPO ="";
		if(Integer.valueOf((int) (System.currentTimeMillis()/1000)) > general.charBufferTime.get(st)){
			if(!opera.haveItem(st, general.BUFFER_ID_ITEM, general.BUFFER_HEAL_VALOR)){
				CORPO = "You don't have enough items:<br>You need: <font color =\"LEVEL\">" + String.valueOf(general.BUFFER_HEAL_VALOR) + " ";
				CORPO += central.getNombreITEMbyID(general.BUFFER_ID_ITEM) + "!";
				return mostraTesto(st, "SORRY!!", CORPO, true, "Return", "principale", playerAbilitato);
			}else{
				if(buffaPet == 1){
					if(st.getSummon() != null){
						central.healAll(st, true);
					}else{
						CORPO = "You can't use the Pet's options.<br>Summon your pet first!";
						return mostraTesto(st, "INFO:" , CORPO, true, "Return", "principale", playerAbilitato);
					}
				}else{
					central.healAll(st, false);
				}
				opera.removeItem(general.BUFFER_ID_ITEM, general.BUFFER_HEAL_VALOR,st);
				if(general.TIME_OUT) {
					barraTempo(st, 1, general.BUFFER_TIME_WAIT / 2, 600);
				}
				return rebuildMainHtml(st, playerAbilitato);
			}
		}else{
			CORPO = "You have to wait a while if you wish to use my services!";
			return mostraTesto(st, "SORRY!!", CORPO, true, "Retry", "principale", playerAbilitato);
		}
	}

	public static String rimuoviBuff(L2PcInstance st, int buffaPet, String playerAbilitato){
		String CORPO ="";
		if(Integer.valueOf((int) (System.currentTimeMillis()/1000)) > general.charBufferTime.get(st)){
			if(!opera.haveItem(st, general.BUFFER_ID_ITEM, general.BUFFER_REMOVE_BUFF_VALOR)){
				CORPO = "You don't have enough items:<br>You need: <font color =\"LEVEL\">" + String.valueOf(general.BUFFER_REMOVE_BUFF_VALOR) + " ";
				CORPO += central.getNombreITEMbyID(general.BUFFER_ID_ITEM) + "!";
				return mostraTesto(st, "SORRY!!", CORPO, true, "Return", "principale", playerAbilitato);
			}else{
				if(buffaPet == 1){
					if(st.getSummon() != null) {
						st.getSummon().stopAllEffects();
					} else{
						CORPO = "You can't use the Pet's options.<br>Summon your pet first!";
						return mostraTesto(st, "INFO:", CORPO, true, "Return", "principale", playerAbilitato);
					}
				}else{
					st.stopAllEffects();
					if(st.getCubics() != null){
						for (L2CubicInstance  cubic : st.getCubics().values()){
							cubic.stopAction();
							//st.del(cubic.getId());
						}
					}
				}
				opera.removeItem(general.BUFFER_ID_ITEM, general.BUFFER_REMOVE_BUFF_VALOR, st);
				if(general.TIME_OUT) {
					barraTempo(st, 2, general.BUFFER_TIME_WAIT / 2, 600);
				}
				return rebuildMainHtml(st, playerAbilitato);
			}
		}else{
			CORPO = "You have to wait a while if you wish to use my services!";
			return mostraTesto(st, "SORRY!!", CORPO, true, "Retry", "principale", playerAbilitato);
		}
	}

	public static String assegnaSchema(L2PcInstance st, int buffaPet, int idSchema, String playerAbilitato){
		int prezzo = 0;
		if(Integer.valueOf((int) (System.currentTimeMillis()/1000)) > general.charBufferTime.get(st)){
			Vector<String> buff = new Vector<String>();
			int id = 0;
			int livello = 0;
			String classeBuff="";
			Connection conn = null;
			PreparedStatement psqry = null;
			ResultSet rss = null;

			try{
				conn = ConnectionFactory.getInstance().getConnection();
				String qry = "SELECT * FROM zeus_buffer_scheme_contents WHERE schemeId=" + String.valueOf(idSchema) + " ORDER BY id";
				psqry = conn.prepareStatement(qry);
				rss = psqry.executeQuery();
				while (rss.next()){
					try{
						id = rss.getInt("buffId");
						livello = rss.getInt("buffLevel");
						classeBuff = rss.getString("buffClass");
						if(classeBuff.equals("0")){
							if(general.BUFFER_IMPROVED_SECTION){
								if(skillAbilitata(id,livello)){
									buff.add(String.valueOf(id) + "_" + String.valueOf(livello));
									prezzo += general.BUFFER_IMPROVED_VALOR;
								}
							}
						}
						if(classeBuff.equals("1")){
							if(general.BUFFER_BUFF_SECTION){
								if(skillAbilitata(id,livello)){
									buff.add(String.valueOf(id) + "_" + String.valueOf(livello));
									prezzo += general.BUFFER_BUFF_VALOR;
								}
							}
						}
						if(classeBuff.equals("4")){
							if(general.BUFFER_CHANT_SECTION){
								if(skillAbilitata(id,livello)){
									buff.add(String.valueOf(id) + "_" + String.valueOf(livello));
									prezzo += general.BUFFER_CHANT_VALOR;
								}
							}
						}
						if(classeBuff.equals("6")){
							if (general.BUFFER_DANCE_SECTION){
								if(skillAbilitata(id,livello)){
									buff.add(String.valueOf(id) + "_" + String.valueOf(livello));
									prezzo += general.BUFFER_DANCE_VALOR;
								}
							}
						}
						if (classeBuff.equals("7")){
							if(general.BUFFER_SONG_SECTION){
								if(skillAbilitata(id,livello)){
									buff.add(String.valueOf(id) + "_" + String.valueOf(livello));
									prezzo += general.BUFFER_SONG_VALOR;
								}
							}
						}
						if(classeBuff.equals("2")){
							if(general.BUFFER_RESIST_SECTION){
								if(skillAbilitata(id,livello)){
									buff.add(String.valueOf(id) + "_" + String.valueOf(livello));
									prezzo += general.BUFFER_RESIST_VALOR;
								}
							}
						}
						if(classeBuff.equals("3")){
							if(general.BUFFER_PROPHECY_SECTION){
								if(skillAbilitata(id,livello)){
									buff.add(String.valueOf(id) + "_" + String.valueOf(livello));
									prezzo += general.BUFFER_PROHECY_VALOR;
								}
							}
						}
						if(classeBuff.equals("8")){
							if(general.BUFFER_SPECIAL_SECTION){
								if(skillAbilitata(id,livello)){
									buff.add(String.valueOf(id) + "_" + String.valueOf(livello));
									prezzo += general.BUFFER_SPECIAL_VALOR;
								}
							}
						}
						if(classeBuff.equals("9")){
							if(general.BUFFER_OTROS_SECTION){
								if(skillAbilitata(id,livello)){
									buff.add(String.valueOf(id) + "_" + String.valueOf(livello));
									prezzo += general.BUFFER_OTROS_VALOR;
								}
							}
						}
					}catch(SQLException e){

					}
				}
			}catch(SQLException b){

			}
			try{
			conn.close();
			}catch(SQLException e){

			}
			String CORPO = "";
			if(buff.size() == 0){
				return visualizzaBuffSchema(idSchema, "0", "aggiungi", playerAbilitato);
			}else{
				if (general.BUFF_GRATIS){
					if(!opera.haveItem(st, general.BUFFER_ID_ITEM, prezzo)){
						CORPO = "You don't have enough items:<br>You need: <font color =\"LEVEL\">" + String.valueOf(prezzo) + " ";
						CORPO += String.valueOf(central.getNombreITEMbyID(general.BUFFER_ID_ITEM)) + "!";
						return mostraTesto(st, "SORRY!!", CORPO, true, "Return", "principale", playerAbilitato);
					}
				}
				int i = 0;
				while(i <= (buff.size() - 1)){
					String datiSkill = buff.get(i);
					datiSkill = datiSkill.replace("_"," ");
					String[] splitSkill = datiSkill.split(" ");
					String skill = splitSkill[0];
					livello = Integer.valueOf(splitSkill[1]);
					if(buffaPet == 0) {
						SkillData.getInstance().getSkill(Integer.valueOf(skill),livello).applyEffects(st,st);
					} else{
						if(st.getSummon() != null) {
							SkillData.getInstance().getSkill(Integer.valueOf(skill),livello).applyEffects(st.getSummon(),st.getSummon());
						} else{
							CORPO = "You can't use the Pet's options.<br>Summon your pet first!";
							return mostraTesto(st, "INFO:", CORPO, true, "Return", "principale", playerAbilitato);
						}
					}
					i++;
				}
				boolean isPet = buffaPet == 1?true:false;
				central.healAll(st, isPet);
				if(!general.BUFF_GRATIS) {
					opera.removeItem(general.BUFFER_ID_ITEM,prezzo,st);
				}
				if(general.TIME_OUT) {
					barraTempo(st, 3, general.BUFFER_TIME_WAIT, 600);
				}
				return rebuildMainHtml(st, playerAbilitato);
			}
		}else{
			String CORPO = "You have to wait a while if you wish to use my services!";
			return mostraTesto(st, "SORRY!!", CORPO, true, "Retry", "principale", playerAbilitato);
		}
	}


	public static String ricaricaConfigurazione(L2PcInstance st, String playerAbilitato){
		return rebuildMainHtml(st, playerAbilitato);
	}


	public static String editaBuff(String skill, String azionePagina, String tipoBuff, String playerAbilitato){
		azionePagina = azionePagina.replace("-"," ");
		String[] split = azionePagina.split(" ");
		String azione = split[0];
		String pagina = split[1];
		String[] buffSplit = skill.split("_");
		String idBuff = buffSplit[0];
		String livelloBuff = buffSplit[1];
		Connection conn = null;
		PreparedStatement psqry = null;

		try{
			conn = ConnectionFactory.getInstance().getConnection();
			String qry = "UPDATE zeus_buffer_buff_list SET canUse=" + String.valueOf(azione) + " WHERE buffId=" + String.valueOf(idBuff) + " AND buffLevel=" + String.valueOf(livelloBuff);
			psqry = conn.prepareStatement(qry);
			try{
				psqry.executeUpdate();
			}catch (SQLException e) {

			}
		}catch(SQLException b){

		}
		try{
			psqry.close();
			conn.close();
		}catch(SQLException c){

		}
		String nomeLista = "";
		if(tipoBuff.equals("improved")) {
			nomeLista = "Improveds";
		}
		if(tipoBuff.equals("buff")) {
			nomeLista = "Buffs";
		}
		if(tipoBuff.equals("chant")) {
			nomeLista = "Chants";
		}
		if(tipoBuff.equals("dance")) {
			nomeLista = "Dances";
		}
		if (tipoBuff.equals("song")) {
			nomeLista = "Songs";
		}
		if(tipoBuff.equals("resist")) {
			nomeLista = "Resists";
		}
		if(tipoBuff.equals("cubic")) {
			nomeLista = "Cubics";
		}
		if(tipoBuff.equals("prophecy")) {
			nomeLista = "Prophecies";
		}
		if(tipoBuff.equals("special")) {
			nomeLista = "Special_Buffs";
		}
		if(tipoBuff.equals("others")) {
			nomeLista = "Others_Buffs";
		}
		return buffer.vediListaBuff(tipoBuff, nomeLista, Integer.parseInt(pagina), playerAbilitato);
	}

	public static String aggiornaSet(String skill, String nuovoValore, int pagina, String playerAbilitato){
		String[] skillSplit = skill.split("_");
		String idBuff = skillSplit[0];
		String livelloBuff = skillSplit[1];
		Connection conn = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			String qry = "UPDATE zeus_buffer_buff_list SET forClass=" + nuovoValore + " WHERE buffId=" + String.valueOf(idBuff) + " AND bufflevel=" + String.valueOf(livelloBuff);
			PreparedStatement psqry = conn.prepareStatement(qry);
			try{
				psqry.executeUpdate();
				psqry.close();
				conn.close();
			}catch (SQLException e) {
				// TODO: handle exception
			}
		}catch(SQLException e){

		}
		return buffer.vediListaBuff("set", "Buff_Sets", pagina, playerAbilitato);
	}

	public static void barraTempo(L2PcInstance st, int coloreBarra, int durata, int compensazione){
		int scadenza = Integer.valueOf((int) ((System.currentTimeMillis() + (durata * 1000))/1000));
		general.charBufferTime.put(st, scadenza);
		st.sendPacket(new SetupGauge(coloreBarra, (durata * 1000) + compensazione));
	}

	public static String Assegna_Buff(L2PcInstance st, String eventParam1, String eventParam2, String eventParam3, String eventParam4){
		String CORPO ="";
		boolean accessoAmministrativo;
		if(eventParam4.equals("True") && general.BUFFER_GM_ONLY) {
			accessoAmministrativo = true;
		} else {
			accessoAmministrativo = false;
		}
		if((System.currentTimeMillis() / 1000) > general.charBufferTime.get(st)){
			if(!general.BUFFER_GM_ONLY || accessoAmministrativo){
				if(!general.BUFFER_CON_KARMA && (st.getKarma() > 0)){
					CORPO = "You have too much <font color=\"FF0000\">karma!</font><br>Come back, when you don't have any karma!";
					if(eventParam4.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, false, "", "", "False");
					}
				}else if(st.getLevel() < general.BUFFER_LVL_MIN){
					CORPO = "Your level is too low!<br>You have to be at least level <font color\"LEVEL\">" + String.valueOf(general.BUFFER_LVL_MIN) + "</font>, to ";
					CORPO += "use my services!";
					if(eventParam4.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, false, "", "", "False");
					}
				}else if(st.getPvpFlag() > 0){
					CORPO = "You can't buff while you are <font color=\"FF00FF\">flagged!</font><br>Wait some time and try again!";
					if(eventParam4.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, false, "", "", "False");
					}
				}else if(st.isInCombat()){
					CORPO = "You can't buff while you are attacking!<br>Stop your fight and try again!";
					if(eventParam4.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, false, "", "", "False");
					}
				}else{
					String[] splitSkill = eventParam1.split("_");
					String idSkill = splitSkill[0];
					String livelloSkill = splitSkill[1];
					return assegnaBuff(st, general.havePetSum.get(st) , idSkill, livelloSkill, eventParam2, Integer.valueOf(eventParam3), eventParam4);
				}
			}else{
				CORPO = "This buffer is only for VIP's!<br>Contact the administrator for more info!";
				return mostraTesto(st, "SORRY!!", CORPO, false, "", "", "False");
			}
		}else{
			CORPO = "You have to wait a while if you wish to use my services!";
			return mostraTesto(st, "SORRY!!", CORPO, false, "", "", "False");
		}
	}


	public static String Assegna_Pacchetto_Buff(L2PcInstance st, String eventParam1){
		String CORPO ="";
		boolean accessoAmministrativo;
		if(eventParam1.equals("True") && general.BUFFER_GM_ONLY) {
			accessoAmministrativo = true;
		} else {
			accessoAmministrativo = false;
		}

		if ((System.currentTimeMillis() / 1000) > general.charBufferTime.get(st)){
			if(!general.BUFFER_GM_ONLY || accessoAmministrativo){
				if(!general.BUFFER_CON_KARMA && (st.getKarma() > 0)){
					CORPO = "You have too much <font color=\"FF0000\">karma!</font><br>Come back, when you don't have any karma!";
					if(eventParam1.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, false, "", "", "False");
					}
				}else if(st.getLevel() < general.BUFFER_LVL_MIN){
					CORPO = "Your level is too low!<br>You have to be at least level <font color\"LEVEL\">" + String.valueOf(general.BUFFER_LVL_MIN) + "</font>, to ";
					CORPO += "use my services!";
					if(eventParam1.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, false, "", "", "False");
					}
				}else if(st.getPvpFlag() > 0){
					CORPO = "You can't buff while you are <font color=\"FF00FF\">flagged!</font><br>Wait some time and try again!";
					if(eventParam1.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, true, "", "", "False");
					}
				}else if(st.isInCombat()){
					CORPO = "You can't buff while you are attacking!<br>Stop your fight and try again!";
					if(eventParam1.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, true, "", "", "False");
					}
				}else{
					return assegnaPacchettoBuff(st, general.havePetSum.get(st), eventParam1);
				}
			}else{
				CORPO = "This buffer is only for VIP's!<br>Contact the administrator for more info!";
				return mostraTesto(st, "SORRY!!", CORPO, false, "", "", "False");
			}
		}else{
			CORPO = "You have to wait a while if you wish to use my services!";
			return mostraTesto(st, "SORRY!!", CORPO, false, "", "", "False");
		}
	}


	public static String Peino(L2PcInstance st, String eventParam1){
		String CORPO ="";
		boolean accessoAmministrativo;
		if(eventParam1.equals("True") && general.BUFFER_GM_ONLY) {
			accessoAmministrativo = true;
		} else {
			accessoAmministrativo = false;
		}

		if((System.currentTimeMillis() / 1000) > general.charBufferTime.get(st)){
			if (!general.BUFFER_GM_ONLY || accessoAmministrativo){
				if(!general.BUFFER_CON_KARMA && (st.getKarma() > 0)){
					CORPO = "You have too much <font color=\"FF0000\">karma!</font><br>Come back, when you don't have any karma!";
					if(eventParam1.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, true, "", "", "False");
					}
				}else if(st.getLevel() < general.BUFFER_LVL_MIN){
					CORPO = "Your level is too low!<br>You have to be at least level <font color\"LEVEL\">" + String.valueOf(general.BUFFER_LVL_MIN) + "</font>, to ";
					CORPO += "use my services!";
					if(eventParam1.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, false, "", "", "False");
					}
				}else if(st.getPvpFlag() > 0){
					CORPO = "You can't buff while you are <font color=\"FF00FF\">flagged!</font><br>Wait some time and try again!";
					if(eventParam1.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, true, "", "", "False");
					}
				}else if(st.isInCombat()){
					CORPO = "You can't buff while you are attacking!<br>Stop your fight and try again!";
					if(eventParam1.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, true, "", "", "False");
					}
				}else{
					return funzionePieno(st, general.havePetSum.get(st), eventParam1);
				}
			}else{
				CORPO = "This buffer is only for VIP's!<br>Contact the administrator for more info!";
				return mostraTesto(st, "SORRY!!", CORPO, true, "", "", "False");
			}
		}else{
			CORPO = "You have to wait a while if you wish to use my services!";
			return mostraTesto(st, "SORRY!!", CORPO, true, "", "", "False");
		}
	}




	public static String Assegna(L2PcInstance st,String eventParam1, String eventParam2, String eventParam3){
		String CORPO ="";
		boolean accessoAmministrativo;
		if(eventParam3.equals("True") && general.BUFFER_GM_ONLY){
			accessoAmministrativo = true;
		}else{
			accessoAmministrativo = false;
		}
		if((System.currentTimeMillis() / 1000) > general.charBufferTime.get(st)){
			if(!general.BUFFER_GM_ONLY || accessoAmministrativo){
				if(!general.BUFFER_CON_KARMA && (st.getKarma() > 0)){
					CORPO = "You have too much <font color=\"FF0000\">karma!</font><br>Come back, when you don't have any karma!";
					if(eventParam3.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, false, "", "", "False");
					}
				}else if(st.getLevel() < general.BUFFER_LVL_MIN){
					CORPO = "Your level is too low!<br>You have to be at least level <font color\"LEVEL\">" + String.valueOf(general.BUFFER_LVL_MIN) + "</font>, to ";
					CORPO += "use my services!";
					if(eventParam3.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, false, "", "", "False");
					}
				}else if(st.getPvpFlag() > 0){
					CORPO = "You can't buff while you are <font color=\"FF00FF\">flagged!</font><br>Wait some time and try again!";
					if(eventParam3.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, false, "", "", "False");
					}
				}else if(st.isInCombat()){
					CORPO = "You can't buff while you are attacking!<br>Stop your fight and try again!";
					if(eventParam3.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, false, "", "", "False");
					}
				}else{
					return assegnaSchema(st, Integer.valueOf(eventParam2), Integer.valueOf(eventParam1), eventParam3);
				}
			}else{
				CORPO = "This buffer is only for VIP's!<br>Contact the administrator for more info!";
				return mostraTesto(st, "SORRY!!", CORPO, false, "", "", "False");
			}
		}else{
			CORPO = "You have to wait a while if you wish to use my services!";
			return mostraTesto(st, "SORRY!!", CORPO, false, "", "", "False");
		}
	}


	public static String DeluxBuffer(L2PcInstance st){
		int petFuori;
		if(st.getSummon() != null) {
			petFuori = 1;
		} else {
			petFuori = 0;
		}

		L2PcInstance player = st;
		general.havePetSum.put(st,petFuori);

		String CORPO ="";
		boolean accessoAmministrativo;
		if (st.isGM()) {
			accessoAmministrativo = true;
		} else {
			accessoAmministrativo = false;
		}

		boolean playerAbilitato;
		if((player.getAccessLevel().getLevel() == general.BUFFER_ID_ACCESO_ADMIN) || (player.getAccessLevel().getLevel() == general.BUFFER_ID_ACCESO_GM)) {
			playerAbilitato = true;
		} else {
			playerAbilitato = false;
		}

		if((System.currentTimeMillis() / 1000) > general.charBufferTime.get(st)){
			if(!general.BUFFER_GM_ONLY || accessoAmministrativo){
				if(!general.BUFFER_CON_KARMA && (player.getKarma() > 0)){
					CORPO = "You have too much <font color=\"FF0000\">karma!</font><br>Come back, when you don't have any karma!";
					if(playerAbilitato){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "script", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, false, "", "", "False");
					}
				}else if(player.getLevel() < general.BUFFER_LVL_MIN){
					CORPO = "Your level is too low!<br>You have to be at least level <font color\"LEVEL\">" + String.valueOf(general.BUFFER_LVL_MIN) + "</font>, to ";
					CORPO += "use my services!";
					if(playerAbilitato){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "script", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, true, "", "", "False");
					}
				}else if(player.getPvpFlag() > 0){
					CORPO = "You can't buff while you are <font color=\"FF00FF\">flagged!</font><br>Wait some time and try again!";
					if(playerAbilitato){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "script", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, true, "", "", "False");
					}
				}else if(player.isInCombat()){
					CORPO = "You can't buff while you are attacking!<br>Stop your fight and try again!";
					if(playerAbilitato){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "script", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, false, "", "", "False");
					}
				}else{
						return rebuildMainHtml(st, playerAbilitato ? "True" : "False");
				}
			}else{
				CORPO = "This buffer is only for VIP's!<br>Contact the administrator for more info!";
				return mostraTesto(st, "SORRY!!", CORPO, false, "", "", "False");
			}
		}else{
			CORPO = "You have to wait a while if you wish to use my services!";
			return mostraTesto(st, "SORRY!!", CORPO, true, "", "", "False");
		}
	}




	public static String Rimouvi_Buff(L2PcInstance st, String eventParam1){
		String CORPO ="";
		boolean accessoAmministrativo;
		if(eventParam1.equals("True") && general.BUFFER_GM_ONLY) {
			accessoAmministrativo = true;
		} else {
			accessoAmministrativo = false;
		}
		if ((System.currentTimeMillis() / 1000) > general.charBufferTime.get(st)){
			if(!general.BUFFER_GM_ONLY || accessoAmministrativo){
				if(!general.BUFFER_CON_KARMA && (st.getKarma() > 0)){
					CORPO = "You have too much <font color=\"FF0000\">karma!</font><br>Come back, when you don't have any karma!";
					if(eventParam1.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, true, "", "", "False");
					}
				}else if(st.getLevel() < general.BUFFER_LVL_MIN){
					CORPO = "Your level is too low!<br>You have to be at least level <font color\"LEVEL\">" + String.valueOf(general.BUFFER_LVL_MIN) + "</font>, to ";
					CORPO += "use my services!";
					if(eventParam1.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, true, "", "", "False");
					}
				}else if(st.getPvpFlag() > 0){
					CORPO = "You can't buff while you are <font color=\"FF00FF\">flagged!</font><br>Wait some time and try again!";
					if (eventParam1.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, true, "", "", "False");
					}
				}else if(st.isInCombat()){
					CORPO = "You can't buff while you are attacking!<br>Stop your fight and try again!";
					if(eventParam1.equals("True")){
						CORPO += "<br><br><font color=\"LEVEL\">As you are an authorised GM, this page is only to see if all works good.<br>You ";
						CORPO += "can go on pressing the button below.</font>";
						return mostraTesto(st, "INFO:", CORPO, true, "Go on", "principale", "True");
					}else{
						return mostraTesto(st, "INFO:", CORPO, true, "", "", "False");
					}
				}else{
					return rimuoviBuff(st, general.havePetSum.get(st), eventParam1);
				}
			}else{
				CORPO = "This buffer is only for VIP's!<br>Contact the administrator for more info!";
				return mostraTesto(st, "SORRY!!", CORPO, false, "", "", "False");
			}
		}else{
			CORPO = "You have to wait a while if you wish to use my services!";
			return mostraTesto(st, "SORRY!!", CORPO, false, "", "", "False");
		}
	}


	public static Boolean skillAbilitata(int id, int livello){
		boolean abilitata;
		try{
			Connection conn = ConnectionFactory.getInstance().getConnection();
			String qry = "SELECT canUse FROM zeus_buffer_buff_list WHERE buffId=" + String.valueOf(id) + " AND buffLevel=" + String.valueOf(livello);
			PreparedStatement psqry = conn.prepareStatement(qry);
			ResultSet rss = psqry.executeQuery();

			rss.next();
			if(rss.getString("canUse").equals("1")) {
				abilitata = true;
			} else {
				abilitata = false;
			}
			conn.close();
		}catch(SQLException e){
			abilitata = false;
		}
		return abilitata;
	}

	public static Boolean buffUsato(int idSchema, int id, int livello){
		boolean usato = false;
		try{
			Connection conn = ConnectionFactory.getInstance().getConnection();
			String qry = "SELECT * FROM zeus_buffer_scheme_contents WHERE schemeId=" + String.valueOf(idSchema) + " AND buffId=" + String.valueOf(id) + " AND buffLevel=" + String.valueOf(livello);
			PreparedStatement psqry = conn.prepareStatement(qry);
			ResultSet rss = psqry.executeQuery();
			if(rss.next()) {
				usato = true;
			}
			conn.close();
			}catch (SQLException e) {

			}
		return usato;
	}

	public static String recuperaDescrizione(int idBuff, int livelloBuff) {
		String descrizione="";
		try{
		Connection conn = ConnectionFactory.getInstance().getConnection();
		String qry = "SELECT buffDesc FROM zeus_buffer_buff_list WHERE buffId=" + String.valueOf(idBuff) + " AND buffLevel=" + String.valueOf(livelloBuff);
		PreparedStatement psqry = conn.prepareStatement(qry);
		ResultSet rss = psqry.executeQuery();
		rss.next();
		descrizione = rss.getString("buffDesc");
		conn.close();
		}catch(SQLException e){
		}
		return descrizione;
	}

	public static String contaBuff(int idSchema){
		int conteggioBuff = 0;
		int conteggioDance = 0;
		try{
			Connection conn = ConnectionFactory.getInstance().getConnection();
			String qry = "SELECT * FROM zeus_buffer_scheme_contents WHERE schemeId=" + String.valueOf(idSchema);
			PreparedStatement psqry = conn.prepareStatement(qry);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				if ((rss.getInt("buffClass") < 5) || (rss.getInt("buffClass") > 7)) {
					conteggioBuff++;
				}
				if ((rss.getInt("buffClass") > 5) && (rss.getInt("buffClass") < 8) ) {
					conteggioDance++;
				}
			}
			conn.close();
		}catch(SQLException e){

		}

		String conteggio = String.valueOf(conteggioBuff) + "_" + String.valueOf(conteggioDance);
		return conteggio;
	}

	public static String costruisciContatore(){
		int contatoreBuffMago = 0; int contatoreDanceMago = 0; int contatoreBuffCombattente = 0; int contatoreDanceCombattente = 0; int contatoreBuffTutti = 0;
		int contatoreDanceTutti = 0; int contatoreBuffScudo = 0;
		try{
		Connection conn = ConnectionFactory.getInstance().getConnection();
		String qry = "SELECT buffClass, forClass FROM zeus_buffer_buff_list WHERE canUse=1";
		PreparedStatement psqry = conn.prepareStatement(qry);
		ResultSet rss = psqry.executeQuery();
		while(rss.next()){
			if((Integer.valueOf(rss.getInt("buffClass")) < 6) || (Integer.valueOf(rss.getInt("buffClass")) > 7) ){
				if(Integer.valueOf(rss.getInt("forClass")) == 1) {
					contatoreBuffMago++;
				} else if(Integer.valueOf(rss.getInt("forClass")) == 2) {
					contatoreBuffCombattente++;
				} else if(Integer.valueOf(rss.getInt("forClass")) == 3) {
					contatoreBuffScudo ++;
				} else if(Integer.valueOf(rss.getInt("forClass")) == 4) {
					contatoreBuffTutti ++;
				}
			}else if((Integer.valueOf(rss.getInt("buffClass")) > 5) && (Integer.valueOf(rss.getInt("buffClass")) < 8)){
				if(Integer.valueOf(rss.getInt("forClass")) == 1) {
					contatoreDanceMago ++;
				} else if(Integer.valueOf(rss.getInt("forClass")) == 2) {
					contatoreDanceCombattente ++;
				} else if(Integer.valueOf(rss.getInt("forClass")) == 4) {
					contatoreDanceTutti ++;
				}
			}
		}
		conn.close();
		}catch(SQLException e){

		}
		String stringaContatori = String.valueOf(contatoreBuffMago) + "_" + String.valueOf(contatoreDanceMago) + "_" + String.valueOf(contatoreBuffCombattente) + "_";
		stringaContatori += String.valueOf(contatoreDanceCombattente) + "_" + String.valueOf(contatoreBuffScudo) + "_" + String.valueOf(contatoreBuffTutti) + "_";
		stringaContatori += String.valueOf(contatoreDanceTutti);
		return stringaContatori;
	}

	public static int ottieniClasseBuff(int idSkill){
		int classeBuff =0;
		try{
		Connection conn = ConnectionFactory.getInstance().getConnection();
		String qry = "SELECT * FROM zeus_buffer_buff_list WHERE buffId=" + String.valueOf(idSkill);
		PreparedStatement psqry = conn.prepareStatement(qry);
		ResultSet rss = psqry.executeQuery();
		rss.next();
		classeBuff = rss.getInt("buffClass");
		conn.close();
		}catch(SQLException e){

		}
		return classeBuff;
	}

	public static String registraSchema(L2PcInstance st, int perPlayer, String nomeSchema, String playerAbilitato){
		Connection conn = null;
		String qry ="";
		PreparedStatement psqry;
		ResultSet rss;
		int idPlayer = 0;
		conn = ConnectionFactory.getInstance().getConnection();
		nomeSchema = nomeSchema.replace("_"," ");
		String CORPO ="";
		idPlayer = st.getObjectId();
		qry = "SELECT * FROM zeus_buffer_scheme_list WHERE playerId=" + String.valueOf(idPlayer) + " AND schemeName=\"" + nomeSchema;
		if (perPlayer == 1) {
			qry += " (Pet)";
		}
		qry += "\" AND buffPlayer=" + String.valueOf(perPlayer);
		try{
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			if(rss.next()){
				CORPO = "You already have a scheme with that name for <font color=\"LEVEL\">";
				if(perPlayer == 0) {
					CORPO += String.valueOf(st.getName());
				} else {
					CORPO += "your pet";
				}
				CORPO += "</font> use, please choose another.";
				try{
					conn.close();
				}catch(SQLException c){

				}
				return buffer.mostraTesto(st, "INFO:", CORPO, true, "Return", "principale", playerAbilitato);
			}
		}catch(SQLException a){
			try {
				conn.close();
			} catch (SQLException e) {

			}
		}

		if (perPlayer == 1) {
			nomeSchema += " (Pet)";
		}
		qry = "INSERT INTO zeus_buffer_scheme_list (playerId, buffPlayer, schemeName) VALUES (" + String.valueOf(idPlayer) + ", " + String.valueOf(perPlayer) + ", \"";
		qry += nomeSchema + "\")";
		try{
			psqry = conn.prepareStatement(qry);
			try{
				psqry.executeUpdate();
				psqry.close();
				conn.close();
			}catch (SQLException e) {
				// TODO: handle exception
			}
		}catch(SQLException a){

		}

		return buffer.rebuildMainHtml(st, playerAbilitato);
	}


	public static String aggiungiBuffSchema(int idSchema, int idSkill, int livello, String paginaBuff, int buffInSchema, String playerAbilitato){
		int classeBuff = ottieniClasseBuff(idSkill);
		try{
		Connection conn = ConnectionFactory.getInstance().getConnection();
		String qry = "INSERT INTO zeus_buffer_scheme_contents (schemeId, buffId, buffLevel, buffClass) VALUES (" + String.valueOf(idSchema) + ", " + String.valueOf(idSkill);
		qry += ", " + String.valueOf(livello) + ", " + String.valueOf(classeBuff) + ")";
		PreparedStatement psqry = conn.prepareStatement(qry);
		psqry.executeUpdate();
		psqry.close();
		conn.close();
		}catch(SQLException e){

		}

		int buffInseriti = buffInSchema + 1;
		if (buffInseriti == (general.BUFF_PER_SCHEMA + general.DANCE_PER_SCHEMA)) {
			return buffer.ottieniListaOpzioni(idSchema, playerAbilitato);
		} else {
			return buffer.visualizzaBuffSchema(idSchema, paginaBuff, "aggiungi", playerAbilitato);
		}
	}

	public static String rimuoviBuffSchema(int idSchema,int idSkill, int livello, String paginaBuff, int buffInSchema, String playerAbilitato){
		Connection conn = null;
		PreparedStatement psqry = null;

		try{
			conn = ConnectionFactory.getInstance().getConnection();
			String qry = "DELETE FROM zeus_buffer_scheme_contents WHERE schemeId=" + String.valueOf(idSchema) + " AND buffId=" + String.valueOf(idSkill) + " AND buffLevel=";
			qry += String.valueOf(livello);
			psqry = conn.prepareStatement(qry);
			try{
				psqry.executeUpdate();
				psqry.close();
				int buffInseriti = buffInSchema - 1;
				if (buffInseriti == 0) {
					try{
						conn.close();
					}catch(SQLException a){

					}
					return buffer.ottieniListaOpzioni(idSchema, playerAbilitato);
				} else {
					try{
						conn.close();
					}catch(SQLException a){

					}
					return buffer.visualizzaBuffSchema(idSchema, paginaBuff, "rimuovi", playerAbilitato);
				}
			}catch(SQLException e){

			}
		}catch(SQLException a){

		}
		try{
			conn.close();
		}catch(SQLException b){

		}
		return buffer.visualizzaBuffSchema(idSchema, paginaBuff, "rimuovi", playerAbilitato);
	}


	public static String cancellaDefinitivo(L2PcInstance st, int idSchema, String playerAbilitato){
		Connection conn = null;
		PreparedStatement psqry = null;
		boolean schemaSvuotato=false;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			String qry = "DELETE FROM zeus_buffer_scheme_contents WHERE schemeId=" + String.valueOf(idSchema);
			psqry = conn.prepareStatement(qry);
			try{
				psqry.executeUpdate();
				psqry.close();
				schemaSvuotato = true;
			}catch(SQLException e){
				schemaSvuotato = false;
			}
		}catch(SQLException a){
			_log.warning("Error borrado sche1: " + a.getMessage());
		}

		if(schemaSvuotato){
			try{
				String qry = "DELETE FROM zeus_buffer_scheme_list WHERE id=" + String.valueOf(idSchema);
				psqry = conn.prepareStatement(qry);
				psqry.executeUpdate();
				conn.close();
			}catch(SQLException e){
				_log.warning("Error borrado sche1: " + e.getMessage());
			}
		}
		try{
			conn.close();
		}catch(SQLException a){

		}

		return buffer.rebuildMainHtml(st, playerAbilitato);
	}

	public static String cambiaGruppo(L2PcInstance st, int idSkill, int livelloSkill, int classe, String tipo, int pagina, String playerAbilitato){
		Connection conn = null;
		PreparedStatement psqry = null;
		ResultSet rss = null;
		int nuovaPosizione = 0;
		int posizioneAttuale=0;
		int classeAttuale = 0;
		String qry="";
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			qry = "SELECT buffClass, buffOrder FROM zeus_buffer_buff_list WHERE buffId=" + String.valueOf(idSkill) + " AND buffLevel=" + String.valueOf(livelloSkill);
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			rss.next();
			classeAttuale = rss.getInt("buffClass");
			posizioneAttuale = rss.getInt("buffOrder");

			try{
				conn.close();
			}catch(SQLException a){
				_log.warning("Error buffer, "+ a.getMessage());
			}

			conn = ConnectionFactory.getInstance().getConnection();
			qry = "SELECT buffOrder FROM zeus_buffer_buff_list WHERE buffClass=" + String.valueOf(classe) + " AND buffType=\"" + String.valueOf(tipo) + "\"";
			try{
				psqry = conn.prepareStatement(qry);
				rss = psqry.executeQuery();
				while(rss.next()){
					if(Integer.valueOf(rss.getInt("buffOrder")) > Integer.valueOf(nuovaPosizione)) {
						nuovaPosizione = rss.getInt("buffOrder");
					}
				}
			}catch(SQLException b){
				_log.warning("Error buffer, "+b.getMessage());
			}
		}catch(SQLException a){

		}

		try{
			conn.close();
		}catch(SQLException a){

		}

		nuovaPosizione++;
		boolean classeOk=false;;
		String CORPO ="";
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			qry = "UPDATE zeus_buffer_buff_list SET buffClass=" + String.valueOf(classe) + " , buffType=\"" + String.valueOf(tipo) + "\" , buffOrder=" + String.valueOf(nuovaPosizione);
			qry += " WHERE buffID=" + String.valueOf(idSkill) + " AND buffLevel=" + String.valueOf(livelloSkill);
			psqry = conn.prepareStatement(qry);
			try{
				psqry.executeUpdate();
				psqry.close();
				classeOk = true;
				CORPO = "Skill id " + String.valueOf(idSkill) + ": class updated in group " + String.valueOf(classe) + ", type " + String.valueOf(tipo) + ", in position ";
				CORPO += String.valueOf(nuovaPosizione);
				conn.close();
			}catch (SQLException e) {
				classeOk = false;
				CORPO += "Skill id " + String.valueOf(idSkill) + ": NOT UPDATED! Try again.";
			}
		}catch(SQLException a){

		}

		try{
			conn.close();
		}catch(Exception a ){

		}

		//st.sendMessage(CORPO);
		String stringa;
		Vector<String> listaCoda = new Vector<String>();

		if(classeOk){
			try{
				conn = ConnectionFactory.getInstance().getConnection();
				qry = "SELECT id, buffOrder FROM zeus_buffer_buff_list WHERE buffClass=" + String.valueOf(classeAttuale) + " AND buffOrder > ";
				qry += String.valueOf(posizioneAttuale) + " ORDER BY buffOrder";
				psqry = conn.prepareStatement(qry);
				rss = psqry.executeQuery();
				while(rss.next()){
					int id = rss.getInt("id");
					int posizione = rss.getInt("buffOrder");
					stringa = String.valueOf(id) + "_" + String.valueOf(posizione);
					listaCoda.add(stringa);
				}
			}catch(SQLException a){

			}

			try{
				conn.close();
			}catch(Exception a ){

			}

			if(listaCoda.size() > 1){
				int i = 0;
				while(i <= (listaCoda.size()-1)){
					stringa = listaCoda.get(i);
					String[] splitDati = stringa.split("_");
					String id = splitDati[0];
					String posizione = splitDati[1];
					try{
						conn = ConnectionFactory.getInstance().getConnection();
						qry = "UPDATE zeus_buffer_buff_list SET buffOrder=" + String.valueOf(posizioneAttuale) + " WHERE id=" + String.valueOf(id);
						posizioneAttuale = Integer.valueOf(posizione);
						psqry = conn.prepareStatement(qry);
						psqry.executeUpdate();
						psqry.close();
					}catch(SQLException a){

					}
					try{
						conn.close();
					}catch(SQLException a){

					}
					i++;
				}
			}
		}

		try{
			conn.close();
		}catch(SQLException a){

		}
		return buffer.editaGruppi(pagina, playerAbilitato);
	}


	public static void ordinaGruppo(String gruppo){
		Vector<String> listaBuff = new Vector<String>();
		String qry;
		PreparedStatement psqry;
		Connection conn = null;
		try{
		conn = ConnectionFactory.getInstance().getConnection();
		qry = "SELECT id, buffOrder FROM zeus_buffer_buff_list WHERE buffType=\"" + String.valueOf(gruppo) + "\" ORDER BY buffOrder";
		psqry = conn.prepareStatement(qry);
		ResultSet rss = psqry.executeQuery();
			while(rss.next()){
				int id = rss.getInt("id");
				int posizione = rss.getInt("buffOrder");
				String stringa = String.valueOf(id) + "_" + String.valueOf(posizione);
				listaBuff.add(stringa);
			}
		}catch(SQLException e){

		}
		int totaleBuff = listaBuff.size();
		String verifica = listaBuff.get(totaleBuff - 1);
		String[] splitVerifica = verifica.split("_");
		String ultimaPosizione = splitVerifica[1];
		if(Integer.valueOf(ultimaPosizione) != Integer.valueOf(totaleBuff)){
			int i = 0;
			while(i <= (listaBuff.size() - 1)){
				String stringa = listaBuff.get(i);
				String[] splitStringa = stringa.split("_");
				String id = splitStringa[0];
				int posizione = Integer.valueOf(splitStringa[1]);
				if(posizione != (i + 1)){
					try{
					qry = "UPDATE zeus_buffer_buff_list SET buffOrder=" + String.valueOf(i + 1) + " WHERE id=" + String.valueOf(id);
					psqry = conn.prepareStatement(qry);
					psqry.executeUpdate();
					psqry.close();
					}catch (SQLException e) {
						// TODO: handle exception
					}
				}
				i++;
			}
		}
		try{
			conn.close();
		}catch (SQLException e) {

		}
	}



	public static String spostaOrdine(int idSkill, int livelloSkill, String tipo, String direzione, String pagina, String playerAbilitato){
		String qry;
		PreparedStatement psqry;
		Connection conn = null;
		ResultSet rss;
		int id = 0; int attuale = 0; int nuovo = 0;

		try{
			conn = ConnectionFactory.getInstance().getConnection();
			qry = "SELECT id, buffType, buffOrder, buffId FROM zeus_buffer_buff_list WHERE buffType=\"" + String.valueOf(tipo) + "\" ORDER BY buffOrder";
			if(direzione.equals("su")) {
				qry += " DESC";
			}
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			while(rss.next()){
				if(rss.getInt("buffId") == Integer.valueOf(idSkill)) {
					attuale = rss.getInt("buffOrder");
				}
				if(direzione.equals("su")){
					if((nuovo == 0) && (rss.getInt("buffOrder") < attuale)){
						id = rss.getInt("id");
						nuovo = rss.getInt("buffOrder");
					}
				}else{
					if((nuovo == 0) && (attuale != 0) && (rss.getInt("buffOrder") > attuale)){
						id = rss.getInt("id");
						nuovo = rss.getInt("buffOrder");
					}
				}
			}
		}catch(SQLException e){

		}
		try {
			conn.close();
		} catch (SQLException e1) {
		}

		boolean aggiornato;
		if(nuovo != 0){
			qry = "UPDATE zeus_buffer_buff_list SET buffOrder=" + String.valueOf(nuovo) + " WHERE buffId=" + String.valueOf(idSkill) + " AND buffLevel=" + String.valueOf(livelloSkill);
			try{
				conn = ConnectionFactory.getInstance().getConnection();
				psqry = conn.prepareStatement(qry);
				psqry.executeUpdate();
				psqry.close();
				aggiornato = true;
			}catch(SQLException e){
				_log.warning("BUFFER->"+e.getMessage());
				aggiornato = false;
			}
			if(aggiornato){
				qry = "UPDATE zeus_buffer_buff_list SET buffOrder=" + String.valueOf(attuale) + " WHERE id=" + String.valueOf(id);
				try{
					//conn = ConnectionFactory.getInstance().getConnection();
					psqry = conn.prepareStatement(qry);
					psqry.executeUpdate();
					psqry.close();
				}catch (SQLException e) {
					_log.warning("BUFFER->"+e.getMessage());
				}
			}
		}
		try {
			conn.close();
		} catch (SQLException e) {

		}
		String nomeGruppo = null;
		if(tipo.equals("improved")) {
			nomeGruppo = "Improveds";
		}
		if(tipo.equals("buff")) {
			nomeGruppo = "Buffs";
		}
		if(tipo.equals("resist")) {
			nomeGruppo = "Resists";
		}
		if(tipo.equals("prophecy")) {
			nomeGruppo = "Prophecies";
		}
		if(tipo.equals("chant")) {
			nomeGruppo = "Chants";
		}
		if(tipo.equals("cubic")) {
			nomeGruppo = "Cubics";
		}
		if(tipo.equals("dance")) {
			nomeGruppo = "Dances";
		}
		if(tipo.equals("song")) {
			nomeGruppo = "Songs";
		}
		if(tipo.equals("special")) {
			nomeGruppo = "Specials";
		}
		if(tipo.equals("others")) {
			nomeGruppo = "Others";
		}
		return buffer.ordinaListaBuff(tipo, nomeGruppo, Integer.valueOf(pagina), playerAbilitato);
	}



}
