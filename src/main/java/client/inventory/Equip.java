package client.inventory;

import client.MapleClient;
import constants.GameConstants;
import handling.world.World;
import server.MapleItemInformationProvider;
import server.ServerProperties;
import tools.MaplePacketCreator;
import tools.Pair;

import java.io.Serializable;
import java.util.List;

import static abc.Game2.永恒装备;
import static abc.Game2.重生装备;
//装备升级给经验？？

public class Equip extends Item implements IEquip, Serializable {

    private byte upgradeSlots = 0, level = 0, vicioushammer = 0, enhance = 0;
    private short str = 0, dex = 0, _int = 0, luk = 0, hp = 0, mp = 0, watk = 0, matk = 0, wdef = 0, mdef = 0, acc = 0, avoid = 0, hands = 0, speed = 0, jump = 0, potential1 = 0, potential2 = 0, potential3 = 0, hpR = 0, mpR = 0;
    private int itemEXP = 0, durability = -1;
    private byte itemLevel;
    private String DaKongFuMo;

    public Equip(int id, short position) {
        super(id, position, (short) 1, (byte) 0);
    }

    public Equip(int id, short position, byte flag) {
        super(id, position, (short) 1, flag);
    }

    public Equip(int id, short position, int uniqueid, byte flag) {
        super(id, position, (short) 1, flag, uniqueid);
    }

    @Override
    public IItem copy() {
        Equip ret = new Equip(getItemId(), getPosition(), getUniqueId(), getFlag());
        ret.str = str;
        ret.dex = dex;
        ret._int = _int;
        ret.luk = luk;
        ret.hp = hp;
        ret.mp = mp;
        ret.matk = matk;
        ret.mdef = mdef;
        ret.watk = watk;
        ret.wdef = wdef;
        ret.acc = acc;
        ret.avoid = avoid;
        ret.hands = hands;
        ret.speed = speed;
        ret.jump = jump;
        ret.enhance = enhance;
        ret.upgradeSlots = upgradeSlots;
        ret.level = level;
        ret.itemEXP = itemEXP;
        ret.durability = durability;
        ret.vicioushammer = vicioushammer;
        ret.potential1 = potential1;
        ret.potential2 = potential2;
        ret.potential3 = potential3;
        ret.hpR = hpR;
        ret.mpR = mpR;
        ret.itemLevel = this.itemLevel;
        ret.setGiftFrom(getGiftFrom());
        ret.setOwner(getOwner());
        ret.setQuantity(getQuantity());
        ret.setExpiration(getExpiration());
        ret.setDaKongFuMo(getDaKongFuMo());
        ret.setPrice(getPrice());
        return ret;
    }

    @Override
    public byte getType() {
        return 1;
    }

    @Override
    public byte getUpgradeSlots() {
        return upgradeSlots;
    }

    @Override
    public short getStr() {
        return str;
    }

    @Override
    public short getDex() {
        return dex;
    }

    @Override
    public short getInt() {
        return _int;
    }

    @Override
    public short getLuk() {
        return luk;
    }

    @Override
    public short getHp() {
        return hp;
    }

    @Override
    public short getMp() {
        return mp;
    }

    @Override
    public short getWatk() {
        return watk;
    }

    @Override
    public short getMatk() {
        return matk;
    }

    @Override
    public short getWdef() {
        return wdef;
    }

    @Override
    public short getMdef() {
        return mdef;
    }

    @Override
    public short getAcc() {
        return acc;
    }

    @Override
    public short getAvoid() {
        return avoid;
    }

    @Override
    public short getHands() {
        return hands;
    }

    @Override
    public short getSpeed() {
        return speed;
    }

    @Override
    public short getJump() {
        return jump;
    }

    public void setStr(short str) {
        if (str < 0) {
            str = 0;
        }
        this.str = str;
    }

    public void setDex(short dex) {
        if (dex < 0) {
            dex = 0;
        }
        this.dex = dex;
    }

