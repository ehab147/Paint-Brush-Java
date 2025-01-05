/**
 *
 * @author Youstina & Ehab
 */
package paintbrush;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class FreeHand implements Shape {
        private final ArrayList<Point> points;
        private final Color color;
        private final boolean dotted;

        public FreeHand(ArrayList<Point> points, Color color, boolean dotted) {
            this.points = points;
            this.color = color;
            this.dotted = dotted;
        }

        @Override
        public void draw(Graphics2D g) {
            g.setColor(color);
            if (dotted) {
                g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
            } else {
                g.setStroke(new BasicStroke());
            }
            for (int i = 0; i < points.size() - 1; i++) {
                Point p1 = points.get(i);
                Point p2 = points.get(i + 1);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        @Override
        public boolean contains(Point point) {
            return false;
        }
    }
    

