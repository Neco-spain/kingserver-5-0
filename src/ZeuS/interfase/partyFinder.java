package ZeuS.interfase;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.zone.ZoneId;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.event.RaidBossEvent;
import ZeuS.procedimientos.opera;

public class partyFinder {
	public static boolean goPartyLeader(L2PcInstance player){

		L2PcInstance PartyLeader = player.getParty().getLeader();

		int xx = PartyLeader.getX();
		int yy = PartyLeader.getY();
		int zz = PartyLeader.getZ();

		player.teleToLocation(xx,yy,zz);

		central.msgbox(msg.PARTY_FINDER_YOU_HAVE_BEEN_SENT_TO_YOUR_PARTY_LEADER, player);
		central.msgbox(msg.PARTY_FINDER_THE_PLAYER_$name_HAS_MOVE_TO_YOU_POSITION.replace("$name", player.getName()), PartyLeader);

		return true;
	}

	public static String MainHtmlParty(L2PcInstance st){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Go to the Party Leader") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.PARTY_FINDER_MENSAJE_CENTRAL,"WHITE") + central.LineaDivisora(2);
		MAIN_HTML += central.ItemNeedShowBox(general.PARTY_FINDER_PRICE);
		MAIN_HTML += "<br><center><button value=\"Come on, Take me to my Party leader\" action=\"bypass -h ZeuSNPC goLeader 0 0 0\" width=235 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br>"+central.BotonGOBACKZEUS()+central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}

	public static boolean canUse(L2PcInstance player){
		if(!general._activated()){
			return false;
		}
		
		if(player.inObserverMode()){
			central.msgbox("The party leader is in Observer Mode", player);
			return false;
		}
		
		if(!player.isInParty()){
			central.msgbox(msg.NECESITAS_ESTAR_EN_PARTY, player);
			return false;
		}
		
		if(player.getParty().getLeader().getObjectId() == player.getObjectId()){
			central.msgbox("Your are the leader party.", player);
			return false;
		}
		
		
		if(general.PARTY_FINDER_CAN_USE_ONLY_NOBLE && !player.isNoble()){
			central.msgbox(msg.NECESITAS_SER_NOBLE_PARA_ESTA_OPERACION , player);
			return false;
		}
		
		
		if(!general.PARTY_FINDER_CAN_USE_FLAG && (player.getPvpFlag() > 0) ){
			central.msgbox(msg.NO_PUEDES_USAR_MIS_SERVICIOS_ESTANDO_FLAG, player);
			return false;
		}
		if(general.PARTY_FINDER_CAN_USE_LVL > player.getLevel()){
			central.msgbox(msg.NECESITAS_TENER_UN_LEVEL_MAYOR_A_$level.replace("$level", String.valueOf(general.PARTY_FINDER_CAN_USE_LVL)) , player);
			return false;
		}

		L2PcInstance PartyLeader = player.getParty().getLeader();
		
		if(PartyLeader.isInsideZone(ZoneId.NO_ZEUS)){
			central.msgbox("The Party Leader is in a restricted area", player);
			return false;			
		}
		
		if(general.PARTY_FINDER_GO_LEADER_NOBLE && !PartyLeader.isNoble()){
			central.msgbox(msg.PARTY_FINDER_NO_PARTY_LEADER_NOBLE, player);
			return false;
		}

		if(RaidBossEvent.isPlayerOnRBEvent(PartyLeader)){
			central.msgbox("Leader is in Raidboss Event.", player);
			return false;			
		}
		

		if(!general.PARTY_FINDER_GO_LEADER_ON_ASEDIO){
			if(PartyLeader.isInsideZone(ZoneId.SIEGE)){
				central.msgbox("Leader is on Siege Zone, you cannot use my service right now.", player);
				return false;
			}
		}

		if(general.PARTY_FINDER_USE_NO_SUMMON_RULEZ){
			if(PartyLeader.isInsideZone(ZoneId.NO_SUMMON_FRIEND)){
				central.msgbox(msg.PARTY_FINDER_NO_PARTY_LEADER_NO_SUMMON_ZONE, player);
				return false;
			}
		}

		if(!general.PARTY_FINDER_GO_LEADER_DEATH && PartyLeader.isDead()){
			central.msgbox(msg.PARTY_FINDER_NO_PARTY_LEADER_DEATH, player);
			return false;
		}
		if(general.PARTY_FINDER_GO_LEADER_FLAGPK && ( (PartyLeader.getKarma()>0) || (PartyLeader.getPvpFlag()!=0) )){
			central.msgbox(msg.PARTY_FINDER_NO_PARTY_LEADER_FLAG, player);
			return false;
		}
		if(general.PARTY_FINDER_GO_LEADER_NOBLE && !PartyLeader.isNoble()){
			central.msgbox(msg.PARTY_FINDER_NO_PARTY_LEADER_NOBLE, player);
			return false;
		}

		if(!general.PARTY_FINDER_GO_LEADER_WHEN_ARE_INSTANCE && PartyLeader.isIn7sDungeon()){
			central.msgbox(msg.PARTY_FINDER_NO_PARTY_LEADER_ISIN_INSTANCE, player);
			return false;
		}

		if(!opera.haveItem(player, general.PARTY_FINDER_PRICE)){
			return false;
		}

		return true;
	}
}
