package com.sophia.helpjack.model

data class Jack(
    val x: Int,
    val y: Int,
    val wood: Int = 0,
    val isHidden: Boolean = false // in cabin
) {
    val maxWood = 100


    init {
        require(wood >= 0)
        require(wood <= maxWood)
    }

    fun hasWood(): Boolean {
        return wood > 0
    }

    fun increaseWood(amount: Int): Jack {
        if (amount < 0) throw IllegalArgumentException("Jack: Amount must be positive")
        if (this.wood >= maxWood) return this
        return copy(wood = (this.wood + amount).coerceAtMost(maxWood))
    }

    fun decreaseWood(amount: Int): Jack {
        if (amount < 0) throw IllegalArgumentException("Jack: Amount must be positive")
        if (this.wood < amount) throw IllegalArgumentException("Jack: Not enough wood")
        return copy(wood = this.wood - amount)
    }

}
