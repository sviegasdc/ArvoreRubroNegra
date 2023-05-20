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
            insercaoCaso2(node);
            corrigirRaiz();
            // CASO 3 (rotações)
            insercaoCaso3(node);
            corrigirRaiz();

        }
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
                        if(avo != null){
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
                    rotacaoSimplesDireita(avo);
                }
                // 3b (rotação esquerda simples)
                else if(ehFilhoDireito(pai)&& ehFilhoDireito(node)){
                    rotacaoSimplesEsquerda(avo);
                }
                // 3c (rotação esquerda dupla)
                else if(ehFilhoDireito(pai) && ehFilhoEsquerdo(node)){
                    // pequena rotação com pai e filho
                    node.setFilhoDireito(pai);
                    avo.setFilhoDireito(node);
                    node.setPai(avo);
                    pai.setPai(node);
                    pai.setFilhoEsquerdo(null);
                    rotacaoSimplesEsquerda(avo);
                }
                // 3d (rotação direita dupla)
                else if(ehFilhoEsquerdo(pai) && ehFilhoDireito(node)){
                    // pequena rotação com pai e filho
                    node.setFilhoEsquerdo(pai);
                    avo.setFilhoEsquerdo(node);
                    node.setPai(avo);
                    pai.setPai(node);
                    pai.setFilhoDireito(null);
                    rotacaoSimplesDireita(avo);

                }
            }
        }
    }

    private No rotacaoSimplesEsquerda(No node) {
        No antigoPaidoNode = node.getPai();
        // para caso haja 'colisão' dos nós ao lado esquerdo
        No filhoEsquerdo = null;
        No filhoDireito = node.getFilhoDireito();
        if((node.getFilhoDireito()).getFilhoEsquerdo() != null) {
            filhoEsquerdo = (node.getFilhoDireito()).getFilhoEsquerdo();
            // não precisa de comparação pq o node sempre vai ser maior que o filho esquerdo (lógica da árvore)
            filhoEsquerdo.setPai(node);
        }
        node.setFilhoDireito(filhoEsquerdo);
        filhoDireito.setFilhoEsquerdo(node);
        filhoDireito.setPai(antigoPaidoNode);
        if(node.getPai() != null) {
            if (ehFilhoEsquerdo(node)) {
                (node.getPai()).setFilhoEsquerdo(filhoDireito);
            } else {
                // se for filho direito
                (node.getPai()).setFilhoDireito(filhoDireito);
            }
        }else{
            filhoDireito.setPai(null);
            raiz = filhoDireito;
        }
        node.setPai(filhoDireito);
        node.setCor(1);
        filhoDireito.setCor(0);
        return node;
    }

    private No rotacaoSimplesDireita(No node)  {
        No antigoPaidoNode = node.getPai();
        // colisão
        No filhoDireito = null;
        No filhoEsquerdo = node.getFilhoEsquerdo();
        if((node.getFilhoEsquerdo()).getFilhoDireito() != null){
            filhoDireito = (node.getFilhoEsquerdo()).getFilhoDireito();
            filhoDireito.setPai(node);
        }
        node.setFilhoEsquerdo(filhoDireito);
        filhoEsquerdo.setFilhoDireito(node);
        filhoEsquerdo.setPai(antigoPaidoNode);
        if(node.getPai() != null){
            if(ehFilhoEsquerdo(node)){
                (node.getPai()).setFilhoEsquerdo(filhoEsquerdo);
            }else{
                (node.getPai()).setFilhoDireito(filhoEsquerdo);
            }
        }else{
            filhoEsquerdo.setPai(null);
            raiz = filhoEsquerdo;
        }
        node.setPai(filhoEsquerdo);
        // setando as novas cores
        node.setCor(1);
        filhoEsquerdo.setCor(0);

        return node;
    }

    public Object removeChave(Object chave) throws InvalidNoException {
        // se o nó for folha
        No node = pesquisar(raiz, chave);
        No noGuardado = node;
        No paiGUardado =  node.getPai();
        if (node == null) {
            throw new InvalidNoException("Não foi possível encontrar essa chave na árvore");
        }
        if (ehExterno(node)) {
            if (node == raiz && node.getFilhoEsquerdo() == null && node.getFilhoDireito() == null) {
                Object temp = node.getChave();
                node.setChave(null);
                return temp;
            }
            // se é folha (não tem filhos)
            Object temp = node.getChave();
            // guarda o elemento da chave para poder retornar
            if (ehFilhoDireito(node)) {
                node.getPai().setFilhoDireito(null);
                // setar o nó como null
            } else {
                node.getPai().setFilhoEsquerdo(null);
            }
            checaSituacaoFolha(noGuardado,paiGUardado );
            corrigirRaiz();
            return temp;
        }
        // se o nó tem apenas um filho
        if (temUmFilho(node)) {
            if (node.getFilhoEsquerdo() != null) {
                filho = node.getFilhoEsquerdo();
            } else {
                filho = node.getFilhoDireito();
            }
            if (ehFilhoDireito(node)) {
                node.getPai().setFilhoDireito(filho);
            } else {
                node.getPai().setFilhoEsquerdo(filho);
            }
            filho.setPai(node.getPai());
            checaRegrasRemocao(noGuardado, filho);
            corrigirRaiz();
            return node.getChave();
        }
        // se o nó tem dois filhos
        if(temDoisFilhos(node)){
            Object temp = node.getChave();
            No sucessor = sucessor(node.getFilhoDireito());
            removeChave(sucessor);
            node.setChave(sucessor.getChave());
            checaRegrasRemocao(noGuardado, sucessor);
            corrigirRaiz();
            return temp;
        }
        return null;
    }

    private No sucessor(No node){
        if(node.getFilhoEsquerdo() == null){
            return node;
        }
        else{
            return sucessor(node.getFilhoEsquerdo());
        }
    }

    private void checaRegrasRemocao(No node, No sucessor){
        // precisa ser passado o noGuardado e a cor do sucessor
        // rubro → 1 e negro → 0
        // situação 1 (v = rubro, sucessor = rubro)
        if(node.getCor() == 1 && sucessor.getCor() == 1){
            return;
        }
        // situação 2 (v = negro, sucessor = rubro)
        if(node.getCor() == 0 && sucessor.getCor() == 1 ){
            sucessor.setCor(0);
            return;
        }
        // situação 3 (v = negro, sucessor = negro)
        remocaoSituacao3(node,sucessor);
        // situação 4 (v = rubro, sucessor = negro)
        remocaoSituacao4(node, sucessor);
    }

    public No checaSituacaoFolha(No node, No pai){
        if(pai != null){
            No irmao = null;
            int irmaoCor = 0;
            if(temFilhoDireito(pai)){
                irmao = pai.getFilhoDireito();
            }
            else if(temFilhoEsquerdo(pai)){
                irmao = pai.getFilhoEsquerdo();
            }
            if(node.getCor() == 0){
                // caso 1 (sucessor tem irmão rubro e pai negro)
                if(irmaoCor == 1 && pai.getCor() == 0){
                    //Faça uma rotação simples esquerda
                    rotacaoSimplesEsquerda(pai);
                    //Pinte w de negro
                    irmao.setCor(0);
                    //Pinte pai de x de rubro
                    pai.setCor(1);
                }
            }
            // caso 2a (sucessor tem irmão negro e pai negro)
            if(irmaoCor == 0 && pai.getCor() == 0){
                //Pinte o irmão w de rubro
                if(irmao != null){
                    irmao.setCor(1);
                }
                irmaoCor = 1;
            }
            if(irmao != null){
                //pegar os filhos do irmão
                int filhoEsquerdoCor = 0;
                int filhoDireitoCor = 0;
                if(temFilhoDireito(irmao)){
                    filhoDireitoCor = (irmao.getFilhoDireito()).getCor();
                }
                if(temFilhoEsquerdo(irmao)){
                    filhoEsquerdoCor = (irmao.getFilhoEsquerdo()).getCor();
                }

                // caso 2b (sucessor tem irmão negro, com filhos negros e pai rubro)
                if(irmaoCor == 1 && filhoEsquerdoCor == 0 && filhoDireitoCor == 0 && pai.getCor() == 1){
                    //Pinte o irmão w de rubro e o pai de x de negro
                    irmao.setCor(1);
                    pai.setCor(0);
                }
                // caso 3 (sucessor tem irmão negro com filho esquerdo rubro e filho direito negro, pai qualquer cor)
                else if(irmaoCor == 0 && filhoEsquerdoCor == 1 && filhoDireitoCor == 0){
                    //Rotação simples direita em w
                    rotacaoSimplesDireita(pai);
                    //Trocar as cores de w com seu filho esquerdo
                    irmao.setCor(1);
                    if(irmao.getFilhoEsquerdo() != null){
                        (irmao.getFilhoEsquerdo()).setCor(0);
                    }

                }
                // caso 4 (sucessor tem irmão negro com filho esquerdo qualquer cor e filho direito rubro, pai qualquer cor)
                if(irmaoCor == 0 && filhoDireitoCor == 1){
                    int temp = pai.getCor();
                    // rotação dupla
                    if(irmao.getFilhoDireito() != null && irmao.getFilhoEsquerdo() == null){
                        // rotação esquerda
                        No paiLocal = pai;
                        No filhoDireito = irmao.getFilhoDireito();
                        irmao.setPai(filhoDireito);
                        filhoDireito.setFilhoEsquerdo(irmao);
                        filhoDireito.setPai(pai);
                        pai.setFilhoEsquerdo(filhoDireito);
                        irmao.setFilhoDireito(null);

                        irmao.setCor(0);
                        pai.setCor(0);
                        filhoDireito.setCor(1);
                        insercaoCaso3(irmao);
                        if(irmao.getCor() == 0 && pai.getCor() == 1 && filhoDireito.getCor() == 0){
                            pai.setCor(0);
                            filhoDireito.setCor(1);
                        }

                    }else{
                        //Rotação simples a esquerda
                        rotacaoSimplesEsquerda(pai);
                        //Pinte o pai de negro
                        pai.setCor(0);
                        //w igual a cor anterior do pai de x
                        if(irmao != null){
                            irmao.setCor(temp);
                            //Pinte o filho direito de w de negro
                            if(irmao.getFilhoDireito() != null){
                                (irmao.getFilhoDireito()).setCor(0);
                            }
                        }
                    }
                }

            }
        }

        return node;
    }

    public No remocaoSituacao3(No node, No sucessor){
        // situação 3 (v = negro, sucessor = negro)
        No pai = sucessor.getPai();
        No irmao = null;
        int irmaoCor = 0;
        if(pai !=null){
            if (temDoisFilhos(pai)) {
                if (ehFilhoDireito(sucessor)) {
                    irmao = pai.getFilhoEsquerdo();
                } else {
                    irmao = pai.getFilhoDireito();
                }
                irmaoCor = irmao.getCor();
            }
            if(node.getCor() == 0 && sucessor.getCor() == 0){
                // caso 1 (sucessor tem irmão rubro e pai negro)
                if(irmaoCor == 1 && pai.getCor() == 0){
                    //Faça uma rotação simples esquerda
                    rotacaoSimplesEsquerda(irmao);
                    //Pinte w de negro
                    irmao.setCor(0);
                    //Pinte pai de x de rubro
                    pai.setCor(1);
                }
                // caso 2a (sucessor tem irmão negro e pai negro)
                if(irmaoCor == 0 && pai.getCor() == 0){
                    //Pinte o irmão w de rubro
                    if(irmao != null){
                        irmao.setCor(1);
                    }
                    irmaoCor = 1;
                }

                //pegar os filhos do irmão
                int filhoEsquerdoCor = 0;
                int filhoDireitoCor = 0;
                if(temFilhoDireito(irmao)){
                    filhoDireitoCor = (irmao.getFilhoDireito()).getCor();
                }
                if(temFilhoEsquerdo(irmao)){
                    filhoEsquerdoCor = (irmao.getFilhoEsquerdo()).getCor();
                }

                // caso 2b (sucessor tem irmão negro, com filhos negros e pai rubro)
                if(irmaoCor == 1 && filhoEsquerdoCor == 0 && filhoDireitoCor == 0 && pai.getCor() == 1){
                    //Pinte o irmão w de rubro e o pai de x de negro
                    irmao.setCor(1);
                    pai.setCor(0);
                }
                // caso 3 (sucessor tem irmão negro com filho esquerdo rubro e filho direito negro, pai qualquer cor)
                if(irmaoCor == 0 && filhoEsquerdoCor == 1 && filhoDireitoCor == 0){
                    //Rotação simples direita em w
                    rotacaoSimplesDireita(irmao);
                    //Trocar as cores de w com seu filho esquerdo
                    irmao.setCor(1);
                    (irmao.getFilhoEsquerdo()).setCor(0);
                }
                // caso 4 (sucessor tem irmão negro com filho esquerdo qualquer cor e filho direito rubro, pai qualquer cor)
                if(irmaoCor == 0 && filhoDireitoCor == 1){
                    int temp = pai.getCor();
                    //Rotação simples a esquerda
                    rotacaoSimplesEsquerda(pai);
                    //Pinte o pai de negro
                    pai.setCor(0);
                    //w igual a cor anterior do pai de x
                    irmao.setCor(temp);
                    //Pinte o filho direito de w de negro
                    (irmao.getFilhoDireito()).setCor(0);
                }
            }
        }

        return node;
    }

    public No remocaoSituacao4(No node, No sucessor){
        // situação 4 (v = rubro, sucessor = negro)
        int sucessorCor = 0;
        if(sucessor != null){
            sucessorCor = sucessor.getCor();
        }
        if(node.getCor() == 1 && sucessor != null && sucessorCor == 0){
            sucessor.setCor(1);
            remocaoSituacao3(node, sucessor);
        }
        if(node.getCor() == 1 && sucessor == null && sucessorCor == 0){
            checaSituacaoFolha(node, node.getPai());
        }
        return node;
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

    public void mostrarArvoreComCor() {
        a.clear(); // limpando a array para ser possível imprimir novamente
        Object[][] m = new Object[altura(raiz) + 1][size()];
        emOrdemP(raiz);
        for (int i = 0; i < a.size(); i++) {
            String cor = a.get(i).getCor() == 1 ? "\033[31m" : "\033[0m"; // Define a cor vermelha se o nó for rubro
            m[profundidade(a.get(i))][i] = cor + a.get(i).getChave() + "\033[0m"; // Define a cor normal após o valor do nó
        }

        for (int l = 0; l < altura(raiz) + 1; l++) {
            for (int c = 0; c < a.size(); c++) {
                System.out.print(m[l][c] == null ? "\t" : m[l][c] + "\t");
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

    public boolean temFilhoEsquerdo(No node){
        return node.getFilhoEsquerdo()!= null;
    }

    public boolean temFilhoDireito(No node){
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
