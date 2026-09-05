package com.sophia.helpjack.model.component

data class WoodCarrier(val wood: Int = 0, val maxWood: Int = 100) : Component {
    init {
        require(wood >= 0) { "WoodCarrier: wood must be >= 0" }
        require(wood <= maxWood) { "WoodCarrier: wood must be <= maxWood" }
    }

    fun hasWood(): Boolean = wood > 0

    fun increase(amount: Int): WoodCarrier {
        require(amount >= 0) { "WoodCarrier: amount must be positive" }
        if (wood >= maxWood) return this
        return copy(wood = (wood + amount).coerceAtMost(maxWood))
    }

    fun decrease(amount: Int): WoodCarrier {
        require(amount >= 0) { "WoodCarrier: amount must be positive" }
        require(wood >= amount) { "WoodCarrier: not enough wood" }
        return copy(wood = wood - amount)
    }
}
