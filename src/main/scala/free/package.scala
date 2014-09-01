package object free {
  type Id[A] = A

  implicit val MonadId: Monad[Id] = new Monad[Id] {
    def of[A](value: A): Id[A] = value
    def flatMap[A, B](value: Id[A])(f: A => Id[B]): Id[B] = f(value)
    override def toString = s"free.MonadId: ${getClass}"
  }

  implicit val MonadListState: Monad[ListState] = new Monad[ListState] {
    def of[A](value: A): ListState[A] =
      ListState(any => (value, any)) // TODO magic - why does this make sense?
    def flatMap[A, B](value: ListState[A])(f: A => ListState[B]): ListState[B] =
      value match {
        case ListState(leftFunc) => // TODO magic - why does this make sense?
          ListState(leftFunc andThen { case (v1, inOut1) => f(v1).runState(inOut1) })
      }
    override def toString = s"free.MonadListState: ${getClass}"
  }
}
