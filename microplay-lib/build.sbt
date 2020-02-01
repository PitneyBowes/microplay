import java.time.{ZoneOffset, ZonedDateTime}

name := "microplay"
organization := "com.pb"
version := sys.env.getOrElse("VERSION", "3.26.0-SNAPSHOT")
scalaVersion := "2.12.8"
bintrayRepository := "PB_Maven"
bintrayPackage := "microplay"
licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.txt"))

lazy val `microplay-lib` = (project in file(".")).configs(IntegrationTest).settings(Defaults.itSettings: _*).enablePlugins(PlayScala, BuildInfoPlugin, GitVersioning)

//resolvers := Seq(Resolver.file("local", file(Path.userHome.absolutePath + "/.ivy2/cache"))(Resolver.ivyStylePatterns))

libraryDependencies ++= Seq(
  ws,
  specs2 % "test,it",
  guice,
  "org.json4s" %% "json4s-native" % "3.5.3", "org.json4s" %% "json4s-ext" % "3.5.3", //for json 2 xml conversion, to support xml as an alternative response medium as part of content negotiation
  "io.swagger" %% "swagger-play2" % "1.6.1",
  "org.webjars" % "swagger-ui" % "3.20.3",
  "ch.qos.logback.contrib" % "logback-json-classic" % "0.1.5",
  "ch.qos.logback.contrib" % "logback-jackson" % "0.1.5",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "net.logstash.logback" % "logstash-logback-encoder" %  "5.3",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.8"
)
//unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

buildInfoPackage := "com.pb.microplay"
lazy val buildTime: SettingKey[String] = SettingKey[String]("buildTime", "time of build")
ThisBuild / buildTime := ZonedDateTime.now(ZoneOffset.UTC).toString //Need to generate build time statically instead of using 'BuildInfoOption.BuildTime' - to avoid re-compilation at it:test phases which causes sbt-coverage to clean the coverage data from the test phase which messes up the coverage report
buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion, git.gitHeadCommit,buildTime)
buildInfoOptions ++= Seq(BuildInfoOption.ToJson, BuildInfoOption.ToMap, /*BuildInfoOption.BuildTime,*/  BuildInfoOption.Traits("com.pb.microplay.services.BuildInfoMeta"))

sourceDirectory in IntegrationTest := baseDirectory.value / "it"
scalaSource in IntegrationTest := baseDirectory.value / "it"
coverageExcludedPackages := "controllers.*;.*Reverse.*Controller"

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
