package editortexto;

import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;


import java.io.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class editorMAIN_2 extends JFrame {

    private JTextPane textPane;
    private File archivoAbierto = null;
    private Editor editor;
    private JPanel sidebarPanel;
    private JLabel font, color, tamaño;
    private JButton colorPick;
    private JComboBox<String> fontComboBox, sizeComboBox;
    private JPanel colorBoxPanel;
    private Color[] selectedColors = new Color[16];

    public editorMAIN_2() {
        setTitle("EDITOR DE TEXTO");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        textPane = new JTextPane();
        textPane.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.CENTER);

        editor = new Editor("myfile.rtf");
        editor.setTextFrame(textPane);

        JPanel fileButtonsPanel = new JPanel();
        JButton nuevo = new JButton("Nuevo");
        nuevo.addActionListener(e -> {
            String nombreArchivo = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo:");
            if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
                crearArchivoDocx(nombreArchivo);
            }
        });

        JButton abrir = new JButton("Abrir");
        abrir.addActionListener(e -> {
            String nombreArchivo = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo a abrir:");
            if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
                abrirArchivo(nombreArchivo);
            }
        });

        JButton guardar = new JButton("Guardar");
        guardar.addActionListener(e -> guardarCambios());

        fileButtonsPanel.add(nuevo);
        fileButtonsPanel.add(abrir);
        fileButtonsPanel.add(guardar);

        //SIDEBAR
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setPreferredSize(new Dimension(300, 180));
        sidebarPanel.setBackground(Color.LIGHT_GRAY);
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        controlPanel.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // FONT SECTION
        gbc.gridx = 0;
        gbc.gridy = 0;
        font = new JLabel("Fuente:");
        font.setPreferredSize(new Dimension(100, 25));
        controlPanel.add(font, gbc);

        gbc.gridx = 1;
        fontComboBox = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontComboBox.setSelectedItem("Arial");
        fontComboBox.setPreferredSize(new Dimension(130, 25));
        fontComboBox.addActionListener(e -> changeFont());
        controlPanel.add(fontComboBox, gbc);

        // SIZE SECTION
        gbc.gridx = 0;
        gbc.gridy = 1;
        tamaño = new JLabel("Tamaño:");
        tamaño.setPreferredSize(new Dimension(100, 25));
        controlPanel.add(tamaño, gbc);

        gbc.gridx = 1;
        String[] sizes = {"10", "12", "14", "16", "18", "20", "24", "28", "32"};
        sizeComboBox = new JComboBox<>(sizes);
        sizeComboBox.setEditable(true);
        sizeComboBox.setSelectedItem("14");
        sizeComboBox.setPreferredSize(new Dimension(130, 25));
        sizeComboBox.addActionListener(e -> changeSize());
        controlPanel.add(sizeComboBox, gbc);

        // COLOR SECTION
        gbc.gridx = 0;
        gbc.gridy = 2;
        color = new JLabel("Color:");
        color.setPreferredSize(new Dimension(100, 25));
        controlPanel.add(color, gbc);

        gbc.gridx = 1;
        colorPick = new JButton("Escoger Color");
        colorPick.setPreferredSize(new Dimension(130, 25));
        colorPick.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Seleccionar Color", textPane.getForeground());
            if (newColor != null) {
                MutableAttributeSet attrs = new SimpleAttributeSet();
                StyleConstants.setForeground(attrs, newColor);
                textPane.getInputAttributes().addAttributes(attrs);
                updateColorBox(newColor);

            }
        });
        controlPanel.add(colorPick, gbc);

        sidebarPanel.add(controlPanel, BorderLayout.WEST);

        JPanel colorBoxContainer = new JPanel();
        colorBoxContainer.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        colorBoxContainer.setBackground(Color.LIGHT_GRAY);

        colorBoxPanel = new JPanel();
        colorBoxPanel.setLayout(new GridLayout(4, 4, 5, 5));
        colorBoxPanel.setPreferredSize(new Dimension(120, 120));
        colorBoxPanel.setBackground(Color.LIGHT_GRAY);

        for (int i = 0; i < 16; i++) {
            JButton colorBoxButton = new JButton();
            colorBoxButton.setBackground(Color.WHITE);
            colorBoxButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            colorBoxButton.addActionListener(e -> setTextColorFromBox(colorBoxButton));
            colorBoxPanel.add(colorBoxButton);
        }

        colorBoxContainer.add(colorBoxPanel);
        sidebarPanel.add(colorBoxContainer, BorderLayout.EAST);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(fileButtonsPanel, BorderLayout.NORTH);
        topPanel.add(sidebarPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void openFile() {
        editor.abrirFile();
    }

    private void saveFile() {
        editor.guardarFile();
    }

    private void changeFont() {
        String selectedFont = (String) fontComboBox.getSelectedItem();
        if (selectedFont != null) {
            Font currentFont = textPane.getFont();
            MutableAttributeSet attrs = new SimpleAttributeSet();
            StyleConstants.setFontFamily(attrs, selectedFont);
            textPane.getInputAttributes().addAttributes(attrs);
        }
    }

    private void changeSize() {
        String selectedSize = (String) sizeComboBox.getSelectedItem();
        if (selectedSize != null) {
            try {
                int fontSize = Integer.parseInt(selectedSize);
                Font currentFont = textPane.getFont();
                MutableAttributeSet attrs = new SimpleAttributeSet();
                StyleConstants.setFontSize(attrs, fontSize);
                textPane.getInputAttributes().addAttributes(attrs);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Número inválido. Por favor, ingrese un número entero.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
                sizeComboBox.setSelectedItem("14");
            }
        }
    }

    private void setTextColorFromBox(JButton colorBoxButton) {
        Color selectedColor = colorBoxButton.getBackground();

        MutableAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setForeground(attrs, selectedColor);
        textPane.getInputAttributes().addAttributes(attrs);
    }

    private void updateColorBox(Color newColor) {
        for (int i = 0; i < 16; i++) {
            if (selectedColors[i] == null) {
                selectedColors[i] = newColor;
                break;
            }
        }
        for (int i = 0; i < 16; i++) {
            JButton colorBoxButton = (JButton) colorBoxPanel.getComponent(i);
            if (selectedColors[i] != null) {
                colorBoxButton.setBackground(selectedColors[i]);
            }
        }
    }

    private void abrirArchivo(String nombreArchivo) {
        String rutaCarpeta = "Documentos";
        File carpeta = new File(rutaCarpeta);

        if (!carpeta.exists()) {
            JOptionPane.showMessageDialog(this, "La carpeta 'Documentos' no existe.");
            return;
        }

        File archivo = new File(carpeta, nombreArchivo + ".rtf");

        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(this, "El archivo no se encuentra en la carpeta 'Documentos'.");
            return;
        }

        archivoAbierto = archivo;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            StringBuilder contenido = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
            textPane.setText(contenido.toString());
            JOptionPane.showMessageDialog(this, "Archivo abierto correctamente.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir el archivo: " + ex.getMessage());
        }
    }

    private void crearArchivoDocx(String nombreArchivo) {
        String rutaCarpeta = "Documentos";
        File carpeta = new File(rutaCarpeta);

        if (!carpeta.exists()) {
            carpeta.mkdir();
        }

        File archivo = new File(carpeta, nombreArchivo + ".rtf");

        if (archivo.exists()) {
            JOptionPane.showMessageDialog(this, "El archivo ya existe en la carpeta 'Documentos'.");
        } else {
            try {
                archivo.createNewFile();
                JOptionPane.showMessageDialog(this, "Archivo creado correctamente.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al crear el archivo: " + ex.getMessage());
            }
        }
    }

    private void guardarCambios() {
        if (archivoAbierto != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoAbierto))) {
                writer.write(textPane.getText());
                JOptionPane.showMessageDialog(this, "Cambios guardados.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar los cambios: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new editorMAIN_2().setVisible(true));
    }
}
