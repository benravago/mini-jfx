package fx.print.ipp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URL;
import java.net.HttpURLConnection;

public interface Connection {

  @FunctionalInterface
  interface Source { void writeTo(OutputStream out) throws IOException; }

  @FunctionalInterface
  interface Result { void readFrom(InputStream in) throws IOException; }

  static int post(String url, Source source, Result result, Result error) throws IOException {
    var http = connect(url);
    try {
      try (var os = http.getOutputStream()) {
        source.writeTo(os); // send request
      }
      var code = http.getResponseCode(); // wait for response
      switch (code) {
        case HttpURLConnection.HTTP_OK -> {
          code = 0; // not failed
          try (var is = http.getInputStream()) {
            result.readFrom(is); // receive response
          }
        }
        default -> {
          try (var is = http.getErrorStream()) {
            error.readFrom(is); // receive fault
          }
        }
      }
      return code;
    }
    finally {
      http.disconnect();
    }
  }

  static HttpURLConnection connect(String url) throws IOException {
    var http = (HttpURLConnection) new URL(url).openConnection();
    http.setRequestMethod("POST");
    http.setDoOutput(true);
    http.setInstanceFollowRedirects(true);
    http.setChunkedStreamingMode(0);
    http.setRequestProperty("Content-Type", "application/ipp");
    // TODO: set other IPP headers
    return http;
  }

}
