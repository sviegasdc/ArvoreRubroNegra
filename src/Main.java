public class Main {
    public static void InsercaoRemocaoSimples() throws InvalidNoException {
        ArvoreRN rn = new ArvoreRN(10);
        //TESTANDO CASOS INSERÇÃO
        No a = rn.addChave(5);
        No b = rn.addChave(15);
        // fica bugado em temas claros
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        No c = rn.addChave(3);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        No d = rn.addChave(7);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        No e = rn.addChave(12);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        No f = rn.addChave(17);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");

        //TESTANDO CASOS REMOÇÃO
        System.out.println("REMOÇÕES:");
        rn.removeChave(17);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        rn.removeChave(12);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        rn.removeChave(7);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        rn.removeChave(3);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        rn.removeChave(15);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        rn.removeChave(5);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        rn.removeChave(10);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    public static void InsercaoRemocaoComplexa() throws InvalidNoException{
        ArvoreRN rn = new ArvoreRN(10);
        //TESTANDO CASOS INSERÇÃO
        No a = rn.addChave(8);
        No b = rn.addChave(1);
        // fica bugado em temas claros
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        No c = rn.addChave(2);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        No d = rn.addChave(4);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        No e = rn.addChave(7);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        No f = rn.addChave(5);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        No g = rn.addChave(13);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        No h = rn.addChave(16);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");

        //TESTANDO CASOS REMOÇÃO
        System.out.println("REMOÇÕES:");
        rn.removeChave(1);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        rn.removeChave(7);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        rn.removeChave(10);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");
        rn.removeChave(5);
        rn.mostrarArvoreComCor();
        System.out.println("-----------------------------------------------------------------------------------------");

    }
    public static void main(String[] args) throws InvalidNoException {
        //InsercaoRemocaoSimples();
        //InsercaoRemocaoComplexa();
    }
}