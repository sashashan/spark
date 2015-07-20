import org.apache.spark.rdd.RDD
import java.awt.Rectangle
import java.util.ArrayList

object Test{

  def main(
    	s_points: RDD[String],
	r_points: RDD[String]) {
		  
    val height = 1
    val width = 1
    
    val parsedData = s_points.map(_.split(' ')) //RDD[String]
    val recData = parsedData.map(line => new Rectangle(line(0).toInt, line(1).toInt, width, height))
    val allObjects = new ArrayList[Rectangle]() // S set
    recData.collect().foreach(line => allObjects.add(line))
    
    
    val parsedData2 = r_points.map(_.split(' '))
    val recData2 = parsedData.map(line => new Rectangle(line(0).toInt, line(1).toInt, width, height))
    val rObjects = new ArrayList[Rectangle]() // R set
    recData2.collect().foreach(line => allObjects.add(line))
    
    val quad = new Quadtree(0, new Rectangle(0, 0, 12, 12))
    quad.clear()
    
    for (i <- 0 until allObjects.size) {
      quad.insert(allObjects.get(i))
    }
    
    val returnObjects = new ArrayList[Rectangle]()
    for (i <- 0 until allObjects.size) {
      returnObjects.clear()
      quad.retrieve(returnObjects, rObjects.get(i))
      for (l <- 0 until returnObjects.size) {
        println(returnObjects.get(l).toString())
      }
    }
    
  } //end main
} //end class
