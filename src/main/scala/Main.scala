import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple:

  def run: IO[Unit] =
    val name = sys.env.get("GREETING_NAME")
    IO.println(s"Hello, ${name.get}!")
