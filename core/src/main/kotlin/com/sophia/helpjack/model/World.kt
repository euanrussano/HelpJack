package com.sophia.helpjack.model

import com.sophia.helpjack.action.Action
import com.sophia.helpjack.action.MoveByAction
import com.sophia.helpjack.action.NoOpAction
import com.sophia.helpjack.model.component.*
import kotlin.math.abs

data class World(
    val width: Int,
    val height: Int,
    val jack: Actor = newJack(width/2, height/2),
    val campfire: Campfire = Campfire(width / 2, height / 2 - 1),
    val bear: Actor = newBear(0, 0),
    val cabin: Cabin = Cabin(width / 2, height - 1),
    val trees: List<Tree> = listOf(Tree(1, 1), Tree(width-1, 1), Tree(width-1, height-1), Tree(1, height-1)),
    val isGameOver: Boolean = false
){
    init {
        require(width >= MIN_WIDTH)
        require(height >= MIN_HEIGHT)
        require(jack.x in 0 until width)
        require(jack.y in 0 until height)
        // TODO: validation of states campfire, bear, cabin, trees
    }

    fun selectActor(id: Int): Actor = when (id) {
        jack.id -> jack
        bear.id -> bear
        else -> error("Invalid actor ID: $id")
    }
    fun moveActorBy(actorId: Int, dx: Int, dy: Int): World {
        if (isGameOver) return this

        val actor = selectActor(actorId)
        val newX = actor.x + dx
        val newY = actor.y + dy
        if (!canMoveTo(actor.id, newX, newY)) return this
        val newActor = actor.moveTo(newX, newY)
        return if (actor == jack) copy(jack = newActor) else copy(bear = newActor)
    }

    private fun canMoveTo(actorId: Int, x: Int, y: Int): Boolean {
        val actor = selectActor(actorId)
        val oldX = actor.x
        val oldY = actor.y
        val dx = abs(oldX - x)
        val dy = abs(oldY - y)
        if (dx > 1 || dy > 1) return false // can move only one cell at a time
        if (dx == 0 && dy == 0) return false // cannot move to same cell
        if (dx != 0 && dy != 0) return false // cannot move in diagonals
        val isOutsideBounds =  x !in 0 until width || y !in 0 until height
        val isTree = trees.any { it.x == x && it.y == y }
        val isCampfire = campfire.x == x && campfire.y == y
        val isBear = bear.x == x && bear.y == y
        val isJack = jack.x == x && jack.y == y
        return !isOutsideBounds && !isTree && !isCampfire && !isBear && !isJack
    }

    fun getActions(actorId: Int): List<Action> {
        if (isGameOver) return emptyList()

        val actor = selectActor(actorId)
        val actions = mutableListOf<Action>()
        val directions = listOf(
            1 to 0,
            -1 to 0,
            0 to 1,
            0 to -1
        )

        val x = actor.x
        val y = actor.y

        for ((dx, dy) in directions) {
            if (canMoveTo(actor.id, x + dx, y + dy)) {
                actions.add(MoveByAction(actor.id, dx, dy))
            }
        }
        actions.add(NoOpAction)
        return actions
    }

    fun tick(delta: Int): World { // delta is in ms
        if (isGameOver) return this

        var newWorld = this
        newWorld = growTrees(newWorld, delta)
        val newCampfire = campfire.update(delta)
        newWorld = newWorld.copy(campfire = newCampfire)
        newWorld = chopTree(newWorld)
        newWorld = feedFire(newWorld)
        newWorld = hideJack(newWorld)
        newWorld = isGameOver(newWorld)

        return newWorld
    }

    fun actorSees(observerId: Int, targetId: Int): Boolean {
        require(observerId != targetId)
        val observer = selectActor(observerId)
        val target = selectActor(targetId)

        if (!observer.has<Sight>()) return false
        if (target.has<Hideable>()){
            if (target.isHidden){
                return false
            }
        }

        val visibleTiles = mutableListOf<Pair<Int, Int>>()
        val (x, y) = observer
        val sightRadius = observer.sightRadius
        for (dx in x - sightRadius .. x + sightRadius){
            for (dy in y - sightRadius .. y + sightRadius){
                if (dx in 0..width && dy in 0..height) {
                    val dst2 = (dx-x)*(dx-x) + (dy-y)*(dy-y)
                    if (dst2 > sightRadius*sightRadius) continue
                    visibleTiles.add(Pair(dx, dy))
                }
            }
        }
        val (tX, tY) = target
        return visibleTiles.contains(Pair(tX, tY))
    }

    fun findTreeAdjacentTo(x: Int, y: Int): Tree?{
        return trees.find {
            abs(it.x - x) + abs(it.y - y) == 1
        }
    }

    fun findNearestTreeTo(x: Int, y: Int, requiredWood: Boolean = true): Tree?{
        if (trees.isEmpty()) return null
        return trees.minBy {
            abs(it.x - x) + abs(it.y - y)
        }
    }

    fun findFireAdjacentTo(x: Int, y: Int): Campfire? {
        if (abs(campfire.x - x) + abs(campfire.y - y) == 1) return campfire
        return null
    }

    fun findNearestCampfireTo(x: Int, y: Int): Campfire{
        return campfire
    }

    fun distance2Between(actor: Actor, actor2: Actor): Int {
        val (x, y) = actor
        val (x2, y2) = actor2
        return (x - x2) * (x - x2) + (y - y2) * (y - y2)
    }


    companion object {
        const val MIN_TICK_TIME = 500 // in ms
        const val MIN_WIDTH = 10
        const val MIN_HEIGHT = 10
        fun create(): World {
            return World(10, 10)
        }

        fun newBear(x: Int, y: Int, sightRadius: Int = 3): Actor = Actor(1, x, y).with(Sight(sightRadius))

        fun newJack(x: Int, y: Int, wood: Int = 0, isHidden: Boolean = false): Actor =
            Actor(0, x, y)
                .with(WoodCarrier(wood))
                .with(Hideable(isHidden))


        private fun chopTree(world: World): World {
            val (x, y) = world.jack.getPosition()
            val tree = world.findTreeAdjacentTo(x, y)?: return world
            if (tree.wood < 1) return world

            val idx = world.trees.indexOf(tree)
            val chopResult = tree.chop()
            val newJack = world.jack.increaseWood(chopResult.wood)
            val trees = world.trees.toMutableList().apply {
                this[idx] = chopResult.tree
            }
            return world.copy(jack = newJack, trees = trees)
        }

        private fun growTrees(world: World, delta: Int): World {
            val trees = world.trees.map { it.update(delta) }
            return world.copy(trees = trees)
        }

        private fun feedFire(world: World): World {
            val (jackX, jackY) = world.jack.getPosition()
            val (campX, campY) = world.campfire

            val campFireNextToJack = abs(campX - jackX) + abs(campY - jackY) == 1

            if (!campFireNextToJack) return world
            if (!world.jack.hasWood()) return world


            val newJack = world.jack.decreaseWood(1)
            val newCampfire = world.campfire.increaseWood(1)

            return world.copy(jack = newJack, campfire = newCampfire)
        }

        private fun isGameOver(world: World): World {
            // if jack is hidden, he is safe
            if (world.jack.isHidden) return world

            // is bear on the side of jack
            val (jackX, jackY) = world.jack.getPosition()
            val (bearX, bearY) = world.bear.getPosition()
            val bearNextToJack = abs(bearX - jackX) + abs(bearY - jackY) == 1

            return world.copy(isGameOver = bearNextToJack)
        }

        private fun hideJack(world: World): World {
            val jackInCabin =
                world.jack.x == world.cabin.x &&
                    world.jack.y == world.cabin.y

            if (world.jack.isHidden == jackInCabin) {
                return world
            }

            val newJack = if (jackInCabin) world.jack.hide() else world.jack.reveal()
            return world.copy(jack = newJack)
        }
    }


}
