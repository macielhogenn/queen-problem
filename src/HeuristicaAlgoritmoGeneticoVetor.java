import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Osmar e Maciel
 *
 */
public class HeuristicaAlgoritmoGeneticoVetor {

    private static int qtdeRainha;

    private static int max_fitness;
    private static int tamanhoPopulacaoInicial = 10;
    private static Random randomico = new Random();
    // parametros do teste de stress
    private static boolean ocorreuFalha = false;
    private static int totalMaxIteracoes = 0;
    private static int totalIteracoes = 0;

    private static boolean disablePrint = false;

    /**
     * Trata a saida de dados
     *
     * @param string
     */
    private static void println(String string) {
        if (!disablePrint) {
            System.out.println(string);
        }
    }

    /**
     * Trata a saida de dados
     *
     * @param string
     */
    private static void print(String string) {
        if (!disablePrint) {
            System.out.print(string);
        }
    }

    /**
     * Transforma o vetor em uma string para escrever
     *
     * @param vet
     * @return Um literal com os dados do vetor separados por ;
     */
    private static String vetorToString(int[] vet) {
        String ret = "";
        if (vet != null) {
            for (int i = 0; i < qtdeRainha; i++) {
                ret = ret + vet[i] + ";";
            }
            //retira o ultimo ;
            ret = ret.substring(0, ret.length() - 1);
        }
        return ret;
    }

    /**
     * Carreca a populacao inicial de individuos do AG
     *
     * @param populacao
     */
    private static void carregarPopulacao(Set populacao) {
        while (populacao.size() < tamanhoPopulacaoInicial) {
            //Gera um individuo
            int[] individuo = gerarIndividuo();
            println("individuo=" + vetorToString(individuo));
            //Adiciona o novo individuo a populacao
            populacao.add(individuo);
        }
    }

    /**
     * Imprime o tabuleiro da solucao do problema das Rainhas
     *
     * @param rainhas vetor das rainhas
     */
    private static void imprime(int[] rainhas) {
        for (int i = 0; i < qtdeRainha; i++) {
            for (int j = 0; j < qtdeRainha; j++) {
                //Posicao ocupada
                if (rainhas[j] == i) {
                    System.out.print(" " + i + " ");
                } else {
                    System.out.print(" . ");
                }
            }
            println(" ");
        }
        println(" ");
    }

    /**
     * Logica do Algoritmo Genetico mantendo os melhores na populacao. 
     * Retorna o melhor individuo da populacao
     *
     * @param populacao
     * @param probabilidadeMutacao
     */
    private static int[] algoritmoGenetico(Set populacao, double probabilidadeMutacao, int fitnessAtual) {
        int[] melhorIndividuo = new int[qtdeRainha];
        Set filhos = new HashSet();
        int tamanhoPopulacao = populacao.size();
        while (filhos.size() < tamanhoPopulacao) {
            int p1[] = selecionarAleatorio(populacao, null);
            int p2[] = selecionarAleatorio(populacao, p1);
            int filho[] = crossover(p1, p2);
            if (randomico.nextDouble() <= probabilidadeMutacao) {
                int ffitness = fitness(filho);
                if (ffitness <= fitnessAtual) {
                    filho = mutacao(filho);
                }
            }
            filhos.add(filho);
        }

        // adicionar dois dos melhores pais
        Object[] pais = populacao.toArray();
        int[] f = new int[pais.length];
        int melhorF = -1;
        for (int i = 0; i < pais.length; i++) {
            f[i] = fitness((int[]) pais[i]);
            if (melhorF < f[i]) {
                melhorF = f[i];
            }
        }
        populacao.clear();
        while (populacao.size() < 2) {
            for (int i = 0; i < f.length; i++) {
                if (f[i] == melhorF) {
                    populacao.add((int[]) pais[i]);
                }
                if (populacao.size() == 2) {
                    break;
                }
            }
            melhorF--;
        }
        filhos.addAll(populacao);
        Object[] pop = filhos.toArray();
        f = new int[pop.length];
        melhorF = -1;
        for (int i = 0; i < f.length; i++) {
            f[i] = fitness((int[]) pop[i]);
            if (melhorF < f[i]) {
                melhorF = f[i];
                melhorIndividuo = (int[]) pop[i];
            }
        }
        populacao.clear();
        while (populacao.size() < tamanhoPopulacao) {
            if (melhorF < 0) {
                break;
            }
            for (int i = 0; i < f.length; i++) {
                if (f[i] == melhorF && populacao.size() < tamanhoPopulacao) {
                    populacao.add((int[]) pop[i]);
                }
            }
            melhorF--;
        }
        return melhorIndividuo;
    }

