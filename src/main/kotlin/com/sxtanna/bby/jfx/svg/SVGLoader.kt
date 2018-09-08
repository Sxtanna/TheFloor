package com.sxtanna.bby.jfx.svg

import com.sxtanna.bby.jfx.BBYWJfx
import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.ImageView
import javafx.scene.paint.Color
import org.apache.batik.anim.dom.SVGDOMImplementation
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.TranscodingHints
import org.apache.batik.transcoder.image.ImageTranscoder
import org.apache.batik.util.SVGConstants
import org.xml.sax.InputSource
import tornadofx.SVGIcon
import java.awt.image.BufferedImage
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class SVGLoader(val app: BBYWJfx) {

    private val factory = DocumentBuilderFactory.newInstance()


    fun load(path : String, size : Double = 20.0, color : Color = Color.BLACK) : SVGIcon {
        val text = app.resource(path).readText()
        return load0(text, size, color)
    }

    fun loadImage(path : String, size : Double = 20.0) : BufferedImage {
        return ApacheBatik.transcode(app.resource(path), size, size)
    }

    fun loadImageView(path : String, size : Double = 100.0) : ImageView {
        val image = loadImage(path, size)

        return ImageView(SwingFXUtils.toFXImage(image, null))
    }


    private fun load0(text : String, size : Double = 20.0, color : Color = Color.BLACK) : SVGIcon {

        val builder = factory.newDocumentBuilder()

        val doc = builder.parse(InputSource(text.byteInputStream()))
        doc.documentElement.normalize()

        val pathData = doc.getElementsByTagName("path").item(0).attributes.getNamedItem("d").textContent

        return SVGIcon(pathData, size, color)
    }


    object ApacheBatik {

        private val defaultHints = mapOf(
                ImageTranscoder.KEY_XML_PARSER_VALIDATING to false,
                ImageTranscoder.KEY_DOCUMENT_ELEMENT to SVGConstants.SVG_SVG_TAG,
                ImageTranscoder.KEY_DOM_IMPLEMENTATION to SVGDOMImplementation.getDOMImplementation(),
                ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI to SVGConstants.SVG_NAMESPACE_URI)


        fun transcode(resource : URL, width : Double, height : Double) : BufferedImage {

            val transcoder = Transcoder()

            val hints = TranscodingHints(defaultHints)
            hints[ImageTranscoder.KEY_WIDTH] = width.toFloat()
            hints[ImageTranscoder.KEY_HEIGHT] = height.toFloat()

            transcoder.transcodingHints = hints

            transcoder.transcode(TranscoderInput(resource.toExternalForm()), null)

            return transcoder.output
        }


        private class Transcoder : ImageTranscoder() {

            lateinit var output : BufferedImage


            override fun createImage(width : Int, height : Int) : BufferedImage {
                return BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
            }

            override fun writeImage(p0 : BufferedImage, p1 : TranscoderOutput?) {
                output = p0
            }

        }

    }

}