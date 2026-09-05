package com.sophia.helpjack.model.component

data class Hideable(val isHidden: Boolean = false) : Component {
    fun hide(): Hideable = copy(isHidden = true)
    fun reveal(): Hideable = copy(isHidden = false)
}
