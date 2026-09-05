package com.sophia.helpjack.agent

import com.sophia.helpjack.agent.bt.*
import com.sophia.helpjack.agent.deco.ReactionTimeAgent
import com.sophia.helpjack.task.BurnWoodTask
import com.sophia.helpjack.task.CollectWoodTask
import com.sophia.helpjack.task.MoveToNearestFireTask
import com.sophia.helpjack.task.MoveToNearestTreeWithWoodTask

object CommonAgents {
    fun humanAgent(actorId: Int) = HumanAgent(actorId = actorId)
    fun taskListAgent(actorId: Int) = ReactionTimeAgent(
        TaskListAgent(
            actorId = actorId,
            MoveToNearestTreeWithWoodTask(),
            CollectWoodTask(10),
            MoveToNearestFireTask(),
            BurnWoodTask()
        ),
        500
    )
    fun bearBehaviorTreeAgent(actorId: Int) = ReactionTimeAgent(
        BehaviorTreeAgent(
            actorId = actorId,
            Selector(
                Guard(
                    IsJackInSight(),
                    SeekActor(actorId)
                ),
                Wander()
            )
        ),
        500
    )


}
