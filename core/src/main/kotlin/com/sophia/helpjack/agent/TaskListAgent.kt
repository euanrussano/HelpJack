package com.sophia.helpjack.agent

import com.sophia.helpjack.action.Action
import com.sophia.helpjack.model.World
import com.sophia.helpjack.task.Task

class TaskListAgent(
    override val actorId: Int,
    vararg val tasks: Task,
    private val loop: Boolean = true
): Agent {
    var currentTask: Task? = tasks.first()
    var action: Action? = null

    override fun decide(
        world: World,
        delta: Float
    ): Action? {
        action = null

        println("TaskListAgent decide: ${currentTask?.let { it::class.simpleName }?: run{ null }}")

        if (currentTask == null) return null

        val isTaskDone = currentTask?.step(this, world) == true
        if (isTaskDone) {
            val idx = tasks.indexOf(currentTask)

            if (idx + 1 !in tasks.indices && !loop){
                currentTask = null
                return null
            }

            currentTask = tasks[(idx + 1) % tasks.size]

            println("TaskListAgent decide: ${currentTask?.let { it::class.simpleName }?: run{ null }}")
        }
        return action
    }

}
