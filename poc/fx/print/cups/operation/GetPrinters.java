package fx.print.cups.operation;

import fx.print.cups.Operation;

public class GetPrinters extends Operation {

  public GetPrinters(String spec) {
    super(spec);
  }

  @Override
  protected byte[] request() {
    return null; // subclass should override
  }

  // TODO: other builder api
}
