package free

/** Side effects for I/O.
  *
  * Note that for simple effects, such as StdEffect, #apply could just return X. However, for advanced effects such as ListEffect,
  * additional state needs to be passed around, which is why we allow G, a higher-kinded type, to be wrapped about X.
  *
  * G is a monad. I renamed it to RETURN for readability.
  */
trait ~>[OP[_], RESULT[_]] { // think of this as Effect[F[_], G[_]]
  /** Applies effect on op. */
  def apply[X](op: OP[X]): RESULT[X]
}
