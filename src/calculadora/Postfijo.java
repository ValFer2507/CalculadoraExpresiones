package calculadora;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Postfijo {

    private static String expresionInfijo;
    private static String expresionPostfijo;
    private static String mensajeError;

    public static void setExpresionInfijo(String expresionInfijo) {
        Postfijo.expresionInfijo = expresionInfijo;
    }

    public static String getExpresionPostfijo() {
        expresionInfijo = expresionInfijo.replace(" ", "");
        Stack pila = new Stack();
        expresionPostfijo = "";
        boolean error = false;
        mensajeError = "";

        int noOperador = 0; //0 no operador, 1 parentesis), 2 parentesis), 3 operando
        int parentesis = 0;

        int i = 0;
        //Recorrer cada uno de los caracteres
        while (i < expresionInfijo.length() && !error) {
            String caracter = expresionInfijo.substring(i, i + 1);
            if (caracter.equals("(")) {
                noOperador = 1;
                pila.push(caracter);
                parentesis++;
            } else if (caracter.equals(")")) {
                noOperador = 2;
                if (parentesis == 0) {
                    error = true;
                    mensajeError = "Hace falta parentesis izquierdo";
                } else {
                    parentesis--;
                    caracter = (String) pila.peek();
                    while (!pila.empty() && !caracter.equals("(")) {
                        expresionPostfijo += " " + pila.pop();
                        caracter = (String) pila.peek();
                    }
                    pila.pop();
                }
            } else if (esOperador(caracter)) {
                if (noOperador < 2) {
                    error = true;
                    mensajeError = "Hace falta operando antes de " + caracter;
                } else {
                    noOperador = 0;
                    expresionPostfijo = expresionPostfijo + " ";
                    while (!pila.empty() && esPredecesor((String) pila.peek(), caracter)) {
                        expresionPostfijo = expresionPostfijo + pila.pop();
                    }
                    pila.push(caracter);
                }
            } else if (esLetra(caracter) || esDigito(caracter)) {
                noOperador = 3;
                expresionPostfijo = expresionPostfijo + caracter;
            } else {
                error = true;
                mensajeError = "Simbolo '" + caracter + "' indefinido ";
            }
            i++;
        }
        //verificando Errores
        if (parentesis > 0) {
            mensajeError = "Error convirtiendo: Hace falta parentesis derecho";
            expresionPostfijo = "";
        } else if (error && !mensajeError.equals("")) {
            mensajeError = "Error convirtiendo: " + mensajeError;
            expresionPostfijo = "";
        } else if (i == 0 || noOperador == 0) {
            mensajeError = "Error convirtiendo: No hay expresión o falta operando";
            expresionPostfijo = "";
        } else {
            //terminar de construir la expresión PostFijo
            expresionPostfijo = expresionPostfijo + " ";
            while (!pila.empty()) {
                expresionPostfijo = expresionPostfijo + (String) pila.pop();
            }
        }
        return expresionPostfijo;
    }

    public static List<String> getVariables() {
        List<String> variables = new ArrayList<>();
        mensajeError = "";

        boolean error = false;
        int tipoOperando = 0; //0: no es operando, 1: variable, 2: constante Numerica
        int i = 0;
        String texto = "";

        //Recorrer caeda uno de los caracteres
        while (i < expresionPostfijo.length() && !error) {
            String caracter = expresionPostfijo.substring(i, i + 1);
            if (esLetra(caracter) && tipoOperando == 2) {
                error = true;
            } else if ((esLetra(caracter) && tipoOperando < 2) || (esDigito(caracter) && tipoOperando == 1)) {
                tipoOperando = 1;
                texto = texto + caracter;
            } else if (esDigito(caracter) && tipoOperando != 1) {
                tipoOperando = 2;
                texto = texto + caracter;
            } else if (caracter.equals(" ") && tipoOperando == 1) {
                if (!variables.contains(texto)) {
                    variables.add(texto);
                }
                texto = "";
                tipoOperando = 0;
            } else if (caracter.equals(" ") && tipoOperando == 2) {
                texto = "";
                tipoOperando = 0;
            }
            i++;
        }

        if (!error) {
            return variables;
        } else {
            mensajeError = "Error obtenido variables: " + mensajeError;
            return null;
        }
    }

    //136
    public static void mostrarVarialbes(JTable tbl) {
        List<String> variables = Postfijo.getVariables();
        String[][] datos;

        if (variables != null) {
            datos = new String[variables.size()][2]; // 2 columnas var y valor
            for (int i = 0; i < variables.size(); i++) {
                datos[i][0] = variables.get(i);
            }
            String[] columnas = new String[]{"Variable", "Valor"};
            DefaultTableModel dtm = new DefaultTableModel(datos, columnas);
            tbl.setModel(dtm);
        }
    }

    public static void calcularExpresion(JTable tbl) {
        List<String> variables = Postfijo.getVariables();
        String[][] matriz = new String[variables.size()][2];
        Stack pilaCalculo = new Stack();
        TipoOperando tipo = TipoOperando.NINGUNO;

        // Recorrer la tabla y almacenar los valores en la matriz
        for (int fila = 0; fila < variables.size(); fila++) {
            for (int col = 0; col < 2; col++) {
                matriz[fila][col] = (String) tbl.getValueAt(fila, col);
            }
        }
    }

    public static ArbolBinario getArbol() {
        Stack pila = new Stack();
        int i = 0;
        String texto = "";
        boolean error = false;
        TipoOperando tipo = TipoOperando.NINGUNO;

        //Recorrer cada uno de los caracteres
        while (i < expresionPostfijo.length() && !error) {
            String caracter = expresionPostfijo.substring(i, i + 1);
            if (esLetra(caracter) && tipo == TipoOperando.CONSTANTE) {
                error = true;
                mensajeError = "Una constante nuérica no puede contener letras";
            } else if ((esLetra(caracter) && tipo != TipoOperando.CONSTANTE)
                    || (esDigito(caracter) && tipo == TipoOperando.VARIABLE)) {
                tipo = TipoOperando.VARIABLE;
                texto += caracter;
            } else if (esDigito(caracter) && tipo != TipoOperando.NINGUNO) {
                tipo = TipoOperando.CONSTANTE;
                texto += caracter;
            } else if (caracter.equals(" ") && tipo != TipoOperando.NINGUNO) {
                Nodo nOperando = new Nodo(texto, tipo);
                pila.push(nOperando);
                texto = "";
                tipo = TipoOperando.NINGUNO;
            } else {
                caracter = expresionPostfijo.substring(i, i + 1);
                if (esOperador(caracter)) {
                    Nodo nOperador = new Nodo(caracter, TipoOperando.NINGUNO);
                    Nodo nDerecho = (Nodo) pila.pop();
                    Nodo nIzquierdo = (Nodo) pila.pop();
                    //Hijos del nodo operador
                    nOperador.izquierdo = nIzquierdo;
                    nOperador.derecho = nDerecho;

                    pila.push(nOperador);
                }
            }
            i++;
        }
        return !error ? new ArbolBinario((Nodo) pila.pop()) : null;
    }

    public static String[] encavezados = new String[]{"Variable", "Valor"};

    public static boolean esLetra(String caracter) {
        return (caracter.compareTo("A") >= 0 && caracter.compareTo("Z") <= 0)
                || (caracter.compareTo("a") >= 0 && caracter.compareTo("z") <= 0);
    }

    public static boolean esDigito(String caracter) {
        return caracter.compareTo("0") >= 0 && caracter.compareTo("9") <= 0;
    }

    public static boolean esOperador(String caracter) {
        String caracteres = "+-*/%^";
        return caracteres.contains(caracter);
    }

    public static boolean esPredecesor(String operador1, String operador2) {
        boolean p = false;
        if (operador1.equals("^")) {
            p = true;
        } else if (operador1.equals("%")) {
            if (!operador2.equals("^")) {
                p = true;
            }
        } else if (operador1.equals("/") || operador1.equals("*")) {
            if (!operador2.equals("^") && !operador1.equals("%")) {
                p = true;
            }
        } else if (operador1.equals("-") || operador1.equals("+")) {
            if (operador2.equals("-") || operador2.equals("+")) {
                p = true;
            }
        }
        return p;
    }
}
