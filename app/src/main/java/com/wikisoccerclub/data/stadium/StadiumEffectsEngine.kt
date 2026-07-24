package com.wikisoccerclub.data.stadium

object StadiumEffectsEngine {

    fun homeAdvantage(stadium: Stadium): Int =
        (
            stadium.pitchQuality * 0.25 +
                stadium.lightingQuality * 0.10 +
                stadium.drainageQuality * 0.15 +
                stadium.capacity
                    .coerceAtMost(80_000) / 4_000.0
            ).toInt().coerceIn(0, 20)

    fun injuryRiskModifier(
        stadium: Stadium,
        raining: Boolean
    ): Double {
        var modifier =
            1.20 - stadium.pitchQuality / 500.0

        if (raining) {
            modifier +=
                (100 - stadium.drainageQuality) /
                    300.0
        }

        return modifier.coerceIn(0.75, 1.75)
    }

    fun matchCancellationRisk(
        stadium: Stadium,
        heavyRain: Boolean,
        nightMatch: Boolean
    ): Int {
        var risk = 0

        if (heavyRain) {
            risk +=
                (100 - stadium.drainageQuality) / 4
        }

        if (nightMatch) {
            risk +=
                (100 - stadium.lightingQuality) / 5
        }

        return risk.coerceIn(0, 60)
    }
}
