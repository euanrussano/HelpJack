package com.sophia.helpjack.agent.bt

import com.sophia.helpjack.action.MoveByAction
import com.sophia.helpjack.agent.BehaviorTreeAgent
import com.sophia.helpjack.model.Status
import com.sophia.helpjack.model.World

class Wander: BTNode {

    override fun step(
        agent: BehaviorTreeAgent,
        world: World
    ): Status {
        // query world for possible actions
        val actions = world.getActions(agent.actorId).filterIsInstance<MoveByAction>()
        // pick one at random if any
        agent.action = actions.randomOrNull(agent.rng)
        return Status.SUCCESS
    }

}
