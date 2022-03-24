package csv

import cats.effect.{ IO, Resource }
import scala.io.BufferedSource

object Reader {
  // Using Resource so I don't forget about closing the file
  def loadFromResource(name: String): Resource[IO, BufferedSource] =
    Resource.make(
      IO.blocking(io.Source.fromResource(name))
    )(source =>
      IO.blocking(source.close())
    )

  def parseLines(source: BufferedSource, headers: Boolean = true, delimiter: String = ","): Iterator[Array[String]] =
    source.getLines().drop(if (headers) 1 else 0).map(_.split(","))
}
