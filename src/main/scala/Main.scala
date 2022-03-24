package main

import cats.effect.{ ExitCode, IO, IOApp }

import data.{ Continent, Profession }
import csv.Decoders

object Main extends IOApp {
  def loadContinents(): IO[List[Continent]] =
    csv.Reader.loadFromResource("continent-intervals.csv").use(resource =>
      IO(csv.Reader.parseLines(resource).map(Decoders.toContinentTuple).toList
        .groupMap { case (name, _, _) => name }{ case (_, lats, longs) => (lats, longs) }
        .foldLeft(List.empty[Continent]){ case (acc, (name, coordinates)) => Continent(name, coordinates)::acc })
    )

  def loadProfessions(): IO[List[Profession]] =
    csv.Reader.loadFromResource("technical-test-professions.csv").use(resource =>
      IO(csv.Reader.parseLines(resource).map(Decoders.toProfession).toList)
    )

  def run(args: List[String]) =
    for {
      continents <- loadContinents()
      professions <- loadProfessions()
      _ = println(continents)
      _ = println(professions)
    } yield ExitCode.Success
}
