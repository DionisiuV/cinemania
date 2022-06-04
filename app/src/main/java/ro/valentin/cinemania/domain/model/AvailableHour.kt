package ro.valentin.cinemania.domain.model

data class AvailableHour(
    val time: String,
    var available: Boolean = true
)