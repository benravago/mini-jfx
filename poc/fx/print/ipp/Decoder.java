package fx.print.ipp;

import java.util.function.IntConsumer;

import static fx.print.ipp.Tag.*;

import java.io.UnsupportedEncodingException;

class Decoder {

  @FunctionalInterface
  interface ValueConsumer {
    void accept(int tag, String name, Object value);
  }

  String charsetName = "UTF-8";
  int version, code, id;

  private byte[] b;
  private int p;

  int short_() {
    return ((b[p++] & 0xff) << 8) | (b[p++] & 0xff);
  }

  int integer_() {
    return ((b[p++] & 0xff) << 24) | ((b[p++] & 0xff) << 16) | ((b[p++] & 0xff) << 8) | (b[p++] & 0xff);
  }

  boolean signed_byte(int off, int len) {
    assert len == 1;
    len = b[off];
    assert len == 0 || len == 1;
    return len == 0x01;
  }

  int signed_integer(int off, int len) {
    assert len == 4;
    len = p; p = off;
    off = integer_();
    p = len;
    return off;
  }

  byte[] octet_string(int off, int len) {
    var copy = new byte[len];
    System.arraycopy(b,off,copy,0,len);
    return copy;
  }

  String us_ascii_string(int off, int len) {
    var s = new StringBuilder();
    for (len += off; off < len; len++) {
      s.append((char)b[off]);
    }
    return s.toString();
  }

  String localized_string(int off, int len) {
    try { return new String(b,off,len,charsetName); }
    catch (UnsupportedEncodingException e) { return uncheck(e); }
  }

  String[] localized_octet_string(int off, int len) {
    try { // bytes[2,n,2,s]
      var save = p;
      p = off;
      len = short_();
      var lang = new String(b,p,len,charsetName);
      p += len;
      len = short_();
      var text = new String(b,p,len,charsetName);
      p = save;
      return new String[]{ lang, text };
    }
    catch (UnsupportedEncodingException e) { return uncheck(e); }
  }

  int[] rangeOfInteger(int off, int len) {
    assert len == 8;
    len = p; p = off;
    var lower = integer_();
    var upper = integer_();
    p = len;
    return new int[]{ lower, upper };
  }

  int[] resolution(int off, int len) {
    assert len == 9;
    len = p; p = off;
    var cross = integer_();
    var feed = integer_();
    var unit = b[p];
    p = len;
    return new int[]{ cross, feed, unit };
  }

  byte[] dateTime(int off, int len) {
    assert len == 11;
    return octet_string(off,11);
  }

  void decode(byte[] in, IntConsumer onDelimiter, ValueConsumer onValue) {
    b = in; p = 0;

    version = short_();
    code = short_();
    id = integer_();

    int tag;
    int name_offset=0, name_length;
    int value_offset=0, value_length;

    var end = b.length;
    while (p < end) {
      tag = b[p++];
      if (Tag.delimiter(tag)) {
        onDelimiter.accept(tag);
      } else if (Tag.value(tag)) {
        if ((name_length = short_()) > 0) {
          name_offset = p;
          p += name_length;
        }
        if ((value_length = short_()) > 0) {
          value_offset = p;
          p += value_length;
        }
        var name = name_length > 0 ? us_ascii_string(name_offset,name_length) : null;
        var value = value_length > 0 ? value_object(tag,value_offset,value_length) : null;
        onValue.accept(tag,name,value);
      } else {
        throw new UnsupportedOperationException("bad tag 0x"+Integer.toHexString(tag));
      }
    } // while()
  }

  Object value_object(int tag, int off, int len) {
    return switch (tag) {
      case textWithoutLanguage_, nameWithoutLanguage_ -> localized_string(off,len);
      case textWithLanguage_, nameWithLanguage_ -> localized_octet_string(off,len);
      case charset_, naturalLanguage_, keyword_, uri_, uriScheme_, mimeMediaType_ -> us_ascii_string(off,len);
      case boolean_ -> signed_byte(off,len);
      case integer_, enum_ -> signed_integer(off,len);
      case octetString_ -> octet_string(off,len);
      case resolution_ -> resolution(off,len);
      case rangeOfInteger_ -> rangeOfInteger(off,len);
      case dateTime_ -> dateTime(off,len);
      default -> octet_string(off,len);
    };
  }

  @SuppressWarnings("unchecked")
  final static <T extends Throwable, V> V uncheck(Throwable e) throws T { throw (T) e; }
}
