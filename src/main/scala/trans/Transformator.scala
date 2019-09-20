package trans

import org.apache.spark.Partition
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable.ArrayBuffer

class Transformator (){
  val nameColumns = ArrayBuffer[String]()
  @throws(classOf[IllegalArgumentException])
  def dropColumns(inputDF: DataFrame, dropList: List[String]): DataFrame = {
    if (inputDF == null || dropList == null)
      throw new IllegalArgumentException("illegal argument exception")
    dropList.foldLeft(inputDF)((df, col) => df.drop(col))
  }
  def message(value: Partition): Unit = {
    println("Partitions")
    println(value.toString)
  }
  val amountColumns:Int= 12
  def getSimpleData(sc:SparkSession,csvFileURL:String):DataFrame = {
    sc.sqlContext.read.format("csv")
      .option("header", "true")
      .option("inferSchema", "false")
      .option("parserLib", "univocity")
      .option("delimiter", ",")
      .load(csvFileURL);
  }
  def getRealData(sc:SparkSession,jdcURL:String):DataFrame = {
    Class.forName("com.ibm.db2.jcc.DB2Driver")
    var props = new java.util.Properties();
    props.setProperty("driver","com.ibm.db2.jcc.DB2Driver" );
    props.setProperty("partitionColumn","YEAR")
    props.setProperty("numPartitions","4")
    props.setProperty("lowerBound","2014")
    props.setProperty("upperBound","2018")// range not delim on 4 ///
    val startReadData = System.currentTimeMillis
    val df = sc.read.jdbc(jdcURL,"NWN80970.MYTABLE2",props)
    val endReadData = System.currentTimeMillis
    println("Read time: "+ (endReadData-startReadData))
    return  df
  }
  @throws(classOf[IllegalArgumentException])
  def sumColumns(df:DataFrame,addNameColumn:String): DataFrame ={
    if(df==null)
      throw new IllegalArgumentException("method require non null dataframe")
    df.withColumn(addNameColumn, nameColumns.map(col).reduce((c1, c2)=>c1+c2))
  }
  def initColumnsName(): ArrayBuffer[String] =
  {
    if(nameColumns.nonEmpty)
      return nameColumns
    var counter = 0
    nameColumns.insert(counter,"AMOUNT1")
    counter+=1
    while(counter < amountColumns) {
      var t= counter+1
      nameColumns.insert(counter,"AMOUNT"+t)
      counter+=1
    }
    return nameColumns
  }
  def initTransformator(): SparkSession ={
    //val jdbcURL = "jdbc:db2://dashdb-txn-sbox-yp-lon02-01.services.eu-gb.bluemix.net:50001/BLUDB:user=nwn80970;password=mg^hv694qsv36km4;sslConnection=true;"
    initColumnsName()
    val env: java.util.Map[String, String] = System.getenv
    SparkSession.builder.
      master("local")
      .config("fs.cos.myCos.iam.service.id", env.get("ServiceID"))
      .config("fs.cos.myCos.secret.key",env.get("SecretKey"))
      .getOrCreate()
  }
  def release(spark:SparkSession): Unit ={
    spark.close()
  }
}
