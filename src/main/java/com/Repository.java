package com;

import com.Moebel.Moebel;
import sun.util.logging.PlatformLogger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.Moebel.Bett.LEFRANCE;
import static com.Moebel.Bett.LEPETITFRANCE;
import static com.Moebel.Esstisch.WIKINGER;
import static com.Moebel.Sessel.DELUXE;
import static com.Moebel.Sofa.KOMFORT;
import static com.Moebel.Sofatisch.ADMIRAL;
import static com.Moebel.Sofatisch.MONTECARLO;
import static com.Moebel.Stuhl.NOMADE;

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

    public static List<? extends Moebel> getAll(){
        List<Moebel> list = new LinkedList<>();

        list.add(NOMADE);
        list.add(KOMFORT);
        list.add(DELUXE);
        list.add(ADMIRAL);
        list.add(MONTECARLO);
        list.add(LEFRANCE);
        list.add(LEPETITFRANCE);
        list.add(WIKINGER);

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
