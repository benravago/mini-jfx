package fx.print.cups;

import javafx.print.Collation;
import javafx.print.PageOrientation;
import javafx.print.PageRange;
import javafx.print.Paper;
import javafx.print.PaperSource;
import javafx.print.PrintColor;
import javafx.print.PrintSides;
import javafx.print.PrintQuality;
// import javafx.print.PrintResolution;

import static fx.print.ipp.Tag.*;

final class Transform {

  record Attribute(int tag, String name, Object value) {}

  final static
  Attribute encode(Object v) {
    return switch (v.getClass().getName()) {
      case "javafx.print.Collation"       -> collation((Collation)v);
      case "javafx.print.PageOrientation" -> page_orientation((PageOrientation)v);
      case "javafx.print.Paper"           -> paper((Paper)v);
      case "javafx.print.PaperSource"     -> paper_source((PaperSource)v);
      case "javafx.print.PrintSides"      -> print_sides((PrintSides)v);
      case "javafx.print.PrintColor"      -> print_color((PrintColor)v);
      case "javafx.print.PrintQuality"    -> print_quality((PrintQuality)v);
      case "javafx.print.PageRange"       -> page_range((PageRange)v);
      // case "javafx.print.PrintResolution" -> print_resolution((PrintResolution)v);
      default -> null;
    };
  }

  @SuppressWarnings("unchecked")
  final static
  <T> T decode(String n, Object v) {
    return (T) switch(n) {
      case "multiple-document-handling" -> multiple_document_handling((String)v);
      case "orientation-requested"      -> orientation_requested((Integer)v);
      case "media"                      -> media((String)v);
      case "media-source"               -> media_source((String)v);
      case "sides"                      -> sides((String)v);
      case "print-color-mode"           -> print_color_mode((String)v);
      case "print-quality"              -> print_quality((Integer)v);
      case "page-ranges"                -> page_ranges((int[])v);
      // case "print-resolution"           -> print_resolution((String)v);
      default -> null;
    };
  }

  final static
  Attribute collation(Collation k) {
    var v = switch(k) {
      case COLLATED   -> "separate-documents-collated-copies";
      case UNCOLLATED -> "separate-documents-uncollated-copies";
      default -> null;
    };
    return v != null ? new Attribute(keyword_, "multiple-document-handling", v) : null;
  }

  final static
  Collation multiple_document_handling(String v) {
    return switch(v) {
      case "separate-documents-collated-copies"   -> Collation.COLLATED;
      case "separate-documents-uncollated-copies" -> Collation.UNCOLLATED;
      default -> null;
    };
  }

  final static
  Attribute page_orientation(PageOrientation k) {
    var v = switch(k) {
      case PORTRAIT          -> 3; // portrait
      case LANDSCAPE         -> 4; // landscape
      case REVERSE_LANDSCAPE -> 5; // reverse-landscape
      case REVERSE_PORTRAIT  -> 6; // reverse-portrait
      default -> null;
    };
    return v != null ? new Attribute(enum_, "orientation-requested", v) : null;
  }

  final static
  PageOrientation orientation_requested(int v) {
    return switch(v) {
      case 3 /* portrait          */ -> PageOrientation.PORTRAIT;
      case 4 /* landscape         */ -> PageOrientation.LANDSCAPE;
      case 5 /* reverse-landscape */ -> PageOrientation.REVERSE_LANDSCAPE;
      case 6 /* reverse-portrait  */ -> PageOrientation.REVERSE_PORTRAIT;
      default -> null;
    };
  }

  final static
  Attribute paper(Paper k) {
    var v = switch(k.getName()) {
      case "A0"                 -> "iso_a0_841x1189mm";
      case "A1"                 -> "iso_a1_594x841mm";
      case "A2"                 -> "iso_a2_420x594mm";
      case "A3"                 -> "iso_a3_297x420mm";
      case "A4"                 -> "iso_a4_210x297mm";
      case "A5"                 -> "iso_a5_148x210mm";
      case "A6"                 -> "iso_a6_105x148mm";
      case "C"                  -> "na_c_17x22in";
      case "Designated Long"    -> "iso_dl_110x220mm";
      case "Executive"          -> "na_executive_7.25x10.5in";
      case "Japanese Postcard"  -> "jpn_hagaki_100x148mm";
      case "B4"                 -> "jis_b4_257x364mm";
      case "B5"                 -> "jis_b5_182x257mm";
      case "B6"                 -> "jis_b6_128x182mm";
      case "Legal"              -> "na_legal_8.5x14in";
      case "Monarch Envelope"   -> "na_monarch_3.875x7.5in";
      case "8x10"               -> "na_govt-letter_8x10in";
      case "Letter"             -> "na_letter_8.5x11in";
      case "Number 10 Envelope" -> "na_number-10_4.125x9.5in";
      case "Tabloid"            -> "na_ledger_11x17in";
      default -> null;
    };
    return v != null ? new Attribute(keyword_, "media", v) : null;
  }

