package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.GameServer;
import com.l2jserver.gameserver.data.xml.impl.ClassListData;
import com.l2jserver.gameserver.data.xml.impl.SkillTreesData;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.skills.Skill;

import ZeuS.Config.general;
import ZeuS.Config.msg;

public class delevel {

	private static final Logger _log = Logger.getLogger(GameServer.class.getName());

	private static void restoreSkills(L2PcInstance player)
	{
		final String RESTORE_SKILLS_FOR_CHAR = "SELECT skill_id,skill_level FROM character_skills WHERE charId=? AND class_index=?";
		try (Connection con = ConnectionFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(RESTORE_SKILLS_FOR_CHAR))
		{
			// Retrieve all skills of this L2PcInstance from the database
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, player.getClassIndex());
			final ResultSet rset = statement.executeQuery();

			// Go though the recordset of this SQL query
			while (rset.next())
			{
				final int id = rset.getInt("skill_id");
				final int level = rset.getInt("skill_level");

				// Create a L2Skill object for each record
				final Skill skill = SkillData.getInstance().getSkill(id, level);

				if (skill == null)
				{
					_log.warning("ZeuS->Skipped null skill Id: " + id + " Level: " + level + " while restoring player skills for playerObjId: " + player.getObjectId() +", Name: " + player.getName());
					continue;
				}

				// Add the L2Skill object to the L2Character _skills and its Func objects to the calculator set of the L2Character
				player.addSkill(skill);

				if (general.DELEVEL_CHECK_SKILL)
				{
					if (!SkillTreesData.getInstance().isSkillAllowed(player, skill))
					{
						central.msgbox(msg.DELEVEL_REMOVE_INVALID_SKILL_$nameskill_$idlevel_$classname.replace("$nameskill", skill.getName()).replace("$idlevel", String.valueOf(skill.getLevel())).replace("$classname",ClassListData.getInstance().getClass(player.getClassId()).getClassName()) ,player);
						player.removeSkill(skill);
					}
				}
			}
			rset.close();
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "ZeuS->Could not restore character " + player.getName() +"("+ player.getObjectId() +")" + " skills: " + e.getMessage(), e);
		}
	}

	public static String DelevelMenu(L2PcInstance st){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Delevel Manager") + central.LineaDivisora(2);
		MAIN_HTML += "<center><br>";
		MAIN_HTML += msg.DELEVEL_MANAGER_MENSAJE_HASTA_$level.replace("$level", String.valueOf(general.DELEVEL_LVL_MAX));
		//MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Necesitarás " + central.ItemNeedShow(general.DELEVEL_PRICE)  ,"LEVEL") + central.LineaDivisora(2);
		MAIN_HTML += central.ItemNeedShow(general.DELEVEL_PRICE);
		MAIN_HTML += "<button value=\"delevel 1 lvl\" action=\"bypass -h ZeuSNPC Delevel 0 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br1>";
		MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br>"+central.BotonGOBACKZEUS()+"</center>"+central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}

	public static boolean canUse(L2PcInstance player){
		if(general.DELEVEL_LVL_MAX > player.getLevel()){
			central.msgbox(msg.NECESITAS_TENER_UN_LEVEL_MAYOR_A_$level.replace("$level", String.valueOf(general.DELEVEL_LVL_MAX)), player);
			return false;
		}
		if(general.DELEVEL_NOBLE && !player.isNoble()){
			central.msgbox(msg.NECESITAS_SER_NOBLE, player);
			return false;
		}
		return true;
	}
	public static boolean delevel1(L2PcInstance player){
		try{
			//int Mayorex = (int) (player.getStat().getExp() + player.getStat().getExpForLevel(player.getLevel()-1));
			player.getStat().setLevel((byte) (player.getLevel()-1));
			restoreSkills(player);
			player.checkPlayerSkills();
			player.broadcastUserInfo();
			player.storeMe();
			return true;
		}catch (Exception e) {
			_log.warning("DELEVEL->" + e.getMessage());
			return false;
		}
	}
	public static String htmlDelevel(L2PcInstance player){
		String htmltext = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		htmltext += central.LineaDivisora(2) + central.headFormat("DELEVEL MANAGER") + central.LineaDivisora(2);
		String BOTON_DISMINUIR = "<center><button value=\"delevel 1 lvl\" action=\"bypass -h ZeuSNPC " +general.QUEST_INFO+ " Delevel 0 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		htmltext += central.LineaDivisora(2) + central.headFormat("Reduced level to: "+ String.valueOf(player.getLevel())," LEVEL") + central.LineaDivisora(2) + BOTON_DISMINUIR;
		htmltext += central.BotonGOBACKZEUS() + central.getPieHTML() + "</body></html>";
		return htmltext;
	}

}
