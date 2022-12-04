package advent

import ResourceFetcher

object Day3 {
    private const val OFFSET_A_LOWER = 'a'.code // 97
    private const val OFFSET_A_UPPER = 'A'.code // 65
    private val KEY = "ZYXWVUTSRQPONMLKJIHGFEDCBAzyxwvutsrqponmlkjihgfedcba".padStart(Long.SIZE_BITS, '#')
    private val WHITESPACE = Regex("\\s")

    fun part1() {
        var totalPriority = 0
        ResourceFetcher.forEachLine("day_3_input.txt") { rucksack ->
            val half = rucksack.length / 2
            val r1 = rucksack.take(half)
            val r2 = rucksack.takeLast(half)
            val sharedItems = r1.toPriorityBytes() and r2.toPriorityBytes()

            println(KEY)
            println(sharedItems.toBinary())

            totalPriority += sharedItems.sumPriorityBytes()
        }
        println("total = $totalPriority")
    }

    fun part2() {
        var totalPriority = 0
        ResourceFetcher.use("day_3_input.txt") {
            readLines().chunked(3).forEach { elfGroup ->
                println(KEY)
                val dumbBadge = elfGroup.fold(Long.MAX_VALUE){ previousBytes, ruckSack ->
                    previousBytes and ruckSack.toPriorityBytes()
                }
                println(dumbBadge.toBinary())
                println("================================")
                totalPriority += dumbBadge.sumPriorityBytes()
            }
        }
        println("total = $totalPriority")
    }

    private fun String.toPriorityBytes() = fold(0L) { previousBytes, currentChar ->
        previousBytes or currentChar.toPriorityByte()
    }

    private fun Long.toBinary(): String {
        return String.format("%${Long.SIZE_BITS}s", toString(2))
            .replace(WHITESPACE, "0")
    }

    private fun Long.sumPriorityBytes(): Int {
        var totalPriority = 0
        repeat(Long.SIZE_BITS){ priority ->
            if(this.shr(priority).takeLowestOneBit() == 1L) {
                totalPriority += priority + 1
            }
        }
        return totalPriority
    }

    private fun Char.toPriorityByte(): Long = 0x01.toLong() shl priority()

    private fun Char.priority(): Int {
        return when{
            code < OFFSET_A_LOWER -> code - OFFSET_A_UPPER + 26
            else -> code - OFFSET_A_LOWER
        }
    }

}