import Dependencies.{Version => CommonVersion}
import sbt._

//Ejecutar spark en local
object SparkDependencies {
  val production: List[ModuleID] =
   // "org.apache.spark" %% "spark-core" % CommonVersion.sparkVersion :: // % "provided" ::
    //  "org.apache.spark" %% "spark-sql" % CommonVersion.sparkVersion :: //% "provided" ::
     // "org.apache.spark" %% "spark-mllib" % CommonVersion.sparkVersion :: //% "provided" ::
      // streaming-kafka
      //"org.apache.spark" % "spark-sql-kafka-0-10_2.12" % CommonVersion.sparkVersion ::
     // "org.apache.spark" %% "spark-sql-kafka-0-10" % CommonVersion.sparkVersion ::
  //Schema registry
   //   "org.apache.avro" % "avro" % "1.11.1" ::
      "com.hortonworks.registries" % "schema-registry-client" % "0.9.0" ::
      "com.hortonworks.registries" % "schema-registry-serdes" % "0.9.0" ::
  Nil

}



