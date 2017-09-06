package ZeuS.server;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.pintar;
import ZeuS.procedimientos.opera;

import com.l2jserver.Config;
import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.skills.AbnormalVisualEffect;
import com.l2jserver.gameserver.model.zone.ZoneId;

public class PvPPk
{
  private static final String SELECT_TOP_PKPVP = "call sp_top_pvppk(?,?,?)";
  private static final String INSERT_LOG_FIGHT = "call sp_log_fight(?,?,?)";
  private static HashMap<Integer, Boolean> CAN_ATTACK = new HashMap<>();
  private static final Logger _log = Logger.getLogger(Config.class.getName());
  
  public static boolean _canScape(L2PcInstance activeChar, L2ItemInstance item)
  {
    if (!general._activated()) {
      return true;
    }
    if (activeChar != null)
    {
      if ((!general.ALLOW_BLESSED_ESCAPE_PVP) && (!activeChar.isGM())) {
        if ((activeChar.getPvpFlag() != 0) && (isBSOE(item)))
        {
          activeChar.sendMessage("Can not Use BSOE while you Flag");
          return false;
        }
      }
      if ((!general.CAN_USE_BSOE_PK) && (!activeChar.isGM()) && 
        (activeChar.getKarma() > 0) && (isBSOE(item)))
      {
        activeChar.sendMessage("Can not Use BSOE while you have Karma");
        return false;
      }
    }
    return true;
  }
  
  public static boolean isBSOE(L2ItemInstance item)
  {
    if ((opera.getIdItem(item) == 1538) || 
      (opera.getIdItem(item) == 3958) || 
      (opera.getIdItem(item) == 5858) || 
      (opera.getIdItem(item) == 5859) || 
      (opera.getIdItem(item) == 9156) || 
      (opera.getIdItem(item) == 10130) || 
      (opera.getIdItem(item) == 13258) || 
      (opera.getIdItem(item) == 13731) || 
      (opera.getIdItem(item) == 13732) || 
      (opera.getIdItem(item) == 13733) || 
      (opera.getIdItem(item) == 13734) || 
      (opera.getIdItem(item) == 13735) || 
      (opera.getIdItem(item) == 13736) || 
      (opera.getIdItem(item) == 13737) || 
      (opera.getIdItem(item) == 13738) || 
      (opera.getIdItem(item) == 13739) || 
      (opera.getIdItem(item) == 20583) || 
      (opera.getIdItem(item) == 21195)) {
      return true;
    }
    return false;
  }
 /* 
  private static int[] getInfoPvPBD(L2PcInstance player)
  {
    Connection con = null;
    CallableStatement statement = null;
    ResultSet rs = null;
    int Respuesta = 0;
    int NumeroPosi = 0;
    try
    {
      con = ConnectionFactory.getInstance().getConnection();
      statement = con.prepareCall("call sp_top_pvppk(?,?,?)");
      statement.setInt(1, 1);
      statement.setString(2, player.getName());
      statement.setInt(3, 5);
      rs = statement.executeQuery();
      while (rs.next())
      {
        Respuesta = rs.getInt("resp");
        NumeroPosi = rs.getInt("posi");
      }
      statement.close();
      try
      {
        if (con != null) {
          con.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    catch (Exception e)
    {
      _log.warning("Problemas Carga Top PK PVP" + e.getMessage());
    }
    finally
    {
      try
      {
        if (con != null) {
          con.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    tmp205_203[0] = Respuesta; int[] tmp210_205 = tmp205_203;tmp210_205[1] = NumeroPosi;int[] Retorno = tmp210_205;
    return Retorno;
  }
  */
  
