fun getInputData(): List<String> = getInputText(4).toList().filter { it != "\n" }

fun getTestData(): List<String> = """
    7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

    22 13 17 11  0
     8  2 23  4 24
    21  9 14 16  7
     6 10  3 18  5
     1 12 20 15 19
    
     3 15  0  2 22
     9 18 13 17  5
    19  8  7 25 23
    20 11 10 24  4
    14 21 16 12  6
    
    14 21 17 24  4
    10 16 15  9 19
    18  8 23 26 20
    22 11 13  6  5
     2  0 12  3  7
    """
    .trimIndent()
    .split("\n")
    .filter { it != "\n" }

fun test() {
    val testInput = getTestData()
    println("=== Test Suite ===")
    assertTestCase("Part One") { partOne(testInput) == 4512 }
    assertTestCase("Part Two") { partTwo(testInput) == 1924 }
}

/** A class that represents a bingo card (or board). */
class Board(layout: String, val id: Any = "aBoard") {

    /** A data class that represents a cell on the bingo card. */
    data class Cell(val row: Int, val column: Int, val value: Int)

    private val moveStack: MutableList<Cell> = mutableListOf()
    private val marked: MutableList<Cell> = mutableListOf()
    private val unmarked: MutableList<Cell> = mutableListOf()

    private val markedRows = MutableList(5) { 0 }
    private val markedCols = MutableList(5) { 0 }

    init {
        val rows = layout.split("\n")
        rows.forEachIndexed { rowIdx, row ->
            val columns = row.split("\\s+".toRegex()).filter { it.matches("\\d+".toRegex()) }
            columns.forEachIndexed { cellIdx, cell -> unmarked.add(Cell(rowIdx, cellIdx, cell.toInt())) }
        }
    }

    /** Whether the bingo card is in a winning state. */
    val isWinning: Boolean
        get() = markedRows.contains(5) || markedCols.contains(5)

    /** Mark the specified number on the card.
     *
     * @param value The number on the card to mark.
     */
    fun move(value: Int) {
        val cell = unmarked.find { it.value == value } ?: return
        moveStack.add(cell); marked.add(cell); unmarked.remove(cell)
        markedRows[cell.row] += 1; markedCols[cell.column] += 1
    }

    /** Returns the number that caused the board to win and the sum of the unmarked cells. */
    fun getWinAndUnmarkedSum(): Pair<Int, Int> {
        return Pair(moveStack.last().value, unmarked.sumOf { it.value })
    }
}

/** Returns the moves to be made on the boards and a list of generated boards.
 *
 * @param configuration The list of strings that define the configuration of the game.
 */
fun createConfigurations(configuration: List<String>): Pair<MutableList<Int>, MutableList<Board>> {
    val moves = configuration.first().split(",").map { it.toInt() }.toMutableList()
    val boards = mutableListOf<Board>()

    configuration.drop(1).windowed(6, 6) {
        val layout = it.joinToString("\n")
        boards.add(Board(layout.trim(), id = boards.count() + 1))
    }
    println("${boards.count()} boards parsed and generated.")
    return Pair(moves, boards)
}

/** Returns a list of boards in the order they were solved.
 *
 * @param moves The list of moves to run on all the boards
 * @param boards The list of boards to tracking the winning state of
 */
fun solveBoards(moves: MutableList<Int>, boards: MutableList<Board>): MutableList<Board> {
    val solved = mutableListOf<Board>()
    while (moves.isNotEmpty()) {
        val move = moves.removeFirst()
        boards.forEach { it.move(move) }

        boards.filter { it.isWinning }.forEach {
            solved.add(it)
            boards.remove(it)
        }
    }
    return solved
}

/** Part One */
fun partOne(configuration: List<String>): Int {
    val (moves, boards) = createConfigurations(configuration)
    val solved = solveBoards(moves, boards)

    val (winningMove, unmarkedSum) = solved.first().getWinAndUnmarkedSum()
    val id = solved.first().id
    println("Winning board $id has unmarked sum of $unmarkedSum from winning move of $winningMove")
    println("The final score for this board: ${winningMove * unmarkedSum}")

    return winningMove * unmarkedSum
}

/** Part Two */
fun partTwo(configuration: List<String>): Int {
    val (moves, boards) = createConfigurations(configuration)
    val solved = solveBoards(moves, boards)

    val (winningMove, unmarkedSum) = solved.last().getWinAndUnmarkedSum()
    val id = solved.last().id
    println("Winning board $id has unmarked sum of $unmarkedSum from winning move of $winningMove")
    println("The final score for this board: ${winningMove * unmarkedSum}")

    return winningMove * unmarkedSum
}

fun main() {
    val mainInput = getInputData()
    partOne(mainInput)
    partTwo(mainInput)
}

test()
main()