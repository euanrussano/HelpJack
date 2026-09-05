package com.sophia.helpjack.task

import com.sophia.helpjack.action.Action
import com.sophia.helpjack.action.MoveByAction
import com.sophia.helpjack.agent.TaskListAgent
import com.sophia.helpjack.model.World
import kotlin.math.sign
import kotlin.random.Random

class MoveToNearestTreeWithWoodTask : Task{

    val rng = Random(999)
    override fun step(
        agent: TaskListAgent,
        world: World
    ): Boolean {
        val actorId = agent.actorId
        val (x, y) = world.selectActor(actorId).getPosition()

        if (world.findTreeAdjacentTo(x, y) != null) return true
        val tree = world.findNearestTreeTo(x, y, requiredWood = true)?: return true

        val dx = (tree.x - x).sign
        val dy = (tree.y - y).sign

        if (dx == 0 && dy == 0) return true // already there / degenerate case

        val bestActions = mutableListOf<Action>()

        if (dx != 0 && dy != 0) {
            val options = mutableListOf(
                MoveByAction(actorId, dx, 0),
                MoveByAction(actorId, 0, dy)
            )
            if (rng.nextBoolean()) options.reverse()
            bestActions.addAll(options)
        } else {
            bestActions.add(MoveByAction(actorId, dx, dy))
        }

        val possibleActions = world.getActions(actorId)
        if (possibleActions.isEmpty()) return true

        for (action in bestActions){
            if (action in possibleActions){
                agent.action = action
                return false
            }
        }
        agent.action = possibleActions.randomOrNull(rng)
        return false
    }

}
