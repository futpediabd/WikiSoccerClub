package com.wikisoccerclub.data.transfer

class TransferNewsRepository {
    private val news = linkedMapOf<String, TransferNewsItem>()
    private val audit = linkedMapOf<String, TransferAuditEvent>()

    fun saveNews(item: TransferNewsItem) {
        news[item.id] = item
    }

    fun saveAudit(event: TransferAuditEvent) {
        audit[event.id] = event
    }

    fun allNews(): List<TransferNewsItem> =
        news.values.sortedWith(
            compareByDescending<TransferNewsItem> { it.seasonYear }
                .thenByDescending { it.important }
        )

    fun newsBySeason(year: Int): List<TransferNewsItem> =
        allNews().filter { it.seasonYear == year }

    fun newsByClub(clubId: String): List<TransferNewsItem> =
        allNews().filter { clubId in it.clubIds }

    fun allAudit(): List<TransferAuditEvent> =
        audit.values.sortedByDescending { it.seasonYear }

    fun auditBySeason(year: Int): List<TransferAuditEvent> =
        allAudit().filter { it.seasonYear == year }

    fun replaceAll(
        newsItems: List<TransferNewsItem>,
        auditEvents: List<TransferAuditEvent>
    ) {
        clear()
        newsItems.forEach(::saveNews)
        auditEvents.forEach(::saveAudit)
    }

    fun clear() {
        news.clear()
        audit.clear()
    }
}
