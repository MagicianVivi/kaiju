package data

import printer.Table

final case class Job(
  professionId: Option[Int],
  contractType: String,
  name: String,
  latitude: Option[Double],
  longitude: Option[Double]
)

object Job {
  type Counts = Map[String, Map[String, Int]]

  private[data] def updateCategory(currentCounts: Map[String, Int], category: String): Map[String, Int] = {
    val totalCount = currentCounts.getOrElse("total", 0) + 1
    val categoryCount = currentCounts.getOrElse(category, 0) + 1

    currentCounts ++ List(("total", totalCount), (category, categoryCount))
  }

  def computeCounts(
    jobs: Iterator[Job],
    continents: List[Continent],
    professions: List[Profession]
  ): Counts = {
    jobs.foldLeft(Map.empty[String, Map[String, Int]]){ case (acc, job) =>
      val continent = (
        for {
          longitude <- job.longitude
          latitude <- job.latitude
          continent <- continents.find(cont =>
            Continent.checkLocation(cont.boxes, longitude, latitude)
          )
        } yield continent.name
      ).getOrElse("unknown")

      val category = professions
        .find(profession => job.professionId.exists(_ == profession.id))
        .map(_.categoryName)
        .getOrElse("unknown")

      val totalCounts = updateCategory(acc.getOrElse("total", Map.empty[String, Int]), category)
      val continentCounts = updateCategory(acc.getOrElse(continent, Map.empty[String, Int]), category)

      acc ++ List(("total", totalCounts), (continent, continentCounts))
    }
  }

  private[data] def createRow(jobCounts: Counts, key: String, headers: List[String]): List[String] =
    jobCounts.get(key).toList
      .flatMap(row => key::headers.map(header => row.getOrElse(header, 0).toString()))

  def tableCounts(jobCounts: Counts): Table = {
    val sortedCategories = jobCounts.get("total").toList.flatMap(_.keys.toList)
      .filterNot(header => header == "total" || header == "unknown").sorted

    val headers = ("total"::sortedCategories) ++ List("unknown")

    val totalRow = createRow(jobCounts, "total", headers)

    val unknownRow = createRow(jobCounts, "unknown", headers)

    val continentNames = jobCounts.keys.toList.filterNot(key => key == "total" || key == "unknown").sorted

    val continentRows = continentNames.map(name => createRow(jobCounts, name, headers))

    val rows = (totalRow::continentRows) ++ List(unknownRow)

    // Add empty space for row's header
    Table(""::headers, rows)
  }
}
