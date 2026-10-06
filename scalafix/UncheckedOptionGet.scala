package fix

import scalafix.lint.LintSeverity
import scalafix.v1._
import scala.meta._

class UncheckedOptionGet extends SemanticRule("UncheckedOptionGet") {

  private val optionGet = SymbolMatcher.normalized("scala.Option#get().")

  override def fix(implicit doc: SemanticDocument): Patch =
    doc.tree.collect {
      case select @ Term.Select(option, get @ Term.Name("get"))
          if optionGet.matches(get) && !isChecked(select, option) =>
        Patch.lint(UncheckedGet(option, select.pos))
    }.asPatch

  // `tree` sits in an `if` branch that only runs when `option` is defined
  private def isChecked(tree: Tree, option: Term): Boolean =
    tree.parent match {
      case Some(parent: Term.If) if parent.thenp eq tree =>
        definedWhen(parent.cond, option, outcome = true) || isChecked(parent, option)
      case Some(parent: Term.If) if parent.elsep eq tree =>
        definedWhen(parent.cond, option, outcome = false) || isChecked(parent, option)
      case Some(parent) => isChecked(parent, option)
      case None         => false
    }

  // `cond` evaluating to `outcome` guarantees that `option` is defined
  private def definedWhen(cond: Term, option: Term, outcome: Boolean): Boolean =
    cond match {
      case Term.Select(qual, Term.Name("isDefined" | "nonEmpty")) => outcome && same(qual, option)
      case Term.Select(qual, Term.Name("isEmpty"))                => !outcome && same(qual, option)
      case Term.ApplyUnary(Term.Name("!"), arg)                   => definedWhen(arg, option, !outcome)
      case infix: Term.ApplyInfix if infix.argClause.values.size == 1 =>
        val (lhs, rhs) = (infix.lhs, infix.argClause.values.head)
        infix.op.value match {
          case "&&" if outcome  => definedWhen(lhs, option, true) || definedWhen(rhs, option, true)
          case "||" if !outcome => definedWhen(lhs, option, false) || definedWhen(rhs, option, false)
          case _                => false
        }
      case _ => false
    }

  private def same(a: Term, b: Term): Boolean = a.structure == b.structure
}

final case class UncheckedGet(option: Term, position: Position) extends Diagnostic {
  override def severity: LintSeverity = LintSeverity.Error
  override def message: String =
    s"`${option.syntax}.get` throws a NoSuchElementException when the Option is None, " +
      "and it isn't checked first. Handle the None case with pattern matching, `fold`, " +
      "`getOrElse` or `map`, or check `isDefined`/`nonEmpty` before calling `.get`."
}
