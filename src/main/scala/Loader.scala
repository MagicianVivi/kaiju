package csv

import cats.effect.{ IO, Resource }

object Loader {
  // Using Resource so I don't forget about closing the file
  def fromResource(name: String, headers: Boolean): Resource[IO, Iterator[Array[String]]] =
    Resource.make(
      IO.blocking(io.Source.fromResource(name))
    )(source =>
      IO.blocking(source.close())
    ).map(source =>
      source.getLines().drop(if (headers) 1 else 0).map(_.split(","))
    )
}
