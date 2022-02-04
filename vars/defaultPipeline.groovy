void call(def stageName, Closure body) {
    return  pipeline {
        agent {
            docker {
                label 'Jenkins-Agent-al2-x64-c54xlarge-Docker-Host'
                image 'opensearchstaging/ci-runner:centos7-x64-arm64-jdkmulti-node10.24.1-cypress6.9.1-20211028'
                alwaysPull true
            }
        }
        stages(stageName) {
            stage {
                body()
            }
        }
        post {
            always {
                script {
                    postCleanUp() // Update to also do docker cleanup too
                }
            }
        }
    }
}