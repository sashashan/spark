package org.apache.spark.mllib.tree.quadtree
import scala.util.Sorting

import scala.collection.JavaConversions._
import java.util.ArrayList
import java.awt.geom.Rectangle2D

class Quadtree(private var level: Int, private var bounds: Rectangle2D) extends Serializable {
  // bounds represents the 2D space that the node occupies
  private var MAX_LEVELS: Int = 1000 
  private var MAX_OBJECTS: Int = 3 // maximum objects per node
  private var NUM_OBJECTS: Int 
  private var objects = new ArrayList[Point]()
  private var nodes: Array[Quadtree] = new Array[Quadtree](4)
  override def toString() = "This tree has " + NUM_OBJECTS + " points " + "Showing the tree: " + printTreeR
  
  /**
   * Clears the quadtree.
   */
  def clear() {
    objects.clear()
    for (i <- 0 until nodes.length if nodes(i) != null) {
    nodes(i).clear()
    nodes(i) = null
    }
  }
  
  /**
   * Splits the node is split into 4.
   */
  private def split() {
    val subWidth = (bounds.getWidth / 2)
    val subHeight = (bounds.getHeight / 2)
    val x = bounds.getX
    val y = bounds.getY
    nodes(0) = new Quadtree(level + 1, new Rectangle2D.Double(x + subWidth, y, subWidth, subHeight))
    nodes(1) = new Quadtree(level + 1, new Rectangle2D.Double(x, y, subWidth, subHeight))
    nodes(2) = new Quadtree(level + 1, new Rectangle2D.Double(x, y + subHeight, subWidth, subHeight))
    nodes(3) = new Quadtree(level + 1, new Rectangle2D.Double(x + subWidth, y + subHeight, subWidth, subHeight))
  }

  /**
   * Determines which node the object belongs to.
   * -1 means that it is on the midline and so cannot be passed to children;
   * and it is left inside the parent node.
   *  _________
   * | 1  | 0  |
   * |____|____|
   * | 2  | 3  | 
   * |____|____|
   */
  private def getIndex(p: Point): Int = {
    var index = -1
    val verticalMidpoint = bounds.getX + (bounds.getWidth / 2)
    val horizontalMidpoint = bounds.getY + (bounds.getHeight / 2)
    // 
    val topQuadrant = (p.getY < horizontalMidpoint)
    val bottomQuadrant = (p.getY > horizontalMidpoint)
    if (p.getX < verticalMidpoint) {
      if (topQuadrant) {
        index = 1
      } else if (bottomQuadrant) {
        index = 2
      }
    } else if (p.getX > verticalMidpoint) {
      if (topQuadrant) {
        index = 0
      } else if (bottomQuadrant) {
        index = 3
      }
    }
    index
  }
  
  /**
   * Checks if the point is inside or touching the node.
   * This is neccessary for the special case of retrieving the points for kNN 
   * when the r point lies on the midline. 
   */
  def isOutsideTheNode(p: Point): Boolean = {
    val isAboveOrBelow = (p.getY < bounds.getY) || (p.getY > (bounds.getY + bounds.getHeight))
    val isRightOrLeft = (p.getX > (bounds.getX + bounds.getWidth)) || (p.getX < bounds.getX)
    isAboveOrBelow || isRightOrLeft
  }

  /**
   * Inserts objects into the tree. When a node exceeds the max capacity
   * of objects, the node is split into 4 and all of the objects are "moved"
   * to the coresponding children.
   */
   
  // A method for interacting with RDD 
  def insert(p: Point): Quadtree = {
    NUM_OBJECTS += 1
    insertR(p)
    return this
  } 
  
  def insertR(p: Point) {
    println("insert: I'm called")
    // if the node is not a leaf ...
    if (nodes(0) != null) {
      val index = getIndex(p)
      if (index != -1) {
        nodes(index).insert(p)
        return
      }
    }
    objects.add(p)
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
  
  /**
   * If the point lies on the midline, it the retrieve method is called for
   * all the children, and those children that do not contain or touch the point
   * are discarded.
   */
  def retrieveForKNN(returnObjects: ArrayList[Point], p: Point): ArrayList[Point] = {
    val index = getIndex(p)
    // Making sure the point is inside the square 
    if (!isOutsideTheNode(p)) {
      if (index != -1 && nodes(0) != null) {
        nodes(index).retrieveForKNN(returnObjects, p)
      } 
      // For points that lie on the midline
      else if (index == -1 && nodes(0) != null) {
        for (i <- 0 until 4) {
          nodes(i).retrieveForKNN(returnObjects, p)
        }
      }
      returnObjects.addAll(objects)
    }
    returnObjects
  }
  
  /**
   * Prints the tree. 
   */
  def printTree: Quadtree = {
    if (objects == null && nodes(0) == null) { 
      println("This tree is empty.")
    }
    else {
      println("Printing the tree:")
      printTreeR
    }
    return this
  }
  
  def printTreeR {
    // printing any surface points
    if (objects != null) {
      println("--------Level:  " + level + "  ---------")
      println("Borders: x: " + bounds.getX + " y: " + bounds.getY + " width: " + bounds.getWidth + " height: " + bounds.getHeight)
      for (i <- 0 until objects.size) {
        println(objects.get(i).toString())
      }
    }
    // printing surface points of the children
    if (nodes(0) != null) {
      println("--------Level:  " + level + "  ---------")
      println("From top rght corner")
      nodes(0).printTree
      println("Level: " + level)
      println("From top left corner")
      nodes(1).printTree
      println("Level: " + level)
      println("From bottom left corner")
      nodes(2).printTree
      println("Level: " + level)
      println("From bottom right corner")
      nodes(3).printTree
    }
    else return
  }
  
  def eucledianDist(p1: Point, p2: Point): Int = {
    var ans = math.sqrt(math.pow(p1.getX - p2.getX, 2) + math.pow(p1.getY - p2.getY, 2)).toInt
    ans
  }
  
  /**
   * Finding the k nearest neighbours of point rp. 
   */
  def kNN(rp: Point, k: Int, result: ArrayList[Point]): ArrayList[Point] = {
    val index = getIndex(rp)
    if (index >= 0) {
      retrieveForKNN(result, rp)
      println("Possible points:")
      for (l <- 0 until result.size) {
        println(result.get(l).toString())
      }
      // Get the distances betweeen the points, sort them and get the first k points
      var list:Array[Point] = new Array[Point](result.size)
      for (i <- 0 until result.size) {
        result.get(i).setDist(eucledianDist(rp, result.get(i)))
        println("The distance was set to: " + result.get(i).getDist)
        list(i) = result.get(i)
      }
      Sorting.quickSort[Point](list)(DistanceOrdering)
      println("The List of ordered distances:")
      for (x <- list ) {
         println(x)
      }
      result.clear()
      for (i <- 0 until k) {
        result.add(list(i))
      }
    }
    result
  }
  
} //end class