    public void setInt(short _int) {
        if (_int < 0) {
            _int = 0;
        }
        this._int = _int;
    }

    public void setLuk(short luk) {
        if (luk < 0) {
            luk = 0;
        }
        this.luk = luk;
    }

    public void setHp(short hp) {
        if (hp < 0) {
            hp = 0;
        }
        this.hp = hp;
    }

    public void setMp(short mp) {
        if (mp < 0) {
            mp = 0;
        }
        this.mp = mp;
    }

    public void setWatk(short watk) {
        if (watk < 0) {
            watk = 0;
        }
        this.watk = watk;
    }

    public void setMatk(short matk) {
        if (matk < 0) {
            matk = 0;
        }
        this.matk = matk;
    }

    public void setWdef(short wdef) {
        if (wdef < 0) {
            wdef = 0;
        }
        this.wdef = wdef;
    }

    public void setMdef(short mdef) {
        if (mdef < 0) {
            mdef = 0;
        }
        this.mdef = mdef;
    }

    public void setAcc(short acc) {
        if (acc < 0) {
            acc = 0;
        }
        this.acc = acc;
    }

    public void setAvoid(short avoid) {
        if (avoid < 0) {
            avoid = 0;
        }
        this.avoid = avoid;
    }

    public void setHands(short hands) {
        if (hands < 0) {
            hands = 0;
        }
        this.hands = hands;
    }

    public void setSpeed(short speed) {
        if (speed < 0) {
            speed = 0;
        }
        this.speed = speed;
    }

    public void setJump(short jump) {
        if (jump < 0) {
            jump = 0;
        }
        this.jump = jump;
    }

    public void setUpgradeSlots(byte upgradeSlots) {
        this.upgradeSlots = upgradeSlots;
    }

    @Override
    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    @Override
    public byte getViciousHammer() {
        return vicioushammer;
    }

    public void setViciousHammer(byte ham) {
        vicioushammer = ham;
    }

    @Override
    public int getItemEXP() {
        return itemEXP;
    }

    public void setItemEXP(int itemEXP) {
        if (itemEXP < 0) {
            itemEXP = 0;
        }
        this.itemEXP = itemEXP;
    }

    @Override
    public int getEquipExp() {
        if (itemEXP <= 0) {
            return 0;
        }
        //aproximate value
        if (GameConstants.isWeapon(getItemId())) {
            return itemEXP / IEquip.WEAPON_RATIO;
        } else {
            return itemEXP / IEquip.ARMOR_RATIO;
        }
    }

    @Override
    public int getEquipExpForLevel() {
        if (getEquipExp() <= 0) {
            return 0;
        }
        int expz = getEquipExp();
        for (int i = getBaseLevel(); i <= GameConstants.getMaxLevel(getItemId()); i++) {
            if (expz >= GameConstants.getExpForLevel(i, getItemId())) {
                expz -= GameConstants.getExpForLevel(i, getItemId());
            } else { //for 0, dont continue;
                break;
            }
        }
        return expz;
    }

    /**
     *
     * @return
     */
    @Override
    public int getExpPercentage() {
        return this.itemEXP;
    }

    /*@Override
    public int getExpPercentage() {
        if (getEquipLevels() < getBaseLevel() || getEquipLevels() > GameConstants.getMaxLevel(getItemId()) || GameConstants.getExpForLevel(getEquipLevels(), getItemId()) <= 0) {
            return 0;
        }
        return getEquipExpForLevel() * 100 / GameConstants.getExpForLevel(getEquipLevels(), getItemId());
    }*/
    public int getEquipLevels() {
        if (GameConstants.getMaxLevel(getItemId()) <= 0) {
            return 0;
        } else if (getEquipExp() <= 0) {
            return getBaseLevel();
        }
        int levelz = getBaseLevel();
        int expz = getEquipExp();
        for (int i = levelz; (GameConstants.getStatFromWeapon(getItemId()) == null ? (i <= GameConstants.getMaxLevel(getItemId())) : (i < GameConstants.getMaxLevel(getItemId()))); i++) {
            if (expz >= GameConstants.getExpForLevel(i, getItemId())) {
                levelz++;
                expz -= GameConstants.getExpForLevel(i, getItemId());
            } else { //for 0, dont continue;
                break;
            }
        }
        return levelz;
    }

