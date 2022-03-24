package csv

import org.apache.commons.csv.{ CSVFormat, CSVParser, CSVRecord }

import scala.jdk.CollectionConverters._

import cats.effect.{ IO, Resource }

object Reader {
  // Using Resource so I don't forget about closing the file
  def loadFromResource(name: String, headers: List[String]): Resource[IO, CSVParser] =
    Resource.make(
      IO.blocking(
        CSVFormat.DEFAULT.builder()
          .setHeader(headers: _*)
          .setSkipHeaderRecord(true)
          .setNullString("")
          .build()
          .parse(io.Source.fromResource(name).bufferedReader())
      )
    )(parser =>
      IO.blocking(parser.close())
    )

  def lines(parser: CSVParser): Iterator[CSVRecord] =
    parser.iterator().asScala
}
