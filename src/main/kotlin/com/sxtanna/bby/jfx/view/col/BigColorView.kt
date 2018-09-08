package com.sxtanna.bby.jfx.view.col

import com.sxtanna.bby.base.anchorAllSides
import com.sxtanna.bby.base.backgroundFill
import com.sxtanna.bby.jfx.view.BigView
import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import tornadofx.add

class BigColorView(private val color: Color) : BigView {

    private val node = Pane()


    override fun init() {
        node.background = backgroundFill(color)
        node.anchorAllSides()
    }

    override fun node(): Node {
        return node
    }

    override fun show(pane: Pane) {
        pane.add(node)
    }

}