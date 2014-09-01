trait Effect[F[_]] {
  def apply[X](r: F[X]): X
}
