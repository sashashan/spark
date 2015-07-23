package org.apache.spark.mllib.tree.quadtree
import scala.util.Sorting

class Point(var x: Int, var y: Int) extends Serializable {
  
  var dist: Double = -1
  
  def getX = x
  def getY = y
  def getDist = dist
  
  override def toString() = "x: " + getX + " y: " + getY
  
}

object DistanceOrdering extends Ordering[Point] {
  def compare(a:Point, b:Point) = a.getDist compare b.getDist
}
