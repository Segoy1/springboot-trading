kubectl create namespace trading
kubectl create -f 'https://strimzi.io/install/latest?namespace=trading' -n trading

kubectl apply -f https://strimzi.io/examples/latest/kafka/kafka-persistent-single.yaml -n trading
kubectl wait kafka/my-cluster --for=condition=Ready --timeout=300s -n trading