  //--------------------------------------------------------------------------------------------------------------
  private static int[] getInfoPvPBD(L2PcInstance player) {
      Connection con = null;
      CallableStatement statement = null;
      ResultSet rs = null;
      int Respuesta = 0;
      int NumeroPosi = 0;

      try {
         con = ConnectionFactory.getInstance().getConnection();
         statement = con.prepareCall("call sp_top_pvppk(?,?,?)");
         statement.setInt(1, 1);
         statement.setString(2, player.getName());
         statement.setInt(3, 5);

         for(rs = statement.executeQuery(); rs.next(); NumeroPosi = rs.getInt("posi")) {
            Respuesta = rs.getInt("resp");
         }

         statement.close();
      } catch (Exception var15) {
         _log.warning("Problemas Carga Top PK PVP" + var15.getMessage());
      } finally {
         try {
            if(con != null) {
               con.close();
            }
         } catch (SQLException var14) {
            var14.printStackTrace();
         }

      }
      
      int[] Retorno = new int[]{Respuesta, NumeroPosi};
      return Retorno;
   }
  //--------------------------------------------------------------------------------------------------------------
  
  
  public static void Ver_TopPVP_PK(L2PcInstance player)
  {
    Ver_TopPVP_PK(player, true);
  }
  
  public static void Ver_TopPVP_PK(L2PcInstance player, boolean Anunciar)
  {
    if (!general._activated()) {
      return;
    }
    if ((!general.PVP_PK_GRAFICAL_EFFECT) && (!general.ANNOUCE_TOP_PPVPPK_ENTER)) {
      return;
    }
    int[] Obtener = getInfoPvPBD(player);
    if ((general.PVP_PK_GRAFICAL_EFFECT) && (opera.TOP_PVP_PK_EFFECT(player))) {
      _getGraficEffect(player, Obtener[0], Obtener[1]);
    }
    if ((Anunciar) && 
      (general.ANNOUCE_TOP_PPVPPK_ENTER) && (Obtener[1] == 1) && (opera.TOP_PVP_PK_ANNOU(player))) {
      _AnunciarEntradaTOP_PVP_PK(player.getName());
    }
  }
  
  private static void _getGraficEffect(L2PcInstance player, int Respuesta, int NumeroPosi)
  {
    if (!general._activated()) {
      return;
    }
    if ((!general.PVP_PK_GRAFICAL_EFFECT) || (player.isGM()) || (!opera.TOP_PVP_PK_EFFECT(player))) {
      return;
    }
    if (Respuesta == 1)
    {
      if (NumeroPosi == 1) {
        //player.startAbnormalVisualEffect(true, AbnormalVisualEffect.STIGMA_OF_SILEN.getMask());
      }
      if (NumeroPosi == 2) {
    	  //player.startAbnormalVisualEffect(true, AbnormalVisualEffect.STIGMA_OF_SILEN.getMask());
      }
      if (NumeroPosi == 3) {
    	  //player.startAbnormalEffect(AbnormalVisualEffect.NAVIT_ADVENT.getMask());
      }
      if (NumeroPosi == 4) {
    	  //player.startAbnormalEffect(AbnormalVisualEffect.DOT_WIND.getMask());
      }
      if (NumeroPosi == 5) {
    	  //player.startAbnormalEffect(AbnormalVisualEffect.DOT_FIRE_AREA.getMask());
      }
    }
  }
  
  private static void _AnunciarEntradaTOP_PVP_PK(String NombrePlayer)
  {
    if (!general._activated()) {
      return;
    }
    String Retorno = general.MENSAJE_ENTRADA_PJ_TOPPVPPK.replace("%CHAR_NAME%", NombrePlayer);
    opera.AnunciarTodos("Assassin", Retorno);
  }
  
  public static void _AnunciarEntradaKarma(L2PcInstance player)
  {
    if (!general._activated()) {
      return;
    }
    if ((!general.ANNOUCE_PJ_KARMA) || (player.getKarma() == 0)) {
      return;
    }
    if (player.getKarma() >= general.ANNOUCE_PJ_KARMA_CANTIDAD)
    {
      String Mensaje = general.MENSAJE_ENTRADA_PJ_KARMA;
      Mensaje = Mensaje.replace("%CHAR_NAME%", player.getName());
      Mensaje = Mensaje.replace("%KARMA%", String.valueOf(player.getKarma()));
      opera.AnunciarTodos("Karma PJ", Mensaje);
    }
  }
  
