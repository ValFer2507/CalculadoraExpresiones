package calculadora;

public class Nodo {
    private TipoOperando tipo;
    private String valor;
    Nodo izquierdo;
    Nodo derecho;
    
    public Nodo(){
    }
    
    public Nodo(String valor, TipoOperando tipo){
        this.tipo = tipo;
        this.valor = valor;
    }
    
    public TipoOperando getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    public double getValorNumerico(){
        try{
            if (tipo == TipoOperando.CONSTANTE){
                return Double.parseDouble(this.valor);
            }
        }catch(Exception e){}
        
        return 0;
    }
}
