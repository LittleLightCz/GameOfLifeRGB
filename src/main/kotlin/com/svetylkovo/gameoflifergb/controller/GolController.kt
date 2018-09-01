package com.svetylkovo.gameoflifergb.controller

import gol.GameOfLife
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import tornadofx.Controller


class GolController : Controller() {

    private val gameOfLife = GameOfLife()

    suspend fun nextStep(imageProp: SimpleObjectProperty<Image?>) {
        imageProp.get()?.let { image ->
            val newImage = gameOfLife.computeNextStep(image)
            imageProp.set(newImage)
        }
    }

}