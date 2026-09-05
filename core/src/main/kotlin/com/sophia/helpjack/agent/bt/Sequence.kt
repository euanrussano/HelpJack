package com.sophia.helpjack.agent.bt

import com.sophia.helpjack.agent.BehaviorTreeAgent
import com.sophia.helpjack.model.Status
import com.sophia.helpjack.model.World

class Sequence(
    private val children: List<BTNode>
): BTNode {
    override fun step(
        agent: BehaviorTreeAgent,
        world: World
    ): Status {
        for (node in children) {
            val status = node.step(agent, world)
            if (status != Status.SUCCESS) return status
        }
        return Status.SUCCESS
    }

}
