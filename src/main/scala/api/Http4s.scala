package api

import cats.implicits._
import cats.effect.IO

import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

object Http4s {
  final case class InputParams(
    continents: List[String],
    categories: List[String]
  )

  val inputParams: EndpointInput[InputParams] =
    query[List[String]]("continent").description("name of continents to display")
      .and(query[List[String]]("category").description("name of categories to display"))
      .mapTo[InputParams]

  val countsEndpoint: PublicEndpoint[InputParams, Unit, data.Job.Counts, Any] =
    endpoint
      .in("job" / "counts")
      .in(inputParams)
      .out(jsonBody[data.Job.Counts])

  val swaggerEndpoints = SwaggerInterpreter().fromEndpoints[IO](
    List(countsEndpoint), "Kaiju", "Exercise 3"
  )

  def routes = Http4sServerInterpreter[IO]().toRoutes(
    countsEndpoint.serverLogic {
      case InputParams(continents, categories) =>
      service.Job.counts(continents, categories).map(_.asRight[Unit])
    }::swaggerEndpoints
  ).orNotFound
}
