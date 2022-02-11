package fx.print.cups;

import javafx.concurrent.Task;

import java.io.IOException;

import fx.print.ipp.Connection;
import fx.print.ipp.Message;
import fx.print.ipp.Messages;

public abstract class Operation extends Task<Message> {

  protected Operation(String spec) {
    url = spec;
  }

  String url;
  byte[] buf;
  int rc;

  @Override
  protected Message call() throws Exception {
    buf = request();
    if (buf != null) {
      rc = Connection.post(url,
        send -> send.write(buf),
        recv -> buf = recv.readAllBytes(),
        fail -> buf = fail.readAllBytes()
      );
      if (rc == 200 && buf != null) {
        return response(buf);
      } else {
        fault(buf,rc);
      }
    }
    return null;
  }

  protected abstract byte[] request();

  protected Message response(byte[] b) {
    return Messages.parse(b);
  }

  protected void fault(byte[] b, int sc) throws IOException {
    System.err.println("POST to "+url+" rc="+sc);
    System.err.write(b);
    System.err.println();
  }

}
