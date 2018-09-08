package com.sxtanna.bby

import com.sxtanna.bby.jfx.BBYWJfx
import javafx.application.Application

fun main(args: Array<String>) {
    startWithJfx()
}


private fun startWithJfx() {
    System.setProperty("javax.xml.parsers.DocumentBuilderFactory",  "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl")
    Application.launch(BBYWJfx::class.java)
}