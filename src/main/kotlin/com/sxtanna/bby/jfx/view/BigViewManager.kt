package com.sxtanna.bby.jfx.view

import com.sxtanna.bby.base.anchorAllSides
import com.sxtanna.bby.jfx.MainWindow
import com.sxtanna.bby.jfx.view.col.BigColorView
import com.sxtanna.bby.jfx.view.web.BBYWBrowser
import javafx.scene.paint.Color

class BigViewManager(private val main: MainWindow) {

    val brow = BBYWBrowser(main)

    val colR = BigColorView(Color.RED)
    val colG = BigColorView(Color.GREEN)
    val colB = BigColorView(Color.BLUE)


    private var index = 0
    private val views = mutableListOf<BigView>()


    fun next() {
        index = (index + 1).coerceAtMost(views.lastIndex)

        hide()
        show()
    }

    fun prev() {
        index = (index - 1).coerceAtLeast(0)

        hide()
        show()
    }


    fun hide() {
        main.sideR.children.clear()
    }

    fun show(view: BigView = views[index]) {
        view.show(main.sideR)
    }


    fun setupViews() {
        setupBrowser()

        views += brow
        views += colR
        views += colG
        views += colB

        initViews()
    }


    private fun setupBrowser() {
        brow.view.anchorAllSides()
        brow.load("https://www.bestbuy.com")
    }


    private fun initViews(vararg views: BigView = this.views.toTypedArray()) {
        views.forEach { it.init() }
    }

}