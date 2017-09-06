package ZeuS.interfase;

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
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.network.serverpackets.SetupGauge;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.procedimientos.opera;

public class buffer_zeus {

	private static final String TITULO = "Buff System";
	private static final Logger _log = Logger.getLogger(buffer_zeus.class.getName());
	private static final String btnBackBuffer = "<button value=\"Buffer\" action=\"bypass -h ZeuSNPC zeus_buffer_npc home 0 0 0 0 0 0\" width=93 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
	private enum TipoBuff{
		ID,
		BUFF_ORDER,
		BUFF_ID,
		BUFF_LEVEL,
		BUFF_DESCRIP,
		FOR_CLASS,
		ACTIVO
	}
	private final static HashMap<Integer,HashMap<Integer,HashMap<String,String>>> BuffList = new HashMap<Integer,HashMap<Integer,HashMap<String,String>>>();
	private final static Vector<String>BuffCategorias = new Vector<String>();
	private final static HashMap<L2PcInstance,Boolean>BUFF_CHAR = new HashMap<L2PcInstance, Boolean>();
	private final static HashMap<L2PcInstance,String> SCHEME_SELECT = new HashMap<L2PcInstance,String>();
	private final static HashMap<Integer,Integer> BUFF_CATEGORIA = new HashMap<Integer,Integer>();
	private final static Vector<Integer> ID_CAT_DANCES = new Vector<Integer>();

	private final static HashMap<L2PcInstance, HashMap<Integer, HashMap<String,HashMap<Integer,HashMap<String,Integer>>>>> CHAR_BUFFER_SCH = new HashMap<L2PcInstance, HashMap<Integer, HashMap<String,HashMap<Integer,HashMap<String,Integer>>>>>();

	protected static final void setSelectScheme(L2PcInstance player, String SchemeName){
		SCHEME_SELECT.put(player, SchemeName);
	}

