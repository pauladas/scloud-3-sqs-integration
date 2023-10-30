#!/bin/bash
echo "########### Creating Queues ###########"
awslocal sqs create-queue --queue-name first-SQS
awslocal sqs create-queue --queue-name second-SQS
awslocal sqs create-queue --queue-name third-SQS

echo "########### Listing queues ###########"
awslocal sqs list-queues

echo "########### Sending message to first queue ###########"
awslocal sqs send-message \
    --queue-url http://localhost:4566/000000000000/first-SQS \


echo "########### Command to view SQS messages ###########"
echo "awslocal sqs receive-message --queue-url http://localhost:4566/000000000000/first-SQS"
echo "awslocal sqs receive-message --queue-url http://localhost:4566/000000000000/second-SQS"
echo "awslocal sqs receive-message --queue-url http://localhost:4566/000000000000/third-SQS"
