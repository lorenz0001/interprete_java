public class Funzioni_calc{
    static String[] oper = {
        "+","-","*","/"
    };
    public static boolean isNumero(String t){
        try{
            Double.parseDouble(t);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static double converti(String s){
        return Double.parseDouble(s);
    }

public static void gestisciOperatori(Op op2, Op op1, Op ope) {
        switch (ope.s) {
            case "/":
                op1.n = op1.n / op2.n;
                break;
            case "*":
                op1.n = op1.n * op2.n;
                break;
            case "+":
                op1.n = op1.n + op2.n;
                break;
            case "-":
                op1.n = op1.n - op2.n;
                break;
        }
    }

        public static int compara(Op op1, Op op2) {
        if (op1.s.equals("(") && op2.s.equals(")")) {
            return -3;
        }
        if (op1.s.equals("(") || op2.s.equals("(")) {
            return -1;
        }
        if (op1.s.equals("√") || op1.s.equals("^2")) {
            return 1;
        }
        if (op2.s.equals("√") || op2.s.equals("^2")) {
            return -1;
        }
        if (op2.s.equals(")")) {
            return -2;
        }
        if ((op1.s.equals("+") || op1.s.equals("-")) && (op2.s.equals("*") || op2.s.equals("/"))) {
            return -1;
        }
        if ((op1.s.equals("*") || op1.s.equals("/")) && (op2.s.equals("+") || op2.s.equals("-"))) {
            return 1;
        }
        return 0;
    }

    public static double main(String espressione, int riga) throws Err{
        Lista lista = new Lista();
        Stack operatori = new Stack();
        Stack operandi = new Stack();
        for(int i=0;i<espressione.length();i++){
            if(espressione.charAt(i) != ' ')
                lista.add(""+espressione.charAt(i), riga);
        }
        return calcola(lista, operatori, operandi, riga);
    }
    
    public static double calcola(Lista l, Stack operatori, Stack operandi, int riga) throws Err{
        Op op1, op2, ope;
        boolean ultimoOperatore = true;

        while (true) {
            Op corrente = l.rimuovi();
            
            if (corrente == null) {
                break;
            }
            if(!Compilatore.controlloSintattico(corrente.s, oper) && !isNumero(corrente.s)){
                Variabile v = Compilatore.cercaVar(corrente.s);
                if(v == null){
                    Compilatore.ElevaErr(riga,0,"Variabile: " + corrente.s + " non dichiarata");
                }
                if(v.tipo == 3)
                    Compilatore.ElevaErr(riga,0,"Variabile: " + corrente.s + " non compatibile con i calcoli");
                corrente.s = Compilatore.converti(v);
            }

            if (isNumero(corrente.s)) {
                operandi.push(new Op(converti(corrente.s)));
                ultimoOperatore = false;
                
                while (operatori.testa != null && (operatori.testa.s.equals("√") || operatori.testa.s.equals("^2"))) {
                    ope = operatori.pop();
                    op2 = operandi.pop();
                    
                    if (ope.s.equals("√")) {
                        op2.n = Math.sqrt(op2.n);
                    } else {
                        op2.n = Math.pow(op2.n, 2);
                    }
                    operandi.push(op2);
                }
            } else {
                if (corrente.s.equals("-") && ultimoOperatore) {
                    operandi.push(new Op(0));
                }
                
                if (operatori.testa == null) {
                    operatori.push(new Op(corrente.s));
                } else {
                    int confronto = compara(operatori.testa, corrente);
                    
                    if (confronto == -3) {
                        operatori.pop();
                        
                        while (operatori.testa != null && (operatori.testa.s.equals("√") || operatori.testa.s.equals("^2"))) {
                            ope = operatori.pop();
                            op2 = operandi.pop();
                            
                            if (ope.s.equals("√")) {
                                op2.n = Math.sqrt(op2.n);
                            } else {
                                op2.n = Math.pow(op2.n, 2);
                            }
                            operandi.push(op2);
                        }
                    } else if (confronto == 0 || confronto == 1) {
                        while (operatori.testa != null && (confronto == 0 || confronto == 1)) {
                            ope = operatori.pop();
                            
                            if (ope.s.equals("√") || ope.s.equals("^2")) {
                                op2 = operandi.pop();
                                
                                if (ope.s.equals("√")) {
                                    op2.n = Math.sqrt(op2.n);
                                } else {
                                    op2.n = Math.pow(op2.n, 2);
                                }
                                operandi.push(op2);
                            } else {
                                op2 = operandi.pop();
                                op1 = operandi.pop();
                                gestisciOperatori(op2, op1, ope);
                                operandi.push(op1);
                            }
                            
                            if (operatori.testa != null) {
                                confronto = compara(operatori.testa, corrente);
                            } else {
                                break;
                            }
                        }
                        operatori.push(new Op(corrente.s));
                    } else if (confronto == -1) {
                        operatori.push(corrente);
                    } else if (confronto == -2) {
                        ope = operatori.pop();
                        
                        if (ope.s.equals("√") || ope.s.equals("^2")) {
                            op2 = operandi.pop();
                            
                            if (ope.s.equals("√")) {
                                op2.n = Math.sqrt(op2.n);
                            } else {
                                op2.n = Math.pow(op2.n, 2);
                            }
                            operandi.push(op2);
                        } else {
                            op2 = operandi.pop();
                            op1 = operandi.pop();
                            gestisciOperatori(op2, op1, ope);
                            operandi.push(op1);
                        }
                        operatori.pop();
                    }
                }
                ultimoOperatore = !corrente.s.equals(")");
            }
        }

        while (operatori.testa != null) {
            ope = operatori.pop();
            
            if (ope.s.equals("√") || ope.s.equals("^2")) {
                op2 = operandi.pop();
                
                if (ope.s.equals("√")) {
                    op2.n = Math.sqrt(op2.n);
                } else {
                    op2.n = Math.pow(op2.n, 2);
                }
                operandi.push(op2);
            } else {
                op2 = operandi.pop();
                op1 = operandi.pop();
                gestisciOperatori(op2, op1, ope);
                operandi.push(op1);
            }
        }
        if(operandi.testa != null)
            return operandi.testa.n;
        else
            return Double.NaN;
    }
}