package scalangband.model.fov

case class Slope(y:Int, x:Int) {
    def greater(y:Int, x:Int):Boolean =this.y *x >this.x *y
    def greaterOrEqual(y:Int, x:Int):Boolean =this.y *x >=this.x *y
    def less(y:Int, x:Int):Boolean =this.y *x< this.x *y
}
