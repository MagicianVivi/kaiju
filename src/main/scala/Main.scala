package main

import cats.effect.{ ExitCode, IO, IOApp }

import data.{ Continent, Profession }

import csv.Database

object Main extends IOApp {
  def loadContinents(): IO[List[Continent]] =
    Database.load(Database.continents).use(lines =>
      IO(Continent.fromLines(lines))
    )

  def loadProfessions(): IO[List[Profession]] =
    Database.load(Database.professions).use(lines =>
      IO(lines.toList)
    )

  def updateCategory(currentCounts: Map[String, Int], category: String): Map[String, Int] = {
    val totalCount = currentCounts.getOrElse("total", 0) + 1
    val categoryCount = currentCounts.getOrElse(category, 0) + 1

    currentCounts ++ List(("total", totalCount), (category, categoryCount))
  }

  def processJobs(continents: List[Continent], professions: List[Profession]): IO[Map[String, Map[String, Int]]] =
    Database.load(Database.jobs).use(lines =>
      IO(lines.foldLeft(Map.empty[String, Map[String, Int]]){ case (acc, job) =>
        val continent = ((job.latitude, job.longitude) match {
          case (Some(latitude), Some(longitude)) =>
            continents.find(cont => Continent.checkLocation(cont.boxes, latitude, longitude)).map(_.name)
          case (_, _) =>
            None
        }).getOrElse("inconnu")

        val category = professions.find(profession => job.professionId.exists(_ == profession.id))
          .map(_.categoryName).getOrElse("inconnue")

        val totalCounts = updateCategory(acc.getOrElse("total", Map.empty[String, Int]), category)
        val continentCounts = updateCategory(acc.getOrElse(continent, Map.empty[String, Int]), category)

        acc ++ List(("total", totalCounts), (continent, continentCounts))
      })
    )

  def run(args: List[String]) =
    for {
      continents <- loadContinents()
      professions <- loadProfessions()
      jobs <- processJobs(continents, professions)
      _ = println(jobs)
    } yield ExitCode.Success
}
