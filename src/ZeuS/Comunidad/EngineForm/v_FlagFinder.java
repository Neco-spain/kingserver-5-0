package ZeuS.Comunidad.EngineForm;

import java.util.Vector;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.interfase.central;
import ZeuS.interfase.flagFinder;
import ZeuS.procedimientos.opera;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class v_FlagFinder extends flagFinder{
	private static String mainHtml(L2PcInstance player, String Params, String BypassAnterior){
		String par = Engine.enumBypass.Flagfinder.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		/*if(Params.equals("FromMain")){
			retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,false,BypassAnterior);
		}else{
			retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica);
		}*/
		
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
		retorno += "<center><table width=720 height=200><tr><td width=220><table width=220 background=L2UI_CT1.Windows_DF_Drawer_Bg><tr><td>";
		
		retorno += cbFormato.getCostOfService(general.FLAG_FINDER_PRICE);
		
		Vector<String>Requerimientos = new Vector<String>();
		
		Requerimientos.add("For Nobles Only," + String.valueOf(general.FLAG_FINDER_CAN_NOBLE));
		Requerimientos.add("Minimun Level to Use," + String.valueOf(general.FLAG_FINDER_LVL));
		Requerimientos.add("Minimun Level of victim," + String.valueOf(general.FLAG_PVP_PK_LVL_MIN));
		Requerimientos.add("Minimun PvP/PK of victim," + String.valueOf(general.FLAG_FINDER_MIN_PVP_FROM_TARGET));
		Requerimientos.add("Use in Flag mode," + String.valueOf(general.FLAG_FINDER_CAN_USE_FLAG));
		Requerimientos.add("Use in Pk mode," + String.valueOf(general.FLAG_FINDER_CAN_USE_PK));
		Requerimientos.add("Pk Pririty," + String.valueOf(general.FLAG_FINDER_PK_PRIORITY));

		retorno += cbFormato.getRequirements(Requerimientos);

		String ByPass = general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";Flagfinder;go;0;0;0;0;0";
		
		retorno += "</td></tr></table></td><td width=500><table width=500 fixwith=220 height=200 background=L2UI_CT1.Windows_DF_Drawer_Bg cellspacing=1 cellpadding=5><tr>"+
		"<td><center><font name=\"hs12\" color=\"FF9900\">"+ msg.FLAG_FINDER_MENSAJE +"</font></center></td></tr><tr><td>"+
        "<center><button value=\""+ msg.FLAG_FINDER_BTN_MENSAJE +"\" action=\"bypass "+ ByPass +"\" width=235 height=40 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>"+
        "</td></tr></table></td></tr></table></center><br><img src=\"L2UI.SquareGray\" width=768 height=2>";
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
		if(parm1.equals("0")){
			if(!general.BTN_SHOW_FLAGFINDER_CBE){
				central.msgbox(msg.DISABLED_BY_ADMIN, player);
				return "";
			}
			return mainHtml(player,parm1,"");
		}else if(parm1.equals("go")){
			if(opera.canUseCBFunction(player)){
				if(canuseFlagFinder(player)){
					if(goFlagFinder(player)){
						opera.removeItem(general.FLAG_FINDER_PRICE, player);
						cbFormato.cerrarCB(player);
					}
				}
			}
		}else if(parm1.equals("FromMain")){
			if(!general.BTN_SHOW_FLAGFINDER_CBE){
				central.msgbox(msg.DISABLED_BY_ADMIN, player);
				return "";
			}			
			return mainHtml(player,parm1,"bypass " + general.COMMUNITY_BOARD_PART_EXEC);
		}
		return "";
	}
}
