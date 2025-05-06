import java.io.*;
import javax.swing.JTextArea;

public class Compilatore{
    static String alfabeto = " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.+-*/=!&;:()[]_<>"+'\t'+'"';
    static String[] dizionario = {
          "if", "let", "while", "for", "print", "else"
    };
    static ListaVar variabili;
    static JTextArea errArea;
    static JTextArea outArea;
    static String outString;
    static Variabile v;
    static Stack condizioni;
    public static boolean controlloSintattico(String s, String[] diz){
        for(int j=0;j<diz.length;j++){
            if(s.equals(diz[j])){
                return true;
            }
        }
        return false;
    }
    public static void ElevaErr(int riga, int colonna, String err) throws Err{
        throw new Err("Errore: " + err + ", riga: " + riga + ", colonna: " + colonna);
    }
    public static void controlloErrori(String righe[]) throws Err{
        for(int i=0;i<righe.length;i++){
            boolean inizioStringa = false;
            int inizio = 0;
            for(int j=0;j<righe[i].length();j++){
                char carattere = righe[i].charAt(j);
                String tokens[] = righe[i].trim().split(" ");
                if(carattere == '"'){
                    inizioStringa = !inizioStringa;
                    inizio=j;
                }
                if(!inizioStringa){
                    if(alfabeto.indexOf(carattere) == -1){
                        ElevaErr(i+1, j+1, "Carattere non riconosciuto: '" + carattere + "'");
                    }
                    if(j == 0 && !controlloSintattico(tokens[0], dizionario)){
                        ElevaErr(i+1, j+1, "Token non riconosciuto: '" + tokens[0] + "'");
                    }
                }
                if(j == righe[i].length()-1 && inizioStringa)
                    ElevaErr(i+1, inizio+1, "Stringa non terminata");
            }
        }
    }
    public static void printError(Exception e){
        errArea.setText(e.getMessage());
    }
    public static void printOut(String s){
        outString+="\n"+s;
        outArea.setText(outString);
    }
    public static String modifica(String s, char trova, char sostituisci){
        StringBuilder str = new StringBuilder();
        boolean trovata = false;
        String temp = "";
        for(int i=0;i<s.length();i++){
            if(s.charAt(i) == '"'){
                trovata = !trovata;
                str.append('"');
            } else if(s.charAt(i) == trova && trovata){
                str.append(sostituisci);
            } else {
                str.append(s.charAt(i));
            }
        }
        return str.toString();
    }
    public static String getCodice(String s) throws Err{
        /*INIZIO
         * 
         * FINE*/
        int inizio = s.indexOf("INIZIO") + "INIZIO".length();
        int fine = s.indexOf("END");
        return s.substring(inizio, fine);
    }
    public static int getLivello(String s){
        /*int z = 0;
        int j = 0;
        for(int i=0;i<s.length();i++){
            if(s.charAt(i) == ' '){
                j+=1;
            }else
                return z;
            if(j == 8){
                j = 0;
                z += 1;
            }
        }*/
        int z = 0;
        for(int i=0;i<s.length();i++){
            if(s.charAt(i) == '\t')
                z+=1;
            else
                return z;
        }
        return z;
    }
    public static void compila(String s, JTextArea errAreas, JTextArea outAreas, String outStrings){
        variabili = new ListaVar();
        int livello_corrente = 0;
        int livello;
        condizioni = new Stack();
        Op preso;
        try{
            errArea = errAreas;
            outArea = outAreas;
            outString = outStrings;
            String[] righe = s.replace("\n","").split(";");
            controlloErrori(righe);
            for(int i=0;i<righe.length;i++){
                livello = getLivello(righe[i]);
                System.out.println("livello: " + getLivello(righe[i]));
                System.out.println("Livello corrente: " + livello_corrente);
                /*if(livello_corrente != livello){
                    while(getLivello(righe[i]) != livello_corrente && i < righe.length)
                        i+=1;
                }*/
                String tokens[] = modifica(righe[i].trim(), ' ', '#').split(" ");
                switch(tokens[0]){
                    case "let":
                        if(getVar(tokens[1]) != null)
                            ElevaErr(i+1, 0, "Variabile già dichiarata: " + tokens[1]);
                        dichiara(tokens, i, righe[i]);
                        break;
                    case "print":
                        String param = tokens[1];//.substring(1,tokens[1].length()-1);
                        if(param.charAt(0) == '"'){
                            printOut(param.substring(1,param.length()-1));
                        } else {
                            String v = converti(getVar(param));
                            if(v == null)
                                ElevaErr(i+1, 0, "La variabile " + param + " non esiste");
                            printOut(modifica(v, '#', ' '));
                        }
                        break;
                    case "else":
                        ElevaErr(i+1, 0, "Else non previsto");
                        break;
                    case "if": // if a > b
                        Variabile v1, v2;
                        v1 = creaVar(tokens[1]);
                        v2 = creaVar(tokens[3]);
                        System.out.println("v2 " + tokens[3]);
                        String ope = tokens[2];
                        System.out.println("tipo1: "+ v1.tipo + ", tipo2: " + v2.tipo);
                        if(v1.tipo != v2.tipo)
                            ElevaErr(i+1, 0, "I tipi dei dati da confrontare devono essere gli stessi");
                        
                        switch(ope){
                            case ">":
                            case "<":
                            case ">=":
                            case "<=":
                                if(v1.tipo == 3)
                                    ElevaErr(i+1, 0, "Una stringa non può accettare questo operatore nel confronto");
                                break;
                        }
                        switch(ope){
                            case ">":
                                if(getNumero(v1) > getNumero(v2))
                                    livello_corrente+=1;
                                break;
                            case "<":
                                if(getNumero(v1) < getNumero(v2))
                                    livello_corrente+=1;
                                break;
                            case ">=":
                                if(getNumero(v1) >= getNumero(v2))
                                    livello_corrente+=1;
                                break;
                            case "<=":
                                if(getNumero(v1) <= getNumero(v2))
                                    livello_corrente+=1;
                                break;
                            case "==":
                                if(getNumero(v1) == getNumero(v2))
                                    livello_corrente+=1;
                                break;
                            case "!=":
                                if(getNumero(v1) != getNumero(v2))
                                    livello_corrente+=1;
                                break;
                        }
                        
                        if(livello_corrente == livello){
                            i+=1;
                            while(getLivello(righe[i]) != livello_corrente && i < righe.length)
                                i+=1;
                        }
                        
                        tokens = modifica(righe[i], ' ', '#').split(" ");
                        System.out.println("token: " + tokens[0]);
                        if(tokens[0].equals("else")){
                            livello_corrente+=1;
                        }
                        break;
                }
            }
        } catch(Err e){
            printError(e);
        }
    }
    public static double getNumero(Variabile v){
        if(v.tipo == 1)
            return Double.parseDouble(Integer.toString(v.val1));
        else
            return v.val2;
    }
    public static String converti(Variabile v){
        if(v == null)
            return null;
        switch(v.tipo){
            case 1:
                return ""+v.val1;
            case 2:
                return ""+v.val2;
            case 3:
                return v.val3;
        }
        return null;
    }
    static Variabile creaVar(String s){
        if((v = getVar(s)) == null){
            int tipo = getTipo(s);
            switch(tipo){
                case 1:
                    return new Variabile("",tipo,Integer.parseInt(s));
                case 2:
                    return new Variabile("",tipo,Float.parseFloat(s));
                case 3:
                    return new Variabile("",tipo,s);
            }
        }else
            return v;
        return null;
    }
    public static Variabile getVar(String s){
        v = variabili.testa;
        while(v != null){
            if(v.nome.equals(s)){
                return v;
            }
            v = v.next;
        }
        return null;
    }
    
