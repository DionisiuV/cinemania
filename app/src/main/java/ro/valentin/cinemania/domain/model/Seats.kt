package ro.valentin.cinemania.domain.model

data class Seat(
    val number: String? = null,
    var available: Boolean = true,
    val lastUpdate: String = "none"
)

fun getSeats() = mutableListOf(
    Seat("0"),
    Seat("1"),
    Seat("2"),
    Seat("3"),
    Seat("4"),
    Seat("5"),
    Seat("6"),
    Seat("7"),
    Seat("8"),
    Seat("9"),
    Seat("10" ),
    Seat("11"),
    Seat("12"),
    Seat("13" ),
    Seat("14" ),
    Seat("15" ),
    Seat("16" ),
    Seat("17" ),
    Seat("18" ),
    Seat("19" ),
    Seat("20" ),
    Seat("21" ),
    Seat("22" ),
    Seat("23" ),
    Seat("24" ),
    Seat("25" ),
    Seat("26" ),
    Seat("27" ),
    Seat("28" ),
    Seat("29" ),
)
