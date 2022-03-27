package service

import cats.effect.IO

import csv.Database

object Job {
  private[this] val logger = org.log4s.getLogger

  def counts(
    continentFilter: List[String],
    categoryFilter: List[String]
  ): IO[data.Job.Counts] =
    for {
      continents <- Database.load(Database.continents).use(lines =>
        IO(data.Continent.fromLines(lines))
      )
      _ = logger.debug(s"loaded continents: $continents")
      professions <- Database.load(Database.professions).use(lines =>
        IO(lines.toList)
      )
      _ = logger.debug(s"loaded professions: $professions")
      jobCounts <- Database.load(Database.jobs).use(lines =>
        IO(data.Job.computeCounts(lines, continents, professions))
      )
      _ = logger.debug(s"job counts: $jobCounts")
    } yield {
      jobCounts.filter { case (continentName, _) =>
        if (continentFilter.isEmpty) true else continentFilter.exists(_ == continentName)
      }.map { case (continentName, categories) =>
        (
          continentName,
          if (categoryFilter.isEmpty) {
            categories
          } else {
            categories.filter { case (categoryName, _) =>
              categoryFilter.exists(_ == categoryName)
            }
          }
        )
      }
    }
}
