
/**
 *
 * @author osmar
 *
 * O programa utiliza o metodo de backtraking para buscar a solucao do problema,
 * movendo as rainhas a frente e retornando quando for impossivel achar a
 * solucao, funcionando como uma busca em profundidade pelas possiveis posicoes
 * onde as rainhas podem ser colocadas.
 *
 * Utiliza um matriz para armazenar as posicoes das rainhas
 *
 */
public class ForcaBrutaBackTrackingMatriz {

    /**
     * Atributo do numero de solucoes encontradas ao final do algoritmo
     */
    private static int solucoes = 0;

    /**
     * Uma das propriedades da rainha e que nao pode haver outra rainha na linha
     * ou na coluna onde esta se encontra. Assim, na construcao do algoritmo de
     * solucao, nao se pode colocar uma rainha em uma posicao que esteja sendo
     * atacada. Esta mesma propriedade tambem vale para as diagonais em relacao
     * as rainha ja posicionadas.
     *
     * @param tabuleiro a matriz do tabuleiro
     * @param linha linha do tabuleiro
     * @param coluna coluna do tabuleiro
     *
     * @return true se a rainha [linha][coluna] nao for atacada nas posicoes ja
     * atacadas por rainhas previamente inseridas
     */
    private static boolean valida1(int[][] tabuleiro, int linha, int coluna) {

        //Recupera a quantidade de rainhas
        int qtdeRainha = tabuleiro.length;

        //Verifica se a rainha esta na mesma linha
        for (int j = 0; j < qtdeRainha; j++) {
            if (tabuleiro[linha][j] == 1) {
                return false;
            }
        }

        //Verifica se a rainha esta na mesma coluna
        for (int i = 0; i < qtdeRainha; i++) {
            if (tabuleiro[i][coluna] == 1) {
                return false;
            }
        }

        // verifica se ocorre ataque na diagonal principal
        // acima e abaixo
        for (int i = linha, j = coluna; i >= 0 && j >= 0; i--, j--) {
            if (tabuleiro[i][j] == 1) {
                return false;
            }
        }
        for (int i = linha, j = coluna; i < qtdeRainha && j < qtdeRainha; i++, j++) {
            if (tabuleiro[i][j] == 1) {
                return false;
            }
        }

        // verifica se ocorre ataque na diagonal secundária
        // acima e abaixo
        for (int i = linha, j = coluna; i >= 0 && j < qtdeRainha; i--, j++) {
            if (tabuleiro[i][j] == 1) {
                return false;
            }
        }
        for (int i = linha, j = coluna; i < qtdeRainha && j >= 0; i++, j--) {
            if (tabuleiro[i][j] == 1) {
                return false;
            }
        }

        // Chegou ate posicao segura
        return true;
    }

    private static boolean valida(int[][] tabuleiro, int linha, int coluna) {

        //Recupera a quantidade de rainhas
        int qtdeRainha = tabuleiro.length;

        //testa se as posicoes sao regioes que estao sendo atacadas
        for (int k = 1; k <= linha; k++) {

            //Verifica se a rainha esta na mesma linha            
            if (tabuleiro[linha - k][coluna] == 1) {
                return false;
            }
            //Verifica se a rainha esta diagonal principal
            if ((coluna - k >= 0) && (tabuleiro[linha - k][coluna - k] == 1)) {
                return false;
            }
            //Verifica se a rainha esta na diagonal secundaria
            if ((coluna + k < qtdeRainha) && (tabuleiro[linha - k][coluna + k]) == 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * *************************************************
     * A funcao recursiva do metodo backTracking(). Cada instancia do metodo e
     * responsavel por posicionar uma rainha na linha (em todas as colunas
     * possiveis). Se uma rainha pode ser posicionada, baseando-se nas suas
     * propriedades, sem que esta seja atacada pelas outras rainhas ja
     * posicionadas, entao esta rainha e' posicionada na posicao corrente e,
     * recursivamente, as rainhas seguintes sao posicionadas.
     *
     * @param tabuleiro o tabuleiro onde as rainhas serao inseridas
     * @param linha coordenada da linha corrente onde a rainhas devera ser
     * inserida
     */
    private static void backTracking(int[][] tabuleiro, int linha) {

        //Recupera a quantidade de rainhas
        int qtdeRainha = tabuleiro.length;

        //Solução completa
        if (linha == qtdeRainha) {
            //Imprime o tabuleiro quando encontrar a solucao
            //imprime(tabuleiro);
            //Conta o numero de solucoes encontradas
            solucoes = solucoes + 1;
        } else {
            for (int coluna = 0; coluna < qtdeRainha; coluna++) {
                // Coloca uma nova rainha na posicao [row][col]                
                tabuleiro[linha][coluna] = 1;
                // Se for possivel posiciona-la...                
                if (valida(tabuleiro, linha, coluna)) {
                    backTracking(tabuleiro, linha + 1);
                }
                // Se nao for possivel, remove a rainha desta posicao, realiza o backTracking
                tabuleiro[linha][coluna] = 0;
            }
        }
    }
      
    /**
     * *************************************************
     * Imprime o tabuleiro da solucao do problema das Rainhas
     *
     * @param tabuleiro a matriz do tabuleiro
     * **************************************************
     */
    private static void imprime(int[][] tabuleiro) {

        //Recupera a quantidade de rainhas
        int qtdeRainha = tabuleiro.length;

        System.out.println(" Solucao numero " + (solucoes + 1) + ":");

        for (int i = 0; i < qtdeRainha; i++) {
            for (int j = 0; j < qtdeRainha; j++) {
                //Posicao ocupada
                if (tabuleiro[i][j] == 1) {
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
     * Executa o teste do agoritmo das N Rainhas
     *
     * @param args
     */
    public static void main(String args[]) {

        //Especifica a quantidade de rainhas serem testadas
        int qtdeRainhasTeste[] = {4};
        //Especifica o numero de vezes a se realizado com cada qtde de rainhas
        int repeticoesTeste[] = {1};

        //Realiza os testes para as quantidades das rainhas especificadas no vetor
        for (int qtdeR = 0; qtdeR < qtdeRainhasTeste.length; qtdeR++) {

            int qtdeRainha = qtdeRainhasTeste[qtdeR];

            int[][] tabuleiro = new int[qtdeRainha][qtdeRainha];

            //Realiza a repeticao do teste para a quantidade de rainhas    
            for (int qtdeT = 0; qtdeT < repeticoesTeste.length; qtdeT++) {

                //Zera o numero de solucoes
                solucoes = 0;

                //Declara o tempo final da repeticao
                long tempoFinal = 0;

                //Repete o teste para as vezes especificadas no vetor
                for (int qtdeV = 0; qtdeV < repeticoesTeste[qtdeT]; qtdeV++) {

                    //Zera o tempo de inicio da vez
                    long tempo = 0;
                    //Pega o tempo corrente
                    tempo = System.currentTimeMillis();

                    //Executa a solucao do algoritmo                    
                    backTracking(tabuleiro, 0);

                    //Pega o tempo final do processamento da vez
                    tempo = System.currentTimeMillis() - tempo;
                    //Acumula o tempo do teste ao tempo final
                    tempoFinal = tempoFinal + tempo;
                }
                //Calcula a media do tempo
                double mediaTempo = tempoFinal / repeticoesTeste[qtdeT];
                System.out.println("O tempo para " + qtdeRainha + " rainhas, executando " + repeticoesTeste[qtdeT] + " é vezes é " + mediaTempo + " milisegundos com " + solucoes / repeticoesTeste[qtdeT] + " solucoes");
            }
        }
    }
}
