package ZeuS.ZeuS;


import java.util.HashMap;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PetInstance;
import com.l2jserver.gameserver.model.actor.instance.L2RaidBossInstance;
import com.l2jserver.gameserver.model.itemcontainer.PcInventory;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;

import ZeuS.Comunidad.CBByPass;
import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Comunidad.EngineForm.C_oly_buff;
import ZeuS.Comunidad.EngineForm.cbFormato;
import ZeuS.Comunidad.EngineForm.v_auction_house;
import ZeuS.Comunidad.EngineForm.C_gmlist;
import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.event.RaidBossEvent;
import ZeuS.event.TownWarEvent;
import ZeuS.interfase.EmailRegistration;
import ZeuS.interfase.ManagerAIONpc;
import ZeuS.interfase.bufferChar;
import ZeuS.interfase.buffer_zeus;
import ZeuS.interfase.captchaPLayer;
import ZeuS.interfase.central;
import ZeuS.interfase.ipGuard;
import ZeuS.interfase.ipblock;
import ZeuS.interfase.overenchant;
import ZeuS.interfase.pinCode;
import ZeuS.interfase.sellBuff;
import ZeuS.interfase.showMyStat;
import ZeuS.interfase.teleport;
import ZeuS.interfase.votereward;
import ZeuS.procedimientos.IPConection;
import ZeuS.procedimientos.ObserveMode;
import ZeuS.procedimientos.handler;
import ZeuS.procedimientos.itemLink;
import ZeuS.procedimientos.msgbox;
import ZeuS.procedimientos.opera;
import ZeuS.server.Anunc;
import ZeuS.server.PvPPk;
import ZeuS.server.olym;

public class ZeuS {
	private static final Logger _log = Logger.getLogger(ZeuS.class.getName());
	private static ZeuS _instance;
	private static HashMap<L2PcInstance, Integer> SkillEnchantTime = new HashMap<L2PcInstance, Integer>();

	@SuppressWarnings("static-access")
	public void msgbox_resp(int mensajeID){
		msgbox.getInstance().setResp(mensajeID);
	}
	
	public static boolean canEnchantSkill(L2PcInstance player){
		if(general.ANTIFEED_ENCHANT_SKILL_REUSE==0){
			return true;
		}
		if(SkillEnchantTime==null){
			SkillEnchantTime.put(player, opera.getUnixTimeNow());
		}else{
			if(SkillEnchantTime.size()>0){
				if(!SkillEnchantTime.containsKey(player) ){
					SkillEnchantTime.put(player, opera.getUnixTimeNow());
					return true;
				}
				int secDif = opera.getUnixTimeNow() - SkillEnchantTime.get(player);
				if(secDif >= general.ANTIFEED_ENCHANT_SKILL_REUSE){
					SkillEnchantTime.put(player, opera.getUnixTimeNow());
				}else{
					central.msgbox("You need to wait "+String.valueOf(general.ANTIFEED_ENCHANT_SKILL_REUSE - secDif)+" second's to enchanting again.", player);
					return false;
				}
			}else{
				SkillEnchantTime.put(player, opera.getUnixTimeNow());
			}
		}
		return true;
	}
	
	public static boolean isCMDFromZeuS(L2PcInstance player, String Command){
		boolean isZeuSCommand = false;
		for(String c : handler.getCommand()){
			if(("." + c).equals(Command.split(" ")[1])){
				isZeuSCommand = true;
			}
		}
		
		/*if(isZeuSCommand){
			_log.warning("Encontrado 1");
			if(general._byPassVoice!=null){
				_log.warning("Encontrado 2");
				if(general._byPassVoice.containsKey(player.getObjectId())){
					Long L_Now = opera.getUnixTimeL2JServer();
					Long L_BP = general._byPassVoice.get(player.getObjectId());
					Long L_Diff = L_Now - L_BP;
					_log.warning("Long->Now " + L_Now + "  -  " + "->Entro " + L_BP);
					if(L_Diff == 0 || ( L_Diff <=10)){
						_log.warning("Encontrado 3");
						general._byPassVoice.remove(player.getObjectId());
						return true;
					}
				}
			}
		}*/
		
		return isZeuSCommand;
	}
	
	public static void setMerchant(L2PcInstance cha, boolean isSell){
		general.isSellMerchant.put(cha.getObjectId(), isSell);
	}

	public static void setEvent(L2PcInstance player, boolean isInEvent){
		bufferChar.setInEvent(player, isInEvent);
	}
	
	public static boolean RBEventCanRevive(L2PcInstance cha){
		return RaidBossEvent.onDieRevive(cha);
	}
	
	public static void raidBossEventStart(){
		RaidBossEvent.autoEvent();
	}
	
	public static void atacaAlRaid(L2Object npc, L2Object player){
		if(!(npc instanceof L2Npc)){
			return;
		}
		if(!(player instanceof L2PcInstance)){
			return;
		}
		RaidBossEvent.atacaAlRaid((L2PcInstance)player, (L2Npc)npc);
	}
	
	public static void _RaidBossEvent(L2Npc raid, L2PcInstance player){
		RaidBossEvent.isEventModRaid(raid, player);
	}
	
	
	public static boolean showGmList(L2PcInstance player){
		
		if(!opera.canUseCBFunction(player)){
			return true;
		}
		
		String Retorno = C_gmlist.bypass(player, "0;0;0;0");
		if(Retorno.length()==0){
			return true;
		}else{
			cbManager.separateAndSend(Retorno, player);
		}
		return false;
	}
	

	public static boolean cbByPass(L2PcInstance player, String command){
		return CBByPass.byPass(player, command);
	}

	public static void setEvent(L2Character player, boolean isInEvent){
		if(player !=null){
			if(player instanceof L2PcInstance){
				bufferChar.setInEvent((L2PcInstance)player, isInEvent);
			}
		}
	}

