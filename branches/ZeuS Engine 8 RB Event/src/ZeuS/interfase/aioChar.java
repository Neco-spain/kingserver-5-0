package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.skills.Skill;

import ZeuS.Config.general;
import ZeuS.Config.msg;



public class aioChar {

	static Connection conn =null;
	static CallableStatement psqry = null;
	static ResultSet rss = null;

	public static String explicaAIO(L2PcInstance st){
		String BOTON = "<button value=\"Transform into a AIO BUFFER\" action=\"bypass -h ZeuSNPC SETBufferDONA 0 0 0 \" width=180 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String BOTON_ATRAS = "<button value=\""+msg.BOTON_ATRAS+"\" action=\"bypass -h ZeuSNPC MenuDonation 0 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("What is a AIO BUFFER?") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.QUE_ES_AIO_BUFFER,"FF8000");
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.REQUERIMIENTOS_BUFFER,"FF8000");
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(BOTON + BOTON_ATRAS,"LEVEL") + central.LineaDivisora(2);
		MAIN_HTML += "</body></html>";
		return MAIN_HTML;
	}

	private static void addSkillAIO(L2PcInstance st, boolean Buff0){
		if(!general._activated()){
			return;
		}
		
		String TipoBuss = Buff0 ? "buffLevel" : "buffEnchant";
		
		String qry = "call sp_buffaio(1,'"+ st.getName() +"')";
		try{
			conn=ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			while (rss.next()){
				Skill newSkill = SkillData.getInstance().getSkill(rss.getInt("idBuff") ,rss.getInt(TipoBuss));
				st.addSkill(newSkill,true);
			}
		}catch(SQLException a){

		}
		try{
			conn.close();
		}catch(Exception a){
			
		}
	}

	public static boolean setNewAIO(L2PcInstance st,boolean setSkill_0){
		if(!general._activated()){
			return false;
		}
		if(!isNameOK(st)){
			central.msgbox(msg.EL_NOMBRE_YA_EXISTS_COMO_AIO, st);
			return false;
		}

		String ActualName = st.getName();
		if(ActualName.length()>14){
			central.msgbox(msg.EL_NOMBRE_ES_MUY_LARGO_PARA_EL_AIO_MAXIMOS_CARACTERES_$maximo.replace("$maximo", "14"), st);
			return false;
		}

		central.msgbox_Lado(msg.LA_CREACION_DEL_AIO_HA_COMENZADO, st);
		central.msgbox(msg.ESTE_PROCESO_PUEDE_DEMORAR_UNOS_SEGUNDOS,st);

		if(!st.isNoble()){
			st.setNoble(true);
			st.addExpAndSp(93836,0);
		}

		addSkillAIO(st,setSkill_0);

		st.setName( "[BUFF]"+st.getName() );

		st.broadcastUserInfo();

		central.msgbox_Lado(msg.AIO_CREADO_CON_EXITO,st);
		return true;

	}

	private static boolean isNameOK(L2PcInstance st){
		
		if(st.getName().startsWith("[BUFF]")){
			central.msgbox("You cant do this again", st);
			return false;
		}
		
		String qry = "call sp_buffaio(2,'"+ st.getName() +"')";
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			while (rss.next()){
				if(rss.getInt("A") == 0){
					try{
						conn.close();
					}catch(SQLException e){
						return false;
					}
					return true;
				}
				try{
					conn.close();
				}catch(SQLException s){

				}
			}
		}catch(SQLException e1){
			return false;
		}
		return false;
	}

}
