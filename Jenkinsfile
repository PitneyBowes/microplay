def __JOB_PROPERTIES() {
    [
            [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '30']],
            [$class: 'DisableConcurrentBuildsJobProperty'],
            [$class: 'ParametersDefinitionProperty', parameterDefinitions: [
                    [$class: 'StringParameterDefinition', name: 'artifactory_user', defaultValue: 'upload', description: 'artifactory user'],
                    [$class: 'StringParameterDefinition', name: 'artifactory_pass', defaultValue: 'upload', description: 'artifactory password'],
                    [$class: 'StringParameterDefinition', name: 'artifact_version', description: 'the artifact version, leave blank to use the default'],
            ]
            ]
    ]
}
__jobProperties(__JOB_PROPERTIES())

@Library('global-pipeline-utils@v1.40') _

timestamps {
    node('generic') {
        checkout scm
        branchId = buildUtils.getBranchId()
        echo "branchId: $branchId"

        if (artifact_version == null || artifact_version == "") {
            artifact_version = "${branchId}.${env.BUILD_NUMBER}"
        }
        echo "artifact_version: $artifact_version"
        if (!branchId) {
            error message: "Cannot calculate branchId from ${env.BRANCH_NAME}"
        }
//        sh "grep -rl 'artifactory-01.borderfdev.net' . | xargs sed -i 's/artifactory-01.borderfdev.net/registry.bfretail.pitneycloud.com/g'"
        stage('build test publish') {
            dir('deploy') {
                def sbtoptions = "-Dsbt.log.noformat=true -Dversion=${artifact_version} " +
                        "-Djdk.logging.allowStackWalkSearch=true " +
                        "-Dsbt.repository.config=.sbt/repositories -Dsbt.override.build.repos=true -Dsbt.override.build.repos=true " +
                        "-Dartifactory_user=${artifactory_user} -Dartifactory_password=${artifactory_pass}"
                sh "sbt ${sbtoptions} test it:test publish"
            }
        }
    }
}