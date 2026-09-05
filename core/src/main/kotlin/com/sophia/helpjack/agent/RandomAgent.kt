package com.sophia.helpjack.agent

import com.sophia.helpjack.action.Action
import com.sophia.helpjack.model.World
import kotlin.random.Random


class RandomAgent(
    override val actorId: Int
): Agent {
    val rng = Random(999)

    override fun decide(world: World, delta: Float): Action? {
        // query world for possible actions
        val actions = world.getActions(actorId)
        // pick one at random if any
        return actions.randomOrNull(rng)
    }
}
