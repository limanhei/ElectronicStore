package li.kevin.electronicStore.testSuites;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages({"li.kevin.electronicStore.controllers"})
@SuiteDisplayName("E2E test")
public class E2ETestSuite {
}
