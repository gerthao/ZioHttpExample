ThisBuild / scalaVersion := "3.2.1"
ThisBuild / version      := "0.1.0"

lazy val root = (project in file("."))
  .settings {
    name := "ZIO HTTP Example"
  }

libraryDependencies ++= Seq(
  "dev.zio"     %% "zio"            % "2.0.6",
  "dev.zio"     %% "zio-streams"    % "2.0.6",
  "dev.zio"     %% "zio-json"       % "0.4.2",
  "dev.zio"     %% "zio-http"       % "0.0.3",
  "io.getquill" %% "quill-zio"      % "4.6.0",
  "io.getquill" %% "quill-jdbc-zio" % "4.6.0"
)

libraryDependencies += "com.github.pureconfig"   %% "pureconfig-core" % "0.17.2"