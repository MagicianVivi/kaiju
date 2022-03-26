package data

import org.scalatest.wordspec.AnyWordSpec

class JobSpec extends AnyWordSpec {
  "Job" when {
    "function updateCategory" should {
      "add 1 to total and category counts" in {
        val key = "godzilla"
        val result = Job.updateCategory(Map.empty, key)
        assert(result == Map(("total", 1), ("godzilla", 1)))
        assert(Job.updateCategory(result, key) == Map(("total", 2), ("godzilla", 2)))
      }
    }

    "function computeCounts" should {
      "return only unknown and total counts without continents and professions" in {
        val jobs = List(
          Job(Some(1), "permanent", "godzilla", Some(1.0), Some(1.0)),
          Job(Some(2), "internship", "mothra", Some(2.0), Some(2.0))
        )
        val result = Job.computeCounts(jobs.iterator, Nil, Nil)

        assert(result == Map(
          ("total", Map(("total", 2), ("unknown", 2))),
          ("unknown", Map(("total", 2), ("unknown", 2)))
        ))
      }

      "return empty if jobs argument is empty" in {
        val result = Job.computeCounts(Iterator.empty, Nil, Nil)

        assert(result == Map.empty)
      }

      "return all counts" in {
        val jobs = List(
          Job(Some(1), "permanent", "godzilla", Some(1.0), Some(1.0)),
          Job(Some(2), "internship", "mothra", Some(2.0), Some(2.0))
        )
        val continents = List(Continent("europe", List(Continent.GPSBox(Continent.Bounds(1.0, 0.0), Continent.Bounds(1.0, 0.0)))))
        val professions = List(Profession(1, "scientist", "science"))

        val result = Job.computeCounts(jobs.iterator, continents, professions)

        assert(result == Map(
          ("total", Map(("total", 2), ("science", 1), ("unknown", 1))),
          ("europe", Map(("total", 1), ("science", 1))),
          ("unknown", Map(("total", 1), ("unknown", 1)))
        ))
      }
    }

    "function createRow" should {
      "return empty list on empty counts input" in {
        assert(Job.createRow(Map.empty, "", Nil).isEmpty)
      }

      "return at least the key if headers is empty" in {
        val key = "godzilla"
        assert(Job.createRow(Map(("godzilla", Map(("mothra", 1)))), key, Nil) == List(key))
      }

      "return 0 if header is not present in counts" in {
        val key = "godzilla"
        assert(Job.createRow(Map(("godzilla", Map.empty)), key, List("mothra")) == List(key, "0"))
      }

      "output row consisting of key and counts" in {
        val key = "godzilla"
        assert(Job.createRow(Map(("godzilla", Map(("mothra", 1)))), key, List("mothra")) == List(key, "1"))
      }
    }

    "function tableCounts" should {
      "return minimum headers and empty rows for empty input" in {
        val result = Job.tableCounts(Map.empty)

        assert(result.headers == List("", "total", "unknown"))
        assert(result.rows.forall(_.isEmpty))
      }

      "always have headers starting with empty, total, and ending with unknown" in {
        val result = Job.tableCounts(Map(("mothra", Map(("godzilla", 1)))))

        assert(result.headers.head == "")
        assert(result.headers.tail.head == "total")
        assert(result.headers.last == "unknown")
      }

      "compute headers from total counts" in {
        val result = Job.tableCounts(Map(("total", Map(("godzilla", 1), ("mothra", 2)))))

        assert(result.headers == List("", "total", "godzilla", "mothra", "unknown"))
      }
    }
  }
}
