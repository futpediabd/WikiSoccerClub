package com.wikisoccerclub.data.scouting

class ShortlistRepository {

    private val entries =
        linkedMapOf<String, ShortlistEntry>()

    fun save(entry: ShortlistEntry) {
        entries[entry.playerId] = entry
    }

    fun remove(playerId: String) {
        entries.remove(playerId)
    }

    fun contains(playerId: String): Boolean =
        entries.containsKey(playerId)

    fun all(): List<ShortlistEntry> =
        entries.values.sortedByDescending {
            priorityWeight(it.priority)
        }

    fun byPriority(
        priority: ShortlistPriority
    ): List<ShortlistEntry> =
        entries.values.filter {
            it.priority == priority
        }

    private fun priorityWeight(
        priority: ShortlistPriority
    ): Int = when (priority) {
        ShortlistPriority.MONITOR -> 1
        ShortlistPriority.INTERESTED -> 2
        ShortlistPriority.PRIORITY -> 3
        ShortlistPriority.NEGOTIATE -> 4
    }
}
