package com.sxtanna.bby.jfx.view.web

import com.sxtanna.bby.jfx.MainWindow
import com.sxtanna.bby.jfx.view.BigView
import com.teamdev.jxbrowser.chromium.Browser
import com.teamdev.jxbrowser.chromium.javafx.BrowserView
import javafx.scene.Node

class BBYWBrowser(private val main: MainWindow) : BigView {

    val brow = Browser()
    val view = BrowserView(brow)


    override fun node(): Node {
        return view
    }


    fun load(url: String) {
        brow.loadURL(url)
        main.root.requestFocus()
    }

}