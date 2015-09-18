[![Build Status](https://travis-ci.org/testobject/appium-test-setup-basic.svg?branch=master)](https://travis-ci.org/testobject/appium-test-setup-basic)

# appium-test-setup-basic
The most basic test setup to run Appium tests on the TestObject platform, exemplified through two basic tests on the [Calculator app](https://github.com/aluedeke/calculator) by Andreas Lüdeke.

# setting up travis ci
1. add a .travis.yml to your project  
2. add your testobject_api_key as encrypted enviroment variables  
```
travis encrypt TESTOBJECT_API_KEY=YOUR_API_KEY --add env.global
```