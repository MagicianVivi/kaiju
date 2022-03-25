package csv

import org.apache.commons.csv.CSVRecord
import cats.effect.{ IO, Resource }

final case class Database[A](filename: String, headers: List[String], decoder: CSVRecord => A)

object Database {
  val continents = Database(
    "continent-intervals.csv",
    List("continent", "west", "south", "east", "north"),
    Decoder.toContinentTuple
  )

  val professions = Database(
    "technical-test-professions.csv",
    List("id", "name", "category_name"),
    Decoder.toProfession
  )

  val jobs = Database(
    "technical-test-jobs.csv",
    List("profession_id", "contract_type", "name", "office_latitude", "office_longitude"),
    Decoder.toJob
  )

  def load[A](db: Database[A]): Resource[IO, Iterator[A]] =
    Reader.loadFromResource(db.filename, db.headers).map(resource =>
      Reader.lines(resource).map(db.decoder)
    )
}
