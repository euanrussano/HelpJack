package com.sophia.helpjack.agent

import com.sophia.helpjack.action.Action
import com.sophia.helpjack.model.World

interface Agent {
    val actorId: Int

    fun decide(world: World, delta: Float): Action?
}
