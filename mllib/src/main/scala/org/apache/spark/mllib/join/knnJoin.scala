import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext._ 
import org.apache.spark.rdd.RDD

//Testing elki library
import de.lmu.ifi.dbs.elki.data.FloatVector
import de.lmu.ifi.dbs.elki.data.DoubleVector
import de.lmu.ifi.dbs.elki.database.DistanceResultPair
import de.lmu.ifi.dbs.elki.distance.FloatDistance
import de.lmu.ifi.dbs.elki.distance.DoubleDistance
import de.lmu.ifi.dbs.elki.distance.distancefunction.DistanceFunction
import de.lmu.ifi.dbs.elki.distance.distancefunction.EuclideanDistanceFunction
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.rstar.RStarTree
import de.lmu.ifi.dbs.elki.index.tree.spatial._
import de.lmu.ifi.dbs.elki.index.tree.TreeIndex
import de.lmu.ifi.dbs.elki.index._
import de.lmu.ifi.dbs.elki.parser._
import de.lmu.ifi.dbs.elki.utilities.optionhandling.ParameterException
import de.lmu.ifi.dbs.elki.utilities.optionhandling.parameterization.ListParameterization
import de.lmu.ifi.dbs.elki.index.Zorder

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
		n: Int){
		
		//setDim(dim)
		println("Hello World!")
		
		val rand = new Random(n)
		
		//vectors.map(e => (rand.nextInt(5), e))
		
		/**
		 * Testing Zorder
		 * Taking in a RDD[String]: <x value>(String) <y value>(String)
		 * and mapping to to key/value: key: rid, vlaue: B(zvalue, source)
		 */
		 
		val parsedData = s_points.map(_.split(' ')) 
		val parsed = parsedData.map(line => line.map(e => (line(0), func1(line))))
		parsed.first()
		
		
	}
	
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
		val b = new B(zval, 0) //source
		println("from func1")
		return b
	}

}
