package data

final case class Job(
  professionId: Option[Int],
  contractType: String,
  name: String,
  latitude: Option[Double],
  longitude: Option[Double]
)
