package ZeuS.Comunidad.EngineForm;

import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Comunidad.CBByPass;
import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.interfase.central;
import ZeuS.interfase.partyFinder;
import ZeuS.procedimientos.opera;

public class v_PartyFinder extends partyFinder{
	private static final Logger _log = Logger.getLogger(v_PartyFinder.class.getName());
	
	private static String mainHtml(L2PcInstance player, String params, String ByPassAnterior){
		String par = Engine.enumBypass.Gopartyleader.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		/*
		if(params.equals("FromMain")){
			retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,false,ByPassAnterior);
		}else{
			retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica);
		}*/
		
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
		retorno += "<center><table width=720 height=200><tr><td width=220><table width=220 background=L2UI_CT1.Windows_DF_Drawer_Bg><tr><td>";
		
		retorno += cbFormato.getCostOfService(general.PARTY_FINDER_PRICE);
		
		Vector<String>Requerimientos = new Vector<String>();
		
		Requerimientos.add("For Nobles Only," + String.valueOf(general.PARTY_FINDER_CAN_USE_ONLY_NOBLE));
		Requerimientos.add("Minimun Level to Use," + String.valueOf(general.PARTY_FINDER_CAN_USE_LVL));
		Requerimientos.add("Use in Flag mode," + String.valueOf(general.PARTY_FINDER_CAN_USE_FLAG));
		Requerimientos.add("Party Leader Noble," + String.valueOf(general.PARTY_FINDER_GO_LEADER_NOBLE));
		Requerimientos.add("Party Leader Flag/PK," + String.valueOf(general.PARTY_FINDER_GO_LEADER_FLAGPK));

		retorno += cbFormato.getRequirements(Requerimientos);

		String ByPass = general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";Gopartyleader;go;0;0;0;0;0";
		
		retorno += "</td></tr></table></td><td width=500><table width=500 fixwith=220 height=200 background=L2UI_CT1.Windows_DF_Drawer_Bg cellspacing=1 cellpadding=5><tr>"+
		"<td><center><font name=\"hs12\" color=\"FF9900\">"+ msg.PARTY_FINDER_MENSAJE_CENTRAL +"</font></center></td></tr><tr><td>"+
        "<center><button value=\"Take me to my party leader\" action=\"bypass "+ ByPass +"\" width=195 height=40 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>"+
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
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			if(!general.BTN_SHOW_PARTYFINDER_CBE){
				central.msgbox(msg.DISABLED_BY_ADMIN, player);
				return "";
			}
			return mainHtml(player,parm1,"");
		}else if(parm1.equals("go")){
			if(opera.canUseCBFunction(player)){
				if(canUse(player)){
					opera.removeItem(general.PARTY_FINDER_PRICE, player);
					goPartyLeader(player);
					cbFormato.cerrarCB(player);
				}
			}
		}else if(parm1.equals("FromMain")){
			if(!general.BTN_SHOW_PARTYFINDER_CBE){
				central.msgbox(msg.DISABLED_BY_ADMIN, player);
				return "";
			}			
			return mainHtml(player,parm1,"bypass " + general.COMMUNITY_BOARD_PART_EXEC);
		}
		return "";
	}
	
}
