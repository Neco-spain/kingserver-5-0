package king.server.gameserver.ai;

import java.util.List;

import king.server.Config;
import king.server.gameserver.ThreadPoolManager;
import king.server.gameserver.datatables.NpcWalkerRoutesData;
import king.server.gameserver.model.L2CharPosition;
import king.server.gameserver.model.L2NpcWalkerNode;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.instance.L2NpcWalkerInstance;
import king.server.gameserver.network.NpcStringId;

public class L2NpcWalkerAI extends L2CharacterAI implements Runnable
{
	private static final int DEFAULT_MOVE_DELAY = 0;
	
	private long _nextMoveTime;
	
	private boolean _walkingToNextPoint = false;
	
	/**
	 * home points for xyz
	 */
	int _homeX, _homeY, _homeZ;
	
	/**
	 * route of the current npc
	 */
	private List<L2NpcWalkerNode> _route;
	
	/**
	 * current node
	 */
	private int _currentPos;
	
	/**
	 * Constructor of L2CharacterAI.
	 * @param accessor The AI accessor of the L2Character
	 */
	public L2NpcWalkerAI(L2Character.AIAccessor accessor)
	{
		super(accessor);
		
		if (!Config.ALLOW_NPC_WALKERS)
		{
			return;
		}
		
		_route = NpcWalkerRoutesData.getInstance().getRouteForNpc(getActor().getNpcId());
		
		// Here we need 1 second initial delay cause getActor().hasAI() will return null...
		// Constructor of L2NpcWalkerAI is called faster then ai object is attached in L2NpcWalkerInstance
		if (_route != null)
		{
			ThreadPoolManager.getInstance().scheduleAiAtFixedRate(this, 1000, 1000);
		}
		else
		{
			_log.warning(getClass().getSimpleName() + ": Missing route data! Npc: " + _actor);
		}
	}
	
	@Override
	public void run()
	{
		onEvtThink();
	}
	
	@Override
	protected void onEvtThink()
	{
		if (!Config.ALLOW_NPC_WALKERS)
		{
			return;
		}
		
		if (isWalkingToNextPoint())
		{
			checkArrived();
			return;
		}
		
		if (_nextMoveTime < System.currentTimeMillis())
		{
			walkToLocation();
		}
	}
	
	/**
	 * If npc can't walk to it's target then just teleport to next point
	 * @param blocked_at_pos ignoring it
	 */
	@Override
	protected void onEvtArrivedBlocked(L2CharPosition blocked_at_pos)
	{
		_log.warning(getClass().getSimpleName() + ": " + getActor().getNpcId() + ": Blocked at rote position [" + _currentPos + "], coords: " + blocked_at_pos.x + ", " + blocked_at_pos.y + ", " + blocked_at_pos.z + ". Teleporting to next point");
		
		int destinationX = _route.get(_currentPos).getMoveX();
		int destinationY = _route.get(_currentPos).getMoveY();
		int destinationZ = _route.get(_currentPos).getMoveZ();
		
		getActor().teleToLocation(destinationX, destinationY, destinationZ, false);
		super.onEvtArrivedBlocked(blocked_at_pos);
	}
	
	private void checkArrived()
	{
		int destinationX = _route.get(_currentPos).getMoveX();
		int destinationY = _route.get(_currentPos).getMoveY();
		int destinationZ = _route.get(_currentPos).getMoveZ();
		
		if (getActor().isInsideRadius(destinationX, destinationY, destinationZ, 5, false, false))
		{
			NpcStringId npcString = _route.get(_currentPos).getNpcString();
			String chat = null;
			if (npcString == null)
			{
				chat = _route.get(_currentPos).getChatText();
			}
			
			if ((npcString != null) || ((chat != null) && !chat.isEmpty()))
			{
				getActor().broadcastChat(chat, npcString);
			}
			
			// time in millis
			long delay = _route.get(_currentPos).getDelay() * 1000;
			
			// sleeps between each move
			if (delay < 0)
			{
				delay = DEFAULT_MOVE_DELAY;
				if (Config.DEVELOPER)
				{
					_log.warning(getClass().getSimpleName() + ": Wrong Delay Set in Npc Walker Functions = " + delay + " secs, using default delay: " + DEFAULT_MOVE_DELAY + " secs instead.");
				}
			}
			
			_nextMoveTime = System.currentTimeMillis() + delay;
			setWalkingToNextPoint(false);
		}
	}
	
	private void walkToLocation()
	{
		if (_currentPos < (_route.size() - 1))
		{
			_currentPos++;
		}
		else
		{
			_currentPos = 0;
		}
		
		boolean moveType = _route.get(_currentPos).getRunning();
		
		/**
		 * false - walking true - Running
		 */
		if (moveType)
		{
			getActor().setRunning();
		}
		else
		{
			getActor().setWalking();
		}
		
		// now we define destination
		int destinationX = _route.get(_currentPos).getMoveX();
		int destinationY = _route.get(_currentPos).getMoveY();
		int destinationZ = _route.get(_currentPos).getMoveZ();
		
		// notify AI of MOVE_TO
		setWalkingToNextPoint(true);
		
		setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(destinationX, destinationY, destinationZ, 0));
	}
	
	@Override
	public L2NpcWalkerInstance getActor()
	{
		return (L2NpcWalkerInstance) super.getActor();
	}
	
	public int getHomeX()
	{
		return _homeX;
	}
	
	public int getHomeY()
	{
		return _homeY;
	}
	
	public int getHomeZ()
	{
		return _homeZ;
	}
	
	public void setHomeX(int homeX)
	{
		_homeX = homeX;
	}
	
	public void setHomeY(int homeY)
	{
		_homeY = homeY;
	}
	
	public void setHomeZ(int homeZ)
	{
		_homeZ = homeZ;
	}
	
	public boolean isWalkingToNextPoint()
	{
		return _walkingToNextPoint;
	}
	
	public void setWalkingToNextPoint(boolean value)
	{
		_walkingToNextPoint = value;
	}
}