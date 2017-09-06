package ZeuS.Comunidad.EngineForm;

import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;

import ZeuS.Comunidad.CBByPass;
import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.interfase.special_elemental;
import ZeuS.procedimientos.opera;

public class v_ElementalSpecial extends special_elemental{
	
	private static final Logger _log = Logger.getLogger(v_ElementalSpecial.class.getName());
	
	private static String getObject(L2PcInstance player){
		String retorno = "<table bgcolor=1D1D1D>";
		int[] VectorLocaciones = {Inventory.PAPERDOLL_HEAD, Inventory.PAPERDOLL_CHEST, Inventory.PAPERDOLL_LEGS, Inventory.PAPERDOLL_GLOVES, Inventory.PAPERDOLL_FEET, Inventory.PAPERDOLL_RHAND };
		
		
		/**if(params.split(" ")[0].equals(Engine.enumBypass.SelectElemental.name())){
								String Retorno = v_ElementalSpecial.bypass(activeChar, params);*/
		
		//tipo lugar
		String Bypass = "bypass -h voice .ZeOcCto " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.SelectElemental + ";Elemental;%TIPO%;%LUGAR%;0;0;0";
		
		int Contador = 0;
		
		String NOITEM = ItemTable.getInstance().getTemplate(3883).getIcon();
		
		for(int Info : VectorLocaciones){
			if(Contador ==0){
				retorno += "<tr>";
			}
			
			if (player.getInventory().getPaperdollItem(Info)!=null) {
				String ImagenItem = player.getInventory().getPaperdollItem(Info).getItem().getIcon();
				String NombreItem = central.getNombreITEMbyID(player.getInventory().getPaperdollItem(Info).getItem().getId());
				//VectorLink.add("1;"+String.valueOf(Info)+";"+st.getInventory().getPaperdollItem(Info).getItem().getIcon());
				retorno += "<td fixwidth=166 align=CENTER><br>"+ cbFormato.getBotonForm(ImagenItem, Bypass.replace("%TIPO%", " $cmbElemental ").replace("%LUGAR%", String.valueOf(Info)) ) +"<br>"+ NombreItem +"<br></td>";				
			}else{
				retorno += "<td fixwidth=166 align=CENTER><br><img src=\""+ NOITEM +"\" width=32 height=32><br>NO ITEM<br></td>";
			}
			
			Contador++;
			
			if(Contador == 3){
				retorno += "</tr>";
				Contador=0;
			}
		}
		
		retorno += "</table>";
		
		return retorno;
	}
	
	private static String getMainWindows(L2PcInstance player){
		
		Vector<String> Reque = new Vector<String>();
		Reque.add("Just Noble:," + (general.ELEMENTAL_NOBLE ? "Yes" : "No"));
		Reque.add("Lv to Use:," + String.valueOf(general.ELEMENTAL_LVL));
		Reque.add("Max Armor Power:," + String.valueOf(general.ELEMENTAL_LVL_ENCHANT_MAX_ARMOR));
		Reque.add("Max Weapon Power:," + String.valueOf(general.ELEMENTAL_LVL_ENCHANT_MAX_WEAPON));
		
		String CostWindows = cbFormato.getCostOfService(general.ELEMENTAL_ITEM_PRICE);
		String Requerimientos = cbFormato.getRequirements(Reque);
		
		String ByPassRefresh = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.SelectElemental.name() + ";0;0;0;0;0;0";
		
		String retorno = "<center><table width=720 height=200><tr><td width=220><table width=220 background=L2UI_CT1.Windows_DF_Drawer_Bg><tr><td>"+
		CostWindows + Requerimientos + "</td></tr></table></td><td width=500><table width=500 fixwith=220 background=L2UI_CT1.Windows_DF_Drawer_Bg cellspacing=1 cellpadding=5><tr><td fixwidth=500 align=CENTER>"+
		"<br><font name=\"hs12\" color=\"FF9900\">Elemental Type</font><br1>"+
		"<combobox width=105 var=\"cmbElemental\" list=\"Fire;Water;Wind;Earth;Dark;Holy\"></td></tr><tr><td fixwdith=500 align=CENTER>"+
		getObject(player) + "</td></tr><tr><td fixwidth=500 align=CENTER><center><br>"+
		"<button value=\"Refresh\" action=\""+ ByPassRefresh +"\" width=138 height=32 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center></td></tr>"+
		"</table></td></tr></table></center><br>"; 
		
		return  retorno;
	}
	
	private static String mainHtml(L2PcInstance player, String Params){
		String par = Engine.enumBypass.SelectElemental.name();
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
		}else if(parm1.equals("Elemental")){
			if(AplicarElemental(player,parm2.trim(),parm3.trim() )){
				opera.removeItem(general.ELEMENTAL_ITEM_PRICE, player);
			}else{
				central.msgbox("Elemental Error.", player);
			}
			return mainHtml(player,params);
		}
		return "";
	}
}
