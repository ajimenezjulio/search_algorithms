import java.io.PrintStream;
import java.util.*;


public class Board {

    PrintStream out;
    Node initial;
    Node end;
    // Count the number of moves performed by IDS search
    int totalIDSMoves;

    // Taking the coordinates and creating the Nodes for the initial and end
    public Board(int[][] coordinates, int boardSize, PrintStream out) throws Exception {
        int[][] c = coordinates;
        int[][] initialC = { {c[0][0],c[0][1]}, {c[1][0],c[1][1]}, {c[2][0],c[2][1]}, {c[3][0],c[3][1]} };
        int[][] finalC = { {c[0][2],c[0][3]}, {c[1][2],c[1][3]}, {c[2][2],c[2][3]}, {c[3][2],c[3][3]} };
        initial = new Node(new State(initialC, boardSize));
        end = new Node(new State(finalC, boardSize));
        this.out = out;
    }

    // Breadth First Search
    public Node bfsSearch(Node start, Node end) throws Exception{
        // Saving the time at the beginning
        long timeStart = System.currentTimeMillis();

        // List containing nodes to analyse - using LinkedList data structure
        Queue queue = new LinkedList();
        // List containing nodes visited
        HashSet set = new HashSet();
        // Adding start node
        queue.add(start);
        set.add(start);

        while(!queue.isEmpty()){
            // Taking first node to analyse
            Node node = (Node) queue.poll();

            // If the node matches the final state, we finish the search and print reusults
            if(node.getState().compareTo(end.getState())){
                double timeEnd = (System.currentTimeMillis() - timeStart);
                printResults("BFS", start, node, timeEnd, set);
                return node;
            }

            // Getting possible moves
            ArrayList<Node> possibleMoves = node.checkMoves();

            // If there is a neighbour not visited yet, add to both queue and set
            for(Node n : possibleMoves){
                if (!set.contains(n)){
                    set.add(n);
                    queue.add(n);
                }
            }
        }

        // If nothing was found
        System.out.println("Search failed. Error while running BFS!");
        return null;
    }


    // Depth First Search
    public Node dfsSearch(Node start, Node end) throws Exception{
        long timeStart = System.currentTimeMillis();
        // List containing nodes to analyse - using Stack data structure
        Stack stack = new Stack();
        HashSet set = new HashSet();
        stack.push(start);
        set.add(start);

        while(!stack.isEmpty()){
            Node node = (Node) stack.pop();

            // If the node matches the final state, we finish the search and print reusults
            if(node.getState().compareTo(end.getState())){
                double timeEnd = (System.currentTimeMillis() - timeStart);
                printResults("DFS", start, node, timeEnd, set);
                return node;
            }

            ArrayList<Node> possibleMoves = node.checkMoves();

            for(Node n : possibleMoves){
                if (!set.contains(n)){
                    set.add(n);
                    stack.push(n);
                }
            }
        }

        System.out.println("Search failed. Error while running DFS!");
        return null;
    }


    // Heuristic A* Search
    public Node heuristicSearch(Node start, Node end) throws Exception{
        long timeStart = System.currentTimeMillis();
        // List containing nodes to analyse - using PriorityQueue data structure
        Queue queue = new PriorityQueue();
        HashSet set = new HashSet();
        // Creating cost estimate
        start.getCostEstimate(end);
        queue.add(start);

        while(!queue.isEmpty()){
            Node node = (Node) queue.poll();

            if(node.getState().compareTo(end.getState())){
                double timeEnd = (System.currentTimeMillis() - timeStart);
                printResults("A*", start, node, timeEnd, set);
                return node;
            }

            set.add(node);
            ArrayList<Node> possibleMoves = node.checkMoves();

            for(Node n : possibleMoves){
                if (!set.contains(n) && !queue.contains(n)){
                    n.getCostEstimate(end);
                    queue.add(n);
                }
            }
        }
        System.out.println("Search failed. Error while running A*!");
        return null;
    }


    // Iterative Deepening Search
    public Node idsSearch(Node start, Node finish, Node end, double time) throws Exception{

        long timeStart = System.currentTimeMillis();
        // Initial depth of 1
        int depth = 1;
        // Creating null node
        Node n = null;

        if(end == null){
            while(n == null){
                // Perform DFS with max depth
                n = performIDS(start, finish, depth, timeStart);
                depth++;
            }
        }
        else{
            //when match found, print to output the results
            //System.out.println("IDS Solution path: " + Arrays.toString(end.displaySolution()));
            out.println("IDS" + "," + start.getState().gridDimension + "," + end.getDepth() + "," + totalIDSMoves + "," + time);
        }
        return n;
    }

    //DFS with limited depth (iterative deepening)
    public Node performIDS(Node start, Node finish, int depth, long time) throws Exception{
        // List containing nodes to analyse - using Stack data structure
        Stack stack = new Stack();
        // Storing nodes and depths - using Map data structure
        Map<Node,Integer> map = new HashMap<Node, Integer>();
        stack.push(start);
        map.put(start, start.getDepth());

        while(!stack.isEmpty()){
            Node node = (Node) stack.pop();

            if(node.getState().compareTo(finish.getState())){
                double endTime = (System.currentTimeMillis() - time);
                idsSearch(start, finish, node, endTime);
                return node;
            }

            // If the node is at smaller depth than initial target maximum depth, check it in the map and add it if succeed
            if(depth > node.getDepth()){
                ArrayList<Node> possibleMoves = node.checkMoves();
                for(Node n : possibleMoves){
                    if (!map.containsKey(n) || map.get(n) >= n.getDepth()){
                        map.put(n, n.getDepth());
                        stack.push(n);
                    }
                }
            }
        }

        // Adding visited nodes
        totalIDSMoves += map.size();
        return null;
    }


    // Printing the results in console
    public void printResults(String search, Node start, Node initial, double srcTime, Set set) throws Exception{
        //System.out.println(search + " Solution path: " + Arrays.toString(initial.displaySolution()));
        out.println(search + "," + initial.getState().gridDimension + "," + initial.getDepth() + "," + set.size() + "," + srcTime);
    }
}