	public static boolean cbByPassWrite (L2PcInstance client, String url, String arg1, String arg2, String arg3, String arg4, String arg5){
		return CBByPass.byPassWrite(client, url, arg1, arg2, arg3, arg4, arg5);
	}

	public static boolean _isTradeRefusal(L2PcInstance cha){
		if(!general.CHAR_PANEL){
			return false;
		}
		return general.getCharConfigTRADE(cha);
	}

	public static boolean _isBadBuffProtected(L2PcInstance cha){
		if(!general.CHAR_PANEL){
			return false;
		}
		return general.getCharConfigBADBUFF(cha);
	}

	public final static boolean isInSameAlly(L2PcInstance player, L2PcInstance target)
	{
		return (((player.getAllyId() != 0) && (target != null) && (target.getAllyId() != 0)) && (player.getAllyId() == target.getAllyId()));
	}

	public static boolean isInSameParty(L2PcInstance player, L2PcInstance target)
	{
		return (((player.getParty() != null) && (target != null) && (target.getParty() != null)) && (player.getParty() == target.getParty()));
	}

	public static boolean isInSameChannel(L2PcInstance player, L2PcInstance target)
	{
		return (((player.getParty() != null) && (target != null) && (target.getParty() != null)) && (player.getParty().getCommandChannel() != null) && (target.getParty().getCommandChannel() != null) && (player.getParty().getCommandChannel() == target.getParty().getCommandChannel()));
	}

	public static boolean isInSameClanWar(L2PcInstance player, L2PcInstance target)
	{
		return (((player.getClan() != null) && (target != null) && (target.getClan() != null)) && (player.getClan().isAtWarWith(target.getClan()) || target.getClan().isAtWarWith(player.getClan())));
	}

	public static boolean isInSameClan(L2PcInstance player, L2PcInstance target)
	{
		return (((player.getClan() != null) && (target != null) && (target.getClan() != null)) && (player.getClanId() == target.getClanId()));
	}

	public static boolean _canGetBuffFromHim(L2PcInstance player, L2PcInstance target){
		if (!isInSameParty(player,target) && !isInSameAlly(player,target) && !isInSameClan(player,target) && !isInSameChannel(player,target)){
			return false;
		}
		return true;
	}

	public static boolean _isHideStoreProtected(L2PcInstance cha){
		if(!general.CHAR_PANEL){
			return false;
		}
		return general.getCharConfigHIDESTORE(cha);
	}

	
	
	public static long getExpAdd(L2PcInstance player, long addToExp){
		long retorno = addToExp;
		int porcen  = 0;
		if(isPremium(player)){
			porcen += general.isPremium(player, true) ? getExpPremiumChar(player) : 0;
			porcen += general.isPremium(player, false) ? getExpPremiumClan(player) : 0;
			if(porcen>0){
				double multi = (addToExp * (porcen / 100.0)) + addToExp;
				retorno = (long)multi;
			}
			if(general.PREMIUM_MESSAGE){
				central.msgbox("*** Premium Exp ***" , player);
			}						
		}
		return retorno;
	}
	public static int getSpAdd(L2PcInstance player, int addToSp){
		int retorno = addToSp;
		int porcen  = 0;
		if(isPremium(player)){
			porcen += general.isPremium(player, true) ? getSpPremiumChar(player) : 0;
			porcen += general.isPremium(player, false) ? getSpPremiumClan(player) : 0;
			if(porcen>0){
				double multi = (addToSp * (porcen / 100.0)) + addToSp;
				retorno = (int)multi;
			}
			if(general.PREMIUM_MESSAGE){
				central.msgbox("*** Premium Exp ***" , player);
			}				
		}
		return retorno;
	}
	
	public static int getEpauletteDrop(L2PcInstance player){
		return getEpauletteDrop(player, -20520);		
	}	
	
	public static int getEpauletteDrop(L2PcInstance player, int valor){
		int PremiumValue = 0;
		int idPremiumType =-1;
		if(opera.isPremium_Player(player)){
			idPremiumType = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getIdPremiumUse();
			PremiumValue = general.getPremiumServices().get(idPremiumType).getEpaulette();
		}
		if(opera.isPremium_Clan(player)){
			idPremiumType = general.getPremiumDataFromPlayerOrClan( String.valueOf( player.getClanId() ) ).getIdPremiumUse();
			PremiumValue = PremiumValue > general.getPremiumServices().get(idPremiumType).getEpaulette() ? PremiumValue : general.getPremiumServices().get(idPremiumType).getEpaulette(); 
		}
		if(valor==-20520){
			return PremiumValue;
		}else{
			if(idPremiumType>0){
				return general.getPremiumServices().get(idPremiumType).getEpaulette(valor);
			}
		}
		return 0;
	}
	
	public static int getCraftChance(L2PcInstance player){
		return getCraftChance(player, -20520);
	}
	
	public static int getCraftChance(L2PcInstance player, int valor){
		int PremiumValue = 0;
		int idPremiumType = -1;
		if(opera.isPremium_Player(player)){
			idPremiumType = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getIdPremiumUse();
			PremiumValue = general.getPremiumServices().get(idPremiumType).getCraft();
		}
		if(opera.isPremium_Clan(player)){
			idPremiumType = general.getPremiumDataFromPlayerOrClan( String.valueOf( player.getClanId() ) ).getIdPremiumUse();
			PremiumValue = PremiumValue > general.getPremiumServices().get(idPremiumType).getCraft() ? PremiumValue : general.getPremiumServices().get(idPremiumType).getCraft(); 
		}
		if(valor==-20520){
			return PremiumValue;
		}else{
			if(idPremiumType>0){
				int retorno = general.getPremiumServices().get(idPremiumType).getCraft(valor); 
				return retorno > 100 ? 100 : retorno; 
			}
		}
		return 0;
	}
	
	public static int getMW_CraftChance(L2PcInstance player){
		return getMW_CraftChance(player, -20520);
	}
	
