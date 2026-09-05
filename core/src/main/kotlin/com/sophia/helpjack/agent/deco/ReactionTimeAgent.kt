package com.sophia.helpjack.agent.deco

import com.sophia.helpjack.action.Action
import com.sophia.helpjack.agent.Agent
import com.sophia.helpjack.model.World

/**
 * Decorator Agent that only takes action after a certain amount of time has passed
 */
class ReactionTimeAgent(
    val agent: Agent,
    val reactionTime: Int // in ms
): Agent {
    override val actorId: Int = agent.actorId

    var timer = 0
    override fun decide(world: World, delta: Float): Action? {
        timer += (delta * 1000).toInt()
        if (timer < reactionTime) return null
        timer = 0

        return agent.decide(world, delta)
    }
}
