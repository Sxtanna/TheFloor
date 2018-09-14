package com.sxtanna.bby.jfx.view

import com.sxtanna.bby.base.anchorAllSides
import com.sxtanna.bby.jfx.MainWindow
import com.sxtanna.bby.jfx.view.sku.SkuSearchBigView
import com.sxtanna.bby.jfx.view.web.BBYWBrowser

class BigViewManager(private val main: MainWindow) {

    val brow = BBYWBrowser(main)
    val skus = SkuSearchBigView(main)


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


    fun view(): BigView {
        return views[index]
    }

    fun hide() {
        main.sideR.children.clear()
    }

    fun show(view: BigView = views[index]) {
        view.show(main.sideR)
        this.index = views.indexOf(view).takeIf { it != -1 } ?: 0
    }


    fun setupViews() {
        setupBrowser()

        views += brow
        views += skus

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