	public static int getMW_CraftChance(L2PcInstance player, int valor){
		int PremiumValue = 0;
		int idPremiumType = 1;
		if(opera.isPremium_Player(player)){
			idPremiumType = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getIdPremiumUse();
			PremiumValue = general.getPremiumServices().get(idPremiumType).get_mwCraft();
		}
		if(opera.isPremium_Clan(player)){
			idPremiumType = general.getPremiumDataFromPlayerOrClan( String.valueOf( player.getClanId() ) ).getIdPremiumUse();
			PremiumValue = PremiumValue > general.getPremiumServices().get(idPremiumType).get_mwCraft() ? PremiumValue : general.getPremiumServices().get(idPremiumType).get_mwCraft(); 
		}
		if(valor==-20520){
			return PremiumValue;
		}else{
			if(idPremiumType>0){
				int retorno = general.getPremiumServices().get(idPremiumType).get_mwCraft(valor); 
				return retorno >100 ? 100 : retorno;
			}			
		}
		return 0;
	}
	
	public static String getPremiumName(L2PcInstance player){
		if(opera.isPremium_Player(player)){
			int idPremiumType = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getIdPremiumUse();
			return general.getPremiumServices().get(idPremiumType).getName();
		}
		if(opera.isPremium_Clan(player)){
			int idPremiumType = general.getPremiumDataFromPlayerOrClan( String.valueOf( player.getClanId() ) ).getIdPremiumUse();
			return general.getPremiumServices().get(idPremiumType).getName(); 
		}
		return "";				
	}
	
	public static int getExpPremiumChar(L2PcInstance player){
		if(opera.isPremium_Player(player)){
			int idPremiumType = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getIdPremiumUse();
			int PremiumValue = general.getPremiumServices().get(idPremiumType).getexp(false);
			return PremiumValue;
		}
		return 0;
	}

	public static int getExpPremiumClan(L2PcInstance player){
		if(opera.isPremium_Clan(player)){
			int idPremiumType = general.getPremiumDataFromPlayerOrClan(String.valueOf(player.getClan().getId())).getIdPremiumUse();
			int PremiumValue = general.getPremiumServices().get(idPremiumType).getexp(false);
			return PremiumValue;
		}
		return 0;
	}

	public static int getSpPremiumChar(L2PcInstance player){
		if(opera.isPremium_Player(player)){
			int idPremiumType = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getIdPremiumUse();
			int PremiumValue = general.getPremiumServices().get(idPremiumType).getsp(false);
			return PremiumValue;
		}
		return 0;
	}

	public static int getSpPremiumClan(L2PcInstance player){
		if(opera.isPremium_Clan(player)){
			int idPremiumType = general.getPremiumDataFromPlayerOrClan(String.valueOf(player.getClan().getId())).getIdPremiumUse();
			int PremiumValue = general.getPremiumServices().get(idPremiumType).getsp(false);
			return PremiumValue;
		}
		return 0;
	}

	public static boolean isPremium(L2PcInstance player){
		boolean retorno = false;
		if(!general.PREMIUM_CHAR && !general.PREMIUM_CLAN){
			return false;
		}

		if(general.isPremium(player, true) || general.isPremium(player, false)){
			retorno= true;
		}
		return retorno;
	}
	
