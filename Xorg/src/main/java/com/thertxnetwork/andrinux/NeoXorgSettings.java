package io.neoterm;

import com.thertxnetwork.andrinux.xorg.NeoXorgViewClient;

/**
 * @author kiva
 */

public class NeoXorgSettings {
  public static void init(NeoXorgViewClient client) {
    Settings.Load(client);
  }
}
