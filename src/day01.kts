import java.io.File


fun getInputData(): List<Int> = File("assets/day01.txt")
    .readText()
    .trim()
    .split("\n")
    .map { it.toInt() }

fun getTestData(): List<Int> = """
        199
        200
        208
        210
        200
        207
        240
        269
        260
        263
    """.trimIndent()
    .split("\n")
    .map(String::toInt)


fun List<Int>.getDeltaChanges(): Pair<Int, Int> {
    var increased = 0; var decreased = 0
    var previousValue = first()

    drop(1).forEach { measurement ->
        if (measurement > previousValue) {
            increased += 1
        } else {
            decreased += 1
        }
        previousValue = measurement
    }

    return Pair(increased, decreased)
}


fun partOne(measurements: List<Int>): Int {
    val increased = measurements.getDeltaChanges().first
    println("Number of total increases from previous measurements: $increased")
    return increased
}

fun partTwo(measurements: List<Int>): Int {
    var slidingWindowStart = 0
    var slidingWindowEnd = 2

    val sums = mutableListOf<Int>()

    while (slidingWindowEnd < measurements.count()) {
        sums.add(measurements.slice(slidingWindowStart..slidingWindowEnd).sum())
        slidingWindowStart += 1
        slidingWindowEnd += 1
    }

    val increased = sums.getDeltaChanges().first
    println("Number of total increases from previous sliding windows: $increased")
    return increased
}

fun test() {
    val testInput = getTestData()
    println("=== Test Suite ===")
    assert(partOne(testInput) == 7)
    assert(partTwo(testInput) == 5)
}

fun main() {
    val measurements = getInputData()
    partOne(measurements)
    partTwo(measurements)
}

test()
main()