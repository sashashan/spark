class B(var zval: java.io.Serializable, var src: java.io.Serializable) extends Serializable{
  
  override def toString = s"zval = $zval src = $src"
  
}
