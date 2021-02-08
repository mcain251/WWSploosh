//positions are from the top left of the board
//orientations are 0= right, 1 = up
//big = 4 spaces, med = 3, sml = 2
//for history, 0 = unknown, 1 = miss, 2 = hit
//for squids, 0,1,2 = big params (x,y,orientation), 3,4,5 = mid params, 6,7,8 = sml params.
//for the board display, X = hit, O = miss, B = best (if you are in seeBoard mode, i = squid, I = best & squid)
import java.util.*;
class Main {
  public static void main(String[] args) {
    //Set to -1 for random seed
    int setSeed = -1;
    boolean seeBoard = true;
    boolean displayBoardSeed = true;
    boolean seeAsProportions = false;
    boolean displayNumberOfPossibleArrangements = true;
    boolean autoTesting = false;
    //True = will give average turns until win
    //False = will give number of tries until number of turns was less than  the successBy int.
    boolean averageTurnsVsSuccessBy = false;
    int successBy = 19;
    boolean trackSinking = true;
    //-1 for infinite
    int numTests = -1;

    int testCounter = 1;
    if (autoTesting){
      testCounter = numTests;
    }
    double averageTurns = 0;
    boolean reachedGoal = false;

    String response = "";
    Scanner in = new Scanner(System.in);
    if (!autoTesting) {
      System.out.println("Do you want a generated board? (y/n)  ");
      while (!response.equals("y") && !response.equals("n")){
        response = in.nextLine();
      }
    }
    boolean isGenerated = !response.equals("n");

    while (testCounter != 0 && !reachedGoal){
      int bestProbability = 0;
      ArrayList<Integer[]> bestMoves = new ArrayList<Integer[]>();

      int turnCounter = 1;
      int winCheckCounter = 0;

      int choice = setSeed;
      if (setSeed == -1){
        choice = (int)(Math.random()*604584);
      }
      
      if (displayBoardSeed && isGenerated){
        System.out.println("Board seed is " + choice + ".");
      }
      boolean[][] board = new boolean[8][8];
      boolean[][] hits = new boolean[8][8];
      boolean[][] misses = new boolean[8][8];

      int[] squids = new int[9];
      boolean big_sunk = false;
      boolean med_sunk = false;
      boolean sml_sunk = false;

      int[][] history = new int[8][8];
      int usr_x,usr_y;
      boolean isHit;

      while (true){
        int[][] probabilitygrid = new int[8][8];
        int counter = 0;
        for (int big_x = 0; big_x != 8; big_x++){
          for (int big_y = 0; big_y != 8; big_y++){
            for (int big_o = 0; big_o != 2; big_o++){
              if (!(big_sunk && (big_x != squids[0] || big_y != squids[1] || big_o != squids[2])) && !(big_o == 0 && big_x > 4 || big_o == 1 && big_y < 3)){
                for (int med_x = 0; med_x != 8; med_x++){
                  for (int med_y = 0; med_y != 8; med_y++){
                    for (int med_o = 0; med_o != 2; med_o++){
                      if (!(med_sunk && (med_x != squids[3] || med_y != squids[4] || med_o != squids[5])) && !(med_o == 0 && med_x > 5 || med_o == 1 && med_y < 2 || (big_x == med_x && big_y == med_y))){
                        for (int sml_x = 0; sml_x != 8; sml_x++){
                          for (int sml_y = 0; sml_y != 8; sml_y++){
                            for (int sml_o = 0; sml_o != 2; sml_o++){
                              if (!(sml_sunk && (sml_x != squids[6] || sml_y != squids[7] || sml_o != squids[8])) && !(sml_o == 0 && sml_x == 7 || sml_o == 1 && sml_y == 0 || (big_x == sml_x && big_y == sml_y) || (med_x == sml_x && med_y == sml_y))){
                                boolean[][] grid = new boolean[8][8];
                                boolean isValid = true;
                                boolean cantExist = true;
                                if (big_o == 0){
                                  for (int i = 0; i != 4; i++){
                                    if (grid[big_x+i][big_y] || misses[big_x+i][big_y]){
                                      isValid = false;
                                    }
                                    grid[big_x+i][big_y] = true;
                                    if (trackSinking && !big_sunk && !hits[big_x+i][big_y]){
                                      cantExist = false;
                                    }
                                  }
                                  if (trackSinking && !big_sunk && cantExist){
                                    isValid = false;
                                  }
                                } else if (big_o == 1){
                                  for (int i = 0; i != 4; i++){
                                    if (grid[big_x][big_y-i] || misses[big_x][big_y-i]){
                                      isValid = false;
                                    }
                                    grid[big_x][big_y-i] = true;
                                    if (trackSinking && !big_sunk && !hits[big_x][big_y-i]){
                                      cantExist = false;
                                    }
                                  }
                                  if (trackSinking && !big_sunk && cantExist){
                                    isValid = false;
                                  }
                                }
                                if (med_o == 0){
                                  for (int i = 0; i != 3; i++){
                                    if (grid[med_x+i][med_y] || misses[med_x+i][med_y]){
                                      isValid = false;
                                    }
                                    grid[med_x+i][med_y] = true;
                                    if (trackSinking && !med_sunk && !hits[med_x+i][med_y]){
                                      cantExist = false;
                                    }
                                  }
                                  if (trackSinking && !med_sunk && cantExist){
                                    isValid = false;
                                  }
                                } else if (med_o == 1){
                                  for (int i = 0; i != 3; i++){
                                    if (grid[med_x][med_y-i] || misses[med_x][med_y-i]){
                                      isValid = false;
                                    }
                                    grid[med_x][med_y-i] = true;
                                    if (trackSinking && !med_sunk && !hits[med_x][med_y-i]){
                                      cantExist = false;
                                    }
                                  }
                                  if (trackSinking && !med_sunk && cantExist){
                                    isValid = false;
                                  }
                                }
                                if (sml_o == 0){
                                  for (int i = 0; i != 2; i++){
                                    if (grid[sml_x+i][sml_y] || misses[sml_x+i][sml_y]){
                                      isValid = false;
                                    }
                                    grid[sml_x+i][sml_y] = true;
                                    if (trackSinking && !sml_sunk && !hits[sml_x+i][sml_y]){
                                      cantExist = false;
                                    }
                                  }
                                  if (trackSinking && !sml_sunk && cantExist){
                                    isValid = false;
                                  }
                                } else if (sml_o == 1){
                                  for (int i = 0; i != 2; i++){
                                    if (grid[sml_x][sml_y-i] || misses[sml_x][sml_y-i]){
                                      isValid = false;
                                    }
                                    grid[sml_x][sml_y-i] = true;
                                    if (trackSinking && !sml_sunk && !hits[sml_x][sml_y-i]){
                                      cantExist = false;
                                    }
                                  }
                                  if (trackSinking && !sml_sunk && cantExist){
                                    isValid = false;
                                  }
                                }
                                if (isValid){
                                  for (int x = 0; x != 8; x++){
                                    for (int y = 0; y != 8; y++){
                                      if (hits[x][y] && !grid[x][y]){
                                        isValid = false;
                                      }
                                    }
                                  }
                                }
                                if (isValid){
                                  counter++;
                                  if (choice != 0){
                                    choice--;
                                  } else {
                                    choice--;
                                    board = grid;
                                    squids[0] = big_x;
                                    squids[1] = big_y;
                                    squids[2] = big_o;
                                    squids[3] = med_x;
                                    squids[4] = med_y;
                                    squids[5] = med_o;
                                    squids[6] = sml_x;
                                    squids[7] = sml_y;
                                    squids[8] = sml_o;
                                    if (seeBoard && isGenerated){
                                      for (int y = 0; y != 8; y++){
                                        for (int x = 0; x != 8; x++){
                                          if (board[x][y]){
                                            System.out.print("[X]");
                                          } else {
                                            System.out.print("[ ]");
                                          }
                                        }
                                        System.out.println();
                                      }
                                    }
                                  }
                                  for (int x = 0; x != 8; x++){
                                    for (int y = 0; y != 8; y++){
                                      if (grid[x][y] && !hits[x][y]){
                                        probabilitygrid[x][y]++;
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        System.out.println("Turn " + turnCounter + ": ");
        winCheckCounter = 0;
        for (int x = 0; x != 8; x++){
          for (int y = 0; y != 8; y++){
            if (history[x][y] == 2){
              winCheckCounter++;
            }
          }
        }
        if (winCheckCounter == 9){
          System.out.println("You won in " + turnCounter + " turns.");
          testCounter--;
          if (autoTesting){
            if (averageTurns == 0){
              averageTurns = turnCounter;
            } else {
              averageTurns *= (double)(numTests-testCounter-1)/(double)(numTests-testCounter);
              averageTurns += (double)turnCounter/(double)(numTests-testCounter);
            }
            if (averageTurnsVsSuccessBy){
              System.out.println("Average turns after " + (numTests-testCounter) + " tests = " + averageTurns);
            } else {
              if (turnCounter <= successBy){
                System.out.println("Turns <= " + successBy + " in " + (numTests-testCounter) + " tries.");
                reachedGoal = true;
              } else {
                System.out.println("Turns still > " + successBy + " after " + (numTests-testCounter) + " tries.");
              }
            }
          }
          break;
        }
        bestMoves.clear();
        bestProbability = 0;
        for (int y = 0; y != 8; y++){
          for (int x = 0; x != 8; x++){
            if (seeAsProportions){
              System.out.printf("%-9s",(double)((int)(((double)probabilitygrid[x][y]/(double)counter) * 1000000))/(double)1000000);
            } else {
              System.out.printf("%-7s",probabilitygrid[x][y]);
            }
            if (probabilitygrid[x][y] > bestProbability){
              bestMoves.clear();
              bestProbability = probabilitygrid[x][y];
              bestMoves.add(new Integer[]{x,y});
            } else if (probabilitygrid[x][y] == bestProbability){
              bestMoves.add(new Integer[]{x,y});
            }
          }
          System.out.println();
        }
        if (displayNumberOfPossibleArrangements){
          System.out.println("There are " + counter + " possible arrangements.");
        }
        if (!autoTesting){
          System.out.print("Your best moves are: ");
          for (Integer[] i : bestMoves){
            System.out.print("(" + i[0] + "," + i[1] + ") ");
          }
          System.out.println();
        }
        for (int y = 0; y != 8; y++){
          for (int x = 0; x != 8; x++){
            if (history[x][y] == 1){
              System.out.print("[O]");
            } else if (history[x][y] == 2){
              System.out.print("[X]");
            } else {
              boolean isBest = false;
              for (Integer[] i : bestMoves){
                if (i[0] == x && i[1] == y){
                  isBest = true;
                  if (isGenerated && seeBoard && board[x][y]) {
                    System.out.print("[I]");
                  } else {
                    System.out.print("[B]");
                  }
                }
              }
              if (!isBest){
                if (isGenerated && seeBoard && board[x][y]) {
                  System.out.print("[i]");
                } else {
                  System.out.print("[ ]");
                }
              }
            }
          }
          System.out.println();
        }
        if (!autoTesting) {
          response = "";
          System.out.println("What is your next move? (format as \"x,y\")");
          while (response.length() < 3) {
            response = in.nextLine();
          }
          usr_x = (int)response.charAt(0)-48;
          usr_y = (int)response.charAt(2)-48;
          while (history[usr_x][usr_y] != 0){
            System.out.println("You already guessed here, try again.");
            response = in.nextLine();
            usr_x = (int)response.charAt(1);
            usr_y = (int)response.charAt(3);
          }
          if (isGenerated) {
            isHit = board[usr_x][usr_y];
            if (isHit) {
              System.out.println("It was a hit.");
            } else {
              System.out.println("It was a miss.");
            }
          } else {
            System.out.println("Was is a hit? (y/n)");
            while (!response.equals("y") && !response.equals("n")){
              response = in.nextLine();
            }
            isHit = response.equals("y");
            if (trackSinking) {
              System.out.println("Did it sink a squid? (And can you tell exactly where it is?) (y/n)");
              response = "";
              while (!response.equals("y") && !response.equals("n")){
                response = in.nextLine();
              }
              boolean sunk = response.equals("y");
              while (sunk){
                System.out.println("Which squid? (sml, med, big)");
                response = "";
                while (!response.equals("sml") && !response.equals("med") && !response.equals("big")){
                  response = in.nextLine();
                }
                int offset = 0;
                if (response.equals("sml")){
                  offset = 6;
                  sml_sunk = true;
                } else if (response.equals("med")){
                  offset = 3;
                  med_sunk = true;
                } else {
                  big_sunk = true;
                }
                System.out.println("Where exactly is the squid? (location of the lower-leftmost part, then 0 if it is horizontal and 1 if it is vertical, in this format: \"x,y,o\")");
                response = "";
                while (response.length() < 5){
                  response = in.nextLine();
                }
                squids[offset] = (int)response.charAt(0)-48;
                squids[offset + 1] = (int)response.charAt(2)-48;
                squids[offset + 2] = (int)response.charAt(4)-48;
                System.out.println("Did this reveal the exact location of another squid that you already sunk? (y/n)");
                response = "";
                while (!response.equals("y") && !response.equals("n")){
                  response = in.nextLine();
                }
                sunk = response.equals("y");
              }
            }
          }
        } else {
          int randomChoice = (int)(Math.random()*bestMoves.size());
          usr_x = bestMoves.get(randomChoice)[0];
          usr_y = bestMoves.get(randomChoice)[1];
          isHit = board[usr_x][usr_y];
        }
        if (isHit) {
          history[usr_x][usr_y] = 2;
          hits[usr_x][usr_y] = true;
        } else {
          history[usr_x][usr_y] = 1;
          misses[usr_x][usr_y] = true;
        }
        if (isGenerated && trackSinking){
          if (!big_sunk){
            boolean isSunk = true;
            for (int i = 0; i < 4; i++){
              if (squids[2] == 0){
                if (history[squids[0]+i][squids[1]] != 2){
                  isSunk = false;
                }
              } else {
                if (history[squids[0]][squids[1]-i] != 2){
                  isSunk = false;
                }
              }
            }
            if (isSunk){
              big_sunk = true;
              if (!autoTesting){
                System.out.println("Big squid sunk");
              }
            }
          }
          if (!med_sunk){
            boolean isSunk = true;
            for (int i = 0; i < 3; i++){
              if (squids[5] == 0){
                if (history[squids[3]+i][squids[4]] != 2){
                  isSunk = false;
                }
              } else {
                if (history[squids[3]][squids[4]-i] != 2){
                  isSunk = false;
                }
              }
            }
            if (isSunk){
              med_sunk = true;
              if (!autoTesting){
                System.out.println("Medium squid sunk");
              }
            }
          }
          if (!sml_sunk){
            boolean isSunk = true;
            for (int i = 0; i < 2; i++){
              if (squids[8] == 0){
                if (history[squids[6]+i][squids[7]] != 2){
                  isSunk = false;
                }
              } else {
                if (history[squids[6]][squids[7]-i] != 2){
                  isSunk = false;
                }
              }
            }
            if (isSunk){
              sml_sunk = true;
              if (!autoTesting){
                System.out.println("Small squid sunk");
              }
            }
          }
        }
        turnCounter++;
      }
    }
    in.close();
  }
}