package printer

import cats.implicits._

import cats.effect.IO

final case class Table(headers: List[String], rows: List[List[String]])

object Table {
  private[printer] def formatRow(row: List[String], maxHeaderSize: Int): String =
    row.foldLeft("|"){ case (acc, value) =>
      s"$acc ${value.toUpperCase().padTo(maxHeaderSize,' ')} |"
    }

  private[printer] def createSeparator(headersSize: Int, maxHeaderSize: Int): String =
    formatRow(List.fill(headersSize)("-".repeat(maxHeaderSize)), maxHeaderSize)

  private[printer] def formatHeaders(headers: List[String]): (String, Int) = {
    val maxHeaderSize = headers.map(_.length).maxOption.getOrElse(0)
    (formatRow(headers, maxHeaderSize), maxHeaderSize)
  }

  def print(table: Table): IO[Unit] = {
    val (headersString, maxHeaderSize) = formatHeaders(table.headers)
    val separatorString = createSeparator(table.headers.length, maxHeaderSize)
    val rowsToDisplay = table.rows.flatMap(row =>
      List(formatRow(row, maxHeaderSize), separatorString)
    )

    (separatorString::headersString::separatorString::rowsToDisplay)
      .traverse(IO.println(_))
      .map(_ => ())
  }
}