  public static boolean _protectionLvL_PVPPK(L2PcInstance PlayerAtacante, L2PcInstance PlayerTarget)
  {
    if (general.PVP_PK_PROTECTION_LVL == 0) {
      return false;
    }
    if ((PlayerTarget.isOnEvent()) || (PlayerTarget.isInsideZone(ZoneId.PVP))) {
      return false;
    }
    if (PlayerAtacante.isGM()) {
      return false;
    }
    if ((PlayerTarget.getPvpFlag() > 0) || (PlayerTarget.getKarma() > 0)) {
      return false;
    }
    if ((!(PlayerAtacante instanceof L2PcInstance)) && (!(PlayerTarget instanceof L2PcInstance))) {
      return false;
    }
    if ((PlayerTarget.isInOlympiadMode()) || (PlayerTarget.isInsideZone(ZoneId.SIEGE))) {
      return false;
    }
    if ((PlayerTarget.getClan() != null) && (PlayerAtacante.getClan() != null) && 
      (PlayerAtacante.getClan().isAtWarWith(PlayerTarget.getId()))) {
      return false;
    }
    if (isLifetimeProtected(PlayerAtacante, PlayerTarget)) {
      return true;
    }
    int Diferencia = PlayerAtacante.getLevel() - PlayerTarget.getLevel();
    if (Diferencia >= general.PVP_PK_PROTECTION_LVL) {
      return true;
    }
    return false;
  }
  
  private static boolean isLifetimeProtected(L2PcInstance PlayerAtacante, L2PcInstance PlayerTarget)
  {
    if (general.PVP_PK_PROTECTION_LIFETIME_MINUTES > 0)
    {
      long vidaTarget = PlayerTarget.getOnlineTime();
      long vidaParaPvP = general.PVP_PK_PROTECTION_LIFETIME_MINUTES * 60;
      if (vidaTarget < vidaParaPvP)
      {
        if (ZeuS.isInSameAlly(PlayerAtacante, PlayerTarget)) {
          return false;
        }
        if (ZeuS.isInSameChannel(PlayerAtacante, PlayerTarget)) {
          return false;
        }
        if (ZeuS.isInSameParty(PlayerAtacante, PlayerTarget)) {
          return false;
        }
        if (ZeuS.isInSameClan(PlayerAtacante, PlayerTarget)) {
          return false;
        }
        if (ZeuS.isInSameClanWar(PlayerAtacante, PlayerTarget)) {
          return false;
        }
        return true;
      }
      return false;
    }
    return false;
  }
  
