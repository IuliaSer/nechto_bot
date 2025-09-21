set -e

mvn clean install

#docker run -it -e NGROK_AUTHTOKEN=${NGROK_AUTHTOKEN} ngrok/ngrok http host.docker.internal:80

docker-compose build
docker-compose up
