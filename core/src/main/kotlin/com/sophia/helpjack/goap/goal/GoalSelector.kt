package com.sophia.helpjack.goap.goal

import com.sophia.helpjack.model.World

class GoalSelector(private val goals: List<Goal>) {
    fun pickGoal(world: World): Goal? {
        return goals
            .filter { !it.isStateSatisfied(world) && it.getWeight(world) > 0 }
            .maxByOrNull { it.getWeight(world) }
    }
}
