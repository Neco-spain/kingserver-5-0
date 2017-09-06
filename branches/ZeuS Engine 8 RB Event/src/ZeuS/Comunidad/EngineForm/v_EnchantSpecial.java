package ZeuS.Comunidad.EngineForm;

import java.util.Vector;

import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.interfase.special_enchant;
import ZeuS.procedimientos.opera;
public class v_EnchantSpecial extends special_enchant{
	
	private static String getObject(L2PcInstance player){
		String retorno = "<table bgcolor=1D1D1D>";
		int[] VectorLocaciones = {
			Inventory.PAPERDOLL_HEAD,
			Inventory.PAPERDOLL_CHEST,
			Inventory.PAPERDOLL_LEGS,
			Inventory.PAPERDOLL_GLOVES,
			Inventory.PAPERDOLL_FEET,
			Inventory.PAPERDOLL_RHAND,
			Inventory.PAPERDOLL_LHAND,
			Inventory.PAPERDOLL_UNDER,
			Inventory.PAPERDOLL_BELT,
			Inventory.PAPERDOLL_LEAR,
			Inventory.PAPERDOLL_REAR,
			Inventory.PAPERDOLL_LFINGER,
			Inventory.PAPERDOLL_RFINGER,
			Inventory.PAPERDOLL_NECK,
			Inventory.PAPERDOLL_LBRACELET};
		
		
		//tipo lugar
		String Bypass = "bypass -h voice .ZeOcCto " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.SelectEnchant.name() + ";Enchant;%POSI%;%IDITEM%;0;0;0";
		
		int Contador = 0;
		
		String NOITEM = ItemTable.getInstance().getTemplate(3883).getIcon();
		
		for(int Info : VectorLocaciones){
			if(Contador ==0){
				retorno += "<tr>";
			}
			
			if (player.getInventory().getPaperdollItem(Info)!=null) {
				String ImagenItem = player.getInventory().getPaperdollItem(Info).getItem().getIcon();
				String NombreItem = central.getNombreITEMbyID(player.getInventory().getPaperdollItem(Info).getItem().getId());
				L2ItemInstance itemSelecc = player.getInventory().getPaperdollItem(Info);
				String IDObjeto = String.valueOf(itemSelecc.getObjectId()); 
				//VectorLink.add("1;"+String.valueOf(Info)+";"+st.getInventory().getPaperdollItem(Info).getItem().getIcon());
				//%POSI%;%IDITEM%
				retorno += "<td fixwidth=166 align=CENTER><br>"+ cbFormato.getBotonForm(ImagenItem, Bypass.replace("%POSI%", String.valueOf(Info)).replace("%IDITEM%", IDObjeto) ) +"<br>"+ NombreItem +"<br></td>";				
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
		Reque.add("Just Noble:," + (general.ENCHANT_NOBLE ? "Yes" : "No"));
		Reque.add("Lv to Use:," + String.valueOf(general.ENCHANT_LVL ));
		Reque.add("Min. Enchant:," + String.valueOf(general.ENCHANT_MIN_ENCHANT));
		Reque.add("Max. Enchant:," + String.valueOf(general.ENCHANT_MAX_ENCHANT));
		Reque.add("Enchant plus:," + String.valueOf(general.ENCHANT_x_VEZ));
		
		String CostWindows = cbFormato.getCostOfService(general.ENCHANT_ITEM_PRICE);
		String Requerimientos = cbFormato.getRequirements(Reque);
		
		String ByPassRefresh = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.SelectEnchant.name() + ";0;0;0;0;0;0";
		
		String retorno = "<center><table width=720 height=200><tr><td width=220><table width=220 background=L2UI_CT1.Windows_DF_Drawer_Bg><tr><td>"+
		CostWindows + Requerimientos + "</td></tr></table></td><td width=500><table width=500 fixwith=220 background=L2UI_CT1.Windows_DF_Drawer_Bg cellspacing=1 cellpadding=5><tr><td fixwdith=500 align=CENTER>"+
		getObject(player) + "</td></tr><tr><td fixwidth=500 align=CENTER><center><br>"+
		"<button value=\"Refresh\" action=\""+ ByPassRefresh +"\" width=138 height=32 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center></td></tr>"+
		"</table></td></tr></table></center><br>"; 
		
		return  retorno;
	}	
	
	
	
	
	private static String mainHtml(L2PcInstance player, String Params){
		String par = Engine.enumBypass.SelectEnchant.name();
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
		}else if(parm1.equals("Enchant")){
			//posicion del item //  ID del Objeto
			//%POSI%;%IDITEM%
			if(AplicarEnchantItem(player,parm2.trim(),parm3.trim() )){
				opera.removeItem(general.ENCHANT_ITEM_PRICE, player);
			}else{
				central.msgbox("Enchant Error.", player);
			}
			return mainHtml(player,params);
		}
		return "";
	}
}
