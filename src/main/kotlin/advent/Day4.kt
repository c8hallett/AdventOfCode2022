package advent

import ResourceFetcher
import checkValidMatch

fun main(args: Array<String>) {
    Day4.part1()
    Day4.part2()
}

object Day4 {
    private val rangeRegex = Regex("(\\d.*)-(\\d.*),(\\d.*)-(\\d.*)")
    fun part1() {
        var overlapCounter = 0
        ResourceFetcher.forEachLine("input/day_4.txt"){ line ->
            rangeRegex.checkValidMatch(line) { (s1, e1, s2, e2) ->
                val sequence1 = Pair(s1.toInt(), e1.toInt())
                val sequence2 = Pair(s2.toInt(), e2.toInt())
                if(sequence1.fullyOverlaps(sequence2)) overlapCounter += 1
            }
        }
        println(overlapCounter)
    }

    fun part2() {
        var overlapCounter = 0
        ResourceFetcher.forEachLine("input/day_4.txt"){ line ->
            rangeRegex.checkValidMatch(line) { (s1, e1, s2, e2) ->
                val sequence1 = Pair(s1.toInt(), e1.toInt())
                val sequence2 = Pair(s2.toInt(), e2.toInt())
                if(sequence1.overlaps(sequence2)) overlapCounter += 1
            }
        }
        println(overlapCounter)
    }

    private fun Pair<Int, Int>.fullyOverlaps(other: Pair<Int, Int>): Boolean {
        return when{
            // this might contain other
            this.first < other.first -> this.second >= other.second
            // other might contain this
            this.first > other.first -> other.second >= this.second
            // has not ruled down which might contain each other
            else -> this.second >= other.second || other.second >= this.second
        }
    }

    private fun Pair<Int, Int>.overlaps(other: Pair<Int, Int>): Boolean {
        return when{
            // this might overlap other
            this.first > other.first -> this.first <= other.second
            // other might contain this
            this.first < other.first -> other.first <= this.second
            // has not ruled down which might contain each other
            else -> this.first <= other.second || other.first <= this.second
        }
    }
}