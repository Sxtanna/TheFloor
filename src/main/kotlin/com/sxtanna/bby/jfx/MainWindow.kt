package com.sxtanna.bby.jfx

import com.jfoenix.controls.JFXTextField
import com.jfoenix.effects.JFXDepthManager
import com.sxtanna.bby.base.*
import com.sxtanna.bby.data.Employee
import com.sxtanna.bby.data.Resource
import com.sxtanna.bby.jfx.anim.FadeDir
import com.sxtanna.bby.jfx.anim.fade
import com.sxtanna.bby.jfx.view.BigView
import com.sxtanna.bby.jfx.view.BigViewManager
import com.sxtanna.korm.Korm
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.layout.*

import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import tornadofx.*
import java.net.URLEncoder


class MainWindow : View("Best Buy Work") {

    val main by lazy { BBYWJfx.inst }


    override val root by fxml<AnchorPane>()


    // tool
    private val tool by fxid<ToolBar>()
    private val bord by fxid<BorderPane>()

    // search
    private val pane by fxid<StackPane>()
    private val rect by fxid<Rectangle>()
    private val text by fxid<JFXTextField>()

    // account
    private val anch by fxid<HBox>()


    // body
    private val body by fxid<AnchorPane>()

    internal val sideL by fxid<AnchorPane>()
    internal val sideR by fxid<AnchorPane>()

    // side l
    private val scrl by fxid<ScrollPane>()
    private val tile = VBox()

    // side r
    val view = BigViewManager(this)


    val korm = Korm()

    val splash = AppSplash()
    val panels = AppPanels()
    val search = AppSearch()

    lateinit var info: Employee


    init {
        view.setupViews()

        setupRoot()

        Resource.load(korm, main.resourceAsStream("docs/docs.korm"))
        info = korm.pull(main.resourceAsStream("docs/info.korm")).to<Employee>() ?: Employee.DEFAULT

        setupTool()
        setupSideL()
        setupSideR()

        HBox.setHgrow(sideL, Priority.ALWAYS)
        HBox.setHgrow(sideR, Priority.ALWAYS)

        sideL.minWidthProperty().bind(main.stage.minWidthProperty().divide(2.0))
        sideL.maxWidthProperty().bind(main.stage.minWidthProperty().divide(2.0))

        // default focus
        root.requestFocus()

        tool.setOnMouseClicked { }
        root.setOnMouseClicked { root.requestFocus() }

        splash.init()
        splash.fade()
    }


    private fun setupRoot() {
        root.background = backgroundFill(Color.rgb(BG_R, BG_G, BG_B))

        root.setOnScroll {
            if (it.isControlDown.not() || view.view() !== view.brow) return@setOnScroll

            when(it.deltaY) {
                +40.0 -> { // scroll up
                    view.brow.brow.zoomIn()
                }
                -40.0 -> { // scroll down
                    view.brow.brow.zoomOut()
                }
            }
        }
    }


    // tool
    private fun setupTool() {
        tool.background = backgroundFill(Color.rgb(BB_BLUE_R, BB_BLUE_G, BB_BLUE_B))

        tool.prefWidthProperty().bind(main.stage.widthProperty())
        bord.prefWidthProperty().bind(tool.prefWidthProperty().subtract(50))

        pane.prefWidthProperty().bind(bord.prefWidthProperty().subtract(800).within(900.0, 1800.0))

        setupToolSearch()
        setupToolPerson()

        tool.setOnMouseClicked {
            root.requestFocus()

            when (it.button) {
                MouseButton.PRIMARY -> {
                    view.prev()
                }
                MouseButton.SECONDARY -> {
                    view.next()
                }
                else -> {}
            }
        }
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

        text.textProperty().onChange {
            search.submitUpdate(it ?: return@onChange)
        }

        text.setOnKeyPressed {
            if (it.code != KeyCode.ENTER) return@setOnKeyPressed
            search.submitSearch(text.text)
        }

        text.focusedProperty().addListener { _, _, new ->
            if (new && text.text.isNotBlank()) {
                main.execute {
                    text.selectAll()
                }
            }
        }
    }

    private fun setupToolPerson() {
        val vbox = VBox().apply {
            alignment = Pos.CENTER_RIGHT
        }

        vbox.add(Label("Ranald T.").apply {
            style(true) {
                fontSize = Dimension(45.0, Dimension.LinearUnits.px)
                fontWeight = FontWeight.BOLD
                fontFamily = "Lato Bold"
                textFill = Color.WHITE
            }
        })

        vbox.add(Label("#1092").apply {
            style(true) {
                fontSize = Dimension(20.0, Dimension.LinearUnits.px)
                fontWeight = FontWeight.BOLD
                fontFamily = "Lato Bold"
                textFill = Color.WHITE
            }
        })

        anch.add(vbox)
        anch.add(main.svgLoader.loadImageView("icon/person.svg", 130.0))
    }


