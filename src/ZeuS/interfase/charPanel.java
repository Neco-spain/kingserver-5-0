package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.Config.premiumsystem;
import ZeuS.procedimientos.opera;

public class charPanel {

	private static final Logger _log = Logger.getLogger(charPanel.class.getName());

	@SuppressWarnings("static-access")
	public static void delegar(L2PcInstance player, String params){
		if(params == null){
			getCharPanel(player, "");
		}else{
			if(params.length()==0){
				getCharPanel(player, "");
			}else{
				String[] parametros = params.split(" ");
				opera.setPlayerConfig(parametros[0],player);
				getCharPanel(player, "");
			}
		}
	}



	public static void getCharInfo(L2PcInstance player){
		String HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		HTML += central.LineaDivisora(1) + central.headFormat("ZeuS Personal Information") + central.LineaDivisora(1);
		HTML += central.LineaDivisora(3);
		String FondoPremium = "7F7F7F", naranja = "2E9AFE";
		String VioletaTitulo ="201020", VioletaTexto="302030";
		String fontDato = "<font color=\"8FDC9D\">%D%</font>";
		String fontResp = "<font color=\"EF8514\">%D%</font>";
		String tablaPremium = "";//"<table width=270 bgcolor=\""+FondoPremium+"\"><tr><td width=270 align=CENTER><font color=\""+ naranja +"\">Premium Information</font></td></tr></table>";

		boolean isPremiumChar = opera.isPremium_Player(player);
		boolean isPremiumClan = opera.isPremium_Clan(player);
		String PremiumCharInicio = "NONE", PremiumCharTermino = "NONE";
		String PremiumClanInicio = "NONE", PremiumClanTermino = "NONE";

		premiumsystem PS = null;
		
		if(general.PREMIUM_CHAR){
			if(isPremiumChar){
				if(general.getPremiumDataFromPlayerOrClan(player.getAccountName()) !=null){
					int idPremium = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getIdPremiumUse();
					PS = general.getPremiumServices().get(idPremium);
					String NombrePremium = PS.getName();
					int Inicio = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getBegin();
					int Termino = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getEnd();
					PremiumCharInicio = opera.getDateFromUnixTime(Inicio);
					PremiumCharTermino = opera.getDateFromUnixTime(Termino);
				}
			}

			if(PS!=null){
				tablaPremium += "<table width=270 bgcolor=\""+ VioletaTitulo +"\" align=CENTER>"+
								"<tr><td width=270 align=CENTER>Premium Account Info.</td></tr></table>";
	
				tablaPremium += "<table width=270 bgcolor=\""+ VioletaTexto +"\" align=CENTER>"+
						"<tr><td width=200 align=LEFT>"+fontDato.replace("%D%", "Player Acc. Premiun?")+"</td><td width=70 align=RIGHT>"+ fontResp.replace("%D%", (isPremiumChar ? "YES" :  "NO")) + "</td></tr>"+
						"<tr><td width=200 align=LEFT>"+fontDato.replace("%D%", "Exp % Plus")+"</td><td width=70 align=RIGHT>"+ fontResp.replace("%D%", String.valueOf(PS.getexp(false))) +"</td></tr>"+
						"<tr><td width=200 align=LEFT>"+fontDato.replace("%D%", "Sp % Plus")+"</td><td width=70 align=RIGHT>"+ fontResp.replace("%D%", String.valueOf(PS.getsp(false))) +"</td></tr>"+
						"<tr><td width=200 align=LEFT>"+fontDato.replace("%D%", "Adena % Drop")+"</td><td width=70 align=RIGHT>"+ fontResp.replace("%D%", String.valueOf( PS.getadena(false))) +"</td></tr>"+
						"<tr><td width=200 align=LEFT>"+fontDato.replace("%D%", "Spoil % Drop")+"</td><td width=70 align=RIGHT>"+ fontResp.replace("%D%", String.valueOf( PS.getDrop(false))) +"</td></tr>"+
						"<tr><td width=200 align=LEFT>"+fontDato.replace("%D%", "Epaulette Chance+")+"</td><td width=70 align=RIGHT>"+ fontResp.replace("%D%", String.valueOf(PS.getEpaulette())) +"</td></tr>"+
						"<tr><td width=200 align=LEFT>"+fontDato.replace("%D%", "Craft Chance+")+"</td><td width=70 align=RIGHT>"+ fontResp.replace("%D%", String.valueOf(PS.getCraft())) +"</td></tr>"+
						"<tr><td width=200 align=LEFT>"+fontDato.replace("%D%", "Mw Craft Chan.+")+"</td><td width=70 align=RIGHT>"+ fontResp.replace("%D%", String.valueOf(PS.get_mwCraft())) +"</td></tr>"+
						"</table>"+
						"<table width=270 bgcolor=\""+ VioletaTexto +"\" align=CENTER>"+
						"<tr><td width=270 align=CENTER>"+fontDato.replace("%D%", "Begin Date")+"</td></tr>"+
						"<tr><td width=270 align=CENTER>"+ fontResp.replace("%D%", PremiumCharInicio) +"</td></tr>"+
						"<tr><td width=270 align=CENTER>"+fontDato.replace("%D%", "End Date")+"</td></tr>"+
						"<tr><td width=270 align=CENTER>"+ fontResp.replace("%D%", PremiumCharTermino) +"</td></tr>"+
						"</table>";
			}
		}
		
		PS = null;

		if(general.PREMIUM_CLAN){
			if(isPremiumClan){
				if(general.getPremiumDataFromPlayerOrClan(player.getAccountName()) !=null){
					int idPremium = general.getPremiumDataFromPlayerOrClan(String.valueOf(player.getClan().getId())).getIdPremiumUse();
					PS = general.getPremiumServices().get(idPremium);
					String NombrePremium = PS.getName();
					int Inicio = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getBegin();
					int Termino = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getEnd();
					PremiumClanInicio = opera.getDateFromUnixTime(Inicio);
					PremiumClanTermino = opera.getDateFromUnixTime(Termino);
				}
			}

			tablaPremium += central.LineaDivisora(1) + central.LineaDivisora(1);

			if(PS!=null){
			
				tablaPremium += "<table width=270 bgcolor=\""+ VioletaTitulo +"\" align=CENTER>"+
						"<tr><td width=270 align=CENTER>Premium Clan Info.</td></tr></table>";
	
				tablaPremium += "<table width=270 bgcolor=\""+ VioletaTexto +"\" align=CENTER>"+
						"<tr><td width=200 align=LEFT>"+fontDato.replace("%D%", "Player Clan Premiun?")+"</td><td width=70 align=RIGHT>"+ fontResp.replace("%D%", (isPremiumClan ? "YES" :  "NO")) + "</td></tr>"+
						"<tr><td width=200 align=LEFT>"+fontDato.replace("%D%", "Exp % Plus")+"</td><td width=70 align=RIGHT>"+ fontResp.replace("%D%", String.valueOf(PS.getexp(false))) +"</td></tr>"+
						"<tr><td width=200 align=LEFT>"+fontDato.replace("%D%", "Sp % Plus")+"</td><td width=70 align=RIGHT>"+ fontResp.replace("%D%", String.valueOf(PS.getsp(false))) +"</td></tr>"+
						"<tr><td width=200 align=LEFT>"+fontDato.replace("%D%", "Adena % Drop")+"</td><td width=70 align=RIGHT>"+ fontResp.replace("%D%", String.valueOf( PS.getadena(false))) +"</td></tr>"+
						"<tr><td width=200 align=LEFT>"+fontDato.replace("%D%", "Spoil % Drop")+"</td><td width=70 align=RIGHT>"+ fontResp.replace("%D%", String.valueOf( PS.getDrop(false))) +"</td></tr>"+
						"<tr><td width=200 align=LEFT>"+fontDato.replace("%D%", "Epaulette Chance+")+"</td><td width=70 align=RIGHT>"+ fontResp.replace("%D%", String.valueOf(PS.getEpaulette())) +"</td></tr>"+
						"<tr><td width=200 align=LEFT>"+fontDato.replace("%D%", "Craft Chance+")+"</td><td width=70 align=RIGHT>"+ fontResp.replace("%D%", String.valueOf(PS.getCraft())) +"</td></tr>"+
						"<tr><td width=200 align=LEFT>"+fontDato.replace("%D%", "Mw Craft Chan.+")+"</td><td width=70 align=RIGHT>"+ fontResp.replace("%D%", String.valueOf(PS.get_mwCraft())) +"</td></tr>"+
						"</table>"+
						"<table width=270 bgcolor=\""+ VioletaTexto +"\" align=CENTER>"+
						"<tr><td width=270 align=CENTER>"+fontDato.replace("%D%", "Begin Date")+"</td></tr>"+
						"<tr><td width=270 align=CENTER>"+ fontResp.replace("%D%", PremiumCharInicio) +"</td></tr>"+
						"<tr><td width=270 align=CENTER>"+fontDato.replace("%D%", "End Date")+"</td></tr>"+
						"<tr><td width=270 align=CENTER>"+ fontResp.replace("%D%", PremiumCharTermino) +"</td></tr>"+
						"</table>";
			}
		}

		HTML += tablaPremium + central.LineaDivisora(1);

		tablaPremium = "<table width=270 bgcolor=\""+ VioletaTitulo +"\" align=CENTER>"+
				"<tr><td width=270 align=CENTER>Last 10 Logins</td></tr></table>"+central.LineaDivisora(3);

		String Colores[] = {VioletaTitulo,VioletaTexto};

		String TablaDiseño = "<table width=270 bgcolor=\"%COLOR%\" align=CENTER>"+
				"<tr><td width=270 align=CENTER>%P% Wan: %IP_WAN%</td></tr></table>";
		TablaDiseño += "<table width=270 bgcolor=\"%COLOR%\" align=CENTER>"+
						"<tr><td width=140 align=LEFT>"+fontDato.replace("%D%", "Lan: %IP_LAN%")+"</td><td width=160 align=RIGHT>"+ fontResp.replace("%D%", "%DATE%") +"</td></tr></table>"+central.LineaDivisora(1);

		String consulta = "SELECT * FROM zeus_connection WHERE zeus_connection.charID = ? ORDER BY zeus_connection.date DESC LIMIT 0,10";


		Connection conn = null;
		PreparedStatement psqry = null;
		int Contador = 1;
		int MaximoColumnas = 5;
		String tamañoColumnas = String.valueOf(750 / MaximoColumnas);
		boolean haveNext = false;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			psqry.setInt(1, player.getObjectId());
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				tablaPremium += TablaDiseño.replace("%COLOR%",Colores[Contador%2] ).replace("%P%",String.valueOf(Contador) + "º").replace("%IP_WAN%", rss.getString(1)).replace("%IP_LAN%", rss.getString(2)).replace("%DATE%", rss.getString(6));
				Contador++;
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}


