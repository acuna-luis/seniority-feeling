mvn clean package -Dmaven.test.skip=true
docker build -t luisacuna/seniority-spring .
docker push luisacuna/seniority-spring
git add *
git commit -m "commit"
git push