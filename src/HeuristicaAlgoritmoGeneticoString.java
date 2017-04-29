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
    private static Random randomico = new Random();

    private static boolean disablePrint = true;


    /**
     *
     * @param string
     *
     */
    private static void println(String string) {
        if (!disablePrint) {
            System.out.println(string);
        }
    }

    /**
     *
     * @param populacao
     *
     */
    private static void carregarPopulacao(Set populacao) {
        while (populacao.size() < 10) {
            String individuo = gerarIndividuo(8);
            println("individuo=" + individuo);
            populacao.add(individuo);
        }
    }

    /**
     *
     * mostrar o tabuleiro graficamente
     *
     *
     *
     * @param melhorIndividuo
     *
     * @return
     *
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
     *
     * @param string
     *
     */
    private static void print(String string) {
        if (!disablePrint) {
            System.out.print(string);
        }
    }

    /**
     *
     * logica GA mantendo os melhores na populacao retorna o melhor individuo
     *
     *
     *
     * @param populacao
     *
     * @param probabilidadeMutacao
     *
     */
    private static String geneticAlgorithm(Set populacao, double probabilidadeMutacao, int fitnessAtual) {
        String melhor = null;
        Set filhos = new HashSet();
        int tamanhoPopulacao = populacao.size();
        while (filhos.size() < tamanhoPopulacao) {
            String p1 = selecionarAleatorio(populacao, "");
            String p2 = selecionarAleatorio(populacao, p1);
            String filho = crossover(p1, p2);
            if (randomico.nextDouble() <= probabilidadeMutacao) {
                int ffitness = avaliacaoIndividuo(filho);
                if (ffitness <= fitnessAtual) {
                    filho = mutate(filho);
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
     *
     * @param filho
     *
     * @return
     *
     */
    private static String mutate(String filho) {        
        int mp = randomico.nextInt(filho.length());        
        int mc = randomico.nextInt(filho.length());        
        String parte1 = filho.substring(0, mp);
        String parte2 = "";
        if (mp + 1 == filho.length())
             parte2 = "";
         else 
             parte2 = filho.substring(mp + 1);
        filho = parte1 + mc + parte2;                
        return filho;
    }

    /**
     *
     * crossover
     *
     *
     *
     * @param p1
     *
     * @param p2
     *
     * @return
     *
     */
    private static String crossover(String p1, String p2) {
        int i = randomico.nextInt(p1.length());
        String ret = p1.substring(0, i) + p2.substring(i);
        return ret;
    }

    /**
     *
     * seleciona um individuo da populacao aleatoriamente
     *
     *
     *
     * @param populacao
     *
     * @return
     *
     */
    private static String selecionarAleatorio(Set populacao, String px) {
        String pn = px;
        
        Object[] tmp = populacao.toArray();

        while (pn.equals(px)) {
            int i = randomico.nextInt((populacao.size()));
            
            pn = (String) tmp[i];
        }
        return pn;
    }

    /**
     *
     * gerar um individuo com n posicoes
     *
     *
     *
     * @param n
     *
     * @return
     *
     */
    private static String gerarIndividuo(int n) {
        String ret = "";
        while (ret.length() < qtdeRainha) {
            ret += (randomico.nextInt(n));
        }
        return ret;
    }

    /**
     *
     * fun��o fitness, retorna a quantidade de rainhas a salvo.
     *
     *
     *
     * @param individuo
     *
     * @return
     *
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
     *
     * verifica se existe uma rainha amea�ando a posicao i,j existindo retorna
     *
     * true caso contr�rio retorna false
     *
     *
     *
     * @param tabuleiro
     *
     * @param i
     *
     * @param j
     *
     * @return
     *
     */
    private static boolean valida(int[][] tabuleiro, int i, int j) {
        // verificar na horizontal
        for (int k = 0; k < qtdeRainha; k++) {
            if (k != i && tabuleiro[k][j] == 1) {
                return true;
            }
        }

        // verificar na vertical
        for (int k = 0; k < qtdeRainha; k++) {
            if (k != j && tabuleiro[i][k] == 1) {
                return true;
            }
        }

        // verificar na diagonal1
        int i0 = i - 1;
        int j0 = j - 1;
        while (i0 >= 0 && j0 >= 0) {
            if (tabuleiro[i0][j0] == 1) {
                return true;
            }
            i0--;
            j0--;
        }

        // verificar na diagonal2
        i0 = i + 1;
        j0 = j + 1;
        while (i0 < qtdeRainha && j0 < qtdeRainha) {
            if (tabuleiro[i0][j0] == 1) {
                return true;
            }
            i0++;
            j0++;
        }

        // verificar na diagonal3
        i0 = i + 1;
        j0 = j - 1;
        while (i0 < qtdeRainha && j0 >= 0) {
            if (tabuleiro[i0][j0] == 1) {
                return true;
            }
            i0++;
            j0--;
        }
        // verificar na diagonal4
        i0 = i - 1;
        j0 = j + 1;
        while (i0 >= 0 && j0 < qtdeRainha) {
            if (tabuleiro[i0][j0] == 1) {
                return true;
            }
            i0--;
            j0++;
        }
        return false; // esta a salvo
    }
    
     private static void executarAlgoritmoGenetico(int qRainha, int qtdeGeracoes) {
        //Define a quantidade rainhas        
        qtdeRainha = qRainha;
        //Define o maior fitness pela quantidade rainhas 
        maiorFitness = qtdeRainha;

        // gerar a populacao inicial com 10 individuos
        Set populacao = new HashSet();
        carregarPopulacao(populacao);
        
        double probabilidadeMutacao = 0.15;        
        String melhorIndividuo = null;        
        int melhorFitness = 0;
        int fitness = 0;
        int geracao = 0;
        int contador = 0;

        while ((geracao < qtdeGeracoes) && (fitness != maiorFitness)) {
            melhorIndividuo = geneticAlgorithm(populacao, probabilidadeMutacao, melhorFitness);
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
                    carregarPopulacao(populacao);
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
        int qtdeRainhasTeste[] = {4,6,8};
        //Especifica o numero de vezes a se realizado com cada qtde de rainhas
        int repeticoesTeste[] = {5,10};

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
                    int tamanhoPopulacaoInicial = 10;
                    //Probabilidade de mutacao dos individuos
                    double probabilidadeMutacao = 0.15;
                    //Gerar individuos com repeticao na populacao inicial
                    boolean repeticao = true;
                    //Executa a solucao do algoritmo
                    //executarAlgoritmoGenetico(qRainha, qtdeGeracoes, tamanhoPopulacaoInicial,probabilidadeMutacao, repeticao);
                    executarAlgoritmoGenetico(qRainha,qtdeGeracoes);
                    
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