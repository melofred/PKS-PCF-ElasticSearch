export SPRING_CLOUD_DEPLOYER_CLOUDFOUNDRY_URL=https://api.sys.pcf-demo.net
export SPRING_CLOUD_DEPLOYER_CLOUDFOUNDRY_ORG=system
export SPRING_CLOUD_DEPLOYER_CLOUDFOUNDRY_SPACE=scdf
export SPRING_CLOUD_DEPLOYER_CLOUDFOUNDRY_DOMAIN=cfapps.pcf-demo.net
export SPRING_CLOUD_DEPLOYER_CLOUDFOUNDRY_USERNAME=scdf
export SPRING_CLOUD_DEPLOYER_CLOUDFOUNDRY_PASSWORD=scdf
export SPRING_CLOUD_DEPLOYER_CLOUDFOUNDRY_SKIP_SSL_VALIDATION=true

export SPRING_CLOUD_DEPLOYER_CLOUDFOUNDRY_STREAM_SERVICES=rabbit
# The following is for letting task apps write to their db.
# Note however that when the *server* is running locally, it can't access that db
# task related commands that show executions won't work then
export SPRING_CLOUD_DEPLOYER_CLOUDFOUNDRY_TASK_SERVICES=mysql

export SPRING_CLOUD_DEPLOYER_CLOUDFOUNDRY_STREAM_MEMORY=1024
export SPRING_CLOUD_DEPLOYER_CLOUDFOUNDRY_STREAM_DISK=2048

java -jar spring-cloud-dataflow-server-cloudfoundry-1.3.0.M2.jar 
