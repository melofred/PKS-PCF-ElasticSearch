# Earthquakes demo with SCDF for PCF and PKS/Kubo

This demo is part of the CF Summit EU 2017 session and shows both PCF Elastic Runtime and PKS being used side-by-side.

## Pre-requisites:
---
The demo was tested using the following software versions:
- Pivotal CloudFoundry(PCF) v1.11
- Kubo v[CHANGEME]
- Spring Cloud Dataflow(SCDF) v.1.3.0.M2
- Docker v.17.06
- Kubectl client v.1.7.6

The SCDF pipeline uses the following Spring Cloud Stream apps:
- Http Client, [CHANGEME] - Bacon Release (maven) http://bit.ly/Bacon-RELEASE-stream-applications-rabbit-maven
- Custom Elastic Search sink app, which can be found in this repository

It also requires a docker registry account, with the ability to push and pull images. Dockerhub (free) will work just fine.

## Runnning the demo
---

1. Deploy ElasticSearch and Kibana: 

kubectl run elastic-kibana --image=nshou/elasticsearch-kibana
kubectl expose deployment elastic-kibana --name=elastic-kibana-svc --port=9200,5601 --type=LoadBalancer



2. Build and publish the Spring Cloud Stream ElasticSearch-sink app. It will build and deploy a docker image to Kubo. The script also exposes a Load Balancer service for that container.

cd Elasticsearch-sink/
./poblish.sh


3. Use the public IP exposed by K8s and import the app at Spring Cloud Dataflow: http://<kubernetes_svc_ip>/Elasticsearch-sink-<version>.jar

Use the SCDF UI and register a "sink" called elasticsearch, pointing to the endpoint above

4. Create a pipeline on SCDF that reads the data and persists to ElasticSearch

Using a local file:

file --directory=<path>/earthquakes --filename-pattern=earthquakes.txt --mode=lines | bridge | elasticsearch

5. Deploy the pipeline. The data should land in ElasticSearch

6. Open the Kibana UI at <IP>:5601 and import the dashboard at earthquakes/ncedc-earthquakes-dashboards.json