	public static boolean isPremiumItem(L2PcInstance cha, int idItem){
		boolean retorno = false;
		if(isPremium(cha)){
			if(opera.isPremium_Player(cha)){
				if(general.PREMIUM_ITEM_CHAR_DROP_LIST != null){
					if(general.PREMIUM_ITEM_CHAR_DROP_LIST.size()>0){
						if(general.PREMIUM_ITEM_CHAR_DROP_LIST.containsKey(idItem)){
							retorno = true;
						}
					}
				}
			}else if(opera.isPremium_Clan(cha)){
				if(general.PREMIUM_ITEM_CLAN_DROP_LIST != null){
					if(general.PREMIUM_ITEM_CLAN_DROP_LIST.containsKey(idItem)){
						retorno = true;
					}
				}
			}
		}
		return retorno;
	}
/*	
	public static float getRateDropItemPremium(L2PcInstance cha, int idItem){
		float retornar = Config.RATE_DROP_ITEMS;
		if(isPremiumItem(cha, idItem)){
			if(!isPremium(cha)){
				if(Config.RATE_DROP_ITEMS_ID.containsKey(idItem)){
					return Config.RATE_DROP_ITEMS_ID.get(idItem);
				}
			}else{
				if(opera.isPremium_Clan(cha)){
					if(general.PREMIUM_ITEM_CLAN_DROP_LIST.containsKey(idItem)){
						retornar = general.PREMIUM_ITEM_CLAN_DROP_LIST.get(idItem) > retornar ? general.PREMIUM_ITEM_CLAN_DROP_LIST.get(idItem) : retornar ;
					}
				}
				if(opera.isPremium_Player(cha)){
					if(general.PREMIUM_ITEM_CHAR_DROP_LIST.containsKey(idItem)){
						retornar = general.PREMIUM_ITEM_CHAR_DROP_LIST.get(idItem) > retornar ? general.PREMIUM_ITEM_CHAR_DROP_LIST.get(idItem) : retornar ;
					}					
				}
				if(general.PREMIUM_MESSAGE){
					central.msgbox("*** Premium Drop Item ("+ central.getNombreITEMbyID(idItem) +"): " + String.valueOf(retornar).replace(".0", "") + " ***" , cha);
				}
			}
		}
		return retornar;				
	}

	public static float getDropRaid(L2PcInstance cha){
		float retornar = Config.RATE_DROP_ITEMS;
		if(isPremium(cha)){
			if(opera.isPremium_Clan(cha)){
				int idPremiumType = general.getPremiumDataFromPlayerOrClan(String.valueOf(cha.getClan().getId())).getIdPremiumUse();
				int PremiumValue = general.getPremiumServices().get(idPremiumType).getDrop(true);
				retornar = PremiumValue >= retornar ? PremiumValue : retornar ;
			}
			if(opera.isPremium_Player(cha)){
				int idPremiumType = general.getPremiumDataFromPlayerOrClan(cha.getAccountName()).getIdPremiumUse();
				int PremiumValue = general.getPremiumServices().get(idPremiumType).getDrop(true);
				retornar = PremiumValue >= retornar ? PremiumValue : retornar ;
			}
			if(general.PREMIUM_MESSAGE){
				central.msgbox("*** Premium Drop Raid: " + String.valueOf(retornar).replace(".0", "") + " ***" , cha);
			}			
		}
		return retornar;
	}


	public static float getDropSpoil(L2PcInstance cha){
		float retornar = Config.RATE_DROP_SPOIL;
		if(isPremium(cha)){
			if(opera.isPremium_Clan(cha)){
				int idPremiumType = general.getPremiumDataFromPlayerOrClan(String.valueOf(cha.getClan().getId())).getIdPremiumUse();
				int PremiumValue = general.getPremiumServices().get(idPremiumType).getSpoil(true);
				retornar = PremiumValue >= retornar ? PremiumValue : retornar ;
			}
			if(opera.isPremium_Player(cha)){
				int idPremiumType = general.getPremiumDataFromPlayerOrClan(cha.getAccountName()).getIdPremiumUse();
				int PremiumValue = general.getPremiumServices().get(idPremiumType).getSpoil(true);
				retornar = PremiumValue >= retornar ? PremiumValue : retornar ;
			}
			if(general.PREMIUM_MESSAGE){
				central.msgbox("*** Premium Spoil: " + String.valueOf(retornar).replace(".0", "") + " ***" , cha);
			}						
		}
		return retornar;
	}

	public static float getDropAdena(L2PcInstance cha){
		float retornar = Config.RATE_DROP_ITEMS_ID.get(PcInventory.ADENA_ID);
		if(isPremium(cha)){
			if(opera.isPremium_Clan(cha)){
				int idPremiumType = general.getPremiumDataFromPlayerOrClan(String.valueOf(cha.getClan().getId())).getIdPremiumUse();
				int PremiumValue = general.getPremiumServices().get(idPremiumType).getadena(true);
				retornar = PremiumValue >= retornar ? PremiumValue : retornar ;
			}
			if(opera.isPremium_Player(cha)){
				int idPremiumType = general.getPremiumDataFromPlayerOrClan(cha.getAccountName()).getIdPremiumUse();
				int PremiumValue = general.getPremiumServices().get(idPremiumType).getadena(true);
				retornar = PremiumValue >= retornar ? PremiumValue : retornar ;
			}
			if(general.PREMIUM_MESSAGE){
				central.msgbox("*** Premium Drop Adena: " + String.valueOf(retornar).replace(".0", "") + " ***" , cha);
			}						
		}
		return retornar;
	}
	
	public static float getDropItem(L2PcInstance cha){
		float retornar = Config.RATE_DROP_ITEMS;
		if(isPremium(cha)){
			if(opera.isPremium_Clan(cha)){
				int idPremiumType = general.getPremiumDataFromPlayerOrClan(String.valueOf(cha.getClan().getId())).getIdPremiumUse();
				int PremiumValue = general.getPremiumServices().get(idPremiumType).getDrop(true);
				retornar = PremiumValue >= retornar ? PremiumValue : retornar ;
			}
			if(opera.isPremium_Player(cha)){
				int idPremiumType = general.getPremiumDataFromPlayerOrClan(cha.getAccountName()).getIdPremiumUse();
				int PremiumValue = general.getPremiumServices().get(idPremiumType).getDrop(true);
				retornar = PremiumValue >= retornar ? PremiumValue : retornar ;
			}
			if(general.PREMIUM_MESSAGE){
				central.msgbox("*** Premium Drop Item: " + String.valueOf(retornar).replace(".0", "") + " ***" , cha);
			}						
		}
		return retornar;
	}
*/
	public static boolean expBlock(L2PcInstance cha){
		if(!general.CHAR_PANEL){
			return false;
		}
		return !general.getCharConfigEXPSP(cha);
	}

	public static void RemovePlayerInZone(L2PcInstance player){
		teleport.removePlayerInZone(player);
	}

	public static boolean canBSOE(L2PcInstance player, L2ItemInstance item){
		if(PvPPk.isBSOE(item) ) {
			return PvPPk._canScape(player,item);
		}
		return true;
	}

	public static boolean isActivePIN(L2PcInstance player){
		return pinCode.getPinStatus(player);
	}

	private static void EnterW_KnowZeus(L2PcInstance Player){

		if(!general.onLine){
			return;
		}

		central.msgbox("ZeuS Engine Activate", Player);

		if(!general.SHOW_ZEUS_ENTER_GAME){
			return;
		}

		String text = "This server Use ZeuS Engine\n";
		if(general.BUFFCHAR_ACT){
			text += ".zeus_buffer -> personal buffer\n";
		}
		if(general.CHAR_PANEL){
			text += ".charpanel -> Char Option's Windows\n";
		}
		if(general.DRESSME_STATUS){
			text += ".dressme, .dressme_target -> dressme windows\n";
		}

		if(general.RATE_EXP_OFF){
			text += ".exp_on, .exp_off -> exp. option\n";
		}

		if(general.ANTIBOT_COMANDO_STATUS){
			text += ".checkbot -> Target Antibot system\n";
		}
		if(general.SHOW_MY_STAT){
			text += ".stat -> Show target Stat\n";
		}

		if(general.SHOW_FIXME_WINDOWS){
			text+=".fixme -> Show the Fix char Windows\n";
		}

		if(Player.isGM()){
			text += "//zeus_config -> Zeus Config Windows\n";
			text += "//zeus -> Help / Ayuda (ADMIN)\n";
			text += ".zeus -> Help / Ayuda (PLAYER)";
		}else{
			text += ".zeus -> Help / Ayuda";
		}



		//Player.sendPacket(new ExShowScreenMessage(1, -1,  ExShowScreenMessage.TOP_RIGHT , 1, 1, 0, 0, true, 15000, false, text, null, text));

	}

