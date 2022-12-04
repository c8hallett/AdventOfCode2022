package advent

import ResourceFetcher

object Day1 {
    fun invoke() {
        val elfTotals = mutableListOf<Int>()
        var currentTotal = 0
        ResourceFetcher.forEachLine("day_1_input.txt") { calorieCount ->
            when {
                calorieCount.isBlank() -> {
                    elfTotals.add(currentTotal)
                    currentTotal = 0
                }
                else -> currentTotal += calorieCount.toInt()
            }
        }

        println(elfTotals.sorted().takeLast(3).sum())
    }
}