    // body
    private fun setupSideL() {
        scrl.add(tile)

        tile.paddingTop = 40.0
        tile.paddingBottom = 40.0

        tile.spacing = 20.0

        panels.init(tile)
    }

    private fun setupSideR() {
        view.show()
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
            tool.hide()
            body.hide()

            root.add(back)

            back.prefWidthProperty().bind(root.widthProperty())
            back.prefHeightProperty().bind(root.heightProperty())
        }

        fun fade() = logo.fade(main, speed = 15L, dir = FadeDir.IN) {
            logo.fade(main, after = 2000L, doneListener = ::clean)
        }


        private fun clean() {
            tool.show()
            body.show()

            logo.hide()
            back.hide()

            back.children.remove(logo)
            root.children.remove(back)
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

        val panels = mutableListOf<Panel>()
        val seeing = mutableListOf<Panel>()

        val noResults = NoResultsPanel()


        fun init(pane: Pane) {
            WelcomePanel()

            BigViewLink(view.skus)

            Resource.values().forEach {
                DocumentLink(it)
            }

            WebPagesLink("DOT COM", "https://www.bestbuy.com", "main best buy website")
            WebPagesLink("Translate", "https://translate.google.com/", "opens google translate")
            WebPagesLink("My HR", "https://hr.bestbuy.com", "best buy human resources")
            WebPagesLink("TLC", "https://mytlc.bestbuy.com/etm/", "access time & labor center")
            WebPagesLink("Employee Hub", "https://hub.bestbuy.com", "best buy employee hub")

            seeing += panels

            seeing.forEach {
                it.initPanel(pane)
                it.fillPanel()

                it.showPanel()
            }

            noResults.initPanel(pane)
            noResults.fillPanel()
        }


        inner class WelcomePanel : Panel() {

            override fun fillPanel() = buildTitleSubTitle("Ranald Taylor", "ranald.taylor@bestbuy.com", titleBlock = {
                it.style(true) {
                    this.textFill = Color.rgb(BB_BLUE_R, BB_BLUE_G, BB_BLUE_B)
                }
            })

        }

        inner class BigViewLink(private val view: BigView) : Panel() {

            override fun fillPanel() = buildTitleSubTitle(view.name, view.desc, titleBlock = {
                it.style(true) {
                    fontSize = Dimension(45.0, Dimension.LinearUnits.px)
                }

                setOnMouseClicked { click ->
                    if (click.button != MouseButton.PRIMARY) return@setOnMouseClicked

                    if (Tracker.checkCurrent(this@BigViewLink)) {
                        this@MainWindow.view.hide()
                        this@MainWindow.view.show(view)

                        text.text = view.name
                        search.submitUpdate("")
                    }
                }
            })

            override fun showNamed(text: String): Boolean {
                return view.name.contains(text, true)
            }

        }

        inner class DocumentLink(private val doc: Resource) : Panel() {

            override fun fillPanel() = buildTitleSubTitle(doc.name, "opens on right panel", titleBlock = {
                it.style(true) {
                    fontSize = Dimension(45.0, Dimension.LinearUnits.px)
                }

                setOnMouseClicked { click ->
                    if (click.button != MouseButton.PRIMARY) return@setOnMouseClicked

                    if (Tracker.checkCurrent(this@DocumentLink)) {
                        if (view.view() != view.brow) {
                            view.hide()
                            view.show(view.brow)
                        }

                        view.brow.load(doc.link)
                        text.text = doc.name
                        search.submitUpdate("")
                    }
                }
            })

            override fun showNamed(text: String): Boolean {
                return doc.name.contains(text, true)
            }

        }

        inner class WebPagesLink(private val name: String, private val url: String, private val desc: String = "opens on right panel") : Panel() {

            override fun fillPanel() = buildTitleSubTitle(name, desc, titleBlock = {
                it.style(true) {
                    fontSize = Dimension(45.0, Dimension.LinearUnits.px)
                }

                setOnMouseClicked { click ->
                    if (click.button != MouseButton.PRIMARY) return@setOnMouseClicked

                    if (Tracker.checkCurrent(this@WebPagesLink)) {
                        if (view.view() != view.brow) {
                            view.hide()
                            view.show(view.brow)
                        }

                        view.brow.load(url)
                        text.text = name
                        search.submitUpdate("")
                    }
                }
            })

            override fun showNamed(text: String): Boolean {
                return name.contains(text, true)
            }

        }

        inner class NoResultsPanel : Panel() {

            init {
                panels.remove(this)
            }

            override fun fillPanel() = buildTitleSubTitle("No Results", "no results for current input", titleBlock = {
                it.style(true) {
                    this.textFill = Color.DARKRED
                }
            })

        }


        abstract inner class Panel {

            lateinit var rootPane: Pane
                private set
            lateinit var tilePane: StackPane
                private set


            init {
                panels.add(this)
            }


            fun showPanel() {
                rootPane.children.add(tilePane)
            }

            fun hidePanel() {
                rootPane.children.remove(tilePane)
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

                this.rootPane = root
                this.tilePane = s
            }

            open fun fillPanel() = Unit

            open fun showNamed(text: String): Boolean {
                return true
            }


            protected fun <N : Node> push(node: N, block: N.() -> Unit = { }): N {
                tilePane.add(node.apply(block))
                return node
            }

            protected fun buildTitleSubTitle(titleText: String, subTitleText: String = "", titleBlock: VBox.(Label) -> Unit = { }, subTitleBlock: VBox.(Label) -> Unit = { }) {
                val vbox = VBox().apply {
                    alignment = Pos.CENTER
                }

                vbox.add(Label(titleText).apply {
                    style(true) {
                        fontSize = Dimension(65.0, Dimension.LinearUnits.px)
                        fontWeight = FontWeight.BOLD
                        fontFamily = "Lato Bold"
                    }
                }.apply { vbox.titleBlock(this) })

                vbox.add(Label(subTitleText).apply {
                    style(true) {
                        fontSize = Dimension(25.0, Dimension.LinearUnits.px)
                        fontFamily = "Lato"
                    }
                }.apply { vbox.subTitleBlock(this) })

                push(vbox)
            }

        }

    }


