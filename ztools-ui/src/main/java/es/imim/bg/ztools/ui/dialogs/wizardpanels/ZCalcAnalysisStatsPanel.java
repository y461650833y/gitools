package es.imim.bg.ztools.ui.dialogs.wizardpanels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URL;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.dialogs.AnalysisWizard.Condition;
import es.imim.bg.ztools.ui.utils.Options;

public class ZCalcAnalysisStatsPanel extends JPanel {
	
	private static final long serialVersionUID = 4868634835041548193L;
    public final String MAX_DEFAULT = "no limit";
    public final String MIN_DEFAULT = "20";
		
	private JLabel blankSpace;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    
    private JComboBox jComboBox1;
    
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;

    private JButton jButton1;

    
    private JLabel panelTitle;
    private JPanel contentPanel;
    
    private JLabel iconLabel;
    private ImageIcon icon;

	public ZCalcAnalysisStatsPanel(){
		
        iconLabel = new JLabel();
        contentPanel = getContentPanel();
        contentPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));

        setLayout(new java.awt.BorderLayout());

        if (icon != null)
            iconLabel.setIcon(icon);
        
        iconLabel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        
        add(iconLabel, BorderLayout.WEST);
        
        JPanel secondaryPanel = new JPanel();
        secondaryPanel.add(contentPanel, BorderLayout.NORTH);
        add(secondaryPanel, BorderLayout.CENTER);
    }
    
    
    private JPanel getContentPanel() {
        
        JPanel contentPanel1 = new JPanel();
        JPanel jPanel1 = new JPanel();
        JPanel jPanel2 = new JPanel();
        JPanel jPanel3 = new JPanel();
        JPanel jPanel4 = new JPanel();
        JPanel jPanel5 = new JPanel();

        panelTitle = new JLabel();
        blankSpace = new JLabel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        jLabel6 = new JLabel();
        jLabel8 = new JLabel();
        jLabel9 = new JLabel();
        
        jComboBox1 = new JComboBox();
        
        jTextField1 = new JTextField();
        jTextField2 = new JTextField();
        jTextField3 = new JTextField();

        jButton1 = new JButton("choose File..");
        
        contentPanel1.setLayout(new java.awt.BorderLayout());

        panelTitle.setText("Module");
        panelTitle.setFont(new java.awt.Font(panelTitle.getFont().getName(), Font.BOLD, 18));
        contentPanel1.add(panelTitle, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.GridLayout(0, 1));

        jPanel1.add(blankSpace);
        jLabel1.setText("Please choose the file containing a module map.");
        jPanel1.add(jLabel1);
        jLabel2.setText("By using the min and/or max fields, you can discard ");
        jPanel1.add(jLabel2);
        jLabel3.setText("modules with an item number out of the defined range. ");
        jPanel1.add(jLabel3);
        jLabel4.setText("Default values are " + MIN_DEFAULT + " (min) & " + MAX_DEFAULT + " (max).");
        jPanel1.add(jLabel4);
        jPanel1.add(blankSpace); 
        
        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.PAGE_AXIS));
        
        jPanel3.setLayout(new BoxLayout(jPanel3, BoxLayout.LINE_AXIS)); 
        jLabel5.setText("Module Map File: ");
        jTextField1.setToolTipText("Choose the data file");
        jTextField1.setEditable(false);
        jPanel3.add(jLabel5);
        jPanel3.add(jTextField1);
        jPanel3.add(jButton1);
        jPanel2.add(jPanel3);
        
        jPanel4.setLayout(new BoxLayout(jPanel4, BoxLayout.LINE_AXIS));

        jLabel6.setText("Minimum of items : ");
        jPanel4.add(jLabel6);
        jTextField2.setToolTipText("Default: " + MIN_DEFAULT);
        jPanel4.add(jTextField2);
        jPanel2.add(blankSpace);
        jPanel2.add(jPanel4);
        
        jPanel5.setLayout(new BoxLayout(jPanel5, BoxLayout.LINE_AXIS));

        jLabel7.setText("Maximum of items : ");
        jPanel5.add(jLabel7);
        jTextField3.setToolTipText("Default: " + MAX_DEFAULT);
        jPanel5.add(jTextField3);
        jPanel2.add(blankSpace);
        jPanel2.add(jPanel5);
        

        contentPanel1.add(jPanel1, java.awt.BorderLayout.CENTER);
        contentPanel1.add(jPanel2, BorderLayout.SOUTH);

        return contentPanel1;
        
    }
	
	public JButton getChoserButton(){
		return jButton1;
	}
	
	public JTextField getFileNameField(){
		return jTextField1;
	}
	
	public JComboBox getBinCutoffConditionBox(){
		return jComboBox1;
	}
	
	public JTextField getMinimumField() {
		return jTextField2;
	}
	
	public JTextField getMaximumField() {
		return jTextField3;
	}	
}


