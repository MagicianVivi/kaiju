package main

import cats.effect.{ IO, IOApp }

import data.{ Continent, Job }

import csv.Database
import printer.Table

object Main extends IOApp.Simple {
  private[this] val logger = org.log4s.getLogger

  def run =
    for {
      continents <- Database.load(Database.continents).use(lines =>
        IO(Continent.fromLines(lines))
      )
      _ = logger.debug(s"loaded continents: $continents")
      professions <- Database.load(Database.professions).use(lines =>
        IO(lines.toList)
      )
      _ = logger.debug(s"loaded professions: $professions")
      jobCounts <- Database.load(Database.jobs).use(lines =>
        IO(Job.computeCounts(lines, continents, professions))
      )
      _ = logger.debug(s"job counts: $jobCounts")
      _ = Table.print(Job.tableCounts(jobCounts))
    } yield ()
}
