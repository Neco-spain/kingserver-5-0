package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.gameserver.GameServer;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.ClassId;

import ZeuS.Config.general;
import ZeuS.Config.msg;


public class classmaster {

	private static final Logger _log = Logger.getLogger(GameServer.class.getName());


	private static final int[] ParentProfesion ={0,0,1,1,0,4,4,0,7,7,10,11,11,11,10,15,15,18,19,19,18,22,22,25,26,26,25,29,31,32,33,31,35,35,38,39,39,
38,42,44,45,44,47,49,50,50,53,54,53,56,2,3,5,6,9,8,12,13,14,16,17,20,21,23,24,27,26,30,33,34,36,37,40,41,43,46,48,51,52,55,123,124,125,
125,126,126,127,128,129,130,126,135};


	Connection conn;
	String ConsultaMYSQL;
	PreparedStatement act;
	ResultSet rs;

	public static boolean AddProfesion(L2PcInstance player, int idProfecion){
		try{
			player.setClassId(idProfecion);
			if(player.isSubClassActive()){
				player.getSubClasses().get(player.getClassIndex()).setClassId(player.getActiveClass());
			}else{
	    		player.setBaseClass(player.getActiveClass());
			}
			player.broadcastUserInfo();
			return true;
		}catch(Exception a){
			_log.warning("CLASS MASTER->"+a.getMessage());
			return false;
		}
	}

	private static final int getMinLevel(int level)
	{
		switch (level)
		{
			case 0:
				return 20;
			case 1:
				return 40;
			case 2:
				return 76;
			default:
				return Integer.MAX_VALUE;
		}
	}

	private static final boolean validateClassId(ClassId oldCID, int val)
	{
		try
		{
			return validateClassId(oldCID, ClassId.values()[val]);
		}
		catch (Exception e)
		{
			// possible ArrayOutOfBoundsException
		}
		return false;
	}

	private static final boolean validateClassId(ClassId oldCID, ClassId newCID)
	{
		if ((newCID == null) || (newCID.getRace() == null))
		{
			return false;
		}

		if (oldCID.equals(newCID.getParent()))
		{
			return true;
		}

		if (Config.ALLOW_ENTIRE_TREE && newCID.childOf(oldCID))
		{
			return true;
		}

		return false;
	}

	private static Vector<String> getProfesiones(L2PcInstance player){
		int level = player.getClassId().level() + 1;//ParentProfesion[player.getBaseClass()];

		Vector<String> profesiones = new Vector<String>();
		final ClassId currentClassId = player.getClassId();
		final int minLevel = getMinLevel(currentClassId.level());

		String BtnProfesion = "";

		if ((player.getLevel() >= minLevel) || Config.ALLOW_ENTIRE_TREE)
		{
			for (ClassId cid : ClassId.values())
			{
				if ((cid == ClassId.inspector) && (player.getTotalSubClasses() < 2))
				{
					continue;
				}
				if (validateClassId(currentClassId, cid) && (cid.level() == level))
				{
					BtnProfesion = central.LineaDivisora(1) + central.headFormat("<button value=\""+central.getClassName(player, cid.getId())+"\" action=\"bypass -h ZeuSNPC tranferclassGO "+String.valueOf(cid.getId())+" 0 0\" width=150 height=28 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">") + central.LineaDivisora(1);
					profesiones.add(BtnProfesion);
				}
			}
		}
		return profesiones;
	}

	public static String okTransfer(L2PcInstance player){
		String MAIN_H = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_H += central.LineaDivisora(2) + central.headFormat("Transfer") + central.LineaDivisora(2);
		MAIN_H += central.LineaDivisora(2) + central.headFormat(msg.CLASS_MASTER_FELICIDADES_$profesion.replace("$profesion", central.getClassName(player)),"WHITE") + central.LineaDivisora(2);
		MAIN_H += central.BotonGOBACKZEUS() + "<br><br>" + central.getPieHTML() + "</body></html>";
		return MAIN_H;
	}

	public static String classmasterShow(L2PcInstance st){
		//int classId = st.getClassId().getId();
		int level = st.getLevel();
		int jobLevel=st.getClassId().level();
		//int ClassJob = st.getClassIndex();
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Class Tranfer Manager") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(msg.CLASS_MASTER_MENSAJE_ELECCION,"FF0000");

		Vector<String>Profesiones = new Vector<String>();

		Profesiones = getProfesiones(st);

		for(String _profes : Profesiones){
			MAIN_HTML += _profes;
		}
		if(Profesiones.size()==0){
			if((jobLevel ==0) && (level < 20)) {
				MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Come back here when you reach level 20.<br1>No more Class Changes Available","FF0000")+central.LineaDivisora(3);
			} else if((jobLevel <=1) && (level < 40)) {
				MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Come back here when you reach level 40.<br1>No more Class Changes Available","FF0000")+central.LineaDivisora(3);
			} else if((jobLevel <=2) && (level < 76)) {
				MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Come back here when you reach level 76.<br1>No more Class Changes Available","FF0000")+central.LineaDivisora(3);
			} else {
				MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Class Manager") + central.LineaDivisora(2);
			}
				MAIN_HTML += central.LineaDivisora(2) + central.headFormat("No more Class Changes Available","LEVEL") + central.LineaDivisora(2);
		}
		//}
		MAIN_HTML += central.BotonGOBACKZEUS() + central.getPieHTML()+"</center></body></html>";
		return MAIN_HTML;
	}
}
