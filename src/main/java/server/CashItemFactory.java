package server;
import database.DatabaseConnection;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;

public class CashItemFactory {
  private static final CashItemFactory instance = new CashItemFactory();
  private static final int[] bestItems = new int[] { 10099999, 10099998, 10099997, 10099996, 10099995 };
  private boolean initialized = false;
  private final Map<Integer, CashItemInfo> itemStats = new HashMap<>();
  private final Map<Integer, List<CashItemInfo>> itemPackage = new HashMap<>();
  private final Map<Integer, CashItemInfo.CashModInfo> itemMods = new HashMap<>();
  private final MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Etc.wz"));
  
  private final MapleDataProvider itemStringInfo = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/String.wz"));
  private Map<Integer, Integer> idLookup = new HashMap<>();

  
  public static final CashItemFactory getInstance() { return instance; }





  
  public void initialize2() {}




  
  public void initialize() {
    List<Integer> itemids = new ArrayList<>();
    for (MapleData field : this.data.getData("Commodity.img").getChildren()) {
      int SN = MapleDataTool.getIntConvert("SN", field, 0);
      int itemId = MapleDataTool.getIntConvert("ItemId", field, 0);
      CashItemInfo stats = new CashItemInfo(itemId, MapleDataTool.getIntConvert("Count", field, 1), MapleDataTool.getIntConvert("Price", field, 0), SN, MapleDataTool.getIntConvert("Period", field, 0), MapleDataTool.getIntConvert("Gender", field, 2), (MapleDataTool.getIntConvert("OnSale", field, 0) > 0));

























      
      if (SN > 0) {
        this.itemStats.put(Integer.valueOf(SN), stats);
        this.idLookup.put(Integer.valueOf(itemId), Integer.valueOf(SN));
      } 
      
      if (itemId > 0) {
        itemids.add(Integer.valueOf(itemId));
      }
    } 
    for (Iterator<Integer> i$ = itemids.iterator(); i$.hasNext(); ) { int i = ((Integer)i$.next()).intValue();
      getPackageItems(i); }
    
    try {
      Connection con = DatabaseConnection.getConnection();
      try(PreparedStatement ps = con.prepareStatement("SELECT * FROM cashshop_modified_items"); ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          CashItemInfo.CashModInfo ret = new CashItemInfo.CashModInfo(rs.getInt("serial"), rs.getInt("discount_price"), rs.getInt("mark"), (rs.getInt("showup") > 0), rs.getInt("itemid"), rs.getInt("priority"), (rs.getInt("package") > 0), rs.getInt("period"), rs.getInt("gender"), rs.getInt("count"), rs.getInt("meso"), rs.getInt("unk_1"), rs.getInt("unk_2"), rs.getInt("unk_3"), rs.getInt("extra_flags"));





          
          if (ret.showUp) {
            this.itemMods.put(Integer.valueOf(ret.sn), ret);
            CashItemInfo cc = this.itemStats.get(Integer.valueOf(ret.sn));
            if (cc != null) {
              ret.toCItem(cc);
            }
          } 
        } 
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    for (Iterator<Integer> i$ = this.itemStats.keySet().iterator(); i$.hasNext(); ) { int i = ((Integer)i$.next()).intValue();
      
      getItem(i); }
    
    this.initialized = true;
  }
  
  public final CashItemInfo getItem(int sn) {
    CashItemInfo stats = this.itemStats.get(Integer.valueOf(sn));
    
    CashItemInfo.CashModInfo z = getModInfo(sn);
    if (z != null && z.showUp) {
      return z.toCItem(stats);
    }
    if (stats == null || !stats.onSale()) {
      return null;
    }
    
    return stats;
  }
  
  public final List<CashItemInfo> getPackageItems(int itemId) {
    if (this.itemPackage.get(Integer.valueOf(itemId)) != null) {
      return this.itemPackage.get(Integer.valueOf(itemId));
    }
    List<CashItemInfo> packageItems = new ArrayList<>();
    
    MapleData b = this.data.getData("CashPackage.img");










    
    if (b == null || b.getChildByPath(itemId + "/SN") == null) {
      return null;
    }
    for (MapleData d : b.getChildByPath(itemId + "/SN").getChildren()) {
      packageItems.add(this.itemStats.get(Integer.valueOf(MapleDataTool.getIntConvert(d))));
    }
    this.itemPackage.put(Integer.valueOf(itemId), packageItems);
    return packageItems;
  }

  
  public final CashItemInfo.CashModInfo getModInfo(int sn) { return this.itemMods.get(Integer.valueOf(sn)); }

























  
   public final Collection<CashItemInfo.CashModInfo> getAllModInfo() {
    if (!this.initialized) {
      initialize();
    }
    return this.itemMods.values();
  }

  
  public final int[] getBestItems() { return bestItems; }


  
  public int getSnFromId(int itemId) { return ((Integer)this.idLookup.get(Integer.valueOf(itemId))).intValue(); }
  
  public final void clearCashShop() {
    this.itemStats.clear();
    this.itemPackage.clear();
    this.itemMods.clear();
    this.idLookup.clear();
    this.initialized = false;
    initialize();
  }
  
  public final int getItemSN(int itemid) {
    for (Map.Entry<Integer, CashItemInfo> ci : this.itemStats.entrySet()) {
      if (((CashItemInfo)ci.getValue()).getId() == itemid) {
        return ((CashItemInfo)ci.getValue()).getSN();
      }
    } 
    return 0;
  }
}
