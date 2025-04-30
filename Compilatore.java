import java.io.*;
import javax.swing.JTextArea;

public class Compilatore{
    static String alfabeto = " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+-*/=!&;:()[]_<>\"";
    static String[] dizionario = {
          "if", "let", "while", "for", "if", "stampa"
    };
    static ListaVar variabili;
    static JTextArea errArea;
    static String errString;
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
    public static void ElevaErr(int riga, int colonna, String err) throws Err{
        throw new Err("Errore: " + err + ", riga: " + riga + ", colonna: " + colonna);
    }
    public static void controlloErrori(String righe[]) throws Err{
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
                    if(alfabeto.indexOf(carattere) == -1){
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
    public static String estraiCodice(String s) throws Err{
        /*INIZIO
         * 
         * FINE*/
        int inizio = s.indexOf("INIZIO") + "INIZIO".length();
        int fine = s.indexOf("END");
        return s.substring(inizio, fine);
    }
    public static void compila(String s, JTextArea output, String testo){
        variabili = new ListaVar();
        try{
            errArea = output;
            errString = testo;
            //s = estraiCodice(s);
            String[] righe = s.split("\n");
            controlloErrori(righe);
            for(int i=0;i<righe.length;i++){
                String tokens[] = modifica(righe[i], ' ', '#').split(" ");
                switch(tokens[0]){
                    case "let":
                        if(cercaVar(tokens[1]) != null)
                            ElevaErr(i+1, 0, "Variabile già dichiarata: " + tokens[1]);
                        dichiara(tokens, i);
                        break;
                    case "stampa":
                        String param = tokens[1].substring(1,tokens[1].length()-1);
                        if(param.charAt(0) == '"'){
                            System.out.println(param.substring(1,param.length()-1));
                        } else {
                            String v = cercaVar(param);
                            if(v == null)
                                ElevaErr(i+1, 0, "La variabile " + param + " non esiste");
                            System.out.println(modifica(v, '#', ' '));
                        }
                        break;
                }
            }
        } catch(Err e){
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
    public static void dichiara(String[] tokens, int riga) throws Err{
        int tipo;
        if(tokens.length < 4){
            ElevaErr(riga+1, 0, "Dichiarazione non valida, lunghezza dichiarazione");
        } else if(!tokens[2].equals("=")){
            ElevaErr(riga+1, 0, "Dichiarazione non valida, manca l'uguale controlla meglio");
        }
        if(tokens.length > 4){
            
        } else {
            tipo = controllaTipo(tokens[3]);
            if(tipo == 3){
                tokens[3] = tokens[3].substring(1,tokens[3].length()-1);
            }
            variabili.push(new Variabile(tokens[1], tipo, tokens[3]));
        }
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