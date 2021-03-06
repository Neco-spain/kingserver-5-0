package king.server.gameserver.taskmanager;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;

import king.server.gameserver.ThreadPoolManager;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.instance.L2CubicInstance;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.network.serverpackets.AutoAttackStop;

public class AttackStanceTaskManager
{
	protected static final Logger _log = Logger.getLogger(AttackStanceTaskManager.class.getName());
	
	protected static final FastMap<L2Character, Long> _attackStanceTasks = new FastMap<>();
	
	/**
	 * Instantiates a new attack stance task manager.
	 */
	protected AttackStanceTaskManager()
	{
		_attackStanceTasks.shared();
		ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new FightModeScheduler(), 0, 1000);
	}
	
	/**
	 * Adds the attack stance task.
	 * @param actor the actor
	 */
	public void addAttackStanceTask(L2Character actor)
	{
		if ((actor != null) && actor.isPlayable())
		{
			final L2PcInstance player = actor.getActingPlayer();
			for (L2CubicInstance cubic : player.getCubics().values())
			{
				if (cubic.getId() != L2CubicInstance.LIFE_CUBIC)
				{
					cubic.doAction();
				}
			}
		}
		_attackStanceTasks.put(actor, System.currentTimeMillis());
	}
	
	/**
	 * Removes the attack stance task.
	 * @param actor the actor
	 */
	public void removeAttackStanceTask(L2Character actor)
	{
		if ((actor != null) && actor.isSummon())
		{
			actor = actor.getActingPlayer();
		}
		_attackStanceTasks.remove(actor);
	}
	
	/**
	 * Checks for attack stance task.<br>
	 * @param actor the actor
	 * @return {@code true} if the character has an attack stance task, {@code false} otherwise
	 */
	public boolean hasAttackStanceTask(L2Character actor)
	{
		if ((actor != null) && actor.isSummon())
		{
			actor = actor.getActingPlayer();
		}
		return _attackStanceTasks.containsKey(actor);
	}
	
	protected class FightModeScheduler implements Runnable
	{
		@Override
		public void run()
		{
			long current = System.currentTimeMillis();
			try
			{
				final Iterator<Entry<L2Character, Long>> iter = _attackStanceTasks.entrySet().iterator();
				Entry<L2Character, Long> e;
				L2Character actor;
				while (iter.hasNext())
				{
					e = iter.next();
					if ((current - e.getValue()) > 15000)
					{
						actor = e.getKey();
						if (actor != null)
						{
							actor.broadcastPacket(new AutoAttackStop(actor.getObjectId()));
							actor.getAI().setAutoAttacking(false);
							if (actor.isPlayer() && actor.hasSummon())
							{
								actor.getSummon().broadcastPacket(new AutoAttackStop(actor.getSummon().getObjectId()));
							}
						}
						iter.remove();
					}
				}
			}
			catch (Exception e)
			{
				// Unless caught here, players remain in attack positions.
				_log.log(Level.WARNING, "Error in FightModeScheduler: " + e.getMessage(), e);
			}
		}
	}
	
	/**
	 * Gets the single instance of AttackStanceTaskManager.
	 * @return single instance of AttackStanceTaskManager
	 */
	public static AttackStanceTaskManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final AttackStanceTaskManager _instance = new AttackStanceTaskManager();
	}
}