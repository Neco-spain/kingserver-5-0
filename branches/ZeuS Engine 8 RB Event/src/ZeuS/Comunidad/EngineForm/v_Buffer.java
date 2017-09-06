package ZeuS.Comunidad.EngineForm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.interfase.bufferChar;
import ZeuS.interfase.central;
import ZeuS.procedimientos.opera;

public class v_Buffer extends bufferChar {
	
	private static Logger _log = Logger.getLogger(v_Buffer.class.getName());
	
	//general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;0;0;0;0;0;0:"
	
	private static HashMap<Integer,Integer>CharPagina = new HashMap<Integer,Integer>();
	private static HashMap<Integer,Boolean>haveNext = new HashMap<Integer,Boolean>();
	
	private static String getTablabuffCategorias(boolean haveNext, L2PcInstance player, int CategoriaIN){
		
		int pagina = CharPagina.get(player.getObjectId());
		
		String ByPassHeal = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;Heal;"+ String.valueOf(CategoriaIN) +";1;0;0;0";
		String ByPassRemove = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;Remove;"+String.valueOf(CategoriaIN)+";1;0;0;0";
		
		String GrillaHealRemove = "<tr><td><table width=200><tr><td><button value=\"Heal\" width=110 height=24 action=\""+ ByPassHeal +"\" fore=\"L2UI_CT1.Button_DF_Calculator\" back=\"L2UI_CT1.Button_DF_Calculator\">"+
		"</td><td><button value=Cancel Buff width=110 height=24 action=\""+ ByPassRemove +"\" fore=\"L2UI_CT1.Button_DF_Calculator\" back=\"L2UI_CT1.Button_DF_Calculator\"></td></tr></table></td></tr>";
		
		String Retorno = "<table width=220 bgcolor=\"2F2E2E\">"+GrillaHealRemove+"<tr><td align=CENTER>";
		
		String ByPass = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;CargarBuffCat;%IDCAT%;1;0;0;0";
		
		String btnBuffSelecCate = "<button value=\"-> %NOMBRE% <-\"align=\"center\" width=200 height=32 action=\""+ByPass+"\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\" back=\"L2UI_CT1.OlympiadWnd_DF_Watch\">";
		
		String bypass_Anterior = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;CargarBuffCat;%IDCAT%;"+ (pagina - 1) +";0;0;0";
		String bypass_Siguente = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;CargarBuffCat;%IDCAT%;"+ (pagina + 1) +";0;0;0";
		
		String btnAnterior = pagina>1 ? "<button width=32 height=16 action=\""+ bypass_Anterior.replace("%IDCAT%", String.valueOf(CategoriaIN)) +"\" fore=\"L2UI_CT1.Button_DF_Left\" back=\"L2UI_CT1.Button_DF_Left\">" : "";
		String btnSiguente = haveNext ? "<button width=32 height=16 action=\"" + bypass_Siguente.replace("%IDCAT%", String.valueOf(CategoriaIN)) + "\" fore=\"L2UI_CT1.Button_DF_Right\" back=\"L2UI_CT1.Button_DF_Right\">" : "";
		
		
		String GrillaBoxControlBuff = "<tr><td><table width=220 background=\"L2UI_CT1.Windows_DF_TooltipBG\"><tr><td align=center><font color=\"F5D0A9\">Buff Page</font></td></tr><tr>"+
        "<td align=center><table width=220><tr><td width=73 align=LEFT>" + btnAnterior + "</td><td width=73 align=CENTER><font name=\"hs12\" color=\"FF9900\">"+ String.valueOf(pagina) +"</font></td>"+
         "<td width=73 align=RIGHT>"+btnSiguente+"</td></tr></table><br></td></tr></table></td></tr>";
		
		
		Iterator itr = general.BUFF_CHAR_DATA.get("CATE").entrySet().iterator();
		
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	HashMap<String,String> datos = general.BUFF_CHAR_DATA.get("CATE").get((int)Entrada.getKey()) ;
	    	//tempTabla += "<tr><td width=200 align=CENTER>"+ (btnBuffSelecCate.replace("%NOMBRE%", datos.get("NOMCATE")).replace("%ID%", datos.get("IDCATE"))) +"</td></tr>";
	    	Retorno += btnBuffSelecCate.replace("%NOMBRE%", datos.get("NOMCATE")).replace("%IDCAT%", datos.get("IDCATE"));
	    }

