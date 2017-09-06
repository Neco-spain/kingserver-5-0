package ZeuS.Comunidad.EngineForm;

import java.util.List;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.L2Henna;
import com.l2jserver.gameserver.network.serverpackets.HennaEquipList;
import com.l2jserver.gameserver.network.serverpackets.HennaRemoveList;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;

public class v_symbolMaker {
	private static String mainHtml(L2PcInstance player, String Params){
		String par = Engine.enumBypass.Symbolmaker.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		
		String bypass_Draw = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.Symbolmaker.name() + ";draw;0;0;0;0;0";
		String bypass_Delete = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.Symbolmaker.name() + ";delete;0;0;0;0;0";
		
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
		retorno += "<table width=760 background=L2UI_CT1.Windows_DF_Drawer_Bg><tr><td></td></tr><tr><td width=760 align=center ><center>";
		
		retorno += cbFormato.getBarra1("icon.etc_dex_symbol_i01", "Draw a Symbol", "Draw your a specific Symbol (DYES)", bypass_Draw, "Draw a Symbol") + "<br><br><br>";
		
		retorno += cbFormato.getBarra1("icon.etc_str_symbol_i01", "Delete a Symbol", "Remove the Symbol (DYE)", bypass_Delete, "Delete a Symbol") + "<br><br>";
		
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
		}else if(parm1.equals("draw")){
			/*List<L2Henna> tato = HennaData.getInstance().getHennaList(player.getClassId());
			player.sendPacket(new HennaEquipList(player, tato));
			*/
			HennaEquipList hel = new HennaEquipList(player);
			player.sendPacket(hel);			
		}else if(parm1.equals("delete")){
			boolean hasHennas = false;
			int i=0;
			int num = 0;
			if(num != 0) {
				player.removeHenna(num);
			}
			while(i <= 2){
				i++;
				L2Henna henna = player.getHenna(i);
				if(henna != null) {
					hasHennas = true;
				}
			}
			if (hasHennas)
			{
				player.sendPacket(new HennaRemoveList(player));
			}
			else
			{
				player.sendMessage("You do not have dyes.");
			}			
		}
		return "";
	}
}
