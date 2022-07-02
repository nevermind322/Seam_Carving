package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


fun main(args: Array<String>) {

    val inName = args[args.indexOf("-in") + 1]
    val outName = args[args.indexOf("-out") + 1]
    val height = args[args.indexOf("-height") + 1].toInt()
    val widht = args[args.indexOf("-width") + 1].toInt()

    val file = File(inName)
    val image = ImageIO.read(file)

    val carver = SeamCarver(image)

   val res = carver.resize(height = height, width = widht)

    ImageIO.write(res, "png", File(outName))

}
