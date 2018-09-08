package com.sxtanna.bby.jfx.base

import com.sxtanna.bby.jfx.BBYWJfx
import javafx.scene.input.KeyCode

class FullScreenManager(private val main: BBYWJfx) {

    private lateinit var hint: String


    fun init() {
        //hint = main.stage.fullScreenExitHint


        main.stage.fullScreenExitHint = "Press F11 to exit fullscreen"

        main.stage.scene.setOnKeyPressed {
            if (it.code == KeyCode.F11) main.stage.isFullScreen = main.stage.isFullScreen.not()
        }
    }

    fun stop() {
        main.stage.fullScreenExitHint = hint
        main.stage.scene.onKeyPressed = null
    }

}