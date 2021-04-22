# Zookeeper

### Zookeeper setup instructions
Setup data folders for Zookeeper config
```shell
mkdir /tmp/zookeeper1
mkdir /tmp/zookeeper2
mkdir /tmp/zookeeper3
echo "1" > /tmp/zookeeper1/myid
echo "2" > /tmp/zookeeper2/myid
echo "3" > /tmp/zookeeper3/myid
```

Running servers
```shell
./bin/zkServer.sh start-foreground /home/maciektr/Programowanie/AGH_Laby/distributed_lab/lab3/apache-zookeeper-3.6.1-bin/bin/../conf/zoo1.cfg

./bin/zkServer.sh start-foreground /home/maciektr/Programowanie/AGH_Laby/distributed_lab/lab3/apache-zookeeper-3.6.1-bin/bin/../conf/zoo2.cfg

./bin/zkServer.sh start-foreground /home/maciektr/Programowanie/AGH_Laby/distributed_lab/lab3/apache-zookeeper-3.6.1-bin/bin/../conf/zoo3.cfg
```

Running clients
```shell
./bin/zkCli.sh -server 127.0.0.1:2181

./bin/zkCli.sh -server 127.0.0.1:2182

./bin/zkCli.sh -server 127.0.0.1:2183
```
