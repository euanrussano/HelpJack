package com.sophia.helpjack.goap

import com.sophia.helpjack.action.Action
import com.sophia.helpjack.goap.goal.Goal
import com.sophia.helpjack.goap.goal.GoalSelector
import com.sophia.helpjack.model.World
import com.sophia.helpjack.model.component.WoodCarrier

data class WorldState(val world: World, val action: Action?)
object GOAPPlanner {
    const val MAX_DEPTH = 100_000

    data class PlanKey(
        val jackPos: Pair<Int, Int>,
        val jackWood: Int,
        val campfireWood: Int,
        val bearPos: Pair<Int, Int>
    ){
        companion object{
            fun from(world: World): PlanKey{
                return PlanKey(
                    jackPos = world.jack.x to world.jack.y,
                    jackWood = world.jack.get<WoodCarrier>()!!.wood,        // adjust to your actual accessor
                    campfireWood = world.campfire.wood,
                    bearPos = world.bear.x to world.bear.y
                )
            }
        }
    }

    fun plan(actorId: Int, startWorld: World, goal: Goal): List<Action> {
        if (goal.isStateSatisfied(startWorld)) return emptyList()

        val startState = WorldState(startWorld, null)

        val queue = mutableListOf<WorldState>()
        val visited = mutableSetOf<PlanKey>()
        val cameFrom = mutableMapOf<WorldState, WorldState>()

        var depth = 0
        queue.add(startState)
        visited.add(PlanKey.from(startWorld))
        while(queue.isNotEmpty()){
            val currentState = queue.removeAt(0)

            for (state in expand(currentState.world, actorId, goal)){
                val key = PlanKey.from(state.world)
                if (key in visited) continue

                cameFrom[state] = currentState

                if (goal.isStateSatisfied(state.world)){
                    return buildPlan(cameFrom, startState, state)
                }

                queue.add(state)
                visited.add(key)
            }
            depth++
            if (depth > MAX_DEPTH) break

        }
        return emptyList()
    }

    private fun buildPlan(
        cameFrom: MutableMap<WorldState, WorldState>,
        startState: WorldState,
        endState: WorldState
    ): List<Action> {
        println("Size cameFrom = ${cameFrom.size}")

        val path = mutableListOf<Action>()
        var current = endState
        path.add(0,current.action!!)
        while(current != startState){
            current = cameFrom[current]?:break
            current.action?.let {action ->
                path.add(0,action)
            }
        }
        return path
    }

    private fun expand(world: World, actorId: Int, goal: Goal): List<WorldState> {
        return world.getActions(actorId)
                .map { action -> WorldState(action.apply(world).tick(World.MIN_TICK_TIME), action) }
                .sortedBy { goal.heuristic(it.world) }

    }



}
