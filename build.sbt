scalaVersion := "2.12.8"

lazy val `microplay-lib` = project in file("microplay-lib")
lazy val `microplay-root` = (project in file(".")).aggregate(`microplay-lib`).settings(aggregate in publish := false)
//lazy val `microplay-root` = project in file(".")

skip in publish := true

