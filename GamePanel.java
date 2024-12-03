import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class GamePanel extends JPanel implements KeyListener {
    private int playerX = 50, playerY = 50;
    private int level = 1;
    private final int playerSize = 30;
    private final int step = 10;
    private int lives = 3; 
    private ArrayList<Rectangle> goals;
    private ArrayList<Color> goalColors;
    private ArrayList<Color> requiredSequence;
    private Rectangle checkPoint; 
    private int currentGoalIndex = -1; 
    private boolean isPaused = false;
    private JPanel menuPanel;
    private JButton openMenuButton;
    private Image backgroundImage;

    public GamePanel(JFrame frame) {
        setFocusable(true);
        addKeyListener(this);

        loadLevel();
        createMenuPanel(frame);

        Timer timer = new Timer(1000 / 60, e -> {
            if (!isPaused) repaint();
        });
        timer.start();
    }

    private void createMenuPanel(JFrame frame) {
        setLayout(null);

        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(0, 0, 0, 200));
        menuPanel.setVisible(false);
        menuPanel.setBounds(800, 0, 200, getHeight());
        add(menuPanel);

        JButton resumeButton = new JButton("Kontynuuj");
        resumeButton.addActionListener(e -> {
            openMenuButton.setVisible(true);
            isPaused = false;
            menuPanel.setVisible(false);
        });

        JButton mainMenuButton = new JButton("Menu Główne");
        mainMenuButton.addActionListener(e -> {
            openMenuButton.setVisible(true);
            JFrame window = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.getContentPane().removeAll();
                window.add(new MainMenu(window));
                window.revalidate();
                window.repaint();
            }
        });

        menuPanel.add(resumeButton);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(mainMenuButton);

        openMenuButton = new JButton("Menu");
        openMenuButton.setBounds(650, 10, 100, 30);
        openMenuButton.addActionListener(e -> {
            isPaused = true;
            openMenuButton.setVisible(false);
            menuPanel.setBounds(getWidth() - 200, 0, 200, getHeight());
            menuPanel.setVisible(true);
        });
        add(openMenuButton);
    }

    private void loadLevel() {
        goals = new ArrayList<>();
        goalColors = new ArrayList<>();

    
        requiredSequence = new ArrayList<>();

        if (level == 1) {
            backgroundImage = new ImageIcon("tlo_zad1.png").getImage();
            goals.add(new Rectangle(50, 100, 30, 30));
            goals.add(new Rectangle(50, 200, 30, 30));
            goals.add(new Rectangle(50, 400, 30, 30));

        
            goalColors.add(Color.RED);
            goalColors.add(Color.RED);
            goalColors.add(Color.RED);

            requiredSequence.add(Color.GREEN);
            requiredSequence.add(Color.RED);
            requiredSequence.add(Color.GREEN);

       
            checkPoint = new Rectangle(900, 600, 30, 30);
        } else if (level == 2) {
            backgroundImage = new ImageIcon("background_level2.jpg").getImage();
            goals.add(new Rectangle(600, 400, 50, 50));
            goals.add(new Rectangle(100, 100, 50, 50));
            goals.add(new Rectangle(400, 500, 50, 50));

            goalColors.add(Color.RED);
            goalColors.add(Color.RED);
            goalColors.add(Color.RED);

            requiredSequence.add(Color.GREEN);
            requiredSequence.add(Color.RED);
            requiredSequence.add(Color.GREEN);

         
            checkPoint = new Rectangle(700, 200, 50, 50);
        }

        currentGoalIndex = -1; 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, playerSize, playerSize);

     
        for (int i = 0; i < goals.size(); i++) {
            g.setColor(goalColors.get(i));
            Rectangle goal = goals.get(i);
            g.fillRect(goal.x, goal.y, goal.width, goal.height);
        }

 
        g.setColor(Color.YELLOW);
        g.fillOval(checkPoint.x, checkPoint.y, checkPoint.width, checkPoint.height);

  
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Poziom: " + level, 10, 20);
        g.drawString("Życia: " + lives, 10, 50);
    }

    private void checkSequence() {

        if (goalColors.equals(requiredSequence)) {
            JOptionPane.showMessageDialog(this, "Brawo! Poprawnie dobrane wejscia ukladu!");

            nextLevel();
        } else {
            lives--; 
            if (lives == 0) {
                JOptionPane.showMessageDialog(this, "Przegrales! Wracasz do menu glownego.");
                returnToMainMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Blednie dobrane wejscia ukladu, tracisz jedno zycie");
                resetLevel();
            }
        }
    }

    private void resetLevel() {

        playerX = 50;
        playerY = 50;
        for (int i = 0; i < goalColors.size(); i++) {
            goalColors.set(i, Color.RED);
        }
        currentGoalIndex = -1;
    }

    private void returnToMainMenu() {
        JFrame window = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.getContentPane().removeAll();
            window.add(new MainMenu(window));
            window.revalidate();
            window.repaint();
        }
    }

    private void nextLevel() {
        level++;
        if (level > 2) {
            JOptionPane.showMessageDialog(this, "Wygrales całą grę!");
            System.exit(0);
        } else {
            lives = 3; 
            loadLevel();
            playerX = 50;
            playerY = 50;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isPaused) return;

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) playerY -= step;
        if (key == KeyEvent.VK_S) playerY += step;
        if (key == KeyEvent.VK_A) playerX -= step;
        if (key == KeyEvent.VK_D) playerX += step;

        Rectangle playerBounds = new Rectangle(playerX, playerY, playerSize, playerSize);

        if (playerBounds.intersects(checkPoint)) {
            checkSequence();
            return;
        }

        for (int i = 0; i < goals.size(); i++) {
            if (playerBounds.intersects(goals.get(i))) {
                if (currentGoalIndex != i) {
                 
                    goalColors.set(i, goalColors.get(i) == Color.RED ? Color.GREEN : Color.RED);


                    currentGoalIndex = i;
                }
                return;
            }
        }


        currentGoalIndex = -1;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
