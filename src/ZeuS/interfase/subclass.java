package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.GameServer;
import com.l2jserver.gameserver.enums.Race;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.ClassId;
import com.l2jserver.gameserver.model.base.PlayerClass;
import com.l2jserver.gameserver.model.base.SubClass;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

import ZeuS.Config.general;

public class subclass {

	static Connection conn;
	static PreparedStatement sub;
	static ResultSet ss;

	private static final Logger _log = Logger.getLogger(GameServer.class.getName());

	public static String subclassopcions(L2PcInstance st,String cases,int id,int index){

		String MAIN_HTML ="";
	   	if(st.isCastingNow() || st.isAllSkillsDisabled()){
	   		st.sendPacket(SystemMessageId.SUBCLASS_NO_CHANGE_OR_CREATE_WHILE_SKILL_IN_USE);
	        return "";
	   	}
        if(cases.equals("addsub")){
        	if(st.getTotalSubClasses() >= Config.MAX_SUBCLASS){
               		st.sendMessage("You can not add more subclasses");
               		return "";
        	}else{
				MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
				MAIN_HTML += subclasslist(st,"acceptsub",0);
				MAIN_HTML += "</center></body></html>";
				return MAIN_HTML;
        	}
        }

		if(cases.equals("acceptsub")){
				if (!st.addSubClass(id,st.getTotalSubClasses() + 1)){
					st.sendMessage("The sub class could not be added.");
					return "";
				}
				st.setActiveClass(st.getTotalSubClasses());
				MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
				MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Sub Class") + central.LineaDivisora(2);
				MAIN_HTML += central.LineaDivisora(1) + central.headFormat("The sub class of <font color=\"LEVEL\">"+ central.getClassName(st,id)+"</font><br> has been added.<br>","LEVEL") + central.LineaDivisora(1);
				MAIN_HTML += "</center></body></html>";
				st.sendPacket(SystemMessageId.CLASS_TRANSFER);
				return MAIN_HTML;
		}
		if(cases.equals("acceptchangesub")){
	        //	if(!st.setActiveClass(index)){
	        //        	st.sendMessage("The sub class could not be changed.");
           //             return "";
	        //	}
				MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
				MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Sub Class") + central.LineaDivisora(2);
				MAIN_HTML += central.LineaDivisora(1) + central.headFormat("Change Subclass:<br>Your active class is now a:<br><font color=\"LEVEL\">"+central.getClassName(st,id)+"</font>","LEVEL") + central.LineaDivisora(1);
				MAIN_HTML += "<br><br><br><br><br>" + central.getPieHTML() + "</center></body></html>";
				st.sendPacket(SystemMessageId.SUBCLASS_TRANSFER_COMPLETED);
				central.setBlockPJSeg(st, 2);
				return MAIN_HTML;
		}
		int xcharid;
		String xsubclassid;
		String xindex;
		Object xclassname;
		if(cases.equals("changesub")){
	        	if(st.getTotalSubClasses() > Config.MAX_SUBCLASS){
	               	st.sendMessage("You can now only delete one of your current sub classes.");
	               	return "";
	        	}
				int i=0;
				MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
				MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Change SubClass")  + central.LineaDivisora(2);
				MAIN_HTML += "<font color=\"FF0000\">Select to subclass want to change</font><br><br1>";
				MAIN_HTML += central.LineaDivisora(2);
				xcharid = st.getObjectId();

				Vector<String> subClases = new Vector<String>();
				subClases = getSubClasesList(st);

				for(String Html:subClases){
					MAIN_HTML += Html;
				}

				MAIN_HTML += central.BotonGOBACKZEUS() + central.getPieHTML() + "</center></body></html>";

				return MAIN_HTML;
		}

		if(cases.equals("deletesub")){
				int i=0;
				xcharid = st.getObjectId();
				MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
				MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Add Subclase") + central.LineaDivisora(2);
				MAIN_HTML += "<font color=\"FF0000\">Which sub class do you wish to delete?</font><br><br1>";

				//acceptnewsub

				Vector<String> SubClaseDeleting = new Vector<String>();
				SubClaseDeleting = getSubClasesList(st,false,"acceptnewsub");

				for(String SubC : SubClaseDeleting){
					MAIN_HTML += SubC;
				}
				MAIN_HTML += "<br>If you change a sub class, you'll start at level 40<br1>after the 2nd class transfer.";
				MAIN_HTML += central.BotonGOBACKZEUS() + central.getPieHTML() + "</center></body></html>";
				if (SubClaseDeleting.size()>0) {
					return MAIN_HTML;
				} else {
					st.sendMessage("There are no sub classes available at this time.");
				}
		}

		if(cases.equals("acceptnewsub")){
			MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
			MAIN_HTML += subclasslist(st,"acceptdelsub",index);
			MAIN_HTML += "</center></body></html>";
			return MAIN_HTML;
		}

		if(cases.equals("acceptdelsub")){
			if(st.modifySubClass(index,id)){
				st.stopAllEffects();
	            st.setActiveClass(index);
				MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
				MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Sub Class") + central.LineaDivisora(2);
				MAIN_HTML += central.LineaDivisora(1) + central.headFormat("Change SubClass<br1> Your sub class has been changed to <font color=LEVEL>"+central.getClassName(st,id)+"</font>","WHITE") + central.LineaDivisora(1);
				MAIN_HTML += central.getPieHTML()+"</center></body></html>";
				st.sendPacket(SystemMessageId.ADD_NEW_SUBCLASS);
				central.setBlockPJSeg(st, 2);
				return MAIN_HTML;
			}else{
                st.sendMessage("The sub class could not be added, you have been reverted to your base class.");
                return "";
			}
		}
		return "";
	}



