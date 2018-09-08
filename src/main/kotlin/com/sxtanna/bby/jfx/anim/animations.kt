package com.sxtanna.bby.jfx.anim

import com.sxtanna.bby.jfx.BBYWJfx
import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.scene.Node
import java.util.concurrent.TimeUnit

enum class FadeDir {

    IN,
    OUT;

}


fun Node.fade(main: BBYWJfx, after: Long = 0L, speed: Long = 10L, dir: FadeDir = FadeDir.OUT, step: Double = 0.01, doneListener: () -> Unit = {}) {
    val task = main.scheduler.scheduleAtFixedRate({
        when (dir) {
            FadeDir.IN -> {
                opacity += step
            }
            FadeDir.OUT -> {
                opacity -= step
            }
        }
    }, after, speed, TimeUnit.MILLISECONDS)


    lateinit var listener: ChangeListener<Number>

    listener = ChangeListener { _, _, value ->
        if (value.toDouble() <= 0.0 || value.toDouble() >= 1.0) {
            Platform.runLater(doneListener)

            task.cancel(true)
            opacityProperty().removeListener(listener)
        }
    }

    opacityProperty().addListener(listener)
}