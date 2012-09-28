package antcolony;
import java.awt.Dimension;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import antcolony.Configuration.Models;
import antcolony.Configuration.Datasets;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


public class Main {

	private Data data;
	private Configuration conf;
	private Grid grid;
	private JFrame frame;
	private JTextField textField_1;
	private JTable table;
	private JTable table_1;
	private JButton btnStop;
	private Thread runner;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
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
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		conf = new Configuration();
		data = new Data(conf);
		grid = new Grid();
		frame = new JFrame();
		frame.setBounds(100, 100, 847, 614);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(277, 21, 552, 555);
		frame.getContentPane().add(scrollPane);
		
		grid.setBackground(Color.WHITE);
		grid.setPreferredSize(new Dimension(800,800));
		scrollPane.setViewportView(grid);
		
		JLabel lblDataset = new JLabel("Dataset");
		lblDataset.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDataset.setBounds(24, 11, 46, 14);
		frame.getContentPane().add(lblDataset);
		
		JToggleButton tglbtnStart = new JToggleButton("Start");
		tglbtnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JToggleButton bt = (JToggleButton) e.getSource();
				if (bt.isSelected()){
					btnStop.setText("");
					if (runner == null) {
						runner = new Thread(grid);
						runner.start();
					}
					else {
						synchronized (grid){
						grid.setSuspend(false);
						grid.notify();
						}
					}
				}
				else {
					synchronized (grid){
					grid.setSuspend(true);
					grid.notify();
					}
					btnStop.setText("Restart");
				}
			}
		});
		tglbtnStart.setFont(new Font("Tahoma", Font.BOLD, 11));
		tglbtnStart.setBounds(24, 106, 91, 23);
		frame.getContentPane().add(tglbtnStart);
		
		btnStop = new JButton("");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnStop.getText()=="Restart"){
					if (runner == null){	
					synchronized (grid) {
						grid.update(conf,data);
						grid.repaint();
						}
					}
					else runner.stop();
				}
			}
		});
		btnStop.setToolTipText("Reset or Pause the simulation");
		btnStop.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnStop.setBounds(146, 105, 91, 23);
		frame.getContentPane().add(btnStop);
		
		JToggleButton tglbtnRecord = new JToggleButton("Record");
		tglbtnRecord.setFont(new Font("Tahoma", Font.BOLD, 11));
		tglbtnRecord.setBounds(24, 537, 76, 23);
		frame.getContentPane().add(tglbtnRecord);
		
		textField_1 = new JTextField();
		textField_1.setBounds(24, 506, 191, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblFilename = new JLabel("Filename");
		lblFilename.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFilename.setBounds(24, 481, 67, 14);
		frame.getContentPane().add(lblFilename);
		
		JLabel lblVariables = new JLabel("Measures");
		lblVariables.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblVariables.setBounds(24, 296, 76, 14);
		frame.getContentPane().add(lblVariables);
		
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
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JComboBox cb = (JComboBox)arg0.getSource();
				conf.setModel((Models)cb.getSelectedItem());
			}
		});
		comboBox_1.setModel(new DefaultComboBoxModel(Models.values()));
		comboBox_1.setToolTipText("Simulation models");
		comboBox_1.setBounds(146, 37, 91, 22);
		frame.getContentPane().add(comboBox_1);
		
		JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JComboBox cb = (JComboBox)arg0.getSource();
				conf.setDataset((Datasets)cb.getSelectedItem());
				data = new Data(conf);
				grid.update(conf,data);
				grid.repaint();
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(Datasets.values()));
		comboBox.setToolTipText("Datasets to be clustered");
		comboBox.setBounds(24, 37, 91, 22);
		frame.getContentPane().add(comboBox);
		
		JCheckBox chckbxDisplyOriginalSet = new JCheckBox("Display original set");
		chckbxDisplyOriginalSet.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				JCheckBox cb = (JCheckBox) arg0.getSource();
				grid.setOriginal(cb.isSelected());
				grid.repaint();
			}
		});
		chckbxDisplyOriginalSet.setSelected(true);
		chckbxDisplyOriginalSet.setBounds(24, 66, 151, 23);
		frame.getContentPane().add(chckbxDisplyOriginalSet);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
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
				false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.setBounds(24, 164, 214, 117);
		frame.getContentPane().add(table);
		
		table_1 = new JTable();
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
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
		table_1.setBounds(23, 319, 214, 117);
		frame.getContentPane().add(table_1);


		
	}	
}