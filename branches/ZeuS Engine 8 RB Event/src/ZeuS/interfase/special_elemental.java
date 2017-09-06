package ZeuS.interfase;

import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.gameserver.GameServer;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.model.Elementals;
import com.l2jserver.gameserver.model.Elementals.ElementalItems;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ExAttributeEnchantResult;
import com.l2jserver.gameserver.network.serverpackets.ExBrExtraUserInfo;
import com.l2jserver.gameserver.network.serverpackets.InventoryUpdate;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.network.serverpackets.UserInfo;
import com.l2jserver.gameserver.util.Util;

import ZeuS.Config.general;
import ZeuS.Config.msg;

public class special_elemental {

	private static final Logger _log = Logger.getLogger(GameServer.class.getName());


	private static int getPowerToAdd(int stoneId, int oldValue, L2ItemInstance item)
	{
		if (Elementals.getItemElement(stoneId) != Elementals.NONE)
		{
			if (item.isWeapon())
			{
				if (oldValue == 0)
				{
					return Elementals.FIRST_WEAPON_BONUS;
				}
				return Elementals.NEXT_WEAPON_BONUS;
			}
			else if (item.isArmor())
			{
				return Elementals.ARMOR_BONUS;
			}
		}
		return 0;
	}


	private static int getLimit(L2ItemInstance item, int sotneId)
	{
		ElementalItems elementItem = Elementals.getItemElemental(sotneId);
		if (elementItem == null)
		{
			return 0;
		}

		if (item.isWeapon())
		{
			return Elementals.WEAPON_VALUES[elementItem._type._maxLevel];
		}
		return Elementals.ARMOR_VALUES[elementItem._type._maxLevel];
	}

	
	public static boolean CanUseStoneOnThis(L2PcInstance player, int idObj, int idElemental){
		return AlternativeEnchant(player, idObj, idElemental,false);
	}
	
	private static boolean AlternativeEnchant(L2PcInstance player, int Obj, int stoneId){
		return AlternativeEnchant(player, Obj, stoneId,true);
	}
	
