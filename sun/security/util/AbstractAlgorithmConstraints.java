package sun.security.util;

import java.security.AccessController;
import java.security.AlgorithmConstraints;
import java.security.PrivilegedAction;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * The class contains common functionality for algorithm constraints classes.
 */
public abstract class AbstractAlgorithmConstraints
        implements AlgorithmConstraints {

    protected final AlgorithmDecomposer decomposer;

    protected AbstractAlgorithmConstraints(AlgorithmDecomposer decomposer) {
        this.decomposer = decomposer;
    }

    // Get algorithm constraints from the specified security property.
    static List<String> getAlgorithms(String propertyName) {
        String property = AccessController.doPrivileged(
                new PrivilegedAction<String>() {
                    @Override
                    public String run() {
                        return Security.getProperty(propertyName);
                    }
                });

        String[] algorithmsInProperty = null;
        if (property != null && !property.isEmpty()) {
            // remove double quote marks from beginning/end of the property
            if (property.length() >= 2 && property.charAt(0) == '"' &&
                    property.charAt(property.length() - 1) == '"') {
                property = property.substring(1, property.length() - 1);
            }
            algorithmsInProperty = property.split(",");
            for (int i = 0; i < algorithmsInProperty.length; i++) {
                algorithmsInProperty[i] = algorithmsInProperty[i].trim();
            }
        }

        // map the disabled algorithms
        if (algorithmsInProperty == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(Arrays.asList(algorithmsInProperty));
    }

    static boolean checkAlgorithm(List<String> algorithms, String algorithm,
            AlgorithmDecomposer decomposer) {
        if (algorithm == null || algorithm.length() == 0) {
            throw new IllegalArgumentException("No algorithm name specified");
        }

        Set<String> elements = null;
        for (String item : algorithms) {
            if (item == null || item.isEmpty()) {
                continue;
            }

            // check the full name
            if (item.equalsIgnoreCase(algorithm)) {
                return false;
            }

            // decompose the algorithm into sub-elements
            if (elements == null) {
                elements = decomposer.decompose(algorithm);
            }

            // check the items of the algorithm
            for (String element : elements) {
                if (item.equalsIgnoreCase(element)) {
                    return false;
                }
            }
        }

        return true;
    }

}
