/**
 *
 * @author Youstina & Ehab
 */
package paintbrush;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Rectangle implements Shape {
        private final Point start, end;
        private final Color color;
        private final boolean dotted;
        private final boolean filled;

        public Rectangle(Point start, Point end, Color color, boolean dotted, boolean filled) {
            this.start = start;
            this.end = end;
            this.color = color;
            this.dotted = dotted;
            this.filled = filled;
        }

        @Override
        public void draw(Graphics2D g) {
            g.setColor(color);
            if (dotted) {
                g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
            } else {
                g.setStroke(new BasicStroke());
            }
            if (filled) {
                g.fillRect(Math.min(start.x, end.x), Math.min(start.y, end.y),
                        Math.abs(start.x - end.x), Math.abs(start.y - end.y));
            } else {
                g.drawRect(Math.min(start.x, end.x), Math.min(start.y, end.y),
                        Math.abs(start.x - end.x), Math.abs(start.y - end.y));
            }
        }

        @Override
        public boolean contains(Point point) {
            return new java.awt.Rectangle(Math.min(start.x, end.x), Math.min(start.y, end.y),
                    Math.abs(start.x - end.x), Math.abs(start.y - end.y)).contains(point);
        }
    }
