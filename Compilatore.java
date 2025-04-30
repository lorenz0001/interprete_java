import java.io.*;
import javax.swing.JTextArea;

public class Compilatore{
    static String vocabolario = " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+-*/=!&;:()[]_<>\"";
    static String[] dizionario = {
          "if", "let", "while", "for", "if",
          "print", "stampa"
    };
    static ListaVar variabili = new ListaVar();
    static Lista errori = new Lista();
    static JTextArea errArea;
    static String errString;
    static boolean continua = true;
    public static boolean controlloSintattico(String s){
        boolean trovata = false;
        for(int j=0;j<dizionario.length;j++){
            if(s.equals(dizionario[j])){
                trovata = true;
                break;
            }
        }
        return trovata;
    }
    public static void ElevaErr(int riga, int colonna, String err) throws Exception{
        throw new Exception("Errore: " + err + ", riga: " + riga + ", colonna: " + colonna);
    }
    public static void controlloErrori(String righe[]) throws Exception{
        for(int i=0;i<righe.length;i++){
            boolean inizioStringa = false;
            int inizio = 0;
            for(int j=0;j<righe[i].length();j++){
                char carattere = righe[i].charAt(j);
                String tokens[] = righe[i].split(" ");
                if(carattere == '"'){
                    inizioStringa = !inizioStringa;
                    inizio=j;
                }
                if(!inizioStringa){
                    if(vocabolario.indexOf(carattere) == -1){
                        ElevaErr(i+1, j+1, "Carattere non riconosciuto: '" + carattere + "'");
                    }
                    if(j == 0 && !controlloSintattico(tokens[0])){
                        ElevaErr(i+1, j+1, "Token non riconosciuto: '" + tokens[0] + "'");
                    }
                }
                if(j == righe[i].length()-1 && inizioStringa)
                    ElevaErr(i+1, inizio+1, "Stringa non terminata");
            }
        }
    }
    public static void stampaError(Exception e){
        errArea.setText(e.getMessage());
    }
    public static String modifica(String s, char trova, char sostituisci){
        //per ogni stringa dentro sostituisco lo spazio con un #
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
    public static void stampa(String s){
        System.out.println(s);
    }
    public static void compila(String s, JTextArea output, String testo){
        try{
            errArea = output;
            errString = testo;
            String[] righe = s.split("\n");
            controlloErrori(righe);
            for(int i=0;i<righe.length;i++){
                /*String temp = modifica(righe[i]);
                stampa(temp);
                String tokens[] = temp.split(" ");*/
                String tokens[] = modifica(righe[i], ' ', '#').split(" ");
                if(tokens[0].equals("let")){
                    dichiara(tokens, i);
                } else if(tokens[0].equals("stampa")){ // stampa (stringa || "ciao")
                    String param = tokens[1].substring(1,tokens[1].length()-1);
                    if(param.charAt(0) == '"'){
                        param = modifica(param.substring(1,tokens[1].length()-1), '#', ' ');
                    } else {
                        System.out.println(modifica(cercaVar(param), '#', ' '));
                    }
                }
            }
        } catch(Exception e){
            stampaError(e);
        }
    }
    public static String cercaVar(String s){
        Variabile v = variabili.testa;
        while(v != null){
            if(v.nome.equals(s)){
                return v.val3;
            }
            v = v.next;
        }
        return null;
    }
    public static void dichiara(String[] tokens, int riga) throws Exception{
    // let a = 1
        if(tokens.length < 4){
            ElevaErr(riga+1, 0, "Dichiarazione non valida, lunghezza dichiarazione");
        } else if(!tokens[2].equals("=")){
            ElevaErr(riga+1, 0, "Dichiarazione non valida, manca l'uguale controlla meglio");
        }
        int tipo = controllaTipo(tokens[3]);
        variabili.push(new Variabile(tokens[1], tipo, tokens[3]));
    }
    public static int controllaTipo(String s){
        try{
            Integer.parseInt(s);
            return 1;
        }catch(Exception e){
            try{
                Float.parseFloat(s);
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