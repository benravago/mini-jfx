package fx.print.cups;

import fx.print.cups.operation.GetDefault;
import fx.print.cups.operation.GetPrinters;

public interface CUPS {

  static GetPrinters Get_Printers(String spec) {
    return new GetPrinters(spec);
  }
  static GetDefault Get_Default(String spec) {
    return new GetDefault(spec);
  }

}
