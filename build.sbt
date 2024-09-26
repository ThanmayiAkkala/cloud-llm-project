ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"
libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-common" % "3.3.6",
  "org.apache.hadoop" % "hadoop-mapreduce-client-core" % "3.3.6",
  "org.apache.hadoop" % "hadoop-mapreduce-client-jobclient" % "3.3.6",
  "org.scala-lang" % "scala-library" % "2.13.8",  // Add Scala standard library dependency
  "org.deeplearning4j" % "deeplearning4j-nlp" % "1.0.0-M1.1",
  "org.deeplearning4j" % "deeplearning4j-core" % "1.0.0-M1.1",
  "org.nd4j" % "nd4j-native-platform" % "1.0.0-M1.1",
  "com.knuddels" % "jtokkit" % "1.1.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.slf4j" % "slf4j-api" % "1.7.30"
)
lazy val root = (project in file("."))
  .settings(
    name := "CloudLLMProject",

    // Set the name of the assembled JAR
    assemblyJarName in assembly := "CloudLLMProject-assembly.jar",

    // Ensure the Scala library is included in the JAR
    assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = true),

    // Merge strategy to handle deduplication errors
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case PathList("org", "slf4j", _*)  => MergeStrategy.first
      case x => MergeStrategy.first
    }
  )

