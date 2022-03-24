package data

final case class Continent(
  name: String,
  coordinates: List[(Continent.Latitudes, Continent.Longitudes)]
)

object Continent {
  final case class Latitudes(east: Double, west: Double)
  final case class Longitudes(north: Double, south: Double)
}
