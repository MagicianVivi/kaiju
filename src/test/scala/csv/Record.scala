package csv

import org.apache.commons.csv.{ CSVRecord, CSVParser }

import scala.jdk.CollectionConverters._

object Record {
  def mock(csv: String, headers: List[String]): CSVRecord =
    CSVParser.parse(csv, Reader.format(headers)).getRecords().asScala.head
}
