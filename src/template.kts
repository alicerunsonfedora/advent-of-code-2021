import java.io.File

fun getInputData(): String = File("assets/dayXX.txt").readText()

fun getTestData(): String = """
    
    """.trimIndent()

fun test() {
    val testInput = getTestData()
    println("=== Test Suite ===")
    println("All test cases passed.\n\n")
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