	public static void EnterW(L2PcInstance player){

		if(!general._activated()){
			return;
		}
		
		if(!player.getClient().isDetached()){
			
			try{
				setIPChar(player);
			}catch(Exception a){
				_log.warning("ZeuS -> Error getting IP's : " + a.getMessage() + ": player Name:" + player.getName() + ", account: " + player.getAccountName());
			}
			
			try{
				IPConection.RegistroPlayer_IPCheck(player);
			}catch(Exception a){
				_log.warning("ZeuS -> Error IP's registered: " + a.getMessage() + ": player Name:" + player.getName() + ", account: " + player.getAccountName());
			}
		}
		
		general.setNewTimeLife(player);

		EmailRegistration.isRegisterUser(player);
		
		opera.setUnBlockSaveInDB(player);

		overenchant.check(player, false);

		EnterW_KnowZeus(player);

		general.setCharVariables(player);

		votereward.getInstance().inicializar();

		general.setCharVariables(player);

		buffer_zeus.getBufferNPC_char(player);

		if(ipblock.isBanIP(player)){
			central.msgbox("Your are Banned", player);
			player.getClient().closeNow();
			return;
		}

		try{
			PvPPk._getColorPvP_PK(player);
		}catch(Exception e){
			_log.warning("COLOR PVP -> Error al cargar Información de Color por PVP PK, Player: " + String.valueOf(player.getObjectId()));
		}

		try{
			PvPPk.Ver_TopPVP_PK(player);
		}catch(Exception e ){
			_log.warning("TOP PVP PK -> Error al cargar Información de TOP PVP PK, Player: " + String.valueOf(player.getObjectId()));
		}

		try{
			if(general.getCharConfigHERO(player)){
				if(!player.isHero()){
					player.setHero(true);
				}
			}
		}catch(Exception a){

		}

		try{
			PvPPk._AnunciarEntradaKarma(player);
		}catch(Exception e ){
			_log.warning("TOP PVP PK -> Error al cargar Anuncion de TOP PVP PK y/o Karma , Player: " + String.valueOf(player.getObjectId()));
		}

		try{
			if(general.isBotCheckPlayer(player)){
				captchaPLayer.checkboot(player);
			}
		}catch(Exception a){

		}

		try{
			general.addPlayerAntibot(player);
		}catch(Exception a){
			_log.warning("Error add player "+ player.getName()+" to antibot list");
		}
		
		general.loadConnections(player);
		
		v_auction_house.createCheckOfflineSell(player);
		
	}

	public static void killMob(L2Character Killer){
		
		if(Killer==null){
			return;
		}
		
		if(!Killer.isPlayer()){
			return;
		}
		
		try{
			if((Killer instanceof L2PcInstance) || (Killer instanceof L2PetInstance)){
				L2PcInstance player = null;
				player = Killer.getActingPlayer();
				if(player.getPvpFlag()>0){
					return;
				}
				if(general.ANTIBOT_AUTO){
					general.addKillAntibot(player);
					captchaPLayer.checkSendAntibot(player);
				}
				captchaPLayer.setLastKillTime(player);
			}
		}catch(Exception a){

		}
	}

	public static void Revive(L2PcInstance player){
		if(player instanceof L2PcInstance){
			try{
				if(general.getCharConfigHERO(player)){
					if(!player.isHero()){
						player.setHero(true);
					}
				}
			}catch(Exception a){

			}
			try{
				PvPPk._getColorPvP_PK(player);
			}catch(Exception e){
				_log.warning("COLOR PVP -> Error al cargar Información de Color por PVP PK Cuando revive, Player: " + String.valueOf(player.getObjectId()));
			}

			try{
				PvPPk.Ver_TopPVP_PK(player,false);
			}catch(Exception e ){
				_log.warning("TOP PVP PK -> Error al cargar Información de TOP PVP PK Cuando revive, Player: " + String.valueOf(player.getObjectId()));
			}
		}
	}

	public static void checkBoot(L2PcInstance player){
		captchaPLayer.checkboot(player);
	}

	public static void LoadZeuS(){
		general.loadConfigs();
		if(general._activated()){
			RaidBossEvent.IntervalEventStart();
		}
	}

	public static boolean isNpcFromZeus(int IDNpc){
		return ManagerAIONpc.isNpcFromZeus(IDNpc);
	}

	public static boolean isNPCZeuS(int IDNpc){
		return ManagerAIONpc.isNpcZeuS(IDNpc);
	}

	public static int getIDNpcZeuS_General(){
		return general.ID_NPC;
	}

	public static void showFirsWindows(L2PcInstance player, int idObjeto){
		ManagerAIONpc.showFirstHtml(player, idObjeto);
	}

	public static boolean canTradeFlag(L2PcInstance player, L2PcInstance partner){
		if(!general.TRADE_WHILE_FLAG){
			if (((player.getPvpFlag() > 0) || (partner.getPvpFlag() > 0))  && (!player.isGM() || !partner.isGM()))
			{
				central.msgbox(msg.YOU_CAN_NOT_TRADE_WHILE_FLAG, player);
				central.msgbox(msg.YOU_CAN_NOT_TRADE_WHILE_FLAG, partner);
				return false;
			}
		}
		return true;
	}

	public static boolean canTradePK(L2PcInstance player, L2PcInstance partner){
		if(general.TRADE_WHILE_PK){
			if (((player.getKarma() > 0) || (partner.getKarma() > 0)) && (!player.isGM() || !partner.isGM()))
			{
				central.msgbox(msg.YOU_CAN_NOT_TRADE_WHILE_PK, player);
				central.msgbox(msg.YOU_CAN_NOT_TRADE_WHILE_PK, partner);
				return false;
			}
		}
		return true;
	}
	
