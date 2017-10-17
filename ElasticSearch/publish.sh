docker build -t fredmelo/elasticsearch:latest .
docker push fredmelo/elasticsearch:latest
kubectl run elastic-kibana --image=fredmelo/elasticsearch:latest 
