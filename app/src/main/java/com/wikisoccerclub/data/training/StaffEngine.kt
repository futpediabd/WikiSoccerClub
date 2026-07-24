package com.wikisoccerclub.data.training

object StaffEngine {

    fun monthlyStaffCost(
        staff: List<StaffMember>
    ): Long =
        staff.sumOf { it.monthlySalary }

    fun roleQuality(
        staff: List<StaffMember>,
        role: StaffRole
    ): Int {
        val members = staff.filter {
            it.role == role
        }
        if (members.isEmpty()) return 0

        return members.maxOf {
            when (role) {
                StaffRole.PHYSIOTHERAPIST,
                StaffRole.DOCTOR ->
                    it.medical
                StaffRole.SCOUT ->
                    it.scouting
                StaffRole.YOUTH_COACH ->
                    it.youthDevelopment
                StaffRole.FITNESS_COACH ->
                    it.fitness
                StaffRole.ASSISTANT_MANAGER,
                StaffRole.TACTICAL_COACH ->
                    maxOf(
                        it.coaching,
                        it.tacticalKnowledge
                    )
                else -> it.coaching
            }
        }.coerceIn(0, 100)
    }

    fun canHire(
        currentStaff: List<StaffMember>,
        candidate: StaffMember,
        availableMonthlyBudget: Long,
        maximumStaff: Int
    ): Result<Unit> = runCatching {
        require(candidate.monthlySalary >= 0)
        require(currentStaff.size < maximumStaff) {
            "O limite de funcionários foi atingido."
        }
        require(
            candidate.id !in
                currentStaff.map { it.id }
        ) {
            "Este funcionário já pertence ao clube."
        }
        require(
            candidate.monthlySalary <=
                availableMonthlyBudget
        ) {
            "Orçamento mensal insuficiente."
        }
    }

    fun expiringContracts(
        staff: List<StaffMember>,
        currentYear: Int
    ): List<StaffMember> =
        staff.filter {
            it.contractEndYear <= currentYear
        }
}
