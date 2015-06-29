object knnJoin
{

	def main (args: Array[String])
	{

		/**
                  * the input: <HDFS input file R points> <HDFS input file S points> <num. of partitions> <HDFS outfile file>
		  * outer: R points; inner: S points 
		  */

		val outer = sc.textFile(args[0])
		val inner = sc.textFile(args[1])
		val n = args[2]

		outer.count()
		inner.count()
	}
}
