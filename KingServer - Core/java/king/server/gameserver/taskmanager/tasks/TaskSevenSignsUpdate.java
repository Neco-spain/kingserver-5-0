package king.server.gameserver.taskmanager.tasks;

import king.server.gameserver.SevenSigns;
import king.server.gameserver.SevenSignsFestival;
import king.server.gameserver.taskmanager.Task;
import king.server.gameserver.taskmanager.TaskManager;
import king.server.gameserver.taskmanager.TaskManager.ExecutedTask;
import king.server.gameserver.taskmanager.TaskTypes;

public class TaskSevenSignsUpdate extends Task
{
	private static final String NAME = "seven_signs_update";
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		try
		{
			SevenSigns.getInstance().saveSevenSignsStatus();
			if (!SevenSigns.getInstance().isSealValidationPeriod())
			{
				SevenSignsFestival.getInstance().saveFestivalData(false);
			}
			_log.info("SevenSigns: Data updated successfully.");
		}
		catch (Exception e)
		{
			_log.warning(getClass().getSimpleName() + ": SevenSigns: Failed to save Seven Signs configuration: " + e.getMessage());
		}
	}
	
	@Override
	public void initializate()
	{
		super.initializate();
		TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_FIXED_SHEDULED, "1800000", "1800000", "");
	}
}