import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

enum status {
    NOT_STARTED("Game is not started"),
    IN_PROGRESS("The turn of"),
    WIN("wins"),
    DRAW("Draw");

    String nazwa;

    status(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getNazwa() {
        return nazwa;
    }
}

public class TicTacToe extends JFrame implements ActionListener {
    //JFrame frame;
    JButton[][] buttons = new JButton[3][3];
    JButton ButtonStartReset = new JButton();
    JButton ButtonPlayer1 = new JButton();
    JButton ButtonPlayer2 = new JButton();
    JLabel LabelStatus = new JLabel();

    JMenuBar menuBar = new JMenuBar();
    JMenu MenuGame = new JMenu("Menu");

    JMenuItem MenuHumanHuman = new JMenuItem("Human vs Human");
    JMenuItem MenuHumanRobot = new JMenuItem("Human vs Robot");
    JMenuItem MenuRobotHuman = new JMenuItem("Robot vs Human");
    JMenuItem MenuRobotRobot = new JMenuItem("Robot vs Robot");
    JMenuItem MenuExit = new JMenuItem("Exit");

    boolean active = false;
    int ruch = 0;

    public TicTacToe() {

        /*frame = new JFrame("Tic Tac Toe");
        frame.setTitle("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 450);
        frame.setResizable(false);
        frame.setVisible(true);*/

        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 450);
        setResizable(false);
        setVisible(true);

        ButtonStartReset.setText("Start");
        ButtonStartReset.setName("ButtonStartReset");
        ButtonStartReset.addActionListener(this);
        ButtonStartReset.setFocusPainted(false);


        ButtonPlayer1.setText("Human");
        ButtonPlayer1.setName("ButtonPlayer1");
        ButtonPlayer1.addActionListener(this);
        ButtonPlayer1.setFocusPainted(false);

        ButtonPlayer2.setText("Human");
        ButtonPlayer2.setName("ButtonPlayer2");
        ButtonPlayer2.addActionListener(this);
        ButtonPlayer2.setFocusPainted(false);

        JPanel toolBar = new JPanel();
        toolBar.add(ButtonPlayer1);
        toolBar.add(ButtonStartReset);
        toolBar.add(ButtonPlayer2);
        toolBar.setLayout(new GridLayout(1, 3));

        createMenu();

        JPanel plansza = new JPanel();
        //plansza.setBounds(0, 30, 400, 400);
        plansza.setLayout(new GridLayout(3, 3));


        //frame.add(toolBar, BorderLayout.PAGE_START);
        add(toolBar, BorderLayout.PAGE_START);

        char alphabet = 'A';
        for (Integer i = 0; i < 3; ++i) {
            for (Integer j = 0; j < 3; ++j) {
                Integer pom = 3 - i;
                buttons[i][j] = new JButton();
                buttons[i][j].setName("Button" + alphabet + pom.toString());
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setText(" ");
                plansza.add(buttons[i][j]);
                buttons[i][j].addActionListener(this);
                buttons[i][j].setEnabled(false);
                alphabet++;
            }
            alphabet = 'A';
        }
        //frame.add(plansza);
        add(plansza);

        LabelStatus.setText(status.NOT_STARTED.getNazwa());
        LabelStatus.setName("LabelStatus");
        //frame.add(LabelStatus, BorderLayout.PAGE_END);
        add(LabelStatus, BorderLayout.PAGE_END);
    }

