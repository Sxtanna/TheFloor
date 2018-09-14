package com.sxtanna.bby.jfx.base

import com.sxtanna.bby.jfx.BBYWJfx
import javafx.scene.input.KeyCode

class FullScreenManager(private val main: BBYWJfx) {

    fun init() {
        main.stage.fullScreenExitHint = "Press F11 to exit fullscreen"

        main.stage.scene.setOnKeyPressed {
            if (it.code == KeyCode.F11) main.stage.isFullScreen = main.stage.isFullScreen.not()
        }
    }

}