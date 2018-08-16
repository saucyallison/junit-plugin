*JUnit Jenkins Plugin*

This was forked in order to prevent the test reporting from marking builds as
`UNSTABLE` (yellow). The plugin claims that it won't affect the build status
when the `healthScoreFactor` is set to 0, but that simply isn't the case, as
it doesn't actually check the `healthScoreFactor` before setting `UNSTABLE`.

Now it does.