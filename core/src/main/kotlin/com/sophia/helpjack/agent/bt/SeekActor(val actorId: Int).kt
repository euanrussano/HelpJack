package com.sophia.helpjack.agent.bt

import com.sophia.helpjack.action.MoveByAction
import com.sophia.helpjack.agent.BehaviorTreeAgent
import com.sophia.helpjack.model.Status
import com.sophia.helpjack.model.World
import kotlin.math.abs
import kotlin.math.sign
import kotlin.random.Random

class SeekActor(val actorId: Int): BTNode {
    private val rng = Random(999)
    override fun step(
        agent: BehaviorTreeAgent,
        world: World
    ): Status {
        val actorId = agent.actorId

        val (jackX, jackY) = world.jack.getPosition()
        val (bearX, bearY) = world.bear.getPosition()

        val diffX = jackX - bearX
        val diffY = jackY - bearY

        val bestMove =  when {
            abs(diffX) >= abs(diffY) && diffX != 0 ->
                MoveByAction(actorId, diffX.sign, 0)

            diffY != 0 ->
                MoveByAction(actorId, 0, diffY.sign)

            else -> null
        }

        val possibleMoves = world.getActions(actorId).filterIsInstance<MoveByAction>()
        // if best move is in possible moves, assign it else assign a random possible move
        val choosenMove = if (bestMove in possibleMoves) bestMove else possibleMoves.random(rng)

        agent.action = choosenMove

        return Status.SUCCESS
    }

}
