package com.svetylkovo.gameoflifergb.view

import com.svetylkovo.gameoflifergb.controller.GolController
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import tornadofx.*
import java.io.File


class GolView : View("Game of Life RGB") {

    private val controller by inject<GolController>()

    private val deadPixelThreshold = SimpleDoubleProperty(0.8)
    private val working = SimpleBooleanProperty(false)
    private val image = SimpleObjectProperty<Image?>()

    override val root = vbox {
        paddingAll = 5.0
        spacing = 2.0

        imageview(image)

        label(deadPixelThreshold.stringBinding{ "Dead pixel threshold: ${"%.2f".format(it)}" })

        slider(0.0, 1.0, deadPixelThreshold.get()) {
            valueProperty().onChange { deadPixelThreshold.set(it) }
        }

        button("Next step") {
            disableWhen(working)

            action {
                launch(JavaFx) {
                    working.set(true)

                    try {
                        controller.nextStep(image, deadPixelThreshold.get())
                    } finally {
                        working.set(false)
                    }
                }
            }
        }
    }

    init {
        image.set(Image(File("c:\\Downloads\\Tmp\\klikatice.jpg").inputStream()))
    }
}