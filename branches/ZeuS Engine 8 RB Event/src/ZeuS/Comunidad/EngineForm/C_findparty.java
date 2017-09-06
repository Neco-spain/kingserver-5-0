package ZeuS.Comunidad.EngineForm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.sendC;
import ZeuS.Comunidad.EngineForm.v_partymatching.PMTimerPlayer;
import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.ZeuS.ZeuS;
import ZeuS.event.RaidBossEvent;
import ZeuS.interfase.central;
import ZeuS.procedimientos.itemLink;
import ZeuS.procedimientos.opera;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.enums.PartyDistributionType;
import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.gameserver.util.Broadcast;

public class C_findparty {
	
	private static final String    LinkSend = "	Type=1 	ID=%IDSEND% 	Color=0 	Underline=0 	Title= ";

	private static final Logger _log = Logger.getLogger(C_findparty.class.getName());
	
	private static HashMap<Integer,Integer>PLAYER_ID_PARTYFIND_REQUEST = new HashMap<Integer,Integer>();
	private static HashMap<Integer,L2PcInstance>PLAYER_PARTYFIND_REQUEST= new HashMap<Integer,L2PcInstance>();
	private static HashMap<L2PcInstance, String> PLAYER_PARTYFIND_MESSAGE = new HashMap<L2PcInstance, String>();
	private static HashMap<Integer,Integer> PLAYER_PARTYFIND_TIME_BEGIN = new HashMap<Integer, Integer>();
	private static HashMap<Integer,Integer> PLAYER_PARTYFIND_TIME_MESSAGE = new HashMap<Integer, Integer>();
	
	private static HashMap<Integer,HashMap<Integer,L2PcInstance>> PLAYER_WAITING_FOR_APPROVAL = new HashMap<Integer,HashMap<Integer,L2PcInstance>>();
	private static HashMap<Integer,Integer> PLAYER_WAITING_FOR_APPROVAL_BEGIN = new HashMap<Integer,Integer>();
	private static HashMap<Integer,Integer>ID_LASTR = new HashMap<Integer, Integer>();
	
	private static int _segundosEsperaEntreMensaje = 40;
	private static int _segundosDuracionRequest = 10;//1 minutos;
	private static int _segundosDuracionPublicacion = 360; // 6 minutos
	
	private static HashMap<Integer,Boolean>CerrarVentana = new HashMap<Integer, Boolean>();
	
	private static void cerrarWindows(L2PcInstance player){
		try{
			if(CerrarVentana!=null){
				if(CerrarVentana.containsKey(player.getObjectId())){
					if(!CerrarVentana.get(player.getObjectId())){
						return;
					}
				}
			}
			player.sendPacket(new sendC());
		}catch(Exception a){
			
		}
	}
	
