package ZeuS.Comunidad.EngineForm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Comunidad.CBByPass;
import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.Engine.enumBypass;
import ZeuS.Comunidad.EngineForm.C_findparty.PMTimerPlayer;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.procedimientos.itemLink;
import ZeuS.procedimientos.opera;

import com.l2jserver.Config;
import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.engines.items.Item;
import com.l2jserver.gameserver.enums.ItemLocation;
import com.l2jserver.gameserver.idfactory.IdFactory;
import com.l2jserver.gameserver.model.Elementals;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2WarehouseInstance;
import com.l2jserver.gameserver.model.itemcontainer.Warehouse;
import com.l2jserver.gameserver.model.items.L2Item;
import com.l2jserver.gameserver.model.items.L2Weapon;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.items.type.ArmorType;
import com.l2jserver.gameserver.model.items.type.ItemType;
import com.l2jserver.gameserver.model.items.type.WeaponType;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.Earthquake;
import com.l2jserver.gameserver.network.serverpackets.InventoryUpdate;
import com.l2jserver.gameserver.network.serverpackets.ItemList;
import com.l2jserver.gameserver.network.serverpackets.StatusUpdate;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

public class v_auction_house {
	private static final Logger _log = Logger.getLogger(v_auction_house.class.getName());
	
	private static HashMap<Integer,InfoAuction> AUCTION_ITEM = new HashMap<Integer, InfoAuction>();
	
	private static HashMap<Integer,String>strControlPagina = new HashMap<Integer, String>();
	
	private static HashMap<Integer,String>PalabraBuscar = new HashMap<Integer, String>();
	private static HashMap<Integer,String>GradeList = new HashMap<Integer, String>();
	private static HashMap<Integer,String>TipoSearch = new HashMap<Integer, String>();
	private static HashMap<Integer,String>TipoSearch_index = new HashMap<Integer, String>();
	
	static final int ID_OWNER_AUCTION = -300;
	private static int ContadorRetroceso = -2099999999;
	
	private static final int[]ID_DUAL_SWORD = {52,10004,10415,11251,11300,14570,16150,16154,16158,20295};
	
	private static Vector<Integer>VECTOR_ID_DUALSWORD = new Vector<Integer>();
	
	private static HashMap<Integer,HashMap<Integer,HashMap<String,String>>> SELLOFFLINER= new HashMap<Integer,HashMap<Integer,HashMap<String,String>>>(); 
	private static HashMap<Integer,Long> SELLOFFLINE_LONG = new HashMap<Integer, Long>();
	
	/*
	private static final int FEED_MASTER = 10000;
	private static final int PORCEN_FEED = 8;
	
	public static int AUCTIONSHOUSE_PERCENT_FEED;
	public static int AUCTIONSHOUSE_FEED_MASTER;
	
	*/
	
