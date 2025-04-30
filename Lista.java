public class Lista{
    Nodo testa = null;
    Nodo coda = null;
    public void push(Nodo n){
        if(testa == null){
            testa = n;
            coda = testa;
        } else {
            coda.next = n;
            coda = n;
        }
    }
    public Nodo pop(){
        if(testa == null)
            return null;
        Nodo temp = testa;
        testa = testa.next;
        return temp;
    }
}