	    Retorno += "</td></tr>"+ GrillaBoxControlBuff +"</table>";
	    
	    return Retorno;
	}
	
	private static String getTablaBuff(L2PcInstance player, int idCategoria){
		
		Vector<String> VectorBuffSelect = new Vector<String>();
		
		int paginaActual = CharPagina.get(player.getObjectId());
		
		String retorno = "";
		String ByPass_Add = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;AddBuffToSch;"+ String.valueOf(idCategoria) +";"+ String.valueOf(paginaActual) +";%IDBUFF%;0;0";
		String ByPass_Remove = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;RemoveBuffToSch;"+ String.valueOf(idCategoria) +";"+ String.valueOf(paginaActual) +";%IDBUFF%;0;0";
		String ByPass_CastSelf = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;CastBuffSelf;"+ String.valueOf(idCategoria) +";"+ String.valueOf(paginaActual) +";%IDBUFF%;0;0";
		String ByPass_CastPet = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;CastBuffPet;"+ String.valueOf(idCategoria) +";"+ String.valueOf(paginaActual) +";%IDBUFF%;0;0";
		
		
		String ByPass_UseSchemeSelf = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;UseSchemeSelf;"+ String.valueOf(idCategoria) +";"+ String.valueOf(paginaActual) +";0;0;0";
		String ByPass_UseSchemePet = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;UseSchemePet;"+ String.valueOf(idCategoria) +";"+ String.valueOf(paginaActual) +";0;0;0";
		String ByPass_RemoveScheme = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;RemoveScheme;"+ String.valueOf(idCategoria) +";"+ String.valueOf(paginaActual) +";0;0;0";
		
		

		String btnAdd = "<button action=\""+ ByPass_Add +"\" width=32 height=32 back=\"L2UI_CH3.mapbutton_zoomin1\" fore=\"L2UI_CH3.mapbutton_zoomin1\">";
		String btnRemove = "<button action=\""+ ByPass_Remove +"\" width=32 height=32 back=\"L2UI_CH3.mapbutton_zoomout1\" fore=\"L2UI_CH3.mapbutton_zoomout1\">";
		String btnCastOnSelf = "<button value=\"Cast on Self\" action=\""+ ByPass_CastSelf +"\" width=80 height=23 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnCastOnPet = "<button value=\"Cast on Pet\" action=\""+ ByPass_CastPet +"\" width=80 height=23 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		
		
		String btnUseSchemeSelf = "<button value=\"Use Scheme on Me\" width=120 height=22 action=\""+ ByPass_UseSchemeSelf + "\" fore=\"L2UI_ct1.button_df\" back=\"L2UI_ct1.button_df\">";
		String btnUseSchemePet = "<button value=\"Use Scheme on Pet\" width=120 height=22 action=\""+ ByPass_UseSchemePet +"\" fore=\"L2UI_ct1.button_df\" back=\"L2UI_ct1.button_df\">";
		String btnRemoveScheme = "<button value=\"Remove Scheme\" width=120 height=22 action=\""+ ByPass_RemoveScheme + "\" fore=\"L2UI_ct1.button_df\" back=\"L2UI_ct1.button_df\">";
		

		String Grilla_Buff = "<table width=225 background=\"L2UI_CT1.Windows_DF_TooltipBG\"><tr><td width=32><img src=\"%ICONBUFF%\" width=32 height=32></td>"+
		"<td width=161><table><tr><td><font color=\"FFFF99\">%NOMBUFF%</font></td></tr><tr><td fixwidth=191>%DESCRIPBUFF%</td></tr></table></td>"+
        "<td width=32>%BTNBUFF%</td></tr><tr><td width=32></td><td width=151><table><tr><td>" + btnCastOnSelf + "</td><td>" + btnCastOnPet + "</td>"+
        "</tr></table><br></td><td width=32></td></tr></table>";
		
		String NomCategoria = getCategoriaBY_id(idCategoria);
		
		HashMap<Integer, HashMap<String, String>> BuffCategoria = general.BUFF_CHAR_DATA.get(NomCategoria);

		int CantidadBuff = getBuffCountFromSchemme(player);
		int CantidadDances = getBuffCountFromSchemme(player, true);
		
		String ByPassPrice = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.Buffer.name() + ";price;0;0;0;0;0";

		String GrillaCantidad = "<table width=500 background=\"L2UI_CT1.Windows_DF_TooltipBG\" height=25><tr><td width=45><table width=45><tr><td width=38>Price</td><td><button action=\""+ ByPassPrice +"\" width=16 height=16 back=\"L2UI_CT1.Chatwindow_DF_ItemInfoIcon\" fore=\"L2UI_CT1.Chatwindow_DF_ItemInfoIcon\"></td></tr></table>"+
	            "</td><td width=485 align=\"CENTER\"><font color=\"FF9900\">Scheme Capacity </font><font color=\"00FFFF\">%TOTALBUFF%</font> <font color=\"FF9900\">buffs and </font><font color=\"00FFFF\">%TOTALDANCE%</font> <font color=\"FF9900\">Dances & song</font>"+
	            "</td></tr><tr><td width=45></td><td width=485><table width=385><tr><td align=CENTER>"+btnUseSchemeSelf+"</td><td align=CENTER>"+
	            btnUseSchemePet+"</td><td align=CENTER>"+ btnRemoveScheme + "</td></tr></table></td></tr></table>";
		
		retorno += GrillaCantidad.replace("%TOTALBUFF%", String.valueOf( Config.BUFFS_MAX_AMOUNT - CantidadBuff)).replace("%TOTALDANCE%", String.valueOf( Config.DANCES_MAX_AMOUNT - CantidadDances)); 	
		
		if(BuffCategoria==null){
			return retorno;
		}else if(BuffCategoria.size()==0){
			return retorno;
		}
		
		
		

		boolean CanAddMoreBuff = true;
		boolean CanAddMoreDance = true;

		int BUFF_MAX_AMOUNT = Config.BUFFS_MAX_AMOUNT;

		if(player.getSkillLevel(1405)>0){
			BUFF_MAX_AMOUNT = BUFF_MAX_AMOUNT + player.getSkillLevel(1405);
		}

		if(CantidadBuff >= BUFF_MAX_AMOUNT){
			CanAddMoreBuff = false;
		}		
		
		
		
		Iterator itr = BuffCategoria.entrySet().iterator();
		boolean ModifScheme = general.BUFFCHAR_EDIT_SCHEME_ON_MAIN_WINDOWS_BUFFER;
		
		int contador = 1;
		
		
		while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	if(BuffCategoria.get(Entrada.getKey()).get("ACT").equals("1")){
	    		
			    	String IDBUFF_IN = BuffCategoria.get(Entrada.getKey()).get("ID");
			    	int IDLEVELBUFF = Integer.valueOf(BuffCategoria.get(Entrada.getKey()).get("LEVEL"));
			    	
					/*"ID","LEVEL", "SEC", "NOM", "DESCR", "ACT"*/
			    	
			    	
			    	String Icono = "Icon.skill"+ opera.getIconSkill(Integer.valueOf(IDBUFF_IN));
			    	String descrip =  BuffCategoria.get(Entrada.getKey()).get("DESCR");
			    	String NombreBuff = BuffCategoria.get(Entrada.getKey()).get("NOM");

			    	boolean canSave = canSaveBuff(player,Integer.valueOf(IDBUFF_IN));
			    	boolean canSupr =  canDeleteBuff(player,Integer.valueOf(IDBUFF_IN));
			    	boolean canAddMoreBuffDance = true;
			    	boolean isDance = true;

			    	if(isBuff(Integer.valueOf(IDBUFF_IN), IDLEVELBUFF)){
			    		isDance = false;
			    		if(!CanAddMoreBuff){
			    			canAddMoreBuffDance = false;
			    		}
			    	}else{
			    		isDance = true;
			    		if(!CanAddMoreDance){
			    			canAddMoreBuffDance = false;
			    		}
			    	}
			    	
			    	String BotonBuff = "";

			    	if(canSave && !canSupr){
			    		if((!isDance && CanAddMoreBuff) || (isDance && CanAddMoreDance)){
			    			BotonBuff = btnAdd.replace("%IDBUFF%", IDBUFF_IN);
			    		}
			    	}else if(canSupr){
			    		BotonBuff = btnRemove.replace("%IDBUFF%", IDBUFF_IN);
			    	}
			    	
			    	VectorBuffSelect.add(Grilla_Buff.replace("%IDBUFF%",IDBUFF_IN).replace("%ICONBUFF%", Icono).replace("%NOMBUFF%", NombreBuff).replace("%DESCRIPBUFF%", descrip).replace("%BTNBUFF%", BotonBuff) );
	    	}
	    }
		
		
		if(VectorBuffSelect!=null){
			
    		String GrillaCentralPortaBuff = "<table width=520 bgcolor=\"2F2E2E\">%BUFF%</table>";
    		
    		/*buff per page*/
    		int BuffPorpagina = 8;
    		/*buff per page*/

    		
    		int DesdeBuff = (paginaActual - 1) * BuffPorpagina;
    		int HastaBuff = paginaActual * BuffPorpagina;
    		
    		
    		int Contador1 = 0;
    		int Contador2 = 0;
    		
    		String Forma = "";
    		
    		int ContadorPaginas_Desde = 0;
    		int ContadorPaginas_Hasta = 0;
    		
    		boolean haveNextHere = false;
    		
    		for(String Buff : VectorBuffSelect){
    			if(!haveNextHere){
	    			if(ContadorPaginas_Desde >= DesdeBuff){
	    				if(ContadorPaginas_Hasta < BuffPorpagina){
			    			if(Contador1==0){
			    				Forma += "<tr><td align=CENTER width=520 fixwidth=520><table width=510><tr>";
			    			}
			    			Forma += "<td widthh=225 height=90>"+Buff+"</td>";
			    			Contador1++;
			    			Contador2++;
			    			if(Contador1==2){
			    				Contador1=0;
			    				Forma += "</tr></table></td></tr>";
			    			}
	    				}else if(ContadorPaginas_Hasta > BuffPorpagina){
	    					haveNextHere = true;
	    					haveNext.put(player.getObjectId(), true);
	    				}
	    				ContadorPaginas_Hasta++;
	    			}
	    			ContadorPaginas_Desde++;
    			}
    		}
    		
    		if((Contador2 % 2)!=0){
    			Forma += "<td widthh=255 height=90></td></tr></table></td></tr>";
    		}
    		
    		retorno += GrillaCentralPortaBuff.replace("%BUFF%", Forma);
    		
    	}
		
		
		return retorno;
	}
	

	
	private static String getTablaScheme(L2PcInstance player){
		String retorno = "";
		
		String NombreScheme = getSchemeSelectedName(player);
		
		String strByPassSelect = "bypass -h voice .zeus_cb_buff select $cmbScheme 0 0 0 0";
		
		String strByPassInsert = "bypass -h voice .zeus_cb_buff insert $txtNewScheme 0 0 0 0";
		
		retorno = "<img src=\"L2UI.SquareGray\" width=744 height=2>"+
				"<table width=\"745\" border=0 cellpadding=0 height=\"48\" bgcolor=\"2F2E2E\"><tr><td width=248 fixwidth=248>"+
                "<table width=248><tr><td>Select Scheme:</td></tr><tr><td><font color=FFFF00>"+ NombreScheme + "</font>"+
                "</td></tr></table></td><td width=248><table width=248><tr><td>Select Scheme to Use</td></tr><tr><td><table><tr>"+
                "<td><combobox width=110 var=\"cmbScheme\" list=\""+getSchemeFromPlayer(player)+ "\"></td><td>"+
                "<button value=\"Select\" action=\""+ strByPassSelect +"\" width=62 height=23 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>"+
                "</tr></table></td></tr></table></td><td width=248><table width=248><tr><td>New Scheme Name</td></tr><tr><td><table>"+
                "<tr><td><edit var=\"txtNewScheme\" width=90></td><td><button value=\"Create\" action=\""+strByPassInsert+"\" width=62 height=23 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"+
                "</td></tr></table></td></tr></table></td></tr></table><img src=\"L2UI.SquareGray\" width=744 height=2><br>";
		
		return retorno;
	}
	
	private static String getCentralHtml(L2PcInstance player, int idCategoria){
		return getCentralHtml(player,idCategoria,"","");
	}
	
	private static String getCentralHtml(L2PcInstance player, int idCategoria, String Params, String ByPassAnterior){
		
		/*String[] Eventos = params.split(";");
		String parm1 = Eventos[1];
		String parm2 = Eventos[2];
		String parm3 = Eventos[3];*/		
		
		String par = Engine.enumBypass.Buffer.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();

		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		/*
		if(Params.equals("FromMain")){
			retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,false,ByPassAnterior);
		}else{
			retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica);
		}*/
		
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
		retorno += getTablaScheme(player);

		haveNext.put(player.getObjectId(), false);
		
		String ParrillaBuff = getTablaBuff(player,idCategoria);
		
		retorno += "<table width=730 border=0 cellpadding=0 height=500><tr><td width=220>"+
				getTablabuffCategorias(haveNext.get(player.getObjectId()),player,Integer.valueOf(idCategoria)) +"</td><td width=510>"+
				ParrillaBuff +"</td></tr></table><br>";
		
		retorno += "</center></body></html>";
		return retorno;
	}
	
	public static void delegar_voice(L2PcInstance player, String params){
		//.zeus_cb_buff select $cmbScheme 0 0 0 0
		_log.warning(params);
		String[] Eventos = params.split(" ");
		String parm1 = Eventos[0];
		String parm2 = Eventos[1];
		String parm3 = Eventos[2];
		String parm4 = Eventos[3];
		String parm5 = Eventos[4];
		String parm6 = Eventos[5];
		if(parm1.equalsIgnoreCase("select")){
			SELECNAME.put(player.getObjectId(), parm2);
			cbManager.separateAndSend(getCentralHtml(player, 0),player);
		}else if(parm1.equalsIgnoreCase("insert")){
			if(saveNameSchem(player,parm2)){
				cbManager.separateAndSend(getCentralHtml(player, 0),player);
			}
		}		
		/*
		 *general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;AddBuffToSch;"+ String.valueOf(idCategoria) +";"+ String.valueOf(paginaActual) +";%IDBUFF%;0;0";
		 *general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;RemoveBuffToSch;"+ String.valueOf(idCategoria) +";"+ String.valueOf(paginaActual) +";%IDBUFF%;0;0";
		 *general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;CastBuffSef;"+ String.valueOf(idCategoria) +";"+ String.valueOf(paginaActual) +";%IDBUFF%;0;0";
		 *general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;CastBuffPet;"+ String.valueOf(idCategoria) +";"+ String.valueOf(paginaActual) +";%IDBUFF%;0;0";
		 */
		
		
		
	}
	
	private static void usarbuff(L2PcInstance player, boolean isChar, int IDBUFF, int IDCATEGORIA){
		int levelBuff = Integer.valueOf(general.BUFF_CHAR_DATA.get( getCategoriaBY_id(IDCATEGORIA)).get(IDBUFF).get("LEVEL"));
		if(general.BUFFCHAR_DONATION_SECCION_ACT && buffIsDona(IDBUFF)){
			if(!opera.haveItem(player, general.BUFFCHAR_DONATION_SECCION_COST)){
				central.msgbox(msg.BUFFVOICE_YOU_DONT_HAVE_THE_DONATION_ITEM_TO_USE_THIS_BUFF, player);
			}else{
				setBuff(player, IDBUFF, levelBuff);
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
				if(isChar){
					setBuff(player, IDBUFF, levelBuff,false);
				}else{
					if(player.getSummon()!=null){
						setBuff(player, IDBUFF, levelBuff,true);
					}else{
						central.msgbox(msg.BUFFERCHAR_YOU_CAN_NOT_BUFF_YOU_PET, player);
					}
				}
			}
		}		
	}
	
	public static String delegar(L2PcInstance player, String params){
		
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		
		String retorno = "";
		
		if(parm1.equals("0")){
			if(!general.BUFFCHAR_ACT){
				central.msgbox(msg.DISABLED_BY_ADMIN, player);
				return "";
			}
			CharPagina.put(player.getObjectId(), 0);
			retorno = getCentralHtml(player, 0);
		}else if(parm1.equals("CargarBuffCat")){
			CharPagina.put(player.getObjectId(), Integer.valueOf(parm3));
			retorno = getCentralHtml(player, Integer.valueOf(parm2));
		}else if(parm1.equals("AddBuffToSch")){
			setBuffToSchemme(player,Integer.valueOf(parm4),getCategoriaBY_id(Integer.valueOf(parm2)),false);
			retorno = getCentralHtml(player, Integer.valueOf(parm2));
		}else if(parm1.equals("RemoveBuffToSch")){
			setBuffToSchemme(player,Integer.valueOf(parm4),getCategoriaBY_id(Integer.valueOf(parm2)),true);
			retorno = getCentralHtml(player, Integer.valueOf(parm2));			
		}else if(parm1.equals("CastBuffSelf")){
			usarbuff(player,true,Integer.valueOf(parm4),Integer.valueOf(parm2));
			retorno = getCentralHtml(player, Integer.valueOf(parm2));
		}else if(parm1.equals("UseSchemeSelf")){
			if(opera.canUseCBFunction(player)){
				if(haveSchemeSelected(player)){
					setBuffSchemmeToPlayer(player,true,false);
				}
				retorno = getCentralHtml(player, Integer.valueOf(parm2));					
			}
		}else if(parm1.equals("UseSchemePet")){
			if(opera.canUseCBFunction(player)){
				if(haveSchemeSelected(player)){
					if(player.getSummon()!=null){
						setBuffSchemmeToPlayer(player,true,true);
					}else{
						central.msgbox(msg.BUFFERCHAR_YOU_CAN_NOT_BUFF_YOU_PET, player);
					}
				}
				retorno = getCentralHtml(player, Integer.valueOf(parm2));
			}
		}else if(parm1.equals("RemoveScheme")){
			if(opera.canUseCBFunction(player)){
				if(haveSchemeSelected(player)){
					deleteSche(player);
				}
				SELECNAME.remove(player.getObjectId());
				retorno = getCentralHtml(player, Integer.valueOf(parm2));
			}
		}else if(parm1.equals("Heal")){
			if(opera.canUseCBFunction(player)){
				boolean curar = true;
				if(!general.BUFFCHAR_HEAL_FOR_FREE){
					if(!opera.haveItem(player, general.BUFFCHAR_COST_HEAL)){
						curar = false;
					}else{
						opera.removeItem(general.BUFFCHAR_COST_HEAL, player);
					}
				}
				if(curar){
					if(player.isInCombat()){
						central.msgbox("You are in combat mode.", player);
					}else{
						central.healAll(player, false);
						if(player.getSummon()!=null){
							if(player.isInCombat() || player.getSummon().isInCombat()){
								central.msgbox("Pet are in combat mode.", player);
							}else{
								central.healAll(player, true);
							}
						}
					}
				}
			}
		}else if(parm1.equals("Remove")){
			if(opera.canUseCBFunction(player)){
				boolean cancelar = true;
				if(!general.BUFFCHAR_CANCEL_FOR_FREE){
					if(!opera.haveItem(player, general.BUFFCHAR_COST_CANCEL)){
						cancelar = false;
					}else{
						opera.removeItem(general.BUFFCHAR_COST_CANCEL, player);
					}
				}
				if(cancelar){
					player.stopAllEffects();
					if(player.getSummon()!=null){
						player.getSummon().stopAllEffects();
					}
				}
			}
		}else if(parm1.equals("FromMain")){
			if(!general.BUFFCHAR_ACT){
				central.msgbox(msg.DISABLED_BY_ADMIN, player);
				return "";
			}
			CharPagina.put(player.getObjectId(), 0);
			retorno = getCentralHtml(player, 0,parm1,"bypass " + general.COMMUNITY_BOARD_PART_EXEC);			
		}else if(parm1.equals("price")){
			
			HashMap<String,String> Precios = new HashMap<String, String>();
			
			if(!general.BUFFCHAR_FOR_FREE){
				Precios.put("Individual Buff", general.BUFFCHAR_COST_INDIVIDUAL);
				Precios.put("Scheme", general.BUFFCHAR_COST_USE);
				Precios.put("Heal", general.BUFFCHAR_COST_HEAL);
				Precios.put("Cancel Buff", general.BUFFCHAR_COST_CANCEL);
			}else{
				Precios.put("Individual Buff", "");
				Precios.put("Scheme", "");
				Precios.put("Heal", "");
				Precios.put("Cancel", "");
			}
			
			
			cbFormato.showItemRequestWindows(player, Precios, "Community Buff");
		}
		
		return retorno;
		
	}
	
}
