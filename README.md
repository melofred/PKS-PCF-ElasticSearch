# Earthquakes demo 
## using SCDF, PCF Elastic Runtime and PKS/Kubo

This demo is part of the CF Summit EU 2017 session and shows both PCF Elastic Runtime and PKS being used side-by-side.

## Pre-requisites:
---
The demo was tested using the following software versions:
- Pivotal CloudFoundry(PCF) v1.11
- Kubo v0.7
- Spring Cloud Dataflow(SCDF) for CloudFoundry v.1.2.4
- Docker v.17.06
- Kubectl client v.1.7.6

The SCDF pipeline uses the following Spring Cloud Stream apps:
- HttpClient processor - Bacon Release (maven) [http://bit.ly/Bacon-RELEASE-stream-applications-rabbit-maven]
- Custom Elastic Search sink app, which can be found in this repository (Elasticsearch-sink folder)

It also requires a docker registry account, with the ability to push and pull images. Dockerhub (free) will work just fine.


## Runnning the demo
---

   1. Deploy ElasticSearch and Kibana. Expose it through a Load Balancer service and note the IP address:

   Alternatively, you can use the __deploy-es-kibana.sh__ script

   $ kubectl run elastic-kibana --image=fredmelo/elasticsearch
```
   deployment "elastic-kibana" created
```
   $ kubectl expose deployment elastic-kibana --name=elastic-kibana-svc --port=9200,5601 --type=LoadBalancer
```
   service "elastic-kibana-svc" exposed
```
   $ kubectl get services
```
   NAME                  CLUSTER-IP       EXTERNAL-IP      PORT(S)                         AGE
   elastic-kibana-svc    10.100.200.232   35.190.151.218   9200:31376/TCP,5601:31047/TCP   13d
   kubernetes            10.100.200.1     <none>           443/TCP                         13d
```

   In this case, the ElasticSearch/Kibana externap IP is 35.190.151.218

   Alternatively, you can use the CF App Runtime integrated routing (using route-sync) to expose the running container and use that route instead of the external IP.


   2. Create the Index in Elastic Search. See the script ./createIndex.sh and replace it with your ElasticSearch IP/host information.

   $ ./createIndex.sh


   3. Build and publish the Spring Cloud Stream ElasticSearch sink app. 

   $ cd Elasticsearch-sink/
   
   
   _TODO: Make the ElasticSearch cluster IP a parameter for this app. At this moment, it's hard-coded within the app as es-kibana.pcf-demo.net_
   
   ---
   Edit the file _src/main/java/io/pivotal/example/stream/elasticsearch/sink/ElasticsearchSinkProperties.java_ and replace the entry _es-kibana.pcf-demo.net:9200_ with your ElasticSearch/Kibana IP address or hostname from step #1 above.
   
   This step is temporary, until somebody can make that a param for the app (to be passed during the pipeline creation process)
   
   ---
   
   $ ./mvnw package
```
(...)
[INFO]
[INFO] --- maven-jar-plugin:2.6:jar (default-jar) @ Elasticsearch-sink ---
[INFO] Building jar: /Users/fmelo/cfsummit-demo/Elasticsearch-sink/target/Elasticsearch-sink-0.2.1-SNAPSHOT.jar
[INFO]
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 11.633 s
[INFO] Finished at: 2017-10-17T09:49:33-07:00
[INFO] Final Memory: 26M/332M
[INFO] ------------------------------------------------------------------------
```

   Note the name of the JAR file created. It will be used on step #5 below.

   Now either

   a) Edit the script at __Elasticsearch-sink/publish.sh__ to use your own docker repository and run it. It will build a docker image and deploy to Kubo.  

   or

   b) use the pre-built image stored at fredmelo/maven:latest and publish it

      $ kubectl run maven --image=fredmelo/maven:latest
      ```
      deployment "maven" created
      ```
      $ kubectl expose deployment maven --name=maven-svc --type=LoadBalancer --port=80
      ``` 
      service "maven-svc" exposed
      ```

   The previous command builds and deploys a HTTP server(httpd) docker image to Kubo. We'll use the http server running on Kubernetes to host both our custom SCDF sink app and the data we'll ingest into Elastic Search using the pipeline.

   $ kubectl get services

   ```
   NAME                  CLUSTER-IP       EXTERNAL-IP      PORT(S)                         AGE
   elastic-kibana-svc    10.100.200.232   35.190.151.218   9200:31376/TCP,5601:31047/TCP   13d
   elastic-kibana-svc2   10.100.200.100   35.190.152.158   9200:31125/TCP,5601:31491/TCP   20m
   kubernetes            10.100.200.1     <none>           443/TCP                         13d
   maven-svc             10.100.200.252   35.196.125.234   80:30035/TCP                    11d
   ```
   
   Note down the external IP address of _maven-svc_. We'll use that on steps #5 and #6 below.


   4. Start the Spring Cloud Dataflow (SCDF) server locally or deploy it to PCF

   Follow the documentation at https://docs.spring.io/spring-cloud-dataflow-server-cloudfoundry/docs/1.3.0.M2/reference/htmlsingle/#getting-started


   5. Import the apps we'll use into SCDF

   Use the UI or command line to: 

   * Builk import the Bacon release apps [http://bit.ly/Bacon-RELEASE-stream-applications-rabbit-maven]

   * Import our custom app:  [http://[HOST]/Elasticsearch-sink-<version>.jar] 

   Replace <version> of the jar file with the correct filename(refer to step #3). You can also find the file at Elasticsearch-sink/target/. Replace [HOST] with the httpd host from step #3.


   6. Create and deploy the SCDF pipeline, importing data into Elastic Search

   Use the UI or SCDF command line to deploy the following pipeline to SCDF:

```
   stream create --name demo --definition "time --fixed-delay=3000 | httpclient --url-expression='''http://[HOST]/earthquakes.txt''' | splitter --delimiters=\"*\" |elasticsearch" --deploy
```

   Replace [HOST] above with your httpd host from step #3.


   7. Check data is being ingested into ElasticSearch

   You can check the SCDF app logs and verify the number of records directly in Elastic Search:

```
   $ curl http://[Elastic-Kibana-IP]:9200/earthquakes/_refresh
   {"_shards":{"total":1,"successful":1,"failed":0}}
    
   $ curl http://[Elastic-Kibana-IP]:9200/earthquakes/_count
   {"count":2,"_shards":{"total":1,"successful":1,"failed":0}}
```

   8. Open the Kibana UI and import the dashboard

   * Open the Kibana UI at http://[Elastic-Kibana-IP]:5601/

   * Click the Management tab >> Index Patterns tab >> Create New. Specify "earthquakes" as the index pattern name and click Create to define the index pattern. (Leave the Use event times to create index names box unchecked and use @timestamp as the Time Field)

   * Click the Management tab >> Saved Objects tab >> Import, and select earthquakes/earthquakes-dashboard.json.

   * Click on Dashboard tab and open the Earthquake dashboard

See data being ingested live!

