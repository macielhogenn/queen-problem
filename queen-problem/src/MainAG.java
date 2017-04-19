
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Ricardo Alberto Harari - ricardo.harari@gmail.com
 *
 */
public class MainAG {

    private static final int MAX_FITNESS = 8;
    private static Random randomico = new Random();
    // parametros de stress test
    private static boolean ocorreuFalha = false;
    private static int totalMaxIteracoes = 0;
    private static int totalIteracoes = 0;

    private static boolean disablePrint = false;

    public static void main(String[] args) {

        go();

    }

    /**
     *
     * inicio do processo
     *
     */
    private static void go() {

        long startTime = System.currentTimeMillis();
        println("Populacao Inicial");
        // gerar a populacao inicial com 10 individuos
        Set populacao = new HashSet();
        carregarPopulacao(populacao);
        println("------------------------------");
        double probabilidadeMutacao = 0.15;
        int maxIteracoes = 300000;
        String melhorIndividuo = null;
        boolean achouSolucao = false;
        int bestFitness = 0;
        int i = 0;
        int counter = 0;

        for (i = 0; i < maxIteracoes; i++) {
            melhorIndividuo = geneticAlgorithm(populacao, probabilidadeMutacao, bestFitness);
            int ftness = fitness(melhorIndividuo);
            if (ftness > bestFitness) {
                probabilidadeMutacao = 0.10;
                counter = 0;
                println("novo fitness = " + ftness);
                bestFitness = ftness;
                if (ftness == MAX_FITNESS) {
                    achouSolucao = true;
                    break;
                }
            } else {
                counter++;
                if (counter > 1000) {
                    probabilidadeMutacao = 0.30;
                } else if (counter > 2000) {
                    probabilidadeMutacao = 0.50;
                } else if (counter > 5000) {
                    populacao.clear();
                    carregarPopulacao(populacao);
                    probabilidadeMutacao = 0.10;
                    bestFitness = -1;
                }
            }
        }

        println("------------------------------");
        if (achouSolucao) {
            println("Solucao encontrada em " + i + " iteracoes");
            println("Solucao =" + melhorIndividuo);
            println("Fitness =" + fitness(melhorIndividuo));
        } else {
            System.out.println("Solucao nao encontrada após " + i + " iteracoes");
            System.out.println("Melhor Individuo =" + melhorIndividuo);
            System.out.println("Fitness =" + fitness(melhorIndividuo));
            ocorreuFalha = true;
        }
        totalIteracoes += i;
        if (i > totalMaxIteracoes) {
            maxIteracoes = i;
        }
        println("------------------------------");
        mostrarTabuleiro(melhorIndividuo);
        println("Elapsed time = " + (System.currentTimeMillis() - startTime) + "ms");
    }

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
    private static void mostrarTabuleiro(String melhorIndividuo) {
        println("|---+---+---+---+---+---+---+---|");
        for (int i = 0; i < 8; i++) {
            print("|");
            int posicaoRainha = Integer.parseInt(melhorIndividuo.substring(i, i + 1)) - 1;
            for (int j = 0; j < 8; j++) {
                if (posicaoRainha == j) {
                    print(" x |");
                } else {
                    print("   |");
                }
            }
            println("\r\n|---+---+---+---+---+---+---+---|");
        }
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
                int ffitness = fitness(filho);
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
            f[i] = fitness((String) pais[i]);
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
            f[i] = fitness((String) pop[i]);
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
        int mc = randomico.nextInt(filho.length()) + 1;
        filho = filho.substring(0, mp) + mc + (mp + 1 == filho.length() ? "" : filho.substring(mp + 1));
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
        while (ret.length() < 8) {
            ret += (randomico.nextInt(n) + 1);
        }
        return ret;
    }

    /**
     *
     * função fitness, retorna a quantidade de rainhas a salvo.
     *
     *
     *
     * @param individuo
     *
     * @return
     *
     */
    public static int fitness(String individuo) {
        int ret = 0;
        int[][] tabuleiro = new int[8][8];
        // primeiro representamos o tabuleiro com 0 e 1
        for (int i = 0; i < 8; i++) {
            int posicaoRainha = Integer.parseInt(individuo.substring(i, i + 1)) - 1;
            for (int j = 0; j < 8; j++) {
                tabuleiro[i][j] = posicaoRainha == j ? 1 : 0;
            }

        }
        // agora verificamos quantas rainhas estao a salvo, este será o nosso
        // retorno da função fitness
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tabuleiro[i][j] == 1) {
                    if (!temAtacante(tabuleiro, i, j)) {
                        ret++;
                    }
                }
            }
        }
        return ret;
    }

    /**
     *
     * verifica se existe uma rainha ameaçando a posicao i,j existindo retorna
     *
     * true caso contrário retorna false
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
    private static boolean temAtacante(int[][] tabuleiro, int i, int j) {
        // verificar na horizontal
        for (int k = 0; k < 8; k++) {
            if (k != i && tabuleiro[k][j] == 1) {
                return true;
            }
        }

        // verificar na vertical
        for (int k = 0; k < 8; k++) {
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
        while (i0 < 8 && j0 < 8) {
            if (tabuleiro[i0][j0] == 1) {
                return true;
            }
            i0++;
            j0++;
        }

        // verificar na diagonal3
        i0 = i + 1;
        j0 = j - 1;
        while (i0 < 8 && j0 >= 0) {
            if (tabuleiro[i0][j0] == 1) {
                return true;
            }
            i0++;
            j0--;
        }
        // verificar na diagonal4
        i0 = i - 1;
        j0 = j + 1;
        while (i0 >= 0 && j0 < 8) {
            if (tabuleiro[i0][j0] == 1) {
                return true;
            }
            i0--;
            j0++;
        }
        return false; // esta a salvo
    }
}
