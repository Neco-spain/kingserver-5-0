package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.GameServer;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.model.zone.ZoneId;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.admin.menu;
import ZeuS.procedimientos.opera;

public class bufferChar {

	private static final Logger _log = Logger.getLogger(bufferChar.class.getName());

	private static int LimiteHoja = 12;
	private static HashMap<L2PcInstance,Integer> PAGINA_ACTUAL = new HashMap<L2PcInstance, Integer>();

	protected static HashMap<Integer,String> SELECNAME = new HashMap<Integer, String>();
	private static HashMap<L2PcInstance,HashMap<String,HashMap<Integer,HashMap<String,Integer>>>> PLAYER_BUFF = new HashMap<L2PcInstance, HashMap<String,HashMap<Integer,HashMap<String,Integer>>>>();
	private static HashMap<L2PcInstance, Boolean> EXTERNAL_GAMEPLAY = new HashMap<L2PcInstance, Boolean>();
	private static HashMap<L2PcInstance, Boolean> useInCHar = new HashMap<L2PcInstance, Boolean>();
	private static HashMap<L2PcInstance, Integer> PAG_ACTUAL_MODIF_SCHEME = new HashMap<L2PcInstance,Integer>();
	//private static HashMap<L2PcInstance,HashMap<String,HashMap<String,Integer>>> PLAYER_BUFF = new HashMap<L2PcInstance, HashMap<String,HashMap<String,Integer>>>();


	public static void setInEvent(L2PcInstance player,boolean status){
		EXTERNAL_GAMEPLAY.put(player, status);
	}

	private static boolean isOnEvent(L2PcInstance player){
		if(player.isOnEvent()){
			return true;
		}
		return false;
	}

	protected static void setBuffToSchemme(L2PcInstance player, int idBuff, String Cate,boolean deleteBuff){
		HashMap<String, String> InfoBuff = general.BUFF_CHAR_DATA.get(Cate).get(idBuff);
		int bufflvl = Integer.valueOf(InfoBuff.get("LEVEL"));
		setBuffToSchemme(player, idBuff, bufflvl,deleteBuff);
	}
	
	protected static void setBuffSchemmeToPlayer(L2PcInstance player){
		setBuffSchemmeToPlayer(player,false,false);
	}

