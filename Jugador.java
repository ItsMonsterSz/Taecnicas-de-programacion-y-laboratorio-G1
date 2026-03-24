import java.util.Random;

import javax.swing.JPanel;

public class Jugador {

    private final int TOTAL_CARTAS = 10;
    private final int MARGEN_SUPERIOR = 10;
    private final int MARGEN_IZQUIERDA = 10;
    private final int DISTANCIA = 40;

    private Carta[] cartas = new Carta[TOTAL_CARTAS];
    private Random r = new Random();

    public void repartir() {
        for (int i = 0; i < cartas.length; i++) {
            cartas[i] = new Carta(r);
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();
        int posicion = MARGEN_IZQUIERDA + DISTANCIA * (TOTAL_CARTAS - 1);
        for (Carta carta : cartas) {
            carta.mostrar(pnl, posicion, MARGEN_SUPERIOR);
            posicion -= DISTANCIA;
        }
        pnl.repaint();
    }

    public String getGrupos() {
        int[] contadores = new int[NombreCarta.values().length];

        for (Carta carta : cartas) {
            contadores[carta.getNombre().ordinal()]++;
        }

        String resultado = "";
        for (int i = 0; i < contadores.length; i++) {
            if (contadores[i] >= 2) {
                resultado += Grupo.values()[contadores[i]] + " de " + NombreCarta.values()[i] + "\n";

            }
        }

        String grupos = "Se encontraron los siguientes grupos:\n" + resultado + getGrupoEscalera() + getPuntaje();

        return resultado.isEmpty() ? "No se encontraron grupos" : grupos;
    }

    public String getGrupoEscalera() {
        String resultado = "";
        NombreCarta[] nombres = NombreCarta.values();

        for (Pinta pinta : Pinta.values()) {
            boolean[] presentes = new boolean[nombres.length];
            for (int i = 0; i < cartas.length; i++) {
                if (cartas[i].getPinta() == pinta) {
                    presentes[cartas[i].getNombre().ordinal()] = true;
                }
            }

            int inicio = -1;
            int longitud = 0;

            for (int i = 0; i <= presentes.length; i++) {
                if (i < presentes.length && presentes[i]) {
                    if (inicio == -1) inicio = i;
                    longitud++;
                } else {
                    if (longitud >= 2) {
                        resultado += Grupo.values()[longitud] + " de " + pinta + " (";
                        for (int j = inicio; j < inicio + longitud; j++) {
                            resultado += nombres[j];
                            if (j < inicio + longitud - 1) resultado += ", ";
                        }
                        resultado += ")\n";
                    }
                    inicio = -1;
                    longitud = 0;
                }
            }
        }

        return resultado;
    }

    public String getPuntaje() {
        int[] contadoresNombre = new int[NombreCarta.values().length];
        for (int i = 0; i < cartas.length; i++) {
            contadoresNombre[cartas[i].getNombre().ordinal()]++;
        }

        boolean[] indicesEnEscalera = new boolean[TOTAL_CARTAS];
        NombreCarta[] nombres = NombreCarta.values();

        for (Pinta pinta : Pinta.values()) {
            boolean[] presentes = new boolean[nombres.length];
            for (int i = 0; i < cartas.length; i++) {
                if (cartas[i].getPinta() == pinta) {
                    presentes[cartas[i].getNombre().ordinal()] = true;
                }
            }

            int inicio = -1;
            int longitud = 0;

            for (int i = 0; i <= presentes.length; i++) {
                if (i < presentes.length && presentes[i]) {
                    if (inicio == -1) inicio = i;
                    longitud++;
                } else {
                    if (longitud >= 2) {
                        for (int j = inicio; j < inicio + longitud; j++) {
                            for (int k = 0; k < cartas.length; k++) {
                                if (cartas[k].getNombre().ordinal() == j && cartas[k].getPinta() == pinta) {
                                    indicesEnEscalera[k] = true;
                                }
                            }
                        }
                    }
                    inicio = -1;
                    longitud = 0;
                }
            }
        }

        int puntaje = 0;
        for (int i = 0; i < cartas.length; i++) {
            NombreCarta nombre = cartas[i].getNombre();
            boolean enGrupoNombre = contadoresNombre[nombre.ordinal()] >= 2;
            boolean enEscalera   = indicesEnEscalera[i];

            if (!enGrupoNombre && !enEscalera) {
                puntaje += getValorCarta(nombre);
            }
        }

        return "Puntaje total: " + puntaje + "\n";
    }

    private int getValorCarta(NombreCarta nombre) {
        int ordinal = nombre.ordinal();
        if (ordinal == 0 || ordinal >= 10) {
            return 10;
        }
        return ordinal + 1;
    }
}
