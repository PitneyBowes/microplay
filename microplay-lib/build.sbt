import scala.sys.Prop

name := "microplay"
organization := "com.borderfree"
version := System.getProperty("version", "1.0.0")
scalaVersion := "2.12.4"

lazy val `microplay-lib` = (project in file(".")).configs(IntegrationTest).settings(Defaults.itSettings: _*).enablePlugins(PlayScala, /*SonarRunnerPlugin, */BuildInfoPlugin, GitVersioning)


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

publishTo := Some("Artifactory Realm Publish" at "http://registry.bfretail.pitneycloud.com:8081/artifactory/libs-release-local")

credentials += {
  val artifactoryUser = Prop[String]("artifactory_user")
  val artifactoryPassword = Prop[String]("artifactory_password")
  if(artifactoryUser.isSet && artifactoryPassword.isSet)
  {
    println(s"publishing using artifactory_user=$artifactoryUser")
    Credentials("Artifactory Realm", "registry.bfretail.pitneycloud.com", artifactoryUser.get, artifactoryUser.get)
  }
  else
  {
    println(s"loading publish credentials from ~/.ivy2/.credentials")
    Credentials(Path.userHome / ".ivy2" / ".credentials")
  }
}


libraryDependencies ++= Seq(
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
)
//unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion, git.gitHeadCommit)
buildInfoPackage := "com.borderfree.microplay"
buildInfoOptions ++= Seq(BuildInfoOption.ToJson, BuildInfoOption.ToMap,BuildInfoOption.BuildTime, BuildInfoOption.Traits("com.borderfree.microplay.services.BuildInfoMeta"))

sourceDirectory in IntegrationTest := baseDirectory.value / "it"
scalaSource in IntegrationTest := baseDirectory.value / "it"

scalacOptions ++= Seq(
  "-deprecation",                // Emit warning and location for usages of deprecated APIs.
  //      "-Xlog-implicits",
  "-encoding", "utf-8",                // Specify character encoding used by source files.
  "-explaintypes",                     // Explain type errors in more detail.
  "-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
  "-language:existentials",            // Existential types (besides wildcard types) can be written and inferred
  "-language:experimental.macros",     // Allow macro definition (besides implementation and application)
  "-language:higherKinds",             // Allow higher-kinded types
  "-language:implicitConversions",     // Allow definition of implicit functions called views
  "-language:postfixOps",              // Allow postFix operations
  "-unchecked",                        // Enable additional warnings where generated code depends on assumptions.
  "-Xcheckinit",                       // Wrap field accessors to throw an exception on uninitialized access.
  //"-Xfatal-warnings",                  // Fail the compilation if there are any warnings.
  "-Xfuture",                          // Turn on future language features.
  "-Xlint:adapted-args",               // Warn if an argument list is modified to match the receiver.
  "-Xlint:by-name-right-associative",  // By-name parameter of right associative operator.
  "-Xlint:constant",                   // Evaluation of a constant arithmetic expression results in an error.
  "-Xlint:delayedinit-select",         // Selecting member of DelayedInit.
  "-Xlint:doc-detached",               // A Scaladoc comment appears to be detached from its element.
  "-Xlint:inaccessible",               // Warn about inaccessible types in method signatures.
  "-Xlint:infer-any",                  // Warn when a type argument is inferred to be `Any`.
  "-Xlint:missing-interpolator",       // A string literal appears to be missing an interpolator id.
  "-Xlint:nullary-override",           // Warn when non-nullary `def f()' overrides nullary `def f'.
  "-Xlint:nullary-unit",               // Warn when nullary methods return Unit.
  "-Xlint:option-implicit",            // Option.apply used implicit view.
  "-Xlint:package-object-classes",     // Class or object defined in package object.
  "-Xlint:poly-implicit-overload",     // Parameterized overloaded implicit methods are not visible as view bounds.
  "-Xlint:private-shadow",             // A private field (or class parameter) shadows a superclass field.
  "-Xlint:stars-align",                // Pattern sequence wildcard must align with sequence component.
  "-Xlint:type-parameter-shadow",      // A local type parameter shadows a type already in scope.
  "-Xlint:unsound-match",              // Pattern match may not be typesafe.
  "-Yno-adapted-args",                 // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
  "-Ypartial-unification",             // Enable partial unification in type constructor inference
  "-Ywarn-dead-code",                  // Warn when dead code is identified.
  "-Ywarn-extra-implicit",             // Warn when more than one implicit parameter section is defined.
  "-Ywarn-inaccessible",               // Warn about inaccessible types in method signatures.
  "-Ywarn-infer-any",                  // Warn when a type argument is inferred to be `Any`.
  "-Ywarn-nullary-override",           // Warn when non-nullary `def f()' overrides nullary `def f'.
  "-Ywarn-nullary-unit",               // Warn when nullary methods return Unit.
  "-Ywarn-numeric-widen",              // Warn when numerics are widened.
  "-Ywarn-unused:imports"             // Warn if an import selector is not referenced.
  //"-Ywarn-value-discard"               // Warn when non-Unit expression results are unused.
)

