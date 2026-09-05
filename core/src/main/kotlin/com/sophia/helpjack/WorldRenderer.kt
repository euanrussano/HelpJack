package com.sophia.helpjack

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.sophia.helpjack.model.World
import com.sophia.helpjack.model.component.WoodCarrier
import com.sophia.helpjack.model.component.isHidden
import com.sophia.helpjack.model.component.sightRadius
import ktx.graphics.center
import space.earlygrey.shapedrawer.ShapeDrawer

class WorldRenderer(
    private val viewport: ExtendViewport,
    private val batch: SpriteBatch
) {

    val UNIT_SIZE = 1f
    val fontColor = Color.WHITE
    val font = BitmapFont().apply {
        setUseIntegerPositions(false)
        color = fontColor
    }
    val drawer = ShapeDrawer(batch).apply {
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.WHITE)
        pixmap.fill()
        setTextureRegion(TextureRegion(Texture(pixmap)))
        pixmap.dispose()
    }
    val bgColor = Color.FOREST
    val sightColor = Color(Color.RED).apply { a = 0.3f }
    val jackRegion = TextureRegion(Texture("jack.png"))
    val treeRegion = TextureRegion(Texture("tree.png"))
    val campfireRegion = TextureRegion(Texture("campfire.png"))
    val bearRegion = TextureRegion(Texture("bear.png"))
    val cabinRegion = TextureRegion(Texture("cabin.png"))
    fun render(world: World) {
        viewport.apply()
        batch.setProjectionMatrix(viewport.camera.combined)
        batch.begin()
        renderBackground(world)
        renderGrid(world)
        for ((treeX, treeY) in world.trees) {
            batch.draw(treeRegion, treeX-UNIT_SIZE/2f, treeY-UNIT_SIZE/2f, UNIT_SIZE, UNIT_SIZE)
        }
        val (campfireX, campfireY) = world.campfire
        batch.draw(campfireRegion, campfireX-UNIT_SIZE/2f, campfireY-UNIT_SIZE/2f, UNIT_SIZE, UNIT_SIZE)
        val (cabinX, cabinY) = world.cabin
        batch.draw(cabinRegion, cabinX-UNIT_SIZE/2f, cabinY-UNIT_SIZE/2f, UNIT_SIZE, UNIT_SIZE)
        if (!world.jack.isHidden) {
            val (jackX, jackY) = world.jack.getPosition()
            batch.draw(jackRegion, jackX - UNIT_SIZE / 2f, jackY - UNIT_SIZE / 2f, UNIT_SIZE, UNIT_SIZE)
        }
        // draw bear sight
        val (bearX, bearY) = world.bear.getPosition()
        val bearSightRadius = world.bear.sightRadius
        drawer.setColor(Color.RED)
        for (x in bearX - bearSightRadius .. bearX + bearSightRadius){
            for (y in bearY - bearSightRadius .. bearY + bearSightRadius){
                if (x in 0..world.width && y in 0..world.height) {
                    val dst2 = (x-bearX)*(x-bearX) + (y-bearY)*(y-bearY)
                    if (dst2 > bearSightRadius*bearSightRadius) continue
                    drawer.filledRectangle(x - UNIT_SIZE / 2f, y - UNIT_SIZE / 2f, UNIT_SIZE, UNIT_SIZE, sightColor)
                }
            }
        }
        batch.draw(bearRegion, bearX-UNIT_SIZE/2f, bearY-UNIT_SIZE/2f, UNIT_SIZE,UNIT_SIZE)
        renderLabels(world)
        batch.end()
    }

    private fun renderBackground(world: World) {
        drawer.setColor(bgColor)
        drawer.filledRectangle(-UNIT_SIZE/2f, -UNIT_SIZE/2f, world.width*UNIT_SIZE, world.height*UNIT_SIZE)
    }

    private fun renderGrid(world: World) {
        val lineWidth = viewport.worldHeight/Gdx.graphics.height
        drawer.setColor(Color.DARK_GRAY)
        for (x in 0 .. world.width){
            drawer.line(x-UNIT_SIZE/2f, -UNIT_SIZE/2f, x-UNIT_SIZE/2f, world.height.toFloat()-UNIT_SIZE/2f, lineWidth)
        }
        for (y in 0 .. world.height){
            drawer.line(-UNIT_SIZE/2f, y-UNIT_SIZE/2f, world.width.toFloat()-UNIT_SIZE/2f, y-UNIT_SIZE/2f, lineWidth)
        }
    }

    private fun renderLabels(world: World) {
        font.data.setScale(viewport.worldHeight/Gdx.graphics.height)

        for ((treeX, treeY, wood) in world.trees) {
            font.draw(batch, "wood: $wood", treeX-UNIT_SIZE/2f, treeY-UNIT_SIZE/2f)
        }
        val (campfireX, campfireY, wood) = world.campfire
        font.draw(batch, "wood: ${wood}", campfireX-UNIT_SIZE/2f, campfireY-UNIT_SIZE/2f)
        val (cabinX, cabinY) = world.cabin
        font.draw(batch, "cabin", cabinX-UNIT_SIZE/2f, cabinY-UNIT_SIZE/2f)
        if (!world.jack.isHidden) {
            val (jackX, jackY) = world.jack.getPosition()
            val jackWood = world.jack.get<WoodCarrier>()!!.wood
            font.draw(batch, "wood: $jackWood", jackX - UNIT_SIZE / 2f, jackY - UNIT_SIZE / 2f)
        }
        val (bearX, bearY) = world.bear.getPosition()
        font.draw(batch, "bear", bearX-UNIT_SIZE/2f, bearY-UNIT_SIZE/2f)

        if (world.isGameOver) {
            val msg = "Game Over"
            val center = font.center(msg, viewport.worldWidth, viewport.worldHeight, -2f)
            font.color = Color.RED
            font.draw(batch, msg, center.x, center.y)
            font.color = fontColor
        }
    }


}
