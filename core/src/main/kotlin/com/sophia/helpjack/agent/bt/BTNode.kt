package com.sophia.helpjack.agent.bt

import com.sophia.helpjack.agent.BehaviorTreeAgent
import com.sophia.helpjack.model.Status
import com.sophia.helpjack.model.World

interface BTNode {
    fun step(agent: BehaviorTreeAgent, world: World): Status

}
