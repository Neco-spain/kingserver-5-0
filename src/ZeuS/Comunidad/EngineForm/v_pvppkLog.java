package ZeuS.Comunidad.EngineForm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.interfase.logpvppk;
import ZeuS.procedimientos.opera;

public class v_pvppkLog extends logpvppk{
	
	private static Logger _log = Logger.getLogger(v_pvppkLog.class.getName());
	
	private static boolean loadInicio = false;

	
	
	private static String getVentanaBusquedaChar(L2PcInstance player,String palabra, int pagina){
		HashMap<String,HashMap<String,String>> infoP = new  HashMap<String, HashMap<String,String>>();
		int contador = 0;
		Iterator itr = general.getCharInfo().entrySet().iterator();
		
		int maximo = 9;
		
		while(itr.hasNext()){
			Map.Entry Info =(Map.Entry)itr.next();
			String Nombre = (String) Info.getKey();
			if(Nombre.toUpperCase().indexOf(palabra.toUpperCase())>=0){
				infoP.put( Nombre , new HashMap<String, String>());
				HashMap<String,String>t1 = (HashMap<String, String>) Info.getValue(); 
				infoP.get(Nombre).putAll(t1);
				contador++;
				if(contador>maximo){
					central.msgbox("You need to be more specific", player);
					return "";
				}				
			}
		}
		
		String retorno = "";
		contador=0;
		
		String Bypass = "bypass "+general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.pvppklog.name()+";LoadLog;%IDCHAR%;0;0;0;0";
		
		itr = infoP.entrySet().iterator();
		
		
		while(itr.hasNext()){
			Map.Entry Info =(Map.Entry)itr.next();
			HashMap<String,String>t1 = (HashMap<String, String>) Info.getValue(); 
			if(contador==0){
				retorno +="<img src=\"L2UI.SquareGray\" width=744 height=2><table width=720><tr>";
			}
			
			retorno += "<td fixwidth=240><table width=240 bgcolor=\"033C70\" height=25 cellspacing=0 cellpadding=0><tr><td>"+
			"<br><font name=\"hs12\"> "+ t1.get("NOM") + "</font><br></td></tr></table><table width=240 bgcolor=03213D height=25 cellspacing=0 cellpadding=0><tr><td width=32> "+
            cbFormato.getBotonForm(general.classData.get( Integer.valueOf(t1.get("BASE")) ).get("IMAGEN"), Bypass.replace("%IDCHAR%", t1.get("ID"))) +  "<br></td><td width=192><table><tr>"+
			"<td fixwidth=192 align=LEFT>"+ opera.getClassName(Integer.valueOf(t1.get("BASE"))) +"</td></tr><tr>"+
            "<td fixwidth=192 align=LEFT><font color=FF78FB>"+ t1.get("PVP") + "</font> pvp - <font color=FF7878>" + t1.get("PK") +"</font> pk<br></td></tr></table></td></tr></table></td>";

			contador++;
			
			if(contador==3){
				retorno +="</tr></table><br><img src=\"L2UI.SquareGray\" width=744 height=2>";
				contador=0;
			}
		}
		
		if(contador>0 && contador <3){
			for(int i=contador;i<3;i++){
				retorno += "<td fixwidth=240></td>";
			}
			retorno += "</tr></table><br><img src=\"L2UI.SquareGray\" width=744 height=2>";
		}
		return retorno;
	}
	
