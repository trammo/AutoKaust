import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class AutoKaust {

    private JFrame frame;
    private JTextArea textArea;
    
    HashMap<String, String> codeReplacementsMap;
    String APP_HEADER = "AutoKaust v1.0";
    String RESULT_FILE_SUFFIX = "MKMBlokid";
    String CONFIG_FILE_NAME = "config.txt";
    private JTextField textWorkNumber;
    private JTextField txtWorkName;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                	AutoKaust window = new AutoKaust();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    
    public AutoKaust() {
        initialize();
    }
        
    
    private void initialize() {        
        frame = new JFrame(APP_HEADER);
        frame.setBounds(100, 100, 801, 667);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        String userhome = System.getProperty("user.home");
        FileNameExtensionFilter txtAndCsvFilter = new FileNameExtensionFilter(".CSV .TXT", "csv", "txt");
        
        JButton btnTekitaKaustad = new JButton("Tekita kaustad");
        btnTekitaKaustad.setBounds(10, 470, 765, 78);
        btnTekitaKaustad.setBackground(UIManager.getColor("InternalFrame.activeTitleGradient"));
        btnTekitaKaustad.addActionListener(new ReplaceBlocksActionListener());
        
        frame.getContentPane().add(btnTekitaKaustad);
        
        textWorkNumber = new JTextField();
        textWorkNumber.setText("xx");
        textWorkNumber.setBounds(10, 13, 264, 22);
        frame.getContentPane().add(textWorkNumber);
        textWorkNumber.setColumns(10);
        
        JLabel lblSisestaTNumber = new JLabel("Sisesta t\u00F6\u00F6 number");
        lblSisestaTNumber.setBounds(288, 16, 214, 16);
        frame.getContentPane().add(lblSisestaTNumber);
        
        txtWorkName = new JTextField();
        txtWorkName.setText("xxxx");
        txtWorkName.setColumns(10);
        txtWorkName.setBounds(10, 48, 264, 22);
        frame.getContentPane().add(txtWorkName);
        
        JLabel lblSisestaTNimi = new JLabel("Sisesta töö nimi");
        lblSisestaTNimi.setBounds(288, 51, 214, 16);
        frame.getContentPane().add(lblSisestaTNimi);
        
        
        String documentsPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
        String projectTempaltesRoot = documentsPath + "\\Kaustade_automatiseerimine";
		File directoryPath = new File(projectTempaltesRoot);
        //List of all files and directories
        String contents[] = directoryPath.list();
        
        JList listWorkType = new JList(contents);
        listWorkType.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listWorkType.setBounds(12, 83, 264, 212);
        frame.getContentPane().add(listWorkType);
        
        JLabel lblValiTLiik = new JLabel("Vali töö liik");
        lblValiTLiik.setBounds(288, 80, 214, 16);
        frame.getContentPane().add(lblValiTLiik);
        
        textArea = new JTextArea();
        textArea.setBounds(10, 308, 761, 149);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        frame.getContentPane().add(textArea);
        
        
        
//        listWorkType.setdata(new ListModel<String>() {
//		});
//        System.out.println("List of files and directories in the specified directory:");
//        for(int i=0; i<contents.length; i++) {
//           System.out.println(contents[i]);
//        }

        
//        try {
//            initializeReplacementsConfig();
//        } catch (Exception e) {
//            textArea.setText(e.getMessage());
//        }
    }

    private void initializeReplacementsConfig() throws IOException {
        codeReplacementsMap = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE_NAME));
        String line;
        while ((line = br.readLine()) != null) {
            String trimmedLine = line.trim();
            if (trimmedLine != null && trimmedLine.length() > 0) {
                String[] configChunks = trimmedLine.split(";");
                if (configChunks.length != 2) {
                    throw new RuntimeException("Invalid piece of config: " + line);
                }
                String key = configChunks[0].trim();
                String value = configChunks[1].trim();
                codeReplacementsMap.put(key, value);
            }
        }
        br.close();
    }

    private class ReplaceBlocksActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        	textArea.setText("action peformed" + e);
//            File currentDirectory = fileChooser.getCurrentDirectory();
//            File selectedFile = fileChooser.getSelectedFile();
//            File absoluteFile2 = selectedFile.getAbsoluteFile();
//            
//            FileWriter fw = null;
//            
//            // create output file
//            String sourceFileNameWithExtension = absoluteFile2.getName();
//            int lastDot = sourceFileNameWithExtension.lastIndexOf('.');
//            
//            String fileNameWithoutExtension = sourceFileNameWithExtension.substring(0, lastDot);
//            String extension = sourceFileNameWithExtension.substring(lastDot+1);
//            String resultFileName = currentDirectory + "\\" + fileNameWithoutExtension + "." + RESULT_FILE_SUFFIX + "." + extension;
//            
//            try {
//                fw = new FileWriter(resultFileName);
//            } catch (IOException e2) {
//                showResult("Cannot create result file. " + e2.getMessage());
//            }
//            
//            
//            // loeb faili sisse ridahaaval
//            try (BufferedReader br = new BufferedReader(new FileReader(absoluteFile2))) {
//                String line;
//                while ((line = br.readLine()) != null) {
//                   // process the line
//                    String replacedString = processLine(line);
//                    fw.write(replacedString);
//                    fw.write("\r\n");
//                }
//                br.close();
//            } catch (Exception e1) {
//                showResult("Something frong with input file's stream. " + e1.getMessage());
//            }
//            
//            try {
//                fw.close();
//            } catch (IOException e1) {
//                showResult("Cannot close result file's stream. " + e1.getMessage());
//            }
//            showResult("Successfully blocked! Lookup your results from " + resultFileName);
        }
        
        private String processLine(String line) {
            int lastIndexOfComma = line.lastIndexOf(',');
            if (lastIndexOfComma > 0 && lastIndexOfComma < line.length()) {
                String lastWord = line.substring(lastIndexOfComma + 1).toUpperCase();
                if (codeReplacementsMap.containsKey(lastWord)) {
                    String value = codeReplacementsMap.get(lastWord);
                    StringBuilder replacedLine = new StringBuilder(line).replace(lastIndexOfComma + 1, lastIndexOfComma + 1 + lastWord.length(), value);
                    return replacedLine.toString();
                } else {
                    return line;
                }
            }
            return line;
        }
    }
    
    private void showResult(String text) {
        textArea.setText(text);
//        makePopup(text);    
    }
    
//    private void makePopup(String text) {
//        Popup popup = new Popup();
//        popup.setText(text);
//        popup.setVisible(true);
//        popup.toFront();
//    }
}
