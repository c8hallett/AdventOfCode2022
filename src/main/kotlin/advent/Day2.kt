package advent

import ResourceFetcher

fun main(args: Array<String>) {
    Day2.part1()
    Day2.part2()
}

object Day2 {
    private const val OFFSET_A = 'A'.code
    private const val OFFSET_X = 'X'.code
    private val moveLabels = listOf("rock", "paper", "scissors")
    private val outcomeLabel = listOf("loses", "draws", "wins")

    fun part1() {
        var totalScore = 0

        ResourceFetcher.forEachLine("input/day_2.txt") { round ->
            val moves = round.filterNot { it.isWhitespace() }
            val opponentMove = moves[0].code - OFFSET_A
            val suggestedMove = moves[1].code - OFFSET_X

            val outcome = ((suggestedMove - opponentMove) + 4) % 3

            log(suggestedMove, opponentMove, outcome)
            totalScore += suggestedMove + 1 + (outcome * 3)
        }
        println("total score = $totalScore")
    }

    fun part2() {
        var totalScore = 0

        ResourceFetcher.forEachLine("input/day_2.txt") { round ->
            val moves = round.filterNot { it.isWhitespace() }
            val opponentMove = moves[0].code - OFFSET_A
            val suggestedOutcome = moves[1].code - OFFSET_X

            val offset = suggestedOutcome % 3 - 1
            val suggestedMove = (opponentMove + offset + 3) % 3

            log(suggestedMove, opponentMove, suggestedOutcome)
            totalScore += suggestedMove + 1 + (suggestedOutcome * 3)
        }
        println("total score = $totalScore")
    }

    private fun log(suggestedMove: Int, opponentMove: Int, outcome: Int) {
        println("${moveLabels[suggestedMove]} ${outcomeLabel[outcome]} against ${moveLabels[opponentMove]}")
    }
}