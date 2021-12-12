/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package jenkins.tests

import org.junit.*

class TestJenkinsRegression extends BuildPipelineTest {

    def image
    def args

    @Before
    void setUp() {
        super.setUp()

        binding.setVariable('INPUT_MANIFEST', '../tests/jenkins/data/opensearch-1.3.0.yml')
        binding.setVariable('WEBHOOK_URL', 'http://slack-notification-webhook')
        binding.setVariable('BUILD_URL', 'http://jenkins.us-east-1.elb.amazonaws.com/job/vars/42')
        binding.setVariable('BUILD_NUMBER', '33')
        binding.setVariable('PUBLIC_ARTIFACT_URL', 'https://ci.opensearch.org/dbc')
        binding.setVariable('JOB_NAME', 'vars-build')
        binding.setVariable('ARTIFACT_BUCKET_NAME', 'artifact-bucket')
        binding.setVariable('AWS_ACCOUNT_PUBLIC', 'account')
        binding.setVariable('STAGE_NAME', 'stage')

        helper.registerAllowedMethod("alwaysPull", [boolean], { val ->
            return null
        })
        helper.registerAllowedMethod("docker", [List], {val -> 
            return null
        })

        helper.registerAllowedMethod("withCredentials", [List, Closure], { list, closure ->
            closure.delegate = delegate
            return helper.callClosure(closure)
        })

        helper.registerAllowedMethod("copyArtifactPermission", [String])
        helper.registerAllowedMethod("cleanWs", [Map])
        helper.registerAllowedMethod("findFiles", [Map], { args -> [] })

        helper.registerAllowedMethod("s3Upload", [Map])
        helper.registerAllowedMethod("withAWS", [Map, Closure], { args, closure ->
            closure.delegate = delegate
            return helper.callClosure(closure)
        })

        helper.registerAllowedMethod("git", [Map])
    }

    @Test
    void testOpenSearchDistributionJob() {
        super.testPipeline(
            "jenkins/opensearch/distribution-build.jenkinsfile",
            "tests/jenkins/regression-tests/opensearch/distribution-build"
        )
    }

    @Test
    void testOpenSearchDashboardsDistributionJob() {
        helper.registerAllowedMethod("zip", [Map])

        super.testPipeline(
            "jenkins/opensearch-dashboards/distribution-build.jenkinsfile",
            "tests/jenkins/regression-tests/opensearch-dashboards/distribution-build"
        )
    }

    void setupCheckForBuildWithExistingSha(boolean existingSha) {
        binding.setVariable('TARGET_JOB_NAME', "distribution-build-opensearch")

        helper.registerAllowedMethod('parameterizedCron', [String])
        helper.registerAllowedMethod("lock", [Map, Closure], { args, closure ->
            closure.delegate = delegate
            return helper.callClosure(closure)
        })
        helper.registerAllowedMethod("sha1", [String], { filename ->
            return 'sha1'
        })

        helper.registerAllowedMethod("s3DoesObjectExist", [Map], { args ->
            return existingSha
        })
    }

    @Test
    void testCheckForBuildJob_Skip() {
        setupCheckForBuildWithExistingSha(true)
        super.testPipeline(
            "jenkins/check-for-build.jenkinsfile",
            "tests/jenkins/regression-tests/check-for-build-existing-sha-true"
        )
    }
    
    @Test
    void testCheckForBuildJob_Complete() {
        setupCheckForBuildWithExistingSha(false)

        super.testPipeline(
            "jenkins/check-for-build.jenkinsfile",
            "tests/jenkins/regression-tests/check-for-build-existing-sha-false"
        )
    }

}
