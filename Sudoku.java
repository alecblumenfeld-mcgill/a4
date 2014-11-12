import java.util.*;
import java.io.*;
import java.util.Random;
import java.util.Arrays;

class Sudoku
{
    /* SIZE is the size parameter of the Sudoku puzzle, and N is the square of the size.  For 
     * a standard Sudoku puzzle, SIZE is 3 and N is 9. */
    int SIZE, N;

    /* The grid contains all the numbers in the Sudoku puzzle.  Numbers which have
     * not yet been revealed are stored as 0. */
    int Grid[][];
 /** Checks if num is an acceptable value for the given row */
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

   /** Checks if num is an acceptable value for the box around row and col */
   public int checkBox(  )
   {
    for (int i = 0; i < 9; i++) {

        int[] square = new int[9];
     

        for (int j = 0; j < 9; j ++) {
            square[j] = Grid[(i / 3) * 3 + j / 3][i * 3 % 9 + j % 3];
        }
    }
        return 0;

   }
   public int checkRow(){
    int[] rowList =new int[N];

    int rowScore =0;
    for (int row =0; row<N  ; row++ ) {
            for (int col=0; col<N  ;col ++ ) {
                rowList[col] = Grid[col][row];
            }

            rowScore = rowScore + CheckDup(rowList);


        }
    return rowScore;    
    }
    /// this is broken fix please
    public int checkCol(){
    int[] colList =new int[N];
    int colScore =0;
    for (int row =0; row<N  ; row++ ) {
            for (int col=0; col<N  ;col ++ ) {
                colList[col] = Grid[row][col];

            }

            colScore = colScore + CheckDup(colList);

        }
    return colScore;    
    }

    /* The solve() method should remove all the unknown characters ('x') in the Grid
     * and replace them with the numbers from 1-9 that satisfy the Sudoku puzzle. */
    public int evaluate(){
        int score =0;
        score = this.checkCol() +this.checkRow() +this.checkBox();
        return score;
    }
    public void solve()
    {
        
       
        Random randomGenerator = new Random();
        for (int x =0 ; x<N ; x++) {
            for (int y =0; y<N ;y++ ) {
            if (Grid[x][y]==0) {
                Grid[x][y]= (int)( Math.random()+1);
                } 
            }
        }
        boolean flag =true;

        while(flag==true){
            flag =false;
            for(int i = 0; i<N-1;i++){
                for(int j = 0; j<N-1;j++){
                    i= (int)( Math.random()+1);
                    j= (int)( Math.random()+1);
                        for(int k = 1; k<N;k++){
                            int lastScore = this.evaluate();
                            int temp = Grid[i][j];
                            Grid[i][j] =k;
                            int nextscore = this.evaluate();
                            if (lastScore >nextscore) {
                                  Grid[i][j]=temp ;
                             } 
                             else{
                                flag =true;
                                System.out.println("test");
                             }

                        }

                }
            }
        }
    }


    /*****************************************************************************/
    /* NOTE: YOU SHOULD NOT HAVE TO MODIFY ANY OF THE FUNCTIONS BELOW THIS LINE. */
    /*****************************************************************************/
 
    /* Default constructor.  This will initialize all positions to the default 0
     * value.  Use the read() function to load the Sudoku puzzle from a file or
     * the standard input. */
    public Sudoku( int size )
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

        Sudoku s = new Sudoku( puzzleSize );

        // read the rest of the Sudoku puzzle
        s.read( in );

        // Solve the puzzle.  We don't currently check to verify that the puzzle can be
        // successfully completed.  You may add that check if you want to, but it is not
        // necessary.
        //s.solve();
        System.out.println("CHECK ORGINAL:" + s.evaluate());
        // Print out the (hopefully completed!) puzzle
        s.print();
    }
}

