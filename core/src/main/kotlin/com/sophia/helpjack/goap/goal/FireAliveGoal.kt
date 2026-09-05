package com.sophia.helpjack.goap.goal

import com.sophia.helpjack.model.World

class FireAliveGoal : Goal {
    override fun isStateSatisfied(world: World): Boolean {
        return world.campfire.wood > 10
    }

    override fun getWeight(world: World): Int {
        if (world.campfire.wood <= 10) return 90
        return 0
    }

    // Rough: wood still needed, plus distance to get adjacent to the fire once.
    override fun heuristic(world: World): Int {
        val woodNeeded = (21 - world.campfire.wood).coerceAtLeast(0)
        val (jx, jy) = world.jack
        val dist = kotlin.math.abs(jx - world.campfire.x) + kotlin.math.abs(jy - world.campfire.y)
        return woodNeeded + dist
    }
}
