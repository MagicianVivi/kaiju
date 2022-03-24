package main

import cats.effect.{ ExitCode, IO, IOApp }

import data.{ Continent, Profession }
import csv.Decoders

object Main extends IOApp {
  def loadContinents(): IO[List[Continent]] =
    csv.Reader.loadFromResource("continent-intervals.csv", List("continent", "west", "south", "east", "north")).use(resource =>
      IO(csv.Reader.lines(resource).map(Decoders.toContinentTuple).toList
        .groupMap { case (name, _, _) => name }{ case (_, lats, longs) => (lats, longs) }
        .foldLeft(List.empty[Continent]){ case (acc, (name, coordinates)) => Continent(name, coordinates)::acc })
    )

  def loadProfessions(): IO[List[Profession]] =
    csv.Reader.loadFromResource("technical-test-professions.csv", List("id", "name", "category_name")).use(resource =>
      IO(csv.Reader.lines(resource).map(Decoders.toProfession).toList)
    )

  def updateCategory(currentCounts: Map[String, Int], category: String): Map[String, Int] = {
    val totalCount = currentCounts.getOrElse("total", 0) + 1
    val categoryCount = currentCounts.getOrElse(category, 0) + 1

    currentCounts ++ List(("total", totalCount), (category, categoryCount))
  }

  def processJobs(continents: List[Continent], professions: List[Profession]): IO[Map[String, Map[String, Int]]] =
    csv.Reader.loadFromResource("technical-test-jobs.csv", List("profession_id", "contract_type", "name", "office_latitude", "office_longitude")).use(resource =>
      IO(csv.Reader.lines(resource).map(Decoders.toJob)
        .foldLeft(Map.empty[String, Map[String, Int]]){ case (acc, job) =>
          val continent = ((job.latitude, job.longitude) match {
            case (Some(latitude), Some(longitude)) =>
              continents.find(cont => cont.checkLocation(latitude, longitude)).map(_.name)
            case (_, _) =>
              None
          }).getOrElse("inconnu")

          val category = professions.find(profession => job.professionId.exists(_ == profession.id))
            .map(_.categoryName).getOrElse("inconnue")

          val totalCounts = updateCategory(acc.getOrElse("total", Map.empty[String, Int]), category)
          val continentCounts = updateCategory(acc.getOrElse(continent, Map.empty[String, Int]), category)

          acc ++ List(("total", totalCounts), (continent, continentCounts))
        }
      )
    )

  def run(args: List[String]) =
    for {
      continents <- loadContinents()
      professions <- loadProfessions()
      jobs <- processJobs(continents, professions)
      _ = println(jobs)
    } yield ExitCode.Success
}
