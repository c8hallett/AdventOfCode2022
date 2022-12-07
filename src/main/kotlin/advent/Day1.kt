package advent

import ResourceFetcher

fun main(args: Array<String>) {
    Day1.invoke()
}

object Day1 {
    fun invoke() {
        val elfTotals = mutableListOf<Int>()
        var currentTotal = 0
        ResourceFetcher.forEachLine("input/day_1.txt") { calorieCount ->
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