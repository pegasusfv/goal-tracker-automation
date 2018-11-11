package stepdefs;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.Before;
import org.json.simple.JSONObject;
import org.junit.Assert;
import util.RestUtil;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static util.RestUtil.*;

/**
 * Created by guall on 08/11/2018.
 */
public class GoalTrackerStepDefs {

    private int goalsCountBefore;
    private boolean applicationUp;
    private JSONObject requestParams = new JSONObject();
    private Response response;
    private Response secondResponse;
    private Map<String, Object> lastGoal;
    private RestUtil restUtil = new RestUtil();

    @Before("@countGoalsBefore")
    public void beforeFirst(){
        goalsCountBefore = getGoalList(getGoals()).size();
    }

    @Given("^the property:\"([^\"]*)\" with value:\"([^\"]*)\"$")
    public void the_property_with_value(String key, String value) throws Throwable {
        if(value.equals("null"))
            requestParams.put(key, null);
        else
            requestParams.put(key, value);
    }

    @Given("^the property:\"([^\"]*)\" with value:(\\d+)$")
    public void the_property_with_value(String key, int value) throws Throwable {
        requestParams.put(key, value);
    }

    @When("^I get the hello service$")
    public void i_get_the_hello_service() throws Throwable {
        response = restUtil.get(HELLO_SERVICE);
    }


    @When("^I get the goals service$")
    public void i_get_the_goals_service() throws Throwable {
        response = getGoals();
    }


    @Then("^there is a new goal added$")
    public void there_is_a_new_goal_added() throws Throwable {
        secondResponse = getGoals();
        Assert.assertThat(getGoalList(secondResponse).size(), is(goalsCountBefore + 1));
    }

    @When("^I send the request to addGoal service$")
    public void i_send_the_request_to_addGoal_service() throws Throwable {
        Response temporalResponse = restUtil.post(ADDGOAL_SERVICE, requestParams.toJSONString());
        temporalResponse.then().assertThat().statusCode(RestUtil.TEMPORARILY_REDIRECT);
        String headerLocationValue = temporalResponse.getHeader("Location");
        response = restUtil.getRedirected(headerLocationValue);
    }

    @Then("^the response code is \"(\\d+)\"$")
    public void the_response_code_is(int expectedHttpCode) throws Throwable {
        response.then().assertThat().statusCode(expectedHttpCode);

    }

    @Then("^response string field \"([^\"]*)\" is \"([^\"]*)\"$")
    public void response_string_field_is(String key, String value) throws Throwable {
        checkValue(key, value);
    }

    @Then("^response int field \"([^\"]*)\" is \"([^\"]*)\"$")
    public void response_int_field_is(String key, int value) throws Throwable {
        checkValue(key, value);
    }

    @Then("^response long field \"([^\"]*)\" is \"([^\"]*)\"$")
    public void response_long_field_is(String key, long value) throws Throwable {
        checkValue(key, value);
    }


    @When("^I ping the host$")
    public void i_ping_the_host_to_check_if_up() throws Throwable {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", 8080), 10000);
            applicationUp = socket.isConnected();
        }
    }

    @Then("^application is up$")
    public void application_is_up() throws Throwable {
        Assert.assertTrue(applicationUp);
    }

    @When("^I get the goals service again$")
    public void i_get_the_goals_service_again() throws Throwable {
        secondResponse = getGoals();
    }

    @When("^both results are identical$")
    public void both_results_are_identical() throws Throwable {
        Assert.assertEquals(response.getBody().asString(), secondResponse.getBody().asString());
    }

    @Then("^all IDs are unique$")
    public void all_IDs_are_unique() throws Throwable {
        Set<String> idSet = new HashSet<>();
        Assert.assertTrue(getGoalList(response)
                .stream()
                .allMatch(goal -> idSet.add(goal.get("id").toString())));
    }

    private void setLastAddedGoal(){
        if(lastGoal == null) {
            List<Map<String, Object>> goalList = getGoalList(response);
            lastGoal = goalList.get(goalList.size() - 1);
        }
    }

    private List<Map<String, Object>> getGoalList(Response response){
        JsonPath jsonPath = new JsonPath(response.asString());
        return jsonPath.getList ("");
    }

    private void checkValue(String key, Object expectedValue){
        setLastAddedGoal();
        Assert.assertThat(lastGoal.get(key), is(expectedValue));
    }

    private Response getGoals() {
        return restUtil.get(GOALS_SERVICE);
    }
}
