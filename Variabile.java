public class Variabile{
    String nome;
    int tipo;
    int val1;
    double val2;
    String val3;
    public Variabile next;
    Variabile(String n, int t, int val){
        nome = n;
        tipo = t;
        val1 = val;
    }
    Variabile(String n, int t, double val){
        nome = n;
        tipo = t;
        if(val == (int)val)
            val1 = (int)val;
        else
            val2 = val;
    }
    Variabile(String n, int t, String val){
        nome = n;
        tipo = t;
        val3 = val;
    }
    public String getStringa(){
        switch(tipo){
            case 1:
                return ""+val1;
            case 2:
                return ""+val2;
            case 3:
                return val3;
        }
        return null;
    }
}