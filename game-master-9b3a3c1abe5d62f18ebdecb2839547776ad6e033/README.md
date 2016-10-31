Game Readme
============
First project for CompSci 308 Fall 2016
<<<<<<< HEAD
**People who worked on this project:** Christopher Lu (cjl46)
**Date Started:** 9/4/16
**Date Finished:** 9/11/16
**Hours Spent:** 30 hours
**Roles:**
		*Christopher Lu*  -- Did everything, including designing the game's win conditions, the mechanics, how to navigate between  menus, the in game user interface such has how to input angles, power, and weapon type as well as how to move the player, power ups, how to tilt the game in the losing player's favor, etc.
**Resources Used:** 
*StackExchange* --> Looked up how to use Math.random, what and how Group objects worked for changing my scenes (which I ended up not using),.
*Oracle's JavaFX 8 Documentation*
*ExampleGame.Java and Main.java template code* --> by far the most helpful resource, as it taught me how to handle mouse and key inputs, how the animation and step functions actually worked, how scenes and Group nodes worked, etc.
**Files Used To Start The Project:** Main.java and ExampleGame.java
**Files Used To Test The Project:** Just ran Game.java every time I wanted to test something I wrote.
**Data Or Resource Files Required:** None.
**How To Use And Play This Program:** 
1. Run Main.java. 
2. The first thing you should see is the start menu. You can either click start (Go to step 4) or help (Go to step 3).
3. The help menu tells you about the backstory and how to win the game. When finished, click the return to main menu screen, or anywhere on the screen.
4. You should see randomly generated terrain made up of lines with two dots representing Player 1 (BLUE) and Player 2 (GREEN). In order to move, click the MOVE button, and press left or right. You can only move once in a turn. The left button takes you to the closest vertex on your left, while the right button takes you to the closest vertex on your right. In order to fire, press the red button. In order to adjust the angle of the shot, click the angle button, and press a number between 0 and 9. If player 1 was the center of a clock, the angle input values of 0 to 9 range between 6 to 12 'o clock counterclockwise. If player 2 was the center of a clock, the angle input values of 0 to 9 range between 6 to 12 'o clock clockwise. In order to adjust the power of the shot, click the power button and input a number between 0 and 9, which will fire the shot at values between 10 and 100 power. In order to change your weapon, select weapon, and select either 0, 1, or 2. If you only have the default weapon, the other weapons should be unavailable to you.
When your shot hits the other player, your score for the round in the lower right hand corner parentheses next to your player should increase. The round is won by the player who hits 5 points first.  After the end of each round, the loser gets to choose a power up from the power up store. The power up that is chosen will be unavailable for the other player, and can be used any number of times in a round. HINT: The Double Shot is the best. The first player to win 2 rounds wins the game. IT IS ALSO IMPORTANT TO NOTE THAT THE LINES REPRESENT A PATH THAT THE PLAYERS CAN MOVE TO AND NOT ACTUAL PHYSICAL TERRAIN, SO BULLETS CAN GO THROUGH THE LINES.
**Bugs:** There are a couple bugs in my game. There is no restriction for the movement of the players, so they can move off the screen, or into the same position, where they would be right on top of each other so that only one player will be visible. In addition, the angle and power values only display the most recently chosen values, so after player 2's turn is finished, player 1 will only see player 2's values even though the actual values of player 1's shot will be the same as the values input by player 1 in the previous turn. 
**Extra Features:** 
*Cheat Codes:* Click MOVE and then press the up arrow for Player 1 to automatically win the round. Click MOVE and then press the down arrow for Player 2 to automatically win the round.
**How To Improve This Project:** Figure out collision, so that when a projectile hits a line of terrain, the bullet stops instead of going straight through. Fix movement so players are not allowed to move past the boundaries and so that players can not be in the exact same place. Fix user interface to display EACH player's most recent values.
=======
The Lines are NOT terrain, rather, they are the movement paths available to each player.
Cheat Codes: After Clicking move, press UP for Player 1 to win round and press DOWN 
for Player 2 to win round.
>>>>>>> d3d256be40ea16edf3eefc951bcd9e4366188fe1
