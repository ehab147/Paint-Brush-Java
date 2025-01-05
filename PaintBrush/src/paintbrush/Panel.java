/**
 *
 * @author Youstina & Ehab
 */
package paintbrush;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;
import javax.swing.JButton;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Panel extends JPanel {
    private boolean drawing;
    private boolean dotted;
    private boolean filled;
    private boolean erasing;
    private Color currentColor;
    private DrawingMode currentMode;
    private Stack<Shape> undoStack;
    private Point startPoint;
    private Point endPoint;
    private BufferedImage canvasImage;
    private Graphics2D canvasGraphics;

    private enum DrawingMode {
        LINE, RECTANGLE, OVAL, PEN, NONE
    }

    public Panel() {
        canvasImage = new BufferedImage(1600, 900, BufferedImage.TYPE_INT_ARGB);
        canvasGraphics = canvasImage.createGraphics();
        canvasGraphics.setColor(Color.WHITE);
        canvasGraphics.fillRect(0, 0, 1600, 900);
        currentColor = Color.BLACK;
        currentMode = DrawingMode.LINE;
        undoStack = new Stack<>();
        dotted = false;
        filled = false;
        erasing = false;
        

        // Grid layout for buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 0, 10, 0));
        panel.setBackground(Color.lightGray);
        add(panel, BorderLayout.AFTER_LAST_LINE);
        
         // Custom drawing surfac
        DrawingArea canvas = new DrawingArea();
        canvas.setPreferredSize(new Dimension(1600, 900));
        add(canvas, BorderLayout.CENTER);

        panel.add(createButton("Pen", e -> setCurrentMode(DrawingMode.PEN)));
        panel.add(createButton("Line", e -> setCurrentMode(DrawingMode.LINE)));
        panel.add(createButton("Rectangle", e -> setCurrentMode(DrawingMode.RECTANGLE)));
        panel.add(createButton("Oval", e -> setCurrentMode(DrawingMode.OVAL)));

        JCheckBox dottedCheckBox = new JCheckBox("Dotted");
        dottedCheckBox.setBackground(Color.lightGray);
        dottedCheckBox.setForeground(Color.WHITE);
        dottedCheckBox.addActionListener(e -> dotted = dottedCheckBox.isSelected());
        panel.add(dottedCheckBox);

        JCheckBox filledCheckBox = new JCheckBox("Fill");
        filledCheckBox.setBackground(Color.lightGray);
        filledCheckBox.setForeground(Color.WHITE);
        filledCheckBox.addActionListener(e -> filled = filledCheckBox.isSelected());
        panel.add(filledCheckBox);

        panel.add(createButton("Color", e -> chooseColor()));
        panel.add(createButton("Undo", e -> undo()));
        panel.add(createButton("Clear", e -> clearCanvas())); 
        panel.add(createButton("Erase", e -> toggleErase()));  // Erase button
        panel.add(createButton("Save", e -> saveCanvas()));
        panel.add(createButton("Open", e -> openImage()));  // Open image button
        
    }

    private class DrawingArea extends JPanel {
        private ArrayList<Point> freeHandPoints = new ArrayList<>();

        public DrawingArea() {
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                    drawing = true;
                    startPoint = e.getPoint();
                    freeHandPoints.clear();
                    freeHandPoints.add(startPoint);
                    
                    if (erasing) {
                        // Draw directly to canvas image when erasing
                        canvasGraphics.setColor(Color.WHITE);
                        canvasGraphics.setStroke(new BasicStroke(10));
                        canvasGraphics.fillOval(e.getX() - 5, e.getY() - 5, 10, 10);
                    } 
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                    if (!erasing) {
                        endPoint = e.getPoint();
                        if (currentMode != DrawingMode.PEN) {
                            Shape shape = createShape(startPoint, endPoint);
                            if (shape != null) {
                                // Draw the shape to the canvas image
                                shape.draw(canvasGraphics);
                                undoStack.push(shape);
                            }
                        } else {
                            FreeHand freeHand = new FreeHand(new ArrayList<>(freeHandPoints), currentColor, dotted);
                            freeHand.draw(canvasGraphics);
                            undoStack.push(freeHand);
                        }
                    }
                    drawing = false;
                    repaint();
                }
            
        });

        addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (erasing) {
                        // Draw directly to canvas image when erasing
                        canvasGraphics.setColor(Color.WHITE);
                        canvasGraphics.setStroke(new BasicStroke(10));
                        Point last = freeHandPoints.get(freeHandPoints.size() - 1);
                        canvasGraphics.drawLine(last.x, last.y, e.getX(), e.getY());
                        freeHandPoints.add(e.getPoint());
                    } else if (currentMode == DrawingMode.PEN) {
                        freeHandPoints.add(e.getPoint());
                    } else {
                        endPoint = e.getPoint();
                    }
                    repaint();
                }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.drawImage(canvasImage, 0, 0, null);
        // Draw the current shape preview if drawing
            if (drawing && !erasing) {
                if (currentMode != DrawingMode.PEN) {
                    Shape previewShape = createShape(startPoint, endPoint);
                    if (previewShape != null) {
                        previewShape.draw(g2d);
                    }
                } else {
                    new FreeHand(freeHandPoints, currentColor, dotted).draw(g2d);
                }
            }
        
    }
    }

    private Shape createShape(Point start, Point end) {
        switch (currentMode) {
            case LINE:
                return new Line(start, end, currentColor, dotted, filled);
            case RECTANGLE:
                return new Rectangle(start, end, currentColor, dotted, filled);
            case OVAL:
                return new Oval(start, end, currentColor, dotted, filled);
            default:
                return null;
        }
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Verdana", Font.PLAIN, 14));
        button.setBackground(Color.lightGray);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(120, 40));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.lightGray);
            }
        });

        button.addActionListener(listener);
        return button;
    }

    private void chooseColor() {
        currentColor = JColorChooser.showDialog(this, "Choose a Color", currentColor);
    }

    private void clearCanvas() {
        undoStack.clear();
        // Clear the canvas image by filling it with white
        canvasGraphics.setColor(Color.WHITE);
        canvasGraphics.fillRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
        repaint();
    }

    private void undo() {
        if (!undoStack.isEmpty()) {
            undoStack.pop();
            // Redraw everything from scratch
            canvasGraphics.setColor(Color.WHITE);
            canvasGraphics.fillRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
            
            // Redraw all shapes in the stack
            for (Shape shape : undoStack) {
                shape.draw(canvasGraphics);
            }
            repaint();
        }
    }

    private void setCurrentMode(DrawingMode mode) {
        currentMode = mode;
    }

    private void toggleErase() {
    erasing = !erasing;
    if (erasing) {
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Use a custom square cursor
    } else {
        setCursor(Cursor.getDefaultCursor());
    }
}

    private void saveCanvas() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Image");
            fileChooser.setSelectedFile(new File("drawing.png"));
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                // Save only the canvas image
                ImageIO.write(canvasImage, "PNG", file);
                JOptionPane.showMessageDialog(this, "Image saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open Image");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                BufferedImage loadedImage = ImageIO.read(file);
                
                // Draw the loaded image onto our canvas
                canvasGraphics.drawImage(loadedImage, 0, 0, null);
                repaint();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
      
    
}
