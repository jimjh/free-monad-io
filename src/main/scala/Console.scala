sealed trait Console[A]
case object GetLine extends Console[String]
case class PutLine(s: String) extends Console[Unit]

// F = Console

object main {
  type ConsoleIO[A] = IO[Console, A]

  val getLine: ConsoleIO[String] =
    // i: Console[String]
    // k: String => IO[Console, String]
    Req(GetLine, (s: String) => Return(s))

  def putLine(s: String): ConsoleIO[Unit] =
    // i: Console[Unit]
    // k: Unit => IO[Console, Unit]
    Req(PutLine(s), (_: Unit) => Return(()))

  def main(args: Array[String]) {
    val ask: ConsoleIO[Unit] = for {
      _ <- putLine("What is your name?")
      name <- getLine
      _ <- putLine("Hello, " ++ name)
    } yield Return(())
    println(ask)
  }
}
