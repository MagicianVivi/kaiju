package csv

import scala.util.control.NonFatal

import data.{ Continent, Profession }

object Decoders {
  def toContinentTuple(line: Array[String]): (String, Continent.Latitudes, Continent.Longitudes) = {
    try {
      (
        line(0),
        Continent.Latitudes(line(3).toDouble, line(1).toDouble),
        Continent.Longitudes(line(4).toDouble, line(2).toDouble)
      )
    } catch {
      case NonFatal(e) => throw e
    }
  }

  def toProfession(line: Array[String]): Profession =
    try {
      Profession(line(0).toInt, line(1), line(2))
    } catch {
      case NonFatal(e) => throw e
    }
}
