import java.util.*;
import java.io.*;
import java.util.Random;
import java.util.Arrays;
//this class are used for unkown points that are given, keeps track of their cordiantes and also
// the possible numbers that could be used in the given array
class Point{
    int x;
    int y;
    List<Integer> posssible = new ArrayList<Integer>();
    public Point(int xx, int yy, List<Integer> a ){
        x= xx;
        y = yy;
        posssible = a;
    } 
     public String toString() {
        return "X: " + x + "  Y:" + y + "  posssible: "+ posssible;
    }
    public List<Integer> getPoint(){
        return this.posssible;     
    } 
    
}

class Change {
    int x1,y1,x2,y2,oneOld,twoOld;
    public Change(Point p1 , Point p2, int uno, int dos){
        x1=p1.x;
        y1=p1.y;
        x2=p2.x;
        y2=p2.y;
        oneOld=uno;
        twoOld=dos;
    }
}
//HILL CLIMBING: heurstic implmentation. randomly fills in given vars with numbers that work for that given cord.
//then changes a random cord for all of its possible vals and finds the lowest score, moves on and repeats
// simulated aniealling is implementend so that only higher vlaue switches are made and random restarts incase it gets stuck.
class Sudoku1
{
    /* SIZE is the size parameter of the Sudoku puzzle, and N is the square of the size.  For 
     * a standard Sudoku puzzle, SIZE is 3 and N is 9. */
    int SIZE, N;
    // where zeros are kept
    List<Point> zerolist = new ArrayList<Point>();
    /* The grid contains all the numbers in the Sudoku puzzle.  Numbers which have
     * not yet been revealed are stored as 0. */
    int Grid[][];
 /** Checks if num is an acceptable value for the given row */
 //takes an array and checks dupplicats
   public int CheckDup(int[] list){
    Arrays.sort(list);
    int count =0;

        for(int i = 1; i < list.length; i++) {
            if(list[i] == list[i - 1] &&list[i] !=0) {
                count++;
            }
        }
        return count;
    }

   // sends a all of the boxes from the puzzle to check dup and returns the sum of boxes 
   public int checkBox( int[][] cGrid ){
    int boxScore =0;
    for (int i = 0; i < SIZE*SIZE; i++) {    
        int[] square = new int[SIZE*SIZE];
        for (int j = 0; j < SIZE*SIZE; j ++) {
            square[j] = cGrid[(i / SIZE) * SIZE + j / SIZE][i * SIZE % N + j % SIZE];
        }
        boxScore = boxScore+ CheckDup(square);
    }
        return boxScore;
   }
     // sends a all of the rows from the puzzle to check dup and returns the sum of rows 

   public int checkRow(int[][] cGrid){
    int[] rowList =new int[N];

    int rowScore =0;
    for (int row =0; row<N  ; row++ ) {
            for (int col=0; col<N  ;col ++ ) {
                rowList[col] = cGrid[col][row];
            }
            rowScore = rowScore + CheckDup(rowList);
        }
    return rowScore;    
    }

int[][] deepCopy(int[][] g){
    int[][] NEWGRID = new int[N][N];
      for(int i = 0; i<N;i++){
                for(int j = 0; j<N;j++){
                    NEWGRID[i][j]=g[i][j];
                    }
                }
        return NEWGRID;
}
// sends a all of the collums from the puzzle to check dup and returns the sum of collums 
    public int checkCol(int[][] cGrid){
    int[] colList =new int[N];
    int colScore =0;
    for (int row =0; row<N  ; row++ ) {
            for (int col=0; col<N  ;col ++ ) {
                colList[col] = cGrid[row][col];

            }

            colScore = colScore + CheckDup(colList);

        }
    return colScore;    
    }

    /* The solve() method should remove all the unknown characters ('x') in the Grid
     * and replace them with the numbers from 1-9 that satisfy the Sudoku puzzle. */
    public int evaluate(int[][] cGrid){
        int score =0;
        score = checkCol(cGrid) + checkRow(cGrid) +checkBox(cGrid);
        return score;
    }

