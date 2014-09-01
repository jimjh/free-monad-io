package free

// http://blog.higher-order.com/assets/scalaio.pdf
sealed trait IO[F[_], A] {
  /** Chain Requests, Returns */
  def flatMap[B](f: A => IO[F, B]): IO[F, B] =
    this match {
      case Return(a) => f(a)
      case Req(r, k) =>
        Req(r, k andThen { _ flatMap f })
    }

  def map[B](f: A => B): IO[F, B] =
    flatMap { a => Return(f(a)) }

  /* Interestingly, it's not possible for runIO to take just a function, because
   * scala doesn't allow you to pass a generic function around i.e. runIO(func)
   * wouldn't work, because scala wouldn't know if it's func[String] or func[Unit].
   */

  /** Execute the encoded request/return. */
  def runIO[G[_]: Monad](effect: Effect[F, G]): G[A] = {
    // G[_]: Monad is mind-blowing - automatic casting?
    // StdEffect extends Effect[Console, Id], but somehow Id gets cast to Monad[Id]?
    val G = implicitly[Monad[G]]
    // implicity[Monad[G]] is mind-blowing
    this match {
      case Return(a) => G.of(a)
      case Req(req, k) => G.flatMap(effect(req))(k andThen { _.runIO(effect)})
      // XXX The following doesn't work
      //   (k andThen { _.runIO(e)})(effect(req))
      // because effect(req) returns a Monad of A, not just A.
    }
  }
}

case class Return[F[_], A](a: A) extends IO[F, A]

case class Req[F[_], I, B](i: F[I],
                           k: I => IO[F, B]) extends IO[F, B]

// what is F? A higher-kinded type.
// what is a free monad?
// what is the difference between the free monad model and the deferred effects model?