	private static void sendRequestWindows(L2PcInstance pplEmite, int IDpplReceptor,boolean automatic, int Id_To_Close){
		L2PcInstance pplReceptor = null;
		if(opera.isCharInGame(IDpplReceptor)){
			pplReceptor = opera.getPlayerbyID(IDpplReceptor);
		}else{
			return;
		}

		if(pplEmite!=null){
				try{
					if(PLAYER_WAITING_FOR_APPROVAL!=null){
						if(!PLAYER_WAITING_FOR_APPROVAL.containsKey(pplReceptor.getObjectId())){
							cerrarWindows(pplReceptor);
							return;
						}else{
							if(PLAYER_WAITING_FOR_APPROVAL.get(pplReceptor.getObjectId()).size()<=0){
								cerrarWindows(pplReceptor);
								return;
							}
						}
					}else{
						cerrarWindows(pplReceptor);
						return;
					}
				}catch(Exception a){
					cerrarWindows(pplReceptor);
				}
				try{
					if(pplEmite!=null){
						if(pplEmite.isOnline() && automatic){
							central.msgbox("Time out, the selected player not answered your party request.", pplEmite);
						}
					}
				}catch(Exception a){
					
				}
		}
		
		
		if(pplReceptor.isInOlympiadMode() || RaidBossEvent.isPlayerOnRBEvent(pplReceptor)){
			return;
		}
		
		
		
		
		
		
		
		String retorno = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		
		retorno += "<center><table width=280 border=0 background=L2UI_CT1.Windows_DF_TooltipBG height=35><tr><td fixwidth=280 align=CENTER><font name=hs12 color=A09FFC>Find Party Request</font></td></tr></table><img src=\"L2UI.SquareGray\" width=280 height=2>";
		
		String ByPassAceptar = "link zeusC;" + general.COMMUNITY_BOARD_PARTYFINDER_EXEC + ";" + Engine.enumBypass.partymatching.name()+";accept;%IDCHAR%;0;0;0;0;0";
		String ByPassRefuse = "link zeusC;" + general.COMMUNITY_BOARD_PARTYFINDER_EXEC + ";" + Engine.enumBypass.partymatching.name()+";refuse;%IDCHAR%;0;0;0;0;0";
		
		String Grilla = "<table width=280 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=0 cellpadding=2><tr><td fixwidth=3></td><td fixwidth=32>"+
        "<img src=\"%IDIMA%\" width=32 height=32><br><br><br><br></td><td fixwidth=240>"+
        "<font color=09FF95>%NOMBRE%</font> <font color=626262>Lv %LEVEL%</font><br1><font color=677C97>%CLASS%</font><font color=626262>  Sec. Decide %SECONDS%</font<table width=240><tr><td fixwidth=120 align=right>"+
        "<button value=\"Acept\" width=55 height=20 action=\""+ ByPassAceptar +"\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator></td><td fixwidth=120>"+
        "<button value=\"Refuse\" width=55 height=20 action=\""+ ByPassRefuse +"\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator></td></tr></table></td></tr></table>";
		
		Iterator itr = PLAYER_WAITING_FOR_APPROVAL.get(pplReceptor.getObjectId()).entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry infoR = (Map.Entry)itr.next();
			int IdChar = (int) infoR.getKey();
			if(opera.isCharInGame(IdChar)){
				L2PcInstance pplB = opera.getPlayerbyID(IdChar);
				String Nombre = pplB.getName();
				String Clase = opera.getClassName(pplB.getClassId().getId());
				String Level = String.valueOf(pplB.getLevel());
				String ImagenID = general.classData.get(pplB.getClassId().getId()).get("IMAGEN");
				int UnixRequest = PLAYER_WAITING_FOR_APPROVAL_BEGIN.get(IdChar) +  general.COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST; /*segundosDuracionRequest;*/
				int UnixAhora = opera.getUnixTimeNow();
				int SegundosFaltan = UnixRequest - UnixAhora;
				retorno += Grilla.replace("%IDCHAR%", String.valueOf(IdChar)) .replace("%SECONDS%", String.valueOf(SegundosFaltan)).replace("%IDIMA%", ImagenID).replace("%NOMBRE%", Nombre).replace("%LEVEL%", Level).replace("%CLASS%", Clase);
			}
		}
		
		
		String ByPas_RefuseAll = "link zeusC;" + general.COMMUNITY_BOARD_PARTYFINDER_EXEC + ";" + Engine.enumBypass.partymatching.name()+";refuseAll;0;0;0;0;0";;
		String ByPass_Disable_P_Matching = "link zeusC;" + general.COMMUNITY_BOARD_PARTYFINDER_EXEC + ";" + Engine.enumBypass.partymatching.name()+";disabled;0;0;0;0;0";;
		
