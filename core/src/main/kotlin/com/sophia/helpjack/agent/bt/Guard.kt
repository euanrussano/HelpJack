package com.sophia.helpjack.agent.bt

import com.sophia.helpjack.agent.BehaviorTreeAgent
import com.sophia.helpjack.model.Status
import com.sophia.helpjack.model.World

class Guard(
    val condition: BTNode,
    val action: BTNode
): BTNode {
    override fun step(
        agent: BehaviorTreeAgent,
        world: World
    ): Status {
        // Only let the child run if the condition is true
        val conditionStatus = condition.step(agent, world)
        if (conditionStatus == Status.SUCCESS){
            return action.step(agent, world)
        }
        return Status.FAILURE

    }

}
