package free

// what is a free monad?

/** `F` is _usually_ some sealed trait with a fixed list of
  * I/O operations encoded as an enum. The tparam that it takes
  * is the result type of each I/O operation.
  *
  * `sealed` means that all possible subclasses are in this file.
  * Useful for creating enums.
  *
  * Request and Return are used to chain I/O operations together.
  * Return is just a plain wrapper around the value, waiting to receive
  * and pass along whatever function it gets through #flatMap.
  *
  * IO is a (free?) monad.
  *
  * @tparam F a higher-kinded type
  * @tparam A "wrapped" or "returned" type
  * @see http://blog.higher-order.com/assets/scalaio.pdf
  */
sealed trait IO[OP[_], A] {
  /** Chains Requests and Returns */
  def flatMap[B](f: A => IO[OP, B]): IO[OP, B]

  def map[B](f: A => B): IO[OP, B] =
    flatMap { a => Return(f(a)) }

  /* Interestingly, it's not possible for runIO to take just a function, because
   * scala doesn't allow you to pass a generic function around i.e. runIO(func)
   * wouldn't work, because scala wouldn't know if it's func[String] or func[Unit].
   * This explains why we need Effect[F].
   *
   * To make effects even more flexible, we allow each effect to return a Monad,
   * so that additional state and be passed around e.g. ListEffect.
   */

  /** Executes the encoded request/return operations using `effect`.
    *
    * Think of `F ~> G` as `Effect[F, G]`. Just syntax sugar.
    *
    * G[_]: Monad means that
    * - we expect a higher-kinded type G, that
    * - has an associated Monad that is passed in as an implicit Monad[G].
    *
    * @see https://twitter.github.io/scala_school/advanced-types.html
    * @see http://stackoverflow.com/questions/2982276/what-is-a-context-bound-in-scala
    */
  def runIO[G[_]: Monad](effect: OP ~> G): G[A]
}

case class Return[OP[_], A](value: A) extends IO[OP, A] {
  override def flatMap[B](f: A => IO[OP, B]): IO[OP, B] =
    f(value)

  override def runIO[G[_]: Monad](effect: OP ~> G): G[A] = {
    // implicity[Monad[G]] returns an implicit value of type Monad[G]
    val G: Monad[G] = implicitly[Monad[G]]
    G.of(value)
  }
}

case class Req[OP[_], I, A](op: OP[I],
                            cont: I => IO[OP, A]) extends IO[OP, A] {
  override def flatMap[B](f: A => IO[OP, B]): IO[OP, B] =
    Req(op, cont andThen { _ flatMap f })
    // k is like a continuation
    // Here, we compose k with f using flatMap.

  override def runIO[G[_]: Monad](effect: OP ~> G): G[A] = {
    // implicity[Monad[G]] returns an implicit value of type Monad[G]
    val G: Monad[G] = implicitly[Monad[G]]
    G.flatMap(effect(op))(cont andThen { _ runIO effect })
    // XXX The following doesn't work
    //   (k andThen { _.runIO(e)})(effect(req))
    // because effect(req) returns a Monad of A, not just A.
  }
}
