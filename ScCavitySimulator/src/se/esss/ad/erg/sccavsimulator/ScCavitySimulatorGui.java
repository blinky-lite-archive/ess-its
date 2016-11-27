package se.esss.ad.erg.sccavsimulator;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import se.esss.ad.erg.sccavsimulator.DavesXYPlot.DavesXYPlotException;

public class ScCavitySimulatorGui extends  JFrame
{
	private static final long serialVersionUID = 6596976469043311172L;
	static final double PI = Math.PI;
	static final double degToRad = PI / 180.0;
	static final double radToDeg = 180.0 / PI;
	static final double TWOPI = 2.0 * PI;

	private int numSimPts = 10000;
	private double rfFreqMHz = 704;
	private double r_Q_accel = 477;
	private double cavityVoltageMV = 17;
	private double referenceCavityVoltageMV = 17;
	private double beamCurrentmA = 40.0;
	private double setBeamCurrentmA = 40.0;
	private double referenceBeamCurrentmA = 50;
	private double phiSynchDeg = 20;
	private double setPhiSynchDeg = 20;
	private double referencePhiSynchDeg = 20.0;
	private double phiDdc = 20.0;
	private double setPhiDdc = 20.0;
	private double tBeamStartmS = 0.238623;
	private double setTBeamStartmS = 0.238623;
	private double tBeamPulsemS = 2.8;
	private double setTBeamPulsemS = 2.8;
	private double tEndmS = 4.0;

	private JPanel plotPanel;
	private JPanel mainPanel;
	private JPanel parameterPanel;

	DavesXYPlot cavVoltageXYPlot = new DavesXYPlot();
	DavesXYPlot klystronXYPlot = new DavesXYPlot();
	DavesXYPlot beamcurrentXYPlot = new DavesXYPlot();
	DavesXYPlot gencurrentXYSeries = new DavesXYPlot();
	int xyPlotHeight = 300;
	int xyPlotWidth = 400;

	private JTextField numSimPtsTextField;
	private JTextField rfFreqMHzTextField;
	private JTextField r_Q_accelTextField;
	private JTextField cavityVoltageMVTextField;
	private JTextField referenceCavityVoltageMVTextField;
	private JTextField beamCurrentmATextField;
	private JTextField setBeamCurrentmATextField;
	private JTextField referenceBeamCurrentmATextField;
	private JTextField phiSynchDegTextField;
	private JTextField setPhiSynchDegTextField;
	private JTextField referencePhiSynchDegTextField;
	private JTextField phiDdcTextField;
	private JTextField setPhiDdcTextField;
	private JTextField tBeamStartmSTextField;
	private JTextField setTBeamStartmSTextField;
	private JTextField tBeamPulsemSTextField;
	private JTextField setTBeamPulsemSTextField;
	private JTextField tEndmSTextField;

	private ScCavitySimulator scs;

	public ScCavitySimulatorGui() throws FileNotFoundException
	{
		super("Superconducting Cavity Voltage Simulator - ver 2.1 - D. McGinnis");
		ImageIcon  logoIcon = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("se/esss/ad/erg/sccavsimulator/resources/essIcon.png"));
        setIconImage(logoIcon.getImage());

