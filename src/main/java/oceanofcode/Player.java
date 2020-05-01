package oceanofcode;

import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/

public class Player {
	public static class Zone {
		private int x1=0;
		
		private int y1=0;
		private int x2=0;
		private int y2=0;
		
		public Zone(int x1,int y1,int x2,int y2) {
			this.updateRange(x1,y1,x2,y2);
		}
		
		public Zone(int number) {
			if(number>0 && number<10) {
				x1=((number-1) % 3) * 5;
				y1=((number-1) / 3) * 5;
				x2=x1+4;
				y2=y1+4;
			} else {
				x1=0;
				y1=0;
				x2=0;
				y2=0;
			}
			
			this.updateRange(x1,y1,x2,y2);
		}
		
		public void displayRange() {
			System.err.println("Zone range is ("+x1+","+y1+")("+x2+","+y2+")");
		}
		
		public void updateRange(int x1,int y1,int x2,int y2) {
			this.x1=x1;
			this.y1=y1;
			this.x2=x2;
			this.y2=y2;
		}

		public int getX1() {
			return x1;
		}

		public void setX1(int x1) {
			this.x1 = x1;
		}

		public int getY1() {
			return y1;
		}

		public void setY1(int y1) {
			this.y1 = y1;
		}

		public int getX2() {
			return x2;
		}

		public void setX2(int x2) {
			this.x2 = x2;
		}

		public int getY2() {
			return y2;
		}

		public void setY2(int y2) {
			this.y2 = y2;
		}
		
		public int getWidth() {
			return(x2-x1+1);
		}
		
		public int getHeight() {
			return(y2-y1+1);
		}
	}

	public static class Position {
	    private int x=0;
	    private int y=0;
	    
	    public Position() {
	        this.x = 0;
	        this.y = 0;
	    }
	    
	    @Override
	    public boolean equals(Object object)
	    {
	        boolean same = false;

	        //System.err.println(this.x+" == "+((Position)object).x+" && "+this.y+" == "+((Position)object).y);
	        
	        if (object instanceof Position)
	        	if(this.x == ((Position)object).x && this.y == ((Position)object).y) same=true;

	        //System.err.println(same);
	        return same;
	    }
	    
	    @Override
	    public int hashCode() {
	    	return(x*y);
	    }
	    
	    public Position(int x, int y) {
	        this.x = x;
	        this.y = y;
	    }
	    
	    public Position(Position position) {
	        this.x = position.getX();
	        this.y = position.getY();
	    }
	    
	    public void setPosition(int newX, int newY) {
	        this.x = newX;
	        this.y = newY;
	    }
	    
	    public int getX() {
	        return x;
	    }
	    
	    public int getY() {
	        return y;
	    }
	    
	    public void setX(int newX) {
	        this.x=newX;
	    }
	    
	    public void setY(int newY) {
	        this.y=newY;
	    }
	    
	    public int getZone() {
	    	int value;
	    	if(x<5) {
	    		if(y<5) value=1;
	    		else if(y<10) value=4;
	    		else value=7;
	    	} else if(x<10) {
	    		if(y<5) value=2;
	    		else if(y<10) value=5;
	    		else value=8;
	    	} else if(y<5) value=3;
	    		else if(y<10) value=6;
	    		else value=9;
	    	
	    	return value;
	    }
	}

	public static class SearchPosition {
	    private int x1;
	    private int x2;
	    private int y1;
	    private int y2;
	    private Zone sonarActiveZone=null;
	    private ArrayList<Character> moves=new ArrayList<Character>();
	/*    private int sumN=0;
	    private int sumS=0;
	    private int sumE=0;
	    private int sumW=0;
	    private Zone estimatedPosition=null;*/
	    private ArrayList<Zone> excludeZone=new ArrayList<Zone>();
	    private int maxDepth=25;
	    
	    public SearchPosition(int width, int height) {
	    	x1=0;
	    	y1=0;
	    	x2=width-1;
	    	y2=height-1;
//	    	estimatedPosition=new Zone(x1,y1,x2,y2);
	    }
	    
	    public void torpedo(Position position, Map map, char direction) {
	    	int oppZoneX1=position.getX()-4;
	    	int oppZoneX2=position.getX()+4;
	    	int oppZoneY1=position.getY()-4;
	    	int oppZoneY2=position.getY()+4;
	    	
	    	int newZoneX1;
	    	int newZoneX2;
	    	int newZoneY1;
	    	int newZoneY2;
	    	
	    	if(oppZoneX1>=x1 && oppZoneX1<=x2) newZoneX1=oppZoneX1;
	    	else newZoneX1=x1;
	    	
	    	if(oppZoneX2>x2) newZoneX2=x2;
	    	else newZoneX2=oppZoneX2;
	    	
	    	if(oppZoneY1>=y1 && oppZoneY1<=y2) newZoneY1=oppZoneY1;
	    	else newZoneY1=y1;
	    	
	    	if(oppZoneY2>y2) newZoneY2=y2;
	    	else newZoneY2=oppZoneY2;
	    	
	    	x1=newZoneX1;
	    	x2=newZoneX2;
	    	y1=newZoneY1;
	    	y2=newZoneY2;
	    	
	        map.updateOppMap(this, String.valueOf(direction));
	    }
	    
	    public void zone(int zone, Map map, String sonarResult) {
	    	if(sonarResult.equals("Y")) {
			    if(x2-x1>4) {
		            x1=((zone-1) % 3) * 5;
		            x2=x1+4;
		        } else {
		            int xA=((zone-1) % 3) * 5;
		            int xB=xA+4;
		            if(x1>xA) x2=xB;
		            else x1=xA;
		        }
		        
		        if(y2-y1>4) {
		            y1=((zone-1) / 3) * 5;
		            y2=y1+4;
		        } else {
		            int yA=((zone-1) / 3) * 5;
		            int yB=yA+4;
		            if(y1>yA) y2=yB;
		            else y1=yA;
		        }
		        map.updateOppMap(this, "Z");
	    	} else {
	    		sonarActiveZone=new Zone(zone);
	    		map.updateOppMap(this, "Z"+zone);
	    	}
	    }
	    
	    public void touch(Position touch, Map map, int oppLooseLife) {
	    	if(oppLooseLife==2) {
	    		x1=touch.getX();
	    		y1=touch.getY();
	    		x2=x1;
	    		y2=y1;
	    	} else {
	    		if(x1>0) {
	            	if(x1<(touch.getX()-1)) x1=touch.getX()-1;
	            } else x1=0;
	            		
	            if(x2<(map.getWidth()-1)) {
	            	if(x2>(touch.getX()+1)) x2=touch.getX()+1;
	            } else x2=map.getWidth()-1;
	            
	            if(y1>0) {
	            	if(y1<(touch.getY()-1)) y1=touch.getY()-1;
	            } else y1=0;
	            		
	            if(y2<(map.getHeight()-1)) {
	            	if(y2>(touch.getY()+1)) y2=touch.getY()+1;
	            } else y2=map.getHeight()-1;
	    	}
	        
	        map.updateOppMap(this, "T");
	    }
	    
	    public void miss(Position miss, Map map) {
	    	int x1, x2, y1, y2;
	    	
	    	if(miss.getX()>0) x1=miss.getX()-1;
	    	else x1=0;
	    	
	    	if(miss.getY()>0) y1=miss.getY()-1;
	    	else y1=0;
	    	
	    	if(miss.getX()<map.getWidth()-1) x2=miss.getX()+1;
	    	else x2=map.getWidth()-1;
	    	
	    	if(miss.getY()<map.getHeight()-1) y2=miss.getY()+1;
	    	else y2=map.getHeight()-1;
	    	
	    	excludeZone.add(new Zone(x1,y1,x2,y2));
	        
	        map.updateOppMap(this, "M");
	    }
	    
	    public boolean backTrackingPossible(Position position, Map map) {
	    	boolean value=true;
	    	Position testPosition=new Position(position);
	    	int limitDepth;
	    	
	    	if(moves.size()<=maxDepth) limitDepth=-1;
	    	else limitDepth=moves.size()-maxDepth;
	    	
	    	for(int i=moves.size()-1;i>limitDepth;i--) {
	    		switch(moves.get(i)) {
					case 'N':
						if(map.south(testPosition, "Initial")=='x') {
							value=false;
							i=0;
						} else testPosition.setY(testPosition.getY()+1);
						break;
					case 'S':
						if(map.north(testPosition, "Initial")=='x') {
							value=false;
							i=0;
						} else testPosition.setY(testPosition.getY()-1);
						break;
					case 'E':
						if(map.west(testPosition, "Initial")=='x') {
							value=false;
							i=0;
						} else testPosition.setX(testPosition.getX()-1);
						break;
					case 'W':
						if(map.east(testPosition, "Initial")=='x') {
							value=false;
							i=0;
						} else testPosition.setX(testPosition.getX()+1);
						break;
	    		}
	    	}
	    			
	    	return value;
	    }
	    
