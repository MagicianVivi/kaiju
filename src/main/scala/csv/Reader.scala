package csv

import org.apache.commons.csv.{ CSVFormat, CSVParser, CSVRecord }

import scala.jdk.CollectionConverters._
import scala.io

import cats.effect.{ IO, Resource }

object Reader {
  private[csv] def format(headers: List[String]): CSVFormat =
    CSVFormat.DEFAULT
      .builder()
      .setHeader(headers: _*)
      // Skip first line when parsing records
      .setSkipHeaderRecord(true)
      // Make sure we get a null when field is empty
      .setNullString("")
      .build()

  // Using Resource so I don't forget about closing the file
  private[csv] def loadFromResource(filename: String, headers: List[String]): Resource[IO, CSVParser] =
    Resource.make(
      IO.blocking(
        format(headers).parse(io.Source.fromResource(filename).bufferedReader())
      )
    )(parser =>
      IO.blocking(parser.close())
    )

  private[csv] def lines(parser: CSVParser): Iterator[CSVRecord] =
    parser.iterator().asScala
}
