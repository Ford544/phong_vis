import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SettingsPanel extends JPanel implements ActionListener, ChangeListener, DocumentListener {

    SphereRenderer sr;

    //LIGHT SOURCE
    ArrayList<LightSourcePanel> lightSources;
    JButton addLightSourceButton;
    JButton removeLightSourceButton;


    //MATERIAL

    JTextField diffuseCoeffRed;
    JTextField diffuseCoeffGreen;
    JTextField diffuseCoeffBlue;

    JTextField specularCoeffRed;
    JTextField specularCoeffGreen;
    JTextField specularCoeffBlue;

    JTextField ambienceCoeffRed;
    JTextField ambienceCoeffGreen;
    JTextField ambienceCoeffBlue;

    JSpinner selfIntensityRed;
    JSpinner selfIntensityGreen;
    JSpinner selfIntensityBlue;

    JTextField glossiness;

    //GLOBAL

    JSpinner ambientIntensityRed;
    JSpinner ambientIntensityGreen;
    JSpinner ambientIntensityBlue;

    JTextField c0;
    JTextField c1;
    JTextField c2;

    JCheckBox self;
    JCheckBox diffuse;
    JCheckBox specular;
    JCheckBox ambience;

    JComponent parent;


    public SettingsPanel(SphereRenderer sr, JComponent parent){
        super();
        this.sr = sr;

        this.parent = parent;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        lightSources = new ArrayList<>();
        lightSources.add(new LightSourcePanel());

        addLightSourceButton = new JButton("Add light source");
        addLightSourceButton.addActionListener(this);

        removeLightSourceButton = new JButton("Remove light source");
        removeLightSourceButton.addActionListener(this);

        diffuseCoeffRed = newTextField();
        diffuseCoeffGreen = newTextField();
        diffuseCoeffBlue = newTextField();

        specularCoeffRed = newTextField();
        specularCoeffGreen = newTextField();
        specularCoeffBlue = newTextField();

        ambienceCoeffRed = newTextField();
        ambienceCoeffGreen = newTextField();
        ambienceCoeffBlue = newTextField();

        selfIntensityRed = newColorSpinner();
        selfIntensityGreen = newColorSpinner();
        selfIntensityBlue = newColorSpinner();

        glossiness = newTextField();

        ambientIntensityRed = newColorSpinner();
        ambientIntensityGreen = newColorSpinner();
        ambientIntensityBlue = newColorSpinner();

        c0 = newTextField();
        c1 = newTextField();
        c2 = newTextField();

        self = newCheckBox();
        diffuse = newCheckBox();
        specular = newCheckBox();
        ambience = newCheckBox();

        add(new JLabel("Light source"));
        add(new JSeparator());

        add(lightSources.get(0));

        add(addLightSourceButton);
        add(removeLightSourceButton);

        add(new JLabel("Material"));
        add(new JSeparator());

        addWithLabel(diffuseCoeffRed, "Diffuse reflection coefficient - red");
        addWithLabel(diffuseCoeffGreen, "Diffuse reflection coefficient - green");
        addWithLabel(diffuseCoeffBlue, "Diffuse reflection coefficient - blue");

        addWithLabel(specularCoeffRed, "Specular reflection coefficient - red");
        addWithLabel(specularCoeffGreen, "Specular reflection coefficient - green");
        addWithLabel(specularCoeffBlue, "Specular reflection coefficient - blue");

        addWithLabel(ambienceCoeffRed, "Ambient reflection coefficient - red");
        addWithLabel(ambienceCoeffGreen, "Ambient reflection coefficient - green");
        addWithLabel(ambienceCoeffBlue, "Ambient reflection coefficient - blue");

        addWithLabel(selfIntensityRed, "Self intensity - red");
        addWithLabel(selfIntensityGreen, "Self intensity - green");
        addWithLabel(selfIntensityBlue, "Self intensity - blue");

        addWithLabel(glossiness, "Glossiness");

        addWithLabel(ambientIntensityRed, "Ambient intensity - red");
        addWithLabel(ambientIntensityGreen, "Ambient intensity - green");
        addWithLabel(ambientIntensityBlue, "Ambient intensity - blue");

        add(new JLabel("Global settings"));
        add(new JSeparator());

        addWithLabel(c0, "c0");
        addWithLabel(c1, "c1");
        addWithLabel(c2, "c2");

        addWithLabel(self, "Self luminescence");
        addWithLabel(diffuse, "Diffuse reflection");
        addWithLabel(specular, "Specular reflection");
        addWithLabel(ambience, "Ambient reflection");

        setDefaults();
        apply();

    }

    private void setDefaults(){
        lightSources.get(0).lightIntensityRed.setValue(255);
        lightSources.get(0).lightIntensityGreen.setValue(255);
        lightSources.get(0).lightIntensityBlue.setValue(255);

        lightSources.get(0).lightXPos.setText("2.");
        lightSources.get(0).lightYPos.setText("2.");
        lightSources.get(0).lightZPos.setText("-4.");

        diffuseCoeffRed.setText("1");
        diffuseCoeffGreen.setText("1");
        diffuseCoeffBlue.setText("1");

        specularCoeffRed.setText("0.1");
        specularCoeffGreen.setText("0.1");
        specularCoeffBlue.setText("0.1");

        ambienceCoeffRed.setText("1");
        ambienceCoeffGreen.setText("1");
        ambienceCoeffBlue.setText("1");

        selfIntensityRed.setValue(0);
        selfIntensityGreen.setValue(0);
        selfIntensityBlue.setValue(0);

        glossiness.setText("2.");

        ambientIntensityRed.setValue(55);
        ambientIntensityGreen.setValue(55);
        ambientIntensityBlue.setValue(55);

        c0.setText("0.1");
        c1.setText("0.1");
        c2.setText("0.1");

        self.setSelected(true);
        diffuse.setSelected(true);
        specular.setSelected(true);
        ambience.setSelected(true);

    }

    private void addWithLabel(JComponent component, String label){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel(label));
        panel.add(component);
        add(panel);
    }

    private void addWithLabel(JComponent parent, JComponent component, String label){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel(label));
        panel.add(component);
        parent.add(panel);
    }

    private JCheckBox newCheckBox(){
        JCheckBox cb = new JCheckBox();
        cb.getModel().addChangeListener(this);
        return cb;
    }

    private JTextField newTextField(){
        JTextField tf = new JTextField();
        tf.setText("0");
        tf.getDocument().addDocumentListener(this);
        return tf;
    }

    private ColorSpinner newColorSpinner(){
        ColorSpinner cs = new ColorSpinner();
        cs.addChangeListener(this);
        return cs;
    }

    private void apply() {

        int i = 0;
        for (LightSourcePanel lsPanel : lightSources){
            int[] magnitudes = new int[] {(int) lsPanel.lightIntensityRed.getValue(), (int) lsPanel.lightIntensityGreen.getValue(), (int) lsPanel.lightIntensityBlue.getValue()};
            sr.setLightIntensity(i, magnitudes);

            double x, y, z;
            x = parseDouble(lsPanel.lightXPos.getText());
            y = parseDouble(lsPanel.lightYPos.getText());
            z = parseDouble(lsPanel.lightZPos.getText());
            sr.setLightPosition(i, new Vector3(x, y, z));
            i++;
        }



        double coeffR, coeffG, coeffB;
        coeffR = parseDouble(diffuseCoeffRed.getText());
        coeffG = parseDouble(diffuseCoeffGreen.getText());
        coeffB = parseDouble(diffuseCoeffBlue.getText());
        sr.setDiffuseCoeffs(new double[] {coeffR, coeffG, coeffB});

        coeffR = parseDouble(specularCoeffRed.getText());
        coeffG = parseDouble(specularCoeffGreen.getText());
        coeffB = parseDouble(specularCoeffBlue.getText());
        sr.setSpecularCoeffs(new double[] {coeffR, coeffG, coeffB});

        coeffR = parseDouble(ambienceCoeffRed.getText());
        coeffG = parseDouble(ambienceCoeffGreen.getText());
        coeffB = parseDouble(ambienceCoeffBlue.getText());
        sr.setAmbientCoeffs(new double[] {coeffR, coeffG, coeffB});

        int[] magnitudes = new int[] {(int) selfIntensityRed.getValue(), (int) selfIntensityGreen.getValue(), (int) selfIntensityBlue.getValue()};
        sr.setSelfIntensity(magnitudes);

        double gloss = parseDouble(glossiness.getText());
        sr.setGlossiness(gloss);

        magnitudes = new int[] {(int) ambientIntensityRed.getValue(), (int) ambientIntensityGreen.getValue(), (int) ambientIntensityBlue.getValue()};
        sr.setAmbientMagnitudes(magnitudes);

        double c0v, c1v, c2v;
        c0v = parseDouble(c0.getText());
        c1v = parseDouble(c1.getText());
        c2v = parseDouble(c2.getText());
        sr.setC0(c0v);
        sr.setC1(c1v);
        sr.setC2(c2v);

        sr.setSelf(self.isSelected());
        sr.setDiffuse(diffuse.isSelected());
        sr.setSpecular(specular.isSelected());
        sr.setAmbience(ambience.isSelected());

        sr.render();
        parent.repaint();
    }

    private double parseDouble(String str){
        double value;
        try{
            value = Double.parseDouble(str);
            return value;
        } catch (NullPointerException | NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        apply();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        apply();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        apply();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        apply();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addLightSourceButton){
            lightSources.add(new LightSourcePanel());
            add(lightSources.get(lightSources.size()-1), 1 + lightSources.size());
        } else if (e.getSource() == removeLightSourceButton) {

            if (lightSources.size() > 0){
                remove(1 + lightSources.size());
                lightSources.remove(lightSources.size()-1);
                sr.removeLightSource();
            }

        }
        repaint();
        revalidate();
        apply();
    }

    static class ColorSpinner extends JSpinner{
        public ColorSpinner(){
            super(new SpinnerNumberModel(0,0,255,1));
            JComponent comp = getEditor();
            JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
            DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
            formatter.setCommitsOnValidEdit(true);
        }
    }

    class LightSourcePanel extends JPanel {
        JSpinner lightIntensityRed;
        JSpinner lightIntensityGreen;
        JSpinner lightIntensityBlue;
        JTextField lightXPos;
        JTextField lightYPos;
        JTextField lightZPos;

        public LightSourcePanel(){
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            lightIntensityRed = newColorSpinner();
            lightIntensityGreen = newColorSpinner();
            lightIntensityBlue = newColorSpinner();

            lightXPos = newTextField();
            lightYPos = newTextField();
            lightZPos = newTextField();

            addWithLabel(this, lightIntensityRed, "Light intensity - red");
            addWithLabel(this, lightIntensityGreen, "Light intensity - green");
            addWithLabel(this, lightIntensityBlue, "Light intensity - blue");

            addWithLabel(this, lightXPos, "Light position - x");
            addWithLabel(this, lightYPos, "Light position - y");
            addWithLabel(this, lightZPos, "Light position - z");

            add(new JSeparator());

            sr.addLightSource();
        }
    }

}
