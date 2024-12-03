import javax.swing.*;
import java.awt.*;

class MainMenu extends JPanel {
    public MainMenu(JFrame frame) {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Obwody i sygnaly", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        add(title, BorderLayout.CENTER);

        JButton startButton = new JButton("Rozpocznij grę");
        startButton.setFont(new Font("Arial", Font.PLAIN, 24));
        add(startButton, BorderLayout.SOUTH);

        startButton.addActionListener(e -> {
            GamePanel gamePanel = new GamePanel(frame);
            frame.getContentPane().removeAll();
            frame.add(gamePanel);
            frame.revalidate();
            frame.repaint();
            gamePanel.requestFocusInWindow();
        });
    }
}