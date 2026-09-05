package com.sophia.helpjack.action

import com.sophia.helpjack.model.World
import kotlin.math.abs

data class MoveByAction(
    val actorId: Int,
    val dx: Int,
    val dy: Int
): Action {

    init {
        require(abs(dx) + abs(dy) == 1) {
            "MoveByAction must move exactly one orthogonal step, but got ($dx, $dy)"
        }
    }

    override fun apply(world: World): World{
        return world.moveActorBy(actorId, dx, dy)
    }

}
