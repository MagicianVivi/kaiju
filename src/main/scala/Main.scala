package main

import cats.effect.{ ExitCode, IO, IOApp }

import com.comcast.ip4s._
import org.http4s.ember.server._

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8083")
      .withHttpApp(api.Http4s.routes)
      .build
      .use(_ => IO.never[Unit])
      .as(ExitCode.Success)
}
