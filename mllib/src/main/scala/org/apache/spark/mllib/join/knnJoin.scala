import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext._ 

object knnJoin {
	

	def main (args: Array[String]) {
		/**
      * the input: <HDFS input file R points> <HDFS input file S points> <num. of partitions> <HDFS outfile file>
      * outer: R points; inner: S points 
      */
		
		println("Hello World!")
		
                val conf = new SparkConf().setAppName("knn")
                val sc = new SparkContext(conf)

		val outer = sc.textFile(args(0))
		outer.count()
	}

}
