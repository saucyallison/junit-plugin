**JUnit Jenkins Plugin**

This was forked in order to prevent the test reporting from marking builds as
`UNSTABLE` (yellow). The plugin claims that it won't affect the build status
when the `healthScoreFactor` is set to 0, but that simply isn't the case, as
it doesn't actually check the `healthScoreFactor` before setting `UNSTABLE`.

Now it does.


**Building the plugin**

1. `mvn install`
2. Find `junit.hpi` in `./target`

If you don't have Java/maven set up locally, use the maven docker container.
Just `docker pull maven` and run it, voluming in the plugin directory via
something like `docker run -v /path/to/repo:/junit -it maven /bin/bash`.

For more info on plugin building, see https://wiki.jenkins.io/display/JENKINS/Plugin+tutorial#Plugintutorial-BuildingaPlugin