package csv

import org.apache.commons.csv.{ CSVFormat, CSVParser, CSVRecord }

import scala.jdk.CollectionConverters._

import cats.effect.{ IO, Resource }

object Reader {
  // Using Resource so I don't forget about closing the file
  private[csv] def loadFromResource(filename: String, headers: List[String]): Resource[IO, CSVParser] =
    Resource.make(
      IO.blocking(
        CSVFormat.DEFAULT.builder()
          .setHeader(headers: _*)
          // Skip first line in when parsing records
          .setSkipHeaderRecord(true)
          // Make sure we get a null when field is empty
          .setNullString("")
          .build()
          .parse(io.Source.fromResource(filename).bufferedReader())
      )
    )(parser =>
      IO.blocking(parser.close())
    )

  private[csv] def lines(parser: CSVParser): Iterator[CSVRecord] =
    parser.iterator().asScala
}
