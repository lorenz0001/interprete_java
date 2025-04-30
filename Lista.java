public class Lista { 
    public Op testa = null;
    public Op coda = null;

    public void add(String c) {
        if (coda != null && (
                c.equals(".") ||
                Funzioni_calc.isNumero(c) && (coda.s.charAt(coda.s.length() - 1) == '.' ||
                Funzioni_calc.isNumero(coda.s) ||
                (coda.s.equals("-") && (coda.prec == coda || coda.prec.s.equals("("))))
            )) {
            coda.s += c;    
        } else {
            Op nuovo = new Op(c);
            if (testa == null) {
                testa = nuovo;
                coda = nuovo;
            } else {
                coda.next = nuovo;
                nuovo.prec = coda;
                coda = nuovo;
            }
        }
    }

    public Op rimuovi() {
        if (testa == null) {
            return null;
        }
        Op tmp = testa;
        testa = testa.next;
        if (testa == null) {
            coda = null;
        } else {
            testa.prec = null;
        }
        return tmp;
    }
}
