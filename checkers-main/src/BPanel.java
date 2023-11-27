import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

// This class actually creates the visual board panel that will go inside the actual frame of
// the "game screen."
public class BPanel extends JPanel {

    private Board board;
    private Point clicked;
    private Point released;
    private boolean playBot;
    private JLabel statusLabel;
    public static final int BOARD_WIDTH = 600;
    public static final int BOARD_HEIGHT = 600;

    public BPanel(JLabel status) {
        board = new Board();
        this.playBot = false;
        setFocusable(true);
        this.statusLabel = status;
        this.setSize(new Dimension(600, 600));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clicked = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                released = e.getPoint();
                makeMove(convertPoint(clicked), convertPoint(released));
            }
        });
    }

    private void makeMove(Coord one, Coord two) {
        if (board.playMove(one, two)) {
            repaint();
            updateStatusText();
        }
        this.botMove();
    }

    @Override
   public void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.setSize(new Dimension(600, 600));
    updateStatusText();

    drawGrid(g);
    drawCheckerboard(g);
  }

    private void drawGrid(Graphics g) {
    for (int i = 0; i <= 600; i += 75) {
        g.drawLine(i, 0, i, 600);
        g.drawLine(0, i, 600, i);
    }
}

    private void drawCheckerboard(Graphics g) {
       for (int row = 0; row < 8; row++) {
        for (int col = 0; col < 8; col++) {
            if ((row % 2 == 0 && col % 2 == 1) || (row % 2 == 1 && col % 2 == 0)) {
                g.fillRect(col * 75, row * 75, 75, 75);
            }

            drawPiece(g, row, col);
        }
    }
}

    private void drawPiece(Graphics g, int row, int col) {
      int piece = board.getPiece(row, col);
       if (piece >= 1 && piece <= 4) {
        String imagePath = "files/";
        String imageName;

        switch (piece) {
            case 1 -> imageName = "blackpiece.png";
            case 2 -> imageName = "blackking.png";
            case 3 -> imageName = "whitepiece.png";
            case 4 -> imageName = "whiteking.png";
            default -> throw new IllegalArgumentException("Invalid piece value: " + piece);
        }

        try {
            Image checkerImage = ImageIO.read(new File(imagePath + imageName));
            g.drawImage(checkerImage, col * 75 + 10, row * 75 + 10, null);
        } catch (IOException ignored) {
            // Handle exception (e.g., log it)
        }
      }
   }

    public void reset() {
        board.init();
        repaint();
        requestFocusInWindow();
    }

    public Coord convertPoint(Point p) {
        int x = p.x / 75;
        int y = p.y / 75;
        return new Coord(y, x);
    }

    public void saveGame() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("SAVE FILES", "txt");
        fileChooser.setDialogTitle("Where to save this file?");
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setSelectedFile(new File("savedGame.txt"));
    
        int userChoice = fileChooser.showSaveDialog(this);
    
        if (userChoice == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.toString();
    
            if (!fileName.endsWith(".txt")) {
                fileName = fileName.concat(".txt");
                selectedFile = new File(fileName);
            }
    
            board.saveGame(selectedFile.getAbsoluteFile());
        }
    }
    

    public void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("SAVE FILES", "txt");
        fileChooser.setDialogTitle("Where is the saved file to load?");
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.showOpenDialog(this);
        board.loadGame(fileChooser.getSelectedFile().getAbsoluteFile());
        repaint();
        requestFocusInWindow();
    }

    public void newGame() {
        board.init();
        repaint();
    }

    public void undo() {
        board.undo();
        repaint();
        updateStatusText();
        requestFocusInWindow();
    }

    public void updateStatusText() {
        if (board.isGameOver() && board.getPlayerTurn()) {
            statusLabel.setText("Game over! A fekete nyert!");
        } else if (board.isGameOver() && !board.getPlayerTurn()) {
            statusLabel.setText("Game over! A fehér nyert!");
        } else if (board.getPlayerTurn()) {
            statusLabel.setText("A fekete bábu következik!");
        } else if (!board.getPlayerTurn()) {
            statusLabel.setText("A fehér bábu következik!");
        }
    }

    public void toggleBot() {
        this.playBot = !this.playBot;
        this.botMove();
    }

    private void botMove() {
    if (shouldPlayBot() && !board.getPlayerTurn()) {
        Bot bot = new Bot();
        int[][] tempBoard = copyBoard(board.getBoard());
        // Use the getRandomMove() function instead of the minimax() function
        List<Coord> move = bot.getRandomMove(tempBoard, board.getPlayerTurn());

        // Check if the move is null, meaning there are no possible moves for the bot
        if (move == null) {
            return;
        }

        // Play the move and update the board and the status text
        board.playMove(move.get(0), move.get(1));
        repaint();
        updateStatusText();
    }
}
    private boolean shouldPlayBot() {
        return playBot && !board.getPlayerTurn();
    }
    
    private int[][] copyBoard(int[][] originalBoard) {
        int[][] copiedBoard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(originalBoard[i], 0, copiedBoard[i], 0, 8);
        }
        return copiedBoard;
    }
    
}
