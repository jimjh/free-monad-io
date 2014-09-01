package free
/** Side effects for I/O.
  *
  * Note that for simple effects, such as StdEffect, #apply could just return X. However, for advanced effects such as ListEffect,
  * additional state needs to be passed around, which is why we allow G, a higher-kinded type, to be wrapped about X.
  */
trait Effect[F[_], G[_]] {
  def apply[X](r: F[X]): G[X]
}
