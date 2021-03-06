/*
 * #%L
 * gitools-core
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
package org.gitools.analysis.stats.test;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.gitools.analysis.stats.test.results.GroupComparisonResult;


public class MannWhitneyWilcoxonTest extends AbstractEnrichmentTest {

    private NaturalRanking naturalRanking;

    public MannWhitneyWilcoxonTest() {
        super("mannWhitneyWilcoxon", GroupComparisonResult.class);
        naturalRanking = new NaturalRanking(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
    }

    public GroupComparisonResult processTest(Iterable<Double> group1, Iterable<Double> group2) {


        double[] x = Doubles.toArray(Lists.newArrayList(group1));
        double[] y = Doubles.toArray(Lists.newArrayList(group2));

        if (x.length == 0 || y.length == 0) {
            getNullResult(x.length + y.length, x.length, y.length);
        }

        final double[] z = concatenateSamples(x, y);
        final double[] ranks = naturalRanking.rank(z);

        double mean1 = StatUtils.mean(x);
        double mean2 = StatUtils.mean(y);

        double sumRankX = 0;

        /*
         * The ranks for x is in the first x.length entries in ranks because x
         * is in the first x.length entries in z
         */
        for (int i = 0; i < x.length; ++i) {
            sumRankX += ranks[i];
        }

        /*
         * U1 = R1 - (n1 * (n1 + 1)) / 2 where R1 is sum of ranks for sample 1,
         * e.g. x, n1 is the number of observations in sample 1.
         */
        final double U1 = sumRankX - (x.length * (x.length + 1)) / 2;

        /*
         * It can be shown that U1 + U2 = n1 * n2
         */
        final double U2 = x.length * y.length - U1;

        final double Umin = FastMath.min(U1, U2);

        double oneTail = calculateOneTailPValue(Umin, x.length, y.length);

        boolean firstGreater = U1 > U2;

        // if U1 smaller use computed value for left tail
        double leftTail = firstGreater ? 1 - oneTail : oneTail;
        // if U2 smaller, computed value for right tail
        double rightTail = firstGreater ? oneTail : 1 - oneTail;
        double twoTail = 2 * oneTail;

        return new GroupComparisonResult(x.length + y.length, x.length, y.length, leftTail, rightTail, twoTail, mean1, mean2, U1, U2);

    }

    public static GroupComparisonResult getNullResult(int n, int n1, int n2) {
        return new GroupComparisonResult(n, n1, n2, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
    }

    /**
     * Concatenate the samples into one array.
     *
     * @param x first sample
     * @param y second sample
     * @return concatenated array
     */
    private double[] concatenateSamples(final double[] x, final double[] y) {
        final double[] z = new double[x.length + y.length];

        System.arraycopy(x, 0, z, 0, x.length);
        System.arraycopy(y, 0, z, x.length, y.length);

        return z;
    }


    private double calculateOneTailPValue(final double Umin, final int n1, final int n2) throws ConvergenceException, MaxCountExceededException {

        /* long multiplication to avoid overflow (double not used due to efficiency
         * and to avoid precision loss)
         */
        final long n1n2prod = (long) n1 * n2;

        // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
        final double EU = n1n2prod / 2.0;
        final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;

        final double z = (Umin - EU) / FastMath.sqrt(VarU);

        // No try-catch or advertised exception because args are valid
        final NormalDistribution standardNormal = new NormalDistribution(0, 1);

        return standardNormal.cumulativeProbability(z);
    }

}
