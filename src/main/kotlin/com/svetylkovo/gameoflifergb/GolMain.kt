package com.svetylkovo.gameoflifergb

import com.svetylkovo.gameoflifergb.view.GolView
import javafx.application.Application
import tornadofx.App


class GolApp : App(GolView::class)

fun main(args: Array<String>) {
    Application.launch(GolApp::class.java)
}
