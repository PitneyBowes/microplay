logLevel := Level.Info

//externalResolvers += Resolver.typesafeRepo("releases")
//externalResolvers += Resolver.typesafeIvyRepo("releases")
//externalResolvers += Resolver.sonatypeRepo("releases")
//externalResolvers += Resolver.sonatypeRepo("public")
//externalResolvers += Resolver.sonatypeRepo("snapshots")
//externalResolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
/*
enable these as a fallback when PB artifactory is down:
externalResolvers += Resolver.jcenterRepo
externalResolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"
*/
//externalResolvers += "Atlassian Releases" at "https://maven.atlassian.com/public/"
//externalResolvers += "SpinGo OSS" at "http://spingo-oss.s3.amazonaws.com/repositories/releases"
//externalResolvers += "Sonatype Repository" at "https://oss.sonatype.org/content/groups/public"

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.20")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "1.0.0")
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.3.7")
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.1.0-M9")