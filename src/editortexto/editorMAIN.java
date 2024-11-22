/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editortexto;
import editortexto.Editor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Dell
 */
public class editorMAIN extends JFrame {
    private JTextPane textPane;
    private File archivoAbierto = null;
    private Editor editor;

    public editorMAIN() {
        setSize(800, 500);
        setTitle("Editor de Texto");
        this.setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        archivoAbierto = null; 
        editor = new Editor("nuevoArchivo");
     
        JPanel toolbarPrincipal = new JPanel();
        toolbarPrincipal.setLayout(new FlowLayout(FlowLayout.LEFT));

    
        JButton nuevo = new JButton("Nuevo");
        JButton abrir = new JButton("Abrir");
        JButton guardar = new JButton("Guardar");

        
        toolbarPrincipal.add(nuevo);
        toolbarPrincipal.add(abrir);
        toolbarPrincipal.add(guardar);

        
        JPanel toolbarFuenteTamano = new JPanel();
        toolbarFuenteTamano.setLayout(new FlowLayout(FlowLayout.LEFT));

    
        String[] fuentes = {"Arial", "Verdana", "Tahoma", "Courier New", "Times New Roman"};
        JComboBox<String> comboFuentes = new JComboBox<>(fuentes);
        Integer[] tamanos = {8, 10, 12, 14, 16, 18, 20, 24, 28, 32, 36};
        JComboBox<Integer> comboTamanos = new JComboBox<>(tamanos);

      
        JButton fuenteButton = new JButton("Fuente");
        JButton tamanoButton = new JButton("Tamaño");

        
        toolbarFuenteTamano.add(fuenteButton);
        toolbarFuenteTamano.add(comboFuentes);
        toolbarFuenteTamano.add(tamanoButton);
        toolbarFuenteTamano.add(comboTamanos);

        
        JPanel toolbarFormato = new JPanel();
        toolbarFormato.setLayout(new FlowLayout(FlowLayout.LEFT));

        
        JButton negrita = new JButton("Negrita");
        JButton cursiva = new JButton("Cursiva");
        JButton subrayado = new JButton("Subrayado");
        JButton color = new JButton("Color");

        
        toolbarFormato.add(negrita);
        toolbarFormato.add(cursiva);
        toolbarFormato.add(subrayado);
        toolbarFormato.add(color);

        
        textPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.CENTER);

       
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

        panelPrincipal.add(toolbarPrincipal);
        panelPrincipal.add(toolbarFuenteTamano);
        panelPrincipal.add(toolbarFormato);
        add(panelPrincipal, BorderLayout.NORTH);

       
        abrir.addActionListener(e -> {
            String nombreArchivo = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo a abrir:");
            if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
                abrirArchivo(nombreArchivo);
                editor = new Editor(nombreArchivo); 
                editor.setTextFrame(textPane);
            }
        });

        
        nuevo.addActionListener(e -> {
            String nombreArchivo = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo:");
            if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
                crearArchivoDocx(nombreArchivo);
            }
        });

        
        guardar.addActionListener(e -> {
            guardarCambios(textPane);
        });

       
        negrita.addActionListener(e -> {
            editor.setBold();
        });

        
        cursiva.addActionListener(e -> {
            editor.setItalic();
        });

        
        subrayado.addActionListener(e -> {
            editor.setSubrayado();
        });

        
        color.addActionListener(e -> {
            Color nuevoColor = JColorChooser.showDialog(this, "Seleccionar color", textPane.getForeground());
            if (nuevoColor != null) {
                editor.changeColor(nuevoColor);
            }
        });


        fuenteButton.addActionListener(e -> {
            String fuenteSeleccionada = (String) comboFuentes.getSelectedItem();
            if (fuenteSeleccionada != null) {
                editor.changeFont(fuenteSeleccionada);
            }
        });

        tamanoButton.addActionListener(e -> {
            Integer tamanoSeleccionado = (Integer) comboTamanos.getSelectedItem();
            if (tamanoSeleccionado != null) {
                editor.changeSize(tamanoSeleccionado);
            }
        });
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
        } else {
            JOptionPane.showMessageDialog(this, "Archivo abierto con exito");
        }

        archivoAbierto = archivo;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            StringBuilder contenido = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
            textPane.setText(contenido.toString());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearArchivoDocx(String nombreArchivo) {
        String rutaCarpeta = "Documentos";
        File carpeta = new File(rutaCarpeta);

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        File archivo = new File(carpeta, nombreArchivo + ".rtf");
        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write("");
            JOptionPane.showMessageDialog(this, "Archivo creado exitosamente: " + archivo.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al crear el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void escribirArchivo(File file, String contenido) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(contenido);
        }
    }

    public void guardarCambios(JTextPane textPane) {
        try {
            if (archivoAbierto != null) {
                escribirArchivo(archivoAbierto, textPane.getText());
            } else {
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showSaveDialog(this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    archivoAbierto = fileChooser.getSelectedFile();
                    escribirArchivo(archivoAbierto, textPane.getText());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        editorMAIN ventana = new editorMAIN();
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
    }
}

     

