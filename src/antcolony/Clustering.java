package antcolony;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import antcolony.Configuration.Datasets;
import antcolony.Configuration.Models;
import javax.swing.border.BevelBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextPane;

/**
 * Clustering is the base class for all graphics display
 * which allow the manipulation of the application; the drawing of the 
 * grid where items are clustered by the ants and the displaying
 * of current parameters and measures obtained at the modulo 100 ticks.
 * <p>
 * A Swing JPanel object encapsulates some state information needed
 * for the start of the simulation.  This
 * state information includes:
 * <ul>
 * <li>The dataset used on the simulation
 * <li>The model used on the simulation
 * <li>The start or pause state of the simulation
 * <li>The filename of the file used to write results
 * </ul>
 * <p>
 * On the right side of the panel a rectangular windows displays the
 * grid on which the ants and the items move. Two <b>Zoom</b> buttons allow the
 * zooming in and out the window to examine in more detail the movements on the grid.
 * When using Lumer and Faieta models, and if the corresponding check box
 * under the <b>Model</b> dropdown is checked,  the display of the clusters used for the
 * calculation of the similarity measures are shown together with their 
 * and their respective centers. If the checkbox is unchecked the pure items
 * colored by their type are shown.
 * <p>
 * The checkbox under the <b>Dataset</b> dropdown determines if the window will display
 * the items at their original position as determined by its inner similarity or
 * if the window displays the items as they are moved on the grid.
 * <p>
 * The <b>Start</b> button initiates a running of the application. Once the application
 * is started it can be pause by clicking again on this button. When paused, and after
 * being started the application can be restarted clicking on the button <b>Restart</b>.
 * This button should also be used to initialize the simulations once the dataset is changed
 * or the model is changed. The <b>Optimize</b> button initiates an optimization and is only active
 * in the ANTCLASS2 model. 
 * <p>
 * The parameter grid shows all the parameters used in the simulation corresponding to each model.
 * Each parameter can be changed by changing is value in the right cell, pressing Enter. Only after
 * the <b>Apply</b> button is pressed the parameters are committed to the simulation running configuration.
 * If the simulation is started without applying the changes the parameters are reversed to
 * their former value.
 * <p>
 * Below the parameters table is the measures table showing the values of the measures
 * after modulo 100 iterations of the simulation.
 * <p>Below the measures table is a text box for entering the filename (with complete path) for
 * the recording of the measures of the simulations.
 * 
 * @author      Ant�nio Fonseca
 * @version     1.5
 * @since       1.0
 * */
public class Clustering {

	private Configuration conf;
	private Simulation simul;
	private JFrame frame;
	private JTextField textField_1;
	private Thread runner;
	private JTable table, table_1;
	private JLabel lblNewLabel;
	private JTextPane textPane;
	private JToggleButton tglbtnStart;
	private JToggleButton btnOptimize;
	private JButton btnRestart;


