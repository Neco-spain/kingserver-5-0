package king.server.gameserver.ai;

import java.util.ArrayList;

import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.L2Character.AIAccessor;

public final class L2SpecialSiegeGuardAI extends L2SiegeGuardAI
{
	private final ArrayList<Integer> _allied;
	
	/**
	 * @param accessor
	 */
	public L2SpecialSiegeGuardAI(AIAccessor accessor)
	{
		super(accessor);
		_allied = new ArrayList<>();
	}
	
	public ArrayList<Integer> getAlly()
	{
		return _allied;
	}
	
	@Override
	protected boolean autoAttackCondition(L2Character target)
	{
		if (_allied.contains(target.getObjectId()))
		{
			return false;
		}
		
		return super.autoAttackCondition(target);
	}
}