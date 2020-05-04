@Library(['private-pipeline-library', 'jenkins-shared']) _

mavenPipeline(
  javaVersion: 'Java 8',
  useMvnw: true,
  usePublicSettingsFile: true,
  useEventSpy: false, 
  testResults: [ '**/target/*-reports/*.xml' ]
)
