sealed trait Console[A]
case object GetLine extends Console[String]
case class PutLine(s: String) extends Console[Unit]

object main {

  type Id[A] = A

  implicit object ConsoleEffect extends (Console ~> Id) {
    def apply[A](r: Console[A]): A =
      r match {
        case GetLine => readLine
        case PutLine(s) => println(s)
      }
  }

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
    } yield ()

    println(ask.runIO(ConsoleEffect))
  }
}

/*
 * val ask: ConsoleIO[Unit] = for {
 *   _ <- putLine("What is your name?")
 *   name <- getLine
 *   _ <- putLine("Hello, " ++ name)
 * } yield ()
 *
 * |-> val ask = Req(PutLine("...", _ => Return(()))).flatMap(
 * |  _ => Req(GetLine, s => Return(s)).flatMap(
 * |    name => Req(PutLine("...", _ => Return(()))).map(
 * |      _ => ()
 * |    )
 * |  )
 * |)
 *
 * |-> val ask = Req(PutLine("...", _ => Return(()))).flatMap(
 * |  _ => Req(GetLine, s => Return(s)).flatMap(
 * |    name => Req(PutLine("...", _ => Return(()))).map(
 * |      _ => ()
 * |    )
 * |  )
 * |)
 *
 * |-> val ask = Req(PutLine("...", _ => Return(()))).flatMap(
 * |  _ => Req(GetLine, s => Return(s)).flatMap(
 * |    name => Req(PutLine("...", _ => Return(()))).flatMap(a => Return(()))
 * |  )
 * |)
 *
 * |-> val ask = Req(PutLine("...", _ => Return(()))).flatMap(
 * |  _ => Req(GetLine, s => Return(s)).flatMap(
 * |    name => Req(PutLine("...", _ => Return(())))
 * |  )
 * |)
 *
 * |-> val ask = Req(PutLine("...", _ => Return(()))).flatMap(
 * |  _ => Req(GetLine, name => Req(PutLine(s"${name}", _ => Return(())))
 * |)
 *
 * |-> val ask = Req(PutLine("...", _ => Return(()))).flatMap(
 * |  _ => Req(GetLine, name => Req(PutLine(s"${name}", _ => Return(())))
 * |) // TODO
 */
