#!/bin/zsh

curl -d @createEmployee.json -H "Content-Type: application/json" http://localhost:8080/api/v1/employees