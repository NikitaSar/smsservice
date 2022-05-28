# Sms service
Receives a message from the queue `RabbitMQ` and sends to `SMS` (sms.ru API).

**Message format:**
``` json
{
    "phone": "phone number to",
    "msg": "text message"
}
```

**Build image:**
```docker
docker build -f Docker/Dockerfile -t smsservice
```

**Run container with docker:**
```docker
docker run -d \ 
-e THRESHOLD_BALANCE=<value> \
-e ADMIN_PHONE=<phone number> \
-e QUEUE_NAME=<value> \
-e SMSRU_TOKEN=<token> \
-e RABBIT_HOST=<host> \
-e RABBIT_USER=<username> \
-e RABBIT_PASS=<password> \
-e RABBIT_PORT=<port> \
--name my-smsservice smsservice
```

**Run container with Docker compose:**

**Docker-compose.yml**
```yml
version: "3.3"
services:
  sms:
    build: Docker/
    environment:
      THRESHOLD_BALANCE: <value>
      ADMIN_PHONE: <phone number>
      SMSRU_TOKEN: <token>
      QUEUE_NAME: <value>
      RABBIT_HOST: <host>
      RABBIT_USER: <username>
      RABBIT_PASS: <password>
      RABBIT_PORT: <port>
```

**Docker compose run:**
```docker
docker-compose up -d
```



