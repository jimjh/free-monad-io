object main {
  import ConsoleIO._
  def main(args: Array[String]) {
    val ask: ConsoleIO[Unit] = for {
      _ <- putLine("What is your name?")
      name <- getLine
      _ <- putLine("Hello, " ++ name)
    } yield ()

    println(ask.runIO(StdEffect))
    println(ask.runIO(ListEffect))
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
