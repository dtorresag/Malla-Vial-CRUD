// The Play plugin
addSbtPlugin("org.playframework" % "sbt-plugin" % "3.0.5")

// Defines scaffolding (found under .g8 folder)
// http://www.foundweekends.org/giter8/scaffolding.html
// sbt "g8Scaffold form"
addSbtPlugin("org.foundweekends.giter8" % "sbt-giter8-scaffold" % "0.16.2")
addSbtPlugin("com.github.sbt" % "sbt-eclipse" % "6.0.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-play-ebean" % "6.1.0")