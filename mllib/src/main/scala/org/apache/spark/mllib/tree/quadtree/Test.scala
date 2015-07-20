import java.awt.Rectangle
import java.util.ArrayList

object Test{

  def main(args: Array[String]) {
    val quad = new Quadtree(0, new Rectangle(0, 0, 600, 600))
    quad.clear()
    val allObjects = new ArrayList()
    for (i <- 0 until allObjects.size) {
      quad.insert(allObjects.get(i))
    }
    val returnObjects = new ArrayList()
    for (i <- 0 until allObjects.size) {
      returnObjects.clear()
      quad.retrieve(returnObjects, objects.get(i))
      for (x <- 0 until returnObjects.size) {
      }
    }
  }
}
