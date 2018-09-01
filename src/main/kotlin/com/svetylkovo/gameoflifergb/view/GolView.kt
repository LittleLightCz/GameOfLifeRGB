package com.svetylkovo.gameoflifergb.view

import com.svetylkovo.gameoflifergb.controller.GolController
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Parent
import javafx.scene.image.Image
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import tornadofx.*
import java.io.File


class GolView : View("Game of Life RGB") {

    private val controller by inject<GolController>()

    private val image = SimpleObjectProperty<Image?>()

    override val root = vbox {
        paddingAll = 5.0
        spacing = 2.0

        imageview(image)

        button("Next step").action {
            launch(JavaFx) {
                controller.nextStep(image)
            }
        }
    }

    init {
        image.set(Image(File("c:\\Downloads\\Tmp\\klikatice.jpg").inputStream()))
    }
}