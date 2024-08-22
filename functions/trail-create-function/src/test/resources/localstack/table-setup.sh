#!/bin/bash

echo ""
echo "CREATING TRAILS TABLE..."
awslocal dynamodb create-table              \
  --table-name 'Trails'                     \
  --attribute-definitions                   \
    AttributeName=id,AttributeType=S        \
    AttributeName=cityId,AttributeType=S    \
  --key-schema                              \
    AttributeName=id,KeyType=HASH           \
    AttributeName=cityId,KeyType=RANGE      \
  --billing-mode PAY_PER_REQUEST

echo ""
echo "WRITING TRAILS ITEMS..."
awslocal dynamodb batch-write-item          \
    --request-items file:///var/lib/localstack/table-data.json
