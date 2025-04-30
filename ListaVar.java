public class ListaVar{
    Variabile testa = null;
    Variabile coda = null;
    public void push(Variabile n){
        if(testa == null){
            testa = n;
            coda = testa;
        } else {
            coda.next = n;
            coda = n;
        }
    }
    public Variabile pop(){
        if(testa == null)
            return null;
        Variabile temp = testa;
        testa = testa.next;
        return temp;
    }
}