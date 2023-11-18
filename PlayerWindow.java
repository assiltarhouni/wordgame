import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PlayerWindow extends JFrame {
    private JTextField playerNameField;
    private JButton submitButton;
    private JTextArea wordTextArea;

    public PlayerWindow(String title) {
        super(title);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        playerNameField = new JTextField();
        submitButton = new JButton("Submit");
        wordTextArea = new JTextArea();

        add(playerNameField, BorderLayout.NORTH);
        add(submitButton, BorderLayout.SOUTH);
        add(new JScrollPane(wordTextArea), BorderLayout.CENTER);

        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public String getPlayerName() {
        return playerNameField.getText();
    }

    public void addSubmitListener(ActionListener listener) {
        submitButton.addActionListener(listener);
    }

    public void addLetter(char letter) {
        wordTextArea.append(String.valueOf(letter));
    }

    public String proposeWord() {
        return wordTextArea.getText().toUpperCase();
    }
}
