import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;


/**
 * Separate the game code from some of the boilerplate code.
 * 
 * @author Robert C. Duvall
 */
class ExampleGame {
    public static final String TITLE = "Example JavaFX";
    public static final int KEY_INPUT_SPEED = 5; // Sets how far (in pixels?) the red block moves with each key press.
    private static final double GROWTH_RATE = 1.1; // Determines how much the beige block grows with each click.
    private static final int BOUNCER_SPEED = 10; // Changes the speed at which the rocket ship moves.

    private Scene startMenu;
    private Scene shopMenu;
    private Scene controls;
    private Scene myScene; // Declare all your different scenes here? Such as the start menu, in game menu, game over menu, different modes, etc.
    private ImageView myBouncer;
    private Rectangle myTopBlock;
    private Rectangle myBottomBlock;


    /**
     * Returns name of the game.
     */
    public String getTitle () {
        return TITLE;
    }
    
    /**
     * Creates start menu
     */
    public Scene initStart(int width, int height) {
    	Group root = new Group();
    	startMenu = new Scene(root, width, height, Color.BLACK);
    	Text t = new Text(50, 50, "Welcome To: The Example Game");
    	return startMenu;
    }

    /**
     * Create the game's scene
     */
    public Scene init (int width, int height) {
        // create a scene graph to organize the scene
        Group root = new Group();
        // create a place to see the shapes
        myScene = new Scene(root, width, height, Color.BLACK); // Sets the width, height, and background color of the game window.
        // make some shapes and set their properties
        Image image = new Image(getClass().getClassLoader().getResourceAsStream("Basketball.gif")); // Determines what gif file the image will be.
        myBouncer = new ImageView(image); // Displays myBouncer as the image. 
        // x and y represent the top left corner, so center it
        myBouncer.setX(width / 2 - myBouncer.getBoundsInLocal().getWidth() / 2); // Sets starting x position of myBouncer
        myBouncer.setY(height / 20  - myBouncer.getBoundsInLocal().getHeight() / 2); // Sets starting y position of myBouncer. Note that the origin, (0,0) is the top left corner of the window.
        myTopBlock = new Rectangle(width / 2 - 25, height / 2 - 100, 150, 50); // First two parameters set the starting x and y position of top block. Next two set the width and height of the block.
        myTopBlock.setFill(Color.YELLOW); // Sets initial color of the block.
        myBottomBlock = new Rectangle(width / 2 - 25, height / 2 + 50, 50, 50); // Same as line 49, but for bottom block.
        myBottomBlock.setFill(Color.BISQUE); // Same as line 50, but for bottom block.
        // order added to the group is the order in which they are drawn
        root.getChildren().add(myBouncer); // Actually draws myBouncer with the given parameters.
        root.getChildren().add(myTopBlock); // Actually draws top block with the given parameters.
        root.getChildren().add(myBottomBlock); // Actually draws the bottom block with the given parameters.Without this line, the bottom block would not appear.
        // respond to input
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode())); // When a key is pressed, call handleKeyInput method.
        myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY())); // When mouse is clicked, call handleMouseInput method.
        return myScene;
    }

    /**
     * Change properties of shapes to animate them
     * 
     * Note, there are more sophisticated ways to animate shapes,
     * but these simple ways work too.
     */
    public void step (double elapsedTime) {
        // update attributes a.k.a. the movement attributes.
        myBouncer.setX(myBouncer.getX() + BOUNCER_SPEED * elapsedTime); // Determines x-direction movement of myBouncer.
        myTopBlock.setRotate(myTopBlock.getRotate() + 10); // Determines rotation of top block. A positive value rotates clockwise. A negative value rotates counterclockwise. The larger the magnitude, the greater the magnitude of angular velocity of the rotation.
        myBottomBlock.setRotate(myBottomBlock.getRotate() + 1); // Same as line 72, but for bottom block.
        
        // check for collisions
        // with shapes, can check precisely
        Shape intersect = Shape.intersect(myTopBlock, myBottomBlock); // Generates a new shape when the two blocks intersect.
        if (intersect.getBoundsInLocal().getWidth() != -1) {
            myTopBlock.setFill(Color.GREEN); // Changes the color of top block to green when it intersects with other block.
        }
        else {
            myTopBlock.setFill(Color.YELLOW); // Sets default color of top block.
        }
        // with images can only check bounding box
        if (myBottomBlock.getBoundsInParent().intersects(myBouncer.getBoundsInParent())) {
            myBottomBlock.setFill(Color.BLUE); // Changes the color of bottom block if it intersects with rocket ship.
        }
        else {
            myBottomBlock.setFill(Color.BISQUE); // Sets default color of bottom block.
        }
    }


    // What to do each time a key is pressed -- Where all key inputs should be stored?
    private void handleKeyInput (KeyCode code) {
        switch (code) {
            case D:
                myTopBlock.setX(myTopBlock.getX() + KEY_INPUT_SPEED); // top block moves right
                break;
            case A:
                myTopBlock.setX(myTopBlock.getX() - KEY_INPUT_SPEED); // top block moves left
                break;
            case W:
                myTopBlock.setY(myTopBlock.getY() - KEY_INPUT_SPEED); // top block moves up
                break;
            case S:
                myTopBlock.setY(myTopBlock.getY() + KEY_INPUT_SPEED); // top block moves down
                break;
            default:
                // do nothing
        }
    }

    // What to do each time mouse is clicked.
    private void handleMouseInput (double x, double y) {
        if (myBottomBlock.contains(x, y)) {
            myBottomBlock.setScaleX(myBottomBlock.getScaleX() * GROWTH_RATE);
            myBottomBlock.setScaleY(myBottomBlock.getScaleY() * GROWTH_RATE);
        }
    }
}