	    public boolean movesArePossible(Position position, Map map) {
	    	boolean value=true;
	    	Position testPosition=new Position(position);
	    	
	    	for(int i=0;i<moves.size();i++) {
	    		switch(moves.get(i)) {
				case 'N':
					if(map.north(testPosition, "Initial")=='x') {
						value=false;
						i=moves.size();
					} else testPosition.setY(testPosition.getY()-1);
					break;
				case 'S':
					if(map.south(testPosition, "Initial")=='x') {
						value=false;
						i=moves.size();
					} else testPosition.setY(testPosition.getY()+1);
					break;
				case 'E':
					if(map.east(testPosition, "Initial")=='x') {
						value=false;
						i=moves.size();
					} else testPosition.setX(testPosition.getX()+1);
					break;
				case 'W':
					if(map.west(testPosition, "Initial")=='x') {
						value=false;
						i=moves.size();
					} else testPosition.setX(testPosition.getX()-1);
					break;
	    		}
	    	}
	    			
	    	return value;
	    }
	    
	    public void deleteMoves() {
	    	moves.clear();
	    }
	    
	    public void direction(char direction, Map map) {
	    	switch(direction) {
	            case 'N':
	            	moves.add(direction);
	                if(y1>0) y1--;
	                else y1=0;
	                
	                y2--;
	                break;
	                
	            case 'S':
	            	moves.add(direction);
	                if(y2<(map.getHeight()-1)) {
	                	y2++;
	                }
	                else y2=map.getHeight()-1;
	                
	                y1++;
	                break;
	            
	            case 'E':
	            	moves.add(direction);
	                if(x2<(map.getWidth()-1)) {
	                	x2++;
	                }
	                else x2=map.getWidth()-1;
	                
	                x1++;
	                break;
	                
	            case 'W':
	            	moves.add(direction);
	                if(x1>0) x1--;
	                else x1=0;
	                
	                x2--;
	                break;
	        }
	        
	    	System.err.println("DEBUG Range oppMap - position range:");
	        displayPositionRange();
	    	
	        map.updateOppMap(this, String.valueOf(direction));
	        
	        Zone rangeOppMapPosition=map.getRangeOfValue(0,"Opponent");
	        System.err.println("DEBUG - Range oppMap :");
	        rangeOppMapPosition.displayRange();
	        
	        boolean updateAgain=false;
	        
	        if(x1<rangeOppMapPosition.getX1()) {
	        	x1=rangeOppMapPosition.getX1();
	        	updateAgain=true;
	        }
	        if(x2>rangeOppMapPosition.getX2()) {
	        	x2=rangeOppMapPosition.getX2();
	        	updateAgain=true;
	        }
	        if(y1<rangeOppMapPosition.getY1()) {
	        	y1=rangeOppMapPosition.getY1();
	        	updateAgain=true;
	        }
	        if(y2>rangeOppMapPosition.getY2()) {
	        	y2=rangeOppMapPosition.getY2();
	        	updateAgain=true;
	        }
	        
	        if(updateAgain) map.updateOppMap(this, String.valueOf('X'));
	    }
	    
	    public void silence(Map map) {
	    	int distance=4; //4 for a maximum distance in SILENCE
	    	
	    	moves.clear();
	        if(x1>=distance) x1-=distance; else x1=0;
	        if(y1>=distance) y1-=distance; else y1=0;
	        if(x2<(map.getWidth()-1-distance)) x2+=distance; else x2=map.getWidth()-1; 
	        if(y2<(map.getHeight()-1-distance)) y2+=distance; else y2=map.getHeight()-1;
	        
	        map.updateOppMap(this, "U");
	    }
	    
	    public void displayPositionRange() {
	        System.err.println("positionRange=(" + x1 + "," + y1 + ")(" + x2 + "," + y2 + ")");
	    }
	    
	    public void displayBackTrackingDepth() {
	    	System.err.println("Back tracking depth="+moves.size());
	    }
	    
	    public int getX1() {
	        return x1;
	    }
	    
	    public int getX2() {
	        return x2;
	    }
	    
	    public int getY1() {
	        return y1;
	    }
	    
	    public int getY2() {
	        return y2;
	    }
	    
	    public Zone getSonarActiveZone() {
	        return sonarActiveZone;
	    }
	    
	    public ArrayList<Zone> getExcludeZone() {
	        return excludeZone;
	    }
	    
	    public double getAccuracy(Map map) {
	    	double value=0;
	        int count=1;
	        
	        for(int y=0;y<map.getHeight();y++) {
	            for (int x=0;x<map.getWidth();x++) {
	            	if(map.getCellValue(x, y, "Opponent")>0) count++;
	            }
	        }
	        value=(double)count/(map.getWidth()*map.getHeight())*100;
	        return value;
	    }
	}

	public static class Submarine {
	    Position myPosition = new Position(0,0);
	    private int myLife = 0;
	    private int oppLife = 0;
	    private int torpedoCooldown = 0;
	    private int sonarCooldown = 0;
	    private int silenceCooldown = 0;
	    private int mineCooldown = 0;
	    private int countMine=0;
	    private char myDirection = 'N';
	    private int sonarZone=0;
	    private Position lastTarget=null;
	    private Random random=new Random();
	    
	    public Submarine(int x, int y, int myLife, int oppLife, int torpedo, int sonar, int silence, int mine) {
	        update(x,y,myLife,oppLife,torpedo,sonar,silence,mine);
	    }  
	    
	    public void update(int x, int y, int myLife, int oppLife, int torpedo, int sonar, int silence, int mine) {
	    	this.myPosition.setPosition(x,y);
	        
	        this.myLife=myLife;
	        this.oppLife=oppLife;
	        this.torpedoCooldown=torpedo;
	        this.sonarCooldown=sonar;
	        this.silenceCooldown=silence;
	        this.mineCooldown=mine;
	    }  
	        
	    public void displayInfo() {
	        System.err.println("pos = (" + myPosition.getX() + ',' + myPosition.getY() + ")");
	        System.err.println("myLife = " + myLife);
	        System.err.println("oppLife = " + oppLife);
	        System.err.println("torpedoCooldown = " + torpedoCooldown);
	        System.err.println("sonarCooldown = " + sonarCooldown);
	        System.err.println("silenceCooldown = " + silenceCooldown);
	        System.err.println("mineCooldown = " + mineCooldown);
	        System.err.println("myDirection = " + myDirection);
	        System.err.println("sonarZone = " + sonarZone);
	    }
	    
	    public void setDirection(char direction) {
	        myDirection=direction;
	    }
	    
	    public void setLastTarget(Position position) {
	        lastTarget=position;
	    }
	    
	    public Position getLastTarget() {
	        return lastTarget;
	    }
	    
	    public void setSonarZone(int n) {
	        sonarZone=n;
	    }
	    
	    public int getSonarZone() {
	        return sonarZone;
	    }
	    
	    public int getCountMine() {
	        return countMine;
	    }
	    
	    public void setCountMine(int value) {
	        countMine=value;
	    }
	    
	    public void setTorpedoCooldown(int n) {
	        torpedoCooldown=n;
	    }
	    
	    public int getTorpedoCooldown() {
	        return torpedoCooldown;
	    }
	    
	    public void setMineCooldown(int n) {
	        mineCooldown=n;
	    }
	    
	    public int getMineCooldown() {
	        return mineCooldown;
	    }
	    
	    public void setSonarCooldown(int n) {
	        sonarCooldown=n;
	    }
	    
	    public int getSonarCooldown() {
	        return sonarCooldown;
	    }
	    
	    public void setSilenceCooldown(int n) {
	        silenceCooldown=n;
	    }
	    
	    public int getSilenceCooldown() {
	        return silenceCooldown;
	    }
	    
	    
	    public void setRandomDirection() {
	        char tabDirection[]={'N','E','S','W'};
	        
	        myDirection=tabDirection[random.nextInt(4)];
	    }
	    
	    public char getDirection() {
	        return myDirection;
	    }
	    
	    public int getMyLife() {
	        return myLife;
	    }
	    
	    public int getOppLife() {
	        return oppLife;
	    }
	    
	    public Position getPosition() {
	        return myPosition;
	    }
	    
	    public char getNextDirectionV2(Map map) {
	        char newDirection=myDirection;
	        int cellN=map.north(myPosition,"Computed");
	        int cellS=map.south(myPosition,"Computed");
	        int cellE=map.east(myPosition,"Computed");
	        int cellW=map.west(myPosition,"Computed");

	        int cells[]={cellN,cellS,cellE,cellW};
	        
	        int i=0;
	        int minValue=1;
	        int direction=-1;
	        int intDir=0;
	        
	        switch(myDirection) {
		        case 'N':
		        	intDir=0;
		    		break;
		        case 'S':
		        	intDir=1;
		    		break;
		        case 'E':
		        	intDir=2;
		    		break;
		        case 'W':
		        	intDir=3;
		    		break;
	        }
	        
	        if(cells[intDir]>minValue) newDirection=myDirection;
	        else {
	        	//map.displayMap("Computed");
	            while(minValue<5 && direction==-1) {
	                while(i<4 && direction==-1) {
	                    if(cells[i]==minValue) {
	                        direction=i;
	                    }
	                    i++;
	                }
	                minValue++;
	                if(minValue<5) i=0;
	            }
	            
	            if(i==4 && minValue==5) newDirection='U';
	            else {
	                switch(direction) {
	                    case 0:
	                        newDirection='N';
	                        break;
	                    case 1: 
	                        newDirection='S';
	                        break;
	                    case 2: 
	                        newDirection='E';
	                        break;
	                    case 3: 
	                        newDirection='W';
	                        break;
	                }
	            }
	        }
	        
	        return newDirection;
	    }
	    
