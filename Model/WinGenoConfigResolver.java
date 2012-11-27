package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class WinGenoConfigResolver {

  private Map<Integer, WinGenoConfig> mapping;

  private WinGenoConfigResolver(Map<Integer, WinGenoConfig> mapping) {
    this.mapping = mapping;
  }

  public WinGenoConfig resolve(int confId) {
    final WinGenoConfig conf = mapping.get(confId);
    if (conf == null)
      throw new IllegalArgumentException("Unrecognized window configuration ID: " + confId);
    return conf;
  }

  public static WinGenoConfigResolver load(String filename) throws IOException {
    Map<Integer, WinGenoConfig> mapping = new HashMap<Integer, WinGenoConfig>();

    BufferedReader reader = new BufferedReader(new FileReader(filename));
    while (true) {
      String line = reader.readLine();
      if (line == null)
        break;

      StringTokenizer st = new StringTokenizer(line);
      Integer confId = Integer.parseInt(st.nextToken());
      WinGenoConfig confRepr = new WinGenoConfig(st.nextToken());
      mapping.put(confId, confRepr);
    }

    return new WinGenoConfigResolver(mapping);
  }

}
