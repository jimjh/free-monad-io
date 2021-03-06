package free

/** An enum that is used to encode IO operations and their parameters for Console.
  */
sealed trait Console[A]
case object GetLine extends Console[String]
case class PutLine(s: String) extends Console[Unit]

object ConsoleIO {
  // higher-kinded type - new syntax!
  type ConsoleIO[A] = IO[Console, A]

  val getLine: ConsoleIO[String] =
    // op: Console[String]
    // cont: String => IO[Console, String]
    Req(GetLine, (s: String) => Return(s))

  def putLine(s: String): ConsoleIO[Unit] =
    // op: Console[Unit]
    // cont: Unit => IO[Console, Unit]
    Req(PutLine(s), (_: Unit) => Return(()))
}
