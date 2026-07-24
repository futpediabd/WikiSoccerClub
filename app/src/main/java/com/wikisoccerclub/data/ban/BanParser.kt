package com.wikisoccerclub.data.ban

/**
 * Leitor inicial do formato BAN.
 *
 * Nesta etapa, o parser entende linhas no formato chave=valor e seções
 * [clube], [estadio] e [jogador]. O formato será ampliado nas próximas etapas.
 */
object BanParser {

    fun parse(fileName: String, content: String): BanClub {
        var section = ""
        val club = mutableMapOf<String, String>()
        val stadium = mutableMapOf<String, String>()
        val players = mutableListOf<MutableMap<String, String>>()
        var currentPlayer: MutableMap<String, String>? = null

        content.lineSequence().forEach { rawLine ->
            val line = rawLine.trim()

            if (line.isBlank() || line.startsWith("#") || line.startsWith(";")) {
                return@forEach
            }

            if (line.startsWith("[") && line.endsWith("]")) {
                section = line.removePrefix("[").removeSuffix("]").lowercase()

                if (section == "jogador") {
                    currentPlayer = mutableMapOf()
                    players += currentPlayer!!
                }
                return@forEach
            }

            val separator = line.indexOf('=')
            if (separator <= 0) return@forEach

            val key = line.substring(0, separator).trim().lowercase()
            val value = line.substring(separator + 1).trim()

            when (section) {
                "estadio" -> stadium[key] = value
                "jogador" -> currentPlayer?.set(key, value)
                else -> club[key] = value
            }
        }

        val level = club["nivel"].toIntSafe(default = 1).coerceIn(1, 25)
        val strength = (level * 4).coerceIn(1, 100)

        return BanClub(
            sourceFile = fileName,
            name = club["nome"].orEmpty().ifBlank { fileName.substringBeforeLast('.') },
            country = club["pais"].orEmpty(),
            city = club["cidade"].orEmpty(),
            stadiumName = stadium["nome"].orEmpty(),
            stadiumCapacity = stadium["capacidade"].toIntSafe(0),
            level = level,
            strength = strength,
            stars = strengthToStars(strength),
            players = players.map { player ->
                BanPlayer(
                    name = player["nome"].orEmpty(),
                    age = player["idade"].toIntSafe(18),
                    nationality = player["nacionalidade"].orEmpty(),
                    position = player["posicao"].orEmpty(),
                    overall = player["overall"].toIntSafe(50).coerceIn(1, 100),
                    potential = player["potencial"].toIntSafe(50).coerceIn(1, 100)
                )
            }
        )
    }

    fun strengthToStars(strength: Int): Double {
        return when (strength.coerceIn(1, 100)) {
            in 1..10 -> 0.5
            in 11..20 -> 1.0
            in 21..30 -> 1.5
            in 31..40 -> 2.0
            in 41..50 -> 2.5
            in 51..60 -> 3.0
            in 61..70 -> 3.5
            in 71..80 -> 4.0
            in 81..90 -> 4.5
            else -> 5.0
        }
    }

    private fun String?.toIntSafe(default: Int): Int {
        return this?.replace(".", "")?.replace(",", "")?.toIntOrNull() ?: default
    }
}
