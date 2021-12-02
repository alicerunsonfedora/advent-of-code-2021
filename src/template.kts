fun getInputData(): String = getInputText(99)

fun getTestData(): String = """
    
    """.trimIndent()

fun test() {
    val testInput = getTestData()
    println("=== Test Suite ===")

    assertTestCase("Part One") {
        // FIXME: Replace 'true' with the condition(s) outlined in the challenge!
        true
    }

    assertTestCase("Part Two") {
        // FIXME: Replace 'true' with the condition(s) outlined in the challenge!
        true
    }
}

fun partOne() {

}

fun partTwo() {

}

fun main() {
    val mainInput = getInputData()
    partOne()
    partTwo()
}

test()
main()