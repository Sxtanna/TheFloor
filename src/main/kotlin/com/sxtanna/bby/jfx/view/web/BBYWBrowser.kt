package com.sxtanna.bby.jfx.view.web

import com.sxtanna.bby.jfx.MainWindow
import com.sxtanna.bby.jfx.view.BigView
import com.teamdev.jxbrowser.chromium.Browser
import com.teamdev.jxbrowser.chromium.javafx.BrowserView
import javafx.scene.Node
import javafx.scene.layout.Pane
import tornadofx.add

class BBYWBrowser(val main: MainWindow) : BigView {

    val brow = Browser()
    val view = BrowserView(brow)


    override fun init() {

    }

    override fun node(): Node {
        return view
    }

    override fun show(pane: Pane) {
        pane.add(view)
    }


    fun load(url: String) {
        brow.loadURL(url)
        main.root.requestFocus()
    }

}