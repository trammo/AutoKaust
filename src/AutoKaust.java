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

public class AutoKaust {

	private JFrame frame;
	private JTextArea textArea;

	HashMap<String, String> codeReplacementsMap;
	String APP_HEADER = "AutoKaust v1.0";
	String RESULT_FILE_SUFFIX = "MKMBlokid";
	String CONFIG_FILE_NAME = "config.txt";
	private JTextField textWorkNumber;
	private JTextField textWorkName;
	private JList listWorkTypes;

	private final String TO_BE_REPLACED = "xxxx-xx";

	private String CURRENT_DIRECTORY;
	private String HOME_DIRECTORY;
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

		textArea = new JTextArea();
		textArea.setBounds(10, 308, 761, 149);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		frame.getContentPane().add(textArea);

		CURRENT_DIRECTORY = new File("").getAbsolutePath();
		String documentsPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
		TEMPLATES_ROOT = documentsPath + "//Kaustade_automatiseerimine";

		HOME_DIRECTORY = System.getProperty("user.home");

		JButton btnTekitaKaustad = new JButton("Tekita kaustad");
		btnTekitaKaustad.setBounds(10, 470, 765, 78);
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
			showResult("No template folders forund from " + TEMPLATES_ROOT);
		} else {
			listWorkTypes = new JList(contents);
			listWorkTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listWorkTypes.setBounds(12, 83, 264, 212);
			frame.getContentPane().add(listWorkTypes);

		}

		JLabel lblValiTLiik = new JLabel("Vali töö liik");
		lblValiTLiik.setBounds(288, 80, 214, 16);
		frame.getContentPane().add(lblValiTLiik);

	}

	private class CreateFoldersActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String toReplace = textWorkNumber.getText() + " " + textWorkName.getText();

			String selectedWorkType = listWorkTypes.getSelectedValue().toString();

			String source = TEMPLATES_ROOT + "//" + selectedWorkType;
			File srcDir = new File(source);

			String destinationFolderName = selectedWorkType.replace(TO_BE_REPLACED, toReplace);
			String destination = CURRENT_DIRECTORY + "//" + destinationFolderName;
			File destDir = new File(destination);

			try {
				FileUtils.copyDirectory(srcDir, destDir);
				showResult("Created folder " + destDir + " with contents from the template");
			} catch (IOException ex) {
				showResult(ex.toString());
			}

			renameFiles(destination, TO_BE_REPLACED, toReplace);
		}
	}

	public void renameFiles(String dir, String replace, String replaceBy) {
		try {
			try (Stream<Path> stream = Files.find(Paths.get(dir), 10,
					(path, attr) -> String.valueOf(path).contains(TO_BE_REPLACED))) {
				stream.map(String::valueOf).forEach(item -> {
					try {
						Path sourcePath = new File(item).toPath();
						Path destinationPath = new File(item.replace(replace, replaceBy)).toPath();
						Files.move(sourcePath, destinationPath);
						showResult("Created entity " + destinationPath.toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}

		} catch (IOException e) {
			showResult(e.toString());
		}
	}

	private void showResult(String text) {
		textArea.setText(text);
	}
}
