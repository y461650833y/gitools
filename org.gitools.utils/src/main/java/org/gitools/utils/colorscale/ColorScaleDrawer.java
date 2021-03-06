/*
 * #%L
 * gitools-utils
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.utils.colorscale;

import org.gitools.utils.formatter.ITextFormatter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class ColorScaleDrawer {

    private NumericColorScale scale;

    private final Color bgColor;

    private final int widthPadding;
    private final int heightPadding;

    private int barSize;
    private final Color barBorderColor;

    private final boolean legendEnabled;
    private final Color legendPointColor;
    private final int legendPadding;
    private final Font legendFont;
    private ITextFormatter textFormatter;


    public ColorScaleDrawer(INumericColorScale scale, ITextFormatter textFormatter) {
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

        this.textFormatter = textFormatter;
    }

    public INumericColorScale getScale() {
        return scale;
    }

    void setScale(INumericColorScale scale) {
        this.scale = (NumericColorScale) scale;
    }

    public void draw(Graphics2D g, Rectangle bounds, Rectangle clip, String name) {

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

        double minRange = adjustRangeWidths(scaleWidth, ranges);

        calculateFontSize(g, Double.valueOf(minRange).intValue(), 8);

        int rangeXLeft = scaleXLeft;

        for (ColorScaleRange range : ranges) {


            int rangeWidth = (int) range.getWidth();

            int rangeEnd = rangeXLeft + rangeWidth;

            //paint the colored box of scale range
            if (!(range.getType().equals(ColorScaleRange.EMPTY_TYPE))) {
                for (int x = rangeXLeft; x <= rangeEnd; x++) {
                    double value = getValueForX(x, range.getType(), rangeXLeft, rangeEnd, range.getMinValue(), range.getMaxValue());
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

                g.setColor(legendPointColor);


                if (range.getLeftLabel() != null) {
                    String legend = textFormatter.format(range.getLeftLabel());
                    int fontWidth = g.getFontMetrics().stringWidth(legend);
                    int legendStart = rangeXLeft + legendPadding;
                    g.drawString(legend, legendStart, ye);
                    lastX = fontWidth + legendPadding;
                }
                if (range.getCenterLabel() != null) {
                    String legend = textFormatter.format(range.getCenterLabel());
                    int fontWidth = g.getFontMetrics().stringWidth(legend);
                    int legendStart = rangeEnd - (rangeWidth / 2 + fontWidth / 2);
                    if (legendStart > lastX) {
                        g.drawString(legend, legendStart, ye);
                        lastX = legendStart + fontWidth + legendPadding;
                    }
                }
                if (range.getRightLabel() != null) {
                    String legend = textFormatter.format(range.getRightLabel());
                    int fontWidth = g.getFontMetrics().stringWidth(legend);
                    int legendStart = rangeXLeft + rangeWidth - legendPadding - fontWidth;
                    if (legendStart > lastX) {
                        g.drawString(legend, legendStart, ye);
                    }
                }
            }

            rangeXLeft = rangeEnd;

        }

        g.setFont(g.getFont().deriveFont(Font.BOLD));
        g.drawString(name, scaleXLeft, scaleYBottom + legendPadding * 2 + g.getFontMetrics().getHeight()*2);

    }

    private double getValueForX(int x, String type, int minX, int maxX, double minValue, double maxValue) {
        if (type.equals(ColorScaleRange.LINEAR_TYPE)) {

            double delta = (maxValue - minValue) / (maxX - minX);
            return minValue + delta * (x - minX);

        } else if (type.equals(ColorScaleRange.LOGARITHMIC_TYPE)) {

            double exp = 1.0 * (x - minX) / (maxX - minX);
            return minValue + ((Math.pow(10, exp) - 1) / 9) * (maxValue - minValue);

        } else if (type.equals(ColorScaleRange.CONSTANT_TYPE)) {

            return maxValue;

        }

        return 0;
    }

    private double adjustRangeWidths(int scaleWidth, ArrayList<ColorScaleRange> ranges) {
        double rangesWidth = 0;
        double minRange = Double.MAX_VALUE;
        for (ColorScaleRange r : ranges) {
            rangesWidth += r.getWidth();
        }

        HashSet rangesSet = new HashSet<>(ranges);

        double resizeFactor = scaleWidth / rangesWidth;

        Iterator iter = rangesSet.iterator();
        while (iter.hasNext()) {
            ColorScaleRange r = (ColorScaleRange) iter.next();
            double newWidth = r.getWidth() * resizeFactor;
            r.setWidth(newWidth);

            if (newWidth < minRange) {
                minRange = newWidth;
            }
        }

        return minRange;
    }


    public Dimension getSize() {
        int height = heightPadding * 2 + barSize;
        if (legendEnabled) {
            BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            height += (g.getFontMetrics(legendFont).getHeight() + legendPadding) * 2 ;
        }

        int width = widthPadding + 20;

        return new Dimension(width, height);
    }

    /**
     * Automatically change the font size to fit in the range width
     *
     * @param g           the Graphics2D object
     * @param rangeWidth  the cell height
     * @param minFontSize the min font size
     * @return Returns true if the new font size fits in the cell height, false if it doesn't fit.
     */
    protected static boolean calculateFontSize(Graphics2D g, int rangeWidth, int minFontSize) {

        float fontHeight = g.getFontMetrics().getHeight();

        float fontSize = g.getFont().getSize();
        while (fontHeight > (rangeWidth - 2) && fontSize > minFontSize) {
            fontSize--;
            g.setFont(g.getFont().deriveFont(fontSize));
            fontHeight = g.getFontMetrics().getHeight();
        }

        return fontHeight <= (rangeWidth - 2);
    }
}
