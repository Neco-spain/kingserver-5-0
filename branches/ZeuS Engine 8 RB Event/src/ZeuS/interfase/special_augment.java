package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.GameServer;
import com.l2jserver.gameserver.model.L2Augmentation;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.network.clientpackets.AbstractRefinePacket;
import com.l2jserver.util.Rnd;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.procedimientos.opera;

public class special_augment {

	private static Connection conn = null;
	private static CallableStatement psqry = null;
	private static ResultSet rss = null;
	private static int[] AugmentValor = new int[4];

	private final static ArrayList<?>[] _blueSkills = new ArrayList[10];
	private final static ArrayList<?>[] _purpleSkills = new ArrayList[10];
	private final static ArrayList<?>[] _redSkills = new ArrayList[10];
	private final static ArrayList<?>[] _yellowSkills = new ArrayList[10];

	//private final TIntObjectHashMap<AugmentationSkill> _allSkills = new TIntObjectHashMap<AugmentationSkill>();

	private static final int STAT_BLOCKSIZE = 3640;
	private static final int STAT_SUBBLOCKSIZE = 91;

	private static final Logger _log = Logger.getLogger(GameServer.class.getName());
	private static String ItemToShow = null;

	private static boolean getValAugmentFreya(int idAugment){
		boolean retorno = false;
		try{
			String Consulta = "call sp_augment(3,?,-1,-1,-1)";
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(Consulta);
			psqry.setString(1,String.valueOf(idAugment));
			rss = psqry.executeQuery();
			try{
				if(rss.next()){
					AugmentValor = null;
					AugmentValor = new int[3];
					AugmentValor[0] = idAugment;
					AugmentValor[1] = rss.getInt("aug_skill");
					AugmentValor[2] = rss.getInt("skill_level");
					retorno =  true;
				}
			}catch(SQLException a){
				conn.close();
				_log.warning("S. AUGMENT-> Error al Cargar Información del Augment");
			}
			conn.close();
		}catch(SQLException a){
			_log.warning("S. AUGMENT-> Error al Cargar Información del Augment");
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}


		return retorno;
	}

	public static boolean CheckToRemoveItemAugment(L2PcInstance Player, String TipoAugment, boolean Remover){
		String RemoverItem = "";
		if(TipoAugment.equals("Chance")){
			RemoverItem  = general.AUGMENT_SPECIAL_PRICE_CHANCE;
		}else if(TipoAugment.equals("Passive")){
			RemoverItem = general.AUGMENT_SPECIAL_PRICE_PASSIVE;
		}else if(TipoAugment.equals("Active")){
			RemoverItem = general.AUGMENT_SPECIAL_PRICE_ACTIVE;
		}

		if(opera.haveItem(Player, RemoverItem)){
			if(Remover){
				opera.removeItem(RemoverItem, Player);
			}
		}else{
			return false;
		}
		return true;
	}

