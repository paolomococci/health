# `care`

![](src/main/resources/static/img/healthcare_blurry.png)

## initialize local repository

Then give the following commands:

```shell
ls -al
git init
git branch -m main
git status
git add .
git status
git config user.email "developer@care.local"
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

## generation of a `secret symmetric key`

Now you will need to generate, preferably from the shell, a symmetric key for creating and verifying JWT tokens.
A key to be kept secret, generated with the following command:

```shell
openssl rand -base64 32
```

and then add it to file `application.yaml`.

## run from shell

Clean, test and build:

```shell
./gradlew clean
./gradlew test --info
./gradlew build
```

To launch the application:

```shell
./gradlew bootRun --help
./gradlew bootRun
```

so I can use the H2 console at the web address: <http://localhost:8080/h2-console> 
by setting the following properties: JDBC URL: jdbc:h2:file:./data/healthdb, user=sa, no password

### other useful commands

Compilation only without testing:

```shell
./gradlew compileJava
```

Dependencies and metadata refresh:

```shell

./gradlew clean build
```

Checking dependencies:

```shell
./gradlew dependencies
```

To view the dependency configuration:

```shell
./gradlew dependencies --configuration runtimeClasspath
```

## interact with endpoints thanks to httpie

**Please note that the names used below are fictional and used for illustrative purposes only; therefore, they do not refer to anyone or anything.**

### illegal commands

The following commands should return an HTTP status code of 403, Forbidden:

```shell
http -v GET http://localhost:8080/api/patients
http -v GET http://localhost:8080/api/patients/1
http -v GET http://localhost:8080/api/patients/2
http -v GET http://localhost:8080/api/patients?name=Rossi
http -v POST http://localhost:8080/api/patients Content-Type:application/json <<< '{
    "name": "Valery",
    "birthdate": "1981-03-25",
    "gender": "FEMALE"
}'
http -v POST http://localhost:8080/api/patients name="Lisa" birthdate=1970-01-05 gender=FEMALE
http -v GET http://localhost:8080/api/patients?page=0&size=20
http -v GET http://localhost:8080/api/patients?page=1&size=20
```

### commands permitted with prior authorization

I'm trying to test authentication and authorization:

```shell
http POST http://localhost:8080/api/auth/login username=user password=testQwerty123
```

I try to save the token in an environment variable:

```shell
TOKEN=$( \
    http --print=b POST http://localhost:8080/api/auth/login \
    Content-Type:application/json \
    <<< '{"username":"user","password":"testQwerty123"}' | jq -r .token)
```

and check:

```shell
echo "Token: $TOKEN"
```

I try to use a token for a GET request:

```shell
http GET http://localhost:8080/api/patients \
    "Authorization: Bearer $TOKEN"
```

I'm trying to register a patient using the token:

```shell
http POST http://localhost:8080/api/patients \
    "Authorization: Bearer $TOKEN" \
    Content-Type:application/json \
    <<< '{
    "name": "Sandra",
    "birthdate": "1986-08-19",
    "gender": "FEMALE"
}'
```

View the latest patient:

```shell
http GET http://localhost:8080/api/patients/1 \
    "Authorization: Bearer $TOKEN"
```

Search by name:

```shell
http GET http://localhost:8080/api/patients?name=Betty \
    "Authorization: Bearer $TOKEN"
```

will return the data of the newly created patient, whereas the following request:

```shell
http GET http://localhost:8080/api/patients?name=John \
    "Authorization: Bearer $TOKEN"
```

it will return an empty array.

Modifications using the verb PUT:

```shell
http -v PUT http://localhost:8080/api/patients/1 \
    "Authorization: Bearer $TOKEN" \
    Content-Type:application/json \
    <<< '{
    "name": "Robert",
    "birthdate": "1985-01-19",
    "gender": "MALE"
}'
```

Some changes using the PATCH verb.

Just the name:

```shell
http -v PATCH http://localhost:8080/api/patients/1 \
    "Authorization: Bearer $TOKEN" \
    Content-Type:application/json \
    <<< '{
    "name": "Betty"
}'
```

Only the date of birth:

```shell
http -v PATCH http://localhost:8080/api/patients/1 \
    "Authorization: Bearer $TOKEN" \
    Content-Type:application/json \
    <<< '{
    "birthdate": "1995-10-02"
}'
```

To update only the value recorded as gender:

```shell
http -v PATCH http://localhost:8080/api/patients/1 \
    "Authorization: Bearer $TOKEN" \
    Content-Type:application/json \
    <<< '{
    "gender": "FEMALE"
}'
```

And finally, now I try to delete a patient from the system:

```shell
http DELETE http://localhost:8080/api/patients/1 \
    "Authorization: Bearer $TOKEN"
```

## images

I generated the images for this project by first creating SVG code and then converting it to other formats using only open-source and free software:

```shell
convert -background none logo.svg -resize 64x64 favicon-64.png && \
convert -background none logo.svg -resize 48x48 favicon-48.png && \
convert -background none logo.svg -resize 32x32 favicon-32.png && \
convert -background none logo.svg -resize 16x16 favicon-16.png && \
convert favicon-16.png favicon-32.png favicon-48.png favicon-64.png favicon.ico && \
rm favicon-16.png favicon-32.png favicon-48.png favicon-64.png
```