	    public char getNextDirection(Map map) {
	        char newDirection='N';
	        int cellN=map.north(myPosition,"Computed");
	        int cellS=map.south(myPosition,"Computed");
	        int cellE=map.east(myPosition,"Computed");
	        int cellW=map.west(myPosition,"Computed");

	        int cells[]={cellN,cellS,cellE,cellW};
	        
	        int i=0;
	        int minValue=1;
	        int direction=-1;
	        
	        //map.displayMap("Computed");
	        while(minValue<5 && direction==-1) {
	            while(i<4 && direction==-1) {
	                if(cells[i]==minValue) {
	                    direction=i;
	                }
	                i++;
	            }
	            minValue++;
	            if(minValue<5) i=0;
	        }
	        
	        if(i==4 && minValue==5) newDirection='U';
	        else {
	            switch(direction) {
	                case 0:
	                    newDirection='N';
	                    break;
	                case 1: 
	                    newDirection='S';
	                    break;
	                case 2: 
	                    newDirection='E';
	                    break;
	                case 3: 
	                    newDirection='W';
	                    break;
	            }
	        }
	        return newDirection;
	    }
	    
	    public void goTo(char direction, Map map) {
	        switch(direction) {
	            case 'N':
	                myPosition.setY(myPosition.getY()-1);
	                break;
	            case 'S': 
	                myPosition.setY(myPosition.getY()+1);
	                break;
	            case 'E': 
	                myPosition.setX(myPosition.getX()+1);
	                break;
	            case 'W': 
	                myPosition.setX(myPosition.getX()-1);
	                break;
	            case 'U':
	                map.initMap("Computed");
	                break;
	        }
	        
	        if(direction!='U') myDirection=direction;
	        map.setCellValue(this.myPosition.getX(), this.myPosition.getY(),"Computed", -1);
	        map.updateCellArroundPosition(this.myPosition);
	    }

	    
	/* First version of the path finder "Turn arround"   
	 * public void goNextDirection(Map map) {
	        char newDirection='N';
	        
	        if(myDirection == 'N') {
	            System.err.println("We try North");
	            if(map.north(myPosition)>0) {
	                myPosition.setY(myPosition.getY()-1);
	                newDirection='N';
	            } else {
	                System.err.println("North not possible, we try East");
	                if(map.east(myPosition)>0) {
	                    System.err.println("East="+map.east(myPosition));
	                    myPosition.setX(myPosition.getX()+1);
	                    newDirection='E';
	                } else {
	                    System.err.println("North and East not possible, we try West");
	                    if(map.west(myPosition)>0) {
	                        System.err.println("west="+map.west(myPosition));
	                        myPosition.setX(myPosition.getX()-1);
	                        newDirection='W';
	                    } else {
	                        System.err.println("North, East, West not possible, we try South");
	                        if(map.south(myPosition)>0) {
	                            System.err.println("south="+map.south(myPosition));
	                            myPosition.setY(myPosition.getY()+1);
	                            newDirection='S';
	                        } else newDirection='U';
	                    }
	                }
	            }    
	        }
	        
	        if(myDirection == 'E') {
	            System.err.println("We try East");
	            if(map.east(myPosition)>0) {
	                myPosition.setX(myPosition.getX()+1);
	                newDirection='E';
	            } else {
	                System.err.println("East not possible, we try South");
	                if(map.south(myPosition)>0) {
	                    myPosition.setY(myPosition.getY()+1);
	                    newDirection='S';
	                } else {
	                    System.err.println("East and South not possible, we try North");
	                    if(map.north(myPosition)>0) {
	                        myPosition.setY(myPosition.getY()-1);
	                        newDirection='N';
	                    } else {
	                        System.err.println("East, South and North not possible, we try West");
	                        if(map.west(myPosition)>0) {
	                            myPosition.setX(myPosition.getX()-1);
	                            newDirection='W';
	                        } else newDirection='U';
	                    }
	                }
	                
	            }
	        }
	        
	         if(myDirection == 'S') {
	            System.err.println("We try South");
	            if(map.south(myPosition)>0) {
	                myPosition.setY(myPosition.getY()+1);
	                newDirection='S';
	            } else {
	                System.err.println("South not possible, we try West");
	                if(map.west(myPosition)>0) {
	                    myPosition.setX(myPosition.getX()-1);
	                    newDirection='W';
	                } else {
	                    System.err.println("South and West not possible, we try East");
	                    if(map.east(myPosition)>0) {
	                        myPosition.setX(myPosition.getX()+1);
	                        newDirection='E';
	                    } else {
	                        System.err.println("South, West and East not possible, we try North");
	                        if(map.north(myPosition)>0) {
	                            myPosition.setY(myPosition.getY()-1);
	                            newDirection='N';
	                        } else newDirection='U';
	                    }
	                }
	            }
	        }
	        
	        if(myDirection == 'W') {
	            System.err.println("We try West");
	            if(map.west(myPosition)>0) {
	                myPosition.setX(myPosition.getX()-1);
	                newDirection='W';
	            } else {
	                System.err.println("West not possible, we try North");
	                if(map.north(myPosition)>0) {
	                    myPosition.setY(myPosition.getY()-1);
	                    newDirection='N';
	                } else {
	                    System.err.println("West and North not possible, we try South");
	                    if(map.south(myPosition)>0) {
	                        myPosition.setY(myPosition.getY()+1);
	                        newDirection='S';
	                    } else {
	                        System.err.println("West, North and South not possible, we try East");
	                        if(map.east(myPosition)>0) {
	                            myPosition.setX(myPosition.getX()+1);
	                            newDirection='E';
	                        } else newDirection='U';
	                    }
	                }
	            }
	        }
	        
	        myDirection=newDirection;
	        map.setPosition(this.myPosition,'s');
	        System.err.println("myDirection="+myDirection);
	    }
	*/
	    public String getMineDirection(Position position, Map map) {
	        int cellN=map.north(myPosition,"Initial");
	        int cellS=map.south(myPosition,"Initial");
	        int cellE=map.east(myPosition,"Initial");
	        int cellW=map.west(myPosition,"Initial");
	        ArrayList<Character> directions = new ArrayList<Character>();
	        String direction="";
	        
	        if((char)cellN!='x' && map.north(myPosition,"Mines")==0) directions.add('N');
	        if((char)cellS!='x' && map.south(myPosition,"Mines")==0) directions.add('S');
	        if((char)cellE!='x' && map.east(myPosition,"Mines")==0) directions.add('E');
	        if((char)cellW!='x' && map.west(myPosition,"Mines")==0) directions.add('W');
	        
	        if(directions.size()==0) {
	            System.err.println("No position found to send mine");
	            direction="";
	        } else {
	            int index=0;
	            if(directions.size()>1) index=random.nextInt(directions.size()-1);
	            
	            direction=directions.get(index).toString();
	            switch(direction) {
	            	case "N":
	            		map.setCellValue(position.getX(), position.getY()-1, "Mines", 1);
	            		break;
	            	case "S":
	            		map.setCellValue(position.getX(), position.getY()+1, "Mines", 1);
	            		break;
	            	case "E":
	            		map.setCellValue(position.getX()+1, position.getY(), "Mines", 1);
	            		break;
	            	case "W":
	            		map.setCellValue(position.getX()-1, position.getY(), "Mines", 1);
	            		break;
	            }
	            
	            countMine++;
	            System.err.println("Mine possible on "+ directions.size()+ "directions, we choose "+direction+" from ("+position.getX()+","+position.getY()+")");        
	        }
	        return direction;
	    }
	    
	    public void sendMine(Position position, Map map, String direction) {
	        switch(direction) {
	            case "N":
	                map.setCellValue(position.getX(), position.getY()-1, "Mines", 1);
	                break;
	            case "S":
	                map.setCellValue(position.getX(), position.getY()+1, "Mines", 1);
	                break;
	            case "E":
	                map.setCellValue(position.getX()+1, position.getY(), "Mines", 1);
	                break;
	            case "W":
	                map.setCellValue(position.getX()-1, position.getY(), "Mines", 1);
	                break;
	        }
	        
	    }
	    
