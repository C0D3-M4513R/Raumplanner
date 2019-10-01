package com;

import com.Moebel.*;
import java.util.LinkedList;
import java.util.List;

public class Repository {
    public static List<Moebel> getAll(){
        List<Moebel> list = new LinkedList();
        /**for (int i = 0; i < 1000 ; i++) {
            list.add(new Stuhl("gvz"+i,i,i/10));
        }**/
        list.add(new Stuhl("Stuhl-Nomade",0.5,0.5));
        list.add(new Sofa("Sofa-Komfort",2,1));
        list.add(new Sessel("Sessel-deLuxe",1,1));
        list.add(new Sofatisch("Sofatisch-Admiral",1,1));
        list.add(new Sofatisch("Sofatisch-Monte Carlo",1,1));
        list.add(new Bett("Bett-Le France",2,2));
        list.add(new Bett("Bett-Le Petit France",1,2));
        list.add(new Esstisch("Esstisch-Wikinger",1.2,1.2));

        return list;
    }
}
