public class State {
    // Properties
    private Block[][] blocks;
    public int gridDimension;
    // Actual maximum dimension - {gridDimension -1}
    private int maxDim;
    public Block agent, a, b, c;


    public State(int[][] coordinates, int boardSize) throws IllegalArgumentException{

        // Setting dimensions
        this.gridDimension = boardSize;
        this.maxDim = boardSize - 1;

        // Validate coordinates
        validate(coordinates, maxDim);

        // Inserting blocks, agent and filling with empty tiles
        blocks = new Block[gridDimension][gridDimension];
        a = new Block("A", coordinates[0]);
        b = new Block("B", coordinates[1]);
        c = new Block("C", coordinates[2]);
        agent = new Block("Ag", coordinates[3]);

        for(int i = 0; i < gridDimension; i++){
            for(int j = 0; j < gridDimension; j++){
                int[] coors = {i, j};
                setPosition( coors, new Block("Empty", coors) );
            }
        }

        setPosition(coordinates[0], a);
        setPosition(coordinates[1], b);
        setPosition(coordinates[2], c);
        setPosition(coordinates[3], agent);
    }


    // Setting position of a block
    public void setPosition(int[] coors, Block block){
        blocks[ coors[0] ] [ coors[1] ] = block;
    }


    // Return the coordinates of the blocks
    public String toString(){
        return "The blocks are in: A - (" + a.getXPos() + "," + a.getYPos() + ")" +
                ", B - (" + b.getXPos() + "," + b.getYPos() + ")" +
                ", C - (" + c.getXPos() + "," + c.getYPos() + ")" +
                ", Ag - (" + agent.getXPos() + "," + agent.getYPos() + ")";
    }


    // Comparing current state vs goal state in each block
    public boolean compareTo(State state){
        return (this.a.getPosition().equals(state.a.getPosition())
                && this.b.getPosition().equals(state.b.getPosition())
                && this.c.getPosition().equals(state.c.getPosition())
                && this.agent.getPosition().equals(state.agent.getPosition()));
    }


    // Comparing current state vs goal state in each block, the agent could be in any place at the end
    public boolean equalToGoal(State goal){
        return (this.a.getPosition().equals(goal.a.getPosition())
                && this.b.getPosition().equals(goal.b.getPosition())
                && this.c.getPosition().equals(goal.c.getPosition()));
    }


    // Method that returns a flag that indicates if the move is posible
    public boolean moveAgent(Move move) throws IllegalArgumentException {

        switch(move){
            case UP:
                if(agent.getYPos() == 0) return false;
                else{
                    upDownProcedure(move);
                    return true;
                }
            case DOWN:
                if(agent.getYPos() == maxDim) return false;
                else{
                    upDownProcedure(move);
                    return true;
                }
            case LEFT:
                if(agent.getXPos() == 0) return false;
                else{
                    leftRightProcedure(move);
                    return true;
                }
            case RIGHT:
                if(agent.getXPos() == maxDim) return false;
                else{
                    leftRightProcedure(move);
                    return true;
                }
        }
        return false;

    }


    // Shifting positions for UP and DOWN cases
    public void upDownProcedure(Move move){
        int newYPos = move == Move.UP ? agent.getYPos() - 1 : agent.getYPos() + 1;
        Block old = blocks[agent.getXPos()][newYPos];
        blocks[agent.getXPos()][agent.getYPos()] = old;
        old.setYPos(agent.getYPos());
        blocks[agent.getXPos()][newYPos] = agent;
        agent.setYPos(newYPos);
    }


    // Shifting positions for LEFT and RIGHT cases
    public void leftRightProcedure(Move move){
        int newXPos = move == Move.LEFT ? agent.getXPos() - 1 : agent.getXPos() + 1;
        Block old = blocks[newXPos][agent.getYPos()];
        blocks[agent.getXPos()][agent.getYPos()] = old;
        old.setXPos(agent.getXPos());
        blocks[newXPos][agent.getYPos()] = agent;
        agent.setXPos(newXPos);
    }


    //Validation function
    public void validate(int[][] coor, int maxDim){
        int[] cA = coor[0];
        int[] cB = coor[1];
        int[] cC = coor[2];
        int[] cAg = coor[3];
        // Validating boundaries
        for(int[] coorBlock : coor){
            for(int position : coorBlock){
                if (position > maxDim || position < 0) {
                    throw new IllegalArgumentException("Error. The minimum x,y position for a tile is 0 and the maximum is " + maxDim);
                }
            }
        }
        // Validating different positions in blocks
        if( (cA == cB) || (cB == cC) || (cA == cC) ){
            throw new IllegalArgumentException("Error. Different coordiantes have to be specified for the blocks");
        }
        // Validating different positions in Agent
        else if( (cA == cAg) || (cB == cAg) || (cC == cAg) ){
            throw new IllegalArgumentException("Error. Agent's coordinates have to differ from blocks");
        }
    }


    // Equals and HashCode methods are automatically generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        if (gridDimension != state.gridDimension) return false;
        if (maxDim != state.maxDim) return false;
        if (a != null ? !a.equals(state.a) : state.a != null) return false;
        if (agent != null ? !agent.equals(state.agent) : state.agent != null) return false;
        if (b != null ? !b.equals(state.b) : state.b != null) return false;
        if (c != null ? !c.equals(state.c) : state.c != null) return false;
        return true;
    }
    @Override
    public int hashCode() {
        int result = gridDimension;
        result = 31 * result + maxDim;
        result = 31 * result + (agent != null ? agent.hashCode() : 0);
        result = 31 * result + (a != null ? a.hashCode() : 0);
        result = 31 * result + (b != null ? b.hashCode() : 0);
        result = 31 * result + (c != null ? c.hashCode() : 0);
        return result;
    }
}