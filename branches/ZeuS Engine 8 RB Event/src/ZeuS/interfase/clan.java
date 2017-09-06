package ZeuS.interfase;

import java.util.List;

import com.l2jserver.Config;
import com.l2jserver.gameserver.data.sql.impl.ClanTable;
import com.l2jserver.gameserver.data.xml.impl.SkillTreesData;
import com.l2jserver.gameserver.instancemanager.CastleManager;
import com.l2jserver.gameserver.instancemanager.FortManager;
import com.l2jserver.gameserver.instancemanager.FortSiegeManager;
import com.l2jserver.gameserver.instancemanager.SiegeManager;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.L2ClanMember;
import com.l2jserver.gameserver.model.L2SkillLearn;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.AcquireSkillType;
import com.l2jserver.gameserver.model.entity.Castle;
import com.l2jserver.gameserver.model.entity.Fort;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.AcquireSkillList;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillLaunched;
import com.l2jserver.gameserver.network.serverpackets.MagicSkillUse;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

import ZeuS.Config.general;
import ZeuS.Config.msg;

public class clan {
	public static String increaseclan(L2PcInstance player){
		String MAIN_HTML ="";
		if ((player.getClan()==null) || !player.isClanLeader()){
  			MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
  			MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Incrase Clan") + central.LineaDivisora(3);
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.SOLO_LIDER_DE_CLAN_PUEDE_REALIZAR_ESTA_OPERACION,"FF0000") + central.LineaDivisora(2);
			MAIN_HTML += "</center></body></html>";
			return MAIN_HTML;
		}
		MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Incrase Clan") + central.LineaDivisora(3);
		MAIN_HTML += central.LineaDivisora(3) + central.headFormat(msg.CLAN_MENSAJE_INCREMENTAR) + central.LineaDivisora(3);