    @Override
    public int getBaseLevel() {
        return (GameConstants.getStatFromWeapon(getItemId()) == null ? 1 : 0);
    }

    @Override
    public void setQuantity(short quantity) {
        if (quantity < 0 || quantity > 1) {
            throw new RuntimeException("Setting the quantity to " + quantity + " on an equip (itemid: " + getItemId() + ")");
        }
        super.setQuantity(quantity);
    }

    @Override
    public int getDurability() {
        return durability;
    }

    public void setDurability(final int dur) {
        this.durability = dur;
    }

    @Override
    public byte getEnhance() {
        return enhance;
    }

    public void setEnhance(final byte en) {
        this.enhance = en;
    }

    @Override
    public short getPotential1() {
        return potential1;
    }

    public void setPotential1(final short en) {
        this.potential1 = en;
    }

    @Override
    public short getPotential2() {
        return potential2;
    }

    public void setPotential2(final short en) {
        this.potential2 = en;
    }

    @Override
    public short getPotential3() {
        return potential3;
    }

    public void setPotential3(final short en) {
        this.potential3 = en;
    }

    @Override
    public byte getState() {
        final int pots = potential1 + potential2 + potential3;
        if (potential1 >= 30000 || potential2 >= 30000 || potential3 >= 30000) {
            return 7;
        } else if (potential1 >= 20000 || potential2 >= 20000 || potential3 >= 20000) {
            return 6;
        } else if (pots >= 1) {
            return 5;
        } else if (pots < 0) {
            return 1;
        }
        return 0;
    }
//
//    public void resetPotential() { //equip first receive
//        //0.04% chance unique, 4% chance epic, else rare
//        final int rank = Randomizer.nextInt(100) < 4 ? (Randomizer.nextInt(100) < 4 ? -7 : -6) : -5;
//        setPotential1((short) rank);
//        setPotential2((short) (Randomizer.nextInt(10) == 1 ? rank : 0)); //1/10 chance of 3 line
//        setPotential3((short) 0); //just set it theoretically
//    }
//
//    public void renewPotential() {
//        //4% chance upgrade
//        final int rank = Randomizer.nextInt(100) < 4 && getState() != 7 ? -(getState() + 1) : -(getState());
//        setPotential1((short) rank);
//        setPotential2((short) (getPotential3() > 0 ? rank : 0)); //1/10 chance of 3 line
//        setPotential3((short) 0); //just set it theoretically
//    }

    @Override
    public short getHpR() {
        return hpR;
    }

    public void setHpR(final short hp) {
        this.hpR = hp;
    }

    @Override
    public short getMpR() {
        return mpR;
    }

    public void setMpR(final short mp) {
        this.mpR = mp;
    }

    public void gainItemLevel() {
        this.itemLevel = (byte) (this.itemLevel + 1);
    }

    public void gainItemExp(MapleClient c, int gain, boolean timeless) {
        this.itemEXP += gain;
        int expNeeded = 0;
        if (timeless) {
            expNeeded = ExpTable.getTimelessItemExpNeededForLevel(this.itemLevel + 1);
        } else {
            expNeeded = ExpTable.getTimelessItemExpNeededForLevel(this.itemLevel + 1);//getReverseItemExpNeededForLevel
        }
        if (this.itemEXP >= expNeeded) {
            // gainItemLevel();
            gainItemLevel(c, timeless);
            //gainLevel();
            //装备升级效果s
            c.sendPacket(MaplePacketCreator.showItemLevelup());
        }
    }

