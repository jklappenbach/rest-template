#!/bin/zsh

curl -d @request.json -H 'Content-Type: application/json'-X PUT http://localhost:8080/api/v1/employees/