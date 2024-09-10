name := "Scala GCS-S3 Project"

version := "0.1"

scalaVersion := "2.13.12"

libraryDependencies += "com.amazonaws" % "aws-java-sdk-s3" % "1.12.154" // AWS S3 SDK

// You may need logging if you want more insights into errors, but it's optional.
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.6"
