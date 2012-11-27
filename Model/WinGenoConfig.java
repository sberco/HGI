package Model;

public class WinGenoConfig {

  private String repr;

  public WinGenoConfig(String repr) {
    this.repr = repr;
  }

  public int countReverseHomozygotes(WinGenoConfig other) {
    final int n = Math.min(repr.length(), other.repr.length());
    int rhCount = 0;
    for (int i = 0; i < n; ++i) {
      final char a = repr.charAt(i);
      final char b = other.repr.charAt(i);
      if ((a == '0' && b == '2') || (a == '2' && b == '0'))
        ++rhCount;
    }
    return rhCount;
  }

}
