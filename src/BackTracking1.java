/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * http://www.vision.ime.usp.br/~pmiranda/mac122_2s14/aulas/aula20/aula20.html
 * @author Osmar
 */
public class BackTracking1 {

    static int numeroSolucao = 0;
    
    public static void troca(int[] v, int i, int j) {
        int auxiliar;
        auxiliar = v[i];
        v[i] = v[j];
        v[j] = auxiliar;
    }

    public static boolean solucaoValida(int[] rainhas, int qtdeRainha) {
        int i;
        int x, y;
        int xx, yy;

        for (i = 0; i < qtdeRainha; i++) {
            x = i;
            y = rainhas[i];

            xx = x;
            yy = y;
            while (true) {
                xx += 1;
                yy -= 1;
                if (xx > qtdeRainha-1 || yy < 0) {
                    break;
                }
                if (yy == rainhas[xx]) {
                    return false;
                }
            }

            xx = x;
            yy = y;
            while (true) {
                xx -= 1;
                yy -= 1;
                if (xx < 0 || yy < 0) {
                    break;
                }

                if (yy == rainhas[xx]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void imprimeSolucao(int rainhas[], int qtdeRainha) {
        String[][] tabuleiro = new String[qtdeRainha][qtdeRainha];
        int i, j;
        int x, y;
        
        numeroSolucao++;
        System.out.print("******** SOL:" + numeroSolucao + " ********\n");
        for (i = 0; i < qtdeRainha; i++) {
            for (j = 0; j < qtdeRainha; j++) {
                tabuleiro[i][j] = ".";
            }
        }
        for (i = 0; i < qtdeRainha; i++) {
            // coluna da rainha
            x = i;
            // o numero da rainha define a posicao da linha
            y = rainhas[i];
            tabuleiro[y][x] = y+"";
        }
        //String da saida dos dados do tabuleiro
        String linha = "";
        for (i = 0; i < qtdeRainha; i++) {
            for (j = 0; j < qtdeRainha; j++) {
                linha = linha +(" " + tabuleiro[i][j] + " ");
            }            
            linha = linha +("\n");
        }
        System.out.println(linha);
    }

    public static void testaPermutacoes(int[] rainhas, int qtdeRainha, int k) {
        int i;
        if (k == qtdeRainha) {
            if (solucaoValida(rainhas, qtdeRainha)) {
                imprimeSolucao(rainhas, qtdeRainha);
            }
        } else {
            for (i = k; i < qtdeRainha; i++) {
                troca(rainhas, k, i);
                testaPermutacoes(rainhas, qtdeRainha, k + 1);
                troca(rainhas, i, k);
            }
        }
    }

    public static void solucoes8Rainhas(int qtdeRainha) {
        int[] rainhas = new int[qtdeRainha];
        int i;
        //Adiciona um numero para cada rainha no vetor de forma sequencial
        for (i = 0; i < qtdeRainha; i++) {
            rainhas[i] = i;
        }
        testaPermutacoes(rainhas, qtdeRainha, 0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int qtdeRainha = 4;
        solucoes8Rainhas(qtdeRainha);
    }

}
