package data

final case class Continent(
  name: String,
  boxes: List[Continent.GPSBox]
)

object Continent {
  final case class GPSBox(longitudes: Bounds, latitudes: Bounds)

  final class Bounds(val upper: Double, val lower: Double) {
    override def toString(): String =
      s"Bounds($upper, $lower)"
  }

  object Bounds {
    def apply(upper: Double, lower: Double): Bounds = {
      if (upper < lower) {
        throw new Exception("Can't create box with lower bound greater than upper bound")
      } else {
        new Bounds(upper, lower)
      }
    }
  }

  def fromLines(lines: Iterator[(String, GPSBox)]): List[Continent] =
    lines.foldLeft(
      Map.empty[String, List[GPSBox]]
    ) { case (acc, (name, box)) =>
        acc + ((name, box::acc.getOrElse(name, Nil)))
    }.toList.map { case (name, boxes) => Continent(name, boxes) }

  def checkLocation(boxes: List[GPSBox], longitude: Double, latitude: Double): Boolean =
    boxes.exists { case GPSBox(longitudes, latitudes) =>
      longitude <= longitudes.upper && longitude >= longitudes.lower &&
      latitude <= latitudes.upper && latitude >= latitudes.lower
    }
}
