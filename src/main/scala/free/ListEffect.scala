package free

case class InOut(in: List[String], out: List[String])
case class ListState[A](runState: InOut => (A, InOut))

/** Implements an effect for Console using an input list and an output list. */
object ListEffect extends (Console ~> ListState) {
  // INSIGHT!
  //
  // For ListEffect, we need additional state to be passed around. It's no longer sufficient to return A (the line that was read); we also
  // need to pass along the state of the list - what has been read, what is leftover. For this reason, we should return a Monad of A instead
  // of A to carry along this additional state.
  def apply[A](r: Console[A]): ListState[A] =
    ListState(state => // TODO magic - why is this a function?
      (r, state) match {
        case (GetLine, InOut(in, out)) =>
          (in.head, InOut(in.tail, out))
        case (PutLine(line), InOut(in, out)) =>
          ((), InOut(in, line::out))
      }
    )
}
