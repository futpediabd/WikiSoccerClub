package com.wikisoccerclub.data.training

class TrainingRepository {

    private val profiles =
        linkedMapOf<String, PlayerTrainingProfile>()

    private val sessions =
        linkedMapOf<String, TrainingSession>()

    private val results =
        mutableListOf<TrainingResult>()

    private val weeklyPlans =
        linkedMapOf<String, WeeklyTrainingPlan>()

    private val staff =
        linkedMapOf<String, StaffMember>()

    fun saveProfile(
        profile: PlayerTrainingProfile
    ) {
        profiles[profile.playerId] = profile
    }

    fun saveProfiles(
        values: List<PlayerTrainingProfile>
    ) {
        values.forEach(::saveProfile)
    }

    fun findProfile(
        playerId: String
    ): PlayerTrainingProfile? =
        profiles[playerId]

    fun allProfiles():
        List<PlayerTrainingProfile> =
        profiles.values.toList()

    fun saveSession(session: TrainingSession) {
        sessions[session.id] = session
    }

    fun sessionsByWeek(
        seasonYear: Int,
        startDay: Int,
        endDay: Int
    ): List<TrainingSession> =
        sessions.values.filter {
            it.seasonYear == seasonYear &&
                it.day in startDay..endDay
        }.sortedBy { it.day }

    fun saveResult(result: TrainingResult) {
        results += result
    }

    fun resultsByPlayer(
        playerId: String
    ): List<TrainingResult> =
        results.filter {
            it.playerId == playerId
        }

    fun savePlan(plan: WeeklyTrainingPlan) {
        weeklyPlans[
            "${plan.clubId}_${plan.seasonYear}_${plan.weekNumber}"
        ] = plan
        plan.sessions.forEach(::saveSession)
    }

    fun findPlan(
        clubId: String,
        seasonYear: Int,
        weekNumber: Int
    ): WeeklyTrainingPlan? =
        weeklyPlans[
            "${clubId}_${seasonYear}_$weekNumber"
        ]

    fun saveStaff(member: StaffMember) {
        staff[member.id] = member
    }

    fun removeStaff(staffId: String) {
        staff.remove(staffId)
    }

    fun allStaff(): List<StaffMember> =
        staff.values.toList()
}