	@SuppressWarnings("unused")
	private static boolean AlternativeEnchant(L2PcInstance player, int Obj, int stoneId, boolean AplicarElemento)
	{
		
		boolean success = true;

		L2ItemInstance item = player.getInventory().getItemByObjectId(Obj);
		
		boolean isWeapon = item.isWeapon();

		if (item == null)
		{
			if(AplicarElemento){
				player.setActiveEnchantAttrItemId(L2PcInstance.ID_NONE);
			}
			return false;
		}

		if(!item.getItem().isElementable()){
			if(AplicarElemento){
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ELEMENTAL_ENHANCE_REQUIREMENT_NOT_SUFFICIENT));
				player.setActiveEnchantAttrItemId(L2PcInstance.ID_NONE);
			}
			return false;
		}


		switch (item.getItemLocation())
		{
			case INVENTORY:
			case PAPERDOLL:
			{
				if (item.getOwnerId() != player.getObjectId())
				{
					if(AplicarElemento){
						player.setActiveEnchantAttrItemId(L2PcInstance.ID_NONE);
					}
					return false;
				}
				break;
			}
			default:
			{
				if(AplicarElemento){
					player.setActiveEnchantAttrItemId(L2PcInstance.ID_NONE);
					Util.handleIllegalPlayerAction(player, "Player " + player.getName() + " tried to use enchant Exploit!", Config.DEFAULT_PUNISH);
				}
				return false;
			}
		}

		byte elementToAdd = Elementals.getItemElement(stoneId);
		// Armors have the opposite element
		if (item.isArmor())
		{
			elementToAdd = Elementals.getOppositeElement(elementToAdd);
		}

		//elementToAdd = Elementals.getOppositeElement(elementToAdd);

		byte opositeElement = Elementals.getOppositeElement(elementToAdd);

		Elementals oldElement = item.getElemental(elementToAdd);

		int elementValue = oldElement == null ? 0 : oldElement.getValue();
		int limit = getLimit(item, stoneId);
		int powerToAdd = getPowerToAdd(stoneId, elementValue, item);

		int idElementalActual = item.getAttackElementType();
		int idElementalPoner = elementToAdd;

		//if ((item.isWeapon() && (oldElement != null) && (oldElement.getElement() != elementToAdd) && (oldElement.getElement() != -2))
		//if ((item.isWeapon() && ((elementToAdd-1) != item.getAttackElementPower()))
		if(  ( item.isWeapon() && ( ((idElementalActual != idElementalPoner) && (idElementalActual != -2)) )    )
			|| (item.isArmor() && (item.getElemental(elementToAdd) == null) && (item.getElementals() != null) && (item.getElementals().length >= 3)))
		{
			if(AplicarElemento){
				player.sendPacket(SystemMessageId.ANOTHER_ELEMENTAL_POWER_ALREADY_ADDED);
				player.setActiveEnchantAttrItemId(L2PcInstance.ID_NONE);
			}
			return false;
		}

		if (item.isArmor() && (item.getElementals() != null))
		{
			// cant add opposite element
			for (Elementals elm : item.getElementals())
			{
				if (elm.getElement() == opositeElement)
				{
					if(AplicarElemento){
						player.setActiveEnchantAttrItemId(L2PcInstance.ID_NONE);
					}
					// Util.handleIllegalPlayerAction(player, "Player " + player.getName() + " tried to add oposite attribute to item!", Config.DEFAULT_PUNISH);
					return false;
				}
			}
		}

		int newPower = elementValue + powerToAdd;
		if (newPower > limit)
		{
			newPower = limit;
			powerToAdd = limit - elementValue;
		}
		if(AplicarElemento){
			if(isWeapon){
				if(newPower>=general.ELEMENTAL_LVL_ENCHANT_MAX_WEAPON){
					central.msgbox("Elemental Atributte fail. Maximus reached", player);
					return false;
				}
			}else{
				if(newPower>=general.ELEMENTAL_LVL_ENCHANT_MAX_ARMOR){
					central.msgbox("Elemental Atributte fail. Maximus reached", player);
					return false;
				}			
			}
		}

		if (powerToAdd <= 0)
		{
			if(AplicarElemento){
				player.sendPacket(SystemMessageId.ELEMENTAL_ENHANCE_CANCELED);
				player.setActiveEnchantAttrItemId(L2PcInstance.ID_NONE);
			}
			return false;
		}

		byte realElement = item.isArmor() ? opositeElement : elementToAdd;

		if(!AplicarElemento){
			return true;
		}
		
		SystemMessage sm;
		if (item.getEnchantLevel() == 0)
		{
			if (item.isArmor())
			{
				sm = SystemMessage.getSystemMessage(SystemMessageId.THE_S2_ATTRIBUTE_WAS_SUCCESSFULLY_BESTOWED_ON_S1_RES_TO_S3_INCREASED);
			}
			else
			{
				sm = SystemMessage.getSystemMessage(SystemMessageId.ELEMENTAL_POWER_S2_SUCCESSFULLY_ADDED_TO_S1);
			}
			sm.addItemName(item);
			sm.addElemental(realElement);
			if (item.isArmor())
			{
				sm.addElemental(Elementals.getOppositeElement(realElement));
			}
		}
		else
		{
			if (item.isArmor())
			{
				sm = SystemMessage.getSystemMessage(SystemMessageId.THE_S3_ATTRIBUTE_BESTOWED_ON_S1_S2_RESISTANCE_TO_S4_INCREASED);
			}
			else
			{
				sm = SystemMessage.getSystemMessage(SystemMessageId.ELEMENTAL_POWER_S3_SUCCESSFULLY_ADDED_TO_S1_S2);
			}
			sm.addInt(item.getEnchantLevel());
			sm.addItemName(item);
			sm.addElemental(realElement);
			if (item.isArmor())
			{
				sm.addElemental(Elementals.getOppositeElement(realElement));
			}
		}
		player.sendPacket(sm);
		item.setElementAttr(elementToAdd, newPower);
		if (item.isEquipped())
		{
			item.updateElementAttrBonus(player);
		}

		// send packets
		InventoryUpdate iu = new InventoryUpdate();
		iu.addModifiedItem(item);
		player.sendPacket(iu);

		player.sendPacket(new ExAttributeEnchantResult(powerToAdd));
		player.sendPacket(new UserInfo(player));
		player.sendPacket(new ExBrExtraUserInfo(player));
		player.setActiveEnchantAttrItemId(L2PcInstance.ID_NONE);

		return success;
	}

	public static boolean AplicarElemental(L2PcInstance st, String ElementalType, String ID_objectPlace){
		
		if(!general._activated()){
			return false;
		}

		if(general.ELEMENTAL_NOBLE && !st.isNoble()){
			central.msgbox (msg.NECESITAS_SER_NOBLE,st);
			return false;
		}

		if(st.getLevel() < general.ELEMENTAL_LVL){
			central.msgbox( msg.NECESITAS_TENER_UN_LEVEL_MAYOR_A_$level.replace("$level", String.valueOf(general.ELEMENTAL_LVL)),st);
			return false;
		}

		int IDItemEnchant =0;

		switch(ElementalType){
			case "Fire":
				IDItemEnchant = 9558;
				break;
			case "Water":
				IDItemEnchant = 9559;
				break;
			case "Wind":
				IDItemEnchant = 9561;
				break;
			case "Earth":
				IDItemEnchant = 9560;
				break;
			case "Dark":
				IDItemEnchant = 9562;
				break;
			case "Holy":
				IDItemEnchant = 9563;
				break;
		}
		int IDLugar = Integer.valueOf(ID_objectPlace);
		L2ItemInstance itemSelecc = st.getInventory().getPaperdollItem(IDLugar);
		int IDObjeto = itemSelecc.getObjectId();

		//_log.warning("Elemento: " + eventParam2 + ", id Piedra: " + String.valueOf(IDItemEnchant));

		if(AlternativeEnchant(st,IDObjeto,IDItemEnchant)) {
			return true;
		} else {
			return false;
		}
	}

	public static String mainHtmlElemental(L2PcInstance st, String Elementando){
		if(!general._activated()){
			return "";
		}
			String ELEMENTAL_VECTOR = "Fire;Water;Wind;Earth;Dark;Holy";
			String STR_COMBO_ELEMENTOS = Elementando;
			String BOTON_REFRESH = "<button value=\"Refresh\" action=\"bypass -h ZeuSNPC ELEMENTAL 0 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

			for(String ComboElementos : ELEMENTAL_VECTOR.split(";")){
				if(!Elementando.equals(ComboElementos)){
					if (STR_COMBO_ELEMENTOS.length()>0) {
						STR_COMBO_ELEMENTOS += ";";
					}
					STR_COMBO_ELEMENTOS += ComboElementos;
				}
			}

			String HTML_NO_ITEM = ItemTable.getInstance().getTemplate(3883).getIcon();
			int[] VectorLocaciones = {Inventory.PAPERDOLL_HEAD, Inventory.PAPERDOLL_CHEST, Inventory.PAPERDOLL_LEGS, Inventory.PAPERDOLL_GLOVES, Inventory.PAPERDOLL_FEET, Inventory.PAPERDOLL_RHAND };
			String[] VectorNombre = {"Helmet", "Chest", "Legs", "Gloves", "Shoes", "Weapon"};
			Vector<String> VectorLink = new Vector<String>();
			for(int Info : VectorLocaciones){
				if (st.getInventory().getPaperdollItem(Info)!=null) {
					VectorLink.add("1;"+String.valueOf(Info)+";"+st.getInventory().getPaperdollItem(Info).getItem().getIcon());
				} else {
					VectorLink.add("0;"+String.valueOf(Info)+";"+HTML_NO_ITEM);
				}
			}
			String PARA_ITEM = "<table with=280><tr>";
			int Contador = 0;
			String ANCHO_BOTON = "88";
			for (String ItemsS : VectorLink){
				String[] VectorSplit = ItemsS.split(";");
				String BOTON;
				String IMAGEN;
				if(VectorSplit[0].equals("1")){
					BOTON = "<button value=\""+VectorNombre[Contador]+"\" action=\"bypass -h ZeuSNPC ELEMENTAL 1 $ebox "+VectorSplit[1]+"\" width="+ANCHO_BOTON+" height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
					IMAGEN = "<img src="+VectorSplit[2]+" width=32 height=32>";
					PARA_ITEM += "<td align=CENTER>"+IMAGEN+BOTON+"</td>";
				}else{
					BOTON = "<button value=\""+VectorNombre[Contador]+"\" action=\"bypass -h ZeuSNPC ELEMENTAL 0 0 0\" width="+ANCHO_BOTON+" height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
					IMAGEN = "<img src="+VectorSplit[2]+" width=32 height=32>";
					PARA_ITEM += "<td align=CENTER>"+IMAGEN+BOTON+"</td>";
				}
				Contador ++;
				if((Contador % 3)==0) {
					PARA_ITEM += "</tr><tr>";
				}
			}
			PARA_ITEM += "</table>";

			String COMBO_BOX = "<combobox width=75 var=ebox list="+STR_COMBO_ELEMENTOS+"><br>";

			String MAIN_HTML = "<html><title>" + general.TITULO_NPC() +"</title><body>";
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Elemental Enchantment") + central.LineaDivisora(2);
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat(BOTON_REFRESH,"LEVEL") + central.LineaDivisora(1);
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat("Select type of Element." + COMBO_BOX,"LEVEL") + central.LineaDivisora(1);
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat(PARA_ITEM,"LEVEL") + central.LineaDivisora(1);
			MAIN_HTML += central.ItemNeedShowBox(general.ELEMENTAL_ITEM_PRICE);
			MAIN_HTML += "<br><br><br>"+central.BotonGOBACKZEUS()+central.getPieHTML() + "</center></body></html>";
			return MAIN_HTML;
	}

}
