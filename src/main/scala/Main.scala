package main

import cats.effect.ResourceApp

import data.Continent
import csv.Decoders

object Main extends ResourceApp.Simple {
  def run =
    for {
      continentsResource <- csv.Loader.fromResource("continent-intervals.csv", headers = true)
      continents = continentsResource.map(Decoders.toContinentTuple).toList
      .groupMap { case (name, _, _) => name }{ case (_, lats, longs) => (lats, longs) }
      .foldLeft(List.empty[Continent]){ case (acc, (name, coordinates)) => Continent(name, coordinates)::acc }
      professionsResource <- csv.Loader.fromResource("technical-test-professions.csv", headers = true)
      professions = professionsResource.map(Decoders.toProfession).toList
      _ = println(continents)
      _ = println(professions)
    } yield ()
}
