/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editortexto;

import java.awt.Color;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;
import java.io.FileOutputStream;
import java.io.IOException;

public class TextPane {
    public static void main(String[] args) {
        JTextPane textPane = new JTextPane();
        StyledDocument doc = textPane.getStyledDocument();

        // Aplicar estilos de ejemplo
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setBold(attrs, true);
        StyleConstants.setForeground(attrs, Color.BLUE);

        try {
            doc.insertString(doc.getLength(), "Texto en negrita y azul\n", attrs);
            doc.insertString(doc.getLength(), "Texto normal", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        // Guardar el contenido en formato RTF
        try (FileOutputStream out = new FileOutputStream("texto_formateado.rtf")) {
            RTFEditorKit rtfKit = new RTFEditorKit();
            rtfKit.write(out, doc, 0, doc.getLength());
            System.out.println("Texto guardado en formato RTF en 'texto_formateado.rtf'");
        } catch (IOException | BadLocationException e) {
            e.printStackTrace();
        }
    }
}


