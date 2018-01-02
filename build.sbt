//name := "microplay"
//organization := "com.borderfree"
//version := System.getProperty("version", "1.0.1")
scalaVersion := "2.12.4"


externalResolvers :=  Seq("Artifactory Realm libs-release" at "https://artifactory-dev.bfretail.pitneycloud.com/artifactory/libs-release")
//externalResolvers += Resolver.sonatypeRepo("public")
//externalResolvers += Resolver.sonatypeRepo("snapshots")
externalResolvers += "scalaz-bintray" at "https://artifactory-dev.bfretail.pitneycloud.com/artifactory/scalaz-bintray"
/*
Repositories included in PB artifactory. enable these as a fallback when artifactory is down:
externalResolvers += Resolver.jcenterRepo
externalResolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"
*/
//externalResolvers += "Atlassian Releases" at "https://artifactory-dev.bfretail.pitneycloud.com/artifactory/atlassian-repo"
externalResolvers += "Artifactory Realm libs-shapshot" at "https://artifactory-dev.bfretail.pitneycloud.com/artifactory/libs-snapshot"
externalResolvers += "Artifactory Realm ext" at "https://artifactory-dev.bfretail.pitneycloud.com/artifactory/ext-release-local"
externalResolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

lazy val `microplay-lib` = (project in file("microplay-lib"))

//lazy val `microplay-root` = (project in file(".")).aggregate(`microplay-lib`).settings(aggregate in publish := false)
lazy val root = (project in file(".")).aggregate(`microplay-lib`).settings(aggregate in publish := false)

//externalResolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

//val ITTest: sbt.Configuration =

/*libraryDependencies ++= Seq(
  ws,
  specs2 % "test,it",
  guice,
//  "ai.x" %% "play-json-extensions" % "0.10.0", //for extended case class to json serialization features
//  "com.typesafe.play" %% "play-json" % "2.6.6",
//  "com.typesafe.play" %% "play-json-joda" % "2.6.6",
  "org.json4s" %% "json4s-native" % "3.5.3", "org.json4s" %% "json4s-ext" % "3.5.3", //for json 2 xml conversion, to support xml as an alternative response medium as part of content negotiation
  "io.swagger" %% "swagger-play2" % "1.6.0",
//  "org.webjars" %% "webjars-play" % "2.6.1",
  "org.webjars" % "swagger-ui" % "3.2.2"
)*/
//unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

//buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion, git.gitHeadCommit)
//buildInfoPackage := "com.borderfree.microplay"
//buildInfoOptions ++= Seq(BuildInfoOption.ToJson, BuildInfoOption.ToMap,BuildInfoOption.BuildTime, BuildInfoOption.Traits("com.borderfree.microplay.services.BuildInfoMeta"))
//
//sourceDirectory in IntegrationTest := baseDirectory.value / "it"
//scalaSource in IntegrationTest := baseDirectory.value / "it"
