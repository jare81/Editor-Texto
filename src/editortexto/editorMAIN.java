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
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import javax.swing.text.rtf.RTFEditorKit;

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

        JComboBox<String> comboFuentes = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
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
            editor.setTextFrame(textPane);
            guardarCambios(textPane);
        });

        negrita.addActionListener(e -> {
            if (validarArchivoAbierto()) {
                editor.setTextFrame(textPane);
                editor.setBold();
            }
        });

        cursiva.addActionListener(e -> {
            if (validarArchivoAbierto()) {
                editor.setTextFrame(textPane);
                editor.setItalic();
            }
        });

        subrayado.addActionListener(e -> {
            if (validarArchivoAbierto()) {
                editor.setTextFrame(textPane);
                editor.setSubrayado();
            }
        });

        color.addActionListener(e -> {
            if (validarArchivoAbierto()) {
                Color nuevoColor = JColorChooser.showDialog(this, "Seleccionar color", textPane.getForeground());
                if (nuevoColor != null) {
                    editor.setTextFrame(textPane);
                    editor.changeColor(nuevoColor);
                }
            }
        });

        // Actualización interactiva para tamaño
        comboFuentes.addActionListener(e -> {
            if (validarArchivoAbierto()) {
                String fuenteSeleccionada = (String) comboFuentes.getSelectedItem();
                if (fuenteSeleccionada != null) {
                    editor.setTextFrame(textPane);
                    editor.changeFont(fuenteSeleccionada);
                }
            }
        });

        comboTamanos.addActionListener(e -> {
            if (validarArchivoAbierto()) {
                Integer tamanoSeleccionado = (Integer) comboTamanos.getSelectedItem();
                if (tamanoSeleccionado != null) {
                    editor.setTextFrame(textPane);
                    editor.changeSize(tamanoSeleccionado);
                }
            }
        });

        fuenteButton.addActionListener(e -> {
            if (validarArchivoAbierto()) {
                String fuenteSeleccionada = (String) comboFuentes.getSelectedItem();

                if (fuenteSeleccionada != null) {
                    editor.setTextFrame(textPane);
                    editor.changeFont(fuenteSeleccionada);
                }
            }
        });

        tamanoButton.addActionListener(e -> {
            if (validarArchivoAbierto()) {
                Integer tamanoSeleccionado = (Integer) comboTamanos.getSelectedItem();

                if (tamanoSeleccionado != null) {
                    editor.setTextFrame(textPane);
                    editor.changeSize(tamanoSeleccionado);
                }
            }
        });
    }

    private boolean validarArchivoAbierto() {
        if (archivoAbierto == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe crear o abrir un archivo antes de editar.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void actualizarTitulo() {
        if (archivoAbierto != null) {
            setTitle("Editor de Texto - " + archivoAbierto.getName());
        } else {
            setTitle("Editor de Texto");
        }
    }

    private void abrirArchivo(String nombreArchivo) {
        String rutaCarpeta = "Documentos";
        File carpeta = new File(rutaCarpeta);
        textPane.setText("");
        if (!carpeta.exists()) {
            JOptionPane.showMessageDialog(this, "La carpeta 'Documentos' no existe.");
            return;
        }

        File archivo = new File(carpeta, nombreArchivo + ".rtf");
        archivoAbierto = archivo;

        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(this, "El archivo no se encuentra en la carpeta 'Documentos'.");
            return;
        } else {
            JOptionPane.showMessageDialog(this, "Archivo abierto con éxito.");
            actualizarTitulo();
        }

        archivoAbierto = archivo;

        try (FileInputStream fis = new FileInputStream(archivo)) {
            RTFEditorKit rtfEditorKit = new RTFEditorKit();
            textPane.setDocument(rtfEditorKit.createDefaultDocument());
            rtfEditorKit.read(fis, textPane.getDocument(), 0);
        } catch (IOException | javax.swing.text.BadLocationException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearArchivoDocx(String nombreArchivo) {
        String rutaCarpeta = "Documentos";
        File carpeta = new File(rutaCarpeta);
        textPane.setText("");

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        File archivo = new File(carpeta, nombreArchivo + ".rtf");
        archivoAbierto = archivo;

        if (archivo.exists()) {
            JOptionPane.showMessageDialog(this, "El archivo ya existe");

            abrirArchivo(nombreArchivo);
        } else {

            try (FileWriter writer = new FileWriter(archivo)) {
                writer.write("");
                JOptionPane.showMessageDialog(this, "Archivo creado exitosamente: " + archivo.getAbsolutePath());
                actualizarTitulo();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al crear el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    public void guardarCambios(JTextPane textPane) {
        try {
            RTFEditorKit rtfEditorKit = new RTFEditorKit(); // Usar para manejar RTF
            if (archivoAbierto != null) {
                try (FileOutputStream fos = new FileOutputStream(archivoAbierto)) {
                    rtfEditorKit.write(fos, textPane.getDocument(), 0, textPane.getDocument().getLength());
                    JOptionPane.showMessageDialog(this, "Archivo guardado con exito ");
                }
            } else {

                validarArchivoAbierto();
            }
        } catch (IOException | BadLocationException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        editorMAIN ventana = new editorMAIN();
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
    }
}
