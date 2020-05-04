@Library(['private-pipeline-library', 'jenkins-shared@mvnw']) _

mavenPipeline(
  javaVersion: 'Java 8',
  useMvnw: true,
  usePublicSettingsFile: true,
  useEventSpy: false, 
  testResults: [ '**/target/*-reports/*.xml' ]
)