	private static String MensajeDeChat_LEVEL = "This Chat is Allowed only for Characters with level Higher than %LEVEL% to avoid spam";
	private static String MensajeDeChat_PVP = "This Chat is Allowed only for Characters with PvP's Higher than %LEVEL% to avoid spam";
	private static String MensajeDeChat_LT = "This Chat is Allowed only for Characters with lifetime Higher than %LEVEL% to avoid spam";
	
	public static boolean canChatShout(L2PcInstance player){
		
		if(general.CHAT_SHOUT_BLOCK && !player.isGM()){
			if(player.getLevel() < general.CHAT_SHOUT_NEED_LEVEL){
				central.msgbox(MensajeDeChat_LEVEL.replace("%LEVEL%", String.valueOf(general.CHAT_SHOUT_NEED_LEVEL)), player);
				return false;
			}
			
			if(player.getPvpKills() < general.CHAT_SHOUT_NEED_PVP){
				central.msgbox(MensajeDeChat_PVP.replace("%LEVEL%",  String.valueOf(general.CHAT_SHOUT_NEED_PVP)), player);
				return false;
			}
			
			if(player.getOnlineTime() < general.CHAT_SHOUT_NEED_LIFETIME){
				central.msgbox(MensajeDeChat_LT.replace("%LEVEL%",  String.valueOf(general.CHAT_SHOUT_NEED_LIFETIME)), player);
				return false;
			}
			
		}
		return true;
	}
	
	public static boolean canChatTrade(L2PcInstance player){
		if(general.CHAT_TRADE_BLOCK && !player.isGM()){
			if(player.getLevel() < general.CHAT_TRADE_NEED_LEVEL){
				central.msgbox(MensajeDeChat_LEVEL.replace("%LEVEL%",  String.valueOf(general.CHAT_TRADE_NEED_LEVEL)), player);
				return false;
			}
			
			if(player.getPvpKills() < general.CHAT_TRADE_NEED_PVP){
				central.msgbox(MensajeDeChat_PVP.replace("%LEVEL%",  String.valueOf(general.CHAT_TRADE_NEED_PVP)), player);
				return false;
			}
			
			if(player.getOnlineTime() < general.CHAT_TRADE_NEED_LIFETIME){
				central.msgbox(MensajeDeChat_LT.replace("%LEVEL%",  String.valueOf(general.CHAT_TRADE_NEED_LIFETIME)), player);
				return false;
			}
		}
		return true;
	}
	
	public static boolean canChatWisp(L2PcInstance player){
		if(general.CHAT_WISP_BLOCK && !player.isGM()){
			if(player.getLevel() < general.CHAT_WISP_NEED_LEVEL){
				central.msgbox(MensajeDeChat_LEVEL.replace("%LEVEL%",  String.valueOf(general.CHAT_WISP_NEED_LEVEL)), player);
				return false;
			}
			
			if(player.getPvpKills() < general.CHAT_WISP_NEED_PVP){
				central.msgbox(MensajeDeChat_PVP.replace("%LEVEL%", String.valueOf(general.CHAT_WISP_NEED_PVP)), player);
				return false;
			}
			
			if(player.getOnlineTime() < general.CHAT_WISP_NEED_LIFETIME){
				central.msgbox(MensajeDeChat_LT.replace("%LEVEL%",  String.valueOf(general.CHAT_WISP_NEED_LIFETIME)), player);
				return false;
			}
		}
		return true;
	}
	

	public static boolean isLevelProtected(L2PcInstance player, L2PcInstance target){
		try{
			overenchant.check(player, true);
		}catch(Exception a){

		}
		
		try{
			if(player.isInDuel() && target.isInDuel()){
				if(player.getDuelId() == target.getDuelId()){
					return false;
				}
			}
		}catch(Exception a){
			
		}
		
		return PvPPk._protectionLvL_PVPPK(player,target);
	}

	public static void logPvP_PK(L2PcInstance Asesino, String Tipo, L2PcInstance Asesinado){
		if((Asesino instanceof L2PcInstance) && (Asesinado instanceof L2PcInstance)){
			try{
				PvPPk._logPvPPK(Asesino, Asesinado, Tipo);
			}catch(Exception a){
				_log.warning("LogPVP-> Error al Ingresar el log PVP PK");
			}
			
			try{
				
				if(Tipo.equalsIgnoreCase("pk")){
					setPvP_PK(Asesino,false);
				}else{
					setPvP_PK(Asesino,true);
				}
				
			}catch(Exception a){
				
			}

			if(Tipo.equalsIgnoreCase("PK")){
				if((Asesino.getKarma()>0) && general.ANNOUNCE_KARMA_PLAYER_WHEN_KILL){
					opera.AnunciarTodos("[PK PLAYER]", general.ANNOUNCE_KARMA_PLAYER_WHEN_KILL_MSN.replace("%CHAR_NAME%", Asesino.getName()).replace("%KARMA%", String.valueOf(Asesino.getKarma())));
				}
			}
		}
	}

