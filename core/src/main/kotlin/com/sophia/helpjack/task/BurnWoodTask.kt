package com.sophia.helpjack.task

import com.sophia.helpjack.agent.TaskListAgent
import com.sophia.helpjack.model.World
import com.sophia.helpjack.model.component.WoodCarrier
import com.sophia.helpjack.model.component.hasWood

class BurnWoodTask(): Task {
    override fun step(
        agent: TaskListAgent,
        world: World
    ): Boolean {
        // make sure there is campfire adjacent
        val actor = world.selectActor(agent.actorId)
        val (x, y) = actor.getPosition()
        val campfire = world.findFireAdjacentTo(x, y)?: return true

        val woodCarrier = actor.get<WoodCarrier>()?: return true
        if (woodCarrier.wood == 0) return true

        return false

    }

}
