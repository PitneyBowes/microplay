scalaVersion := "2.12.8"

externalResolvers := Seq(Resolver.file("local", file(Path.userHome.absolutePath + "/.ivy2/cache"))(Resolver.ivyStylePatterns))
//externalResolvers += Resolver.sonatypeRepo("public")
//externalResolvers += Resolver.sonatypeRepo("snapshots")
/*
Repositories included in PB artifactory. enable these as a fallback when artifactory is down:
externalResolvers += Resolver.jcenterRepo
externalResolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"
*/
externalResolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

lazy val `microplay-lib` = project in file("microplay-lib")
//lazy val `microplay-root` = (project in file(".")).aggregate(`microplay-lib`).settings(aggregate in publish := false)
lazy val `microplay-root` = project in file(".")

