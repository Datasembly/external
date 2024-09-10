# external
Code samples intended for demo or as solutions to data sharing questions


## Accessing Google Storage using AWS SDK

1. Follow Google Cloud Storage's instructions to obtain an HMAC key, or get it from the partner with whom you are working on data transfer.

2. Once that is done, adjust the `Main.scala` to contain the agreed-on bucket name:

```scala
...
val bucketName = "your-gs-bucket"
...
```

3. Set the HMAC Access Key and Secret Key values as environment variables to avoid accidental sharing:

```shell
export GCS_ACCESS_KEY="" # insert the ACCESS KEY
export GCS_SECRET_KEY="" # insert the SECRET KEY
``` 

4. Run the test assuming Java/Scala/sbt are installed on your system:

```shell
$ sbt run
```