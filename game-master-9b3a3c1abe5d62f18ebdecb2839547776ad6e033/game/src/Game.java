import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;


/**
 * Separate the game code from some of the boilerplate code.
 * 
 * @author Christopher Lu
 */
class Game {
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
    private int gameMode = 1;

    // ESTABLISHING OBJECTS THAT WILL REPRESENT PLAYERS 1 AND 2 AND THEIR PROJECTILES.
    private Circle Player1;
    private Circle Player2;
	public Circle Arrow1 =new Circle();
	public Circle Arrow2 = new Circle();
    
	// SHOOTING PARAMETERS FOR EACH PLAYER: ANGLE, POWER, WEAPON TYPE.
	private int Degrees1;
	private int Degrees2;
	private int Velocity1;
	private int Velocity2;
	private double vx1;
	private double vy1;
	private double vx2;
	private double vy2;
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
    
    // BOOLEANS TO ENSURE THE PLAYERS CAN ONLY DO A CERTAIN NUMBER OF ACTIONS IN ONE TURN, CHECKS WHETHER OR NOT THEY HAVE WEAPON OF CHOICE.
    private boolean Firebool1 = false;
    private boolean Firebool2 = false;
    private boolean Movebool1 = true;
    private boolean Movebool2 = true;
    private boolean DoubleShot1 = false;
    private boolean DoubleShot2 = false;
    private boolean Tankbool1 = false;
    private boolean Tankbool2 = false;
    private boolean QCbool1 = false;
    private boolean QCbool2 = false;
    
    // VARIABLES TO KEEP TRACK OF SCORE.
    private int P1Points = 0;
    private int P2Points = 0;
    private int P1Rounds = 0;
    private int P2Rounds = 0;
    private Text Score = new Text(0.6*XSIZE, 0.8*0.6*XSIZE, "Current Round: Player 1 (" + P1Points + ") Player 2 ("+ P2Points + ")");
	private Text Rounds;
	private boolean hitP1 = false;
	private boolean hitP2 = false;
	private String PlayerLose;
	
	// ONJECTS FOR TRANSITION BETWEEN DIFFERENT MENUS.
    private Rectangle StartButton;
    private Rectangle HelpButton;
    
    // INITIALIZATION OF POWER UP TEXTS.
    private Text Double;
	private Text QuickCement;
	private Text Tank;
	private Text Loser;

    /**
     * Returns name of the game.
     */
    public String getTitle () {
        return TITLE;
    }
    
    /**
     * Generates XY coordinate array of randomly generated terrain, where index is x value, and value of array at that index is corresponding y value.
     */
    