		HTML += tablaPremium;

		HTML += central.LineaDivisora(3);
		HTML += central.getPieHTML(false) + "</body></html>";
		central.sendHtml(player, HTML);
	}

	private static void getCharPanel(L2PcInstance player, String params){
		String HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		HTML += central.LineaDivisora(1) + central.headFormat("ZeuS Char Config Panel") + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat(msg.CHARPANEL_PANEL_TO_SET_VARIOUS_SETTING_FOR_YOUR_CHARACTER,"LEVEL") + central.LineaDivisora(1);
		String btnChange ="<button value=\"Change\" action=\"bypass -h voice %COMANDO%\" width=60 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String tablaConfigBox = "<table width=280><tr><td width=270 align=CENTER>%TABLA1%</td></tr></table>";
		String tablaConfigIndi = "<table width=260><tr><td width=200 align=LEFT>%DESCRIP%</td><td width=60 align=CENTER>"+btnChange+"</td></tr></table>";
		tablaConfigIndi += "<table><tr><td width=260 align=LEFT>%ESTADO%</td></tr></table><br>";

		tablaConfigBox = tablaConfigBox.replace("%TABLA1%", tablaConfigIndi);

		String GrillaPrincipal = central.LineaDivisora(1) + central.headFormat( tablaConfigBox ) + central.LineaDivisora(1);

		Vector<String> SetConfig = new Vector<String>();
		SetConfig.add("Block Experience:;.CharPnl expsp 0 0;" + (!general.getCharConfigEXPSP(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Trade Refusal:;.CharPnl tradec 0 0;" + (general.getCharConfigTRADE(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Badbuff Protection:;.CharPnl noBuff 0 0;" + (general.getCharConfigBADBUFF(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Hide Stores:;.CharPnl hideStore 0 0;" + (general.getCharConfigHIDESTORE(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Party Matching Refusal:;.CharPnl partymatching 0 0;" + (general.getCharConfigPartyMatching(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Show My Stat:;.CharPnl showmystat 0 0;" + (general.getCharConfigSHOWSTAT(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Show Top pvp/pk Effect:;.CharPnl effectpvppk 0 0;" + (general.getCharConfigEFFECT(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Announce top pvp/pk entrance:;.CharPnl annoupvppk 0 0;" + (general.getCharConfigANNOU(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));
		SetConfig.add("Refusal Mode:;.CharPnl Refusal 0 0;" + (general.getCharConfigREFUSAL(player) ? "<font color=849D68>Enabled</font>" : "<font color=A26D64>Disabled</font>"));

		for(String secciones: SetConfig){
			String[] Seccc = secciones.split(";");
			String paraGrilla =  GrillaPrincipal;
			paraGrilla = paraGrilla.replace("%DESCRIP%", Seccc[0]).replace("%COMANDO%", Seccc[1]).replace("%ESTADO%", Seccc[2]);
			HTML += paraGrilla+"<br1>";
		}

		HTML += central.getPieHTML() + "</body></html>";

		opera.enviarHTML(player, HTML);

	}

}
