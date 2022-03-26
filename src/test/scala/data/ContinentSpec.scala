package data

import org.scalatest.wordspec.AnyWordSpec

class ContinentSpec extends AnyWordSpec {
  "Continent" when {
    "function Bounds.apply" should {
      "create Bounds object" in {
        val result = Continent.Bounds(2.0, 0.0)
        assert(result.upper == 2.0)
        assert(result.lower == 0.0)
      }
      "throws if lower greater than upper" in {
        assertThrows[Exception](Continent.Bounds(0.0, 2.0))
      }
    }

    "function fromLines" should {
      "return an empty list when input is empty" in {
        assert(Continent.fromLines(Iterator.empty) == Nil)
      }

      "group a continent boxes into one continent" in {
        val continentLines = List(
          ("test", Continent.GPSBox(Continent.Bounds(1.0, 0.0), Continent.Bounds(1.0, 0.0))),
          ("test", Continent.GPSBox(Continent.Bounds(3.0, 2.0), Continent.Bounds(3.0, 2.0)))
        )
        val result = Continent.fromLines(continentLines.iterator)
        assert(result.length == 1)
        assert(result.head.name == "test")
        assert(result.head.boxes.length == 2)
      }

      "keep different continents separated" in {
        val continentLines = List(
          ("test", Continent.GPSBox(Continent.Bounds(1.0, 0.0), Continent.Bounds(1.0, 0.0))),
          ("test", Continent.GPSBox(Continent.Bounds(3.0, 2.0), Continent.Bounds(3.0, 2.0))),
          ("test1", Continent.GPSBox(Continent.Bounds(1.0, 0.0), Continent.Bounds(1.0, 0.0))),
          ("test1", Continent.GPSBox(Continent.Bounds(3.0, 2.0), Continent.Bounds(3.0, 2.0))),
          ("test2", Continent.GPSBox(Continent.Bounds(1.0, 0.0), Continent.Bounds(1.0, 0.0))),
          ("test2", Continent.GPSBox(Continent.Bounds(3.0, 2.0), Continent.Bounds(3.0, 2.0)))
        )
        val result = Continent.fromLines(continentLines.iterator)
        assert(result.length == 3)
      }
    }

    "function checkLocation" should {
      "return false on out of bound check" in {
        val boxes = List(Continent.GPSBox(Continent.Bounds(1.0, 0.0), Continent.Bounds(1.0, 0.0)))

        assert(!Continent.checkLocation(boxes, 2.0, 2.0))
        assert(!Continent.checkLocation(boxes, 1.0, 2.0))
        assert(!Continent.checkLocation(boxes, 2.0, 1.0))
      }

      "return true on inbound coordinates" in {
        val boxes = List(Continent.GPSBox(Continent.Bounds(1.0, 0.0), Continent.Bounds(1.0, 0.0)))

        assert(Continent.checkLocation(boxes, 0.5, 0.5))
        assert(Continent.checkLocation(boxes, 1.0, 0.5))
        assert(Continent.checkLocation(boxes, 0.5, 1.0))
      }

      "be inclusive in it's check" in {
        val boxes = List(Continent.GPSBox(Continent.Bounds(1.0, 0.0), Continent.Bounds(1.0, 0.0)))

        assert(Continent.checkLocation(boxes, 1.0, 1.0))
        assert(Continent.checkLocation(boxes, 0.0, 0.0))
      }
    }
  }
}
