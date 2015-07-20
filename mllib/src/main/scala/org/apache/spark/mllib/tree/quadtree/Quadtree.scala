import scala.collection.JavaConversions._
import java.awt.Rectangle
import java.util.ArrayList

class Quadtree(private var level: Int, private var bounds: Rectangle) {
  // bounds represents the 2D space that the node occupies
  private var MAX_LEVELS: Int = 5
  private var objects: List = new ArrayList()
  private var nodes: Array[Quadtree] = new Array[Quadtree](4)
  
  def clear() {
    objects.clear()
    for (i <- 0 until nodes.length if nodes(i) != null) {
    nodes(i).clear()
    nodes(i) = null
    }
  }
  
  private def split() {
    val subWidth = (bounds.getWidth / 2).toInt
    val subHeight = (bounds.getHeight / 2).toInt
    val x = bounds.getX.toInt
    val y = bounds.getY.toInt
    nodes(0) = new Quadtree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight))
    nodes(1) = new Quadtree(level + 1, new Rectangle(x, y, subWidth, subHeight))
    nodes(2) = new Quadtree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight))
    nodes(3) = new Quadtree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight))
  }

  private def getIndex(pRect: Rectangle): Int = {
    var index = -1
    val verticalMidpoint = bounds.getX + (bounds.getWidth / 2)
    val horizontalMidpoint = bounds.getY + (bounds.getHeight / 2)
    val topQuadrant = (pRect.getY < horizontalMidpoint && pRect.getY + pRect.getHeight < horizontalMidpoint)
    val bottomQuadrant = (pRect.getY > horizontalMidpoint)
    if (pRect.getX < verticalMidpoint && pRect.getX + pRect.getWidth < verticalMidpoint) {
      if (topQuadrant) {
        index = 1
      } else if (bottomQuadrant) {
        index = 2
      }
    } else if (pRect.getX > verticalMidpoint) {
      if (topQuadrant) {
        index = 0
      } else if (bottomQuadrant) {
        index = 3
      }
    }
    index
  }

  def insert(pRect: Rectangle) {
    if (nodes(0) != null) {
      val index = getIndex(pRect)
      if (index != -1) {
        nodes(index).insert(pRect)
        return
      }
    }
    objects.add(pRect)
    if (objects.size > MAX_OBJECTS && level < MAX_LEVELS) {
      if (nodes(0) == null) {
        split()
      }
      var i = 0
      while (i < objects.size) {
        val index = getIndex(objects.get(i))
        if (index != -1) {
          nodes(index).insert(objects.remove(i))
        } else {
          i += 1
        }
      }
    }
  }
  
  def retrieve(returnObjects: List, pRect: Rectangle): List = {
    val index = getIndex(pRect)
    if (index != -1 && nodes(0) != null) {
      nodes(index).retrieve(returnObjects, pRect)
    }
    returnObjects.addAll(objects)
    returnObjects
  }
  
  
} //end class
