Paint Brush Application
A Java-based drawing application that provides various tools for creating and editing digital artwork.
Features

    Drawing tools: Line, Rectangle, Oval, and Freehand pen
    Shape customization: Dotted lines and fill options
    Color selection
    Undo functionality
    Canvas clearing
    Eraser tool
    Save/Open functionality for drawings

Project Structure
Main Classes
PaintBrush.java

    Main application class that initializes the GUI window
    Sets up the application frame with proper dimensions and properties
    Creates the main drawing panel

Panel.java

    Core functionality class containing the drawing canvas and UI controls
    Manages drawing modes, tools, and user interactions
    Key methods:
        createButton(): Creates styled UI buttons with hover effects
        chooseColor(): Opens color picker dialog
        clearCanvas(): Resets the drawing area
        undo(): Removes the last drawn shape
        saveCanvas(): Saves the drawing as a PNG file
        openImage(): Loads an existing image into the canvas
        toggleErase(): Switches between drawing and erasing modes

Shape Classes
Shape.java (Interface)

    Defines the contract for all drawable shapes
    Methods:
        draw(): Renders the shape on the canvas
        contains(): Checks if a point is within the shape

Line.java

    Implements straight line drawing
    Supports dotted line style
    Properties: start point, end point, color, line style

Rectangle.java

    Implements rectangle drawing
    Supports both outlined and filled styles
    Properties: start point, end point, color, fill mode, line style

Oval.java

    Implements oval/circle drawing
    Supports both outlined and filled styles
    Properties: start point, end point, color, fill mode, line style

FreeHand.java

    Implements freehand drawing tool
    Stores points for smooth curve drawing
    Properties: point collection, color, line style

Features in Detail
Drawing Tools

    Line Tool: Creates straight lines between two points
    Rectangle Tool: Draws rectangles with adjustable dimensions
    Oval Tool: Creates circles and ovals
    Pen Tool: Enables freehand drawing
    Eraser: Removes parts of the drawing

Style Options

    Dotted Line: Toggle between solid and dotted lines
    Fill: Toggle between outlined and filled shapes
    Color Picker: Choose custom colors for drawing

Canvas Operations

    Undo: Remove the last drawn shape
    Clear: Reset the entire canvas
    Save: Export drawing as PNG file
    Open: Import existing images for editing

Technical Implementation

    Uses Java Swing for GUI components
    Implements double buffering for smooth drawing
    Utilizes Graphics2D for advanced drawing capabilities
    Maintains shape history for undo functionality
    Supports PNG image format for file operations