	    public Position getTorpedoTargetBeforeMove(SearchPosition searchPosition, Map map, char direction) {
	        ArrayList <Position> targets=new ArrayList<Position>();
	        ArrayList <Position> tmpTargets=new ArrayList<Position>();
	        ArrayList <Position> forbidenTarget=new ArrayList<Position>();
	       
	        Position ciblePosition=new Position();
	        
	        for(int x=-1;x<2;x++) {
	        	for(int y=-1;y<2;y++) {
	        		forbidenTarget.add(new Position(myPosition.getX()+x, myPosition.getY()+y));
	        	}
	        }
	                
	        int minX=myPosition.getX()-4-1;
	        int minY=myPosition.getY()-4-1;
	        int maxX=myPosition.getX()+4+1;
	        int maxY=myPosition.getY()+4+1;
	        
	        if(minX<0) minX=0;
	        if(minY<0) minY=0;
	        if(maxX>(map.getWidth()-1)) maxX=map.getWidth();
	        if(maxY>(map.getHeight()-1)) maxY=map.getHeight();
	        
	        for(int x=minX;x<maxX;x++) {
		    	for(int y=minY;y<maxY;y++) {
		    		if(map.getCellValue(x, y, "Opponent")!=0) {
		    			forbidenTarget.add(new Position(x, y));
		    		}
		    	}
	    	}
	        
	        targets.add(myPosition);
	        
	        for(int i=0;i<4;i++) {
	        	for(int j=0; j<targets.size(); j++) {
	        		if((char)map.north(targets.get(j), "Initial")=='.') {
	    				if(!targets.contains(new Position(targets.get(j).getX(),targets.get(j).getY()-1)) &&
	    				!tmpTargets.contains(new Position(targets.get(j).getX(),targets.get(j).getY()-1))) {        					
	    					tmpTargets.add(new Position(targets.get(j).getX(),targets.get(j).getY()-1));
	    				}
	    			}
	    			
	    			if((char)map.south(targets.get(j), "Initial")=='.') {
	    				if(!targets.contains(new Position(targets.get(j).getX(),targets.get(j).getY()+1)) &&
	    				!tmpTargets.contains(new Position(targets.get(j).getX(),targets.get(j).getY()+1))) {        					
	    					tmpTargets.add(new Position(targets.get(j).getX(),targets.get(j).getY()+1));
	    				}
	    			}
	    			
	    			if((char)map.east(targets.get(j), "Initial")=='.') {
	    				if(!targets.contains(new Position(targets.get(j).getX()+1,targets.get(j).getY())) &&
	            		!tmpTargets.contains(new Position(targets.get(j).getX()+1,targets.get(j).getY()))) {        					
	    					tmpTargets.add(new Position(targets.get(j).getX()+1,targets.get(j).getY()));
	    				}
	    			}
	    			
	    			if((char)map.west(targets.get(j), "Initial")=='.') {
	    				if(!targets.contains(new Position(targets.get(j).getX()-1,targets.get(j).getY())) &&
	            		!tmpTargets.contains(new Position(targets.get(j).getX()-1,targets.get(j).getY()))) {        					
	    					tmpTargets.add(new Position(targets.get(j).getX()-1,targets.get(j).getY()));
	    				}
	    			}
	        	}
	            
	            targets.addAll(tmpTargets);
	            tmpTargets.clear();
	        }
	        
	        targets.removeAll(forbidenTarget);
	        
	        //Center the target
	        if(targets.size()>0) {
		        minX=targets.get(0).getX();
		    	minY=targets.get(0).getY();
		    	maxX=targets.get(0).getX();
		    	maxY=targets.get(0).getY();
		    	
		    	System.err.println("Targets before center");
		    	targets.forEach(target -> {
		    		System.err.println("x,y="+target.getX()+","+target.getY());
		    	});
		    	
		        for(int i=1;i<targets.size();i++) {
		        	if(minX>targets.get(i).getX()) minX=targets.get(i).getX();
		        	if(minY>targets.get(i).getY()) minY=targets.get(i).getY();
		        	if(maxX<targets.get(i).getX()) maxX=targets.get(i).getX();
		        	if(minY<targets.get(i).getY()) maxY=targets.get(i).getY();
		        }
		        
		        System.err.println("TOPEDO BEFORE MOVE : minX,minY="+minX+","+minY);
		        System.err.println("TOPEDO BEFORE MOVE : maxX,maxY="+maxX+","+maxY);
		        if(maxX-minX>1) {
		        	for(int i=0;i<targets.size();i++) {
		        		System.err.println("TOPEDO BEFORE MOVE - Remove : X,Y="+targets.get(i).getX()+","+targets.get(i).getY());
		        		if(targets.get(i).getX()==minX || targets.get(i).getX()==maxX) targets.remove(i);
		        	}
		        }
		        
		        if(maxY-minY>1) {
		        	for(int i=0;i<targets.size();i++) {
		        		System.err.println("TOPEDO BEFORE MOVE - Remove : X,Y="+targets.get(i).getX()+","+targets.get(i).getY());
		        		if(targets.get(i).getY()==minY || targets.get(i).getY()==maxY) targets.remove(i);
		        	}
		        }
		        System.err.println("Targets after center");
		        targets.forEach(target -> {
		    		System.err.println("x,y="+target.getX()+","+target.getY());
		    	});
	        }
	        // End Center Target
	        
	               
	        if(targets.size()!=0) {
	            if(targets.size()>1) {
	            	ciblePosition=targets.get(random.nextInt(targets.size()-1));
	            } else ciblePosition=targets.get(0);
	            System.err.println("Target on ("+ciblePosition.getX()+","+ciblePosition.getY()+") for "+targets.size()+ " possibilities");
	        } else {
	            ciblePosition=null;
	            System.err.println("No torpedo target available");
	        }
	        
	        return ciblePosition;
	    }
	    
	    public Position getTorpedoTargetAfterMove(SearchPosition searchPosition, Map map, char direction) {
	        ArrayList <Position> targets=new ArrayList<Position>();
	        ArrayList <Position> tmpTargets=new ArrayList<Position>();
	        ArrayList <Position> forbidenTarget=new ArrayList<Position>();
	       
	        Position ciblePosition=new Position();
	        
	        Position futurPosition=new Position(myPosition);
	        
	        switch (direction) {
	        	case 'N':
	        		futurPosition.setY(myPosition.getY()-1);
	        		break;
	        	case 'S':
	        		futurPosition.setY(myPosition.getY()+1);
	        		break;
	        	case 'E':
	        		futurPosition.setY(myPosition.getX()+1);
	        		break;
	        	case 'W':
	        		futurPosition.setY(myPosition.getX()-1);
	        		break;
	        }
	        
	        for(int x=-1;x<2;x++) {
	        	for(int y=-1;y<2;y++) {
	        		forbidenTarget.add(new Position(futurPosition.getX()+x, futurPosition.getY()+y));
	        	}
	        }
	                
	        int minX=futurPosition.getX()-4-1;
	        int minY=futurPosition.getY()-4-1;
	        int maxX=futurPosition.getX()+4+1;
	        int maxY=futurPosition.getY()+4+1;
	        
	        if(minX<0) minX=0;
	        if(minY<0) minY=0;
	        if(maxX>(map.getWidth()-1)) maxX=map.getWidth();
	        if(maxY>(map.getHeight()-1)) maxY=map.getHeight();
	        
	        for(int x=minX;x<maxX;x++) {
		    	for(int y=minY;y<maxY;y++) {
		    		if(map.getCellValue(x, y, "Opponent")!=0) {
		    			forbidenTarget.add(new Position(x, y));
		    		}
		    	}
	    	}
	        
	        targets.add(futurPosition);
	        
	        for(int i=0;i<4;i++) {
	        	for(int j=0; j<targets.size(); j++) {
	        		if((char)map.north(targets.get(j), "Initial")=='.') {
	    				if(!targets.contains(new Position(targets.get(j).getX(),targets.get(j).getY()-1)) &&
	    				!tmpTargets.contains(new Position(targets.get(j).getX(),targets.get(j).getY()-1))) {        					
	    					tmpTargets.add(new Position(targets.get(j).getX(),targets.get(j).getY()-1));
	    				}
	    			}
	    			
	    			if((char)map.south(targets.get(j), "Initial")=='.') {
	    				if(!targets.contains(new Position(targets.get(j).getX(),targets.get(j).getY()+1)) &&
	    				!tmpTargets.contains(new Position(targets.get(j).getX(),targets.get(j).getY()+1))) {        					
	    					tmpTargets.add(new Position(targets.get(j).getX(),targets.get(j).getY()+1));
	    				}
	    			}
	    			
	    			if((char)map.east(targets.get(j), "Initial")=='.') {
	    				if(!targets.contains(new Position(targets.get(j).getX()+1,targets.get(j).getY())) &&
	            		!tmpTargets.contains(new Position(targets.get(j).getX()+1,targets.get(j).getY()))) {        					
	    					tmpTargets.add(new Position(targets.get(j).getX()+1,targets.get(j).getY()));
	    				}
	    			}
	    			
	    			if((char)map.west(targets.get(j), "Initial")=='.') {
	    				if(!targets.contains(new Position(targets.get(j).getX()-1,targets.get(j).getY())) &&
	            		!tmpTargets.contains(new Position(targets.get(j).getX()-1,targets.get(j).getY()))) {        					
	    					tmpTargets.add(new Position(targets.get(j).getX()-1,targets.get(j).getY()));
	    				}
	    			}
	        	}
	            
	            targets.addAll(tmpTargets);
	            tmpTargets.clear();
	        }
	        
	        targets.removeAll(forbidenTarget);
	        
	      //Center the target
	        if(targets.size()>0) {
		        minX=targets.get(0).getX();
		    	minY=targets.get(0).getY();
		    	maxX=targets.get(0).getX();
		    	maxY=targets.get(0).getY();
		    	
		    	System.err.println("Targets before center");
		    	targets.forEach(target -> {
		    		System.err.println("x,y="+target.getX()+","+target.getY());
		    	});
		    	
		        for(int i=1;i<targets.size();i++) {
		        	if(minX>targets.get(i).getX()) minX=targets.get(i).getX();
		        	if(minY>targets.get(i).getY()) minY=targets.get(i).getY();
		        	if(maxX<targets.get(i).getX()) maxX=targets.get(i).getX();
		        	if(minY<targets.get(i).getY()) maxY=targets.get(i).getY();
		        }
		        
		        System.err.println("TOPEDO BEFORE MOVE : minX,minY="+minX+","+minY);
		        System.err.println("TOPEDO BEFORE MOVE : maxX,maxY="+maxX+","+maxY);
		        if(maxX-minX>1) {
		        	for(int i=0;i<targets.size();i++) {
		        		System.err.println("TOPEDO BEFORE MOVE - Remove : X,Y="+targets.get(i).getX()+","+targets.get(i).getY());
		        		if(targets.get(i).getX()==minX || targets.get(i).getX()==maxX) targets.remove(i);
		        	}
		        }
		        
		        if(maxY-minY>1) {
		        	for(int i=0;i<targets.size();i++) {
		        		System.err.println("TOPEDO BEFORE MOVE - Remove : X,Y="+targets.get(i).getX()+","+targets.get(i).getY());
		        		if(targets.get(i).getY()==minY || targets.get(i).getY()==maxY) targets.remove(i);
		        	}
		        }
		        System.err.println("Targets after center");
		        targets.forEach(target -> {
		    		System.err.println("x,y="+target.getX()+","+target.getY());
		    	});
	        }
	        // End Center Target

	               
	        if(targets.size()!=0) {
	            if(targets.size()>1) {
	            	ciblePosition=targets.get(random.nextInt(targets.size()-1));
	            } else ciblePosition=targets.get(0);
	            System.err.println("Target on ("+ciblePosition.getX()+","+ciblePosition.getY()+") for "+targets.size()+ " possibilities");
	        } else {
	            ciblePosition=null;
	            System.err.println("No torpedo target available");
	        }
	        
	        return ciblePosition;
	    }
	    
