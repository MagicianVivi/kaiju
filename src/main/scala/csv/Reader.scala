package csv

import org.apache.commons.csv.{ CSVFormat, CSVParser, CSVRecord }

import scala.jdk.CollectionConverters._

import cats.effect.{ IO, Resource }

object Reader {
  // Using Resource so I don't forget about closing the file
  def loadDatabase(db: Database): Resource[IO, CSVParser] =
    Resource.make(
      IO.blocking(
        CSVFormat.DEFAULT.builder()
          .setHeader(db.headers: _*)
          // Skip first line in when parsing records
          .setSkipHeaderRecord(true)
          // Make sure we get a null when field is empty
          .setNullString("")
          .build()
          .parse(io.Source.fromResource(db.filename).bufferedReader())
      )
    )(parser =>
      IO.blocking(parser.close())
    )

  def lines(parser: CSVParser): Iterator[CSVRecord] =
    parser.iterator().asScala
}
