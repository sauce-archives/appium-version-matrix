[![Build Status](https://travis-ci.org/testobject/appium-version-matrix.svg?branch=master)](https://travis-ci.org/testobject/appium-version-matrix)

# appium-version-matrix
Runs basic tests on every Appium version. Modify the array in MatrixTestSetup:appiumVersions to add or remove versions.

# setting up travis ci
1. add a .travis.yml to your project  
2. add your testobject_api_key as encrypted enviroment variables  
```
travis encrypt TESTOBJECT_API_KEY=YOUR_API_KEY --add env.global
```