	private final static void ejecutarSQL(String sqlIN){
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn=ConnectionFactory.getInstance().getConnection();
			psqry=conn.prepareStatement(sqlIN);
			try{
				psqry.executeUpdate();
				psqry.close();
			}catch(SQLException e){
			}
		}catch (SQLException e) {
		}
		try{
			conn.close();
		}catch (SQLException e2) {

		}
	}

	private final static void removeBuffFromScheme(L2PcInstance player, int IDBuff){
		Connection conn = null;
		PreparedStatement psqry = null;
		int IDScheme = getIDFromSchemeName(player, getSchemePlayer(player));
		String NAMEScheme = getSchemePlayer(player);
		try{
			conn=ConnectionFactory.getInstance().getConnection();
			psqry=conn.prepareStatement("DELETE FROM zeus_buffer_scheme_contents WHERE schemeId=? AND buffId=?");
			psqry.setInt(1, IDScheme);
			psqry.setInt(2,IDBuff);
			try{
				psqry.executeUpdate();
				psqry.close();
			}catch(SQLException e){
			}
		}catch (SQLException e) {
		}
		try{
			conn.close();
		}catch (SQLException e2) {

		}

		if(CHAR_BUFFER_SCH.get(player).get(IDScheme).get(NAMEScheme).containsKey(IDBuff)){
			CHAR_BUFFER_SCH.get(player).get(IDScheme).get(NAMEScheme).remove(IDBuff);
		}

	}

	private final static String showDeleteSchemaWindows(L2PcInstance player){
		String html ="<html><title>"+ general.TITULO_NPC() +"</title><body>";

		html += central.LineaDivisora(1) + central.headFormat("Delete Scheme " + getSchemePlayer(player)) + central.LineaDivisora(1);

		String btnAceptar = "<button value=\"YES\" action=\"bypass -h ZeuSNPC zeus_buffer_npc confirmDelete 0 0 0 0 0 0\" width=65 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnCancelar = "<button value=\"NO\" action=\"bypass -h ZeuSNPC zeus_buffer_npc confirmCancel 0 0 0 0 0 0\" width=65 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String Tabla ="<table width=270><tr><td width=135 align=CENTER>"+ btnAceptar +"</td><td width=135 align=CENTER>"+ btnCancelar +"</td></tr></table>";
		html += central.LineaDivisora(3) + central.headFormat("You want to delete the schema?<br1><font color=FF8000>"+ getSchemePlayer(player) +"</font><br1>" + Tabla,"LEVEL") + central.LineaDivisora(3);
		html += central.LineaDivisora(1) +  central.headFormat(btnBackBuffer) + central.LineaDivisora(1) + central.BotonGOBACKZEUS() + central.getPieHTML() + "</body></html>";
		return html;
	}

	private final static void createNewBuffSche(L2PcInstance player, int IDBuff, int lvlBuff){
		int idSchemma = getIDFromSchemeName(player, getSchemePlayer(player));
		int idCategoria = BUFF_CATEGORIA.get(IDBuff);
		String qry = "INSERT INTO zeus_buffer_scheme_contents(schemeId,buffId,buffLevel,buffClass) VALUES(?,?,?,?)";
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn=ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(qry);
			psqry.setInt(1, idSchemma);
			psqry.setInt(2, IDBuff);
			psqry.setInt(3, lvlBuff);
			psqry.setInt(4, idCategoria);
			try{
				psqry.executeUpdate();
				psqry.close();
				conn.close();
			}catch (SQLException e) {
			}
		}catch(SQLException a){
		}
		try{
			conn.close();
		}catch(SQLException a){

		}

		/*
		 * 			CHAR_BUFFER_SCH.get(player).get(idScheme).get(Nombre).put(rss.getInt(2), new HashMap<String, Integer>());
		 *			CHAR_BUFFER_SCH.get(player).get(idScheme).get(Nombre).get(rss.getInt(2)).put(TipoBuff.BUFF_ID.name(), rss.getInt(2));
		 *			CHAR_BUFFER_SCH.get(player).get(idScheme).get(Nombre).get(rss.getInt(2)).put(TipoBuff.BUFF_LEVEL.name(), rss.getInt(3));
		*/

		if(CHAR_BUFFER_SCH==null){
			CHAR_BUFFER_SCH.put(player, new HashMap<Integer, HashMap<String,HashMap<Integer,HashMap<String,Integer>>>>() );
		}
		if(!CHAR_BUFFER_SCH.containsKey(player)){
			CHAR_BUFFER_SCH.put(player, new HashMap<Integer, HashMap<String,HashMap<Integer,HashMap<String,Integer>>>>() );
		}
		if(!CHAR_BUFFER_SCH.get(player).containsKey(idSchemma)){
			CHAR_BUFFER_SCH.get(player).put(idSchemma, new HashMap<String,HashMap<Integer,HashMap<String,Integer>>>());
			CHAR_BUFFER_SCH.get(player).get(idSchemma).put(getSchemePlayer(player), new HashMap<Integer,HashMap<String,Integer>>());
		}
		if(!CHAR_BUFFER_SCH.get(player).get(idSchemma).get(getSchemePlayer(player)).containsKey(IDBuff)){
			CHAR_BUFFER_SCH.get(player).get(idSchemma).get(getSchemePlayer(player)).put(IDBuff, new HashMap<String,Integer>());
		}
		CHAR_BUFFER_SCH.get(player).get(idSchemma).get(getSchemePlayer(player)).get(IDBuff).put(TipoBuff.BUFF_ID.name(), IDBuff);
		CHAR_BUFFER_SCH.get(player).get(idSchemma).get(getSchemePlayer(player)).get(IDBuff).put(TipoBuff.BUFF_LEVEL.name(), lvlBuff);

	}


	private final static void createNewSchema(L2PcInstance player, String NombreScheme){
		String qry = "INSERT INTO zeus_buffer_scheme_list(playerId,schemeName,buffPlayer) VALUES (?,?,0)";
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn=ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(qry);
			psqry.setInt(1, player.getObjectId());
			psqry.setString(2, NombreScheme);
			try{
				psqry.executeUpdate();
				psqry.close();
				conn.close();
			}catch (SQLException e) {
			}
		}catch(SQLException a){
		}
		try{
			conn.close();
		}catch(SQLException a){

		}
		getBufferNPC_char(player);
		setSelectScheme(player, NombreScheme);

	}

	private final static String getSchemePlayer(L2PcInstance player){
		if(SCHEME_SELECT!=null){
			if(SCHEME_SELECT.containsKey(player)){
				return SCHEME_SELECT.get(player);
			}
		}
		return "";
	}

	public static void ModificarUbicacionBuff(int idBuff, boolean Subir){
		int PosicionBuff = Integer.valueOf(BuffList.get( BUFF_CATEGORIA.get(idBuff)).get(idBuff).get(TipoBuff.BUFF_ORDER.name()));
		HashMap<Integer,Integer> BUFFORDER = new HashMap<Integer,Integer>();
		int idCategoria = BUFF_CATEGORIA.get(idBuff);
		Iterator itr = BuffList.get(idCategoria).entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	int idBuffSet = (int)Entrada.getKey();
	    	BUFFORDER.put(Integer.valueOf(BuffList.get(idCategoria).get(idBuffSet).get(TipoBuff.BUFF_ORDER.name())),idBuffSet);
	    }


		String Qry1 = "";
		if(Subir && (PosicionBuff==1)){
			return;
		}
		if(Subir){
			Qry1 = "UPDATE zeus_buffer_buff_list SET buffOrder=" + String.valueOf(PosicionBuff - 1) + " where buffId=" + idBuff +" and buffClass="+String.valueOf(idCategoria);
			ejecutarSQL(Qry1);
			if(BUFFORDER.containsKey(PosicionBuff+1)){
				Qry1 = "UPDATE zeus_buffer_buff_list SET buffOrder=" + String.valueOf(PosicionBuff)  + " where buffId=" + BUFFORDER.get(PosicionBuff+1) +" and buffClass="+String.valueOf(idCategoria);
				ejecutarSQL(Qry1);
			}
		}else{
			Qry1 = "UPDATE zeus_buffer_buff_list SET buffOrder=" + String.valueOf(PosicionBuff + 1) + " where buffId=" + idBuff +" and buffClass="+String.valueOf(idCategoria);
			ejecutarSQL(Qry1);
			if(BUFFORDER.containsKey(PosicionBuff+1)){
				Qry1 = "UPDATE zeus_buffer_buff_list SET buffOrder=" + String.valueOf(PosicionBuff)  + " where buffId=" + BUFFORDER.get(PosicionBuff+1) +" and buffClass="+String.valueOf(idCategoria);
				ejecutarSQL(Qry1);
			}
		}

		loadBuff();


	}


	private static void setBuffToScheme(L2PcInstance player,int idScheme, String Nombre){
		String strConsulta = "SELECT * FROM zeus_buffer_scheme_contents WHERE schemeId=" + String.valueOf(idScheme);
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(strConsulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					CHAR_BUFFER_SCH.get(player).get(idScheme).get(Nombre).put(rss.getInt(3), new HashMap<String, Integer>());
					CHAR_BUFFER_SCH.get(player).get(idScheme).get(Nombre).get(rss.getInt(3)).put(TipoBuff.BUFF_ID.name(), rss.getInt(3));
					CHAR_BUFFER_SCH.get(player).get(idScheme).get(Nombre).get(rss.getInt(3)).put(TipoBuff.BUFF_LEVEL.name(), rss.getInt(4));
				}catch(SQLException e){
				}
			}
		}catch (SQLException e){

		}
		try{
			conn.close();
		}catch(SQLException a ){

		}
	}

	private static boolean buffIsInSchemeSelect(L2PcInstance Player, int idBuff){

		if(CHAR_BUFFER_SCH==null){
			return false;
		}

		if(!CHAR_BUFFER_SCH.containsKey(Player)){
			return false;
		}

		if(getSchemePlayer(Player).length()==0){
			return false;
		}

		int idSchemeSelect = getIDFromSchemeName(Player, getSchemePlayer(Player));
		//HashMap<L2PcInstance, HashMap<Integer, HashMap<String,HashMap<String,Integer>>>>
		if(CHAR_BUFFER_SCH.get(Player).containsKey(idSchemeSelect)){
			if(CHAR_BUFFER_SCH.get(Player).get(idSchemeSelect).get(getSchemePlayer(Player)).containsKey(idBuff)){
				return true;
			}
		}

		return false;
	}

	private static String getNameFromSchemeID(L2PcInstance player, int IDScheme){

		if(CHAR_BUFFER_SCH == null){
			return "";
		}

		if(!CHAR_BUFFER_SCH.containsKey(player)){
			return "";
		}

		HashMap<Integer, HashMap<String, HashMap<Integer, HashMap<String, Integer>>>> misCH = CHAR_BUFFER_SCH.get(player);
		Iterator itr = misCH.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	int idScheme = (int)Entrada.getKey();
	    	Iterator itr2 = misCH.get(idScheme).entrySet().iterator();
	    	while(itr2.hasNext()){
	    		Map.Entry Nom = (Map.Entry)itr2.next();
	    		String Nombre = (String)Nom.getKey();
	    		return Nombre;
	    	}
	    }
	    return "";
	}

	private static int getIDFromSchemeName(L2PcInstance player, String NameSch){
		HashMap<Integer, HashMap<String, HashMap<Integer, HashMap<String, Integer>>>> misCH = CHAR_BUFFER_SCH.get(player);
		Iterator itr = misCH.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	int idScheme = (int)Entrada.getKey();
	    	Iterator itr2 = misCH.get(idScheme).entrySet().iterator();
	    	while(itr2.hasNext()){
	    		Map.Entry Nom = (Map.Entry)itr2.next();
	    		String Nombre = (String)Nom.getKey();
	    		if(Nombre.equals(NameSch)){
	    			return idScheme;
	    		}
	    	}
	    }
	    return -1;
	}

	private static Vector<String> SchemePlayer(L2PcInstance player){
		Vector<String> Nombres = new Vector<String>();
		if(CHAR_BUFFER_SCH!=null){
			if(CHAR_BUFFER_SCH.containsKey(player)){
				HashMap<Integer, HashMap<String, HashMap<Integer, HashMap<String, Integer>>>> misCH = CHAR_BUFFER_SCH.get(player);
				Iterator itr = misCH.entrySet().iterator();
			    while(itr.hasNext()){
			    	Map.Entry Entrada = (Map.Entry)itr.next();
			    	int idScheme = (int)Entrada.getKey();
			    	if(CHAR_BUFFER_SCH.get(player).containsKey(idScheme)){
				    	Iterator itr2 = misCH.get(idScheme).entrySet().iterator();
				    	while(itr2.hasNext()){
				    		Map.Entry Nom = (Map.Entry)itr2.next();
				    		Nombres.add((String)Nom.getKey());
				    	}
			    	}
			    }
			}
		}
	    return Nombres;
	}

	public static void getBufferNPC_char(L2PcInstance player){
		String strConsulta = "SELECT * FROM zeus_buffer_scheme_list WHERE playerId=" + String.valueOf(player.getObjectId());
		Connection conn = null;
		PreparedStatement psqry = null;
		if(CHAR_BUFFER_SCH!=null){
			if(CHAR_BUFFER_SCH.containsKey(player)){
				CHAR_BUFFER_SCH.get(player).clear();
			}
		}
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(strConsulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					if(!CHAR_BUFFER_SCH.containsKey(player)){
						CHAR_BUFFER_SCH.put(player, new HashMap<Integer, HashMap<String,HashMap<Integer, HashMap<String,Integer>>>>());
					}
					CHAR_BUFFER_SCH.get(player).put(rss.getInt(1), new HashMap<String,HashMap<Integer, HashMap<String,Integer>>>());
					CHAR_BUFFER_SCH.get(player).get(rss.getInt(1)).put(rss.getString(4), new HashMap<Integer, HashMap<String,Integer>>());
					setBuffToScheme(player,rss.getInt(1), rss.getString(4));
				}catch(SQLException e){
				}
			}
		}catch (SQLException e){

		}
		try{
			conn.close();
		}catch(SQLException a ){

		}
	}

	public static void loadBuff(){
		try{
			BuffCategorias.clear();
		}catch(Exception a){
		}
		if(BuffCategorias!=null){
			BuffCategorias.clear();
		}
		if(BuffList !=null){
			BuffList.clear();
		}
		BuffCategorias.add("Improved_0");
		BuffCategorias.add("Buff_1");
		BuffCategorias.add("Resist_2");
		BuffCategorias.add("Prophecy_3");
		BuffCategorias.add("Chant_4");
		BuffCategorias.add("Cubic_5");
		BuffCategorias.add("Dance_6");
		BuffCategorias.add("Song_7");
		BuffCategorias.add("Special_8");
		BuffCategorias.add("Others_9");

		ID_CAT_DANCES.add(6); ID_CAT_DANCES.add(7);

		String strConsulta = "SELECT * FROM zeus_buffer_buff_list ORDER BY buffOrder";
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(strConsulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					if(!BuffList.containsKey(rss.getInt(2))){
						BuffList.put(rss.getInt(2),new HashMap<Integer,HashMap<String,String>>());
					}
					if(!BuffList.get(rss.getInt(2)).containsKey(rss.getInt(5))){
						BuffList.get(rss.getInt(2)).put(rss.getInt(5), new HashMap<String,String>());
					}
					BuffList.get(rss.getInt(2)).get(rss.getInt(5)).put(TipoBuff.ID.name(), String.valueOf(rss.getInt(1)));
					BuffList.get(rss.getInt(2)).get(rss.getInt(5)).put(TipoBuff.BUFF_ID.name(), String.valueOf(rss.getInt(5)));
					BuffList.get(rss.getInt(2)).get(rss.getInt(5)).put(TipoBuff.BUFF_LEVEL.name(), String.valueOf(rss.getInt(6)));
					BuffList.get(rss.getInt(2)).get(rss.getInt(5)).put(TipoBuff.BUFF_DESCRIP.name(), rss.getString(7));
					BuffList.get(rss.getInt(2)).get(rss.getInt(5)).put(TipoBuff.BUFF_ORDER.name(), String.valueOf(rss.getInt(4)));
					BuffList.get(rss.getInt(2)).get(rss.getInt(5)).put(TipoBuff.FOR_CLASS.name(), String.valueOf(rss.getInt(8)));
					BuffList.get(rss.getInt(2)).get(rss.getInt(5)).put(TipoBuff.ACTIVO.name(), String.valueOf(rss.getInt(9)));
					BUFF_CATEGORIA.put(rss.getInt(5), rss.getInt(2));
				}catch(SQLException e){
				}
			}
		}catch (SQLException e){

		}
		try{
			conn.close();
		}catch(SQLException a ){

		}
	}

	private static String getGrillaBuff(L2PcInstance player,String IDBUFFER, String LVLBUFF, String DESCRIP, int ColorShow, int Pagina, int idCategoria){
		boolean IsInSchema = buffIsInSchemeSelect(player,Integer.valueOf(IDBUFFER));
		//action=\"bypass -h ZeuSNPC zeus_buffer_npc char 0 0 0 0 0 0\"
		String btnUsar = "<button value=\"Buff Me\" action=\"bypass -h ZeuSNPC zeus_buffer_npc BuffUsar "+IDBUFFER+" "+LVLBUFF+" "+ String.valueOf(Pagina) +" "+ String.valueOf(idCategoria) +" 0 0\" width=100 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnGuardar = "<button value=\"Save to Scheme\" action=\"bypass -h ZeuSNPC zeus_buffer_npc BuffSave "+IDBUFFER+" "+LVLBUFF+" "+ String.valueOf(Pagina) +" "+String.valueOf(idCategoria)+" 0 0\" width=100 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String btnBorrar = "<button value=\"Supr from S.\" action=\"bypass -h ZeuSNPC zeus_buffer_npc BuffSupr "+IDBUFFER+" "+LVLBUFF+" "+ String.valueOf(Pagina) +" "+String.valueOf(idCategoria)+" 0 0\" width=100 height=18 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">";
		String Color = ColorShow==1?"424242":"585858";
		String HTML = "<center><table width=270 bgcolor="+ Color +">";
		String Icono = "<img src=\"Icon.skill"+ opera.getIconSkill(Integer.valueOf(IDBUFFER)) +"\" width=32 height=32 >";

		String BtnChicos = "<table width=230><tr>";
		if(general.BUFFER_SCHEME_SYSTEM && (getSchemePlayer(player).length()>0)){
			BtnChicos += "<td width=115 align=CENTER>" + btnUsar + "</td>";
			if(IsInSchema){
				BtnChicos += "<td width=115 align=CENTER>" + btnBorrar + "</td>";
			}else{
				BtnChicos += "<td width=115 align=CENTER>" + btnGuardar + "</td>";
			}
		}else{
			BtnChicos += "<td width=230 align=CENTER>" + btnUsar + "</td>";
		}
		BtnChicos +="</tr></table>";

		String ColorFont = (IsInSchema?"A4A4A4":"C8FE2E");

		String Nombre = opera.getSkillName(Integer.valueOf(IDBUFFER), Integer.valueOf(LVLBUFF));
		HTML += "<tr><td width=32>" + Icono + "</td><td width=238 align=CENTER><font color="+ColorFont+">" + Nombre + "</font></td></tr>";
		HTML += "<tr><td width=32></td><td width=238 fixwidth=230><font color=" + ColorFont + ">" + DESCRIP + "</font><br1>" + BtnChicos + "<br1></td></tr></table><br1>";
		return HTML;
	}

	private static String getCategoriaWindows(L2PcInstance player, int IdCategoria, String NombreCategoria, int Pagina){
		String HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		HTML += central.LineaDivisora(1) + central.headFormat(TITULO)+ central.LineaDivisora(1);
		String SchemeSelected = getSchemePlayer(player);
		boolean haveSchemeSelect = (SchemeSelected.length()>0 ? true : false);


		if(SchemeSelected.length()>0){
			SchemeSelected = "<br1>"+"Scheme: " + SchemeSelected;
		}

		String CantidadBuff = "";

		if(!general.BUFFER_SCHEME_SYSTEM){
			SchemeSelected = "";
		}else{
			if(SchemeSelected.length()>0){
				int CantidadBuffAguanta = Config.BUFFS_MAX_AMOUNT;
				if(player.getSkillLevel(1405)>0){
					CantidadBuffAguanta = CantidadBuffAguanta + player.getSkillLevel(1405);
				}
				CantidadBuff = "<br1>You have <font color=FF8000>" + String.valueOf(getCantidadBuff_SchemaPlayer(player, true)) + "</font> of " + CantidadBuffAguanta + " Buff, and <font color=FF8000>" + String.valueOf(getCantidadBuff_SchemaPlayer(player, false)) + "</font> of " + Config.DANCES_MAX_AMOUNT + " Dances.";
			}
		}

		HTML += central.LineaDivisora(1) + central.headFormat("You are in " + NombreCategoria + SchemeSelected + CantidadBuff ,"LEVEL");

		if(!general.BUFF_GRATIS){
			HTML += central.LineaDivisora(1) + central.headFormat("Cost of Service: " + opera.getFormatNumbers(getCobrarBuff(IdCategoria,true)) + " " + central.getNombreITEMbyID(general.BUFFER_ID_ITEM)) + central.LineaDivisora(1);
		}

		int MaximoPorPagina = 7;
		int Desde = Pagina * MaximoPorPagina;
		int Hasta = MaximoPorPagina * ( Pagina + 1);
		int Contador = 0;
		int ContadorPases = 0;

		boolean Siguente=false;
		boolean Anterior=(Pagina>0?true:false);

		String btnAnt = "<button value=\""+ msg.BOTON_ATRAS +"\" action=\"bypass -h ZeuSNPC zeus_buffer_npc loadCate "+ String.valueOf(IdCategoria) +" "+ NombreCategoria +" "+ (Pagina - 1) +" 0 0 0\" width=80 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnSig = "<button value=\""+ msg.BOTON_SIGUENTE +"\" action=\"bypass -h ZeuSNPC zeus_buffer_npc loadCate "+ String.valueOf(IdCategoria) +" "+ NombreCategoria +" "+ (Pagina + 1) +" 0 0 0\" width=80 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		int tipoColor =0;

		String GrillaBuff ="";

		if(BuffList != null){
			if(BuffList.containsKey(IdCategoria)){
				HashMap<Integer, HashMap<String, String>> BUFF_SHOW = BuffList.get(IdCategoria);
				Iterator itr = BUFF_SHOW.entrySet().iterator();
			    while(itr.hasNext()){
			    	Map.Entry Entrada = (Map.Entry)itr.next();
			    	int idBuff = (int)Entrada.getKey();
			    	HashMap<String, String> BuffIndo = BUFF_SHOW.get(idBuff);
			    	if(BUFF_SHOW.get(idBuff)!=null){
	    				if(BuffIndo.get(TipoBuff.ACTIVO.name()).equals("1")){
	    					if(ContadorPases >= Desde){
	    						if(Contador < /*Hasta*/MaximoPorPagina){
	    							GrillaBuff += getGrillaBuff(player,BuffIndo.get(TipoBuff.BUFF_ID.name()),BuffIndo.get(TipoBuff.BUFF_LEVEL.name()),BuffIndo.get(TipoBuff.BUFF_DESCRIP.name()),((tipoColor++%2)==0?1:0),Pagina,IdCategoria) + "<br>";
	    							Contador++;
	    						}else{
	    							Siguente = true;
	    						}
	    					}
	    					ContadorPases++;
						}
			    	}
			    }
			}
		}

		String GrillaBtnChicos = "<table width=280><tr>" +
				"<td align=CENTER width=93>"+ (Anterior? btnAnt:"") +"</td>" +
				"<td align=CENTER width=93>"+ btnBackBuffer +"</td>"+
				"<td align=CENTER width=93>"+(Siguente?btnSig:"")+"</td></tr></table>";

		HTML += central.headFormat(GrillaBtnChicos) + central.LineaDivisora(1) + GrillaBuff;

		HTML += central.LineaDivisora(1) + central.headFormat(GrillaBtnChicos) + central.LineaDivisora(1);

		HTML += central.BotonGOBACKZEUS() + central.getPieHTML() + "</body></html>";
		return HTML;
	}

	private static void deleteScheme(L2PcInstance player){
		if(getSchemePlayer(player).length()>0){
			String NombreSchemme = getSchemePlayer(player);
			int idScheme = getIDFromSchemeName(player, NombreSchemme);

			Connection conn = null;
			PreparedStatement psqry = null;
			try{
				conn=ConnectionFactory.getInstance().getConnection();
				psqry=conn.prepareStatement("DELETE FROM zeus_buffer_scheme_list WHERE id=? AND schemeName=?");
				psqry.setInt(1, idScheme);
				psqry.setString(2, NombreSchemme);
				try{
					psqry.executeUpdate();
					psqry.close();
				}catch(SQLException e){
				}
			}catch (SQLException e) {
			}
			try{
				conn.close();
			}catch (SQLException e2) {

			}

			psqry = null;

			try{
				conn=ConnectionFactory.getInstance().getConnection();
				psqry=conn.prepareStatement("DELETE FROM zeus_buffer_scheme_contents WHERE schemeId=?");
				psqry.setInt(1, idScheme);
				try{
					psqry.executeUpdate();
					psqry.close();
				}catch(SQLException e){
				}
			}catch (SQLException e) {
			}
			try{
				conn.close();
			}catch (SQLException e2) {

			}

			if(CHAR_BUFFER_SCH.containsKey(player)){
				if(CHAR_BUFFER_SCH.get(player).containsKey(idScheme)){
					CHAR_BUFFER_SCH.get(player).remove(idScheme);
				}
			}

			if(SCHEME_SELECT.size()>0){
				if(SCHEME_SELECT.containsKey(player)){
					SCHEME_SELECT.put(player, "");
				}
			}

		}
	}

	private static void barraDeTiempo(L2PcInstance player, int ColorBarra){
		int tmpBloqueado = Integer.valueOf((int) ((System.currentTimeMillis() + (general.BUFFER_TIME_WAIT * 1000))/1000));
		general.charBufferTime.put(player, tmpBloqueado);
		player.sendPacket(new SetupGauge(ColorBarra, (general.BUFFER_TIME_WAIT * 1000) + 600));
	}

	private static boolean canDoIt(L2PcInstance player){
		if (Integer.valueOf((int) (System.currentTimeMillis()/1000)) > general.charBufferTime.get(player)){
			barraDeTiempo(player,3);
			return true;
		}else{
			return false;
		}
	}


	public static String Delegar(L2PcInstance player, String Parametros){
		if(general.BUFFER_GM_ONLY){
			if(!player.isGM()){
				central.msgbox("Buffer GM ONLY.", player);
				return "";
			}
		}

		if(general.BUFFER_LVL_MIN > player.getLevel() ){
			central.msgbox(msg.NECESITAS_SER_LVL_$level_PARA_ESTA_OPERACION.replace("$Level", String.valueOf(player.getLevel())), player);
			return "";
		}

		if(BUFF_CHAR==null){
			BUFF_CHAR.put(player, true);
		}
		if(!BUFF_CHAR.containsKey(player)){
			BUFF_CHAR.put(player, true);
		}
		String Operacion,P1,P2,P3,P4,P5,P6, strHTML = "";

		if(Parametros.split(" ").length>8){
			strHTML = getMainWindowsBuffer(player);
			return strHTML;
		}

		String[] SplitOpera = Parametros.split(" ");
		int i=1;
		Operacion = SplitOpera[i++];
		P1 = SplitOpera[i++];
		P2 = SplitOpera[i++];
		P3 = SplitOpera[i++];
		P4 = SplitOpera[i++];
		try{
			P5 = SplitOpera[i++];
		}catch(Exception a){
			P5 = "0";
		}

		try{
			P6 = SplitOpera[i++];
		}catch(Exception a){
			P6 = "0";
		}

		boolean removeItem = false;
		switch (Operacion){
		//BuffUsar "+IDBUFFER+" "+LVLBUFF+" "+ String.valueOf(Pagina) +" "+ String.valueOf(idCategoria) +" 0 0\"
			case "autobuff":
				if(!general.BUFFER_CON_KARMA){
					if(player.getKarma()>0){
						central.msgbox("Karma player cannot use me", player);
						strHTML = getMainWindowsBuffer(player);
						break;
					}
				}
				if(canDoIt(player)){
					setAutoBuff_To_Char(player);
				}
				strHTML = getMainWindowsBuffer(player);
				break;
			case "confirmDelete":
				deleteScheme(player);
			case "confirmCancel":
				strHTML = getMainWindowsBuffer(player);
				break;
			case "delScheme":
				strHTML = showDeleteSchemaWindows(player);
				break;
			case "useScheme":
				setBuffToPlayerOrSummon(player);
				strHTML = getMainWindowsBuffer(player);
				break;
			case "BuffSupr":
				removeBuffFromScheme(player,Integer.valueOf(P1));
				strHTML = getCategoriaWindows(player, Integer.valueOf(P4), getNameFromSchemeID(player, Integer.valueOf(P4)) , Integer.valueOf(P3));
				break;
			case "BuffSave":
				boolean SalvarBuff = true;
				boolean IsDance = ID_CAT_DANCES.contains(BUFF_CATEGORIA.get(Integer.valueOf(P1)));
				if(IsDance){
					if(getCantidadBuff_SchemaPlayer(player, false) >= Config.DANCES_MAX_AMOUNT ){
						SalvarBuff = false;
						central.msgbox("You can not add more Dances", player);
					}
				}else{
					int cantidadBuff = Config.BUFFS_MAX_AMOUNT;
					if(player.getSkillLevel(1405)>0){
						cantidadBuff = cantidadBuff + player.getSkillLevel(1405);
					}
					if(getCantidadBuff_SchemaPlayer(player, true) >= cantidadBuff ){
						SalvarBuff = false;
						central.msgbox("You can not add more Buff", player);
					}
				}
				if(canDoIt(player)){
					if(SalvarBuff){
						createNewBuffSche(player,Integer.valueOf(P1),Integer.valueOf(P2));
					}
				}
				strHTML = getCategoriaWindows(player, Integer.valueOf(P4), getNameFromSchemeID(player, Integer.valueOf(P4)) , Integer.valueOf(P3));
				break;
			case "BuffUsar":
				if(!general.BUFFER_CON_KARMA){
					if(player.getKarma()>0){
						central.msgbox("Karma player cannot use me", player);
						strHTML = getMainWindowsBuffer(player);
						break;
					}
				}
				boolean bufear=false;
				if(BUFF_CHAR!=null){
					if(BUFF_CHAR.containsKey(player)){
						if(!BUFF_CHAR.get(player)){
							if(player.getSummon()==null){
								central.msgbox("You dont have summon", player);
								BUFF_CHAR.put(player, true);
								strHTML = getMainWindowsBuffer(player);
								break;
							}
						}
					}else{
						BUFF_CHAR.put(player, true);
					}
				}else{
					BUFF_CHAR.put(player, true);
				}
				if(!general.BUFF_GRATIS){
					if(opera.haveItem(player, general.BUFFER_ID_ITEM, getCobrarBuff( Integer.valueOf(P1) ))){
						opera.removeItem(general.BUFFER_ID_ITEM, getCobrarBuff( Integer.valueOf(P1) ), player);
						bufear =true;
					}
				}else{
					bufear = true;
				}
				if(canDoIt(player)){
					if(bufear){
						int idBuff = Integer.valueOf(P1);
						int BuffLevel = Integer.valueOf(P2);
						setBuffUse(player, idBuff, BuffLevel);

					}
				}
				strHTML = getCategoriaWindows(player, Integer.valueOf(P4), getNameFromSchemeID(player, Integer.valueOf(P4)) , Integer.valueOf(P3));
				break;
			case "selectSch":
				setSelectScheme(player, P1);
				strHTML = getMainWindowsBuffer(player);
				break;
			case "CreatSh":
				Vector<String> Schemas = SchemePlayer(player);
				if(Schemas.size()>=general.BUFFER_SCHEMA_X_PLAYER){
					central.msgbox("You cannot create more Schemme's", player);
					strHTML = getMainWindowsBuffer(player);
					break;
				}

				if(!existNameScheme(player, P1)){
					createNewSchema(player,P1);
				}else{
					central.msgbox("Schema name already exist", player);
				}
				strHTML = getMainWindowsBuffer(player);
			case "char":
				setBuffObjeto(true, player);
				strHTML = getMainWindowsBuffer(player);
				break;
			case "pet":
				setBuffObjeto(false, player);
				strHTML = getMainWindowsBuffer(player);
				break;
			case "home":
				strHTML = getMainWindowsBuffer(player);
				break;
			case "loadCate":
				//ID Categori, Nombre, Página
				//%IDCATE% %NOMBRE%
				strHTML = getCategoriaWindows(player, Integer.valueOf(P1),P2,Integer.valueOf(P3));
				break;
			case "heal":
				
				if(!general.BUFFER_CON_KARMA){
					if(player.getKarma()>0){
						central.msgbox("Karma player cannot use me", player);
						strHTML = getMainWindowsBuffer(player);
						break;
					}
				}
				
				if(!general.BUFFER_HEAL_CAN_FLAG){
					if(player.getPvpFlag()>0){
						central.msgbox("You cannot heal on flag mode", player);
						strHTML = getMainWindowsBuffer(player);
						break;						
					}
				}
				
				if(!general.BUFFER_HEAL_CAN_IN_COMBAT){
					if(player.isInCombat()){
						central.msgbox("You cannot heal on combat mode", player);
						strHTML = getMainWindowsBuffer(player);
						break;						
					}
				}				
				
				if(!general.BUFF_GRATIS){
					if(general.BUFFER_HEAL_VALOR>0){
						if(!opera.haveItem(player,general.BUFFER_ID_ITEM,general.BUFFER_HEAL_VALOR)){
							strHTML = getMainWindowsBuffer(player);
							break;
						}
						removeItem=true;
					}
				}else{
					removeItem = false;
				}
				if(canDoIt(player)){
					if(BUFF_CHAR.get(player)){
						central.healAll(player, false);
						if(removeItem) {
							opera.removeItem(general.BUFFER_ID_ITEM, general.BUFFER_HEAL_VALOR, player);
						}
					}else{
						if(player.getSummon()==null){
							central.msgbox(msg.BUFFERCHAR_YOU_CAN_NOT_HEAL_YOU_PET, player);
							BUFF_CHAR.put(player, true);
						}else{
							central.healAll(player, true);
							if(removeItem) {
								opera.removeItem(general.BUFFER_ID_ITEM, general.BUFFER_HEAL_VALOR, player);
							}
						}
					}
				}
				strHTML = getMainWindowsBuffer(player);
				break;
			case "remoBuff":
				if(!general.BUFFER_CON_KARMA){
					if(player.getKarma()>0){
						central.msgbox("Karma player cannot use me", player);
						strHTML = getMainWindowsBuffer(player);
						break;
					}
				}
				if(!general.BUFF_GRATIS){
					if(general.BUFFER_REMOVE_BUFF_VALOR>0){
						if(!opera.haveItem(player,general.BUFFER_ID_ITEM,general.BUFFER_REMOVE_BUFF_VALOR)){
							strHTML = getMainWindowsBuffer(player);
							break;
						}
						removeItem=true;
					}
				}else{
					removeItem = false;
				}
				if(canDoIt(player)){
					if(BUFF_CHAR.get(player)){
						player.stopAllEffects();
						if(removeItem) {
							opera.removeItem(general.BUFFER_ID_ITEM, general.BUFFER_REMOVE_BUFF_VALOR, player);
						}
					}else{
						if(player.getSummon()==null){
							central.msgbox(msg.BUFFERCHAR_YOU_CAN_NOT_HEAL_YOU_PET, player);
							BUFF_CHAR.put(player, true);
						}else{
							player.getSummon().stopAllEffects();
							if(removeItem) {
								opera.removeItem(general.BUFFER_ID_ITEM, general.BUFFER_REMOVE_BUFF_VALOR, player);
							}
						}
					}
				}
				strHTML = getMainWindowsBuffer(player);
				break;
		}


		return strHTML;

	}

	private static void setBuffObjeto(boolean BuffChar,L2PcInstance player){
		BUFF_CHAR.put(player, BuffChar);
	}

	public static String getMainWindowsBuffer(L2PcInstance player){
		
		boolean isCharOption = true;
		String btnPet = "<button value=\"Pet Options\" action=\"bypass -h ZeuSNPC zeus_buffer_npc char 0 0 0 0 0 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnChar = "<button value=\"Char Options\" action=\"bypass -h ZeuSNPC zeus_buffer_npc pet 0 0 0 0 0 0\" width=130 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnCategoria = "<button value=\"%NOMBRE%\" action=\"bypass -h ZeuSNPC zeus_buffer_npc loadCate %IDCATE% %NOMBRE% 0 0 0 0\" width=120 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnHeal = "<button value=\"Heal\" action=\"bypass -h ZeuSNPC zeus_buffer_npc heal 0 0 0 0 0 0\" width=120 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnRemoveBuff = "<button value=\"Remove Buff\" action=\"bypass -h ZeuSNPC zeus_buffer_npc remoBuff 0 0 0 0 0 0\" width=120 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String btnNewScheme = "<button value=\"Create\" action=\"bypass -h ZeuSNPC zeus_buffer_npc CreatSh $txtName 0 0 0 0 0\" width=65 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnSelectScheme = "<button value=\"Selec\" action=\"bypass -h ZeuSNPC zeus_buffer_npc selectSch $cmbScheme 0 0 0 0 0\" width=50 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		if(BUFF_CHAR!=null){
			if(BUFF_CHAR.containsKey(player)){
				isCharOption = BUFF_CHAR.get(player);
			}else{
				BUFF_CHAR.put(player, true);
			}
		}
		String HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		HTML += central.LineaDivisora(1) + central.headFormat(TITULO) + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat( ( isCharOption ? btnChar : btnPet ) ) + central.LineaDivisora(1);
		String TablaGrilla = "<table width=275><tr>";
		
		if(general.BUFFER_SCHEME_SYSTEM){
			String txtNuevoSchema = "<edit var=\"txtName\" width=120>";
			String btnUsarScheme = "<button value=\"Use Sch\" action=\"bypass -h ZeuSNPC zeus_buffer_npc useScheme 0 0 0 0 0 0\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			String btnBorrarScheme = "<button value=\"Delete Sch.\" action=\"bypass -h ZeuSNPC zeus_buffer_npc delScheme 0 0 0 0 0 0\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			String cmbSeleccion = "<combobox width=120 var=cmbScheme list=%LIST% >";
			String CantidadBuff = "<br1>You Scheme have <font color=FF8000>" + String.valueOf(getCantidadBuff_SchemaPlayer(player, true)) + "</font> Buff and <font color=FF8000>" + String.valueOf(getCantidadBuff_SchemaPlayer(player, false)) + "</font> Dances";
			HTML += central.LineaDivisora(1) + central.headFormat("Scheme System<br1>Selected: <font color=FF8000>" +( getSchemePlayer(player).length()>0? getSchemePlayer(player) + "<font><br1>" + CantidadBuff: "NONE" ),"LEVEL") + central.LineaDivisora(1);
			Vector<String> Schemas = SchemePlayer(player);
			String Lista ="";
			if(Schemas.size()>0){
				Lista = getSchemePlayer(player);
				for(String Nom : Schemas){
					if(!getSchemePlayer(player).equals(Nom)){
						if(Lista.length()>0){
							Lista+=";";
						}
						Lista += Nom;
					}
				}
			}
			String Temporal = "";
			if(Lista.length()>0){
				Temporal += "<table width=275>";
				Temporal +="<tr><td width=50 align=CENTER>Scheme</td><td width=120 align=CENTER>"+ cmbSeleccion.replace("%LIST%", Lista) +"</td><td width=105 align=CENTER>" +btnSelectScheme+"</td></tr>" +
						"<tr><td width=50></td><td width=120>"+ (getSchemePlayer(player).length()>0?btnUsarScheme:"") +"</td><td width=105>"+ (getSchemePlayer(player).length()>0?btnBorrarScheme:"") +"</td></tr>"+
						"</table><br1>";
			}
			Temporal += central.headFormat("New Scheme","LEVEL");
			Temporal +="<table width=275>";
			Temporal +="<tr><td width=50 align=CENTER>Name</td><td width=120 align=CENTER>"+ txtNuevoSchema + "</td><td width=105 align=CENTER>"+btnNewScheme+"</td></tr>";
			Temporal +="</table><br>";
			HTML += central.LineaDivisora(3) + central.headFormat(Temporal);
		}		
		
		
		if(general.BUFFER_SINGLE_BUFF_CHOICE){
			int Pasos = 0;
			for(String Cates : BuffCategorias){
				if((Pasos%2)==0){
					Pasos = 0;
					TablaGrilla += "</tr><tr>";
				}
				TablaGrilla += "<td width=140 align=CENTER>"+ btnCategoria.replace("%NOMBRE%", Cates.split("_")[0]).replace("%IDCATE%", Cates.split("_")[1]) +"</td>";
				Pasos++;
				if((Pasos%2)==0){
					TablaGrilla += "<tr>";
				}
			}
			if(TablaGrilla.endsWith("<tr>")){
				TablaGrilla += "</tr>";
			}
			TablaGrilla += "</table>";
			HTML += central.LineaDivisora(3) +  central.LineaDivisora(1) + central.headFormat("Buff","LEVEL") + central.LineaDivisora(1);
			HTML += central.headFormat(TablaGrilla) + central.LineaDivisora(1) + central.LineaDivisora(3);
		}

		if(general.BUFFER_HEAL || general.BUFFER_REMOVE_BUFF){
			String opcionesVarias ="";
			HTML += central.LineaDivisora(1) + central.headFormat("Option's","LEVEL") + central.LineaDivisora(1);
			opcionesVarias += "<table with=280><tr>";
			if(general.BUFFER_HEAL && general.BUFFER_REMOVE_BUFF){
				opcionesVarias += "<td width=140 align=CENTER>"+ btnHeal +"</td><td width=140 align=CENTER>"+ btnRemoveBuff +"</td>";
			}else if(general.BUFFER_HEAL){
				opcionesVarias += "<td width=280 align=CENTER>"+ btnHeal +"</td>";
			}else if(general.BUFFER_REMOVE_BUFF){
				opcionesVarias += "<td width=280 align=CENTER>"+ btnRemoveBuff +"</td>";
			}
			opcionesVarias += "</tr></table>";
			HTML += central.LineaDivisora(3) + central.headFormat(opcionesVarias) + central.LineaDivisora(3);
		}

		if(general.BUFFER_AUTOBUFF){
			HTML += central.LineaDivisora(1) + central.headFormat("Auto Buff System") + central.LineaDivisora(1);
			String btnAutoBuff = "<button value=\"Auto Buff\" action=\"bypass -h ZeuSNPC zeus_buffer_npc autobuff 0 0 0 0 0 0\" width=120 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			HTML += central.LineaDivisora(3) + central.headFormat(btnAutoBuff) + central.LineaDivisora(3);
		}
		
		if(!general.BUFFER_SCHEME_SYSTEM && !general.BUFFER_SINGLE_BUFF_CHOICE){
			if(general.BUFFCHAR_ACT){
				HTML += central.LineaDivisora(1) + central.headFormat("For all buff and Scheme use <br1> <font color=FF8000>.zeus_buffer</font>") + central.LineaDivisora(1);
			}
		}

		HTML += central.BotonGOBACKZEUS() + central.getPieHTML() + "</body></html>";
		return HTML;
	}

	private static boolean existNameScheme(L2PcInstance player, String NombreBuscar){
		Vector<String> VectorNombre = SchemePlayer(player);
		for(String Nom : VectorNombre){
			if (Nom.equals(NombreBuscar)){
				return true;
			}
		}
		return false;
	}

	private static int getCantidadBuff_SchemaPlayer(L2PcInstance player,boolean countBuff){
		int Contador = 0;
		if(CHAR_BUFFER_SCH!=null){
			if(CHAR_BUFFER_SCH.containsKey(player)){
				String NombreSchema = getSchemePlayer(player);
				if(NombreSchema.length()>0){
					int idSchema = getIDFromSchemeName(player, NombreSchema);
					HashMap<Integer, HashMap<String, Integer>> BuffDelSchema = CHAR_BUFFER_SCH.get(player).get(idSchema).get(NombreSchema);
					if(BuffDelSchema.size()>0){
						Iterator itr = BuffDelSchema.entrySet().iterator();
						while(itr.hasNext()){
							Map.Entry Entrada = (Map.Entry)itr.next();
							int idBuffer = (int)Entrada.getKey();
							if(countBuff){
								if(!ID_CAT_DANCES.contains(BUFF_CATEGORIA.get(idBuffer))){
									Contador++;
								}
							}else{
								if(ID_CAT_DANCES.contains(BUFF_CATEGORIA.get(idBuffer))){
									Contador++;
								}
							}
						}
					}
				}
			}
		}
		return Contador;
	}

	private static void setBuffToPlayerOrSummon(L2PcInstance player){
		if(!BUFF_CHAR.get(player)){
			if(player.getSummon()==null){
				central.msgbox("You dont have summon to buff", player);
				return;
			}
		}
		int CantidadNecesaria = getCostSchemeUser(player);
		if(!opera.haveItem(player, general.BUFFER_ID_ITEM, CantidadNecesaria)){
			return;
		}

		String nameScheme = getSchemePlayer(player);
		int idScheme = getIDFromSchemeName(player, getSchemePlayer(player));
		HashMap<Integer, HashMap<String, Integer>> buffesDelScheme = CHAR_BUFFER_SCH.get(player).get(idScheme).get(nameScheme);
		if(buffesDelScheme.size()>0){
			Iterator itr = buffesDelScheme.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry Entrada = (Map.Entry)itr.next();
				int idBuff = (int)Entrada.getKey();
				int leveBuff = buffesDelScheme.get(idBuff).get(TipoBuff.BUFF_LEVEL.name());
				setBuffUse(player,idBuff,leveBuff);
			}
		}

		opera.removeItem(general.BUFFER_ID_ITEM, CantidadNecesaria, player);


	}


	private static int getCostSchemeUser(L2PcInstance player){
		int Retorno = 0;
		String nameScheme = getSchemePlayer(player);
		int idScheme = getIDFromSchemeName(player, getSchemePlayer(player));
		HashMap<Integer, HashMap<String, Integer>> buffesDelScheme = CHAR_BUFFER_SCH.get(player).get(idScheme).get(nameScheme);
		if(buffesDelScheme.size()>0){
			Iterator itr = buffesDelScheme.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry Entrada = (Map.Entry)itr.next();
				int idBuff = (int)Entrada.getKey();
				Retorno += getCobrarBuff(idBuff);
			}
		}
		return Retorno;
	}

	private static void setAutoBuff_To_Char(L2PcInstance player){
		//DESACTIVADO=0;Mago=1;fighter=2;Shield=3;Todos=4;

		if(!BUFF_CHAR.get(player)){
			if(player.getSummon()==null){
				central.msgbox("You dont have summon.", player);
				BUFF_CHAR.put(player, true);
				return;
			}
		}



		boolean isMago = player.isMageClass();
		Vector<Integer> BUFF_SELECCIONAR = new Vector<Integer>();

		HashMap<Integer,HashMap<String,Integer>> BUFF_A_BUFFEAR = new HashMap<Integer,HashMap<String,Integer>>();

		BUFF_SELECCIONAR.add(isMago ? 1 : 2);
		int isManoIzquierda = player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LHAND);
		if(isManoIzquierda != 0){
			String TipoItem = central.getTipoITEMbyID(isManoIzquierda);
			if(String.valueOf(TipoItem).equals("Shield")) {
				BUFF_SELECCIONAR.add(3);
			}
		}

		BUFF_SELECCIONAR.add(4);


		int MontoCobrar = 0;

		Iterator itr = BuffList.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	int idCategoria = (int)Entrada.getKey();
	    	if(BuffList.get(idCategoria).size()>0){
	    		Iterator BuffList_ID = BuffList.get(idCategoria).entrySet().iterator();
	    		while(BuffList_ID.hasNext()){
	    			Map.Entry EntradaIDBUFF = (Map.Entry)BuffList_ID.next();
	    			int IDBUFF = (int)EntradaIDBUFF.getKey();
	    			if(BuffList.get(idCategoria).containsKey(IDBUFF)){
	    				if(BuffList.get(idCategoria).get(IDBUFF).size()>0){
			    			int IDTipoCharBuffID = Integer.valueOf(BuffList.get(idCategoria).get(IDBUFF).get(TipoBuff.FOR_CLASS.name()));
			    			int LVLBUFF = Integer.valueOf(BuffList.get(idCategoria).get(IDBUFF).get(TipoBuff.BUFF_LEVEL.name()));
			    			boolean canUseBuff = BuffList.get(idCategoria).get(IDBUFF).get(TipoBuff.ACTIVO.name()).equals("1") ? true:false;
			    			if(BUFF_SELECCIONAR.contains(Integer.valueOf(IDTipoCharBuffID)) && canUseBuff){
			    				BUFF_A_BUFFEAR.put(IDBUFF, new HashMap<String,Integer>());
			    				BUFF_A_BUFFEAR.get(IDBUFF).put(TipoBuff.BUFF_ID.name(), IDBUFF);
			    				BUFF_A_BUFFEAR.get(IDBUFF).put(TipoBuff.BUFF_LEVEL.name(), LVLBUFF);
			    				MontoCobrar +=getCobrarBuff(IDBUFF);
			    			}
	    				}
	    			}
	    		}
	    	}
	    }
	    if(BUFF_A_BUFFEAR.size()>0){
	    	if(!general.BUFF_GRATIS){
			    if(!opera.haveItem(player, general.BUFFER_ID_ITEM, MontoCobrar)){
			    	return;
			    }
			    opera.removeItem(general.BUFFER_ID_ITEM, MontoCobrar, player);
	    	}
		    Iterator itr_BuffBuff = BUFF_A_BUFFEAR.entrySet().iterator();
		    while (itr_BuffBuff.hasNext()){
		    	Map.Entry inBuff = (Map.Entry)itr_BuffBuff.next();
		    	int IDBuff = (int)inBuff.getKey();
		    	int LVLBuff = BUFF_A_BUFFEAR.get(IDBuff).get(TipoBuff.BUFF_LEVEL.name());
		    	setBuffUse(player,IDBuff,LVLBuff);
		    }
	    }

	}

	private static int getCobrarBuff(int IdBuff){
		return getCobrarBuff(IdBuff,false);
	}


	private static int getCobrarBuff(int idBuff, boolean isCategoria){
		int idCategoria = 0;
		if(!isCategoria){
			idCategoria = BUFF_CATEGORIA.get(idBuff);
		}else{
			idCategoria = idBuff;
		}
		int Retorno = 10000;
		switch(idCategoria){
			case 0:
				Retorno = general.BUFFER_IMPROVED_VALOR;
				break;
			case 1:
				Retorno = general.BUFFER_BUFF_VALOR;
				break;
			case 2:
				Retorno = general.BUFFER_RESIST_VALOR;
				break;
			case 3:
				Retorno = general.BUFFER_PROHECY_VALOR;
				break;
			case 4:
				Retorno = general.BUFFER_CHANT_VALOR;
				break;
			case 5:
				Retorno = general.BUFFER_CUBIC_VALOR;
				break;
			case 6:
				Retorno = general.BUFFER_DANCE_VALOR;
				break;
			case 7:
				Retorno = general.BUFFER_SONG_VALOR;
				break;
			case 8:
				Retorno = general.BUFFER_SPECIAL_VALOR;
				break;
			case 9:
				Retorno = general.BUFFER_OTROS_VALOR;
				break;
		}
		return Retorno;
	}
	private static void setBuffUse(L2PcInstance cha, int idSkill, int skillLevel){
		if(BUFF_CHAR.get(cha)){
			SkillData.getInstance().getSkill(idSkill,skillLevel).applyEffects(cha,cha);
		}else{
			if(cha.getSummon() != null){
				SkillData.getInstance().getSkill(Integer.valueOf(idSkill),skillLevel).applyEffects(cha.getSummon(),cha.getSummon());
			}
		}
	}
}
