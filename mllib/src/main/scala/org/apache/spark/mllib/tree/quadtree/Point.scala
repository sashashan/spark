package org.apache.spark.mllib.tree.quadtree
import scala.util.Sorting

class Point(var pid: Int, var x: Double, var y: Double) extends Serializable {
  
  var dist: Double = -1
  
  def getID = pid
  def getX = x
  def getY = y
  def getDist = dist
  def setDist(d: Double) {dist = d}
  
  override def toString() = "id: " + getID + " x: " + getX + " y: " + getY + " dist: " + getDist
  
}

object DistanceOrdering extends Ordering[Point] {
  def compare(a:Point, b:Point) = a.getDist compare b.getDist
}