	    public int getSonarTarget(Map map) {
	        int zone=0;
	        HashMap<Integer, Double> accuracies = new HashMap<Integer, Double>();
	        double minValue=0;
	        
	        for(int i=1;i<10;i++) {
	        	double accuracyZone=map.getAccuracyByZone(i);
	        	
	        	if(i==1 || accuracyZone<minValue) {
	        		minValue=accuracyZone;
	        		zone=i;
	        	}
	        	accuracies.put(i,accuracyZone);	
	        }
	               
	        System.err.println("The zone with the accuracy minimum at "+minValue+" is "+zone);
	        
	        return(zone);
	    }
	}

	public static class Map {
	    private int width;
	    private int height;
	    private int initialMap[][];
	    private int computedMap[][];
	    private int initialComputedMap[][];
	    private int minesMap[][];
	    private int oppMap[][];
	    private Random random=new Random();
	    
	    public Map(int width, int height) {
	        this.width=width;
	        this.height=height;
	        this.initialMap = new int[width][height];
	        this.computedMap = new int[width][height];
	        this.initialComputedMap = new int[width][height];
	        this.minesMap = new int[width][height];
	        this.oppMap = new int[width][height];
	        
	        initMap("Mines");
	    }
	    
	    public void updateOppMap(SearchPosition searchPosition, String action) {
	    	Zone sonarActiveZone=searchPosition.getSonarActiveZone();
	    	ArrayList <Zone>excludeZones=searchPosition.getExcludeZone();
	    	
	    	// Set at 1 in oppMap all cells outside the searchPosition range.
	    	for(int y=0;y<height;y++) {
				for(int x=0;x<width;x++) {
					// Set all the oppMap outside the search zone at 1
					if(x<searchPosition.getX1() || x>searchPosition.getX2() ||
					y<searchPosition.getY1() || y>searchPosition.getY2()) {
						if((char)initialMap[x][y]=='x') oppMap[x][y]=1;
						else oppMap[x][y]=2;
					}
					else if((char)initialMap[x][y]=='.') oppMap[x][y]=0;				
	            }
			}
	    			
			// Set at 1 all cells where the backtracking failed
			for(int y=0;y<height;y++) {
				for(int x=0;x<width;x++) {
					if((char)initialMap[x][y]!='x') {
						if(!searchPosition.backTrackingPossible(new Position(x,y), this)) oppMap[x][y]=3;
					}
				}
			}
			
			// We reduce the range of the Sonar Active Zone
			if(sonarActiveZone!=null)
				switch(action.charAt(0)) {
					case 'N':
						if(sonarActiveZone.getY2()>sonarActiveZone.getY1()) sonarActiveZone.setY2(sonarActiveZone.getY2()-1);
						else sonarActiveZone=null;
						break;
					case 'S':
						if(sonarActiveZone.getY1()<sonarActiveZone.getY2()) sonarActiveZone.setY1(sonarActiveZone.getY1()+1);
						else sonarActiveZone=null;
						break;
					case 'E':
						if(sonarActiveZone.getX1()<sonarActiveZone.getX2()) sonarActiveZone.setX1(sonarActiveZone.getX1()+1);
						else sonarActiveZone=null;
						break;
					case 'W':
						if(sonarActiveZone.getX2()>sonarActiveZone.getX1()) sonarActiveZone.setX2(sonarActiveZone.getX2()-1);
						sonarActiveZone=null;
						break;
					default:
						break;
				}
			
			//We reduce the range of the last miss
			
			for(int i=0;i<excludeZones.size();i++) {
				switch(action.charAt(0)) {
					case 'N':
						if(excludeZones.get(i).getY2()>excludeZones.get(i).getY1()) excludeZones.get(i).setY2(excludeZones.get(i).getY2()-1);
						else excludeZones.remove(i);
						break;
					case 'S':
						if(excludeZones.get(i).getY1()<excludeZones.get(i).getY2()) excludeZones.get(i).setY1(excludeZones.get(i).getY1()+1);
						else excludeZones.remove(i);
						break;
					case 'E':
						if(excludeZones.get(i).getX1()<excludeZones.get(i).getX2()) excludeZones.get(i).setX1(excludeZones.get(i).getX1()+1);
						else excludeZones.remove(i);
						break;
					case 'W':
						if(excludeZones.get(i).getX2()>excludeZones.get(i).getX1()) excludeZones.get(i).setX2(excludeZones.get(i).getX2()-1);
						else excludeZones.remove(i);
						break;
					default:
						break;
				}
			}
					
			// Set at 2 in oppMap all cells in the active sonar Zone
			if(sonarActiveZone!=null)
		    	for(int y=sonarActiveZone.getY1();y<(sonarActiveZone.getY2()+1);y++) {
					for(int x=sonarActiveZone.getX1();x<(sonarActiveZone.getX2()+1);x++) {
						oppMap[x][y]=4;	
		            }
				}
	/*		
			// Set at 2 in oppMap all cells in the exclude zone
			for(int i=0;i<excludeZones.size();i++) {
				for(int y=excludeZones.get(i).getY1();y<(excludeZones.get(i).getY2()+1);y++) {
					for(int x=excludeZones.get(i).getX1();x<(excludeZones.get(i).getX2()+1);x++) {
						oppMap[x][y]=5;
		            }
				}
			}
	*/
			
			//searchPosition.displayPositionRange();
			//displayMap("Opponent");
		}

	    public Zone getRangeOfValue(int value, String layer) {
	    	int minX=width;
	    	int minY=height;
	    	int maxX=0;
	    	int maxY=0;
	    	
	    	
	    	
	    	switch(layer) {
	    		case "Opponent":
	    			for(int y=0;y<height;y++) {
	    				for(int x=0;x<height;x++) {
	    					if(oppMap[x][y]==value && minX>x) minX=x;
	    					if(oppMap[x][y]==value && maxX<x) maxX=x;
	    					if(oppMap[x][y]==value && minY>y) minY=y;
	    					if(oppMap[x][y]==value && maxY<y) maxY=y;
	    				}
	    			}
	    			break;
	    	}
	    	
	    	return(new Zone(minX, minY, maxX, maxY));
	    }
	    
	    public int[][] getComputedMap() {
	    	return computedMap;
	    }
	    
		public int getWidth() {
	        return width;
	    }
	    
	    public int getHeight() {
	        return height;
	    }
	    
	    public void setLine(int y, String line) {
	        for(int x=0;x<width;x++) {
	            initialMap[x][y]=line.charAt(x);
	        }
	    }
	    
	    public void setLine(int y, int value, String layer) {
	        switch(layer) {
	            case "Initial":
	                for(int x=0;x<width;x++) {
	                    initialMap[x][y]=value;
	                }
	                break;
	            case "Computed":
	                for(int x=0;x<width;x++) {
	                    computedMap[x][y]=value;
	                }
	                break;
	            case "Mines":
	                for(int x=0;x<width;x++) {
	                    minesMap[x][y]=value;
	                }
	                break;
	            case "Opponent":
	                for(int x=0;x<width;x++) {
	                    oppMap[x][y]=value;
	                }
	                break;
	        }
	    }
	    
