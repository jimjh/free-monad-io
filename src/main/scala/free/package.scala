package object free {
  type Id[A] = A
  implicit val MonadId: Monad[Id] = new Monad[Id] {
    def of[A](a: A): Id[A] = a
    def flatMap[A, B](a: Id[A])(f: A => Id[B]): Id[B] = f(a)
    override def toString = s"free.MonadId: ${getClass}"
  }
  implicit val MonadListState: Monad[ListState] = new Monad[ListState] {
    def of[A](a: A): ListState[A] =
      ListState(any => (a, any))
    def flatMap[A, B](a: ListState[A])(f: A => ListState[B]): ListState[B] =
      a match {
        case ListState(runState) =>
          ListState({state =>
            val (a1, inOut1) = runState(state)
            val runState2 = f(a1).runState
            runState2(inOut1)
          })
      }
    override def toString = s"free.MonadListState: ${getClass}"
  }
}
