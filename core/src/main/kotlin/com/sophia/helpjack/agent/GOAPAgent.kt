package com.sophia.helpjack.agent

import com.badlogic.gdx.Gdx
import com.sophia.helpjack.action.Action
import com.sophia.helpjack.goap.GOAPPlanner
import com.sophia.helpjack.goap.goal.Goal
import com.sophia.helpjack.goap.goal.GoalSelector
import com.sophia.helpjack.model.World

class GOAPAgent(
    override val actorId: Int,
    val goals: List<Goal>
): Agent {

    var currentPlan: MutableList<Action> = mutableListOf()
    var action: Action? = null

    override fun decide(
        world: World,
        delta: Float
    ): Action? {
        if (world.distance2Between(world.jack, world.bear) < 3*3){
            Gdx.app.log("GOAPAgent", "Cancel plan, bear is too close")
            currentPlan.clear()

        }

        if (currentPlan.isEmpty()){
            Gdx.app.log("GOAPAgent", "Planning...")

            val goalSelector = GoalSelector(goals)

            goalSelector.pickGoal(world)?.let { goal ->

                Gdx.app.log("GOAPAgent", "Selected goal ${goal::class.simpleName}")

                currentPlan = GOAPPlanner.plan(actorId, world, goal).toMutableList()

                Gdx.app.log("GOAPAgent", "Plan created with ${currentPlan.size} actions...")
            }
        }
        Gdx.app.log("GOAPAgent", "Executing plan...")
        if (currentPlan.isEmpty()) {
            Gdx.app.log("GOAPAgent", "Plan is empty, no action to execute.")
            return null
        }

        Gdx.app.log("GOAPAgent", "Executing action ${currentPlan[0]}")
        return currentPlan.removeAt(0)

    }

}
