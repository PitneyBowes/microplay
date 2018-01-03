def __JOB_PROPERTIES() {
    [
            [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '100']],
            [$class: 'ParametersDefinitionProperty', parameterDefinitions: [
                    [$class: 'StringParameterDefinition', name: 'artifactory_user', defaultValue: 'upload', description: 'artifactory user'],
                    [$class: 'StringParameterDefinition', name: 'artifactory_pass', defaultValue: 'upload', description: 'artifactory password'],
                    [$class: 'StringParameterDefinition', name: 'artifact_version', defaultValue: 'latest', description: 'the artifact version'],
            ]
            ]
    ]
}
__jobProperties(__JOB_PROPERTIES())

@Library('global-pipeline-utils@v1.40') _

timestamps {
    node('generic') {
        def GIT_BRANCH_NAME = env.BRANCH_NAME
        echo "git branch: $GIT_BRANCH_NAME"
        stage('Checkout') {
            checkout scm
        }
        predefinedVersion = getVersion()
        if (!artifact_version || artifact_version == "latest") {
            artifact_version = "${predefinedVersion}-${env.BUILD_NUMBER}"
        }
        echo "artifact_version: $artifact_version"
        if(GIT_BRANCH_NAME != 'master'){
            artifact_version+= ("-"+GIT_BRANCH_NAME)
        }
        currentBuild.displayName = artifact_version
        def sbtOptions = "-Dsbt.log.noformat=true -Dversion=${artifact_version} " +
                "-Djdk.logging.allowStackWalkSearch=true " +
                "-Dsbt.repository.config=.sbt/repositories -Dsbt.override.build.repos=true -Dsbt.override.build.repos=true " +
                "-Dartifactory_user=${artifactory_user} -Dartifactory_password=${artifactory_pass}"
        stage('Build, UnitTest, IntegrationTest') {
            sh "sbt ${sbtOptions} microplay-lib/test microplay-lib/it:test microplay-lib/publishLocal"
        }
        //todo test dependant project
        stage('Publish') {
            sh "sbt ${sbtOptions} microplay-lib/publish"
        }
    }
}

def getVersion() {
    def version = readFile('version.txt').trim().split('\n')[0].trim()
    println "version:${version}"
    version
}
