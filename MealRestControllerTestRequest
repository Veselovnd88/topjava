Get meals

curl --location 'http://localhost:8080/topjava/rest/meals'

Get one meal

curl --location 'http://localhost:8080/topjava/rest/meals/100005'

Get filtered

curl --location 'http://localhost:8080/topjava/rest/meals?startDate=2020-30-01&startTime=10%3A00&endDate=2020-30-01&endTime=20%3A00'

Create meal

curl --location 'http://localhost:8080/topjava/rest/meals' \
--header 'Content-Type: application/json' \
--data '{
    "dateTime": "2024-03-23 14:01",
    "calories": 150,
    "description": "кушоц кушоц"
}'

Change meal

curl --location --request PUT 'http://localhost:8080/topjava/rest/meals/100005' \
--header 'Content-Type: application/json' \
--data '{
    "id":100005,
    "dateTime": "2024-03-23 14:02",
    "calories": 150,
    "description": "кушоц changed"
}'

Delete meal

curl --location --request DELETE 'http://localhost:8080/topjava/rest/meals/100005'
