import java.awt.Point;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

enum TileValue{
	UNKNOWN,
	POTENTIAL_FOOD,
	FOOD,
	WALL,
	ROCK,
	KEY,
	HAMMER,
	DOOR,
	AGENT,
	NOTHING
}

public class MemoryMap {
	public static final char UNKNOWN_CHAR        = 'u';
	static final char POTENTIAL_FOOD_CHAR = 'p';
	static final char FOOD_CHAR           = '+';
	static final char WALL_CHAR 		  = '*';
	static final char ROCK_CHAR           = '@';
	static final char DOOR_CHAR           = '#';
	static final char KEY_CHAR 			  = 'K';
	static final char HAMMER_CHAR         = 'T';
	static final char AGENT_CHAR          = '0';
	
	private TileValue[][] map;
	private int xDomain;
	private int yDomain;
	
	public MemoryMap(int x, int y){
		this.xDomain = x;
		this.yDomain = y;
		map = new TileValue[xDomain][yDomain];
		setMapUnknown();
	}
	
	private void setMapUnknown() { 
		for (Tile t : this.getAllTiles())
			setTile(t.getX(), t.getY(), TileValue.UNKNOWN);
	}
	
	public int getXDomain() {
		return this.xDomain;
	}
	
	public int getYDomain() {
		return this.yDomain;
	}
	
	public void clearMemory(){
		for(int x = 0; x < xDomain; ++x){
			for(int y = 0; y < yDomain; ++y){
				setTile(x, y, TileValue.UNKNOWN);
			}
		}
	}
	
	public void setTile(Point p, TileValue v){
		setTile(p.x, p.y, v);
	}

	public void setTile(int x, int y, TileValue v){
		map[x][y] = v;
	}
	
	public void setTile(Point p, char c){
		setTile(p.x, p.y, c);
	}
	
	public void setTile(int x, int y, char c){
		setTile(x, y, getValueFromChar(c));
	}
	
	public TileValue getTileValue(Point p){
		return getTileValue(p.x, p.y);
	}
	public TileValue getTileValue(int x, int y){
		return map[x][y];
	}
	
	public Tile getTile(Point p) {
		return new Tile(p, getTileValue(p));
	}
	
	public Tile getTile(int x, int y){
		return new Tile(x, y, getTileValue(x, y));
	}
	
	public int getNumTiles(){
		return xDomain*yDomain;
	}
	
	public List<Tile> getAllTiles(){
		List<Tile> tiles = new LinkedList<Tile>();
		for(int x = 0; x < xDomain; ++x){
			for(int y = 0; y < yDomain; ++y){
				tiles.add(new Tile(x, y, map[x][y]));
			}
		}
		return tiles;
	}
	
	public TileValue getValueFromChar(char c){
		switch(c){
		case POTENTIAL_FOOD_CHAR:
			return TileValue.POTENTIAL_FOOD;
		case UNKNOWN_CHAR:
			return TileValue.UNKNOWN;
		case FOOD_CHAR:
			return TileValue.FOOD;
		case WALL_CHAR:
			return TileValue.WALL;
		case ROCK_CHAR:
			return TileValue.ROCK;
		case DOOR_CHAR:
			return TileValue.DOOR;
		case KEY_CHAR:
			return TileValue.KEY;
		case HAMMER_CHAR:
			return TileValue.HAMMER;
		case AGENT_CHAR:
			return TileValue.AGENT;
		default:
			return null;
		}
	}

	public void print() {
		for (int x=0; x<xDomain; ++x){
			for (int y=0; y<yDomain; ++y){
				new Tile(x, y, getTileValue(x, y)).print();
			}
			System.out.println();
		}
	}

    protected final Point visLocation        = new Point(5, 2);
    protected static final int visualRows    = 7;
    protected static final int visualColumns = 5;

    /**
     *
     * @param memoryMap
     * @param facingDirection
     * @param visualInformation
     * @return
     */

    public void updateMemoryMap(MaedenAgent agent, String visualInformation) {

        StringTokenizer visTokens = new StringTokenizer(visualInformation, "(", true);
        visTokens.nextToken();
        for (int r = visualRows-1; r >= 0; --r) {
            visTokens.nextToken();
            for (int c=0; c <= visualColumns-1; ++c) {
                visTokens.nextToken();
                String visChars = visTokens.nextToken();
                Point visTile = new Point(r,c);
                updateSightField(agent, visTile, visChars);
            }
        }
    }

    /**
     * WARNING: This method only returns the latter of two object if they are on the same spot
     * @param memoryMap
     * @param agent
     * @param visTile
     * @param visChars
     * @return
     */
    public void updateSightField(MaedenAgent agent, Point visTile, String visChars) {
        Point actual = visualToActual(agent, visTile);
        if(visChars.startsWith(")")) {
        	setTile(actual, TileValue.NOTHING);
        	return;
        }
        setTile(actual, visChars.toCharArray()[1]); 
    }

    /**
     * This method takes a location on the visual field map and translates that into an actual location on the memorymap
     * @param currentDirection
     * @param currentLocationRow
     * @param currentLocationColumn
     * @param visualLocationRow
     * @param visualLocationColumn
     * @return
     */
    public Point visualToActual(MaedenAgent agent, Point visTile){
        int columnDistance = visTile.x - this.visLocation.x;
        int rowDistance    = visTile.y - this.visLocation.y;

        switch (agent.getCompassDirection()){
            case NORTH:
                return new Point(agent.getX()+columnDistance, agent.getY()-rowDistance);
            case EAST :
                return new Point(agent.getX()+rowDistance, 	  agent.getY()+columnDistance);
            case SOUTH:
                return new Point(agent.getX()-columnDistance, agent.getY()+rowDistance);
            case WEST : //case 'w' :
                return new Point(agent.getX()-rowDistance,    agent.getY()-columnDistance);
            default: return null;
        }
    }


}
