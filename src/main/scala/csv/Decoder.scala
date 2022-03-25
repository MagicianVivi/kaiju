package csv

import org.apache.commons.csv.CSVRecord

import scala.util.control.NonFatal

import data.{ Continent, Job, Profession }

object Decoder {
  def toContinentTuple(line: CSVRecord): (String, Continent.Latitudes, Continent.Longitudes) =
    decoderHelper(line, line => (
      line.get("continent"),
      Continent.Latitudes(line.get("east").toDouble, line.get("west").toDouble),
      Continent.Longitudes(line.get("north").toDouble, line.get("south").toDouble)
    ))

  def toProfession(line: CSVRecord): Profession =
    decoderHelper(line, line => Profession(line.get("id").toInt, line.get("name"), line.get("category_name")))

  def toJob(line: CSVRecord): Job =
    decoderHelper(line, line =>
      Job(
        Option(line.get("profession_id")).map(_.toInt),
        line.get("contract_type"),
        line.get("name"),
        Option(line.get("office_latitude")).map(_.toDouble),
        Option(line.get("office_longitude")).map(_.toDouble)
      )
    )

  private def decoderHelper[A](line: CSVRecord, func: CSVRecord => A): A =
    try {
      func(line)
    } catch {
      case NonFatal(e) =>
        println(s"Error when decoding line ${line.toList} from csv")
        throw e
    }
}