    public int[][] populate(){
        /// randomize the given vars absed one possible values
        int[] poss = new int[N];
        for(int i =0;i<N;i++){
            poss[i]=N;
        }
        for(int x=0;x<N;x++){
            for(int y=0;y<N;y++){
                if(Grid[x][y]!=0){
                    poss[Grid[x][y]-1]--; //Number of possibilities of each number
                }
            }
        }
        int[][] repoplated = deepCopy(Grid);         
        for(Point p: zerolist){
            int random = (int ) (Math.random() * N);
            while(poss[random]==0){
                // System.out.println(random);
                random = (int ) (Math.random() * N);
            }
            repoplated[p.x][p.y]=random+1;
            poss[random]--;
        }
     return repoplated;   
    }


    public void solve()
    {
        
    
        boolean flag =true;
        for(int i = 0; i<N;i++){
                for(int j = 0; j<N;j++){
                  if(Grid[i][j] == 0){
                        List<Integer> poslist = new ArrayList<Integer>();
                        Point p = new Point(i,j,poslist);
                        zerolist.add(p);
                    }
              }
          }
        //intilizing vars used by the solve loop
        int CURRENTGRID[][] =populate();
        double threshold = 1.0;
        double coolingrate = 0.02;
        int oldScore = evaluate(CURRENTGRID);
        if(oldScore == 0){
            System.out.println("HERERE");
            Grid=CURRENTGRID;
            return;
         }

        int count = 0;
        while(flag==true){            
            int randpoint = (int)(Math.random()*zerolist.size());
            Point one = zerolist.get(randpoint);
            int randtwo = (int)(Math.random()*zerolist.size());
            Point two = zerolist.get(randtwo);
            Change change = new Change(one,two, CURRENTGRID[one.x][one.y], CURRENTGRID[two.x][two.y]);
            int temp = CURRENTGRID[change.x1][change.y1];
            CURRENTGRID[change.x1][change.y1] = CURRENTGRID[change.x2][change.y2];
             CURRENTGRID[change.x2][change.y2]= temp;
            int newscore = evaluate(CURRENTGRID);
            if(newscore ==0){
                Grid= CURRENTGRID;
                break;
            }
            if (oldScore < newscore) { //We made the board worse
                System.out.println(newscore +"    "+ count+"  "+ oldScore);
                  count++;
                  // threshold = threshold - (1*coolingrate);

                  if (count ==1000) {
                    System.out.println("stuck in a minima");
                    CURRENTGRID =populate();
                    oldScore=evaluate(CURRENTGRID);
                    threshold =1.0;
                    count = 0;
                  }  
                  if (true) { //Reject change, revert back
                    // count = 0;

                       temp = CURRENTGRID[change.x1][change.y1];
                        CURRENTGRID[change.x1][change.y1] = CURRENTGRID[change.x2][change.y2];
                     CURRENTGRID[change.x2][change.y2]= temp;
                  }     
                }
                else if (oldScore==newscore){ //we made the board better
                    // System.out.println("Better board");
                    count++;
                    oldScore=newscore;
            }
                else{
                    count = 0;
                    oldScore=newscore;
                }
         }
         
    }


    /*****************************************************************************/
    /* NOTE: YOU SHOULD NOT HAVE TO MODIFY ANY OF THE FUNCTIONS BELOW THIS LINE. */
    /*****************************************************************************/
 
    /* Default constructor.  This will initialize all positions to the default 0
     * value.  Use the read() function to load the Sudoku puzzle from a file or
     * the standard input. */
    public Sudoku1( int size )
    {
        SIZE = size;
        N = size*size;

        Grid = new int[N][N];
        for( int i = 0; i < N; i++ ) 
            for( int j = 0; j < N; j++ ) 
                Grid[i][j] = 0;
    }


