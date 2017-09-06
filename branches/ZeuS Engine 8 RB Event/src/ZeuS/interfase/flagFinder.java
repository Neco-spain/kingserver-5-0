package ZeuS.interfase;

import java.util.Collection;
import java.util.Random;
import java.util.Vector;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.zone.ZoneId;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.event.RaidBossEvent;
import ZeuS.procedimientos.opera;

public class flagFinder {

	public static String MainHtmlFlagFinder(){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Flag Finder") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.FLAG_FINDER_MENSAJE,"WHITE") + central.LineaDivisora(2);
		//MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Necesitas " + central.ItemNeedShow(general.FLAG_FINDER_PRICE) ,"LEVEL") + central.LineaDivisora(2);
		MAIN_HTML += central.ItemNeedShowBox(general.FLAG_FINDER_PRICE);
		MAIN_HTML += "<br><center><button value=\""+msg.FLAG_FINDER_BTN_MENSAJE +"\" action=\"bypass -h ZeuSNPC goFlag 0 0 0\" width=235 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br>"+central.BotonGOBACKZEUS()+central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}

	public static boolean canuseFlagFinder(L2PcInstance player){
		if(!general._activated()){
			return false;
		}
		if(!general.FLAG_FINDER_CAN_USE_FLAG && (player.getPvpFlag()>0)){
			central.msgbox(msg.NO_PUEDES_USAR_MIS_SERVICIOS_ESTANDO_FLAG, player);
			return false;
		}
		if(!general.FLAG_FINDER_CAN_USE_PK && (player.getKarma()>0)){
			central.msgbox(msg.NO_PUEDES_USAR_MIS_SERVICIOS_ESTANDO_PK, player);
			return false;
		}
		if(general.FLAG_FINDER_CAN_NOBLE && !player.isNoble()){
			central.msgbox(msg.TU_NO_ERES_NOBLE, player);
			return false;
		}
		if(general.FLAG_FINDER_LVL > player.getLevel()){
			central.msgbox(msg.NECESITAS_TENER_UN_LEVEL_MAYOR_A_$level.replace("$level", String.valueOf(general.FLAG_FINDER_LVL)), player);
			return false;
		}
		if(!opera.haveItem(player, general.FLAG_FINDER_PRICE)){
			return false;
		}
		return true;
	}

	private static boolean isInTheSameClan(L2PcInstance playerUse, L2PcInstance playerTarget){
		if(playerUse.getClan()!=null){
			if(playerTarget.getClan()!=null){
				if(playerUse.getClanId() == playerTarget.getClanId()){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean goFlagFinder(L2PcInstance player){
		if(!general._activated()){
			return false;
		}
		String htmltext = "<html><title>" + general.TITULO_NPC() + "</title><body>" + central.LineaDivisora(2) + central.headFormat("Flag Finder") + central.LineaDivisora(2);

		if(!canuseFlagFinder(player)) {
			return false;
		}

		String PreList = "";
		Vector<L2PcInstance> playersOnline = new Vector<L2PcInstance>();
		Vector<L2PcInstance> playersOnlinePK = new Vector<L2PcInstance>();

		//Freya Collection<L2PcInstance> pls = L2World.getInstance().getAllPlayers().values();
		//Vector<L2PcInstance> pls = new Vector<L2PcInstance>();

		Collection<L2PcInstance> pls = opera.getAllPlayerOnWorld();

		for(L2PcInstance onlinePlayer : pls){
			if(onlinePlayer.isOnline() && !onlinePlayer.getName().equals(player.getName())){
				if((onlinePlayer.getPvpFlag() != 0) || (onlinePlayer.getKarma() > 0)){
					if(onlinePlayer.inObserverMode()){
						continue;
					}
					int insiege = Integer.valueOf(onlinePlayer.getSiegeState());
					if (insiege <=0){
						if(!opera._checkIsEventServer(onlinePlayer)){
							if(!onlinePlayer.isInStance() && !opera.isMaster(onlinePlayer)){
								if(onlinePlayer.getLevel() >= general.FLAG_PVP_PK_LVL_MIN){
									if(!onlinePlayer.isInsideZone(ZoneId.CASTLE) || (onlinePlayer.isInsideZone(ZoneId.CASTLE) && general.FLAG_FINDER_CAN_GO_IS_INSIDE_CASTLE)){
										if(!onlinePlayer.isInsideZone(ZoneId.CLAN_HALL)){
											if(!RaidBossEvent.isPlayerOnRBEvent(onlinePlayer)){
												if(!onlinePlayer.isInsideZone(ZoneId.NO_SUMMON_FRIEND) && !onlinePlayer.isInsideZone(ZoneId.QUEEN_ANT)){
													if(general.FLAG_FINDER_MIN_PVP_FROM_TARGET==0 || onlinePlayer.getKarma()>0 || (general.FLAG_FINDER_MIN_PVP_FROM_TARGET <= onlinePlayer.getPvpKills())){
														if(!onlinePlayer.isInsideZone(ZoneId.NO_ZEUS)){
															if((general.FLAG_FINDER_CHECK_CLAN && !isInTheSameClan(player,onlinePlayer)) || !general.FLAG_FINDER_CHECK_CLAN){
																if(!onlinePlayer.isGM()){
																	playersOnline.add(onlinePlayer);
																	if(onlinePlayer.getKarma()>0){
																		playersOnlinePK.add(onlinePlayer);
																	}
																}
															}	
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		if (playersOnline.size() <= 0){
			central.msgbox(msg.FLAG_FINDER_MENSAJE_NO_PVP, player);
			return false;
		}

		Random aleatorio = new Random();

		int RandChar = 0;

		L2PcInstance playerElegido = null;

		if(general.FLAG_FINDER_PK_PRIORITY && (playersOnlinePK.size()>0) ){
			RandChar = aleatorio.nextInt( playersOnlinePK.size() );
			playerElegido = playersOnlinePK.get(RandChar);
		}else{
			RandChar = aleatorio.nextInt(playersOnline.size());
			playerElegido = playersOnline.get(RandChar);
		}




		player.teleToLocation(playerElegido.getX(),playerElegido.getY(),playerElegido.getZ());
		central.msgbox_Lado( msg.FLAG_FINDER_ENCONTRADO_$player.replace("$player", playerElegido.getName()), player,"Go PvP");
		if(!opera.isMaster(player)){
			central.msgbox_Lado( msg.FLAG_FINDER_VIENEN_POR_TI_$player.replace("$player", player.getName()) ,playerElegido,"Go PvP");
		}
		return true;
	}
}
