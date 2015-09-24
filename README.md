[![Build Status](https://travis-ci.org/testobject/appium-version-matrix.svg?branch=master)](https://travis-ci.org/testobject/appium-version-matrix)

# appium-test-random-device
Selects a random available Android device from TestObject, then runs a basic test on it with the Calculator 2.0 app.

Note that the app requires the environment variable TESTOBJECT_API_KEY to be set; you can do this in Travis or locally.

# Building with Travis
The `.travis.yml` file included refers to multiple Appium versions. In a Travis build, the tests will be run on every Appium version specified here.