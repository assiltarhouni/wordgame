import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameWindow extends JFrame {
    private List<JButton> letterButtons;
    private ActionListener buttonClickListener;

    public GameWindow(String title, List<Character> letters, ActionListener buttonClickListener) {
        super(title);
        this.buttonClickListener = buttonClickListener;
        letterButtons = new ArrayList<>();
    
        setLayout(new GridLayout(3, 4));
        for (Character letter : letters) {
            JButton button = new JButton(letter.toString());
            button.addActionListener(this.buttonClickListener);
            letterButtons.add(button);
            add(button);
        }
    
        setSize(400, 300); // Set an appropriate size for the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensure the window closes properly
    }
    
    public void setButtonClickListener(ActionListener listener) {
        this.buttonClickListener = listener;
        for (JButton button : letterButtons) {
            button.removeActionListener(button.getActionListeners()[0]); // Remove existing listeners
            button.addActionListener(this.buttonClickListener);
        }
    }
    
    
    private void handleButtonClick(ActionEvent e) {
        char clickedLetter = ((JButton) e.getSource()).getText().charAt(0);
        // Handle the clicked letter logic here
        System.out.println("Clicked letter: " + clickedLetter);
        disableLetterButton(clickedLetter);
    }
    public void disableLetterButtons() {
        for (JButton button : letterButtons) {
            button.setEnabled(false);
        }
    }

    public void enableAllLetterButtons() {
        for (JButton button : letterButtons) {
            button.setEnabled(true);
        }
    }

    public void disableLetterButton(char letter) {
        for (JButton button : letterButtons) {
            if (button.getText().charAt(0) == letter) {
                button.setEnabled(false);
                break;
            }
        }
    }

    public char getClickedLetter(ActionEvent e) {
        return ((JButton) e.getSource()).getText().charAt(0);
    }
}
