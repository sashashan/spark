package org.apache.spark.mllib.join
import org.sparkalgos.mllib.join.KnnJoin

object knnJoin {
	

	def main (args: Array[String]) {
		/**
                  * the input: <HDFS input file R points> <HDFS input file S points> <num. of partitions> <HDFS outfile file>
                  * outer: R points; inner: S points 
                  */
		
		println("Hello World!")
		
		val sc = new SparkContext("local","knn")

		val outer = sc.textFile(args(0))
		outer.count()
	}

}
