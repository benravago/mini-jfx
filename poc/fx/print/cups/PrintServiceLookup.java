package fx.print.cups;

import com.sun.javafx.print.PrinterJobImpl;

import javafx.collections.ObservableSet;
import javafx.print.Printer;
import javafx.print.PrinterJob;

public class PrintServiceLookup extends com.sun.javafx.tk.PrintPipeline {

  @Override
  public PrinterJobImpl createPrinterJob(PrinterJob job) {
    return null; // javax.print.PrintService#createPrintJob()
  }

  @Override
  public ObservableSet<Printer> getAllPrinters() {
    return null; // javax.print.PrintServiceLookup#getPrintServices()
  }

  @Override
  public Printer getDefaultPrinter() {
    return null; // javax.print.PrintServiceLookup#getDefaultPrintService()
  }

}
