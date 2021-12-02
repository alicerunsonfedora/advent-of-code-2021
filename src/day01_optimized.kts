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


/* Rather than using the original method from day01.kts, this version utilizes the windowed method on lists, which lets
 * you view a small window of numbers. JetBrains uses this in their official solution
 * (see https://blog.jetbrains.com/kotlin/2021/12/advent-of-code-2021-in-kotlin-day-1/). */


/** Part One */
fun partOne(measurements: List<Int>): Int {
    val increased = measurements.windowed(2).count { it[0] < it[1] }
    println("Number of total increases from previous measurements: $increased")
    return increased
}

/** Part Two */
fun partTwo(measurements: List<Int>): Int {
    val increased = measurements.windowed(4).count { it[0] < it[3] }
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