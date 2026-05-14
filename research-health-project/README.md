# health-care `research` project

![](./src/main/resources/static/images/research-1200x627.png)

## scaffolding

Maven:

**docker-compose** optional dependencies.

```shell
cd ~/Workshop/Projects/spring-boot-projects/
curl --verbose --get https://start.spring.io/
curl --verbose \
  --data dependencies=web,graphql,data-jpa,h2,lombok,spring-restclient,actuator,devtools \
  --data language=java \
  --data bootVersion=4.0.6 \
  --data type=maven-project \
  --data javaVersion=17 \
  --data groupId=local.health \
  --data artifactId=research \
  --data configurationFileFormat=yaml \
  --output research.zip \
  --get https://start.spring.io/starter.zip
ls -l research.zip
file research.zip
unzip -q research.zip -d ./research-health-project
cd ./research-health-project/
java --version
./mvnw spring-boot:run
```

---

## initialize local repository

Then give the following commands:

```shell
ls -al
git init
git branch -m main
git status
git add .
git status
git config user.email "developer@research.health.local"
git config user.name "developer"
git commit -m "initializing the local repository"
git tag -a v0.0.0 -m "starting version of clean repo"
git log
git checkout -b staging
git merge --no-ff main -m "merge main into staging"
git checkout -b draft
git merge --no-ff main -m "merge main into draft"
git status
git log
git branch --list | wc -l
git branch --list
```

And, after each change, the cycle repeats:

```shell
git status
git add .
git commit -m "further adjustments"
git tag -a v0.0.1 -m "further adjustments"
git log
git checkout staging
git merge --no-ff draft -m "merge draft into staging"
git checkout main
git merge --no-ff staging -m "merge staging into main"
git checkout draft
```

Or, grouping the last commands:

```shell
git add . && git commit -m "further adjustments" && git tag -a v0.0.1 -m "further adjustments"
git checkout staging && git merge --no-ff draft -m "merge draft into staging" && git checkout main && git merge --no-ff staging -m "merge staging into main" && git checkout draft
```

To see, for example, the last three commits:

```shell
git log -3
```

If something were to go wrong:

```shell
git reset --hard v0.0.0
```

To remove, after modifying file `gitignore` , for example, a directory from change tracking but not from the filesystem:

```shell
git rm -r --cached data/
git commit -m "Remove data directory from tracking"
git tag -a v0.0.2 -m "remove data directory from tracking"
```

---

## example settings for the `vscode` code editor and similar

Example of `.vscode/settings.json`:

```json
{
  "java.jdt.ls.java.home": "/home/developer_username/.opt/Java/jdk-17.0.18+8",
  "java.configuration.runtimes": [
    {
      "name": "JavaSE-17",
      "path": "/home/developer_username/.opt/Java/jdk-17.0.18+8",
      "default": true
    },
    {
      "name": "JavaSE-21",
      "path": "/home/developer_username/.opt/Java/jdk-21.0.10+7"
    },
    {
      "name": "JavaSE-25",
      "path": "/home/developer_username/.opt/Java/jdk-25.0.2+10"
    },
    {
      "name": "JavaSE-26",
      "path": "/home/developer_username/.opt/Java/jdk-26+35"
    }
  ],
  "java.configuration.updateBuildConfiguration": "interactive",
  "java.compile.nullAnalysis.mode": "automatic"
}
```

---

## images

I generated the images for this project by first creating SVG code and then converting it to other formats using only open-source and free software:

```shell
convert -background none research-1200x627.png -resize 64x64 favicon-64.png && \
convert -background none research-1200x627.png -resize 48x48 favicon-48.png && \
convert -background none research-1200x627.png -resize 32x32 favicon-32.png && \
convert -background none research-1200x627.png -resize 16x16 favicon-16.png && \
convert favicon-16.png favicon-32.png favicon-48.png favicon-64.png favicon.ico && \
rm favicon-16.png favicon-32.png favicon-48.png favicon-64.png
```

and simply:

```shell
convert -background none research-1200x627.png -resize 64x64 research-64.png
```
