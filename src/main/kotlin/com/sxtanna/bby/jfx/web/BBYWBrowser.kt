package com.sxtanna.bby.jfx.web

import com.teamdev.jxbrowser.chromium.Browser
import com.teamdev.jxbrowser.chromium.javafx.BrowserView
import javafx.scene.layout.Pane
import tornadofx.add

class BBYWBrowser {

    lateinit var brow: Browser
        private set
    lateinit var view: BrowserView
        private set


    fun init() {
        brow = Browser()
        view = BrowserView(brow)
    }


    fun show(pane: Pane) {
        pane.add(view)
    }

    fun load(url: String) {
        brow.loadURL(url)
    }

}