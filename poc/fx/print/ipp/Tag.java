package fx.print.ipp;

public final class Tag {

  public final static int

    // Delimiter tags

    operation_attributes_   = 0x01 ,
    job_attributes_         = 0x02 ,
    printer_attributes_     = 0x04 ,
    unsupported_attributes_ = 0x05 ,

    end_of_attributes_      = 0x03 ,

    // Out-of-Band Values

    unsupported_            = 0x10 ,
    unknown_                = 0x12 ,
    no_value_               = 0x13 ,

    // Integer Tags

    integer_                = 0x21 ,
    boolean_                = 0x22 ,
    enum_                   = 0x23 ,

    // Octet-String Tags

    octetString_            = 0x30 ,
    dateTime_               = 0x31 ,
    resolution_             = 0x32 ,
    rangeOfInteger_         = 0x33 ,


    // String Tags

    keyword_                = 0x44 ,
    uri_                    = 0x45 ,
    uriScheme_              = 0x46 ,
    charset_                = 0x47 ,
    naturalLanguage_        = 0x48 ,
    mimeMediaType_          = 0x49 ,

    // collection tags

    begCollection_          = 0x34 ,
    endCollection_          = 0x37 ,
    memberAttrName_         = 0x4a ,

    // Localized-String tags

    textWithoutLanguage_    = 0x41 ,
    nameWithoutLanguage_    = 0x42 ,
    textWithLanguage_       = 0x35 ,
    nameWithLanguage_       = 0x36 ,

    // Unassigned data types
    undefined = 0;

  static boolean delimiter(int x) {
    return switch(x) {
      case operation_attributes_, job_attributes_, printer_attributes_,
           unsupported_attributes_, end_of_attributes_ -> true;
      default -> false;
    };
  }

  static boolean value(int x) {
    return switch(x) {
      case integer_, boolean_, enum_,
           octetString_, dateTime_,
           resolution_, rangeOfInteger_,
           textWithLanguage_, nameWithLanguage_,
           textWithoutLanguage_, nameWithoutLanguage_,
           keyword_, uri_, uriScheme_,
           charset_, naturalLanguage_,
           mimeMediaType_ -> true;
      default ->
           out_of_band(x) || collection(x);
    };
  }

  static boolean out_of_band(int x) {
    return switch(x) {
      case unsupported_, unknown_, no_value_ -> true;
      default -> false;
    };
  }

  static boolean collection(int x) {
    return switch(x) {
      case begCollection_, endCollection_, memberAttrName_ -> true;
      default -> false;
    };
  }

}
