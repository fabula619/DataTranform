//import org.apache.spark.sql.SparkSession
//import org.apache.spark.sql.SparkSession.Builder
//
//object OptimizePartitions {
//  def main(args: Array[String]): Unit = {
//    val spark = SparkSession.builder.master("yarn").enableHiveSupport().
//   config("spark.executor.heartbeatInterval","120s").config("spark.network.timeout","12000s").config("spark.sql.inMemoryColumnarStorage.compressed", "true").
//      config("spark.sql.orc.filterPushdown","true").config("spark.serializer", "org.apache.spark.serializer.KryoSerializer").config("spark.kryoserializer.buffer.max","512m").config("spark.serializer", classOf[org.apache.spark.serializer.KryoSerializer].getName).config("spark.streaming.stopGracefullyOnShutdown","true").config("spark.yarn.driver.memoryOverhead","7168").config("spark.yarn.executor.memoryOverhead","7168").config("spark.sql.shuffle.partitions", "61").config("spark.default.parallelism", "60")
//      .config("spark.memory.storageFraction","0.5").config("spark.memory.fraction","0.6").config("spark.memory.offHeap.enabled","true").
//      config("spark.memory.offHeap.size","16g").config("spark.dynamicAllocation.enabled", "false").config("spark.dynamicAllocation.enabled","true").config("spark.shuffle.service.enabled","true")
//    .config("hive.exec.dynamic.partition", "true").config("hive.exec.dynamic.partition.mode", "nonstrict").getOrCreate()
//  }
//
//}
