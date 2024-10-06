ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"
libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-common" % "3.3.6",
  "org.apache.hadoop" % "hadoop-mapreduce-client-core" % "3.3.6",
  "org.apache.hadoop" % "hadoop-mapreduce-client-jobclient" % "3.3.6",
  "org.scala-lang" % "scala-library" % "2.13.8",  // Add Scala standard library dependency
  "org.deeplearning4j" % "deeplearning4j-nlp" % "1.0.0-M2.1",
  "org.deeplearning4j" % "deeplearning4j-core" % "1.0.0-M2.1",
  "org.nd4j" % "nd4j-native-platform" % "1.0.0-M2.1",
  "com.knuddels" % "jtokkit" % "1.1.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.slf4j" % "slf4j-api" % "1.7.30",
  "com.typesafe" % "config" % "1.4.3",
  "org.scalatest" %% "scalatest" % "3.2.15" % Test,
  "org.scalamock" %% "scalamock" % "5.2.0" % Test,
  "org.mockito" %% "mockito-scala" % "1.17.7" % Test,
    "org.scalatest" %% "scalatest" % "3.2.9" % Test,
    "org.mockito" % "mockito-core" % "4.0.0" % Test,
    "org.scalatestplus" %% "mockito-3-4" % "3.2.9.0" % Test

)
lazy val root = (project in file("."))
  .settings(
    name := "CloudLLMProject",

    // Set the name of the assembled JAR
    assemblyJarName in assembly := "CloudLLMProject-assembly.jar",

//    // Ensure the Scala library is included in the JAR
//    assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = true),
//
//    // Merge strategy to handle deduplication errors
//    assemblyMergeStrategy in assembly := {
//      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
//      case PathList("org", "slf4j", _*)  => MergeStrategy


//    }
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", xs@_*) =>
        xs match {
          case "MANIFEST.MF" :: Nil => MergeStrategy.discard
          case "services" :: _ => MergeStrategy.concat
          case _ => MergeStrategy.discard
        }

      case "reference.conf" => MergeStrategy.concat
      case x if x.endsWith(".proto") => MergeStrategy.rename
      case x if x.contains("hadoop") => MergeStrategy.first
      case PathList("org", "slf4j", _*) => MergeStrategy.first // Handle SLF4J binding conflicts
      case PathList("org", "nd4j", _*) => MergeStrategy.first // Handle ND4J backend conflicts
      case PathList("org", "deeplearning4j", _*) => MergeStrategy.first // Handle Deeplearning4j conflicts
      case _ => MergeStrategy.first
    }

  )

