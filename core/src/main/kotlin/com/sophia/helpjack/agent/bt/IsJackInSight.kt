package com.sophia.helpjack.agent.bt

import com.sophia.helpjack.agent.BehaviorTreeAgent
import com.sophia.helpjack.model.Status
import com.sophia.helpjack.model.World

class IsJackInSight: BTNode {
    override fun step(
        agent: BehaviorTreeAgent,
        world: World
    ): Status {
        if (world.actorSees(world.bear.id, world.jack.id)){
            return Status.SUCCESS
        }
        return Status.FAILURE
    }

}
