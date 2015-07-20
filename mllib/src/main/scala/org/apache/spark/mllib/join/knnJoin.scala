import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext._ 
import org.apache.spark.rdd.RDD

import java.io._
import java.util._

object knnJoin {
	
	// Note to self: need to create a global variable "dimension" and set it in run
	
	
	/**
	 * Top-level methods for calling knnJoin clustering.
	 * @param r_points points of the R set 
	 * @param s_points points of the S set
	 * @param dim the dimenstion of data sets
	 * @param k number of nearest neighbours 
	 * @param n number of partitions
	 */

	def run(
		r_points: RDD[String],
		s_points: RDD[String],
		dim: Int,
		k: Int,
		numberOfPartition: Int){
		
		//setDim(dim)
		println("Hello World!")
		
		val rand = new Random(numberOfPartition)
		
		//vectors.map(e => (rand.nextInt(5), e))
		
	}
	
	/**
	//returns: B object
	def func1 (s: Array[String]): B = {
		val scale = 1000
		val coord = Array.ofDim[Float](2) //dimension; should I maybe use Double?
		for (i <- 0 until 2) {
        		coord(i) = s(i).toFloat
      		}
		val converted_coord = Array.ofDim[Int](2) //dimension
      		for (i <- 0 until 2) { //dimension
        		converted_coord(i) = coord(i).toInt
        		coord(i) = coord(i) - converted_coord(i)
        		converted_coord(i) *= scale
        		val temp = (coord(i) * scale).toInt
        		converted_coord(i) += temp
      		}
      		val zval = Zorder.valueOf(2, converted_coord) //dimension
      		println(zval)
		val b = new B(zval.asInstanceOf[java.io.Serializable], 0) //source
		return b
	}
	*/

}
