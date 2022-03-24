package data

final case class Continent(
  name: String,
  coordinates: List[(Continent.Latitudes, Continent.Longitudes)]
) {
  def checkLocation(latitude: Double, longitude: Double): Boolean =
    coordinates.exists { case (Continent.Latitudes(east, west), Continent.Longitudes(north, south)) =>
      longitude <= east && longitude >= west && latitude <= north && latitude >= south
    }
}

object Continent {
  final case class Latitudes(east: Double, west: Double)
  final case class Longitudes(north: Double, south: Double)
}
