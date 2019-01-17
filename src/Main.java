import java.io.File;
import java.io.PrintStream;

public class Main {

    // Enabling search types     {DFS, BFS, IDS, HEUR}
    static boolean[] searches = {true, true, true, true};

    public static void main(String[] args) {
        try{
            // Results File
            PrintStream out = new PrintStream(new File("SearchResultsAgent.csv"));
            // Creating boards from 4x4 to 20x20
            for(int i = 4; i <= 20; i++){
                createBoard(i, out);
            }
        }
        // Catching if there was an error
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }


    // Creating the tiles board and giving it the initial and final state
    public static void createBoard(int size,PrintStream out) throws Exception{
        // Start and Final Coordinates in each block and Agent {start x, start y, final x, final y}
        int lastBlock = size - 1;
        int[] blockA = {0, lastBlock, 1, (size - 3)};
        int[] blockB = {1, lastBlock, 1, (size - 2)};
        int[] blockC = {2, lastBlock, 1, lastBlock};
        int[] blockAg = {lastBlock, lastBlock, lastBlock, lastBlock};

        int[][] coordinates = {blockA, blockB, blockC, blockAg};
        Board temp = new Board(coordinates, size, out);

        // Executing searches
        try{
            performSearches(temp,size);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }


    public static void performSearches(Board world, int size) throws Exception{

        System.out.println("\n\n*****  SEARCHING IN " + size + "x" + size + "BOARD  *****");
        int idx = 0;
        for(boolean search : searches){
            String name = "";

            if (search) {
                // Choosing search method {0 - DFS, 1 - BFS, 2 - IDS, 3 - HEUR}
                try{
                    switch(idx){
                        case 0: world.dfsSearch(world.initial, world.end);
                            name = "DFS";
                            break;
                        case 1: world.bfsSearch(world.initial, world.end);
                            name = "BFS";
                            break;
                        case 2: world.idsSearch(world.initial, world.end, null, 0);
                            name = "IDS";
                            break;
                        case 3: world.heuristicSearch(world.initial, world.end);
                            name = "HEUR";
                            break;
                    }
                }
                // Out of memory exception
                catch(OutOfMemoryError outMemory){
                    System.out.println( name + " search failed for " + size + "x" + size + " board. Out of memory error");
                    searches[idx] = false;
                }
                // Any other exception
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            idx++;
        }
    }

}