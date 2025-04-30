import java.io.*;
import javax.swing.JTextArea;

public class Compilatore{
    static String vocabolario = " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+-*/=!&;:()[]_<>\"";
    static String[] dizionario = {
          "if", "let", "while", "for", "if",
          "print", "input"
    };
    static String[] nomi_variabili = {
        
    };
    static Lista errori = new Lista();
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
    public static boolean compila(String s, JTextArea output, String testo){
        boolean res = controlloErrori(s,output,testo);
        if(res){
            String[] righe = s.split("\n");
            for(int i=0;i<righe.length;i++){
                String[] tokens = righe[i].split(" ");
                if(tokens[0].equals("let")){
                    if()
                }
            }
        }
        return res;
    }
    public static boolean controlloErrori(String s, JTextArea output, String testo){
        String[] splittata = s.split("\n");
        System.out.println("len: " + splittata.length);
        Nodo n;
        for(int i=0;i<splittata.length;i++){
            boolean inizioStringa = false;
            int inizio = 0;
            for(int j=0;j<splittata[i].length();j++){
                char carattere = splittata[i].charAt(j);
                String tokens[] = splittata[i].split(" ");
                if(carattere == '"'){
                    inizioStringa = !inizioStringa;
                    inizio=j;
                }
                if(!inizioStringa){
                    if(vocabolario.indexOf(carattere) == -1){
                        errori.push(new Nodo(i+1, j+1, "Carattere non riconosciuto: '" + carattere + "'"));
                    }
                    if(j == 0 && !controlloSintattico(tokens[0])){
                        errori.push(new Nodo(i+1, j+1, "Token non riconosciuto: '" + tokens[0] + "'"));
                    }
                }
                if(j == splittata[i].length()-1 && inizioStringa)
                    errori.push(new Nodo(i+1, inizio+1, "Stringa non terminata"));
            }
        }
        boolean a = false;
        while((n = errori.pop()) != null){
            testo += "Errore: " + n.err + ", riga: " + n.riga + " colonna: " + n.colonna + "\n";
            output.setText(testo);
            a = true;
        }
        
        if(!a)
            output.setText("Compilazione completata senza errori");
        
        return true;
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