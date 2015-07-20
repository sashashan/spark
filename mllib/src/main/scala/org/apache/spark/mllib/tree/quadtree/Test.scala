import org.apache.spark.rdd.RDD
import java.awt.Rectangle
import java.util.ArrayList

object Test{

  def main(
    	s_points: RDD[String],
	r_points: RDD[String]) {
		  
    val height = 2
    val width = 2
    
    val parsedData = s_points.map(_.split(' '))
    val allObjects = new ArrayList[Rectangle]() // S set
    
    
    val parsedData2 = r_points.map(_.split(' '))
    val rObjects = new ArrayList[Rectangle]() // R set
    
    val quad = new Quadtree(0, new Rectangle(0, 0, 600, 600))
    quad.clear()
    
    for (i <- 0 until allObjects.size) {
      quad.insert(allObjects.get(i))
    }
    
    val returnObjects = new ArrayList[Rectangle]()
    for (i <- 0 until allObjects.size) {
      returnObjects.clear()
      quad.retrieve(returnObjects, rObjects.get(i))
      for (l <- 0 until returnObjects.size) {
        println("X val" + returnObjects.get(l).x)
        println("Y val" + returnObjects.get(l).y)
      }
    }
  }
}
