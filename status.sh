#!/bin/bash

while [ 1 ] ; do
	echo "-----------------------------"
	echo "Core        : "`curl -s http://localhost:8080/info` $? &
	echo "Worker 9010 : "`curl -s http://localhost:9010/info` $? &
	echo "Worker 9011 : "`curl -s http://localhost:9011/info` $? & 
	echo "Worker 9012 : "`curl -s http://localhost:9012/info` $? &
	echo "Worker 9013 : "`curl -s http://localhost:9013/info` $? &
	echo "Worker 9014 : "`curl -s http://localhost:9014/info` $? &
	echo "Worker 9015 : "`curl -s http://localhost:9015/info` $? &
	echo "Worker 9016 : "`curl -s http://localhost:9016/info` $? &
	echo "Worker 9017 : "`curl -s http://localhost:9017/info` $? &
	echo "Worker 9018 : "`curl -s http://localhost:9018/info` $? &
	echo "Worker 9019 : "`curl -s http://localhost:9019/info` $? &
	echo "Worker 9020 : "`curl -s http://localhost:9020/info` $? &
	sleep 5
done