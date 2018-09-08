package com.sxtanna.bby.jfx

import com.jfoenix.controls.JFXScrollPane
import com.jfoenix.controls.JFXTextField
import com.jfoenix.effects.JFXDepthManager
import com.sxtanna.bby.base.*
import com.sxtanna.bby.jfx.anim.FadeDir
import com.sxtanna.bby.jfx.anim.fade
import com.sxtanna.bby.jfx.web.BBYWBrowser
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.layout.*

import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.web.WebView
import tornadofx.*


class MainWindow : View("Best Buy Work") {

    private val main by lazy { BBYWJfx.inst }


    override val root by fxml<AnchorPane>()


    // tool
    private val tool by fxid<ToolBar>()
    private val bord by fxid<BorderPane>()

    // search
    private val pane by fxid<StackPane>()
    private val rect by fxid<Rectangle>()
    private val text by fxid<JFXTextField>()

    // account
    private val anch by fxid<BorderPane>()


    // body
    private val body by fxid<AnchorPane>()

    private val sideL by fxid<AnchorPane>()
    private val sideR by fxid<AnchorPane>()

    // side l
    private val scrl by fxid<ScrollPane>()
    private val tile = VBox()

    // side r
    private val view = WebView().anchorAllSides()
    private val brow = BBYWBrowser()


    init {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true")
        setupRoot()

        val splash = AppSplash()
        splash.init()

        setupTool()
        setupSideL()
        setupSideR()

        HBox.setHgrow(sideL, Priority.ALWAYS)
        HBox.setHgrow(sideR, Priority.ALWAYS)

        sideL.minWidthProperty().bind(main.stage.minWidthProperty().divide(2.0))
        sideL.maxWidthProperty().bind(main.stage.minWidthProperty().divide(2.0))

        tool.toFront()

        // default focus
        root.requestFocus()

        tool.setOnMouseClicked { root.requestFocus() }
        root.setOnMouseClicked { root.requestFocus() }

        tool.hide()
        body.hide()

        splash.fade()
    }


    private fun setupRoot() {
        root.background = backgroundFill(Color.rgb(BG_R, BG_G, BG_B))
    }


    // tool
    private fun setupTool() {
        tool.background = backgroundFill(Color.rgb(BB_BLUE_R, BB_BLUE_G, BB_BLUE_B))

        tool.prefWidthProperty().bind(main.stage.widthProperty())
        bord.prefWidthProperty().bind(tool.prefWidthProperty().subtract(50))

        pane.prefWidthProperty().bind(bord.prefWidthProperty().subtract(800).within(900.0, 1800.0))

        setupToolSearch()
        setupToolPerson()
    }

    private fun setupToolSearch() {
        rect.widthProperty().bind(pane.prefWidthProperty().subtract(50))

        text.maxWidthProperty().bind(rect.widthProperty())
        text.prefWidthProperty().bind(rect.widthProperty())

        pane.add(main.svgLoader.loadImageView("icon/search.svg", 50.0).stackpaneConstraints {
            marginLeft = 20.0
            alignment = Pos.CENTER_LEFT
        })

        // I absolutely hate that this value updates every time it changes
        text.fontProperty().bind(text.textProperty().isBlank().objectBinding {
            if (it == false) {
                Font.font("Lato", 35.0)
            } else {
                Font.font("Lato Bold", 35.0)
            }
        })


        val urlRegex = Regex("((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)")
        text.setOnKeyPressed {
            if (it.code != KeyCode.ENTER) return@setOnKeyPressed

            var input = text.text

            if (urlRegex.matches(input)) {

                if (input.startsWith("http", true).not()) {
                    input = "http://$input"
                }

                //view.engine.load(input)
            } else {
                println("Executing search for $input")
            }

            text.textProperty().set("")
        }
    }

    private fun setupToolPerson() {
        anch.center = main.svgLoader.loadImageView("icon/person.svg", 120.0)
    }


    // body
    private fun setupSideL() {
        scrl.add(tile)

        tile.paddingTop = 40.0
        tile.paddingBottom = 40.0

        tile.spacing = 20.0

        val panels = AppPanels()
        panels.init(tile)

        main.execute { scrl.requestLayout() }
        JFXScrollPane.smoothScrolling(scrl)
    }

    private fun setupSideR() {
        brow.init()
        brow.view.anchorAllSides()

        brow.show(sideR)
        brow.load("https://www.bestbuy.com")

        //sideR.add(view)
        //view.engine.load("https://www.bestbuy.com")
    }


    inner class AppSplash {

        private val logo = StackPane().apply {
            stick()

            val load = main.svgLoader.loadImageView("Best_Buy_2018 W.svg", SPLASH_LOGO_SCALE)
            add(load)

            opacity = 0.0
        }

        private val back = AnchorPane().apply {
            stick()

            add(logo)
            background = backgroundFill(Color.rgb(BB_BLUE_R, BB_BLUE_G, BB_BLUE_B))
        }


        fun init() {
            root.add(back)

            back.prefWidthProperty().bind(root.widthProperty())
            back.prefHeightProperty().bind(root.heightProperty())
        }

        fun fade() = logo.fade(main, speed = 15L, dir = FadeDir.IN) {
            logo.fade(main, after = 2000L, doneListener = ::clean)
        }


        private fun clean() {
            logo.hide()
            back.hide()

            back.children.remove(logo)
            root.children.remove(back)

            tool.show()
            body.show()
        }

        private fun Pane.stick() {
            anchorpaneConstraints {
                topAnchor = 0.0
                rightAnchor = 0.0
                bottomAnchor = 0.0
                leftAnchor = 0.0
            }
        }

    }

    inner class AppPanels {

        private val panels = mutableListOf<Panel>()


        fun init(pane: Pane) {
            repeat(10) {
                WelcomePanel()
            }

            panels.forEach {
                it.initPanel(pane)
                it.fillPanel()
            }
        }


        inner class WelcomePanel : Panel() {

            override fun fillPanel() {

                push(Text("Welcome")) {

                    style = "-fx-font-size: 50"
                    stackpaneConstraints {
                        alignment = Pos.TOP_LEFT
                    }

                }

            }

        }


        abstract inner class Panel {

            private lateinit var rootPane: Pane
            private lateinit var tilePane: StackPane

            init {
                panels.add(this)
            }


            open fun initPanel(root: Pane) {
                val w = 750.0
                val h = 200.0

                val s = StackPane()
                val r = Rectangle(w, h, Color.WHITE).stackpaneConstraints {
                    alignment = Pos.CENTER
                }
                r.arcWidth = 36.0
                r.arcHeight = 36.0

                s.add(r)
                JFXDepthManager.setDepth(r, 4)

                //s.background = backgroundFill(Color.GOLD)

                root.add(s)

                this.rootPane = root
                this.tilePane = s
            }

            open fun fillPanel() = Unit


            protected fun <N : Node> push(node: N, block: N.() -> Unit = { }) {
                tilePane.add(node.apply(block).apply(::applyRectMargins))
            }


            private fun applyRectMargins(node: Node) {
                node.stackpaneConstraints {
                    marginTop = 60.0
                    marginLeft = 60.0
                }
            }

        }

    }


}