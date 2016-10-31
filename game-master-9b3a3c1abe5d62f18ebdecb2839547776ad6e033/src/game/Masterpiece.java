/**
 * This code's purpose is to set up the in game environment. It creates all the buttons and text that displays text explaining how to use each control,
 * it generates the random terrain, it generates and places the players in their random starting positions, and sets up all the objects that will be
 * used in the in game scene. I believe this is good design, especially in the context of how my mode/scene switching works in the rest of my code. 
 * I only use one scene and one root, so to switch in between different menus and modes, I clear the root, then repopulate it with all the correct variables.
 * By putting all the objects I need and all the initialization procedures I need into one class and the method, MakeGameEnvironment(), any time I want 
 * to switch to the in game environment, all I have to do is call MakeGameEnvironment() and I will have everything set up to play the game.
 * I understand that I have a lot of objects, but that is because my user interface and in game environment has a lot of unique objects that cannot
 * be generated with loops, so a large quantity of variables is required. The code is very readable, as it has clear variable names, clear method names,
 * clear comments, spatial organization to separate the initialization of different objects to the main game environment, and is also very easily testable.
 * Due to the separation of different objects into different blocks, I can easily change the in game element by changing that block of code. It is also quite 
 * flexible, in that all I have to do to add an in game object (button/text/player/projectile/etc) is add the object parameters to the MakeGameEnvironment() method. 
 */
