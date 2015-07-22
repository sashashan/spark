package org.apache.spark.mllib.tree.quadtree

import scala.collection.JavaConversions._
import java.util.ArrayList
import java.awt.Rectangle

class Quadtree(private var level: Int, private var bounds: Rectangle) {
  // bounds represents the 2D space that the node occupies
  private var MAX_LEVELS: Int = 5
  private var MAX_OBJECTS: Int = 3
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
  
  def retrieve(returnObjects: ArrayList[Point], p: Point): ArrayList[Point] = {
    val index = getIndex(p)
    if (index != -1 && nodes(0) != null) {
      nodes(index).retrieve(returnObjects, p)
    }
    returnObjects.addAll(objects)
    returnObjects
  }
  
  def printTree(tree: Quadtree) {
    if (tree == null) return
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
      printTree(nodes(0))
      println("Level: " + level)
      println("From top left corner")
      printTree(nodes(1))
      println("Level: " + level)
      println("From bottom left corner")
      printTree(nodes(2))
      println("Level: " + level)
      println("From bottom right corner")
      printTree(nodes(3))
    }
  }
  
} //end class
