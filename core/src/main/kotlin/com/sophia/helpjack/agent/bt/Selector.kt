package com.sophia.helpjack.agent.bt

import com.sophia.helpjack.agent.BehaviorTreeAgent
import com.sophia.helpjack.model.Status
import com.sophia.helpjack.model.World

class Selector(
    private vararg val children: BTNode
): BTNode {
    override fun step(
        agent: BehaviorTreeAgent,
        world: World
    ): Status {
        for (node in children) {
            val status = node.step(agent, world)
            if (status != Status.FAILURE) return status
        }
        return Status.FAILURE
    }

}
