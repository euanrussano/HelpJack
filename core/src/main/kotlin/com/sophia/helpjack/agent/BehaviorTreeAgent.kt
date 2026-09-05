package com.sophia.helpjack.agent

import com.sophia.helpjack.action.Action
import com.sophia.helpjack.agent.bt.BTNode
import com.sophia.helpjack.model.World
import kotlin.random.Random

class BehaviorTreeAgent(
    override val actorId: Int,
    val node: BTNode
): Agent {
    val rng: Random = Random(999)
    var action: Action? = null

    override fun decide(
        world: World,
        delta: Float
    ): Action? {
        node.step(this, world)
        return action
    }


}
