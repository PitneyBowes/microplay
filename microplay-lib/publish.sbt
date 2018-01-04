import scala.sys.Prop
publishTo := Some("Artifactory Realm Publish" at "https://artifactory-dev.bfretail.pitneycloud.com/artifactory/libs-release-local")

credentials += {
  val artifactoryUser = Prop[String]("artifactory_user")
  val artifactoryPassword = Prop[String]("artifactory_password")
  if(artifactoryUser.isSet && artifactoryPassword.isSet)
  {
    println(s"publishing using artifactory_user=$artifactoryUser")
    Credentials("Artifactory Realm", "artifactory-dev.bfretail.pitneycloud.com", artifactoryUser.get, artifactoryUser.get)
  }
  else
  {
    println(s"loading publish credentials from ~/.ivy2/.credentials")
    Credentials(Path.userHome / ".ivy2" / ".credentials")
  }
}
