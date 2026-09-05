package com.sophia.helpjack.agent

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.sophia.helpjack.action.Action
import com.sophia.helpjack.action.MoveByAction
import com.sophia.helpjack.model.World


class HumanAgent(override val actorId: Int): Agent {
    override fun decide(world: World, delta: Float): Action? {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            return MoveByAction(actorId,0, 1)
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            return MoveByAction(actorId,0, -1)
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            return MoveByAction(actorId,-1, 0)
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            return MoveByAction(actorId,1, 0)
        }
        return null
    }
}
