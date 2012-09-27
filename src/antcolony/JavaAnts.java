package antcolony;
import java.awt.EventQueue;

import javaants.Data;
import javaants.Document;
import javaants.Position;

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


public class JavaAnts {

	private Data data;
	private Grid grid;
	private Configuration conf;
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
		conf = new Configuration();
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
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(Datasets.values()));
		comboBox.setToolTipText("Datasets to be clustered");
		comboBox.setBounds(24, 37, 91, 22);
		frame.getContentPane().add(comboBox);
	}

/*********** hardcoded generation of artificial data *************************************/

/** Generate the hardcoded data for the test distributions
* @param dist switch between the three possible distributions (0, 1 or 2)
* @return the generated test data
*/
	
public static Data generate(int dist) {
		int ndocs = 400;
		int nkeys = 2;

		String [] keys = new String[2];
		keys[0] = new String("ycoord");
		keys[1] = new String("xcoord");
			
		Document[] docs = null;

		


		// Hard-coded test distribution (Uniform Distribution)
		if (dist == 1) {

			ndocs = 450;
			docs = new Document[ndocs + nkeys];
			Random generator = new Random();

			Position center[] = new Position[9];
			center[0] = new Position(0, 0);
			center[1] = new Position(0, 8);
			center[2] = new Position(8, 0);
			center[3] = new Position(8, 8);
			center[4] = new Position(0, 16);
			center[5] = new Position(16, 0);
			center[6] = new Position(16, 8);
			center[7] = new Position(8, 16);
			center[8] = new Position(16, 16);
			Position var[] = new Position [10];
			var[0] = new Position(2, 2);
			var[1] = new Position(2, 2);
			var[2] = new Position(2, 2);
			var[3] = new Position(2, 2);
			var[4] = new Position(2, 2);
			var[5] = new Position(2, 2);
			var[6] = new Position(2, 2);
			var[7] = new Position(2, 2);
			var[8] = new Position(2, 2);
					
			double data[] = new double[2];
			Position pos;
			
			// create individual vectors distributed randomly around the given mean
			for (int j=0; j< 50; j++) {
				for (int i = 0; i< 9; i++) {
					data[0] = Math.random() * 4*var[i].getY() + center[i].getY();
					data[1] = Math.random() * 4*var[i].getX() + center[i].getX();
					pos = new Position((int)Math.round(data[0]), (int)Math.round(data[1]));
					docs[j*9+i] = new Document(data, 2, pos, i+1);
				}
			}
			

			
		}
		
		// Hard-coded test distribution (Normal Distribution)
		else if (dist == 0) {
			
		        ndocs = 800;
			nkeys = 2;

			docs = new Document[ndocs + nkeys];
			Random generator = new Random();

			Position center[] = new Position[4];
			center[0] = new Position(0, 0);
			center[1] = new Position(0, 8);
			center[2] = new Position(8, 0);
			center[3] = new Position(8, 8);
			Position var[] = new Position [4];
			var[0] = new Position(2, 2);
			var[1] = new Position(2, 2);
			var[2] = new Position(2, 2);
			var[3] = new Position(2, 2);
		
			double data[] = new double[2];
			Position pos;
			
			for (int i = 0; i< 4; i++) {
				for (int j=0; j< 200 ; j++) {
					data[0] = generator.nextGaussian() * var[i].getY() + center[i].getY();
					data[1] = generator.nextGaussian() * var[i].getX() + center[i].getX();
					pos = new Position((int)Math.round(data[0]), (int)Math.round(data[1]));
					docs[i*200+j] = new Document(data, 2, pos, i+1);

				}
			}



		}

		else if (dist == 2) {

			ndocs = 90;
			nkeys = 20;
			docs = new Document[ndocs+nkeys];
			
			
			keys = new String[nkeys];
			keys[0] = "Computer Graphics";
			keys[1] = "Splines";
			keys[2] = "Mesh";
			keys[3] = "Visualization";
			keys[4] = "Topic Maps";
			keys[5] = "Fisheye Views";
			keys[6] = "Graphs";

			keys[7] = "Artificial Life";
			keys[8] = "Game of Life";
			keys[9] = "Turing";
			keys[10] = "Chaos";
			keys[11] = "Self-organisation";

			keys[12] = "Artificial Intelligence";
			keys[13] = "Bayesian Reasoning";
			keys[14] = "Symbolic";
			keys[15] = "Expert System";
			keys[16] = "Neural Network";
			keys[17] = "Knowledge";
			keys[18] = "Representation";
			keys[19] = "Robot";


			double data[] = new double[nkeys];
				
			for (int j=0; j<ndocs; j++) {
			
				int color = 1;
				if (j < 30) {
					for (int k=0; k<7; k++) {
						data[k] =  (int)Math.floor(3.0+Math.random()*3.0);
					}
					for (int k=7; k<20; k++) {
						data[k] = (int)Math.floor(Math.random()*3.0);
					}
					color = 2;
				}
				else if (j < 60) {
					for (int k=0; k<7; k++) {
						data[k] = (int)Math.floor(Math.random()*3.0);
					}
					for (int k=7; k<12; k++) {
						data[k] = (int)Math.floor(3.0+Math.random()*10.0);
					}
					for (int k=12; k<20; k++) {
						data[k] = (int)Math.floor(Math.random()*3.0);
					}
					color = 3;
				}	
				else {
					for (int k=0; k<12; k++) {
						data[k] = (int)Math.floor(Math.random()*3.0);
					}
					for (int k=12; k<20; k++) {
						data[k] = (int)Math.floor(30.0+Math.random()*5.0);
					}
					color = 4;
				}
			
				docs[j] = new Document(color);
				docs[j].setVector(nkeys, data);
			}


				
			
		}
		return new Data(nkeys, ndocs, keys, docs);
		}

	}	

}
