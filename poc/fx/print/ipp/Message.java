package fx.print.ipp;

public record Message(int version, int code, int id, Iterable<Group> groups) {
  public record Group(int tag, Iterable<Entry> entries) {}
  public record Entry(int tag, String name, Object value) {}
}
