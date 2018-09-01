package com.svetylkovo.gameoflifergb.controller

import gol.GameOfLife
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import tornadofx.Controller


class GolController : Controller() {

    suspend fun nextStep(
        imageProp: SimpleObjectProperty<Image?>,
        gameOfLife: GameOfLife
    ) {
        imageProp.get()?.let { image ->
            val newImage = gameOfLife.computeNextStep(image)
            imageProp.set(newImage)
        }
    }

}