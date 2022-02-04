void call(Closure body) {
    return  pipeline {
        agent any
        stages{
            stage("Stage") {
                steps {
                    script {
                        echo "hello"
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