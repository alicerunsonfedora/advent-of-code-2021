import kotlin.math.absoluteValue

fun getInputData(): List<Int> = getInputText(7).trim().split(",").map { it.toInt() }

fun getTestData(): List<Int> = """
    16,1,2,0,4,2,7,1,2,14
    """
    .trimIndent()
    .trim()
    .split(",")
    .map { it.toInt() }

fun test() {
    val testInput = getTestData()
    println("=== Test Suite ===")

    assertTestCase("Part One") {
        partOne(testInput) == 37
    }

    assertTestCase("Part Two") {
        partTwo(testInput) == 168
    }
}

/** Returns the Gaussian sum of itself [(n^2 + n) / 2]. */
fun Int.gaussianSum() = (this * (this + 1)) / 2

/** Part One */
fun partOne(data: List<Int>): Int {
    val maxPosition = data.maxOrNull() ?: 1
    val options = mutableMapOf<Int, List<Int>>()

    for (i in 1..maxPosition)
        options[i] = data.map { (it - i).absoluteValue }

    val result = options.minByOrNull { it.value.sum() } ?: return -1
    println("Option with most optimization is ${result.key} with cost of ${result.value.sum()}")
    return result.value.sum()
}

/** Part Two */
fun partTwo(data: List<Int>): Int {
    val maxPosition = data.maxOrNull() ?: 1
    val options = mutableMapOf<Int, List<Int>>()

    for (i in 1..maxPosition) {
        options[i] = data.map { (it - i).absoluteValue.gaussianSum() }
    }

    val result = options.minByOrNull { it.value.sum() } ?: return -1
    println("Option with most optimization is ${result.key} with cost of ${result.value.sum()}")
    return result.value.sum()
}

fun main() {
    val mainInput = getInputData()
    partOne(mainInput)
    partTwo(mainInput)
}

test()
main()