package handlers.effecthandlers;

import king.server.gameserver.model.effects.L2Effect;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.stats.Env;
import king.server.gameserver.model.effects.EffectTemplate;
import king.server.gameserver.model.effects.L2EffectType;


public class RecoBonus extends L2Effect
{
    public RecoBonus(Env env, EffectTemplate template)
    {
        super(env, template);
    }

    /**
     *
     * @see king.server.gameserver.model.effects.L2Effect#getEffectType()
     */
    @Override
    public L2EffectType getEffectType()
    {
        return L2EffectType.BUFF;
    }

    /**
     *
     * @see king.server.gameserver.model.effects.L2Effect#onStart()
     */
    @Override
    public boolean onStart()
    {
        if (!(getEffected() instanceof L2PcInstance))
        {
            return false;
        }

        ((L2PcInstance) getEffected()).setRecomBonusType(1).setRecoBonusActive(true);
        return true;
    }

    /**
     *
     * @see king.server.gameserver.model.effects.L2Effect#onExit()
     */
    @Override
    public void onExit()
    {
        ((L2PcInstance) getEffected()).setRecomBonusType(0).setRecoBonusActive(false);
    }

    @Override
    protected boolean effectCanBeStolen()
    {
        return false;
    }

    /**
     *
     * @see king.server.gameserver.model.effects.L2Effect#onActionTime()
     */
    @Override
    public boolean onActionTime()
    {
        return false;
    }
}