    public static void dichiara(String[] tokens, int riga, String r) throws Err{
        int tipo;
        if(tokens.length < 4){
            ElevaErr(riga+1, 0, "Dichiarazione non valida, lunghezza dichiarazione");
        } else if(!tokens[2].equals("=")){
            ElevaErr(riga+1, 0, "Dichiarazione non valida, manca l'uguale controlla meglio");
        }
        tokens[3] = r.substring(r.indexOf("=")+1, r.length()).trim();
        tipo = getTipo(tokens[3]);
        switch(tipo){
            case 1:
                variabili.push(new Variabile(tokens[1], tipo, Integer.parseInt(tokens[3])));
                break;
            case 2:
                variabili.push(new Variabile(tokens[1], tipo, Float.parseFloat(tokens[3])));
                break;
            case 3:
                if(tokens[3].charAt(0) == '"' && tokens[3].charAt(tokens[3].length()-1) == '"'){
                    variabili.push(new Variabile(tokens[1], tipo, tokens[3]));
                } else {
                    double risultato = Funzioni_calc.main(tokens[3], riga);
                    variabili.push(new Variabile(tokens[1], 2, risultato));
                }            
                break;
        }
    }
    public static int getTipo(String s){
        try{
            Integer.parseInt(s);
            return 1;
        }catch(Exception e){
            try{
                Double.parseDouble(s);
                return 2;
            } catch(Exception e2){
                return 3;
            }
        }
    }
    /*public static void compila() throws IOException{
        FileReader f = new FileReader("./codice.wow7");
        BufferedReader r = new BufferedReader(f);
        String codice = "";
        String riga;
        while((riga = r.readLine()) != null){
            codice += riga + "\n";
        }
        controlloErrori(codice);
    }*/
}