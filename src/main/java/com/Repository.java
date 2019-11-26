package com;

import com.Moebel.*;
import javafx.scene.image.Image;
import sun.util.logging.PlatformLogger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Misc class for holding information
 */
public class Repository {

    /**
     * A way of transporting variables, because of Overrides don't allow for changing variables in the class they are defined in
     */
    private static HashMap<String, Double> x = new HashMap<>();
    /**
     * A way of transporting variables, because of Overrides don't allow for changing variables in the class they are defined in
     */
    private static HashMap<String, Double> y = new HashMap<>();

    public static List<Moebel> getAll(){
        List<Moebel> list = new LinkedList<>();
        /**for (int i = 0; i < 1000 ; i++) {
            list.add(new Stuhl("gvz"+i,i,i/10));
         }**/
        String img = Objects.requireNonNull(Repository.class.getClassLoader().getResource("chair.png")).toExternalForm();
        Image chair = new Image(img,true);

        list.add(new Stuhl("Nomade",0.5,0.5,chair));
        list.add(new Sofa("Komfort",2,1,chair));
        list.add(new Sessel("deLuxe",1,1,chair));
        list.add(new Sofatisch("Admiral",1,1,chair));
        list.add(new Sofatisch("Monte Carlo",1,1,chair));
        list.add(new Bett("Le France",2,2,chair));
        list.add(new Bett("Le Petit France",1,2,chair));
        list.add(new Esstisch("Wikinger",1.2,1.2,chair));

        return list;
    }

    /**
     * Simple setter
     * @param toString Some reproducible, unique identifier. Preferably an Objects .toString method
     * @param x value to be set
     */
    public static void setx(String toString, Double x){
        if(Main.layoutLogger.isLoggable(PlatformLogger.Level.FINEST)) Main.layoutLogger.finest("Setting x for "+toString+" :" + x);
        Repository.x.put(toString, x);
    }

    /**
     * Gets the value of the value, that was previously stored with {@link Repository#setx}
     * @param toString Some reproducible, unique identifier. Preferably an Objects .toString method
     * @return x-value, that was set
     */
    public static double getx(String toString){
        Double x = Repository.x.get(toString);
        if(Main.layoutLogger.isLoggable(PlatformLogger.Level.FINEST)) Main.layoutLogger.finest("Getting x for "+toString+" :" + x);
        return Operators.ifNullRet(x,0.0);
    }

    /**
     * Simple setter
     * @param toString Some reproducible, unique identifier. Preferably an Objects .toString method
     * @param y value to be set
     */
    public static void sety(String toString, Double y){
        if(Main.layoutLogger.isLoggable(PlatformLogger.Level.FINEST)) Main.layoutLogger.finest("Setting y for "+toString+" :" + y);
        Repository.y.put(toString, y);
    }
    /**
     * Gets the value of the value, that was previously stored with {@link Repository#sety}
     * @param toString Some reproducible, unique identifier. Preferably an Objects .toString method
     * @return y-value, that was set
     */
    public static double gety(String toString){
        Double y = Repository.y.get(toString);
        if(Main.layoutLogger.isLoggable(PlatformLogger.Level.FINEST)) Main.layoutLogger.finest("Getting y for "+toString+" :" + y);
        return Operators.ifNullRet(y,0.0);
    }
}
