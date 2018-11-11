import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by guall on 08/11/2018.
 */

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        plugin = { "pretty", "html:target/cucumber-reports" },
        monochrome = true
)
public class CucumberTest {
}
