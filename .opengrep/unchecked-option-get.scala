import cats.effect.{IO, Ref}

object UncheckedOptionGet:

  final case class User(id: Int, name: String)

  def parameter(name: Option[String]): String =
    // ruleid: unchecked-option-get
    name.get

  def interpolated(name: Option[String]): String =
    // ruleid: unchecked-option-get
    s"Hello, ${name.get}!"

  def annotatedVal(): Int =
    val port: Option[Int] = sys.env.get("PORT").map(_.toInt)
    // ruleid: unchecked-option-get
    port.get

  def envLookup(): String =
    val name = sys.env.get("NAME")
    // ruleid: unchecked-option-get
    name.get

  def chainedLookup(): String =
    // ruleid: unchecked-option-get
    sys.env.get("PORT").get

  def chainedFind(users: List[User]): User =
    // ruleid: unchecked-option-get
    users.find(_.id == 1).get

  def headOption(users: List[User]): User =
    val first = users.headOption
    // ruleid: unchecked-option-get
    first.get

  def wrappedNullable(value: String): String =
    // ruleid: unchecked-option-get
    Option(value).get

  def checkedOtherOption(name: Option[String], other: Option[String]): String =
    // ruleid: unchecked-option-get
    if other.isDefined then name.get else "World"

  def checkedIsDefined(name: Option[String]): String =
    // ok: unchecked-option-get
    if name.isDefined then name.get else "World"

  def checkedNonEmpty(name: Option[String]): String =
    // ok: unchecked-option-get
    if (name.nonEmpty) name.get else "World"

  def checkedIsEmpty(name: Option[String]): String =
    // ok: unchecked-option-get
    if name.isEmpty then "World" else name.get

  def checkedVal(): String =
    val name = sys.env.get("NAME")
    // ok: unchecked-option-get
    if name.isDefined then name.get else "World"

  def handled(name: Option[String]): String =
    // ok: unchecked-option-get
    name.getOrElse("World")

  def mapLookup(env: Map[String, String]): Option[String] =
    // ok: unchecked-option-get
    env.get("NAME")

  def counter(ref: Ref[IO, Int]): IO[Int] =
    // ok: unchecked-option-get
    ref.get
