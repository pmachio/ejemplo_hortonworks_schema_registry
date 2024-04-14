package org.uam.masterbigdata.drivers


import com.hortonworks.registries.schemaregistry.SchemaCompatibility
import com.hortonworks.registries.schemaregistry.SchemaFieldQuery
import com.hortonworks.registries.schemaregistry.SchemaIdVersion
import com.hortonworks.registries.schemaregistry.SchemaMetadata
import com.hortonworks.registries.schemaregistry.SchemaVersion
import com.hortonworks.registries.schemaregistry.SchemaVersionInfo
import com.hortonworks.registries.schemaregistry.SchemaVersionKey
import com.hortonworks.registries.schemaregistry.avro.AvroSchemaProvider
import com.hortonworks.registries.schemaregistry.client.SchemaRegistryClient
import org.apache.commons.io.IOUtils

import java.io.InputStream



object SchemaRegistryTest {
  //val LOG: Logger = LoggerFactory.getLogger(SchemaRegistryTest.getClass)

  val configScala:Map[String, Object] = Map[String, Object](
    SchemaRegistryClient.Configuration.SCHEMA_REGISTRY_URL.name -> "http://127.0.0.1:10015/api/v1"
    , SchemaRegistryClient.Configuration.CLASSLOADER_CACHE_SIZE.name -> 10.asInstanceOf[Object]
    , SchemaRegistryClient.Configuration.CLASSLOADER_CACHE_EXPIRY_INTERVAL_SECS.name -> 5000.asInstanceOf[Object]
    , SchemaRegistryClient.Configuration.SCHEMA_VERSION_CACHE_SIZE.name -> 1000.asInstanceOf[Object]
    , SchemaRegistryClient.Configuration.SCHEMA_VERSION_CACHE_EXPIRY_INTERVAL_SECS.name -> (60 * 60 * 1000).asInstanceOf[Object]
  )

  val config:java.util.Map[String, Object] = new java.util.HashMap[String, Object]()
  configScala.foreach(p=> config.put(p._1, p._2))
  val schemaRegistryClient = new SchemaRegistryClient(config)

  @throws[Exception]
  def runSchemaApis(): Unit = {
    val schemaFileName: String = "/device.avsc"
    val schema1: String = getSchema(schemaFileName)
    val schemaMetadata: SchemaMetadata = createSchemaMetadata("com.hwx.schemas.sample-" + System.currentTimeMillis)
    // registering a new schema
    val v1: SchemaIdVersion = schemaRegistryClient.addSchemaVersion(schemaMetadata, new SchemaVersion(schema1, "Initial version of the schema"))
    println(s"Registered schema [$schema1] and returned version [$v1]")
    // adding a new version of the schema
    val schema2: String = getSchema("/device-next.avsc")
    val schemaInfo2: SchemaVersion = new SchemaVersion(schema2, "second version")
    val v2: SchemaIdVersion = schemaRegistryClient.addSchemaVersion(schemaMetadata, schemaInfo2)
   println(s"Registered schema [$schema2] and returned version [$v2]")
    //adding same schema returns the earlier registered version
    val version: SchemaIdVersion = schemaRegistryClient.addSchemaVersion(schemaMetadata, schemaInfo2)
   println(s"Received version [$version] for schema metadata [$schemaMetadata]")
    // get a specific version of the schema
    val schemaName: String = schemaMetadata.getName
    val schemaVersionInfo: SchemaVersionInfo = schemaRegistryClient.getSchemaVersionInfo(new SchemaVersionKey(schemaName, v2.getVersion))
   println(s"Received schema version info [$schemaVersionInfo] for schema metadata [$schemaMetadata]")
    // get latest version of the schema
    val latest: SchemaVersionInfo = schemaRegistryClient.getLatestSchemaVersionInfo(schemaName)
   println(s"Latest schema with schema key [$schemaMetadata] is : [$latest]")
    // get all versions of the schema
    val allVersions: java.util.Collection[SchemaVersionInfo] = schemaRegistryClient.getAllVersions(schemaName)
   println(s"All versions of schema key [$schemaMetadata] is : [$allVersions]")
    // finding schemas containing a specific field
    val md5FieldQuery: SchemaFieldQuery = new SchemaFieldQuery.Builder().name("md5").build
    val md5SchemaVersionKeys: java.util.Collection[SchemaVersionKey] = schemaRegistryClient.findSchemasByFields(md5FieldQuery)
   println(s"Schemas containing field query [$md5FieldQuery] : [$md5SchemaVersionKeys]")
    val txidFieldQuery: SchemaFieldQuery = new SchemaFieldQuery.Builder().name("txid").build
    val txidSchemaVersionKeys: java.util.Collection[SchemaVersionKey] = schemaRegistryClient.findSchemasByFields(txidFieldQuery)
   println(s"Schemas containing field query [$txidFieldQuery] : [$txidSchemaVersionKeys]" )
  }

  def getSchema(schemaFileName: String): String = {
    val schemaResourceStream: InputStream = SchemaRegistryTest
      .getClass
      .getResourceAsStream(schemaFileName)
    if (schemaResourceStream == null) {
      throw new IllegalArgumentException("Given schema file [" + schemaFileName + "] does not exist");
    }

    IOUtils.toString(schemaResourceStream, "UTF-8");
  }

  private def createSchemaMetadata(name: String) = new SchemaMetadata.Builder(name).`type`(AvroSchemaProvider.TYPE).schemaGroup("sample-group").description("Sample schema").compatibility(SchemaCompatibility.BACKWARD).build


  def main(args: Array[String]): Unit = {


    runSchemaApis()

    //runCustomSerDesApi()

    //runAvroSerDesApis()
  }

}
