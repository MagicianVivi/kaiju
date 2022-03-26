package main

import cats.effect.{ IO, IOApp }

import data.{ Continent, Job }

import csv.Database
import printer.Table

object Main extends IOApp.Simple {
  def run =
    for {
      continents <- Database.load(Database.continents).use(lines =>
        IO(Continent.fromLines(lines))
      )
      professions <- Database.load(Database.professions).use(lines =>
        IO(lines.toList)
      )
      jobCounts <- Database.load(Database.jobs).use(lines =>
        IO(Job.computeCounts(lines, continents, professions))
      )
      _ = Table.print(Job.tableCounts(jobCounts))
    } yield ()
}
