import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;

public class ZeichenErsetzen extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel status;
	private JButton btnImportieren;
	private JButton btnLos;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ZeichenErsetzen frame = new ZeichenErsetzen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ZeichenErsetzen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 466, 293);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnImportieren = new JButton("Importieren");
		btnImportieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				datei = new ArrayList<String>();
				status.setText("");
				pfad = readAs(null);
				importieren();
			}
		});
		btnImportieren.setBounds(58, 46, 169, 23);
		contentPane.add(btnImportieren);
		
		JLabel lblZuErsetzen = new JLabel("Zu ersetzen:");
		lblZuErsetzen.setHorizontalAlignment(SwingConstants.RIGHT);
		lblZuErsetzen.setBounds(45, 105, 86, 14);
		contentPane.add(lblZuErsetzen);
		
		JLabel lblNeuesZeichen = new JLabel("Neues Zeichen:");
		lblNeuesZeichen.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNeuesZeichen.setBounds(45, 133, 89, 14);
		contentPane.add(lblNeuesZeichen);
		
		textField = new JTextField();
		textField.setToolTipText("Soll ein Punkt ersetzt werden, muss er mit \"\\.\" angegeben werden!");
		textField.setBounds(141, 102, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(141, 130, 86, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		status = new JLabel("");
		status.setBounds(58, 213, 151, 14);
		contentPane.add(status);
		
		btnLos = new JButton("LOS");
		btnLos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (datei == null) {
					status.setText("Keine Datei!");
					return;
				}
				ersetzen();
				status.setText("Erfolgreich!");
			}
		});
		btnLos.setBounds(99, 161, 89, 23);
		contentPane.add(btnLos);
		
		rdbtnZahlenModifizieren = new JRadioButton("Zahlen modifizieren");
		rdbtnZahlenModifizieren.setBounds(251, 105, 169, 23);
		contentPane.add(rdbtnZahlenModifizieren);
		
		
	}
	
	public String pfad;
	public ArrayList<String> datei = new ArrayList<String>();
	private JRadioButton rdbtnZahlenModifizieren;
	
	public void ersetzen () {
		String zuErs = textField.getText();
		String neuZ = textField_1.getText();
		long anfang = 0;
		if (rdbtnZahlenModifizieren.isSelected()) {
			anfang = Long.parseLong(datei.get(0).substring(0, 13));
		}
		for (int i = 0; i < datei.size(); i++) {
			if (rdbtnZahlenModifizieren.isSelected()) {
				String temp = datei.get(i);
				long zeitT = Long.parseLong(datei.get(i).substring(0, 13));
				long diff = Math.abs(zeitT-anfang);
//				temp.replace(String.valueOf(zeitT), String.valueOf(diff));
				temp.replaceAll(zuErs, neuZ);
				temp = temp.substring(13, temp.length());
				temp = diff + temp;
				System.out.println(temp);
				datei.set(i, temp);
			} else {
				datei.set(i, datei.get(i).replace(zuErs, neuZ));
			}
			
		}
		
		String endung = pfad.substring(pfad.lastIndexOf("."), pfad.length());
		String pfadNeu = pfad.substring(0, pfad.lastIndexOf(endung)) + "_bearb" + endung;
		File file = new File(pfadNeu);
		try {
			FileWriter fw = new FileWriter(file);
			fw.write("" + System.lineSeparator());	//Um in Excel Bezeichnungen hinzufügen zu können
			fw.write(datei.get(0));
			for (int i = 1; i < datei.size(); i++) {
				fw.write(System.lineSeparator() + datei.get(i));
			}
			fw.flush();
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		rdbtnZahlenModifizieren.setSelected(false);
		
	}
	
	public void importieren () {
		String next = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(pfad));
			while (true) {
					next = br.readLine();
					if (next == null) {
						break;
					}
					datei.add(next);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			status.setText("fehlgeschlagen");
		} catch (IOException e) {
			e.printStackTrace();
		}
		


		status.setText("Erfolgreich importiert!");
	}
	
	public String readAs (String pfad) { 
		JFileChooser chooser;
		chooser = new JFileChooser("."); 
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
//		FileNameExtensionFilter alexTab = new FileNameExtensionFilter(
//				"Alle Dateien (.)", "");
		chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
//		chooser.setFileFilter(alexTab);
		chooser.setDialogTitle("Lisa's Umgebung auswählen...");
		chooser.setVisible(true);
		int result = chooser.showOpenDialog(this);
		return chooser.getSelectedFile().toString();
	}
}