	public static void setPvP_PK(L2PcInstance player, boolean isPvP){

		if(!(player instanceof L2PcInstance)){
			return;
		}
		
		L2Object chaTarget = player.getTarget();
		
		if(!(chaTarget instanceof L2PcInstance)){
			return;
		}
		
		L2PcInstance playerTarget = (L2PcInstance) chaTarget;

		
		try{
//			if(SunriseEvents.isInEvent(player)){
//				return;
//			}
		}catch(Exception a){
			
		}
		
		
		
		
		boolean isOkDualBox = false;
		
		if(general.PVP_REWARD_CHECK_DUALBOX){
			if(isDualBox_pc(player, playerTarget)){
				isOkDualBox = true;
			}
		}
		
		if(player.isInTownWarEvent()){
			if((!isDualBox_pc(player, playerTarget) && general.EVENT_TOWN_WAR_DUAL_BOX_CHECK) || !general.EVENT_TOWN_WAR_DUAL_BOX_CHECK  ){
				TownWarEvent.getInstance().setKills(player);
				if(general.EVENT_TOWN_WAR_REWARD_GENERAL.length()>0){
					opera.giveReward(player, general.EVENT_TOWN_WAR_REWARD_GENERAL);
				}
				if(!general.EVENT_TOWN_WAR_GIVE_PVP_REWARD){
					return;
				}
			}
		}
		
		
		if(!isOkDualBox || !player.isOnEvent() || !player.isOlympiadStart()){
			if(general.PVP_REWARD){
				if(general.PVP_REWARD_ITEMS.length()>0){
					opera.giveReward(player, general.PVP_REWARD_ITEMS);
				}
				if(general.PVP_PARTY_REWARD){
					if(player.isInParty()){
						for(L2PcInstance playerParty : player.getParty().getMembers()){
							if(playerParty != player){
								if((!isDualBox_pc(playerParty, player) && general.PVP_REWARD_CHECK_DUALBOX) || !general.PVP_REWARD_CHECK_DUALBOX){
									if(playerParty.isInsideRadius(player.getLocation(), general.PVP_REWARD_RANGE, true, true)){
										opera.giveReward(playerParty, general.PVP_PARTY_REWARD_ITEMS);
									}
								}
							}
						}
					}
				}
			}
		}


		try{
			PvPPk._getColorPvP_PK(player);
		}catch(Exception a){
			_log.warning("COLOR PVP-> Error en Implementar Color PVP PK en Tiempo real al player" + String.valueOf(player.getObjectId()));
		}
		try{
			Anunc._AnunciarCiclosPvP_PK(player, isPvP);
		}catch(Exception a){
			_log.warning("Anunciar Ciclos PVP PK-> Error en Implementar los Mensajes PVP PK en Tiempo real al player " + String.valueOf(player.getName()) + ", ->" + a.getMessage());
		}
	}

	public static String getBaseName(L2Character player){
		try{
			return central.getClassName((L2PcInstance)player);
		}catch(Exception a){
			_log.warning("Error en Capturar Clase");
			return "unknown";
		}
	}

	public static String getBaseName(L2PcInstance player){
		return central.getClassName(player, player.getBaseClass());
	}

	public static void Anun_Clase_Oponente(L2PcInstance player1, L2PcInstance player2){
		try{
			olym._Anun_oponent(player1,player2);
		}catch(Exception a){
			_log.warning("ANNOUCEMENT OPONENTE-> Error al Anunciar al Oponente en Olys");
		}
	}

	public static void Anun_Raid(L2RaidBossInstance raidboss){
		Anunc.AnunciarRaidBoss(raidboss);
	}

	public static void Anun_Raid(L2RaidBossInstance raidboss, long respawnTime){
		Anunc.AnunciarRaidBoss(raidboss, respawnTime);
	}

	public static void talkNpc(L2PcInstance player, String Parametro){
		ManagerAIONpc.talkNpc(player,Parametro);
	}

	public static boolean AnunciarEnchant(int Enchant){
		return Anunc._AnunciarEnchant(Enchant);
	}

	public static boolean ShowAnuncio_PIN(boolean isClan, L2PcInstance player){
		return pinCode.ShowAnuncio_PIN(isClan, player);
	}

	public static void showMyStat(L2PcInstance target, L2PcInstance player){
		showMyStat.showMyStat(target,player);
	}

	protected static String getIP_Internet(L2PcInstance Player){
		return Player.getClient().getConnection().getInetAddress().getHostAddress();
	}

	public static String getIp_Wan(L2PcInstance player){
		return getIP_Internet(player);
	}

	protected static void setIPChar(L2PcInstance Player){
		int[][] trace1 = Player.getClient().getTrace();
		String ips = "";
		int idSeccion = 0;
		for(int primero[] : trace1){
			for(int Segundo : primero){
				if(ips.length()>0){
					ips += ".";
				}
				ips += String.valueOf(Segundo);
			}
			general.RegistrarPlayerIPs(Player, ips, idSeccion);
			//_log.warning("IP->"+idSeccion+":"+ips);
			ips = "";
			idSeccion++;
		}
	}

	protected static String getIPpc(L2PcInstance Player){
		return getIPpc(Player, 0);
	}

	protected static String getIPpc(L2PcInstance Player, int IPGet){
		/*_log.warning("Desde el HashMaps 0 = " + general.getIPPlayer(Player,0));
		_log.warning("Desde el HashMaps 1 = " + general.getIPPlayer(Player,1));
		_log.warning("Desde el HashMaps 2 = " + general.getIPPlayer(Player,2));
		_log.warning("Desde el HashMaps 3 = " + general.getIPPlayer(Player,3));
		_log.warning("Desde el HashMaps 4 = " + general.getIPPlayer(Player,4));
		_log.warning("Desde el HashMaps 5 = " + general.getIPPlayer(Player,5));
		*/
		return general.getIPPlayer(Player,IPGet);
	}

	public static String getIp_pc(L2PcInstance player){
		/*
		 * 0=pcIp // lasip
		 * 1=hop1
		 * 2=hop2
		 * 3=hop3
		 * 4=hop4
		 * 5=ipPc from BD
		 * */
		return getIPpc(player,0);
	}

	public static boolean isOlyBanned(L2PcInstance Player){
		boolean retorno = false;
		
		if(Player.getName().startsWith("[BUFF]")){
			central.msgbox("The AIO BUFF can not register into olys", Player);
			return false;
		}

		if(general.getCharConfigBANOLY(Player)){
			central.msgbox(msg.TU_ESTÁS_BANEADO_DE_LAS_OLYES, Player);
			return true;
		}

		return retorno;
	}

	public static void changeTemplateOly(L2PcInstance player){
		changeTemplateOly(player, true);
		general.resetBuffRemoved(player);
	}

