/**
 *  http://www.ic.unicamp.br/~zanoni/mc102/2013-1s/aulas/aula22.pdf
 * 
 * @author osmar
 *
 * Forca bruta por permutacao
 *
 */
public class ForcaBrutaPermutacao {

    /**
     * Acumula a quantidade de solucoes encontradas
     */
    private static int solucoes;

    /**
     * Função que valida o vetor das posicoes das rainhas
     *
     * @param rainhas
     * @param k
     * @return Veradeiro ou falso se as posicoes sao validas.
     */
    public static boolean solucaoValida(int k, int[] rainhas) {
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
     * Realiza a pertumacao de todos os resultados
     *
     * @param qtdeRainha
     * @param rainhas
     * @param usado
     * @param k
     */
    public static void permutacao(int qtdeRainha, int rainhas[], int[] usado, int k) {
        int i;
        if (k == qtdeRainha) {
            if (solucaoValida(k, rainhas)) {
                //imprime(qtdeRainha, rainhas);
                solucoes = solucoes + 1;
            }
        } else {
            for (i = 0; i < qtdeRainha; i++) {
                //realiza a permutacao somente para elementos não utilizados
                if (usado[i] == 0) {
                    usado[i] = 1;
                    rainhas[k] = i;
                    permutacao(qtdeRainha, rainhas, usado, k + 1);
                    usado[i] = 0;
                }
            }
        }
    }

    /**
     * Imprime o tabuleiro com as rainhas
     *
     * @param qtdeRainha
     * @param rainhas
     */
    private static void imprime(int qtdeRainha, int rainhas[]) {
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

    public static void main(String args[]) {
        //Especifica a quantidade de rainhas serem testadas
        int qtdeRainhasTeste[] = {4, 6};
        //Especifica o numero de vezes a se realizado com cada qtde de rainhas
        int repeticoesTeste[] = {5, 10};

        //Testa as quantidades das rainhas especificadas no vetor
        for (int qtdeR = 0; qtdeR < qtdeRainhasTeste.length; qtdeR++) {

            int qtdeRainha = qtdeRainhasTeste[qtdeR];
            int rainhas[] = new int[qtdeRainha];
            int usado[] = new int[qtdeRainha];

            //Realiza a repeticao das quantidades           
            for (int qtdeT = 0; qtdeT < repeticoesTeste.length; qtdeT++) {

                //Zera o numero de solucoes
                solucoes = 0;

                //Declara o tempo final da repeticao
                long tempoFinal = 0;

                //Repete o teste as vezes especificadas
                for (int qtdeV = 0; qtdeV < repeticoesTeste[qtdeT]; qtdeV++) {

                    //Zera o tempo de inicio da vez
                    long tempo = 0;
                    tempo = System.currentTimeMillis();

                    /* se um elemento i estiver em uso, entao used[i] == 1, caso contrario, used[i] == 0. */
                    for (int i = 0; i < qtdeRainha; i++) {
                        usado[i] = 0;
                    }
                    permutacao(qtdeRainha, rainhas, usado, 0);

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
