package org.apache.spark.mllib.tree.quadtree
import scala.util.Sorting

import scala.collection.JavaConversions._
import java.util.ArrayList
import java.awt.Rectangle

class Quadtree(private var level: Int, private var bounds: Rectangle) {
  // bounds represents the 2D space that the node occupies
  private var MAX_LEVELS: Int = 10000
  private var MAX_OBJECTS: Int = 100
  private var objects = new ArrayList[Point]()
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

  def insert(p: Point) {
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
  
  def retrieveForKNN(returnObjects: ArrayList[Point], p: Point): ArrayList[Point] = {
    val index = getIndex(p)
    if (index != -1 && nodes(0) != null) {
      nodes(index).retrieveForKNN(returnObjects, p)
    } 
    returnObjects.addAll(objects)
    returnObjects
  }
  
  def printTree {
    // printing any surface points
    if (objects != null) {
      println("Level: " + level)
      for (i <- 0 until objects.size) {
        println(objects.get(i).toString())
      }
    }
    // printing surface points of the children
    if (nodes(0) != null) {
      println("Level: " + level)
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
  
  def kNN(rp: Point, k: Int, result: ArrayList[Point]): ArrayList[Point] = {
    val index = getIndex(rp)
    // case 1: where the r point lies in the square, and not on the border
    if (index >= 0) {
      retrieveForKNN(result, rp)
      println("Case 1, possible points:")
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
      for ( x <- list ) {
         println(x)
      }
    }
    // case 2: where the r point lies on the border 
    else {
      
    }
    
    result
  }
  
} //end class

