package ZeuS.Comunidad.EngineForm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Comunidad.CBByPass;
import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Comunidad.sendH;
import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.interfase.central;
import ZeuS.interfase.teleport;
import ZeuS.procedimientos.opera;

public class v_Teleport extends teleport{
	
	private static Logger _log = Logger.getLogger(v_Teleport.class.getName());
	
	private static HashMap<Integer, Boolean> _FromMain = new HashMap<Integer, Boolean>();
	
	private static String getMenuCentral(){
		
		String retorno = "<table width=220 background=L2UI_CT1.Windows_DF_Drawer_Bg><tr><td align=CENTER>";
		
		String ByPass = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";"+ Engine.enumBypass.Teleport.name() +";%TIPO%;%IDTELEPORT%;%NOMBRE%;0;0;0";
		
		String btnTeleport = "<button value=\"%NOMBRE%\" action=\""+ ByPass +"\" width=200 height=32 back=\"L2UI_CT1.OlympiadWnd_DF_Fight3None\" fore=\"L2UI_CT1.OlympiadWnd_DF_Fight3None\">";
		
		Iterator itr = general.TELEPORT_DATA.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    		HashMap<String, String>Temporal = new HashMap<String,String>();
	    		Temporal = (HashMap<String, String>) Entrada.getValue();
	    	  	if(Temporal.get(general.teleportType.ID_SECC.name()).equals("-1")){
	    	  		retorno += btnTeleport.replace("%NOMBRE%", Temporal.get(general.teleportType.NOM.name())).replace("%IDTELEPORT%", Temporal.get(general.teleportType.ID.name())).replace("%TIPO%", Temporal.get(general.teleportType.TIP.name())) ;
	    	  	}
	    }
	    
	    retorno += "</td></tr></table>";
	    
	    return retorno;
	}
	
	private static String getMainTeleportMenu(int idCategoria){
		String retorno ="";

		if(idCategoria<-100){
			return "";
		}
		
		String NombreCategoria = teleport.getNomSeccion(String.valueOf(idCategoria));
		
		retorno = "<table width=520 background=L2UI_CT1.Windows_DF_Drawer_Bg cellspacing=0 cellpadding=4><tr><td align=CENTER fixwidth=520>"+
                  "<font name=\"hs12\" color=\"CCFFCC\">"+ NombreCategoria +"</font><br></td></tr></table>";
		
		retorno += "<center><table width=500 fixwith=220 background=L2UI_CT1.Windows_DF_Drawer_Bg cellspacing=0 cellpadding=4>";
		
		
		String ByPass = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";"+ Engine.enumBypass.Teleport.name() +";%TIPO%;%IDTELEPORT%;%NOMBRE%;0;0;0";
		
		String btnTeleport = "<button action=\""+ ByPass + "\" value=\"%NOMBRE%\" width=225 height=28 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		Iterator itr = general.TELEPORT_DATA.entrySet().iterator();
		int contador = 1;
		while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    		HashMap<String, String>Temporal = new HashMap<String,String>();
	    		Temporal = (HashMap<String, String>) Entrada.getValue();
	    	  	if(Temporal.get(general.teleportType.ID_SECC.name()).equals(String.valueOf(idCategoria))){
	    	  		if(contador==1){
	    	  			retorno +="<tr>";
	    	  		}
	    	  		retorno += "<td width=225>" + btnTeleport.replace("%NOMBRE%", Temporal.get(general.teleportType.NOM.name())).replace("%IDTELEPORT%", Temporal.get(general.teleportType.ID.name())).replace("%TIPO%", Temporal.get(general.teleportType.TIP.name())) + "</td>";
	    	  		
	    	  		contador++;
	    	  		
	    	  		if(contador>=3){
	    	  			retorno += "</tr>";
	    	  			contador = 1;
	    	  		}
	    	  	}
	    }
		
		if(contador==2){
			retorno+= "<td></td></tr>";
		}
		
		retorno += "</table></center>";
		
		
		return retorno;
	}
	
	private static String mainHtml(L2PcInstance player, String Params, int idCategoria, String ByPassAnterior){
		String par = Engine.enumBypass.Teleport.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		/*if(Params.equalsIgnoreCase("FromMain") || CBByPass.IsFromMain.get(player.getObjectId()) ){
			retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,false,"bypass " + general.COMMUNITY_BOARD_PART_EXEC);
		}else{
			retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica);
		}*/
		
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
		retorno += "<center><table width=760 height=200><tr>"+
                   "<td width=220>"+ getMenuCentral() +"</td><td width=520>"+
                   getMainTeleportMenu(idCategoria)+"</td></tr></table></center><br>";
		
		
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
			if(!general.BTN_SHOW_TELEPORT_CBE){
				central.msgbox(msg.DISABLED_BY_ADMIN, player);
				return "";
			}
			return mainHtml(player,parm1,-300,"");
		}else if(parm1.equals("secc")){
			return mainHtml(player,parm1,Integer.valueOf(parm2),"");
		}else if(parm1.equals("go")){
			if(opera.canUseCBFunction(player)){
				if(goTeleportplayer(Integer.valueOf(parm2),player)) {
					cbFormato.cerrarCB(player);
				}				
			}
		}else if(parm1.equals("FromMain")){
			if(!general.BTN_SHOW_TELEPORT_CBE){
				central.msgbox(msg.DISABLED_BY_ADMIN, player);
				return "";
			}
			return mainHtml(player,parm1,-300,"");
		}
		return "";
	}
}
