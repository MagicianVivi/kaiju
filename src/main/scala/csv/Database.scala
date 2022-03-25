package csv

import org.apache.commons.csv.CSVRecord
import cats.effect.{ IO, Resource }

import data.{ Continent, Profession, Job }

final case class Database[A](filename: String, headers: Seq[String], decoder: CSVRecord => A)

object Database {
  val continents = Database[(String, Continent.Latitudes, Continent.Longitudes)](
    "continent-intervals.csv",
    List("continent", "west", "south", "east", "north"),
    Decoder.toContinentTuple
  )

  val professions = Database[Profession](
    "technical-test-professions.csv",
    List("id", "name", "category_name"),
    Decoder.toProfession
  )

  val jobs = Database[Job](
    "technical-test-jobs.csv",
    List("profession_id", "contract_type", "name", "office_latitude", "office_longitude"),
    Decoder.toJob
  )

  def load[A](db: Database[A]): Resource[IO, Iterator[A]] =
    Reader.loadFromResource(db.filename, db.headers).map(resource =>
      Reader.lines(resource).map(db.decoder)
    )
}
