package com.sxtanna.bby.jfx.view

import javafx.scene.Node
import javafx.scene.layout.Pane

interface BigView {

    fun init()

    fun node(): Node

    fun show(pane: Pane)

}