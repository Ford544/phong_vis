import javax.swing.*;
import java.awt.*;

public class OuterPane extends JSplitPane {

    SphereRenderer sr;

    JPanel settingsPanel;
    JPanel displayPanel;

    public OuterPane(){
        super();
        sr = new SphereRenderer();
        settingsPanel = new SettingsPanel(sr, this);
        setLeftComponent(new JScrollPane(settingsPanel));
        displayPanel = new DisplayPanel(sr);
        setRightComponent(displayPanel);
        setResizeWeight(0.2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        rightComponent.repaint();
    }
}
