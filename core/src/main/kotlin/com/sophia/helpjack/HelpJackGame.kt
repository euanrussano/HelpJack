package com.sophia.helpjack

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class HelpJackGame : KtxGame<KtxScreen>() {



    override fun create() {
        KtxAsync.initiate()

        addScreen(FirstScreen(this))
        setScreen<FirstScreen>()
    }
}

