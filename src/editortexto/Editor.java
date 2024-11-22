    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editortexto;

import java.io.File;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;

/**
 *
 * @author Hp
 */
public class Editor {

    private File myFile = null;
    private JTextPane textPane;
    private RTFEditorKit rtfEditorKit = new RTFEditorKit(); 

    public Editor(String nombreArchivo) {
        if (!nombreArchivo.endsWith(".rtf")) {
            nombreArchivo += ".rtf"; 
        }
        myFile = new File(nombreArchivo);
    }

    public void setTextFrame(JTextPane frame) {
        this.textPane = frame;
    }

    private void applyStyle(Style style) {
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();
        if (start != end) {
            textPane.getStyledDocument().setCharacterAttributes(start, end - start, style, false);
        } else {
            textPane.getStyledDocument().setCharacterAttributes(0, textPane.getText().length(), style, false);
        }
    }

    public void changeFont(String fontName) {
        Style style = textPane.addStyle("fontStyle", null);
        StyleConstants.setFontFamily(style, fontName);
        applyStyle(style);
    }

    public void changeSize(int size) {
        Style style = textPane.addStyle("sizeStyle", null);
        StyleConstants.setFontSize(style, size);
        applyStyle(style);
    }

public void changeColor(Color color) {
    if (textPane.getSelectedText() != null) {
        Style style = textPane.addStyle("colorStyle", null);
        StyleConstants.setForeground(style, color);
        applyStyle(style);
    } else {
        JOptionPane.showMessageDialog(null, "Por favor, selecciona un texto para cambiar el color.");
    }
}

    public void setBold() {
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();
        StyledDocument doc = textPane.getStyledDocument();

        boolean hasBold = false;
        for (int i = start; i < end; i++) {
            if (StyleConstants.isBold(doc.getCharacterElement(i).getAttributes())) {
                hasBold = true;
                break;
            }
        }
        Style style = textPane.addStyle("boldStyle", null);
        if (hasBold) {
            StyleConstants.setBold(style, false);
        } else {
            StyleConstants.setBold(style, true);
        }
        doc.setCharacterAttributes(start, end - start, style, false);
    }

    public void setItalic() {
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();
        StyledDocument doc = textPane.getStyledDocument();

        boolean hasItalic = false;
        for (int i = start; i < end; i++) {
            if (StyleConstants.isItalic(doc.getCharacterElement(i).getAttributes())) {
                hasItalic = true;
                break;
            }
        }

        Style style = textPane.addStyle("italicStyle", null);
        if (hasItalic) {
            StyleConstants.setItalic(style, false);
        } else {
            StyleConstants.setItalic(style, true);
        }
        doc.setCharacterAttributes(start, end - start, style, false);
    }
    
    public void setSubrayado() {
    int start = textPane.getSelectionStart();
    int end = textPane.getSelectionEnd();
    StyledDocument doc = textPane.getStyledDocument();

    boolean hasUnderline = false;
    for (int i = start; i < end; i++) {
        if (StyleConstants.isUnderline(doc.getCharacterElement(i).getAttributes())) {
            hasUnderline = true;
            break;
        }
    }
    Style style = textPane.addStyle("underlineStyle", null);
    if (hasUnderline) {
        StyleConstants.setUnderline(style, false);
    } else {
        StyleConstants.setUnderline(style, true);
    }
    doc.setCharacterAttributes(start, end - start, style, false);
}

    public void abrirFile() {
        try (FileReader reader = new FileReader(myFile)) {
            textPane.setText(""); 
            rtfEditorKit.read(reader, textPane.getDocument(), 0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al abrir el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void guardarFile() {
        try (FileWriter writer = new FileWriter(myFile)) {
            rtfEditorKit.write(writer, textPane.getDocument(), 0, textPane.getDocument().getLength());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

