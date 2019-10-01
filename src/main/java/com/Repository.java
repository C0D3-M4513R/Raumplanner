package com;

import com.Moebel.*;
import javafx.scene.image.Image;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Repository {
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
}
