import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        MainWindow wnd = new MainWindow();
        wnd.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        wnd.setVisible(true);
        wnd.setBounds( 70, 70, 1200, 600);
    }
}

class MainWindow extends JFrame
{

    JSplitPane outerPane;
    public MainWindow()
    {
        Container contents = getContentPane();
        outerPane = new OuterPane();
        contents.add( outerPane );
        pack();
    }
}