package game;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Masterpiece {
	// CONSTANTS
    public static final String TITLE = "ARROW";
    public static final int KEY_INPUT_SPEED = 5; // Sets how far (in pixels?) the red block moves with each key press.
    public static final int XSIZE = 1920; // Number of X pixels in display.
    public static final double YSIZE = 0.6*XSIZE; // Number of Y pixels in display. 
	public static double GRAVITY = 0.3;
    
    // VARIABLES FOR RANDOM TERRAIN GENERATION, 50 DATA POINTS FOR 49 LINES. EACH LINE HAS AN X LENGTH OF X SCREEN SIZE DIVIDED BY 50.
	private int[] Terrain = new int[50];
    private List<Integer> TerrainList = new ArrayList<Integer>();
    private Line[] LineArray = new Line[50];
    private int Xincrement = XSIZE/50;
    private int randx1;
    private int randx2;
    
    // SETS WHICH PLAYER'S TURN IT IS.
    private int PlayerTurn = 1;

    // SCENE CONTROL --> ONE SCENE, BUT 4 DIFFERENT MODES. GAMEMODE 1 = START MENU. GAMEMODE 2 = IN GAME BATTLE. GAMEMODE 3 = HELP MENU. GAMEMODE 4 = SHOP MENU.
    private Group root = new Group();
    private Scene myScene; 

    // ESTABLISHING OBJECTS THAT WILL REPRESENT PLAYERS 1 AND 2 AND THEIR PROJECTILES.
    private Circle Player1;
    private Circle Player2;
	public Circle Arrow1 =new Circle();
	public Circle Arrow2 = new Circle();
    
	// SHOOTING PARAMETERS FOR EACH PLAYER: ANGLE, POWER, WEAPON TYPE.
	private int Degrees2;
    private int Weapon1;
    private int Weapon2;
	
    // INITIALIZES OBJECTS FOR IN GAME USER INTERFACE.
    private Rectangle Sidebar;
    private Rectangle AngleSelect;
    private Rectangle PowerSelect;
    private Rectangle WeaponSelect;
    private Rectangle MoveSelect;
    private Circle FireButton;
	private Text TurnDisplay = new Text(400, 50, "Turn: Player " + PlayerTurn);
	private Text P1AngleDisplay = new Text(XSIZE-60, 65, "");
	private Text P2AngleDisplay = new Text(XSIZE-60, 65, "" + Degrees2);
	private Text P1PowerDisplay = new Text(XSIZE-60, 170, "");
    private Text P2PowerDisplay = new Text(XSIZE-60, 170, "");
	private Text WeaponSelectNumber1;
	private Text WeaponSelectNumber2;
    
    // VARIABLES TO KEEP TRACK OF SCORE.
    private int P1Points = 0;
    private int P2Points = 0;
    private int P1Rounds = 0;
    private int P2Rounds = 0;
    private Text Score = new Text(0.6*XSIZE, 0.8*0.6*XSIZE, "Current Round: Player 1 (" + P1Points + ") Player 2 ("+ P2Points + ")");
	private Text Rounds;
    
	/**
     * Generates XY coordinate array of randomly generated terrain, where index is x value, and value of array at that index is corresponding y value.
     */
	  private int[] XYTerrain (int XSIZE, int YSize) {
	    	TerrainList.clear();
	    	Random r = new Random();
			int High = 0;
			int Low = XSIZE;
			int current = 0;
	    	for (int i = 0; i < 50; i ++) {
	    		current = (int) Math.abs((YSize - r.nextInt(Low - High))/1.7);
	    		Terrain[i] = current;
	    		TerrainList.add(current);
	    		High = current - ((int)(0.3*YSize));
	    		Low = current + ((int)(0.3*YSize));
	    	}
	    	return Terrain;
	    }
	
  /**
     * Draws the terrain based off the coordinates in the Terrain array.
     */
    private Line DrawScene(int XSIZE, int YSize, int i) {
		Line line = new Line();
		line.setFill(Color.RED);
		line.setStartX(Xincrement*i);
		line.setStartY(Terrain[i]);
		line.setEndX(Xincrement*(i+1));
		line.setEndY(Terrain[i+1]);
		return line;
    }
	  
    /**
     * Generates all the objects to be used in the in game environment, including the user interface.
     */
	 private void MakeGameEnvironment() {
 		int MaxY = (int) myScene.getHeight()-50;
	        int MaxX = (int) myScene.getWidth();
	        Terrain = XYTerrain(MaxX, MaxY);
	        for (int i = 0; i < 49; i ++) {
	        	Line line = DrawScene(MaxX,MaxY,i);
	        	LineArray[i] = line;
	        	root.getChildren().add(line);
	        }
	        
	        // Initializes the two players' random positions on the map.
	        Random r1 = new Random();
	        Random r2 = new Random();
	        randx1 = r1.nextInt(25);
	        randx2 = r2.nextInt(49-randx1) + randx1;
	        
	        double P1X = (double) Xincrement*(randx1);
	        double P1Y = (double) TerrainList.get(randx1)-10;
	        Player1 = new Circle(P1X, P1Y, 10, Color.BLUE);
	        root.getChildren().add(Player1);
	        
	        double P2X = (double) Xincrement*randx2;
	        double P2Y = (double) TerrainList.get(randx2)-10;
	        Player2 = new Circle(P2X, P2Y, 10, Color.GREEN);
	        root.getChildren().add(Player2);
	        
	        // Initializes the user side bar in the in-game screen, that contains the buttons for firing the weapon, selecting angle, weapon, and power levels.
	        Sidebar = new Rectangle(XSIZE-95, 0, 95, MaxY+50);
	        Sidebar.setFill(Color.BLACK);
	        root.getChildren().add(Sidebar);
	        
	        AngleSelect = new Rectangle(XSIZE-90, 20, 85, 65);
	        AngleSelect.setFill(Color.AQUA);
	        root.getChildren().add(AngleSelect);
	        
	        Text AngleText = new Text(XSIZE-85, 40, "ANGLE");
	        AngleText.setFill(Color.BLACK);
	        AngleText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	        root.getChildren().add(AngleText);
	        
	        PowerSelect = new Rectangle(XSIZE-90, 125, 85, 65);
	        PowerSelect.setFill(Color.AQUA);
	        root.getChildren().add(PowerSelect);
	        
	        Text PowerText = new Text(XSIZE-85, 145, "POWER");
	        PowerText.setFill(Color.BLACK);
	        PowerText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	        root.getChildren().add(PowerText);
	        
	        WeaponSelect = new Rectangle(XSIZE-90, 230, 85, 65);
	        WeaponSelect.setFill(Color.AQUA);
	        root.getChildren().add(WeaponSelect);
	        
	        Text WeaponText = new Text(XSIZE-88, 250, "WEAPON");
	        WeaponText.setFill(Color.BLACK);
	        WeaponText.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
	        root.getChildren().add(WeaponText);
	        
	        MoveSelect = new Rectangle(XSIZE-90, 335, 85, 65);
	        MoveSelect.setFill(Color.AQUA);
	        root.getChildren().add(MoveSelect);
	        
	        Text MoveText = new Text(XSIZE-80, 355, "MOVE");
	        MoveText.setFill(Color.BLACK);
	        MoveText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	        root.getChildren().add(MoveText);
	        
	        FireButton = new Circle(MaxX * 0.975, MaxY * 0.90, 30, Color.RED);
	    	root.getChildren().add(FireButton);
	        
	        Text FireText = new Text(XSIZE-72, 1000, "FIRE");
	        FireText.setFill(Color.BLACK);
	        FireText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	        root.getChildren().add(FireText);
	        
	        TurnDisplay.setFill(Color.BLACK);
	        TurnDisplay.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
	        root.getChildren().add(TurnDisplay);
	        
	        // In game text to help the user figure out how to use the controls.
	        Text HelpFire = new Text(1200, 88, "Press the Red Button When Ready To Fire");
	        HelpFire.setFill(Color.BLACK);
	        HelpFire.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	    	root.getChildren().add(HelpFire);
	    	
	        Text WeaponKey = new Text(XSIZE*0.35, YSIZE*0.75, "0: Regular, 1: Double, 2: QC");
	    	WeaponKey.setFill(Color.BLACK);
	    	WeaponKey.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	    	root.getChildren().add(WeaponKey);
	        
	        Text HelpMove = new Text(20, 0.8*YSIZE, "Press Left Or Right To Move");
	    	HelpMove.setFill(Color.BLACK);
	        HelpMove.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	    	root.getChildren().add(HelpMove);
	    	
	    	Text HelpAngle = new Text(20, 0.84*YSIZE, "Enter an angle between 0 and 360 Degrees");
	    	HelpAngle.setFill(Color.BLACK);
	    	HelpAngle.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	    	root.getChildren().add(HelpAngle);
	    	
	    	Text HelpPower = new Text(20, 0.88*YSIZE, "Enter a power between 0 and 100");
	    	HelpPower.setFill(Color.BLACK);
	    	HelpPower.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	    	root.getChildren().add(HelpPower);
	    	
	    	Text HelpWeapon = new Text(20, 0.92*YSIZE, "Click the weapon you wish to use. Click red button to fire.");
	    	HelpWeapon.setFill(Color.BLACK);
	    	HelpWeapon.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	    	root.getChildren().add(HelpWeapon);
	    	
	    	// Displays each player's chosen weapon.
	    	WeaponSelectNumber1 = new Text(XSIZE-60, 280, "" + Weapon1);
	    	WeaponSelectNumber1.setFill(Color.BLACK);
	    	WeaponSelectNumber1.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
			root.getChildren().add(WeaponSelectNumber1);
	    	
	    	WeaponSelectNumber2 = new Text(XSIZE-60, 280, "" + Weapon2);
	    	WeaponSelectNumber2.setFill(Color.BLACK);
	    	WeaponSelectNumber2.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
			root.getChildren().add(WeaponSelectNumber2);
	    	
	    	// Displays  the score for each round and the total number of rounds each player has won.
	    	Score.setFill(Color.GREEN);
	    	Score.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	    	root.getChildren().add(Score);
	    	
	    	Rounds = new Text(0.6*XSIZE, 0.9*0.6*XSIZE, "Rounds Won: Player 1 (" + P1Rounds + ") Player 2 (" + P2Rounds + ")");
	    	Rounds.setFill(Color.GREEN);
	    	Rounds.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	    	root.getChildren().add(Rounds);
	    	
	    	// Displays the values of the angle, power, and weapon that each player has chosen. Arrow1 and Arrow2 are the projectiles for each player.
	    	Arrow1 = new Circle(P1X, P1Y, 3, Color.RED);
	    	root.getChildren().add(Arrow1);
	 
	    	Arrow2 = new Circle(P2X, P2Y, 3, Color.RED);
	    	root.getChildren().add(Arrow2);
	    	
	    	P1AngleDisplay.setFill(Color.BLACK);
	        P1AngleDisplay.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
	    	root.getChildren().add(P1AngleDisplay);
	    	
	    	P2AngleDisplay.setFill(Color.BLACK);
	        P2AngleDisplay.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
	        root.getChildren().add(P2AngleDisplay);
	        
	        P1PowerDisplay.setFill(Color.BLACK);
	        P1PowerDisplay.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
			root.getChildren().add(P1PowerDisplay);
			
	        P2PowerDisplay.setFill(Color.BLACK);
	        P2PowerDisplay.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
			root.getChildren().add(P2PowerDisplay);
			}
}
