package com.sophia.helpjack.model

data class Campfire(
    val x: Int,
    val y: Int,
    val wood: Int = 10,
    val timer: Int = 0 // in ms
) {
    val consumptionTime = 2000 // in ms

    val isAlive = wood > 0

    init {
        require(consumptionTime >= World.MIN_TICK_TIME)
        require(wood >= 0)
    }

    fun update(delta: Int): Campfire {
        var newTimer = timer + delta
        if (newTimer < consumptionTime) return copy(timer = newTimer)

        newTimer = 0
        return copy(timer = newTimer, wood = (wood - 1).coerceAtLeast(0))
    }

    fun increaseWood(amount: Int): Campfire {
        return copy(wood = wood + amount)
    }

}