  public static void _getColorPvP_PK(L2PcInstance player)
  {
    if (!general._activated()) {
      return;
    }
    if (((!general.PVP_COLOR_SYSTEM_ENABLED) && (!general.PK_COLOR_SYSTEM_ENABLED)) || (player.isGM())) {
      return;
    }
    if (general.PVP_COLOR_SYSTEM_ENABLED)
    {
      int _pvp = player.getPvpKills();
      int color = -1;
      if ((_pvp >= general.PVP_AMOUNT_1) && (_pvp <= general.PVP_AMOUNT_2)) {
        color = general.NAME_COLOR_FOR_PVP_AMOUNT_1;
      } else if ((_pvp >= general.PVP_AMOUNT_2) && (_pvp <= general.PVP_AMOUNT_3)) {
        color = general.NAME_COLOR_FOR_PVP_AMOUNT_2;
      } else if ((_pvp >= general.PVP_AMOUNT_3) && (_pvp <= general.PVP_AMOUNT_4)) {
        color = general.NAME_COLOR_FOR_PVP_AMOUNT_3;
      } else if ((_pvp >= general.PVP_AMOUNT_4) && (_pvp <= general.PVP_AMOUNT_5)) {
        color = general.NAME_COLOR_FOR_PVP_AMOUNT_4;
      } else if ((_pvp >= general.PVP_AMOUNT_5) && (_pvp <= general.PVP_AMOUNT_6)) {
        color = general.NAME_COLOR_FOR_PVP_AMOUNT_5;
      } else if ((_pvp >= general.PVP_AMOUNT_6) && (_pvp <= general.PVP_AMOUNT_7)) {
        color = general.NAME_COLOR_FOR_PVP_AMOUNT_6;
      } else if ((_pvp >= general.PVP_AMOUNT_7) && (_pvp <= general.PVP_AMOUNT_8)) {
        color = general.NAME_COLOR_FOR_PVP_AMOUNT_7;
      } else if ((_pvp >= general.PVP_AMOUNT_8) && (_pvp <= general.PVP_AMOUNT_9)) {
        color = general.NAME_COLOR_FOR_PVP_AMOUNT_8;
      } else if ((_pvp >= general.PVP_AMOUNT_9) && (_pvp <= general.PVP_AMOUNT_10)) {
        color = general.NAME_COLOR_FOR_PVP_AMOUNT_9;
      } else if (_pvp >= general.PVP_AMOUNT_10) {
        color = general.NAME_COLOR_FOR_PVP_AMOUNT_10;
      }
      if (color != -1) {
        pintar.ColorNombre(player, color);
      }
    }
    if (general.PK_COLOR_SYSTEM_ENABLED)
    {
      int _PK = player.getPkKills();
      int color = -1;
      if ((_PK >= general.PK_AMOUNT_1) && (_PK <= general.PK_AMOUNT_2)) {
        color = general.TITLE_COLOR_FOR_PK_AMOUNT_1;
      } else if ((_PK >= general.PK_AMOUNT_2) && (_PK <= general.PK_AMOUNT_3)) {
        color = general.TITLE_COLOR_FOR_PK_AMOUNT_2;
      } else if ((_PK >= general.PK_AMOUNT_3) && (_PK <= general.PK_AMOUNT_4)) {
        color = general.TITLE_COLOR_FOR_PK_AMOUNT_3;
      } else if ((_PK >= general.PK_AMOUNT_4) && (_PK <= general.PK_AMOUNT_5)) {
        color = general.TITLE_COLOR_FOR_PK_AMOUNT_4;
      } else if ((_PK >= general.PK_AMOUNT_5) && (_PK <= general.PK_AMOUNT_6)) {
        color = general.TITLE_COLOR_FOR_PK_AMOUNT_5;
      } else if ((_PK >= general.PK_AMOUNT_6) && (_PK <= general.PK_AMOUNT_7)) {
        color = general.TITLE_COLOR_FOR_PK_AMOUNT_6;
      } else if ((_PK >= general.PK_AMOUNT_7) && (_PK <= general.PK_AMOUNT_8)) {
        color = general.TITLE_COLOR_FOR_PK_AMOUNT_7;
      } else if ((_PK >= general.PK_AMOUNT_8) && (_PK <= general.PK_AMOUNT_9)) {
        color = general.TITLE_COLOR_FOR_PK_AMOUNT_8;
      } else if ((_PK >= general.PK_AMOUNT_9) && (_PK <= general.PK_AMOUNT_10)) {
        color = general.TITLE_COLOR_FOR_PK_AMOUNT_9;
      } else if (_PK >= general.PK_AMOUNT_10) {
        color = general.TITLE_COLOR_FOR_PK_AMOUNT_10;
      }
      if (color != -1) {
        pintar.ColorTitulo(player, color);
      }
    }
    player.broadcastUserInfo();
  }
  
  public static void _logPvPPK(L2PcInstance Player_Atacante, L2PcInstance Player_Asesinado, String Tipo)
  {
    if (!general._activated()) {
      return;
    }
    general.charPVPCOUNT.put(Player_Asesinado, Integer.valueOf(0));
    general.charPKCOUNT.put(Player_Asesinado, Integer.valueOf(0));
    if (!general.LOG_FIGHT_PVP_PK) {
      return;
    }
    int Atacante = Player_Atacante.getObjectId();
    int Asesinado = Player_Asesinado.getObjectId();
    
    Connection con = null;
    try
    {
      con = ConnectionFactory.getInstance().getConnection();
      CallableStatement statement = con.prepareCall("call sp_log_fight(?,?,?)");
      statement.setInt(1, Atacante);
      statement.setInt(2, Asesinado);
      statement.setString(3, Tipo);
      ResultSet rs = statement.executeQuery();
      rs.close();
      statement.close(); return;
    }
    catch (Exception e)
    {
      _log.warning("Problemas al Crear log Fight" + e.getMessage());
    }
    finally
    {
      try
      {
        if (con != null) {
          con.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
  }
}