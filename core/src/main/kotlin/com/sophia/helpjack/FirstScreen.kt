package com.sophia.helpjack

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.sophia.helpjack.agent.CommonAgents
import com.sophia.helpjack.agent.GOAPAgent
import com.sophia.helpjack.agent.bt.*
import com.sophia.helpjack.agent.deco.ReactionTimeAgent
import com.sophia.helpjack.goap.goal.AvoidBearGoal
import com.sophia.helpjack.goap.goal.FireAliveGoal
import com.sophia.helpjack.model.World
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.graphics.center

class FirstScreen(val game: HelpJackGame) : KtxScreen {
    private var world = World.create()
    private var timerMs = 0 // in ms
    private val batch = SpriteBatch()
    private val viewport = ExtendViewport(world.width.toFloat(), world.height.toFloat()).apply {
        camera.center(minWorldWidth, minWorldHeight, -.5f, -.5f)
        camera.update()
    }
    private val renderer = WorldRenderer(viewport, batch)
    private val jackAgent = ReactionTimeAgent(
        GOAPAgent(
            world.jack.id,
                listOf(
                    FireAliveGoal(),
                    AvoidBearGoal()
                )
        ),
        500
    )
    private val bearAgent = CommonAgents.humanAgent(world.bear.id)

    override fun render(delta: Float) {
        logic(delta)

        renderer.render(world)

    }

    private fun logic(delta: Float) {
        if (world.isGameOver) return

        jackAgent.decide(world, delta)?.let {action ->
            world = action.apply(world)
        }
        bearAgent.decide(world, delta)?.let {action ->
            world = action.apply(world)
        }

        timerMs += (delta*1000).toInt()
        if (timerMs >= World.MIN_TICK_TIME) {
            world = world.tick(timerMs)
            timerMs -= World.MIN_TICK_TIME
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun dispose() {

        batch.disposeSafely()
    }
}
