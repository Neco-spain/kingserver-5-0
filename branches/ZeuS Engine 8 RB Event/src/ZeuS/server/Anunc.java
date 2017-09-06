package ZeuS.server;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.central;
import ZeuS.procedimientos.opera;

import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2RaidBossInstance;

public class Anunc
{
  private static final Logger _log = Logger.getLogger(Anunc.class.getName());
  
  public static void AnunciarRaidBoss(L2RaidBossInstance raidboss)
  {
    if ((!general.ANNOUCE_RAID_BOS_STATUS) || (!general._activated())) {
      return;
    }
    String Mensaje = general.RAID_ANNOUCEMENT_LIFE;
    Mensaje = Mensaje.replace("%RAID_NAME%", raidboss.getName());
    opera.AnunciarTodos(general.RAID_ANNOUCEMENT_ID_ANNOUCEMENT, "RaidBoss:", Mensaje);
  }
  
  public static void AnunciarRaidBoss(L2RaidBossInstance raidboss, long respawnTime)
  {
    if ((!general.ANNOUCE_RAID_BOS_STATUS) || (!general._activated())) {
      return;
    }
    Calendar time = Calendar.getInstance();
    time.setTimeInMillis(respawnTime);
    String RespawnHora = time.getTime().toString();
    String Mensaje = general.RAID_ANNOUCEMENT_DIED;
    Mensaje = Mensaje.replace("%RAID_NAME%", raidboss.getName());
    Mensaje = Mensaje.replace("%DATE%", RespawnHora);
    opera.AnunciarTodos(general.RAID_ANNOUCEMENT_ID_ANNOUCEMENT, "RaidBoss:", Mensaje);
  }
  
  public static boolean _anunciarOponente(int Segundos)
  {
    int Encontro = Arrays.binarySearch(general.OLY_SECOND_SHOW_OPPONET, Segundos);
    if (Encontro >= 0) {
      return true;
    }
    return false;
  }
  
  public static boolean _AnunciarEnchant(int Enchant)
  {
    if (!general._activated())
    {
      if ((Enchant == 3) || (Enchant == 7)) {
        return true;
      }
      return false;
    }
    int Encontrado = Arrays.binarySearch(general.ENCHANT_ANNOUCEMENT, Enchant);
    if (Encontrado >= 0) {
      return true;
    }
    return false;
  }
  
