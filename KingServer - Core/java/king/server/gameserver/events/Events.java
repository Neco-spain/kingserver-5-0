package king.server.gameserver.events;

import king.server.gameserver.events.functions.Buffer;
import king.server.gameserver.events.functions.Scheduler;
import king.server.gameserver.events.functions.Vote;
import king.server.gameserver.events.io.Out;

public class Events
{
	public static void eventStart()
	{
		Config.getInstance();
		
		if (Config.getInstance().getBoolean(0, "voteEnabled"))
		{
			Vote.getInstance();
		}
		if (Config.getInstance().getBoolean(0, "schedulerEnabled"))
		{
			Scheduler.getInstance();
		}
		if (Config.getInstance().getBoolean(0, "eventBufferEnabled"))
		{
			Buffer.getInstance();
		}
		
		Out.registerHandlers();
	}
}