/** Implements an effect for Console using an input list and an output list. */
object ListEffect extends Effect[Console] {
  // INSIGHT!
  //
  // For ListEffect, we need additional state to be passed around. It's no longer sufficient to return A (the line that was read); we also
  // need to pass along the state of the list - what has been read, what is leftover. For this reason, we should return a Monad of A instead
  // of A to carry along this additional state.
  def apply[A](r: Console[A]): A =
    r match {
      case GetLine => ???
      case PutLine(s) => ???
    }
}
