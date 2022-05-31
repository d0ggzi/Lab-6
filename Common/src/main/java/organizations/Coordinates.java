package organizations;

import java.io.Serializable;

public class Coordinates implements Serializable {

    private int x; //Значение поля должно быть больше -900
    private int y;

    public Coordinates(int x, int y){
        super();
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString(){
        return "(" + x + "," + y + ")";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