    public void gainItemLevel(MapleClient c, boolean timeless) {
        List<Pair<String, Integer>> stats = MapleItemInformationProvider.getInstance().getItemLevelupStats(getItemId(), itemLevel, timeless);
        int 等级 = getEquipLevel();

        String 属性 = "";
        String 属性1 = "";
        String 属性2 = "";
        String 属性3 = "";
        switch (等级) {
            case 1:
                属性 = "ZEV.四维属性1";
                属性1 = "ZEV.生命魔法1";
                属性2 = "ZEV.双攻1";
                属性3 = "ZEV.双防1";
                break;
            case 2:
                属性 = "ZEV.四维属性2";
                属性1 = "ZEV.生命魔法2";
                属性2 = "ZEV.双攻2";
                属性3 = "ZEV.双防2";
                break;
            case 3:
                属性 = "ZEV.四维属性3";
                属性1 = "ZEV.生命魔法3";
                属性2 = "ZEV.双攻3";
                属性3 = "ZEV.双防3";
                break;
            case 4:
                属性 = "ZEV.四维属性4";
                属性1 = "ZEV.生命魔法4";
                属性2 = "ZEV.双攻4";
                属性3 = "ZEV.双防4";
                break;
            case 5:
                属性 = "ZEV.四维属性5";
                属性1 = "ZEV.生命魔法5";
                属性2 = "ZEV.双攻5";
                属性3 = "ZEV.双防5";
                break;
            case 6:
                属性 = "ZEV.四维属性6";
                属性1 = "ZEV.生命魔法6";
                属性2 = "ZEV.双攻6";
                属性3 = "ZEV.双防6";
                break;
            case 7:
                属性 = "ZEV.四维属性7";
                属性1 = "ZEV.生命魔法7";
                属性2 = "ZEV.双攻7";
                属性3 = "ZEV.双防7";
                break;
            case 8:
                属性 = "ZEV.四维属性8";
                属性1 = "ZEV.生命魔法8";
                属性2 = "ZEV.双攻8";
                属性3 = "ZEV.双防8";
                break;
            case 9:
                属性 = "ZEV.四维属性9";
                属性1 = "ZEV.生命魔法9";
                属性2 = "ZEV.双攻9";
                属性3 = "ZEV.双防9";
                break;
            case 10:
                属性 = "ZEV.四维属性10";
                属性1 = "ZEV.生命魔法10";
                属性2 = "ZEV.双攻10";
                属性3 = "ZEV.双防10";
                break;
            case 11:
                属性 = "ZEV.四维属性11";
                属性1 = "ZEV.生命魔法11";
                属性2 = "ZEV.双攻11";
                属性3 = "ZEV.双防11";
                break;
            case 12:
                属性 = "ZEV.四维属性12";
                属性1 = "ZEV.生命魔法12";
                属性2 = "ZEV.双攻12";
                属性3 = "ZEV.双防12";
                break;
            case 13:
                属性 = "ZEV.四维属性13";
                属性1 = "ZEV.生命魔法13";
                属性2 = "ZEV.双攻13";
                属性3 = "ZEV.双防13";
                break;
            case 14:
                属性 = "ZEV.四维属性14";
                属性1 = "ZEV.生命魔法14";
                属性2 = "ZEV.双攻14";
                属性3 = "ZEV.双防14";
                break;
            case 15:
                属性 = "ZEV.四维属性15";
                属性1 = "ZEV.生命魔法15";
                属性2 = "ZEV.双攻15";
                属性3 = "ZEV.双防15";
                break;
            case 16:
                属性 = "ZEV.四维属性16";
                属性1 = "ZEV.生命魔法16";
                属性2 = "ZEV.双攻16";
                属性3 = "ZEV.双防16";
                break;
            case 17:
                属性 = "ZEV.四维属性17";
                属性1 = "ZEV.生命魔法17";
                属性2 = "ZEV.双攻17";
                属性3 = "ZEV.双防17";
                break;
            case 18:
                属性 = "ZEV.四维属性18";
                属性1 = "ZEV.生命魔法18";
                属性2 = "ZEV.双攻18";
                属性3 = "ZEV.双防18";
                break;
            case 19:
                属性 = "ZEV.四维属性19";
                属性1 = "ZEV.生命魔法19";
                属性2 = "ZEV.双攻19";
                属性3 = "ZEV.双防19";
                break;
            case 20:
                属性 = "ZEV.四维属性20";
                属性1 = "ZEV.生命魔法20";
                属性2 = "ZEV.双攻20";
                属性3 = "ZEV.双防20";
                break;
            case 21:
                属性 = "ZEV.四维属性21";
                属性1 = "ZEV.生命魔法21";
                属性2 = "ZEV.双攻21";
                属性3 = "ZEV.双防21";
                break;
            case 22:
                属性 = "ZEV.四维属性22";
                属性1 = "ZEV.生命魔法22";
                属性2 = "ZEV.双攻22";
                属性3 = "ZEV.双防22";
                break;
            case 23:
                属性 = "ZEV.四维属性23";
                属性1 = "ZEV.生命魔法23";
                属性2 = "ZEV.双攻23";
                属性3 = "ZEV.双防23";
                break;
            case 24:
                属性 = "ZEV.四维属性24";
                属性1 = "ZEV.生命魔法24";
                属性2 = "ZEV.双攻24";
                属性3 = "ZEV.双防24";
                break;
            case 25:
                属性 = "ZEV.四维属性25";
                属性1 = "ZEV.生命魔法25";
                属性2 = "ZEV.双攻25";
                属性3 = "ZEV.双防25";
                break;
            case 26:
                属性 = "ZEV.四维属性26";
                属性1 = "ZEV.生命魔法26";
                属性2 = "ZEV.双攻26";
                属性3 = "ZEV.双防26";
                break;
            case 27:
                属性 = "ZEV.四维属性27";
                属性1 = "ZEV.生命魔法27";
                属性2 = "ZEV.双攻27";
                属性3 = "ZEV.双防27";
                break;
            case 28:
                属性 = "ZEV.四维属性28";
                属性1 = "ZEV.生命魔法28";
                属性2 = "ZEV.双攻28";
                属性3 = "ZEV.双防28";
                break;
            case 29:
                属性 = "ZEV.四维属性29";
                属性1 = "ZEV.生命魔法29";
                属性2 = "ZEV.双攻29";
                属性3 = "ZEV.双防29";
                break;
            case 30:
                属性 = "ZEV.四维属性30";
                属性1 = "ZEV.生命魔法30";
                属性2 = "ZEV.双攻30";
                属性3 = "ZEV.双防30";
                break;
            default:
                属性 = "ZEV.四维属性30";
                属性1 = "ZEV.生命魔法30";
                属性2 = "ZEV.双攻30";
                属性3 = "ZEV.双防30";
                break;
        }
        int 四维属性 = Integer.parseInt(ServerProperties.getProperty(属性));
        int 生命魔法 = Integer.parseInt(ServerProperties.getProperty(属性1));
        int 双攻 = Integer.parseInt(ServerProperties.getProperty(属性2));
        int 双防 = Integer.parseInt(ServerProperties.getProperty(属性3));
        if (永恒装备(getItemId()) || 重生装备(getItemId())) {
            watk += 双攻;
            matk += 双攻;
            mdef += 双防;
            wdef += 双防;
            hp += 生命魔法;
            mp += 生命魔法;
            dex += 四维属性;
            str += 四维属性;
            _int += 四维属性;
            luk += 四维属性;
        } else {
            mdef += 双防 / 2;
            wdef += 双防 / 2;
            hp += 生命魔法 / 2;
            mp += 生命魔法 / 2;
            dex += 四维属性 / 2;
            str += 四维属性 / 2;
            _int += 四维属性 / 2;
            luk += 四维属性 / 2;
        }

        //提示语
        c.getPlayer().dropMessage(5, "[装备升级]: " + MapleItemInformationProvider.getInstance().getName(getItemId()) + " 达到 " + (getEquipLevel() + 1) + " 级，达到下一等级需要 " + ExpTable.getTimelessItemExpNeededForLevel((getEquipLevel() + 1)) + "");
        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[装备升级]: " + c.getPlayer().getName() + " 成功将 " + MapleItemInformationProvider.getInstance().getName(getItemId()) + " 升级到 " + (getEquipLevel() + 1) + " 级"));
        //增加一级
        this.itemLevel++;
        //刷新
        c.getPlayer().getClient().sendPacket(MaplePacketCreator.showEquipmentLevelUp());
        c.getPlayer().getClient().sendPacket(MaplePacketCreator.updateSpecialItemUse(this, getType()));

    }

