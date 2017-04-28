
/**
 *
 *  http://www.ic.unicamp.br/~zanoni/mc102/2013-1s/aulas/aula22.pdf
 *
 * @author osmar
 *
 * O programa utiliza o metodo de backtraking para buscar a solucao do problema,
 * movendo as rainhas a frente e retornando quando for impossivel achar a
 * solucao, funcionando como uma busca em profundidade pelas possiveis posicoes
 * onde as rainhas podem ser colocadas.
 *
 * Utiliza um vetor para armazenar as posicoes das rainhas
 *
 */

public class ForcaBrutaBackTracking {

    /**
     * Atributo do numero de solucoes encontradas ao final do algoritmo
     */
    private static int solucoes;

    /**
     * Uma das propriedades da rainha e que nao pode haver outra rainha na linha
     * ou na coluna onde esta se encontra. Assim, na construcao do algoritmo de
     * solucao, nao se pode colocar uma rainha em uma posicao que esteja sendo
     * atacada. Esta mesma propriedade tambem vale para as diagonais em relacao
     * as rainha ja posicionadas.
     *
     * @param rainhas o vetor das rainhas
     * @param k linha do vetor a ser analisa
     *
     * @return true se a rainha [k] nao for atacada nas posicoes ja atacadas por
     * rainhas previamente inseridas
     */
    public static boolean valida(int[] rainhas, int k) {

        //Percorre o vetor de rainhas
        for (int i = 0; i < k; i++) {
            //Verifica se a rainha esta na mesma coluna
            if (rainhas[i] == rainhas[k]) {
                return false;
            }
            //Verifica se a rainha esta diagonal principal
            if ((rainhas[i] - rainhas[k]) == (k - i)) {
                return false;
            }
            //Verifica se a rainha esta na diagonal secundaria
            if ((rainhas[k] - rainhas[i]) == (k - i)) {
                return false;
            }

            // Código alternativo, pois faz verificao da diagonal principal e secundaria simultaneamente usando abs para tirar o sinal*/
            //if ( Math.abs(rainhas[i] - rainhas[qtdeRainha]) == (k - i)) {
            // return false;                
            //}
        }
        return true;
        /* solucao valida */
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
     * @param rainhas o vetor onde as rainhas serao inseridas
     * @param k coordenada da linha corrente onde a rainhas devera ser inserida
     */
    public static void backTracking(int[] rainhas, int k) {

        //Recupera a quantidade de rainhas
        int qtdeRainha = rainhas.length;

        /* solucao completa */
        if (k == qtdeRainha) {
            //Imprime o tabuleiro quando encontrar a solucao
            //imprime(rainhas);
            //Conta o numero de solucoes encontradas
            solucoes = solucoes + 1;
        } else {
            /* posiciona a rainha k + 1 */
            for (int i = 0; i < qtdeRainha; i++) {
                rainhas[k] = i;
                if (valida(rainhas, k)) {
                    backTracking(rainhas, k + 1);
                }
            }
        }
    }

    /**
     * Imprime o tabuleiro com as rainhas
     *
     * @param rainhas
     */
    private static void imprime(int rainhas[]) {

        //Recupera a quantidade de rainhas
        int qtdeRainha = rainhas.length;

        System.out.println(" Solucao numero " + (solucoes + 1) + ":");

        for (int i = 0; i < qtdeRainha; i++) {
            for (int j = 0; j < qtdeRainha; j++) {
                //Posicao ocupada
                if (rainhas[j] == i) {
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
     * Executa o teste do agoritmo
     *
     * @param args
     */
    public static void main(String args[]) {

        //Especifica a quantidade de rainhas serem testadas
        int qtdeRainhasTeste[] = {4, 6};
        //Especifica o numero de vezes a se realizado com cada qtde de rainhas
        int repeticoesTeste[] = {5, 10};

        //Realiza os testes para as quantidades das rainhas especificadas no vetor
        for (int qtdeR = 0; qtdeR < qtdeRainhasTeste.length; qtdeR++) {

            int qtdeRainha = qtdeRainhasTeste[qtdeR];
            int rainhas[] = new int[qtdeRainha];

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
                    backTracking(rainhas, 0);

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
