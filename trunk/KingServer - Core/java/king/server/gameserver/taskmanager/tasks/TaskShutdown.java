package king.server.gameserver.taskmanager.tasks;

import king.server.gameserver.Shutdown;
import king.server.gameserver.taskmanager.Task;
import king.server.gameserver.taskmanager.TaskManager.ExecutedTask;

public class TaskShutdown extends Task
{
	public static final String NAME = "shutdown";
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		Shutdown handler = new Shutdown(Integer.parseInt(task.getParams()[2]), false);
		handler.start();
	}
}