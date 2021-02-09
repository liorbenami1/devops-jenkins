pipeline {
    agent { docker 'maven:3.5-alpine' }
    stages {
        stage ('Checkout') {
          steps {
            git 'https://github.com/lev-tmp/jenkins2-course-spring-petclinic.git'
          }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package'
                junit '**/target/surefire-reports/TEST-*.xml'
                //archiveArtifacts artifacts: 'target/*.war', fingerprint: true
                nexusPublisher nexusInstanceId: 'my-repo', 
                    nexusRepositoryId: 'my-maven2', 
                    packages: [[$class: 'MavenPackage', 
                                mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'target/']], 
                                mavenCoordinate: [artifactId: 'simple-maven-project-with-tests', 
                                                  groupId: 'org.apache.maven', 
                                                  packaging: 'simple-maven-project-with-tests-1.0-SNAPSHOT', 
                                                  version: '1.0']
                               ]
                              ], 
                    tagName: 'DEV'
            }
        }
    }
}
