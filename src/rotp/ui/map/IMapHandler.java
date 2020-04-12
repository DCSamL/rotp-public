/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.ui.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.Border;
import rotp.model.Sprite;
import rotp.model.empires.Empire;
import rotp.model.empires.SystemView;
import rotp.model.galaxy.Galaxy;
import rotp.model.galaxy.IMappedObject;
import rotp.model.galaxy.Location;
import rotp.model.galaxy.StarSystem;
import rotp.ui.main.GalaxyMapPanel;

public interface IMapHandler {
    void repaint();
    GalaxyMapPanel map();

    Sprite hoveringSprite();
    Sprite clickedSprite();
    void clickedSprite(Sprite s);

    Border mapBorder();
    float startingScalePct();
    void checkMapInitialized();

    Location mapFocus();

    Color shadeC();
    Color backC();
    Color lightC();
    default void mapFocus(IMappedObject obj) { mapFocus().setXY(obj.x(), obj.y()); }
    default void drawYear(Graphics2D g) { }
    default void drawTitle(Graphics2D g) { }
    
    default boolean animating()    { return true; }

    default boolean forwardMouseEvents() { return false; }
    default void mouseWheelMoved(MouseWheelEvent e)  { }
    default boolean dragSelect(int x0, int y0, int x1, int y) { return false; }

    default void hoveringOverSprite(Sprite o)              { }

    default void clickingOnSprite(Sprite o, int cnt, boolean right, boolean click)       { }

    default void clickingNull(int cnt, boolean right) {  }

    default boolean masksMouseOver(int x, int y)       { return false; }

    default boolean isClicked(Sprite s)             { return clickedSprite() == s; }
    default boolean isHovering(Sprite s)            { return hoveringSprite() == s; }
    default boolean isHighlighting(Sprite s)        { return false; }
    default boolean isLowlighting(Sprite s)         { return false; }
    default boolean allowsDragSelect()              { return false; }
    default boolean hoverOverFleets()               { return true; }
    default boolean hoverOverSystems()              { return true; }
    default boolean hoverOverFlightPaths()          { return true; }

    default String systemLabel(StarSystem s)        { return Empire.thePlayer().sv.name(s.id); }
    default String systemLabel2(StarSystem s)       { return ""; }
    default Color systemLabelColor(StarSystem s)    { return Empire.thePlayer().sv.empireColor(s.id); }
    default List<Sprite> nextTurnSprites()          { return new ArrayList<>(); }
    default List<Sprite> controlSprites()           { return new ArrayList<>(); }
    default void reselectCurrentSystem() { }

    default int defaultFleetDisplay()             { return GalaxyMapPanel.SHOW_IMPORTANT_FLIGHTPATHS; }
    default int defaultShipRangesDisplay()        { return GalaxyMapPanel.SHOW_STARS_AND_RANGES; }
    default boolean defaultGridCircularDisplay()  { return false; }
    default IMappedObject gridOrigin()            { return null; }
    default void drawAlerts(Graphics2D g)         { }

    default Empire empireBoundaries()                    { return Galaxy.current().player(); }
    default float systemClickRadius()             { return 0.5f; }
    default boolean showYear()                    { return true; }
    default boolean drawBanner(StarSystem s)             { return false; }
    default boolean drawStar(StarSystem s)               { return true; }
    default boolean showOwnerReach(StarSystem s)         { return false; }
    default boolean showOwnership(StarSystem s)          { return true; }
    default float ownerReach(StarSystem s)              { return 0; }
    default boolean drawShield(StarSystem s)             { return false; }
    default boolean shouldDrawSprite(Sprite s)           { return true; }
    default boolean canChangeMapScales()                 { return true; }
    default boolean displayNextTurnNotice()              { return false; }
    default void paintOverMap(GalaxyMapPanel ui, Graphics2D g) { }
    default Color alertColor(SystemView sv)              { return null; }

}
