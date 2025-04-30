public class Nodo{
    public int riga; // tipo
    public int colonna; // valore
    public String err; // valore
    public Nodo next;
    Nodo(int r, int c, String e){
        riga = r;
        colonna = c;
        err = e;
    }
}