    @Override
    public void setEquipLevel(byte gf) {
        this.itemLevel = gf;
    }

    @Override
    public byte getEquipLevel() {
        return itemLevel;
    }
}
/*for (Pair<String, Integer> stat : stats) {
             matk += 1;
            /*if (stat.getLeft().equals("incDEX")) {
                dex += stat.getRight();
            } else if (stat.getLeft().equals("incSTR")) {
                str += stat.getRight();
            } else if (stat.getLeft().equals("incINT")) {
                _int += stat.getRight();
            } else if (stat.getLeft().equals("incLUK")) {
                luk += stat.getRight();
            } else if (stat.getLeft().equals("incMHP")) {
                hp += stat.getRight();
            } else if (stat.getLeft().equals("incMMP")) {
                mp += stat.getRight();
            } else if (stat.getLeft().equals("incPAD")) {
                watk += stat.getRight();
            } else if (stat.getLeft().equals("incMAD")) {
                matk += stat.getRight();
            } else if (stat.getLeft().equals("incPDD")) {
                wdef += stat.getRight();
            } else if (stat.getLeft().equals("incMDD")) {
                mdef += stat.getRight();
            } else if (stat.getLeft().equals("incEVA")) {
                avoid += stat.getRight();
            } else if (stat.getLeft().equals("incACC")) {
                acc += stat.getRight();
            } else if (stat.getLeft().equals("incSpeed")) {
                speed += stat.getRight();
            } else if (stat.getLeft().equals("incJump")) {
                jump += stat.getRight();
            }
        }*/
