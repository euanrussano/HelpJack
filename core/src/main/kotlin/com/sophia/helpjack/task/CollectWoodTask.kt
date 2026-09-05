package com.sophia.helpjack.task

import com.sophia.helpjack.agent.TaskListAgent
import com.sophia.helpjack.model.World
import com.sophia.helpjack.model.component.WoodCarrier
import com.sophia.helpjack.model.component.hasWood

class CollectWoodTask(val amountToCollect: Int): Task {
    override fun step(
        agent: TaskListAgent,
        world: World
    ): Boolean {
        // make sure there is tree adjacent
        val actor = world.selectActor(agent.actorId)
        val (x, y) = actor.getPosition()
        val tree = world.findTreeAdjacentTo(x, y)?: return true

        val woodCarrier = actor.get<WoodCarrier>()?: return true
        if (woodCarrier.wood >= amountToCollect) return true

        return false

    }

}
