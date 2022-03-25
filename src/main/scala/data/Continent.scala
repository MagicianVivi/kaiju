package data

final case class Continent(
  name: String,
  boxes: List[(Continent.Latitudes, Continent.Longitudes)]
)

object Continent {
  final case class Latitudes(east: Double, west: Double)
  final case class Longitudes(north: Double, south: Double)

  def fromLines(lines: Iterator[(String, Latitudes, Longitudes)]): List[Continent] =
    lines.foldLeft(
      Map.empty[String, List[(Continent.Latitudes, Continent.Longitudes)]]
    ) { case (acc, (name, lats, longs)) =>
        val boxes = acc.getOrElse(name, Nil)
        acc + (name -> ((lats, longs)::boxes))
    }.toList.map { case (name, boxes) => Continent(name, boxes) }

  def checkLocation(boxes: List[(Continent.Latitudes, Continent.Longitudes)], latitude: Double, longitude: Double): Boolean =
    boxes.exists { case (Continent.Latitudes(east, west), Continent.Longitudes(north, south)) =>
      longitude <= east && longitude >= west && latitude <= north && latitude >= south
    }
}
