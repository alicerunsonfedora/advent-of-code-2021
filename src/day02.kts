enum class Movement { FORWARD, DOWN, UP, UNKNOWN }

data class MovementCommand(val direction: Movement, val amount: Int)

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

fun String.toMovementCommand(): Movement {
    return when(this) {
        "forward" -> Movement.FORWARD
        "up" -> Movement.UP
        "down" -> Movement.DOWN
        else -> Movement.UNKNOWN
    }
}

fun parseCommands(commandList: List<String>): List<MovementCommand> = commandList.map { source ->
    val args = source.split(" ")
    MovementCommand(args[0].toMovementCommand(), args[1].toInt())
}

fun track(commands: List<MovementCommand>): Pair<Int, Int> {
    var breadth = 0 ; var depth = 0
    commands.forEach { command ->
        when (command.direction) {
            Movement.FORWARD -> breadth += command.amount
            Movement.UP -> depth -= command.amount
            Movement.DOWN -> depth += command.amount
            else -> {}
        }
    }
    println("Tracking of ${commands.count()} commands complete.")
    println("Breadth: $breadth\tDepth: $depth")
    return Pair(breadth, depth)
}

fun trackWithAim(commands: List<MovementCommand>): Triple<Int, Int, Int> {
    var breadth = 0 ; var depth = 0 ; var aim = 0
    commands.forEach { command ->
        when (command.direction) {
            Movement.FORWARD -> {
                breadth += command.amount
                depth += (aim * command.amount)
            }
            Movement.UP -> aim -= command.amount
            Movement.DOWN -> aim += command.amount
            else -> {}
        }
    }
    println("Corrected tracking of ${commands.count()} commands complete.")
    println("Breadth: $breadth\tDepth: $depth\tAim: $aim")
    return Triple(breadth, depth, aim)
}

fun partOne(inputData: List<String>) {
    val (breadth, depth) = track(parseCommands(inputData))
    println(breadth * depth)
}

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