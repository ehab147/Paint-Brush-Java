/**
 *
 * @author Youstina & Ehab
 */
package paintbrush;

import java.awt.Graphics2D;
import java.awt.Point;

public interface Shape {
       void draw(Graphics2D g);
        boolean contains(Point point);
}
