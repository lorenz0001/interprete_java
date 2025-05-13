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
    
    
    public static String getCodice(String s) throws Err{
        /*INIZIO
         * 
         * FINE*/
        int inizio = s.indexOf("INIZIO") + "INIZIO".length();
        int fine = s.indexOf("END");
        return s.substring(inizio, fine);
    }
    
    
    public static void compila(String s, JTextArea errAreas, JTextArea outAreas, String outStrings){
        variabili = new ListaVar();
        int livello_corrente = 0;
        Variabile v;
        int livello;
        
        try{
            errArea = errAreas;
            outArea = outAreas;
            outString = outStrings;
            String[] righe = s.replace("\n","").split(";");
            //controlloErrori(righe);
            Funzioni.trasformaElse(righe);
            //ElevaErr(0,0,String.join("\n", righe));
            for(int i=0;i<righe.length;i++){
                livello = Funzioni.getLivello(righe[i]);
                //System.out.println("livello: " + Funzioni.getLivello(righe[i]));
                //System.out.println("Livello corrente: " + livello_corrente);
                /*if(livello_corrente != livello){
                    while(Funzioni.getLivello(righe[i]) != livello_corrente && i < righe.length)
                        i+=1;
                }*/
                if(livello < livello_corrente){
                    livello_corrente = livello;
                }
                String tokens[] = Funzioni.modifica(righe[i].trim(), ' ', '#').split(" ");
                switch(tokens[0]){
                    case "let":
                        if(getVar(tokens[1]) != null)
                            ElevaErr(i+1, 0, "Variabile già dichiarata: " + tokens[1]);
                        dichiara(tokens, i, righe[i], 0);
                        String[] nuovoDizionario = new String[dizionario.length + 1];
                        System.arraycopy(dizionario, 0, nuovoDizionario, 0, dizionario.length);
                        nuovoDizionario[dizionario.length] = tokens[1];
                        dizionario = nuovoDizionario;
                        break;
                    case "print":
                        String param = tokens[1];//.substring(1,tokens[1].length()-1);
                        if(param.charAt(0) == '"'){
                            Funzioni.printOut(param.substring(1,param.length()-1));
                        } else {
                            String str = Funzioni.converti(getVar(param));
                            if(str == null)
                                ElevaErr(i+1, 0, "La variabile " + param + " non esiste");
                            Funzioni.printOut(Funzioni.modifica(str, '#', ' '));
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
                                if(Funzioni.getNumero(v1) > Funzioni.getNumero(v2))
                                    livello_corrente+=1;
                                break;
                            case "<":
                                if(Funzioni.getNumero(v1) < Funzioni.getNumero(v2))
                                    livello_corrente+=1;
                                break;
                            case ">=":
                                if(Funzioni.getNumero(v1) >= Funzioni.getNumero(v2))
                                    livello_corrente+=1;
                                break;
                            case "<=":
                                if(Funzioni.getNumero(v1) <= Funzioni.getNumero(v2))
                                    livello_corrente+=1;
                                break;
                            case "==":
                                if(Funzioni.getNumero(v1) == Funzioni.getNumero(v2))
                                    livello_corrente+=1;
                                break;
                            case "!=":
                                if(Funzioni.getNumero(v1) != Funzioni.getNumero(v2))
                                    livello_corrente+=1;
                                break;
                        }
                        
                        if(livello_corrente == livello){
                            i+=1;
                            while(i < righe.length && Funzioni.getLivello(righe[i]) != livello_corrente){
                                i+=1;
                                if(i < righe.length && Funzioni.getLivello(righe[i]) < livello_corrente){
                                    livello_corrente-=1;
                                    System.out.println("idnaiu2");
                                }
                            }
                            i-=1;
                        }
                       
                        break;
                    default:
                        if((v = getVar(tokens[0])) != null){
                            dichiara(tokens, i, righe[i], 1);
                        } else
                            ElevaErr(i+1,0,"token non riconosciuto");
                        break;
                }
            }
        } catch(Err e){
            Funzioni.printError(e);
        }
    }
    
    static Variabile creaVar(String s){
        Variabile v;
        if((v = getVar(s)) == null){
            int tipo = Funzioni.getTipo(s);
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
        Variabile v = variabili.testa;
        while(v != null){
            if(v.nome.equals(s)){
                return v;
            }
            v = v.next;
        }
        return null;
    }
    
    public static void modificaVar(Variabile s, Variabile d) {
    Variabile v = variabili.testa;

    if (v == s) {
        d.next = s.next; // collega il nuovo nodo
        variabili.testa = d;
        return;
    }

    while (v != null && v.next != null) {
        if (v.next == s) {
            d.next = s.next; // collega il nuovo nodo
            v.next = d;
            break;
        }
        v = v.next;
    }
}

    
    public static void dichiara(String[] tokens, int riga, String r, int q) throws Err{
        int tipo;
        if(tokens.length < 4-q){
            ElevaErr(riga+1, 0, "Dichiarazione non valida, lunghezza dichiarazione");
        } else if(!tokens[2-q].equals("=")){
            ElevaErr(riga+1, 0, "Dichiarazione non valida, manca l'uguale controlla meglio");
        }
        tokens[3-q] = r.substring(r.indexOf("=")+1, r.length()).trim();
        tipo = Funzioni.getTipo(tokens[3-q]);
        switch(tipo){
            case 1:
                if(q == 0){
                    variabili.push(new Variabile(tokens[1-q], tipo, Integer.parseInt(tokens[3])));
                    System.out.println("ciao");
                }else
                    modificaVar(getVar(tokens[1-q]), new Variabile(tokens[1-q], tipo,  Integer.parseInt(tokens[3-q])));
                break;
            case 2:
                if(q == 0)
                    variabili.push(new Variabile(tokens[1-q], tipo, Float.parseFloat(tokens[3])));
                else
                    modificaVar(getVar(tokens[1-q]), new Variabile(tokens[1-q], tipo,  Float.parseFloat(tokens[3-q])));
                break;
            case 3:
                if(tokens[3-q].charAt(0) == '"' && tokens[3-q].charAt(tokens[3-q].length()-1) == '"'){
                    if(q == 0)
                        variabili.push(new Variabile(tokens[1-q], tipo, tokens[3-q]));
                    else
                        modificaVar(getVar(tokens[1-q]), new Variabile(tokens[1-q], tipo, tokens[3-q]));
                } else {
                    double risultato = Funzioni_calc.main(tokens[3-q], riga);
                    if(q == 0){
                        variabili.push(new Variabile(tokens[1-q], 2, risultato));
                    } else{
                        modificaVar(getVar(tokens[1-q]), new Variabile(tokens[1-q], 2, risultato));
                        
                    }
                }            
                break;
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