public class Stack {
    Op testa = null;

    public void push(Op n) {
        n.next = testa;
        testa = n;
    }

    public Op pop() {
        if (testa != null) {
            Op old = testa;
            testa = testa.next;
            return old;
        } else {
            return null;
        }
    }

    public boolean isEmpty() {
        return testa == null;
    }
}
