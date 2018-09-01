package com.svetylkovo.gameoflifergb.controller

import gol.GameOfLife
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import tornadofx.Controller


class GolController : Controller() {

    suspend fun nextStep(
        imageProp: SimpleObjectProperty<Image?>,
        deadPixelThreshold: Double
    ) {
        imageProp.get()?.let { image ->
            val gameOfLife = GameOfLife(deadPixelThreshold)

            val newImage = gameOfLife.computeNextStep(image)
            imageProp.set(newImage)
        }
    }

}