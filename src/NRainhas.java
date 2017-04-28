
import javax.swing.JOptionPane;

/**
 * *****************************************************
 *
 * <b> Solu��o do problema de N Rainhas </b>
 * Implementacao da solu��o do problema das oito rainhas. <br />
 * O programa utiliza o metodo de backtraking para buscar a solucao do problema,
 * movendo as rainhas a frente e retornando quando for impossivel achar a
 * solucao, funcionando como uma busca em profundidade pelas possiveis posicoes
 * onde as rainhas podem ser colocadas.
 *
 * @author Raul Joaquim C�mara dos Santos
 * @since Maio/2008
 *
 *******************************************************
 */
public class NRainhas {

    /**
     * Atributo do numero de rainhas que se deseja encontrar a solucao
     */
    protected int N;
    /**
     * Atributo do numero de solucoes encontradas ao final do algoritmo
     */
    protected int contador = 0;

    protected String print = "";

    /**
     * *************************************************
     * Construtor padrao da classe NRainhas (sem parametos)
     *
     **************************************************
     */
    public NRainhas() {
    }

    /**
     * *************************************************
     * Uma das propriedades da rainha e que nao pode haver outra rainha na linha
     * ou na coluna onde esta se encontra. Assim, na constru��o do algoritmo de
     * solu��o, n�o posicionaremos uma rainha em uma posi��o que esteja sendo
     * atacada. Esta mesma propriedade tamb�m vale para as diagonais em rela��o
     * as rainha j� posicionadas.
     *
     *
     * This method prints all the solutions possible for an <i>n X n</i> board.
     *
     ***************************************************
     */
    public void solucionar() {
        boolean[][] tabuleiro = new boolean[N][N];
        solucionar(tabuleiro, 0);
    }

    /**
     * *************************************************
     * A funcao recursiva do metodo solve(). <br />
     * - Cada instancia do metodo e' responsavel por posicionar uma rainhas na
     * linha 'row' (em todas as colunas possiveis). <br />
     * Se uma rainha pode ser posicionada, baseando-se nas suas propriedades,
     * sem que esta seja atacada pelas outras rainhas ja posicionadas, entao
     * esta rainha e' posicionada na posicao corrente e, recursivamente, as
     * rainhas seguintes sao posicionadas.
     *
     * @param tabuleiro o tabuleiro onde as rainhas serao inseridas
     * @param linha coordenada da linha corrente onde a rainhas devera ser
     * inserida
	 ***************************************************
     */
    private void solucionar(boolean[][] tabuleiro, int linha) {

        final int tamanhoTabuleiro = tabuleiro.length;

        for (int coluna = 0; coluna < tamanhoTabuleiro; coluna++) {

            // Coloca rainha na posicao [row][col]
            tabuleiro[linha][coluna] = true;

            // Se for possivel posiciona-la...
            if (valida(tabuleiro, linha, coluna)) {
                if (linha < tamanhoTabuleiro - 1) {
                    solucionar(tabuleiro, linha + 1);
                } else {
                    print = print + " Solucao numero " + (contador + 1) + ":" + System.getProperty("line.separator");
                    print(tabuleiro);
                    this.contador++;
                }
            }

            // Se nao for possivel, remove a rainha desta posicao
            tabuleiro[linha][coluna] = false;
        }
    }

    /**
     * *************************************************
     * Isto pode ser feito usando uma linha sobre o tabuleiro usando <i>k =
     * 1..row</i> como se pode ver abaixo:<br />
     *
     * Atencao:esta tabela nao sera bem visualizada no javadoc <br />
     * |-- col-k --| |---- col+k ----| <br />
     *
     * - +---+---+---+---+---+---+---+---+ <br />
     * | | | | | x | | | | x | <br />
     * | |---|---|---|---|---|---|---|---| <br />
     * | | x | | | x | | | x | | <br />
     * row-k |---|---|---|---|---|---|---|---| <br />
     * | | | x | | x | | x | | | <br />
     * | |---|---|---|---|---|---|---|---| <br />
     * | | | | x | x | x | | | | <br />
     * - |---|---|---|---|---|---|---|---| <br />
     * | | | | Q | | | | |row <br />
     * |---|---|---|---|---|---|---|---| <br />
     * col <br />
     *
     * @param tabuleiro a matriz do tabuleiro
     * @param linha linha do tabuleiro
     * @param coluna coluna do tabuleiro
     *
     * @return true se a rainha [linha][coluna] nafor atacada nas posicoes ja
     * atacadas por rainhas previamente inseridas                                      
     */
    private static boolean valida(boolean[][] tabuleiro, int linha, int coluna) {
        final int tamanhoTabuleiro = tabuleiro.length;

        //testa se as posicoes sao regioes que estao sendo atacadas
        for (int k = 1; k <= linha; k++) {
            if ((tabuleiro[linha - k][coluna] == true)
                    || ((coluna - k >= 0) && tabuleiro[linha - k][coluna - k])
                    || ((coluna + k < tamanhoTabuleiro) && tabuleiro[linha - k][coluna + k])) {
                return false;
            }
        }
        return true;
    }

    /**
     * *************************************************
     * Imprime o tabuleiro da solucao do problema das Rainhas
     *
     * @param tabuleiro a matriz do tabuleiro
	 ***************************************************
     */
    private void print(boolean[][] tabuleiro) {

        final int tamanhoTabuleiro = tabuleiro.length;
        // <b>each line in the board will be separated using
        // the following string:</b>
        String sepLine = " +";
        for (int i = 0; i < tamanhoTabuleiro; i++) {
            sepLine += " --- +";
        }

        // <b>print the board:</b>
        for (int r = 0; r < tamanhoTabuleiro; r++) {
            print = print + sepLine + System.getProperty("line.separator");
            print = print + "  |";
            for (int c = 0; c < tamanhoTabuleiro; c++) {
                String q = tabuleiro[r][c] ? "X" : " ";
                print = print + "   " + q + "   |";
            }
            print = print + System.getProperty("line.separator");
        }
        print = print + sepLine + "\n\n" + System.getProperty("line.separator");
    }

    /**
     * *************************************************
     * Inicia a solucao do problema das Rainhas <br />
     *
     * - E pedido o numero de Rainhas do problema que se deseja solucionar.
	 ***************************************************
     */
    public void iniciar() {
        this.N = 8;
        this.solucionar();
        if (contador == 0) {
            System.out.println("O problema de " + N + " Rainhas nao possui solucoes");
        } else {
            System.out.println("Foram encontradas " + this.contador + " solucoes do problema de " + N + " Rainhas");
        }
        String s = this.print;
        System.out.println(s);
    }

    /**
     * *************************************************
     * Teste de codigo de solucao do problema
	 ***************************************************
     */
    public static void main(String argv[]) {

        NRainhas r = new NRainhas();

        r.iniciar();

    }
}//;~ fim NRainhas
