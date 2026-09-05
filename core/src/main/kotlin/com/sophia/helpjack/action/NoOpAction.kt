package com.sophia.helpjack.action

import com.sophia.helpjack.model.World

data object NoOpAction: Action {
    override fun apply(world: World): World {
        return world
    }
}
