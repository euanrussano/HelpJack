package com.sophia.helpjack.agent

import com.sophia.helpjack.action.Action
import com.sophia.helpjack.action.MoveByAction
import com.sophia.helpjack.model.World
import kotlin.math.abs
import kotlin.math.sign
import kotlin.random.Random

class SeekJackAgent(override val actorId: Int): Agent {
    private val rng = Random(999)
    override fun decide(
        world: World,
        delta: Float
    ): Action? {
        val (jackX, jackY) = world.jack.getPosition()
        val (bearX, bearY) = world.bear.getPosition()

        val diffX = jackX - bearX
        val diffY = jackY - bearY

        val bestMoves = when {
            abs(diffX) >= abs(diffY) && diffX != 0 ->
                MoveByAction(actorId, diffX.sign, 0)

            diffY != 0 ->
                MoveByAction(actorId, 0, diffY.sign)

            else -> null
        }
        val possibleMoves = world.getActions(actorId).filterIsInstance<MoveByAction>()
        // if best move is in possible moves, return it
        if (bestMoves in possibleMoves) return bestMoves
        // else return a random possible move
        return possibleMoves.random(rng)
    }

}
