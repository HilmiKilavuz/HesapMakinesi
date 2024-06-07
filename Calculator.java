import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

// Yuvarlak kenarlı buton sınıfı
class RoundedButton extends JButton {
    private int radius;

    // Yuvarlak buton oluşturucu
    public RoundedButton(String label, int radius) {
        super(label);
        this.radius = radius;
        setOpaque(false); // Özelleştirilmiş boyamayı sağlar
        setBorderPainted(false); // Kenarlık kaldırılır
    }

    // Butonun bileşenini boyamak için kullanılır
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Arka planı boyar
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }
}

// Hesap makinesi sınıfı, JFrame ve ActionListener'dan türetilir
public class Calculator extends JFrame implements ActionListener {
    private JTextField display; // Sonuç ekranı
    private JPanel panel; // Butonların yer aldığı panel
    private String operator; // İşlemci (örn: +, -, *, /)
    private double num1, num2, result; // Hesaplamalar için sayılar ve sonuç
    private Stack<String> history; // Hesaplama geçmişi

    // Hesap makinesi oluşturucu
    public Calculator() {
        history = new Stack<>();
        
        // Özelleştirilmiş fontu yükler
        Font customFont = loadCustomFont("Poppins-Bold.ttf");

        display = new JTextField();
        display.setEditable(false); // Sonuç ekranı düzenlenemez
        display.setHorizontalAlignment(JTextField.RIGHT); // Metin sağa hizalanır
        display.setFont(customFont.deriveFont(Font.BOLD, 20)); // Font ayarlanır
        display.setPreferredSize(new Dimension(400, 50)); // Boyut ayarlanır

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 10, 10)); // 5x4'lik ızgara düzeni

        panel.setBackground(new Color(36, 7, 80)); // Panel arka plan rengi ayarlanır
        String[] buttons = {
            "C", "SİL", "√", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "x²", "="
        };

        // Butonların renkleri
        Color[] colors = {
            new Color(87, 166, 161), new Color(87, 166, 161), new Color(87, 123, 141), new Color(87, 123, 141),
            new Color(52, 76, 100), new Color(52, 76, 100), new Color(52, 76, 100), new Color(87, 123, 141),
            new Color(52, 76, 100), new Color(52, 76, 100), new Color(52, 76, 100), new Color(87, 123, 141),
            new Color(52, 76, 100), new Color(52, 76, 100), new Color(52, 76, 100), new Color(87, 123, 141),
            new Color(52, 76, 100), new Color(87, 166, 161), new Color(87, 123, 141), new Color(204, 204, 204)
        };

        // Butonları oluştur ve panele ekle
        for (int i = 0; i < buttons.length; i++) {
            RoundedButton button = new RoundedButton(buttons[i], 30); // Radius 30 olarak ayarlanır
            button.setFont(customFont.deriveFont(Font.BOLD, 20)); // Font ayarlanır
            button.setBackground(colors[i]); // Arka plan rengi ayarlanır
            button.setFocusPainted(false); // Fokus boyama kapatılır
            button.addActionListener(this); // ActionListener eklenir
            panel.add(button);
        }

        // JFrame düzeni ayarlanır ve bileşenler eklenir
        setLayout(new BorderLayout(10, 10));
        add(display, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        // JFrame arka plan rengi ayarlanır
        getContentPane().setBackground(new Color(36, 7, 80));

        setTitle("Hesap Makinesi");
        setSize(400, 500); // Boyut ayarlanır
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Pencere ekranın ortasında açılır
        setVisible(true); // Pencere görünür yapılır
    }

    // Özelleştirilmiş fontu yükleme metodu
    private Font loadCustomFont(String fontPath) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, 20); // Yedek font
        }
    }

    // Butonlara basıldığında yapılacak işlemler
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if ((command.charAt(0) >= '0' && command.charAt(0) <= '9') || command.equals(".")) {
            display.setText(display.getText() + command); // Sayı veya nokta ekle
        } else if (command.equals("=")) {
            try {
                num2 = Double.parseDouble(display.getText());

                // İşlemleri gerçekleştir
                switch (operator) {
                    case "+": result = num1 + num2; break;
                    case "-": result = num1 - num2; break;
                    case "*": result = num1 * num2; break;
                    case "/": 
                        if (num2 != 0) {
                            result = num1 / num2;
                        } else {
                            display.setText("Error");
                            return;
                        }
                        break;
                }

                display.setText(String.valueOf(result)); // Sonucu göster
                history.push(num1 + " " + operator + " " + num2 + " = " + result); // İşlem geçmişine ekle
                operator = "";
            } catch (NumberFormatException ex) {
                display.setText("Error");
            }
        } else if (command.equals("C")) {
            display.setText(""); // Ekranı temizle
        } else if (command.equals("SİL")) {
            String currentText = display.getText();
            if (currentText.length() > 0) {
                display.setText(currentText.substring(0, currentText.length() - 1)); // Son karakteri sil
            }
        } else if (command.equals("√")) {
            try {
                double value = Double.parseDouble(display.getText());
                result = Math.sqrt(value); // Kare kök hesapla
                display.setText(String.valueOf(result));
            } catch (NumberFormatException ex) {
                display.setText("Error");
            }
        } else if (command.equals("x²")) {
            try {
                double value = Double.parseDouble(display.getText());
                result = value * value; // Karesini al
                display.setText(String.valueOf(result));
            } catch (NumberFormatException ex) {
                display.setText("Error");
            }
        } else {
            if (!display.getText().equals("")) {
                try {
                    num1 = Double.parseDouble(display.getText());
                    operator = command; // İşlemciyi ayarla
                    display.setText("");
                } catch (NumberFormatException ex) {
                    display.setText("Error");
                }
            }
        }
    }

    // Programın başlangıç noktası
    public static void main(String[] args) {
        new Calculator();
    }
}