  final static
  Paper media(String v) {
    return switch(v) {
      case "iso_a0_841x1189mm"        -> Paper.A0;
      case "iso_a1_594x841mm"         -> Paper.A1;
      case "iso_a2_420x594mm"         -> Paper.A2;
      case "iso_a3_297x420mm"         -> Paper.A3;
      case "iso_a4_210x297mm"         -> Paper.A4;
      case "iso_a5_148x210mm"         -> Paper.A5;
      case "iso_a6_105x148mm"         -> Paper.A6;
      case "na_c_17x22in"             -> Paper.C;
      case "iso_dl_110x220mm"         -> Paper.DESIGNATED_LONG;
      case "na_executive_7.25x10.5in" -> Paper.EXECUTIVE;
      case "jpn_hagaki_100x148mm"     -> Paper.JAPANESE_POSTCARD;
      case "jis_b4_257x364mm"         -> Paper.JIS_B4;
      case "jis_b5_182x257mm"         -> Paper.JIS_B5;
      case "jis_b6_128x182mm"         -> Paper.JIS_B6;
      case "na_legal_8.5x14in"        -> Paper.LEGAL;
      case "na_monarch_3.875x7.5in"   -> Paper.MONARCH_ENVELOPE;
      case "na_govt-letter_8x10in"    -> Paper.NA_8X10;
      case "na_letter_8.5x11in"       -> Paper.NA_LETTER;
      case "na_number-10_4.125x9.5in" -> Paper.NA_NUMBER_10_ENVELOPE;
      case "na_ledger_11x17in"        -> Paper.TABLOID;
      default -> null;
    };
  }

  final static
  Attribute paper_source(PaperSource k) {
    var v = switch(k.getName()) {
      case "Automatic"      -> "auto";
      case "Bottom"         -> "bottom";
      case "Envelope"       -> "envelope";
      case "Large Capacity" -> "large-capacity";
      case "Main"           -> "main";
      case "Manual"         -> "manual";
      case "Middle"         -> "middle";
      case "Side"           -> "side";
      case "Top"            -> "top";
      default -> null;
    };
    return v != null ? new Attribute(keyword_, "media-source", v) : null;
  }

  final static
  PaperSource media_source(String v) {
    return switch(v) {
      case "auto"           -> PaperSource.AUTOMATIC;
      case "bottom"         -> PaperSource.BOTTOM;
      case "envelope"       -> PaperSource.ENVELOPE;
      case "large-capacity" -> PaperSource.LARGE_CAPACITY;
      case "main"           -> PaperSource.MAIN;
      case "manual"         -> PaperSource.MANUAL;
      case "middle"         -> PaperSource.MIDDLE;
      case "side"           -> PaperSource.SIDE;
      case "top"            -> PaperSource.TOP;
      default -> null;
    };
  }

  final static
  Attribute print_sides(PrintSides k) {
    var v = switch(k) {
      case ONE_SIDED -> "one-sided";
      case DUPLEX    -> "two-sided-long-edge";
      case TUMBLE    -> "two-sided-short-edge";
      default -> null;
    };
    return v != null ? new Attribute(keyword_, "sides", v) : null;
  }

  final static
  PrintSides sides(String v) {
    return switch(v) {
      case "one-sided"            -> PrintSides.ONE_SIDED;
      case "two-sided-long-edge"  -> PrintSides.DUPLEX;
      case "two-sided-short-edge" -> PrintSides.TUMBLE;
      default -> null;
    };
  }

  final static
  Attribute print_color(PrintColor k) {
    var v = switch(k) {
      case COLOR      -> "color";
      case MONOCHROME -> "monochrome";
      default -> null;
    };
    return v != null ? new Attribute(keyword_, "print-color", v) : null;
  }

  final static
  PrintColor print_color_mode(String v) {
    return switch(v) {
      case "color"      -> PrintColor.COLOR;
      case "monochrome" -> PrintColor.MONOCHROME;
      default -> null;
    };
  }

  final static
  Attribute print_quality(PrintQuality k) {
    var v = switch(k) {
      case LOW, DRAFT -> 3; // draft
      case NORMAL     -> 4; // normal
      case HIGH       -> 5; // high
      default -> null;
    };
    return v != null ? new Attribute(enum_, "print-quality", v) : null;
  }

  final static
  PrintQuality print_quality(int v) {
    return switch(v) {
      case 3 /* draft  */ -> PrintQuality.DRAFT; // LOW
      case 4 /* normal */ -> PrintQuality.NORMAL;
      case 5 /* high   */ -> PrintQuality.HIGH;
      default -> null;
    };
  }

  final static
  Attribute page_range(PageRange v) {
    return (v == null) ? null : new Attribute(rangeOfInteger_, "page-ranges", new int[]{ v.getStartPage(), v.getEndPage() });
  }

  final static
  PageRange page_ranges(int[] v) { // start-page, end-page -> lowerBound, upperBound
    return (v == null || v.length != 2) ? null : new PageRange(v[0],v[1]);
  }

// TODO: needs a helper to create a javafx.print.PrintResolution object

// final static
// Attribute print_resolution(PrintResolution v) { return null; }

// final static
// PrintResolution print_resolution(int[] v) { // cross-feed-resolution, feed-resolution, unit-resolution
//   return (v == null || v.length != 3) ? null
//     : switch (v[2]) {                       // crossFeed, feed, DPI
//         case 3 /* DPI  */ -> new PrintResolution( v[0], v[1] );
//         case 4 /* DPCM */ -> new PrintResolution( v[0]*2.54, v[1]*2.54 );
//         default -> null;
//       };
// }

}