  public static void _AnunciarCiclosPvP_PK(L2PcInstance player, boolean _pvp)
  {
    if ((!general.MENSAJE_PVP_PK_CICLOS) || (player.isGM()) || (!general._activated())) {
      return;
    }
    try
    {
      L2Object targetPlayer = player.getTarget();
      if ((targetPlayer != null) && 
        ((targetPlayer instanceof L2PcInstance)))
      {
        if ((((Integer)general.charPKCOUNT.get(targetPlayer)).intValue() == 0) || (((Integer)general.charPVPCOUNT.get(targetPlayer)).intValue() == 0)) {
          central.msgbox("PvP/Pk points have been reset", (L2PcInstance)targetPlayer);
        }
        general.charPKCOUNT.put((L2PcInstance)targetPlayer, Integer.valueOf(0));
        general.charPVPCOUNT.put((L2PcInstance)targetPlayer, Integer.valueOf(0));
      }
    }
    catch (Exception a) {}
    if (_pvp)
    {
      if (!general.charPVPCOUNT.containsKey(player)) {
        general.charPVPCOUNT.put(player, Integer.valueOf(1));
      } else {
        general.charPVPCOUNT.put(player, Integer.valueOf(((Integer)general.charPVPCOUNT.get(player)).intValue() + 1));
      }
    }
    else if (!general.charPKCOUNT.containsKey(player)) {
      general.charPKCOUNT.put(player, Integer.valueOf(1));
    } else {
      general.charPKCOUNT.put(player, Integer.valueOf(((Integer)general.charPKCOUNT.get(player)).intValue() + 1));
    }
    String Anunciar = "";
    if (_pvp)
    {
      if (((Integer)general.charPVPCOUNT.get(player)).intValue() == general.CANTIDAD_CICLO_MENSAJE_PVP_1) {
        Anunciar = general.CICLO_MENSAJE_PVP_1;
      }
      if (((Integer)general.charPVPCOUNT.get(player)).intValue() == general.CANTIDAD_CICLO_MENSAJE_PVP_2) {
        Anunciar = general.CICLO_MENSAJE_PVP_2;
      }
      if (((Integer)general.charPVPCOUNT.get(player)).intValue() == general.CANTIDAD_CICLO_MENSAJE_PVP_3) {
        Anunciar = general.CICLO_MENSAJE_PVP_3;
      }
      if (((Integer)general.charPVPCOUNT.get(player)).intValue() == general.CANTIDAD_CICLO_MENSAJE_PVP_4) {
        Anunciar = general.CICLO_MENSAJE_PVP_4;
      }
      if (((Integer)general.charPVPCOUNT.get(player)).intValue() == general.CANTIDAD_CICLO_MENSAJE_PVP_5) {
        Anunciar = general.CICLO_MENSAJE_PVP_5;
      }
      if (Anunciar.length() > 0)
      {
        Anunciar = Anunciar.replace("%CHAR_NAME%", player.getName()).replace("%CANT%", String.valueOf(general.charPVPCOUNT.get(player)));
        opera.AnunciarTodos("PVP Manager", Anunciar);
      }
    }
    else
    {
      if (((Integer)general.charPKCOUNT.get(player)).intValue() == general.CANTIDAD_CICLO_MENSAJE_PK_1) {
        Anunciar = general.CICLO_MENSAJE_PK_1;
      }
      if (((Integer)general.charPKCOUNT.get(player)).intValue() == general.CANTIDAD_CICLO_MENSAJE_PK_2) {
        Anunciar = general.CICLO_MENSAJE_PK_2;
      }
      if (((Integer)general.charPKCOUNT.get(player)).intValue() == general.CANTIDAD_CICLO_MENSAJE_PK_3) {
        Anunciar = general.CICLO_MENSAJE_PK_3;
      }
      if (((Integer)general.charPKCOUNT.get(player)).intValue() == general.CANTIDAD_CICLO_MENSAJE_PK_4) {
        Anunciar = general.CICLO_MENSAJE_PK_4;
      }
      if (((Integer)general.charPKCOUNT.get(player)).intValue() == general.CANTIDAD_CICLO_MENSAJE_PK_5) {
        Anunciar = general.CICLO_MENSAJE_PK_5;
      }
      if (Anunciar.length() > 0)
      {
        Anunciar = Anunciar.replace("%CHAR_NAME%", player.getName()).replace("%CANT%", String.valueOf(((Integer)general.charPKCOUNT.get(player)).intValue() + 1));
        opera.AnunciarTodos("PK Manager", Anunciar);
      }
    }
  }
  
  public static void Anunciar_All_Char_IP(L2PcInstance player, String Mensaje, String Titulo)
  {
    Anunciar_All_Char_IP(player, Mensaje, Titulo, true);
  }
  
  public static void Anunciar_All_Char_IP(L2PcInstance player, String Mensaje, String Titulo, boolean SendToMainPlayer)
  {
    Collection<L2PcInstance> players = opera.getAllPlayerOnWorld();
    if (player == null) {
      return;
    }
    String ipPlayer = ZeuS.getIp_Wan(player);
    String ipForPlayer = "";
    for (L2PcInstance chars : players) {
      if ((chars != null) && 
        (chars.isOnline())) {
        try
        {
          ipForPlayer = ZeuS.getIp_Wan(chars);
          if ((ipPlayer.equalsIgnoreCase(ipForPlayer)) && (
            (chars != player) || ((chars == player) && (SendToMainPlayer)))) {
            central.msgbox_Lado(Mensaje, chars, Titulo);
          }
        }
        catch (Exception a) {}
      }
    }
  }
}