    /**
     * Realiza a mutacao em um individuo de forma aleatoria
     *
     * @param filho
     * @return
     */
    private static int[] mutacao(int[] filho) {
        //Seleciona a posicao da mutacao
        int posicao = randomico.nextInt(filho.length);
        //Novo valor para a posicao selecionado
        int novovalor = randomico.nextInt(filho.length);
        filho[posicao] = novovalor;
        return filho;
    }

    /**
     * Realiza o crossover entre dois individuos
     *
     * @param p1
     * @param p2
     * @return
     */
    private static int[] crossover(int[] p1, int[] p2) {

        //Filho resultante do crossover
        int[] filho = new int[p1.length];

        //Seleciona o ponto de crossover
        int posicao = randomico.nextInt(p1.length);
        for (int i = 0; i < posicao; i++) {
            filho[i] = p1[i];
        }

        for (int i = posicao; i < p1.length; i++) {
            filho[i] = p2[i];
        }

        return filho;
    }

    /**
     * Seleciona um individuo da populacao aleatoriamente
     *
     * @param populacao
     * @return
     */
    private static int[] selecionarAleatorio(Set populacao, int[] individuoBase) {
        //Cria um vetor para receber um novo elemento
        int[] individuoSelecionado = new int[qtdeRainha];
        //Somente se px diferente de null
        if (individuoBase != null) {
            //Copia o vetor px para pn
            System.arraycopy(individuoBase, 0, individuoSelecionado, 0, individuoBase.length);
        }
        //Transforma a populacao em um vetor de objetos
        Object[] tmp = populacao.toArray();
        //Enquanto for igual a px seleciona outro individuo
        while (java.util.Arrays.equals(individuoSelecionado, individuoBase)) {
            int i = randomico.nextInt((populacao.size()));
            individuoSelecionado = (int[]) tmp[i];
        }

        return individuoSelecionado;
    }

    /**
     * Gera um individuo com n posicoes de forma aleatoria de acordo com a
     * quantidade de rainhas
     *
     * @return
     */
      private static int[] gerarIndividuo1() {
        //Inicializa o vetor de retorno
        int[] ret = new int[qtdeRainha];


        int i = 0;
        while (i < qtdeRainha) {
            //Gera um uma rainha aleatoria
            ret[i] = randomico.nextInt(qtdeRainha);
            
            i = i + 1;
        }
        return ret;
    }
    
    
    private static int[] gerarIndividuo() {
        //Inicializa o vetor de retorno
        int[] ret = new int[qtdeRainha];

        //Inicializa o vetor da roleta
        int[] roleta = new int[qtdeRainha];
        //Controla o tamanho da roleta
        int tamanhoRoleta = qtdeRainha;
        //Coloca as rainhas na roleta para serem sortedas
        for (int i = 0; i < qtdeRainha; i++) {
            //Soma 1 para que as rainhas comecem com 1
            roleta[i] = i ;
        }

        int i = 0;
        while (i < qtdeRainha) {
            //Gera um numero aleatorio para selecionar uma rainha da roleta
            int pos = randomico.nextInt(tamanhoRoleta);
            ret[i] = roleta[pos];

            //Retira elemento sortedo da roleta            
            for (int j = pos; j < tamanhoRoleta - 1; j++) {
                roleta[j] = roleta[j + 1];
            }

            //Decrementa a quantidade de elementos da roleta
            tamanhoRoleta = tamanhoRoleta - 1;

            i = i + 1;
        }
        return ret;
    }

    /**
     * função fitness, retorna a quantidade de rainhas a salvo.
     *
     * @param individuo
     * @return
     */
    public static int fitness(int[] individuo) {
        int ret = 0;
        int[][] tabuleiro = new int[qtdeRainha][qtdeRainha];

        // Monta o tabuleiro com 0 em posicao vazia e 1 com a rainha
        for (int i = 0; i < qtdeRainha; i++) {
            //int posicaoRainha = Integer.parseInt(individuo.substring(i, i + 1)) - 1;
            int posicaoRainha = individuo[i];
            for (int j = 0; j < qtdeRainha; j++) {
                if (posicaoRainha == j) {
                    tabuleiro[i][j] = 1;
                } else {
                    tabuleiro[i][j] = 0;
                }
            }

        }
        // Verifica se as rainhas estao salvas
        // A antidade rainhas não salvas retorno da função fitness
        for (int i = 0; i < qtdeRainha; i++) {
            for (int j = 0; j < qtdeRainha; j++) {
                if (tabuleiro[i][j] == 1) {
                    if (!valida(tabuleiro, i, j)) {
                        ret++;
                    }
                }
            }
        }
        return ret;
    }

