#!/bin/bash

zip -r get-offer.zip build/libs/get-offer-0.0.1-SNAPSHOT.jar
aws configure set aws_access_key_id ${API_ACCESS_KEY}
aws configure set aws_secret_access_key ${API_SECRET_KEY}
aws --endpoint-url=https://kr.object.ncloudstorage.com s3 rm s3://${your_bucket_name}/get-offer.zip
aws --endpoint-url=https://kr.object.ncloudstorage.com s3 cp get-offer.zip s3://${your_bucket_name}/get-offer.zip