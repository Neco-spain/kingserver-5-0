# Made by Edge
import sys
from king.server import Config
from king.server.gameserver.model.quest import State
from king.server.gameserver.model.quest import QuestState
from king.server.gameserver.model.quest.jython import QuestJython as JQuest

qn = "646_SignsOfRevolt"

class Quest (JQuest) :
 def __init__(self,id,name,descr):
    JQuest.__init__(self,id,name,descr)
    self.questItemIds = [8087]

 def onTalk (self,npc,player):
   st = player.getQuestState(qn)
   if st :
      # Quest is no longer available
      st.unset("cond")
      st.exitQuest(1);
   return "32016-00.htm"

QUEST       = Quest(646, qn, "Signs of Revolt")

QUEST.addStartNpc(32016)
QUEST.addTalkId(32016)
