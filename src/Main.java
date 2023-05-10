public class Main {
    public static void main(String[] args) {
        ArvoreRN rn = new ArvoreRN(10);
        No a = rn.addChave(8);
        No b = rn.addChave(1);
        rn.mostrarArvore();
        System.out.println("---------------");
        No c = rn.addChave(2);
        rn.mostrarArvore();
        System.out.println("---------------");
        No d = rn.addChave(4);
        rn.mostrarArvore();
        System.out.println("---------------");
        No e = rn.addChave(7);
        rn.mostrarArvore();
        System.out.println("---------------");
        No f = rn.addChave(5);
        rn.mostrarArvore();
        System.out.println("---------------");
        No g = rn.addChave(13);
        rn.mostrarArvore();
        System.out.println("---------------");
        No h = rn.addChave(16);
        rn.mostrarArvore();
        System.out.println("---------------");

    }
}