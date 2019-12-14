package com;

import com.Moebel.Moebel;
import com.Moebel.SchrankWand;

import java.util.LinkedList;
import java.util.List;

import static com.Moebel.Bett.LEFRANCE;
import static com.Moebel.Bett.LEPETITFRANCE;
import static com.Moebel.Esstisch.WIKINGER;
import static com.Moebel.Schrank.BILLY;
import static com.Moebel.Sessel.DELUXE;
import static com.Moebel.Sofa.KOMFORT;
import static com.Moebel.Sofatisch.ADMIRAL;
import static com.Moebel.Sofatisch.MONTECARLO;
import static com.Moebel.Stuhl.NOMADE;

public class Repository {
	public static List<? extends Moebel> getAll(){
		List<Moebel> list = new LinkedList<>();

		list.add(NOMADE.get());
		list.add(KOMFORT.get());
		list.add(DELUXE.get());
		list.add(ADMIRAL.get());
		list.add(MONTECARLO.get());
		list.add(LEFRANCE.get());
		list.add(LEPETITFRANCE.get());
		list.add(WIKINGER.get());
		list.add(BILLY.get());
		list.add(new SchrankWand("BigBilly",0));

		return list;
	}
}
