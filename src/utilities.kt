import java.io.File

/** A typealias that represents input data for a given challenge.*/
typealias InputData = String

/** Returns the text contents of an input file from the assets folder. */
fun getInputText(day: Int): InputData = File("assets/day%02d.txt".format(day)).readText()

/** Convert an input data to a string, splitting at newline characters. */
fun InputData.toList(): List<String> = trim().split("\n")

/** Verify that a test case is correct. */
fun assertTestCase(testName: String, condition: () -> Boolean) {
    println(if (condition()) "Test case '$testName' passed.\n" else "Test case '$testName' failed.\n")
}