		String MAIN_HTML2 = "<button value=\"Level Up\" action=\"bypass -h ZeuSNPC increase_clan 0 0 0\" width=80 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"><br1>";
		MAIN_HTML2 += "<button value=\"Back\" action=\"bypass -h ZeuSNPC chat4 0 0 0\" width=80 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"><br>";
		MAIN_HTML2 += "<font color=\"LEVEL\">Level 1:</font> 20,000 SP, 650,000 Adena<br1>";
		MAIN_HTML2 += "<font color=\"LEVEL\">Level 2:</font> 100,000 SP, 2,500,000 Adena<br1>";
		MAIN_HTML2 += "<font color=\"LEVEL\">Level 3:</font> 350,000 SP, Evidence of Blood<br1>";
		MAIN_HTML2 += "<font color=\"LEVEL\">Level 4:</font> 1,000,000 SP, Evidence of Determination<br1>";
		MAIN_HTML2 += "<font color=\"LEVEL\">Level 5:</font> 2,500,000 SP, Evidence of Aspiration<br1>";
		MAIN_HTML2 += "<font color=\"LEVEL\">Level 6:</font> 10,000 Clan Fame points,<br1>more than 30 clan members<br1>";
		MAIN_HTML2 += "<font color=\"LEVEL\">Level 7:</font> 20,000 Clan Fame points,<br1>more than 80 clan members<br1>";
		MAIN_HTML2 += "<font color=\"LEVEL\">Level 8:</font> 40,000 Clan Fame points,<br1>more than 120 clan members<br1>";
		MAIN_HTML2 += "<font color=\"LEVEL\">Level 9:</font> 40,000 Clan Fame points,<br1>more than 120 clan members, 150 Blood Oaths<br1>";
		MAIN_HTML2 += "<font color=\"LEVEL\">Level 10:</font> 40,000 Clan Fame points,<br1>more than 140 clan members, 5 Blood Alliances<br1>";
		MAIN_HTML2 += "<font color=\"LEVEL\">Level 11:</font> 75,000 Clan Fame points,<br1>more than 170 clan members, Territory Owner<br1>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(MAIN_HTML2,"WHITE") + central.LineaDivisora(2);
		MAIN_HTML += "</center>"+central.BotonGOBACKZEUS()+ central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}
	public static boolean increase_clan(L2PcInstance player){
		if (!player.isClanLeader()){
    		player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
    		return false;
		}
		if (player.getClan().levelUpClan(player)){
 			player.broadcastPacket(new MagicSkillUse(player, 5103, 1, 0, 0));
			player.broadcastPacket(new MagicSkillLaunched(player, 5103, 1));
		}
		return true;
	}

	public static String DisbandClan(L2PcInstance player){
		//if st.getPlayer().getClan() == None or not st.getPlayer().isClanLeader() :
		String MAIN_HTML="";
		if((player.getClan()==null) || !player.isClanLeader()){
  			MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
  			MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Dissolution Clan") + central.LineaDivisora(3);
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.SOLO_LIDER_DE_CLAN_PUEDE_REALIZAR_ESTA_OPERACION,"FF0000") + central.LineaDivisora(2);
			MAIN_HTML += "</center></body></html>";
			return MAIN_HTML;
		}

		MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Dissolved Clan") + central.LineaDivisora(3);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.CLAN_MENSAJE_ELIMINAR,"FF0000") + central.LineaDivisora(2);
		String MAIN_HTML2 = "<button value=\"Dissolution\" action=\"bypass -h ZeuSNPC dissolve_clan 0 0 0\" width=80 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"><br1>";
		MAIN_HTML2 += "<button value=\"Back\" action=\"bypass -h ZeuSNPC chat4 0 0 0\" width=80 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(MAIN_HTML2) + central.LineaDivisora(1);
		MAIN_HTML += central.getPieHTML() + "</center></body></html>";
		return MAIN_HTML;
	}

	public static boolean dissolve_clan(L2PcInstance player){
		if(!player.isClanLeader()){
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return false;
		}
		L2Clan clan = player.getClan();

		if(clan.getAllyId() != 0){
    		player.sendPacket(SystemMessageId.CANNOT_DISPERSE_THE_CLANS_IN_ALLY);
    		return false;
		}

		if(clan.isAtWar()){
			player.sendPacket(SystemMessageId.CANNOT_DISSOLVE_WHILE_IN_WAR);
			return false;
		}

		if((clan.getCastleId() !=0) || (clan.getHideoutId() != 0) || (clan.getFortId() != 0)){
			player.sendPacket(SystemMessageId.CANNOT_DISSOLVE_WHILE_OWNING_CLAN_HALL_OR_CASTLE);
			return false;
		}

		for(Castle  castle: CastleManager.getInstance().getCastles()){
			if( SiegeManager.getInstance().checkIsRegistered(clan, castle.getResidenceId())){
				player.sendPacket(SystemMessageId.CANNOT_DISSOLVE_WHILE_IN_SIEGE);
				return false;
			}
		}

		for(Fort fort : FortManager.getInstance().getForts()){
			if(FortSiegeManager.getInstance().checkIsRegistered(clan, fort.getResidenceId())){
				player.sendPacket(SystemMessageId.CANNOT_DISSOLVE_WHILE_IN_SIEGE);
			}
		}

		if (player.isInsideZone(ZoneId.SIEGE))
		{
			player.sendPacket(SystemMessageId.CANNOT_DISSOLVE_WHILE_IN_SIEGE);
			return false;
		}
		if (clan.getDissolvingExpiryTime() > System.currentTimeMillis())
		{
			player.sendPacket(SystemMessageId.DISSOLUTION_IN_PROGRESS);
			return false;
		}

		clan.setDissolvingExpiryTime(System.currentTimeMillis() + (Config.ALT_CLAN_DISSOLVE_DAYS * 86400000L)); // 24*60*60*1000 = 86400000
		clan.updateClanInDB();

		ClanTable.getInstance().scheduleRemoveClan(clan.getId());

		// The clan leader should take the XP penalty of a full death.
		player.calculateDeathExpPenalty(null, false);
		ClanTable.getInstance().scheduleRemoveClan(clan.getId());
		return true;
	}
	public static String RestoreClan(L2PcInstance player){
		String MAIN_HTML="";
		if((player.getClan()==null) || !player.isClanLeader()){
  			MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
  			MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Restore Clan") + central.LineaDivisora(3);
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.SOLO_LIDER_DE_CLAN_PUEDE_REALIZAR_ESTA_OPERACION,"FF0000") + central.LineaDivisora(2);
			MAIN_HTML += "</center></body></html>";
			return MAIN_HTML;
		}
		MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Restore Clan") + central.LineaDivisora(3);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(msg.CLAN_MENSAJE_RESTORE,"FF0000") + central.LineaDivisora(1);
		String MAIN_HTML2 = "<button value=\"Restoration\" action=\"bypass -h ZeuSNPC recover_clan 0 0 0\" width=80 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"><br1>";
		MAIN_HTML2 += "<button value=\"Back\" action=\"bypass -h ZeuSNPC chat4 0 0 0\" width=80 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(MAIN_HTML2) + central.LineaDivisora(1);
		MAIN_HTML += central.getPieHTML()+"</center></body></html>";
		return MAIN_HTML;
	}
	public static boolean recover_clan(L2PcInstance player){
        if(!player.isClanLeader()){
        	player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
    		return false;
        }
		L2Clan clan = player.getClan();
		clan.setDissolvingExpiryTime(0);
		clan.updateClanInDB();
		central.msgbox(msg.PROCESO_DE_ELIMINACION_DE_CLAN_CANCELADA, player);
		return true;
	}

	public static String giveclanl(L2PcInstance player){
		String MAIN_HTML="";
		if((player.getClan()==null) || !player.isClanLeader()){
  			MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
  			MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Change Clan leader") + central.LineaDivisora(3);
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.SOLO_LIDER_DE_CLAN_PUEDE_REALIZAR_ESTA_OPERACION,"FF0000") + central.LineaDivisora(2);
			MAIN_HTML += "</center></body></html>";
			return MAIN_HTML;
		}
		MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
	  	MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Change Clan leader") + central.LineaDivisora(3);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.CLAN_CHANGE_CLAN_LEADER,"FF0000") + central.LineaDivisora(2);
		String MAIN_HTML2 = "<edit var=\"name\" width=110><br><br><button value=\"Enter\" action=\"bypass -h ZeuSNPC change_clan_leader $name no_data 0 0\" width=80 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"><br1>";
		MAIN_HTML2 += "<button value=\"Back\" action=\"bypass -h ZeuSNPC chat4 0 0 0\" width=80 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(MAIN_HTML2)+central.LineaDivisora(2)+central.getPieHTML()+"</center></body></html>";
		return MAIN_HTML;
	}
	public static boolean change_clan_leader(L2PcInstance player, String eventParam1){
		if (!player.isClanLeader())
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return false;
		}

		if (player.getName().equalsIgnoreCase(eventParam1))
		{
			central.msgbox(msg.INGRESE_NOMBRE_DEL_MIEMBRO_A_POSEER_EL_CLAN, player);
			return false;
		}

		final L2Clan clan = player.getClan();
		final L2ClanMember member = clan.getClanMember(eventParam1);
		if (member == null)
		{
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_DOES_NOT_EXIST);
			sm.addString(eventParam1);
			player.sendPacket(sm);
			return false;
		}

		if (!member.isOnline())
		{
			player.sendPacket(SystemMessageId.INVITED_USER_NOT_ONLINE);
			return false;
		}

		if (member.getPlayerInstance().isAcademyMember())
		{
			player.sendPacket(SystemMessageId.RIGHT_CANT_TRANSFERRED_TO_ACADEMY_MEMBER);
			return false;
		}

		clan.setNewLeader(member);
		return true;
	}
	public static String learn_clan_skills(L2PcInstance player){
		String MAIN_HTML="";
		if((player.getClan()==null) || !player.isClanLeader()){
  			MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
  			MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Learn Clan Skills") + central.LineaDivisora(3);
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.SOLO_LIDER_DE_CLAN_PUEDE_REALIZAR_ESTA_OPERACION,"FF0000") + central.LineaDivisora(2);
			MAIN_HTML += "</center></body></html>";
			return MAIN_HTML;
		}

		final List<L2SkillLearn> skills = SkillTreesData.getInstance().getAvailablePledgeSkills(player.getClan());
		//freya final AcquireSkillList asl = new AcquireSkillList(SkillType.Pledge);
		final AcquireSkillList asl = new AcquireSkillList(AcquireSkillType.PLEDGE);
		int counts = 0;

		/*Freya
		 *for (L2SkillLearn s : skills){
			asl.addSkill(s.getSkillId(), s.getSkillLevel(), s.getSkillLevel(), s.getLevelUpSp(), s.getSocialClass());
			counts++;
		}*/

		for (L2SkillLearn s : skills)
		{
			asl.addSkill(s.getSkillId(), s.getSkillLevel(), s.getSkillLevel(), s.getLevelUpSp(), s.getSocialClass().ordinal());
			counts++;
		}

		if (counts == 0)
		{
			if (player.getClan().getLevel() < 8)
			{
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.DO_NOT_HAVE_FURTHER_SKILLS_TO_LEARN_S1);
				if (player.getClan().getLevel() < 5)
				{
					sm.addInt(5);
				}
				else
				{
					sm.addInt(player.getClan().getLevel() + 1);
				}
				player.sendPacket(sm);
			}
			else
			{
	  			MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
	  			MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Learn Clan Skills") + central.LineaDivisora(3);
				MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.NO_HAY_SKILL_PARA_APRENDER,"FF0000") + central.LineaDivisora(2);
				MAIN_HTML += central.getPieHTML() + "</center></body></html>";
				return MAIN_HTML;
			}
		}
		else
		{
			player.sendPacket(asl);
		}

		return MAIN_HTML;
	}
	public static String createclan(L2PcInstance player){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Create Clan") + central.LineaDivisora(3);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(msg.CLAN_NOMBRE_CLAN_CREAR,"LEVEL") + central.LineaDivisora(1);
		String MAIN_HTML2 = "<edit var=\"name\" width=110><br><br>";
		MAIN_HTML2 += "<button value=\"Enter\" action=\"bypass -h ZeuSNPC create_clan $name no_data 0 0 \" width=80 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"><br1>";
		MAIN_HTML2 += "<button value=\"Back\" action=\"bypass -h ZeuSNPC chat4 0 0 0\" width=80 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += central.LineaDivisora(3) + central.headFormat(MAIN_HTML2) + central.LineaDivisora(3);
		MAIN_HTML += central.getPieHTML() + "</center></body></html>";
		return MAIN_HTML;
	}

	public static void create_clan(L2PcInstance player, String eventParam1){
		ClanTable.getInstance().createClan(player, eventParam1);
	}

	public static String createally(L2PcInstance player){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Create Alliance") + central.LineaDivisora(3);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(msg.CLAN_CREAR_ALIANZA,"LEVEL") + central.LineaDivisora(1);
		String MAIN_HTML1 = "<edit var=\"name\" width=110><br><br>";
		MAIN_HTML1 += "<button value=\"Enter\" action=\"bypass -h ZeuSNPC create_ally $name no_data 0 0 \" width=80 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"><br1>";
		MAIN_HTML1 += "<button value=\"Back\" action=\"bypass -h ZeuSNPC chat4 0 0 0\" width=80 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(MAIN_HTML1) + central.LineaDivisora(1);
		MAIN_HTML += central.getPieHTML() + "</center></body></html>";
		return MAIN_HTML;
	}

	public static boolean create_ally(L2PcInstance player, String eventParam1){
		if (eventParam1.isEmpty()){
			central.msgbox(msg.DEBE_INRGESAR_EL_NOMBRE_DE_LA_ALIANZA, player);
			return false;
		}

		if (player.getClan() == null){
			player.sendPacket(SystemMessageId.ONLY_CLAN_LEADER_CREATE_ALLIANCE);
			return false;
		}else{
			player.getClan().createAlly(player, eventParam1);
		}
		return true;
	}


	public static boolean dissolve_allly(L2PcInstance player){
		if(player.getClan() == null){
			central.msgbox(msg.NO_TIENES_CLAN, player);
			return false;
		}
		if(!player.isClanLeader()){
			central.msgbox(msg.SOLO_LIDER_DE_CLAN_PUEDE_REALIZAR_ESTA_OPERACION, player);
			return false;
		}
		player.getClan().dissolveAlly(player);
		return true;

	}

}