    /**
     * verifica se existe uma rainha ameaçando a posicao i,j existindo retorna
     * true caso contrário retorna false
     *
     * @param tabuleiro
     * @param linha
     * @param coluna
     * @return
     *
     */
    private static boolean valida(int[][] tabuleiro, int linha, int coluna) {
        //Recupera a quantidade de rainhas
        int qtdeRainha = tabuleiro.length;

        // verificar na horizontal
        for (int i = 0; i < qtdeRainha; i++) {
            if (i != linha && tabuleiro[i][coluna] == 1) {
                return true;
            }
        }

        // verificar na vertical
        for (int j = 0; j < qtdeRainha; j++) {
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
        while (i < qtdeRainha && j < qtdeRainha) {
            if (tabuleiro[i][j] == 1) {
                return true;
            }
            i++;
            j++;
        }

        // verificar na diagonal3
        i = linha + 1;
        j = coluna - 1;
        while (i < qtdeRainha && j >= 0) {
            if (tabuleiro[i][j] == 1) {
                return true;
            }
            i++;
            j--;
        }
        // verificar na diagonal4
        i = linha - 1;
        j = coluna + 1;
        while (i >= 0 && j < qtdeRainha) {
            if (tabuleiro[i][j] == 1) {
                return true;
            }
            i--;
            j++;
        }
        return false; // esta a salvo
    }

    public static void main(String[] args) {

        //Define a quantidade rainhas
        qtdeRainha = 8;
        max_fitness = qtdeRainha;

        //Pega o tempo inicial
        long tempo = System.currentTimeMillis();

        println("Populacao Inicial");

        // gerar a populacao inicial com 10 individuos
        Set populacao = new HashSet();
        carregarPopulacao(populacao);

        println("------------------------------");
        double probabilidadeMutacao = 0.15;
        int maxIteracoes = 300000;
        int[] melhorIndividuo = new int[qtdeRainha];
        boolean achouSolucao = false;
        int melhorFitness = 0;
        int i = 0;
        int contador = 0;

        for (i = 0; i < maxIteracoes; i++) {
            //Retorna o melhor individuo da populacao
            melhorIndividuo = algoritmoGenetico(populacao, probabilidadeMutacao, melhorFitness);
            //Retorna o fitness do melhor inidividuo da populacao
            int ftness = fitness(melhorIndividuo);
            //Verifica se o fitness do melhor individuo é o melhor fitnesss
            if (ftness > melhorFitness) {
                probabilidadeMutacao = 0.10;
                contador = 0;
                println("novo fitness = " + ftness);
                melhorFitness = ftness;
                if (ftness == max_fitness) {
                    achouSolucao = true;
                    break;
                }
            } else {
                contador++;
                if (contador > 1000) {
                    probabilidadeMutacao = 0.30;
                } else if (contador > 2000) {
                    probabilidadeMutacao = 0.50;
                } else if (contador > 5000) {
                    //Limpa populacao
                    populacao.clear();
                    //Carrega uma nova populacao
                    carregarPopulacao(populacao);
                    probabilidadeMutacao = 0.10;
                    melhorFitness = -1;
                }
            }
        }

        println("------------------------------");
        if (achouSolucao) {
            println("Solucao encontrada em " + i + " iteracoes");
            println("Solucao = " + vetorToString(melhorIndividuo));
            println("Fitness = " + fitness(melhorIndividuo));
        } else {
            System.out.println("Solucao nao encontrada após " + i + " iteracoes");
            System.out.println("Melhor Individuo = " + vetorToString(melhorIndividuo));
            System.out.println("Fitness = " + fitness(melhorIndividuo));
            ocorreuFalha = true;
        }
        totalIteracoes = totalIteracoes + i;
        if (i > totalMaxIteracoes) {
            maxIteracoes = i;
        }
        println("------------------------------");
        imprime(melhorIndividuo);
        println("Tempo gasto = " + (System.currentTimeMillis() - tempo) + "ms");
    }
}
