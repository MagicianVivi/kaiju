package printer

import cats.implicits._

final case class Table(headers: List[String], rows: List[List[String]])

object Table {
  private[printer] def formatRow(row: List[String], cellSize: Int): String =
    if (row.isEmpty) {
      ""
    } else {
      row.foldLeft("|"){ case (acc, value) =>
        s"$acc ${value.toUpperCase().padTo(cellSize,' ')} |"
      }
    }

  private[printer] def createSeparator(numberOfCells: Int, cellSize: Int): String =
    formatRow(List.fill(numberOfCells)("-".repeat(cellSize)), cellSize)

  private[printer] def formatHeaders(headers: List[String]): (String, Int) = {
    val maxHeaderSize = headers.map(_.length).maxOption.getOrElse(0)
    (formatRow(headers, maxHeaderSize), maxHeaderSize)
  }

  def print(table: Table): Unit = {
    val (headersString, maxHeaderSize) = formatHeaders(table.headers)
    val separatorString = createSeparator(table.headers.length, maxHeaderSize)
    val rowsToDisplay = table.rows.flatMap(row =>
      List(formatRow(row, maxHeaderSize), separatorString)
    )

    (separatorString::headersString::separatorString::rowsToDisplay)
      .foreach(println(_))
  }
}
