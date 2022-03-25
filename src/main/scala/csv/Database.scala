package csv

final case class Database(filename: String, headers: Seq[String])

object Database {
  val continents = Database(
    "continent-intervals.csv",
    List("continent", "west", "south", "east", "north")
  )

  val professions = Database(
    "technical-test-professions.csv",
    List("id", "name", "category_name")
  )

  val jobs = Database(
    "technical-test-jobs.csv",
    List("profession_id", "contract_type", "name", "office_latitude", "office_longitude")
  )
}