	public static String  getMainHtml(L2PcInstance player){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() +"</title><body>";
		String btnEntrar = "<button value=\"See the List\" action=\"bypass -h ZeuSNPC AUGMENTSP 0 0 0\" width=150 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Special Augment") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.LineaDivisora(1) + central.headFormat("Chance");
		MAIN_HTML += central.ItemNeedShowBox(general.AUGMENT_SPECIAL_PRICE_CHANCE ) + central.LineaDivisora(1);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat("Passive");
		MAIN_HTML += central.ItemNeedShowBox(general.AUGMENT_SPECIAL_PRICE_PASSIVE) + central.LineaDivisora(1);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat("Active");
		MAIN_HTML += central.ItemNeedShowBox(general.AUGMENT_SPECIAL_PRICE_ACTIVE) + central.LineaDivisora(1);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(btnEntrar) + central.LineaDivisora(1);
		MAIN_HTML += central.getPieHTML() + central.BotonGOBACKZEUS() + "</body></html>";
		return MAIN_HTML;
	}

	protected static boolean setAugment(L2PcInstance st, String IdAugment, boolean ShowMensaje){
		if(!general._activated()){
			return false;
		}
		try{
			L2ItemInstance itemSelecc = st.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
			if(itemSelecc.isAugmented()){
				central.msgbox(msg.AUGMENT_SPECIAL_REMOVE_AUGMENT,st);
				return false;
			}
			/*
			if(!getValAugmentFreya(Integer.valueOf(eventParam2))){
				central.msgbox("Error al Implementar Augment", st);
				_log.warning("S. AUGMENT-> Error al implementar el Augment");
				return false;
			}
			*/
			//Freya itemSelecc.setAugmentation(new L2Augmentation(AugmentValor[0], AugmentValor[1],AugmentValor[2]));
			itemSelecc.setAugmentation(new L2Augmentation(Integer.valueOf(IdAugment)));
			st.getInventory().unEquipItemInBodySlotAndRecord(128);
			st.broadcastUserInfo();
			return true;
		}catch (Exception e) {
			if(ShowMensaje){
				_log.warning("AUGMENT SPECIAL->" + e.getMessage());
			}
			return false;
		}
	}

	public static boolean setAugment(L2PcInstance st,String eventParam2){
		return setAugment(st, eventParam2, true);
	}


	public static String mainHTMLAumentoDetalleSkill(L2PcInstance st,int idSkill,String seccion, int pagina){
		if(!general._activated()){
			return "";
		}
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() +"</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Augment Info - Skill") + central.LineaDivisora(2);
		String BOTON_TEMP = "<center><button value=\"Back\" action=\"bypass -h ZeuSNPC " +general.QUEST_INFO+ " AUGMENTSP 0 "+seccion+" "+String.valueOf(pagina)+" \" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		String BOTON_ACCEPTAR ="";
		String Consulta = "call sp_augment(2,'',?,0,0)";
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(Consulta);
			psqry.setInt(1,idSkill);
			rss = psqry.executeQuery();
			try{
				while (rss.next()){
					MAIN_HTML +=central.LineaDivisora(1) + central.headFormat("Tipo: <font color=LEVEL>"+rss.getString(3)+"</font>","") + central.LineaDivisora(1);
					MAIN_HTML +=central.LineaDivisora(1) + central.headFormat("Skill Descrip: <font color=LEVEL>"+rss.getString(6)+"</font>","") + central.LineaDivisora(1);
					MAIN_HTML +=central.LineaDivisora(1) + central.headFormat("Skill Level: <font color=LEVEL>"+String.valueOf(rss.getInt(7))+"</font>","") + central.LineaDivisora(1);
					if(rss.getString(3).equals("Active")){
						ItemToShow = general.AUGMENT_SPECIAL_PRICE_ACTIVE;
					}else if(rss.getString(3).equals("Passive")){
						ItemToShow = general.AUGMENT_SPECIAL_PRICE_PASSIVE;
					}else if(rss.getString(3).equals("Chance")){
						ItemToShow = general.AUGMENT_SPECIAL_PRICE_CHANCE;
					}
					MAIN_HTML += central.ItemNeedShowBox(ItemToShow);
					BOTON_ACCEPTAR = "<center><button value=\"Obtener\" action=\"bypass -h ZeuSNPC " +general.QUEST_INFO+ " AUGMENTSP 3 "+String.valueOf(rss.getInt(2))+" "+ rss.getString(3) +" \" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
					MAIN_HTML +=central.LineaDivisora(1) + central.LineaDivisora(2) + central.headFormat(BOTON_ACCEPTAR,"") + central.LineaDivisora(1) +central.LineaDivisora(1);
				}
			}catch(SQLException a){
				conn.close();
			}
			conn.close();
		}catch(SQLException a){

		}
		try{
			conn.close();
		}catch(SQLException a ){

		}
		MAIN_HTML +=central.LineaDivisora(1) + central.headFormat(BOTON_TEMP,"") + central.LineaDivisora(1);

		/*_log.warning("ID AUGMENT PARA VER:_>IDAugment " + String.valueOf(GeneraColor(15869)));
		_log.warning("ID AUGMENT PARA VER: " + String.valueOf(GeneraColor(3165)));
*/
		MAIN_HTML += "<br1><br1><br1><br1><br1>"+central.BotonGOBACKZEUS()+"</center></body></html>";

		return MAIN_HTML;
	}



	private static String mainHTMLAumentoDetalle(L2PcInstance st,String seccion,int pagina){
		if(!general._activated()){
			return "";
		}
		String Retornar = "";
		String[] Colores = {"WHITE","LEVEL","FF8000"};
		String Consulta = "call sp_augment(1,?,-1,?,?)";
		int ContadorPintar = 0, Contador = 1;
		Connection conn = null;
		CallableStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(Consulta);
			psqry.setString(1,seccion);
			psqry.setInt(2,pagina * general.AUGMENT_SPECIAL_x_PAGINA);
			psqry.setInt(3, general.AUGMENT_SPECIAL_x_PAGINA);
			ResultSet rss = psqry.executeQuery();
			try{
				while (rss.next()){
					if (ContadorPintar >2) {
						ContadorPintar = 0;
					}
					String BOTON_TEMP = "<center><button value=\"Detail\" action=\"bypass -h ZeuSNPC " +general.QUEST_INFO+ " AUGMENTSP 1 "+seccion+"_"+ String.valueOf(pagina) +" "+ String.valueOf(rss.getInt(5))+" \" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
					Retornar+=central.LineaDivisora(1) + central.headFormat(rss.getString(4)+BOTON_TEMP,Colores[ContadorPintar]) + central.LineaDivisora(1);
					ContadorPintar++;
					Contador++;
				}
			}catch(SQLException a ){
				conn.close();
			}
		}catch(SQLException e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}


		String BOTON_SIGUENTE;
		if (Contador > general.AUGMENT_SPECIAL_x_PAGINA) {
			BOTON_SIGUENTE = "<button value=\"Next->\" action=\"bypass -h ZeuSNPC AUGMENTSP 0 "+seccion+" "+String.valueOf(pagina + 1)+ "\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		} else {
			BOTON_SIGUENTE = "";
		}

		String BOTON_ANTERIOR;
		if(pagina > 0) {
			BOTON_ANTERIOR = "<button value=\"<-Back\" action=\"bypass -h ZeuSNPC AUGMENTSP 0 "+seccion+" "+ String.valueOf(pagina - 1)+ "\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		} else {
			BOTON_ANTERIOR = "";
		}

		String BOTON_LISTADOS = "<center><table width=240 ><tr><td width=120 align=CENTER>"+BOTON_ANTERIOR+"</td><td width=120 align=CENTER>"+BOTON_SIGUENTE+"</td></tr></table></center>";


		Retornar += "<br>"+BOTON_LISTADOS;

		return Retornar;


	}

	public static String mainHtmlAugment_Special(L2PcInstance st,String seccion,int pagina){

		String[] AUGMENT_SPECIAL_ARRAY ={"CHANCE","PASSIVE","ACTIVE"};
		String STR_COMBO_AUGMENT = seccion.equals("0")?"":seccion ;

		for (String ComboAUGMENT : AUGMENT_SPECIAL_ARRAY){
			if(!seccion.equals(ComboAUGMENT)){
				if(STR_COMBO_AUGMENT.length() != 0) {
					STR_COMBO_AUGMENT += ";";
				}
				STR_COMBO_AUGMENT += ComboAUGMENT;
			}
		}

		String BTN_CAMBIAR = "<button value=\"Load\" action=\"bypass -h ZeuSNPC AUGMENTSP 0 $ebox 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String COMBO_BOX = "<combobox width=95 var=ebox list="+STR_COMBO_AUGMENT+"><br>";
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() +"</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Augment Item") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat("Select Ability"+COMBO_BOX+BTN_CAMBIAR,"LEVEL") + central.LineaDivisora(1);
		MAIN_HTML += mainHTMLAumentoDetalle(st,(seccion.length()==0) || seccion.equals("0") ? "CHANCE": seccion ,pagina);
		MAIN_HTML += "<br1>"+central.BotonGOBACKZEUS()+"</center></body></html>";
		return MAIN_HTML;
	}


	private static int GeneraColor(int idAugment){

		final int BASESTAT_STR = 16341;
		final int BASESTAT_MEN = 16344;
		// Note that stat12 stands for stat 1 AND 2 (same for stat34 ;p )
		// this is because a value can contain up to 2 stat modifications
		// (there are two short values packed in one integer value, meaning 4 stat modifications at max)
		// for more info take a look at getAugStatsById(...)

		// Note: lifeStoneGrade: (0 means low grade, 3 top grade)
		// First: determine whether we will add a skill/baseStatModifier or not
		// because this determine which color could be the result
		int stat12 = 0;
		int stat34 = idAugment;
		boolean generateGlow = true;

		// Second: decide which grade the augmentation result is going to have:
		// 0:yellow, 1:blue, 2:purple, 3:red
		// The chances used here are most likely custom,
		// whats known is: you cant have yellow with skill(or baseStatModifier)
		// noGrade stone can not have glow, mid only with skill, high has a chance(custom), top allways glow
		int resultColor = Rnd.get(0, 100);
		int lifeStoneGrade = AbstractRefinePacket.GRADE_TOP;
		if (stat34 == 0)
		{
			if (resultColor <= ((15 * lifeStoneGrade) + 40)) {
				resultColor = 1;
			} else {
				resultColor = 0;
			}
		}
		else
		{
			if ((resultColor <= ((10 * lifeStoneGrade) + 5)) || (stat34 != 0)) {
				resultColor = 3;
			} else if (resultColor <= ((10 * lifeStoneGrade) + 10)) {
				resultColor = 1;
			} else {
				resultColor = 2;
			}
		}

		// generate a skill if neccessary
		int lifeStoneLevel = 83;
		// Third: Calculate the subblock offset for the choosen color,
		// and the level of the lifeStone
		// from large number of retail augmentations:
		// no skill part
		// Id for stat12:
		// A:1-910 B:911-1820 C:1821-2730 D:2731-3640 E:3641-4550 F:4551-5460 G:5461-6370 H:6371-7280
		// Id for stat34(this defines the color):
		// I:7281-8190(yellow) K:8191-9100(blue) L:10921-11830(yellow) M:11831-12740(blue)
		// you can combine I-K with A-D and L-M with E-H
		// using C-D or G-H Id you will get a glow effect
		// there seems no correlation in which grade use which Id except for the glowing restriction
		// skill part
		// Id for stat12:
		// same for no skill part
		// A same as E, B same as F, C same as G, D same as H
		// A - no glow, no grade LS
		// B - weak glow, mid grade LS?
		// C - glow, high grade LS?
		// D - strong glow, top grade LS?

		// is neither a skill nor basestat used for stat34? then generate a normal stat
		int offset;
		if (stat34 == 0)
		{
			int temp = Rnd.get(2, 3);
			int colorOffset = (resultColor * (10 * STAT_SUBBLOCKSIZE)) + (temp * STAT_BLOCKSIZE) + 1;
			offset = (lifeStoneLevel * STAT_SUBBLOCKSIZE) + colorOffset;

			stat34 = Rnd.get(offset, (offset + STAT_SUBBLOCKSIZE) - 1);
			if (generateGlow && (lifeStoneGrade >= 2)) {
				offset = (lifeStoneLevel * STAT_SUBBLOCKSIZE) + ((temp - 2) * STAT_BLOCKSIZE) + (lifeStoneGrade
				* (10 * STAT_SUBBLOCKSIZE)) + 1;
			} else {
				offset = (lifeStoneLevel * STAT_SUBBLOCKSIZE) + ((temp - 2) * STAT_BLOCKSIZE) + (Rnd.get(0, 1)
				* (10 * STAT_SUBBLOCKSIZE)) + 1;
			}
		}
		else
		{
			if (!generateGlow) {
				offset = (lifeStoneLevel * STAT_SUBBLOCKSIZE) + (Rnd.get(0, 1) * STAT_BLOCKSIZE) + 1;
			} else {
				offset = (lifeStoneLevel * STAT_SUBBLOCKSIZE) + (Rnd.get(0, 1) * STAT_BLOCKSIZE) + (((lifeStoneGrade + resultColor) / 2)
				* (10 * STAT_SUBBLOCKSIZE)) + 1;
			}
		}
		stat12 = Rnd.get(offset, (offset + STAT_SUBBLOCKSIZE) - 1);

		return ((stat34 << 16) + stat12);
	}












}
