package csv

import org.scalatest.wordspec.AnyWordSpec

class DecoderSpec extends AnyWordSpec {
  "Decoder" when {
    "function decoderHelper" should {
      "throw if function argument throws an exception" in {
        val mockRecord = Record.mock("godzilla\r\nmothra", List("godzilla"))
        assertThrows[Exception](Decoder.decoderHelper(mockRecord, _ => throw new Exception("")))
      }
    }

    "function toJob" should {
      "wrap null values as options" in {
        val mockRecord = Record.mock(Database.jobs.headers.mkString(",") ++ "\r\n,godzilla,mothra,,", Database.jobs.headers)
        val result = Decoder.toJob(mockRecord)

        assert(result.professionId.isEmpty)
        assert(result.latitude.isEmpty)
        assert(result.longitude.isEmpty)
      }

      "throw for invalid parsing inside options" in {
        val mockRecord = Record.mock(Database.jobs.headers.mkString(",") ++ "\r\nkingkong,godzilla,mothra,,", Database.jobs.headers)

        assertThrows[Exception](Decoder.toJob(mockRecord))
      }
    }
  }
}
