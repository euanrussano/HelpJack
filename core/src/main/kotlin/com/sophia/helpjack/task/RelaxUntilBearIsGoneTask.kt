package com.sophia.helpjack.task

import com.sophia.helpjack.action.Action
import com.sophia.helpjack.action.MoveByAction
import com.sophia.helpjack.agent.TaskListAgent
import com.sophia.helpjack.model.World
import kotlin.math.sign
import kotlin.random.Random

class RelaxUntilBearIsGoneTask : Task{

    val rng = Random(999)
    val distanceRequired = 5
    override fun step(
        agent: TaskListAgent,
        world: World
    ): Boolean {
        return world.distance2Between(world.jack, world.bear) >= distanceRequired*distanceRequired
    }

}
