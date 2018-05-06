
Write-Host "Press any key to continue ..."
docker-machine rm -y -f final 
docker-machine create  --driver virtualbox --virtualbox-share-folder "$pwd/:app" --virtualbox-memory "4024" final
docker-machine ssh final "cp -rf /app/data/config.xls /home/docker/config.xls "
docker-machine ssh final "chmod 777 /app/data/start.sh"
docker-machine ssh final "chmod 777 /app/data/stop.sh"
docker-machine ssh final "chmod 777 /app/data/install.sh"


$a = docker-machine ip final
echo $a":8081"
start powershell { docker-machine ssh final "sh /app/data/install.sh"}

Write-Host "Press any key to continue ..."
