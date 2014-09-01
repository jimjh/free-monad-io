package free

/** Implements an effect for Console using standard in and standard out. */
object StdEffect extends (Console ~> Id) {
  def apply[A](r: Console[A]): Id[A] =
    r match {
      case GetLine => readLine
      case PutLine(s) => println(s)
    }
}
