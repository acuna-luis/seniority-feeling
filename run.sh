mvn clean package -Dmaven.test.skip=true
docker build -t luisacuna/db-spring .
docker push luisacuna/db-spring
git add *
git commit -m "commit"
git push
