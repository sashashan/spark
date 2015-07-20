import java.awt.Rectangle
import java.util.ArrayList

object Test{

  def main(
    r_points: RDD[String],
		s_points: RDD[String]) {
		  
    val height = 2
    val width = 2
    
    val quad = new Quadtree(0, new Rectangle(0, 0, 600, 600))
    quad.clear()
    val allObjects = new ArrayList() // S set
    for (i <- 0 until allObjects.size) {
      quad.insert(allObjects.get(i))
    }
    val rObjects = new ArrayList() // R set
    val returnObjects = new ArrayList()
    for (i <- 0 until allObjects.size) {
      returnObjects.clear()
      quad.retrieve(returnObjects, rObjects.get(i))
      for (x <- 0 until returnObjects.size) {
        println(returnObjects.get(x).x)
      }
    }
  }
}
