package myTests


import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.junit.Before
import org.scalactic.source.Position
import org.scalatest.BeforeAndAfter
import trans.Transformator

import scala.collection.mutable.ArrayBuffer
class FunctionTest extends  UnitSpec with DataFrameComparer with BeforeAndAfter {
  val columnsName1 = "product_id"
  val columnsName2 = "product_group"
  val columnsName3 = "product_year"
  val newColumn = "year_purchases"
  val amountColumns = transformator.amountColumns
  var columnsName:ArrayBuffer[String] = new ArrayBuffer[String]()
  override var spark = transformator.initTransformator()
  override var sourceDF: DataFrame = _
  def initDefaultSourceDF(){
    columnsName += columnsName1
    columnsName += columnsName2
    columnsName += columnsName3
    var rdd = spark.sparkContext.parallelize(Seq((1, 1, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), (1, 1, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13)))
    columnsName ++= transformator.initColumnsName()
    sourceDF = spark.createDataFrame(rdd).toDF(columnsName: _*)
  }
  before
  {
   //initDefaultSourceDF()
  }
  describe("A Transformations should sum values at 12 columns and save result into new column") {
    try {
      initDefaultSourceDF()
      var sourceDF1 = transformator.sumColumns(sourceDF, newColumn)
      columnsName += newColumn
      val expectedRDD = spark.sparkContext.parallelize(Seq((1, 1, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 78), (1, 1, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 79)))
      val expectedDF = spark.createDataFrame(expectedRDD).toDF(columnsName: _*)
      it("DataFrames shoud be equal") {
        assertSmallDataFrameEquality(expectedDF, sourceDF1, false)
      }
      it("if inputDataFrame is empty then a exception shoud be thrown away") {
        assertThrows[IllegalArgumentException] {
          sourceDF1 = transformator.sumColumns(null, newColumn)
        }
      }
    }
    catch
    {
      case y:Throwable =>println(y)
    }
  }
  describe("A transformation should drop setting columns")
  {
    it("shoud drop several columns and schema be right change") {
      sourceDF.printSchema()
      val sourceDF2 = transformator.dropColumns(sourceDF, transformator.initColumnsName().toList)
      sourceDF2.printSchema()
      println(sourceDF2.schema.length)
      assert(sourceDF2.schema.length.equals(3))
//      assert(sourceDF2.schema.contains(columnsName1))
//      assert(sourceDF2.schema.contains(columnsName2))
//      assert(sourceDF2.schema.contains(columnsName3))
//      assert(sourceDF.schema.contains(newColumn))
    }
    it("should drop right one column")
    {
      sourceDF.printSchema()
      val sourceDF1  = transformator.dropColumns(sourceDF,List(columnsName1))
      var rdd = spark.sparkContext.parallelize(Seq(( 1, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), (1, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13)))
      val array = sourceDF.columns.drop(1)
      val expectedDF1 = spark.createDataFrame(rdd).toDF(array: _*)
      assertSmallDataFrameEquality(sourceDF1, expectedDF1, false)
    }
  }

  //
}
