package com.sxtanna.bby.jfx.view.sku

import com.jfoenix.controls.JFXButton
import com.sxtanna.bby.base.*
import com.sxtanna.bby.jfx.MainWindow
import com.sxtanna.bby.jfx.view.BigView
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Basically its a number pad for entering skus
 *
 * 7 | 8 | 9
 * 4 | 5 | 6
 * 1 | 2 | 3
 *     0
 *   ENTER
 */
class SkuSearchBigView(private val main: MainWindow) : BigView {

    override val name = "Search by SKU"
    override val desc = "search a sku on dot com"


    private val bord = BorderPane()
    private val text = "Input SKU Below"

    private lateinit var number: Label
    private lateinit var numpad: List<JFXButton>


    override fun init() {
        bord.anchorAllSides()

        val stack = VBox().apply {
            spacing = 30.0
            alignment = Pos.CENTER
        }

        number = createSkuDisplay()
        stack.add(number)


        val grid = createNumPadGrid()
        stack.add(grid)


        val submit = createEnterButton()
        stack.add(submit)


        bord.center = stack.borderpaneConstraints {
            alignment = Pos.CENTER
        }
    }

    override fun node(): Node {
        return bord
    }


    private fun createSkuDisplay(): Label {
        val label = Label(text)

        label.style(true) {
            fontSize = Dimension(65.0, Dimension.LinearUnits.px)
            fontWeight = FontWeight.BOLD
            fontFamily = "Lato Bold"
        }

        return label
    }

    private fun createNumPadGrid(): GridPane {
        val grid = GridPane().apply {
            maxWidth = 700.0
            maxHeight = 900.0

            hgap = 10.0
            vgap = 10.0
        }


        numpad = listOf(
                JFXButton("7"), JFXButton("8"), JFXButton("9"),
                JFXButton("4"), JFXButton("5"), JFXButton("6"),
                JFXButton("1"), JFXButton("2"), JFXButton("3"),
                JFXButton(" "), JFXButton("0")
        )

        numpad.forEach {
            it.prefWidthProperty().bind(grid.maxWidthProperty().divide(3))
            it.prefHeightProperty().bind(grid.maxHeightProperty().divide(4))

            it.style(true) {
                textFill = Color.WHITE
                fontSize = Dimension(80.0, Dimension.LinearUnits.px)
                backgroundColor += Color.rgb(BB_BLUE_R, BB_BLUE_G, BB_BLUE_B)
            }

            it.ripplerFill = Color.WHITESMOKE

            if (it.text.isBlank()) return@forEach

            it.setOnMouseClicked { click ->
                if (click.button != MouseButton.PRIMARY) return@setOnMouseClicked

                if (number.text.length > 7) {
                    number.text = it.text

                    number.style(true) {
                        textFill = Color.BLACK
                    }
                } else {
                    number.text = number.text + it.text
                }

                if (number.text.length == 7) {
                    numpad.forEach { button ->
                        if (button.text.isNotBlank()) button.isDisable = true
                    }
                }
            }
        }

        var index = 0

        (0..2).forEach { _ ->
            grid.row {
                repeat(3) {
                    add(numpad[index++])
                }
            }
        }

        grid.row {
            add(numpad[9].apply {
                isDisable = true
                style(true) {
                    backgroundColor += Color.TRANSPARENT
                }
            })
            add(numpad[10])
        }

        grid.children.forEach {
            GridPane.setVgrow(it, Priority.ALWAYS)
            GridPane.setHgrow(it, Priority.ALWAYS)
        }

        return grid
    }

    private fun createEnterButton(): JFXButton {
        val button = JFXButton("ENTER")

        button.apply {
            buttonType = JFXButton.ButtonType.RAISED

            style(true) {
                fontSize = Dimension(65.0, Dimension.LinearUnits.px)
                fontWeight = FontWeight.BOLD
                fontFamily = "Lato Bold"
            }

            setOnMouseClicked {
                main.search.submitSearch("bb: ${number.text}")

                lateinit var repeat: ScheduledFuture<*>

                repeat = main.main.scheduler.scheduleAtFixedRate({
                    if (main.view.brow.brow.isLoading) {
                        return@scheduleAtFixedRate
                    }

                    repeat.cancel(true)
                    main.main.execute {
                        val url = main.view.brow.brow.url

                        if (url.startsWith("https://www.bestbuy.com/site/searchpage.jsp?st=")) { // failed search
                            number.text = "Invalid Sku"

                            number.style(true) {
                                textFill = Color.RED
                            }
                        } else {
                            number.text = this@SkuSearchBigView.text
                            main.view.show(main.view.brow)
                        }

                        numpad.forEach { button ->
                            if (button.text.isNotBlank()) button.isDisable = false
                        }
                    }


                }, 0L, 50L, TimeUnit.MILLISECONDS)

            }
        }

        return button
    }

}