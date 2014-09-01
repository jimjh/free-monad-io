package free

/** One way to implement a monad. */
trait Monad[M[_]] {
  def of[A](a: A): M[A]
  def flatMap[A, B](a: M[A])(f: A => M[B]): M[B]
}