	public static String subclasslist(L2PcInstance st,String cases,int index){
		int charClassId = st.getClassId().getId();
	        if(((charClassId >= 88) && (charClassId <= 118)) || ((charClassId >= 131) && (charClassId <= 134)) || (charClassId == 136)){
	        	if (st.getClassId().getParent() != null) {
					charClassId = st.getClassId().getParent().ordinal();
				}
	        }
		int num=0;
		Vector<String> SubClasses = new Vector<String>();

		String HTML = central.LineaDivisora(2) + central.headFormat("Add Subclase") + central.LineaDivisora(2);
		if (index == 0) {
			HTML += "<br><font color=\"FF0000\">Choose sub class to add</font><br>" + central.LineaDivisora(2);
			SubClasses = getNewSubClases(st, cases, index);
		}
		if (index > 0) {
			HTML += "<font color=\"FF0000\">Please, choose a new sub class to change to.</font><br1><font color=\"LEVEL\">Warning!</font> All classes and skills for this class<br1>will be removed.";
			SubClasses = getSubClasesChange(st,cases,index);
		}



		for(String _subclass : SubClasses){
			HTML += _subclass;
		}



		if((num == 1) || (st.getLevel() < 75)){
			HTML = "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"FF0000\">You may not add a new sub class before <br>you are level 75 on your previous class.</font><br>";
		}
		return HTML;
	}

	public static String getsecondclass(L2PcInstance st,int class_list_Id){
		String val = "0";
		try{
		Connection con = ConnectionFactory.getInstance().getConnection();
		PreparedStatement skillList = con.prepareStatement("SELECT * FROM class_list WHERE id = \""+String.valueOf(class_list_Id)+"\"");
		ResultSet sil = skillList.executeQuery();
		if(sil != null){
			sil.next();
			try{
				val = sil.getString("parent_id");
			}catch (Exception e) {
				val = "0";
			}

		}
		try{
			con.close();
		}catch (SQLException e) {
			// TODO: handle exception
		}
		}catch (SQLException e) {
			return val;
		}
		return val;
	}
	
	protected static Vector<Integer> getSubClases(L2PcInstance player){
		
		Vector<Integer> SubClases = new Vector<Integer>();
		
		Set<PlayerClass> subsAvailable = getAvailableSubClasses(player);
		// another validity check
		if ((subsAvailable == null) || subsAvailable.isEmpty())
		{
			player.sendMessage("There are no sub classes available at this time.");
			return SubClases;
		}

		String MAIN_HTML;

		for (PlayerClass subClass : subsAvailable)
		{
			SubClases.add(subClass.ordinal());
		}

		return SubClases;		
		
	}