	public static void changeTemplateOly(L2PcInstance player, boolean Activar){
		try{
			cbFormato.cerrarCB(player);
		}catch(Exception a){
			
		}
		
//		if(general.OLY_ANTIFEED_CHANGE_TEMPLATE && Activar){
//			player.startAntifeedProtection(true, true, true, true);
//		}else{
//			player.startAntifeedProtection(false, true, false, false);
//		}
	}

	public static boolean changeTituloOly(){
		return general.OLY_ANTIFEED_SHOW_IN_NAME_CLASS;
	}

	public static boolean Annou_opponent(int segundos){
		if(!general.ANNOUCE_CLASS_OPONENT_OLY){
			return false;
		}
		return Anunc._anunciarOponente(segundos);
	}

	public static boolean showNameNpcOly(){
		return general.OLY_ANTIFEED_SHOW_NAME_NPC;
	}

	public static boolean isDualBox_pc(L2PcInstance Player1, Object Player2){

		if(Player2 instanceof L2PcInstance){
			return isDualBox_pc(Player1,(L2PcInstance)Player2);
		}
			return false;
	}

	public static boolean OlyChangeName_npc(){
		return general.OLY_ANTIFEED_SHOW_NAME_NPC;
	}

	public boolean OlyChangeName_char(L2PcInstance player){
		if(player.isInOlympiadMode()){
			return general.OLY_ANTIFEED_CHANGE_TEMPLATE;
		}else{
			return false;
		}
	}

	public boolean OlyChangeName_char(L2Character cha){
		return OlyChangeName_char((L2PcInstance)cha);
	}

	public boolean OlyUserClassNameInCharName(){
		return general.OLY_ANTIFEED_SHOW_IN_NAME_CLASS;
	}

	public static boolean OlyChangeName_char(){
		
		if(general.OLY_ANTIFEED_CHANGE_TEMPLATE){
			return true;
		}
		
		return false;
	}

	public static boolean OlyShowOpponent(){
		return general.ANNOUCE_CLASS_OPONENT_OLY;
	}
	
	public static boolean isDualBox_pc_Oly(L2PcInstance player1, L2PcInstance player2){
		if(!general.OLY_DUAL_BOX_CONTROL){
			return false;
		}
		return isDualBox_pc(player1, player2);
	}

	public static void showBuffWindows(L2PcInstance player, L2PcInstance playerComprador){
		sellBuff.SetSelectedCharBuffer(playerComprador.getObjectId(), player);
		sellBuff.getBuffForSell(player, playerComprador,0);
	}
	
	public static boolean isSellingBuf(L2PcInstance player){
		return sellBuff.isBuffSeller(player);
	}
	
	public static boolean isDualBox_pc(L2PcInstance Player1, L2PcInstance Player2){
		String ip_pc1="";
		String ip_pc2="";
		String ip_inter_1 = getIP_Internet(Player1);
		String ip_inter_2 = getIP_Internet(Player2);

		if(ip_inter_1.equals(ip_inter_2)){
			ip_pc1 = getIPpc(Player1);
			ip_pc2 = getIPpc(Player2);

			if(ip_pc1.equals(ip_pc2)){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	public static final ZeuS getInstance()
	{
		return _instance;
	}
	
	public ZeuS getInstance2(){
		return _instance;
	}

	public static void jailOut(L2PcInstance player){
		general.removeBotPlayer(player);
	}

	public static void Anun_Clase_Oponente(String[] playerNames) {
		for(int i=0;i<= playerNames.length;i++){
			try{
				L2PcInstance player1 = L2World.getInstance().getPlayer(playerNames[i++]);
				L2PcInstance player2 = L2World.getInstance().getPlayer(playerNames[i]);
				Anun_Clase_Oponente(player1, player2);
			}catch(Exception a){

			}
		}
	}
	
	public static void stopSellBuffStore(L2PcInstance player){
		sellBuff.setStopSellBuff(player);
	}
	
	public static void restoreBuffStore(){
		sellBuff.restoreOffline();
	}
	
	public static int getNameColorBeforeBuffStore(L2PcInstance player){
		return sellBuff.getNameColorBefore(player);
	}
	
	public static int getTitleColorBeforeBuffStore(L2PcInstance player){
		return sellBuff.getTitleColorBefore(player);
	}
	
	public static String getTitleBeforeBuffStore(L2PcInstance player){
		return sellBuff.getTitleBefore(player);
	}
	
	public static boolean saveCharDataOnBD(L2PcInstance player){
		return general.canSaveInBD(player); 
	}
	
	public static boolean isInObserveMode(L2PcInstance player){
		return ObserveMode.isInObserveMode(player);
	}
	public static void setObserveMode(L2PcInstance player, boolean p1){
		ObserveMode.setObserveMode(player, p1);
	}
	
	public static boolean idLinkFromZeuS(L2PcInstance charRequestInfo, int idLink){
		_log.warning("Char Request Info:" + charRequestInfo.getName() + ", idLink:" + String.valueOf(idLink));
		return itemLink.isLinkFromZeuS(charRequestInfo,idLink);
	}
	
	public static boolean showOlyBuff_CB(L2PcInstance player){
		
		if(!general.OLY_CAN_USE_SCHEME_BUFFER){
			return false;
		}
		
		int buffCount = player.getOlympiadBuffCount();
		if(buffCount<=0 && player.isInOlympiadMode()){
			central.msgbox("You dont have more buff to use.", player);
			cbFormato.cerrarCB(player);
			return true;
		}	

		String EnviarVacio = general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.OlyBuffer.name() + ";0;0;0;0;0;0";
		String Retorno = C_oly_buff.bypass(player, EnviarVacio);
		cbManager.separateAndSend(Retorno, player);
		return true;
	}
	
	public static boolean zeusByPass(L2PcInstance player, String Command){
		if(Command==null){
			return false;
		}
		if(Command.startsWith("zeusC")){
			String Cmd = Command.substring(6);
			CBByPass.byPass(player, Cmd);
		}else if(Command.startsWith("zeusS")){
			
		}else{
			return false;
		}
		return true;
	}
	
}
