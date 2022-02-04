void call() {
    return  pipeline {
        agent any
        stages{
            stage("Stage") {
                steps {
                    script {
                        echo "hello"
//                        body()
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