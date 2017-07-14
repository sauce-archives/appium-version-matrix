#!groovy

def runTest() {
    node {
        stage("checkout") {
            checkout scm
        }
        stage("staging test") {
            try {
                docker.image("java:8").inside {
                    sh "./gradlew clean test -i"
                }
            } finally {
                junit "**/test-results/*.xml"
            }
        }
    }
}

if (env.TESTOBJECT_APPIUM_ENDPOINT.contains("staging.testobject.org")) {
    // Can't lock device; we don't know what we're testing on. Don't run this on staging for now.
} else {
    try {
        runTest()
        if (env.SUCCESS_NOTIFICATION_ENABLED) {
            slackSend channel: "#${env.SLACK_CHANNEL}", color: "good", message: "`${env.JOB_BASE_NAME}` passed (<${BUILD_URL}|open>)", teamDomain: "${env.SLACK_SUBDOMAIN}", token: "${env.SLACK_TOKEN}"
        }
    } catch (err) {
        if (env.TESTOBJECT_APPIUM_ENDPOINT.contains("testobject.com") || env.FAILURE_NOTIFICATION_ENABLED) {
            slackSend channel: "#${env.SLACK_CHANNEL}", color: "bad", message: "`${env.JOB_BASE_NAME}` failed: $err (<${BUILD_URL}|open>)", teamDomain: "${env.SLACK_SUBDOMAIN}", token: "${env.SLACK_TOKEN}"
        }
        throw err
    }
}
