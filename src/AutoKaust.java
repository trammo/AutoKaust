import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;
import javax.swing.JCheckBox;

public class AutoKaust {

	private JFrame frame;
	private JTextArea logArea;

	String APP_HEADER = "AutoKaust v1.1";
	private JTextField textWorkNumber;
	private JTextField textWorkName;
	private JList<String> listWorkTypes;

	private JCheckBox chckbxMootala;
	private JCheckBox chckbxKaevud;
	private JCheckBox chckbxYksikpuud;
	private JCheckBox chckbxMaamudel;
	
	private final String TO_BE_REPLACED = "xxxx-xx";
	private final String FOLDER_SEPARATOR = "//";
	
	private String CURRENT_DIRECTORY;
	private String TEMPLATES_ROOT;

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

		logArea = new JTextArea();
		logArea.setBounds(12, 390, 761, 149);
		logArea.setLineWrap(true);
		logArea.setEditable(false);
		frame.getContentPane().add(logArea);

		CURRENT_DIRECTORY = new File("").getAbsolutePath();
		String documentsPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
		TEMPLATES_ROOT = documentsPath + FOLDER_SEPARATOR + "Kaustade_automatiseerimine";

		JButton btnTekitaKaustad = new JButton("Tekita kaustad");
		btnTekitaKaustad.setBounds(12, 552, 765, 55);
		btnTekitaKaustad.setBackground(UIManager.getColor("InternalFrame.activeTitleGradient"));
		btnTekitaKaustad.addActionListener(new CreateFoldersActionListener());

		frame.getContentPane().add(btnTekitaKaustad);

		textWorkNumber = new JTextField();
		textWorkNumber.setText("xx");
		textWorkNumber.setBounds(10, 13, 264, 22);
		frame.getContentPane().add(textWorkNumber);
		textWorkNumber.setColumns(10);

		JLabel lblSisestaTNumber = new JLabel("Sisesta t\u00F6\u00F6 number");
		lblSisestaTNumber.setBounds(288, 16, 214, 16);
		frame.getContentPane().add(lblSisestaTNumber);

		textWorkName = new JTextField();
		textWorkName.setText("xxxx");
		textWorkName.setColumns(10);
		textWorkName.setBounds(10, 48, 264, 22);
		frame.getContentPane().add(textWorkName);

		JLabel lblSisestaTNimi = new JLabel("Sisesta töö nimi");
		lblSisestaTNimi.setBounds(288, 51, 214, 16);
		frame.getContentPane().add(lblSisestaTNimi);

		File directoryPath = new File(TEMPLATES_ROOT);
		// List of all files and directories
		String contents[] = directoryPath.list();

		
		if (contents == null || contents.length == 0) {
			logMessage("No template folders forund from " + TEMPLATES_ROOT);
			return; // avoids NullPointerException from the following new JList(contents) creation with empty array.
		}
		
		listWorkTypes = new JList(contents);
		listWorkTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listWorkTypes.setBounds(12, 83, 264, 171);
		frame.getContentPane().add(listWorkTypes);
		

		JLabel lblValiTLiik = new JLabel("Vali töö liik");
		lblValiTLiik.setBounds(288, 80, 214, 16);
		frame.getContentPane().add(lblValiTLiik);
		
		chckbxMootala = new JCheckBox("Mõõtala");
		chckbxMootala.setBounds(12, 263, 113, 25);
		frame.getContentPane().add(chckbxMootala);
		
		chckbxKaevud = new JCheckBox("Kaevud");
		chckbxKaevud.setBounds(12, 288, 113, 25);
		frame.getContentPane().add(chckbxKaevud);
		
		chckbxYksikpuud = new JCheckBox("Üksikpuud");
		chckbxYksikpuud.setBounds(12, 310, 113, 25);
		frame.getContentPane().add(chckbxYksikpuud);
		
		chckbxMaamudel = new JCheckBox("Maamudel");
		chckbxMaamudel.setBounds(12, 335, 113, 25);
		frame.getContentPane().add(chckbxMaamudel);

	}

	private class CreateFoldersActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String toReplace = textWorkNumber.getText() + " " + textWorkName.getText();

			String selectedWorkType = listWorkTypes.getSelectedValue().toString();

			String source = TEMPLATES_ROOT + FOLDER_SEPARATOR + selectedWorkType;
			File srcDir = new File(source);

			String destinationFolderName = selectedWorkType.replace(TO_BE_REPLACED, toReplace);
			String destinationFolder = CURRENT_DIRECTORY + FOLDER_SEPARATOR + destinationFolderName;
			File destDir = new File(destinationFolder);

			try {
				FileUtils.copyDirectory(srcDir, destDir);
				logMessage("Created folder " + destDir + " with contents from the template");
			} catch (IOException ex) {
				logMessage(ex.toString());
			}
			
			// rename the files that contain the TO_BE_REPLACED pathpattern
			renameFiles(destinationFolder, TO_BE_REPLACED, toReplace);
			
			// create the empty .txt files 
			if (chckbxMootala.isSelected()) {
				createEmptyFile(destinationFolder, "Mõõtala.txt");
			}
			
			if (chckbxKaevud.isSelected()) {
				createEmptyFile(destinationFolder, "Kaevud.txt");
			}
			
			if (chckbxYksikpuud.isSelected()) {
				createEmptyFile(destinationFolder, "Üksikpuud.txt");
			}
			
			if (chckbxMaamudel.isSelected()) {
				createEmptyFile(destinationFolder, "Maamudel.txt");
			}
			
			logMessage("All done!");
		}
	}

	private void createEmptyFile(String destinationFolder, String fileName) {
		String filePath = destinationFolder + FOLDER_SEPARATOR + fileName;
		try {
			new File(filePath).createNewFile();
			logMessage("Created empty file " + filePath);
		} catch (IOException e) {
			logMessage(e.toString());
		}
	}
	
	private void renameFiles(String dir, String replace, String replaceBy) {
		try {
			try (Stream<Path> stream = Files.find(Paths.get(dir), 10,
					(path, attr) -> String.valueOf(path).contains(TO_BE_REPLACED))) {
				stream.map(String::valueOf).forEach(item -> {
					try {
						Path sourcePath = new File(item).toPath();
						Path destinationPath = new File(item.replace(replace, replaceBy)).toPath();
						Files.move(sourcePath, destinationPath);
						logMessage("Created entity " + destinationPath.toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}

		} catch (IOException e) {
			logMessage(e.toString());
		}
	}

	private void logMessage(String text) {
		logArea.append(text + "\r\n");
	}
}
