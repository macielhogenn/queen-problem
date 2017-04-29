
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Ricardo Alberto Harari - ricardo.harari@gmail.com
 *
 */
public class HeuristicaAlgoritmoGeneticoString {

    /**
     * Atributo do numero de solucoes encontradas ao final do algoritmo
     */
    private static int solucoes;
    private static int geracaoSolucao;
    private static int[] individuoEncontrado;

    private static int qtdeRainha;
    private static int maiorFitness;
    private static Random randomico;

    private static boolean desabilitarImpressao = true;

    /**
     * Trata a saida de dados
     *
     * @param string
     */
    private static void println(String string) {
        if (!desabilitarImpressao) {
            System.out.println(string);
        }
    }

    /**
     * Trata a saida de dados
     *
     * @param string
     */
    private static void print(String string) {
        if (!desabilitarImpressao) {
            System.out.print(string);
        }
    }

    /**
     *
     * @param populacao
     *
     */
    private static Set geraPopulacaoInicial(int tamanhoIndividuo) {
        Set populacao = new HashSet();
        while (populacao.size() < tamanhoIndividuo) {
            String individuo = gerarIndividuo();
            println("individuo=" + individuo);
            populacao.add(individuo);
        }
        return populacao;
    }

    /**
     * Mostrar o tabuleiro graficamente
     *
     * @param melhorIndividuo
     * @return
     */
    private static void imprime(String melhorIndividuo) {

        for (int i = 0; i < qtdeRainha; i++) {
            int posicaoRainha = Integer.parseInt(melhorIndividuo.substring(i, i + 1)) - 1;
            for (int j = 0; j < qtdeRainha; j++) {
                if (posicaoRainha == j) {
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
     * logica GA mantendo os melhores na populacao retorna o melhor individuo
     *
     * @param populacao
     * @param probabilidadeMutacao
     */
    private static String proximaGeracao(Set populacao, double probabilidadeMutacao, int fitnessAtual) {
        String melhor = null;
        Set filhos = new HashSet();
        int tamanhoPopulacao = populacao.size();
        while (filhos.size() < tamanhoPopulacao) {
            String p1 = selecionarIndividuo(populacao, "");
            String p2 = selecionarIndividuo(populacao, p1);
            String filho = crossover(p1, p2);
            if (randomico.nextDouble() <= probabilidadeMutacao) {
                int ffitness = avaliacaoIndividuo(filho);
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
            f[i] = avaliacaoIndividuo((String) pais[i]);
            if (melhorF < f[i]) {
                melhorF = f[i];
            }
        }
        populacao.clear();
        while (populacao.size() < 2) {
            for (int i = 0; i < f.length; i++) {
                if (f[i] == melhorF) {
                    populacao.add((String) pais[i]);
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
            f[i] = avaliacaoIndividuo((String) pop[i]);
            if (melhorF < f[i]) {
                melhorF = f[i];
                melhor = (String) pop[i];
            }
        }
        populacao.clear();
        while (populacao.size() < tamanhoPopulacao) {
            if (melhorF < 0) {
                // should never happen...
                System.out.println("???????");
                break;
            }
            for (int i = 0; i < f.length; i++) {
                if (f[i] == melhorF && populacao.size() < tamanhoPopulacao) {
                    populacao.add((String) pop[i]);
                }
            }
            melhorF--;
        }
        return melhor;
    }

    /**
     * Realiza a mutacao em um individuo de forma aleatoria
     *
     * @param individuo Um individuo a sofrer mutacao
     * @return um individuo com a mutacao
     */
    private static String mutacao(String individuo) {
        int mp = randomico.nextInt(individuo.length());
        int mc = randomico.nextInt(individuo.length());
        String parte1 = individuo.substring(0, mp);
        String parte2 = "";
        if (mp + 1 == individuo.length()) {
            parte2 = "";
        } else {
            parte2 = individuo.substring(mp + 1);
        }
        individuo = parte1 + mc + parte2;
        return individuo;
    }

    /**
     * Realiza o crossover entre dois individuos da populacao
     *
     * @param individuo1 Individuo que fornece a 1a parte dos genes para o novo
     * individuo
     * @param individuo2 Individuo que fornece a 2a parte dos genes para o novo
     * individuo
     * @return um individuo resultante do crossover
     */
    private static String crossover(String individuo1, String individuo2) {
        int i = randomico.nextInt(individuo1.length());
        String ret = individuo1.substring(0, i) + individuo2.substring(i);
        return ret;
    }

    /**
     * Seleciona um individuo da populacao aleatoriamente
     *
     * @param populacao
     * @param individuoBase para ser utilizado para nao selecionar
     * @return Um individuo selecionado aleatoramente da populacao
     */
    private static String selecionarIndividuo(Set populacao, String individuoBase) {
        //Cria um novo individuo para receber um novo elemento
        String individuoSelecionado = individuoBase;
        Object[] tmp = populacao.toArray();
        //Enquanto for igual a individuoBase seleciona outro individuo
        while (individuoSelecionado.equals(individuoBase)) {
            //Seleciona uma posicao da populacao
            int i = randomico.nextInt((populacao.size()));
            individuoSelecionado = (String) tmp[i];
        }
        return individuoSelecionado;
    }

    /**
     * Gera um individuo com n posicoes de forma aleatoria de acordo com a
     * quantidade de rainhas. Gera rainhas repetidas
     *
     * @return um individuo da populacao com repeticao de rainha.
     */
    private static String gerarIndividuo() {
        String ret = "";
        //Gera os genes de individuo de acordo com o tamanho do gene
        while (ret.length() < qtdeRainha) {
            //Gera um uma rainha aleatoria
            ret += (randomico.nextInt(qtdeRainha));
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
    public static int avaliacaoIndividuo(String individuo) {
        int ret = 0;
        int[][] tabuleiro = new int[qtdeRainha][qtdeRainha];
        // primeiro representamos o tabuleiro com 0 e 1
        //System.out.println("individuo="+individuo);
        for (int i = 0; i < qtdeRainha; i++) {

            int posicaoRainha = Integer.parseInt(individuo.substring(i, i + 1));
            //System.out.println("posicaoRainha="+posicaoRainha);
            for (int j = 0; j < qtdeRainha; j++) {
                tabuleiro[i][j] = posicaoRainha == j ? 1 : 0;
            }

        }
        // agora verificamos quantas rainhas estao a salvo, este ser� o nosso
        // retorno da fun��o fitness
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
     * Verifica se existe uma rainha ameaçando a posicao i,j existindo retorna
     * true caso contrário retorna false
     *
     * @param tabuleiro a ser validado
     * @param linha linha da rainha
     * @param coluna coluna da rainha
     * @return true se a rainha esta salva.
     *
     */
    private static boolean valida(int[][] tabuleiro, int linha, int coluna) {
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

    /**
     * Executa as geracoes do algoritmo genetico
     *
     * @param qRainha
     * @param qtdeGeracoes
     */
    private static void algoritmoGenetico(int qRainha, int qtdeGeracoes, int tamanhoIndividuo, double probabilidadeMutacao) {
        //Define a quantidade rainhas        
        qtdeRainha = qRainha;
        //Define o maior fitness pela quantidade rainhas 
        maiorFitness = qtdeRainha;

        // gerar a populacao inicial com 10 individuos
        Set populacao = geraPopulacaoInicial(tamanhoIndividuo);

        String melhorIndividuo = null;
        int melhorFitness = 0;
        int fitness = 0;
        int geracao = 0;
        int contador = 0;

        while ((geracao < qtdeGeracoes) && (fitness != maiorFitness)) {
            melhorIndividuo = proximaGeracao(populacao, probabilidadeMutacao, melhorFitness);
            fitness = avaliacaoIndividuo(melhorIndividuo);
            if (fitness > melhorFitness) {
                probabilidadeMutacao = 0.10;
                contador = 0;
                melhorFitness = fitness;
            } else {
                contador++;
                if (contador > 1000) {
                    probabilidadeMutacao = 0.30;
                } else if (contador > 2000) {
                    probabilidadeMutacao = 0.50;
                } else if (contador > 5000) {
                    populacao.clear();
                     populacao = geraPopulacaoInicial(tamanhoIndividuo);
                    probabilidadeMutacao = 0.10;
                    melhorFitness = -1;
                }
            }
            geracao = geracao + 1;
        }

        if (fitness == maiorFitness) {
            solucoes = solucoes + 1;
            geracaoSolucao = geracao;
            println("Solucao encontrada em " + geracao + " geracoes");
            println("Solucao =" + melhorIndividuo);
            println("Fitness =" + avaliacaoIndividuo(melhorIndividuo));
        } else {
            System.out.println("Solucao nao encontrada apos " + geracao + " geracoes");
            System.out.println("Melhor Individuo =" + melhorIndividuo);
            System.out.println("Fitness =" + avaliacaoIndividuo(melhorIndividuo));
        }
        println("Solucao");
        imprime(melhorIndividuo);
    }

    public static void main(String[] args) {

        //Especifica a quantidade de rainhas serem testadas
        int qtdeRainhasTeste[] = {4};
        //Especifica o numero de vezes a se realizado com cada qtde de rainhas
        int repeticoesTeste[] = {1};

        //Declara o tempo total do teste
        double tempoTeste = 0;

        //Realiza os testes para as quantidades das rainhas especificadas no vetor
        for (int qtdeR = 0; qtdeR < qtdeRainhasTeste.length; qtdeR++) {

            int qRainha = qtdeRainhasTeste[qtdeR];

            //Realiza a repeticao do teste para a quantidade de rainhas    
            for (int qtdeT = 0; qtdeT < repeticoesTeste.length; qtdeT++) {

                println("Execuntando com " + qRainha + " rainhas por " + repeticoesTeste[qtdeT] + " vezes.");

                //Zera o numero de solucoes
                solucoes = 0;

                //Declara o tempo final da repeticao
                long tempoFinal = 0;

                //Repete o teste para as vezes especificadas no vetor
                for (int qtdeV = 0; qtdeV < repeticoesTeste[qtdeT]; qtdeV++) {

                    //Executa o gc antes de cada teste
                    System.gc();

                    //Zera o tempo de inicio da vez
                    long tempo = 0;

                    //Pega o tempo corrente
                    tempo = System.currentTimeMillis();

                    //Parametros do algoritmo genetico
                    //Quantidade de geracoes
                    int qtdeGeracoes = 300000;
                    //Tamanho da populacao
                    int tamanhoIndividuo = 10;
                    //Probabilidade de mutacao dos individuos
                    double probabilidadeMutacao = 0.15;
                    //Gerar individuos com repeticao na populacao inicial
                    boolean repeticao = true;
                    //Incializa o gerador de numeros aleatorios
                    randomico = new Random();
                    //Executa a solucao do algoritmo                    
                    algoritmoGenetico(qRainha, qtdeGeracoes, tamanhoIndividuo, probabilidadeMutacao);

                    //Pega o tempo final do processamento da vez
                    tempo = System.currentTimeMillis() - tempo;
                    //Acumula o tempo do teste ao tempo final
                    tempoFinal = tempoFinal + tempo;
                }
                //Calcula a media do tempo
                double mediaTempo = tempoFinal / repeticoesTeste[qtdeT];
                System.out.println("O tempo medio para " + qRainha + " rainhas, executando " + repeticoesTeste[qtdeT] + " é vezes é " + mediaTempo + " milisegundos com " + solucoes + " solucoes em " + repeticoesTeste[qtdeT] + " repeticoes");
                tempoTeste = tempoTeste + mediaTempo;
            }
        }
        System.out.println("O tempo total do teste e " + tempoTeste + " milisegundos.");
    }

}
