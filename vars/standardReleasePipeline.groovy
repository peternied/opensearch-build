/** A standard release pipeline for OpenSearch projects */

void call(Map args = [:], Closure body) {
    pipeline {
        agent any
        // Note; testing this on custom jenkins instance that doesn't
        // have docker installed
        // {
        //     docker {
        //         label 'AL2-X64'
        //         image args.overrideDockerImage ?: 'alpine:latest'
        //         alwaysPull true
        //     }
        // }
        options {
            timeout(time: 1, unit: 'HOURS')
        }
        stages{
            stage("Release") {
                steps {
                    script {
                        body()
                    }
                }
            }
        }
        post {
            always {
                script {
                    postCleanup()
                    // Update to also do docker cleanup too
                    if (args.cleanup && args.cleanup instanceof Closure) {
                        cleanup()
                    }
                }
            }
        }
    }
}