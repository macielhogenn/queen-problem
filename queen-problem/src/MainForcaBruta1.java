/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * http://www.vision.ime.usp.br/~pmiranda/mac122_2s14/aulas/aula20/aula20.html
 * @author Osmar
 */
public class MainForcaBruta1 {

    static int numeroSolucao = 0;
    
    public static void troca(int[] v, int i, int j) {
        int auxiliar;
        auxiliar = v[i];
        v[i] = v[j];
        v[j] = auxiliar;
    }

    public static boolean solucaoValida(int[] linhas, int qtdeRainha) {
        int i;
        int x, y;
        int xx, yy;

        for (i = 0; i < qtdeRainha; i++) {
            x = i;
            y = linhas[i];

            xx = x;
            yy = y;
            while (true) {
                xx += 1;
                yy -= 1;
                if (xx > qtdeRainha-1 || yy < 0) {
                    break;
                }
                if (yy == linhas[xx]) {
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

                if (yy == linhas[xx]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void imprimeSolucao(int linhas[], int qtdeRainha) {
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
            x = i;
            y = linhas[i];
            tabuleiro[y][x] = y+"";
        }
        String linha = "";
        for (i = 0; i < qtdeRainha; i++) {
            for (j = 0; j < qtdeRainha; j++) {
                linha = linha +(" " + tabuleiro[i][j] + " ");
            }            
            linha = linha +("\n");
        }
        System.out.println(linha);
    }

    public static void testaPermutacoes(int[] linhas, int qtdeRainha, int k) {
        int i;
        if (k == qtdeRainha) {
            if (solucaoValida(linhas, qtdeRainha)) {
                imprimeSolucao(linhas, qtdeRainha);
            }
        } else {
            for (i = k; i < qtdeRainha; i++) {
                troca(linhas, k, i);
                testaPermutacoes(linhas, qtdeRainha, k + 1);
                troca(linhas, i, k);
            }
        }
    }

    public static void solucoes8Rainhas(int qtdeRainha) {
        int[] linhas = new int[qtdeRainha];
        int i;
        for (i = 0; i < qtdeRainha; i++) {
            linhas[i] = i;
        }
        testaPermutacoes(linhas, qtdeRainha, 0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int qtdeRainha = 8;
        solucoes8Rainhas(qtdeRainha);
    }

}