    public void createMenu() {
        setJMenuBar(menuBar);
        MenuGame.setMnemonic(KeyEvent.VK_F);
        MenuGame.setName("MenuGame");
        menuBar.add(MenuGame);

        MenuHumanHuman.setName("MenuHumanHuman");
        MenuHumanRobot.setName("MenuHumanRobot");
        MenuRobotHuman.setName("MenuRobotHuman");
        MenuRobotRobot.setName("MenuRobotRobot");
        MenuExit.setName("MenuExit");

        MenuGame.add(MenuHumanHuman);
        MenuGame.add(MenuHumanRobot);
        MenuGame.add(MenuRobotHuman);
        MenuGame.add(MenuRobotRobot);
        MenuGame.addSeparator();
        MenuGame.add(MenuExit);

        MenuHumanHuman.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                changePlayer(event);
            }
        });
        MenuHumanRobot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                changePlayer(event);
            }
        });
        MenuRobotHuman.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                changePlayer(event);
            }
        });
        MenuRobotRobot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                changePlayer(event);
            }
        });
        MenuExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ButtonStartReset) {
            if(ButtonStartReset.getText().equals("Start")) {
                startGame();
            } else {
                resetGame();
            }
        } else if ((e.getSource() == ButtonPlayer1 || e.getSource() == ButtonPlayer2) && !active) {
            changePlayer(e);
        } else if (e.getSource() == menuBar && !active) {
            changePlayer(e);
        } else if (active) {
            ButtonStartReset.setText("Reset");
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    if (e.getSource() == buttons[i][j] && buttons[i][j].getText().equals(" ")) {
                        if (ruch % 2 == 0) {
                            buttons[i][j].setText("X");
                            LabelStatus.setText(status.IN_PROGRESS.getNazwa() + " " + ButtonPlayer2.getText() + " Player (O)");

                        } else {
                            buttons[i][j].setText("O");
                            LabelStatus.setText(status.IN_PROGRESS.getNazwa() + " " + ButtonPlayer1.getText() + " Player (X)");
                        }
                        ruch++;
                        if(ruch > 4) {
                            isWon();
                        }
                        if(ruch == 9) {
                            draw();
                        }
                    }
                }
            }
            if(ButtonPlayer1.getText().equals("Robot") && ruch % 2 == 0) {
                computerMove();
            }
            if(ButtonPlayer2.getText().equals("Robot") && ruch % 2 != 0) {
                computerMove();
            }
        }
    }

    public void isWon() {
       for (int i = 0; i < 3; ++i) {
           if (buttons[i][0].getText().equals(buttons[i][1].getText()) && buttons[i][0].getText().equals(buttons[i][2].getText()) && !buttons[i][0].getText().equals(" ")) {    //kolumny
               endGame(buttons[i][0].getText());
           }
           if (buttons[0][i].getText().equals(buttons[1][i].getText()) && buttons[0][i].getText().equals(buttons[2][i].getText()) && !buttons[0][i].getText().equals(" ")) {    //wiersze
               endGame(buttons[0][i].getText());
           }
       }
       if (buttons[0][0].getText().equals(buttons[1][1].getText()) && buttons[0][0].getText().equals(buttons[2][2].getText()) && !buttons[0][0].getText().equals(" ")) {    //diagonalne
           endGame(buttons[0][0].getText());
       }
       if (buttons[0][2].getText().equals(buttons[1][1].getText()) && buttons[0][2].getText().equals(buttons[2][0].getText()) && !buttons[0][2].getText().equals(" ")) {    //diagonalne
           endGame(buttons[0][2].getText());
       }
    }

    public void endGame(String winner) {
        ruch = 0;
        active = false;
        if (winner.equals("X")) {
            LabelStatus.setText("The " + ButtonPlayer1.getText() + " Player (" + winner + ") " + status.WIN.getNazwa());
        } else {
            LabelStatus.setText("The " + ButtonPlayer2.getText() + " Player (" + winner + ") " + status.WIN.getNazwa());
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    public void changePlayer(ActionEvent e) {
        if(e.getSource() == MenuHumanHuman) {
            ButtonPlayer1.setText("Human");
            ButtonPlayer2.setText("Human");
            startGame();
        } else if (e.getSource() == MenuHumanRobot) {
            ButtonPlayer1.setText("Human");
            ButtonPlayer2.setText("Robot");
            startGame();
        } else if (e.getSource() == MenuRobotHuman) {
            ButtonPlayer1.setText("Robot");
            ButtonPlayer2.setText("Human");
            startGame();
        } else if (e.getSource() == MenuRobotRobot) {
            ButtonPlayer1.setText("Robot");
            ButtonPlayer2.setText("Robot");
            startGame();
        } else if(e.getSource() == ButtonPlayer1) {
            if(ButtonPlayer1.getText().equals("Robot")) {
                ButtonPlayer1.setText("Human");
            } else {
                ButtonPlayer1.setText("Robot");
            }
        } else {
            if(ButtonPlayer2.getText().equals("Robot")) {
                ButtonPlayer2.setText("Human");
            } else {
                ButtonPlayer2.setText("Robot");
            }
        }
    }

    public void resetGame() {
        LabelStatus.setText(status.NOT_STARTED.getNazwa());
        ButtonStartReset.setText("Start");
        ruch = 0;
        active = false;
        ButtonPlayer1.setEnabled(true);
        ButtonPlayer2.setEnabled(true);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                buttons[i][j].setText(" ");
                buttons[i][j].setEnabled(false);
            }
        }
    }

    public void startGame() {
        LabelStatus.setText(status.IN_PROGRESS.getNazwa() + " " + ButtonPlayer1.getText() + " Player (X)");
        ButtonStartReset.setText("Reset");
        ruch = 0;
        active = true;
        ButtonPlayer1.setEnabled(false);
        ButtonPlayer2.setEnabled(false);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                buttons[i][j].setText(" ");
                buttons[i][j].setEnabled(true);
            }
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (ButtonPlayer1.getText().equals("Robot")) {
            computerMove();
        }
    }

    public void draw() {
        active = false;
        LabelStatus.setText(status.DRAW.getNazwa());
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    public void computerMove() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (buttons[i][j].getText().equals(" ")) {
                    buttons[i][j].doClick();
                    return;
                }
            }
        }
    }
}