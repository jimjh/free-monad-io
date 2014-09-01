/** Implements an effect for Console using standard in and standard out. */
object StdEffect extends Effect[Console] {
  def apply[A](r: Console[A]): A =
    r match {
      case GetLine => readLine
      case PutLine(s) => println(s)
    }
}
