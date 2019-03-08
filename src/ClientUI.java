import javax.swing.*;

public class ClientUI {
    private JFrame frame = new JFrame("Login Interface");
    private JPanel panel = new JPanel();
    private JLabel userLabel = new JLabel("username:");
    private JTextField userText = new JTextField();
    private JLabel passwordLabel = new JLabel("password:");
    private JPasswordField passwordText = new JPasswordField();
    private JButton loginButton = new JButton("login");
    private JButton registerButton = new JButton("register");

    public ClientUI() {
        frame.setSize(350, 200);
        panel.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        init();
        frame.setVisible(true);
    }

    public void init() {
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);
        loginButton.setBounds(60, 80, 80, 25);
        panel.add(loginButton);
        registerButton.setBounds(200, 80, 80, 25);
        panel.add(registerButton);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }


    public void successfulLogin() {
        JOptionPane.showMessageDialog(frame, "You are online", "Login success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void loginFailed() {
        JOptionPane.showMessageDialog(frame, "Wrong password/username", "Login failed", JOptionPane.ERROR_MESSAGE);
    }

    public JTextField getUserText() {
        return userText;
    }

    public JPasswordField getPasswordText() {
        return passwordText;
    }

    public JPanel getPanel() {
        return panel;
    }

    public JLabel getUserLabel() {
        return userLabel;
    }

    public JLabel getPasswordLabel() {
        return passwordLabel;
    }

    public JButton getRegisterButton() {
        return registerButton;
    }

    public JFrame getFrame() {
        return frame;
    }

    public JButton getLoginButton() {
        return loginButton;
    }
}
