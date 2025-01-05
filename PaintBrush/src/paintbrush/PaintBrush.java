/**
 *
 * @author Youstina & Ehab
 */
package paintbrush;
import javax.swing.JFrame;

public class PaintBrush {

    public static void main(String[] args) {
    
            JFrame f = new JFrame();  // Create a new JFrame (main window)
            f.setTitle("Paint Brush ");  // Set the title of the window to "Paint Brush"
            f.setContentPane(new Panel());  // Set the content of the window to a custom Panel (your drawing area)
            f.setSize(900, 650);  // Set the size of the window (width: 2000px, height: 2000px)
            f.setVisible(true);  // Make the window visible
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Close the program when the window is closed
            f.setExtendedState(JFrame.MAXIMIZED_BOTH); // Start the app in fullscreen mode.
        
    }
        
}
