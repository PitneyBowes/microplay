logLevel := Level.Info

externalResolvers :=  Seq("Artifactory Realm sbt-plugin-releases" at "https://artifactory-dev.bfretail.pitneycloud.com/artifactory/sbt-plugin-releases")
externalResolvers += "Artifactory Realm typesafe-ivy-releases" at "https://artifactory-dev.bfretail.pitneycloud.com/artifactory/typesafe-ivy-releases"
externalResolvers += "Artifactory Realm libs-release" at "https://artifactory-dev.bfretail.pitneycloud.com/artifactory/libs-release"

externalResolvers += Resolver.typesafeRepo("releases")
externalResolvers += Resolver.typesafeIvyRepo("releases")
externalResolvers += Resolver.sonatypeRepo("releases")
//externalResolvers += Resolver.sonatypeRepo("public")
//externalResolvers += Resolver.sonatypeRepo("snapshots")
externalResolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
/*
enable these as a fallback when PB artifactory is down:
externalResolvers += Resolver.jcenterRepo
externalResolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"
*/
//externalResolvers += "Atlassian Releases" at "https://maven.atlassian.com/public/"
//externalResolvers += "SpinGo OSS" at "http://spingo-oss.s3.amazonaws.com/repositories/releases"
externalResolvers += "Artifactory Realm libs-shapshot" at "https://artifactory-dev.bfretail.pitneycloud.com/artifactory/libs-snapshot"
externalResolvers += "Artifactory Realm ext" at "https://artifactory-dev.bfretail.pitneycloud.com/artifactory/ext-release-local"
externalResolvers += "Artifactory Realm plugins-release" at "https://artifactory-dev.bfretail.pitneycloud.com/artifactory/plugins-release"
externalResolvers += "Artifactory Realm plugins-snapshot" at "https://artifactory-dev.bfretail.pitneycloud.com/artifactory/plugins-snapshot"
externalResolvers += "Sonatype Repository" at "https://oss.sonatype.org/content/groups/public"


addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.7")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.9.3")

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.2.1")

addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-RC13")