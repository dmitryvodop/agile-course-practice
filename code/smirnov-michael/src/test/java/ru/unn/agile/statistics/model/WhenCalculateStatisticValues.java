package ru.unn.agile.statistics.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class WhenCalculateStatisticValues {
    @Before
    public void preparing() {
        statisticsCalculator = new StatisticValuesCalculator();
        numericalConverter = new NumericStatisticsConverter();
        dataInstances = null;
    }

    @Test
    public void enumerationOfSimpleIntArrayIsCorrect() {
        Integer[] data = {5, 5, 5, 5};
        formDataInstances(data);

        assertEqualsForDoublesWithStandardDelta(5.0,
                statisticsCalculator.calculateEnumeration());
    }

    @Test
    public void enumerationOfIntArrayWithDifferentElementsIsCorrect() {
        formDataInstances(formBigIntArray());

        assertEqualsForDoublesWithStandardDelta(49.5,
                statisticsCalculator.calculateEnumeration());
    }

    @Test
    public void enumerationOfDoubleArrayWithDataOfOneCosPeriodIsCorrect() {
        formDataInstances(formBigDoubleArray());

        assertEqualsForDoublesWithStandardDelta(0.0,
                statisticsCalculator.calculateEnumeration());
    }

    @Test
    public void enumerationOfFloatArrayWithOneElementIsCorrect() {
        Float[] data = {3.14f};
        formDataInstances(data);

        assertEqualsForDoublesWithStandardDelta(3.14f,
                statisticsCalculator.calculateEnumeration());
    }

    @Test
    public void enumerationIsZeroWhenStatisticDataIsEmpty() {
        statisticsCalculator.setStatisticData(null);

        assertEqualsForDoublesWithStandardDelta(0.0,
                statisticsCalculator.calculateEnumeration());
    }

    @Test
    public void statisticDataIsEmptyWhenConvertingIntArrayIsEmpty() {
        numericalConverter.setData(null);
        Collection<IStatisticDataInstance> dataInstances =
                numericalConverter.convertDataToStatistics();

        assertEquals(dataInstances, null);
    }

    @Test
    public void probabilityOfSingleEventEqualsToOne() {
        Integer[] data = {7};
        IStatisticDataInstance event = new NumericStatisticDataInstance(data[0]);
        formDataInstances(data);

        assertEqualsForDoublesWithStandardDelta(1,
                statisticsCalculator.calculateProbabilityOfEvent(event));
    }

    @Test
    public void probabilityOfImpossibleEventEqualsToZero() {
        Float[] data = {1.f, 2.f, 3.f, 4.1f, 5.f, 6.f, 7.f, 8.f, 9.f, 10.f};
        IStatisticDataInstance event = new NumericStatisticDataInstance(4);
        formDataInstances(data);

        assertEqualsForDoublesWithStandardDelta(0.0,
                statisticsCalculator.calculateProbabilityOfEvent(event));
    }

    @Test
    public void probabilityOfSomePossibleEventIsCorrect() {
        Double[] data = {-1., 3.14, 5., 7.13, 8.25, 3.14};
        IStatisticDataInstance event = new NumericStatisticDataInstance(3.14f);
        formDataInstances(data);

        assertEqualsForDoublesWithStandardDelta(1. / 3,
                statisticsCalculator.calculateProbabilityOfEvent(event));
    }

    @Test
    public void probabilityWithoutDataIsZero() {
        Double[] data = null;
        IStatisticDataInstance event = new NumericStatisticDataInstance(0);
        formDataInstances(data);

        assertEqualsForDoublesWithStandardDelta(0.0,
                statisticsCalculator.calculateProbabilityOfEvent(event));
    }

    @Test
    public void varianceOfConstantDataIsZero() {
        Integer[] data = {1, 1, 1, 1, 1};
        formDataInstances(data);

        assertEqualsForDoublesWithStandardDelta(0.0,
                statisticsCalculator.calculateVariance());
    }

    @Test
    public void varianceWithoutDataIsZero() {
        Integer[] data = null;
        formDataInstances(data);

        assertEqualsForDoublesWithStandardDelta(0.0,
                statisticsCalculator.calculateVariance());
    }

    @Test
    public void varianceOfSingleDataInstanceIsZero() {
        Double[] data = {1.72};
        formDataInstances(data);

        assertEqualsForDoublesWithStandardDelta(0.0,
                statisticsCalculator.calculateVariance());
    }

    @Test
    public void varianceOfDataRepresentsOneCosPeriodIsEqualToOneHalf() {
        formDataInstances(formBigDoubleArray());

        assertEqualsForDoublesWithStandardDelta(1. / 2,
                statisticsCalculator.calculateVariance());
    }

    @Test
    public void rawMomentOfThirdOrderWithoutDataIsZero() {
        Integer[] data = null;
        formDataInstances(data);

        assertEqualsForDoublesWithStandardDelta(0.0,
                statisticsCalculator.calculateRawMoment(3));
    }

    @Test
    public void rawMomentOfFirstOrderIsEqualToEnumeration() {
        formDataInstances(formBigDoubleArray());

        assertEqualsForDoublesWithStandardDelta(
                statisticsCalculator.calculateRawMoment(1),
                statisticsCalculator.calculateEnumeration());
    }

    @Test
    public void rawMomentOfSecondOrderSmallerThanFourthOrderWhenDataHasBigVariance() {
        formDataInstances(formShortIntArray());

        assertTrue(
                statisticsCalculator.calculateRawMoment(2)
                        < statisticsCalculator.calculateRawMoment(4)
        );
    }

    @Test
    public void rawMomentOfSixOrderBiggerThanEighthOrderWhenDataHasSmallVariance() {
        formDataInstances(formShortDoubleArray());

        assertTrue(
                statisticsCalculator.calculateRawMoment(6)
                        > statisticsCalculator.calculateRawMoment(8)
        );
    }

    @Test
    public void rawMomentOfZeroOrderIsEqualsToZero() {
        formDataInstances(formShortDoubleArray());

        assertEqualsForDoublesWithStandardDelta(0.0,
                statisticsCalculator.calculateRawMoment(0));
    }

    @Test
    public void rawMomentOfNegativeOrderIsEqualsToZero() {
        formDataInstances(formShortFloatArray());

        assertEqualsForDoublesWithStandardDelta(0.0,
                statisticsCalculator.calculateRawMoment(-5));
    }

    @Test
    public void centralMomentOfEmptyDataEqualsToZero() {
        Integer[] data = null;
        formDataInstances(data);

        assertEqualsForDoublesWithStandardDelta(0.0,
                statisticsCalculator.calculateCentralMoment(2));
    }

    @Test
    public void centralMomentOfConstantDataEqualsToZero() {
        formDataInstances(formShortFloatArray());

        assertEqualsForDoublesWithStandardDelta(0.0,
                statisticsCalculator.calculateCentralMoment(1));
    }

    @Test
    public void centralMomentOfSecondOrderEqualsToUnbiasedVariance() {
        Double[] data = formShortDoubleArray();
        formDataInstances(data);

        double unbiasedVariance = ((double) (data.length - 1)) / data.length
                * statisticsCalculator.calculateVariance();
        assertEqualsForDoublesWithStandardDelta(unbiasedVariance,
                statisticsCalculator.calculateCentralMoment(2));
    }

    @Test
    public void centralMomentOfSecondOrderSmallerThanFourthOrderWhenDataHasBigVariance() {
        formDataInstances(formShortIntArray());

        assertTrue(
                statisticsCalculator.calculateCentralMoment(2)
                        < statisticsCalculator.calculateCentralMoment(4)
        );
    }

    @Test
    public void centralMomentOfSixOrderBiggerThanEighthOrderWhenDataHasSmallVariance() {
        formDataInstances(formShortDoubleArray());

        assertTrue(
                statisticsCalculator.calculateCentralMoment(6)
                        > statisticsCalculator.calculateCentralMoment(8));
    }

    @Test
    public void centralMomentOfNegativeOrderIsEqualsToZero() {
        formDataInstances(formShortIntArray());

        assertEqualsForDoublesWithStandardDelta(0.0,
                statisticsCalculator.calculateCentralMoment(-3));
    }

    @Test
    public void centralMomentOfZeroOrderIsEqualsToZero() {
        formDataInstances(formShortDoubleArray());

        assertEqualsForDoublesWithStandardDelta(0.0,
                statisticsCalculator.calculateCentralMoment(0));
    }

    private Integer[] formShortIntArray() {
        Integer[] result = {-1, -1, 5, 8, 10, 4, 4, 8};
        return result;
    }

    private Integer[] formBigIntArray() {
        Integer[] result = new Integer[100];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }

    private Float[] formShortFloatArray() {
        Float[] result = {1.f, 2.f, 3.14f, 5.25f, 6.37f};
        return result;
    }

    private Double[] formShortDoubleArray() {
        Double[] result = {-0.1, -0.1, 0.5, 0.8, 1.0, 0.4, 0.4, 0.8};
        return result;
    }

    private Double[] formBigDoubleArray() {
        Double[] result = new Double[1000];
        for (int i = 0; i < result.length; i++) {
            result[i] = Math.cos(i * 2 * Math.PI / 1000.0);
        }
        return result;
    }

    private void assertEqualsForDoublesWithStandardDelta(final double firstValue,
                                                         final double secondValue) {
        assertEquals(firstValue, secondValue, deltaForDoubleAssertEquals);
    }

    private void formDataInstances(final Double[] data) {
        numericalConverter.setData(data);

        Collection<IStatisticDataInstance> dataInstances =
                numericalConverter.convertDataToStatistics();

        statisticsCalculator.setStatisticData(dataInstances);
    }

    private void formDataInstances(final Float[] data) {
        numericalConverter.setData(data);

        Collection<IStatisticDataInstance> dataInstances =
                numericalConverter.convertDataToStatistics();

        statisticsCalculator.setStatisticData(dataInstances);
    }

    private void formDataInstances(final Integer[] data) {
        numericalConverter.setData(data);

        Collection<IStatisticDataInstance> dataInstances =
                numericalConverter.convertDataToStatistics();

        statisticsCalculator.setStatisticData(dataInstances);
    }

    private StatisticValuesCalculator statisticsCalculator;
    private Collection<IStatisticDataInstance> dataInstances;
    private double deltaForDoubleAssertEquals = 1e-3;
    private NumericStatisticsConverter numericalConverter;
}
