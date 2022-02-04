void call() {
    return { label 'Jenkins-Agent-al2-x64-c54xlarge-Docker-Host'
            image 'opensearchstaging/ci-runner:ci-runner-centos7-v1'
            alwaysPull true
    }
}