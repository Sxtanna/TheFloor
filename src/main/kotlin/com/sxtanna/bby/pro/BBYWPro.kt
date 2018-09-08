package com.sxtanna.bby.pro

import com.sxtanna.bby.base.APP_FPS
import processing.core.PApplet
import processing.core.PShapeSVG

class BBYWPro : PApplet() {

    private val start = StartAnimation(APP_FPS * 5L)


    override fun settings() {
        size(1824 / 2, 2736 / 2)
    }

    override fun setup() {
        background(14F, 77F, 181F)
        frameRate(APP_FPS.toFloat())

        start.logo = loadShape("com/sxtanna/bby/jfx/Best_Buy_2018 W.svg") as PShapeSVG
    }

    override fun draw() {
        background(14F, 77F, 181F)

        if (start.doneDrawing.not()) {
            start.draw()
        }


    }


    inner class StartAnimation(private val drawFrames: Long) {

        private var step = 10F

        var drawnFrames = 0L
            private set
        var doneDrawing = false
            private set


        lateinit var logo: PShapeSVG



        fun draw() {
            if (drawnFrames++ > drawFrames) {
                doneDrawing = true
            }

            var (drawX, drawY) = (width / 2F) to (height / 4F)

            if (drawnFrames > ((drawFrames / 4) * 3)) {
                logo.scale(0.9F)

                drawX -= step
                drawY -= step

                step += 10F
            }

            shapeMode(CENTER)
            shape(logo, drawX, drawY, logo.width / 2F, logo.height / 2F)
        }

    }

}