	public static Vector<String> getSubClasesChange(L2PcInstance player,String linkDeriva, int index){
		Vector<String> SubClases = new Vector<String>();
		if ((index < 1) || (index > Config.MAX_SUBCLASS))
		{
			return SubClases;
		}

		Set<PlayerClass> subsAvailable = getAvailableSubClasses(player);
		// another validity check
		if ((subsAvailable == null) || subsAvailable.isEmpty())
		{
			player.sendMessage("There are no sub classes available at this time.");
			return SubClases;
		}

		String MAIN_HTML;

		for (PlayerClass subClass : subsAvailable)
		{
			//StringUtil.append(content6, "<a action=\"bypass -h npc_%objectId%_Subclass 7 ", String.valueOf(paramOne), " ", String.valueOf(subClass.ordinal()), "\" msg=\"1445;", "\">", ClassListData.getInstance().getClass(subClass.ordinal()).getClientCode(), "</a><br>");
			MAIN_HTML = "<button value=\""+central.getClassName(player, subClass.ordinal())+"\" action=\"bypass -h ZeuSNPC subclass "+linkDeriva+" "+String.valueOf(subClass.ordinal())+" "+String.valueOf(index)+"\" width=150 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			SubClases.add(MAIN_HTML);
		}

		return SubClases;
	}

	public static Vector<String> getSubClasesList(L2PcInstance player){
		return getSubClasesList(player,true,"");
	}


	public static Vector<String> getSubClasesList(L2PcInstance player, boolean includeMain, String linkDeriva){

		if(linkDeriva.length()==0){
			linkDeriva = "acceptchangesub";
		}

		Vector<String> SubClases = new Vector<String>();
		String MAIN_HTML;
		if(includeMain){
			if (player.getClassId().getId() != player.getBaseClass())
			{
				MAIN_HTML = central.headFormat("Main","LEVEL") + central.LineaDivisora(2);
				MAIN_HTML += "<button value=\""+central.getClassName(player,player.getBaseClass())+"\" action=\"bypass -h ZeuSNPC subclass "+linkDeriva+" "+String.valueOf(player.getBaseClass())+" 0\" width=150 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
			}else{
				MAIN_HTML = central.headFormat("Main","LEVEL") + central.LineaDivisora(2);
				MAIN_HTML += central.LineaDivisora(1) + "<font color=DF7401>->" + central.getClassName(player,player.getBaseClass()) + "<-</font>" + central.LineaDivisora(1) + "<br1>";
			}
			SubClases.add(MAIN_HTML);
		}
		int i=1;
		for (Iterator<SubClass> subList = iterSubClasses(player); subList.hasNext();)
		{
			SubClass subClass = subList.next();
			if(subClass.getClassId() != player.getClassId().getId()){
				MAIN_HTML = central.headFormat("Sub-class "+String.valueOf(i),"LEVEL") + central.LineaDivisora(2);
				//MAIN_HTML += "<button value=\""+ClassListData.getInstance().getClass(subClass.getClassId()).getClientCode()+"\" action=\"bypass -h ZeuSNPC subclass "+linkDeriva+" "+String.valueOf(subClass.getClassId())+" "+String.valueOf(subClass.getClassIndex())+"\" width=150 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				MAIN_HTML += "<button value=\""+central.getClassName(player, subClass.getClassId())+"\" action=\"bypass -h ZeuSNPC subclass "+linkDeriva+" "+String.valueOf(subClass.getClassId())+" "+String.valueOf(subClass.getClassIndex())+"\" width=150 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			}else{
				MAIN_HTML = central.headFormat("Sub-class "+String.valueOf(i),"LEVEL") + central.LineaDivisora(2);
				MAIN_HTML += central.LineaDivisora(1) + "<font color=DF7401>->" + central.getClassName(player, subClass.getClassId()) + "<-</font>" + central.LineaDivisora(1) + "<br1>";
			}
			SubClases.add(MAIN_HTML);
			i++;
		}

		return SubClases;
	}

	private static final Iterator<SubClass> iterSubClasses(L2PcInstance player)
	{
		return player.getSubClasses().values().iterator();
	}

