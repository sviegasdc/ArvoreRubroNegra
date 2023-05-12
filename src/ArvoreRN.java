import java.util.ArrayList;
import java.util.Comparator;

public class ArvoreRN {
//    i. Se v é nó externo , v é negro
//    ii. O nó raiz é negro
//    iii. Se v é rubro, então ambos os filhos são negros
//    iv. Os caminhos de v para seus nós descendentes
//    externos possuem idêntico número de nós negros
    No raiz;
    int tamanho;
    No filho;
    private Comparator<Object> comparadorDechaves;

    public ArvoreRN(Object chave){
        this.comparadorDechaves = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                // para evitar erro de nó sendo passado
                if (o1 instanceof No) {
                    o1 = ((No) o1).getChave();
                }
                if (o2 instanceof No) {
                    o2 = ((No) o2).getChave();
                }
                int chave1 = (int) o1;
                int chave2 = (int) o2;
                if(chave1 < chave2){
                    return -1;
                }
                if(chave1 > chave2){
                    return 1;
                }
                else{
                    return 0;
                }
            }
        };
        raiz = new No(chave);
        // já inicializa os filhos como nós externos
        tamanho = 1;
        // então não é necessário setar os filhos como "null"
        // raiz.setFilhoEsquerdo(null); raiz.setFilhoDireito(null);
    }

    public int compareChaves (Object chave1, Object chave2){
        return comparadorDechaves.compare(chave1, chave2);
        // se a primeira chave é menor que a segunda, o método retorna um número negativo,"-1"
        // se a primeira chave é igual à segunda, o método retorna "0"
        // se a primeira chave é maior que a segunda, o método retorna um número positivo,"1"
    }

    public int altura(No node) {
        int alturaEsquerda = 0;
        int alturaDireita = 0;
        if(
                ehExterno(node)){
            return 0;
        }
        if(node.getFilhoEsquerdo() != null){
            // contando só os nós negros
            alturaEsquerda = altura(node.getFilhoEsquerdo()) +1;
        }
        if(node.getFilhoDireito() != null){
            alturaDireita = altura(node.getFilhoDireito()) +1;
        }
        return Math.max(alturaEsquerda, alturaDireita);
    }

    public int alturaNegra(No node) {
        int alturaEsquerda = 0;
        int alturaDireita = 0;
        if(
                ehExterno(node)){
            return 0;
        }
        if(node.getFilhoEsquerdo() != null && node.getCor() != 0){
            // contando só os nós negros
            alturaEsquerda = altura(node.getFilhoEsquerdo()) +1;
        }
        if(node.getFilhoDireito() != null && node.getCor() != 0){
            // contando só os nós negros
            alturaDireita = altura(node.getFilhoDireito()) +1;
        }
        return Math.max(alturaEsquerda, alturaDireita);
    }


    public boolean ehExterno(No node){
        // para ser externo/folha ele não pode ter nenhum filho
        return node.getFilhoDireito() == null && node.getFilhoEsquerdo() == null;
    }

    public boolean ehInterno(No node){
        // para ser interno ele deve possuir pelo menos um filho
        return node.getFilhoDireito() != null || node.getFilhoEsquerdo() != null;
    }

    private void corrigirRaiz() {
        if (raiz != null) {
            raiz.setCor(0); // garante que a raiz seja sempre preta
        }
    }

    public No pesquisar(No node, Object chave){
        if(ehExterno(node)){
            return node;
        }
        // passar pelo método de comparação onde pode retornar (1,-1 ou 0)
        int comparacao = compareChaves(node.getChave(),chave);
        if (comparacao > 0){
            if(node.getFilhoEsquerdo() !=null){
                return pesquisar(node.getFilhoEsquerdo(), chave);
            }
            else{
                return node;
            }
            // pesquisar recursivamente passando o filho esquerdo do nó e a chave como parâmetro
        } else if (comparacao == 0) {
            return node;
            // se a comparação der igual, retorna o nó passado inicialmente no parâmetro
        }
        else {
            if(node.getFilhoDireito() !=null){
                return pesquisar(node.getFilhoDireito(), chave);
            }
            else{
                return node;
            }
        }
    }

    public No addChave(Object chave){
        No noApesquisar = pesquisar(raiz,chave);
        // pesquisa para saber onde inserir o novo nó
        if (ehExterno(noApesquisar)) {
            // se o nó pesquisado for externo (folha) adicionamos o novo nó como filho
            No novoNo = new No(chave);
            novoNo.setPai(noApesquisar);
            int comparacao = compareChaves(chave, noApesquisar.getChave());
            if (comparacao < 0) {
                // Se a primeira chave é menor que a segunda vai setar o novo nó como filho esquerdo
                noApesquisar.setFilhoEsquerdo(novoNo);
            } else {
                noApesquisar.setFilhoDireito(novoNo);
            }
            tamanho++;
            // atualizar as cores e fazer a checagem de rotação ou re-colorações
            checaRegrasInsercao(novoNo);
            return novoNo;
        } else if (noApesquisar.getFilhoEsquerdo() == null || noApesquisar.getFilhoDireito() == null) {
            // se o nó pesquisado é interno e tem pelo menos um filho vazio, adicionamos o novo nó nesse filho vazio
            No novoNo = new No(chave);
            novoNo.setPai(noApesquisar);
            if (noApesquisar.getFilhoEsquerdo() == null) {
                noApesquisar.setFilhoEsquerdo(novoNo);
            } else {
                noApesquisar.setFilhoDireito(novoNo);
            }
            tamanho++;
            // atualizar as cores e fazer a checagem de rotação ou re-colorações
            checaRegrasInsercao(novoNo);
            return novoNo;
        } else {
            System.out.println("A chave já foi adicionada na árvore");
            return null;
        }

    }

    private void checaRegrasInsercao(No node){
        // CASO 1
        // evitando o NullPointerException
        if(node.getPai() != null){
            No pai = node.getPai();
            // se for 0 é negra, se for 1 é rubro
            node.setCor(1);
            // checando os outros casos
            // CASO 2
            insercaoCasoR(node);
            corrigirRaiz();
            // CASO 3 (rotações)
            insercaoCaso3(node);
            corrigirRaiz();

        }
    }

    public No insercaoCasoR(No node) {
        if(node==null)
            return node;
       // No avo;
       // No tio;
        No pai = node.getPai();
        if (pai != null) {
            No avo = pai.getPai();
            No tio = null;
            // se tio for nulo então é um sentinela negro
            int tioCor = 0;
            // tentando achar o tio
            if (avo != null) {
                if (temDoisFilhos(avo)) {
                    if (ehFilhoDireito(pai)) {
                        tio = avo.getFilhoEsquerdo();
                    } else {
                        tio = avo.getFilhoDireito();
                    }
                    tioCor = tio.getCor();
                    if(tioCor==1 && avo.getCor()==0 && pai.getCor()==1) {
                        avo.setCor(1);
                        pai.setCor(0);
                        tio.setCor(0);
                        return insercaoCasoR(avo);
                    }
                }
            }
        }

        return null;
    }

    public void insercaoCaso2(No node) {
        No pai = node.getPai();
        // evitando o NullPointerException
        if (pai.getPai() != null) {
            No avo = pai.getPai();
            No tio = null;
            // se tio for nulo então é um sentinela negro
            int tioCor = 0;
            // tentando achar o tio
            if(avo != null){
                if (temDoisFilhos(avo)) {
                    if (ehFilhoDireito(pai)) {
                        tio = avo.getFilhoEsquerdo();
                    } else {
                        tio = avo.getFilhoDireito();
                    }
                    tioCor = tio.getCor();
                }
                // CASO 2
                // pai rubro, avô negro e tio rubro (apenas recoloração)
                // se o pai for rubro significa que ele não é raiz, ou seja, sempre vai ter um pai
                while (pai != null && pai.getCor() == 1 && avo.getCor() == 0 && tioCor == 1) {
                    // se for 0 é negra, se for 1 é rubro
                    avo.setCor(1);
                    pai.setCor(0);
                    tio.setCor(0);
                    // atualiza as variáveis para verificar novamente
                    node = avo;
                    pai = node.getPai();
                    if (pai != null) {
                        avo = pai.getPai();
                        tio = null;
                        tioCor = 0;
                        // tentando achar o tio
                        if (temDoisFilhos(avo)) {
                            if (ehFilhoDireito(pai)) {
                                tio = avo.getFilhoEsquerdo();
                            } else {
                                tio = avo.getFilhoDireito();
                            }
                            tioCor = tio.getCor();
                        }

                    }

                }
            }

        }
    }

    public void insercaoCaso3(No node){
        No pai = node.getPai();
        if(pai.getPai() != null) {
            No avo = pai.getPai();
            No tio = null;
            // se tio for nulo então é um sentinela negro
            int tioCor = 0;
            // tentando achar o tio
            if (temDoisFilhos(avo)) {
                if (ehFilhoDireito(pai)) {
                    tio = avo.getFilhoEsquerdo();
                } else {
                    tio = avo.getFilhoDireito();
                }
                tioCor = tio.getCor();
            }
            // pai rubro, avô negro, tio negro
            if(pai.getCor() == 1 && avo.getCor() == 0 && tioCor == 0){
                // 3a (rotação direita simples)
                if(ehFilhoEsquerdo(pai) && ehFilhoEsquerdo(node)){
                    rotacaosimplesDireita(node);
                }
                // 3b (rotação esquerda simples)
                else if(ehFilhoDireito(pai)&& ehFilhoDireito(node)){
                    rotacaosimplesEsquerda(node);
                }
                // 3c (rotação esquerda dupla)
                else if(ehFilhoDireito(pai) && ehFilhoEsquerdo(node)){
                    // pequena rotação com pai e filho
                    node.setFilhoDireito(pai);
                    avo.setFilhoDireito(node);
                    node.setPai(avo);
                    pai.setPai(node);
                    rotacaosimplesEsquerda(pai);
                }
                // 3d (rotação direita dupla)
                else if(ehFilhoEsquerdo(pai) && ehFilhoDireito(node)){
                    // pequena rotação com pai e filho
                    node.setFilhoEsquerdo(pai);
                    avo.setFilhoEsquerdo(node);
                    node.setPai(avo);
                    pai.setPai(node);
                    rotacaosimplesDireita(pai);

                }
            }
        }
    }

    public void rotacaosimplesEsquerda(No node){
        No pai = node.getPai();
        No avo = pai.getPai();
        // 'colisão'
        // se tiver um filho esquerdo ele sempre vai ser filho esquerdo do antigo avô
        No filhoEsquerdo = null;
        if(pai.getFilhoEsquerdo()!=null){
            filhoEsquerdo = pai.getFilhoEsquerdo();
            filhoEsquerdo.setPai(avo);
        }
        avo.setFilhoDireito(filhoEsquerdo);
        pai.setFilhoEsquerdo(avo);
        pai.setPai(avo.getPai());
        // que tipo de filho o pai vai ser
        if(avo.getPai()!=null){
            if(ehFilhoDireito(avo)){
                (avo.getPai()).setFilhoDireito(pai);
            }
            else{
                (avo.getPai()).setFilhoEsquerdo(pai);
            }
        }
        // se o avo for raiz
        else{
            pai.setPai(null);
            raiz = pai;
        }
        avo.setPai(pai);
        // setando as novas cores
        pai.setCor(0);
        avo.setCor(1);
    }

    public void rotacaosimplesDireita(No node){
        No pai = node.getPai();
        No avo = pai.getPai();
        // 'colisão'
        // se tiver um filho direito ele sempre vai ser filho esquerdo do antigo avô
        No filhoDireito = null;
        if(pai.getFilhoDireito()!=null){
            filhoDireito = pai.getFilhoDireito();
            filhoDireito.setPai(avo);
        }
        avo.setFilhoEsquerdo(filhoDireito);
        pai.setFilhoDireito(avo);
        pai.setPai(avo.getPai());
        // que tipo de filho o pai vai ser
        if(avo.getPai()!=null){
            if(ehFilhoDireito(avo)){
                (avo.getPai()).setFilhoDireito(pai);
            }
            else{
                (avo.getPai()).setFilhoEsquerdo(pai);
            }
        }
        // se o avo for raiz
        else{
            pai.setPai(null);
            raiz = pai;
        }
        avo.setPai(pai);
        // setando as novas cores
        pai.setCor(0);
        avo.setCor(1);
    }

    private No sucessor(No node){
        if(node.getFilhoEsquerdo() == null){
            return node;
        }
        else{
            return sucessor(node.getFilhoEsquerdo());
        }
    }

    ArrayList<No> a = new ArrayList<>();
    // array para armazenar os nós em ordem

    public void emOrdemP(No node){
        // filho esquerdo, nó pai e depois filho direito
        if(node.getFilhoEsquerdo() != null) {
            // filho esquerdo
            emOrdemP(node.getFilhoEsquerdo());
        }
        // nó pai
        a.add(node);
        // filho direito
        if(node.getFilhoDireito() != null){
            emOrdemP(node.getFilhoDireito());
        }
    }

    public void mostrarArvore(){
        a.clear(); // limpando a array para ser possível imprimir novamente
        Object[][] m = new Object[altura(raiz)+1][size()];
        emOrdemP(raiz);
        for(int i=0; i < a.size(); i++ ){
            m[profundidade(a.get(i))] [i]=a.get(i).getChave() + "[" + (a.get(i).getCor() == 1 ? "R" : "N") + "]";
        }

        for(int l = 0; l < altura(raiz)+1; l++){
            for(int c = 0; c < a.size(); c++){
                System.out.print(m[l][c]==null?"\t":m[l][c]+"\t");
            }
            System.out.println();
        }
    }

    public boolean temUmFilho(No node){
        if(node.getFilhoEsquerdo() != null && node.getFilhoDireito() == null){
            // se o filho esquerdo não for nulo e filho direito for nulo
            return true;
        }
        // se o filho esquerdo for nulo e filho direito não for nulo
        // se tiver mais de um filho
        return node.getFilhoEsquerdo() == null && node.getFilhoDireito() != null;
    }
    public boolean temDoisFilhos(No node){
        if(node.getFilhoEsquerdo() != null && node.getFilhoDireito() != null){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean ehFilhoDireito(No node) {
        // pegar o pai desse nó
        No paiDoNo = node.getPai();
        // checar se é nulo
        if (paiDoNo == null) {
            return false;
        }
        // se for o filho direito = true
        return paiDoNo.getFilhoDireito() == node;
    }

    public boolean ehFilhoEsquerdo(No node) {
        // pegar o pai desse nó
        No paiDoNo = node.getPai();
        // checar se é nulo
        if (paiDoNo == null) {
            return false;
        }
        // se for o filho esquerdo = true
        return paiDoNo.getFilhoEsquerdo() == node;
    }

    public No getRaiz(){
        // retornar raiz da arvore
        return raiz;
    }
    public No setRaiz(No node){
        return raiz;
    }

    public boolean temFilhoEsquerdo(No node){
        System.out.println("Nó '"+ node.getChave() + "' tem filho esquerdo? ");
        return node.getFilhoEsquerdo()!= null;
    }

    public boolean temFilhoDireito(No node){
        System.out.println("Nó '"+ node.getChave() + "' tem filho direito? ");
        return node.getFilhoDireito()!= null;
    }

    public int profundidade(No node){
//        if (node == raiz)
        if (node.getPai() == null) {
            return 0;
        }
        else{
            return 1 + profundidade(node.getPai());
        }
    }

    public int size(){
        return tamanho;
    }

    public boolean isEmpty(){
        return tamanho == 0;
    }

}
