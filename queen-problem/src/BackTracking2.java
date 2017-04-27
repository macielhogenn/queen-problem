
/**
 * https://www.williamrufino.com.br/problema-das-n-rainhas-em-java/
 */

import java.util.Random;

public class BackTracking2 {

    private static int[] diagonal_positiva;
    private static int diagonal_negativa[];
    private static int rainhas[];
    private static int qtdeRainha;
    private static int qtdeDiagonal;

    private static void inicializaTabuleiro() {
        
        //Define o numero de diagonais apartir do numero de rainhas
        qtdeDiagonal = (qtdeRainha * 2) - 1;
      
        //Cria o vetor das rainhas
        rainhas = new int[qtdeRainha];
        //Cria o vetor das diagonais positivas    
        diagonal_positiva = new int[qtdeDiagonal];
        //Cria o vetor das diagonais negativas 
        diagonal_negativa = new int[qtdeDiagonal];

        //Adiciona um numero para cada rainha no vetor de  forma sequencial
        for (int i = 0; i < qtdeRainha; i++) {
            rainhas[i] = i;
        }

        //Percorre as diagonais do tabuleiro
        for (int indiceDiagonal = 0; indiceDiagonal < qtdeDiagonal; indiceDiagonal++) {
            double count1 = indiceDiagonal;
            
            //Verifica se a diagonal é par
            if (indiceDiagonal % 2 == 0) {
                //Diagonal de indice par recebe 1
                diagonal_positiva[indiceDiagonal] = 1;
            } else {
                //Diagonal de indice impar recebe 0
                diagonal_positiva[indiceDiagonal] = 0;
            }
            //Verifica se o indice é a diagonal principal
            if (indiceDiagonal == ((qtdeDiagonal) / 2)) {
                diagonal_negativa[indiceDiagonal] = qtdeRainha;
            } else {
                diagonal_negativa[indiceDiagonal] = 0;
            }
        }
        
        //Distribui as rainhas de forma aleatoria ou utiliza elas em sequencia
        //Random aleatorio = new Random();
        //for (int i = 0; i < numeroRainhas; ++i) {
            //troca(aleatorio.nextInt(numeroRainhas), aleatorio.nextInt(numeroRainhas));
        //}
        
    }

    private static void troca(int Coluna01, int Coluna02) {
        int linha01, linha02;
        linha01 = rainhas[Coluna01];
        linha02 = rainhas[Coluna02];

        int Diagonais_Neg_Aux;
        int Diagonais_Pos_Aux;

        //********************* Primeira Rainha ******************************
        Diagonais_Pos_Aux = linha01 + Coluna01;
        Diagonais_Neg_Aux = (Coluna01 - linha01) + (qtdeRainha - 1);
        diagonal_positiva[Diagonais_Pos_Aux] = diagonal_positiva[Diagonais_Pos_Aux] - 1;
        diagonal_negativa[Diagonais_Neg_Aux] = diagonal_negativa[Diagonais_Neg_Aux] - 1;

        Diagonais_Pos_Aux = linha02 + Coluna01;
        Diagonais_Neg_Aux = ((Coluna01 - linha02) + (qtdeRainha - 1));
        diagonal_positiva[Diagonais_Pos_Aux] = diagonal_positiva[Diagonais_Pos_Aux] + 1;
        diagonal_negativa[Diagonais_Neg_Aux] = diagonal_negativa[Diagonais_Neg_Aux] + 1;

        //******************** Segunda Rainha ********************************
        Diagonais_Pos_Aux = linha02 + Coluna02;
        Diagonais_Neg_Aux = (Coluna02 - linha02) + (qtdeRainha - 1);
        diagonal_positiva[Diagonais_Pos_Aux] = diagonal_positiva[Diagonais_Pos_Aux] - 1;
        diagonal_negativa[Diagonais_Neg_Aux] = diagonal_negativa[Diagonais_Neg_Aux] - 1;

        Diagonais_Pos_Aux = linha01 + Coluna02;
        Diagonais_Neg_Aux = (Coluna02 - linha01) + (qtdeRainha - 1);
        diagonal_positiva[Diagonais_Pos_Aux] = diagonal_positiva[Diagonais_Pos_Aux] + 1;
        diagonal_negativa[Diagonais_Neg_Aux] = diagonal_negativa[Diagonais_Neg_Aux] + 1;

        //******************* Substitui rainhas ********************************
        rainhas[Coluna01] = linha02;
        rainhas[Coluna02] = linha01;
    }

    private static Boolean verificaAtaquesRainhas(int Coluna) {
        Boolean Ataques = false;
        if (diagonal_positiva[procuraDiagonalPos(Coluna)] > 1) {
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
        diagonal = (Coluna - rainhas[Coluna]) + (qtdeRainha - 1);
        return diagonal;
    }

    private static int verificaAtaques() {
        int ataques = 0;
        for (int indiceDiagonal = 0; indiceDiagonal < qtdeDiagonal; indiceDiagonal++) {
            if (diagonal_positiva[indiceDiagonal] > 1) {
                ataques = ataques + diagonal_positiva[indiceDiagonal] - 1;
            }
            if (diagonal_negativa[indiceDiagonal] > 1) {
                ataques = ataques + diagonal_negativa[indiceDiagonal] - 1;
            }
        }
        return ataques;
    }

    private static Boolean testar() {
        for (int indiceDiagonal = 0; indiceDiagonal < qtdeDiagonal; indiceDiagonal++) {
            if (diagonal_positiva[indiceDiagonal] > 1) {                
                return false;
            }
            if (diagonal_negativa[indiceDiagonal] > 1) {
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
            for (int coluna01 = 0; (coluna01 < qtdeRainha - 1); coluna01++) {                
                for (int coluna02 = coluna01 + 1; (coluna02 < qtdeRainha); coluna02++) {
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
        if (testar()) {
            return;
        } 
    }

    private static void imprime() {
        for (int i = 0; i < qtdeRainha; i++) {
            for (int i1 = 0; i1 < qtdeRainha; i1++) {
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
        long tempo=0, tempoFinal = 0;
        qtdeRainha = 4;
        
        inicializaTabuleiro();
        tempo = System.currentTimeMillis();
        Controlador();
        tempo = System.currentTimeMillis() - tempo;
        tempoFinal = tempoFinal + tempo;        
        imprime();
            
        System.out.println("O tempo para " + qtdeRainha + " rainhas  é " + tempoFinal + " milisegundos");
    }
}
