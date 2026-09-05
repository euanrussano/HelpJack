package com.sophia.helpjack.agent

import com.sophia.helpjack.action.Action
import com.sophia.helpjack.model.World

class DoNothingAgent(override val actorId: Int): Agent {
    override fun decide(
        world: World,
        delta: Float
    ): Action? {
        return null
    }

}
