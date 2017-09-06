package ZeuS.procedimientos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.l2jserver.Config;
import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.GameServer;
import com.l2jserver.gameserver.data.xml.impl.AdminData;
import com.l2jserver.gameserver.data.xml.impl.ExperienceData;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.instancemanager.PunishmentManager;
import com.l2jserver.gameserver.model.ClanPrivilege;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.punishment.PunishmentAffect;
import com.l2jserver.gameserver.model.punishment.PunishmentTask;
import com.l2jserver.gameserver.model.punishment.PunishmentType;
import com.l2jserver.gameserver.model.skills.AbnormalVisualEffect;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ActionFailed;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.SortedWareHouseWithdrawalList;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.network.serverpackets.SortedWareHouseWithdrawalList.WarehouseListType;
import com.l2jserver.gameserver.network.serverpackets.WareHouseWithdrawalList;
import com.l2jserver.gameserver.util.Broadcast;

import ZeuS.Comunidad.cbManager;
import ZeuS.Comunidad.EngineForm.cbFormato;
import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.central;
import ZeuS.interfase.pinCode;
import ZeuS.interfase.sellBuff;


public class opera {

	private static final Logger _log = Logger.getLogger(GameServer.class.getName());

	public static Vector<String> getAllItemByName(String nameSearch){
		return getAllItemByName(nameSearch,false);
	}
	
	public static boolean isMasterWorkItem (int idItem){
		if(idItem>=10870 && idItem <= 11352){
			return true;
		}else if(idItem >= 11353 && idItem <= 11392){
			return true;
		}else if(idItem >= 11393 && idItem <= 11435){
			return true;
		}else if(idItem >= 11436 && idItem <= 11470){
			return true;
		}else if(idItem >= 11471 && idItem <= 11504){
			return true;
		}else if((idItem >= 11505 && idItem <= 11547) || (idItem >= 11552 && idItem <= 11574)){
			return true;
		}else if( (idItem >= 11575 && idItem <= 11604) ){
			return true;
		}else if(idItem >= 12852 && idItem <= 13001){
			return true;
		}else if(idItem >= 14412 && idItem <= 14460){
			return true;
		}else if(idItem >= 14526 && idItem <= 14529){
			return true;
		}else if(idItem >= 16042 && idItem <= 16097){
			return true;
		}else if(idItem >= 16134 && idItem <= 16151){
			return true;
		}else if(idItem >= 16719 && idItem <= 16220){
			return true;
		}else if(idItem >= 16289 && idItem <= 16356){
			return true;
		}else if(idItem >= 16369 && idItem <= 16380){
			return true;
		}else if(idItem >= 16837 && idItem <= 16851){
			return true;
		}else if((idItem >= 21927 && idItem <= 21934) || ( idItem >= 21937 && idItem <= 21938 )){
			return true;
		}else if((idItem >= 21947 && idItem <= 21954) || (idItem >= 21957 && idItem <= 21958)){
			return true;
		}else if( (idItem >= 21966 && idItem <= 21972) || (idItem >= 21977 && idItem <= 21980)){
			return true;
		}else if(idItem >= 10120 && idItem <=10121){
			return true;
		}
		
		
		
		return false;
	}
	
	public static void setBlockSaveInDB(L2PcInstance player){
		general.setStatusCanSave(player, true);
	}
	
	public static void setUnBlockSaveInDB(L2PcInstance player){
		general.setStatusCanSave(player, false);
	}
	
	public static String getBotonForm(String imagen, String ByPass){
		return cbFormato.getBotonForm(imagen, ByPass); 
	}
	
	public static boolean setDif(boolean metod){
		if(metod){
			return false;
		}
		return true;
	}	
	
	public static boolean exitIDItem(int ID){
		try{
			String t = ItemTable.getInstance().getTemplate(ID).getName();
			if(t!=null){
				if(t.length()>0){
					return true;
				}
			}
		}catch(Exception a){
			
		}
		return false;
	}
	
	public static String getChancePorcentaje(String Chance){
		return String.valueOf((100 * Integer.valueOf(850200)) / 1000000.0);
	}
	
