package handling.channel.handler;

import server.maps.AnimatedMapleMapObject;
import server.movement.LifeMovement;
import server.movement.LifeMovementFragment;
import server.movement.StaticLifeMovement;
import tools.data.LittleEndianAccessor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MovementParse {

    //1 = player, 2 = mob, 3 = pet, 4 = summon, 5 = dragon
    public static final List<LifeMovementFragment> parseMovement(final LittleEndianAccessor lea, int kind) {
        final List<LifeMovementFragment> res = new ArrayList<>();
        final byte numCommands = lea.readByte();

       for (byte i = 0; i < numCommands; i++) {
                byte command = lea.readByte();
                switch (command) {

                    case 0: // normal move
                    case 5:
                    case 15:
                    case 17: { // Float
                        final short xpos = lea.readShort();
                        final short ypos = lea.readShort();
                        final short xwobble = lea.readShort();
                        final short ywobble = lea.readShort();
                        final short unk = lea.readShort();
                        short fh = 0;

                        if (command == 15) {
                            fh = lea.readShort();
                        }
                        byte newstate = lea.readByte();
                        short duration = lea.readShort();
                        StaticLifeMovement mov = new StaticLifeMovement(command, new Point(xpos, ypos), duration, newstate, unk);
                        mov.setUnk(unk);
                        mov.setFh(fh);
                        mov.setPixelsPerSecond(new Point(xwobble, ywobble));
                        res.add(mov);
                        break;
                    }
                    case 1:
                    case 2:
                    case 6:
                    case 12:
                    case 13:
                    case 16:
                    case 18:
                    case 19:
                    case 22: {
                        final short xwobble = lea.readShort();
                        final short ywobble = lea.readShort();
                        byte newstate = lea.readByte();
                        short duration = lea.readShort();
                        StaticLifeMovement mov = new StaticLifeMovement(command, null, duration, newstate, 0);
                        mov.setPixelsPerSecond(new Point(xwobble, ywobble));
                        res.add(mov);
                        break;
                    }

                    case 3:
                    case 4:
                    case 7:
                    case 8:
                    case 9:
                    case 11: {
                        final short xpos = lea.readShort();
                        final short ypos = lea.readShort();
                        final short unk = lea.readShort();
                        final byte newstate = lea.readByte();
                        final short duration = lea.readShort();
                        StaticLifeMovement mov = new StaticLifeMovement(command, new Point(xpos, ypos), 0, newstate, 0);
                        mov.setUnk(unk);
                        res.add(mov);
                        break;
                    }
                    case 10: // Change Equip
                    {
                        final byte newstate = 0;
                        final short duration = 0;
                        final int wui = lea.readByte();
                        final StaticLifeMovement mov = new StaticLifeMovement(command, null, duration, newstate, 0);
                        mov.setWui(wui);
                        res.add(mov);
                        break;
                    }
                    case 14: {
                        final short xwobble = lea.readShort();
                        final short ywobble = lea.readShort();
                        short fh = lea.readShort();
                        byte newstate = lea.readByte();
                        short duration = lea.readShort();
                        StaticLifeMovement mov = new StaticLifeMovement(command, null, duration, newstate, 0);
                        mov.setPixelsPerSecond(new Point(xwobble, ywobble));
                        mov.setFh(fh);
                        res.add(mov);
                        break;
                    }
                    default:
                        byte newstate = lea.readByte();
                        short duration = lea.readShort();
                        StaticLifeMovement mov = new StaticLifeMovement(command, null, duration, newstate, 0);
                        res.add(mov);
                        break;
                }
            }
        if (numCommands != res.size()) {
            System.out.println("´íÎóµÄÔË¶¯");
            return null; // Probably hack
        }
        return res;
    }

    public static final void updatePosition(final List<LifeMovementFragment> movement, final AnimatedMapleMapObject target, final int yoffset) {
        for (final LifeMovementFragment move : movement) {
            if (move instanceof LifeMovement) {
                if (move instanceof StaticLifeMovement) {
                    Point position = move.getPosition();
                    if (position != null) {
                        position.y += yoffset;
                        target.setPosition(position);
                    }
                }
                target.setFh(((LifeMovement) move).getNewFh());
                target.setStance(((StaticLifeMovement) move).getNewstate());
            }
        }
    }
}
