package com.sxtanna.bby.jfx

import com.sxtanna.bby.base.SP_H
import com.sxtanna.bby.base.SP_W
import com.sxtanna.bby.jfx.base.FullScreenManager
import com.sxtanna.bby.jfx.svg.SVGLoader
import javafx.application.Platform
import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.App
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors

class BBYWJfx : App(MainWindow::class) {

    lateinit var stage: Stage
        private set

    val svgLoader = SVGLoader(this)
    val scheduler = Executors.newScheduledThreadPool(10)

    val fsManager = FullScreenManager(this)


    init {
        inst = this
    }


    override fun start(stage: Stage) {
        this.stage = stage

        super.start(stage)

        val logo = Image(resourceAsStream("icon/Best_Buy_2018_Tag-S.png"))
        stage.icons.add(logo)

        stage.width = SP_W
        stage.height = SP_H

        stage.minWidth = SP_W * 0.60
        stage.minHeight = SP_H / 2.0

        stage.centerOnScreen()
        stage.isFullScreen = true

        stage.setOnCloseRequest {
            scheduler.shutdownNow()
            Runtime.getRuntime().halt(0)
        }

        stage.scene.stylesheets.add(resource("css/base.css").toExternalForm())

        fsManager.init()
    }


    fun resource(path: String): URL {
        return this::class.java.getResource("/com/sxtanna/bby/jfx/$path")
    }

    fun resourceAsStream(path: String): InputStream {
        return this::class.java.getResourceAsStream("/com/sxtanna/bby/jfx/$path")
    }


    fun execute(block: () -> Unit) {
        Platform.runLater(block)
    }


    companion object {

        lateinit var inst: BBYWJfx
            private set

    }

}