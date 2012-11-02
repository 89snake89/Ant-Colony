/*  
    Copyright (C) 2012 Antonio Fonseca
    Email: antoniofilipefonseca@gmail.com

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*/

/*****************************************************************
	Antonio Fonseca
	antoniofilipefonseca@gmail.com

	File: Clustering.java
	Package: antcolony

	Description:

	* Represents the main panel for the application
	* Stores the configuration, the data and the simulation variables
	* Performs actions according to user commands
		
                                                                                                                        	
*****************************************************************/

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


public class Clustering {

	private Configuration conf;
	private Simulation simul;
	private JFrame frame;
	private JTextField textField_1;
	private JButton btnStop;
	private Thread runner;
	private JTable table, table_1;
	private JLabel lblNewLabel;
	private JTextPane textPane;
	private JToggleButton tglbtnStart;

	/**
	 * Launch the application.
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
		frame.setBounds(100, 100, 980, 857);
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
		
		tglbtnStart = new JToggleButton("Start");
		tglbtnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JToggleButton bt = (JToggleButton) e.getSource();
				if (bt.isSelected()){
					btnStop.setText("");
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
					}
					btnStop.setText("Restart");
				}
			}
		});
		tglbtnStart.setFont(new Font("Tahoma", Font.BOLD, 11));
		tglbtnStart.setBounds(34, 105, 91, 23);
		frame.getContentPane().add(tglbtnStart);
		
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
		tglbtnRecord.setBounds(24, 796, 76, 23);
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
		textField_1.setBounds(24, 765, 340, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblFilename = new JLabel("Filename (without extension, press ENTER to check)");
		lblFilename.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFilename.setBounds(24, 740, 340, 14);
		frame.getContentPane().add(lblFilename);
		
		JLabel lblVariables = new JLabel("Measures");
		lblVariables.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblVariables.setBounds(24, 624, 76, 14);
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
			},
			new String[] {
				"Parameter", "Value"
			}
		) );
		table.setBounds(25, 162, 340, 451);
		frame.getContentPane().add(table);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JComboBox cb = (JComboBox)arg0.getSource();
				conf.setModel((Models)cb.getSelectedItem());
				simul.update(conf);
				HashMap<String,Double> h = conf.getParameters();
				table.setModel(toTableModel(h));			
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
		
		btnStop = new JButton("");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnStop.getText()=="Restart"){
					if (runner == null){	
					synchronized (simul) {
						simul.update(conf);
						simul.repaint();
						}
					}
					else {
						//runner.stop();
						runner = null;
						synchronized (simul) {
							simul.update(conf);
							simul.repaint();
							}
					}
					lblNewLabel.setText("Tick : 0");
					if (simul.getRec()) simul.writeRecord(conf.getFilename());
				}
				HashMap<String,Double> h = conf.getParameters();
				table.setModel(toTableModel(h));
			}
		});
		btnStop.setToolTipText("Reset the simulation Conditions");
		btnStop.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnStop.setBounds(146, 105, 91, 23);
		frame.getContentPane().add(btnStop);
		
		JCheckBox chckbxDisplyOriginalSet = new JCheckBox("Display original set");
		chckbxDisplyOriginalSet.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				JCheckBox cb = (JCheckBox) arg0.getSource();
				simul.setOriginal(cb.isSelected());
				simul.repaint();
			}
		});
		chckbxDisplyOriginalSet.setSelected(true);
		chckbxDisplyOriginalSet.setBounds(24, 66, 123, 23);
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
		btnNewButton.setBounds(332, 34, 58, 29);
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
		button.setBounds(332, 65, 58, 29);
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
			},
			new String[] {
				"Measure", "Value"
			}
		));
		
		table_1.setValueAt("Pearson's Correlation", 0, 0);
		table_1.setValueAt("Entropy", 1, 0);
		table_1.setValueAt("F measure", 2, 0);
		table_1.setValueAt("Rand", 3, 0);
		table_1.setValueAt("Inner Cluster Variance", 4, 0);
		
		table_1.setBounds(24, 649, 341, 80);
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
		btnNewButton_1.setBounds(273, 131, 91, 29);
		frame.getContentPane().add(btnNewButton_1);
		
		lblNewLabel = new JLabel("Tick: 0");
		lblNewLabel.setBounds(273, 106, 117, 16);
		frame.getContentPane().add(lblNewLabel);
		
		textPane = new JTextPane();
		textPane.setBounds(406, 599, 557, 220);
		frame.getContentPane().add(textPane);
		
		HashMap<String,Double> h = conf.getParameters();
		table.setModel(toTableModel(h));
		
		JCheckBox chckbxDisplayClusters = new JCheckBox("Display clusters");
		chckbxDisplayClusters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JCheckBox cb = (JCheckBox) arg0.getSource();
				simul.setClusters(cb.isSelected());
				simul.repaint();	
			}
		});
		chckbxDisplayClusters.setSelected(true);
		chckbxDisplayClusters.setBounds(152, 66, 123, 23);
		frame.getContentPane().add(chckbxDisplayClusters);
	
	}
	
	
	/*
	 * The model for the table of parameters
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
	
	/* Set the pearson correlation value on measure table
	 * @param pearson value
	 */
	public void setPearsons(double v){
		table_1.setValueAt(v, 0, 1);
	}
	
	
	/* Set the entropy correlation value on measure table
	 * @param entropy
	 */
	public void setEntropy(double v){
		table_1.setValueAt(v, 1, 1);
	}
	
	/* Set the F measure value on measure table
	 * @param F measure
	 */
	public void setF(double v){
		table_1.setValueAt(v, 2, 1);
	}
	
	/* Set the rand index value on measure table
	 * @param rand index value
	 */
	public void setRand(double v){
		table_1.setValueAt(v, 3, 1);
	}
	
	/* Set the inner cluster variance value on measure table
	 * @param rand index value
	 */
	public void setInnerCV(double v){
		table_1.setValueAt(v, 4, 1);
	}
	
	/* Set tick value on panel
	 * @param tick value
	 */
	public void setTick(int v){
		lblNewLabel.setText("Tick : "+ v);
	}
	
	public void setText(String s){
		textPane.setText(s);
	}
	
	public void stop(){
		textPane.setText(textPane.getText()+"\n Reached minimum F");
		synchronized (simul){
			simul.setInterrupted(true);
			simul.notify();
		}
		btnStop.setText("Restart");
		tglbtnStart.setSelected(false);
	}
}
