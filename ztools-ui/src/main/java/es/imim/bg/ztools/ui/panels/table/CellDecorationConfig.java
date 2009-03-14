package es.imim.bg.ztools.ui.panels.table;

import java.text.DecimalFormat;

import es.imim.bg.colorscale.ColorScale;
import es.imim.bg.colorscale.PValueColorScale;

public class CellDecorationConfig {
	public boolean showColors = true;
	
	public DecimalFormat textFormat = new DecimalFormat("#.######");
	
	public CellDecoration.TextAlignment textAlign = 
		CellDecoration.TextAlignment.left;
	
	public ColorScale scale = new PValueColorScale();
	
	public CellDecorationConfig() {
		this.showColors = true;

		this.textFormat = new DecimalFormat("#.######");
		
		this.textAlign = CellDecoration.TextAlignment.left;
		
		this.scale = new PValueColorScale();
	}
}
