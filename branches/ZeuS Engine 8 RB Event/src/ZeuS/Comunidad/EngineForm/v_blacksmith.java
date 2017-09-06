package ZeuS.Comunidad.EngineForm;

import java.util.Vector;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;

public class v_blacksmith extends v_Shop{
	
	private static String getMainWindows(L2PcInstance player){
		Vector<String>Opciones = new Vector<String>();
		
		Opciones.add("icon.etc_ancient_adena_i00:Exchange:Trade Ancient Adena for Mammon Goods:311132501:multi");
		Opciones.add("icon.etc_old_evil_book_i00:Exchange:Trade Ancient Tomes of the Demon:323470002:multi");
		Opciones.add("icon.etc_seed_of_evil_i03:Exchange:Trade Seeds of Evil for Dynasty Pieces:350980012:multi");
		Opciones.add("icon.etc_broken_crystal_red_i00:Exchange:Trade Cursed Soul Crystal:323470001:multi");
		Opciones.add("icon.etc_mineral_general_i00:Exchange:Create Accesory Life Stone:12754:multi");
		Opciones.add("icon.etc_blacksmiths_frame_i00:Exchange:Create Crafting Materials:1004:multi");
		Opciones.add("icon.weapon_dual_sword_i00:Dual Sword:Create or Disassembly dual Swords:20150001:exec");
		Opciones.add("icon.armor_t96_u_i02:Release Seal:Exchange Sealed Item:20150002:exec");
		Opciones.add("icon.energy_condenser_i01:Upgrade Item:Rare, Vesper Noble, Dynasty Upgrade:20150003:exec");
		Opciones.add("icon.etc_armor_soul_i00:Downgrade Item:Remove Masterwork or Dynasty Upgrades:20150004:exec");
		Opciones.add("icon.weapon_incessantcore_sword_i00:Add Special Ability:Add SA to a Weapon with Soul Crystal:20150005:exec");
		Opciones.add("icon.weapon_incessantcore_sword_i01:Remove Special Ability:Remove SA of you weapon for free:20150006:exec");
		
		String ByPassMultisell = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.blacksmith + ";multi_show;%ID%;0;0;0;0";
		String ByPassExe = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.blacksmith + ";multiexc_show;%ID%;0;0;0;0";
		
		int Contador = 0;
		
		String tdBotonera = "<td fixwidth=380><table fixwidth=360 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=32>"+
			"%BOTON%<br><br></td><td fixwidth=328><table width=328><tr><td fixwidth=328>"+
			"<font name=hs12 color=87AFFF>%NOMBRE%</font></td></tr><tr><td fixwidth=328>"+
			"<font color=8A8A8A>%EXPLICA%</font></td></tr></table></td></tr></table></td>";
		
		
		String Retorno = "<center><table width=760 height=200>";
		for(String strLinea : Opciones){
			if(Contador==0){
				Retorno += "<tr>";
			}
			/*0=Imagen,1=Nombre,2=Explica,3=ID,4=Tipo*/
			Retorno += tdBotonera.replace("%EXPLICA%", strLinea.split(":")[2]) .replace("%NOMBRE%", strLinea.split(":")[1]) .replace("%BOTON%", cbFormato.getBotonForm(strLinea.split(":")[0], strLinea.split(":")[4].equals("multi") ? ByPassMultisell.replace("%ID%", strLinea.split(":")[3]) : ByPassExe.replace("%ID%", strLinea.split(":")[3])  ));

			Contador++;
			
			if(Contador>=2){
				Retorno += "</tr>";
				Contador=0;
			}
			
		}
		
		if(Contador==1){
			Retorno += "<td fixwidth=380></td></tr>";
		}

		Retorno += "</table></center><br><img src=\"L2UI.SquareGray\" width=768 height=2>";
		
		return Retorno;
	}
	
	private static String mainHtml(L2PcInstance player, String Params){
		String par = Engine.enumBypass.blacksmith.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
		retorno += getMainWindows(player);
		
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
		}else if(parm1.equals("multi_show")){
			showMultisell(Integer.valueOf(parm2),player,true);
		}else if(parm1.equals("multiexc_show")){
			showMultisell(Integer.valueOf(parm2),player,false);
		}
		return "";
	}
}
