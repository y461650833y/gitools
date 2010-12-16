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

package org.gitools.stats;

public class HyperGeometric {
	private int sn11, sn1_, sn_1, sn;
	private double sprob;

	private double lngamm(double z)
	// Reference: "Lanczos, C. 'A precision approximation
	// of the gamma function', J. SIAM Numer. Anal., B, 1, 86-96, 1964."
	// Translation of Alan Miller's FORTRAN-implementation
	// See http://lib.stat.cmu.edu/apstat/245
	{
		double x = 0;
		x += 0.1659470187408462e-06 / (z + 7);
		x += 0.9934937113930748e-05 / (z + 6);
		x -= 0.1385710331296526 / (z + 5);
		x += 12.50734324009056 / (z + 4);
		x -= 176.6150291498386 / (z + 3);
		x += 771.3234287757674 / (z + 2);
		x -= 1259.139216722289 / (z + 1);
		x += 676.5203681218835 / (z);
		x += 0.9999999999995183;
		return (Math.log(x) - 5.58106146679532777 - z + (z - 0.5)
				* Math.log(z + 6.5));
	}

	private double lnfact(int n) {
		if (n <= 1)
			return (0);
		return (lngamm(n + 1));
	}

	private double lnbico(int n, int k) {
		return (lnfact(n) - lnfact(k) - lnfact(n - k));
	}

	private double hyper_323(int n11, int n1_, int n_1, int n) {
		return (Math.exp(lnbico(n1_, n11) + lnbico(n - n1_, n_1 - n11)
				- lnbico(n, n_1)));
	}

	public double hyper(int n11) {
		return (hyper0(n11, 0, 0, 0));
	}

	public double hyper0(int n11i, int n1_i, int n_1i, int ni) {
		if (!(n1_i != 0 || n_1i != 0 || ni != 0)) {
			if (!(n11i % 10 == 0)) {
				if (n11i == sn11 + 1) {
					sprob *= ((double) (sn1_ - sn11) / (double) n11i)
							* ((double) (sn_1 - sn11) / (double) (n11i + sn
									- sn1_ - sn_1));
					sn11 = n11i;
					return sprob;
				}
				if (n11i == sn11 - 1) {
					sprob *= ((double) sn11 / (double) (sn1_ - n11i))
							* ((double) (sn11 + sn - sn1_ - sn_1) / (double) (sn_1 - n11i));
					sn11 = n11i;
					return sprob;
				}
			}
			sn11 = n11i;
		} else {
			sn11 = n11i;
			sn1_ = n1_i;
			sn_1 = n_1i;
			sn = ni;
		}
		sprob = hyper_323(sn11, sn1_, sn_1, sn);
		return sprob;
	}
}