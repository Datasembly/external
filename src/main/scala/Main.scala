import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.amazonaws.ClientConfiguration
import com.amazonaws.services.s3.model.{ListObjectsV2Request, S3ObjectSummary}
import scala.jdk.CollectionConverters._  // Import for Scala-Java collection conversion

object GCSConnectionTest {

  // Function to create an S3 client configured to connect to Google Cloud Storage
  def createClient(region: String = "us"): AmazonS3 = {
    // Retrieve access and secret keys from environment variables
    val accessKey = sys.env.getOrElse("GCS_ACCESS_KEY", throw new Exception("GCS_ACCESS_KEY not set"))
    val secretKey = sys.env.getOrElse("GCS_SECRET_KEY", throw new Exception("GCS_SECRET_KEY not set"))

    // Endpoint for GCS (different from AWS S3)
    val endpointConfig = new EndpointConfiguration("https://storage.googleapis.com", region)

    // AWS-style credentials using GCS HMAC credentials
    val credentials = new BasicAWSCredentials(accessKey, secretKey)
    val credentialsProvider = new AWSStaticCredentialsProvider(credentials)

    // AWS Client configuration (optional, can customize further)
    val clientConfig = new ClientConfiguration()
    clientConfig.setUseGzip(true)
    clientConfig.setMaxConnections(200)
    clientConfig.setMaxErrorRetry(1)

    // Build and return the S3 client, configured to talk to GCS
    AmazonS3ClientBuilder.standard()
      .withEndpointConfiguration(endpointConfig)
      .withCredentials(credentialsProvider)
      .withClientConfiguration(clientConfig)
      .enablePathStyleAccess()  // Important: GCS requires path-style access
      .build()
  }

  // Function to list objects in a specific bucket
  def testConnection(s3Client: AmazonS3, bucketName: String): Unit = {
    try {
      // Create a request to list objects in the specified bucket
      val request = new ListObjectsV2Request().withBucketName(bucketName)

      // Get the list of objects in the bucket
      val result = s3Client.listObjectsV2(request)

      // Convert Java list to Scala list for easier iteration
      val objectSummaries: List[S3ObjectSummary] = result.getObjectSummaries.asScala.toList

      // Print the bucket and its objects
      println(s"Objects in bucket '$bucketName':")
      objectSummaries.foreach(obj => println(s"- ${obj.getKey}"))

    } catch {
      case e: Exception =>
        println(s"Error connecting to bucket '$bucketName': " + e.getMessage)
    }
  }

  def main(args: Array[String]): Unit = {
    // Bucket name to list objects from
    val bucketName = "your-gs-bucket"

    // Create the S3 client for GCS using environment variables for access and secret keys
    val s3Client = createClient()

    // Test connection by listing objects in a specific bucket
    testConnection(s3Client, bucketName)
  }
}
