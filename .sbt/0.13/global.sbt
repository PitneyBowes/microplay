externalResolvers <<= (bootResolvers, externalResolvers) map (
    (boot: Option[Seq[sbt.Resolver]], ext: Seq[sbt.Resolver]) => {
        boot.getOrElse(Seq.empty[sbt.Resolver]).foreach(b => println("Boot::: " + b.toString))
        ext.foreach(e => println("External::: " + e.toString))
        (boot.getOrElse(Seq.empty[sbt.Resolver]) ++ ext).distinct
    }
)
