package printer

import org.scalatest.wordspec.AnyWordSpec

class TableSpec extends AnyWordSpec {
  "Table" when {
    "function formatRow" should {
      "return empty string if row is empty" in {
        assert(Table.formatRow(Nil, 0) == "")
      }

      "return string starting and ending with | and a space" in {
        val row = List("godzilla", "mothra")
        val size = 10
        val result = Table.formatRow(row, size)

        assert(result.startsWith("| "))
        assert(result.endsWith(" |"))
      }

      "return cells of given size" in {
        val row = List("godzilla")
        val size = 10
        val result = Table.formatRow(row, size)
        assert(result.length == (4 + size))
      }

      "return uppercased cells" in {
        val row = List("godzilla", "mothra")
        val size = 10
        val result = Table.formatRow(row, size)

        assert(result.contains("GODZILLA"))
        assert(result.contains("MOTHRA"))
      }
    }

    "function createSeparator" should {
      "return cells full of dashes" in {
        val result = Table.createSeparator(1, 10)
        assert(result == "| ---------- |")
      }
    }

    "function formatHeaders" should {
      "return cellSize 0 on empty input" in {
        val (string, cellSize) = Table.formatHeaders(Nil)
        assert(cellSize == 0)
      }

      "return length of longest element" in {
        val (string, cellSize) = Table.formatHeaders(List("godzilla", "mothra"))
        assert(cellSize == 8)
      }
    }
  }
}
