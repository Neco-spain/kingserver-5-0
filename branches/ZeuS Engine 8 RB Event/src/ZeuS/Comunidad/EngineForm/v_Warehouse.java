package ZeuS.Comunidad.EngineForm;

import com.l2jserver.gameserver.model.ClanPrivilege;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ActionFailed;
import com.l2jserver.gameserver.network.serverpackets.SortedWareHouseWithdrawalList;
import com.l2jserver.gameserver.network.serverpackets.SortedWareHouseWithdrawalList.WarehouseListType;
import com.l2jserver.gameserver.network.serverpackets.WareHouseDepositList;
import com.l2jserver.gameserver.network.serverpackets.WareHouseWithdrawalList;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;

public class v_Warehouse {
	
	private static String getWareFormato(){
		
		String ByPass_Private_Deposit = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" +Engine.enumBypass.Warehouse + ";depo_ware_per;0;0;0;0;0" ;
		String ByPass_Private_Withdraw = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" +Engine.enumBypass.Warehouse + ";saca_ware_per;0;0;0;0;0" ;
		String ByPass_Clan_Deposit = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" +Engine.enumBypass.Warehouse + ";depo_ware_clan;0;0;0;0;0" ;
		String ByPass_Clan_Withdraw = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" +Engine.enumBypass.Warehouse + ";saca_ware_clan;0;0;0;0;0" ;
		
		/*String retorno ="<table width=760 background=L2UI_CT1.Windows_DF_Drawer_Bg><tr><td></td></tr>"+
        "<tr><td width=760 align=center ><table width=700 background=\"L2UI_CT1.Windows_DF_TooltipBG\" height=50 cellpadding=5><tr>"+
        "<td width=32 alignt=CENTER><img src=\"icon.etc_treasure_box_i08\" width=32 height=32><br></td><td width=500><table width=500>"+
        "<tr><td align=\"LEFT\"><font name=\"hs12\" color=\"FFCC00\">Private Warehouse</font></td></tr><tr><td><font color=\"99CCFF\">Deposit your selected Item's</font></td>"+
        "</tr></table></td><td width=\"168\" align=RIGHT><button value=Deposit action=\""+ ByPass_Private_Deposit +"\" width=188 height=32 back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>"+
        "</td></tr></table><br><table width=700 background=\"L2UI_CT1.Windows_DF_TooltipBG\" height=50 cellpadding=5><tr><td width=32 alignt=CENTER><img src=\"icon.skill0629\" width=32 height=32><br>"+
        "</td><td width=500><table width=500><tr><td align=\"LEFT\"><font name=\"hs12\" color=\"FFCC00\">Private Warehouse</font></td></tr><tr><td><font color=\"99CCFF\">Withdraw your selected Item's</font></td>"+
        "</tr></table></td><td width=\"168\" align=RIGHT><button value=\"Withdraw\" action=\""+ ByPass_Private_Withdraw +"\" width=188 height=32 back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td></tr></table>"+
        "<br><br><img src=\"L2UI.SquareGray\" width=720 height=3><br><br><table width=700 background=\"L2UI_CT1.Windows_DF_TooltipBG\" height=50 cellpadding=5><tr><td width=32 alignt=CENTER><img src=\"icon.etc_treasure_box_i08\" width=32 height=32><br>"+
        "</td><td width=500><table width=500><tr><td align=\"LEFT\"><font name=\"hs12\" color=\"FFCC00\">Clan Warehouse</font></td></tr><tr><td><font color=\"99CCFF\">Deposit your Clan selected Item's</font></td></tr></table>"+
        "</td><td width=\"168\" align=RIGHT><button value=\"Deposit\" action=\""+ ByPass_Clan_Deposit +"\" width=188 height=32 back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td></tr></table><br><table width=700 background=\"L2UI_CT1.Windows_DF_TooltipBG\" height=50 cellpadding=5><tr><td width=32 alignt=CENTER><img src=\"icon.skill0629\" width=32 height=32><br>"+
        "</td><td width=500><table width=500><tr><td align=\"LEFT\"><font name=\"hs12\" color=\"FFCC00\">Clan Warehouse</font></td></tr><tr><td><font color=\"99CCFF\">Withdraw your Clan selected Item's</font></td></tr></table>"+
        "</td><td width=\"168\" align=RIGHT><button value=\"Withdraw\" action=\""+ ByPass_Clan_Withdraw +"\" width=188 height=32 back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td></tr></table><br></td></tr></table>";
        */

		String retorno = "<table width=760 background=L2UI_CT1.Windows_DF_Drawer_Bg><tr><td></td></tr><tr><td width=760 align=center ><center>";
		
		retorno += cbFormato.getBarra1("icon.etc_treasure_box_i08", "Private Warehouse", "Deposit your selected item's", ByPass_Private_Deposit, "Deposit") + "<br>";
		
		retorno += cbFormato.getBarra1("icon.skill0629", "Private Warehouse", "Withdraw your selected item's", ByPass_Private_Withdraw, "Withdraw") + "<br><br>";
		
		retorno += cbFormato.getBarra1("icon.etc_treasure_box_i08", "Clan Warehouse", "Deposit your clan selected item's", ByPass_Clan_Deposit, "Deposit") + "<br>";
		
		retorno += cbFormato.getBarra1("icon.skill0629", "Clan Warehouse", "Withdraw you selected clan item's", ByPass_Clan_Withdraw, "Widthdraw") + "<br>";
		
		retorno += "</td></tr></table></center>";
		

		return retorno;
		
	}
	
