import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Die extends JButton {

    private int radius;
    boolean isLocked ;
    boolean isFixed ;

    Die(String label, int radius) {
      super(label);
      this.radius = radius;
      setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
      g.setColor(getBackground());
      g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
      super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
      g.setColor(getForeground());
      g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
    }
}



public class Tenzies {

  int frameWidth = 1000;
  int frameHeight = 900;
  int rows = 2;
  int cols = 5;

  JFrame frame = new JFrame("Tenzies");
  JPanel page = new JPanel(new BorderLayout());
  JPanel textPanel = new JPanel(new BorderLayout());
  JPanel boardDiv = new JPanel(new BorderLayout());
  JLabel textLabel = new JLabel();
  JLabel alertLabel = new JLabel();
  JPanel boardPanel = new JPanel(new GridLayout(rows,cols));

  Die[][] board = new Die[rows][cols];
  JButton rollButton = new JButton("Roll");
  JButton resetButton = new JButton("Reset");
  boolean gameOver = false;
  int turns = 0;
  String textContent = "Tenzies - Turns:" + Integer.toString(turns);
  String alertContent = "";

  Tenzies() {

    frame.setLayout(new BorderLayout());
    frame.setVisible(true);
    frame.setSize(frameWidth, frameHeight);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    int fullDivPadding = 20;
    page.setBorder(BorderFactory.createEmptyBorder(fullDivPadding, fullDivPadding, fullDivPadding, fullDivPadding));
    page.setBackground(Color.decode("#FFFFFF"));

    textPanel.setBackground(Color.decode("#FFFFFF"));
    textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    int padding = 20;
    boardDiv.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
    boardDiv.setBackground(Color.decode("#FF0000"));
    boardDiv.setSize(500, 350);

    textLabel.setBackground(Color.getColor("#FFFFFF"));
    textLabel.setForeground(Color.getColor("#000000"));
    textLabel.setFont(new Font("Arial", Font.BOLD, 50));
    textLabel.setHorizontalAlignment(JLabel.CENTER);
    textLabel.setOpaque(true);
    textLabel.setText(textContent);
    
    alertLabel.setBackground(Color.getColor("#FFFFFF"));
    alertLabel.setForeground(Color.getColor("#000000"));
    alertLabel.setFont(new Font("Arial", Font.BOLD, 20));
    alertLabel.setHorizontalAlignment(JLabel.CENTER);
    alertLabel.setText(alertContent);
    alertLabel.setOpaque(true);
    alertLabel.setSize(200, 100);

    boardPanel.setBackground(Color.getColor("#FFFFFF"));

    rollButton.setBackground(Color.decode("#59E391"));
    rollButton.setPreferredSize(new Dimension(200, 100));
    rollButton.setMaximumSize(new Dimension(200, 100));
    resetButton.setBackground(Color.decode("#59E391"));
    resetButton.setPreferredSize(new Dimension(200, 100));
    resetButton.setMaximumSize(new Dimension(200, 100));
    JPanel buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.setBackground(Color.decode("#FFFFFF"));
    
    textPanel.add(textLabel, BorderLayout.NORTH);
    textPanel.add(alertLabel, BorderLayout.CENTER);
    
    buttonPanel.add(rollButton, BorderLayout.NORTH);
    buttonPanel.add(resetButton, BorderLayout.SOUTH);
    boardDiv.add(boardPanel, BorderLayout.WEST);
    boardDiv.add(buttonPanel, BorderLayout.EAST);

    page.add(textPanel, BorderLayout.NORTH);
    page.add(boardDiv, BorderLayout.CENTER);
    frame.add(page, BorderLayout.CENTER);

    for(int r=0; r<rows; r++) {
      for(int c=0; c< cols; c++) {

        Die die = new Die(null, 20) ;
        die.setSize(100,100);
        die.isLocked = false;
        board[r][c] = die;

        JPanel emptyPanel = new JPanel(new BorderLayout());
        int margin = 20;
        emptyPanel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        emptyPanel.setBackground(Color.decode("#FFFFFF"));
        emptyPanel.setSize(120,120) ;

        emptyPanel.add(die, BorderLayout.CENTER);

        boardPanel.add(emptyPanel);

        die.setBackground(Color.decode("#FFFFFF"));
        die.setForeground(Color.decode("#000000"));
        die.setFont(new Font("Arial", Font.BOLD, 50));
        die.setFocusable(false);
        die.setText(Integer.toString((int)(Math.random()*6)+1));

        die.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if(gameOver) return;
            Die die = (Die) e.getSource();
            if(die.isLocked && !die.isFixed) {
              die.setBackground(Color.decode("#FFFFFF"));
              die.isLocked = false;
              return;
            } else if(!die.isLocked) {
              die.setBackground(Color.decode("#3770FF"));
              die.isLocked = true;
            }
            checkWin(0);
          }
        });
      }
    }

    rollButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        checkWin(1);
        if(gameOver) {
          return;
        }
        if(!isValid()) {
          return;
        }

        for(int r=0; r<rows; r++) {
          for(int c=0; c< cols; c++) {
            Die die = board[r][c];
            if(die.isLocked) {
              die.isFixed = true;
            } else {
              die.setText(Integer.toString((int)(Math.random()*6)+1));
            }
          }
        }

        turns++;
        textLabel.setText("Tenzies - Turns:" + Integer.toString(turns));
      }
    });

    resetButton.addActionListener( new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        for(int r=0; r<rows; r++) {
          for(int c=0; c< cols; c++) {
            Die die = board[r][c];
            die.isLocked = false;
            die.isFixed = false;
            die.setBackground(Color.decode("#FFFFFF"));
            die.setText(Integer.toString((int)(Math.random()*6)+1));
          }
        }

        turns = 0;
        textLabel.setText("Tenzies - Turns:" + Integer.toString(turns));
        alertLabel.setText("");
        gameOver = false;
      }
    
    });
  }

  boolean isValid () {

    String firstLockedValue = "" ; 
    boolean valid = true ;

    for(int r=0; r<rows; r++) {
      for(int c=0; c< cols; c++) {
        Die die = board[r][c];
        if(die.isLocked && firstLockedValue.equals("")) {
          firstLockedValue = die.getText() ;
        } else if(die.isLocked && !die.getText().equals(firstLockedValue)) {
          valid = false ;
          break;
        }
      }
    }

    if(!valid) {
      alertLabel.setText("Invalid move");
    } else {
      alertLabel.setText("");
    }

    return valid ;
  }

  void checkWin(int flag) {

    String firstValue = board[0][0].getText();
    boolean allSame = true;
    boolean allLocked = true;

    for(int r=0; r<rows; r++) {
      for(int c=0; c<cols; c++) {
        Die die = board[r][c];
        if(!die.isLocked) {
          allLocked = false;
          break;
        }
        if(!die.getText().equals(firstValue)) {
          allSame = false;
          break;
        }
      }
    }

    if(allSame && allLocked) {
      alertLabel.setText("You won!");
      gameOver = true;
    } else if(turns == 10 && flag==1) {
      alertLabel.setText("You lost!");
      gameOver = true;
    }

  }

}