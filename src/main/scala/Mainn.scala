import org.apache.spark
import org.apache.spark.{SparkConf, SparkContext, rdd}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.functions.col
import org.apache.spark.ml.feature.{HashingTF, IDF, VectorAssembler}
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.mllib.linalg.VectorUDT
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField, StructType}

import scala.collection.mutable.ArrayBuffer
import java.net.URLClassLoader
import java.util

object Mainn {

  def dropColumns(inputDF: DataFrame, dropList: List[String]): DataFrame =
    dropList.foldLeft(inputDF)((df, col) => df.drop(col))

  val amountColumns:Int= 12
  def main(args: Array[String]): Unit = {
    var counter = 0
    val nameColumns = ArrayBuffer[String]()
    nameColumns.insert(counter,"AMOUNT1")
    counter+=1
    while(counter < amountColumns) {
      var t= counter+1
      nameColumns.insert(counter,"AMOUNT"+t)
      counter+=1
    }
    val env: java.util.Map[String, String] = System.getenv
    val spark = SparkSession.builder.
      master("local")
//      .appName("spark session example")
//      .config("spark.testing.memory", "2147480000")
//      .config("fs.cos.myCos.iam.service.id", env.get("ServiceID"))
//      .config("fs.cos.myCos.endpoint", "s3.eu-de.cloud-object-storage.appdomain.cloud")
//      .config("fs.cos.myCos.iam.api.key","3VuFyjFd_WsFysHSnRinV_TSaB35ODKqai0igZpzJZ6l")
//      .config("fs.stocator.cos.scheme", "cos")
//      .config("fs.stocator.cos.impl", "com.ibm.stocator.fs.cos.COSAPIClient")
//      .config("fs.cos.impl", "com.ibm.stocator.fs.ObjectStoreFileSystem")
//      .config("fs.stocator.scheme.list", "cos")
//      .config("fs.cos.myCos.access.key","28cee42f068941dfa0a0c84376875eba")
//      .config("fs.cos.myCos.secret.key",env.get("SecretKey"))
//    .config("filename","file")
      .config("fs.cos.myCos.v2.signer.type","true").getOrCreate()
   Class.forName("com.ibm.db2.jcc.DB2Driver")
    var props = new java.util.Properties();
    props.setProperty("driver","com.ibm.db2.jcc.DB2Driver" );
    val df = spark.read.jdbc("jdbc:db2://dashdb-txn-sbox-yp-lon02-01.services.eu-gb.bluemix.net:50001/BLUDB:user=nwn80970;password=mg^hv694qsv36km4;sslConnection=true;"
      ,"NWN80970.MYTABLE2",props)
    println("dataframe show")
    df.printSchema()
    val df2 = df.withColumn("year_purchases", nameColumns.map(col).reduce((c1, c2)=>c1+c2))
    val df3 = dropColumns(df2,nameColumns.toList)
    df3.show()
    df3.write.format("csv").save("cos://productsofcompany.myCos/"+spark.sparkContext.getConf.get("filename")+".csv)")
    spark.close()
  }
}
