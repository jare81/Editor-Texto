/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editortexto;

import java.awt.Color;
 import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTMLEditorKit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class GuardarHTML {
    public static void main(String[] args) {
        JTextPane textPane = new JTextPane();
        StyledDocument doc = textPane.getStyledDocument();

        // Aplicar estilos de ejemplo
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setItalic(attrs, true);
        StyleConstants.setForeground(attrs, Color.RED);

        try {
            doc.insertString(doc.getLength(), "Texto en cursiva y rojo\n", attrs);
            doc.insertString(doc.getLength(), "Texto normal", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        // Guardar el contenido en formato HTML
        try (FileOutputStream out = new FileOutputStream("texto_formateado.html");
             OutputStreamWriter writer = new OutputStreamWriter(out)) {
            HTMLEditorKit htmlKit = new HTMLEditorKit();
            htmlKit.write(writer, doc, 0, doc.getLength());
            System.out.println("Texto guardado en formato HTML en 'texto_formateado.html'");
        } catch (IOException | BadLocationException e) {
            e.printStackTrace();
        }
    }


}
