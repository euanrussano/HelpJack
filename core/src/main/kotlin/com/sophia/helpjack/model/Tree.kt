package com.sophia.helpjack.model

data class Tree(
    val x: Int,
    val y: Int,
    val wood: Int = 500,
    val timer: Int = 0
) {
    val growthTime = 1000 // in ms
    val maxWood = 500

    init {
        require(growthTime >= World.MIN_TICK_TIME)
    }

    fun update(delta: Int): Tree {
        var newTimer = timer + delta
        if (newTimer < growthTime) return copy(timer = newTimer)

        newTimer = 0
        if (wood < maxWood){
            return copy(wood = wood + 1, timer = newTimer)
        }
        return copy(timer = newTimer)
    }

    fun chop(): ChopResult {
        if (wood == 0) return ChopResult(this, 0)
        val amountChopped = 1
        return ChopResult(copy(wood = wood - amountChopped), amountChopped)
    }

}
