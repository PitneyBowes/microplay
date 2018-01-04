import scala.sys.Prop

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
