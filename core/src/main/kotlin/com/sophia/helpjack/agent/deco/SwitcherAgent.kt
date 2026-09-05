package com.sophia.helpjack.agent.deco

import com.sophia.helpjack.action.Action
import com.sophia.helpjack.agent.Agent
import com.sophia.helpjack.model.World
import kotlin.random.Random

class SwitcherAgent(
    override val actorId: Int,
    val agent1: Agent,
    val agent2: Agent,
    val probChange: Float,
): Agent {
    // will start with agent 1 behavior,
    // for every action will choose to change the current behavior with probChange

    val rng = Random(999)
    var currentAgent = agent1

    init {
        require(agent1.actorId == actorId)
        require(agent2.actorId == actorId)
        require(probChange in 0f..1f)
    }

    override fun decide(
        world: World,
        delta: Float
    ): Action? {
        val p = rng.nextFloat()
        if (p < probChange) {
            currentAgent = if (currentAgent == agent1) agent2 else agent1
        }
        return currentAgent.decide(world, delta)
    }

}
