# Cecilie

docker network create cecilie
docker run -d -p 27017:27017 --name mongodb   --network cecilie mongo 
docker run -d -p 8910:8910   --name phantomjs --network cecilie wernight/phantomjs phantomjs --webdriver=8910
docker run -v ~/Desktop/:/app/stuff --network cecilie  --link mongodb:mongo  webcrawler/webcrawler