	private static final void showPWithdrawWindow(L2PcInstance player, WarehouseListType itemtype, byte sortorder)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
		player.setActiveWarehouse(player.getWarehouse());
		
		if (player.getActiveWarehouse().getSize() == 0)
		{
			player.sendPacket(SystemMessageId.NO_ITEM_DEPOSITED_IN_WH);
			return;
		}
		
		if (itemtype != null)
		{
			player.sendPacket(new SortedWareHouseWithdrawalList(player, WareHouseWithdrawalList.PRIVATE, itemtype, sortorder));
		}
		else
		{
			player.sendPacket(new WareHouseWithdrawalList(player, WareHouseWithdrawalList.PRIVATE));
		}
	}
	
	
	private static final void showCWithdrawWindow(L2PcInstance player, WarehouseListType itemtype, byte sortorder)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
		
		if (!player.hasClanPrivilege(ClanPrivilege.CL_VIEW_WAREHOUSE))
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_THE_RIGHT_TO_USE_CLAN_WAREHOUSE);
			return;
		}
		
		player.setActiveWarehouse(player.getClan().getWarehouse());
		
		if (player.getActiveWarehouse().getSize() == 0)
		{
			player.sendPacket(SystemMessageId.NO_ITEM_DEPOSITED_IN_WH);
			return;
		}
		
		if (itemtype != null)
		{
			player.sendPacket(new SortedWareHouseWithdrawalList(player, WareHouseWithdrawalList.CLAN, itemtype, sortorder));
		}
		else
		{
			player.sendPacket(new WareHouseWithdrawalList(player, WareHouseWithdrawalList.CLAN));
		}
	}
	
	private static String mainHtml(L2PcInstance player, String Params){
		String par = Engine.enumBypass.Warehouse.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		retorno += getWareFormato();
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
		}else if(parm1.equals("depo_ware_per")){
			player.sendPacket(ActionFailed.STATIC_PACKET);
			player.setActiveWarehouse(player.getWarehouse());
			if (player.getWarehouse().getSize() == player.getWareHouseLimit())
			{
				player.sendPacket(SystemMessageId.WAREHOUSE_FULL);
			}
		//	player.setIsUsingAioWh(true);
		//	player.tempInventoryDisable();
			player.sendPacket(new WareHouseDepositList(player, WareHouseDepositList.PRIVATE));		
			
		}else if(parm1.equals("saca_ware_per")){
		//	player.setIsUsingAioWh(true);
			showPWithdrawWindow(player, WarehouseListType.ALL, SortedWareHouseWithdrawalList.A2Z);
			
		}else if(parm1.equals("depo_ware_clan")){
			if (player.getClan() == null)
			{
				player.sendPacket(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER);
				return "";
			}
			player.sendPacket(ActionFailed.STATIC_PACKET);
			player.setActiveWarehouse(player.getClan().getWarehouse());
			
			if (player.getClan().getLevel() == 0)
			{
				player.sendPacket(SystemMessageId.ONLY_LEVEL_1_CLAN_OR_HIGHER_CAN_USE_WAREHOUSE);
				return "";
			}
		//	player.setIsUsingAioWh(true);
			player.setActiveWarehouse(player.getClan().getWarehouse());
			player.isInventoryDisabled();
			player.sendPacket(new WareHouseDepositList(player, WareHouseDepositList.CLAN));			
		}else if(parm1.equals("saca_ware_clan")){
		//	player.setIsUsingAioWh(true);
			showCWithdrawWindow(player, WarehouseListType.ALL, SortedWareHouseWithdrawalList.A2Z);
		}
		
		return "";
	}
}
