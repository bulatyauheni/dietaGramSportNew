package bulat.diet.helper_sport.utils;
import com.google.api.services.storage.Storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/** Example of downloading a GCS object. */
public class ObjectsDownloadExample {

  private static final boolean IS_APP_ENGINE = false;
  

  public static InputStream download(Storage storage, String bucketName, String objectName)
      throws IOException {
    Storage.Objects.Get getObject = storage.objects().get(bucketName, objectName);
    getObject.getMediaHttpDownloader().setDirectDownloadEnabled(!IS_APP_ENGINE);
    return getObject.executeMediaAsInputStream(); 
  }

  public static void downloadToOutputStream(Storage storage, String bucketName, String objectName,
      OutputStream data) throws IOException {
    Storage.Objects.Get getObject = storage.objects().get(bucketName, objectName);
    getObject.getMediaHttpDownloader().setDirectDownloadEnabled(!IS_APP_ENGINE);
    getObject.executeMediaAndDownloadTo(data); 
  }
  
  /**
   * This shows how to download a portion of an object. Especially useful for
   * resuming after a download fails, but can also be used to download in
   * parallel.
   */
  public static void downloadRangeToOutputStream(Storage storage, String bucketName,
      String objectName, long firstBytePos, long lastBytePos, OutputStream data)
      throws IOException {
    Storage.Objects.Get getObject = storage.objects().get(bucketName, objectName);
    // Remove cast after https://github.com/google/google-api-java-client/issues/937 is addressed.
    getObject.getMediaHttpDownloader().setDirectDownloadEnabled(!IS_APP_ENGINE)
        .setContentRange(firstBytePos, (int) lastBytePos);
    getObject.executeMediaAndDownloadTo(data);
  }
     
  public static void main(String[] args) throws Exception {
   /* HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    Credential credential = CredentialsProvider.authorize(httpTransport, jsonFactory);
    Storage storage = new Storage.Builder(httpTransport, jsonFactory, credential)
        .setApplicationName("Google-ObjectsDownloadExample/1.0").build();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    downloadToOutputStream(storage, BUCKET_NAME, OBJECT_NAME, out);
    System.out.println("Downloaded " + out.toByteArray().length + " bytes");*/
  }
}