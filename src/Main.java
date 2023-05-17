public class Main {
    public static void main(String[] args) throws InvalidNoException {
        ArvoreRN rn = new ArvoreRN(10);
        No a = rn.addChave(8);
        No b = rn.addChave(1);
        No c = rn.addChave(2);
        No d = rn.addChave(4);
        No e = rn.addChave(7);
        No f = rn.addChave(5);
        No g = rn.addChave(13);
        No h = rn.addChave(16);
        rn.mostrarArvore();
        System.out.println("-----------------------------------------------------------------------------------------");
        rn.removeChave(8);
        // fica bugado em temas claros
        rn.mostrarArvoreComCor();

    }
}