	//L2Item.SLOT_R_HAND
	//weapon.getItemType() == WeaponType.BOW
	
	
	private static void haveItemToWidthdraw(L2PcInstance player, boolean Entrada){
		if(Entrada){
			if(SELLOFFLINER!=null){
				if(SELLOFFLINER.containsKey(player.getObjectId())){
					SELLOFFLINER.remove(player.getObjectId());					
				}
			}
			
			String Consulta = "select idItemVendido, idItemSolicitado, totalItemEntregar, totalItemVendidos from zeus_auctions_house_offline where idChar=?";
			try (Connection con = ConnectionFactory.getInstance().getConnection();
					PreparedStatement statement = con.prepareStatement(Consulta))
				{
					if(SELLOFFLINER!=null){
						if(!SELLOFFLINER.containsKey(player.getObjectId())){
							SELLOFFLINER.put(player.getObjectId(), new HashMap<Integer,HashMap<String,String>>());
						}
					}else if(SELLOFFLINER==null){
						SELLOFFLINER.put(player.getObjectId(), new HashMap<Integer,HashMap<String,String>>());
					}
					statement.setInt(1, player.getObjectId());
					try (ResultSet inv = statement.executeQuery())
					{
						if (inv.next())
						{
							int idTemp = ContadorRetroceso++;
							SELLOFFLINER.get(player.getObjectId()).put(idTemp, new HashMap<String,String>());
							SELLOFFLINER.get(player.getObjectId()).get(idTemp).put("ID_ITEM_SOLD", String.valueOf(inv.getInt(1)));
							SELLOFFLINER.get(player.getObjectId()).get(idTemp).put("ID_ITEM_REQUEST", String.valueOf(inv.getInt(2)));
							SELLOFFLINER.get(player.getObjectId()).get(idTemp).put("TOTAL_ITEM_A_ENTREGAR", String.valueOf(inv.getLong(3)));
							SELLOFFLINER.get(player.getObjectId()).get(idTemp).put("TOTAL_VENDIDO", String.valueOf(inv.getDouble(4)));
						}
					}catch(Exception a){
						_log.warning("Error ZeuS A ->" + a.getMessage());						
					}
				}
				catch (Exception e)
				{
					_log.warning("Error ZeuS E ->" + e.getMessage());
				}					
		}
		
		if(Entrada){
			if(SELLOFFLINER==null){
				return;
			}
			
			if(!SELLOFFLINER.containsKey(player.getObjectId())){
				return;
			}
			
			if(SELLOFFLINER.get(player.getObjectId())==null){
				return;
			}
			
			if(SELLOFFLINER.get(player.getObjectId()).size()<=0){
				return;
			}
		}
		
		String retorno = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		retorno += "<table width=280><tr><td align=CENTER fixwidth=280><font color=\"DD9B22\">Offline Seller</font></td></tr></table><img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=0>";
		
		int contador =0;
		int Maximo = 10;
		
		Iterator itr = SELLOFFLINER.get(player.getObjectId()).entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry inf = (Map.Entry)itr.next();
			int IdInterno = (int) inf.getKey();
			int idItemVendido = Integer.valueOf(SELLOFFLINER.get(player.getObjectId()).get(IdInterno).get("ID_ITEM_SOLD"));
			String CantidadVendida = SELLOFFLINER.get(player.getObjectId()).get(IdInterno).get("TOTAL_VENDIDO");
			String MontoVendido = SELLOFFLINER.get(player.getObjectId()).get(IdInterno).get("TOTAL_ITEM_A_ENTREGAR");
			String NombreItemRequest = central.getNombreITEMbyID( Integer.valueOf(SELLOFFLINER.get(player.getObjectId()).get(IdInterno).get("ID_ITEM_REQUEST")));

			String ByPass = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";GiveItem;" + String.valueOf(IdInterno) + ";0;0;0;0;0";
			retorno += "<table width=280 border=0 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td align=CENTER fixwidth=32>"+
               "<img src=\""+ opera.getIconImgFromItem(idItemVendido, true) +"\" width=32 height=32><br><br><br><br><br><br><br>"+
           "</td>"+
           "<td fixwidth=248>"+
               "<font color=FAAC58>"+ central.getNombreITEMbyID(idItemVendido) +"</font><br1>"+
               "Quantity Sold:<font color=97F776>"+ opera.getFormatNumbers(CantidadVendida) +"</font><br1>"+
               "Sold:<font color=97F776>"+ opera.getFormatNumbers(MontoVendido) +" ("+ NombreItemRequest +")</font><br1>"+
               "<button value=\"Withdraw\" action=\""+ ByPass +"\" width=80 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"+
           "</td>"+
       "</tr>"+
       "</table>";
		}
		retorno += "<img src=L2UI.SquareBlank width=280 height=1><img src=L2UI.SquareGray width=280 height=1><img src=L2UI.SquareBlank width=270 height=0>";
		retorno += central.getPieHTML(false) + "</center></body></html>";
		central.sendHtml(player, retorno);
	}
	
	
	private static void createofflineSell(L2PcInstance player, InfoAuction itemInfo, long CantidadVender ){
		String Consulta = "INSERT INTO zeus_auctions_house_offline "+
				"(idChar, idItemVendido,idItemSolicitado,totalItemEntregar,totalItemVendidos) values "+
				"( ?,?,?,?,? ) "+
				"ON DUPLICATE KEY UPDATE "+
				"zeus_auctions_house_offline.totalItemEntregar = totalItemEntregar + ?, "+
				"zeus_auctions_house_offline.totalItemVendidos = totalItemVendidos + ?";
		
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = ConnectionFactory.getInstance().getConnection();
			ins = con.prepareStatement(Consulta);
			ins.setInt(1, itemInfo.getPlayerID());
			ins.setInt(2, itemInfo.getItemInstance().getItem().getId());
			ins.setInt(3, itemInfo.getIDItemRequested());
			ins.setLong(4, CantidadVender * itemInfo.getQuantityRequest());
			ins.setLong(5, CantidadVender );
			
			ins.setLong(6, CantidadVender * itemInfo.getQuantityRequest());
			ins.setLong(7, CantidadVender );
			try{
				ins.executeUpdate();
			}catch(SQLException e){
				_log.warning("Error ZeuS E ->" + e.getMessage());
			}
		}catch(SQLException a){
			_log.warning("Error ZeuS A ->" + a.getMessage());
		}
		try{
			con.close();
		}catch(SQLException a){
			_log.warning("Error ZeuS A2 ->" + a.getMessage());
		}			
		
	}
	
	private static boolean TypeSearch_addToList(InfoAuction ItemSelected, String tipoBusqueda){
		/***JEWEL_RING
			JEWEL_EARRING
			JEWEL_NECKLACE
			
			ARMOR_HELMET
			
			WEAPON_SWORD
		*/
		
		if(VECTOR_ID_DUALSWORD!=null){
			for(int i : ID_DUAL_SWORD){
				VECTOR_ID_DUALSWORD.add(i);
			}
		}
		
		String tipo = tipoBusqueda.split("_")[1];
		L2ItemInstance item = ItemSelected.getItemInstance();
		int BodyPart = item.getItem().getBodyPart();
		ItemType WType = item.getItem().getItemType();
		ItemType ArmorT = item.getItemType();
		switch(tipoBusqueda.split("_")[0]){
			case "JEWEL":
				if( tipo.equals("EARRING") && (BodyPart == L2Item.SLOT_R_EAR || BodyPart == L2Item.SLOT_L_EAR || BodyPart == L2Item.SLOT_LR_EAR)){ 
					return true;
				}else if(tipo.equals("RING") && ( BodyPart == L2Item.SLOT_R_FINGER || BodyPart == L2Item.SLOT_L_FINGER) || BodyPart == L2Item.SLOT_LR_FINGER){
					return true;
				}else if(tipo.equals("NECKLACE") && BodyPart == L2Item.SLOT_NECK ){
					return true;
				}
				break;
			case "ARMOR":
				if(tipo.equals("HELMET") && BodyPart == L2Item.SLOT_HEAD){
					return true;
				}else if(tipo.equals("CHEST") && BodyPart == L2Item.SLOT_CHEST){
					return true;
				}else if(tipo.equals("LEGS") && BodyPart == L2Item.SLOT_LEGS){
					return true;
				}else if(tipo.equals("GLOVES") && BodyPart == L2Item.SLOT_GLOVES){
					return true;
				}else if(tipo.equals("SHOES") && BodyPart == L2Item.SLOT_FEET){
					return true;
				}else if(tipo.equals("CLOAK") && BodyPart == L2Item.SLOT_BACK){
					return true;
				}else if(tipo.equals("SHIRT") && BodyPart == L2Item.SLOT_UNDERWEAR){
					return true;
				}else if(tipo.equals("BELT") && BodyPart == L2Item.SLOT_BELT){
					return true;
				}else if(tipo.equals("SIGIL") && ( BodyPart == L2Item.SLOT_R_HAND && ArmorT == ArmorType.SIGIL ) ){
					return true;
				}else if(tipo.equals("SHIELD") && ( BodyPart == L2Item.SLOT_R_HAND && ArmorT == ArmorType.SHIELD ) ){
					return true;
				}
				break;
			case "WEAPON":
				if(tipo.equals("SWORD") && WType == WeaponType.SWORD){
					return true;
				}else if(tipo.equals("BIGSWORD") && (WType == WeaponType.SWORD && BodyPart == L2Item.SLOT_LR_HAND && !VECTOR_ID_DUALSWORD.contains(item.getItem().getId()))){
					return true;
				}else if(tipo.equals("ANCIENTSWORD") && WType == WeaponType.ANCIENTSWORD){
					return true;
				}else if(tipo.equals("BLUNT") && WType == WeaponType.BLUNT){
					return true;
				}else if(tipo.equals("BIGBLUNT") && (WType == WeaponType.BLUNT && BodyPart == L2Item.SLOT_LR_HAND)){
					return true;
				}else if(tipo.equals("DAGGER") && WType == WeaponType.DAGGER){
					return true;
				}else if(tipo.equals("DUALDAGGER") && WType == WeaponType.DUALDAGGER){
					return true;
				}else if(tipo.equals("BOW") && WType == WeaponType.BOW){
					return true;
				}else if(tipo.equals("CROSSBOW") && WType == WeaponType.CROSSBOW){
					return true;
				}else if(tipo.equals("POLE") && WType == WeaponType.POLE){
					return true;
				}else if(tipo.equals("FISTS") && (WType == WeaponType.FIST || WType == WeaponType.DUALFIST)){
					return true;
				}else if(tipo.equals("RAPIER") && WType == WeaponType.RAPIER){
					return true;
				}else if(tipo.equals("OTHER") && (WType == WeaponType.ETC || WType == WeaponType.FISHINGROD|| WType == WeaponType.NONE || WType == WeaponType.OWNTHING)){
					return true;
				}else if(tipo.equals("DUALSWORD") && VECTOR_ID_DUALSWORD.contains(item.getItem().getId())){
					return true;
				}
				break;
		}
		
		return false;
	}
	
	private static int getFeedFromItem(int idObjectItem){
		if(idObjectItem>0){
			try{
				L2Object object = L2World.getInstance().findObject(idObjectItem);
				if (object instanceof L2ItemInstance){
					L2ItemInstance item = (L2ItemInstance) object;
					int ReferentePrice = item.getReferencePrice();
					if(ReferentePrice>0){
						int FeedToBack = ( ReferentePrice * general.AUCTIONSHOUSE_PERCENT_FEED ) / 100 ;
						
						if(FeedToBack > general.AUCTIONSHOUSE_FEED_MASTER){
							return FeedToBack;
						}else{
							return general.AUCTIONSHOUSE_FEED_MASTER + FeedToBack;							
						}
					}
				}
			}catch(Exception a){
				_log.warning("Error ZeuS A ->" + a.getMessage());
				return general.AUCTIONSHOUSE_FEED_MASTER;
			}
		}
		return general.AUCTIONSHOUSE_FEED_MASTER;
	}
	
	private static void getInfoItemWindows(L2PcInstance player, int idObjecto){
		
		
		String Elemental[] = new String[6];
		
		Elemental[0] = "<td fixwidth=257>Fire Atribute (%POWER%)<br1><table width=257 background=L2UI_CT1.Gauge_DF_Attribute_Fire_bg cellspacing=0 cellpadding=0><tr><td fixwidth=257 height=18>"+
        "<img src=\"L2UI_CT1.Gauge_DF_Attribute_Fire\" width=%TAMAÑO% height=10></td></tr></table></td>";
		
		Elemental[1] = "<td fixwidth=257>Water Atribute (%POWER%)<br1><table width=257 background=L2UI_CT1.Gauge_DF_Attribute_Water_bg cellspacing=0 cellpadding=0><tr><td fixwidth=257 height=18>"+
		"<img src=\"L2UI_CT1.Gauge_DF_Attribute_Water\" width=%TAMAÑO% height=10></td></tr></table></td>";

		Elemental[2] = "<td fixwidth=257>Earth Atribute (%POWER%)<br1><table width=257 background=L2UI_CT1.Gauge_DF_Attribute_Earth_bg cellspacing=0 cellpadding=0><tr><td fixwidth=257 height=18>"+
       "<img src=\"L2UI_CT1.Gauge_DF_Attribute_Earth\" width=%TAMAÑO% height=10></td></tr></table></td>";		
		
		Elemental[3] = "<td fixwidth=257>Wind Atribute (%POWER%)<br1><table width=257 background=L2UI_CT1.Gauge_DF_Attribute_Wind_bg cellspacing=0 cellpadding=0><tr><td fixwidth=257 height=18>"+
		"<img src=\"L2UI_CT1.Gauge_DF_Attribute_Wind\" width=%TAMAÑO% height=10></td></tr></table></td>";
		
		Elemental[4] = "<td fixwidth=257>Dark Atribute (%POWER%)<br1><table width=257 background=L2UI_CT1.Gauge_DF_Attribute_Dark_bg cellspacing=0 cellpadding=0><tr><td fixwidth=257 height=18>"+
		"<img src=\"L2UI_CT1.Gauge_DF_Attribute_Dark\" width=%TAMAÑO% height=10></td></tr></table></td>";
		
		Elemental[5] = "<td fixwidth=257>Holy Atribute (%POWER%)<br1><table width=257 background=L2UI_CT1.Gauge_DF_Attribute_Divine_bg cellspacing=0 cellpadding=0><tr><td fixwidth=257 height=18>"+
		"<img src=\"L2UI_CT1.Gauge_DF_Attribute_Divine\" width=%TAMAÑO% height=10></td></tr></table></td>";		
		
		String DivineS = "";
		
		String html = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		
		InfoAuction p = AUCTION_ITEM.get(idObjecto);
		
		String fontColorName = ( opera.isMasterWorkItem(p.getIdItem()) ? "LEVEL" : "A4FFA9" );
		
		html += "<table width=257 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2><tr><td fixwidth=32>"+
        "<br><img src=\""+ p.getIconImgID() +"\" width=32 height=32><br><br></td>"+
        "<td fixwidth=228><font name=hs12 color="+ fontColorName +">"+ p.getItemName() +"</font></td></tr></table>";
		
		if(p.getItemEnchant()>0){
			html +="<table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2><tr>"+
			"<td fixwidth=262><font name=hs12 color=A4FFA9>Enchant: +" + p.getItemEnchant() +"</font></td></tr></table>";
		}
		
		
		int Divino=0,Dark=0,Earth=0,Wind=0,Water=0,Fire=0;
		
		InfoAuction itemSel = AUCTION_ITEM.get(idObjecto);
		
		String html2 = "";
		
		if(itemSel.getItemInstance().isWeapon()){
			int idAtri = itemSel.getItemInstance().getAttackElementType();
			int Atributo = itemSel.getItemInstance().getAttackElementPower();
			int maximo = 40;
			int Tamaño = (257 * Atributo) / maximo;
			if(Tamaño>257){
				Tamaño=257;
			}
			for(int i=0 ;i<=5 ;i++){
				if(i==idAtri){
					html2 += "<tr>"+ Elemental[i].replace("%TAMAÑO%", String.valueOf(Tamaño)).replace("%POWER%", String.valueOf(Atributo)) +"</tr>";
				}
			}
		}else if(itemSel.getItemInstance().isArmor()){
			Integer[]Atri = new Integer[6];
			Atri[5] = itemSel.getDivineAtributte();
			Atri[4] = itemSel.getDarkAtributte();
			Atri[2] = itemSel.getEarthAtributte();
			Atri[3] = itemSel.getWindAtributte();
			Atri[1] = itemSel.getWaterAtributte();
			Atri[0] = itemSel.getFireAtributte();
			for(int i=0 ;i<=5 ;i++){
				int Tamaño = ( 257 * Atri[i] ) / 180;
				int Atributo = Atri[i];
				if(Tamaño > 257){
					Tamaño = 257;
				}
				if(Atributo>0){
					html2 += "<tr>"+ Elemental[i].replace("%TAMAÑO%", String.valueOf(Tamaño)).replace("%POWER%", String.valueOf(Atributo)) +"</tr>";
				}
			}			
		}

		if(html2.length()>0){
			html += "<table width=257 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2>";
			html += html2 + "</table>";
		}
		
		
		html += "<table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=112>"+
        "<font color=F5D0A9>Quantity to Sell:</font></td><td fixwidth=152><font color=FE9A2E>"+ String.valueOf(itemSel.getQuantityItemToSell()) +"</font></td></tr></table><table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=112>"+
        "<font color=F5D0A9>Item Request:</font></td><td fixwidth=152><font color=FE9A2E>"+ itemSel.getNameItemToRequest() +"</font></td></tr></table><table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=112>"+
        "<font color=F5D0A9>Quantity R/Item:</font></td><td fixwidth=152><font color=FE9A2E>"+ opera.getFormatNumbers(itemSel.getQuantityRequest()) +"</font></td></tr></table><table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=112>"+
        "<font color=F5D0A9>Saller Name:</font></td><td fixwidth=152><font color=FE9A2E>"+ itemSel.getSellerName() +"</font></td></tr></table><table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=112>"+
        "<font color=F5D0A9>Is Online:</font></td><td fixwidth=152>"+ (itemSel.isPlayerOnline() ? "<font color=59FF85>Yes" : "<font color=FF5959>No") +"</font></td></tr></table>";
		
		if(player.getObjectId() == itemSel.getPlayerID()){
			String BypassCancel = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";cancelauction;" + String.valueOf(idObjecto) + ";0;0;0;0";
			html+= "<table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=264 align=CENTER>"+
            "<button value=\"Cancel Auction\" action=\""+ BypassCancel +"\" width=150 height=34 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";
		}else{
		
		}
		
		if(opera.haveItem(player, itemSel.getIDItemRequested(), itemSel.getQuantityRequest(), false)){
			
			String ByPassBuyItNow = "bypass -h voice .AuCtIoNsCaS BuyItNow " + idObjecto + " $txtQuantity";
			html+= "<table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=264 align=CENTER>"+
            "<font name=hs12 color=LEVEL>Quantity for Purchase</font><edit type=number var=\"txtQuantity\" width=120><br>"+
			"<button value=\"Buy It Now\" action=\""+ ByPassBuyItNow +"\" width=150 height=34 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";			
			
			String itemHave = opera.getFormatNumbers( String.valueOf(player.getInventory().getItemByItemId( itemSel.getIDItemRequested()).getCount() ) );
			html += "<table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=264 align=CENTER>"+
			"<font name=hs12 color=FAF991>You item's</font><br>"+
            "<font name=hs12 color=B8FA91>"+ itemSel.getNameItemToRequest() +"</font><br1>"+
            "<font name=hs12 color=91DEFA>"+ itemHave +"</font><br>"+
           "</td></tr></table>";
		}else{
			html += "<table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=264 align=CENTER>"+
            "<font name=hs12 color=FAF991>You item's</font><br>You dont have the Requested Item</td></tr></table>";
		}
		
		html += "</body></html>";
		opera.enviarHTML(player, html);
	}
	
	
	private static void setCreateNewItemInBD(L2PcInstance player, int IdObject, int itemID, Long Count){
		String Consulta = "insert into items(owner_id, object_id, item_id, count, enchant_level, loc, loc_data) values (?,?,?,?,?,?,?)";
		
		Connection con = null;
		try{
			con = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(Consulta);
			ins.setInt(1, ID_OWNER_AUCTION);
			ins.setInt(2, IdObject);
			ins.setInt(3, itemID);
			ins.setLong(4, Count);
			ins.setInt(5, 0);
			ins.setString(6, "INVENTORY");
			ins.setInt(7, 22);
			try{
				ins.executeUpdate();
				ins.close();
				con.close();
			}catch(SQLException e){
				_log.warning("Error ZeuS E->" + e.getMessage());
			}
		}catch(Exception a){
			_log.warning("Error ZeuS A->" + a.getMessage());
		}		
		
	}
	
	
	private static boolean setNewAuction_INBD(L2PcInstance player, int idObject, int idItemRequest, Long SellItem_Quantity, Long ItemRequestQuantity, int FeedIN){
		
		boolean retorno = false;
		
		String Consulta = "INSERT INTO zeus_auctions_house("+
		"idObjeto,idOwner,idItemRequest,ItemRequestQuantity,ItemQuantityToSell,startUnix,feed) VALUES(?,?,?,?,?,?,?)";
		Connection con = null;
		try{
			con = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(Consulta);
			ins.setInt(1, idObject);/**/
			ins.setInt(2, player.getObjectId());/**/
			ins.setInt(3, idItemRequest);/**/
			ins.setLong(4, ItemRequestQuantity);
			ins.setLong(5, SellItem_Quantity);
			ins.setFloat(6, opera.getUnixTimeL2JServer());
			ins.setInt(7, FeedIN);
			try{
				ins.executeUpdate();
				ins.close();
				con.close();
				retorno = true;
			}catch(SQLException e){
				_log.warning("Error ZeuS E->" + e.getMessage());
			}
		}catch(Exception a){
			_log.warning("Error ZeuS A->" + a.getMessage());
		}

		return retorno;
	}
	
	private static void updateInventario(L2PcInstance player)
	{
		getAllItem();
	
		player.sendPacket(new ItemList(player, false));
		StatusUpdate su = new StatusUpdate(player);
		su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
		player.sendPacket(su);
	}

	
	private static boolean haveItemAlready(L2PcInstance player, int idItem){
		Iterator itr = AUCTION_ITEM.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry inf = (Map.Entry)itr.next();
			InfoAuction m1 = (InfoAuction) inf.getValue();
			if(m1.getIdItem() == idItem && m1.getPlayerID() == player.getObjectId()){
				return true;
			}
		}
		return false;
	}
	
	
	//createAuctionWithItem(player, idObjeto, TipoMonedaPide, Cantidad_A_Vender, ValorPorCadaItemA_Vender );
	
	private static void createAuctionWithItem(L2PcInstance player, int idObject, int idItemRequest, Long Cantidad_a_Vender, Long ValorPorCadaItem_A_Vender){
		L2Object object = L2World.getInstance().findObject(idObject);
		if (object instanceof L2ItemInstance){
			L2ItemInstance item = (L2ItemInstance) object;
			if(item.isEquipped()){
				central.msgbox("Auctions create process fail. The "+ item.getName() +" is equipped", player);
				return;
			}
			
			if(item.getOwnerId() != player.getObjectId()){
				central.msgbox("Auctions Error. You try to cheat me :). But you cant. The Item change owner.", player);
				return;
			}
			
			int FeedRequested = getFeedFromItem(idObject);
			
			if(!opera.haveItem(player, 57, FeedRequested )){
				central.msgbox("You dont have the requested Feed of " + String.valueOf(FeedRequested), player);
				return;
			}
			
			
			try{
				L2ItemInstance itemW = player.getWarehouse().getItemByObjectId(idObject);
				if(itemW!=null){
					central.msgbox("Auctions Error. You try to cheat me :). But you cant. You Change the selected item to the Warehouse.", player);
					return;					
				}
			}catch(Exception a){
				
			}
			
			try{
				L2ItemInstance itemIn = player.getInventory().getItemByObjectId(idObject);
				if(itemIn==null){
					central.msgbox("Auctions Error. You try to cheat me :). But you cant. You dont have the selected item.", player);
					return;					
				}
			}catch(Exception a){
				
			}
			
			
			if(!item.isStackable() && Cantidad_a_Vender>1){
				central.msgbox("Auctions Change. The "+ item.getName() +" is not a Stackable item,  quantity change automatic to 1", player);
				Cantidad_a_Vender = 1L;
			}
			
			
			if(haveItemAlready(player,item.getItem().getId())){
				central.msgbox("If you want sell this Item, you need to cancel the another auction with the same item", player);
				return;
			}
			
			
			int IdObjectNew = idObject;
			
			//_log.warning("Cantidad de Item " + item.getCount() + " yo pide->" + ItemToSellQuantity);
			
			int FeedIN = getFeedFromItem(idObject);
			
			if(Cantidad_a_Vender > item.getCount()){
				central.msgbox("Auctions create process fail. Please Check Item Quantity to Sell.", player);
				return;
			}else if(Cantidad_a_Vender < item.getCount()){
				IdObjectNew = getNewIDObject();
			}
			
			//setNewAuction_INBD_(player, IdObjectNew, idItemRequest, ItemRequestQuantity, ItemToSellQuantity, FeedIN);
			//setNewAuction_INBD_(player,IdObjectNew,idItemRequest,ValorPorCadaItem_A_Vender,Cantidad_a_Vender,FeedIN);
			setNewAuction_INBD(player,IdObjectNew,idItemRequest,Cantidad_a_Vender,ValorPorCadaItem_A_Vender,FeedIN);
			if(item.isStackable()){
				opera.removeItem(item.getItem().getId(), Cantidad_a_Vender, player);
				setCreateNewItemInBD(player, IdObjectNew, item.getItem().getId(),Cantidad_a_Vender);
			}else{
				item.setOwnerId(ID_OWNER_AUCTION);
				item.updateDatabase(true);
				item.broadcastInfo();
				player.destroyItemWithoutTrace("auction_house", item.getObjectId(), 1, null, false);
				//updateInventario(player);
			}
			//player.getInventory().updateDatabase();
			player.broadcastStatusUpdate();
			player.isInventoryDisabled();
			player.getInventory().refreshWeight();
			central.msgbox("Auction created!", player);
			getAllItem(false,player,item.getItem().getId());
			opera.removeItem(57, FeedRequested, player);
		}else{
			central.msgbox("Auctions create process fail. Please Check.", player);
		}
	}
	
	private static int getNewIDObject(){
		//IdFactory.getInstance().releaseId(getObjectId());
		int _objectId = IdFactory.getInstance().getNextId();
		return _objectId;
	}
	
	
	private static String getInventaryWindows(L2PcInstance player, int pagina, int idObjectItem){
		String retorno = "<table width=198><tr><td fixwidth=250>Auctionable Items</td></tr></table><table width=200 cellspacing=0 cellpadding=0 border=0 background=L2UI_CT1.Windows_DF_TooltipBG><tr>";
		
		String ByPass_Arriba_Top = "";
		String ByPass_Arriba_1 = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";0;" + String.valueOf(  pagina>0 ? pagina - 1 : 0    ) + ";"+ String.valueOf(idObjectItem) +";0;0;0";
		String BotoneraArriba = "<button value=\"\" width=16 height=16 action=\""+ ByPass_Arriba_Top +"\" back=L2UI_CH3.joypad_shortcut fore=L2UI_CH3.joypad_shortcut><button value=\"\" width=16 height=16 action=\""+ ByPass_Arriba_1 +"\" back=LL2UI_CH3.shortcut_nextv fore=L2UI_CH3.shortcut_nextv>";
		
		String ByPass_Abajo_Top = "";
		String ByPass_Abajo_1 = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";0;" + String.valueOf(pagina + 1) + ";"+ String.valueOf(idObjectItem) +";0;0;0";
		String BotoneraAbajo = "<button value=\"\" width=16 height=16 action=\""+ ByPass_Abajo_1 +"\" back=L2UI_CH3.shortcut_prevv fore=L2UI_CH3.shortcut_prevv><button value=\"\" width=16 height=16 action=\""+ ByPass_Abajo_Top +"\" back=L2UI_CH3.joypad_shortcut fore=L2UI_CH3.joypad_shortcut>";

		int ContadorFilas=0;
		int ContadorColumnas=0;
		boolean makeLinkArriba = true;
		boolean Continua = true;
		boolean haveNext = false;
		String ItemByPass = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";0;" + String.valueOf(pagina) + ";%IDOBJETO%;0;0;0";
		
		int ItemLementos = 5;
		int desde = ItemLementos * pagina;
		int ContadorElementos =1;
		
		for(L2ItemInstance items : player.getInventory().getItems()){
			if(items.getItem().getId()==57){
				continue;
			}
			if(items.isEquipped()){
				continue;
			}
			if(ContadorElementos>desde){
				if(Continua){
					if(items.isTradeable()){
						if(ContadorFilas==0){
							retorno += "<tr>";
						}
						if(ContadorFilas <= 4){
							retorno += "<td fixwidth=33 height=50>"+ cbFormato.getBotonForm(opera.getIconImgFromItem(items.getItem().getId(), true), ItemByPass.replace("%IDOBJETO%", String.valueOf(items.getObjectId())  ) ) +"</td>";
						}else if(ContadorFilas==5){
							if(makeLinkArriba){
								retorno += "<td fixwidth=33 height=50>"+ BotoneraArriba +"</td>";
								makeLinkArriba = false;
							}else if(ContadorColumnas == 2){
								retorno += "<td fixwidth=33 height=50>"+ BotoneraAbajo +"</td>";
								Continua=false;
							}else{
								retorno += "<td fixwidth=33 height=50></td>";
							}
							ContadorColumnas++;
						}
						ContadorFilas++;
						if(ContadorFilas==6){
							ContadorFilas=0;
							retorno += "</tr>";						
						}
					}
				}else{
					haveNext=true;
				}
			}
			ContadorElementos++;
		}
		
		if(ContadorFilas <6 && ContadorFilas>0){
			for(int i=ContadorFilas;i<6;i++){
				retorno += "<td fixwidth=33></td>";
			}
			retorno += "</tr>";
		}
		
		retorno += "</table><br>";
		
		return retorno;
	}
	
	private static String getSelectedItemToAuction(L2PcInstance player, int idObjectItem){
		String retorno ="";
		
		L2Object object = L2World.getInstance().findObject(idObjectItem);
		if (object instanceof L2ItemInstance){
			L2ItemInstance item = (L2ItemInstance) object;
			
			int getEnchant = 0;
			
			try{
				getEnchant = item.getEnchantLevel();
			}catch(Exception a){
				getEnchant = 0;
			}
			
			retorno = "<table width=200 border=0 cellspacing=1 cellpadding=1 background=L2UI_CT1.Windows_DF_TooltipBG>"+
                      "<tr>"+
                      "<td fixwidth=32><img src=\""+ opera.getIconImgFromItem(item.getItem().getId() , true) +"\" width=32 height=32><br></td>"+
                      "<td fixwidth=168>"+ item.getName() + ( getEnchant>0 ? " (+" + String.valueOf(getEnchant) + ")" : "" )  + "</td>"+
                      "</tr></table><br>";
		}		
		
		return retorno;
	}
	
	
	private static int getIdFromItemRequest(String NombreItem){
		if(general.AUCTIONSHOUSE_ITEM_REQUEST.size()>0){
			for(String t : general.AUCTIONSHOUSE_ITEM_REQUEST){
				if(t.split(":")[1].equalsIgnoreCase(NombreItem)){
					return Integer.valueOf(t.split(":")[0]);
				}
			}
		}
		return 57;
	}
	
	private static String getCmbItemRequest(){
		//ID:NOMBRE_ITEM
		String Retorno = "";
		if(general.AUCTIONSHOUSE_ITEM_REQUEST.size()>0){
			for(String t : general.AUCTIONSHOUSE_ITEM_REQUEST){
				if(Retorno.length()>0){
					Retorno += ";";
				}
				Retorno += t.split(":")[1];
			}
			return Retorno;
		}
		return "Adena";
	}
	
	
	private static String getSellBox(L2PcInstance player, int Pagina, int idObjeto){
		
		String ByPassCreateAuction = "bypass -h voice .AuCtIoNsCaS create " + String.valueOf(Pagina) + " " + String.valueOf(idObjeto) + " $txtQuantity $cmbItemRequest $txtPrice";
		
		String comboList = getCmbItemRequest();//"Adena;Apiga;Golden_Apiga";
		String retorno = "<table width=200 border=0 cellspacing=1 cellpadding=1 background=L2UI_CT1.Windows_DF_TooltipBG>"+
                                             "<tr><td fixwidth=100>Quantity:</td><td fixwidth=100><edit type=number var=\"txtQuantity\" width=100><br></td></tr>"+
                                             "<tr><td fixwidth=100>Item Request:</td><td fixwidth=100><combobox width=100 var=\"cmbItemRequest\" list=\""+ comboList +"\"><br></td></tr>"+
                                             "<tr><td fixwidth=100>Price per Item:</td><td fixwidth=100><edit type=number var=\"txtPrice\" width=100><br></td></tr></table>"+
                                             "<button value=\"Create Auction\" width=95 height=20 action=\""+ ByPassCreateAuction +"\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator><br>";
		return retorno;
	}
	
	private static String getFeeAndYouAdena(L2PcInstance player, int IdObjectItem){
		int Feed = getFeedFromItem(IdObjectItem);
		String YouAdena = opera.getFormatNumbers(String.valueOf(player.getInventory().getAdena()));
		String retorno = "<center><table width=220 background=L2UI_CT1.Windows_DF_TooltipBG><tr>"+
        "<td width=80>Sale Fee:</td><td width=140><font color=LEVEL>"+ opera.getFormatNumbers(Feed) +"</font></td></tr>"+
        "<tr><td width=80>Your Adena:<br></td><td width=140><font color=LEVEL>"+ YouAdena + "</font><br></td></tr></table><br></center>";
		
		return retorno;
	}
	
	private static String getMyAuctionList(L2PcInstance player, int PaginaMisVentas, int PaginaMisItemInventario, int idObjecto){
		String retorno = "<table cellspacing=0 cellpadding=0 border=0><tr><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td fixwidth=220 align=center>"+
		"<button value=\"Item to Sell\" width=220 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td>"+
		"<td fixwidth=20 align=center><button value=\"G\" width=20 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td fixwidth=60 align=center>"+
        "<button value=\"Quantity\" width=60 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td fixwidth=80 align=center><button value=\"I. Request\" width=80 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td fixwidth=110 align=center><button value=\"Sale Price\" width=110 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td></tr></table>";
		
		String []ColorBG = {"001818","010B0B"};

		HashMap<String,String> GradeItem = new HashMap<String, String>();
		GradeItem.put("S84", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_84\" width=16 height=16>");
		GradeItem.put("S80", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_80\" width=16 height=16>");
		GradeItem.put("S", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_S\" width=16 height=16>");
		GradeItem.put("A", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_A\" width=16 height=16>");
		GradeItem.put("B", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_B\" width=16 height=16>");
		GradeItem.put("C", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_C\" width=16 height=16>");
		GradeItem.put("D", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_D\" width=16 height=16>");
		GradeItem.put("", "");
		
		String Grilla = "<table cellspacing=0 cellpadding=0 border=0 bgcolor=%BGCOLOR%><tr>"+
        "<td fixwidth=32 height=42>%BTN_ITEM%</td>"+
        "<td fixwidth=200 align=center>%ITEM_NAME%</td>"+
        "<td fixwidth=20 align=center>%GRADE%</td>"+
        "<td fixwidth=65 align=center>%QUANTITY%</td>"+
        "<td fixwidth=85 align=center>%REQUEST_ITEM_NAME%</td>"+
        "<td fixwidth=110 align=center>%SALE_PRICE%</td>"+
        "</tr></table>";
		
		int contador = 0;
		
		/*else if(parm1.equals("showItemWindows")){
			getInfoItemWindows(player, Integer.valueOf(parm2));*/
		
		String BypassItemSee = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";showItemWindows;%IDOBJECTO%;0;0;0;0";
		
		int Limite = 6;
		
		int desde = Limite * PaginaMisVentas;
		int hasta = desde + Limite;
		boolean havenext = false;
		

		Iterator itr = AUCTION_ITEM.entrySet().iterator();
		while(itr.hasNext() && !havenext){
			Map.Entry inf = (Map.Entry)itr.next();
			int idObject = (int)inf.getKey();
			InfoAuction p = (InfoAuction)inf.getValue();
			if(p.getPlayerID() == player.getObjectId()){
				if(contador >= desde && contador < hasta){
					String Grados = "";
					try{
						Grados = GradeItem.get(p.getItemGrade());
					}catch(Exception a){
						
					}
					if(Grados==null){
						Grados="";
					}else if(Grados.isEmpty()){
						Grados = "";
					}
					
					
					
					String Enchant = "<font color=6E6E6E>+%ENCHANT%</font> ";
					if(p.getItemEnchant()>0){
						Enchant = Enchant.replace("%ENCHANT%", String.valueOf(p.getItemEnchant()));
					}else{
						Enchant = "";
					}
					
					String NombreItem = ( opera.isMasterWorkItem(p.getIdItem()) ? "<font color=LEVEL>"+ p.getItemName() +"</font>" : p.getItemName());
					
					retorno += Grilla.replace("%SALE_PRICE%", opera.getFormatNumbers(p.getQuantityRequest()));
					retorno = retorno.replace("%REQUEST_ITEM_NAME%", p.getNameItemToRequest());
					retorno = retorno.replace("%QUANTITY%", opera.getFormatNumbers(p.getQuantityItemToSell()));
					retorno = retorno.replace("%GRADE%", Grados);
					retorno = retorno.replace("%ITEM_NAME%", Enchant + " " + NombreItem);
					retorno = retorno.replace("%BTN_ITEM%", cbFormato.getBotonForm(opera.getIconImgFromItem(p.getIdItem() , true), BypassItemSee.replace("%IDOBJECTO%", String.valueOf(idObject))));
					retorno = retorno.replace("%BGCOLOR%", ColorBG[contador%2]);
				}else if(contador > hasta){
					havenext = true;
				}
				contador ++;
			}
		}
		
		if(havenext || PaginaMisVentas >0){
			String bypassAntes = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";"+ Engine.enumBypass.AuctionHouse.name() +";0;"+ String.valueOf(PaginaMisItemInventario) +";"+ String.valueOf(idObjecto) +";"+ String.valueOf(PaginaMisVentas - 1) +";0;0"; 
			String bypassProx = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";"+ Engine.enumBypass.AuctionHouse.name() +";0;"+ String.valueOf(PaginaMisItemInventario) +";"+ String.valueOf(idObjecto) +";"+ String.valueOf(PaginaMisVentas + 1) +";0;0";
			
			String btnAntes = "<button  action=\""+ bypassAntes +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\">";
			String btnProx = "<button  action=\""+ bypassProx +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\">";

			retorno += "<table width=512 cellspacing=2 cellpadding=2 border=0 bgcolor=012B2A><tr><td fixwidth=200 align=RIGHT>"+
				(PaginaMisVentas > 0 ? btnAntes : "" ) +"</td><td fixwidth=112 align=center>"+
                "<font name=HS12>"+ (PaginaMisVentas + 1) +"</font></td><td fixwidth=200 align=LEFT>"+
                (havenext ? btnProx : "") + "</td></tr></table>";
			
		}
				
		
		
		
		return retorno;
	}
	
	private static void buyItemAuction(L2PcInstance playerComprador, int idObject){
		
	}
	
	private static void removeAuctionFromBD(L2PcInstance player, int IdObject, boolean fromItemTable, int IdPlayerDueño){
		String consulta = "delete from zeus_auctions_house where idObjeto=? and idOwner=?";
		try (Connection conn = ConnectionFactory.getInstance().getConnection();
				PreparedStatement statementt = conn.prepareStatement(consulta))
			{
				statementt.setInt(1, IdObject);
				statementt.setInt(2, IdPlayerDueño);
				statementt.execute();
			}
			catch (Exception e)
			{
				
			}
		
		if(fromItemTable){
			consulta = "delete from items where owner_id=? and object_id=?";
			try (Connection conn = ConnectionFactory.getInstance().getConnection();
					PreparedStatement statementt = conn.prepareStatement(consulta))
				{
					statementt.setInt(1, ID_OWNER_AUCTION);
					statementt.setInt(2, IdObject);
					statementt.execute();
				}
				catch (Exception e)
				{
					
				}
		}
	}
	
	
	private static boolean buyitProcess(L2PcInstance player, int idObject, Long Quantity){
		
		if(AUCTION_ITEM != null){
			if(AUCTION_ITEM.containsKey(idObject)){
				InfoAuction ItemAuc = AUCTION_ITEM.get(idObject) ;
				int idItemRequeridos = ItemAuc.getIDItemRequested();
				Long CantidadRequeridos = ItemAuc.getQuantityRequest();
				int CantidadAVender = ItemAuc.getQuantityItemToSell();
				long TotalRequestItem = Quantity * CantidadRequeridos;
				if(opera.haveItem(player, idItemRequeridos, TotalRequestItem)){
					if(!ItemAuc.getItemInstance().isStackable()){
						opera.removeItem(idItemRequeridos, CantidadRequeridos, player);
						L2ItemInstance ItemToGive = AUCTION_ITEM.get(idObject).getItemInstance();
						player.getInventory().addItem("Auction House", ItemToGive, player, null);
						int idPlayerSeller = ItemAuc.getPlayerID();
						if(opera.isCharInGame(idPlayerSeller)){
							L2PcInstance t_p_s = opera.getPlayerbyID(idPlayerSeller);
							opera.giveReward(t_p_s, idItemRequeridos, TotalRequestItem);
							central.msgbox_Lado("Player " + player.getName() + " buy you the " + ItemToGive.getName() , t_p_s, "AUCTION H.");
						}else{
							createofflineSell(player, ItemAuc, Quantity);
						}
						removeAuctionFromBD(player, idObject,true,ItemAuc.getPlayerID());
						AUCTION_ITEM.remove(idObject);
						return true;
					}else{
						if(CantidadAVender >= Quantity){
							opera.removeItem( idItemRequeridos, TotalRequestItem, player);
							opera.giveReward(player, ItemAuc.getItemInstance().getItem().getId(), Quantity);
							if(Quantity!=CantidadAVender){
								ItemAuc.RemoveQuantity((long) Quantity);
							}else{
								removeAuctionFromBD(player, idObject,true,ItemAuc.getPlayerID());
								AUCTION_ITEM.remove(idObject);								
							}
						}else{
							central.msgbox("You enter the wrog quantity to buy. Please Check", player);
							return false;
						}
						if(opera.isCharInGame(ItemAuc.getPlayerID())){
							L2PcInstance t_p_s = opera.getPlayerbyID(ItemAuc.getPlayerID());
							opera.giveReward(t_p_s, idItemRequeridos, TotalRequestItem);
							central.msgbox_Lado("Player " + player.getName() + " buy you " + String.valueOf(Quantity) + " " + ItemAuc.getItemName() , t_p_s, "AUCTION H.");
						}else{
							createofflineSell(player, ItemAuc,Quantity);
						}
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private static boolean removeAuction(L2PcInstance player, int idObject){
		InfoAuction p = AUCTION_ITEM.get(idObject);
		int feePay = p.getFee();
		int Cantidad = p.getQuantityItemToSell();
		int idItem = p.getIdItem();
		L2ItemInstance item = p.getItemInstance();
		
		if(item.isStackable()){
			opera.giveReward(player, idItem, Cantidad);
			opera.giveReward(player, 57, feePay);
			removeAuctionFromBD(player, idObject,true,player.getObjectId());
		}else if(!item.isStackable()){
			player.getInventory().addItem("Auction House", item, player, null);
			opera.giveReward(player, 57, feePay);
			removeAuctionFromBD(player, idObject,false,player.getObjectId());
		}
		try{
			AUCTION_ITEM.remove(idObject);
		}catch(Exception a){
			return false;
		}
		
		return true;
		
	}
	
	private static String getMainWindows_List(L2PcInstance player, int Pagina, String _Grado, String Tipo, String Busqueda){
		
		Vector<String> GradosV = new Vector<String>();
		GradosV.add("All");
		GradosV.add("S84");
		GradosV.add("S80");
		GradosV.add("S");
		GradosV.add("A");
		GradosV.add("B");
		GradosV.add("C");
		GradosV.add("D");
		GradosV.add("NONE");
		
		String GradoAqui = "";
		if(GradeList!=null){
			if(GradeList.containsKey(player.getObjectId())){
				GradoAqui = GradeList.get(player.getObjectId());
				if(GradoAqui.equals("0")){
					GradoAqui = "All";
				}
			}
		}
		
		
		String GradosLista = GradoAqui;
		
		
		for (String GR : GradosV){
			if(!GR.equalsIgnoreCase(GradoAqui)){
				GradosLista += ";" + GR;
			}
		}
		
		String BypassBusGrado = "bypass -h voice .AuCtIoNsCaS gradelist $cmbItemRequest " + Tipo + " " + (Busqueda.equals("0") ? "-1" : Busqueda);
		String BypassBusWord = "bypass -h voice .AuCtIoNsCaS searchword $cmbItemRequest " + Tipo + " $txtWord";
		String BypassRefresh = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() +";1;0;0;0;0;0";
		String retorno ="<table border=0 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 height=30><tr><td fixwidth=70>Grade List</td><td fixwidth=70><combobox width=70 var=\"cmbItemRequest\" list=\""+GradosLista+"\">"+
        "</td><td fixwidth=50><button value=\"Apply\" action=\""+ BypassBusGrado +"\" width=50 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"+
        "</td><td fixwidth=158 align=RIGHT>Search Word</td><td fixwidth=200><edit var=\"txtWord\" width=200></td><td fixwidth=80>"+
        "<button value=\"Search\" action=\""+ BypassBusWord +"\" width=80 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>"+
        "<td fixwidth=80><button value=\"Refresh\" action=\""+ BypassRefresh +"\" width=80 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";

		retorno += "<table width=740 border=0 background=L2UI_CT1.Windows_DF_TooltipBG><tr>"+
        "<td fixwidth=170>"+ auction_Search_getTypes(player, Tipo, GradoAqui) +"</td>"+
        "<td fixwidth=560><table width=560 border=0 background=L2UI_CT1.Windows_DF_TooltipBG  height=310>"+
        "<tr><td fixwidth=560>"+
		auction_Search_getAllAuction(player, Tipo, GradoAqui, Busqueda, Pagina) +
        "</td></tr></table>%CONTROLES_PAGINA%</td></tr></table>";
		
		String Controles = "";
		
		if(strControlPagina!=null){
			if(strControlPagina.containsKey( player.getObjectId() )){
				Controles = strControlPagina.get(player.getObjectId());
			}
		}
		retorno = retorno.replace("%CONTROLES_PAGINA%", Controles);
		return retorno;
	}
	
	
	private static String getMainWindows(L2PcInstance player, int Pagina, int idObjectItem, int paginaMisItemAuction){
		String retorno ="<center>"+
				"<table width=750 border=0 height=80 cellspacing=0 cellpadding=0 bgcolor=141A12>"+
				"<tr>"+
					"<td fixwidth=198><br><center>Auctionable items</center>"+
							getInventaryWindows(player,Pagina,idObjectItem)+
							"<table width=220 border=0 cellspacing=1 cellpadding=2 background=L2UI_CT1.Windows_DF_TooltipBG>"+
							"<tr><td fixwidth=220><center>Item to be Auctioned</center></td></tr>"+
							"<tr><td fixwidth=220 height=80>"+
                              "<center>"+
                              		getSelectedItemToAuction(player,idObjectItem)+
                              		getSellBox(player,Pagina,idObjectItem)+
                              "</center>"+
                      "</td>"+
                "</tr>"+
                "<tr>"+
                	"<td fixwidth=220><center>"+
                	getFeeAndYouAdena(player, idObjectItem)+
                	"</td>"+
                "</tr>"+
               "</table><br>"+
           "</td>"+
           "<td fixwidth=450>"+
           "<table width=450>"+
           "<tr>"+
           "<td fixwidth=450>"+
           getMyAuctionList(player,paginaMisItemAuction,Pagina,idObjectItem)+
           
           "</td>"+
           "</tr>"+
           "</table>"+
           "</td>"+
           "</tr>"+
           "</table>"+
           "</center>";
           
		return retorno;
	}
	
	
	public static void getAllItem(){
		getAllItem(true, null, 0);
	}
	
	
	public static void getAllItem(boolean isAutomatic, L2PcInstance player, int idItem){
	
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT object_id, item_id, count, enchant_level, loc, loc_data, custom_type1, custom_type2, mana_left, time FROM items WHERE owner_id=? ORDER BY loc_data"))
			{
				statement.setInt(1, ID_OWNER_AUCTION);
				try (ResultSet inv = statement.executeQuery())
				{
					L2ItemInstance item;
					while (inv.next())
					{
						if(isAutomatic || (!isAutomatic && (player.getObjectId() == inv.getInt(1) && idItem == inv.getInt(2)))){
							item = L2ItemInstance.restoreFromDb(ID_OWNER_AUCTION, inv);
							if (item == null)
							{
								continue;
							}
							try{
								L2World.getInstance().removeObject(item);
							}catch(Exception a){
							}
							
							L2World.getInstance().storeObject(item);
							
							InfoAuction V = new InfoAuction(item.getObjectId(),item);
							
							if(AUCTION_ITEM!=null){
								if(AUCTION_ITEM.containsKey(item.getObjectId())){
									AUCTION_ITEM.remove(item.getObjectId());
								}
							}
							
							String mm = item.getItem().getItemGrade().name();
	
							AUCTION_ITEM.put(item.getObjectId(), V);
						}
					}
				}
			}
			catch (Exception e)
			{
				_log.warning("Could not restore inventory: " + e.getMessage());
			}
	}
	
	
	private static String mainHtml(L2PcInstance player, String Params, int PaginaInventario, int idObjectItem, int PaginaMisItem){
		String par = Engine.enumBypass.AuctionHouse.name();
		String retorno = "<html><body><title>Auction House System</title><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
		String ByPassList = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + enumBypass.AuctionHouse.name() + ";1;0;0;0;0;0;0";
		String ByPassMy = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + enumBypass.AuctionHouse.name() + ";0;0;0;0;0;0;0";
		retorno += "<table widht=770><tr>"+
        "<td fixwith=385><button value=\"Auction List\" action=\""+ ByPassList +"\" width=380 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>"+
        "<td fixwith=385><button value=\"Your Auctions\" action=\""+ ByPassMy +"\" width=380 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>"+
        "</tr></table>";
		
		if(Params.split(";").length>1){
			if(Params.split(";")[2].equals("0")){
				retorno += getMainWindows(player,PaginaInventario,idObjectItem, PaginaMisItem);
			}else if(Params.split(";")[2].equals("1")){
				String Grado = Params.split(";")[3];
				String Tipo = Params.split(";")[4];
				String Pagina = Params.split(";")[5];
				String Busqueda = Params.split(";")[6];
				retorno += getMainWindows_List(player, Integer.valueOf(Pagina), Grado, Tipo, Busqueda);
			}
		}else{
			retorno += getMainWindows(player,PaginaInventario,idObjectItem, PaginaMisItem);
		}
		retorno += "</body></html>";
		return retorno;
	}
	
	private static int getIdItemFromItemRequest(String ItemRequest){
		return getIdFromItemRequest(ItemRequest);
	}
	
	public static void voiceByPass(L2PcInstance player, String params){
		
		if(params==null){
			central.msgbox("Input error, Try Again.", player);
			return;
		}
		
		if(params.length()==0){
			central.msgbox("Input error, Try Again.", player);
			return;			
		}
		String []cm = params.split(" ");
		if(cm[0].equals("BuyItNow")){
			int idItem = Integer.valueOf(cm[1]);
			Long CantidadComprar = Long.valueOf(cm[2]);
			if(buyitProcess(player, idItem, CantidadComprar)){
				String Retornar = bypass(player, "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() +";1;0;0;0;0;0");
				cbManager.separateAndSend(Retornar, player);
			}
		}else if(cm[0].equals("create")){
			int Pagina = Integer.valueOf(cm[1]);
			int idObjeto = Integer.valueOf(cm[2]);
			int Cantidad_A_Vender = Integer.valueOf(cm[3]);
			int TipoMonedaPide = getIdItemFromItemRequest(cm[4]);
			Long ValorPorCadaItemA_Vender = Long.parseLong(cm[5]);
			//createAuctionWithItem(player,idObjeto,TipoMonedaPide,CantidadVender,PrecioVender);
			createAuctionWithItem(player, idObjeto, TipoMonedaPide, (long) Cantidad_A_Vender, ValorPorCadaItemA_Vender );
			String htmlCB = mainHtml(player,"",Pagina,0,0);
			cbManager.separateAndSend(htmlCB, player);
		}else if(cm[0].equals("gradelist")){
			if(GradeList!=null){
				if(GradeList.containsKey( player.getObjectId() )){
					GradeList.remove(player.getObjectId());					
				}
			}
			GradeList.put(player.getObjectId(), cm[1]);
			String TipoMenu = cm[2];
			String ByPassEnviar = general.COMMUNITY_BOARD_ENGINE_PART_EXEC +  ";" + Engine.enumBypass.AuctionHouse.name() + ";1;"+ cm[1] +";"+ TipoMenu +";0;0;0";
			String retorno = bypass(player, ByPassEnviar);
			cbManager.separateAndSend(retorno, player);
		}else if(cm[0].equals("searchword")){
			String Grado_ = "All";
			if(GradeList!=null){
				if(GradeList.containsKey( player.getObjectId() )){
					Grado_ = GradeList.get( player.getObjectId() );					
				}
			}
			try{
				PalabraBuscar.remove(player.getObjectId());
			}catch(Exception a){
				
			}
			String _BString = "";
			int Cont = 3;
			for (int cont=3 ; cont <= cm.length - 1 ; cont++ ){
				_BString += " " + cm[cont];
			}
			_BString =  _BString.trim(); 
			PalabraBuscar.put(player.getObjectId(), _BString);
			//_log.warning("Palabra Guardada = " + _BString);
			String TipoMenu = cm[2];
			String ByPassEnviar = general.COMMUNITY_BOARD_ENGINE_PART_EXEC +  ";" + Engine.enumBypass.AuctionHouse.name() + ";1;"+ Grado_ +";"+ TipoMenu +";0;"+ PalabraBuscar +";0";
			String retorno = bypass(player, ByPassEnviar);
			cbManager.separateAndSend(retorno, player);
			
		/*
		 * String BypassBusGrado = "bypass -h voice .AuCtIoNsCaS gradelist $cmbItemRequest " + Busqueda;
		 * String BypassBusWord = "bypass -h voice .AuCtIoNsCaS searchword $cmbItemRequest $txtWord";
		 */
			
			
		}
	}
	
	private static String auction_Search_getAllAuction(L2PcInstance player, String tipo, String _Grade, String Buscar, int Pagina){
		
		
		HashMap<String,String> GradeItem = new HashMap<String, String>();
		GradeItem.put("S84", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_84\" width=16 height=16>");
		GradeItem.put("S80", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_80\" width=16 height=16>");
		GradeItem.put("S", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_S\" width=16 height=16>");
		GradeItem.put("A", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_A\" width=16 height=16>");
		GradeItem.put("B", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_B\" width=16 height=16>");
		GradeItem.put("C", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_C\" width=16 height=16>");
		GradeItem.put("D", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_D\" width=16 height=16>");
		GradeItem.put("", "");		
				
		
		String Retorno = "<table cellspacing=0 cellpadding=0 border=0><tr><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td fixwidth=240 align=center><button value=\"Item to Sell\" width=240 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td fixwidth=20 align=center><button value=\"G\" width=20 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td fixwidth=80 align=center><button value=\"Quantity\" width=80 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter>"+
		"</td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td fixwidth=85 align=center><button value=\"I. Request\" width=85 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td fixwidth=110 align=center><button value=\"Sale Price\" width=110 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td></tr></table>";
				
		String Grilla ="<table cellspacing=0 cellpadding=0 border=0 bgcolor=%BGCOLOR%>"+
		"<tr><td fixwidth=32 height=42>%ICONO_LINK%</td>"+
		"<td fixwidth=216 align=center>%NOMBRE%</td>"+
		"<td fixwidth=21 align=center>%GRADO%</td>"+
		"<td fixwidth=87 align=center>%CANTIDA_VENDER%</td>"+
		"<td fixwidth=85 align=center>%ITEM_REQUEST_NAME%</td>"+
		"<td fixwidth=112 align=center>%SALE_PRICE%</td></tr></table>";

		String []BGColor = {"031C1C","011313"};
		
		int Contador=0;
		int MaximoLista = 7;
		boolean haveNext = false;
		String ByPass_item = "";
		int desde = MaximoLista * Pagina;
		int hasta = desde + MaximoLista;
		
		String _strBuscar = "";
		
		if(PalabraBuscar!=null){
			if(PalabraBuscar.containsKey( player.getObjectId() )){
				_strBuscar = PalabraBuscar.get( player.getObjectId() );
			}
		}
		
		
		String tipoIndex = "";
		
		if(TipoSearch_index!=null){
			if(TipoSearch_index.containsKey(player.getObjectId())){
				tipoIndex = TipoSearch_index.get(player.getObjectId());
			}
		}
		

		String ItemByPass = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";showItemWindows;%IDOBJETO%;0;0;0;0";

		String GradeIN = "";
		
		Iterator itr = AUCTION_ITEM.entrySet().iterator();
		while(itr.hasNext() && !haveNext){
			Map.Entry inf = (Map.Entry)itr.next();
			int idObject = (int)inf.getKey();
			InfoAuction p = (InfoAuction)inf.getValue();
			
			
			
			
			if(GradeList!=null){
				if(GradeList.containsKey(player.getObjectId())){
					GradeIN =  GradeList.get(player.getObjectId());
				}else{
					GradeIN = "All";
				}
			}
			
			boolean Grabar = true;
			
			if(!GradeIN.equals("0") && !GradeIN.equalsIgnoreCase("All")){
				if(!p.getItemGrade().equalsIgnoreCase(GradeIN)){
					Grabar = false;
					continue;
				}
			}
			
			if( _strBuscar.length()>0 ){
				if( p.getItemName().toUpperCase().indexOf( _strBuscar.toUpperCase() )<0 ){
					Grabar=false;
					continue;
				}
			}
			
			if(tipoIndex.length()>0){
				if(!TypeSearch_addToList(p,tipoIndex)){
					continue;
				}
			}
			
			//PalabraBuscar
			
			
			
			if(Grabar){
				String Grados = "";
				Grados = GradeItem.get(p.getItemGrade());
				if(Grados==null){
					Grados="";
				}else if(Grados.isEmpty()){
					Grados = "";
				}
				
				
				if(Contador >= desde && Contador <hasta){
					Integer[]Atri = new Integer[6];
					
					Atri[5] = p.getDivineAtributte();
					Atri[4] = p.getDarkAtributte();
					Atri[2] = p.getEarthAtributte();
					Atri[3] = p.getWindAtributte();
					Atri[1] = p.getWaterAtributte();
					Atri[0] = p.getFireAtributte();
					
					if(p.getItemInstance().isWeapon()){
						if(p.getItemInstance().getAttackElementType()>=0){
							Atri[p.getItemInstance().getAttackElementType()] = p.getItemInstance().getAttackElementPower();
						}
					}
					
					
                    String A_Fire = Atri[0]>0 ? "<font color=AB3737>Fire:"+ String.valueOf(Atri[0]) +"</font> ": "";
                    String A_Water = Atri[1]>0 ? "<font color=4965CB>Water:"+ String.valueOf(Atri[1]) +"</font> ": "";
                    String A_Earth = Atri[2]>0 ? "<font color=A9803F>Earth:"+ String.valueOf(Atri[2]) +"</font> ": "";
                    String A_Wind = Atri[3]>0 ? "<font color=4C9961>Wind:"+ String.valueOf(Atri[3]) +"</font> ": "";
                    String A_Dark = Atri[4]>0 ? "<font color=8E3FAD>Dark:"+ String.valueOf(Atri[4]) +"</font> ": "";
                    String A_Divine = Atri[5]>0 ? "<font color=808080>Divine:"+ String.valueOf(Atri[5]) +"</font> ": "";
					
                    String ElementalItem = (A_Fire + A_Water + A_Earth + A_Wind + A_Dark + A_Divine).trim();
                    
                    
                    
					String Nombre = (p.getItemEnchant()>0 ? "<font color=5D6C5E>+"+ String.valueOf( p.getItemEnchant() ) + "</font> " : "" ) + ( opera.isMasterWorkItem(p.getIdItem()) ? "<font color=LEVEL>" +  p.getItemName() + "</font>" : p.getItemName());
					Nombre += "<br1>" + ElementalItem;
					
					Retorno += Grilla.replace("%SALE_PRICE%", opera.getFormatNumbers(p.getQuantityRequest())).replace("%ITEM_REQUEST_NAME%", p.getNameItemToRequest()) .replace("%CANTIDA_VENDER%", opera.getFormatNumbers(p.getQuantityItemToSell())) .replace("%GRADO%", Grados).replace("%NOMBRE%", Nombre) .replace("%ICONO_LINK%", cbFormato.getBotonForm(opera.getIconImgFromItem(p.getIdItem(), true), ItemByPass.replace("%IDOBJETO%", String.valueOf(p.getItemInstance().getObjectId()))) ) .replace("%BGCOLOR%", BGColor[Contador%2]);
				}else{
					if(Contador>=hasta){
						haveNext=true;
					}
				}
				Contador ++;
			}else{
				
			}
		}
		
		
		if(strControlPagina!=null){
			if(strControlPagina.containsKey(player.getObjectId())){
				strControlPagina.remove( player.getObjectId() );
			}
		}
		
		
		if(Pagina>0 || haveNext){
			String ByPass_Prev = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";1;" + GradeIN + ";" + tipo + ";" + String.valueOf( Pagina - 1 ) + ";" + _strBuscar + ";0";
			String ByPass_Next = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";1;" + GradeIN + ";" + tipo + ";" + String.valueOf( Pagina + 1 ) + ";" + _strBuscar + ";0";
			String strRetorno = "<table width=562 border=0 background=L2UI_CT1.Windows_DF_TooltipBG>"+
	        "<tr><td fixwidth=220 align=RIGHT>"+
			(Pagina>0 ? "<button action=\""+ ByPass_Prev +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\">" : "") + "</td>"+
	        "<td fixwidth=120 align=CENTER><font name=hs12 color=91DEFA>"+ String.valueOf(Pagina+ 1) +"</font></td>"+
	        "<td fixwidth=220 align=LEFT>"+
	        (haveNext ? "<button action=\""+ ByPass_Next +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\">" : "") + "</td>"+
	        "</tr></table>";
			strControlPagina.put(player.getObjectId(),strRetorno);
		}else{
			strControlPagina.put(player.getObjectId(),"");
		}
		
		return Retorno;
	}
	
	private static String auction_Search_getTypes(L2PcInstance player, String Tipo, String _Grade){
		
		/*String ByPassIn = "bypass " + 
		 * general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";1;" + GradeList.get(player.getObjectId()) + ";" + parm3 + ";0;0;0";*/
		
		
		String Grade = "All";
		if(GradeList!=null){
			if(GradeList.containsKey( player.getObjectId() )){
				Grade = GradeList.get( player.getObjectId() );
			}
		}
		
		
		
		String ByPass_TYPE_JEWEL = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";1;" + Grade + ";J;0;0;0";
		String ByPass_TYPE_ARMOR = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";1;" + Grade + ";A;0;0;0";
		String ByPass_TYPE_WEAPON = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";1;" + Grade + ";W;0;0;0";
		
		String html = "<table width=170 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=16></td><td fixwidth=154><font name=hs12 color=8FFFBC>Type to Search</font><br></td></tr><tr><td fixwidth=16>"+
		"<button action=\""+ ByPass_TYPE_JEWEL +"\" width=16 height=16 back=\"L2UI_CT1.Inventory_DF_Btn_Align\" fore=\"L2UI_CT1.Inventory_DF_Btn_Align\"></td><td fixwidth=154>"+
		"Jewels Type<br1>%JOYAS%<br></td></tr><tr><td fixwidth=16>"+
        "<button action=\""+ ByPass_TYPE_ARMOR +"\" width=16 height=16 back=\"L2UI_CT1.Inventory_DF_Btn_Align\" fore=\"L2UI_CT1.Inventory_DF_Btn_Align\"></td><td fixwidth=154>"+
        "Armor Type<br1>%ARMOR%<br></td></tr><tr><td fixwidth=16>"+
        "<button action=\""+ ByPass_TYPE_WEAPON +"\" width=16 height=16 back=\"L2UI_CT1.Inventory_DF_Btn_Align\" fore=\"L2UI_CT1.Inventory_DF_Btn_Align\"></td><td fixwidth=154>"+
        "Weapon Type<br1>%WEAPON%<br></td></tr></table><br>";
		
		/*String ByPassIn = "bypass " + 
		 * general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";1;" + GradeList.get(player.getObjectId()) + ";" + parm3 + ";0;0;0";*/		

		String ByPass_RING= "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";-1;JEWEL_RING;" + Tipo + ";0;0;0";
		String ByPass_EARRING= "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";-1;JEWEL_EARRING;" + Tipo + ";0;0;0";
		String ByPass_NECKLACE= "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";-1;JEWEL_NECKLACE;" + Tipo + ";0;0;0";
		String TypoJoyas = "<table width=150 cellspacing=0 cellpadding=0><tr><td><button value=\"Ring\" action=\""+ ByPass_RING +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><tr><td><button value=\"Earring\" action=\""+ ByPass_EARRING +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><tr><td><button value=\"Necklace\" action=\""+ ByPass_NECKLACE +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";
		
		
		
		String ByPassArmor = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";-1;ARMOR_%TIP%;" + Tipo + ";0;0;0";
		
		String TypoArmor = "<table width=150 cellspacing=0 cellpadding=0>"+
        "<tr><td><button value=\"Helmet\" action=\""+ ByPassArmor.replace("%TIP%", "HELMET") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
        "<tr><td><button value=\"Chest\" action=\""+ ByPassArmor.replace("%TIP%", "CHEST") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
        "<tr><td><button value=\"Legs\" action=\""+ ByPassArmor.replace("%TIP%", "LEGS") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
        "<tr><td><button value=\"Gloves\" action=\""+ ByPassArmor.replace("%TIP%", "GLOVES") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
        "<tr><td><button value=\"Shoes\" action=\""+ ByPassArmor.replace("%TIP%", "SHOES") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
        "<tr><td><button value=\"Cloak\" action=\""+ ByPassArmor.replace("%TIP%", "CLOAK") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
        "<tr><td><button value=\"Shirt\" action=\""+ ByPassArmor.replace("%TIP%", "SHIRT") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
        "<tr><td><button value=\"Belt\" action=\""+ ByPassArmor.replace("%TIP%", "BELT") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
        "<tr><td><button value=\"Sigil\" action=\""+ ByPassArmor.replace("%TIP%", "SIGIL") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
        "<tr><td><button value=\"Shield\" action=\""+ ByPassArmor.replace("%TIP%", "SHIELD") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";
		

		String ByPassWaepon = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";-1;WEAPON_%TIP%;" + Tipo + ";0;0;0";
		
		String TypoWeapon = "<table width=150 cellspacing=0 cellpadding=0>"+
			"<tr><td><button value=\"Sword\" action=\""+ ByPassWaepon.replace("%TIP%", "SWORD") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
			"<tr><td><button value=\"Dual Sword\" action=\""+ ByPassWaepon.replace("%TIP%", "DUALSWORD") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
			"<tr><td><button value=\"Big Sword\" action=\""+ ByPassWaepon.replace("%TIP%", "BIGSWORD") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
			"<tr><td><button value=\"Ancient Sword\" action=\""+ ByPassWaepon.replace("%TIP%", "ANCIENTSWORD") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
			"<tr><td><button value=\"Blunt\" action=\""+ ByPassWaepon.replace("%TIP%", "BLUNT") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
			"<tr><td><button value=\"Big Blunt\" action=\""+ ByPassWaepon.replace("%TIP%", "BIGBLUNT") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
			"<tr><td><button value=\"Dagger\" action=\""+ ByPassWaepon.replace("%TIP%", "DAGGER") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
			"<tr><td><button value=\"Dual Dagger\" action=\""+ ByPassWaepon.replace("%TIP%", "DUALDAGGER") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
			"<tr><td><button value=\"Bow\" action=\""+ ByPassWaepon.replace("%TIP%", "BOW") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
			"<tr><td><button value=\"Crossbow\" action=\""+ ByPassWaepon.replace("%TIP%", "CROSSBOW") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
			"<tr><td><button value=\"Pole\" action=\""+ ByPassWaepon.replace("%TIP%", "POLE") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
			"<tr><td><button value=\"Fists\" action=\""+ ByPassWaepon.replace("%TIP%", "FISTS") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
			"<tr><td><button value=\"Rapier\" action=\""+ ByPassWaepon.replace("%TIP%", "RAPIER") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>"+
			"<tr><td><button value=\"Other\" action=\""+ ByPassWaepon.replace("%TIP%", "OTHER") +"\" width=144 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";

		TipoSearch.put(player.getObjectId(), Tipo);
		
		if(Tipo.equalsIgnoreCase("J")){
			html = html.replace("%JOYAS%", TypoJoyas).replace("%ARMOR%", "").replace("%WEAPON%", "");
		}else if(Tipo.equalsIgnoreCase("A")){
			html = html.replace("%JOYAS%", "").replace("%ARMOR%", TypoArmor).replace("%WEAPON%", "");
		}else if(Tipo.equalsIgnoreCase("W")){
			html = html.replace("%JOYAS%", "").replace("%ARMOR%", "").replace("%WEAPON%", TypoWeapon);
		}else if(Tipo.equalsIgnoreCase("0")){
			html = html.replace("%JOYAS%", "").replace("%ARMOR%", "").replace("%WEAPON%", "");
		}
		
		//_log.warning(html);
		
		
		return html;
	}
	
	private static void giveItemOffline(L2PcInstance player, int idInterno){
		if(SELLOFFLINER!=null){
			if(SELLOFFLINER.containsKey(player.getObjectId())){
				if(SELLOFFLINER.get(player.getObjectId())!=null){
					if(SELLOFFLINER.get(player.getObjectId()).size()>0){
						int idItemToGive = Integer.valueOf(SELLOFFLINER.get(player.getObjectId()).get(idInterno).get("ID_ITEM_REQUEST")); 
						Long CantidadToGive = Long.valueOf(SELLOFFLINER.get(player.getObjectId()).get(idInterno).get("TOTAL_ITEM_A_ENTREGAR").replace(".0", ""));
						int idItemVendido = Integer.valueOf(SELLOFFLINER.get(player.getObjectId()).get(idInterno).get("ID_ITEM_SOLD"));

						String Consulta = "delete from zeus_auctions_house_offline where idChar=? and idItemVendido=?";
						//_log.warning("Consulta->" + Consulta);
						try (Connection conn = ConnectionFactory.getInstance().getConnection();
								PreparedStatement statementt = conn.prepareStatement(Consulta))
							{
								statementt.setInt(1, player.getObjectId());
								statementt.setInt(2, idItemVendido);
								statementt.execute();
							}
							catch (Exception e)
							{
								_log.warning("Error ZeuS E->" + e.getMessage());
							}
						opera.giveReward(player, idItemToGive, CantidadToGive);

						SELLOFFLINER.get(player.getObjectId()).remove(idInterno);
					}
				}
			}
		}
		
	}
	
	
	public static String bypass(L2PcInstance player, String params){
		_log.warning(params);
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		//String ByPass = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse + ";GiveItem;" + String.valueOf(IdInterno) + ";0;0;0;0;0";
		
		if(parm1.equals("GiveItem")){
			giveItemOffline(player, Integer.valueOf(parm2));
			haveItemToWidthdraw(player,false);
		}else if(parm1.equals("-1")){
			try{
				TipoSearch_index.remove(player.getObjectId());
			}catch(Exception a){
				
			}
			TipoSearch_index.put(player.getObjectId(), parm2);
			String ByPassIn = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.AuctionHouse.name() + ";1;" + GradeList.get(player.getObjectId()) + ";" + parm3 + ";0;0;0";
			return mainHtml(player,ByPassIn,0,0,0);
		}else if(parm1.equals("0")){
			return mainHtml(player,params,Integer.valueOf(parm2),Integer.valueOf(parm3), Integer.valueOf(parm4));
		}else if(parm1.equals("1")){
			if(parm2.equals("0")){
				try{
					GradeList.remove(player.getObjectId());
				}catch(Exception a){
					
				}
				try{
					PalabraBuscar.remove(player.getObjectId());
				}catch(Exception a){
					
				}
				try{
					TipoSearch_index.remove(player.getObjectId());
				}catch(Exception a){
					
				}
			}
			
			return mainHtml(player,params,0,0,0);
		}else if(parm1.equals("showItemWindows")){
			getInfoItemWindows(player, Integer.valueOf(parm2));
		}else if(parm1.equals("cancelauction")){
			if(!removeAuction(player,Integer.valueOf(parm2))){
				getInfoItemWindows(player, Integer.valueOf(parm2));
			}else{
				central.sendHtml(player, "<html><title>"+ general.TITULO_NPC() +"</title><body>DONE!!!</body></html>");
				mainHtml(player,params,0,0,0);				
			}
		}else if(parm1.equals("buyItNow")){
			//_log.warning("Comprando");
			buyitProcess(player,Integer.valueOf(parm2),Long.valueOf(parm3));
		}
		return "";
	}
	
	public static void createCheckOfflineSell(L2PcInstance player){
		ThreadPoolManager.getInstance().scheduleGeneral(new CheckSellOfflinePlayer(player), 5  * 1000);
	}
	
	
	private static class CheckSellOfflinePlayer implements Runnable{
		L2PcInstance activeChar_;
		int LastR_ID=0;
		public CheckSellOfflinePlayer(L2PcInstance player){
			activeChar_ = player;
		}
		@Override
		public void run(){
			try{
				if(!opera.isCharInGame(activeChar_.getObjectId())){
					return;
				}
				haveItemToWidthdraw(activeChar_, true);
			}catch(Exception a){
				_log.warning("Error ZeuS A->" + a.getLocalizedMessage() + "->" + a.getMessage());
			}

		}
	}
	
	
}

class InfoAuction{
	private String ITEM_NAME;
	private String ITEM_GRADE;
	private int ITEM_ID_OBJECT;
	private int ITEM_ENCHANT;
	private int ITEM_PRICE_L2J;
	
	private L2ItemInstance ITEM_INSTANCE;
	
	private int SELL_QUANTITY_ITEM_TO_SELL;
	private Long SELL_PRICE_ITEM_BY_1;
	private int SELL_ITEM_REQUEST_ID;
	private String SELL_ITEM_REQUEST_NAME;
	private String PLAYER_NAME;
	private int PLAYER_ID;
	private int SELL_FEE;
	
	private final Logger _log = Logger.getLogger(v_auction_house.class.getName());
	
	private void saveInBD(){
		
		String Consulta = "update zeus_auctions_house set ItemQuantityToSell=? where idObjeto=? and idOwner=?";
		
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = ConnectionFactory.getInstance().getConnection();
			ins = con.prepareStatement(Consulta);
			ins.setLong(1, this.SELL_QUANTITY_ITEM_TO_SELL);
			ins.setInt(2, this.ITEM_ID_OBJECT );
			ins.setInt(3, this.PLAYER_ID);
			try{
				ins.executeUpdate();
			}catch(SQLException e){
				_log.warning("Error ZeuS E->" + e.getMessage());
			}
		}catch(Exception a){
			
		}
		try{
			ins.close();
			con.close();			
		}catch(Exception a){
			
		}
		
		Consulta = "update items set count=? where owner_id=? and object_id=?";
		try{
			con = ConnectionFactory.getInstance().getConnection();
			ins = con.prepareStatement(Consulta);
			ins.setLong(1, this.SELL_QUANTITY_ITEM_TO_SELL);
			ins.setInt(2, v_auction_house.ID_OWNER_AUCTION);
			ins.setInt(3, this.ITEM_ID_OBJECT);
			try{
				ins.executeUpdate();
				ins.close();
				con.close();
			}catch(SQLException e){
				_log.warning("Error ZeuS E->" + e.getMessage());
			}
		}catch(Exception a){
			
		}
		
		
	}
	
	public void RemoveQuantity(Long Quantity){
		this.SELL_QUANTITY_ITEM_TO_SELL = (int) (this.SELL_QUANTITY_ITEM_TO_SELL - Quantity);
		saveInBD();
	}
	
	private String ElementalType[] = {"Fire","Water","Earth","Wind","Dark","Holy"};
	
	public String getIconImgID(){
		return opera.getIconImgFromItem(this.ITEM_INSTANCE.getItem().getId(), true);
	}
	
	public int getIDItemRequested(){
		return this.SELL_ITEM_REQUEST_ID;
	}
	
	public L2ItemInstance getItemInstance(){
		return this.ITEM_INSTANCE;
	}
	
	public int getDivineAtributte(){
		try{
			return this.ITEM_INSTANCE.getElementDefAttr(Elementals.HOLY);
		}catch(Exception a){
			return 0;
		}
	}
	
	public boolean isChaosItem(){
		int idItem = this.ITEM_INSTANCE.getItem().getId();
		return opera.isMasterWorkItem(idItem);
	}
	
	public int getDarkAtributte(){
		try{
			//return this.ITEM_INSTANCE.getArmorItem().getElemental(Elementals.DARK).getValue();
			return this.ITEM_INSTANCE.getElementDefAttr(Elementals.DARK);
		}catch(Exception a){
			return 0;
		}
	}
	
	public int getFireAtributte(){
		try{
			//return this.ITEM_INSTANCE.getArmorItem().getElemental(Elementals.FIRE).getValue();
			return this.ITEM_INSTANCE.getElementDefAttr(Elementals.FIRE);
		}catch(Exception a){
			return 0;
		}
	}
	
	public int getWaterAtributte(){
		try{
			//return this.ITEM_INSTANCE.getArmorItem().getElemental(Elementals.WATER).getValue();
			return this.ITEM_INSTANCE.getElementDefAttr(Elementals.WATER);
		}catch(Exception a){
			return 0;
		}
	}
	
	public int getEarthAtributte(){
		try{
			//return this.ITEM_INSTANCE.getArmorItem().getElemental(Elementals.EARTH).getValue();
			return this.ITEM_INSTANCE.getElementDefAttr(Elementals.EARTH);
		}catch(Exception a){
			return 0;
		}
	}
	public int getWindAtributte(){
		try{
			//return this.ITEM_INSTANCE.getArmorItem().getElemental(Elementals.WIND).getValue();
			return this.ITEM_INSTANCE.getElementDefAttr(Elementals.WIND);
		}catch(Exception a){
			return 0;
		}
	}
	
	public String getAtributteElementalType_weapon(){
		return ElementalType[this.ITEM_INSTANCE.getAttackElementType()];
	}
	
	public int getAtributteElementalPower_weapon(){
		return this.ITEM_INSTANCE.getAttackElementPower();
	}
	
	public String getElementalTypeFromWeapon(){
		return ElementalType[this.ITEM_INSTANCE.getAttackElementType()];
	}
	
	public Long getQuantityRequest(){
		return this.SELL_PRICE_ITEM_BY_1;
	}
	public String getNameItemToRequest(){
		return this.SELL_ITEM_REQUEST_NAME;
	}
	
	public int getQuantityItemToSell(){
		return this.SELL_QUANTITY_ITEM_TO_SELL;
	}
	
	public String getItemName(){
		return this.ITEM_NAME;
	}
	
	public String getItemGrade(){
		return this.ITEM_GRADE;
	}
	
	public boolean isPlayerOnline(){
		try{
			return opera.isCharInGame(this.PLAYER_ID);
		}catch(Exception a){
			return false;
		}
	}
	
	public String getSellerName(){
		return this.PLAYER_NAME;
	}
	
	public int getItemEnchant(){
		return this.ITEM_ENCHANT;
	}
	
	public int getPlayerID(){
		return this.PLAYER_ID;
	}
	
	public int getIdItem(){
		return this.ITEM_INSTANCE.getItem().getId();
	}
	
	public int getFee(){
		return this.SELL_FEE;
	}
	
	private void getInfo(int idObjectitem){
		String Consulta = "SELECT zeus_auctions_house.idObjeto, zeus_auctions_house.idOwner, zeus_auctions_house.idItemRequest, zeus_auctions_house.ItemRequestQuantity, zeus_auctions_house.ItemQuantityToSell, zeus_auctions_house.startUnix, characters.char_name, zeus_auctions_house.feed FROM zeus_auctions_house INNER JOIN characters ON zeus_auctions_house.idOwner = characters.charId WHERE zeus_auctions_house.idObjeto = ?";

		
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(Consulta))
			{
				statement.setInt(1, idObjectitem);
				try (ResultSet inv = statement.executeQuery())
				{
					L2ItemInstance item;
					if (inv.next())
					{
						this.PLAYER_ID = inv.getInt("idOwner");
						this.PLAYER_NAME = inv.getString("char_name");
						this.SELL_PRICE_ITEM_BY_1 = inv.getLong("ItemRequestQuantity");
						this.SELL_QUANTITY_ITEM_TO_SELL = inv.getInt("ItemQuantityToSell");
						this.SELL_ITEM_REQUEST_ID = inv.getInt("idItemRequest");
						this.SELL_ITEM_REQUEST_NAME = central.getNombreITEMbyID(inv.getInt("idItemRequest"));
						this.SELL_FEE = inv.getInt("feed");
					}
				}
			}
			catch (Exception e)
			{
				_log.warning("Erro ZeuS E->" + e.getMessage());
			}
		
		
		
	}
	
	public InfoAuction() {
	}
	public InfoAuction(int idObjectItem, L2ItemInstance item){
		this.ITEM_ID_OBJECT = idObjectItem;
		this.ITEM_INSTANCE = item;
		this.ITEM_NAME = item.getName();
		this.ITEM_GRADE = item.getItem().getItemGrade().name(); 
		this.ITEM_ENCHANT = item.getEnchantLevel();
		
		getInfo(idObjectItem);
	}	
}
