package ZeuS.ZeuS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import com.l2jserver.gameserver.GameServer;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.effects.L2EffectType;
import com.l2jserver.gameserver.model.skills.BuffInfo;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.interfase.central;

public class cancelbuff {

	   private final Logger _log = Logger.getLogger(GameServer.class.getName());


	   private void addcancelbuffs(L2PcInstance player, BuffInfo effect) {
	      if(!general.BUFF_REMOVED.containsKey(player)) {
	         general.BUFF_REMOVED.put(player, new ArrayList());
	      }

	      ((ArrayList)general.BUFF_REMOVED.get(player)).add(effect);
	   }

	   private void clearcancelbuffs(L2PcInstance player) {
	      if(general.BUFF_REMOVED.containsKey(player)) {
	         general.BUFF_REMOVED.remove(player);
	      }

	   }

	   public boolean canStealBuff(BuffInfo effect) {
	      return !general.RETURN_BUFF_NOT_STEALING.contains(Integer.valueOf(effect.getSkill().getId()));
	   }

	   public void setCancel(L2PcInstance player, BuffInfo buff) {
	      if(general.RETURN_BUFF || general.RETURN_BUFF_IN_OLY) {
	         boolean SegundosSeleccionados = false;
	         int SegundosSeleccionados1;
	         if(!player.isInOlympiadMode() && !player.isOlympiadStart()) {
	            SegundosSeleccionados1 = general.RETURN_BUFF_SECONDS_TO_RETURN;
	         } else {
	            if(!general.RETURN_BUFF_IN_OLY) {
	               return;
	            }

	            SegundosSeleccionados1 = general.RETURN_BUFF_IN_OLY_MINUTES_TO_RETURN;
	         }

	         if(!general.CANCEL_TASK.containsKey(player)) {
	            general.CANCEL_TASK.put(player, Boolean.valueOf(false));
	         }

	         this.addcancelbuffs(player, buff);
	         if(!((Boolean)general.CANCEL_TASK.get(player)).booleanValue()) {	            
	            ThreadPoolManager.getInstance().executeGeneral(new ReturnEffects(SegundosSeleccionados1 * 1000, player));
	            general.CANCEL_TASK.put(player, Boolean.valueOf(true));
	            central.msgbox_Lado(msg.CANCEL_BUFF_RETURNED_IN_$timeSeconds.replace("$timeSeconds", String.valueOf(SegundosSeleccionados1)), player, "Cancel Buff");
	         }

	      }
	   }

	   public static cancelbuff getInstance() {
	      return SingletonHolder._instance;
	   }

	    private static class SingletonHolder {
	        protected static final cancelbuff _instance = new cancelbuff();

	        private SingletonHolder() {
	        }
	    }

	    class ReturnEffects
	    implements Runnable {
	        private int _time;
	        ArrayList<BuffInfo> _list;
	        L2Character _player;

	        public ReturnEffects(int time, L2Character player) {
	            this._time = time;
	            this._list = (ArrayList)general.BUFF_REMOVED.get((Object)player);
	            this._player = player;
	        }
        

			@Override
	        public void run() {
	            if (this._time > 0) {
	                ThreadPoolManager.getInstance().scheduleGeneral((Runnable)this, 1000);
	                this._time -= 1000;
	            }
	            if (this._time == 0) {
	                if (this._list == null) {
	                    return;
	                }
	                if (this._list.size() == 0) {
	                    return;
	                }
	                if (((Boolean)general.CANCEL_TASK.get((Object)this._player)).booleanValue()) {
	                    for (BuffInfo e : this._list) {
	                        Skill skill = SkillData.getInstance().getSkill(e.getSkill().getId(), e.getSkill().getLevel());
	                        if (skill == null) continue;
	                        skill.applyEffects(this._player, this._player);
	                        SystemMessage sm = SystemMessage.getSystemMessage((SystemMessageId)SystemMessageId.YOU_FEEL_S1_EFFECT);
	                        sm.addSkillName(e.getSkill().getId());	                        
	                        _player.sendPacket(sm);
	                    }	                    
	                    general.CANCEL_TASK.put((L2PcInstance)_player, false);
	                    central.msgbox_Lado(msg.CANCEL_BUFF_YOUR_CANCEL_BUFF_HAVE_RETURNED, (L2PcInstance)this._player, "Cancel Buff");
	                }
	                cancelbuff.this.clearcancelbuffs((L2PcInstance)this._player);
	            }
	        }
	    }

	}

