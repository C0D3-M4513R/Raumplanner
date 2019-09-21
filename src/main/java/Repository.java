import Moebel.Moebel;
import Moebel.Stuhl;

import java.util.LinkedList;
import java.util.List;

public class Repository {
    public static List<Moebel> getAll(){
        List<Moebel> list = new LinkedList();
        for (int i = 0; i < 1000 ; i++) {
            list.add(new Stuhl("gvz"+i,i,i/10));
        }
        return list;
    }
}
