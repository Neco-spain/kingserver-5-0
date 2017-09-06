package ZeuS.Comunidad.EngineForm;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ExShowVariationCancelWindow;
import com.l2jserver.gameserver.network.serverpackets.ExShowVariationMakeWindow;
import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;

public class v_augmentManager {
	private static String mainHtml(L2PcInstance player, String Params){
		String par = Engine.enumBypass.AugmentManager.name();
		String retorno = "<html><title>Augment Manager System</title><body><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
		String bypass_Draw = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + par + ";aug;0;0;0;0;0";
		String bypass_Delete = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + par + ";deaug;0;0;0;0;0";
		
		retorno += "<table width=760 background=L2UI_CT1.Windows_DF_Drawer_Bg><tr><td></td></tr><tr><td width=760 align=center ><center>";
		
		retorno += cbFormato.getBarra1("icon.skill3631", "Augment Item", "Add more Power and become Stronger", bypass_Draw, "Augment Item") + "<br><br><br>";
		
		retorno += cbFormato.getBarra1("icon.skill3630", "Remove Augment", "Remove augment", bypass_Delete, "Remove Augment") + "<br><br>";
		
		retorno += "</td></tr></table></center>";		
		
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
			return mainHtml(player,params);
		}else if(parm1.equals("aug")){
			player.sendPacket(new ExShowVariationMakeWindow());
		}else if(parm1.equals("deaug")){
			player.sendPacket(new ExShowVariationCancelWindow());
		}
		return "";
	}
}
