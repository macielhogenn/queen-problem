
import java.util.Random;

/**
 *
 * @author admin
 */
public class HeuristicaHillClimbing {

    /**
     * Atributo do numero de solucoes encontradas ao final do algoritmo
     */
    private static int solucoes;
    private static int interacaoSolucao;

    private static Random randomico = new Random();

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
     * Transforma o vetor em uma string para escrever em tela.
     *
     * @param vet Vetor com os dados inteiros a serem transformados em string
     * @return Um literal com os dados do vetor separados por ;
     */
    private static String vetorToString(int[] vet) {
        String ret = "";
        if ((vet != null) && (vet.length > 0)) {
            for (int i = 0; i < vet.length; i++) {
                ret = ret + vet[i] + ";";
            }
            //retira o ultimo ; da string
            ret = ret.substring(0, ret.length() - 1);
        }
        return ret;
    }

    /**
     * Imprime o tabuleiro da solucao do problema das Rainhas
     *
     * @param rainhas vetor das rainhas
     */
    private static void imprime(int[] rainhas) {
        for (int i = 0; i < rainhas.length; i++) {
            for (int j = 0; j < rainhas.length; j++) {
                //Posicao ocupada
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

    public static int getNumeroRainhasTabuleiro(int[][] tabuleiro) {
        int count = 0;
        for (int i = 0; i < tabuleiro.length; i++) {
            for (int j = 0; j < tabuleiro.length; j++) {
                if (tabuleiro[i][j] == 1) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Realiza a mutacao em um individuo de forma aleatoria
     *
     * @param individuo Um individuo a sofrer mutacao
     * @return um individuo com a mutacao
     */
    private static int[] mutacao(int[] individuo) {
        //Seleciona a posicao da mutacao
        int posicao = randomico.nextInt(individuo.length);
        //Novo valor para a posicao selecionado
        int novovalor = randomico.nextInt(individuo.length);
        //Realiza a mutacao na posicao com o novoValor
        individuo[posicao] = novovalor;
        return individuo;
    }

    /**
     * Carrega a populacao inicial de individuos
     *
     * @param tamanhoIndividuo quantidade de individuos do conjunto inicial
     */
    private static int[] geraPopulacaoInicial(int tamanhoIndividuo) {
        //Inicializa o vetor de retorno
        int[] ret = new int[tamanhoIndividuo];
        int i = 0;
        //Gera os individuos de acordo com o tamanho do candidato
        while (i < tamanhoIndividuo) {
            //Gera um uma rainha aleatoria
            ret[i] = new Random().nextInt(tamanhoIndividuo);
            i = i + 1;
        }
        return ret;
    }

    /**
     * Função de avaliacao do individuo, retorna a quantidade de rainhas a
     * salvo.
     *
     * @param individuo
     * @return a quantidade de rainhas salvas no individuo
     */
    public static int funcaoFitness(int[] individuo) {
        int[][] tabuleiro = new int[individuo.length][individuo.length];

        // Monta o tabuleiro com 0 em posicao vazia e 1 com a rainha
        for (int i = 0; i < individuo.length; i++) {
            int posicaoRainha = individuo[i];
            for (int j = 0; j < individuo.length; j++) {
                if (posicaoRainha == j) {
                    tabuleiro[i][j] = 1;
                } else {
                    tabuleiro[i][j] = 0;
                }
            }
        }

        // Verifica se as rainhas estao salvas
        // A quantidade rainhas na salvas e o retorno da função fitness
        int ret = 0;
        for (int i = 0; i < individuo.length; i++) {
            for (int j = 0; j < individuo.length; j++) {
                if (tabuleiro[i][j] == 1) {
                    if (valida(tabuleiro, i, j) == false) {
                        ret++;
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Verifica se existe uma rainha ameaçando a posicao linha,coluna existindo
     * retorna true caso contrário retorna false
     *
     * @param tabuleiro a ser validado
     * @param linha linha da rainha
     * @param coluna coluna da rainha
     * @return true se a rainha esta salva.
     *
     */
    private static boolean valida(int[][] tabuleiro, int linha, int coluna) {

        // verificar na linha da rainha
        for (int i = 0; i < tabuleiro.length; i++) {
            if (i != linha && tabuleiro[i][coluna] == 1) {
                return true;
            }
        }

        // verificar na coluna da rainha
        for (int j = 0; j < tabuleiro.length; j++) {
            if (j != coluna && tabuleiro[linha][j] == 1) {
                return true;
            }
        }

        // verificar na diagonal1
        int i = linha - 1;
        int j = coluna - 1;
        while (i >= 0 && j >= 0) {
            if (tabuleiro[i][j] == 1) {
                return true;
            }
            i--;
            j--;
        }

        // verificar na diagonal2
        i = linha + 1;
        j = coluna + 1;
        while (i < tabuleiro.length && j < tabuleiro.length) {
            if (tabuleiro[i][j] == 1) {
                return true;
            }
            i++;
            j++;
        }

        // Verificar a diagonal3
        i = linha + 1;
        j = coluna - 1;
        while (i < tabuleiro.length && j >= 0) {
            if (tabuleiro[i][j] == 1) {
                return true;
            }
            i++;
            j--;
        }
        // Verifica a diagonal4
        i = linha - 1;
        j = coluna + 1;
        while (i >= 0 && j < tabuleiro.length) {
            if (tabuleiro[i][j] == 1) {
                return true;
            }
            i--;
            j++;
        }
        return false; // esta a salvo
    }

    /**
     * Verifica se o individuo e uma solucao do problema
     *
     * @param individuo um solucao a ser verifica
     * @return true se o individuo e uma solucao do problema
     */
    public static boolean verificaSolucao(int[] individuo) {
        int[][] tabuleiro = new int[individuo.length][individuo.length];

        // Monta o tabuleiro com 0 em posicao vazia e 1 com a rainha
        for (int i = 0; i < individuo.length; i++) {
            int posicaoRainha = individuo[i];
            for (int j = 0; j < individuo.length; j++) {
                if (posicaoRainha == j) {
                    tabuleiro[i][j] = 1;
                } else {
                    tabuleiro[i][j] = 0;
                }
            }
        }

        //Verifica se todas as rainhas estao em posicoes validas
        int cont = 0;
        for (int i = 0; i < individuo.length; i++) {
            for (int j = 0; j < individuo.length; j++) {
                //Verifica se a posicao esta ocupada
                if (tabuleiro[i][j] == 1) {
                    if (valida(tabuleiro, i, j) == true) {
                        cont = cont + 1;
                    }
                }
            }
        }
        boolean ret = false;
        //if ((cont == 0) && (getNumeroRainhasTabuleiro(tabuleiro) == individuo.length)) {
        if (cont == 0) {
            ret = true;
        }
        return ret;
    }

    /**
     * Algoritmo que executa as interacoes do algoritmo Hill Climbing
     *
     * @param qtdeInteracoes Numeros de vezes a executar as interacoes no
     * algoritmo
     * @param qtdeRainha Quantidade de rainhas no tabuleiro
     * 
     * @return Retorna o melhor individuo encontrado nas interacoes
     */
    public static int[] hillClimbing(int qtdeInteracoes, int qtdeRainha) {
        //Gera o candidato inicial
        int[] candidato = geraPopulacaoInicial(qtdeRainha);
        //Calcula o custo do candidato inicial
        int custoCandidato = funcaoFitness(candidato);

        //Controla as interacoes 
        int interacao = 0;
        //Pàra se chegar no numero maximo de interacoes ou achar a solucao
        while ((interacao < qtdeInteracoes) && (verificaSolucao(candidato) == false)) {
            // Gera o proximo candidato aleatoriamente
            int[] vizinho = mutacao(candidato);
            //Calcula o Custo do novo vizinho
            int custoVizinho = funcaoFitness(vizinho);
            // Verifica se é maior que o anterior
            if (custoVizinho >= custoCandidato) {
                //Troca se o custo for maior
                candidato = vizinho;
            }
            //Avanca para a proxima interacao
            interacao = interacao + 1;
        }
        //Armazena a interacao que encontrou a solucao
        interacaoSolucao = interacao;
        //Retorna o candidato
        return candidato;
    }

    /**
     * Faz a chamada do algoritmo e Hill Climbine apresenta as estatisticas
     * @param qtdeInteracoes
     * @param qtdeRainha 
     */    
    public static void algoritmoHillClimbing(int qtdeInteracoes, int qtdeRainha) {
        //Guarda em que interacao ocorreu a solucao
        interacaoSolucao = -1;
        //Guarda o melhor invididuo

        //Procura o menor individuo
        int[] melhorIndividuo = hillClimbing(qtdeInteracoes, qtdeRainha);

        if (verificaSolucao(melhorIndividuo)) {
            solucoes = solucoes + 1;
            println("Solucao encontrada em " + interacaoSolucao + " interacoes");
            println("Melhor Individuo = " + vetorToString(melhorIndividuo));
            println("Fitness  = " + funcaoFitness(melhorIndividuo));
        } else {
            println("Solucao nao encontrada em " + interacaoSolucao + " interacoes");
            println("Melhor Individuo = " + vetorToString(melhorIndividuo));
            println("Fitness  = " + funcaoFitness(melhorIndividuo));
        }
        println("Solucao:");
        imprime(melhorIndividuo);
    }

    public static void main(String[] args) {

        System.out.println("HillClimbing");
        
        //Especifica a quantidade de rainhas serem testadas
        int qtdeRainhasTeste[] = {4, 6, 8, 10};
        //Especifica o numero de vezes a se realizado com cada qtde de rainhas
        int repeticoesTeste[] = {10};

        //Parametros do algoritmo genetico
        //Quantidade de geracoes
        int qtdeIteracoes = 1000000;

        //Declara o tempo total do teste
        double tempoTeste = 0;

        //Realiza os testes para as quantidades das rainhas especificadas no vetor
        for (int qtdeR = 0; qtdeR < qtdeRainhasTeste.length; qtdeR++) {

            int qtdeRainha = qtdeRainhasTeste[qtdeR];

            //Realiza a repeticao do teste para a quantidade de rainhas    
            for (int qtdeT = 0; qtdeT < repeticoesTeste.length; qtdeT++) {

                println("Execuntando com " + qtdeRainha + " rainhas por " + repeticoesTeste[qtdeT] + " vezes.");

                //Zera o numero de solucoes
                solucoes = 0;

                //Declara o tempo final da repeticao
                long tempoFinal = 0;

                //Repete o teste para as vezes especificadas no vetor
                for (int qtdeV = 0; qtdeV < repeticoesTeste[qtdeT]; qtdeV++) {

                    //Executa o gc antes de cada teste
                    System.gc();

                    //Pega o tempo corrente
                    long tempo = System.currentTimeMillis();

                    //Executa a solucao do algoritmo         
                    algoritmoHillClimbing(qtdeIteracoes, qtdeRainha);

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
