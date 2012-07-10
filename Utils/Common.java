package Utils;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;
import java.util.Set;
import java.util.HashSet;

import Model.Blocks;
import Model.Index;
import Model.WindowIndex;
import Model.Labels;
import Model.Query;
import Model.Relations;
import Model.Scores;
import Model.WinModels;
import Model.Windows;
import Model.Window;
import Model.Block;

public class Common
{
  public static int CountReverseHomozygotes(Windows windows, Block block, Query query, Index dbSnpIndex, int queryIndIdx, int dbIndIdx) {
    Set<Integer> rh_snps = new HashSet<Integer>();

    int wstart = block.getFirstWindow();
    int wend   = block.getLastWindow() + 1;
    int mstart = windows.get(wstart).getFirstSnp();
    int mend   = windows.get(wend - 1).getLastSnp() + 1;

    int[] query_snp_confs = query.getIndConf(queryIndIdx);

    for (int s = mstart; s < mend; ++s) {
      WindowIndex dbIndex = dbSnpIndex.getWindowIndex(s);

      if (dbIndex.getConfigurationString(0) != null && !dbIndex.getConfigurationString(0).equals("0"))
        throw new RuntimeException("Expecting snp-based genotype configurations (configuration 0 should correspond to genotype conf '0')");
      if (dbIndex.getConfigurationString(2) != null && !dbIndex.getConfigurationString(2).equals("2"))
        throw new RuntimeException("Expecting snp-based genotype configurations (configuration 2 should correspond to genotype conf '2')");

      boolean ind1is0 = false;
      boolean ind2is0 = false;
      boolean ind1is2 = false;
      boolean ind2is2 = false;

      ind1is0 = query_snp_confs[s] == 0;
      ind1is2 = query_snp_confs[s] == 2;
      
      if (dbIndex.getIndList(0) != null)
      {
        for (int i : dbIndex.getIndList(0)) {
          if (i == dbIndIdx)
            ind2is0 = true;
        }
      }

      if (dbIndex.getIndList(2) != null)
      {
        for (int i : dbIndex.getIndList(2)) {
          if (i == dbIndIdx)
            ind2is2 = true;
        }
      }

      if ((ind1is0 && ind2is2) || (ind1is2 && ind2is0))
        rh_snps.add(s);
    }

    return rh_snps.size();
  }
}
