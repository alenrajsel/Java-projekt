import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.lang.Math;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Projekt_java extends JPanel {
    int x;
    int y;
    int smallSquareSize;
    int mainSquareSize;
    static int[][] matrika;
    static boolean gameOver = false;
    static boolean win = false;

    public static void main(String[] args) {
        JFrame frame = new JFrame("2048 Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Projekt_java());
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    public Projekt_java() {
        matrika = matrix();
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	if(!gameOver || !win) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> go(38);
                    case KeyEvent.VK_RIGHT -> go(40);
                    case KeyEvent.VK_UP -> go(37);
                    case KeyEvent.VK_DOWN -> go(39);
                }
                repaint();
            }
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameOver || win) {
                    restartGame(); // Restart the game if it's over
                }
       
            }
        });

        setPreferredSize(new Dimension(400, 400)); 
    }
    
    private void restartGame() {
        // Resetiramo igro
        matrika = matrix();
        gameOver = false;
        win = false;
        repaint(); 
    }
    public void dimenzije() {
        Dimension size = getSize();
        mainSquareSize = Math.min(size.width, size.height) - 20; // Leave some margin
        smallSquareSize = mainSquareSize / 5;

        x = (size.width - mainSquareSize) / 2;
        y = (size.height - mainSquareSize) / 2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        dimenzije();
        
        drawGame(g);
        
        if (gameOver) {
            g.setColor(new Color(128, 128, 128, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
            Font font = new Font("Helvetica", Font.BOLD, (int) (smallSquareSize / 2.5));
            g.setFont(font);
            g.setColor(Color.BLACK);
            
            FontMetrics fm = g.getFontMetrics();
            String text = "Game Over! Click to restart.";
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();

            int textX = x + (mainSquareSize - textWidth) / 2;
            int textY = (mainSquareSize + textHeight) / 2 - fm.getDescent();

            g.drawString(text, textX, textY);
            return;
        }
        if (win) {
        	g.setColor(new Color(128, 128, 128, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
            Font font = new Font("Helvetica", Font.BOLD, (int) (smallSquareSize / 2.5));
            g.setFont(font);
            g.setColor(Color.BLACK);
            
            FontMetrics fm = g.getFontMetrics();
            String text = "You got 2048, you WIN! Click to restart.";
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();

            int textX = x + (mainSquareSize - textWidth) / 2;
            int textY = (mainSquareSize + textHeight) / 2 - fm.getDescent();

            g.drawString(text, textX, textY);
            return;
        }
    }
    public void drawGame(Graphics g) {
       
        // Draw the main square
        g.setColor(new Color(188, 173, 181));
        g.fillRect(x, y, mainSquareSize, mainSquareSize);

        Color[] barve = {
            new Color(205, 193, 181), new Color(238, 228, 219), new Color(239, 225, 202), 
            new Color(244, 176, 125), new Color(248, 147, 104), new Color(249, 121, 99), 
            new Color(250, 90, 64), new Color(238, 206, 122), new Color(238, 203, 107), 
            new Color(212, 177, 82), new Color(194, 162, 75), new Color(168, 141, 65)
        };

        int pad = smallSquareSize / 5;
        Color color;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (matrika[row][col] == 0) {
                    color = barve[0];
                } else {
                    color = barve[(int) (Math.log(matrika[row][col]) / Math.log(2))];
                }

                g.setColor(color);
                g.fillRect(x + pad + (pad + smallSquareSize) * row, y + pad + (pad + smallSquareSize) * col, smallSquareSize, smallSquareSize);
                g.setColor(Color.BLACK);

                if (matrika[row][col] != 0) {
                    Font font = new Font("Helvetica", Font.BOLD, (int) (smallSquareSize / 2.5));
                    g.setFont(font);

                    FontMetrics fm = g.getFontMetrics();
                    String text = String.valueOf(matrika[row][col]);
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getAscent();

                    int squareX = x + pad + (pad + smallSquareSize) * row;
                    int squareY = y + pad + (pad + smallSquareSize) * col;

                    int textX = squareX + (smallSquareSize - textWidth) / 2;
                    int textY = squareY + (smallSquareSize + textHeight) / 2 - fm.getDescent();

                    g.drawString(text, textX, textY);
                }
            }
        }
    
}

    public static void go(int input) {
        boolean premik = false; // ali smo opravili premik (true: dodamo random število)
        if (input == 37) {
            premik = left();
        } else if (input == 39) {
            premik = right();
        } else if (input == 38) {
            premik = up();
        } else if (input == 40) {
            premik = down();
        }
        
      //ŠTEVILO KI GA BOMO PO VSAKEM OBHODU ZANKE, ČE SE IZVEDE PREMIK, DODALI
        if (premik) {
            double x = Math.random();
            int stevilo = 2;
            if (x > 0.85) stevilo = 4;

            List<int[]> zeros = findAllIndexes(matrika, 0); //INDEKSI KJER NIMAMO ŠTEVIL
            if (!zeros.isEmpty()) {
                Random rand = new Random();
                int index = rand.nextInt(zeros.size());
                int[] par = zeros.get(index);

                matrika[par[0]][par[1]] = stevilo;
            }
        }

     // ZAUSTAVITEV
        if (findAllIndexes(matrika, 0).isEmpty() && end(matrika)) {
            gameOver = true;
        }
    }

    //ZAČETNA MATRIKA
    public static int[][] matrix() {
        int[][] matrika = new int[4][4];
        double x = Math.random();
        double y = Math.random();

        int prva = 2;
        int druga = 2;
        if (x > 0.85) prva = 4;
        if (y > 0.85) druga = 4;
        Random rand = new Random();
        int index1 = rand.nextInt(4);
        int index2 = rand.nextInt(4);
        int index3 = rand.nextInt(4);
        int index4 = rand.nextInt(4);
        while (index1 == index3 && index2 == index4) {
            index1 = rand.nextInt(4);
            index2 = rand.nextInt(4);
            index3 = rand.nextInt(4);
            index4 = rand.nextInt(4);
        }
        matrika[index1][index2] = prva;
        matrika[index3][index4] = druga;

        return matrika;
    }

    public static List<int[]> findAllIndexes(int[][] matrix, int target) { // hkrati uporabimo to še za preverjenje ali smo zmagali igro (dobili število 2048)
        List<int[]> indexes = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == target) {
                    indexes.add(new int[]{i, j});
                }
                if(matrix[i][j] == 2048) {win = true;}
            }
        }
        return indexes;
    }

    public static int[] reverseArray(int[] array) {
        int n = array.length;
        int[] reversedArray = new int[n];
        for (int i = 0; i < n; i++) {
            reversedArray[i] = array[n - 1 - i];
        }
        return reversedArray;
    }
    
    
    public static boolean end(int[][] array) { // PREVERIMO ALI SO KAKŠNA SOSEDNJA ŠTEVILA ENAKA
        int[][] matrika = array;
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                if (matrika[i][j] == matrika[i][j - 1]) return false;
            }
        }
        for (int i = 1; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (matrika[i][j] == matrika[i - 1][j]) return false;
            }
        }
        return true;
    }

    public static boolean left() {
        boolean[][] merged = new boolean[4][4]; //matrika za preverjanje ali so se števila v potezi že seštela 
        boolean premik = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                for (int k = j; k > 0; k--) {
                    if (matrika[i][k] != 0) {
                        if (matrika[i][k - 1] == matrika[i][k] && !merged[i][k] && !merged[i][k - 1]) {
                            matrika[i][k - 1] *= 2;
                            matrika[i][k] = 0;
                            merged[i][k - 1] = true;
                            premik = true;
                        }
                        if (matrika[i][k - 1] == 0) {
                            matrika[i][k - 1] = matrika[i][k];
                            matrika[i][k] = 0;
                            premik = true;
                        }
                    }
                }
            }
        }
        return premik;
    }

    public static boolean right() {
        boolean[][] merged = new boolean[4][4];
        boolean premik = false;
        for (int i = 0; i < 4; i++) {
            matrika[i] = reverseArray(matrika[i]);
            for (int j = 1; j < 4; j++) {
                for (int k = j; k > 0; k--) {
                    if (matrika[i][k] != 0) {
                        if (matrika[i][k - 1] == matrika[i][k] && !merged[i][k] && !merged[i][k - 1]) {
                            matrika[i][k - 1] *= 2;
                            matrika[i][k] = 0;
                            merged[i][k - 1] = true;
                            premik = true;
                        }
                        if (matrika[i][k - 1] == 0) {
                            matrika[i][k - 1] = matrika[i][k];
                            matrika[i][k] = 0;
                            premik = true;
                        }
                    }
                }
            }
            matrika[i] = reverseArray(matrika[i]);
        }
        return premik;
    }

    public static boolean up() {
        boolean[][] merged = new boolean[4][4];
        boolean premik = false;
        for (int i = 1; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = i; k > 0; k--) {
                    if (matrika[k][j] != 0) {
                        if (matrika[k - 1][j] == matrika[k][j] && !merged[k][j] && !merged[k - 1][j]) {
                            matrika[k - 1][j] *= 2;
                            matrika[k][j] = 0;
                            merged[k - 1][j] = true;
                            premik = true;
                        }
                        if (matrika[k - 1][j] == 0) {
                            matrika[k - 1][j] = matrika[k][j];
                            matrika[k][j] = 0;
                            premik = true;
                        }
                    }
                }
            }
        }
        return premik;
    }

    public static boolean down() {
        boolean[][] merged = new boolean[4][4];
        boolean premik = false;
        for (int i = 2; i > -1; i--) {
            for (int j = 0; j < 4; j++) {
                for (int k = i; k < 3; k++) {
                    if (matrika[k][j] != 0) {
                        if (matrika[k + 1][j] == matrika[k][j] && !merged[k][j] && !merged[k + 1][j]) {
                            matrika[k + 1][j] *= 2;
                            matrika[k][j] = 0;
                            merged[k + 1][j] = true;
                            premik = true;
                        } else if (matrika[k + 1][j] == 0) {
                            matrika[k + 1][j] = matrika[k][j];
                            matrika[k][j] = 0;
                            premik = true;
                        }
                    }
                }
            }
        }
        return premik;
    }
}
