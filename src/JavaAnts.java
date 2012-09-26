import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import antcolony.Dataset;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class JavaAnts {

	private JFrame frame;
	private JTextField textField_1;
	private JTable table;
	private JTable table_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaAnts window = new JavaAnts();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public JavaAnts() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 720, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(271, 11, 426, 444);
		frame.getContentPane().add(scrollPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		scrollPane.setViewportView(panel);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(Dataset.values()));
		comboBox.setEditable(true);
		comboBox.setToolTipText("Datasets to be clustered");
		comboBox.setBounds(24, 37, 91, 22);
		frame.getContentPane().add(comboBox);
		
		JLabel lblDataset = new JLabel("Dataset");
		lblDataset.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDataset.setBounds(24, 11, 46, 14);
		frame.getContentPane().add(lblDataset);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnStart.setToolTipText("Start the simulation");
		btnStart.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnStart.setBounds(24, 92, 91, 23);
		frame.getContentPane().add(btnStart);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setToolTipText("Stop the simulation");
		btnStop.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnStop.setBounds(146, 92, 91, 23);
		frame.getContentPane().add(btnStop);
		
		JToggleButton tglbtnRecord = new JToggleButton("Record");
		tglbtnRecord.setFont(new Font("Tahoma", Font.BOLD, 11));
		tglbtnRecord.setBounds(24, 432, 76, 23);
		frame.getContentPane().add(tglbtnRecord);
		
		textField_1 = new JTextField();
		textField_1.setBounds(24, 404, 191, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblFilename = new JLabel("Filename");
		lblFilename.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFilename.setBounds(24, 379, 67, 14);
		frame.getContentPane().add(lblFilename);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"Tick", null},
				{"Pearson's Correlation", null},
				{"Entropy", null},
				{null, null},
				{null, null},
			},
			new String[] {
				"Measure", "Value"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Double.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.setBounds(24, 276, 213, 80);
		frame.getContentPane().add(table);
		
		JLabel lblVariables = new JLabel("Measures");
		lblVariables.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblVariables.setBounds(24, 251, 76, 14);
		frame.getContentPane().add(lblVariables);
		
		table_1 = new JTable();
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
				{"Kd", null},
				{"K1", null},
				{"alpha", null},
				{null, null},
				{null, null},
			},
			new String[] {
				"Parameter", "Value"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Double.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_1.getColumnModel().getColumn(0).setPreferredWidth(100);
		table_1.getColumnModel().getColumn(1).setPreferredWidth(100);
		table_1.setBounds(24, 160, 213, 80);
		frame.getContentPane().add(table_1);
		
		JLabel lblParameters = new JLabel("Model Parameters");
		lblParameters.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblParameters.setBounds(24, 139, 116, 14);
		frame.getContentPane().add(lblParameters);
		
		JLabel lblModel = new JLabel("Model");
		lblModel.setToolTipText("Model Simulated");
		lblModel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblModel.setBounds(146, 11, 46, 14);
		frame.getContentPane().add(lblModel);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setToolTipText("Datasets to be clustered");
		comboBox_1.setBounds(146, 37, 91, 22);
		frame.getContentPane().add(comboBox_1);
	}
}
