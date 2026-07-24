package com.wikisoccerclub.data.transfer

class ClubTransferRepository {
    private val clubs = linkedMapOf<String, ClubTransferState>()

    fun save(club: ClubTransferState) {
        clubs[club.clubId] = club
    }

    fun saveAll(values: List<ClubTransferState>) {
        values.forEach(::save)
    }

    fun find(clubId: String): ClubTransferState? = clubs[clubId]

    fun all(): List<ClubTransferState> = clubs.values.toList()

    fun replaceAll(values: List<ClubTransferState>) {
        clubs.clear()
        saveAll(values)
    }

    fun clear() {
        clubs.clear()
    }
}
