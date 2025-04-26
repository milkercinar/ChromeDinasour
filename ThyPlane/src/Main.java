import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        int boardWidth = 750;
        int boardHeight = 250;

        JFrame frame = new JFrame("Plane Game");
        //frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ChromeDinasour planeGame = new ChromeDinasour();
        frame.add(planeGame);
        frame.pack();
        planeGame.requestFocus();
        frame.setVisible(true);

    }
}