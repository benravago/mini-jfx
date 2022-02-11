package fx.print.ipp;

public class Messages {

  @SuppressWarnings("unchecked")
  public static Message parse(byte[] bytes) {
    var dec = new Decoder();
    var groups = new Vector<Message.Group>();
    var entries = new Vector[1];
    dec.decode(bytes,
      (delimiter) -> {
        if (delimiter == Tag.end_of_attributes_) return;
        entries[0] = new Vector<Message.Entry>();
        groups.add(new Message.Group(delimiter, entries[0]));
      },
      (tag,name,value) -> {
        entries[0].add(new Message.Entry(tag,name,value));
      }
    );
    return new Message( dec.version, dec.code, dec.id, groups );
  }

  public static byte[] format(Message message) {
    var enc = new Encoder();
    enc.integer_(message.version());
    enc.integer_(message.code());
    enc.integer_(message.id());
    for (var group:message.groups()) {
      enc.write(group.tag());
      for (var entry:group.entries()) {
        enc.encode(entry.tag(), entry.name(), entry.value());
      }
    }
    enc.write(Tag.end_of_attributes_);
    return enc.toByteArray();
  }

}