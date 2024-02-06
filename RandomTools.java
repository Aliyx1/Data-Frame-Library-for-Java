import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import java.util.*;

/**
 * RandomTools provides utility methods for generating random data based on different probability distributions
 * @author 659358id Ihor Dumanskyi
 */
public class RandomTools {
    private final RealDistribution distribution;

    /**
     * Constructs a RandomTools instance with the specified RealDistribution
     *
     * @param distribution the RealDistribution to use for be used for generating random data
     */
    public RandomTools(RealDistribution distribution) {
        this.distribution = distribution;
    }

    /**
     * Generates random data for a DataFrame using the specified seed, number of rows, and column names
     *
     * @param seed the seed for the random number generator
     * @param rows the number of rows in the DataFrame
     * @param colnames a list of column names for the DataFrame
     * @return a DataFrame containing random data generated based on the distribution
     */
    public DataFrame<Double> generate(long seed, int rows, List<String> colnames) {
        double[][] randomData = new double[rows][colnames.size()];
        distribution.reseedRandomGenerator(seed);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colnames.size(); j++) {
                randomData[i][j] = distribution.sample();
            }
        }
        return new DoubleDataFrame(colnames, randomData);
    }

    /**
     * Creates a RandomTools instance for generating random data with a uniform distribution
     *
     * @param lb the lower bound of the uniform distribution
     * @param ub the upper bound of the uniform distribution
     * @return a RandomTools instance for generating random data with a uniform distribution
     */
    public static RandomTools uniform(double lb, double ub) {
        return new RandomTools(new UniformRealDistribution(lb, ub));
    }

    /**
     * Creates a RandomTools instance for generating random data with a Gaussian (normal) distribution
     *
     * @param mu the mean of the Gaussian distribution
     * @param sigma the standard deviation of the Gaussian distribution
     * @return a RandomTools instance for generating random data with a Gaussian distribution
     */
    public static RandomTools gaussian(double mu, double sigma) {
        return new RandomTools(new NormalDistribution(mu, sigma));
    }

    /**
     * Creates a RandomTools instance for generating random datta with an exponential distribution
     *
     * @param mean the mean of the exponential distribution
     * @return a RandomTools instance for generating random data with an exponential distribution
     */
    public static RandomTools exponential(double mean) {
        return new RandomTools(new ExponentialDistribution(mean));
    }
}


