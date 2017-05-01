
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Osmar e Maciel
 *
 */
public class HeuristicaAlgoritmoGeneticoVetor1 {

    /**
     * Atributo do numero de solucoes encontradas ao final do algoritmo
     */
    private static int solucoes;
    private static int geracaoSolucao;
    private static int[] individuoEncontrado;
    private static int maiorFitness;

    private static int qtdeRainha;
    private static Random randomico = new Random();

    //Habilita ou desabilida a saida dos dados de impressao
    private static boolean desabilidarImpressao = false;

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
     * Carrega a populacao inicial de individuos do AG
     *
     * @param populacao Conjunto de individuos da populacao
     * @param tamanhoIndividuo quantidade de individuos da populacao inicial
     */
    private static Set geraPopulacaoInicial(int tamanhoIndividuo) {
        Set populacao = new HashSet();
        while (populacao.size() < tamanhoIndividuo) {
            //Gera um individuo
            int[] individuo = gerarIndividuo();
            println("individuo=" + vetorToString(individuo) + "=" + funcaoFitness(individuo) + "=" + verificaSolucao(individuo) + "=" + getNumeroRainhas(individuo));
            //Adiciona o novo individuo a populacao
            populacao.add(individuo);
        }
        return populacao;
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
     * Gera a proxima geracao de individuos. Retorna a nova populacao.
     *
     * @param populacao
     * @param probabilidadeMutacao
     */
    private static Set proximaGeracao(Set populacao, double probabilidadeMutacao) {

        //Conjunto de novos individuos da proxima geracao
        Set novaPopulacao = new HashSet();
        //Gera a nova populacao com base na populacao anteior        
        int tamanhoPopulacao = populacao.size();

        //Gera os novos invidiuos para a nova geracao
        while (novaPopulacao.size() < tamanhoPopulacao) {
            //Seleciona o primeiro individuo para realizar o crossover
            int individuo1[] = selecionarIndividuo(populacao, null);
            //Seleciona o segundo indiviuo diferente do anterior para realiar o crossover
            int individuo2[] = selecionarIndividuo(populacao, individuo1);
            //Gera o crossover entre individuo1 e individuo2 para gerar u novo individuo do cruzamento
            int novoIndividuo[] = crossover(individuo1, individuo2);
            //Verifica a probabilidade de realizar mutacao no filho
            if (randomico.nextDouble() <= probabilidadeMutacao) {
                //So realiza a mutacao se o ffitness for pior que o atual
                novoIndividuo = mutacao(novoIndividuo);
            }
            //Adiciona o filho ao conjunto
            novaPopulacao.add(novoIndividuo);
        }
        return novaPopulacao;
    }

    public static int[] localizarMelhorIndividuo(Set populacao) {
        //Guarda o melhor individuo
        int[] melhorIndividuo = null;
        //Procura o melhor individuo na nova populacao
        Object[] vetorPopulacao = populacao.toArray();
        //Guarda o melhor fitness
        int melhorFitness = -1;
        for (int i = 0; i < vetorPopulacao.length; i++) {
            int fitness = funcaoFitness((int[]) vetorPopulacao[i]);
            if (fitness > melhorFitness) {
                melhorFitness = fitness;
                melhorIndividuo = (int[]) vetorPopulacao[i];
            }
        }
        return melhorIndividuo;
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
     * Realiza o crossover entre dois individuos da populacao
     *
     * @param individuo1 Individuo que fornece a 1a parte dos genes para o novo
     * individuo
     * @param individuo2 Individuo que fornece a 2a parte dos genes para o novo
     * individuo
     * @return um individuo resultante do crossover
     */
    private static int[] crossover(int[] individuo1, int[] individuo2) {
        //Cria o filho resultante do crossover
        int[] novo = new int[individuo1.length];
        //Seleciona o ponto de crossover
        int posicao = randomico.nextInt(individuo1.length);
        //Copia os genes do inicio ate a posicao do individuo1 para o filho
        for (int i = 0; i < posicao; i++) {
            novo[i] = individuo1[i];
        }
        //Copia os genes da posicao ate o final do individuo2 para o filho
        for (int i = posicao; i < individuo2.length; i++) {
            novo[i] = individuo2[i];
        }
        return novo;
    }

    /**
     * Seleciona um individuo da populacao aleatoriamente
     *
     * @param populacao
     * @param individuoBase para ser utilizado para nao selecionar
     * @return Um individuo selecionado aleatoramente da populacao
     */
    private static int[] selecionarIndividuo1(Set populacao, int[] individuoBase) {
        //Cria um vetor para receber um novo elemento
        int[] individuoSelecionado = new int[qtdeRainha];
        //Transforma a populacao em um vetor de objetos

        Object[] pais = populacao.toArray();
        int[] fitness = new int[pais.length];
        //Um numero baixo para o menor fitness

        for (int i = 0; i < pais.length; i++) {
            fitness[i] = funcaoFitness((int[]) pais[i]);
        }

        // Normaliza
        fitness = normalizar(fitness);

        int prob = randomico.nextInt();
        int totalSoFar = 0;
        for (int i = 0; i < fitness.length; i++) {
            totalSoFar = totalSoFar + fitness[i];
            if (prob <= totalSoFar) {
                individuoSelecionado = (int[]) pais[i];
                break;
            }
        }
        return individuoSelecionado;
    }

    private static int[] selecionarIndividuo(Set populacao, int[] individuoBase) {
        //Cria um vetor para receber um novo elemento
        int[] individuoSelecionado = new int[qtdeRainha];
        //Transforma a populacao em um vetor de objetos
        Object[] tmp = populacao.toArray();
        //Enquanto for igual a individuoBase seleciona outro individuo
        while (java.util.Arrays.equals(individuoSelecionado, individuoBase)) {
            //Seleciona uma posicao da populacao
            int i = randomico.nextInt((populacao.size()));
            //Recupera o individuo sorteado
            individuoSelecionado = (int[]) tmp[i];
        }
        return individuoSelecionado;
    }

    /**
     * Gera um individuo com n posicoes de forma aleatoria de acordo com a
     * quantidade de rainhas. Gera rainhas repetidas
     *
     * @return um individuo da populacao com repeticao de rainha.
     */
    private static int[] gerarIndividuo() {
        //Inicializa o vetor de retorno
        int[] ret = new int[qtdeRainha];
        int i = 0;
        //Gera os genes de individuo de acordo com o tamanho do gene
        while (i < qtdeRainha) {
            //Gera um uma rainha aleatoria
            ret[i] = new Random().nextInt(qtdeRainha);
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
    public static int funcaoFitness1(int[] individuo) {
        int fitness = 0;
        int[][] tabuleiro = new int[qtdeRainha][qtdeRainha];

        // Monta o tabuleiro com 0 em posicao vazia e 1 com a rainha
        for (int i = 0; i < qtdeRainha; i++) {
            int posicaoRainha = individuo[i];
            for (int j = 0; j < qtdeRainha; j++) {
                if (posicaoRainha == j) {
                    tabuleiro[i][j] = 1;
                } else {
                    tabuleiro[i][j] = 0;
                }
            }
        }

        for (int i = 0; i < qtdeRainha - 1; i++) {
            for (int j = i + 1; j < qtdeRainha; j++) {
                boolean semAtaque = true;
                if (tabuleiro[i][j] == 1) {
                    if (valida(tabuleiro, i, j) == true) {
                        semAtaque = false;
                    }
                }
                if (semAtaque == true) {
                    fitness = fitness + 1;
                }
            }
        }
        return fitness;
    }

    public static int funcaoFitness(int[] individuo) {
        int[][] tabuleiro = new int[qtdeRainha][qtdeRainha];

        // Monta o tabuleiro com 0 em posicao vazia e 1 com a rainha
        for (int i = 0; i < qtdeRainha; i++) {
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
        // A quantidade rainhas na salvas e o retorno da função fitness
        int ret = 0;
        for (int i = 0; i < qtdeRainha; i++) {
            for (int j = 0; j < qtdeRainha; j++) {
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
     * Retorna o numero de rainhas do tabuleiro
     * @param individuo Individuo com o vetor das rainhas
     * @return 
     */
    public static int getNumeroRainhas(int[] individuo) {
        int[][] tabuleiro = new int[qtdeRainha][qtdeRainha];

        // Monta o tabuleiro com 0 em posicao vazia e 1 com a rainha
        for (int i = 0; i < qtdeRainha; i++) {
            int posicaoRainha = individuo[i];
            for (int j = 0; j < qtdeRainha; j++) {
                if (posicaoRainha == j) {
                    tabuleiro[i][j] = 1;
                } else {
                    tabuleiro[i][j] = 0;
                }
            }
        }
        
        return getNumeroRainhasTabuleiro(tabuleiro);
    }

     /**
     * Retorna o numero de rainhas do tabuleiro
     * @param tabuleiro Um tabuleiro preenchido com as rainhas
     * @return 
     */
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
     * Verifica se o individuo e uma solucao do problema
     * 
     * @param individuo um solucao a ser verifica
     * @return true se o individuo e uma solucao do problema
     */    
    public static boolean verificaSolucao(int[] individuo) {
        int[][] tabuleiro = new int[qtdeRainha][qtdeRainha];

        // Monta o tabuleiro com 0 em posicao vazia e 1 com a rainha
        for (int i = 0; i < qtdeRainha; i++) {
            int posicaoRainha = individuo[i];
            for (int j = 0; j < qtdeRainha; j++) {
                if (posicaoRainha == j) {
                    tabuleiro[i][j] = 1;
                } else {
                    tabuleiro[i][j] = 0;
                }
            }
        }
        
        //Verifica se todas as rainhas estao em posicoes validas
        int cont = 0;
        for (int i = 0; i < qtdeRainha; i++) {
            for (int j = 0; j < qtdeRainha; j++) {
                //Verifica se a posicao esta ocupada
                if (tabuleiro[i][j] == 1) {
                    if (valida(tabuleiro, i, j) == true) {
                        cont = cont + 1;
                    }
                }
            }
        }
        boolean ret = false;
        if (cont == 0) {
            ret = true;
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
        for (int i = 0; i < qtdeRainha; i++) {
            if (i != linha && tabuleiro[i][coluna] == 1) {
                return true;
            }
        }

        // verificar na coluna da rainha
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

        // Verificar a diagonal3
        i = linha + 1;
        j = coluna - 1;
        while (i < qtdeRainha && j >= 0) {
            if (tabuleiro[i][j] == 1) {
                return true;
            }
            i++;
            j--;
        }
        // Verifica a diagonal4
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

    public static int[] normalizar(int[] probDist) {
        int len = probDist.length;
        int total = 0;
        for (int d : probDist) {
            total = total + d;
        }

        int[] normalized = new int[len];
        if (total != 0) {
            for (int i = 0; i < len; i++) {
                normalized[i] = probDist[i] / total;
            }
        }
        return normalized;
    }

    /**
     * Executa as geracoes do algoritmo genetico
     *
     * @param qRainha
     * @param qtdeGeracoes
     * @param tamanhoIndividuo
     * @param probabilidadeMutacao
     */
    public static void algoritmoGenetico(int qRainha, int qtdeGeracoes, int tamanhoIndividuo, double probabilidadeMutacao) {

        //Define a quantidade rainhas        
        qtdeRainha = qRainha;
        //Define o maior fitness pela quantidade rainhas 
        maiorFitness = qtdeRainha;

        // Gera a populacao inicial 
        Set populacao = geraPopulacaoInicial(tamanhoIndividuo);
        // Guarda o melhor Individuo
        int[] melhorIndividuo = null;
        // Guarda o fitness do melhor Individuo
        int fitness = 0;
        //Conta o numero de geracoes
        int geracao = 0;

        do {
            //Gera a proxima populacao
            populacao = proximaGeracao(populacao, probabilidadeMutacao);

            //Retorna o melhor individuo da populacao
            melhorIndividuo = localizarMelhorIndividuo(populacao);

            // Calcula o fitness do melhor individuo
            fitness = funcaoFitness(melhorIndividuo);

            //Avanca para proxima geracao
            geracao = geracao + 1;
            // Ate que a geracao atinja o maxiou ou alcance o maior fitness    

            //}  while ((geracao < qtdeGeracoes) && (fitness != maiorFitness));
        } while ((geracao < qtdeGeracoes) && (verificaSolucao(melhorIndividuo) == false));

        //System.out.println("geracao:" + geracao);
        //System.out.println("funcaoFitness:" + funcaoFitness(melhorIndividuo));        
        //System.out.println("fitness:" + fitness);
        //System.out.println("maiorFitness:" + maiorFitness);
        //Estatisticas da execucao
        if (verificaSolucao(melhorIndividuo)) {
            solucoes = solucoes + 1;
            geracaoSolucao = geracao;
            individuoEncontrado = melhorIndividuo;
            println("Solucao encontrada em " + geracao + " geracoes");
            println("Melhor Individuo = " + vetorToString(melhorIndividuo));
            println("Objetivo= " + verificaSolucao(melhorIndividuo));
            println("Numero Rainhas= " + getNumeroRainhas(melhorIndividuo));
            println("Fitness = " + funcaoFitness(melhorIndividuo));
        } else {
            println("Solucao nao encontrada após " + geracao + " geracoes");
            println("Melhor Individuo = " + vetorToString(melhorIndividuo));
            println("Objetivo= " + verificaSolucao(melhorIndividuo));
            println("Numero Rainhas= " + getNumeroRainhas(melhorIndividuo));
            println("Fitness = " + funcaoFitness(melhorIndividuo));
        }
        println("Solucao:");
        imprime(melhorIndividuo);
    }

    public static void main(String[] args) {

        //Especifica a quantidade de rainhas serem testadas
        int qtdeRainhasTeste[] = {4, 5, 6};
        //Especifica o numero de vezes a se realizado com cada qtde de rainhas
        int repeticoesTeste[] = {1};

        //Parametros do algoritmo genetico
        //Quantidade de geracoes
        int qtdeGeracoes = 300000;
        //Tamanho da populacao
        int tamanhoIndividuo = 30;
        //Probabilidade de mutacao dos individuos
        double probabilidadeMutacao = 0.15;

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

                    //Pega o tempo corrente
                    long tempo = System.currentTimeMillis();

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
    
    
     public static boolean eEstadoObjetivo(int[] individuo) {
        if (paresAtacados(individuo) == 0) {
            return true;
        } else {
            return false;
        }
    }

    //linha
    public static int ataquesHorizontal(int[][] tabuleiro, int linha, int coluna) {
        int ataque = 0;
        // verificar na linha da rainha
        for (int i = 0; i < qtdeRainha; i++) {
            if (i != linha) {
                //Posicao ocupada    
                if (tabuleiro[i][coluna] == 1) {
                    ataque = ataque + 1;
                }
            }
        }
        return ataque;
    }

    public static int ataques(int[][] tabuleiro, int linha, int coluna) {
        int ret = 0;
        ret = ret + ataquesVertical(tabuleiro, linha, coluna);
        ret = ret + ataquesHorizontal(tabuleiro, linha, coluna);
        ret = ret + ataquesDiagonal(tabuleiro, linha, coluna);
        return ret;
    }

    //coluna    
    public static int ataquesVertical(int[][] tabuleiro, int linha, int coluna) {
        int ataque = 0;
        // verificar na linha da rainha
        for (int j = 0; j < qtdeRainha; j++) {
            if (j != coluna) {
                //Posicao ocupada        
                if (tabuleiro[linha][j] == 1) {
                    ataque = ataque + 1;
                }
            }
        }

        return ataque;
    }

    //diagonal
    public static int ataquesDiagonal(int[][] tabuleiro, int linha, int coluna) {
        int ataque = 0;
        // verificar na diagonal1
        int i = linha - 1;
        int j = coluna - 1;
        while (i >= 0 && j >= 0) {
            if (tabuleiro[i][j] == 1) {
                ataque = ataque + 1;
            }
            i--;
            j--;
        }

        // verificar na diagonal2
        i = linha + 1;
        j = coluna + 1;
        while (i < qtdeRainha && j < qtdeRainha) {
            if (tabuleiro[i][j] == 1) {
                ataque = ataque + 1;
            }
            i++;
            j++;
        }

        // Verificar a diagonal3
        i = linha + 1;
        j = coluna - 1;
        while (i < qtdeRainha && j >= 0) {
            if (tabuleiro[i][j] == 1) {
                ataque = ataque + 1;
            }
            i++;
            j--;
        }
        // Verifica a diagonal4
        i = linha - 1;
        j = coluna + 1;
        while (i >= 0 && j < qtdeRainha) {
            if (tabuleiro[i][j] == 1) {
                ataque = ataque + 1;
            }
            i--;
            j++;
        }
        return ataque;
    }

    public static int paresAtacados(int[] individuo) {
        int ret = 0;
        int[][] tabuleiro = new int[qtdeRainha][qtdeRainha];

        // Monta o tabuleiro com 0 em posicao vazia e 1 com a rainha
        for (int i = 0; i < qtdeRainha; i++) {
            int posicaoRainha = individuo[i];
            for (int j = 0; j < qtdeRainha; j++) {
                if (posicaoRainha == j) {
                    tabuleiro[i][j] = 1;
                } else {
                    tabuleiro[i][j] = 0;
                }
            }
        }
        int soma = 0;
        for (int i = 0; i < qtdeRainha - 1; i++) {
            for (int j = i + 1; j < qtdeRainha; j++) {
                if (tabuleiro[i][j] == 1) {
                    soma = soma + ataques(tabuleiro, i, j);                    
                }
            }
        }
System.out.println("Total ataques:"+ soma);
        return soma;
    }

}
