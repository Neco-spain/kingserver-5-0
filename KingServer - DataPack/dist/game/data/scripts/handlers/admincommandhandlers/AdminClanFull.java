package handlers.admincommandhandlers;

import king.server.Config;
import king.server.gameserver.handler.IAdminCommandHandler;
import king.server.gameserver.model.L2Object;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.serverpackets.EtcStatusUpdate;
import king.server.gameserver.network.serverpackets.SystemMessage;

public class AdminClanFull implements IAdminCommandHandler
{
	private static final String ADMIN_COMMANDS[] =
    {
		"admin_clanfull"
    };

	public AdminClanFull() {}

	@Override
    public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
		if (command.startsWith("admin_clanfull"))
		{
			try
			{
				adminAddClanSkill(activeChar);
				activeChar.sendMessage("Todas as skills de clan foram adicionadas.");
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Use: //clanfull");
			}
		}
		return true;
	}

	private void adminAddClanSkill(L2PcInstance activeChar)
	{
		L2Object target = activeChar.getTarget();
		if (target == null)
		{
			target = activeChar;
		}
		L2PcInstance player = null;
		if (target instanceof L2PcInstance)
		{
			player = (L2PcInstance) target;
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.INCORRECT_TARGET));
			return;
		}
		if (!player.isClanLeader())
		{
			player.sendPacket((new SystemMessage(SystemMessageId.S1_IS_NOT_A_CLAN_LEADER)).addString(player.getName()));
			return;
		}
		player.getClan().changeLevel(Config.CLAN_LEVEL);
		player.ClanSkills();
		player.sendPacket(new EtcStatusUpdate(activeChar));
	}

	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}