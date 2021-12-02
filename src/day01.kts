fun getInputData(): List<Int> = getInputText(1).toList().map { it.toInt() }

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


/** Returns a pair of integers that represents the number of times the reading increases and decreases.*/
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

/** Part One */
fun partOne(measurements: List<Int>): Int {
    val increased = measurements.getDeltaChanges().first
    println("Number of total increases from previous measurements: $increased")
    return increased
}

/** Part Two */
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

    assertTestCase("Part One") {
        partOne(testInput) == 7
    }

    assertTestCase("Part Two") {
        partTwo(testInput) == 5
    }
}

fun main() {
    val measurements = getInputData()
    partOne(measurements)
    partTwo(measurements)
}

test()
main()