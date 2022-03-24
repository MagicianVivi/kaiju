package csv

import data.{ Continent, Profession }
import scala.util.control.NonFatal

object Decoders {
  def toContinentTuple(line: Array[String]): (String, Continent.Latitudes, Continent.Longitudes) =
    decoderHelper(line, line => (
      line(0),
      Continent.Latitudes(line(3).toDouble, line(1).toDouble),
      Continent.Longitudes(line(4).toDouble, line(2).toDouble)
    ))

  def toProfession(line: Array[String]): Profession =
    decoderHelper(line, line => Profession(line(0).toInt, line(1), line(2)))

  private def decoderHelper[A](line: Array[String], func: Array[String] => A): A =
    try {
      func(line)
    } catch {
      case NonFatal(e) =>
        println(s"Error when decoding line ${line.toList} from csv")
        throw e
    }
}
