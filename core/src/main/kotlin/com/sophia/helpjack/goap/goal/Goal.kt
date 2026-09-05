package com.sophia.helpjack.goap.goal


import com.sophia.helpjack.model.World

interface Goal{
    fun isStateSatisfied(world: World): Boolean
    fun getWeight(world: World): Int
    fun heuristic(world: World): Int

}
