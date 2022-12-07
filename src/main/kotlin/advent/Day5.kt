package advent

import ResourceFetcher
import checkValidMatch

fun main(args: Array<String>) {
    Day5.part2()
}

object Day5 {
    private val moveRegex = Regex("move (\\d+) from (\\d+) to (\\d+)")
    fun part1() {
        var stackComplete = false
        val stacks = mutableListOf<MutableList<Char>>()
        ResourceFetcher.forEachLine("input/day_5.txt") { row ->
            when{
                !stackComplete -> {
                    if (row.all { it.isDigit() || it.isWhitespace() }) {
                        stackComplete = true
                    } else {
                        row.chunked(4).forEachIndexed { index, byte ->
                            if(byte.isNotBlank()) {
                                // for index of 4, size should be 5
                                while(stacks.size <= index) stacks.add(mutableListOf())
                                stacks[index].add(0, byte.first{ it.isLetter() })
                            }
                        }
                    }
                }
                row.isNotBlank() -> {
                    moveRegex.checkValidMatch(row){ (c, fi, ti) ->
                        val count = c.toInt()
                        val fromIndex = fi.toInt() - 1
                        val toIndex = ti.toInt() - 1

                        repeat(count) {
                            stacks[toIndex].add(stacks[fromIndex].removeLast())
                        }
                    }
                }
            }
        }

        val answer = stacks.fold("") { message, stack ->
            message + if(stack.isEmpty())" " else stack.last()
        }
        println(answer)
    }

    fun part2() {
        var stackComplete = false
        val stacks = mutableListOf<MutableList<Char>>()
        ResourceFetcher.forEachLine("input/day_5.txt") { row ->
            when{
                !stackComplete -> {
                    if (row.all { it.isDigit() || it.isWhitespace() }) {
                        stackComplete = true
                    } else {
                        row.chunked(4).forEachIndexed { index, byte ->
                            if(byte.isNotBlank()) {
                                // for index of 4, size should be 5
                                while(stacks.size <= index) stacks.add(mutableListOf())
                                stacks[index].add(0, byte.first{ it.isLetter() })
                            }
                        }
                    }
                }
                row.isNotBlank() -> {
                    moveRegex.checkValidMatch(row){ (c, fi, ti) ->
                        val count = c.toInt()
                        val fromIndex = fi.toInt() - 1
                        val toIndex = ti.toInt() - 1

                        val removed = mutableListOf<Char>().apply {
                            repeat(count){
                                add(0, stacks[fromIndex].removeLast())
                            }
                        }
                        stacks[toIndex].addAll(removed)
                    }
                }
            }
        }

        val answer = stacks.fold("") { message, stack ->
            message + if(stack.isEmpty())" " else stack.last()
        }
        println(answer)
    }
}