void call(def stageName, Closure body) {
    return  pipeline {
        agent any
        stages{
            stage(stageName) {
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
                    postCleanup() // Update to also do docker cleanup too
                }
            }
        }
    }
}