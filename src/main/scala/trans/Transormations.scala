//import org.apache.spark
//import org.apache.spark.{Partition, SparkConf, SparkContext, rdd}
//import org.apache.spark.sql.{DataFrame, Row, SparkSession}
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//import org.apache.spark.sql.functions._
//import org.apache.spark.sql.functions.col
//import org.apache.spark.ml.feature.{HashingTF, IDF, VectorAssembler}
//import org.apache.spark.ml.regression.LinearRegression
//import org.apache.spark.mllib.linalg.VectorUDT
//import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField, StructType}
//
//import scala.collection.mutable.ArrayBuffer
//import java.net.URLClassLoader
//import java.util
//
//class Transformations {
//  val nameColumns = ArrayBuffer[String]()
//  def dropColumns(inputDF: DataFrame, dropList: List[String]): DataFrame =
//    dropList.foldLeft(inputDF)((df, col) => df.drop(col))
//  def message(value: Partition): Unit = {
//    println("Partitions")
//    println(value.toString)
//  }
//
//  val amountColumns:Int= 12
//  def getSimpleData(sc:SparkSession,csvFileURL:String):DataFrame = {
//    sc.sqlContext.read.format("csv")
//      .option("header", "true")
//      .option("inferSchema", "false")
//      .option("parserLib", "univocity")
//      .option("delimiter", ",")
//      .load(csvFileURL);
//  }
//  //year_purchases
//  def getRealData(sc:SparkSession,jdcURL:String):DataFrame = {
//    Class.forName("com.ibm.db2.jcc.DB2Driver")
//    var props = new java.util.Properties();
//    props.setProperty("driver","com.ibm.db2.jcc.DB2Driver" );
//    props.setProperty("partitionColumn","YEAR")
//    props.setProperty("numPartitions","4")
//    props.setProperty("lowerBound","2014")
//    props.setProperty("upperBound","2018")// range not delim on 4 ///
//    val startReadData = System.currentTimeMillis
//    val df = sc.read.jdbc(jdcURL,"NWN80970.MYTABLE2",props)
//    val endReadData = System.currentTimeMillis
//    println("Read time: "+ (endReadData-startReadData))
//    return  df
//  }
//  def sumColumns(df:DataFrame,addNameColumn:String): DataFrame ={
//    df.withColumn(addNameColumn, nameColumns.map(col).reduce((c1, c2)=>c1+c2))
//  }
//  def main(args: Array[String]): Unit = {
//    val jdbcURL = "jdbc:db2://dashdb-txn-sbox-yp-lon02-01.services.eu-gb.bluemix.net:50001/BLUDB:user=nwn80970;password=mg^hv694qsv36km4;sslConnection=true;"
//    var counter = 0
//    nameColumns.insert(counter,"AMOUNT1")
//    counter+=1
//    while(counter < amountColumns) {
//      var t= counter+1
//      nameColumns.insert(counter,"AMOUNT"+t)
//      counter+=1
//    }
//    val env: java.util.Map[String, String] = System.getenv
//    val spark = SparkSession.builder.
//      master("local")
//      .config("fs.cos.myCos.iam.service.id", env.get("ServiceID"))
//      //      .appName("spark session example")
//      //      .config("spark.testing.memory", "2147480000")
//      //      .config("fs.cos.myCos.endpoint", "s3.eu-de.cloud-object-storage.appdomain.cloud")
//      //      .config("fs.cos.myCos.iam.api.key","3VuFyjFd_WsFysHSnRinV_TSaB35ODKqai0igZpzJZ6l")
//      //      .config("fs.stocator.cos.scheme", "cos")
//      //      .config("fs.stocator.cos.impl", "com.ibm.stocator.fs.cos.COSAPIClient")
//      //      .config("fs.cos.impl", "com.ibm.stocator.fs.ObjectStoreFileSystem")
//      //      .config("fs.stocator.scheme.list", "cos")
//      //      .config("fs.cos.myCos.access.key","28cee42f068941dfa0a0c84376875eba")
//      //    .config("filename","file")
//      .config("fs.cos.myCos.secret.key",env.get("SecretKey"))
//      .config("fs.cos.myCos.v2.signer.type","true").getOrCreate()
//    //    val startReadData = System.currentTimeMillis
//    val df1 = getSimpleData(spark, "data (6).csv")
//    //    println("partitions size = "+df.rdd.partitions.size)
//    //    //df.foreachPartition(partition =>  message(partitio)
//    //    val partitionArray = df.rdd.partitions
//    //    partitionArray.foreach(partition =>message(partition))
//    //    df.printSchema()
//    val df2 = sumColumns(df1,"year_purchases")
//    val df3 = dropColumns(df2,nameColumns.toList)
//    df3.show()
//    //df3.write.format("csv").save("cos://productsofcompany.myCos/"+spark.sparkContext.hadoopConfiguration.get("filename")+".csv)")
//    spark.close()
//  }
//}