    inner class AppSearch { // implement the everywhere search system

        private val engines = Engines()
        private val regexes = Regexes()


        fun submitUpdate(input: String) {
            if (input.length <= 2 && input.isNotBlank()) return

            panels.seeing.clear()

            if (input.isBlank() || engines.find(input.substringBefore(':', "")) != null) {
                Tracker.reset()
                showAsRight()

                panels.seeing += panels.panels
            } else {
                val results = panels.panels.filter {
                    it.showNamed(input)
                }

                panels.seeing += if (results.size > 1) {
                    showAsRight()
                    results
                } else {
                    showAsWrong()
                    listOf(panels.noResults)
                }
            }

            panels.noResults.hidePanel()
            panels.panels.forEach { it.hidePanel() }
            panels.seeing.forEach { it.showPanel() }
        }

        fun submitSearch(input: String) {
            val regex = regexes.find(input)

            when {
                regex != null -> {
                    regex.invoke(input)
                }
                else -> {
                    val base = engines.find(input.substringBefore(':')) ?: return
                    val site = base.replace("{search}", URLEncoder.encode(input.substringAfter(':'), "UTF-8"))

                    view.brow.load(site)
                }
            }
        }


        private fun showAsRight() {
            text.style {
                textFill = Color.BLACK
            }
        }

        private fun showAsWrong() {
            text.style {
                textFill = Color.RED
            }
        }


        private inner class Regexes {

            private val regexes = mutableMapOf<Regex, (String) -> Unit>()

            init {
                regexes[Regex("((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)")] = {
                    view.brow.load(it)

                    text.text = view.brow.brow.url
                    search.submitUpdate("")
                }
                regexes[Regex("(.+(\\.pdf))")] = {
                    view.brow.load(it)
                }
            }


            fun find(input: String): ((String) -> Unit)? {
                return regexes.entries.find { it.key.matches(input) }?.value
            }

        }

        private inner class Engines {

            private val engines = mutableMapOf<String, String>()

            init {
                engines["google"] = "https://www.google.com/search?q={search}"
                engines["bb"] = "https://www.bestbuy.com/site/searchpage.jsp?st={search}"
                engines["bbs"] = "https://stores.bestbuy.com/{search}"
                engines["amazon"] = "https://www.amazon.com/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords={search}"
                engines["reddit"] = "https://www.reddit.com/search?q={search}"
            }


            fun find(input: String): String? {
                return engines[input.toLowerCase().trim()]
            }

        }

    }


    private object Tracker {

        private var current: MainWindow.AppPanels.Panel? = null

        fun reset() {
            current = null
        }

        fun checkCurrent(other: MainWindow.AppPanels.Panel): Boolean {
            if (current !== other) {
                current = other
                return true
            }

            return false
        }

    }

}