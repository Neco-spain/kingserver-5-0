package ZeuS.interfase;

import java.util.Arrays;
import java.util.Vector;

import com.l2jserver.Config;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.enums.ItemLocation;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.model.items.L2Item;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.items.type.WeaponType;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.procedimientos.opera;

public class special_enchant {
	public static boolean AplicarEnchantItem(L2PcInstance st,String PosicionDelItem, String IDOBJETO_del_Item){
		if(!general._activated()){
			return false;
		}
		int IDPosiInventario = Integer.valueOf(PosicionDelItem);
		int IDObjetoInventario = Integer.valueOf(IDOBJETO_del_Item);

		if (st.getInventory().getPaperdollItem(IDPosiInventario).equals(null)){
			central.msgbox("You have removed the item from the position that was",st);
			//return mainHtmlEnchantItem(st)
			return false;
		}

		L2ItemInstance itemSelecc = st.getInventory().getPaperdollItem(IDPosiInventario);
		int IDObjeto = itemSelecc.getObjectId();

		if (IDObjeto != IDObjetoInventario){
			central.msgbox("The Selected Item is not for the Current position in your inventory",st);
			return false;
		}

		L2ItemInstance Item = st.getInventory().getItemByObjectId(IDObjetoInventario);

		if (general.ENCHANT_NOBLE && !st.isNoble()){
			central.msgbox(msg.NECESITAS_SER_NOBLE,st);
			return false;
		}

		if(st.getLevel() < general.ENCHANT_LVL){
			central.msgbox(msg.NECESITAS_TENER_UN_LEVEL_MAYOR_A_$level.replace("$level",  String.valueOf(general.ENCHANT_LVL)),st);
			return false;
		}

		if((Item.getEnchantLevel() < general.ENCHANT_MIN_ENCHANT) || (Item.getEnchantLevel() >= general.ENCHANT_MAX_ENCHANT)){
			central.msgbox("Sorry, but your "+ Item.getItem().getName() +" does not meet the parameters to be Enchanted. The item must have a enchant between "+String.valueOf(general.ENCHANT_MIN_ENCHANT)+ " and " + String.valueOf(general.ENCHANT_MAX_ENCHANT),st);
			return false;
		}

		int IntEnchant = Item.getEnchantLevel() + general.ENCHANT_x_VEZ;

		if(IntEnchant > general.ENCHANT_MAX_ENCHANT){
			Item.setEnchantLevel(general.ENCHANT_MAX_ENCHANT);
			IntEnchant = general.ENCHANT_MAX_ENCHANT;
		}else{
			Item.setEnchantLevel(IntEnchant);
		}
		st.broadcastUserInfo();
		st.getInventory().reloadEquippedItems();
		central.msgbox("Congratulations, your "+Item.getItem().getName()+" has a new enchant of "+String.valueOf(IntEnchant),st);
		return true;
	}

	@SuppressWarnings("unused")
	
	public static void setWeaponEnchant(int Enchant, L2PcInstance Player){
		int[] VectorLocaciones = {Inventory.PAPERDOLL_RHAND, Inventory.PAPERDOLL_LHAND};
		for(int posicion  : VectorLocaciones){
			try{
				L2ItemInstance itemSelecc = Player.getInventory().getPaperdollItem(posicion);
				itemSelecc.setEnchantLevel(Enchant);
			}catch(Exception a){
				central.msgbox(a.getMessage(), Player);
			}
		}
		try{
			Player.broadcastInfo();
			Player.broadcastUserInfo();
		}catch(Exception a){
			
		}
		
	}
	
	public static void setAllArmorEnchant(int Enchant, L2PcInstance Player){
		int[] VectorLocaciones = {Inventory.PAPERDOLL_HEAD, Inventory.PAPERDOLL_CHEST, Inventory.PAPERDOLL_LEGS, Inventory.PAPERDOLL_GLOVES, Inventory.PAPERDOLL_FEET, Inventory.PAPERDOLL_RHAND, Inventory.PAPERDOLL_LHAND, Inventory.PAPERDOLL_UNDER, Inventory.PAPERDOLL_BELT, Inventory.PAPERDOLL_LEAR,Inventory.PAPERDOLL_REAR, Inventory.PAPERDOLL_LFINGER, Inventory.PAPERDOLL_RFINGER, Inventory.PAPERDOLL_NECK, Inventory.PAPERDOLL_LBRACELET };
		int[] VectorJoyas = {Inventory.PAPERDOLL_LEAR,Inventory.PAPERDOLL_REAR, Inventory.PAPERDOLL_LFINGER, Inventory.PAPERDOLL_RFINGER, Inventory.PAPERDOLL_NECK};
		for(int posicion  : VectorLocaciones){
			try{
				if(Player.getInventory().getPaperdollItem(posicion)!=null){
					L2ItemInstance itemSelecc = Player.getInventory().getPaperdollItem(posicion);
					itemSelecc.setEnchantLevel(Enchant);
				}
			}catch(Exception a){
				central.msgbox(a.getMessage(), Player);
			}
		}
		for(int posicion  : VectorJoyas){
			try{
				if(Player.getInventory().getPaperdollItem(posicion)!=null){
					L2ItemInstance itemSelecc = Player.getInventory().getPaperdollItem(posicion);
					itemSelecc.setEnchantLevel(Enchant);
				}
			}catch(Exception a){
				central.msgbox(a.getMessage(), Player);
			}
		}		
		try{
			Player.broadcastInfo();
			Player.broadcastUserInfo();
		}catch(Exception a){
			
		}
	}

