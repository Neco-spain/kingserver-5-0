package king.server.gameserver.handler;

import king.server.gameserver.model.L2Object;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.skills.L2Skill;
import king.server.gameserver.model.skills.targets.L2TargetType;

public interface ITargetTypeHandler
{
	static final L2Object[] _emptyTargetList = new L2Object[0];
	public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target);
	public Enum<L2TargetType> getTargetType();
}