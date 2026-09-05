package com.sophia.helpjack.model.component

import com.sophia.helpjack.model.Actor

// ---- Convenience extensions on Actor -----------------------------------
// These let call sites keep reading like `jack.hasWood()`, `jack.increaseWood(5)`,
// `bear.sightRadius`, etc., without Actor itself knowing about Jack/Bear specifics.

private inline fun <reified T : Component> Actor.require(): T =
    get<T>() ?: error("Actor has no ${T::class.simpleName} component")

val Actor.sightRadius: Int
    get() = require<Sight>().radius

fun Actor.hasWood(): Boolean = require<WoodCarrier>().hasWood()

fun Actor.increaseWood(amount: Int): Actor =
    with(require<WoodCarrier>().increase(amount))

fun Actor.decreaseWood(amount: Int): Actor =
    with(require<WoodCarrier>().decrease(amount))

val Actor.isHidden: Boolean
    get() = require<Hideable>().isHidden

fun Actor.hide(): Actor = with(require<Hideable>().hide())
fun Actor.reveal(): Actor = with(require<Hideable>().reveal())
