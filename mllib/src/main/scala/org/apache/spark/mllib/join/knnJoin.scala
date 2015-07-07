import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext._ 
import org.apache.spark.rdd.RDD
import scala.collection.immutable.Vector
import java.util.Random

object knnJoin {
	
	/**
	 * Top-level methods for calling knnJoin clustering.
	 * @param r_points points of the R set 
	 * @param s_points points of the S set
	 * @param dim the dimenstion of data sets
	 * @param k number of nearest neighbours 
	 * @param n number of partitions
	 */

	def run(
		r_points: RDD[Vector[Double]],
		s_points: RDD[Vector[Double]],
		dim: Int,
		k: Int,
		n: Int){
		
		println("Hello World!")
		
		val rand = new Random(n)
		
		
	}

}
