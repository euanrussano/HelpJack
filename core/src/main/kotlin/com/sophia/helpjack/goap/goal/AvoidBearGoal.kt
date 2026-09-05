package com.sophia.helpjack.goap.goal

import com.sophia.helpjack.model.World

class AvoidBearGoal: Goal {
    override fun isStateSatisfied(world: World): Boolean {
        return world.distance2Between(world.bear, world.jack) > 3*3
    }

    override fun getWeight(world: World): Int {
        if (world.distance2Between(world.bear, world.jack) <= 3*3) return 100
        return 0
    }

    // Manhattan-ish estimate of moves needed to get distance² above 25.
    // Cheap and inadmissible-safe enough for a game AI (doesn't need to be perfectly admissible).
    override fun heuristic(world: World): Int {
        val d2 = world.distance2Between(world.bear, world.jack)
        val needed = 5*5 - d2
        return if (needed <= 0) 0 else needed
    }
}
