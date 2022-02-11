package fx.print.ipp;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import static fx.print.ipp.Tag.*;

class Encoder extends ByteArrayOutputStream {

  String charsetName = "UTF-8";

  private final byte[] b = new byte[16];

  void short_(int i) {
    b[0] = (byte) (i >> 8);
    b[1] = (byte) (i);
    write(b, 0, 2);
  }

  void integer_(int i) {
    b[0] = (byte) (i >> 24);
    b[1] = (byte) (i >> 16);
    b[2] = (byte) (i >> 8);
    b[3] = (byte) (i);
    write(b, 0, 4);
  }

  void signed_byte(int v) {
    b[0] = 0;
    b[1] = 1;
    b[2] = (byte) (v);
    write(b, 0, 3);
  }

  void signed_integer(int v) {
    b[0] = 0;
    b[1] = 4;
    b[2] = (byte) (v >> 24);
    b[3] = (byte) (v >> 16);
    b[4] = (byte) (v >> 8);
    b[5] = (byte) (v);
    write(b, 0, 6);
  }

  void octet_string(byte[] a) {
    var n = a.length;
    b[0] = (byte)(n >> 8);
    b[1] = (byte)(n);
    write(b, 0, 2);
    write(a, 0, n);
  }

  void us_ascii_string(String s) {
    var n = s.length();
    var a = new byte[n];
    for (var i = 0; i < n; i++) {
      a[i] = (byte) s.charAt(i);
    }
    octet_string(a);
  }

  void localized_string(String s) {
    try { octet_string(s.getBytes(charsetName)); }
    catch (UnsupportedEncodingException e) { uncheck(e); }
  }

  void localized_octet_string(String language, String string) {
    try {
      var lang = language.getBytes(charsetName);
      var text = string.getBytes(charsetName);
      short_(lang.length + text.length);
      octet_string(lang);
      octet_string(text);
    }
    catch (UnsupportedEncodingException e) { uncheck(e); }
  }

  void rangeOfInteger(int lower, int upper) {
    b[0]=0; b[1]=8; // value-length = 4+4
    b[2]=(byte)(lower>>24); b[3]=(byte)(lower>>16); b[4]=(byte)(lower>>8); b[5]=(byte)(lower);
    b[6]=(byte)(upper>>24); b[7]=(byte)(upper>>16); b[8]=(byte)(upper>>8); b[9]=(byte)(upper);
    write(b,0,10);
  }

  void resolution(int cross, int feed, int unit) {
    b[0]=0; b[1]=9; // value-length = 4+4+1
    b[2]=(byte)(cross>>24); b[3]=(byte)(cross>>16); b[4]=(byte)(cross>>8); b[5]=(byte)(cross);
    b[6]=(byte)(feed>>24);  b[7]=(byte)(feed>>16);  b[8]=(byte)(feed>>8);  b[9]=(byte)(feed);
    b[10]=(byte)(unit);
    write(b,0,11);
  }

  void dateTime(byte[] dt) {
    assert dt.length == 11;
    write(dt,0,11); // eleven octets as defined by "DateAndTime" in RFC 2579 [RFC2579]
  }

  void encode(int tag, String name, Object... values) {
    assert name != null && !name.isBlank();
    assert values != null && values.length > 0;
    var n = values.length;
    var t = new byte[]{ (byte)tag, 0, 0 };
    write(t,0,1); // value-tag
    us_ascii_string(name); // name-length, name
    for (var v:values) { // value-length, value
      switch (tag) {
        case textWithoutLanguage_, nameWithoutLanguage_ -> localized_string( (String)v );
        case textWithLanguage_, nameWithLanguage_ -> { var s=(String[])v; localized_octet_string(s[0],s[1]); }
        case charset_, naturalLanguage_, keyword_, uri_, uriScheme_, mimeMediaType_ -> us_ascii_string( (String)v );
        case boolean_ -> signed_byte( (Boolean)v ? 0x01 : 0x00 );
        case integer_, enum_ -> signed_integer( (Integer)v );
        case octetString_ -> octet_string( (byte[])v );
        case resolution_ -> { var i=(int[])v; resolution(i[0],i[1],i[2] ); }
        case rangeOfInteger_ -> { var i=(int[])v; rangeOfInteger(i[0],i[1]); }
        case dateTime_ -> dateTime( (byte[])v );
        default -> write(t,1,2); // ignore; value = 0x0000;
      }
      if (--n < 1) break;
      write(t,0,3); // value-tag for additional value
    }
  }

  @SuppressWarnings("unchecked")
  final static <T extends Throwable, V> V uncheck(Throwable e) throws T { throw (T) e; }
}