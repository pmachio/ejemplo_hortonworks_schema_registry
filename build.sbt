ThisBuild / scalaVersion := "2.11.8"

lazy val spark_proj = (project in file("schema_registry_client_example"))
  .settings(name := "schema_registry_client_example")
  .settings(SparkAssemblyStrategy.value)
  //si queremos crear el ensamblado para lanzar spark en un cluster debemos usar SparkDependenciesProvided, para local SparkDependencies
  //.settings(libraryDependencies ++= SparkDependenciesProvided.production)
  .settings(libraryDependencies ++= SparkDependencies.production)
  //.settings(libraryDependencies ++= SparkDependenciesProvided.test)