wartremoverErrors ++= Seq(
  //Wart.Any ,                            // The Scala compiler loves to infer Any as a generic type but that is almost always incorrect. Explicit type arguments should be used instead.
  // Wart.AsInstanceOf,                    //  unsafe in isolation and violates parametricity when guarded by isInstanceOf. Refactor so that the desired type is proven statically.
  // Wart.DefaultArguments,              // Scala allows methods to have default arguments, which make it hard to use methods as functions.
  //Wart.EitherProjectionPartial,         // scala.util.Either.LeftProjection and scala.util.Either.RightProjection have a get method which will throw if the value doesn't match the projection.
  // The program should be refactored to use scala.util.Either.LeftProjection#toOption

  // Wart.Enumeration,                     //  Instead of Enumeration, a sealed abstract class extended by case objects should be used instead.
  Wart.ExplicitImplicitTypes,      // Scala has trouble correctly resolving implicits when some of them lack explicit result types. To avoid this all implicits should have explicit type ascriptions.
  // Wart.FinalCaseClass,              //Scala's case classes provide a useful implementation of logicless data types. Extending a case class can break this functionality in surprising ways. This can be avoided by always making them final.
  // Wart.IsInstanceOf,               // isInstanceOf violates parametricity. Refactor so that the type is established statically
  // JavaConversions,                 // The standard library provides implicits conversions to and from Java types in scala.collection.JavaConversions. This can make code difficult to understand and read about. The explicit conversions provided by scala.collection.JavaConverters instead.
  //Wart.ListOps,                     // cala.collection.immutable.List has methods which will throw if the list is empty. The program should be refactored to use methods ..explicitly handle both the populated and empty List
  //Wart.MutableDataStructures,       //Mutation breaks equational reasoning
  //Wart.NonUnitStatements,       //Scala allows statements to return any type. Statements should only return Unit (this ensures that they're really intended to be statements).
  //Wart.Nothing ,               //The Scala compiler loves to infer Nothing as a generic type but that is almost always incorrect. Explicit type arguments should be used instead.
  //Wart.Null  ,               // Null is a special value that inhabits all reference types. It breaks type safety.
  // Wart.Option2Iterable,      //Scala inserts an implicit conversion from Option to Iterable. This can hide bugs and creates surprising situations
  //Wart.OptionPartial ,         //scala.Option has a get method which will throw if the value is None. The program should be refactored to use scala.Option#fold to explicitly handle both the Some and None cases.
  //Wart.Product  ,             //Product is a type common to many structures; it is the supertype of case classes and tuples. The Scala compiler loves to infer Product as a generic type, but that is almost always incorrect. Explicit type arguments should be used instead.
  Wart.Return ,             //return breaks referential transparency. Refactor to terminate computations in a safe way.
  // Wart.Serializable         //The Scala compiler loves to infer Serializable as a generic type, but that is almost always incorrect. Explicit type arguments should be used instead.
  //Wart.Throw                 //throw implies partiality. Encode exceptions/errors as return values instead using Either
  //  Wart.ToString ,           // Scala creates a toString method automatically for all classes. Since toString is based on the class name, any rename can potentially introduce bugs. This is especially pernicious for case objects. toString should be explicitly overridden wherever used.
  Wart.TryPartial
)
wartremoverExcluded += baseDirectory.value / "target" / "scala-2.12" / "src_managed" / "main" / "sbt-buildinfo" / "BuildInfo.scala"
wartremoverExcluded += baseDirectory.value / "target" / "scala-2.12" / "routes" / "main" / "controllers" / "ReverseRoutes.scala"