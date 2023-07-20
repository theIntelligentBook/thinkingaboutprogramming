enablePlugins(ScalaJSPlugin)

name := "Thinking about Programming"
scalaVersion := "3.1.1"

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

resolvers += "jitpack" at "https://jitpack.io"

updateOptions := updateOptions.value.withLatestSnapshots(false)

libraryDependencies ++= Seq(
  "com.github.wbillingsley" % "lavamaze" % "master-SNAPSHOT", // Need to single-% as it's a top-level jitpack project
  "com.github.theintelligentbook" % "circuitsup" % "master-SNAPSHOT"
)

val deployScript = taskKey[Unit]("Copies the fullOptJS script to deployscripts/")
val fastDeploy = taskKey[Unit]("Copies the fastOptJS script to deployscripts/")

// Used by Travis-CI to get the script out from the .gitignored target directory
// Don't run it locally, or you'll find the script gets loaded twice in index.html!
deployScript := {
  val opt = (Compile / fullOptJS).value
  IO.copyFile(opt.data, new java.io.File("deployscripts/compiled.js"))
}

fastDeploy := {
  val opt = (Compile / fastOptJS).value
  IO.copyFile(opt.data, new java.io.File("deployscripts/compiled.js"))
}