public class No {
    private Object chave;

    private No pai;

    private No filhoEsquerdo;

    private No filhoDireito;

    private No irmao;

    private int cor;

    public No(Object newChave) {
        this.chave = newChave;
    }

    public void setPai(No pai) {
        this.pai = pai;
    }
    public No getPai(){
        return pai;
    }

    public Object getChave(){
        return chave;
    }
    public void setChave(Object chave) {
        this.chave = chave;
    }

    public void setFilhoEsquerdo(No filhoEsquerdo){
        this.filhoEsquerdo = filhoEsquerdo;
    }

    public No getFilhoEsquerdo(){
        return filhoEsquerdo;
    }

    public void setFilhoDireito(No filhoDireito){
        this.filhoDireito = filhoDireito;
    }

    public No getFilhoDireito(){
        return filhoDireito;
    }

    public void setCor(int cor){
        this.cor = cor;
    }

    public int getCor(){
        return cor;
    }

    public void setIrmao(No irmao){
        this.irmao = irmao;
    }

    public No getIrmao(){
        return irmao;
    }
}
