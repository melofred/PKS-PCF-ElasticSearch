docker build -t fredmelo/maven:latest .
docker push fredmelo/maven:latest
kubectl run maven --image=fredmelo/maven:latest 
#kubectl expose deployment maven --name=maven-svc --type=LoadBalancer --port=80
