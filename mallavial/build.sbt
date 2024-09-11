name := """MallaVial"""
organization := "com.geoSAT"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.14"

libraryDependencies ++= Seq(
javaJdbc,
cacheApi,
guice
)


EclipseKeys.preTasks := Seq(compile in Compile, compile in Test)
EclipseKeys.projectFlavor := EclipseProjectFlavor.Java
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(
  EclipseCreateSrc.ManagedClasses,
  EclipseCreateSrc.ManagedResources
)