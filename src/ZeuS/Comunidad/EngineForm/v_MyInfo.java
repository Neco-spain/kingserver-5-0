package ZeuS.Comunidad.EngineForm;

import java.util.HashMap;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.Config.premiumsystem;
import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.central;
import ZeuS.procedimientos.opera;

public class v_MyInfo {
	private static String CrearCharInformation(L2PcInstance player){
		
		String PremiumCharInicio = "NONE", PremiumCharTermino = "NONE", NombrePremium = "NONE";
		if(general.getPremiumDataFromPlayerOrClan(player.getAccountName()) !=null){
			int idPremium = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getIdPremiumUse();
			premiumsystem PS = general.getPremiumServices().get(idPremium);
			NombrePremium = PS.getName();
			int Inicio = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getBegin();
			int Termino = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getEnd();
			PremiumCharInicio = opera.getDateFromUnixTime(Inicio);
			PremiumCharTermino = opera.getDateFromUnixTime(Termino);
		}
		String strHTML = "<table width=450 background=\"L2UI_CT1.Windows_DF_Drawer_Bg\" height=495  align=CENTER>"+
       "<tr><td width=450 fixwidth=450 align=center><table width=450><tr><td><img src=\"L2UI.SquareGray\" width=450 height=1><img src=\"L2UI.SquareGray\" width=450 height=3>"+
       "<table width=428 bgcolor=1A1A1A><tr><td width=16><img src=\"L2UI_CT1.RadarMap_DF_iCN_Target01\" width=16 height=16></td><td align=CENTER width=680><font color=B96F00>"+
       player.getName() + " character Information</font><br1></td><td width=16><img src=\"L2UI_CT1.RadarMap_DF_iCN_Target01\" width=16 height=16></td></tr></table><img src=\"L2UI.SquareBlank\" width=450 height=3><img src=\"L2UI.SquareGray\" width=450 height=3>"+
       "</td></tr><tr><td></td></tr><tr><td width=450 align=\"CENTER\"><center>";
		
		
		
		
		strHTML += "<table width=330>"+
                         "<tr><td width=210 fixwidth=200 align=RIGHT><font color=B99D73>Nick: </font></td><td width=210 fixwidth=200>%CHAR%</td></tr>"+
                         "<tr><td width=210 fixwidth=200 align=RIGHT><font color=B99D73>Profession: </font></td><td width=210 fixwidth=200>%PROFESION%</td></tr>"+
                         "<tr><td width=210 fixwidth=200 align=RIGHT><font color=B99D73>Level: </font></td><td width=210 fixwidth=200>%LEVEL%</td></tr>"+
                         "<tr><td width=210 fixwidth=200 align=RIGHT><font color=B99D73>Clan: </font></td><td width=210 fixwidth=200>%CLAN%</td></tr>"+
                         "<tr><td width=210 fixwidth=200 align=RIGHT><font color=B99D73>Online Time: </font></td><td width=210 fixwidth=200>%ONLINE%</td></tr>"+
                         "<tr><td width=210 fixwidth=200 align=RIGHT><font color=B99D73>Life Time: </font></td><td width=210 fixwidth=200>%LIFETIME%</td></tr>"+
                         "<tr><td width=210 fixwidth=200 align=RIGHT><font color=B99D73>IP Lan: </font></td><td width=210 fixwidth=200>%IPLAN%</td></tr>"+
                         "<tr><td width=210 fixwidth=200 align=RIGHT><font color=B99D73>IP Wan: </font></td><td width=210 fixwidth=200>%IPWAN%</td></tr>"+
                         "<tr><td width=210 fixwidth=200 align=RIGHT><font color=00FFFF>Premium Account: </font></td><td width=210 fixwidth=200>%VIPACCOUNT%</td></tr>"+
                         "<tr><td width=210 fixwidth=200 align=RIGHT><font color=00FFFF>Premium Account Name: </font></td><td width=210 fixwidth=200>%VIPACCOUNT_NAME%</td></tr>"+
                         "<tr><td width=210 fixwidth=200 align=RIGHT><font color=00FFFF>P. Account Begin Date: </font></td><td width=210 fixwidth=200>%VIPACCOUNT_BEGIN%</td></tr>"+
                         "<tr><td width=210 fixwidth=200 align=RIGHT><font color=00FFFF>P. Account End Date: </font></td><td width=210 fixwidth=200>%VIPACCOUNT_END%</td></tr>"+
                  "</table><br><br><font color=88B973>Last 10 access to your account</font><br>"+
                  "<table width=390 background=\"L2UI_CT1.Windows_DF_Drawer_Bg\">"+
                         "<tr><td width=130 fixwidth=130></td><td width=130 fixwidth=130></td><td width=130 fixwidth=130></td></tr>"+
                         "<tr><td width=130 fixwidth=130><font color=7375B9>WAN IP</font></td><td width=130 fixwidth=130><font color=7375B9>LAN IP</font></td><td width=130 fixwidth=130><font color=7375B9>DATE</font></td></tr>"+
                         "%LASTCON%"+
                         "<tr><td width=130 fixwidth=130></td><td width=130 fixwidth=130></td><td width=130 fixwidth=130></td></tr>"+
                  "</table></center></td></tr></table></td><td width=5></td></tr></table></center>";
			strHTML = strHTML.replace("%CHAR%", player.getName()).replace("%PROFESION%", central.getClassName(player));
			strHTML = strHTML.replace("%LEVEL%", String.valueOf(player.getLevel())).replace("%CLAN%", ( player.getClan() !=null ? player.getClan().getName() : "NO CLAN" ));
			strHTML = strHTML.replace("%ONLINE%", general.getLifeToday(player)).replace("%LIFETIME%", opera.getTiempoON(player.getOnlineTime()));
			strHTML = strHTML.replace("%IPLAN%", ZeuS.getIp_pc(player) ).replace("%IPWAN%", ZeuS.getIp_Wan(player));
			strHTML = strHTML.replace("%VIPACCOUNT%", opera.isPremium_Player(player) ? "YES" : "NO" ).replace("%VIPACCOUNT_BEGIN%", PremiumCharInicio).replace("%VIPACCOUNT_END%", PremiumCharTermino) ;
			strHTML = strHTML.replace("%VIPACCOUNT_NAME%", NombrePremium ) ;
			

			String Colores[] = {"73B9B3","73B98A"};
			if(general.getLastConnections(player)!=null){
				HashMap<Integer, HashMap<String, String>>LastCon = general.getLastConnections(player);
	
				boolean Continuar = true;
				
				String strLastConnection = "";
				
				int Contador = 1;
				
				while(Continuar){
					if(LastCon.containsKey(Contador)){
						strLastConnection += " <tr><td width=130 fixwidth=130><font color="+Colores[Contador%2]+">"+ LastCon.get(Contador).get("WAN") +"</font></td>"
								+ "<td width=130 fixwidth=130><font color="+Colores[Contador%2]+">"+ LastCon.get(Contador).get("LAN") +"</font></td>"
										+ "<td width=130 fixwidth=130><font color="+Colores[Contador%2]+">"+ LastCon.get(Contador).get("DATE") +"</font></td></tr>";
					}else{
						Continuar = false;
					}
					Contador++;
				}
				
				strHTML = strHTML.replace("%LASTCON%", strLastConnection);
			}else{
				strHTML = strHTML.replace("%LASTCON%", "");
			}
			
			
			
			
		return strHTML;
	}
	
	
	
	private static String mainHtml(L2PcInstance player, String Params, int idCategoria, String ByPassAnterior){
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = "icon.skill4109";
		String Explica = "My Personal Information";
		String Nombre = "My Personal Information";
		/*
		if(Params.equalsIgnoreCase("FromMain")){
			retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,false,ByPassAnterior);
		}else{
			retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica);
		}*/
		
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
		retorno += CrearCharInformation(player);
		
		retorno +=  cbManager.getPieCommunidad() +  "</body></html>";
		return retorno;
	}
	
	public static String bypass(L2PcInstance player, String params){
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			return mainHtml(player,"FromMain",0,"bypass "+general.COMMUNITY_BOARD_PART_EXEC);
		}
		return "";
	}
	
	
	
}
