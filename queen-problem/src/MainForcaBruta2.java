
/**
 * https://www.williamrufino.com.br/problema-das-n-rainhas-em-java/
 */

import java.util.Random;

public class MainForcaBruta2 {

    private static int diagonal_posistiva[];
    private static int diagonal_negativa[];
    private static int rainhas[];
    private static int numeroRainhas;
    private static int numeroDiagonais;

    private static void inicializaTabuleiro() {
        Random aleatorio = new Random();

        rainhas = new int[numeroRainhas];
        diagonal_posistiva = new int[numeroDiagonais];
        diagonal_negativa = new int[numeroDiagonais];

        for (int i = 0; i < numeroRainhas; i++) {
            rainhas[i] = i;
        }

        for (int count = 0; count < numeroDiagonais; count++) {
            double count1 = count;
            if (count % 2 == 0) {
                diagonal_posistiva[count] = 1;
            } else {
                diagonal_posistiva[count] = 0;
            }
            int count2 = count;
            if (count1 == ((numeroDiagonais) / 2)) {
                count1 = Math.ceil(count1);
                count2 = (int) count1;
                diagonal_negativa[count2] = numeroRainhas;
            } else {
                diagonal_negativa[count2] = 0;
            }
        }

        for (int i = 0; i < numeroRainhas; ++i) {
            troca(aleatorio.nextInt(numeroRainhas), aleatorio.nextInt(numeroRainhas));
        }

    }

    private static void troca(int Coluna01, int Coluna02) {
        int linha01, linha02;
        linha01 = rainhas[Coluna01];
        linha02 = rainhas[Coluna02];

        int Diagonais_Neg_Aux;
        int Diagonais_Pos_Aux;

        //********************* Primeira Rainha ******************************
        Diagonais_Pos_Aux = linha01 + Coluna01;
        Diagonais_Neg_Aux = (Coluna01 - linha01) + (numeroRainhas - 1);
        diagonal_posistiva[Diagonais_Pos_Aux] = diagonal_posistiva[Diagonais_Pos_Aux] - 1;
        diagonal_negativa[Diagonais_Neg_Aux] = diagonal_negativa[Diagonais_Neg_Aux] - 1;

        Diagonais_Pos_Aux = linha02 + Coluna01;
        Diagonais_Neg_Aux = ((Coluna01 - linha02) + (numeroRainhas - 1));
        diagonal_posistiva[Diagonais_Pos_Aux] = diagonal_posistiva[Diagonais_Pos_Aux] + 1;
        diagonal_negativa[Diagonais_Neg_Aux] = diagonal_negativa[Diagonais_Neg_Aux] + 1;

        //******************** Segunda Rainha ********************************
        Diagonais_Pos_Aux = linha02 + Coluna02;
        Diagonais_Neg_Aux = (Coluna02 - linha02) + (numeroRainhas - 1);
        diagonal_posistiva[Diagonais_Pos_Aux] = diagonal_posistiva[Diagonais_Pos_Aux] - 1;
        diagonal_negativa[Diagonais_Neg_Aux] = diagonal_negativa[Diagonais_Neg_Aux] - 1;

        Diagonais_Pos_Aux = linha01 + Coluna02;
        Diagonais_Neg_Aux = (Coluna02 - linha01) + (numeroRainhas - 1);
        diagonal_posistiva[Diagonais_Pos_Aux] = diagonal_posistiva[Diagonais_Pos_Aux] + 1;
        diagonal_negativa[Diagonais_Neg_Aux] = diagonal_negativa[Diagonais_Neg_Aux] + 1;

        //******************* Substitui rainhas ********************************
        rainhas[Coluna01] = linha02;
        rainhas[Coluna02] = linha01;
    }

    private static Boolean verificaAtaquesRainhas(int Coluna) {
        Boolean Ataques = false;
        if (diagonal_posistiva[procuraDiagonalPos(Coluna)] > 1) {
            return true;
        }
        if (diagonal_negativa[procuraDiagonalNeg(Coluna)] > 1) {
            return true;
        }
        return Ataques;
    }

    private static int procuraDiagonalPos(int Coluna) {
        int diagonal = -1;
        diagonal = Coluna + rainhas[Coluna];
        return diagonal;
    }

    private static int procuraDiagonalNeg(int Coluna) {
        int diagonal = -1;
        diagonal = (Coluna - rainhas[Coluna]) + (numeroRainhas - 1);
        return diagonal;
    }

    private static int verificaAtaques() {
        int ataques = 0;
        for (int Diagonal_count = 0; Diagonal_count < numeroDiagonais; Diagonal_count++) {
            if (diagonal_posistiva[Diagonal_count] > 1) {
                ataques = ataques + diagonal_posistiva[Diagonal_count] - 1;
            }
            if (diagonal_negativa[Diagonal_count] > 1) {
                ataques = ataques + diagonal_negativa[Diagonal_count] - 1;
            }
        }
        return ataques;
    }

    private static Boolean test() {
        for (int Diagonal_count = 0; Diagonal_count < numeroDiagonais; Diagonal_count++) {
            if (diagonal_posistiva[Diagonal_count] > 1) {                
                return false;
            }
            if (diagonal_negativa[Diagonal_count] > 1) {
                return false;
            }
        }
        return true;
    }

    private static void Controlador() {
        int colisoes01 = 0, colisoes02 = 0;
        int[] rainhas_aux = new int[2];
        int executouTroca = 1;
        while (executouTroca != 0) {
            executouTroca = 0;
            for (int coluna01 = 0; (coluna01 < numeroRainhas - 1); coluna01++) {
                int coluna02;
                for (coluna02 = coluna01 + 1; (coluna02 < numeroRainhas); coluna02++) {
                    if ((verificaAtaquesRainhas(coluna01) == true) || (verificaAtaquesRainhas(coluna02) == true)) {
                        colisoes01 = verificaAtaques();
                        troca(coluna01, coluna02);
                        colisoes02 = verificaAtaques();
                        executouTroca = executouTroca + 1;
                        if (colisoes01 == 0) {
                            executouTroca = 0;
                        }
                        if (colisoes01 < colisoes02) {
                            troca(coluna01, coluna02);
                            executouTroca = executouTroca - 1;
                        }
                    }
                }
            }
        }
        if (test()) {
            return;
        } 
    }

    private static void imprime() {
        for (int i = 0; i < numeroRainhas; i++) {
            for (int i1 = 0; i1 < numeroRainhas; i1++) {
                if (rainhas[i1] == i) {
                    System.out.print(" " + i + " ");
                } else {
                    System.out.print(" . ");
                }
            }
            System.out.println(" ");
        }
        System.out.println(" ");   
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long tempo, tempoFinal = 0;
        numeroRainhas = 8;
        numeroDiagonais = (numeroRainhas * 2) - 1;
        
        inicializaTabuleiro();
        tempo = System.currentTimeMillis();
        Controlador();
        tempo = System.currentTimeMillis() - tempo;
        tempoFinal += tempo;        
        imprime();
            
        System.out.println("O tempo para " + numeroRainhas + " rainhas  é " + tempoFinal + " milisegundos");
    }
}