	public static String mainHtmlEnchantItem(L2PcInstance st){
		String BOTON_REFRESH = "<button value=\"Refresh\" action=\"bypass -h ZeuSNPC ENCHANTITEM 0 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String HTML_NO_ITEM = ItemTable.getInstance().getTemplate(3883).getIcon();

		int[] VectorLocaciones = {Inventory.PAPERDOLL_HEAD, Inventory.PAPERDOLL_CHEST, Inventory.PAPERDOLL_LEGS, Inventory.PAPERDOLL_GLOVES, Inventory.PAPERDOLL_FEET, Inventory.PAPERDOLL_RHAND, Inventory.PAPERDOLL_LHAND, Inventory.PAPERDOLL_UNDER, Inventory.PAPERDOLL_BELT, Inventory.PAPERDOLL_LEAR,Inventory.PAPERDOLL_REAR, Inventory.PAPERDOLL_LFINGER, Inventory.PAPERDOLL_RFINGER, Inventory.PAPERDOLL_NECK, Inventory.PAPERDOLL_LBRACELET };
		String[] VectorNombre = {"helmet","Chest","Legs","Gloves","Choes","Weapon","Weapon","Underware","Belt","Earring","Earring","Ring","Ring","Necklace","Bracelet"};
		//String[] VectorLink = null;
		Vector<String> VectorLink = new Vector<String>();
		for(int posicion  : VectorLocaciones){
			if(st.getInventory().getPaperdollItem(posicion)!= null){
				if( isEnchantable(st.getInventory().getPaperdollItem(posicion))){
					L2ItemInstance itemSelecc = st.getInventory().getPaperdollItem(posicion);
					String IDObjeto = String.valueOf(itemSelecc.getObjectId());
					VectorLink.add("1;"+String.valueOf(posicion)+";"+st.getInventory().getPaperdollItem(posicion).getItem().getIcon()+";"+IDObjeto);
				}else{
					VectorLink.add("0;"+String.valueOf(posicion)+";"+HTML_NO_ITEM+";0");
				}
			}else{
				VectorLink.add("0;"+String.valueOf(posicion)+";"+HTML_NO_ITEM);
			}
		}
		String PARA_ITEM = "<table with=280><tr>";
		int Contador = 0;
		String ANCHO_BOTON = "88";
		for(String ItemsS : VectorLink){
			String[] VectorSplit = ItemsS.split(";");
			String BOTON;
			String IMAGEN;
			if(VectorSplit[0].equals("1")){
				BOTON = "<button value=\""+VectorNombre[Contador]+"\" action=\"bypass -h ZeuSNPC ENCHANTITEM 1 "+VectorSplit[1]+" "+VectorSplit[3]+"\" width="+ANCHO_BOTON+" height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				IMAGEN = "<img src="+VectorSplit[2]+" width=32 height=32>";
				PARA_ITEM += "<td align=CENTER>"+IMAGEN+BOTON+"</td>";
			}else{
				BOTON = "<button value=\""+VectorNombre[Contador]+"\" action=\"bypass -h ZeuSNPC ENCHANTITEM 0 0 0\" width="+ANCHO_BOTON+" height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				IMAGEN = "<img src="+VectorSplit[2]+" width=32 height=32>";
				PARA_ITEM += "<td align=CENTER>"+IMAGEN+BOTON+"</td>";
			}
			Contador++;
			if ((Contador % 3)==0){
				PARA_ITEM += "</tr><tr>";
			}
		}
		PARA_ITEM += "</table>";

		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() +"</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Items Enchantments") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(BOTON_REFRESH,"LEVEL") + central.LineaDivisora(1);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(PARA_ITEM,"LEVEL") + central.LineaDivisora(1);
		MAIN_HTML += central.ItemNeedShowBox(general.ENCHANT_ITEM_PRICE);
		MAIN_HTML += "<br1>"+central.BotonGOBACKZEUS()+central.getPieHTML()+"</center></body></html>";
		return MAIN_HTML;
	}

	protected static final boolean isEnchantable(L2ItemInstance item)
	{
		if (item.isHeroItem()) {
			return false;
		}
		if (item.isShadowItem()) {
			return false;
		}
		if (item.isCommonItem()) {
			return false;
		}
		if (item.isEtcItem()) {
			return false;
		}
		if (item.isTimeLimitedItem()) {
			return false;
		}
		// rods
		if (item.getItem().getItemType() == WeaponType.FISHINGROD){//L2WeaponType.FISHINGROD) {
			return false;
		}
		// bracelets
		if (item.getItem().getBodyPart() == L2Item.SLOT_L_BRACELET) {
			return false;
		}
		if (item.getItem().getBodyPart() == L2Item.SLOT_R_BRACELET) {
			return false;
		}
		if (item.getItem().getBodyPart() == L2Item.SLOT_BACK) {
			return false;
		}
		// blacklist check
		if (Arrays.binarySearch(Config.ENCHANT_BLACKLIST, opera.getIdItem(item)) >= 0) {
			return false;
		}
		// only items in inventory and equipped can be enchanted
		if ((item.getItemLocation() != ItemLocation.INVENTORY)
				&& (item.getItemLocation() != ItemLocation.PAPERDOLL)) {
			return false;
		}

		return true;
	}
}
