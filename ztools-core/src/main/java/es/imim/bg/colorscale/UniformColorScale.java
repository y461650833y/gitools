package es.imim.bg.colorscale;

import java.awt.Color;

public class UniformColorScale implements ColorScale {

	protected Color color;
	
	public UniformColorScale(Color color) {
		this.color = color;
	}
	
	@Override
	public Color getColor(double value) {
		return color;
	}

}
