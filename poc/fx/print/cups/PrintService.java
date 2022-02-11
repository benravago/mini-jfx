package fx.print.cups;

import java.util.Set;

import com.sun.javafx.print.PrinterImpl;

import javafx.geometry.Rectangle2D;
import javafx.print.Collation;
import javafx.print.JobSettings;
import javafx.print.PageOrientation;
import javafx.print.PageRange;
import javafx.print.Paper;
import javafx.print.PaperSource;
import javafx.print.PrintColor;
import javafx.print.PrintQuality;
import javafx.print.PrintResolution;
import javafx.print.PrintSides;
import javafx.print.Printer;

public class PrintService implements PrinterImpl { // javax.print.PrintService

  @Override
  public void setPrinter(Printer printer) {}

  @Override
  public String getName() { return null; }

  @Override
  public JobSettings getDefaultJobSettings() { return null; }

  @Override
  public Rectangle2D printableArea(Paper paper) { return null; }

  @Override
  public int defaultCopies() { return 0; }

  @Override
  public int maxCopies() { return 0; }

  @Override
  public Collation defaultCollation() { return null; }

  @Override
  public Set<Collation> supportedCollations() { return null; }

  @Override
  public PrintSides defaultSides() { return null; }

  @Override
  public Set<PrintSides> supportedSides() { return null; }

  @Override
  public PageRange defaultPageRange() { return null; }

  @Override
  public boolean supportsPageRanges() { return false; }

  @Override
  public PrintResolution defaultPrintResolution() { return null; }

  @Override
  public Set<PrintResolution> supportedPrintResolution() { return null; }

  @Override
  public PrintColor defaultPrintColor() { return null; }

  @Override
  public Set<PrintColor> supportedPrintColor() { return null; }

  @Override
  public PrintQuality defaultPrintQuality() { return null; }

  @Override
  public Set<PrintQuality> supportedPrintQuality() { return null; }

  @Override
  public PageOrientation defaultOrientation() { return null; }

  @Override
  public Set<PageOrientation> supportedOrientation() { return null; }

  @Override
  public Paper defaultPaper() { return null; }

  @Override
  public Set<Paper> supportedPapers() { return null; }

  @Override
  public PaperSource defaultPaperSource() { return null; }

  @Override
  public Set<PaperSource> supportedPaperSources() { return null; }

}
