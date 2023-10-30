#!/bin/bash
echo "########### Setting SQS names as env variables ###########"
export FIRST_QUEUE=first-SQS
export SECOND_QUEUE=second-SQS
export THIRD_QUEUE=third-SQS

echo "########### Creating Queues ###########"
awslocal sqs create-queue --queue-name $FIRST_QUEUE
awslocal sqs create-queue --queue-name $SECOND_QUEUE
awslocal sqs create-queue --queue-name $THIRD_QUEUE

echo "########### Listing queues ###########"
awslocal sqs list-queues

echo "########### Retrieving Queue URLs ###########"
FIRST_QUEUE_URL=$(awslocal sqs get-queue-url --queue-name $FIRST_QUEUE |  sed 's/"QueueUrl"/\n"QueueUrl"/g' | grep '"QueueUrl"' | awk -F '"QueueUrl":' '{print $2}' | tr -d '"' | xargs)
SECOND_QUEUE_URL=$(awslocal sqs get-queue-url --queue-name $SECOND_QUEUE |  sed 's/"QueueUrl"/\n"QueueUrl"/g' | grep '"QueueUrl"' | awk -F '"QueueUrl":' '{print $2}' | tr -d '"' | xargs)
THIRD_QUEUE_URL=$(awslocal sqs get-queue-url --queue-name $THIRD_QUEUE |  sed 's/"QueueUrl"/\n"QueueUrl"/g' | grep '"QueueUrl"' | awk -F '"QueueUrl":' '{print $2}' | tr -d '"' | xargs)

echo "########### Sending message to first queue ###########"
awslocal sqs send-message \
    --queue-url "$FIRST_QUEUE_URL" \
    --message-body file:///tmp/localstack/sqs-messages/01_message.json

echo "########### Command to view SQS messages ###########"
echo "awslocal sqs receive-message --queue-url $FIRST_QUEUE_URL"
echo "awslocal sqs receive-message --queue-url $SECOND_QUEUE_URL"
echo "awslocal sqs receive-message --queue-url $THIRD_QUEUE_URL"
