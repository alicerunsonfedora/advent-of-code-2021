import kotlin.math.absoluteValue
import kotlin.math.max

fun getInputData(): List<String> = getInputText(5).toList()

fun getTestData(): List<String> = """
    0,9 -> 5,9
    8,0 -> 0,8
    9,4 -> 3,4
    2,2 -> 2,1
    7,0 -> 7,4
    6,4 -> 2,0
    0,9 -> 2,9
    3,4 -> 1,4
    0,0 -> 8,8
    5,5 -> 8,2
    """.trimIndent()
    .split("\n")

/** A data structure that represents a coordinate. */
data class Coordinate(val x: Int, val y: Int)

/** A data class that represents a range of coordinates. */
data class CoordinateRange(val start: Coordinate, val end: Coordinate) {

    /** An enumeration for the different axes types a coordinate range or line can form. */
    enum class Axis {
        Horizontal,
        Vertical,
        Downhill,
        Uphill,
        Unknown
    }

    override fun toString(): String = "CoordinateRange(${start.x},${start.y} -> ${end.x},${end.y}, ${axis})"

    /** The axis type of the range of coordinates. */
    val axis: Axis
        get() {
            return when {
                (start.x == end.x) -> Axis.Horizontal
                (start.y == end.y) -> Axis.Vertical
                (slope > 0) -> Axis.Downhill
                (slope < 0) -> Axis.Uphill
                else -> Axis.Unknown
            }
        }

    private val rise: Int
        get() = end.y - start.y

    private val run: Int
        get() = end.x - start.x

    private val slope: Float
        get() = rise.toFloat() / run.toFloat()

    /** Returns a list of coordinates that fit in the range, described by the axis type. */
    fun getAllCoords(): List<Coordinate> {
        val coords = mutableListOf<Coordinate>()
        when (axis) {
            Axis.Vertical -> {
                val order = listOf(start.x, end.x).sorted()
                for (i in order.first()..order.last()) coords.add(Coordinate(i, start.y))
            }
            Axis.Horizontal -> {
                val order = listOf(start.y, end.y).sorted()
                for (i in order.first()..order.last()) coords.add(Coordinate(start.x, i))
            }
            Axis.Downhill, Axis.Uphill -> {
                var nextCoordinate = start
                var totalIterationsNeeded = end.x - start.x
                if (totalIterationsNeeded < 0) totalIterationsNeeded *= -1

                val riseDirection = (rise / rise.absoluteValue)
                val runDirection = (run / run.absoluteValue)

                for (i in 0..totalIterationsNeeded) {
                    coords.add(nextCoordinate)
                    nextCoordinate = Coordinate(nextCoordinate.x + runDirection, nextCoordinate.y + riseDirection)
                }

            }
            else -> {}
        }
        return coords.toList()
    }
}

/** Returns a coordinate described by the string. */
fun String.toCoordinate(): Coordinate {
    val ints = split(",").map { it.toInt() }
    return Coordinate(ints[0], ints[1])
}

/** Returns a map containing coordinates from (0, 0) to (width, height). */
fun createCoordinateMap(width: Int, height: Int): MutableMap<Coordinate, Int> {
    val coordinates = mutableMapOf<Coordinate, Int>()
    for (x in 0..width) {
        for (y in 0..height) {
            coordinates[Coordinate(x, y)] = 0
        }
    }
    return coordinates
}

/** Calculates the number of times any line overlaps another. */
fun calculateOverlaps(coordinateRanges: List<CoordinateRange>, drawMap: Boolean): Int {
    val coordinateRangeGroups = coordinateRanges.flatMap { listOf(it.start, it.end) }
    val floorHeight = max(coordinateRangeGroups.maxOf { it.x }, coordinateRangeGroups.maxOf { it.y })
    val coordinateMap = createCoordinateMap(floorHeight, floorHeight)

    coordinateRanges.forEach { range ->
        range.getAllCoords().forEach { coordinateMap[it] = coordinateMap[it]?.plus(1) ?: 0 }
    }

    if (drawMap) {
        drawCoordinateMap(floorHeight, coordinateMap)
    }

    val overlap = coordinateMap.values.count { it > 1 }
    println("There are $overlap lines in the coordinate map that overlap.")

    return overlap
}

/** Prints a graphical representation of a coordinate map, with dots representing no lines and numbers representing
 * the number of lines that cover a coordinate point.
 */
fun drawCoordinateMap(floorHeight: Int, coordinateMap: MutableMap<Coordinate, Int>) {
    for (i in 0..floorHeight) {
        var diagramRow = ""
        for (j in 0..floorHeight) {
            val numOverlaps = coordinateMap[Coordinate(j, i)] ?: 0
            diagramRow += if (numOverlaps >= 1) numOverlaps else "."
        }
        println(diagramRow)
    }
}

fun test() {
    val testInput = getTestData()
    println("=== Test Suite ===")
    assertTestCase("Part One") { partOne(testInput, drawMap = true) == 5 }
    assertTestCase("Part Two") { partTwo(testInput, drawMap = true) == 12 }
//    assertTestCase("Custom Part") {
//        partOne(listOf("1,1 -> 3,3", "9,7 -> 7,9"), drawMap = true) == 0
//    }
}

/** Part One */
fun partOne(ventLines: List<String>, drawMap: Boolean = false): Int {
    val coordinateRanges = ventLines
        .map { it.split("\\s+->\\s+".toRegex()) }
        .map { CoordinateRange(it[0].toCoordinate(), it[1].toCoordinate()) }
        .filter { listOf(CoordinateRange.Axis.Horizontal, CoordinateRange.Axis.Vertical).contains(it.axis) }
    return calculateOverlaps(coordinateRanges, drawMap)
}

/** Part Two */
fun partTwo(ventLines: List<String>, drawMap: Boolean = false): Int {
    val coordinateRanges = ventLines
        .map { it.split("\\s+->\\s+".toRegex()) }
        .map { CoordinateRange(it[0].toCoordinate(), it[1].toCoordinate()) }
    return calculateOverlaps(coordinateRanges, drawMap)
}

fun main() {
    val mainInput = getInputData()
    partOne(mainInput)
    partTwo(mainInput)
}

test()
main()