	public static String getVarcharacters(L2PcInstance st,String Buscar){
		String val = "";
		try{
			conn=ConnectionFactory.getInstance().getConnection();
			PreparedStatement act = conn.prepareStatement("SELECT * FROM characters WHERE charId="+String.valueOf(st.getObjectId()));
			ResultSet rs = act.executeQuery();
			if (rs!=null){
				rs.next();
				try{
					val = rs.getString(Buscar);
					conn.close();
				}catch (SQLException e) {
					val = "0";
				}
				try{
					conn.close();
				}catch (SQLException e) {
					return val;
				}
			}else{
				val = "0";
			}
		}catch(SQLException a){

		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
		return val;
	}

	public static Vector<String> getNewSubClases(L2PcInstance player, String linkDeriva, int index){
		Vector<String> SubClassAdd = new Vector<String>();

		if (player.getTotalSubClasses() >= Config.MAX_SUBCLASS)
		{
			central.msgbox("You can not add new subclasses", player);
			return SubClassAdd;
		}
		final NpcHtmlMessage html = new NpcHtmlMessage(Integer.valueOf(general.npcGlobal(player)));
		String MAIN_HTML = "";
		Set<PlayerClass> subsAvailable = getAvailableSubClasses(player);
		if ((subsAvailable != null) && !subsAvailable.isEmpty())
		{
			for (PlayerClass subClass : subsAvailable)
			{
				MAIN_HTML = "<button value=\""+central.getClassName(player, subClass.ordinal())+"\" action=\"bypass -h ZeuSNPC subclass "+linkDeriva+" "+String.valueOf(subClass.ordinal())+" "+String.valueOf(index)+"\" width=150 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				SubClassAdd.add(MAIN_HTML);
			}
		}
		else
		{
			if ((player.getRace() == Race.ELF) || (player.getRace() == Race.DARK_ELF))
			{
				central.msgbox("Elves and Dark Elves may not use each other's subclasses.", player);
			}
			else if (player.getRace() == Race.KAMAEL)
			{
				html.setFile(player.getHtmlPrefix(), "data/html/villagemaster/SubClass_Fail_Kamael.htm");
				player.sendPacket(html);
			}
			else
			{
				player.sendMessage("There are no sub classes available at this time.");
			}
		}
		return SubClassAdd;

	}

	protected static boolean checkVillageMasterPcRace(PlayerClass pclass)
	{
		return true;
	}

	protected static boolean checkVillageMasterTeachType(PlayerClass pclass)
	{
		return true;
	}

	public final static boolean checkVillageMaster(PlayerClass pclass)
	{
		if (Config.ALT_GAME_SUBCLASS_EVERYWHERE) {
			return true;
		}

		return checkVillageMasterPcRace(pclass) && checkVillageMasterTeachType(pclass);
	}

	private final static Set<PlayerClass> getAvailableSubClasses(L2PcInstance player)
	{
		// get player base class
		final int currentBaseId = player.getBaseClass();
		final ClassId baseCID = ClassId.values()[currentBaseId];

		// we need 2nd occupation ID
		final int baseClassId;
		if (baseCID.level() > 2) {
			baseClassId = baseCID.getParent().ordinal();
		} else {
			baseClassId = currentBaseId;
		}

		Set<PlayerClass> availSubs = PlayerClass.values()[baseClassId].getAvailableSubclasses(player);

		if ((availSubs != null) && !availSubs.isEmpty())
		{
			for (Iterator<PlayerClass> availSub = availSubs.iterator(); availSub.hasNext();)
			{
				PlayerClass pclass = availSub.next();

				// check for the village master
				if (!checkVillageMaster(pclass))
				{
					availSub.remove();
					continue;
				}

				// scan for already used subclasses
				int availClassId = pclass.ordinal();
				ClassId cid = ClassId.values()[availClassId];
				for (Iterator<SubClass> subList = iterSubClasses(player); subList.hasNext();)
				{
					SubClass prevSubClass = subList.next();
					ClassId subClassId = ClassId.values()[prevSubClass.getClassId()];

					if (subClassId.equalsOrChildOf(cid))
					{
						availSub.remove();
						break;
					}
				}
			}
		}

		return availSubs;
	}


}
