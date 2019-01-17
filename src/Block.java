public class Block {
    // Block properties
    private String name;
    private int yPos;
    private int xPos;


    // Constructor
    public Block(String name, int[] coors){
        this.name = name;
        this.xPos = coors[0];
        this.yPos = coors[1];
    }


    // Getter functions
    public String getName(){
        return this.name;
    }

    public int getYPos(){
        return this.yPos;
    }

    public int getXPos(){
        return this.xPos;
    }

    public String getPosition(){
        return "(" + this.xPos + "," + this.getYPos() + ")";
    }


    //Setter functions
    public void setXPos(int newXPos){
        this.xPos = newXPos;
    }

    public void setYPos(int newYPos){
        this.yPos = newYPos;
    }


    // Equals and HashCode methods are automatically generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        if (xPos != block.xPos) return false;
        if (yPos != block.yPos) return false;
        if (!name.equals(block.name)) return false;
        return true;
    }
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + yPos;
        result = 31 * result + xPos;
        return result;
    }
}
