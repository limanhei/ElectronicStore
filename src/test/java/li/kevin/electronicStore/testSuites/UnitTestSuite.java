package li.kevin.electronicStore.testSuites;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages({"li.kevin.electronicStore.services"})
@SuiteDisplayName("Unit test")
public class UnitTestSuite {
}
