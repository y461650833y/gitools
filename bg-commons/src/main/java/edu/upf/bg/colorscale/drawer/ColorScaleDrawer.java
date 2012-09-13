/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package edu.upf.bg.colorscale.drawer;

import edu.upf.bg.colorscale.ColorScaleRange;
import edu.upf.bg.colorscale.IColorScale;
import edu.upf.bg.colorscale.NumericColorScale;
import edu.upf.bg.colorscale.impl.CategoricalColorScale;
import edu.upf.bg.formatter.GenericFormatter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class ColorScaleDrawer {

	private NumericColorScale scale;

	private Color bgColor;

	private double zoomRangeMin;
	private double zoomRangeMax;

	private int widthPadding;
	private int heightPadding;
	
	private int barSize;
	private Color barBorderColor;

	private boolean legendEnabled;
	private Color legendPointColor;
	private int legendPadding;
	private Font legendFont;
	private String legendFormat;

	public ColorScaleDrawer(IColorScale scale) {
		setScale(scale);

		this.bgColor = Color.WHITE;

		this.widthPadding = 8;
		this.heightPadding = 8;

		this.barSize = 18;
		this.barBorderColor = Color.BLACK;

		this.legendEnabled = true;
		this.legendPointColor = Color.BLACK;
		this.legendPadding = 4;
		this.legendFont = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
		this.legendFormat = "%.3g";
	}

	public IColorScale getScale() {
		return scale;
	}

	public void setScale(IColorScale scale) {
		this.scale = (NumericColorScale) scale;
        resetZoom();
	}

	public void resetZoom() {
        zoomRangeMin = scale.getMinValue();
		zoomRangeMax = scale.getMaxValue();
	}

	public double getZoomRangeMin() {
		return zoomRangeMin;
	}

	public void setZoomRangeMin(double zoomRangeMin) {
		this.zoomRangeMin = zoomRangeMin;
	}

	public double getZoomRangeMax() {
		return zoomRangeMax;
	}

	public void setZoomRangeMax(double zoomRangeMax) {
		this.zoomRangeMax = zoomRangeMax;
	}

	public int getBarSize() {
		return barSize;
	}

	public void setBarSize(int barSize) {
		this.barSize = barSize;
	}

	public void draw(Graphics2D g, Rectangle bounds, Rectangle clip) {

		g.setColor(bgColor);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		
		bounds.x += widthPadding;
		bounds.width -= widthPadding * 2;
		bounds.y += heightPadding;
		bounds.height -= heightPadding * 2;

		int scaleXLeft = bounds.x;
		int scaleXRight = scaleXLeft + bounds.width - 1;
		int scaleYTop = bounds.y;
		int scaleYBottom = scaleYTop + barSize - 1;
        int scaleWidth = scaleXRight - scaleXLeft;
        int scaleHeight = scaleYBottom - scaleYTop;

        ArrayList<ColorScaleRange> ranges = scale.getScaleRanges();

        adjustRangeWidths(scaleWidth, ranges);

        int rangeXLeft = scaleXLeft;
        
        for (ColorScaleRange range : ranges) {


            int rangeWidth = (int) range.getWidth();

            int rangeEnd = rangeXLeft + rangeWidth;

            //paint the colored box of scale range
            if (!(range.getType().equals(ColorScaleRange.EMPTY_TYPE))) {
                for (int x = rangeXLeft; x <= rangeEnd; x++) {
                    double value = getValueForX(x,range.getType(),rangeXLeft,rangeEnd,range.getMinValue(),range.getMaxValue());
                    final Color color = scale.valueColor(value);
                    g.setColor(color);
                    g.drawLine(x, scaleYTop, x, scaleYBottom);
                }
            }

            //paint Border
            if (range.isBorderEnabled()) {
                g.setColor(barBorderColor);
                g.drawRect(rangeXLeft, scaleYTop, rangeWidth, scaleHeight);
            }

            //paint legend
            if (legendEnabled) {

                int lastX = rangeXLeft;
                int fontHeight = g.getFontMetrics().getHeight();
                int legendYTop = scaleYBottom + legendPadding;
                int ye = legendYTop + fontHeight;
                GenericFormatter gf = new GenericFormatter();
                gf.addCustomFormatter(Double.class,this.legendFormat);
                g.setColor(legendPointColor);


                if (range.getLeftLabel() != null) {
                    String legend = gf.format(range.getLeftLabel());
                    int fontWidth = g.getFontMetrics().stringWidth(legend);
                    int legendStart = rangeXLeft + legendPadding;
                    g.drawString(legend, legendStart , ye);
                    lastX = fontWidth + legendPadding;
                }
                if (range.getCenterLabel() != null) {
                    String legend = gf.format(range.getCenterLabel());
                    int fontWidth = g.getFontMetrics().stringWidth(legend);
                    int legendStart = rangeEnd - (rangeWidth/2 + fontWidth/2);
                    if (legendStart > lastX + legendPadding*2) {
                        g.drawString(legend, legendStart , ye);
                        lastX = legendStart + fontWidth + legendPadding;
                    }
                }
                if (range.getRightLabel() != null) {
                    String legend = gf.format(range.getRightLabel());
                    int fontWidth = g.getFontMetrics().stringWidth(legend);
                    int legendStart = rangeXLeft + rangeWidth - legendPadding - fontWidth;
                    if (legendStart > lastX + legendPadding)
                        g.drawString(legend, legendStart , ye);
                }
            }

            rangeXLeft = rangeEnd;

        }
	}

    private double getValueForX(int x, String type, int minX, int maxX, double minValue, double maxValue) {
        if (type.equals(ColorScaleRange.LINEAR_TYPE)) {

            double delta = (maxValue - minValue) / (maxX-minX);
            return minValue + delta * (x-minX);

        }
        else if (type.equals(ColorScaleRange.LOGARITHMIC_TYPE)) {

            double exp = 1.0 * (x - minX) / (maxX-minX);
            return minValue + ((Math.pow(10,exp) - 1) / 9) * (maxValue-minValue);

        } else if (type.equals(ColorScaleRange.CONSTANT_TYPE)) {

            return maxValue;

        }


        return 0;
    }

    private void adjustRangeWidths(int scaleWidth, ArrayList<ColorScaleRange> ranges) {
        double rangesWidth = 0;
        for (ColorScaleRange r : ranges) {
            rangesWidth += r.getWidth();
        }

        HashSet rangesSet = new HashSet<ColorScaleRange>(ranges);
        
        double resizeFactor = scaleWidth / rangesWidth;

        Iterator iter = rangesSet.iterator();
        while (iter.hasNext()) {
            ColorScaleRange r = (ColorScaleRange) iter.next();
            r.setWidth(r.getWidth()*resizeFactor);
        }
    }

    public Dimension getSize() {
		int height = heightPadding * 2 + barSize;
		if (legendEnabled) {
			BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			Graphics g = image.getGraphics();
			height += g.getFontMetrics(legendFont).getHeight() + legendPadding;
		}

		int width = widthPadding + 20;

		return new Dimension(width, height);
	}
}
