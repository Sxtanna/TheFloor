package com.sxtanna.bby.base

import javafx.beans.binding.DoubleBinding
import javafx.beans.binding.DoubleExpression
import javafx.beans.property.DoubleProperty
import javafx.beans.value.ObservableDoubleValue
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Paint
import tornadofx.anchorpaneConstraints
import tornadofx.doubleBinding

fun backgroundFill(paint : Paint, radii : CornerRadii = CornerRadii.EMPTY, insets : Insets = Insets.EMPTY) : Background {
    return Background(BackgroundFill(paint, radii, insets))
}


fun <N : Node> N.anchorAllSides(value: Double = 0.0): N {
    return anchorpaneConstraints {
        topAnchor = value
        leftAnchor = value
        bottomAnchor = value
        rightAnchor = value
    }
}


fun DoubleBinding.min(value: Double): ObservableDoubleValue {
    return this.doubleBinding { (it?.toDouble() ?: 0.0).coerceAtLeast(value) }
}

fun DoubleBinding.max(value: Double): ObservableDoubleValue {
    return this.doubleBinding { (it?.toDouble() ?: 0.0).coerceAtMost(value) }
}

fun DoubleBinding.within(min: Double, max: Double): ObservableDoubleValue {
    return this.doubleBinding { (it?.toDouble() ?: 0.0).coerceIn(min, max) }
}

fun DoubleBinding.max(property: DoubleExpression): ObservableDoubleValue {
    return this.doubleBinding { (it?.toDouble() ?: 0.0).coerceAtMost(property.value) }
}


fun DoubleProperty.min(value: Double): ObservableDoubleValue {
    return this.doubleBinding {
        println("Coercing $it")
        (it?.toDouble() ?: 0.0).coerceAtLeast(value)
    }
}

fun DoubleProperty.max(value: Double): ObservableDoubleValue {
    return this.doubleBinding { (it?.toDouble() ?: 0.0).coerceAtMost(value) }
}

fun DoubleProperty.within(min: Double, max: Double): ObservableDoubleValue {
    return this.doubleBinding { (it?.toDouble() ?: 0.0).coerceIn(min, max) }
}