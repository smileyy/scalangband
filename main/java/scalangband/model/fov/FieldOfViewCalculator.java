package scalangband.model.fov;

import scalangband.model.level.DungeonLevel;
import scalangband.model.location.Coordinates;

/**
 * Translated from http://www.adammil.net/blog/v125_roguelike_vision_algorithms.html
 */
public class FieldOfViewCalculator {
    public void recompute(Coordinates origin, DungeonLevel level, int range) {
        if (level.debug()) {
            level.setAllTilesVisible();
        } else {
            level.setAllTilesInvisible();
        }
        
        level.apply(origin).setVisible(true);
        for (int octant = 0; octant < 8; octant++) {
            compute(octant, origin, 1, new Slope(1, 1), new Slope(0, 1), level, range);
        }
    }

    private void compute(int octant, Coordinates origin, int x, Slope top, Slope bottom, DungeonLevel level, int range) {
        for (; x <= range; x++) {
            int topY;
            if (top.x() == 1) {
                topY = x;
            } else {
                topY = ((x * 2 - 1) * top.y() + top.x()) / (top.x() * 2);
                if (blocksLight(x, topY, octant, origin, level)) {
                    if (top.greaterOrEqual(topY * 2 + 1, x * 2) && !blocksLight(x, topY + 1, octant, origin, level)) {
                        topY++;
                    }
                } else {
                    int ax = x * 2;
                    if (blocksLight(x + 1, topY + 1, octant, origin, level)) {
                        ax++;
                    }
                    if (top.greater(topY * 2 + 1, ax)) {
                        topY++;
                    }
                }
            }

            int bottomY;
            if (bottom.y() == 0) {
                bottomY = 0;
            } else {
                bottomY = ((x * 2 - 1) * bottom.y() + bottom.x()) / (bottom.x() * 2);
                if (bottom.greaterOrEqual(bottomY * 2 + 1, x * 2) && blocksLight(x, bottomY, octant, origin, level) && !blocksLight(x, bottomY + 1, octant, origin, level)) {
                    bottomY++;
                }
            }

            int wasOpaque = -1; // 0:false, 1:true, -1:not applicable
            for(int y = topY; y >= bottomY; y--)
            {
                if(range < 0 || getDistance(x, y) <= range) {
                    boolean isOpaque = blocksLight(x, y, octant, origin, level);
                    boolean isVisible = isOpaque || ((y != topY || top.greater(y * 4 - 1, x * 4 + 1)) && (y != bottomY || bottom.less(y * 4 + 1, x * 4 - 1)));

                    if (isVisible) {
                        setVisible(x, y, octant, origin, level);
                    }

                    if(x != range) {
                        if(isOpaque) {
                            if(wasOpaque == 0) {
                                int nx = x * 2;
                                int ny = y * 2 + 1;
                                if (blocksLight(x, y+1, octant, origin, level)) {
                                    nx--;
                                }
                                if(top.greater(ny, nx)) {
                                    if (y == bottomY) {
                                        bottom = new Slope(ny, nx);
                                        break;
                                    } // don't recurse unless necessary
                                    else compute(octant, origin, x+1, top, new Slope(ny, nx), level, range);
                                }
                                else {
                                    if(y == bottomY) return;
                                }
                            }
                            wasOpaque = 1;
                        }
                        else
                        {
                            if (wasOpaque > 0) {
                                int nx = x * 2;
                                int ny = y * 2 + 1;

                                if(blocksLight(x + 1, y + 1, octant, origin, level)) {
                                    nx++; // check the right of the opaque tile (y+1), not this one
                                }
                                if (bottom.greaterOrEqual(ny, nx)) {
                                    return;
                                }
                                top = new Slope(ny, nx);
                            }
                            wasOpaque = 0;
                        }
                    }
                }
            }
            if(wasOpaque != 0) break;
        }
    }

    private static int getDistance(int x, int y) {
        return (int)Math.sqrt(x * x + y * y);
    }

    private static boolean blocksLight(int x, int y, int octant, Coordinates origin, DungeonLevel level) {
        int nx = origin.col();
        int ny = origin.row();

        switch (octant) {
            case 0:
                nx += x;
                ny -= y;
                break;
            case 1:
                nx += y;
                ny -= x;
                break;
            case 2:
                nx -= y;
                ny -= x;
                break;
            case 3:
                nx -= x;
                ny -= y;
                break;
            case 4:
                nx -= x;
                ny += y;
                break;
            case 5:
                nx -= y;
                ny += x;
                break;
            case 6:
                nx += y;
                ny += x;
                break;
            case 7:
                nx += x;
                ny += y;
                break;
        }

        if (nx < 0 || nx > level.width() - 1) return false;
        if (ny < 0 || ny > level.height() - 1) return false;

        return level.apply(ny, nx).opaque();
    }

    private static void setVisible(int x, int y, int octant, Coordinates origin, DungeonLevel level) {
        int nx = origin.col();
        int ny = origin.row();
        switch(octant) {
            case 0: nx += x; ny -= y; break;
            case 1: nx += y; ny -= x; break;
            case 2: nx -= y; ny -= x; break;
            case 3: nx -= x; ny -= y; break;
            case 4: nx -= x; ny += y; break;
            case 5: nx -= y; ny += x; break;
            case 6: nx += y; ny += x; break;
            case 7: nx += x; ny += y; break;
        }

        level.apply(ny, nx).setVisible(true);
    }
}
