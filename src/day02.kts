/** A data class that represents a movement command with a direction and an amount. */
data class MovementCommand(val direction: String, val amount: Int)

fun getInputData(): List<String> = getInputText(2).toList()

fun getTestData(): List<String> = """
    forward 5
    down 5
    forward 8
    up 3
    down 8
    forward 2
    """.trimIndent()
    .split("\n")


/** Returns a list of movement commands by parsing the list of strings for valid commands. */
fun parseCommands(commandList: List<String>): List<MovementCommand> = commandList.map { source ->
    val args = source.split(" ")
    MovementCommand(args[0], args[1].toInt())
}

/** Returns a pair of the overall breadth (horizontal movement) and depth by tracking the directions. */
fun track(commands: List<MovementCommand>): Pair<Int, Int> {
    val breadth = commands.filter { it.direction == "forward" }.sumOf { it.amount }

    val depth = commands.fold(0) { accumulated, command ->
        when (command.direction) {
            "up" -> accumulated - command.amount
            "down" -> accumulated + command.amount
            else -> accumulated
        }
    }

//  var breadth = 0; var depth = 0
//    commands.forEach { command ->
//        when (command.direction) {
//            "forward" -> breadth += command.amount
//            "up" -> depth -= command.amount
//            "down" -> depth += command.amount
//            else -> {}
//        }
//    }

    println("Tracking of ${commands.count()} commands complete.")
    println("Breadth: $breadth\tDepth: $depth")
    return Pair(breadth, depth)
}

/** Returns a triple of the overall breadth (horizontal movement), depth, and aim by tracking the directions. */
fun trackWithAim(commands: List<MovementCommand>): Triple<Int, Int, Int> {
    val breadth = commands.filter { it.direction == "forward" }.sumOf { it.amount }
    var aim = 0; var depth = 0

    commands.forEach { command ->
        when (command.direction) {
            "forward" -> depth += aim * command.amount
            "up" -> aim -= command.amount
            "down" -> aim += command.amount
            else -> {}
        }
    }

//    var breadth = 0; var depth = 0 ; var aim = 0
//    commands.forEach { command ->
//        when (command.direction) {
//            "forward" -> {
//                breadth += command.amount
//                depth += (aim * command.amount)
//            }
//            "up" -> aim -= command.amount
//            "down" -> aim += command.amount
//            else -> {}
//        }
//    }

    println("Corrected tracking of ${commands.count()} commands complete.")
    println("Breadth: $breadth\tDepth: $depth\tAim: $aim")
    return Triple(breadth, depth, aim)
}

/** Part One */
fun partOne(inputData: List<String>) {
    val (breadth, depth) = track(parseCommands(inputData))
    println(breadth * depth)
}

/** Part Two */
fun partTwo(inputData: List<String>) {
    val (breadth, depth, _) = trackWithAim(parseCommands(inputData))
    println(breadth * depth)
}

fun test() {
    val testInput = getTestData()
    println("=== Test Suite ===")

    assertTestCase("Part One") {
        val (firstTestBreadth, firstTestDepth) = track(parseCommands(testInput))
        firstTestBreadth * firstTestDepth == (15 * 10)
    }

    assertTestCase("Part Two") {
        val (secondTestBreadth, secondTestDepth, _) = trackWithAim(parseCommands(testInput))
        secondTestBreadth * secondTestDepth == (15 * 60)
    }
}

fun main() {
    val mainInput = getInputData()
    partOne(mainInput)
    partTwo(mainInput)
}

test()
main()