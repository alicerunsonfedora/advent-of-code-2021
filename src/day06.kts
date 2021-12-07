import java.io.File

fun getInputData(): List<Int> = getInputText(6).trim().split(",").map { it.toInt() }

fun getTestData(): List<Int> = """
    3,4,3,1,2
    """.trimIndent()
    .trim()
    .split(",")
    .map { it.toInt() }

fun test() {
    val testInput = getTestData()
    println("=== Test Suite ===")
    println("Testing Data: $testInput\n")

    assertTestCase("Part One") {
        partOne(testInput) == 5934
    }

    assertTestCase("Part Two") {
        val result = partTwo(testInput)
        if (result != 26984457539)
            println("Difference of results: ${result - 26984457539}")
        result == 26984457539
    }
}

/** Returns the generation that follows the one specified. */
fun updateGeneration(timers: List<Int>): List<Int> {
    val newTimers = timers.toMutableList()
    timers.forEachIndexed { idx, value ->
        if (value <= 0) {
            newTimers[idx] = 6
            newTimers.add(8)
        } else {
            newTimers[idx] -= 1
        }
    }
    return newTimers.toList()
}

/** Run a simulation of fish aging from an initial configuration for a number of iterations. */
fun runSimulation(initConfig: List<Int>, iterations: Int): List<Int> {
    var simulation = initConfig.toList()
    for (i in 1..iterations) {
        simulation = updateGeneration(simulation)
    }
    return simulation
}

/** Write the contents of the first eighteen iterations to a CSV file.
 *
 * This is used for debugging purposes.
 * */
fun writeCSV(data: List<Int>) {
    var csvData = "Iteration,FishCount,DeadFish,NewFish,RunningDeadTally,RunningNewTally\n"
    var curr = data.toList()
    var runningDeadTotal = 0
    var runningNewTotal = 0
    for (i in 0..18) {
        runningDeadTotal += curr.count { it == 0 }
        runningNewTotal += curr.count { it == 8 }
        csvData += "$i,${curr.count() - 5},${curr.count { it == 0 }},${curr.count { it == 8 }},"
        csvData += "$runningDeadTotal,$runningNewTotal\n"
        curr = updateGeneration(curr)
    }
    with(File("fishRuns2.csv")) { writeText(csvData) }
}

/** Part One */
fun partOne(data: List<Int>): Int {
    val results = runSimulation(data, 80)
    println("Ran simulation for 80 generations.\nRemaining fish: ${results.count()}")
    return results.count()
}

/** Part Two
 *
 * `runSimulation` theoretically works here if we have infinite memory. However, the Java heap space isn't able to hold
 * all the data needed for each generation. The solution is to count the number of fish with a given age and run the
 * algorithm by shifting the lives over to the left in the list. For fish that have "died" already, the number of
 * fish with ages 6 and 8 add the number of dead fish to simulate a respawn.
 */
fun partTwo(data: List<Int>): Long {

    // Create the initial configuration for the zeroth generation.
    val zeroGen = mutableListOf(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L)
    for (i in 0..8) { zeroGen[i] = data.count { it == i }.toLong() }
    val generations = mutableListOf(zeroGen.toList())

    // Run through the specified iterations.
    for (i in 1..256) {

        // Grab a copy of the previous generation for reference and copy it to a new list for updates. This allows us to
        // make the new generation without worrying about concurrent modification issues.
        val prevGeneration = generations.last().toMutableList()
        val generation = prevGeneration.toMutableList()

        // Grab the number of fish who died in the previous run, which will be used to update the number of fish whose
        // ages are either six or eight.
        val prevNumDead = prevGeneration[0]

        // While we can use windowed(), it's a bit tricky to work with and doesn't provide indices, either. Instead, we
        // use a simple loop over the elements with the index included.
        prevGeneration.forEachIndexed { index, _ ->
            when (index) {
                // For those with ages of 6, perform the regular routine and then add the number of dead fish from the
                // previous round. This helps ensure accuracy when applying it in this order.
                6 -> {
                    generation[index] = generation[index + 1]
                    generation[index] += prevNumDead
                }

                // For those with ages of 8, set the value to zero unless there are dead fish from the previous run, as
                // fish with an age of eight will now be an age of seven.
                8 -> generation[index] = if (prevNumDead > 0) prevNumDead else 0

                // Otherwise, grab the age from the next age bracket to shift the list over by one.
                else -> generation[index] = generation[index + 1]
            }
        }
        generations.add(generation.toList())
    }

    println("Ran simulation for 256 generations.\nRemaining fish: ${generations[256].sum()}")
    return generations[256].sum()
}

fun main() {
    val mainInput = getInputData()
    partOne(mainInput)
    partTwo(mainInput)
}

test()
main()