//c.getPlayer().getClient().sendPacket(MaplePacketCreator.getCharInfo(c.getPlayer()));
/*  c.getPlayer().getMap().removePlayer(c.getPlayer());
            c.getPlayer().getMap().addPlayer(c.getPlayer());*/
//c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.showForeignEffect(c.getPlayer().getId(), 17));
// c.getPlayer().forceUpdateItem(MapleInventoryType.EQUIPPED, this);
/*if (getEquipLevel() <= 10) {
            数值 = (int) Math.ceil(Math.random() * 10);
            数值1 = (int) Math.ceil(Math.random() * 5);
        } else if (getEquipLevel() > 10 && getEquipLevel() <= 20) {
            数值 = (int) Math.ceil(Math.random() * 15);
            数值1 = (int) Math.ceil(Math.random() * 10);
        } else if (getEquipLevel() > 20 && getEquipLevel() <= 30) {
            数值 = (int) Math.ceil(Math.random() * 15);
            数值1 = (int) Math.ceil(Math.random() * 15);
        } else if (getEquipLevel() > 30 && getEquipLevel() <= 40) {
            数值 = (int) Math.ceil(Math.random() * 20);
            数值1 = (int) Math.ceil(Math.random() * 20);
        } else if (getEquipLevel() > 40 && getEquipLevel() <= 50) {
            数值 = (int) Math.ceil(Math.random() * 25);
            数值1 = (int) Math.ceil(Math.random() * 25);
        } else if (getEquipLevel() > 50 && getEquipLevel() <= 60) {
            数值 = (int) Math.ceil(Math.random() * 30);
            数值1 = (int) Math.ceil(Math.random() * 30);
        } else if (getEquipLevel() > 60) {
            数值 = (int) Math.ceil(Math.random() * 40);
            数值1 = (int) Math.ceil(Math.random() * 40);
        }*/
        //"四维属性" + getEquipLevel() + ""
