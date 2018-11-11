To run, you will need:

* [Maven 3](http://maven.apache.org) or above installed
* goal-tracker application running on local port 8080

- To run all tests locally:
      mvn clean verify -Plocal

- To run smoke tests locally:
      mvn clean verify -Plocal -Dcucumber.options="--tags @smoke"

- To run regression tests locally:
      mvn clean verify -Plocal -Dcucumber.options="--tags @regression"

