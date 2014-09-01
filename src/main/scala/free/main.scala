package free

/** Example of how to use free monads to do pure functional programming by pushing all side effects into a #runIO method. ListEffect
  * demonstrates easy mockability - a benefit of using this approach.
  *
  * @author Jim Lim - jim@jimjh.com
  * @see http://blog.higher-order.com/assets/scalaio.pdf
  */
object main {
  import ConsoleIO._
  def main(args: Array[String]) {
    val ask: ConsoleIO[Unit] = for {
      _ <- putLine("What is your name?") // flatmap
      name <- getLine                    // flatmap
      _ <- putLine(s"Hello ${name}")     // map
    } yield ()

    // Use stdin and stdout
    println(ask.runIO(StdEffect))

    // Use a mock IO list
    val io = ask.runIO(ListEffect)
    println(io.runState(InOut(List("Stranger"), List())))
  }
}

/*
 * val ask: ConsoleIO[Unit] = for {
 *   _ <- putLine("What is your name?")
 *   name <- getLine
 *   _ <- putLine("Hello, " ++ name)
 * } yield ()
 *
 * |-> val ask = Req(PutLine("..."), _ => Return(())).flatMap(
 * |  _ => Req(GetLine, s => Return(s)).flatMap(
 * |    name => Req(PutLine("..."), _ => Return(())).map(
 * |      _ => ()
 * |    )
 * |  )
 * |)
 *
 * |-> val ask = Req(PutLine("..."), _ => Return(())).flatMap(
 * |  _ => Req(GetLine, s => Return(s)).flatMap(
 * |    name => Req(PutLine("..."), _ => Return(())).map(
 * |      _ => ()
 * |    )
 * |  )
 * |)
 *
 * |-> val ask = Req(PutLine("..."), _ => Return(())).flatMap(
 * |  _ => Req(GetLine, s => Return(s)).flatMap(
 * |    name => Req(PutLine("..."), _ => Return(())).flatMap(a => Return(()))
 * |  )
 * |)
 *
 * |-> val ask = Req(PutLine("..."), _ => Return(())).flatMap(
 * |  _ => Req(GetLine, s => Return(s)).flatMap(
 * |    name => Req(PutLine("..."), _ => Return(()))
 * |  )
 * |)
 *
 * |-> val ask = Req(PutLine("..."), _ => Return(())).flatMap(
 * |  _ => Req(GetLine, name => Req(PutLine(s"Hello ${name}"), _ => Return(())))
 * |)
 *
 * |-> val ask = Req(PutLine("..."), _ => Return(())).flatMap(
 * |  _ => Req(GetLine, name => Req(PutLine(s"Hello ${name}"), _ => Return(())))
 * |)
 *
 * |-> val ask = Req(PutLine("..."),
 * |     _ => Req(GetLine,
 * |       name => Req(PutLine(s"Hello ${name}"),
 * |         _ => Return(())
 * |      )
 * |   )
 * |)
 */
