package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

fun BufferedImage.getEnergy(x: Int, y: Int): Double {
    var res: Double

    val delta_x: Double
    val delta_y: Double
    var _x = x
    var _y = y

    if (x == 0) _x = 1;
    if (y == 0) _y = 1
    if (x == width - 1) _x = width - 2
    if (y == height - 1) _y = height - 2
    val c1 = Color(getRGB(_x + 1, y))
    val c2 = Color(getRGB(_x - 1, y))
    val c3 = Color(getRGB(x, _y - 1))
    val c4 = Color(getRGB(x, _y + 1))

    delta_x = ((c1.red - c2.red).toDouble().pow(2)
            + (c1.green - c2.green).toDouble().pow(2)
            + (c1.blue - c2.blue).toDouble().pow(2))

    delta_y = ((c3.red - c4.red).toDouble().pow(2)
            + (c3.green - c4.green).toDouble().pow(2)
            + (c3.blue - c4.blue).toDouble().pow(2))
    res = delta_x + delta_y
    res = sqrt(res)

    return res
}

fun BufferedImage.calcEnergy(): List<List<Double>> {
    val res = MutableList(width) { MutableList(height) { 0.0 } }
    var energy: Double
    println(" w: $width h: $height")
    for (x in 0 until width)
        for (y in 0 until height) {
            energy = getEnergy(x, y)
            res[x][y] = energy
        }

    return res
}

