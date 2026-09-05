package com.sophia.helpjack.model

import com.sophia.helpjack.model.component.Component
import kotlin.reflect.KClass

/**
 * A generic, immutable entity. Position is mandatory for every actor;
 * everything else (sight radius, wood carrying, hiding, ...) is an
 * optional Component, so Bear and Jack (and future actors) can share
 * this single class instead of duplicating fields they don't use.
 */
data class Actor(
    val id: Int,
    val x: Int,
    val y: Int,
    val components: Map<KClass<out Component>, Component> = emptyMap()
) {
    fun getPosition(): Pair<Int, Int> = Pair(x, y)

    inline fun <reified T : Component> get(): T? = components[T::class] as? T

    inline fun <reified T : Component> has(): Boolean = components.containsKey(T::class)

    fun <T : Component> with(component: T): Actor =
        copy(components = components + (component::class to component))

    fun moveTo(newX: Int, newY: Int): Actor = copy(x = newX, y = newY)
}
