package com.sxtanna.bby

import com.sxtanna.bby.jfx.BBYWJfx
import javafx.application.Application

fun main(args: Array<String>) {
    startWithJfx()
}


private fun startWithJfx() {
    Application.launch(BBYWJfx::class.java)
}