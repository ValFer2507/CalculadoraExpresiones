package calculadora;

public class ArbolBinario {

    private Nodo raiz;

    public Nodo getRaiz() {
        return raiz;
    }

    public void setRaiz(Nodo raiz) {
        this.raiz = raiz;
    }

    //constructores
    public ArbolBinario() {
        raiz = null;
    }

    public ArbolBinario(Nodo n) {
        raiz = n;
    }

    public String mostrarInorden(Nodo n) {
        if (n != null) {
            return mostrarInorden(n.izquierdo) + " "
                    + n.getValor() + " "
                    + mostrarInorden(n.derecho);
        }
        return "";
    }
/*
    public String mostrarPreorden(Nodo n) {
        if (n != null) {
            return n.getValor() + " "
                    + mostrarPreorden(n.izquierdo) + " "
                    + mostrarPreorden(n.derecho);
        }
        return "";
    }
*/
    public String mostrarPostorden(Nodo n) {
        if (n != null) {
            return mostrarPostorden(n.izquierdo) 
                    + mostrarPostorden(n.derecho) 
                    + n.getValor();
        }
        return "";
    }
    public String asignaValores(Nodo n) {
        if (n != null) {
            //mostrarPostorden(n.izquierdo);
        }
        return "";
    }
}
