package com.svetylkovo.gameoflifergb.view

import com.svetylkovo.gameoflifergb.controller.GolController
import gol.GameOfLife
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import kotlinx.coroutines.Job
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import tornadofx.*
import java.io.File


class GolView : View("Game of Life RGB") {

    private val controller by inject<GolController>()

    private val deadPixelThreshold = SimpleDoubleProperty(0.5)
    private val resurrectionRate = SimpleDoubleProperty(0.01)

    private val working = SimpleBooleanProperty(false)
    private val image = SimpleObjectProperty<Image?>()

    private var gameOfLifeJob: Job? = null

    override val root = vbox {
        paddingAll = 5.0
        spacing = 2.0

        imageview(image)

        label(deadPixelThreshold.stringBinding { "Dead pixel threshold: ${"%.2f".format(it)}" })

        slider(0.0, 1.0, deadPixelThreshold.get()) {
            valueProperty().onChange { deadPixelThreshold.set(it) }
        }

        label(resurrectionRate.stringBinding { "Resurrection rate: ${"%.2f".format(it)}" })

        slider(0.0, 1.0, resurrectionRate.get()) {
            valueProperty().onChange { resurrectionRate.set(it) }
        }

        hbox {
            spacing = 2.0

            button("Run!") {
                disableWhen(working)

                action {
                    gameOfLifeJob = launch(JavaFx) {
                        working.set(true)

                        try {
                            val gameOfLife = GameOfLife(deadPixelThreshold.get(), resurrectionRate.get())

                            while (isActive) {
                                controller.nextStep(image, gameOfLife)
                            }
                        } finally {
                            working.set(false)
                        }
                    }
                }

            }

            button("Stop") {
                enableWhen(working)
                action { gameOfLifeJob?.cancel() }
            }
        }
    }

    init {
        image.set(Image(File("c:\\Downloads\\Tmp\\klikatice_small.jpg").inputStream()))
    }
}