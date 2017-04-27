/**
 *
 * @author osmar
 *
 * Forca bruta por permutacao
 *
 */
public class ForcaBrutaBackTracking {

    private static int solucoes;

    public static boolean valida(int qtdeRainha, int[] rainhas) {        
        //Percorre o vetor de rainhas
        for (int i = 0; i < qtdeRainha; i++) 
             /* se duas rainhas na mesma coluna ... */ {
            if ((rainhas[i] == rainhas[qtdeRainha])
                    || /* ... ou duas rainhas na mesma diagonal */ 
                    (Math.abs(rainhas[i] - rainhas[qtdeRainha]) == (qtdeRainha - i))) {
                return false;
                /* solucao invalida */
            }
        }
        return true;
        /* solucao valida */
    }
    
    public static void backTracking(int qtdeRainha, int[] rainhas, int k) {
        int i;
        if (k == qtdeRainha) /* solucao completa */ {
            //imprime( qtdeRainha, rainhas);
            solucoes = solucoes + 1;
        } else /* posiciona a rainha k + 1 */ {
            for (i = 0; i < qtdeRainha; i++) {
                rainhas[k] = i;
                if (valida(k, rainhas)) {
                    backTracking(qtdeRainha, rainhas, k + 1);
                }
            }
        }
    }

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
        int qtdeRainhasTeste[] = {4,6};
        //Especifica o numero de vezes a se realizado com cada qtde de rainhas
        int repeticoesTeste[] = {5,10};

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
                    
                    //permutacao(qtdeRainha, rainhas, usado, 0);
                    backTracking(qtdeRainha, rainhas, 0);

                    //Pega o tempo final do processamento da vez
                    tempo = System.currentTimeMillis() - tempo;
                    tempoFinal = tempoFinal + tempo;
                }
                System.out.println("O tempo para " + qtdeRainha + " rainhas, executando " + repeticoesTeste[qtdeT] + " é vezes é " + tempoFinal / repeticoesTeste[qtdeT] + " milisegundos com " + solucoes / repeticoesTeste[qtdeT] + " solucoes");
            }
        }
    }
}