	    public void initMap(String layer) {
	        int x=0;
	        int y=0;
	        
	        switch(layer) {
	        	case "Initial" :
	        		for(y=0;y<height;y++) {
	                    for(x=0;x<width;x++) {
	                    	if(x==0) {
	                    		if(initialMap[x+1][y]=='x') initialMap[x][y]=x;
	                    	}
	                    	
	                    	if(x==width-1) {
	                    		if(initialMap[x-1][y]=='x') initialMap[x][y]=x;
	                    	}
	                    	
	                    	if(x>0 && x<width-1) {
	                    		if(initialMap[x-1][y]=='x' && initialMap[x+1][y]=='x') initialMap[x][y]=x;
	                    	}
	                    	
	                    	if(y==0) {
	                    		if(initialMap[x][y+1]=='x') initialMap[x][y]=x;
	                    	}
	                    	
	                    	if(y==height-1) {
	                    		if(initialMap[x][y-1]=='x') initialMap[x][y]=x;
	                    	}
	                    	
	                    	if(y>0 && y<height-1) {
	                    		if(initialMap[x][y-1]=='x' && initialMap[x][y+1]=='x') initialMap[x][y]=x;
	                    	}
	                    }
	                }
	        	case "InitialComputed":
	        		for(y=0;y<height;y++) {
	                    for(x=0;x<width;x++) {
	                    	initialComputedMap[x][y]=computeCellValue(x,y);
	                    }
	                }
	        		break;
	            case "Computed":
	                for(y=0;y<height;y++) {
	                    for(x=0;x<width;x++) {
	                        computedMap[x][y]=initialComputedMap[x][y];
	                    }
	                }
	                break;
	            case "Mines":
	                for(y=0;y<height;y++) {
	                    for(x=0;x<width;x++) {
	                        minesMap[x][y]=0;
	                    }
	                }
	                break;
	            case "Opponent":
	                for(y=0;y<height;y++) {
	                    for(x=0;x<width;x++) {
	                        if((char)initialMap[x][y]=='x') oppMap[x][y]=1;
	                        else oppMap[x][y]=0;
	                    }
	                }
	                break;
	        }
	    }
	    
	    public double getAccuracyByZone(int zoneNumber) {
	    	Zone zone=new Zone(zoneNumber);
	    	double value=0;
	    	int count=0;
	    	
	    	for(int x=zone.getX1();x<zone.getX2()+1;x++) {
	    		for(int y=zone.getY1();y<zone.getY2()+1;y++) {
	    			if(oppMap[x][y]>0) count++;
	    		}
	    	}
	    	value=(double)count/(zone.getWidth()*zone.getHeight());
	    			
	    	return(value);
	    }
	    
	    public void displayMap(String layer) {
	        String line="";
	        
	        switch(layer) {
	            case "Initial":
	                for(int y=0;y<width;y++) {
	                    line="";
	                    for(int x=0;x<height;x++) {
	                        line=line + " " + (char)initialMap[x][y];
	                    }
	                    System.err.println(line);
	                }
	                break;
	            case "Computed":
	                for(int y=0;y<width;y++) {
	                    line="";
	                    for(int x=0;x<height;x++) {
	                        line=line + " " + computedMap[x][y];
	                    }
	                    System.err.println(line);
	                }
	                break;
	            case "Mines":
	                for(int y=0;y<width;y++) {
	                    line="";
	                    for(int x=0;x<height;x++) {
	                        line=line + " " + minesMap[x][y];
	                    }
	                    System.err.println(line);
	                }
	                break;
	            case "Opponent":
	                for(int y=0;y<width;y++) {
	                    line="";
	                    for(int x=0;x<height;x++) {
	                        line=line + " " + oppMap[x][y];
	                    }
	                    System.err.println(line);
	                }
	                break;
	        }
	    }
	    
	    public boolean isWatter(Position position) {
	        if(computedMap[position.getX()][position.getY()]>0) return true;
	        else return false;
	    }
	    
	    public boolean isWatter(int x, int y) {
	        if(computedMap[x][y]>0) return true;
	        else return false;
	    }
	    
	    public boolean isIsland(Position position) {
	        if(computedMap[position.getX()][position.getY()]==0) return true;
	        else return false;
	    }
	    
	    public boolean isIsland(int x, int y) {
	        if(computedMap[x][y]==0) return true;
	        else return false;
	    }
	    
	    public Position getInitialPos() {
	        Position position=new Position();
	        
	        //position=new Position(random.nextInt(width),random.nextInt(height));
	        position=new Position(0,random.nextInt(height));
	        
	        while(isIsland(position) || getCellValue(position.getX(),position.getY(),"Computed")!=3) {
	            //position=new Position(random.nextInt(width),random.nextInt(height));
	        	position=new Position(0,random.nextInt(height));
	        }
	        
	        return position;
	    }
	    
	    public int north(Position position, String layer) {
	        int newY=position.getY()-1;
	        int value=0;
	        
	        if(newY < 0) {
	            switch(layer) {
	                case "Initial":
	                    // If outside the map, we return 'x' as Island
	                    value='x';
	                    break;
	                case "Computed":
	                    // If outside the map, we return -1 as Path
	                    value=-1;
	                    break;
	                case "Mines":
	                    // If outside the map, we return 1 as Mine
	                    value=1;
	                    break;
	                    
	            }
	        }
	        else {
	            //System.err.println("DEBUG computedMap["+position.getX()+"]["+newY+"]="+computedMap[position.getX()][newY]);
	            switch(layer) {
	                case "Initial":
	                    value=initialMap[position.getX()][newY];
	                    break;
	                case "Computed":
	                    value=computedMap[position.getX()][newY];
	                    break;
	                case "Mines":
	                    value=minesMap[position.getX()][newY];
	                    break;
	                    
	            }
	        }
	     
	        return value;
	    }
	    
	    public int west(Position position, String layer) {
	        int newX=position.getX()-1;
	        int value=0;
	        
	        if(newX < 0) {
	            switch(layer) {
	                case "Initial":
	                    // If outside the map, we return 'x' as Island
	                    value='x';
	                    break;
	                case "Computed":
	                    // If outside the map, we return -1 as Path
	                    value=-1;
	                    break;
	                case "Mines":
	                    // If outside the map, we return 1 as Mine
	                    value=1;
	                    break;
	                    
	            }
	        } else {
	            //System.err.println("DEBUG computedMap["+newX+"]["+position.getY()+"]="+computedMap[newX][position.getY()]);
	            switch(layer) {
	                case "Initial":
	                    value=initialMap[newX][position.getY()];
	                    break;
	                case "Computed":
	                    value=computedMap[newX][position.getY()];
	                    break;
	                case "Mines":
	                    value=minesMap[newX][position.getY()];
	                    break;
	                    
	            }
	        }
	        return value;
	    }
	    
	    public int south(Position position, String layer) {
	        int newY=position.getY()+1;
	        int value=0;
	        
	        if(newY >= height) {
	            switch(layer) {
	                case "Initial":
	                    // If outside the map, we return 'x' as Island
	                    value='x';
	                    break;
	                case "Computed":
	                    // If outside the map, we return -1 as Path
	                    value=-1;
	                    break;
	                case "Mines":
	                    // If outside the map, we return 1 as Mine
	                    value=1;
	                    break;
	                    
	            }
	        } else {
	            //System.err.println("DEBUG computedMap["+position.getX()+"]["+newY+"]="+computedMap[position.getX()][newY]);
	            switch(layer) {
	                case "Initial":
	                    value=initialMap[position.getX()][newY];
	                    break;
	                case "Computed":
	                    value=computedMap[position.getX()][newY];
	                    break;
	                case "Mines":
	                    value=minesMap[position.getX()][newY];
	                    break;
	                    
	            }
	        }
	        return value;
	    }
	    
	    public int east(Position position, String layer) {
	        int newX=position.getX()+1;
	        int value=0;
	        
	        if(newX >= width) {
	            switch(layer) {
	                case "Initial":
	                    // If outside the map, we return 'x' as Island
	                    value='x';
	                    break;
	                case "Computed":
	                    // If outside the map, we return -1 as Path
	                    value=-1;
	                    break;
	                case "Mines":
	                    // If outside the map, we return 1 as Mine
	                    value=1;
	                    break;
	                    
	            }
	        } else {
	            //System.err.println("DEBUG computedMap["+newX+"]["+position.getY()+"]="+computedMap[newX][position.getY()]);
	            switch(layer) {
	                case "Initial":
	                    value=initialMap[newX][position.getY()];
	                    break;
	                case "Computed":
	                    value=computedMap[newX][position.getY()];
	                    break;
	                case "Mines":
	                    value=minesMap[newX][position.getY()];
	                    break;
	            }
	        }
	        return value;
	    }
	    
	    public int getCellValue(int x, int y, String layer) {
	        int value=0;
	        
	        switch(layer) {
	            case "Initial":
	                value=initialMap[x][y];
	                break;
	            case "Computed":
	                value=computedMap[x][y];
	                break;
	            case "Mines":
	                value=minesMap[x][y];
	                break;
	            case "Opponent":
	            	value=oppMap[x][y];
	            	break;
	        }
	        return value;
	    }
	    
	    public void setCellValue(int x, int y, String layer, int value) {
	        switch(layer) {
	            case "Initial":
	                initialMap[x][y]=value;
	                break;
	            case "Computed":
	                computedMap[x][y]=value;
	                break;
	            case "Mines":
	                minesMap[x][y]=value;
	                break;
	        }
	    }
	    
	    public void setCellValue(Position position, String layer, int value) {
	        switch(layer) {
	            case "Initial":
	                initialMap[position.getX()][position.getY()]=value;
	                break;
	            case "Computed":
	                computedMap[position.getX()][position.getY()]=value;
	                break;
	            case "Mines":
	                minesMap[position.getX()][position.getY()]=value;
	                break;
	        }
	    }
	    
