package ZeuS.event;

import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.gameserver.model.L2ClanMember;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.skills.CommonSkill;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse;
//import com.mysql.jdbc.EscapeTokenizer;
import com.mysql.cj.core.util.EscapeTokenizer; 

import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.procedimientos.opera;

public class ClanReputationEvent {

	private static final Logger _log = Logger.getLogger(ClanReputationEvent.class.getName());
	
	public static void mainWindows(L2PcInstance player){
		String main = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		
		main += central.LineaDivisora(1) + central.headFormat("Clan Reward","FF8000") + central.LineaDivisora(1);
		
		if(!general.EVENT_REPUTATION_CLAN){
			main = central.LineaDivisora(1) + central.headFormat("Clan Reward Disabled")+central.LineaDivisora(1);
			opera.enviarHTML(player, main);
			return;
		}

		
		String requerimientos = "<table with=280>";
		requerimientos += "<tr><td with=280 align=CENTER>Min Player: <font color=088A08>"+ String.valueOf(general.EVENT_REPUTATION_MIN_PLAYER) +"</font</td></tr>";
		requerimientos += "<tr><td with=280 align=CENTER>All Member's online: <font color=088A08>"+ (general.EVENT_REPUTATION_NEED_ALL_MEMBER_ONLINE ? "YES" : "NO") +"</font></td></tr>";
		requerimientos += "</table>";
		
		main += central.LineaDivisora(1) + central.headFormat("<font color=\"FF8000\">Requirements</font><br1>" + requerimientos,"LEVEL") + central.LineaDivisora(1);

		requerimientos = "<table with=280>";
		requerimientos += "<tr><td with=280 align=CENTER>Clan Level: <font color=088A08>"+ String.valueOf(general.EVENT_REPUTATION_LVL_TO_GIVE) +"</font></td></tr>";
		requerimientos += "<tr><td with=280 align=CENTER>Clan Reputation: <font color=088A08>"+ String.valueOf(general.EVENT_REPUTATION_REPU_TO_GIVE) +"</font></td></tr>";		
		requerimientos += "</table>";
		
		main += central.LineaDivisora(1) + central.headFormat("<font color=\"FF8000\">Reward</font><br1>" + requerimientos,"LEVEL") + central.LineaDivisora(1);
	
		
		String btnPremiar = "<button value=\"Reward me\" action=\"bypass ZeuSNPC ClanReward\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		
		main += central.LineaDivisora(1) + central.headFormat(btnPremiar) + central.LineaDivisora(1);
		
		
		
		main += central.getPieHTML(true) + "</body></html>";
		
		opera.enviarHTML(player, main);
		
	}
	
	public static String delegar(L2PcInstance player, String params){

		String retorno = "";
		
		if(params.equals("ClanReward")){
			if(GiveReward(player)){
				retorno = "<html><title>"+ general.TITULO_NPC()  + "</title><body>";
				retorno += central.LineaDivisora(1) + central.headFormat("Clan Reward","FF8000") + central.LineaDivisora(1);
				retorno += central.LineaDivisora(1) + central.headFormat("DONE!!!!!","LEVEL") + central.LineaDivisora(1);
				retorno += central.getPieHTML(true) + "</body></html>";
			}
		}
		
		return retorno;
	}
	
	
	public static boolean GiveReward(L2PcInstance player){
		if(!isClanLeader(player)){
			central.msgbox("We are sorry, but just the clan leader can do this request", player);
			return false;
		}
		
		if(!isAllMembersOnline(player)){
			return false;
		}
		
		if(getCountMember(player) < general.EVENT_REPUTATION_MIN_PLAYER){
			central.msgbox("We are sorry, but you need at least " + String.valueOf(general.EVENT_REPUTATION_MIN_PLAYER) + " clan member's to do this procedure", player);
			return false;
		}
		
		int lvlClan = player.getClan().getLevel();
		
		if(lvlClan >= general.EVENT_REPUTATION_LVL_TO_GIVE){
			central.msgbox("You clan is invalid for this request", player);
			return false;
		}
		
		player.getClan().setLevel(general.EVENT_REPUTATION_LVL_TO_GIVE);
		player.getClan().changeLevel(general.EVENT_REPUTATION_LVL_TO_GIVE);
		player.getClan().setReputationScore(player.getClan().getReputationScore() + general.EVENT_REPUTATION_REPU_TO_GIVE, true);
		player.getClan().updateClanInDB();
		player.getClan().broadcastClanStatus();
		
		RewardEfect(player);
		central.msgbox("Congratulation!!! You clan has been rewarded", player);
		
		return true;
	}
	
	public static void RewardEfect(L2PcInstance player){
		for(L2ClanMember cha : player.getClan().getMembers()){
			try{
				L2PcInstance activeChar = cha.getPlayerInstance();
				Skill skill = CommonSkill.LARGE_FIREWORK.getSkill();
				if (skill != null)
				{
					activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, skill.getId(), skill.getLevel(), skill.getHitTime(), skill.getReuseDelay()));
				}			
			}catch(Exception a){
				
			}
		}
	}
	

	public static boolean isClanLeader(L2PcInstance player){
		if(player.getClan()==null){
			return false;
		}
		return player.isClanLeader();
	}
	
	@SuppressWarnings("unused")
	public static boolean isAllMembersOnline(L2PcInstance player){
		Vector<L2ClanMember> Playerss = new Vector<L2ClanMember>();
		
		if(!general.EVENT_REPUTATION_NEED_ALL_MEMBER_ONLINE){
			return true;
		}
		for(L2ClanMember cha : player.getClan().getMembers()){
			if(cha!=null){
				if(cha.isOnline()){
					if(!cha.getPlayerInstance().getClient().isDetached()){
						Playerss.add(cha);
					}
					//central.msgbox("Sorry, but u need all member's online.", player);
				}
			}
		}
		if(Playerss == null){
			central.msgbox("Sorry, but u need all member's online.", player);
			return false;
		}else{
			if(Playerss.size() < general.EVENT_REPUTATION_MIN_PLAYER){
				central.msgbox("Sorry, but u need all member's online.", player);
				return false;
			}
		}
		return true;
	}
	
	private static int getCountMember(L2PcInstance player){
		return player.getClan().getMembersCount();
	}
	
	private static void UpLevelToClan(L2PcInstance player){
		
	}
	
}
