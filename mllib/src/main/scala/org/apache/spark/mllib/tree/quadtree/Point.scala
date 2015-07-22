package org.apache.spark.mllib.tree.quadtree

class Point(var x: Int, var y: Int) extends Serializable {
  
  def getX = x
  def getY = y
  
}
