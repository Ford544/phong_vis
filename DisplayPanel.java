import javax.swing.*;
import java.awt.*;

public class DisplayPanel extends JPanel {

    SphereRenderer sr;

    public DisplayPanel(SphereRenderer sr){
        super();
        this.sr = sr;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(sr.getOutput(), 0, 0, null);
    }
}
