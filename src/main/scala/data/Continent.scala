package data

final case class Continent(
  name: String,
  boxes: List[(Continent.Longitudes, Continent.Latitudes)]
)

object Continent {
  final case class Longitudes(east: Double, west: Double)
  final case class Latitudes(north: Double, south: Double)

  def fromLines(lines: Iterator[(String, Longitudes, Latitudes)]): List[Continent] =
    lines.foldLeft(
      Map.empty[String, List[(Continent.Longitudes, Continent.Latitudes)]]
    ) { case (acc, (name, longs, lats)) =>
        val boxes = (longs, lats)::acc.getOrElse(name, Nil)
        acc + ((name, boxes))
    }.toList.map { case (name, boxes) => Continent(name, boxes) }

  def checkLocation(boxes: List[(Longitudes, Latitudes)], longitude: Double, latitude: Double): Boolean =
    boxes.exists { case (Continent.Longitudes(east, west), Continent.Latitudes(north, south)) =>
      longitude <= east && longitude >= west && latitude <= north && latitude >= south
    }
}