	    public void updateCellArroundPosition(Position position) {
	        int x=position.getX();
	        int y=position.getY();
	        
	        if(x>0 && x<14) {
	                if(initialMap[x-1][y]=='.') computedMap[x-1][y]--;
	                if(initialMap[x+1][y]=='.') computedMap[x+1][y]--;
	            } else {
	                if(x==0) {
	                    if(initialMap[x+1][y]=='.') computedMap[x+1][y]--;
	                } else if(initialMap[x-1][y]=='.') computedMap[x-1][y]--;
	            }
	            if(y>0 && y<14) {
	                if(initialMap[x][y-1]=='.') computedMap[x][y-1]--;
	                if(initialMap[x][y+1]=='.') computedMap[x][y+1]--;
	            } else {
	                if(y==0) {
	                    if(initialMap[x][y+1]=='.') computedMap[x][y+1]--;
	                } else if(initialMap[x][y-1]=='.') computedMap[x][y-1]--;
	            }
	    }
	    
	    public int computeCellValue(int x, int y) {
	        int valueCell=0;
	        
	        if(initialMap[x][y]=='.') {
	            if(x>0 && x<(width-1)) {
	                if(initialMap[x-1][y]=='.') valueCell++;
	                if(initialMap[x+1][y]=='.') valueCell++;
	            } else {
	                if(x==0) {
	                    if(initialMap[x+1][y]=='.') valueCell++;
	                } else if(initialMap[x-1][y]=='.') valueCell++;
	            }
	            if(y>0 && y<(height-1)) {
	                if(initialMap[x][y-1]=='.') valueCell++;
	                if(initialMap[x][y+1]=='.') valueCell++;
	            } else {
	                if(y==0) {
	                    if(initialMap[x][y+1]=='.') valueCell++;
	                } else if(initialMap[x][y-1]=='.') valueCell++;
	            }
	        } else valueCell=0;
	        
	        return valueCell;
	    }
	    
	    public Position getMine(SearchPosition searchPosition, Position position) {
	        ArrayList<Position> mines=new ArrayList<Position>();
	        Position positionMine=null;
	        
	        for(int y=searchPosition.getY1();y<(searchPosition.getY2()+1);y++) {
	            for(int x=searchPosition.getX1();x<(searchPosition.getX2()+1);x++) {
	                if(minesMap[x][y]==1 & oppMap[x][y]==0) {
	                    mines.add(new Position(x,y));
	                }
	            }
	        }
	        
	        if(mines.size()!=0) {
	        	if(mines.size()>1) positionMine=mines.get(random.nextInt(mines.size()-1));
	        	else positionMine=mines.get(0);

	        	System.err.println("on "+mines.size()+ " mines we choose ("+positionMine.getX()+","+positionMine.getY()+")");
	        } else System.err.println("No mine found");
	        
	        return(positionMine);
	    }
	}

	public static class Orders {
	    private char direction;
	    private int surface=0;
	    private Position torpedo=null;
	    private boolean silence=false;
	    private boolean moveBeforeTorpedo=false;
	    private boolean moveBeforeSilence=false;
	    
	    public Orders(String orders) {
	        update(orders);
	    }
	    
	    public void update(String orders) {
	        String arrayOrders[] = orders.split("\\|");
	        int ordersCount=arrayOrders.length;
	        String coupleOrder[];
	        
	        surface=0;
	        silence=false;
	        direction='?';
	        torpedo=null;
	        
	        for(int i=0;i<ordersCount;i++) {
	            coupleOrder=arrayOrders[i].split(" ");
	            if(coupleOrder[0].equals("MOVE")) {
	            	if(torpedo==null) moveBeforeTorpedo=true;
	            	else moveBeforeTorpedo=false;
	            	
	            	if(silence==false) moveBeforeSilence=true;
	            	else moveBeforeSilence=false;
	        			
	                direction=coupleOrder[1].charAt(0);
	            }
	            
	            if(coupleOrder[0].equals("SURFACE")) {
	                surface=Integer.parseInt(coupleOrder[1]);
	            }
	            
	            if(coupleOrder[0].equals("SILENCE")) {
	                silence=true;
	            }
	            
	            if(coupleOrder[0].equals("TORPEDO")) {
	                int torpedoX=Integer.parseInt(coupleOrder[1]);
	                int torpedoY=Integer.parseInt(coupleOrder[2]);
	                
	                torpedo = new Position(torpedoX,torpedoY);                
	            }
	        }
	    }
	    
	    public int getSurface() {
	        return surface;
	    }
	    
	    public boolean isMoveBeforeTorpedo() {
	        return moveBeforeTorpedo;
	    }
	    
	    public boolean isMoveBeforeSilence() {
	        return moveBeforeSilence;
	    }
	    
	    public Position getTorpedo() {
	        return torpedo;
	    }
	    
	    public void setSurface(int surface) {
	        this.surface=surface;
	    }
	    
	    public char getDirection() {
	        return direction;
	    }
	    
	    public boolean getSilence() {
	        return silence;
	    }
	}
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt();
        int height = in.nextInt();
        int myId = in.nextInt();
        
        Map map=new Map(width, height);
        
        if (in.hasNextLine()) {
            in.nextLine();
        }
        for (int i = 0; i < height; i++) {
            String line = in.nextLine();
            map.setLine(i, line);
        }
        //map.initMap("Initial");
        map.initMap("InitialComputed");
        map.initMap("Computed");
        map.initMap("Opponent");
        
        Position initialPosition=new Position(map.getInitialPos());
        
        System.out.println(initialPosition.getX()+" "+initialPosition.getY());
        
        map.setCellValue(initialPosition.getX(),initialPosition.getY(),"Computed", -1);
        map.updateCellArroundPosition(initialPosition);

        // Write an action using System.out.println()
        // To debug: System.err.println("Debug messages...");
        
        int turnWithoutSilence=0;
        Submarine mySubmarine=null;
        Orders oppOrders=null;
        SearchPosition searchPosition=new SearchPosition(map.getWidth(),map.getHeight());
        boolean silenceNeeded=false;

