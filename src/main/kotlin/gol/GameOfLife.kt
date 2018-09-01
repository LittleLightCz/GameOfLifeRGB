package gol

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import kotlinx.coroutines.async


class GameOfLife(val deadPixelThreshold: Double) {

    suspend fun computeNextStep(input: Image): Image {
        val width = input.width.toInt()
        val height = input.height.toInt()

        val pixelReader = input.pixelReader

        val colors = Array(width) { x ->
            Array(height) { y -> pixelReader.getColor(x, y) }
        }

        val output = WritableImage(width, height)

        val pixelWriter = output.pixelWriter

        val redsDef = async {
            computeNextStep(colors.extractColor { it.red })
        }

        val greensDef = async {
            computeNextStep(colors.extractColor { it.green })
        }

        val bluesDef = async {
            computeNextStep(colors.extractColor { it.blue })
        }

        val reds = redsDef.await()
        val greens = greensDef.await()
        val blues = bluesDef.await()

        for (x in 0 until width) {
            for (y in 0 until height) {
                val color = Color(reds[x][y], greens[x][y], blues[x][y], 1.0)
                pixelWriter.setColor(x, y, color)
            }
        }

        return output
    }

    private fun computeNextStep(pixels: Array<DoubleArray>): Array<DoubleArray> {
        return Array(pixels.size) { x ->
            DoubleArray(pixels[0].size) { y -> computeNextStepForPixel(x, y, pixels) }
        }
    }

    private fun computeNextStepForPixel(x: Int, y: Int, pixels: Array<DoubleArray>): Double {

        val pixel = pixels[x][y]
        val surrounding = getSurroundingPixels(x, y, pixels)

        val (aliveSurrounding, deadSurrounding) = surrounding.partition { it > deadPixelThreshold }
        val aliveSurroundingCount = aliveSurrounding.size

        val alivePixel = if (aliveSurrounding.isNotEmpty()) aliveSurrounding.random() else pixel
        val deadPixel = if (deadSurrounding.isNotEmpty()) deadSurrounding.random() else pixel

        return if (pixel < deadPixelThreshold) when (aliveSurroundingCount) {
            3 -> alivePixel
            else -> pixel
        }
        else when {
            aliveSurroundingCount < 2 -> deadPixel
            aliveSurroundingCount > 3 -> deadPixel
            else -> pixel
        }
    }

    private fun getSurroundingPixels(origX: Int, orixY: Int, pixels: Array<DoubleArray>): List<Double> {
        val width = pixels.size
        val height = pixels[0].size

        return (origX - 1..origX + 1).asSequence().map { x ->
            (orixY - 1..orixY + 1)
                .asSequence()
                .filterNot { y -> x == origX && y == orixY }
                .filter { y -> x >= 0 && y >= 0 }
                .filter { y -> x < width && y < height }
                .map { y -> pixels[x][y] }
        }.flatten().toList()
    }

}

private inline fun Array<Array<Color>>.extractColor(block: (Color) -> Double) = map { row ->
    row.map(block).toDoubleArray()
}.toTypedArray()
