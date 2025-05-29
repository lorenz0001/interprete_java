import java.io.*;
import javax.swing.JTextArea;
import javax.swing.*;

public class Compilatore{
    static String alfabeto = " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.+-*/=!&;:()[]_<>%"+'\t'+'"';
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
            //ElevaErr(0,0,String.join(" ", righe));
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
                String tokens[] = Funzioni.modificaString(righe[i].trim(), ' ', '#').split(" ");
                if(righe[i].startsWith("//"))
                    continue;
                switch(tokens[0]){
                    case "input":
                        if(tokens.length != 3)
                            ElevaErr(i+1, 0, "Sintassi input sbagliata");
                        if(getVar(tokens[1]) == null)
                            ElevaErr(i+1, 0, "Variabile non dichiarata");
                        String in = JOptionPane.showInputDialog(null, Funzioni.modificaString(tokens[2], '#', ' '));
                        if (in == null) {
                            modificaVar(getVar(tokens[1]), new Variabile(tokens[1],3,"null"));
                        } else {
                            int tipo = Funzioni.getTipo(in);
                            switch(tipo){
                                case 1:
                                    modificaVar(getVar(tokens[1]), new Variabile(tokens[1],1,Integer.parseInt(in)));
                                    break;
                                case 2:
                                    modificaVar(getVar(tokens[1]), new Variabile(tokens[1],2,Double.parseDouble(in)));
                                    break;
                                case 3:
                                    modificaVar(getVar(tokens[1]), new Variabile(tokens[1],3,in));
                                    break;
                            }
                            
                        }
                        break;
                    case "let":
                        if(getVar(tokens[1]) != null)
                            ElevaErr(i+1, 0, "Variabile già dichiarata: " + tokens[1]);
                        dichiara(tokens, i, righe[i], 0);
                        break;
                    case "print":
                        String param = tokens[1];
                        if (param.charAt(0) == '"') {
                            Funzioni.printOut(Funzioni.modificaString2(param.substring(1, param.length()-1), '#', ' '));
                        } else if (param.charAt(0) == '(') {
                            param = righe[i].substring(righe[i].indexOf("print (")+7, righe[i].length()-1);
                            StringBuilder p = new StringBuilder();
                            boolean trovata = false;
                            for(int h=0;h<param.length();h++){
                                if(param.charAt(h) == '"')
                                    trovata = !trovata;
                                
                                if(trovata){
                                    p.append(param.charAt(h));
                                } else {
                                    if(param.charAt(h) != ' ')
                                        p.append(param.charAt(h));
                                }
                            }
                            param = p.toString();
                            System.out.println(param);
                            String[] tokens_print = param.split("\\+");
                            String stringa = "";
                            for (int z = 0; z < tokens_print.length; z++) {
                                //System.out.println("token: " + tokens_print[z]);
                                if (tokens_print[z].charAt(0) == '"') {
                                    stringa += Funzioni.modificaString2(tokens_print[z].substring(1, tokens_print[z].length()-1),'#',' ');
                                } else {
                                    String str = Funzioni.converti(getVar(tokens_print[z]));
                                    if (str == null)
                                        ElevaErr(i + 1, 0, "La variabile " + tokens_print[z] + " non esiste");
                                    stringa += Funzioni.modificaString2(str, '#', ' ');
                                }
                            }
                            Funzioni.printOut(stringa);
                        }else{
                            v = getVar(param);
                            if(v == null)
                                ElevaErr(i+1, 0, "La variabile " + param + " non esiste");
                            String str = Funzioni.converti(v);
                            Funzioni.printOut(Funzioni.modificaString(str, '#', ' '));
                        }
                        break;
                    case "else":
                        ElevaErr(i+1, 0, "Else non previsto");
                        break;
                    case "goto":
                        if(tokens.length != 2)
                            ElevaErr(i+1, 0, "Sintassi goto sbagliata");
                        if(Integer.parseInt(tokens[1]) < 1 || Integer.parseInt(tokens[1]) > righe.length)
                            ElevaErr(i+1, 0, "Valore goto non valido");
                        i = Integer.parseInt(tokens[1])-1-1;
                        //System.out.println("GOTO A " + i+1);
                        livello_corrente = Funzioni.getLivello(righe[i]);
                        break;
                    case "if": // if a > b
                        Variabile v1, v2;
                        v1 = creaVar(tokens[1]);
                        v2 = creaVar(tokens[3]);
                        System.out.println("V1: " + v1.val2 + " " + v1.tipo + " V2: " + v2.val2 + " " + v2.tipo);
                        
                        String ope = tokens[2];
                        
                        if(v1.tipo != v2.tipo){
                            ElevaErr(i+1, 0, "I tipi dei dati da confrontare devono essere gli stessi");
                        }
                        
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
                                }
                            }
                            i-=1;
                        }
                       
                        break;
                    default:
                        System.out.println("NON RICONOSC " + tokens[0]);
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
                case 2:
                    return new Variabile("",2,Double.parseDouble(s),1);
                case 3:
                    return new Variabile("",tipo,s);
            }
        }else{
            switch(v.tipo){
                case 1:
                    return new Variabile("",2,Double.parseDouble(""+v.val1),1);
                case 2:
                    return new Variabile("",2,v.val2,1);
                case 3:
                    return v;
            }
        }
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
        if(tokens.length == 2){
            variabili.push(new Variabile(tokens[1],1,0));
            return;
        }
        if(tokens.length < 4 - q || !tokens[2 - q].equals("=")) {
            ElevaErr(riga + 1, 0, "Dichiarazione non valida");
            return;
        }
        
        String nomeVar = tokens[1 - q];
        String valoreStr = r.substring(r.indexOf("=") + 1).trim();
        int tipo = Funzioni.getTipo(valoreStr);
        
        Variabile nuovaVar;
        
        switch(tipo) {
            case 1: // Intero
                nuovaVar = new Variabile(nomeVar, tipo, Integer.parseInt(valoreStr));
                break;
            case 2: // Float
                nuovaVar = new Variabile(nomeVar, tipo, Float.parseFloat(valoreStr));
                break;
            case 3: // Stringa o espressione
                if (valoreStr.startsWith("\"") && valoreStr.endsWith("\"")) {
                    nuovaVar = new Variabile(nomeVar, tipo, valoreStr.substring(1,valoreStr.length()-1));
                } else {
                    double risultato = Funzioni_calc.main(valoreStr, riga);
                    tipo = (risultato == (int) risultato) ? 1 : 2;
                    nuovaVar = new Variabile(nomeVar, tipo, (tipo == 1) ? (int) risultato : risultato);
                }
                break;
            default:
                ElevaErr(riga + 1, 0, "Tipo di valore non supportato");
                return;
        }
        
        if (q == 0)
            variabili.push(nuovaVar);
        else{
            modificaVar(getVar(nomeVar), nuovaVar);
        }
    }
}