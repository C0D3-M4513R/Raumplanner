package com.Moebel;


import java.util.HashMap;
import java.util.function.Supplier;

public class Stuhl extends Moebel {

	public static final Supplier<Stuhl> NOMADE = () -> new Stuhl("Nomade", 0.5, 0.5);

	public Stuhl(String name, double width, double height) {
		super(name, width, height);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected HashMap<String, Supplier<? extends Moebel>> getPreset() {
        HashMap<String,Supplier<? extends Moebel>> presets = new HashMap<>();
        presets.put("Nomade",NOMADE);
        return presets;
    }

}
