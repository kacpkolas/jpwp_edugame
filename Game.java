import javax.swing.*;



public class Game {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Obwody i sygnaly");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1024, 768);
            frame.setResizable(false);

            MainMenu menu = new MainMenu(frame);
            frame.add(menu);

            frame.setVisible(true);
        });
    }
}