    /* readInteger is a helper function for the reading of the input file.  It reads
     * words until it finds one that represents an integer. For convenience, it will also
     * recognize the string "x" as equivalent to "0". */
    static int readInteger( InputStream in ) throws Exception
    {
        int result = 0;
        boolean success = false;

        while( !success ) {
            String word = readWord( in );

            try {
                result = Integer.parseInt( word );
                success = true;
            } catch( Exception e ) {
                // Convert 'x' words into 0's
                if( word.compareTo("x") == 0 ) {
                    result = 0;
                    success = true;
                }
                // Ignore all other words that are not integers
            }
        }

        return result;
    }


    /* readWord is a helper function that reads a word separated by white space. */
    static String readWord( InputStream in ) throws Exception
    {
        StringBuffer result = new StringBuffer();
        int currentChar = in.read();
	String whiteSpace = " \t\r\n";
        // Ignore any leading white space
        while( whiteSpace.indexOf(currentChar) > -1 ) {
            currentChar = in.read();
        }

        // Read all characters until you reach white space
        while( whiteSpace.indexOf(currentChar) == -1 ) {
            result.append( (char) currentChar );
            currentChar = in.read();
        }
        return result.toString();
    }


    /* This function reads a Sudoku puzzle from the input stream in.  The Sudoku
     * grid is filled in one row at at time, from left to right.  All non-valid
     * characters are ignored by this function and may be used in the Sudoku file
     * to increase its legibility. */
    public void read( InputStream in ) throws Exception
    {
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                Grid[i][j] = readInteger( in );
            }
        }
    }


    /* Helper function for the printing of Sudoku puzzle.  This function will print
     * out text, preceded by enough ' ' characters to make sure that the printint out
     * takes at least width characters.  */
    void printFixedWidth( String text, int width )
    {
        for( int i = 0; i < width - text.length(); i++ )
            System.out.print( " " );
        System.out.print( text );
    }


    /* The print() function outputs the Sudoku grid to the standard output, using
     * a bit of extra formatting to make the result clearly readable. */
    public void print()
    {
        // Compute the number of digits necessary to print out each number in the Sudoku puzzle
        int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;

        // Create a dashed line to separate the boxes 
        int lineLength = (digits + 1) * N + 2 * SIZE - 3;
        StringBuffer line = new StringBuffer();
        for( int lineInit = 0; lineInit < lineLength; lineInit++ )
            line.append('-');

        // Go through the Grid, printing out its values separated by spaces
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                printFixedWidth( String.valueOf( Grid[i][j] ), digits );
                // Print the vertical lines between boxes 
                if( (j < N-1) && ((j+1) % SIZE == 0) )
                    System.out.print( " |" );
                System.out.print( " " );
            }
            System.out.println();

            // Print the horizontal line between boxes
            if( (i < N-1) && ((i+1) % SIZE == 0) )
                System.out.println( line.toString() );
        }
    }


    /* The main function reads in a Sudoku puzzle from the standard input, 
     * unless a file name is provided as a run-time argument, in which case the
     * Sudoku puzzle is loaded from that file.  It then solves the puzzle, and
     * outputs the completed puzzle to the standard output. */
    public static void main( String args[] ) throws Exception
    {
        InputStream in;
        if( args.length > 0 ) 
            in = new FileInputStream( args[0] );
        else
            in = System.in;

        // The first number in all Sudoku files must represent the size of the puzzle.  See
        // the example files for the file format.
        int puzzleSize = readInteger( in );
        if( puzzleSize > 100 || puzzleSize < 1 ) {
            System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
            System.exit(-1);
        }

        Sudoku1 s = new Sudoku1( puzzleSize );

        // read the rest of the Sudoku puzzle
        s.read( in );

        // Solve the puzzle.  We don't currently check to verify that the puzzle can be
        // successfully completed.  You may add that check if you want to, but it is not
        // necessary.
        //s.solve();
        s.solve();
        // Print out the (hopefully completed!) puzzle
        s.print();
       

       
    }
}

