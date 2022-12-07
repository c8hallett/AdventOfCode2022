package advent

import ResourceFetcher

fun main(args: Array<String>) {
    Day6.part1()
    Day6.part2()
}

object Day6 {
    fun part1() {
        println(getEndOfPacket(4))
    }
    fun part2() {
        println(getEndOfPacket(14))
    }

    private fun getEndOfPacket(packetLength: Int): Int {
        return ResourceFetcher.with("input/day_6.txt") {
            readLine().windowed(packetLength, 1, partialWindows = false).indexOfFirst { packet ->
                packet.toSet().size == packet.length
            } + packetLength
        }
    }
}