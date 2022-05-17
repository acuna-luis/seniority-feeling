mvn clean package -Dmaven.test.skip=true
docker build -t luisacuna/td-spring .
docker push luisacuna/td-spring
git add *
git commit -m "commit"
git push