		retorno += "<table width=280 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=0 cellpadding=2><tr><td fixwidth=280 align=CENTER>"+
        "<button value=\"Refuse All\" width=120 height=20 action=\""+ ByPas_RefuseAll +"\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator></td>" /*<td fixwidth=140 align=CENTER>"+
        "<button value=\"Disable P. Matching\" width=120 height=20 action=\""+ ByPass_Disable_P_Matching +"\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator></td>*/ + "</tr></table>";
		retorno += "</center>" + central.getPieHTML(false) + "</body></html>";
		central.sendHtml(pplReceptor, retorno,true);
		CerrarVentana.put(pplReceptor.getObjectId(), true);
		int LastR = opera.getUnixTimeNow();
		ID_LASTR.put(pplReceptor.getObjectId(), LastR);
		if(!automatic && pplEmite!=null){
			ThreadPoolManager.getInstance().scheduleGeneral(new PMTimerPlayer(pplReceptor,pplEmite,LastR), general.COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST  * 1000);
			central.msgbox("Request sending to " + pplReceptor.getName() + ", now u need to wait.",pplEmite);
		}
		
	}	
	
	
	
	private static void sendToAll(String Mensaje){
		CreatureSay strMensaje = new CreatureSay(0,1,"[Find Party]",Mensaje);
		Broadcast.toAllOnlinePlayers(strMensaje);
	}
	
	private static boolean canSendMessageTime(L2PcInstance player){
		if(PLAYER_PARTYFIND_TIME_MESSAGE!=null){
			if(PLAYER_PARTYFIND_TIME_MESSAGE.containsKey(player.getObjectId())){
				int unixTimePlayer = PLAYER_PARTYFIND_TIME_MESSAGE.get(player.getObjectId()) + general.COMMUNITY_BOARD_PARTYFINDER_SEC_BETWEEN_MSJE;
				int UnixAhora = opera.getUnixTimeNow();
				if(UnixAhora>unixTimePlayer){
					return true;
				}else{
					central.msgbox("You need to wait " + String.valueOf( unixTimePlayer - UnixAhora ) + ".",player);
					return false;
				}
			}
		}
		return true;
	}
	
	public static void bypass(L2PcInstance playerAsking, int idLink){
		//L2PcInstance charLink = PLAYER_REQUEST.get(idLink);
		requestParty(idLink,playerAsking);
	}
	
	private static boolean canUse(L2PcInstance playerReceptor, L2PcInstance playerEmite){
		if(!playerReceptor.isOnline()){
			central.msgbox("Selected player is not Online", playerEmite);
			return false;
		}
		
		if(playerEmite == playerReceptor){
			return false;
		}
		
		if(playerReceptor.isJailed()){
			 central.msgbox("Seleted player is in Jail.", playerEmite);
			 return false;
		}

		if(playerEmite.isJailed()){
			 central.msgbox("Your are in Jail.", playerEmite);
			 return false;
		}		
		
		if(playerReceptor.getBlockList().isInBlockList(playerEmite)){
			central.msgbox("You are on this player block list", playerEmite);
			return false;
		}
		
		if(playerReceptor.isInDuel()){
			central.msgbox("Selected player is in Combat (Duel)", playerEmite);
			return false;
		}
		
		if(playerReceptor.isInOlympiadMode() || playerEmite.isInOlympiadMode()){
			central.msgbox("You or the Selected player is Olympiad Mode", playerEmite);
			return false;
		}
		
		if(playerReceptor.isInBoat() || playerEmite.isInBoat()){
			central.msgbox("You or the Selected player is in Boat", playerEmite);
			return false;
		}
		
		if(playerReceptor.inObserverMode() || playerEmite.inObserverMode()){
			central.msgbox("You or the Selected player is Olympiad Observation Mode", playerEmite);
			return false;
		}
		
		if(playerReceptor.isOnEvent() || playerEmite.isOnEvent()){
			central.msgbox("You or the Selected player is in Event", playerEmite);
			return false;
		}
		
		if(RaidBossEvent.isPlayerOnRBEvent(playerReceptor) || RaidBossEvent.isPlayerOnRBEvent(playerEmite)){
			central.msgbox("You or the Selected player is in Event", playerEmite);
			return false;
		}
		
		if(playerEmite.isInParty()){
			if(!playerEmite.getParty().isLeader(playerEmite)){
				return false;
			}
		}
		
		if(playerReceptor.isInParty()){
			if(!playerReceptor.getParty().isLeader(playerReceptor)){
				central.msgbox("Selected player is not Party Leader", playerEmite);
				return false;
			}
			if(playerReceptor.getParty().getMemberCount()==9){
				central.msgbox("Selected player party no have more Slot's", playerEmite);
				return false;
			}
		}
		
		
		
		return true;
	}
	
	private static void refreshList(L2PcInstance partyLeader, boolean isAutomatic){
		if(PLAYER_WAITING_FOR_APPROVAL!=null){
			if(PLAYER_WAITING_FOR_APPROVAL.containsKey(partyLeader.getObjectId())){
				Iterator itr = PLAYER_WAITING_FOR_APPROVAL.get(partyLeader.getObjectId()).entrySet().iterator();
				while(itr.hasNext()){
					Map.Entry infoR = (Map.Entry)itr.next();
					int idPartyRequest = (int)infoR.getKey();
					HashMap<Integer,L2PcInstance> t1 = PLAYER_WAITING_FOR_APPROVAL.get(idPartyRequest);
					if(PLAYER_WAITING_FOR_APPROVAL_BEGIN!=null){
						if(PLAYER_WAITING_FOR_APPROVAL_BEGIN.containsKey(idPartyRequest)){
							int UnixPeticion = PLAYER_WAITING_FOR_APPROVAL_BEGIN.get(idPartyRequest);
							int UnixAhora = opera.getUnixTimeNow();
							if(UnixAhora >= (UnixPeticion + general.COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST)){
								PLAYER_WAITING_FOR_APPROVAL.get(partyLeader.getObjectId()).remove(idPartyRequest);
								PLAYER_WAITING_FOR_APPROVAL_BEGIN.remove(idPartyRequest);
								if(isAutomatic){
									if(opera.isCharInGame(idPartyRequest)){
										L2PcInstance ppl = opera.getPlayerbyID(idPartyRequest);
										central.msgbox("Time out. The Selected player not answer your invitation ", ppl);
									}
								}
							}							
						}else{
							PLAYER_WAITING_FOR_APPROVAL.get(partyLeader.getObjectId()).remove(idPartyRequest);
						}
					}
				}
			}
		}
	}
	
	private static void registerRequest(L2PcInstance partyL, L2PcInstance partyReq){
		//refreshList(partyL);
		if(PLAYER_WAITING_FOR_APPROVAL!=null){
			if(!PLAYER_WAITING_FOR_APPROVAL.containsKey(partyL.getObjectId())){
				PLAYER_WAITING_FOR_APPROVAL.put(partyL.getObjectId(), new HashMap<Integer,L2PcInstance>());				
			}
		}else{
			PLAYER_WAITING_FOR_APPROVAL.put(partyL.getObjectId(), new HashMap<Integer,L2PcInstance>());
		}
		PLAYER_WAITING_FOR_APPROVAL.get(partyL.getObjectId()).put(partyReq.getObjectId(), partyReq);
		PLAYER_WAITING_FOR_APPROVAL_BEGIN.put(partyReq.getObjectId(),opera.getUnixTimeNow());
	}
	
	private static void cleanWaitingInformation(L2PcInstance ptleader,L2PcInstance playerSoli){
		if(PLAYER_WAITING_FOR_APPROVAL!=null){
			if(PLAYER_WAITING_FOR_APPROVAL.containsKey(ptleader.getObjectId())){
				if(PLAYER_WAITING_FOR_APPROVAL.get(ptleader.getObjectId())!=null){
					if(PLAYER_WAITING_FOR_APPROVAL.get(ptleader.getObjectId()).containsKey(playerSoli.getObjectId())){
						PLAYER_WAITING_FOR_APPROVAL.get(ptleader.getObjectId()).remove(playerSoli.getObjectId());
						if(PLAYER_WAITING_FOR_APPROVAL_BEGIN!=null){
							if(PLAYER_WAITING_FOR_APPROVAL_BEGIN.containsKey(playerSoli)){
								PLAYER_WAITING_FOR_APPROVAL_BEGIN.remove(playerSoli.getObjectId());
							}
						}
					}
				}
			}
		}
	}
	
	private static boolean isInTimeToRegisInWaiting(L2PcInstance player){
		if(PLAYER_WAITING_FOR_APPROVAL_BEGIN!=null){
			if(PLAYER_WAITING_FOR_APPROVAL_BEGIN.containsKey(player.getObjectId())){
				int tiempoComenzo = PLAYER_WAITING_FOR_APPROVAL_BEGIN.get(player.getObjectId()) + general.COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST;
				int ahora = opera.getUnixTimeNow();
				if(ahora < tiempoComenzo){
					central.msgbox("You need to wait " + (tiempoComenzo - ahora) + " to send another request", player);
					return false;
				}
			}
		}
		return true;
	}
	
	private static void refreshPartysFinder(int IdParty){
		if(PLAYER_PARTYFIND_REQUEST==null){
			return;
		}
		
		L2PcInstance pplleader = null;
		if(PLAYER_PARTYFIND_REQUEST.containsKey(IdParty)){
			if(opera.isCharInGame(PLAYER_PARTYFIND_REQUEST.get(IdParty).getObjectId())){
				pplleader = PLAYER_PARTYFIND_REQUEST.get(IdParty);
			}else{
				return;
			}
		}else{
			return;
		}
			
		if(PLAYER_PARTYFIND_TIME_BEGIN!=null){
			if(PLAYER_PARTYFIND_TIME_BEGIN.containsKey(pplleader.getObjectId())){
				int ComenzoPTFinder = PLAYER_PARTYFIND_TIME_BEGIN.get(pplleader.getObjectId());
				int Ahora = opera.getUnixTimeNow();
				int HastaUnix = ComenzoPTFinder + general.COMMUNITY_BOARD_PARTYFINDER_SEC_ON_BOARD;
				if(Ahora > HastaUnix){
					PLAYER_PARTYFIND_TIME_BEGIN.remove(pplleader.getObjectId());
					PLAYER_PARTYFIND_TIME_MESSAGE.remove(pplleader.getObjectId());
					PLAYER_PARTYFIND_REQUEST.remove(pplleader.getObjectId());
				}
			}
		}	
	}
	
	public static void requestParty(int idParty, L2PcInstance playerRequesting){
		refreshPartysFinder(idParty);
		if(PLAYER_PARTYFIND_REQUEST.containsKey(idParty)){
			L2PcInstance PartyLeader = null;
			try{
				if(opera.isCharInGame( PLAYER_PARTYFIND_REQUEST.get(idParty).getObjectId())){
					PartyLeader = PLAYER_PARTYFIND_REQUEST.get(idParty);					
				}
			}catch(Exception a){
				
			}
			if(PartyLeader!=null){
				if(canUse(PartyLeader,playerRequesting)){
					if(isInTimeToRegisInWaiting(playerRequesting)){
						registerRequest(PartyLeader,playerRequesting);
						sendRequestWindows(playerRequesting, PartyLeader.getObjectId(),false,0);
					}else{
						return;						
					}
				}else{
					return;
				}
			}else{
				central.msgbox("The player is disconnected", playerRequesting);
			}
		}else{
			central.msgbox("This Find Party has Ending, please contact him", playerRequesting);
		}
	}
	
	
	
	private static int getIdToPartyFinder(L2PcInstance player){
		if(PLAYER_ID_PARTYFIND_REQUEST!=null){
			if(PLAYER_ID_PARTYFIND_REQUEST.containsKey(player.getObjectId())){
				int idPartyFromPpl = PLAYER_ID_PARTYFIND_REQUEST.get(player.getObjectId());
				return idPartyFromPpl;
			}
		}
		int t = itemLink.getNewIdLink(itemLink.sectores.V_FIND_PARTY.name());
		PLAYER_ID_PARTYFIND_REQUEST.put(player.getObjectId(), t);
		return t;
	}
	
	public static void sendRequestToAll(L2PcInstance player, String Mensaje){
		
		if(!general.COMMUNITY_BOARD_PARTYFINDER_COMMAND_ENABLE){
			central.msgbox(msg.DISABLED_BY_ADMIN, player);
			return;
		}
		
		
		if(player.isJailed()){
			central.msgbox("This action are disabled for the jailed players", player);
			return;
		}
		
		
		int t = getIdToPartyFinder(player);
		
		
		
		
		int ptCount = 8;
		
		if(player.getParty()!=null){
			ptCount = 9 - player.getParty().getMemberCount();
			if(player.getParty().getMemberCount()==9){
				return;
			}
		}
		
		
		if(!canSendMessageTime(player)){
			return;
		}
		
		
		String MensajeFromPlayer = "";
		
		if(Mensaje==null){
			if(PLAYER_PARTYFIND_MESSAGE!=null){
				if(!PLAYER_PARTYFIND_MESSAGE.containsKey(player)){
					return;
				}else{
					MensajeFromPlayer = PLAYER_PARTYFIND_MESSAGE.get(player);
				}
			}else{
				return;
			}
		}else if(Mensaje.length()==0){
			if(PLAYER_PARTYFIND_MESSAGE!=null){
				if(!PLAYER_PARTYFIND_MESSAGE.containsKey(player)){
					return;
				}else{
					MensajeFromPlayer = PLAYER_PARTYFIND_MESSAGE.get(player);
				}
			}else{
				return;
			}			
		}else if(Mensaje.length()>0){
			MensajeFromPlayer = Mensaje;
		}
		
		cleanRegistroPTFINDER(player,t);
		
		PLAYER_PARTYFIND_MESSAGE.put(player, MensajeFromPlayer);
		PLAYER_PARTYFIND_TIME_MESSAGE.put(player.getObjectId(), opera.getUnixTimeNow());
		PLAYER_PARTYFIND_TIME_BEGIN.put(player.getObjectId(), opera.getUnixTimeNow());
		PLAYER_PARTYFIND_REQUEST.put(t, player);
		
		String strMensaje = LinkSend.replace("%IDSEND%", String.valueOf(t)) + " " + player.getName() + " ("+ String.valueOf(ptCount) +"/9) free slots " + MensajeFromPlayer;
		sendToAll(strMensaje);
	}
	
	private static void cleanRegistroPTFINDER(L2PcInstance player, int idParty){
		if(PLAYER_PARTYFIND_MESSAGE!=null){
			if(PLAYER_PARTYFIND_MESSAGE.containsKey(player)){
				PLAYER_PARTYFIND_MESSAGE.remove(player);
			}
		}
		
		if(PLAYER_PARTYFIND_MESSAGE!=null){
			if(PLAYER_PARTYFIND_MESSAGE.containsKey(player)){
				PLAYER_PARTYFIND_MESSAGE.remove(player);
			}
		}
		
		if(PLAYER_PARTYFIND_TIME_MESSAGE!=null){
			if(PLAYER_PARTYFIND_TIME_MESSAGE.containsKey(player.getObjectId())){
				PLAYER_PARTYFIND_TIME_MESSAGE.remove(player.getObjectId());
			}
		}
		
		if(PLAYER_PARTYFIND_TIME_BEGIN!=null){
			if(PLAYER_PARTYFIND_TIME_BEGIN.containsKey(player.getObjectId())){
				PLAYER_PARTYFIND_TIME_BEGIN.remove(player.getObjectId());
			}
		}
		
		if(PLAYER_PARTYFIND_REQUEST!=null){
			if(PLAYER_PARTYFIND_REQUEST.containsKey(idParty)){
				PLAYER_PARTYFIND_REQUEST.remove(idParty);
			}
		}

	}
	
	private static Vector<HashMap<String,String>> pplsSolicitando(String Busqueda){
		Vector<HashMap<String,String>> t = new Vector<HashMap<String, String>>();
		Iterator itr = PLAYER_PARTYFIND_REQUEST.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry infoR = (Map.Entry)itr.next();
			int idParty = (int)infoR.getKey();
			try{
				L2PcInstance pp = null;
				if(opera.isCharInGame(PLAYER_PARTYFIND_REQUEST.get(idParty).getObjectId())){
					pp = PLAYER_PARTYFIND_REQUEST.get(idParty);
					if(pp!=null){
						refreshList(pp,false);
						int MaxDuracion = PLAYER_PARTYFIND_TIME_BEGIN.get(pp.getObjectId()) + general.COMMUNITY_BOARD_PARTYFINDER_SEC_ON_BOARD;
						int Ahora = opera.getUnixTimeNow();
						if(MaxDuracion>Ahora){
							HashMap<String,String> F = new HashMap<String, String>();
							F.put("NOMBRE",pp.getName());
							F.put("CLAN", (pp.getClan()!= null ? pp.getClan().getName() : "No Have" ) );
							F.put("MENSAJE", PLAYER_PARTYFIND_MESSAGE.get(pp));
							F.put("COUNT",  String.valueOf(  ( pp.getParty() != null ? pp.getParty().getMemberCount() : 1 )   ) );
							F.put("IDPLAYER", String.valueOf(pp.getObjectId()));
							F.put("IDPARTY", String.valueOf(idParty));
							F.put("CLASS", opera.getClassName(pp.getClassId().getId()));
							F.put("SEGUNDOSESPERA", String.valueOf(MaxDuracion-Ahora) );
							t.add(F);
						}
					}
				}
			}catch(Exception a){
				
			}
		}
		
		return t;
	}
	
	
	private static String getGrilla(L2PcInstance player, String Buscar, int pagina){
		String retorno = "<center><table cellspacing=0 cellpadding=0 border=0><tr><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td fixwidth=160 align=center><button value=\"Player Name\" width=160 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td fixwidth=257 align=center><button value=\"Message\" width=257 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td fixwidth=160 align=center><button value=\"Clan\" width=160 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter>"+
        "</td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td fixwidth=70 align=center><button value=\"P. Members\" width=70 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td fixwidth=55 align=center><button value=\"\" width=55 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td></tr></table>";
		
		String []bgcolor= {"180606","191919"};
		String []fccolor= {"FAAC58","86FFFE"};
		String []fccolor_2= {"7C5A34","3B6363"};
		
		String ByPassRequest = "bypass " + general.COMMUNITY_BOARD_PARTYFINDER_EXEC + ";" + Engine.enumBypass.partymatching.name()+";sendInvitation;%IDCHAR%;0;0;0;0;0";
		int Contador = 0;
		
		
		String Muestra = "<table cellspacing=0 cellpadding=0 border=0 bgcolor=%BGCOLOR%><tr>"+
        "<td fixwidth=164 align=center><br><font color=%FONTCOLOR%>%NOMBRE%</font><br1><font color=%FONTCOLOR2%>%CLASS%</font><br></td>"+
        "<td fixwidth=265 align=center><font color=%FONTCOLOR%>%MENSAJE%</font> <font color=6C6C6C>Sec.Left: %TIME%</font></td>"+
        "<td fixwidth=164 align=center><font color=%FONTCOLOR%>%CLAN%</font></td>"+
        "<td fixwidth=74 align=center><font color=%FONTCOLOR%>%PARTYCOUNT%</font></td>"+
        "<td fixwidth=59 align=center><br><button value=\"Request\" width=55 height=30 action=\""+ ByPassRequest +"\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator></td></tr></table><img src=\"L2UI.SquareGray\" width=725 height=2><img src=\"L2UI.SquareGray\" width=725 height=2>";
		
		for(HashMap<String, String> g : pplsSolicitando(Buscar)){
			retorno += Muestra.replace("%IDCHAR%", g.get("IDPARTY")) .replace("%TIME%", g.get("SEGUNDOSESPERA")) .replace("%CLASS%", g.get("CLASS")) .replace("%FONTCOLOR2%",fccolor_2[Contador%2]).replace("%PARTYCOUNT%", g.get("COUNT")) .replace("%CLAN%", g.get("CLAN")) .replace("%MENSAJE%", g.get("MENSAJE")) .replace("%NOMBRE%", g.get("NOMBRE")) .replace("%FONTCOLOR%", fccolor[Contador%2]) .replace("%BGCOLOR%", bgcolor[Contador%2]);
		}
		
		retorno += "</center>";
		
		
		return retorno;
	}
	
	
	
	private static String mainHtml(L2PcInstance player, String Parametro, String Busqueda, int pagina){
		String par = Engine.enumBypass.partymatching.name();
		String retorno = "<html><body><title>Party Finder System</title><center>" +cbFormato.getTituloEngine();
		String Icono = "icon.skill1255";
		String Explica = "<br>Find Party Player List (to use: .party [Message] or .party)";
		String Nombre = "Find Party System";
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,false);
		
		retorno +=  getGrilla(player,Busqueda,pagina);
		
		retorno += "</body></html>";
		return retorno;
	}
	
	
	private static void cleanRequest(L2PcInstance playerReceptor){
		if(PLAYER_WAITING_FOR_APPROVAL!=null){
			if(PLAYER_WAITING_FOR_APPROVAL.containsKey(playerReceptor.getObjectId())){
				Iterator itr = PLAYER_WAITING_FOR_APPROVAL.get(playerReceptor.getObjectId()).entrySet().iterator();
				while(itr.hasNext()){
					Map.Entry InfoRequest = (Map.Entry)itr.next();
					int IdChar = (int) InfoRequest.getKey();
					if(opera.isCharInGame(IdChar)){
						int InicioUnix = PLAYER_WAITING_FOR_APPROVAL_BEGIN.get(IdChar);
						int ActualUnix = opera.getUnixTimeNow();
						if((InicioUnix + general.COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST) <= ActualUnix){
							PLAYER_WAITING_FOR_APPROVAL.get(playerReceptor.getObjectId()).remove(IdChar);
						}
					}else{
						PLAYER_WAITING_FOR_APPROVAL.get(playerReceptor.getObjectId()).remove(IdChar);
					}
				}
			}
		}
	}	
	
	public static String bypass(L2PcInstance player, String params){
		_log.warning(params);
		
		if(params.equalsIgnoreCase(general.COMMUNITY_BOARD_PARTYFINDER_EXEC)){
			return mainHtml(player,"0","0",0);
		}
		
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			return mainHtml(player,"0","0",0);
		}else if(parm1.equals("accept")){
			if(opera.isCharInGame(Integer.valueOf(parm2))){
				L2PcInstance playerEmisor = opera.getPlayerbyID(Integer.valueOf(parm2));
				if(canUse(player, playerEmisor)){
					if(player.getParty()==null){
						player.setParty(new L2Party(player, PartyDistributionType.RANDOM));
					}
					playerEmisor.joinParty(player.getParty());
					if(player.isInParty()){
						player.getParty().setPendingInvitation(false); // if party is null, there is no need of decreasing
					}
					player.setActiveRequester(null);
					player.onTransactionResponse();					
					try{
						if(PLAYER_WAITING_FOR_APPROVAL!=null){
							if(PLAYER_WAITING_FOR_APPROVAL.containsKey(player.getObjectId())){
								if(PLAYER_WAITING_FOR_APPROVAL.get(player.getObjectId()).containsKey(playerEmisor.getObjectId())){
									PLAYER_WAITING_FOR_APPROVAL.get(player.getObjectId()).remove(playerEmisor.getObjectId());
									try{
										if(PLAYER_WAITING_FOR_APPROVAL_BEGIN.containsKey(playerEmisor.getObjectId())){
											PLAYER_WAITING_FOR_APPROVAL_BEGIN.remove(playerEmisor.getObjectId());
										}
									}catch(Exception a){
										
									}
									if(PLAYER_WAITING_FOR_APPROVAL.get(player.getObjectId()).size()==0){
										cerrarWindows(player);
										CerrarVentana.put(player.getObjectId(), false);
									}else{
										sendRequestWindows(null,player.getObjectId(),false,0);
									}
								}
							}
						}
					}catch(Exception a){
						
					}
				}
			}else{
				central.msgbox("Player is not in game right now.", player);
			}
		}else if(parm1.equals("refuse")){
			if(opera.isCharInGame(Integer.valueOf(parm2))){
				L2PcInstance playerEmisor = opera.getPlayerbyID(Integer.valueOf(parm2));
				if(PLAYER_WAITING_FOR_APPROVAL!=null){
					if(PLAYER_WAITING_FOR_APPROVAL.containsKey(player.getObjectId())){
						if(PLAYER_WAITING_FOR_APPROVAL.get(player.getObjectId()).containsKey(playerEmisor.getObjectId())){
							PLAYER_WAITING_FOR_APPROVAL.get(player.getObjectId()).remove(playerEmisor.getObjectId());
							central.msgbox("Selected player refuse you Party Request.", playerEmisor);
							if(PLAYER_WAITING_FOR_APPROVAL.get(player.getObjectId()).size()==0){
								cerrarWindows(player);
								CerrarVentana.put(player.getObjectId(), false);
							}else{
								sendRequestWindows(null,player.getObjectId(),false,0);
							}							
						}
					}
				}
			}else{
				cleanRequest(player);
			}
			
		}else if(parm1.equals("refuseAll")){
			if(PLAYER_WAITING_FOR_APPROVAL!=null){
				if(PLAYER_WAITING_FOR_APPROVAL.containsKey(player.getObjectId())){
					//HashMap<Integer,HashMap<Integer,L2PcInstance>> PLAYER_WAITING_FOR_APPROVAL
					Iterator itr = PLAYER_WAITING_FOR_APPROVAL.get(player.getObjectId()).entrySet().iterator();
					while(itr.hasNext()){
						Map.Entry w8 = (Map.Entry)itr.next();
						int idCharToRefuse = (int)w8.getKey();
						if(opera.isCharInGame(idCharToRefuse)){
							central.msgbox("Player refused you party request", PLAYER_WAITING_FOR_APPROVAL.get(player.getObjectId()).get(idCharToRefuse));
						}
						try{
							PLAYER_WAITING_FOR_APPROVAL_BEGIN.remove(idCharToRefuse);
						}catch(Exception a){
							
						}
					}
					PLAYER_WAITING_FOR_APPROVAL.remove(player.getObjectId());
					CerrarVentana.put(player.getObjectId(), true);
					cerrarWindows(player);
					CerrarVentana.put(player.getObjectId(), false);
				}
			}
		}else if(parm1.equals("sendInvitation")){
			requestParty(Integer.valueOf(parm2), player);
			return mainHtml(player,"0","0",0);
		}
		return "";
	}	
	
	
	
	
	
	
	public static class PMTimerPlayer implements Runnable{
		L2PcInstance activeChar_Receptor;
		L2PcInstance activeChar_Emite;
		int LastR_ID=0;
		public PMTimerPlayer(L2PcInstance player_Receptor, L2PcInstance player_Emite, int LastID_R){
			activeChar_Receptor = player_Receptor;
			activeChar_Emite = player_Emite;
			LastR_ID = LastID_R;
		}
		@Override
		public void run(){
			try{
				refreshList(activeChar_Receptor, true);
				
				sendRequestWindows(activeChar_Emite , activeChar_Receptor.getObjectId(),true,LastR_ID);
			}catch(Exception a){

			}

		}
	}
	
}
