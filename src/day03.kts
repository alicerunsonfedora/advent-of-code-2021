fun getInputData(): List<String> = getInputText(3).toList()

fun getTestData(): List<String> = """
    00100
    11110
    10110
    10111
    10101
    01111
    00111
    11100
    10000
    11001
    00010
    01010
    """.trimIndent()
    .split("\n")

/** Returns the rating from a list of report entries.
 *
 * @param report The list of entries to return the rating from
 * @param observedIdx The starting index to look at the bits from
 * @param criteria A closure that compares the counts of the number of zeroes and ones to make a distinction.
 *
 * @return The integer representation of the reported entry that matches the rating criteria.
 */
fun getRating(report: List<String>, observedIdx: Int = 0, criteria: (Int, Int) -> Char): Int {
    if (report.count() == 1)
        return report.first().toInt(radix = 2)

    val charCounts = report.map { it[observedIdx] }.groupingBy { it }.eachCount()
    val selector = criteria(charCounts['0'] ?: 0, charCounts['1'] ?: 0)
    return getRating(report.filter { item -> item[observedIdx] == selector }, observedIdx + 1, criteria)
}

fun test() {
    val testInput = getTestData()
    println("=== Test Suite ===")

    assertTestCase("Part One") {
        partOne(testInput) == 198
    }

    assertTestCase("Part Two") {
        partTwo(testInput) == 230
    }
}

/** Part One */
fun partOne(report: List<String>): Int {
    val bitNumLength = report[0].count()
    val bitNumber = MutableList(bitNumLength) { 0 }

    var idx = 0
    while (idx < bitNumLength) {
        val bits = report.map { bitString -> bitString[idx] }
        bitNumber[idx] = (bits.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key ?: '0').digitToInt()
        idx += 1
    }

    val gamma = bitNumber.joinToString("").toInt(radix = 2)
    val epsilon = bitNumber.map { if (it == 1) 0 else 1 }.joinToString("").toInt(radix = 2)

    println("Total power consumption: ${gamma * epsilon}")
    return gamma * epsilon
}

/** Part Two */
fun partTwo(report: List<String>): Int {
    val oxygen = getRating(report) { zeroCounts, oneCounts ->
        if (zeroCounts > oneCounts) '0' else '1'
    }

    val scrubber = getRating(report) { zeroCounts, oneCounts ->
        if (zeroCounts > oneCounts) '1' else '0'
    }

    println("Life support rating: ${oxygen * scrubber}")
    return oxygen * scrubber
}

fun main() {
    val mainInput = getInputData()
    partOne(mainInput)
    partTwo(mainInput)
}

test()
main()