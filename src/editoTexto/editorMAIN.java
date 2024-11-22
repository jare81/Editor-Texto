/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editoTexto;

import editortexto.Editor;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
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
        setSize(500, 500);
        setTitle("INTERFAZ");

        this.setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        archivoAbierto = null; // Inicializa como null.
        editor = new Editor("nuevoArchivo"); // Usa un nombre predeterminado.

        JPanel I = new JPanel(new GridBagLayout());
        GridBagConstraints gridI = new GridBagConstraints();
        gridI.insets = new Insets(10, 10, 10, 10);

        Dimension size = new Dimension(80, 30);

        JPanel principales = new JPanel(new GridBagLayout());
        GridBagConstraints gridP = new GridBagConstraints();
        gridP.insets = new Insets(10, 10, 10, 10);
        gridI.gridx = 0;
        gridI.gridy = 0;
        I.add(principales, gridI);

        JButton abrir = new JButton("Abrir");
        abrir.setPreferredSize(size);
        abrir.setContentAreaFilled(false);
        gridP.gridx = 0;
        gridP.gridy = 0;
        principales.add(abrir, gridP);

        JButton nuevo = new JButton("Nuevo");
        nuevo.setPreferredSize(size);
        nuevo.setContentAreaFilled(false);
        gridP.gridx = 1;
        gridP.gridy = 0;
        principales.add(nuevo, gridP);

        JButton guardar = new JButton("Guardar");
        guardar.setPreferredSize(size);
        guardar.setContentAreaFilled(false);
        gridP.gridx = 3;
        gridP.gridy = 0;
        principales.add(guardar, gridP);

        JPanel opciones = new JPanel(new GridBagLayout());
        GridBagConstraints gridO = new GridBagConstraints();
        gridO.insets = new Insets(10, 10, 10, 10);
        gridI.gridx = 0;
        gridI.gridy = 1;
        I.add(opciones, gridI);

        JButton copiar = new JButton("Copiar");
        copiar.setPreferredSize(size);
        copiar.setContentAreaFilled(false);
        gridO.gridx = 1;
        gridO.gridy = 0;
        opciones.add(copiar, gridO);

        JButton pegar = new JButton("Pegar");
        pegar.setPreferredSize(size);
        pegar.setContentAreaFilled(false);
        gridO.gridx = 2;
        gridO.gridy = 0;
        opciones.add(pegar, gridO);

        JButton negrita = new JButton("Negrita");
        negrita.setPreferredSize(size);
        negrita.setContentAreaFilled(false);
        gridO.gridx = 3;
        gridO.gridy = 0;
        opciones.add(negrita, gridO);

        JButton cursiva = new JButton("Cursiva");
        cursiva.setPreferredSize(size);
        cursiva.setContentAreaFilled(false);
        gridO.gridx = 4;
        gridO.gridy = 0;
        opciones.add(cursiva, gridO);

        JButton subrayado = new JButton("Subrayar");
        subrayado.setPreferredSize(size);
        subrayado.setContentAreaFilled(false);
        gridO.gridx = 5;
        gridO.gridy = 0;
        opciones.add(subrayado, gridO);

        add(I, BorderLayout.NORTH);

        textPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.CENTER);

        nuevo.addActionListener(e -> {
            String nombreArchivo = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo:");
            if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
                crearArchivo(nombreArchivo);
                if (archivoAbierto != null) {
                    abrirArchivo(nombreArchivo);
                } else {
                    JOptionPane.showMessageDialog(this, "No hay ningún archivo abierto actualmente.");
                }

            }
        });

        abrir.addActionListener(e -> {
            String nombreArchivo = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo a abrir:");
            if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
                abrirArchivo(nombreArchivo);
            }
        });

        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambios(textPane);
            }
        });

        negrita.addActionListener(e -> {
            aplicarNegrita();
        });

        cursiva.addActionListener(e -> editor.setItalic());

        
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

        archivoAbierto = archivo; // Actualiza la referencia del archivo abierto.

        try (FileInputStream fis = new FileInputStream(archivo)) {
            RTFEditorKit rtfEditorKit = new RTFEditorKit();
            StyledDocument doc = (StyledDocument) rtfEditorKit.createDefaultDocument();
            rtfEditorKit.read(fis, doc, 0);
            textPane.setStyledDocument(doc);
            JOptionPane.showMessageDialog(this, "Archivo abierto con éxito.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearArchivo(String nombreArchivo) {
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

   

    public void guardarCambios(JTextPane textPane) {
        if (archivoAbierto == null) {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(this);

            if (option == JFileChooser.APPROVE_OPTION) {
                archivoAbierto = fileChooser.getSelectedFile();
                if (!archivoAbierto.getName().endsWith(".rtf")) {
                    archivoAbierto = new File(archivoAbierto.getAbsolutePath() + ".rtf");
                }
            } else {
                return; // Si no selecciona un archivo, salir del método
            }
        }

        try (FileOutputStream fos = new FileOutputStream(archivoAbierto)) {
            RTFEditorKit rtfEditorKit = new RTFEditorKit();
            rtfEditorKit.write(fos, textPane.getStyledDocument(), 0, textPane.getStyledDocument().getLength());
            JOptionPane.showMessageDialog(this, "Archivo guardado correctamente: " + archivoAbierto.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarNegrita() {
        StyledDocument doc = textPane.getStyledDocument();

        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();

        if (start == end) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un texto para aplicar el formato.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Element element = doc.getCharacterElement(start);
        AttributeSet attrs = element.getAttributes();
        boolean esNegrita = StyleConstants.isBold(attrs);

        SimpleAttributeSet nuevoEstilo = new SimpleAttributeSet(attrs);
        StyleConstants.setBold(nuevoEstilo, !esNegrita);

        doc.setCharacterAttributes(start, end - start, nuevoEstilo, false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            editorMAIN ventana = new editorMAIN();
            ventana.setVisible(true);
        });
    }

}
