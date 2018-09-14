package com.sxtanna.bby.jfx.view

import javafx.scene.Node
import javafx.scene.layout.Pane
import tornadofx.add

interface BigView {

    val name: String
        get() = this::class.java.simpleName

    val desc: String
        get() = "big view named $name"


    fun init() = Unit

    fun node(): Node


    fun show(pane: Pane) {
        pane.add(node())
    }

}