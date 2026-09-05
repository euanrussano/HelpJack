package com.sophia.helpjack.task

import com.sophia.helpjack.agent.TaskListAgent
import com.sophia.helpjack.model.World

interface Task {
    fun step(agent: TaskListAgent, world: World): Boolean // true if task is done
}
