package com.sophia.helpjack.action

import com.sophia.helpjack.model.World

interface Action {
    fun apply(world: World): World

}
