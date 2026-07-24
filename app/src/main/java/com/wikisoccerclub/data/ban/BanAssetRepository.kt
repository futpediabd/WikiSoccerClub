package com.wikisoccerclub.data.ban

import android.content.Context

class BanAssetRepository(private val context: Context) {

    fun loadAllClubs(): List<BanClub> {
        val files = context.assets.list(TEAMS_FOLDER).orEmpty()
            .filter { it.endsWith(".ban", ignoreCase = true) }
            .sorted()

        return files.mapNotNull { fileName ->
            runCatching {
                val path = "$TEAMS_FOLDER/$fileName"
                val content = context.assets.open(path)
                    .bufferedReader(Charsets.UTF_8)
                    .use { it.readText() }

                BanParser.parse(fileName, content)
            }.getOrNull()
        }
    }

    companion object {
        const val TEAMS_FOLDER = "teams"
        const val SHIELDS_FOLDER = "teams/escudos"
        const val FLAGS_FOLDER = "bandeiras"
    }
}
