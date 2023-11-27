import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePanel implements Runnable {

    @Override
    public void run() {
        final JFrame frame = new JFrame("Dámajáték");
        frame.getContentPane().setLayout(null);

        JPanel statusPanel = new JPanel();
        JLabel statusLabel = new JLabel("A fekete bábu következik!");
        statusPanel.add(statusLabel);
        statusPanel.setBounds(0, 0, 600, 20);
        frame.add(statusPanel);

        BPanel boardPanel = new BPanel(statusLabel);
        boardPanel.setSize(new Dimension(100, 100));
        frame.add(boardPanel, BorderLayout.CENTER);
        boardPanel.setLocation(10, 50);
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        JPanel buttonsPanel = new JPanel();
        JButton undoBtn = new JButton("Vissza");

        undoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardPanel.undo();
            }
        });

        JButton saveBtn = new JButton("Mentés");

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardPanel.saveGame();
            }
        });

        JButton loadBtn = new JButton("Betöltés");

        loadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardPanel.loadGame();
            }
        });

        JButton newGameBtn = new JButton("Új játék");

        newGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardPanel.newGame();
            }
        });

        JButton backBtn = new JButton("Szabályok");

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,  "A dáma egy 2 személyes táblajáték, hasonlóan a sakkhoz, egy 8x8-as táblán játszák,\r\n" +
            "ellentétben a bábukkal, itt csak sima korongok vannak. A játékosok fehér és fekete bábukat\r\n" +
            "használnak, amelyeket a tábla négy sarkától indulva helyeznek el. A bábukkal csak előre és\r\n" +
            "átlósan lehet lépni egyet, ütni pedig akkor lehet, ha az ellenfél bábuja közvetlen a sajátjával\r\n" +
            "van átlósa, és a mögötte lévő mező üres. Ilyenkor „átugorja” és leüti. Ha az egyik játékos az\r\n" +
            "bábujával átér az ellenfél oldalára, akkor „átváltozik” egy dáma bábuvá, ami már hátrafele is\r\n" +
            "tud lépni, ugyanazokkal a szabályokkal. A játék célja az ellenfél összes bábújának leütése.\r\n");
		}
            }
        );

        JButton botBtn = new JButton("VS Bot");

        botBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardPanel.toggleBot();
            }
        });

        JButton exitBtn = new JButton("Kilépés");

        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonsPanel.add(newGameBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(150, 20)));
        buttonsPanel.add(botBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(150, 20)));
        buttonsPanel.add(saveBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(150, 20)));
        buttonsPanel.add(loadBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(150, 20)));
        buttonsPanel.add(undoBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(150, 20)));
        buttonsPanel.add(backBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(150, 20)));
        buttonsPanel.add(exitBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(150, 20)));
        
        frame.add(buttonsPanel);
        buttonsPanel.setBounds(600, 100, 150, 600);

        frame.setResizable(false);
        frame.setIconImage(new ImageIcon("files/blackking.png").getImage());
        frame.setSize(new Dimension(800, 800));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
