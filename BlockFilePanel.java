/*
Utilized in RSAGUI
Authors : Dhrumil Patel, Kena Patel
*/

import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class BlockFilePanel extends javax.swing.JPanel {

    private javax.swing.JButton create;

    /**
     * Creates new form KeyCreation
     */
    public BlockFilePanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        create = new javax.swing.JButton();


        create.setText("Start");
        create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    createActionPerformed(evt);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Some error occurred during block file generation. Please try again.");

                }
            }
        });
        setLayout(new BorderLayout());
        add(create, BorderLayout.CENTER);

    }// </editor-fold>

    private void createActionPerformed(java.awt.event.ActionEvent evt) throws SAXException, TransformerException, ParserConfigurationException, IOException {


        String acsii_file_name = JOptionPane.showInputDialog("Enter ascii text file name: ");
        int block = Integer.parseInt(JOptionPane.showInputDialog("Enter block size: "));

        String block_filename = JOptionPane.showInputDialog("Enter block file name: ");
        String message = RSA.readFile(acsii_file_name, StandardCharsets.UTF_8);
        ArrayList<LargeInteger> blocked_values = RSA.blockString(message, block);
        PrintWriter writer = new PrintWriter(new File(block_filename));
        for (LargeInteger value : blocked_values) {
            writer.println(value);
        }
        writer.close();
        JOptionPane.showMessageDialog(null, "Successfully created block file!");
    }
}
