package org.apache.spark.mllib.tree.quadtree
import scala.util.Sorting

class Point(var x: Double, var y: Double) extends Serializable {
  
  var dist: Double = -1
  
  def getX = x
  def getY = y
  def getDist = dist
  def setDist(d: Double) {dist = d}
  
  override def toString() = "x: " + getX + " y: " + getY + " dist: " + getDist
  
}

object DistanceOrdering extends Ordering[Point] {
  def compare(a:Point, b:Point) = a.getDist compare b.getDist
}
