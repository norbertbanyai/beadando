package core;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	CalculationsDayOffsTests.class,
	CalculationsSalaryTest.class,
	CalculationsSickDaysTests.class,
	CalculationsWorkTests.class
})
public class CalculationsSuite {
	
}
