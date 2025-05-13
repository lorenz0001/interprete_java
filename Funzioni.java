import java.io.*;
import javax.swing.*;

public class Funzioni{
    public static void trasformaElse(String[] righe){
        int z;
        int l;
        for(int i=0;i<righe.length;i++){
            z = i;
            String tokens[] = modifica(righe[i].trim(), ' ', '#').split(" ");
            if(tokens[0].equals("if")){
                l = getLivello(righe[i]);
                z+=1;
                while(z < righe.length && getLivello(righe[z]) != l)
                    z+=1;
                if(z != righe.length && righe[z].replace("\t","").equals("else")){
                    String op = tokens[2];
                    switch(op){
                        case "<":
                            op = ">=";
                            break;
                        case ">":
                            op = "<=";
                            break;
                        case ">=":
                            op = "<";
                            break;
                        case "<=":
                            op = ">";
                            break;
                        case "==":
                            op = "!=";
                            break;
                        case "!=":
                            op = "==";
                            break;
                    }
                    String nuovoIf = "\t".repeat(l)+"if "+tokens[1]+" "+op+" "+tokens[3];
                    righe[z] = nuovoIf;
                }
            }
        }
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
    public static void printError(Exception e){
        Compilatore.errArea.setText(e.getMessage());
    }
    public static void printOut(String s){
        Compilatore.outString+="\n"+s;
        Compilatore.outArea.setText(Compilatore.outString);
    }
    public static boolean checkEsiste(String nomeFile, String mail) throws IOException{
        FileReader f = new FileReader(nomeFile);
        BufferedReader r = new BufferedReader(f);
        String letta;
        String splittata[];
        while((letta = r.readLine()) != null){
            if(letta.contains(",")){
                splittata = letta.split(",",2);
                if(splittata[0].equals(mail))
                    return true;
            }
        }
        return false;
    }
    public static void registra(String nomeFile, String mail, String password, JFrame frame) throws IOException{
        if(checkEsiste(nomeFile, mail)){
            CreaFinestra.crea("Utenza già esistente");
            return;
        }
        if(!checkMail(mail)){
            CreaFinestra.crea("La mail inserita non rispetta i criteri");
            return;
        }
        
        if(!checkPass(password)){
            CreaFinestra.crea("La password inserita non rispetta i criteri");
            return;
        }
        FileWriter f = new FileWriter(nomeFile, true);
        BufferedWriter w = new BufferedWriter(f);
        w.newLine();
        w.write(mail+","+password);
        w.newLine();
        w.flush();
        CreaFinestra.crea("L'utenza è stata creata");
        frame.dispose();
    }
    public static boolean login(String nomeFile, String mail, String password, JFrame frame) throws IOException{
        if(!checkEsiste(nomeFile, mail)){
            CreaFinestra.crea("L'utenza non esiste");
            return false;
        }
        
        FileReader f;
        f = new FileReader(nomeFile);
        BufferedReader r = new BufferedReader(f);
        String letta;
        String riga[];
        while((letta = r.readLine()) != null){
            riga = letta.split(",",2);
            if(riga[0].equals(mail)){
                if(riga[1].equals(password)){
                    //CreaFinestra.crea("Accesso garantito");
                    frame.dispose();
                    Editor.main();
                }else{
                    CreaFinestra.crea("Password dell'utenza errata");
                    return false;
                }
            }
        }
        return false;
    }
    public static boolean checkMail(String mail) throws IOException{
        if(!mail.contains("@"))
            return false;
        String m[] = mail.split("\\.",2);
        
        FileReader f = new FileReader("suffissi.txt");
        BufferedReader r = new BufferedReader(f);
        
        String letta;
        while((letta = r.readLine()) != null){
            if(("."+m[1]).equals(letta)){
                return true;
            }
        }
        return false;
    }
    public static boolean checkPass(String password){
        String caratteri[] = {",", ".", "&", "!", "$"};
        
        if(password.length() < 10)
            return false;
        
        boolean trovato = false;
        
        for(int i=0;i<5;i++){
            if(password.contains(caratteri[i])){
                trovato = true;
                break;
            }
        }
        
        if(!trovato)
            return false;
        trovato = false;
        
        for(int i=0;i<password.length();i++){
            char c = password.charAt(i);
            if(c >= 'A' && c <= 'Z'){
                trovato = true;
                break;
            }
        }

        return true;
    }
    public static String recupera(String nomeFile, String mail) throws IOException{
        if(!checkEsiste(nomeFile, mail)){
            CreaFinestra.crea("L'utenza non esiste");
            return null;
        }
        
        FileReader f;
        f = new FileReader(nomeFile);
        BufferedReader r = new BufferedReader(f);
        String letta;
        String splittata[];
        
        while((letta = r.readLine()) != null){
            splittata = letta.split(",",2);
            if(splittata[0].equals(mail)){
                return splittata[1];
            }
        }
        return null;
    }
}