	/**
	 * Launch the application.
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Clustering window = new Clustering();
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
	public Clustering() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		conf = new Configuration();
		simul = new Simulation(conf,this);
		simul.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		frame = new JFrame();
		frame.setBounds(100, 100, 980, 920);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(402, 21, 561, 555);
		frame.getContentPane().add(scrollPane);
		
		simul.setBackground(Color.WHITE);
		simul.setPreferredSize(new Dimension(2000,2000));
		scrollPane.setViewportView(simul);
		
		JLabel lblDataset = new JLabel("Dataset");
		lblDataset.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDataset.setBounds(33, 11, 46, 14);
		frame.getContentPane().add(lblDataset);
		
		JToggleButton tglbtnRecord = new JToggleButton("Record");
		tglbtnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JToggleButton bt = (JToggleButton)arg0.getSource();
				if (bt.isSelected()){
					bt.setForeground(Color.RED);
					textField_1.setText(conf.getFilename());
					textField_1.setForeground(Color.BLUE);
					simul.setRec(true);
				}
				else{
					bt.setForeground(Color.BLACK);
					simul.setRec(false);
				}
			}
		});
		tglbtnRecord.setFont(new Font("Tahoma", Font.BOLD, 11));
		tglbtnRecord.setBounds(24, 846, 76, 23);
		frame.getContentPane().add(tglbtnRecord);
		
		textField_1 = new JTextField();
		textField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				JTextField txt = (JTextField)arg0.getSource();
				if (arg0.getKeyCode() == 10) {
					txt.setForeground(Color.BLUE);
					conf.setFilename(txt.getText()+".txt");
				}
				else txt.setForeground(Color.BLACK);
			}
		});
		textField_1.setBounds(24, 815, 340, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblFilename = new JLabel("Filename (without extension, press ENTER to check)");
		lblFilename.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFilename.setBounds(24, 790, 340, 14);
		frame.getContentPane().add(lblFilename);
		
		JLabel lblVariables = new JLabel("Measures");
		lblVariables.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblVariables.setBounds(24, 607, 76, 14);
		frame.getContentPane().add(lblVariables);
		
		JLabel lblParameters = new JLabel("Model Parameters");
		lblParameters.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblParameters.setBounds(24, 136, 154, 14);
		frame.getContentPane().add(lblParameters);
		
		JLabel lblModel = new JLabel("Model");
		lblModel.setToolTipText("Model Simulated");
		lblModel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblModel.setBounds(158, 11, 46, 14);
		frame.getContentPane().add(lblModel);
		
		table = new JTable();
		table.setCellSelectionEnabled(true);
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
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
		) );
		table.setBounds(25, 170, 340, 426);
		frame.getContentPane().add(table);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JComboBox cb = (JComboBox)arg0.getSource();
				conf.setModel((Models)cb.getSelectedItem());
				simul.update(conf);
				HashMap<String,Double> h = conf.getParameters();
				table.setModel(toTableModel(h));
				if (cb.getSelectedItem().equals(Configuration.Models.ANTCLASS2))
					btnOptimize.setText("Optimize");
				else btnOptimize.setText("");
			}
		});
		comboBox_1.setModel(new DefaultComboBoxModel(Models.values()));
		comboBox_1.setToolTipText("Simulation models");
		comboBox_1.setBounds(152, 37, 157, 22);
		frame.getContentPane().add(comboBox_1);
		
		JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JComboBox cb = (JComboBox)arg0.getSource();
				conf.setDataset((Datasets)cb.getSelectedItem());
				simul.update(conf);
				simul.repaint();
				HashMap<String,Double> h = conf.getParameters();
				table.setModel(toTableModel(h));
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(Datasets.values()));
		comboBox.setToolTipText("Datasets to be clustered");
		comboBox.setBounds(24, 37, 123, 22);
		frame.getContentPane().add(comboBox);
		
		tglbtnStart = new JToggleButton("Start");
		tglbtnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JToggleButton bt = (JToggleButton) e.getSource();
				if (bt.getText()=="Start"){
				if (bt.isSelected()){
					btnRestart.setText("");
					btnOptimize.setText("");
					if (runner == null) {
						simul.setInterrupted(false);
						runner = new Thread(simul);
						runner.start();
					}
					else {
						synchronized (simul){
						simul.setInterrupted(false);
						simul.notify();
						}
					}
				}
				else {
					synchronized (simul){
					simul.setInterrupted(true);
					simul.notify();
					simul.stop();
					}
					btnRestart.setText("Restart");
				}
			}
			}
		});
		tglbtnStart.setFont(new Font("Tahoma", Font.BOLD, 11));
		tglbtnStart.setBounds(24, 96, 91, 23);
		frame.getContentPane().add(tglbtnStart);
		
		btnRestart = new JButton("");
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnRestart.getText()=="Restart"){
					if (runner == null){
					synchronized (simul) {
						simul.update(conf);
						simul.repaint();
						}
					}
					else {
						runner = null;
						synchronized (simul) {
							simul.update(conf);
							simul.repaint();
							}
					}
					lblNewLabel.setText("Tick : 0");
					if (simul.getRec()) simul.writeRecord(conf.getFilename());
					HashMap<String,Double> h = conf.getParameters();
					table.setModel(toTableModel(h));
					tglbtnStart.setText("Start");
					if (conf.getModel()==Configuration.Models.ANTCLASS2) btnOptimize.setText("Optimize");
			}
			}
		});
		btnRestart.setToolTipText("Reset the simulation Conditions");
		btnRestart.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnRestart.setBounds(125, 96, 91, 23);
		frame.getContentPane().add(btnRestart);
		
		btnOptimize = new JToggleButton("");
		btnOptimize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JToggleButton bt = (JToggleButton) e.getSource();
				if (bt.getText()=="Optimize")
					if (bt.isSelected()){
						tglbtnStart.setText("");
						btnRestart.setText("");
						if (runner == null)	{
								simul.setOpt(true);
								simul.setInterrupted(false);
								runner = new Thread(simul);
								runner.start();
							}
					}
					else {
						synchronized (simul){
							simul.setOpt(false);
							simul.setInterrupted(true);
							simul.notify();
							simul.stop();
							}
						textPane.setText(textPane.getText()+"\n\nWait for the run to complete.");

					}
					HashMap<String,Double> h = conf.getParameters();
					table.setModel(toTableModel(h));
				}
		});

		btnOptimize.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnOptimize.setBounds(226, 96, 91, 23);
		frame.getContentPane().add(btnOptimize);
		
		JCheckBox chckbxDisplyOriginalSet = new JCheckBox("Display original set");
		chckbxDisplyOriginalSet.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				JCheckBox cb = (JCheckBox) arg0.getSource();
				simul.setOriginal(cb.isSelected());
				simul.repaint();
			}
		});
		chckbxDisplyOriginalSet.setSelected(true);
		chckbxDisplyOriginalSet.setBounds(24, 66, 154, 23);
		frame.getContentPane().add(chckbxDisplyOriginalSet);
				
		JButton btnNewButton = new JButton("+");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				synchronized (simul){
					simul.zoom(true);
					simul.notify();
					simul.repaint();
				}
			}
		});
		btnNewButton.setBounds(332, 34, 58, 23);
		frame.getContentPane().add(btnNewButton);
		
		JButton button = new JButton("-");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				synchronized (simul){
					simul.zoom(false);
					simul.notify();
					simul.repaint();
				}
			}
		});
		button.setBounds(332, 66, 58, 23);
		frame.getContentPane().add(button);
		
		JLabel lblZoom = new JLabel("Zoom");
		lblZoom.setToolTipText("Model Simulated");
		lblZoom.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblZoom.setBounds(344, 9, 46, 14);
		frame.getContentPane().add(lblZoom);
		


		table_1 = new JTable();
		table_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
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
		));
		
		table_1.setValueAt("Pearson's Correlation", 0, 0);
		table_1.setValueAt("Entropy", 1, 0);
		table_1.setValueAt("F measure", 2, 0);
		table_1.setValueAt("Rand Index", 3, 0);
		table_1.setValueAt("Inner Cluster Variance", 4, 0);
		table_1.setValueAt("Silhouette Index", 5, 0);
		
		table_1.setBounds(24, 632, 341, 147);
		frame.getContentPane().add(table_1);
		
		JButton btnNewButton_1 = new JButton("Apply");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TableModel t = table.getModel();
				for (int i=0; i< t.getRowCount(); i++){
					String k = (String)t.getValueAt(i, 0);
					double v = new Double(t.getValueAt(i, 1).toString());
					conf.setParameters(k,v);
					HashMap<String,Double> h = conf.getParameters();
					table.setModel(toTableModel(h));
				}
			}
		});
		btnNewButton_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		btnNewButton_1.setBounds(288, 133, 76, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		lblNewLabel = new JLabel("Tick: 0");
		lblNewLabel.setBounds(158, 137, 123, 16);
		frame.getContentPane().add(lblNewLabel);
		
		textPane = new JTextPane();
		textPane.setBounds(406, 599, 557, 283);
		frame.getContentPane().add(textPane);
		
		HashMap<String,Double> h = conf.getParameters();
		table.setModel(toTableModel(h));
		
		JCheckBox chckbxDisplayClusters = new JCheckBox("Display clusters");
		chckbxDisplayClusters.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				JCheckBox cb = (JCheckBox) arg0.getSource();
				simul.setClusters(cb.isSelected());
				simul.repaint();	
			}
		});
		chckbxDisplayClusters.setSelected(false);
		chckbxDisplayClusters.setBounds(186, 66, 123, 23);
		frame.getContentPane().add(chckbxDisplayClusters);
	
	}
	
	
	/**
	 * The model for the table of parameters
	 * @param map a map with the parameters names and values
	 * @return a table model
	 */
	public TableModel toTableModel(HashMap<String,Double> map) {
	    DefaultTableModel model = new DefaultTableModel(
	        new Object[] { "Parameter", "Value" }, 0
	    );
	    for (Map.Entry<String,Double> entry : map.entrySet()) {
	        model.addRow(new Object[] { entry.getKey(), entry.getValue() });
	    }
	    return model;
	}
	
