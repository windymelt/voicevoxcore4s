addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.11.0")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.1.0")

addSbtPlugin("com.github.sbt" % "sbt-release" % "1.1.0")

addSbtPlugin(
  "org.xerial.sbt" % "sbt-sonatype" % "0.0.0-734-5b5db727"
) // CAVEAT: Due to incapability with latest sbt-sonatype, use local built one
// cf. https://github.com/xerial/sbt-sonatype/issues/465

addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.1.2")
