package myTests
import org.scalatest._
import com.github.mrpowers.spark.fast.tests._
import org.apache.spark.sql.{DataFrame, SparkSession}
import trans.Transformator
abstract class UnitSpec extends FunSpec {
  val transformator: Transformator = new Transformator()
  var spark:SparkSession
  var sourceDF:DataFrame
}
