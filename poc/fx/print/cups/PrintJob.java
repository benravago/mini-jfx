package fx.print.cups;

import com.sun.javafx.print.PrinterImpl;
import com.sun.javafx.print.PrinterJobImpl;

import javafx.print.PageLayout;
import javafx.scene.Node;
import javafx.stage.Window;

public class PrintJob implements PrinterJobImpl { //  javax.print.DocPrintJob

  @Override
  public PrinterImpl getPrinterImpl() { return null; }

  @Override
  public void setPrinterImpl(PrinterImpl printerImpl) {}

  @Override
  public boolean showPrintDialog(Window owner) { return false; }

  @Override
  public boolean showPageDialog(Window owner) { return false; }

  @Override
  public PageLayout validatePageLayout(PageLayout pageLayout) { return null; }

  @Override
  public boolean print(PageLayout pageLayout, Node node) { return false; }

  @Override
  public boolean endJob() { return false; }

  @Override
  public void cancelJob() {}

}
