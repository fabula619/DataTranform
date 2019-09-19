name := "DataTranform"

version := "0.1"

scalaVersion := "2.11.12"

val SparkVersion = "2.3.1"


// or if using sbt version < 0.13
// seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

libraryDependencies += "commons-lang" % "commons-lang" % "2.6"
// Note, I use a val with the Spark version
// to make it easier to include several Spark modules in my project,
// this way, if I want/have to change the Spark version,
// I only have to modify one line,
// and avoid strange erros because I changed some versions, but not others.
// Also note the 'Provided' modifier at the end,
// it indicates SBT that it shouldn't include the Spark bits in the generated jar
// neither in package nor assembly tasks.
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % SparkVersion %"provided",
  "org.apache.spark" %%"spark-sql" % SparkVersion %"provided"
)
libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.1.0"% "provided"
libraryDependencies += "io.snappydata" % "snappy-spark-mllib_2.11" % "2.1.1.7"% "provided"
libraryDependencies += "org.apache.spark" % "spark-mllib_2.11" % "2.2.0"
libraryDependencies += "com.ibm.db2" % "jcc" % "11.5.0.0"
libraryDependencies += "com.ibm.stocator" % "stocator" % "1.0.35"

//scla-fast-tests
// https://mvnrepository.com/artifact/MrPowers/spark-fast-tests
//libraryDependencies += "com.lihaoyi" %% "utest" % "0.6.3" % "test"
//libraryDependencies += "MrPowers" % "spark-fast-tests" % "0.17.1-s_2.11" % "test"


//scala test
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.8"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"
//resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"
//addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.3")
//resolvers += "jitpack" at "https://jitpack.io"
//libraryDependencies += "com.github.mrpowers" % "spark-fast-tests" % "v0.16.0" % "test"
//libraryDependencies += "com.lihaoyi" %% "utest" % "0.6.3" % "test"
//testFrameworks += new TestFramework("com.github.mrpowers.spark.fast.tests.CustomFramework") // scala test


//addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.2")
//libraryDependencies += "com.eed3si9n" % "sbt-assembly" % "0.13.0"
// https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
//libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.2"


//Thanks for using https://jar-download.com
//Thanks for using https://jar-download.com
//Thanks for using https://jar-download.com
// Exclude Scala from the assembly jar, because spark already includes it.
//assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"

// Scala 2.11
libraryDependencies += "MrPowers" % "spark-fast-tests" % "0.17.1-s_2.11"