	private static String getLog(String CharID, int Pagina){
		String retorno ="<img src=\"L2UI.SquareGray\" width=\"744\" height=2><br><table width=200 cellspacing=-1 cellpadding=-1><tr>"+
           "<td><button value=\"\" action=\"\" width=32 height=16 back=\"L2UI_CH3.HorizontalSliderBarCenter\" fore=\"L2UI_CH3.HorizontalSliderBarCenter\"></td>"+
           "<td><button value=\"Winner\" action=\"\" width=250 height=16 back=\"L2UI_CH3.HorizontalSliderBarCenter\" fore=\"L2UI_CH3.HorizontalSliderBarCenter\"></td>"+
           "<td><button value=\"\" action=\"\" width=4 height=16 back=\"L2UI_CH3.HorizontalSliderBarRigh\" fore=\"L2UI_CH3.HorizontalSliderBarRigh\"></td>"+
           "<td><button value=\"Type\" action=\"\" width=60 height=16 back=\"L2UI_CH3.HorizontalSliderBarCenter\" fore=\"L2UI_CH3.HorizontalSliderBarCenter\"></td>"+
           "<td><button value=\"\" action=\"\" width=4 height=16 back=\"L2UI_CH3.HorizontalSliderBarRigh\" fore=\"L2UI_CH3.HorizontalSliderBarRigh\"></td>"+
           "<td><button value= \"\" action=\"\" width=32 height=16 back=\"L2UI_CH3.HorizontalSliderBarCenter\" fore=\"L2UI_CH3.HorizontalSliderBarCenter\"></td>"+
           "<td><button value=\"Looser\" action=\"\" width=250 height=16 back=\"L2UI_CH3.HorizontalSliderBarCenter\" fore=\"L2UI_CH3.HorizontalSliderBarCenter\"></td>"+
           "<td><button value=\"\" action=\"\" width=4 height=16 back=\"L2UI_CH3.HorizontalSliderBarRigh\" fore=\"L2UI_CH3.HorizontalSliderBarRigh\"></td>"+
           "<td><button value=\"Count\" action=\"\" width=60 height=16 back=\"L2UI_CH3.HorizontalSliderBarCenter\" fore=\"L2UI_CH3.HorizontalSliderBarCenter\"></td>"+
       "</tr></table>";
		

		int contador = 0;
		int Maximo = 12;
		boolean continua = false;
		
		String ByPass = "";
		
		String []Botones = {"1B2723","151E1B"};
		
		String Botonera = "<table width=670 cellspacing=0 cellpadding=0 border=0 bgcolor=%BGCOLOR%  height=38><tr>"+
           "<td fixwidth=32>%WIN_ICONO%<br></td>"+
           "<td fixwidth=265 align=CENTER>%WIN_NOMBRE%</td>"+
           "<td fixwidth=58 align=CENTER>%TIPO%</td>"+
           "<td fixwidth=32>%LOO_ICONO%<br></td>"+
           "<td fixwidth=265 align=CENTER> %LOO_NOMBRE%  </td>"+
           "<td fixwidth=60 align=CENTER> %VECES% </td></tr></table>";
		
		Connection conn = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			String qry = "call sp_lista_log_pvp(2,?," + String.valueOf(Maximo + 1) + "," + String.valueOf( Pagina * Maximo ) + ")";
			CallableStatement psqry = conn.prepareCall(qry);
			psqry.setString(1,CharID);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				if (!rss.getString(1).equals("err")){
					if(!rss.getString(5).equalsIgnoreCase("null") && !rss.getString(6).equalsIgnoreCase("null")){
						if(contador < Maximo){
							String IconoWin = general.classData.get(rss.getInt(5)).get("IMAGEN");
							String IconoLoo = general.classData.get(rss.getInt(6)).get("IMAGEN");
							String NomWin = rss.getString(1);
							String NomLoo = rss.getString(2);
							
							String IDWin = general.getCharInfo().get(NomWin).get("ID");
							String IDLoo = general.getCharInfo().get(NomLoo).get("ID");
							
							String ByPassWin = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+ ";" + Engine.enumBypass.pvppklog.name()+";LoadLog;" + IDWin + ";0;0;0;0";
							String BypassLoo = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+ ";" + Engine.enumBypass.pvppklog.name()+";LoadLog;" + IDLoo + ";0;0;0;0";
							
							String GetNameID = general.getIdToCharName().get(Integer.valueOf(CharID));
							
							NomWin = (NomWin.equals(GetNameID) ? "<font color=01A9DB>"+NomWin+"</font>" : NomWin);
							NomLoo = (NomLoo.equals(GetNameID) ? "<font color=01A9DB>"+NomLoo+"</font>" : NomLoo);
							
							String BaseWin = "<font color=585858>"+opera.getClassName(rss.getInt(5)) + "</font>";
							String BaseLooser = "<font color=585858>"+opera.getClassName(rss.getInt(6)) + "</font>";
							
							String Tipo = rss.getString(4);
							String Veces = String.valueOf(rss.getInt(3));
							retorno += Botonera.replace("%BGCOLOR%", Botones[contador % 2]).replace("%WIN_ICONO%", cbFormato.getBotonForm(IconoWin, ByPassWin) ).replace("%WIN_NOMBRE%", NomWin+"<br1>" + BaseWin).replace("%TIPO%", Tipo)
									.replace("%LOO_ICONO%", cbFormato.getBotonForm(IconoLoo, BypassLoo)).replace("%LOO_NOMBRE%", NomLoo + "<br1>" + BaseLooser).replace("%VECES%", Veces);
						}else{
							continua=true;							
						}
					}
					contador++;
				}
			}
		}
		catch (SQLException e){

		}
		try{
			conn.close();
		}catch(SQLException e){

		}		

		if(continua || Pagina>0 ){
			
			String bypassAntes = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.pvppklog.name() + ";LoadLog;" + String.valueOf(CharID) + ";" +String.valueOf(Pagina-1)+";0;0;0"; 
			String bypassProx = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.pvppklog.name() + ";LoadLog;" + String.valueOf(CharID) + ";" +String.valueOf(Pagina+1)+";0;0;0";
			
			String btnAntes = "<button  action=\""+ bypassAntes +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\">";
			String btnProx = "<button  action=\""+ bypassProx +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\">";				
			
			String Grilla = "<center><table fixwith=767 cellspacing=-4 cellpadding=-1 height=38 bgcolor=0E180B><tr><td fixwidth=767 align=CENTER><br><table with=96>"+
            "<tr><td width=32>"+ ( Pagina>0 ? btnAntes : "") +"</td>"+
                "<td width=32 align=CENTER><font name=\"hs12\">"+ String.valueOf(Pagina+1) +"</font></td>"+
                "<td width=32>"+ (continua ? btnProx : "") +"</td></tr></table></td></tr></table></center>";
			
			retorno += Grilla;
		}
		
		
		retorno += "<img src=\"L2UI.SquareGray\" width=744 height=2><br>";
		
		
		
		return retorno;
	}
	
	private static String mainHtml(L2PcInstance player, String Params,String Buscar, int pagina){
		String par = Engine.enumBypass.pvppklog.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
		String Bypass = "bypass -h voice .Z_DS_pvppkLoG $txtName";
		
		retorno += "<table width=350 border=0><tr><td width=32><img src=\"icon.skill0817\" width=32 height=32></td><td width=310><table width=310>"+
        "<tr><td><font name=\"hs12\" color=\"FA8258\">Player Log Fights Search</font></td></tr><tr><td><font>Enter the player name to check their log fights</font></td></tr></table></td><td><table><tr>"+
        "<td><edit var=\"txtName\" width=150></td></tr><tr><td>"+
        "<button value=\"Search\" action=\""+ Bypass +"\" width=55 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"+
        "</td></tr></table></td></tr></table>";
		if(Params.equals("0")){
			
		}else if(Params.equals("findppl")){
			retorno += getVentanaBusquedaChar(player,Buscar,pagina);
		}else if(Params.equals("LoadLog")){
			retorno += getLog(Buscar, pagina);
		}
		retorno += "</body></html>";
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
			return mainHtml(player,parm1,"", Integer.valueOf(parm2));
		}else if(parm1.equals("findppl")){
			return mainHtml(player,parm1,parm2, Integer.valueOf(parm3));
		}else if(parm1.equals("LoadLog")){
			return mainHtml(player,parm1,parm2, Integer.valueOf(parm3));
		}
		return "";
	}
	
	public static void bypassVoice(L2PcInstance player, String Buscar){
		String enviar = "";
		enviar = mainHtml(player, "findppl",Buscar, 0);
		cbManager.separateAndSend(enviar, player);
	}
}
