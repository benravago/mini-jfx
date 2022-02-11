package fx.print.cups;

import fx.print.cups.operation.CancelJob;
import fx.print.cups.operation.CloseJob;
import fx.print.cups.operation.CreateJob;
import fx.print.cups.operation.GetPrinterAttributes;
import fx.print.cups.operation.SendDocument;
import fx.print.cups.operation.ValidateJob;

public interface IPP {

  static GetPrinterAttributes Get_Printer_Attributes(String spec) {
    return new GetPrinterAttributes(spec);
  }
  static ValidateJob Validate_Job(String spec) {
    return new ValidateJob(spec);
  }
  static CreateJob Create_Job(String spec) {
    return new CreateJob(spec);
  }
  static SendDocument Send_Document(String spec) {
    return new SendDocument(spec);
  }
  static CloseJob Close_Job(String spec) {
    return new CloseJob(spec);
  }
  static CancelJob Cancel_Job(String spec) {
    return new CancelJob(spec);
  }

}
