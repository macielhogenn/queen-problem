/**
 * @author osmar e maciel
 *
 * O programa utiliza o metodo de permutacao para gerar todas as solucoes do problema,
 * Utiliza um vetor para armazenar as posicoes das rainhas
 *
 * Forca bruta por permutacao
 *
 */
public class ForcaBrutaPermutacao {

    /**
     * Atributo do numero de solucoes encontradas ao final do algoritmo
     */
    private static int solucoes;

    //Habilita ou desabilida a saida dos dados de impressao
     private static boolean desabilidarImpressao = true;

    /**
     * Trata a saida de dados
     *
     * @param string
     */
    private static void println(String string) {
        if (!desabilidarImpressao) {
            System.out.println(string);
        }
    }

    /**
     * Trata a saida de dados
     *
     * @param string
     */
    private static void print(String string) {
        if (!desabilidarImpressao) {
            System.out.print(string);
        }
    }
        
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
        int x, y;
        for (int i = 0; i < k; i++) {

            x = i;
            //Recupera o numero da rainha da posicao i 
            y = rainhas[i];
            while (true) {
                x = x + 1;
                y = y - 1;
                if ((x > k - 1) || (y < 0)) {
                    break;
                }
                /* se duas rainhas na mesma coluna */
                if (y == rainhas[x]) {
                    return false;
                }
            }
            x = i;
            y = rainhas[i];
            while (true) {
                x = x - 1;
                y = y - 1;
                if (x < 0 || y < 0) {
                    break;
                }
                //Verifica as diagonais
                if (y == rainhas[x]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * *************************************************
     * A funcao recursiva do metodo permutacao(). Cada instancia do metodo e
     * responsavel por posicionar uma rainha na linha (em todas as colunas
     * possiveis). Se uma rainha pode ser posicionada, baseando-se nas suas
     * propriedades, sem que esta seja atacada pelas outras rainhas ja
     * posicionadas, entao esta rainha e' posicionada na posicao corrente e,
     * recursivamente, as rainhas seguintes sao posicionadas.
     *
     * @param rainhas o vetor onde as rainhas serao inseridas
     * @param usado o vetor onde as posicoes usadas sao marcadas
     * @param k coordenada da linha corrente onde a rainhas devera ser inserida
     */
    public static void permutacao(int rainhas[], int[] usado, int k) {

        //Recupera a quantidade de rainhas
        int qtdeRainha = rainhas.length;

        //Se k e igual da quantidade rainhas cheguei no final da linha
        if (k == qtdeRainha) {
            if (valida(rainhas, k)) {
                //Imprime o tabuleiro quando encontrar a solucao
                imprime(rainhas);
                //Conta o numero de solucoes encontradas
                solucoes = solucoes + 1;
            }
        } else {
            //Percorre o vetor de rainhas
            for (int i = 0; i < qtdeRainha; i++) {
                //realiza a permutacao somente para elementos não utilizados
                if (usado[i] == 0) {
                    usado[i] = 1;
                    rainhas[k] = i;
                    //Avanca para proxima linha (k=k+1)
                    permutacao(rainhas, usado, k + 1);
                    usado[i] = 0;
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

        println(" Solucao numero " + (solucoes + 1) + ":");

        for (int i = 0; i < qtdeRainha; i++) {
            for (int j = 0; j < qtdeRainha; j++) {
                if (rainhas[j] == i) {
                    print(" " + i + " ");
                } else {
                    print(" . ");
                }
            }
            println(" ");
        }
        println(" ");
    }

    /**
     * Executa o teste do agoritmo
     *
     * @param args
     */
    public static void main(String args[]) {
        
        System.out.println("Permutacao");
        
        //Especifica a quantidade de rainhas serem testadas
        int qtdeRainhasTeste[] = {4, 6, 8, 10};
        //Especifica o numero de vezes a se realizado com cada qtde de rainhas
        int repeticoesTeste[] = {10};
        
        //Declara o tempo total do teste
        double tempoTeste = 0;

        //Testa as quantidades das rainhas especificadas no vetor
        for (int qtdeR = 0; qtdeR < qtdeRainhasTeste.length; qtdeR++) {

            int qtdeRainha = qtdeRainhasTeste[qtdeR];
            int rainhas[] = new int[qtdeRainha];
            int usado[] = new int[qtdeRainha];

            //Realiza a repeticao das quantidades           
            for (int qtdeT = 0; qtdeT < repeticoesTeste.length; qtdeT++) {

                println("Execuntando com " + qtdeRainha + " rainhas por " + repeticoesTeste[qtdeT] + " vezes.");
                
                //Zera o numero de solucoes
                solucoes = 0;

                //Declara o tempo final da repeticao
                long tempoFinal = 0;

                //Repete o teste as vezes especificadas
                for (int qtdeV = 0; qtdeV < repeticoesTeste[qtdeT]; qtdeV++) {

                    //Executa o gc antes de cada teste
                    System.gc();
                    
                    //Zera o tempo de inicio da vez
                    long tempo = 0;
                    tempo = System.currentTimeMillis();

                    /* se um elemento i estiver em uso, entao used[i] == 1, caso contrario, used[i] == 0. */
                    for (int i = 0; i < qtdeRainha; i++) {
                        usado[i] = 0;
                    }
                    permutacao(rainhas, usado, 0);

                    //Pega o tempo final do processamento da vez
                    tempo = System.currentTimeMillis() - tempo;
                    //Acumula o tempo do teste ao tempo final
                    tempoFinal = tempoFinal + tempo;
                }
                //Calcula a media do tempo
                double mediaTempo = tempoFinal / repeticoesTeste[qtdeT];
                System.out.println("O tempo medio para " + qtdeRainha + " rainhas, executando " + repeticoesTeste[qtdeT] + " é vezes é " + mediaTempo + " milisegundos com " + solucoes + " solucoes em " + repeticoesTeste[qtdeT] + " repeticoes");
                tempoTeste = tempoTeste + mediaTempo;
            }
        }
        System.out.println("O tempo total do teste e " + tempoTeste + " milisegundos.");
    }
}