	/************* Access Functions *****************************************************************/	
	
	/** Set the pearson correlation value on measure table
	 * @param v the pearson value
	 */
	public void setPearsons(double v){
		table_1.setValueAt(v, 0, 1);
	}
	
	
	/** Set the entropy correlation value on measure table
	 * @param v the entropy value
	 */
	public void setEntropy(double v){
		table_1.setValueAt(v, 1, 1);
	}
	
	/** Set the F measure value on measure table
	 * @param v the F measure
	 */
	public void setF(double v){
		table_1.setValueAt(v, 2, 1);
	}
	
	/** Set the rand index value on measure table
	 * @param v the index value
	 */
	public void setRand(double v){
		table_1.setValueAt(v, 3, 1);
	}
	
	/** Set the inner cluster variance value on measure table
	 * @param v the inner variance value
	 */
	public void setInnerCV(double v){
		table_1.setValueAt(v, 4, 1);
	}
	
	/** Set the silhoutette index value on measure table
	 * @param v the silhouette index value
	 */
	public void setSilhouette(double v){
		table_1.setValueAt(v, 5, 1);
	}
	
	/** Set tick value on panel
	 * @param v the tick value
	 */
	public void setTick(int v){
		lblNewLabel.setText("Tick : "+ v);
	}
	
	/**Set text value on window below grid
	 * @param s string to display in window
	 */
	public void setText(String s){
		textPane.setText(s);
	}
	
	/**Update the table with current parameter values
	 */
	public void updateTable(){
		HashMap<String,Double> h = conf.getParameters();
		table.setModel(toTableModel(h));
	}
	
	/** Stop the simulation and update panel
	 * @param text text to display in window
	 */
	public void stop(String text){
		textPane.setText(textPane.getText()+text);
		synchronized (simul){
			simul.setInterrupted(true);
			simul.notify();
		}
		btnRestart.setText("Restart");
		tglbtnStart.setText("");
		btnOptimize.setText("");
		tglbtnStart.setSelected(false);
	}
	
	/** Stop the simulation and update panel
	 * @param text text to display in window
	 */
	public void stopOpt(String text){
		textPane.setText(textPane.getText()+text);
		btnRestart.setText("Restart");
		btnOptimize.setText("");
		tglbtnStart.setText("");
		btnOptimize.setSelected(false);
	}
	
}