	protected static void setBuffSchemmeToPlayer(L2PcInstance player,boolean fromCB, boolean forPet){

		if(!general.BUFFCHAR_FOR_FREE){
			if(!opera.haveItem(player, general.BUFFCHAR_COST_USE)){
				return;
			}
		}

		opera.removeItem(general.BUFFCHAR_COST_USE, player);

		if(getNameSchemeSelec(player).length()==0){
			return;
		}
		if(PLAYER_BUFF.get(player)==null){
			return;
		}
		if(PLAYER_BUFF.get(player).get(getNameSchemeSelec(player))==null){
			return;
		}
		HashMap<Integer, HashMap<String, Integer>> BuffDar = PLAYER_BUFF.get(player).get(getNameSchemeSelec(player));
		if(BuffDar==null){
			return;
		}

		Iterator itr = BuffDar.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	int idBuff = (int)Entrada.getKey();
	    	if(BuffDar.get(idBuff)!=null){
	    		HashMap<String, Integer> BuffIndo = BuffDar.get(idBuff);
		    	int BuffLevel = BuffIndo.get("LVLBUFF");
		    	if(fromCB){
		    		setBuff(player, idBuff, BuffLevel,forPet);
		    	}else{
		    		setBuff(player, idBuff, BuffLevel);
		    	}
	    	}
	    }
	}
	
	protected static String getCategoriaBY_id(int idCategoria){
		Iterator itr = general.BUFF_CHAR_DATA.get("CATE").entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	HashMap<String,String> datos = general.BUFF_CHAR_DATA.get("CATE").get((int)Entrada.getKey()) ;
	    	//tempTabla += "<tr><td width=200 align=CENTER>"+ (btnBuffSelecCate.replace("%NOMBRE%", datos.get("NOMCATE")).replace("%ID%", datos.get("IDCATE"))) +"</td></tr>";
	    	if(Integer.valueOf(datos.get("IDCATE")) == idCategoria){
	    		return datos.get("NOMCATE");
	    	}
	    }
	    return "";
	}
	
	private static void getAllBuffFromSchemeForEdit(L2PcInstance player){
		getAllBuffFromSchemeForEdit(player, false, 0, 0);
	}

	private static void getAllBuffFromSchemeForEdit(L2PcInstance player, boolean removeBuff, int idBuff, int lvlBuff){
		boolean isEmpty = true;
		
		int MaxBuffPorPagina = 10;
		int DesdeBuff = PAG_ACTUAL_MODIF_SCHEME.get(player) * MaxBuffPorPagina;
		int HastaBuff = DesdeBuff + MaxBuffPorPagina;
		
		HashMap<Integer, HashMap<String, Integer>> Buff_Char = null;
		try{
			String NameScheme = getNameSchemeSelec(player);
			if(PLAYER_BUFF.get(player)==null){
				return;
			}
			if(PLAYER_BUFF.get(player).get(getNameSchemeSelec(player))==null){
				return;
			}
			Buff_Char = PLAYER_BUFF.get(player).get(getNameSchemeSelec(player));
			
			if(Buff_Char.size()>0){
				isEmpty = false;
			}
			
		}catch(Exception a){
			
		}
		String Tabla = "<table width=270>";
		if(removeBuff){

		}
		
		String btnBackMain = "<button value=\"Back\" action=\"bypass -h voice .zeus_buffer\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		
		String btnPrevio = "<button value=\"<-\" action=\"bypass -h voice .zeus_buffer 0 17 0\" width=33 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnNext = "<button value=\"->\" action=\"bypass -h voice .zeus_buffer 0 16 0\" width=33 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";		
		
		
		String HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		
		
		String BtnAuxi = "<table width=270><tr><td width=90 align=CENTER>%ANTES%</td><td width=90 align=CENTER>"+btnBackMain+"</td><td width=90 align=CENTER>%DESPUES%</td></tr></table>";
		
		
		HTML += central.LineaDivisora(1)  + central.headFormat("Scheme Edit - Click to remove<br1>%TABLABOTON%" ) + central.LineaDivisora(1);
		
		if(isEmpty){
			HTML += central.LineaDivisora(3) + central.LineaDivisora(1) + central.headFormat("Empty Scheme","LEVEL") + central.LineaDivisora(1) + central.LineaDivisora(3);
			HTML = HTML.replace("%TABLABOTON%", "");
		}else{
			Iterator itr = Buff_Char.entrySet().iterator();
			
			/*if(Buff_Char.size() <= HastaBuff ){
				PAG_ACTUAL_MODIF_SCHEME.put(player, PAG_ACTUAL_MODIF_SCHEME.get(player) - 1);
				DesdeBuff = PAG_ACTUAL_MODIF_SCHEME.get(player) * MaxBuffPorPagina;
				HastaBuff = DesdeBuff + MaxBuffPorPagina;				
			}*/
			
			//BtnAuxi = "<table width=270><tr><td width=90>%ANTES%</td><td width=90>"+btnBackMain+"</td><td width=90>%DESPUES%</td></tr></table>";
			
			if(PAG_ACTUAL_MODIF_SCHEME.get(player)>0){
				BtnAuxi = BtnAuxi.replace("%ANTES%", btnPrevio);
			}else{
				BtnAuxi = BtnAuxi.replace("%ANTES%", "");
			}
			
			int contador =0;
			while(itr.hasNext()){
				Map.Entry Entrada = (Map.Entry)itr.next();
				if(contador >= DesdeBuff && contador < HastaBuff){
			    	int idBuffEntrada = (int)Entrada.getKey();
			    	int LevelEntrada = Buff_Char.get(idBuffEntrada).get("LVLBUFF");
			    	String NombreBuff = "<a action=\"bypass -h voice .zeus_buffer 0 15 "+ String.valueOf(idBuffEntrada + "-" + LevelEntrada) +"\">"+ opera.getSkillName(idBuffEntrada, LevelEntrada) +"</a><br>";
			    	String Icono = "<img src=\"Icon.skill"+ opera.getIconSkill(idBuffEntrada) +"\" width=32 height=32 >";
			    	Tabla += "<tr><td width=32>"+Icono+"</td><td width=238>"+ NombreBuff  +"</td></tr>";
				}
				contador++;
			}
			
			if(contador > HastaBuff){
				BtnAuxi = BtnAuxi.replace("%DESPUES%", btnNext);
			}else if(contador <= HastaBuff){
				BtnAuxi = BtnAuxi.replace("%DESPUES%", "");				
			}
			

			
			HTML = HTML.replace("%TABLABOTON%", BtnAuxi);
			
			Tabla += "</table>";			
			HTML += central.headFormat(Tabla);
		}
		HTML += central.getPieHTML(false)  + "<br1></body></html>";
		central.sendHtml(player, HTML);
	}
	
	private static void setBuffToSchemme(L2PcInstance player, int idBuff, int bufflvl, boolean deleteBuff){
		String tipoOpera = "4";
		if(deleteBuff){
			tipoOpera = "5";
		}

		if(general.BUFFCHAR_DONATION_SECCION_ACT){
			if(tipoOpera.equals("4")){
				if(buffIsDona(idBuff)){
					if(!opera.haveItem(player, general.BUFFCHAR_DONATION_SECCION_COST)){
						central.msgbox(msg.BUFFERCHAR_YOU_DONT_HAVE_DONATION_COIN, player);
						return;
					}
				}
			}
		}


		String qry = "CALL sp_buff_char_sch("+tipoOpera+"," + String.valueOf(player.getObjectId()) + ",'" + SELECNAME.get(player.getObjectId()) + "',"+ String.valueOf(idBuff) +","+ String.valueOf(bufflvl) +",'')";
		Connection conn = null;
		CallableStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			try{
				while (rss.next()){
					if(rss.getString(1).equals("cor")){
						
						if(PLAYER_BUFF==null){
							PLAYER_BUFF.put(player, new HashMap<String,HashMap<Integer,HashMap<String,Integer>>>());
						}
						
						if(!PLAYER_BUFF.containsKey(player)){
							PLAYER_BUFF.put(player, new HashMap<String,HashMap<Integer,HashMap<String,Integer>>>());
						}
						
						if(!PLAYER_BUFF.get(player).containsKey(SELECNAME.get(player.getObjectId()))){
							PLAYER_BUFF.get(player).put(SELECNAME.get(player.getObjectId()),new HashMap<Integer, HashMap<String,Integer>>());
						}
						
						
						if(!PLAYER_BUFF.get(player).get(SELECNAME.get(player.getObjectId())).containsKey(idBuff)){
							PLAYER_BUFF.get(player).get(SELECNAME.get(player.getObjectId())).put(idBuff,new HashMap<String, Integer>());
						}
						PLAYER_BUFF.get(player).get(SELECNAME.get(player.getObjectId())).get(idBuff).put("IDBUFF", idBuff);
						PLAYER_BUFF.get(player).get(SELECNAME.get(player.getObjectId())).get(idBuff).put("LVLBUFF", bufflvl);
					}else if(rss.getString(1).equals("cor1")){
						PLAYER_BUFF.get(player).get(SELECNAME.get(player.getObjectId())).remove(idBuff);
					}
				}
			}catch(SQLException e){
				conn.close();
			}
			conn.close();
		}catch(SQLException a){
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
	}

	private static void getBuffFromSchemme(L2PcInstance player,String nomSchemme){
		Connection conn = null;
		CallableStatement psqry = null;
		ResultSet rss = null;
		String qry = "CALL sp_buff_char_sch(3," + String.valueOf(player.getObjectId()) + ",'" + nomSchemme + "',0,0,'')";
		boolean Respuesta = false;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			try{
				while (rss.next()){
					if(!PLAYER_BUFF.containsKey(player)){
						PLAYER_BUFF.put(player, new HashMap<String,HashMap<Integer,HashMap<String,Integer>>>());
					}
					if(!PLAYER_BUFF.get(player).containsKey(nomSchemme)){
						PLAYER_BUFF.get(player).put(nomSchemme,new HashMap<Integer, HashMap<String,Integer>>());
					}
					if(!PLAYER_BUFF.get(player).get(nomSchemme).containsKey(rss.getInt(1))){
						PLAYER_BUFF.get(player).get(nomSchemme).put(rss.getInt(1),new HashMap<String,Integer>());
					}
					PLAYER_BUFF.get(player).get(nomSchemme).get(rss.getInt(1)).put("IDBUFF", rss.getInt(1));
					PLAYER_BUFF.get(player).get(nomSchemme).get(rss.getInt(1)).put("LVLBUFF", rss.getInt(2));
				}
			}catch(SQLException e){
				conn.close();
			}
			conn.close();
		}catch(SQLException a){
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}

	}

	public static void getSchemmeToChar(L2PcInstance player){
		if(PLAYER_BUFF.containsKey(player)){
			PLAYER_BUFF.get(player).clear();
		}else{
			PLAYER_BUFF.put(player,new HashMap<String,HashMap<Integer,HashMap<String,Integer>>>());
		}
		Connection conn = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			String qry = "SELECT id, NomSch, idChar FROM zeus_buff_char_sch WHERE idChar = " + player.getObjectId() ;
			PreparedStatement psqry = conn.prepareStatement(qry);
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					try{
						PLAYER_BUFF.get(player).put(rss.getString(2),new HashMap<Integer,HashMap<String,Integer>>());
						getBuffFromSchemme(player,rss.getString(2));
					}catch(SQLException e){

					}
				}
			conn.close();
			}catch(SQLException e){

			}
		try{
			conn.close();
		}catch (Exception e) {

		}
	}

	private static int lenghMax = 14;

	protected static boolean saveNameSchem(L2PcInstance player, String NomIngresado){
		if(NomIngresado.length()==0){
			return false;
		}

		if(NomIngresado.length()>lenghMax){
			central.msgbox(msg.BUFFERCHAR_LENGTH_OF_THE_NAME_MUST_BE_LESS_THAN_$size.replace("$size", String.valueOf(lenghMax)) , player);
			return false;
		}



		Connection conn = null;
		CallableStatement psqry = null;
		ResultSet rss = null;
		String qry = "CALL sp_buff_char_sch(2," + String.valueOf(player.getObjectId()) + ",'" + NomIngresado + "',0,0,'')";
		boolean Respuesta = false;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			try{
				while (rss.next()){
					if(rss.getString(1).equals("err1")){
						central.msgbox(msg.BUFFERCHAR_NAME_ALREADY_EXISTS_IN_YOU_SCHEMES,player);
						Respuesta = false;
					}else if(rss.getString(1).equals("cor")){
						central.msgbox(msg.BUFFERCHAR_SCHEME_NAME_SAVED,player);
						SELECNAME.put(player.getObjectId(), NomIngresado);
						Respuesta = true;
					}
				}
			}catch(SQLException e){
				conn.close();
				return false;
			}
			conn.close();
		}catch(SQLException a){
			try {
				conn.close();
			} catch (SQLException e) {
			}
			return false;
		}

		return Respuesta;
	}

	protected static boolean buffIsDona(int SkillID){

		if(!general.BUFFCHAR_DONATION_SECCION_ACT){
			return false;
		}

		//HashMap<Integer, HashMap<String, String>> BuffCategoria = general.BUFF_CHAR_DATA.get(Categoria);
		Iterator itr = general.BUFF_CHAR_DATA.entrySet().iterator();
		boolean retorno = false, salto = false;;
		while(itr.hasNext() && !salto){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	String NomCategoria = (String) Entrada.getKey();
	    	if(general.BUFFCHAR_DONATION_SECCION.equals(NomCategoria)){
	    		if(general.BUFF_CHAR_DATA.get(NomCategoria).containsKey(SkillID)){
	    			retorno = true;
	    			salto = true;
	    		}
	    	}
		}
		return retorno;
	}

	public static void setBuff(L2PcInstance cha, int idSkill, int skillLevel){
		if(!general.BUFFCHAR_PET){
			if(!useInCHar.get(cha)){
				central.msgbox(msg.BUFFERCHAR_YOU_CAN_NOT_BUFF_YOU_PET, cha);
				useInCHar.put(cha, true);
				return;
			}
		}
		setBuff(cha,idSkill,skillLevel,!useInCHar.get(cha));
	}

	public static void setBuff(L2PcInstance cha, int idSkill, int skillLevel,boolean forPet){
		if(!forPet){
			SkillData.getInstance().getSkill(idSkill,skillLevel).applyEffects(cha,cha);
		}else{
			if(cha.getSummon() != null){
				SkillData.getInstance().getSkill(Integer.valueOf(idSkill),skillLevel).applyEffects(cha.getSummon(),cha.getSummon());
			}
		}
	}

	private static void showMainAdminWindows(L2PcInstance cha){
		showMainAdminWindows(cha,false);
	}



	private static void showMainAdminWindows(L2PcInstance cha, boolean MainBackMenu){
		PAGINA_ACTUAL.put(cha, 1);
		
		
		String HTML_MAIN = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		String btnBuffSelecCate = "<button value=\"%NOMBRE%\" action=\"bypass -h admin_zeus_buff_voice 2 %NOMBRE% 0 0 0\" width=140 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnSupr = "<button value=\"Supr\" action=\"bypass -h admin_zeus_buff_voice 15 %NOMBRE% 0 0 0\" width=40 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnModificar = "<button value=\"Edit\" action=\"bypass -h admin_zeus_buff_voice 16 %NOMBRE% 0 0 0\" width=40 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";

		HTML_MAIN += central.LineaDivisora(1) + central.headFormat("ZeuS Voice Buff Config") + central.LineaDivisora(1);

		String txtNom = "<edit var=\"txtCatName\" width=150>";
		String btnGuardar = "<button value=\"Save\" action=\"bypass -h admin_zeus_buff_voice 6 $txtCatName 0 0 0\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";

		String MAIN_NEW_CAT = "<table width=260><tr>" +
				"<td width=180 align=center>"+ txtNom +"</td>" +
				"<td width=80 align=center>"+ btnGuardar +"</td>" +
				"</tr></table><br>";

		Iterator itr = general.BUFF_CHAR_DATA.get("CATE").entrySet().iterator();
		String tempTabla = "",tablaBotones="";
		tablaBotones = "<center><table width=270 align=CENTER>%DATA%</table></center>";
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	HashMap<String,String> datos = general.BUFF_CHAR_DATA.get("CATE").get((int)Entrada.getKey()) ;
	    	tempTabla += "<tr><td width=170 align=CENTER>"+ (btnBuffSelecCate.replace("%NOMBRE%", datos.get("NOMCATE"))) +"</td>" +
	    			"<td width=50>"+ btnSupr.replace("%NOMBRE%", datos.get("NOMCATE")) +"</td>" +
	    			"<td width=50>"+ btnModificar.replace("%NOMBRE%", datos.get("NOMCATE")) +"</td>"+
	    			"</tr>";
	    }

	    tablaBotones = tablaBotones.replace("%DATA%", tempTabla);

	    HTML_MAIN += central.LineaDivisora(1) + central.headFormat("New Categories <br1>" + MAIN_NEW_CAT,"LEVEL") + central.LineaDivisora(1);

	    HTML_MAIN += central.LineaDivisora(1) + central.headFormat(tablaBotones) + central.LineaDivisora(1);

	    if(MainBackMenu){
	    	HTML_MAIN+= central.LineaDivisora(1) + central.headFormat(menu.getBtnbackConfig()) + central.LineaDivisora(1);
	    }
		//:
		HTML_MAIN += central.getPieHTML(false) + "</body></html>";
		opera.enviarHTML(cha, HTML_MAIN);
	}

	private static void showBuffAdminWindows(L2PcInstance player, String Categoria){
		String HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		HTML += central.LineaDivisora(1) + central.headFormat("ZeuS Voice Buff Config") + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat("Cetegory: " + Categoria);

		String LISTACOMBO = Categoria ;
		Iterator itr = general.BUFF_CHAR_DATA.get("CATE").entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	HashMap<String,String> datos = general.BUFF_CHAR_DATA.get("CATE").get((int)Entrada.getKey()) ;
	    	if(!datos.get("NOMCATE").equals(Categoria)){
	    		LISTACOMBO += ";" + datos.get("NOMCATE");
	    	}
	    }
		String cmbSeleccion = "<combobox width=120 var=cmbCate%IDBUFF% list="+ LISTACOMBO + " >";

		String btnChange = "<button value=\"Save\" action=\"bypass -h admin_zeus_buff_voice 1 $cmbCate%IDBUFF% %IDBUFF% "+ Categoria +"\" width=33 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnChangeStatus = "<button value=\"%ESTADO%\" action=\"bypass -h admin_zeus_buff_voice 3 %IDBUFF% "+ Categoria +"\" width=33 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";

		String Tabla = "<table width=270><tr>%DATOS%</tr></table>";
		String TablaInterna = "<table width=230><tr><td width=120>"+cmbSeleccion+"</td><td width=55>"+btnChange+"</td><td width=55>"+btnChangeStatus+"</td></tr></table>";
		String Fila = "<tr><td width=32>%LOGO%</td><td width=238>%NOMBRE%<br1>"+ TablaInterna +"<br></td></tr>";


		int paginaActual = PAGINA_ACTUAL.get(player);
		HashMap<Integer, HashMap<String, String>> BuffCategoria = general.BUFF_CHAR_DATA.get(Categoria);

		if(BuffCategoria==null){
			showMainAdminWindows(player);
			return;
		}else if(BuffCategoria.size()==0){
			showMainAdminWindows(player);
			return;
		}


		itr=null;
		itr = BuffCategoria.entrySet().iterator();
		String ParaBuff = "";
		int Contador = 0;
		int Hasta = paginaActual * LimiteHoja;
		int Desde = (paginaActual -1) *  LimiteHoja;
		boolean haveNext = false;
		String EstatusSTR = "";
		while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	if(Contador >= Desde){
		    	if(Contador < Hasta){
			    	String idBuffSearch = BuffCategoria.get(Entrada.getKey()).get("ID");
			    	String Icono = "<img src=\"Icon.skill"+ opera.getIconSkill(Integer.valueOf(idBuffSearch)) +"\" width=32 height=32 >";
			    	boolean buffAct = BuffCategoria.get(Entrada.getKey()).get("ACT").equals("1");
			    	String NomBuff = BuffCategoria.get(Entrada.getKey()).get("NOM");
			    	String Color = "";

			    	if(buffAct){
			    		Color = "01DF01";
			    	}else{
			    		Color = "424242";
			    	}
			    	if(buffAct){
			    		EstatusSTR="DESC";
			    	}else{
			    		EstatusSTR="ACT";
			    	}
			    	ParaBuff += Fila.replace("%LOGO%", Icono).replace("%NOMBRE%", "<font color="+ Color +">" + NomBuff + "</font>").replace("%IDBUFF%", idBuffSearch).replace("%ESTADO%", EstatusSTR);
		    	}else{
		    		haveNext=true;
		    	}
	    	}
	    	Contador++;
	    }

		String btnBackMain = "<button value=\"Back\" action=\"bypass -h admin_zeus_buff_voice\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnPrevio = "<button value=\"<-\" action=\"bypass -h admin_zeus_buff_voice 5 "+ Categoria +"\" width=33 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnNext = "<button value=\"->\" action=\"bypass -h admin_zeus_buff_voice 4 "+ Categoria +"\" width=33 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";

		String TablaBotones ="<table width=116><tr>" +
				"<td width=33>"+ (paginaActual>1 ? btnPrevio : "" )+"</td>" +
				"<td width=50>"+ btnBackMain +"</td>" +
				"<td width=33>"+ (  haveNext ? btnNext : "" ) +"</td>" +
				"</tr></table>";

		HTML += central.LineaDivisora(1) + central.headFormat(TablaBotones);
		HTML += central.headFormat("<table><tr><td width=120>Buff Name</td><td width=55></td><td width=55>Status</td></tr></table>") + central.LineaDivisora(1);

		HTML += central.LineaDivisora(1) + central.headFormat(Tabla.replace("%DATOS%", ParaBuff)) + central.LineaDivisora(1);
		HTML += central.getPieHTML(false) + "</body></html>";
		opera.enviarHTML(player, HTML);

	}

	private static void cambiarCategoria(int idBuff,String NuevaCate){
		String qry = "call sp_buff_char_sch(7,0,'"+ NuevaCate +"',"+ String.valueOf(idBuff) +",0,'')";
		Connection conn = null;
		CallableStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			rss.next();
		}catch(SQLException e){
		}
		try {
			conn.close();
		} catch (SQLException e) {
		}
		general.getInstance().loadBuffData();
	}

	private static void setNewCate(String nomCate){
		String qry = "call sp_buff_char_sch(9,0,'"+ nomCate +"',0,0,'')";
		Connection conn = null;
		CallableStatement psqry = null;
		ResultSet rss = null;
		boolean cambiar = false;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			rss.next();
			if(rss.getString(1).equals("cor")){
				cambiar = true;
			}
		}catch(SQLException e){
		}
		try {
			conn.close();
		} catch (SQLException e) {
		}
		if(cambiar){
			general.loadBuffData();
		}
	}

	public static void CambiarEstadoBuff(int idBuff, String Categoria){
		String qry = "call sp_buff_char_sch(8,0,'"+ Categoria +"',"+ String.valueOf(idBuff) +",0,'')";
		Connection conn = null;
		CallableStatement psqry = null;
		ResultSet rss = null;
		boolean cambiar = false;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			rss.next();
			if(rss.getString(1).equals("cor")){
				cambiar = true;
			}
		}catch(SQLException e){
		}
		try {
			conn.close();
		} catch (SQLException e) {
		}
		String estadoBuff = general.BUFF_CHAR_DATA.get(Categoria).get(idBuff).get("ACT");
		if(cambiar){
			general.BUFF_CHAR_DATA.get(Categoria).get(idBuff).put("ACT",( estadoBuff.equalsIgnoreCase("1") ? "0" : "1"));
		}
	}

	private static void setSuprCategoria(String nomCate, L2PcInstance player){
		String qry = "call sp_buff_char_sch(10,0,'"+ nomCate +"',0,0,'')";
		Connection conn = null;
		CallableStatement psqry = null;
		ResultSet rss = null;
		boolean cambiar = false;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			rss.next();
			if(rss.getString(1).equals("cor")){
				cambiar = true;
			}else if(rss.getString(1).equals("err1")){
				central.msgbox("Must remove/change all buff of this category before removing the category", player);
			}else if(rss.getString(1).equals("err")){
				central.msgbox("Category not exist", player);
			}
		}catch(SQLException e){
		}
		try {
			conn.close();
		} catch (SQLException e) {
		}
		if(cambiar){
			general.loadBuffData();
		}
	}


	private static boolean setUpdateCategoria(String nomCate, L2PcInstance player, String newNomCate){
		String qry = "call sp_buff_char_sch(11,0,'"+ nomCate +"',0,0,'"+ newNomCate +"')";
		Connection conn = null;
		CallableStatement psqry = null;
		ResultSet rss = null;
		boolean cambiar = false;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			rss.next();
			if(rss.getString(1).equals("cor")){
				cambiar = true;
			}else if(rss.getString(1).equals("err1")){
				central.msgbox("Category not exists", player);
			}else if(rss.getString(1).equals("err")){
				central.msgbox("Category Name already exists", player);
			}
		}catch(SQLException e){
		}
		try {
			conn.close();
		} catch (SQLException e) {
		}
		if(cambiar){
			general.loadBuffData();
		}
		return cambiar;
	}

	public static void adminDelegar(L2PcInstance player, String command){

		if(command.equals("admin_zeus_buff_voice")){
			showMainAdminWindows(player);
			return;
		}

		String Comandos[] = command.split(" ");
		//0 %NOMBRE% 0 0 0
		switch (Comandos[1]){
			case "0":
				if(Comandos[2].equals("1")){
					showMainAdminWindows(player,true);
				}else{
					showMainAdminWindows(player);
				}
				break;
			case "1"://Cambiar de Categoria
				int idBuff = Integer.valueOf(Comandos[3]);
				cambiarCategoria(idBuff,Comandos[2]);
				showBuffAdminWindows(player,Comandos[4]);
				break;
			case "2"://Cambiar Estado del Buff
				showBuffAdminWindows(player,Comandos[2]);
				break;
			case "3":
				int idBuff2 = Integer.valueOf(Comandos[2]);
				CambiarEstadoBuff(idBuff2,Comandos[3]);
				showBuffAdminWindows(player,Comandos[3]);
				break;
				//:
			case "4":
				PAGINA_ACTUAL.put(player, PAGINA_ACTUAL.get(player)+1);
				showBuffAdminWindows(player,Comandos[2]);
				break;
			case "5":
				PAGINA_ACTUAL.put(player, PAGINA_ACTUAL.get(player)-1);
				showBuffAdminWindows(player,Comandos[2]);
				break;
			case "6":
				if((Comandos.length>6) || (Comandos.length<6)){
					central.msgbox("Worg category Name", player);
					showMainAdminWindows(player);
					return;
				}

				setNewCate(Comandos[2]);
				//admin_zeus_buff_voice 6 $txtCatName 0 0 0
				showMainAdminWindows(player);
				break;
			case "15":
				setSuprCategoria(Comandos[2],player);
				showMainAdminWindows(player);
				break;
			case "16":
				showAdminUpdateNameCate(player, Comandos[2]);
				break;
			case "17":
				if(setUpdateCategoria(Comandos[2] , player, Comandos[3])){
					showMainAdminWindows(player);
				}else{
					showAdminUpdateNameCate(player, Comandos[2]);
				}
				break;

		}
	}

	public static void showAdminUpdateNameCate(L2PcInstance player, String Category){
		String HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		HTML += central.LineaDivisora(1) + central.headFormat("ZeuS Voice Buff Config") + central.LineaDivisora(1);
		HTML += central.LineaDivisora(2) + central.LineaDivisora(1) + central.headFormat("Change name for Category: <br1><font color=DF7401>"+ Category +"</font>","LEVEL") + central.LineaDivisora(1) + central.LineaDivisora(2);

		//String txtNom = "<edit var=\"nomBuff\" width=150>";
		//String btnSave = "<button value=\"Save\" action=\"bypass -h voice .zeus_buffer 93 "+ String.valueOf(idBuff) +" "+ Cate +"\" width=33 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";

		String txtNombre = "<edit var=\"txtNomSch\" width=100>";
		String btnSaveName = "<button value=\"Save\" action=\"bypass -h admin_zeus_buff_voice 17 "+ Category +" $txtNomSch 0 0\" width=33 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";

		HTML += central.LineaDivisora(1) + central.headFormat("Enter new Category Name<br1>" + txtNombre+"<br1>" + btnSaveName + "<br>") + central.LineaDivisora(1);

		HTML += central.getPieHTML(false) + "</body></html>";
		opera.enviarHTML(player, HTML);
	}

	public static void delegar(L2PcInstance player, String command, String params){
		if(player.isInOlympiadMode()){
			return;
		}
		if(player.isOlympiadStart()){
			return;
		}		

		if(!general.BUFFCHAR_ACT){
			central.msgbox("ZeuS Buffer Voice desactivated by Admin/GM", player);
			return;
		}

		if(player.isDead()){
			return;
		}

		if(!general.BUFFCHAR_CAN_USE_FLAG && (player.getPvpFlag() != 0)){
			central.msgbox(msg.NO_PUEDES_USAR_MIS_SERVICIOS_ESTANDO_FLAG, player);
			return;
		}
		if(!general.BUFFCHAR_CAN_USE_PK && (player.getKarma()>0)){
			central.msgbox(msg.NO_PUEDES_USAR_MIS_SERVICIOS_ESTANDO_PK, player);
			return;
		}

		if(!general.BUFFCHAR_CAN_USE_COMBAT_MODE && player.isInCombat()){
			central.msgbox(msg.BUFFVOICE_YOU_CANNOT_USE_ME_IN_COMBAT_MODE, player);
			return;
		}

		int siegeEstate = player.getSiegeState();

		if(!general.BUFFCHAR_CAN_USE_SIEGE_ZONE && (siegeEstate>0)){
			if(!player.isInsideZone(ZoneId.PEACE)){
				central.msgbox(msg.BUFFVOICE_YOU_CANNOT_USE_ME_IN_SIEGE_AND_TW, player);
				return;
			}
		}

		if((params==null) && command.equals("zeus_buffer")){
			showWindows(player);
		}else{
			String []splitWin = params.split(" ");
			if(splitWin[0].equals("91")){
				//91 %IDBUFF% "+ BuffSelecc
				String idBuff = splitWin[1];
				String Categoria = splitWin[2];
				showInfoBuff(player,Categoria,Integer.valueOf(idBuff));
			}else if(splitWin[0].equals("92")){
				String Categoria = splitWin[2];
				int idBuff = Integer.valueOf(splitWin[1]);
				int levelBuff = Integer.valueOf(general.BUFF_CHAR_DATA.get(Categoria).get(idBuff).get("LEVEL"));
				if(general.BUFFCHAR_DONATION_SECCION_ACT && buffIsDona(idBuff)){
					if(!opera.haveItem(player, general.BUFFCHAR_DONATION_SECCION_COST)){
						central.msgbox(msg.BUFFVOICE_YOU_DONT_HAVE_THE_DONATION_ITEM_TO_USE_THIS_BUFF, player);
					}else{
						setBuff(player, idBuff, levelBuff);
						if(general.BUFFCHAR_DONATION_SECCION_REMOVE_ITEM){
							opera.removeItem(general.BUFFCHAR_DONATION_SECCION_COST, player);
						}
					}
				}else{
					boolean buffear = true;
					if(!general.BUFFCHAR_INDIVIDUAL_FOR_FREE){
						if(!opera.haveItem(player, general.BUFFCHAR_COST_INDIVIDUAL)){
							buffear = false;
						}else{
							opera.removeItem(general.BUFFCHAR_COST_INDIVIDUAL, player);
						}
					}
					if(buffear){
						idBuff = Integer.valueOf(splitWin[1]);
						setBuff(player, idBuff, levelBuff);
					}
				}
				showWindowsSetBuff(player,Categoria);
			}else if(splitWin[0].equals("93")){
				//93 %IDBUFF% "+ BuffSelecc
				setBuffToSchemme(player,Integer.valueOf(splitWin[1]),splitWin[2],false);
				showWindowsSetBuff(player,splitWin[2]);
			}else if(splitWin[0].equals("94")){
				setBuffToSchemme(player,Integer.valueOf(splitWin[1]),splitWin[2],true);
				showWindowsSetBuff(player,splitWin[2]);
			}else if(splitWin[0].equals("60")){
				PAGINA_ACTUAL.put(player, PAGINA_ACTUAL.get(player) - 1);
				showWindowsSetBuff(player,splitWin[1]);
			}else if(splitWin[0].equals("61")){
				PAGINA_ACTUAL.put(player, PAGINA_ACTUAL.get(player) + 1);
				showWindowsSetBuff(player,splitWin[1]);
			}else if(splitWin[1].equals("0")){
				SELECNAME.put(player.getObjectId(), splitWin[0]);
				showWindows(player,SELECNAME.get(player.getObjectId()));
				return;
			}else if(splitWin[1].equals("2")){
				showWindowsSetBuff(player,splitWin[0]);
				return;
			}else if(splitWin[1].equals("5")){
				if((splitWin.length>3) || (splitWin.length<3)){
					showWindows(player,SELECNAME.get(player.getObjectId()));
					central.msgbox(msg.BUFFERCHAR_WROG_SCHEME_NAME, player);
					return;
				}
				if(saveNameSchem(player, splitWin[0])){
					general.getInstance().loadCharBuffer(player);
					SELECNAME.put(player.getObjectId(), splitWin[0]);
					PLAYER_BUFF.get(player).get(SELECNAME.get(player.getObjectId()));
					if(PLAYER_BUFF !=null){
						if(!PLAYER_BUFF.containsKey(player)){
							PLAYER_BUFF.put(player, new HashMap<String, HashMap<Integer,HashMap<String,Integer>>>());
						}
						if(PLAYER_BUFF.get(player).containsKey(SELECNAME.get(player.getObjectId()))){
							PLAYER_BUFF.get(player).remove(SELECNAME.get(player.getObjectId()));
						}
						PLAYER_BUFF.get(player).put(SELECNAME.get(player.getObjectId()),new HashMap<Integer, HashMap<String,Integer>>());
					}
				}
				showWindows(player,SELECNAME.get(player.getObjectId()));
			}else if(splitWin[1].equals("6")) {
				showWindows(player,true);
			}else if(splitWin[1].equals("10")){
				if(!general.BUFFCHAR_PET){
					if(!useInCHar.get(player)){
						central.msgbox(msg.BUFFERCHAR_YOU_CAN_NOT_BUFF_YOU_PET, player);
						useInCHar.put(player, true);
						showWindows(player,getNameSchemeSelec(player));
						return;
					}
				}
				setBuffSchemmeToPlayer(player);
				showWindows(player,getNameSchemeSelec(player));
			}else if(splitWin[1].equals("11")){
				deleteSche(player);
				SELECNAME.remove(player.getObjectId());
				showWindows(player);
			}else if(splitWin[1].equals("12")){
				boolean curar = true;
				if(!general.BUFFCHAR_HEAL_FOR_FREE){
					if(!opera.haveItem(player, general.BUFFCHAR_COST_HEAL)){
						curar = false;
					}else{
						opera.removeItem(general.BUFFCHAR_COST_HEAL, player);
					}
				}
				if(curar){
					if(useInCHar.get(player)){
						if(player.isInCombat()){
							central.msgbox("You are in combat mode.", player);
						}else{
							central.healAll(player, false);
						}
					}else{
						if(player.getSummon()!=null){
							if(player.isInCombat() || player.getSummon().isInCombat()){
								central.msgbox("You are in combat mode.", player);
							}else{
								central.healAll(player, true);
							}
						}else{
							central.msgbox(msg.BUFFERCHAR_YOU_CAN_NOT_HEAL_YOU_PET, player);
							useInCHar.put(player, true);
						}
					}
				}
				showWindows(player, getNameSchemeSelec(player));
			}else if(splitWin[1].equals("13")){
				boolean cancelar = true;
				if(!general.BUFFCHAR_CANCEL_FOR_FREE){
					if(!opera.haveItem(player, general.BUFFCHAR_COST_CANCEL)){
						cancelar = false;
					}else{
						opera.removeItem(general.BUFFCHAR_COST_CANCEL, player);
					}
				}
				if(cancelar){
					if(useInCHar.get(player)){
						player.stopAllEffects();
					}else{
						if(player.getSummon()!=null){
							player.getSummon().stopAllEffects();
						}else{
							central.msgbox(msg.BUFFERCHAR_YOU_CAN_NOT_CANCEL_YOU_PET_BUFF, player);
							useInCHar.put(player, true);
						}
					}
				}
				showWindows(player, getNameSchemeSelec(player));
			}else if(splitWin[1].equals("14")){
				PAG_ACTUAL_MODIF_SCHEME.put(player, 0);
				getAllBuffFromSchemeForEdit(player);
			}else if(splitWin[1].equals("15")){
				String[] InfoSkillDelete = splitWin[2].split("-");
				setBuffToSchemme(player,Integer.valueOf(InfoSkillDelete[0]),Integer.valueOf(InfoSkillDelete[1]),true);
				getAllBuffFromSchemeForEdit(player);
			}else if(splitWin[1].equals("16")){
				PAG_ACTUAL_MODIF_SCHEME.put(player, PAG_ACTUAL_MODIF_SCHEME.get(player) + 1);
				getAllBuffFromSchemeForEdit(player);
			}else if(splitWin[1].equals("17")){
				PAG_ACTUAL_MODIF_SCHEME.put(player, PAG_ACTUAL_MODIF_SCHEME.get(player) - 1);
				getAllBuffFromSchemeForEdit(player);				
			}else if(splitWin[1].equals("80")){
				if(useInCHar.get(player)){
					if(player.getSummon() != null){
						useInCHar.put(player, false);
					}else{
						central.msgbox(msg.BUFFERCHAR_YOU_NOT_HAVE_PET, player);
					}
				}else{
					useInCHar.put(player, true);
				}
				showWindows(player);
			}else{
				showWindows(player,splitWin[0],params,false);
			}
		}
	}


	protected static void deleteSche(L2PcInstance player){
		//IN tipo SMALLINT, IN idCharIN INTEGER, IN nomSch VARCHAR(16), IN idBuffIN INTEGER, IN idLevelIN INTEGER
		String qry = "call sp_buff_char_sch(6,"+ player.getObjectId() +",'"+ getNameSchemeSelec(player) + "',1,1,'')";
		Connection conn = null;
		CallableStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			rss.next();
		}catch(SQLException e){
		}
		try {
			conn.close();
		} catch (SQLException e) {
		}
		PLAYER_BUFF.get(player).remove(getNameSchemeSelec(player));
		general.deleteSchemme(player, getNameSchemeSelec(player));
	}


	protected static String getNameSchemeSelec(L2PcInstance player){
		if(SELECNAME==null){
			return "";
		}
		if(!SELECNAME.containsKey(player.getObjectId())){
			return "";
		}

		return SELECNAME.get(player.getObjectId());
	}

	private static void showWindows(L2PcInstance player,boolean showNewSchemme){
		showWindows(player, "","", showNewSchemme);
	}

	private static void showWindows(L2PcInstance player){
		showWindows(player, "", "",false);
	}

	private static void showWindows(L2PcInstance player, String NomSchSelec){
		showWindows(player, NomSchSelec, "",false);
	}

	@SuppressWarnings("unused")

	private static void showInfoBuff(L2PcInstance player, String Cate, int idBuff){

		String btnDescrip = "<button value=\"Desc\" action=\"bypass -h voice .zeus_buffer 91 "+ String.valueOf(idBuff) +" "+ Cate +"\" width=33 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnSave = "<button value=\"Save\" action=\"bypass -h voice .zeus_buffer 93 "+ String.valueOf(idBuff) +" "+ Cate +"\" width=33 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnUse = "<button value=\"Use\" action=\"bypass -h voice .zeus_buffer 92 "+ String.valueOf(idBuff) +" "+ Cate +"\" width=33 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";

		String tablaBotones = "<center><table width=100><tr><td width = 32>"+ btnUse +"</td><td width = 32>"+ ( canSaveBuff(player, idBuff) ? btnSave : "" ) +"</td><td width = 32></td></tr></table></center>";

		//"<button value=\"%NOMBRE%\" action=\"bypass -h voice .zeus_buffer %NOMBRE% 2 0\" width=140 height=30 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnAtras = "<button value=\"Back\" action=\"bypass -h voice .zeus_buffer "+ Cate +" 2 0\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";;
		String HTML_MAIN = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		HTML_MAIN += central.LineaDivisora(1) + central.headFormat("Zeus Buffer - Buff Info") + central.LineaDivisora(1);
		String NomBuff = general.BUFF_CHAR_DATA.get(Cate).get(idBuff).get("NOM");
		String InfoBuff = general.BUFF_CHAR_DATA.get(Cate).get(idBuff).get("DESCR");
		HTML_MAIN += central.LineaDivisora(1) + central.headFormat(NomBuff,"LEVEL") + central.LineaDivisora(1);

		String Descrip = "<table width=270 align=CENTER><tr><td width=270 fixwidth=200 align=CENTER>"+ InfoBuff +"<br></td></tr></table>";

		HTML_MAIN += central.LineaDivisora(1) + central.headFormat(tablaBotones) + central.LineaDivisora(1);

		HTML_MAIN += central.headFormat(Descrip) + central.LineaDivisora(1) + central.headFormat(btnAtras) + central.LineaDivisora(1);
		HTML_MAIN += central.getPieHTML(false) + "</body></html>";
		opera.enviarHTML(player, HTML_MAIN);
	}


	protected static int getBuffCountFromSchemme(L2PcInstance player){
		return getBuffCountFromSchemme(player, false);
	}

	protected static boolean isBuff(int idSkill, int idLevel){
		Skill skill = SkillData.getInstance().getSkill(Integer.valueOf(idSkill),Integer.valueOf(idLevel));
		return !skill.isDance();
	}

	protected static int getBuffCountFromSchemme(L2PcInstance player, boolean checkDance){
		//L2Skill skill = SkillData.getInstance().getSkill(Integer.valueOf(idSkill),Integer.valueOf(livelloSkill));
		int NumBuffInSchemme = 0;
		if(getNameSchemeSelec(player).length()!=0){
			if(PLAYER_BUFF.containsKey(player)){
				if(PLAYER_BUFF.get(player).containsKey(getNameSchemeSelec(player))){
					if(PLAYER_BUFF.get(player).get(getNameSchemeSelec(player))!=null ){
						HashMap<Integer, HashMap<String, Integer>> BuffDar = PLAYER_BUFF.get(player).get(getNameSchemeSelec(player));
						if(BuffDar==null){
							return 0;
						}
						Iterator itr = BuffDar.entrySet().iterator();
					    while(itr.hasNext()){
					    	Map.Entry Entrada = (Map.Entry)itr.next();
					    	int idBuff = (int)Entrada.getKey();
					    	if(BuffDar.get(idBuff)!=null){
					    		HashMap<String, Integer> BuffIndo = BuffDar.get(idBuff);
						    	int BuffLevel = BuffIndo.get("LVLBUFF");
						    	if(!checkDance){
						    		if(isBuff(idBuff, BuffLevel)){
						    			NumBuffInSchemme++;
						    		}
						    	}else{
						    		if(!isBuff(idBuff, BuffLevel)){
						    			NumBuffInSchemme++;
						    		}
						    	}
					    	}
					    }
//						NumBuffInSchemme = PLAYER_BUFF.get(player).get(getNameSchemeSelec(player)).size();
					}
				}
			}
		}

		return NumBuffInSchemme;
	}

	protected static boolean canDeleteBuff(L2PcInstance player, int idBuffSearch){
    	boolean PassSave = false;
    	if(SELECNAME.get(player.getObjectId())!=null){
	    	if(PLAYER_BUFF.containsKey(player)){
	    		if(PLAYER_BUFF.get(player).containsKey(SELECNAME.get(player.getObjectId()))){
	    			if(PLAYER_BUFF.get(player).get(SELECNAME.get(player.getObjectId())).containsKey(idBuffSearch)){
	    				PassSave = true;
	    			}
	    		}
	    	}
    	}
    	return PassSave;
	}

	protected static boolean canSaveBuff(L2PcInstance player, int idBuffSearch){
    	boolean PassSave = false;
    	if(SELECNAME.get(player.getObjectId())!=null){
	    	if(PLAYER_BUFF.containsKey(player)){
	    		if(PLAYER_BUFF.get(player).containsKey(SELECNAME.get(player.getObjectId()))){
	    			if(!PLAYER_BUFF.get(player).get(SELECNAME.get(player.getObjectId())).containsKey(idBuffSearch)){
	    				PassSave = true;
	    			}
	    		}else{
	    			PassSave = true;
	    		}
	    	}else{
	    		PassSave = true;
	    	}
    	}
    	return PassSave;
	}
	
	

	private static void showWindowsSetBuff(L2PcInstance player, String BuffSelecc){

		int paginaActual = PAGINA_ACTUAL.get(player);

		
		
		String btnBackMain = "<button value=\"Back\" action=\"bypass -h voice .zeus_buffer " + (getNameSchemeSelec(player).length() == 0 ? "" : getNameSchemeSelec(player) + " 0 0") + "\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnDescrip = "<button value=\"Desc\" action=\"bypass -h voice .zeus_buffer 91 %IDBUFF% "+ BuffSelecc +"\" width=33 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnSave = "<button value=\"Save\" action=\"bypass -h voice .zeus_buffer 93 %IDBUFF% "+ BuffSelecc +"\" width=33 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnSupr = "<button value=\"Supr\" action=\"bypass -h voice .zeus_buffer 94 %IDBUFF% "+ BuffSelecc +"\" width=33 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnUse = "<button value=\"Use\" action=\"bypass -h voice .zeus_buffer 92 %IDBUFF% "+ BuffSelecc +"\" width=33 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";

		String btnPrevio = "<button value=\"<-\" action=\"bypass -h voice .zeus_buffer 60 "+ BuffSelecc +"\" width=33 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnNext = "<button value=\"->\" action=\"bypass -h voice .zeus_buffer 61 "+ BuffSelecc +"\" width=33 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";

		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML = central.LineaDivisora(1) + central.headFormat("Zeus Buffer") + central.LineaDivisora(1);

		String TablaBackNextPre = "<table width = 270><tr>" +
				"<td width = 90 align=center>%PREV%</td>" +
				"<td width = 90 align=center>"+btnBackMain+"</td>"+
				"<td width = 90 align=center>%NEXT%</td>"+
				"</tr></table>";

		int NumBuffInSchemme = getBuffCountFromSchemme(player);
		int NumDanceInSchemme = getBuffCountFromSchemme(player,true);

		MAIN_HTML += central.LineaDivisora(1) + central.headFormat("Scheme Selected <font color=LEVEL>" + (getNameSchemeSelec(player).length()==0?"NO SELECTED":getNameSchemeSelec(player))  +"</font>");
		MAIN_HTML += central.headFormat("Scheme Buffs: " + String.valueOf(NumBuffInSchemme) + ", Dances: " + String.valueOf(NumDanceInSchemme) ,"A4A4A4");
		MAIN_HTML += central.headFormat("Category: " + BuffSelecc,"A4A4A4") + central.LineaDivisora(1);

		MAIN_HTML += central.headFormat(TablaBackNextPre)+central.LineaDivisora(1);

		String TablaBuff = "<table width=270>%DATA%</table><br>";
		String FilasTabla = "<tr><td width=32 align=LEFT>%ICONO%</td><td width=238 align=LEFT><font color=%COLOR%>%NOMBRE%<br1>%BOTONES%</font></td></tr>";

		if(general.BUFF_CHAR_DATA==null){
			showWindows(player);
			return;
		}

		if(general.BUFF_CHAR_DATA.get(BuffSelecc)==null){
			showWindows(player);
			return;
		}

		if(general.BUFF_CHAR_DATA.get(BuffSelecc).isEmpty()){
			showWindows(player);
			return;
		}

		if(general.BUFF_CHAR_DATA.get(BuffSelecc).size()<=0){
			showWindows(player);
			return;
		}


		HashMap<Integer, HashMap<String, String>> BuffCategoria = general.BUFF_CHAR_DATA.get(BuffSelecc);


		if(BuffCategoria==null){
			showWindows(player);
			central.msgbox("This Category, " + BuffSelecc + " dont have buff", player);
			return;
		}else if(BuffCategoria.size()==0){
			showWindows(player);
			central.msgbox("This Category, " + BuffSelecc + " dont have buff", player);
			return;
		}

		int LimiteHojaBuffSelect = 10;
		
		Iterator itr = BuffCategoria.entrySet().iterator();
		String ParaBuff = "";
		int Contador = 0;
		int Desde = (paginaActual -1) *  LimiteHojaBuffSelect;
		int Hasta = Desde + LimiteHojaBuffSelect;
		boolean haveNext = false;

		int CantidadBuff = getBuffCountFromSchemme(player);
		int CantidadDances = getBuffCountFromSchemme(player, true);

		boolean CanAddMoreBuff = true;
		boolean CanAddMoreDance = true;

		int BUFF_MAX_AMOUNT = Config.BUFFS_MAX_AMOUNT;

		if(player.getSkillLevel(1405)>0){
			BUFF_MAX_AMOUNT = BUFF_MAX_AMOUNT + player.getSkillLevel(1405);
		}

		if(CantidadBuff >= BUFF_MAX_AMOUNT){
			CanAddMoreBuff = false;
		}

		if(CantidadDances >= Config.DANCES_MAX_AMOUNT){
			CanAddMoreDance = false;
		}

		boolean ModifScheme = general.BUFFCHAR_EDIT_SCHEME_ON_MAIN_WINDOWS_BUFFER;
		
	    while(itr.hasNext()){
	    	//fore=\"Icon.skill" + formato + "\">
	    	//<img src=\"L2UI.SquareBlank\" width=280 height=" + String.valueOf(Ancho)  + ">
	    	boolean sumarContador = true;
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	if(BuffCategoria.get(Entrada.getKey()).get("ACT").equals("1")){
		    	if(Contador >= Desde){
			    	if(Contador < Hasta){
				    	String idBuffSearch = BuffCategoria.get(Entrada.getKey()).get("ID");

						/*BUFF_CHAR_DATA.get(NomCat).get(rss.getInt(2)).put("ID", String.valueOf(rss.getInt(2)));
						BUFF_CHAR_DATA.get(NomCat).get(rss.getInt(2)).put("LEVEL", String.valueOf(rss.getInt(3)));*/
				    	int IDLEVELBUFF = Integer.valueOf(BuffCategoria.get(Entrada.getKey()).get("LEVEL"));

				    	String Icono = "<img src=\"Icon.skill"+ opera.getIconSkill(Integer.valueOf(idBuffSearch)) +"\" width=32 height=32 >";
				    	String descripIN = btnDescrip.replace("%IDBUFF%", idBuffSearch);
				    	String usarIN = btnUse.replace("%IDBUFF%", idBuffSearch);
				    	String SalvarIN = btnSave.replace("%IDBUFF%", idBuffSearch);
				    	String SuprIN = btnSupr.replace("%IDBUFF%", idBuffSearch);

				    	boolean canSave = canSaveBuff(player,Integer.valueOf(idBuffSearch));
				    	boolean canSupr =  canDeleteBuff(player,Integer.valueOf(idBuffSearch)); //getNameSchemeSelec(player).length()>0;

				    	if(isBuff(Integer.valueOf(idBuffSearch), IDLEVELBUFF)){
				    		if(!CanAddMoreBuff){
				    			canSave = false;
				    		}
				    	}else{
				    		if(!CanAddMoreDance){
				    			canSave = false;
				    		}
				    	}

				    	String tablaBotones = "<center><table width=100><tr><td width = 32>"+ usarIN +"</td><td width = 32>"+
				    	(canSave ? SalvarIN : ( canSupr ? SuprIN : "" ) ) +"</td><td width = 32>"+descripIN+"</td></tr></table></center>";

				    	String NomBuff = BuffCategoria.get(Entrada.getKey()).get("NOM");
				    	String Color = "";

				    	if(canSave || (getNameSchemeSelec(player).length()<=0)){
				    		Color = "01DF01";
				    	}else{
				    		Color = "424242";
				    	}
				    	
				    	if((ModifScheme && canSave) || !ModifScheme || (getNameSchemeSelec(player).length()<=0)){
				    		ParaBuff += FilasTabla.replace("%NOMBRE%", NomBuff).replace("%ICONO%", Icono).replace("%BOTONES%", tablaBotones).replace("%COLOR%", Color);
				    		sumarContador = true;
				    	}else{
				    		sumarContador = false;
				    	}
			    	}else{
			    		haveNext=true;
			    	}
		    	}
		    	if(sumarContador){
		    		Contador++;
		    	}
	    	}
	    }

	    TablaBuff = TablaBuff.replace("%DATA%", ParaBuff);

	    MAIN_HTML += central.LineaDivisora(1) + central.headFormat(TablaBuff) + central.LineaDivisora(1);

	    MAIN_HTML += central.headFormat(TablaBackNextPre) + central.getPieHTML(false) + "</body></html>";

	    if(haveNext){
	    	MAIN_HTML = MAIN_HTML.replace("%NEXT%", btnNext);
	    }else{
	    	MAIN_HTML = MAIN_HTML.replace("%NEXT%", "");
	    }

	    if(paginaActual>1){
	    	MAIN_HTML = MAIN_HTML.replace("%PREV%", btnPrevio);
	    }else{
	    	MAIN_HTML = MAIN_HTML.replace("%PREV%", "");
	    }


	    opera.enviarHTML(player, MAIN_HTML);
	}

	private static void showWindows(L2PcInstance player, String NomSchSelec, String params, boolean newScheme){

		PAGINA_ACTUAL.put(player, 1);

		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		//"<edit type=\"seguridad\" var=\"PIN_INGRESO\" width=150>" +

		String txtNom = "<edit var=\"nomBuff\" width=150>";
		String cmbSeleccion = "<combobox width=120 var=cmbIdDressme list=%LIST% >";
		String cmbBuff = "";

		String txtNameNuevo = "<edit var=\"txtName\" width=120>";

		String btnHeal = "<button value=\"Heal Me\" action=\"bypass -h voice .zeus_buffer 0 12 0\" width=100 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnRemoveBuff = "<button value=\"Cancel Buff's\" action=\"bypass -h voice .zeus_buffer 0 13 0\" width=100 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";

		String btnSelecCharPet = "<button value=\"%USE%\" action=\"bypass -h voice .zeus_buffer 0 80 %USE%\" width=100 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";

		String tablaAyuda = "<table width=260><tr><td width=130 align=center>"+ btnHeal +"</td><td width=130 align=center>"+ btnRemoveBuff +"</td></tr></table>";

		//:

		String btnSeleccionar = "<button value=\"Select\" action=\"bypass -h voice .zeus_buffer $cmbIdDressme 0 0\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnGuardar = "<button value=\"Save\" action=\"bypass -h voice .zeus_buffer $txtName 5 0\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnBuffSelecCate = "<button value=\"%NOMBRE%\" action=\"bypass -h voice .zeus_buffer %NOMBRE% 2 0\" width=140 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnBackMain = "<button value=\"Back\" action=\"bypass -h voice .zeus_buffer " + (getNameSchemeSelec(player).length() == 0 ? "" : getNameSchemeSelec(player) + " 0 0") + "\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnNewSche = "<button value=\"New\" action=\"bypass -h voice .zeus_buffer 0 6 0\" width=80 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnBuffMe = "<button value=\"Buff Me\" action=\"bypass -h voice .zeus_buffer 0 10 0\" width=86 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnEditScheme = "<button value=\"Edit Scheme\" action=\"bypass -h voice .zeus_buffer 0 14 0\" width=86 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnBuffDelete = "<button value=\"Delete Sch.\" action=\"bypass -h voice .zeus_buffer 0 11 0\" width=96 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String ListaCombo = "";



		if(NomSchSelec.length()>0){
			ListaCombo = NomSchSelec;
		}

		String tablaBotones = "<center><table width=270 align=CENTER>%DATA%</table></center>";
		String tempTabla = "";


		/*
		if(!BUFF_CHAR_DATA.containsKey("CATE")){
			BUFF_CHAR_DATA.put("CATE", new HashMap<Integer,HashMap<String,String>>());
		}
		BUFF_CHAR_DATA.get("CATE").put(rss.getInt(1), new HashMap<String, String>());
		BUFF_CHAR_DATA.get("CATE").get(rss.getInt(1)).put("IDCATE", String.valueOf(rss.getInt(1)));
		BUFF_CHAR_DATA.get("CATE").get(rss.getInt(1)).put("NOMCATE", String.valueOf(rss.getInt(2)));
		BUFF_CHAR_DATA.get("CATE").get(rss.getInt(1)).put("POSI", String.valueOf(rss.getInt(3)));
		 * */


		Iterator itr = general.BUFF_CHAR_DATA.get("CATE").entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	HashMap<String,String> datos = general.BUFF_CHAR_DATA.get("CATE").get((int)Entrada.getKey()) ;
	    	tempTabla += "<tr><td width=200 align=CENTER>"+ (btnBuffSelecCate.replace("%NOMBRE%", datos.get("NOMCATE")).replace("%ID%", datos.get("IDCATE"))) +"</td></tr>";
	    }
	    tablaBotones = tablaBotones.replace("%DATA%", tempTabla);


		Vector<String> MySch = new Vector<String>();
		MySch = general.getNameBuffSch(player);

		if(MySch!=null){
			if(!MySch.isEmpty()){
				for(String NomCHS : MySch){
					if(!NomCHS.equals(NomSchSelec)){
						if(ListaCombo.length()>0){
							ListaCombo += ";";
						}
						ListaCombo += NomCHS;
					}
				}
			}
		}

		String Tabla = "";

		if((ListaCombo.length()>0) && !newScheme){
		cmbSeleccion = cmbSeleccion.replace("%LIST%", ListaCombo);
		Tabla = "<table width=275><tr>" +
			"<td width=90>Select schemes</td><td width=120>"+cmbSeleccion+"</td><td>"+btnSeleccionar+"</td></tr>" +
			"</tr>" +
			"</table><table width=275><tr><td width=275 align=center>"+ btnNewSche +"</td></tr></table><br1>";
		}else{
			Tabla = "<table width=275>" +
				"<tr><td width=275 align=center>New Scheme Name</td></tr>" +
				"<tr><td width=275 align=center>"+txtNameNuevo+"<br1></td></tr>" +
				"<tr><td width=275 align=center>"+btnGuardar+"<br1></td></tr>" +
				(newScheme? "<tr><td width=275 align=center>"+btnBackMain+"</td></tr>":"") +
				"</table><br1>";
		}

		MAIN_HTML += central.LineaDivisora(1) + central.headFormat("ZeuS Buffer") + central.LineaDivisora(1);

		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(Tabla) + central.headFormat(tablaAyuda) + central.LineaDivisora(2);

		if(useInCHar == null){
			useInCHar.put(player, true);
		}else{
			if(!useInCHar.containsKey(player)){
				useInCHar.put(player, true);
			}
		}

		if(SELECNAME.containsKey(player.getObjectId())){
			if(SELECNAME.get(player.getObjectId())!=null){
				String tablaBtn = "<table width = 260><tr><td width=80 align=center>"+ btnBuffMe +"</td><td width=80 align=center>"+ ( general.BUFFCHAR_EDIT_SCHEME_ON_MAIN_WINDOWS_BUFFER ? btnEditScheme : "" )  +"</td><td width=80align=CENTER>"+btnBuffDelete+"</td></tr></table>";
				MAIN_HTML += central.LineaDivisora(1) + central.headFormat("Selected Schema <font color=LEVEL>" + SELECNAME.get(player.getObjectId()) + "</font><br1>" );
				if(general.BUFFCHAR_PET){
					String buffTo = useInCHar.containsKey(player) ? (useInCHar.get(player) ? "Char" : "Pet") : "Char";
					String btnChange = btnSelecCharPet.replace("%USE%",  useInCHar.get(player) ? "Pet" : "Char"  );
					MAIN_HTML += central.LineaDivisora(1) + central.headFormat("You'll buff you <font color=LEVEL>" + buffTo  + "</font>, change to "+ btnChange +"<br1>" );
				}

				int CantidadBuff = getBuffCountFromSchemme(player);
				int CantidadDances = getBuffCountFromSchemme(player, true);

				MAIN_HTML += central.headFormat("Scheme Buffs: " + String.valueOf(  CantidadBuff ) + ", Dances: " + String.valueOf(CantidadDances),"A4A4A4");
				MAIN_HTML += central.headFormat(tablaBtn) + central.LineaDivisora(1);
			}
		}

		MAIN_HTML += central.LineaDivisora(1) + central.LineaDivisora(2) + central.headFormat(tablaBotones) + central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += central.getPieHTML(false) + "</body></html>";

		opera.enviarHTML(player, MAIN_HTML);
	}
	
	protected static String getSchemeFromPlayer(L2PcInstance player){
		Vector<String> MySch = new Vector<String>();
		MySch = general.getNameBuffSch(player);

		String NomSchSelec = getNameSchemeSelec(player);
		
		String ListaCombo = NomSchSelec;
		
		if(MySch!=null){
			if(!MySch.isEmpty()){
				for(String NomCHS : MySch){
					if(!NomCHS.equals(NomSchSelec)){
						if(ListaCombo.length()>0){
							ListaCombo += ";";
						}
						ListaCombo += NomCHS;
					}
				}
			}
		}
		return ListaCombo;
	}
	
	protected static boolean haveScheme(L2PcInstance player){
		Vector<String> MySch = new Vector<String>();
		MySch = general.getNameBuffSch(player);
		if(MySch!=null){
			if(!MySch.isEmpty()){
				return true;
			}
		}
		return false;
	}
	
	protected static boolean haveSchemeSelected(L2PcInstance player){
		return getSchemeSelectedName(player).equals("NONE") ? false : true;
	}
	
	
	protected static String getSchemeSelectedName(L2PcInstance player){
		String retorno = "NONE";
		if(SELECNAME!=null){
			if(SELECNAME.containsKey(player.getObjectId())){
				retorno = SELECNAME.get(player.getObjectId());
			}
		}
		return retorno;
	}
	
}
