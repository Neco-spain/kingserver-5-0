package handlers.voicedcommandhandlers;

import king.server.Config;
import king.server.gameserver.handler.IVoicedCommandHandler;
import king.server.gameserver.model.actor.instance.L2PcInstance;


public class Teleport implements IVoicedCommandHandler
{
 private static final String[] VOICED_COMMANDS =
 {
        "up10",
        "up20",
        "up30",
        "up40",
        "up50",
		"up60",
		"up70",
		"up80"
 };

 @Override
public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
 {
        {
        if (Config.ALLOW_TELEPORT_VOICECOMMAND)
        {
       
                        if (activeChar.isFestivalParticipant())
                        {
                        activeChar.sendMessage("Comando bloqueado voce esta em um Evento!");
                        return false;
                        }
                        else if(activeChar.isInJail())
                        {
                        activeChar.sendMessage("Comando bloqueado, voce esta na Jaula");
                        return false;
                        }
                        else if(activeChar.isDead())
                        {
                        activeChar.sendMessage("Comando bloqueado porque voce esta Morto.");
                        return false;
                        }
                        else if(activeChar.isInCombat())
                        {
                        activeChar.sendMessage("Comando bloqueado em pvp ou em modo de Combat.");
                        return false;
                        }
                        else if (activeChar.isInDuel())
                        {
                        activeChar.sendMessage("Comando bloqueado voce esta em um Duelo.");
                        return false;
                        }
                        else if (activeChar.isInOlympiadMode())
                        {
                        activeChar.sendMessage("Comando bloqueado em olympiada.");
                        return false;
                        }
                        else if (activeChar.getKarma() > 0)
                        {
                        activeChar.sendMessage("Ha.. ha.. ha, voce nao pode teleportar com Karma");
                        return false;
                        }
                        else if (activeChar.inObserverMode())
                        {
                        activeChar.sendMessage("Comando Bloqueado quando porque voce esta no modo de observador.");
                        return false;
                        }
                        else if (!activeChar.inObserverMode() && !activeChar.isInOlympiadMode() && !activeChar.isInDuel() && !activeChar.isInCombat() && !activeChar.isDead() && !activeChar.isInJail())
                        {
                       
                        if(command.startsWith("up10"))
                        {
                        activeChar.teleToLocation(-18896, 122236, -3229);
                        activeChar.sendMessage("Up do level 10 ao 20, Bem vindo a Gludio ");
                        return false;
                        }
                        else if(command.startsWith("up20"))
                        {
                        activeChar.teleToLocation(13137, 137838, -3095);
                        activeChar.sendMessage("Up do level 20 ao 30, Bem vindo a Dion ");
                        return false;
                        }
                        else if(command.startsWith("up30"))
                        {
                        activeChar.teleToLocation(102656, 101463, -3571);
                        activeChar.sendMessage("Up do level 30 ao 40, Bem vindo a Hardin’s Private Academy ");
                        return false;
                        }
                        else if(command.startsWith("up40"))
                        {
                        activeChar.teleToLocation(17722, 114358, -11673);
                        activeChar.sendMessage("Up do level 40 ao 50, Bem vindo a kruma tower primeiro andar ");
                        return false;
                        }              
                        else if(command.startsWith("up50"))
                        {
                        activeChar.teleToLocation(167312, 20289, -3330);
                        activeChar.sendMessage("Up do level 50 ao 60, Bem vindo a The Cemetary");
                        return false;
                        }
						else if(command.startsWith("up60"))
                        {
                        activeChar.teleToLocation(165172 , -47741, -3577);
                        activeChar.sendMessage("Up do level 60 ao 70, Bem vindo a Wall of Argos");
                        return false;
                        }
						else if(command.startsWith("up70"))
                        {
                        activeChar.teleToLocation(144516 , -69284, -3674);
                        activeChar.sendMessage("Up do level 70 ao 80, Bem vindo a Ketra Orc Outpost");
                        return false;
                        }
						else if(command.startsWith("up80"))
                        {
                        activeChar.teleToLocation(133988 , 114443, -3725);
                        activeChar.sendMessage("Up acima do level 80, Bem vindo a Antharas' Lair");
                        return false;
                        }
                        }
        }
        }
        return true;
 }

 @Override
public String[] getVoicedCommandList()
 {
        return VOICED_COMMANDS;
 }
}