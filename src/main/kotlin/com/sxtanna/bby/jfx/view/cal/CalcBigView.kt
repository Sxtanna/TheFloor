package com.sxtanna.bby.jfx.view.cal

import com.jfoenix.controls.JFXButton
import com.sxtanna.bby.base.anchorAllSides
import com.sxtanna.bby.base.backgroundFill
import com.sxtanna.bby.calc.Calculator
import com.sxtanna.bby.jfx.view.BigView
import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.*
import javafx.scene.paint.Color
import tornadofx.*


class CalcBigView : BigView {

    private val bord = BorderPane()


    private lateinit var stack: VBox

    private lateinit var resultI: Label
    private lateinit var resultO: Label


    private var nextIsNegative = false


    override fun init() {
        bord.anchorAllSides()

        stack = VBox().apply {
            maxWidth = 800.0
            alignment = Pos.CENTER
        }

        bord.center = stack.borderpaneConstraints {
            alignment = Pos.CENTER
        }

        //stack.background = backgroundFill(Color.ORANGE)

        buildResult()
        buildCalculator()
    }

    override fun node(): Node {
        return bord
    }


    private fun buildResult() {
        resultI = Label("")
        resultO = Label("")

        val vbox = VBox(0.0, resultI, resultO).apply {
            alignment = Pos.TOP_RIGHT
        }

        resultI.style {
            textFill = Color.BLACK
            fontSize = Dimension(120.0, Dimension.LinearUnits.px)
        }
        resultO.style {
            textFill = Color.DIMGRAY
            fontSize = Dimension(50.0, Dimension.LinearUnits.px)
        }

        resultI.textProperty().onChange {
            //updateText(resultI)

            /*val clipped = Utils.computeClippedText(resultI.font, it, resultI.width, resultI.textOverrun, resultI.ellipsisString) ?: return@onChange
            if (clipped.isEmpty()) return@onChange


            if (resultI.text.length > clipped.length) {
                Platform.runLater {
                    println("Shrinking font ${resultI.font.size}")
                    resultI.style {
                        fontSize = Dimension(resultI.font.size - 20.0, Dimension.LinearUnits.px)
                    }
                }
            }*/

        }

        stack.add(vbox)
    }

    private fun buildCalculator() {
        val grid = GridPane().apply {
            maxWidth = 600.0
            maxHeight = 900.0
        }

        val numpad = listOf(
                JFXButton("7"), JFXButton("8"), JFXButton("9"),
                JFXButton("4"), JFXButton("5"), JFXButton("6"),
                JFXButton("1"), JFXButton("2"), JFXButton("3"),
                JFXButton("."), JFXButton("0"), JFXButton("=")
        )

        numpad.forEach {
            it.prefWidthProperty().bind(grid.maxWidthProperty().divide(3))
            it.prefHeightProperty().bind(grid.maxHeightProperty().divide(4))

            it.background = backgroundFill(Color.rgb(60, 64, 67))
            it.ripplerFill = Color.WHITESMOKE

            it.style(true) {
                textFill = Color.WHITE
                fontSize = Dimension(80.0, Dimension.LinearUnits.px)
            }
        }

        var index = 0
        (0..3).forEach { _ ->
            grid.row {
                repeat(3) {
                    add(numpad[index++])
                }
            }
        }

        grid.children.forEach {
            GridPane.setVgrow(it, Priority.ALWAYS)
            GridPane.setHgrow(it, Priority.ALWAYS)
        }

        val vbox = VBox().apply {
            maxHeight = 900.0
        }.borderpaneConstraints {
            alignment = Pos.CENTER_RIGHT
        }

        val operator = listOf(
                JFXButton("DEL"),
                JFXButton("/"),
                JFXButton("*"),
                JFXButton("-"),
                JFXButton("+")
        )

        operator.forEach {
            it.prefWidthProperty().bind(grid.maxWidthProperty().divide(3))
            it.prefHeightProperty().bind(grid.maxHeightProperty().divide(5))

            it.background = backgroundFill(Color.rgb(95, 99, 104))
            it.ripplerFill = Color.WHITESMOKE

            it.style(true) {
                textFill = Color.WHITE
                fontSize = Dimension(40.0, Dimension.LinearUnits.px)
            }

            vbox.add(it)
        }

        val hbox = HBox(grid, vbox).apply {
            alignment = Pos.CENTER
        }

        hbox.requestLayout()

        numpad.forEach {
            if (it.text == "=") return@forEach

            it.setOnMouseClicked { click ->
                resultI.text = if (nextIsNegative) {
                    "${resultI.text}-${it.text}"
                }
                else {
                    "${resultI.text}${it.text}"
                }
                updateResult()
            }
        }

        operator[0].setOnMouseClicked { // del
            if (resultI.text.isBlank()) return@setOnMouseClicked
            resultI.text = resultI.text.dropLast(1)
            updateResult()
        }

        operator.drop(1).forEach { op ->
            op.setOnMouseClicked { click ->
                if (operator.any { resultI.text.trimEnd().endsWith(it.text) }) {

                    if (op.text == "-") {
                        println("Next is negative")
                        nextIsNegative = true
                    }

                    return@setOnMouseClicked
                }

                resultI.text = "${resultI.text} ${op.text} "
                updateResult()
            }
        }

        stack.add(hbox)
    }


    private fun updateResult() {
        try {
            resultO.text = if (resultI.text.isBlank()) {
                ""
            }
            else {
                Calculator.exec(resultI.text)
            }
        }
        catch (ex: Exception) {
            resultO.text = "ERR"
        }
    }

    private fun updateText(label: Label) {
        Platform.runLater {
            var newFontSize = label.font.size
            var clippedText = Utils.computeClippedText(label.font, label.text, label.width, label.textOverrun, label.ellipsisString)

            while (!label.text.equals(clippedText) && newFontSize > 0.5) {
                println("fontSize = $newFontSize, clippedText = $clippedText")

                newFontSize -= 20.0

                label.style(true) {
                    fontSize = Dimension(newFontSize, Dimension.LinearUnits.px)
                }

                clippedText = Utils.computeClippedText(label.font, label.text, label.width, label.textOverrun, label.ellipsisString)
            }
        }
    }


}