name := """mallaVialB"""
organization := "com.geoSAT"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.14"

libraryDependencies ++= Seq(
javaJdbc,
cacheApi,
guice,
javaJpa,
"org.hibernate" % "hibernate-core" % "6.5.2.Final",
"org.postgresql" % "postgresql" % "42.5.1",
"jakarta.persistence" % "jakarta.persistence-api" % "3.0.0",
"com.typesafe.akka" %% "akka-actor-typed" % "2.6.20",  
"com.typesafe.akka" %% "akka-stream" % "2.6.20",
"com.typesafe.akka" %% "akka-actor" % "2.6.20"
)


PlayKeys.externalizeResourcesExcludes += baseDirectory.value

EclipseKeys.preTasks := Seq(compile in Compile, compile in Test)
EclipseKeys.projectFlavor := EclipseProjectFlavor.Java
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(
  EclipseCreateSrc.ManagedClasses,
  EclipseCreateSrc.ManagedResources
)