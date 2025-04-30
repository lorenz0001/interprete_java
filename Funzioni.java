import java.io.*;
import javax.swing.*;

public class Funzioni{
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