        // game loop
        while (true) {
        	long startTime = System.nanoTime();
            int x = in.nextInt();
            int y = in.nextInt();
            int myLife = in.nextInt();
            int oppLife = in.nextInt();
            int torpedoCooldown = in.nextInt();
            int sonarCooldown = in.nextInt();
            int silenceCooldown = in.nextInt();
            int mineCooldown = in.nextInt();
            String sonarResult = in.next();
            
            if (in.hasNextLine()) {
                in.nextLine();
            }
            String opponentOrders = in.nextLine();
            //System.err.println("opponentOrders="+opponentOrders);
            
            //turn+=2;
            //System.err.println("Tour #" + turn);

/////////////////
/// Manage results
/////////////////

                        
            if(oppOrders==null) oppOrders = new Orders(opponentOrders);
            else oppOrders.update(opponentOrders);
            
            int myLooseLife=0;
            int oppLooseLife=0;

            if(mySubmarine==null) mySubmarine = new Submarine(x,y,myLife,oppLife,torpedoCooldown,sonarCooldown,silenceCooldown,mineCooldown);
            else {
            	System.err.println("sonarResult="+sonarResult);
                if(sonarResult.equals("Y")) {
                    System.err.println("Sonar found opponent in zone "+mySubmarine.getSonarZone());
                    searchPosition.zone(mySubmarine.getSonarZone(), map, sonarResult);
                } else {
                	if(sonarResult.equals("N")) searchPosition.zone(mySubmarine.getSonarZone(), map, sonarResult);
                	//else System.err.println("No sonar result");
                }
                
            	myLooseLife=mySubmarine.getMyLife();
            	oppLooseLife=mySubmarine.getOppLife();
            	mySubmarine.update(x,y,myLife,oppLife,torpedoCooldown,sonarCooldown,silenceCooldown,mineCooldown);
            	
            	myLooseLife-=mySubmarine.getMyLife();
            	oppLooseLife-=mySubmarine.getOppLife();
            	if(myLooseLife>0 && oppOrders.getTorpedo()!=null) silenceNeeded=true;
            	if(oppLooseLife>0 && oppOrders.getSurface()==0 && mySubmarine.getLastTarget()!=null) {
            		searchPosition.touch(mySubmarine.getLastTarget(),map, oppLooseLife);
            		mySubmarine.setLastTarget(null);
            	} else if(oppLooseLife==0 && mySubmarine.getLastTarget()!=null) {
            		searchPosition.miss(mySubmarine.getLastTarget(),map);
            		mySubmarine.setLastTarget(null);
            	}
            }
            
            // If opponent sent a torpedo before move we manage the order now else we will manage the order later
            if(oppOrders.getTorpedo()!=null && !oppOrders.isMoveBeforeTorpedo()) {
            	searchPosition.torpedo(oppOrders.getTorpedo(), map, 'T');
            }
            
            if(oppOrders.getSurface()>0) {
                searchPosition.zone(oppOrders.getSurface(), map, "Y");
            } else {
                if(oppOrders.getSilence()) {
                	System.err.println("Range before Silence:");
                	searchPosition.displayPositionRange();
                	
                	if(oppOrders.isMoveBeforeSilence()) {
                		searchPosition.direction(oppOrders.getDirection(), map);
                	
                		System.err.println("Range after Move before Silence:");
                		searchPosition.displayPositionRange();
                	}
                    searchPosition.silence(map);
                    
                    System.err.println("Range after Silence:");
                    searchPosition.displayPositionRange();
                    
                    if(!oppOrders.isMoveBeforeSilence() && oppOrders.getDirection()!='?') {
                		searchPosition.direction(oppOrders.getDirection(), map);
                		System.err.println("Range after Move after Silence:");
                        searchPosition.displayPositionRange();
                    }
                } else {
                	searchPosition.direction(oppOrders.getDirection(), map);
                	System.err.println("Range after Move :");
                    searchPosition.displayPositionRange();
                }
            }
            
            // If opponent sent a torpedo after move we manage this order now 
            if(oppOrders.getTorpedo()!=null) {
            	searchPosition.torpedo(oppOrders.getTorpedo(), map, 'T');
            }
            
            String strMessage;
            double accuracy=((double)Math.round(searchPosition.getAccuracy(map)*100)/100);
            
            if(accuracy>100) {
            	searchPosition.deleteMoves();
            	searchPosition=new SearchPosition(map.getWidth(),map.getHeight());
            }
            
            strMessage="Accuracy=" + accuracy + "%";
            
            
/////////////////
/// Prepare actions
/////////////////
            
            double torpedoAccuracy=93.7;
            double triggerAccuracy=97;
            double sonarAccuracy=80;
            
            // Retrieve the next direction
            char direction=mySubmarine.getNextDirection(map);
            System.err.println("Direction choose = "+direction);
            
            Position torpedoTarget=null;
            String strCibleTorpedo="";
            String strTrigger="";
            Boolean moveBeforeTorpedo=false;
            
            // Retrieve Torpedo target
            if(mySubmarine.getTorpedoCooldown()==0 && accuracy>torpedoAccuracy){
            	// Check is target is available before move
            	torpedoTarget=mySubmarine.getTorpedoTargetBeforeMove(searchPosition, map, direction);
            	
                if(torpedoTarget==null) {
                	// Check is target is available after move
                	torpedoTarget=mySubmarine.getTorpedoTargetAfterMove(searchPosition, map, direction);
                	if(torpedoTarget!=null) {
                		strCibleTorpedo="TORPEDO " + torpedoTarget.getX() + " " + torpedoTarget.getY() + "|";
                        mySubmarine.setLastTarget(torpedoTarget);
                        moveBeforeTorpedo=true;
                	}
                } else {
                    strCibleTorpedo="TORPEDO " + torpedoTarget.getX() + " " + torpedoTarget.getY() + "|";
                    mySubmarine.setLastTarget(torpedoTarget);
                }
            }
            
            String strSonar="";
            
            // If sonar is loaded we get Sonar zone target
            if(mySubmarine.getSonarCooldown()==0 && accuracy<sonarAccuracy) {
                int sonarCible=mySubmarine.getSonarTarget(map);
                if(sonarCible!=0) {
                    strSonar="SONAR "+sonarCible;
                    mySubmarine.setSonarZone(sonarCible);
                }
            }
            
            // GET TRIGGER
            Position triggerTarget=null;
            if(mySubmarine.getCountMine()>0  && accuracy>triggerAccuracy) {
            	triggerTarget=map.getMine(searchPosition, mySubmarine.getPosition());
            	if(triggerTarget!=null) strTrigger="TRIGGER " + triggerTarget.getX()+" "+triggerTarget.getY();
            }
            
            String strMine="";
            String dirMine="";
            if(mySubmarine.getMineCooldown()==0) {
            	dirMine=mySubmarine.getMineDirection(mySubmarine.getPosition(),map);
            	if(!dirMine.equals("")) strMine="MINE " + dirMine;
            }
            
            String strReload="";
            if(mySubmarine.getTorpedoCooldown()!=0) {
                System.err.println("Reload Torpedo");
                strReload="TORPEDO";
            } else if(mySubmarine.getSilenceCooldown()!=0) {
                System.err.println("Reload Silence");
                strReload="SILENCE";
            } /*else if(mySubmarine.getSonarCooldown()!=0) {
                System.err.println("Reload Sonar");
                strReload="SONAR";
            } */else if(mySubmarine.getMineCooldown()!=0) {
                System.err.println("Reload Mine");
                strReload="MINE";
            }  
                        
            if(turnWithoutSilence>7) {
            	silenceNeeded=true;
            } else turnWithoutSilence++;
            
/////////////////
/// Choose actions
/////////////////        
            
            // Priority to SURFACE if needed
            if(direction=='U') {
                mySubmarine.goTo(direction, map);
                silenceNeeded=true;
                System.err.println("MSG "+strMessage+"| SURFACE");
                System.out.println("MSG "+strMessage+"| SURFACE");
            } else {
            	// If SURFACE is not needed and SILENCE is needed (after loose life)
                if(mySubmarine.getSilenceCooldown()==0 && silenceNeeded) {
                	//SILENCE
                	turnWithoutSilence=0;
                	silenceNeeded=false;
                	mySubmarine.goTo(direction,map);
                	map.displayMap("Computed");
                	char direction2=mySubmarine.getNextDirection(map);
                	
                	if(direction2!='U') {
                		mySubmarine.goTo(direction2,map);
	                
                		System.err.println("MSG "+strMessage+"|MOVE " + direction+ " " + strReload + "|"+strCibleTorpedo+"|SILENCE " + direction2 + " 1");
                		System.out.println("MSG "+strMessage+"|MOVE " + direction+ " " + strReload + "|"+strCibleTorpedo+"|SILENCE " + direction2 + " 1");
                	} else {
                		System.err.println("MSG "+strMessage+"|SILENCE " + direction + " 1");
                		System.out.println("MSG "+strMessage+"|SILENCE " + direction + " 1");
                	}
                }
                // If SILENCE is not needed we try SONAR without MOVE !
                else if(mySubmarine.getSonarCooldown()==0 && !strSonar.equals("") && accuracy<sonarAccuracy) {
                	// SONAR
                	System.err.println("MSG "+strMessage+"|"+strSonar);
                	System.out.println("MSG "+strMessage+"|"+strSonar);
                }
                // If SONAR is not needed and we have a TORPEDO cible with accuracy > torpedoAccuracy with send a torpedo.
            	else if(mySubmarine.getTorpedoCooldown()==0 && !strCibleTorpedo.equals("") && accuracy>torpedoAccuracy) {
              		// TORPEDO and MOVE
            		silenceNeeded=true;
            		mySubmarine.setLastTarget(torpedoTarget);
            		mySubmarine.goTo(direction, map);
            		
            		if(moveBeforeTorpedo) {
            			System.err.println("MSG "+strMessage + "|MOVE " + mySubmarine.getDirection()+ " " + strReload + "|" + strCibleTorpedo);
            			System.out.println("MSG "+strMessage + "|MOVE " + mySubmarine.getDirection()+ " " + strReload + "|" + strCibleTorpedo);
            		} else
            		{
            			System.err.println("MSG "+strMessage+"|"+strCibleTorpedo + "MOVE " + mySubmarine.getDirection()+ " " + strReload);
                		System.out.println("MSG "+strMessage+"|"+strCibleTorpedo + "MOVE " + mySubmarine.getDirection()+ " " + strReload);            			
            		}
            	}
                // If TORPEDO is not possible and we have mine to place, we MINE 
                else if(mySubmarine.getMineCooldown()==0 && !strMine.equals("")) {
                	// MINE
	                mySubmarine.sendMine(mySubmarine.getPosition(),map,dirMine);
	                System.err.println("MSG "+strMessage+"|"+strMine);
	                System.out.println("MSG "+strMessage+"|"+strMine);
                }
                else { 
                    // If TRIGGER is available with accuracy>triggerAccuracy we TRIGGER
                	if(!strTrigger.equals("") && accuracy>triggerAccuracy) {
                		mySubmarine.goTo(direction, map);
                		mySubmarine.setLastTarget(triggerTarget);
                		map.setCellValue(triggerTarget.getX(), triggerTarget.getY(), "Mines", 0);
                		mySubmarine.setCountMine(mySubmarine.getCountMine()-1);
                		System.err.println("MSG "+strMessage+"|" + "MOVE " + mySubmarine.getDirection()+ " | " + strTrigger);
                		System.out.println("MSG "+strMessage+"|" + "MOVE " + mySubmarine.getDirection()+ " | " + strTrigger);
                	}
                    // An to finish If TRIGGER is not available we only MOVE
                	else {
                		mySubmarine.goTo(direction, map);
                		System.err.println("MSG "+strMessage+"|" + "MOVE " + mySubmarine.getDirection()+ " " + strReload);
                		System.out.println("MSG "+strMessage+"|" + "MOVE " + mySubmarine.getDirection()+ " " + strReload);
                	}
                }
            }
            
            searchPosition.displayPositionRange();
            searchPosition.displayBackTrackingDepth();
            System.err.println("Opponent map:");
            map.displayMap("Opponent");
            System.err.println("Computed map:");
            map.displayMap("Computed");
            //System.err.println("Mines map:");
            //map.displayMap("Mines");
            
            long endTime = System.nanoTime();
    		long timeElapsed = endTime - startTime;
    		System.err.println("Execution time in milliseconds: " + timeElapsed / 1000000);
        }
    }
}