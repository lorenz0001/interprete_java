public class Op{
    public double n;
    public String s = null;
    public Op next;
    public Op prec;
    Op(String s){
        this.s = s;
        next = null;
        prec = null;
    }
    Op(double n){
        this.n = n;
        next = null;
        prec = null;
    }
}