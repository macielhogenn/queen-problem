
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Osmar e Maciel
 *
 */
public class HeuristicaAlgoritmoGeneticoVetor {

    /**
     * Atributo do numero de solucoes encontradas ao final do algoritmo
     */
    private static int solucoes;
    private static int geracaoSolucao;
    private static int[] individuoEncontrado;

    private static int qtdeRainha;
    private static int maiorFitness;
    private static Random randomico;

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
     * Carreca a populacao inicial de individuos do AG
     *
     * @param populacao Conjunto de individuos da populacao
     * @param tamanhoIndividuo quantidade de individuos da populacao
     * inicial
     */
    private static Set geraPopulacaoInicial(int tamanhoIndividuo, boolean repeticao) {
        Set populacao = new HashSet();
        while (populacao.size() < tamanhoIndividuo) {
            //Gera um individuo
            int[] individuo = gerarIndividuo();
            println("individuo=" + vetorToString(individuo));
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
     * Logica do Algoritmo Genetico mantendo os melhores na populacao. Retorna o
     * melhor individuo da populacao
     *
     * @param populacao
     * @param probabilidadeMutacao
     */
    private static int[] proximaGeracao(Set populacao, double probabilidadeMutacao, int fitnessAtual) {
        
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
                int ffitness = avaliacaoIndividuo(novoIndividuo);
                if (ffitness <= fitnessAtual) {
                    novoIndividuo = mutacao(novoIndividuo);
                }
            }
            //Adiciona o filho ao conjunto
            novaPopulacao.add(novoIndividuo);
        }

        // Adicionar dois dos melhores pais a populacao
        Object[] pais = populacao.toArray();
        int[] fitness = new int[pais.length];
        //Um numero baixo para o menor fitness
        int melhorF = -1;
        for (int i = 0; i < pais.length; i++) {
            fitness[i] = avaliacaoIndividuo((int[]) pais[i]);
            //Localiza o melhor fitness
            if (melhorF < fitness[i]) {
                melhorF = fitness[i];
            }
        }
        populacao.clear();
        while (populacao.size() < 2) {
            for (int i = 0; i < fitness.length; i++) {
                //Adiciona os melhores a populacao
                if (fitness[i] == melhorF) {
                    populacao.add((int[]) pais[i]);
                }
                //Se adicionou 2 e sai
                if (populacao.size() == 2) {
                    break;
                }
            }
            //Decrementa o fitness para pegar o proximo
            melhorF--;
        }        
        novaPopulacao.addAll(populacao);
                
         //Guarda o melhor individuo
        int[] melhorIndividuo = null;
        //Procura o melhor individuo na nova populacao
        Object[] pop = novaPopulacao.toArray();
        fitness = new int[pop.length];
        melhorF = -1;
        for (int i = 0; i < fitness.length; i++) {
            fitness[i] = avaliacaoIndividuo((int[]) pop[i]);
            if (melhorF < fitness[i]) {
                melhorF = fitness[i];
                melhorIndividuo = (int[]) pop[i];
            }
        }
        populacao.clear();
        while (populacao.size() < tamanhoPopulacao) {
            if (melhorF < 0) {
                break;
            }
            for (int i = 0; i < fitness.length; i++) {
                if (fitness[i] == melhorF && populacao.size() < tamanhoPopulacao) {
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
     * Gera um individuo com n posicoes de de forma aleatoria de acordo com o tipo.
     *
     * @param repetir Indica se deve ser gerado com repeticao ou nao os individuos
     * @return um individuo da populacao com repeticao de rainha.
     */
    
    private static int[] gerarIndividuo1(boolean repeticao) {
        if (repeticao==true) {
            return gerarIndividuo();
        } else {
            return gerarIndividuoSemRepeticao();
        }           
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
            ret[i] = randomico.nextInt(qtdeRainha);
            i = i + 1;
        }
        return ret;
    }

    /**
     * Gera um individuo com n posicoes de forma aleatoria de acordo com a
     * quantidade de rainhas. Usa uma roleta para evitar individuos reptidos
     *
     * @return um individuo da populacao sem repeticao de rainha.
     */
    private static int[] gerarIndividuoSemRepeticao() {
        //Inicializa o vetor de retorno
        int[] ret = new int[qtdeRainha];

        //Inicializa o vetor da roleta
        int[] roleta = new int[qtdeRainha];
        //Controla o tamanho da roleta
        int tamanhoRoleta = qtdeRainha;
        //Coloca as rainhas na roleta para serem sortedas
        for (int i = 0; i < qtdeRainha; i++) {
            //Soma 1 para que as rainhas comecem com 1
            roleta[i] = i;
        }

        int i = 0;
        //Gera os genes de individuo de acordo com o tamanho do gene
        while (i < qtdeRainha) {
            //Gera um numero aleatorio para selecionar um elemento da roleta
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
     * Função de avaliacao do individuo, retorna a quantidade de rainhas a salvo.
     *
     * @param individuo
     * @return a quantidade de rainhas salvas no individuo
     */
    public static int avaliacaoIndividuo(int[] individuo) {
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
     * @param tamanhoIndividuo
     * @param probabilidadeMutacao
     * @param repeticao 
     */
    public static void algoritmoGenetico(int qRainha, int qtdeGeracoes, int tamanhoIndividuo, double probabilidadeMutacao, boolean repeticao) {

        //Define a quantidade rainhas        
        qtdeRainha = qRainha;
        //Define o maior fitness pela quantidade rainhas 
        maiorFitness = qtdeRainha;

        // gerar a populacao inicial com 10 individuos
        Set populacao = geraPopulacaoInicial(tamanhoIndividuo, repeticao);
        
        int[] melhorIndividuo = null;
        int melhorFitness = 0;
        int fitness = 0;
        int geracao = 0;
        int contador = 0;
        
        do{            
            //Retorna o melhor individuo da populacao
            melhorIndividuo = proximaGeracao(populacao, probabilidadeMutacao, melhorFitness);
            //Retorna o fitness do melhor inidividuo da populacao
            fitness = avaliacaoIndividuo(melhorIndividuo);
            //Verifica se o fitness do melhor individuo é o melhor fitnesss
            if (fitness > melhorFitness) {
                probabilidadeMutacao = 0.10;
                contador = 0;
                melhorFitness = fitness;
            } else {
                contador++;
                //Se nao ocorrer aumento do fitness aumenta a probabilidade de mutacao
                if (contador > 1000) {
                    probabilidadeMutacao = 0.30;
                } else if (contador > 2000) {
                    probabilidadeMutacao = 0.50;
                } else if (contador > 5000) {
                    //Limpa populacao
                    populacao.clear();
                    //Carrega uma nova populacao
                    populacao = geraPopulacaoInicial(tamanhoIndividuo, repeticao);
                    probabilidadeMutacao = 0.10;
                    melhorFitness = -1;
                }
            }
            geracao = geracao + 1;
        // Ate que a geracao atinja o maxiou ou alcance o maior fitness    
        }  while ((geracao < qtdeGeracoes) && (fitness != maiorFitness));
        
        //Estatisticas da execucao
        if (fitness == maiorFitness) {
            solucoes = solucoes + 1;
            geracaoSolucao = geracao;
            individuoEncontrado = melhorIndividuo;
            println("Solucao encontrada em " + geracao + " geracoes");
            println("Solucao = " + vetorToString(melhorIndividuo));
            println("Fitness = " + avaliacaoIndividuo(melhorIndividuo));
        } else {
            println("Solucao nao encontrada após " + geracao + " geracoes");
            println("Melhor Individuo = " + vetorToString(melhorIndividuo));
            println("Fitness = " + avaliacaoIndividuo(melhorIndividuo));            
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
                    algoritmoGenetico(qRainha, qtdeGeracoes, tamanhoIndividuo,probabilidadeMutacao, repeticao);

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