        try 
        {
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {}
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scs = new ScCavitySimulator( referenceCavityVoltageMV,  r_Q_accel,  rfFreqMHz,  referenceBeamCurrentmA,  referencePhiSynchDeg,  tEndmS,  numSimPts);
        
		
		numSimPtsTextField = new JTextField(Integer.toString(numSimPts),6);
		rfFreqMHzTextField = new JTextField(Double.toString(rfFreqMHz),6);
		r_Q_accelTextField = new JTextField(Double.toString(r_Q_accel),6);
		
		cavityVoltageMVTextField = new JTextField(Double.toString(cavityVoltageMV),6);
		referenceCavityVoltageMVTextField = new JTextField(Double.toString(referenceCavityVoltageMV),6);
		
		beamCurrentmATextField = new JTextField(Double.toString(beamCurrentmA),6);
		setBeamCurrentmATextField = new JTextField(Double.toString(setBeamCurrentmA),6);
		referenceBeamCurrentmATextField = new JTextField(Double.toString(referenceBeamCurrentmA),6);
		
		phiSynchDegTextField = new JTextField(Double.toString(phiSynchDeg),6);
		setPhiSynchDegTextField = new JTextField(Double.toString(setPhiSynchDeg),6);
		referencePhiSynchDegTextField = new JTextField(Double.toString(referencePhiSynchDeg),6);
		
		phiDdcTextField = new JTextField(Double.toString(phiDdc),6);
		setPhiDdcTextField = new JTextField(Double.toString(setPhiDdc),6);
		
		tBeamStartmSTextField = new JTextField(Double.toString(tBeamStartmS),6);
		setTBeamStartmSTextField = new JTextField(Double.toString(setTBeamStartmS),6);
		
		tBeamPulsemSTextField = new JTextField(Double.toString(tBeamPulsemS),6);
		setTBeamPulsemSTextField = new JTextField(Double.toString(setTBeamPulsemS),6);
		
		tEndmSTextField = new JTextField(Double.toString(tEndmS),6);
		
		setupMainPanel();		
		setContentPane(mainPanel);
		pack();
		setVisible(true);

		setParameters();
		scs.simpleSimulate();
		
		plotData(null, false,true);
		
	}
/*	private void writePropertyFile()
	{
		Properties prop = new Properties();
		 
    	try {
    		//set the properties value
    		prop.setProperty("database", "localhost");
    		prop.setProperty("dbuser", "mkyong");
    		prop.setProperty("dbpassword", "password");
 
    		//save properties to project root folder
    		prop.store(new FileOutputStream("config.properties"), null);
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
	}
*/	public void setupMainPanel()
	{
		parameterPanel = new JPanel();
		parameterPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Parameter"),BorderFactory.createEmptyBorder(5,5,5,5)));
		parameterPanel.setLayout(new GridLayout(19,3));

		parameterPanel.add(new JLabel("No. Sim. Pts."));
		parameterPanel.add(numSimPtsTextField);
		parameterPanel.add(new JLabel(""));

		parameterPanel.add(new JLabel("RF Freq"));
		parameterPanel.add(rfFreqMHzTextField);
		parameterPanel.add(new JLabel("MHz"));

		parameterPanel.add(new JLabel("R / Q (acc)"));
		parameterPanel.add(r_Q_accelTextField);
		parameterPanel.add(new JLabel("Ohms"));

		parameterPanel.add(new JLabel("Cavity Voltage"));
		parameterPanel.add(cavityVoltageMVTextField);
		parameterPanel.add(new JLabel("MV"));

		parameterPanel.add(new JLabel("Ref. Cavity Voltage"));
		parameterPanel.add(referenceCavityVoltageMVTextField);
		parameterPanel.add(new JLabel("MV"));

		parameterPanel.add(new JLabel("Beam Current"));
		parameterPanel.add(beamCurrentmATextField);
		parameterPanel.add(new JLabel("mA"));

		parameterPanel.add(new JLabel("Set Beam Current"));
		parameterPanel.add(setBeamCurrentmATextField);
		parameterPanel.add(new JLabel("mA"));

		parameterPanel.add(new JLabel("Ref. Beam Current"));
		parameterPanel.add(referenceBeamCurrentmATextField);
		parameterPanel.add(new JLabel("mA"));

		parameterPanel.add(new JLabel("Synch. Phase"));
		parameterPanel.add(phiSynchDegTextField);
		parameterPanel.add(new JLabel("deg."));

		parameterPanel.add(new JLabel("Set Synch. Phase"));
		parameterPanel.add(setPhiSynchDegTextField);
		parameterPanel.add(new JLabel("deg."));

		parameterPanel.add(new JLabel("Ref. Synch. Phase"));
		parameterPanel.add(referencePhiSynchDegTextField);
		parameterPanel.add(new JLabel("deg."));

		parameterPanel.add(new JLabel("Static Detuning Phase"));
		parameterPanel.add(phiDdcTextField);
		parameterPanel.add(new JLabel("deg."));

		parameterPanel.add(new JLabel("Set Static Detuning Phase"));
		parameterPanel.add(setPhiDdcTextField);
		parameterPanel.add(new JLabel("deg."));

		parameterPanel.add(new JLabel("Beam Start Time"));
		parameterPanel.add(tBeamStartmSTextField);
		parameterPanel.add(new JLabel("mS"));

		parameterPanel.add(new JLabel("SetBeam Start Time"));
		parameterPanel.add(setTBeamStartmSTextField);
		parameterPanel.add(new JLabel("mS"));

		parameterPanel.add(new JLabel("Beam Pulse Length"));
		parameterPanel.add(tBeamPulsemSTextField);
		parameterPanel.add(new JLabel("mS"));

		parameterPanel.add(new JLabel("Set Beam Pulse Length"));
		parameterPanel.add(setTBeamPulsemSTextField);
		parameterPanel.add(new JLabel("mS"));

		parameterPanel.add(new JLabel("Simulation End Time"));
		parameterPanel.add(tEndmSTextField);
		parameterPanel.add(new JLabel("mS"));

		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(new GuiActionListener("Update"));
		parameterPanel.add(new JLabel(""));
		parameterPanel.add(updateButton);
		parameterPanel.add(new JLabel(""));
		

		plotPanel = new JPanel();
		plotPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Plots"),BorderFactory.createEmptyBorder(5,5,5,5)));
		plotPanel.setLayout(new GridLayout(2,2));

		mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),BorderFactory.createEmptyBorder(5,5,5,5)));
		mainPanel.setLayout(new FlowLayout());
		
		mainPanel.add(plotPanel);
		mainPanel.add(parameterPanel);

		return;
	}
	public void plotData(String dataFileName, boolean printData, boolean initialize)
	{
		double[] beamCurrentmAMag = new double[scs.getNumSimPts()];
		double[] beamCurrentmARe = new double[scs.getNumSimPts()];
		double[] beamCurrentmAIm = new double[scs.getNumSimPts()];
		double[] genCurrentmAMag = new double[scs.getNumSimPts()];
		double[] genCurrentmARe = new double[scs.getNumSimPts()];
		double[] genCurrentmAIm = new double[scs.getNumSimPts()];
		double[] cavVoltsMVMag = new double[scs.getNumSimPts()];
		double[] cavVoltsMVRe = new double[scs.getNumSimPts()];
		double[] cavVoltsMVIm = new double[scs.getNumSimPts()];
		double[] simTimemS = new double[scs.getNumSimPts()];
		double[] klystronPowerKwForward = new double[scs.getNumSimPts()];
		double[] klystronPowerKwReflected = new double[scs.getNumSimPts()];

		double klystronPowerkW = 0.0;
		double klystronPhaseDeg = 0.0;
		PrintWriter pw = null;
		if (printData)
		{
			try {pw = new PrintWriter(dataFileName);} catch (FileNotFoundException e) {e.printStackTrace();}
			pw.println("index" + "," + "time (ms)" + "," + "Re Volts (MV)" + "," + "Im Volts (MV)" +  "," + "Klystron Power (kW)" +  "," + "Klystron Phase (Deg)" +  "," + "iBeam Mag (mA)"  +  "," + "iBeam Ang (deg)");
		}	
		for (int ii = 0; ii < scs.getNumSimPts(); ++ii)
		{
			klystronPowerkW = scs.klystronPower( ii) / 1000;
			klystronPhaseDeg = scs.getGenCurrent()[ii].theta() * radToDeg;
			genCurrentmAMag[ii] = scs.getGenCurrent()[ii].magnitude() * 1000.0;
			genCurrentmARe[ii] = scs.getGenCurrent()[ii].re * 1000.0;
			genCurrentmAIm[ii] = scs.getGenCurrent()[ii].im * 1000.0;
			klystronPowerKwForward[ii] = scs.klystronPower( ii) / 1000;
			klystronPowerKwReflected[ii] = klystronPowerKwForward[ii] * scs.gamma(ii).magnitudeSquared();
			beamCurrentmAMag[ii] = scs.getRfbeamCurrent()[ii].magnitude() * 1000.0 / 2.0;
			beamCurrentmARe[ii] = beamCurrentmAMag[ii] * Math.cos( scs.getRfbeamCurrent()[ii].theta());
			beamCurrentmAIm[ii] = beamCurrentmAMag[ii] * Math.sin( scs.getRfbeamCurrent()[ii].theta());
			cavVoltsMVMag[ii] = scs.getCavVolts()[ii].magnitude() / 1e+6;
			cavVoltsMVRe[ii] = scs.getCavVolts()[ii].re / 1e+6;
			cavVoltsMVIm[ii] = scs.getCavVolts()[ii].im / 1e+6;
			simTimemS[ii] = scs.simTime(ii) * 1000.0;
			if (printData) pw.println(ii + "," + simTimemS[ii] + "," + (cavVoltsMVRe[ii]) + "," + (cavVoltsMVIm[ii]) +  "," + klystronPowerkW +  "," + klystronPhaseDeg +  "," + beamCurrentmAMag[ii]  +  "," + scs.getRfbeamCurrent()[ii].theta() * radToDeg);
		}
		if (printData) pw.close();

		try 
		{
			if (initialize)
			{
				cavVoltageXYPlot.addSeries("Mag.", simTimemS, cavVoltsMVMag);
				cavVoltageXYPlot.addSeries("Real", simTimemS, cavVoltsMVRe);
				cavVoltageXYPlot.addSeries("Imag", simTimemS, cavVoltsMVIm);
				cavVoltageXYPlot.setChartTitle("Cavity Voltage");
				cavVoltageXYPlot.setxAxisTitle("Time (mS)");
				cavVoltageXYPlot.setyAxisTitle("Volts (MV)");
				cavVoltageXYPlot.createChartPanel( xyPlotWidth, xyPlotHeight);
			}
			else
			{
				cavVoltageXYPlot.removeAllSeries();
				cavVoltageXYPlot.addSeries("Mag.", simTimemS, cavVoltsMVMag);
				cavVoltageXYPlot.addSeries("Real", simTimemS, cavVoltsMVRe);
				cavVoltageXYPlot.addSeries("Imag", simTimemS, cavVoltsMVIm);
			}
		} catch (DavesXYPlotException e) {e.printStackTrace();}
		
		try 
		{
			if (initialize)
			{
				klystronXYPlot.addSeries("Forward.", simTimemS, klystronPowerKwForward);
				klystronXYPlot.addSeries("Reflected", simTimemS, klystronPowerKwReflected);
				klystronXYPlot.setChartTitle("Klystron Power");
				klystronXYPlot.setxAxisTitle("Time (mS)");
				klystronXYPlot.setyAxisTitle("Power (kW)");
				klystronXYPlot.createChartPanel(  xyPlotWidth, xyPlotHeight);
			}
			else
			{
				klystronXYPlot.removeAllSeries();
				klystronXYPlot.addSeries("Forward.", simTimemS, klystronPowerKwForward);
				klystronXYPlot.addSeries("Reflected", simTimemS, klystronPowerKwReflected);
			}
		} catch (DavesXYPlotException e) {e.printStackTrace();}
		
		try 
		{
			if (initialize)
			{
				beamcurrentXYPlot.addSeries("Beam - Mag", simTimemS, beamCurrentmAMag);
				beamcurrentXYPlot.addSeries("Beam - Re ", simTimemS, beamCurrentmARe);
				beamcurrentXYPlot.addSeries("Beam - Im ", simTimemS, beamCurrentmAIm);
				beamcurrentXYPlot.setChartTitle("Beam Current");
				beamcurrentXYPlot.setxAxisTitle("Time (mS)");
				beamcurrentXYPlot.setyAxisTitle("Current (mA)");
				beamcurrentXYPlot.createChartPanel(  xyPlotWidth, xyPlotHeight);
			}
			else
			{
				beamcurrentXYPlot.removeAllSeries();
				beamcurrentXYPlot.addSeries("Beam - Mag", simTimemS, beamCurrentmAMag);
				beamcurrentXYPlot.addSeries("Beam - Re ", simTimemS, beamCurrentmARe);
				beamcurrentXYPlot.addSeries("Beam - Im ", simTimemS, beamCurrentmAIm);
			}
		} catch (DavesXYPlotException e) {e.printStackTrace();}
		
		try 
		{
			if (initialize)
			{
				gencurrentXYSeries.addSeries("Generator - Mag", simTimemS, genCurrentmAMag);
				gencurrentXYSeries.addSeries("Generator - Re ", simTimemS, genCurrentmARe);
				gencurrentXYSeries.addSeries("Generator - Im ", simTimemS, genCurrentmAIm);
				gencurrentXYSeries.setChartTitle("Generator Current");
				gencurrentXYSeries.setxAxisTitle("Time (mS)");
				gencurrentXYSeries.setyAxisTitle("Current (mA)");
				gencurrentXYSeries.createChartPanel(  xyPlotWidth, xyPlotHeight);
			}
			else
			{
				gencurrentXYSeries.removeAllSeries();
				gencurrentXYSeries.addSeries("Generator - Mag", simTimemS, genCurrentmAMag);
				gencurrentXYSeries.addSeries("Generator - Re ", simTimemS, genCurrentmARe);
				gencurrentXYSeries.addSeries("Generator - Im ", simTimemS, genCurrentmAIm);
			}
		} catch (DavesXYPlotException e) {e.printStackTrace();}

		if (initialize)
		{
			plotPanel.removeAll();
			plotPanel.add(cavVoltageXYPlot.getChartPanel());
			plotPanel.add(klystronXYPlot.getChartPanel());
			plotPanel.add(beamcurrentXYPlot.getChartPanel());
			plotPanel.add(gencurrentXYSeries.getChartPanel());
			pack();
		}

//		System.out.println(cavVoltageXYPlot.getSeriesCount());
	}
	private boolean textFieldOk(JTextField jTextField, String desc, String type)
	{
		boolean isNumber = true;
		try
		{
			if (type.toLowerCase().equals("int")) Integer.parseInt(jTextField.getText());
			if (type.toLowerCase().equals("double")) Double.parseDouble(jTextField.getText());
		}
		catch (NumberFormatException nfe) {isNumber = false;}
		if (!isNumber)
		{
			jTextField.setText("");
			JOptionPane.showMessageDialog(this, "Bad value for " + desc); 
			return false;
		}
		return true;
	}
	private boolean textFieldsOk()
	{
		if (!textFieldOk(numSimPtsTextField, "Num. Sim Pts.", "int")) return false;
		if (!textFieldOk(rfFreqMHzTextField, "RF Freq", "double")) return false;
		if (!textFieldOk(r_Q_accelTextField, "R / Q", "double")) return false;
		if (!textFieldOk(cavityVoltageMVTextField, "Cavity Volts", "double")) return false;
		if (!textFieldOk(referenceCavityVoltageMVTextField, "Ref. Cavity Volts", "double")) return false;
		if (!textFieldOk(beamCurrentmATextField, "Beam Current", "double")) return false;
		if (!textFieldOk(setBeamCurrentmATextField, "Set Beam Current", "double")) return false;
		if (!textFieldOk(referenceBeamCurrentmATextField, "Ref. Beam Current", "double")) return false;
		if (!textFieldOk(phiSynchDegTextField, "Synchronous Phase", "double")) return false;
		if (!textFieldOk(setPhiSynchDegTextField, "Set Synchronous Phase", "double")) return false;
		if (!textFieldOk(referencePhiSynchDegTextField, "Ref. Synchronous Phase", "double")) return false;
		if (!textFieldOk(phiDdcTextField, "Static Detuning Phase", "double")) return false;
		if (!textFieldOk(setPhiDdcTextField, "Set Static Detuning Phase", "double")) return false;
		if (!textFieldOk(tBeamStartmSTextField, "Beam Start Time", "double")) return false;
		if (!textFieldOk(setTBeamStartmSTextField, "Set Beam Start Time", "double")) return false;
		if (!textFieldOk(tBeamPulsemSTextField, "Beam Pulse length", "double")) return false;
		if (!textFieldOk(setTBeamPulsemSTextField, "Set Beam Pulse length", "double")) return false;
		if (!textFieldOk(tEndmSTextField, "Sim. End Time", "double")) return false;

		return true;
	}
	private void readTextFields()
	{
		numSimPts = Integer.parseInt(numSimPtsTextField.getText());
		rfFreqMHz = Double.parseDouble(rfFreqMHzTextField.getText());
		r_Q_accel = Double.parseDouble(r_Q_accelTextField.getText());
		
		cavityVoltageMV = Double.parseDouble(cavityVoltageMVTextField.getText());
		referenceCavityVoltageMV = Double.parseDouble(referenceCavityVoltageMVTextField.getText());
		
		beamCurrentmA = Double.parseDouble(beamCurrentmATextField.getText());
		setBeamCurrentmA = Double.parseDouble(setBeamCurrentmATextField.getText());
		referenceBeamCurrentmA = Double.parseDouble(referenceBeamCurrentmATextField.getText());
		
		phiSynchDeg = Double.parseDouble(phiSynchDegTextField.getText());
		setPhiSynchDeg = Double.parseDouble(setPhiSynchDegTextField.getText());
		referencePhiSynchDeg = Double.parseDouble(referencePhiSynchDegTextField.getText());
		
		phiDdc = Double.parseDouble(phiDdcTextField.getText());
		setPhiDdc = Double.parseDouble(setPhiDdcTextField.getText());
		
		tBeamStartmS = Double.parseDouble(tBeamStartmSTextField.getText());
		setTBeamStartmS = Double.parseDouble(setTBeamStartmSTextField.getText());
		
		tBeamPulsemS = Double.parseDouble(tBeamPulsemSTextField.getText());
		setTBeamPulsemS = Double.parseDouble(setTBeamPulsemSTextField.getText());
		
		tEndmS = Double.parseDouble(tEndmSTextField.getText());

	}
	private void setParameters()
	{
		scs.setNumSimPts(numSimPts);
		scs.setRfFreqMHz(rfFreqMHz);
		scs.setR_Q_accel(r_Q_accel);
		
		scs.setCavityVoltageMV(cavityVoltageMV);
		scs.setReferenceCavityVoltageMV(referenceCavityVoltageMV);
		
		scs.setBeamCurrentmA(beamCurrentmA);
		scs.setSetBeamCurrentmA(setBeamCurrentmA);
		scs.setReferenceBeamCurrentmA(referenceBeamCurrentmA);
		
		scs.setPhiSynchDeg(phiSynchDeg);
		scs.setSetPhiSynchDeg(setPhiSynchDeg);
		scs.setReferencePhiSynchDeg(referencePhiSynchDeg);
		
		scs.setPhiDdc(phiDdc);
		scs.setSetPhiDdc(setPhiDdc);
		
		scs.settBeamStartmS(tBeamStartmS);
		scs.setSetTBeamStartmS(setTBeamStartmS);
		
		scs.settBeamPulsemS(tBeamPulsemS);
		scs.setSetTBeamPulsemS(setTBeamPulsemS);
		
		scs.settEndmS(tEndmS);
	}
	private void update()
	{
		if (!textFieldsOk()) return;
		readTextFields();
		setParameters();
		
		scs.simpleSimulate();
		plotData(null, false,false);
	}
	private class GuiActionListener implements ActionListener
	{
		String actionString;
		private GuiActionListener(String actionString)
		{
			this.actionString = actionString;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			if (actionString.equals("Update"))
			{
				update();
//				System.out.println("Updated");
			}
			
		}
	}
	public static void main(String[] args) throws FileNotFoundException 
	{
		new ScCavitySimulatorGui();
	}

}
