// I think this means that F is a monad.
trait Monad[F[_]] {
  def unit[A](a: A): F[A]
  // aka flatMap
  def bind[A, B](fa: F[A])(f: A => F[B]): F[B]
}
