package fs2

import cats.Id
import cats.effect.IO

object ThisModuleShouldCompile {

  /* Some checks that `.pull` can be used without annotations */
  Stream(1,2,3,4) through pipe.take(2)
  Stream.eval(IO.pure(1)) through pipe.take(2)
  Stream(1,2,3,4) through[Int] pipe.take(2)
  Stream(1,2,3).covary[IO].pull.uncons1.stream
  Stream.eval(IO.pure(1)).pull.uncons1.stream

  /* Also in a polymorphic context. */
  def a[F[_],A](s: Stream[F,A]) = s through pipe.take(2)
  def b[F[_],A](s: Stream[F,A]): Stream[F,A] = s through pipe.take(2)
  def c[F[_],A](s: Stream[F,A]): Stream[F,A] = s through pipe.take(2)

  pipe.take[Id,Int](2)
  Stream(1,2,3) ++ Stream(4,5,6)
  Stream(1,2,3) ++ Stream.eval(IO.pure(4))
  Stream(1,2,3) ++ Stream.eval(IO.pure(4))
  Stream.eval(IO.pure(4)) ++ Stream(1,2,3)
  Stream.eval(IO.pure(4)) ++ Stream(1,2,3).covary[IO]
  Stream.eval(IO.pure(4)) ++ (Stream(1,2,3): Stream[IO, Int])
  Stream(1,2,3).flatMap(i => Stream.eval(IO.pure(i)))
  (Stream(1,2,3).covary[IO]).pull.uncons1.flatMapOpt { case (hd,_) => Pull.output1(hd).as(None) }.stream
  Stream(1,2,3).pull.uncons1.flatMapOpt { case (hd,_) => Pull.output1(hd).as(None) }.stream
  Stream(1,2,3).pull.uncons1.flatMapOpt { case (hd,_) => Pull.eval(IO.pure(1)) >> Pull.output1(hd).as(None) }.stream
  (Stream(1,2,3).evalMap(IO(_))): Stream[IO,Int]
  (Stream(1,2,3).flatMap(i => Stream.eval(IO(i)))): Stream[IO,Int]

  val s: Stream[IO,Int] = if (true) Stream(1,2,3) else Stream.eval(IO(10))

  import scala.concurrent.ExecutionContext.Implicits.global
  Stream.eval(IO.pure(1)).pull.unconsAsync.stream

  (pipe.take[Id,Int](2)): Pipe[IO,Int,Int]
  pipe.take[Id,Int](2).covary[IO]
  pipe.take[Id,Int](2).attachL(pipe2.interleave)
  pipe.take[Id,Int](2).attachR(pipe2.interleave)
  pipe.take[Id,Int](2).attachR(pipe2.interleave)

  val p: Pull[Id,Nothing,Option[(Segment[Int,Unit],Stream[Id,Int])]] = Stream(1, 2, 3).pull.uncons
  val q: Pull[IO,Nothing,Option[(Segment[Int,Unit],Stream[Id,Int])]] = p
}