	public static void setPlayerConfig(String Status,L2PcInstance player){
		switch (Status){
			case "expsp":
				general.getInstance().setConfigExpSp(player, setDif(general.getCharConfigEXPSP(player)));
				central.setAllConfigCharToBD(player);
				break;
			case "tradec":
				general.getInstance().setConfigTrade(player, setDif(general.getCharConfigTRADE(player)));
				central.setAllConfigCharToBD(player);
				break;
			case "noBuff":
				general.getInstance().setConfigBadBuff(player, setDif(general.getCharConfigBADBUFF(player)));
				central.setAllConfigCharToBD(player);
				break;
			case "hideStore":
				general.getInstance().setConfigHideStore(player, setDif(general.getCharConfigHIDESTORE(player)));
				central.setAllConfigCharToBD(player);
				break;
			case "partymatching":
				general.getInstance().setConfigPartyMatching(player, setDif(general.getCharConfigPartyMatching(player)));
				central.setAllConfigCharToBD(player);
				break;
			case "showmystat":
				general.getInstance().setConfigSHOWSTAT(player, setDif(general.getCharConfigSHOWSTAT(player)));
				central.setAllConfigCharToBD(player);
				break;
			case "effectpvppk":
				general.getInstance().setConfigEFFECT(player, setDif(general.getCharConfigEFFECT(player)));
				central.setAllConfigCharToBD(player);
				break;
			case "annoupvppk":
				general.getInstance().setConfigANNOU(player, setDif(general.getCharConfigANNOU(player)));
				central.setAllConfigCharToBD(player);
				break;
			case "Refusal":
				general.getInstance().setConfigREFUSAL(player, setDif(general.getCharConfigREFUSAL(player)));
				central.setAllConfigCharToBD(player);
				if(general.getCharConfigREFUSAL(player)){
					player.getBlockList().setBlockAll(player, true);
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.MESSAGE_REFUSAL_MODE));
				}else{
					player.getBlockList().setBlockAll(player, false);
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.MESSAGE_ACCEPTANCE_MODE));
				}
				break;
		}		
	}
	
	public static String getHoraActual(){
		Date date = new Date();
		DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
		return hourFormat.format(date); 		
	}
	
	
	public static boolean isCharInGame(int idChar){
		try{
			L2PcInstance p = getPlayerbyID(idChar);
			if(p!=null){
				return true;
			}
		}catch(Exception a){
			
		}
		return false;
	}
	
	public static String getFechaActual(){
		Date date = new Date();
		DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date) + " - " + hourFormat.format(date); 		
	}
	
	public static int getOffliners(){
		Vector<String> PlayerNameOnlineWorld = opera.getAllNamePlayerOnWorld();
		int offliners = 0;
		for(String playerName : PlayerNameOnlineWorld){
			L2PcInstance player = opera.getPlayerbyName(playerName);
			if(player!=null){
				if(!player.isGM()){
					if(player.getClient().isDetached()){
						offliners++;
					}
				}
			}
		}
		return offliners;
	}
	
	
	public static int getOnlinePlayer(){
		int onlinePlayer = L2World.getInstance().getAllPlayersCount();
		
		if(!general.COMMUNITY_BOARD_REGION){
			onlinePlayer += general.PLAYER_BASE_TO_SHOW;
		}
		
		return onlinePlayer;
	}
	
	
	public static void GiveRewardAllConnect(L2PcInstance player, String RewardToGive){
		Vector<String>ipLanRewarded = new Vector<String>();
		Collection<L2PcInstance> players = opera.getAllPlayerOnWorld();
		if(player==null){
			return;
		}
		String ipPlayer = ZeuS.getIp_Wan(player);
		String ipLan = ZeuS.getIp_pc(player);
		ipLanRewarded.add(ipLan);
		String ipForPlayer = "";
		String ipForPlayerLAN = "";
		for(L2PcInstance chars: players){
			ipForPlayerLAN = "";
			if(chars!=null){
				if(chars.isOnline() && !chars.getClient().isDetached()){
					try{
						ipForPlayer = ZeuS.getIp_Wan(chars);
						ipForPlayerLAN = ZeuS.getIp_pc(chars);
						if(ipPlayer.equalsIgnoreCase(ipForPlayer)){
							if(chars != player && !ipLanRewarded.contains(ipForPlayerLAN) ) {
								opera.giveReward(chars,RewardToGive);
								ipLanRewarded.add(ipForPlayerLAN);
							}
						}
					}catch(Exception a){

					}
				}
			}
		}		
	}	
	
	
	public static int getServerID(L2PcInstance player){
		int retorno = 1;
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("select lastServer from "+general.LOGINSERVERNAME+".accounts where login=?"))
			{
				statement.setString(1, player.getAccountName());
				try (ResultSet rset = statement.executeQuery())
				{
					if (rset.next())
					{
						retorno = rset.getInt(1);
					}
				}
			}
			catch (Exception e)
			{
				_log.warning("Cannot select user Last Server Information: Exception");
				_log.warning(e.getMessage());
			}

			return retorno;
		
	}

	public static boolean emailHasBannedAccount(L2PcInstance player, String emailRegistro){
		boolean retorno = false;
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("select email from "+general.LOGINSERVERNAME+".accounts where email=? and accessLevel < 0"))
			{
				statement.setString(1, emailRegistro);
				try (ResultSet rset = statement.executeQuery())
				{
					if (rset.next())
					{
						retorno = true;
					}
				}
			}
			catch (Exception e)
			{
				_log.warning("Cannot select user mail: Exception");
				_log.warning(e.getMessage());
			}

			return retorno;


	}

	public static Vector<String> getAllItemByName(String nameSearch, boolean forDropSearch){
		Vector<String> ItemSelect = new Vector<String>();
		String NomTemp = "";

		for(int i=0;i<=65557;i++){
			try{
				NomTemp = central.getNombreITEMbyID(i,false,forDropSearch);
				if(!NomTemp.equals("NULL")){
					if(NomTemp.toUpperCase().indexOf(nameSearch.toUpperCase())>=0){
						ItemSelect.add(NomTemp+"@"+String.valueOf(i));
					}else if(nameSearch.equalsIgnoreCase(String.valueOf(i))){
						ItemSelect.add(NomTemp+"@"+String.valueOf(i));
					}
				}
			}catch(Exception a){

			}

		}

		try{
			Collections.sort(ItemSelect);
		}catch(Exception a){

		}

		return ItemSelect;
	}

	public static boolean canUseCBFunction(L2PcInstance player){
		if(player.isOnEvent()){
			return false;
		}
		if(player.isInOlympiadMode()){
			return false;
		}
		if(player.isOlympiadStart()){
			return false;
		}
		
		return true;
	}

	public static void summonAll(L2PcInstance player, String Params){
		summonAll(false, player,Params);
	}

	public static void setImmbileChar(L2PcInstance player, boolean block){
		if(block){
			player.hasAbnormalVisualEffect(AbnormalVisualEffect.ROOT);
			player.setIsParalyzed(true);
			player.setIsInvul(true);
		}else{
			player.stopAbnormalVisualEffect(true, AbnormalVisualEffect.ROOT);
			if(!player.isGM()){
				player.setIsInvul(false);
			}
			player.setIsParalyzed(false);
		}
	}

	public static String getClassName(int idClass){
		HashMap<Integer, String> clases = general.getClases();
		return clases.get(idClass);
	}

	public static boolean setUserEmailtoBD(L2PcInstance player, String Email){
		String Consulta = "Update " + general.LOGINSERVERNAME + ".accounts set " + general.LOGINSERVERNAME + ".accounts.email=? where " + general.LOGINSERVERNAME + ".accounts.login=?";
		Connection conn = null;
		PreparedStatement psqry = null;
		boolean retorno = true;
		try{
			conn=ConnectionFactory.getInstance().getConnection();
			psqry=conn.prepareStatement(Consulta);
			psqry.setString(1, Email);
			psqry.setString(2, player.getAccountName());
			try{
				psqry.executeUpdate();
				psqry.close();

			}catch(SQLException e){
				_log.warning("ZeuS Error Email Registration Update BD-> " + e.getMessage() + " ->" + player.getName() + ", Acc->" + player.getAccountName());
				return false;
			}
		}catch (SQLException e) {
		}
		try{
			conn.close();
		}catch (SQLException e2) {

		}
		return retorno;
	}

	public static String getUserMail(String username)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("select email from "+general.LOGINSERVERNAME+".accounts where login=?"))
		{
			statement.setString(1, username);
			try (ResultSet rset = statement.executeQuery())
			{
				if (rset.next())
				{
					String mail = rset.getString(1);
					return mail;
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("Cannot select user mail: Exception");
			_log.warning(e.getMessage());
		}

		return null;
	}

	public static boolean canShowHTMLRad(L2PcInstance player){
		try{
			if(general.IS_USING_NPC.get(player.getObjectId()) && !general.IS_USING_CB.get(player.getObjectId())){
				L2Object Objetotarget = player.getTarget();
				if(Objetotarget instanceof L2Npc){
					if(!player.isInsideRadius(Objetotarget, general.RADIO_PLAYER_NPC_MAXIMO, true, true)){
						return false;
					}
				}else{
					return false;
				}
			}
			return true;
		}catch(Exception a){
			if(player.isGM()){
				return true;
			}else{
				return false;
			}
		}
	}

	public static boolean isAccountPremium_BD(String AccountName){
		return isPremium_BD(AccountName, "CHAR");
	}

	public static boolean isClanPremium_BD(String IdClan){
		if(IdClan==null){
			return false;
		}
		return isPremium_BD(IdClan , "CLAN");
	}


	public static boolean isAccountPremium_BD(L2PcInstance player){
		return isPremium_BD(player.getAccountName(), "CHAR");
	}

	public static boolean isClanPremium_BD(L2PcInstance player){
		if(player.getClan()==null){
			return false;
		}
		return isPremium_BD(String.valueOf(player.getClan().getId()) , "CLAN");
	}

	private static boolean isPremium_BD(String Busqueda, String tipo){
		boolean retorno = false;
		String consulta = "SELECT * FROM zeus_premium WHERE zeus_premium.id = ? AND zeus_premium.tip = ? AND zeus_premium.end_date >= UNIX_TIMESTAMP();";
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			psqry.setString(1, Busqueda);
			psqry.setString(2, tipo);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				retorno = true;
			}
		}catch (SQLException e){
			_log.warning("ZeuS Error getting Premium from BD -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}
		return retorno;
	}

	public static Collection<L2PcInstance> getAllPlayerOnWorld(){
		return L2World.getInstance().getPlayers();
	}

	public static Vector<L2PcInstance> getAllPlayersOnThisRegion(L2PcInstance player){
		Vector<L2PcInstance>charPlayer = new Vector<L2PcInstance>();
		for(L2PcInstance cha : opera.getAllPlayerOnWorld()){
			if(opera.isInGeoZone(cha, player)){
				charPlayer.add(cha);
			}
		}
		return charPlayer;
	}

	public static Vector<String> getAllNamePlayerOnRegion(L2PcInstance player){
		Vector<String> NamePlayer = new Vector<String>();
		for(L2PcInstance cha : getAllPlayersOnThisRegion(player)){
			NamePlayer.add(cha.getName());
		}
		return NamePlayer;
	}

	public static Vector<String> getAllNamePlayerOnWorld(){
		Vector<String> NamePlayer = new Vector<String>();
		for(L2PcInstance cha : getAllPlayerOnWorld()){
			NamePlayer.add(cha.getName());
		}
		return NamePlayer;
	}


	public static void getInfoPlayer(String NomChar, L2PcInstance playerVisor){
		String retorno = "<html><title>ZeuS Engine Player Information</title><body>";
		retorno+= central.LineaDivisora(1)+central.headFormat(NomChar + " Information",cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name())) + central.LineaDivisora(1) + central.LineaDivisora(3);
		HashMap<String,String> Informacion = new HashMap<String, String>();
		L2PcInstance playerSelected = opera.getPlayerbyName(NomChar);

		if(playerSelected != null){
			if((playerVisor != playerSelected) && playerVisor.isGM() && playerSelected.isOnline() ){
				Informacion.put("Name:", "<font color=" + cbManager.getFontColor(cbManager.enumColor.Celeste.name()) + "><a action=\"bypass -h admin_move_to "+ String.valueOf(playerSelected.getLocation().getX()) +" "+ String.valueOf(playerSelected.getLocation().getY()) +" "+ String.valueOf(playerSelected.getLocation().getZ()) +"\">" +  NomChar + "</a></font>");
			}else{
				Informacion.put("Name:", "<font color=" + cbManager.getFontColor(cbManager.enumColor.Celeste.name()) + ">" +  NomChar + "</font>");
			}			
		}else{
			Informacion.put("Name:", "<font color=" + cbManager.getFontColor(cbManager.enumColor.Celeste.name()) + ">" +  NomChar + "</font>");
		}
		
		
		String formatoFilas = "<table width=270 align=center bgColor=%COLOR%><tr><td width=135 align=CENTER>%TITULO%</td><td width=135 align=CENTER>%DATO%</td></table>" + central.LineaDivisora(1);
		String[] ColorG = { cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_BRILLANTE.name()), cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name()) };

		String strMySql = "SELECT level, sex, exp, sp, karma," +
							" pkkills, pvpkills, clanid, " +
							"IFNULL((SELECT CONCAT(clan_data.clan_name,\":\",clan_data.clan_level) FROM clan_data WHERE clan_data.clan_id = clanid),\"NONE:0\")," +
							" classid, base_class, title, onlinetime, nobless," +
							" IFNULL((SELECT 'YES' FROM heroes WHERE heroes.charId = characters.charId AND heroes.played = 1),'NO'), account_name FROM characters WHERE char_name=?";
		Connection conn = null;
		PreparedStatement psqry = null;
		boolean haveNext = false;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(strMySql);
			psqry.setString(1, NomChar);
			ResultSet rss = psqry.executeQuery();
			while (rss.next() && !haveNext){
				try{
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Level:"))
						Informacion.put("Level:", String.valueOf(rss.getInt(1)));
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Sex:"))
						Informacion.put("Sex:", rss.getInt(2)==0 ? "Man" : "Women");
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Exp:"))
						Informacion.put("Exp:", String.valueOf(rss.getBigDecimal(3)));
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Sp:"))
						Informacion.put("Sp:", String.valueOf(rss.getInt(4)));
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("karma:"))
						Informacion.put("Karma:", String.valueOf(rss.getInt(5)));
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Pk Kills:"))
						Informacion.put("Pk Kills:", String.valueOf(rss.getInt(6)));
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("PvP Kills:"))
						Informacion.put("PvP Kills:", String.valueOf(rss.getInt(7)));
					Informacion.put("--ClanID:", String.valueOf(rss.getInt(8)));
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Clan Name:"))
						Informacion.put("Clan Name:", rss.getString(9).split(":")[0]);
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Clan Lv:"))
						Informacion.put("Clan Lv:", rss.getString(9).split(":")[1]);
					
					Informacion.put("--ClassID:", String.valueOf(rss.getInt(10)));
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Class:"))
						Informacion.put("Class:", opera.getClassName(rss.getInt(10)));
					Informacion.put("--BaseID:", String.valueOf(rss.getInt(11)));
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Base:"))
						Informacion.put("Base:", opera.getClassName(rss.getInt(11)));
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Title:"))
						Informacion.put("Title:", rss.getString(12));
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Life Time:"))
						Informacion.put("Life Time:", opera.getTiempoON(rss.getInt(13)));
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Online Time:"))
						Informacion.put("Online Time:","00:00:00");
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Noble:"))
						Informacion.put("Noble:", rss.getInt(14)==0?"NO":"YES");
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Heore:"))
						Informacion.put("Heroe:", rss.getString(15));
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Is Premium Acc:"))
						Informacion.put("Is Premium Acc:", isAccountPremium_BD(rss.getString(16)) ? "YES" : "NO" );
					if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Is Premiun Clan:"))
						Informacion.put("Is Premium Clan:", isClanPremium_BD(rss.getString(9)) ? "YES" : "NO" );
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}

		if(playerSelected != null){
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Level:"))
				Informacion.put("Level:", String.valueOf(playerSelected.getLevel()));
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Sex:"))
				Informacion.put("Sex:", playerSelected.getAppearance().getSex() ? "Women" : "Man");
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Exp:"))
				Informacion.put("Exp:", String.valueOf(playerSelected.getExp()));
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Sp:"))
				Informacion.put("Sp:", String.valueOf(playerSelected.getSp()));
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Karma:"))
				Informacion.put("Karma:", String.valueOf(playerSelected.getKarma()));
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Pk Kills:"))
				Informacion.put("Pk Kills:", String.valueOf(playerSelected.getPkKills()));
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("PvP Kills:"))
				Informacion.put("PvP Kills:", String.valueOf(playerSelected.getPvpKills()));
			Informacion.put("--ClanID:", String.valueOf(playerSelected.getId()));
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Clan Name:"))
				Informacion.put("Clan Name:", playerSelected.getClan()==null ? "None" : playerSelected.getClan().getName());
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Clan Lv:"))
				Informacion.put("Clan Lv:", playerSelected.getClan()==null ? "0" : String.valueOf(playerSelected.getClan().getLevel()));
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Title:"))
				Informacion.put("Title:", playerSelected.getTitle());
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Noble:"))
				Informacion.put("Noble:", playerSelected.isNoble() ? "YES" : "NO");
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Heroe:"))
				Informacion.put("Heroe:", playerSelected.isHero() ? "YES" : "NO");
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Is Premium Acc:"))
				Informacion.put("Is Premium Acc:", isPremium_Player(playerSelected) ? "YES" : "NO" );
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Is Premium Clan:"))
				Informacion.put("Is Premium Clan:", isPremium_Clan(playerSelected) ? "YES" : "NO" );
			int idClaseUsando = playerSelected.getClassId().getId();
			Informacion.put("--ClassID:", String.valueOf(idClaseUsando));
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Class:"))
				Informacion.put("Class:", opera.getClassName(idClaseUsando));
			Informacion.put("--BaseID:", String.valueOf(playerSelected.getBaseClass()));
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Base:"))
				Informacion.put("Base:", opera.getClassName(playerSelected.getBaseClass()));
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Life Time:"))
				Informacion.put("Life Time:", opera.getTiempoON(playerSelected.getOnlineTime()));
			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Online Time:"))
				Informacion.put("Online Time:", general.getLifeToday(playerSelected));

			if(!general.COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.contains("Online:")){
			
				if(playerSelected.isOnline()){
					if(!playerSelected.isGM()){
						if(!playerSelected.getClient().isDetached()){
							Informacion.put("Online:", "Yes");
							if(playerSelected.isInCraftMode()){
								Informacion.put("Online:", "Yes, Craft Mode");
							}else if(playerSelected.isInStoreMode()){
								Informacion.put("Online:", "Yes, Store Mode");
							}else if(sellBuff.isBuffSeller(playerSelected)){
								Informacion.put("Online:", "Yes, Buff Store");
							}
						}else{
							Informacion.put("Online:", "No");
							if(playerSelected.isInCraftMode()){
								Informacion.put("Online:", "No, Craft Mode");
							}else if(playerSelected.isInStoreMode()){
								Informacion.put("Online:", "No, Store Mode");
							}else if(sellBuff.isBuffSeller(playerSelected)){
								Informacion.put("Online:", "No, Buff Store");
							}
						}
					}else{
						if(isGmAllVisible(playerSelected)){
							Informacion.put("Online:", "Yes");
						}else{
							Informacion.put("Online:", "No");
						}
					}
				}else{
					Informacion.put("Online:", "No");
				}
			}
		}
		
			Iterator itr = Informacion.entrySet().iterator();
			int Contador = 0;
		    while(itr.hasNext()){
		    	Map.Entry Entrada = (Map.Entry)itr.next();
		    	if(!Entrada.getKey().toString().startsWith("--")){
		    		retorno += formatoFilas.replace("%COLOR%", ColorG[Contador%2]).replace("%TITULO%", Entrada.getKey().toString()).replace("%DATO%", Entrada.getValue().toString());
		    		Contador++;
		    	}
		    }

		retorno += central.getPieHTML(false) + "</body></html>";
		opera.enviarHTML(playerVisor, retorno);
	}



	public static boolean isGmAllVisible(L2PcInstance player){
		return isGmAllVisible(player,null);
	}
	

	public static boolean isGmAllVisible(L2PcInstance player, L2PcInstance Visitante){
		boolean retorno = false;
		if(player.isGM()){
			List<L2PcInstance> gms = AdminData.getInstance().getAllGms(false);//L2World.getInstance().getAllGMs(false);
			if(gms.contains(player)){
				return true;
			}else{
				if(Visitante==null){
					return false;
				}else{
					if(player.isVisibleFor(Visitante)){
						return true;
					}
				}
			}
		}else{
			return true;
		}

		return retorno;
	}

	public static boolean setBanToAccChar(L2PcInstance player, String Razon){
		String AccountPJ = player.getAccountName();
		if(player!=null){
			PunishmentManager.getInstance().startPunishment(new PunishmentTask(AccountPJ, PunishmentAffect.ACCOUNT ,PunishmentType.BAN , System.currentTimeMillis(), Razon, "ADMIN"));
			_log.info("ZeuS Engine - "+ Razon +" - Player " + player.getName() + " and his Account "+ player.getAccountName() +" has been Banned");
			return true;
		}
		return false;
	}

	public static boolean setBanToChar(L2PcInstance player, String Razon){
			if (player != null){
				player.setAccessLevel(0);
				player.sendMessage("Your character has been banned. Goodbye.");
				player.logout();
				_log.info("ZeuS Engine - "+ Razon +" - Player " + player.getName() + " has been Banned");
				return true;
			}
			return false;
	}


	public static L2PcInstance getPlayerByName(String nomPlayer){
		L2PcInstance retorno =null;
		try
		{
			retorno  = L2World.getInstance().getPlayer(nomPlayer);
		}
		catch (Exception e)
		{

		}
		return retorno;
	}

	public static boolean isPlusChar(L2PcInstance player){

		if(general.CHAR_PLUS!=null){
			try{
				if(general.CHAR_PLUS.contains(player.getName())){
					if(general.CHAR_PLUS_IP!=null){
						if(general.CHAR_PLUS_IP.containsKey(player.getName())){
							if(general.CHAR_PLUS_IP.get(player.getName()).equals(ZeuS.getIp_Wan(player))){
								return true;
							}
						}
					}
				}
			}catch(Exception a){

			}
		}

		if(general.PLUS_C != null){
			if(general.PLUS_C.containsKey(player.getAccountName())){
				if(!general.PLUS_C.get(player.getAccountName()).get("CHAR").equals(player.getName())){
					return false;
				}else{
					if(!ZeuS.getIp_Wan(player).equals(general.IP_PLUS_C)){
						return false;
					}
				}
			}else{
				return false;
			}
		}else{
			return false;
		}

		return true;
	}

	public static void summonAll(boolean force, L2PcInstance player, String Params){
		int RadioSummon = 100;
		if(Params.split(" ").length==2){
			if(isNumeric(Params.split(" ")[1])){
				int radTempo = Integer.valueOf(Params.split(" ")[1]);
				if(radTempo>0){
					RadioSummon = radTempo;
				}
			}
		}
		Collection<L2PcInstance> pls = getAllPlayerOnWorld();
		for(L2PcInstance onlinePlayer : pls){
			if( !force &&  (onlinePlayer.isOnline() && !onlinePlayer.isOlympiadStart() && !onlinePlayer.inObserverMode() && !onlinePlayer.isInOlympiadMode() && !onlinePlayer.isIn7sDungeon())){
				if(!onlinePlayer.equals(player) && !onlinePlayer.isGM()){
					onlinePlayer.teleToLocation(player.getX(), player.getY(), player.getZ(), RadioSummon);
					central.msgbox("You are summon by a GM", onlinePlayer);
				}
			}
		}
	}

	public static boolean isInGeoZone(L2PcInstance player,L2PcInstance GMC ){

		String zoneGM = getGeoRegion(GMC.getX(), GMC.getY());
		String zonePlayer = getGeoRegion(player.getX(), player.getY());
		return zoneGM.equals(zonePlayer);

	}

	private static String getGeoRegion(int x, int y)
	{
		int worldX = x;// activeChar.getX();
		int worldY = y;// activeChar.getY();
		int geoX = ((((worldX - (-327680)) >> 4) >> 11) + 10);
		int geoY = ((((worldY - (-262144)) >> 4) >> 11) + 10);
		return geoX + "_" + geoY;
	}

	public static boolean checkCondition(L2PcInstance player, int condicion, int Cantidad, int idItem){
		boolean retorno = false;

		switch(condicion){
			case -500://pvp
				retorno = (player.getPvpKills()>=Cantidad);
				break;
			case -501://pk
				retorno = (player.getPkKills()>=Cantidad);
				break;
			case -502://fame
				retorno = (player.getFame()>=Cantidad);
				break;
			case -503://noble
				retorno = player.isNoble();
				break;
			case -504://hero
				retorno = player.isHero();
				break;
			case -505://reco
				retorno = (player.getRecomHave() >= Cantidad);
				break;
			case -506://life minutes
				retorno = (getMinutosVida(player)>=Cantidad);
				break;
			case -507://level
				retorno = (player.getLevel()>= Cantidad);
				break;
		}

		return retorno;
	}

	public static void sendToJail(L2PcInstance player, int minutos){
		int objID = player.getObjectId();
		long expirationTime = general.ANTIBOT_MINUTOS_JAIL;
		if (expirationTime > 0)
		{
			expirationTime = System.currentTimeMillis() + (expirationTime * 60 * 1000);
		}		
		
		PunishmentManager.getInstance().startPunishment(new PunishmentTask(objID, PunishmentAffect.CHARACTER,PunishmentType.JAIL , expirationTime, "Antibot Jail", "ADMIN"));
	}

	public static int getIDNPCTarget(L2PcInstance player){
		L2Object target = player.getTarget();
		if(target instanceof L2Npc){
			return ((L2Npc)target).getId();
		}
		return -1;
	}

	public static int getMinutosVida(L2PcInstance Player){
		Connection conn = null;
		int idPlayer = Player.getObjectId();
		int MinutosVida =0;
		int SegundosCap = 0;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			String qry = "SELECT characters.onlinetime FROM characters WHERE characters.charId = ?";
			PreparedStatement psqry = conn.prepareStatement(qry);
			psqry.setInt(1, idPlayer);
			ResultSet rss = psqry.executeQuery();
			if (rss.next()){
				try{
					SegundosCap = rss.getInt("onlinetime");
				}catch(SQLException e){
					SegundosCap = 0;
				}
			}
		}catch(SQLException a){

		}
		
		try{
			conn.close();
		}catch(Exception a){
			
		}
		

		MinutosVida = Integer.valueOf(SegundosCap / 60);

		return MinutosVida;
	}

	public static boolean searchInArray(String[]ArrayBuscar, String Dato){
		boolean retorno = false;

		for(String Parte: ArrayBuscar){
			if(Parte.equalsIgnoreCase(Dato)){
				return true;
			}
		}

		return retorno;
	}

	public static Map<Integer, String> getAllPlayerOnThisAccount(L2PcInstance cha){
		Map<Integer, String> Retorno = cha.getAccountChars();
		return Retorno;
	}

	public static Vector<L2PcInstance> getAllPlayerOnThisIp(L2PcInstance player){
		Vector<L2PcInstance> retorno = new Vector<L2PcInstance>();
		for(L2PcInstance cha : getAllPlayerOnWorld()){
			if(cha!=player){
				if(ZeuS.isDualBox_pc(player, cha)){
					retorno.add(cha);
				}
			}
		}
		return retorno;
	}

	public static void removeItem(Integer idItem, Long cantidadRemover,L2PcInstance player) {
		try{
			player.destroyItemByItemId("ZeuS", idItem, cantidadRemover , player, true);
		}catch(Exception a){
			_log.warning("REMOVE ITEM-> Error. No se reconocio el Item con ID " + String.valueOf(idItem));
			central.msgbox(msg.ITEM_ID_NO_ENCONTRADO, player);
		}
	}


	public static L2PcInstance getPlayerByID(int IDPlayer){
		L2PcInstance player = null;
		try
		{
			player = L2World.getInstance().getPlayer(IDPlayer);
		}
		catch (Exception e)
		{
			player=null;
		}
		return player;
	}

	public static long getUnixTimeL2JServer(){
		return System.currentTimeMillis();
	}
	
	public static int getUnixTimeNow(){
		return (int) (System.currentTimeMillis() / 1000);
	}

	public static String getDateFromUnixTime(Long Unixtime){
		Date anotherDate = new Date(Unixtime);
		String DATE_FORMAT = "dd-MMM-yyyy HH:mm";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(anotherDate);
		/*double UnixT = Unixtime * 1000.0;
		final Calendar time = Calendar.getInstance();
		time.setTimeInMillis((long)UnixT);
		String RespawnHora = time.getTime().toString();
		return RespawnHora;*/
	}

	public static String getDateFromUnixTime(int Unixtime){
		double UnixT = Unixtime * 1000.0;
		final Calendar time = Calendar.getInstance();
		time.setTimeInMillis((long)UnixT);
		String RespawnHora = time.getTime().toString();
		return RespawnHora;
	}

	public static boolean isMaster(L2PcInstance player){

		if((player.getAccessLevel().getLevel() == 127) || (player.getAccessLevel().getLevel() == 8) ){
			return true;
		}
		
		if(player.isGM()){
			return true;
		}

		int PosseAcce = Arrays.binarySearch(general.get_AccessConfig(), player.getAccessLevel().getLevel());
		if(PosseAcce>=0){
			return true;
		}
		return false;
	}

	public static L2PcInstance getPlayerbyName(String Name){
		L2PcInstance player = null;
		try
		{
			player = L2World.getInstance().getPlayer(Name);
		}
		catch (Exception e)
		{
		}

		return player;

	}
	
	public static L2PcInstance getPlayerbyID(int idPplayer){
		L2PcInstance player = null;
		try
		{
			player = L2World.getInstance().getPlayer(idPplayer);
		}
		catch (Exception e)
		{
		}

		return player;

	}



	public static boolean isValidName(String text)
	{
		boolean result = true;
		String test = text;
		Pattern pattern;
		try
		{
			pattern = Config.PLAYER_NAME_TEMPLATE;
		}
		catch (PatternSyntaxException e) // case of illegal pattern
		{
			_log.warning("ERROR : Character name pattern of config is wrong!");
			pattern = Pattern.compile(".*");
		}
		Matcher regexp = pattern.matcher(test);
		if (!regexp.matches())
		{
			result = false;
		}
		return result;
	}

	public static void AnunciarTodos(int TipoMensaje, String Titulo, String Mensaje){
		CreatureSay strMensaje = new CreatureSay(0,TipoMensaje,"","["+Titulo+"]"+Mensaje);
		Broadcast.toAllOnlinePlayers(strMensaje);
	}

	public static void AnunciarTodos(String Titulo,String Mensaje){
		AnunciarTodos(18,Titulo,Mensaje);
	}

	public static void AnunciarTodos(int TipoMensaje,String Mensaje){
		AnunciarTodos(TipoMensaje,"ZeuS",Mensaje);
	}

	public static void AnunciarTodos(String Mensaje){
		AnunciarTodos(18,"ZeuS",Mensaje);
	}

	public static boolean TOP_PVP_PK_EFFECT(L2PcInstance player){
		return general.getCharConfigEFFECT(player);
	}

	public static boolean SHOW_MY_STAT_SHIFT(L2PcInstance player){
		return general.getCharConfigSHOWSTAT(player);
	}

	public static boolean TOP_PVP_PK_ANNOU(L2PcInstance player){
		return general.getCharConfigANNOU(player);
	}

	public static boolean SHOW_PIN_WINDOWS(L2PcInstance player){
		return general.getCharConfigPIN(player);
	}

	public static boolean SHOW_HERO_PLAYER(L2PcInstance player){
		return general.getCharConfigHERO(player);
	}


	public static boolean isActivePIN(L2PcInstance player){
		return pinCode.getPinStatus(player);
	}

	public static boolean _checkIsEventServer(L2PcInstance player){
		/*if(player._inEventRaid){
			return true;
		}*/
		return false;
	}

	public static String getFormatNumbers(String Numero){
		String strNumero = "";
		if(Numero.endsWith(".0")){
			strNumero = Numero.substring(0,Numero.length()-2);
		}else{
			strNumero = Numero;
		}
		Double num = Double.parseDouble(strNumero);
		DecimalFormat formateador = new DecimalFormat("###,###,###,###,###,###.##");
		return formateador.format(num);
	}

	public static String getFormatNumbers(int Numero){
		DecimalFormat formateador = new DecimalFormat("###,###,###,###,###,###.##");
		return formateador.format(Numero);
	}

	public static String getFormatNumbers(Long Numero){
		DecimalFormat formateador = new DecimalFormat("###,###,###,###,###,###.##");
		return formateador.format(Numero);
	}


	public static boolean haveDonationItem(L2PcInstance st, int MontoSolicitado){
		boolean retorno = true;

		int ItemPosee = (int) st.getInventory().getInventoryItemCount(general.DONA_ID_ITEM,-1);

		if(ItemPosee < MontoSolicitado){
			central.msgbox("You don't have " + getFormatNumbers(MontoSolicitado) + " " + central.getNombreITEMbyID(general.DONA_ID_ITEM) + " requested.", st);
			return false;
		}

		return retorno;
	}

	public static void changeSex(L2PcInstance Player){
		if(Player.getAppearance().getSex()) {
			Player.getAppearance().setSex(false);
		} else {
			Player.getAppearance().setSex(true);
		}
		Player.broadcastUserInfo();
	}

	public static boolean setNoble(L2PcInstance Player){
		if(Player.isNoble()){
			central.msgbox(msg.USTED_YA_ES_NOBLE_NO_PUEDE_CONTINUAR, Player);
			return false;
		}else{
			Player.setNoble(true);
		}
		Player.broadcastUserInfo();
		return true;
	}
	public static void setLevel(L2PcInstance player, int Level){
		setLevel(player, String.valueOf(Level));
	}

	public static void setLevel(L2PcInstance player, String Level){
		try{
			byte lvl = Byte.parseByte(Level);
			if ((lvl >= 1) && (lvl <= ExperienceData.getInstance().getMaxLevel()))
			{
				long pXp = player.getExp();
				long tXp = ExperienceData.getInstance().getExpForLevel(lvl);

				if (pXp > tXp)
				{
					player.removeExpAndSp(pXp - tXp, 0);
				}
				else if (pXp < tXp)
				{
					player.addExpAndSp(tXp - pXp, 0);
				}
			}
			player.broadcastUserInfo();
		}catch(Exception a){

		}
	}

	public static boolean set85(L2PcInstance player){
		if(player.getLevel() == 85){
			central.msgbox(msg.USTED_YA_ES_85_NO_PUEDE_CONTINUAR, player);
			return false;
		}else{
			setLevel(player, "86");
		}
		return true;
	}

	public static void enviarHTML(L2PcInstance player, String HTML){
		enviarHTML(player,HTML,false);
	}

	public static void enviarHTML(L2PcInstance player, String HTML, boolean checkRadius){
		if(!player.isGM() && checkRadius){
			L2Object target = player.getTarget();
			if(target!=null){
				if(!ZeuS.isNpcFromZeus( opera.getIDNPCTarget(player) )){
					return;
				}
			}
		}
		player.sendPacket(new NpcHtmlMessage(0,HTML));
	}

	public static long ContarItem(L2PcInstance player, int IDItem){
		Long IteminChar = player.getInventory().getInventoryItemCount(IDItem,-1);
		return IteminChar;
	}
	
	public static boolean haveItem(L2PcInstance st, int IDItemSolicita,Long CantidadNecesita) {
		return haveItem(st, IDItemSolicita,CantidadNecesita, true);
	}
	
	public static boolean haveItem(L2PcInstance st, int IDItemSolicita,int CantidadNecesita) {
		return haveItem(st, IDItemSolicita,CantidadNecesita, true);
	}
	
	public static boolean haveItem(L2PcInstance st, int IDItem){
		return haveItem(st, IDItem, 1, false);
	}
	
	
	public static boolean haveItem(L2PcInstance st, int IDItemSolicita,Long CantidadNecesita, boolean showMensaje) {
		//_log.warning("Pregunta por ID:"+String.valueOf(IDItemSolicita)+", Cantidad:"+String.valueOf(CantidadNecesita));
		boolean retorno = true;
		try{
			Long IteminChar = st.getInventory().getInventoryItemCount(IDItemSolicita,-1);
			if(IteminChar < CantidadNecesita){
				if(showMensaje){
					central.msgbox("You don't have " + getFormatNumbers(CantidadNecesita) + " " + central.getNombreITEMbyID(IDItemSolicita) + " requested.",st);
				}
				return false;
			}
		}catch(Exception a){
			retorno = false;
			_log.warning("CHECK ITEM-> Error. No se reconocio el Item con ID " + String.valueOf(IDItemSolicita));
			central.msgbox(msg.ITEM_ID_NO_ENCONTRADO, st);
		}
		return retorno;
	}
	
	
	public static boolean haveItem(L2PcInstance st, int IDItemSolicita,int CantidadNecesita, boolean showMensaje) {
		//_log.warning("Pregunta por ID:"+String.valueOf(IDItemSolicita)+", Cantidad:"+String.valueOf(CantidadNecesita));
		boolean retorno = true;
		try{
			Long IteminChar = st.getInventory().getInventoryItemCount(IDItemSolicita,-1);
			if(IteminChar < CantidadNecesita){
				if(showMensaje){
					central.msgbox("You don't have " + getFormatNumbers(CantidadNecesita) + " " + central.getNombreITEMbyID(IDItemSolicita) + " requested.",st);
				}
				return false;
			}
		}catch(Exception a){
			retorno = false;
			_log.warning("CHECK ITEM-> Error. No se reconocio el Item con ID " + String.valueOf(IDItemSolicita));
			central.msgbox(msg.ITEM_ID_NO_ENCONTRADO, st);
		}
		return retorno;
	}

	public static boolean haveItem(L2PcInstance st, String ItemSolicita){
		boolean retorno = true;
		String[] _Solicitados = ItemSolicita.split(";");
		for(String _Individuales : _Solicitados){
			String[] ItemPide = _Individuales.split(",");
			try{
				Long IteminChar = st.getInventory().getInventoryItemCount(Integer.valueOf(ItemPide[0]),-1);
				if(IteminChar < Integer.valueOf(ItemPide[1])){
					central.msgbox("You don't have " + ItemPide[1] + " " + central.getNombreITEMbyID(Integer.valueOf(ItemPide[0])) + " requested.",st);
					return false;
				}
			}catch(Exception a){
				_log.warning("CHECK ITEM-> Error. No se reconocio el Item con ID " + String.valueOf(ItemPide[0]));
				central.msgbox(msg.ITEM_ID_NO_ENCONTRADO, st);
			}
		}
		return retorno;
	}

	public static void giveEnchantReward(L2PcInstance st,String Reward_){
		String _Premios[] = Reward_.split(";");
		for(String PremiosGive : _Premios){
			PremiosGive.split(",");
		}
	}

	public static void giveReward(L2PcInstance st,String Reward_){
		String _Premios[] = Reward_.split(";");
		for(String PremiosGive : _Premios){
			String _RewardGive[] = PremiosGive.split(",");
			try{
				st.addItem(general.NOMBRE_NPC, Integer.valueOf(_RewardGive[0]), Integer.valueOf(_RewardGive[1]), st, true);
			}catch(Exception a){
				_log.warning("GIVE REWARD-> Error. No se reconocio el Item con ID " + String.valueOf(_RewardGive[0]));
				central.msgbox(msg.ITEM_ID_NO_ENCONTRADO, st);
			}
		}
	}

	public static void giveReward(L2PcInstance st,int IDItem, int Cantidad){
		giveReward(st, IDItem, Cantidad, general.NOMBRE_NPC,true);
	}
	
	public static void giveReward(L2PcInstance st,int IDItem, Long Cantidad){
		try{
			st.addItem(central.getNombreITEMbyID(IDItem), IDItem, Cantidad, st, true);
		}catch(Exception a){
				_log.warning("GIVE REWARD ERROR -> " + String.valueOf(IDItem));
		}
	}
	
	
	public static boolean isGMPlayer(int idChar){
		boolean retorno = false;
		L2PcInstance player = getPlayerbyID(idChar);
		
		if(player!=null){
			return player.isGM();
		}else{
			String Consulta = "SELECT characters.char_name FROM characters WHERE characters.charId = ? AND characters.accesslevel > 0";
			Connection conn = null;
			CallableStatement psqry;
			try{
				conn = ConnectionFactory.getInstance().getConnection();
				psqry = conn.prepareCall(Consulta);
				psqry.setInt(1, idChar);
				ResultSet rss = psqry.executeQuery();
				if(rss.next()){
					retorno = true;
				}
			}catch(SQLException e){

			}
			try{
				conn.close();
			}catch(SQLException a ){

			}				
		}
		
		return retorno;
	}
	
	
	public static String getPlayerNameById(int idChar){
		L2PcInstance player = getPlayerbyID(idChar);
		String retorno = "";
		if(player!=null){
			return player.getName();
		}else{
			String Consulta = "SELECT characters.char_name FROM characters WHERE characters.charId = ?";
			Connection conn = null;
			CallableStatement psqry;
			try{
				conn = ConnectionFactory.getInstance().getConnection();
				psqry = conn.prepareCall(Consulta);
				psqry.setInt(1, idChar);
				ResultSet rss = psqry.executeQuery();
				if(rss.next()){
					retorno = rss.getString("char_name");
				}
			}catch(SQLException e){

			}
			try{
				conn.close();
			}catch(SQLException a ){

			}			
		}
		return retorno;
	}
	

	public static void giveReward(L2PcInstance st,int IDItem, int Cantidad, String NameDar, boolean showMensaje){
		try{
			st.addItem(NameDar, Integer.valueOf(IDItem), Integer.valueOf(Cantidad), st, true);
		}catch(Exception a){
			if(showMensaje){
				_log.warning("GIVE REWARD-> Error. No se reconocio el Item con ID " + String.valueOf(IDItem));
				central.msgbox(msg.ITEM_ID_NO_ENCONTRADO, st);
			}
		}
	}
	
	public static void giveReward(int idPlayer, String reward){
		L2PcInstance player = getPlayerbyID(idPlayer);
		if(player!=null){
			giveReward(player, reward);
		}
	}

	public static int haveDonaCreditCoin(L2PcInstance st, int IDItemConsulta){
		int Responder=0;
		Connection conn = null;
		CallableStatement psqry;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			String qry = "call sp_creditosnpc(2,'"+ st.getAccountName() +"',"+String.valueOf(IDItemConsulta)+")";
			psqry = conn.prepareCall(qry);
			ResultSet rss = psqry.executeQuery();
			Responder = 0;
			while (rss.next()) {
				Responder = rss.getInt("A");
			}
		}catch(SQLException e){

		}
		try{
			conn.close();
		}catch(SQLException a ){

		}
		return Responder;
	}

	public static void Dona_removeItem(int Cantidad, L2PcInstance Player){
			Player.destroyItemByItemId("ZeuS Donacion", general.DONA_ID_ITEM , Cantidad, Player, true);

	}

	public static void removeItem(String ItemSolicita, L2PcInstance Player){
		String[] _Solicitados = ItemSolicita.split(";");
		for(String _Individuales : _Solicitados){
			String[] ItemPide = _Individuales.split(",");
			try{
				Player.destroyItemByItemId("ZeuS", Integer.parseInt(ItemPide[0]) , Integer.parseInt(ItemPide[1]), Player, true);
			}catch(Exception a){
				_log.warning("REMOVE ITEM-> Error. No se reconocio el Item con ID " + String.valueOf(ItemPide[0]));
				central.msgbox(msg.ITEM_ID_NO_ENCONTRADO, Player);
			}
		}
	}

	public static int getIdItem(L2ItemInstance item){
		return item.getId();
	}


	public static void removeItem(int IdItem, int Cantidad, L2PcInstance Player){
		try{
			Player.destroyItemByItemId("ZeuS", IdItem, Cantidad, Player, true);
		}catch(Exception a){
			_log.warning("REMOVE ITEM-> Error. No se reconocio el Item con ID " + String.valueOf(IdItem));
			central.msgbox(msg.ITEM_ID_NO_ENCONTRADO, Player);
		}
	}

	public static boolean isNumeric(String cadena){
		
		if(cadena==null){
			return false;
		}
		
		if(cadena.length()==0){
			return false;
		}
		
		try {
			if(cadena.contains(".") || cadena.contentEquals(",")){
				return false;
			}
			int Prueba = Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}

	public static String getSkillName(Integer ID, Integer LVL) {
		return SkillData.getInstance().getSkill(Integer.valueOf(ID), Integer.valueOf(LVL)).getName();
	}

	public static String getIconSkill(int skill){
		String formato = "";
		if(skill == 4) {
			formato = "0004";
		} else if((skill > 9) && (skill < 100)) {
			formato = "00" + String.valueOf(skill);
		} else if((skill > 99) && (skill < 1000)) {
			formato = "0" + String.valueOf(skill);
		} else if(skill == 1517) {
			formato = "1536";
		} else if(skill == 1518) {
			formato = "1537";
		} else if(skill == 1547) {
			formato = "0065";
		} else if(skill == 2076) {
			formato = "0195";
		} else if((skill > 4550) && (skill < 4555)) {
			formato = "5739";
		} else if((skill > 4698) && (skill < 4701)) {
			formato = "1331";
		} else if((skill > 4701) && (skill < 4704)) {
			formato = "1332";
		} else if(skill == 6049) {
			formato = "0094";
		} else {
			formato = String.valueOf(skill);
		}
		return formato;
	}

	public static boolean isPlusHTML(L2PcInstance activeChar, boolean Mensajes){
		if(general.PLUS_C != null){
			if(general.PLUS_C.containsKey(activeChar.getAccountName())){
				String nomPJ = general.PLUS_C.get(activeChar.getAccountName()).get("CHAR");
				if(nomPJ.equals(activeChar.getName())){
						central.msgbox("Listo : " + ZeuS.getIp_Wan(activeChar) , activeChar);
						return true;
				}else{
					if(Mensajes) {
						central.msgbox("N.N.E.=",activeChar);
					}
				}
			}else{
				if(Mensajes) {
					central.msgbox("No Contiene", activeChar);
				}
			}
		}else{
			if(Mensajes) {
				central.msgbox("Nulo", activeChar);
			}
		}
		return false;
	}

	public static String getTiempoON(int Segundos) {
		return getTiempoON(Segundos,true);
	}

	public static String getTiempoON(long Segundos) {
		return getTiempoON((int) Segundos, true);
	}
	
	public static String getTiempoON(int Segundos, boolean showDays) {
			int UnixTimeLleva = Segundos;
			Date date = new Date(0,0,0,0,0,UnixTimeLleva);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); // the format of your date
			SimpleDateFormat Days = new SimpleDateFormat("D"); // the format of your date

			int Dias = 0;
			if(UnixTimeLleva>=86400){
				Dias = Integer.valueOf(Days.format(date));
			}

			String formattedDate = (showDays ? "Day: " + String.valueOf(Dias) + "/" : "") + sdf.format(date);
			return formattedDate;
	}
	
	public static String getIconImgFromItem(int IdItem, boolean justIdImg){
		String itemI = getTemplateItem(IdItem);
		String NoItem = ItemTable.getInstance().getTemplate(3883).getIcon();
		
		if(justIdImg){
			return ( itemI==null ? NoItem : itemI );	
		}
		
		return "<img src="+ ( itemI==null ? NoItem : itemI ) +" width=32 height=32>";
	}

	public static String getIconImgFromItem(int IdItem){
		return getIconImgFromItem(IdItem,false);
	}


	public static final void showWithdrawWindow(L2PcInstance player, WarehouseListType itemtype, byte sortorder)
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



	public static boolean isPremium_Clan(L2PcInstance player){
		return general.isPremium(player, false);
	}

	public static boolean isPremium_Player(L2PcInstance player){
		return general.isPremium(player, true);
	}




	public static String getTemplateItem(int IdItem){
		  String retorno = null;
		  try{
			  retorno = ItemTable.getInstance().getTemplate(IdItem).getIcon();
		  }catch (Exception e) {
			  retorno = null;
		  }
		  return retorno;
	}

	public static String getTemplateItemStatic(int IdItem){
		  String retorno = null;
		  try{
			  retorno = ItemTable.getInstance().getTemplate(IdItem).getIcon();
		  }catch (Exception e) {
			  retorno = null;
		  }
		  return retorno;
	}
}
