import java.util.ArrayList;

public class Node implements Comparable{

    // Properties of node
    private Node parent;
    private State state;
    private int pathCost;
    private int depth;
    private String direction;
    // Heuristic cost (to finish the node)
    private int heuristic;

    // Constructors
    public Node(Node parent, State state, String direction){
        this.parent = parent;
        this.state = state;
        if(parent != null){
            this.pathCost = parent.pathCost + 2;
            this.depth = parent.depth + 1;
        }
        this.direction = direction;
    }

    public Node(State state){
        this.state = state;
        this.depth = 0;
        this.pathCost = 0;
    }

    // Getter functions
    public Node getParent(){
        return parent;
    }

    public State getState(){
        return state;
    }

    public int getDepth(){
        return depth;
    }

    public int getCost(){
        return pathCost;
    }


    // Possible moves
    public ArrayList<Node> checkMoves(){
        // Array that will contain possible moves
        ArrayList<Node> moves = new ArrayList<Node>();
        // Moves
        boolean up = state.agent.getYPos() > 0;
        boolean down = state.agent.getYPos() < state.gridDimension - 1;
        boolean left = state.agent.getXPos() > 0;
        boolean right = state.agent.getXPos() < state.gridDimension - 1;
        // Feasible moves
        boolean[] canMoveTo = {up, down, left, right};
        // Arrow character pointing the direction
        String[] aDirection = {"\u2191", "\u2193", "\u2190", "\u2192"};

        int idx = 0;
        for(boolean isMove : canMoveTo){
            if(isMove){
                // Getting coordinates from blocks and creating the temporal state
                int[] bA = {this.state.a.getXPos(),this.state.a.getYPos()};
                int[] bB = {this.state.b.getXPos(),this.state.b.getYPos()};
                int[] bC = {this.state.c.getXPos(),this.state.c.getYPos()};
                int[] bAg = {this.state.agent.getXPos(),this.state.agent.getYPos()};
                int[][] coors = {bA, bB, bC, bAg};
                State stateTemp = new State(coors, this.state.gridDimension);
                // Creating temporal node
                Node temp = new Node(this, stateTemp, aDirection[idx]);
                // Moving the temporal node through the directions
                switch (idx){
                    case 0: temp.state.moveAgent(Move.UP);
                        break;
                    case 1: temp.state.moveAgent(Move.DOWN);
                        break;
                    case 2: temp.state.moveAgent(Move.LEFT);
                        break;
                    case 3: temp.state.moveAgent(Move.RIGHT);
                        break;
                }
                // Adding node to the list of possible moves
                moves.add(temp);
            }
            idx++;
        }
        return moves;
    }


    // Equals and HashCode methods are automatically generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        if (state != null ? !state.equals(node.state) : node.state != null) return false;
        return true;
    }
    @Override
    public int hashCode() {
        int result = (state != null ? state.hashCode() : 0);
        return result;
    }


    // Directions or path taken from the start to the solution
    public String[] displaySolution(){
        Node node = this;
        ArrayList<String> moves = new ArrayList<String>();

        // While parent isn't null, add direction from parent to this node
        while(node.parent != null){
            moves.add(0,node.direction);
            node = node.parent;
        }
        // Returning the resultant path
        String[] result = (String[]) moves.toArray(new String[moves.size()]);
        return result;
    }


    // Heuristic cost from current node to final node
    public void getCostEstimate(Node target){
        // Calculating Manhattan distance for each block
        int aCostX = Math.abs(target.getState().a.getXPos() - getState().a.getXPos());
        int aCostY = Math.abs(target.getState().a.getYPos() - getState().a.getYPos());
        int bCostX = Math.abs(target.getState().b.getXPos() - getState().b.getXPos());
        int bCostY = Math.abs(target.getState().b.getYPos() - getState().b.getYPos());
        int cCostX = Math.abs(target.getState().c.getXPos() - getState().c.getXPos());
        int cCostY = Math.abs(target.getState().c.getYPos() - getState().c.getYPos());

        // Sum of distances plus depth
        this.heuristic = aCostX + aCostY + bCostX + bCostY + cCostX + cCostY + getDepth();
    }


    // Flags of the comparison indicating the 3 possible states
    public int compareTo(Object node){
        if(heuristic < ((Node) node).heuristic) return -1;
        else if(heuristic == ((Node) node).heuristic) return 0;
        else return 1;
    }
}
