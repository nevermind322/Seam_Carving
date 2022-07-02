package seamcarving

import java.awt.image.BufferedImage

class SeamCarver(var image: BufferedImage) {

    private val energyMatrix
        get() = image.calcEnergy()

    private fun _findVertSeam(energyMatrix: List<List<Double>>): List<Pair<Int, Int>> {

        val width = energyMatrix.size
        val height = energyMatrix[0].size

        val minMatr = MutableList(width) { MutableList(height) { 0.0 } }
        for (y in 0 until height) {
            for (x in 0 until width) {
                minMatr[x][y] = if (y == 0) energyMatrix[x][y] else {
                    energyMatrix[x][y] + minOf(
                        minMatr[x][y - 1],
                        minMatr[x + 1 * (if (x != width - 1) 1 else 0)][y - 1],
                        minMatr[x - 1 * (if (x != 0) 1 else 0)][y - 1]
                    )
                }
            }
        }


        var minX = 0;

        for (x in 0 until width)
            minX =
                if (minMatr[x][height - 1] < minMatr[minX][height - 1]) x else minX

        var y: Int
        return List(height) { n ->
            y = height - 1 - n
            if (n == 0) {
                Pair(minX, y)
            } else {
                for (x in minX - 1 * (if (minX != 0) 1 else 0)..minX + 1 * (if (minX != width - 1) 1 else 0)) {
                    minX = if (minMatr[x][y] < minMatr[minX][y]) x else minX
                }
                Pair(minX, y)
            }

        }
    }

    fun findVertSeam(): List<Pair<Int, Int>> = _findVertSeam(energyMatrix)

    fun findHorSeam() : List<Pair<Int, Int>> = _findVertSeam(transpose(energyMatrix))

    fun resize(height: Int, width : Int) : BufferedImage{

        repeat(width){
            removeVertSeam(findVertSeam())
        }

        repeat(height){
            removeHorSeam(findHorSeam())
        }

        return image
    }

    private fun removeVertSeam(seam: List<Pair<Int, Int>>){



        val newMatr: MutableList<MutableList<Int>> = MutableList(image.height)
        {y->
            MutableList(image.width){x->
                image.getRGB(x,y)
            }
        }
        println(seam)
        seam.forEach{newMatr[it.second].removeAt(it.first)}
        image = BufferedImage(image.width -1, image.height, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until image.width  )
            for(y in 0 until image.height) {
                image.setRGB(x, y, newMatr[y][x])
            }

    }

    private fun removeHorSeam(seam: List<Pair<Int, Int>>){
        val newMatr: MutableList<MutableList<Int>> = MutableList(image.width)
        {x->
            MutableList(image.height){y->
                image.getRGB(x,y)
            }
        }
        seam.forEach{newMatr[it.second].removeAt(it.first)}
        image = BufferedImage(image.width , image.height - 1, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until image.width  )
            for(y in 0 until image.height) {
                image.setRGB(x, y, newMatr[x][y])
            }
    }

    companion object {
        fun <T> transpose(matrix: List<List<T>>) = List(matrix[0].size) { i -> List(matrix.size) { j -> matrix[j][i] } }

    }
}
