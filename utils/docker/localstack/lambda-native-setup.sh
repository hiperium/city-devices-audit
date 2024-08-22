#!/bin/bash

echo ""
echo "WAITING FOR RESOURCES FROM BUILDER CONTAINER..."
CREATE_FUNCTION_PATH="/var/tmp/trails-create/create-function-assembly.zip"
while [ ! -f "$CREATE_FUNCTION_PATH" ]; do
    sleep 3
done
echo "DONE!"

echo ""
echo "CREATING LAMBDA ROLE..."
awslocal iam create-role                    \
    --role-name 'lambda-role'               \
    --assume-role-policy-document '{
        "Version": "2012-10-17",
        "Statement": [
          {
            "Effect": "Allow",
            "Principal": {
              "Service": "lambda.amazonaws.com"
            },
            "Action": "sts:AssumeRole"
          }
        ]
      }'

echo ""
echo "ALLOWING LAMBDA TO ACCESS LOGS..."
awslocal iam put-role-policy                \
    --role-name 'lambda-role'               \
    --policy-name 'CloudWatchLogsPolicy'    \
    --policy-document '{
        "Version": "2012-10-17",
        "Statement": [
          {
            "Effect": "Allow",
            "Action": [
              "logs:CreateLogGroup",
              "logs:CreateLogStream",
              "logs:PutLogEvents"
            ],
            "Resource": "arn:aws:logs:*:*:*"
          }
        ]
      }'

echo ""
echo "ALLOWING LAMBDA TO ACCESS DYNAMODB..."
awslocal iam put-role-policy                \
    --role-name 'lambda-role'               \
    --policy-name 'DynamoDBPolicy'          \
    --policy-document '{
        "Version": "2012-10-17",
        "Statement": [
          {
            "Effect": "Allow",
            "Action": [
              "dynamodb:GetItem",
              "dynamodb:PutItem",
              "dynamodb:UpdateItem",
              "dynamodb:Scan",
              "dynamodb:Query"
            ],
            "Resource": "arn:aws:dynamodb:us-east-1:000000000000:table/Trails"
          }
        ]
      }'

echo ""
echo "CREATING SQS-DLQ FOR TRAIL CREATE FUNCTION..."
awslocal sqs create-queue  \
    --queue-name 'trail-create-function-dlq'

echo ""
echo "CREATING TRAIL CREATE FUNCTION..."
awslocal lambda create-function                                                                       \
    --function-name 'trail-create-function'                                                           \
    --runtime 'provided.al2023'                                                                       \
    --architectures 'arm64'                                                                           \
    --zip-file fileb://"$CREATE_FUNCTION_PATH"                                                        \
    --handler 'org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest'         \
    --dead-letter-config 'TargetArn=arn:aws:sqs:us-east-1:000000000000:trail-create-function-dlq'     \
    --timeout 20                                                                                      \
    --memory-size 512                                                                                 \
    --role 'arn:aws:iam::000000000000:role/lambda-role'                                               \
    --environment 'Variables={SPRING_CLOUD_AWS_ENDPOINT=http://host.docker.internal:4566}'

echo "CREATING EVENTBRIDGE RULE FOR TRAIL CREATE FUNCTION..."
awslocal events put-rule                                        \
    --name 'trail-create-function-rule'                         \
    --event-pattern '{
      "source": ["hiperium.city.tasks.api"],
      "detail-type": ["ExecutedTaskEvent"]
    }'

echo ""
echo "ALLOWING EVENTBRIDGE TO INVOKE TRAIL CREATE FUNCTION..."
awslocal lambda add-permission                                                  \
    --function-name 'trail-create-function'                                     \
    --statement-id  'eventbridge-invoke-trail-create-function-permission'       \
    --action        'lambda:InvokeFunction'                                     \
    --principal     'events.amazonaws.com'                                      \
    --source-arn    'arn:aws:events:us-east-1:000000000000:rule/trail-create-function-rule'

echo ""
echo "SETTING TRAIL CREATE FUNCTION AS EVENTBRIDGE TARGET..."
awslocal events put-targets                                     \
    --rule 'trail-create-function-rule'                         \
    --targets 'Id=1,Arn=arn:aws:lambda:us-east-1:000000000000:function:trail-create-function'

echo ""
echo "DONE!"
