package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Config.general;


public class pvppk {
	public static String PKlistoption(L2PcInstance player, String eventParam1, String eventParam2, String eventParam3){
		String temp,temp1,temp2,temp3;
		int total_pkpvpkills = 0;
		temp = "pvpkills";
		temp1 = "pkkills";
		temp2= "FF0000";
		temp3= "FFFFFF";

		if(eventParam1.equals("2")){
			temp = "pkkills";
			temp1 = "pvpkills";
			temp2= "FFFFFF";
			temp3= "FF0000";
		}
		total_pkpvpkills = 0;
		String htmltext = "<html><head><title>" + general.TITULO_NPC() + "</title></head><body>" + central.LineaDivisora(2);
		htmltext += central.headFormat("Top PvP/PK") + central.LineaDivisora(2);
		htmltext += " <table width=280 bgcolor=AC2F10><tr><td width=40 align=\"center\"><font color =\"FFFFFF\">Pos.</td><td width=100 align=\"left\"><font color =\"FFFFFF\">Player Name</color></td><td width=30 align=\"left\"><a action=\"bypass -h ZeuSNPC PKlistoption 1 0 0\"><font color=\""+temp2+"\">PVPs</font></a></td><td width=30 align=\"left\"><a action=\"bypass -h ZeuSNPC PKlistoption 2 0 0\"><font color=\""+temp3+"\">PKs</font></a></td></tr></table>";
		htmltext += central.LineaDivisora(2);

		Connection con = null;
		PreparedStatement pks = null;
		ResultSet rs = null;
		int pos = 0,A=10,i=0,j=0;
		try{
			con = ConnectionFactory.getInstance().getConnection();
			pks = con.prepareStatement("SELECT char_name,pvpkills,pkkills FROM characters WHERE "+temp+">0 and accesslevel=0 ORDER BY "+temp+" DESC,"+temp1+" DESC,char_name");
			rs = pks.executeQuery();
			A=10; i=0; j=0;

			if(eventParam2.equals("adelante")){
				j = Integer.valueOf(eventParam3);
				j=j+A;
			}
			if(eventParam2.equals("atras")){
				j = Integer.valueOf(eventParam3);
				j=j-A;
			}
			try{
				while (rs.next()){
					i++;
					if(eventParam1.equals("1")){
						if((i > j) && (i <= (A+j))) {
							htmltext +="<table width=280 bgcolor=9A513F><tr><td width=40 align=\"center\"><font color =\"FFFFFF\">" + String.valueOf(i) + "</td><td width=100 align=\"left\"><font color =\"FFFFFF\">" + rs.getString("char_name") +"</td><td width=30 align=\"left\"><font color =\"FF0000\">" + rs.getString("pvpkills") + "</td><td width=30 align=\"left\"><font color =\"FFFFFF\">" + rs.getString("pkkills") + "</td></tr></table>" + central.LineaDivisora(2);
						}
					}
					if(eventParam1.equals("2")){
						if((i > j) && (i <= (A+j))) {
							htmltext += "<table width=280 bgcolor=9A513F><tr><td width=40 align=\"center\"><font color =\"FFFFFF\">" + String.valueOf(i) + "</td><td width=100 align=\"left\"><font color =\"FFFFFF\">" + rs.getString("char_name") +"</td><td width=30 align=\"left\"><font color =\"FFFFFF\">" + rs.getString("pvpkills") + "</td><td width=30 align=\"left\"><font color =\"FF0000\">" + rs.getString("pkkills") + "</td></tr></table>" + central.LineaDivisora(2);
						}
					}
				}
			}catch(SQLException e){
				try{
					con.close();
				}catch(SQLException b){

				}
			}
			con.close();
		}catch(SQLException a){

		}

		if((i > A) && (j < A)){
			htmltext += central.LineaDivisora(2)+"<center><tr><td><table width=120 border=0>";
			htmltext += "<tr><td align=\"center\"><button value=\"Next\" action=\"bypass -h ZeuSNPC PKlistoption "+eventParam1+" adelante "+String.valueOf(j)+"\" width=70 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
			htmltext += "</table></td></tr></center>";
		}else if((i > A) && (j >= A) && (j < (i-A))){
			htmltext += central.LineaDivisora(2)+"<center><tr><td><table width=160 border=0>";
			htmltext += "<tr><td align=\"center\"><button value=\"Back\" action=\"bypass -h ZeuSNPC PKlistoption "+eventParam1+" atras "+String.valueOf(j)+"\" width=70 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			htmltext += "<td align=\"center\"><button value=\"Next\" action=\"bypass -h ZeuSNPC PKlistoption "+eventParam1+" adelante "+String.valueOf(j)+"\" width=70 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
			htmltext += "</table></td></tr></center>";
		}else if((i > A) && (j >= (i-A))){
			htmltext += central.LineaDivisora(2)+"<center><tr><td><table width=120 border=0>";
			htmltext += "<tr><td align=\"center\"><button value=\"Back\" action=\"bypass -h ZeuSNPC PKlistoption "+eventParam1+" atras "+String.valueOf(j)+"\" width=70 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
			htmltext += "</table></td></tr></center>";
		}
		htmltext += central.BotonGOBACKZEUS() + "</body></html>";
		return htmltext;
	}

}
