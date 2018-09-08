package processing.core

var PShapeSVG.opacity: Float
    get() = this.fillOpacity
    set(value) = this.setFillOpacity(value.toString())