    public int[] XYTerrain (int XSIZE, int YSize) {
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
    public Line DrawScene(int XSIZE, int YSize, int i) {
		Line line = new Line();
		line.setFill(Color.RED);
		line.setStartX(Xincrement*i);
		line.setStartY(Terrain[i]);
		line.setEndX(Xincrement*(i+1));
		line.setEndY(Terrain[i+1]);
		return line;
    }
    
    /**
     * Changes player turn display in in game screen.
     */
    public void TurnSwitchDisplay(int Turn) {
    	root.getChildren().remove(TurnDisplay);
		TurnDisplay = new Text(400, 50, "Turn: Player " + Turn);
		PlayerTurn = Turn;
		TurnDisplay.setFill(Color.BLACK);
        TurnDisplay.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		root.getChildren().add(TurnDisplay);
    }
    
    /**
     * Alternates Player turns. This method should be called at the end of every turn.
     * @param PlayerTurn
     * @return
     */
    public void TurnSwitch() {
    	if (PlayerTurn == 1) {
    		TurnSwitchDisplay(2);
    	}
    	else {
    		TurnSwitchDisplay(1);
    	}
    }
    
    private void BooleanReset() {
    	Firebool1 = false;
	    Firebool2 = false;
	    Movebool1 = true;
	    Movebool2 = true;
	    DoubleShot1 = false;
	    DoubleShot2 = false;
	    Tankbool1 = false;
	    Tankbool2 = false;
	    QCbool1 = false;
	    QCbool2 = false;
	    hitP1 = false;
		hitP2 = false;
    }
    
    /**
     * Creates the main menu, with start and help buttons as start and help rectangles with text overlaid.
     *  The decimals are numbers that determine the position of the buttons and text.
     */
    private void MakeMainMenu() {
    	 	StartButton = new Rectangle(XSIZE*0.3, YSIZE*0.3, XSIZE*0.4, YSIZE*0.1);
	        StartButton.setFill(Color.WHITE);
	        root.getChildren().add(StartButton);
	        
	        HelpButton = new Rectangle(XSIZE*0.3, YSIZE*0.5, XSIZE*0.4, YSIZE*0.1);
	        HelpButton.setFill(Color.WHITE);
	        root.getChildren().add(HelpButton);
	        
	        Text Start = new Text(XSIZE*0.48, YSIZE*0.35, "START");
	        Start.setFill(Color.BLACK);
	        Start.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
	        root.getChildren().add(Start);
	        
	        Text Help = new Text(XSIZE*0.48, YSIZE*0.55, "HELP");
	        Help.setFill(Color.BLACK);
	        Help.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
	        root.getChildren().add(Help);
	        
	        Text Title = new Text(XSIZE*0.42, YSIZE*0.2, "PARABOLA");
	        Title.setFill(Color.BLACK);    
	        Title.setFont(Font.font("Verdana", FontWeight.BOLD, 52));
	        root.getChildren().add(Title);
	        
	        BooleanReset();
    }
    
    /**
     * Creates objects and interface for in game environment.
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
    
    /**
     * Creates objects and texts for the help menu.
     */
    private void MakeHelpMenu() {
    	Rectangle ReturnToMainMenu = new Rectangle(0.9*XSIZE, 0.95*YSIZE*0.6, 192, 50);
		ReturnToMainMenu.setFill(Color.BLACK);
		root.getChildren().add(ReturnToMainMenu);
    	
		Text Synopsis = new Text(0, 0.1*XSIZE, "The year is 3033. Earth is gone, but humanity has learned to thrive on spaceships. A giant planet with alien life, CS308, has been discovered, but mankind's conflict over ownership of this planet has led to war.");
		Synopsis.setFill(Color.BLACK);
		Synopsis.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		root.getChildren().add(Synopsis);
		
		Text WinCondition = new Text(0, 0.2*XSIZE, "Be the first player to win 3 rounds. The first player to hit the opponent a total of 5 times wins the round.");
		WinCondition.setFill(Color.BLACK);
		WinCondition.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		root.getChildren().add(WinCondition);
		
		Text ShopDescription = new Text(0, 0.3*XSIZE, "The loser of the previous round will choose a special weapon that can be used once in each of the following rounds. A weapon taken by one player is unavailable to the other player.");
		ShopDescription.setFill(Color.BLACK);
		ShopDescription.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		root.getChildren().add(ShopDescription);
		
		Text ReturnMenu = new Text(0.91*XSIZE, 0.36*XSIZE, "Return To Menu");
		ReturnMenu.setFill(Color.RED);
		ReturnMenu.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		root.getChildren().add(ReturnMenu);
    }
    
    /**
     * Creates objects and texts for in game shop menu.
     */
    private void MakeShopMenu() {
    	// The text that is clicked on to select a special weapon.
    	root.getChildren().clear();
    	Double = new Text(XSIZE*0.1, YSIZE*0.4, "Double Shot");
       	Double.setFill(Color.BLACK);
    	Double.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
    	root.getChildren().add(Double);
    	
    	QuickCement = new Text(XSIZE*0.4, YSIZE*0.4, "QUICK CEMENT");
    	QuickCement.setFill(Color.BLACK);
    	QuickCement.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
    	root.getChildren().add(QuickCement);
    	
    	Tank = new Text(XSIZE*0.85, YSIZE*0.4, "6-TANK");
    	Tank.setFill(Color.BLACK);
    	Tank.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
    	root.getChildren().add(Tank);
    	
    	// The text that describes each special weapon.
    	Text DoubleDescription = new Text(XSIZE*0.02, YSIZE*0.5, "This shot is worth 2 points if it hits.");
    	DoubleDescription.setFill(Color.BLACK);
    	DoubleDescription.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
    	root.getChildren().add(DoubleDescription);
    	
    	Text QCDescription = new Text(XSIZE*0.3, YSIZE*0.5, "If hit, the player cannot move unless other player does.");
    	QCDescription.setFill(Color.BLACK);
    	QCDescription.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
    	root.getChildren().add(QCDescription);
    	
    	Text TankDescription = new Text(XSIZE*0.75, YSIZE*0.5, "Opponent must hit you 6 times to win round.");
    	TankDescription.setFill(Color.BLACK);
    	TankDescription.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
    	root.getChildren().add(TankDescription);

    	// Displays the loser, as only the loser of the previous round can buy.
    	Loser = new Text(XSIZE*0.4, YSIZE*0.1, PlayerLose + "Shop");
    	Loser.setFill(Color.BLACK);
    	Loser.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
    	root.getChildren().add(Loser);
    	
    	// Deletes the special weapons if they have been selected already.
    	if (DoubleShot1 || DoubleShot2 == true) {
    		root.getChildren().remove(Double);
    		root.getChildren().remove(DoubleDescription);
    	}
    	if (QCbool1 || QCbool2 == true) {
    		root.getChildren().remove(QuickCement);
    		root.getChildren().remove(QCDescription);
    	}
    	if (Tankbool1 || Tankbool2 == true) {
    		root.getChildren().remove(Tank);
    		root.getChildren().remove(TankDescription);
    	}
    }
    
    /**
     * Displays the player who wins the game. If screen is clicked, user is taken back to main menu.
     * @param Player
     */
    private void MakeVictorScreen(Circle Player) {
    	Text Victor = new Text();
    	if (Player.equals(Player1)) {
    		Victor = new Text(XSIZE*0.4, XSIZE*0.6*0.4, "PLAYER 1 WINS!");
    		Victor.setFill(Color.GREEN);
    		Victor.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
    	}
    	if (Player.equals(Player2)) {
    		Victor = new Text(XSIZE*0.4, XSIZE*0.6*0.4, "PLAYER 2 WINS!");
    		Victor.setFill(Color.GREEN);
    		Victor.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
    	}
		root.getChildren().add(Victor);
		P1Rounds = 0;
		P2Rounds = 0;
    	Text ReturnMain = new Text(XSIZE*0.4, XSIZE*0.6*0.9, "Return To Main Menu");
    	ReturnMain.setFill(Color.BLACK);
    	ReturnMain.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		root.getChildren().add(ReturnMain);
		myScene.setOnMouseClicked(e -> mouseReturnMain(e.getX(), e.getY()));
    }
    
    /**
     * Create the game's start mode. Delegates the control to and from different modes to handleMouseScenes.
     */
    public Scene init (int width, int height) {
        // create a scene graph to organize the scene
        myScene = new Scene(root, width, height, Color.WHITE);
        if (gameMode == 1) {
	        MakeMainMenu();
        }
        myScene.setOnMouseClicked(e -> handleMouseScenes(e.getX(), e.getY()));
        return myScene;
    }   
    
    /**
     * Handles a majority of the mouse clicking functions in the game.
     */
    public void handleMouseScenes(double x, double y){
    	// When start button in main menu is clicked, go to the in game mode.
    	if (StartButton.contains(x,y)) {
    		gameMode = 2;
    		root.getChildren().clear();
    		MakeGameEnvironment();
 	        myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY())); 
    	}
    	// When help button in main menu is clicked, go to the help screen.
    	if (HelpButton.contains(x,y)) {
    		gameMode = 3;
    		root.getChildren().clear();
    		MakeHelpMenu();
    		myScene.setOnMouseClicked(e -> mouseReturnMain(e.getX(), e.getY())); 
    	}
    	// When player 1 clicks the special weapon DoubleShot in shop menu, set Player 1's Double Shot status to true, return to in game environment.
    	if (Double.contains(x,y) && PlayerLose.equals("Player 1 ")) {
    		DoubleShot1 = true;
    		gameMode = 2;
    		root.getChildren().clear();
    		MakeGameEnvironment();
 	        myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY())); 
    	}
    	// When player 2 clicks the special weapon DoubleShot in shop menu, set Player 2's Double Shot status to true, return to in game environment.
    	if (Double.contains(x,y) && PlayerLose.equals("Player 2 ")) {
    		DoubleShot2 = true;
    		gameMode = 2;
    		root.getChildren().clear();
    		MakeGameEnvironment();
 	        myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
    	}
    	// When player 1 clicks the special weapon Quick Cement in shop menu, set Player 1's Quick Cement status to true, return to in game environment.
    	if (QuickCement.contains(x,y) && PlayerLose.equals("Player 1 ")) {
    		QCbool1 = true;
    		Movebool1 = true;
    		gameMode = 2;
    		root.getChildren().clear();
    		MakeGameEnvironment();
 	        myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY())); 
    	}
    	// When player 2 clicks the special weapon Quick Cement in shop menu, set Player 2's Quick Cement status to true, return to in game environment.
    	if (QuickCement.contains(x,y) && PlayerLose.equals("Player 2 ")) {
    		QCbool2 = true;
    		Movebool2 = true;
    		gameMode = 2;
    		root.getChildren().clear();
    		MakeGameEnvironment();
 	        myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
    	}
    	// When player 1 clicks the special power up Tank in shop menu, set Player 1's Tank status to true, return to in game environment.
    	if (Tank.contains(x,y) && PlayerLose.equals("Player 1 ")) {
    		Tankbool1 = true;
    		gameMode = 2;
    		root.getChildren().clear();
    		MakeGameEnvironment();
 	        myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
    	}
    	// When player 2 clicks the special power up Tank in shop menu, set Player 2's Tank status to true, return to in game environment.
    	if (Tank.contains(x,y) && PlayerLose.equals("Player 2 ")) {
    		Tankbool2 = true;
    		gameMode = 2;
    		root.getChildren().clear();
    		MakeGameEnvironment();
 	        myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
    	}
    }
    
    /** When this function is called, the game returns to main menu when the mouse is clicked.
     * @param x
     * @param y
     */
    private void mouseReturnMain(double x, double y) {
    	gameMode = 1;
    	root.getChildren().clear();
    	MakeMainMenu();
    	P1Rounds = 0;
    	P2Rounds = 0;
        myScene.setOnMouseClicked(e -> handleMouseScenes(e.getX(), e.getY()));
    }
    
    /**
     * Resets the round.
     */
    
    public void ResetRound() {
    	Rounds.setText("");
		Rounds.setText("Rounds Won: Player 1 (" + P1Rounds + ") Player 2 (" + P2Rounds + ")");
		Score.setText("");
		P1Points = 0;
		P2Points = 0;
		Score.setText("Current Round: Player 1 (" + P1Points + ") Player 2 ("+ P2Points + ")");
    }
    
    /**
     * This method handles the motion of the projectile when fired.
     */
    
    public void step (double elapsedTime) {
    	if (gameMode == 2) {
    		// Determines what happens if player 1's shot hits player 2.
    		if (P1Rounds >= 2) {
	    		root.getChildren().clear();
	    		MakeVictorScreen(Player1);
	    	}
	    	else if (P2Rounds >= 2) {
	    		root.getChildren().clear();
	    		MakeVictorScreen(Player2);
	    	}
	    	Shape intersect1 = Shape.intersect(Arrow1,  Player2);
	    	if (Firebool1 == true) {
	    		for (double time = 0.0; time < 1; time += elapsedTime) {
	    			Arrow1.setCenterX(Arrow1.getCenterX() + vx1*elapsedTime);
	    			Arrow1.setCenterY(Arrow1.getCenterY() + vy1*elapsedTime);
	    			vy1 += GRAVITY*elapsedTime;
	    			time += elapsedTime;
	    		}
	    		if (intersect1.getBoundsInLocal().getWidth() != -1 && hitP1 == false) {
	    			Player2.setFill(Color.RED);
	    			if (Weapon1 == 0) {
	    				P1Points++;
	    			}
	    			if (Weapon1 == 1) {
	    				P1Points+=2;
	    			}
	    			if (Weapon1 == 2) {
	    				Movebool2 = false;
	    			}
	    			hitP1 = true;
	    			Score.setText("");
	    			Score.setText("Current Round: Player 1 (" + P1Points + ") Player 2 ("+ P2Points + ")");
	    		}
	    		else {
	    			P1Points += 0;
	    			Player2.setFill(Color.GREEN);
	    		}
	    	}
	    	// Determines what happens if player2's shot hits player 1.
			Shape intersect2 = Shape.intersect(Arrow2, Player1);
	    	if (Firebool2 == true) {
	    		for (double time = 0.0; time < 1; time += elapsedTime) {
	    			Arrow2.setCenterX(Arrow2.getCenterX() + vx2*elapsedTime);
	    			Arrow2.setCenterY(Arrow2.getCenterY() + vy2*elapsedTime);
	    			vy2 += GRAVITY*elapsedTime;
	    			time += elapsedTime;
	    		}
	    		if (intersect2.getBoundsInLocal().getWidth() != -1 && hitP2 == false) {
	    			Player1.setFill(Color.RED);
	    			if (Weapon2 == 0) {
	    				P2Points++;
	    			}
	    			if (Weapon2 == 1) {
	    				P2Points+=2;
	    			}
	    			if (Weapon2 == 2) {
	    				Movebool1 = false;
	    			}
	    			hitP2 = true;
	    			Score.setText("");
	    			Score.setText("Current Round: Player 1 (" + P1Points + ") Player 2 ("+ P2Points + ")");
	    		}
	    		else {
	    			P2Points += 0;
	    			Player1.setFill(Color.BLUE);
	    		}
	    	}
	    	// Determines when to end the round --> Either with 5 points or 6 points if one of the players has the tank power up.
	    	if (Tankbool1 == true) {
	    		if (P2Points >= 6) {
		    		P2Rounds++;
		    		PlayerLose = "Player 1 ";	    		
		    		ResetRound();
		    		MakeShopMenu();
		    		myScene.setOnMouseClicked(e -> handleMouseScenes(e.getX(), e.getY()));
	    		}
		    	else if (P1Points >= 5) {
		    		P1Rounds++;
		    		PlayerLose = "Player 2 ";
		    		ResetRound();
		    		MakeShopMenu();
		    		myScene.setOnMouseClicked(e -> handleMouseScenes(e.getX(), e.getY()));
		    	}
	    	}
	    	else if (Tankbool2 == true) {
	    		if (P1Points >= 6) {
		    		P1Rounds++;
		    		PlayerLose = "Player 2 ";
		    		ResetRound();
		    		MakeShopMenu();
		    		myScene.setOnMouseClicked(e -> handleMouseScenes(e.getX(), e.getY()));
	    		}
		    	else if (P2Points >= 5) {
		    		P2Rounds++;
		    		PlayerLose = "Player 1 ";
		    		ResetRound();
		    		MakeShopMenu();
		    		myScene.setOnMouseClicked(e -> handleMouseScenes(e.getX(), e.getY()));
		    	}
	    	}
	    	else if (P1Points >= 5) {
	    		P1Rounds++;
	    		PlayerLose = "Player 2 ";
	    		ResetRound();
	    		MakeShopMenu();
	    		myScene.setOnMouseClicked(e -> handleMouseScenes(e.getX(), e.getY()));
	    		}
	    	else if (P2Points >= 5) {
	    		P2Rounds++;
	    		PlayerLose = "Player 1 ";
	    		ResetRound();
	    		MakeShopMenu();
	    		myScene.setOnMouseClicked(e -> handleMouseScenes(e.getX(), e.getY()));
	    	}
    	}
    }

    /**
     * Movement mechanics 
     */
    public void MoveRight() {
    	if (PlayerTurn == 1) {
        	int IndexNewX1 = 0;
        	IndexNewX1 = randx1 + 1;
        	randx1 = IndexNewX1;
       		Player1.setCenterX((double) ((IndexNewX1)*Xincrement));
       		Player1.setCenterY(TerrainList.get(IndexNewX1));
       		Firebool1 = false;
       		return;
    	}
    	if (PlayerTurn == 2) {
    		int IndexNewX2 = 0;
    		IndexNewX2 = randx2 + 1;
        	randx2 = IndexNewX2;
        	Player2.setCenterX((double) ((IndexNewX2)*Xincrement));
       		Player2.setCenterY(TerrainList.get(IndexNewX2));
       		Firebool2 = false;
       		return;
    	}
    }
    
    public void MoveLeft() {
    	if (PlayerTurn == 1) {
        	int IndexNewX1 = 0;
        	IndexNewX1 = randx1 - 1;
        	randx1 = IndexNewX1;
       		Player1.setCenterX((double) ((IndexNewX1)*Xincrement));
       		Player1.setCenterY(TerrainList.get(IndexNewX1));
       		Firebool1 = false;
       		return;
    	}
    	else {
    		int IndexNewX2 = 0;
    		IndexNewX2 = randx2 - 1;
        	randx2 = IndexNewX2;
        	Player2.setCenterX((double) ((IndexNewX2)*Xincrement));
       		Player2.setCenterY(TerrainList.get(IndexNewX2));
       		Firebool2 = false;
       		return;
    	}

    }
    // Key inputs for movement AND CHEATCODES
    private void handleKeyMove (KeyCode code) {
		switch (code) {
		case RIGHT:
			MoveRight();
			TurnSwitch();
			break;
		case LEFT:
			MoveLeft();
			TurnSwitch();
			break;
		case UP:
			P1Rounds++;
    		PlayerLose = "Player 2 ";
    		ResetRound();
    		myScene.setOnMouseClicked(e -> handleMouseScenes(e.getX(), e.getY()));
    		MakeShopMenu();
			break;
		case DOWN:
			P2Rounds++;
    		PlayerLose = "Player 1 ";	    		
    		ResetRound();
    		myScene.setOnMouseClicked(e -> handleMouseScenes(e.getX(), e.getY()));
    		MakeShopMenu();
			break;
		default:
		}
		return;
   }
    
    // Handles user input for angle of shot.
    private void handleKeyAngle (KeyCode code) {
    	if (PlayerTurn == 1) {
            P2AngleDisplay.setText("");
	    	switch (code) {
	    	case DIGIT0:
		    	Degrees1 = -80;
	    		break;
	    	case DIGIT1:
	    		Degrees1 = -62;
	    		break;
	    	case DIGIT2:
	    		Degrees1 = -44;
	    		break;
	    	case DIGIT3:
	    		Degrees1 = -26;
	    		break;
	    	case DIGIT4:
	    		Degrees1 = -8;
	    		break;
	    	case DIGIT5:
	    		Degrees1 = 10;
	    		break;
	    	case DIGIT6:
	    		Degrees1 = 28;
	    		break;
	    	case DIGIT7:
	    		Degrees1 = 46;
	    		break;
	    	case DIGIT8:
	    		Degrees1 = 64;
	    		break;
	    	case DIGIT9:
	    		Degrees1 = 82;
	    		break;
	    		default:	
	    	}
	    	P1AngleDisplay.setText("");
	    	P2AngleDisplay.setText("");
    		P1AngleDisplay.setText("" + Degrees1);
    	}
    	if (PlayerTurn == 2) {
	    	switch (code) {
	    	case DIGIT0:
		    	Degrees2 = -80;
	    		break;
	    	case DIGIT1:
	    		Degrees2 = -62;
	    		break;
	    	case DIGIT2:
	    		Degrees2 = -44;
	    		break;
	    	case DIGIT3:
	    		Degrees2 = -26;
	    		break;
	    	case DIGIT4:
	    		Degrees2 = -8;
	    		break;
	    	case DIGIT5:
	    		Degrees2 = 10;
	    		break;
	    	case DIGIT6:
	    		Degrees2 = 28;
	    		break;
	    	case DIGIT7:
	    		Degrees2 = 46;
	    		break;
	    	case DIGIT8:
	    		Degrees2 = 64;
	    		break;
	    	case DIGIT9:
	    		Degrees2 = 82;
	    		break;
	    		default:	
	    	}
	    	P1AngleDisplay.setText("");
	    	P2AngleDisplay.setText("");
    		P2AngleDisplay.setText("" + Degrees2);
    	}
	}
    

    private void handleKeyPower (KeyCode code) {
    	if (PlayerTurn == 1) {
	    	switch (code) {
	    	case DIGIT0:
		    	Velocity1 = 10;
	    		break;
	    	case DIGIT1:
	    		Velocity1 = 20;
	    		break;
	    	case DIGIT2:
	    		Velocity1 = 30;
	    		break;
	    	case DIGIT3:
	    		Velocity1 = 40;
	    		break;
	    	case DIGIT4:
	    		Velocity1 = 50;
	    		break;
	    	case DIGIT5:
	    		Velocity1 = 60;
	    		break;
	    	case DIGIT6:
	    		Velocity1 = 70;
	    		break;
	    	case DIGIT7:
	    		Velocity1 = 80;
	    		break;
	    	case DIGIT8:
	    		Velocity1 = 90;
	    		break;
	    	case DIGIT9:
	    		Velocity1 = 100;
	    		break;
	    		default:	
	    	}
	    	P1PowerDisplay.setText("");;
	    	P2PowerDisplay.setText("");
    		P1PowerDisplay.setText("" + Velocity1);
    	}
    	if (PlayerTurn == 2) {
	    	switch (code) {
	    	case DIGIT0:
		    	Velocity2 = 10;;
	    	case DIGIT1:
	    		Velocity2 = 20;
	    		break;
	    	case DIGIT2:
	    		Velocity2 = 30;
	    		break;
	    	case DIGIT3:
	    		Velocity2 = 40;
	    		break;
	    	case DIGIT4:
	    		Velocity2 = 50;
	    		break;
	    	case DIGIT5:
	    		Velocity2 = 60;
	    		break;
	    	case DIGIT6:
	    		Velocity2 = 70;
	    		break;
	    	case DIGIT7:
	    		Velocity2 = 80;
	    		break;
	    	case DIGIT8:
	    		Velocity2 = 90;
	    		break;
	    	case DIGIT9:
	    		Velocity2 = 100;
	    		break;
	    		default:	
	    	}
	    	P1PowerDisplay.setText("");
	    	P2PowerDisplay.setText("");
    		P2PowerDisplay.setText("" + Velocity2);	
    	}
    }
    
    // Toggles between different types of weapons
    private void handleKeyWeapons (KeyCode code) {
    	if (PlayerTurn == 1) {
	    	switch (code) {
	    	case DIGIT0:
		    	Weapon1 = 0;
	    		break;
	    	case DIGIT1:
	    		if (DoubleShot1 == true) {
	    			Weapon1 = 1;
	    		}
	    		break;
	    	case DIGIT2:
	    		if (QCbool1 == true) {
	    			Weapon1 = 2;
	    		}
	    		break;
	    		default:	
	    	}
	    	WeaponSelectNumber1.setText("");
	    	WeaponSelectNumber2.setText("");
    		WeaponSelectNumber1.setText("" + Weapon1);
    	}
    	if (PlayerTurn == 2) {
	    	switch (code) {
	    	case DIGIT0:
		    	Weapon2 = 0;
	    	case DIGIT1:
	    		if (DoubleShot2 == true) {
	    			Weapon2 = 1;
	    		}
	    		break;
	    	case DIGIT2:
	    		if (QCbool2 == true) {
	    			Weapon2 = 2;
	    		}
	    		break;
	    		default:	
	    	}
	    	WeaponSelectNumber1.setText("");;
	    	WeaponSelectNumber2.setText("");
    		WeaponSelectNumber2.setText("" + Weapon2);
    	}
    }

    // What to do for each button that is pressed in the in game menu such as the angle, power, or weapon type button.
    private void handleMouseInput (double x, double y) {
        if (MoveSelect.contains(x, y)) {
        	if (PlayerTurn == 1 && Movebool1 == true) {
        		Firebool1 = false;
        		Firebool2 = false;
        		Movebool1 = true;
        	}
        	if (PlayerTurn == 1 && Movebool1 == false) {
        		Firebool1 = false;
        		Firebool2 = false;
        		Movebool1 = false;
        	}
        	if (PlayerTurn == 2 && Movebool2 == true) {
        		Firebool1 = false;
        		Firebool2 = false;
        		Movebool2 = true;
        	}
        	if (PlayerTurn == 2 && Movebool2 == false) {
        		Firebool1 = false;
        		Firebool2 = false;
        		Movebool2 = false;
        	}
            if (PlayerTurn == 1 && Movebool1 == true) {
            	if (Player1.getCenterX() < Xincrement) {
            		MoveRight();
            		TurnSwitch();
            	}
            	if (Player1.getCenterX() > XSIZE - Xincrement) {
            		MoveLeft();
            		TurnSwitch();
            	}
            	else {
            		myScene.setOnKeyPressed(e -> handleKeyMove(e.getCode()));
            		return;
            	}
            }
            else if (PlayerTurn == 2 && Movebool2 == true) {
            	if (Player2.getCenterX() < Xincrement) {
            		MoveRight();
            		TurnSwitch();
            	}
            	if (Player2.getCenterX() > XSIZE - Xincrement) {
            		MoveLeft();
            		TurnSwitch();
            	}
            	else {
            		myScene.setOnKeyPressed(e -> handleKeyMove(e.getCode()));
            		return;
            	}
            }
            return;
        }
        
        else if (WeaponSelect.contains(x,y)) {
        	myScene.setOnKeyPressed(e -> handleKeyWeapons(e.getCode()));
        }
        else if (AngleSelect.contains(x, y)) {
        	myScene.setOnKeyPressed(e -> handleKeyAngle(e.getCode()));
        }
        else if (PowerSelect.contains(x, y)) {
            myScene.setOnKeyPressed(e -> handleKeyPower(e.getCode()));
        }
        else if (FireButton.contains(x,y)) {
        	if (PlayerTurn == 1) {
        		Firebool1 = true;
        		Firebool2 = false;
        		Arrow1.setCenterX(Player1.getCenterX());
        		Arrow1.setCenterY(Player1.getCenterY());
        		vy1 = -Velocity1*Math.sin(Math.toRadians(Degrees1))/4;
        	}
        	if (PlayerTurn == 2) {
        		Firebool2 = true;
        		Firebool1 = false;
        		Arrow2.setCenterX(Player2.getCenterX());
        		Arrow2.setCenterY(Player2.getCenterY());
        		vy2 = -Velocity2*Math.sin(Math.toRadians(180-Degrees2))/4;
        	}
        	vx1 = (Velocity1*Math.cos(Math.toRadians(Degrees1)))/4;
    		vy1 = -Velocity1*Math.sin(Math.toRadians(Degrees1))/4;
    		vx2 = (Velocity2*Math.cos(Math.toRadians(180-Degrees2)))/4;
    		vy2 = -Velocity2*Math.sin(Math.toRadians(180-Degrees2))/4;
        	hitP1 = false;
        	hitP2 = false;
            step(0.01);
        	TurnSwitch();
        }

    }
}
