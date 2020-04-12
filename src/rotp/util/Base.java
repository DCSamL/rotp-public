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
package rotp.util;

import rotp.Rotp;
import rotp.apachemath.FastMath;
import rotp.model.empires.Empire;
import rotp.model.galaxy.Galaxy;
import rotp.model.galaxy.StarSystem;
import rotp.model.game.GameSession;
import rotp.model.game.IGameOptions;
import rotp.model.tech.Tech;
import rotp.model.tech.TechLibrary;
import rotp.ui.BasePanel;
import rotp.ui.RotPUI;
import rotp.ui.UserPreferences;
import rotp.util.sound.SoundClip;
import rotp.util.sound.SoundManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public interface Base {
    String[] monthName = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    String[] letter = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N"};
    Random random = new Random();
    DecimalFormat df1 = new DecimalFormat("0.0");
    DecimalFormat df2 = new DecimalFormat("0.00");
    DecimalFormat df3 = new DecimalFormat("0.000");
    DecimalFormat df4 = new DecimalFormat("0.0000");
    DecimalFormat df5 = new DecimalFormat("0.00000");
    DecimalFormat df6 = new DecimalFormat("0.000000");
    DecimalFormat sf1 = new DecimalFormat("0.0E00");
    DecimalFormat sf2 = new DecimalFormat("0.00E00");
    DecimalFormat sf3 = new DecimalFormat("0.000E00");
    DecimalFormat sf4 = new DecimalFormat("0.0000E00");
    DecimalFormat sf5 = new DecimalFormat("0.00000E00");
    DecimalFormat sf6 = new DecimalFormat("0.000000E00");
    DecimalFormat sf7 = new DecimalFormat("0.0000000E00");
    DecimalFormat sf8 = new DecimalFormat("0.00000000E00");

    ImageColorizer colorizer = new ImageColorizer();
    String[] textSubs = {"%1", "%2", "%3", "%4", "%5", "%6", "%7", "%8"};

    default GameSession session() {
        return GameSession.instance();
    }

    default Galaxy galaxy() {
        return session().galaxy();
    }

    default IGameOptions options() {
        return session().options();
    }

    default Empire player() {
        return galaxy().player();
    }

    default boolean isPlayer(Empire e) {
        return galaxy().isPlayer(e);
    }

    default LabelManager labels() {
        return LabelManager.current();
    }

    default CursorManager cursors() {
        return CursorManager.current();
    }

    default Object sessionVar(String key) {
        return session().var(key);
    }

    default void removeSessionVar(String key) {
        session().removeVar(key);
    }

    default void sessionVar(String key, Object value) {
        session().var(key, value);
    }

    default int scaled(int i) {
        return RotPUI.scaledSize(i);
    }

    default int unscaled(int i) {
        return RotPUI.unscaledSize(i);
    }

    default void mapClick() {
        playAudioClip("MapClick");
    }

    default void buttonClick() {
        playAudioClip("ButtonClick");
    }

    default void menuClick() {
        playAudioClip("MenuClick");
    }

    default void softClick() {
        playAudioClip("SoftClick");
    }

    default void misClick() {
        playAudioClip("MisClick");
    }

    default long timeMs() {
        return System.currentTimeMillis() - Rotp.startMs;
    }

    default boolean playAnimations() {
        return AnimationManager.current().playAnimations();
    }

    default void stopAmbience() {
        SoundManager.current().stopAmbience();
    }

    default void playAmbience(String key) {
        SoundManager.current().playAmbience(key);
    }

    default SoundClip playAudioClip(String key) {
        return SoundManager.current().playAudioClip(key);
    }

    default SoundClip alwaysPlayAudioClip(String key) {
        return SoundManager.current().alwaysPlay(key);
    }

    default int id(Empire e) {
        return e == null ? Empire.NULL_ID : e.id;
    }

    default int id(StarSystem s) {
        return s == null ? StarSystem.NULL_ID : s.id;
    }

    default String text(String key) {
        if ((galaxy() == null) || (player() == null))
            return labels().label(key);
        else
            return player().race().text(key);
    }

    default String text(String key, String... vals) {
        String str = text(key);
        for (int i = 0; i < vals.length; i++)
            str = str.replace(textSubs[i], vals[i]);
        return str;
    }

    default String text(String key, int... vals) {
        String str = text(key);
        for (int i = 0; i < vals.length; i++)
            str = str.replace(textSubs[i], String.valueOf(vals[i]));
        return str;
    }

    default String text(String key, String val1, int val2) {
        String str = text(key);
        str = str.replace("%1", val1);
        return str.replace("%2", String.valueOf(val2));
    }

    default String text(String key, String val1, String val2, int val3) {
        String str = text(key);
        str = str.replace("%1", val1);
        str = str.replace("%2", val2);
        return str.replace("%3", String.valueOf(val3));
    }

    default Font dlgFont(int size) {
        return FontManager.current().dlgFont(size);
    }

    default Font narrowFont(int size) {
        return FontManager.current().narrowFont(size);
    }

    default Font plainFont(int size) {
        return FontManager.current().plainFont(size);
    }

    default Font font(int size) {
        return FontManager.current().font(size);
    }

    default Font logoFont(int size) {
        return FontManager.current().logoFont(size);
    }

    default void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    default void exception(Exception e) {
        e.printStackTrace();
        if (RotPUI.useDebugFile) {
            PrintWriter debugFile = RotPUI.debugFile();
            if (debugFile != null) {
                e.printStackTrace(debugFile);
                debugFile.flush();
            }
        }
        RotPUI.instance().selectErrorPanel(e);
    }

    default void err(String... text) {
        String output = concat(text);
        System.err.println(output);
        if (RotPUI.useDebugFile) {
            PrintWriter debugFile = RotPUI.debugFile();
            if (debugFile != null) {
                debugFile.println(output);
                debugFile.flush();
            }
        }
    }

    default void log(String... text) {
        if (!Rotp.logging)
            return;
        String output = concat(text);
        System.out.println(output);
        if (RotPUI.useDebugFile) {
            PrintWriter debugFile = RotPUI.debugFile();
            if (debugFile != null) {
                debugFile.println(output);
                debugFile.flush();
            }
        }
    }

    default int maximumSystems() {
        return (int) (240 * (Rotp.maxHeapMemory - 250));
    }

    default boolean veryLowMemory() {
        return (Rotp.maxHeapMemory < 500)
                || (galaxy() != null) && (galaxy().numStarSystems() > (maximumSystems() * 3 / 4));
    }

    default boolean lowMemory() {
        return (Rotp.maxHeapMemory < 800)
                || ((galaxy() != null) && (galaxy().numStarSystems() > (maximumSystems() / 2)));
    }

    default boolean midMemory() {
        return (galaxy() != null) && (galaxy().numStarSystems() > (maximumSystems() / 3));
    }

    default Image image(String s) {
        return ImageManager.current().image(s);
    }

    default Image scaledImageW(String s, int w) {
        return ImageManager.current().scaledImageW(s, w);
    }

    default int animationCount() {
        return RotPUI.instance().animationCount();
    }

    default long animationMs() {
        return RotPUI.instance().animationMs();
    }

    default void allFrames(String key, int cnt, int imgIndex, List<Image> frames, List<Integer> refs) {
        AnimationManager.current().allFrames(key, cnt, imgIndex, frames, refs);
    }

    default BufferedImage currentFrame(String key) {
        return AnimationManager.current().currentFrame(key);
    }

    default BufferedImage currentFrame(String key, List<String> exclusions) {
        return AnimationManager.current().currentFrame(key, exclusions);
    }

    default void setFontHints(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
    }

    default void resetAnimation(String key) {
        AnimationManager.current().reset(key);
    }

    default List<BufferedImage> allExplosionFrames(String key) {
        return AnimationManager.current().allExplosionFrames(key);
    }

    default float distance(float x0, float y0, float x1, float y1) {
        return (float) Math.sqrt(((x1 - x0) * (x1 - x0)) + ((y1 - y0) * (y1 - y0)));
    }

    default float random() {
        return random.nextFloat();
    }

    default float random(float d) {
        return d * random();
    }

    default float random(float low, float hi) {
        return low + ((hi - low) * random());
    }

    default <T> T random(T[] array) {
        return array == null ? null : array[(random.nextInt(array.length))];
    }

    default <T> T random(List<T> list) {
        return (list == null || list.isEmpty()) ? null : list.get(random.nextInt(list.size()));
    }

    default <T> T random(Set<T> list) {
        return random(new ArrayList<>(list));
    }

    default float asin(float d) {
        return (float) FastMath.asin(d);
    }

    default int bounds(int low, int val, int hi) {
        return Math.min(Math.max(low, val), hi);
    }

    default float bounds(float low, float val, float hi) {
        return Math.min(Math.max(low, val), hi);
    }

    default int roll(int low, int hi) {
        if (low == hi)
            return low;
        return low + (int) ((hi - low + 1) * random());
    }

    default int summate(float n) {
        int n1 = (int) n;
        if (n1 < 1)
            return 0;
        else
            return n1 * (n1 + 1) / 2;
    }

    default List<String> varTokens(String s, String key) {
        String startKey = concat("[", key, "_");
        int keySize = startKey.length();
        List<String> tokens = new ArrayList<>();
        int prevIndex = -1;
        int nextIndex = s.indexOf(startKey, prevIndex);
        while (nextIndex >= 0) {
            int endIndex = s.indexOf(']', nextIndex);
            if (endIndex <= nextIndex)
                return tokens;
            String var = s.substring(nextIndex + keySize - 1, endIndex);
            tokens.add(var);
            prevIndex = nextIndex;
            nextIndex = s.indexOf(startKey, endIndex);
        }
        return tokens;
    }

    default String concat(String s1) {
        return s1;
    }

    default String concat(String s1, String s2) {
        return str(s1).concat(str(s2));
    }

    default String concat(String... s) {
        if (s.length == 1)
            return s[0];
        if (s.length == 2)
            return s[0].concat(s[1]);

        StringBuilder sb = new StringBuilder(s.length * 16);
        for (int i = 0; i < s.length; i++)
            sb.append(s[i]);
        return sb.toString();
    }

    default float sqrt(int i) {
        return (float) Math.sqrt(i);
    }

    default float sqrt(float f) {
        return (float) Math.sqrt(f);
    }

    default int abs(int v1) {
        return Math.abs(v1);
    }

    default float abs(float v1) {
        return Math.abs(v1);
    }

    default int max(int v1, int v2) {
        return Math.max(v1, v2);
    }

    default int max(int v1, int v2, int v3) {
        return max(v1, max(v2, v3));
    }

    default int max(int... n) {
        int max = n[0];
        for (int i = 1; i < n.length; i++) {
            if (n[i] > max)
                max = n[i];
        }
        return max;
    }

    default float max(float v1, float v2) {
        return Math.max(v1, v2);
    }

    default float max(float v1, float v2, float v3) {
        return max(v1, max(v2, v3));
    }

    default float max(float... n) {
        float max = n[0];
        for (int i = 1; i < n.length; i++) {
            if (n[i] > max)
                max = n[i];
        }
        return max;
    }

    default int min(int v1, int v2) {
        return Math.min(v1, v2);
    }

    default int min(int v1, int v2, int v3) {
        return min(v1, min(v2, v3));
    }

    default int min(int... n) {
        int min = n[0];
        for (int i = 1; i < n.length; i++) {
            if (n[i] < min)
                min = n[i];
        }
        return min;
    }

    default float min(float v1, float v2) {
        return Math.min(v1, v2);
    }

    default float min(float v1, float v2, float v3) {
        return min(v1, min(v2, v3));
    }

    default float min(float... n) {
        float min = n[0];
        for (int i = 1; i < n.length; i++) {
            if (n[i] < min)
                min = n[i];
        }
        return min;
    }

    default float avg(float v1, float v2) {
        return (v1 + v2) / 2;
    }

    default float avg(float v1, float v2, float v3) {
        return (v1 + v2 + v3) / 3;
    }

    default float avg(float... n) {
        float sum = n[0];
        for (int i = 1; i < n.length; i++)
            sum = n[i];
        return sum / n.length;
    }

    default String scifmt(float d, int n) {
        String res = null;
        switch (n) {
            case 1:
                res = sf1.format(d);
                break;
            case 2:
                res = sf2.format(d);
                break;
            case 3:
                res = sf3.format(d);
                break;
            case 4:
                res = sf4.format(d);
                break;
            case 5:
                res = sf5.format(d);
                break;
            case 6:
                res = sf6.format(d);
                break;
            case 7:
                res = sf7.format(d);
                break;
            case 8:
                res = sf8.format(d);
                break;
            default:
                res = sf3.format(d);
                break;
        }
        return res.replace('E', 'e');
    }

    default String fmt(float d, int n) {
        if (n == 0)
            return str((int) d);
        switch (n) {
            case 1:
                return df1.format(d);
            case 2:
                return df2.format(d);
            case 3:
                return df3.format(d);
            case 4:
                return df4.format(d);
            case 5:
                return df5.format(d);
            case 6:
                return df6.format(d);
            default:
                return df3.format(d);
        }
    }

    default String fmt(float d) {
        if (Math.abs(d) < .0005)
            return "0";
        else if (d < .1)
            return df3.format(d);
        else if (d < 1)
            return df2.format(d);
        else if (d < 10)
            return df1.format(d);
        else
            return str((int) d);
    }

    default float round(float val, float precision) {
        return ((int) ((val + (precision / 2.0)) / precision)) * precision;
    }

    default int round(float val, int precision) {
        return ((int) ((val + (precision / 2)) / precision)) * precision;
    }

    default int getAlpha(int pixel) {
        return (pixel >> 24) & 0xFF;
    }

    default int getRed(int pixel) {
        return (pixel >> 16) & 0xFF;
    }

    default int getGreen(int pixel) {
        return (pixel >> 8) & 0xFF;
    }

    default int getBlue(int pixel) {
        return (pixel >> 0) & 0xFF;
    }

    default Tech tech(String id) {
        return TechLibrary.current().tech(id);
    }

    default String date(float n) {
        int year = (int) n;
        float frac = n - (int) n;
        int month = (int) (frac * 12);
        int day = (int) ((frac - (month / 12)) * 30);

        return concat(str(year), ".", monthName[month], ".", str(++day));
    }

    default String displayYearOrTurn() {
        if (UserPreferences.displayYear())
            return text("MAIN_YEAR_DISPLAY", galaxy().currentYear());
        else
            return text("MAIN_TURN_DISPLAY", galaxy().currentTurn());
    }

    default boolean equal(float d1, float d2, float precision) {
        return Math.abs(d1 - d2) < precision;
    }

    default List<String> substrings(String input, char delim) {
        return substrings(input, delim, 0);
    }

    default List<String> substrings(String input, char delim, int min) {
        // min - minimum number of fields to be returned... missing fields returned as empty
        List<String> res = new ArrayList<>();
        int from = 0;
        int mark = 0;
        String subString;

        while ((mark >= 0) || (res.size() < min)) {
            mark = input.indexOf(delim, from);
            if ((mark < 0) || (res.size() == (min - 1)))
                subString = input.substring(from).trim();
            else
                subString = input.substring(from, mark).trim();
            res.add(subString);
            from = mark + 1;
        }
        return res;
    }

    default float pow(float d, int e) {
        // Math.pow(d1,d2) is slow for integer exponents
        // there are faster algorithms for large values of e,
        // but this game predominantly uses e <10, so this is fine
        float res = 1;
        if (e > 0) {
            for (int i = 1; i <= e; i++)
                res *= d;
        } else if (e < 0) {
            for (int i = -1; i >= e; i--)
                res /= d;
        }
        return res;
    }

    default float parseFloat(String s0) throws NumberFormatException {
        String s = s0.trim();
        if (s.isEmpty())
            return 0.0f;
        // checks for scientific notation
        if (s.contains("e")) {
            List<String> strings = substrings(s, 'e', 2);
            try {
                float num = Float.valueOf(strings.get(0));
                int exp = Integer.valueOf(strings.get(1));
                return num * pow(10, exp);
            } catch (NumberFormatException e) {
                err("Base.parseDouble (1) -- error parsing: " + s);
                throw e;
            }
        }

        try {
            return Float.valueOf(s);
        } catch (NumberFormatException e) {
            err("Base.parseDouble (2) -- error parsing: " + s);
            throw e;
        }
    }

    default List<String> parsedValues(String s, char delim) {
        // used for parsing delimited text file lines that may be commented
        // null means EOF... preserve that
        if (s == null)
            return null;
        // trim spaces
        String s1 = s.trim();

        if (s1.isEmpty())
            return new ArrayList<>();
        else {
            char char0 = s1.charAt(0);
            if ((char0 == '/') || (char0 == '\\') || (char0 == '#') || (char0 == '*'))
                // if a comment, then return empty string
                return new ArrayList<>();
            else
                // else return the trimmed string
                return substrings(s, delim);
        }
    }

    default int parseInt(String s0) throws NumberFormatException {
        String s = s0.trim();
        if (s.isEmpty())
            return 0;
        try {
            return Integer.valueOf(s.trim());
        } catch (NumberFormatException e) {
            err("Base.parseInteger -- error parsing: " + s);
            throw e;
        }
    }

    default Color parseColor(String s) throws NumberFormatException {
        if (s.trim().isEmpty())
            return new Color(0, 0, 0);
        int red = 0;
        int green = 0;
        int blue = 0;
        List<String> rgbs = substrings(s, ',');
        try {
            if (rgbs.size() > 0)
                red = parseInt(rgbs.get(0));
            if (rgbs.size() > 1)
                green = parseInt(rgbs.get(1));
            if (rgbs.size() > 2)
                blue = parseInt(rgbs.get(2));
        } catch (NumberFormatException e) {
            err("Base.parseColor -- error parsing: " + s);
            throw e;
        }
        return new Color(red, green, blue);
    }

    default boolean isComment(String line) {
        String s = line.trim();
        if (s.isEmpty())
            return true;

        char char0 = s.charAt(0);
        if ((char0 == '/') || (char0 == '\\') || (char0 == '#'))
            return true;

        // special char in UTF-8
        return char0 == 65279;
    }

    default URL url(String n) {
        return Rotp.class.getResource(n);
    }

    default ImageIcon icon(String n) {
        return icon(n, true);
    }

    default ImageIcon icon(String n, boolean logError) {
        if ((n == null) || n.isEmpty()) {
            //("Base.icon() -- resource is empty or null");
            return null;
        }
        URL resource = null;
        try {
            resource = url(n);
        } catch (Exception e) {
            err("Base.icon() -- error retrieving resource: ", n + " : ", e.getMessage());
            return null;
        }
        if (resource == null) {
            if (logError)
                err("Base.icon() -- Resource not found:", n);
            return null;
        } else
            return new ImageIcon(resource);
    }

    default File file(String n) {
        return new File(Rotp.jarPath(), n);
    }

    default InputStream fileInputStream(String n) {
        String fullString = "../rotp/" + n;

        try {
            return new FileInputStream(new File(Rotp.jarPath(), n));
        } catch (FileNotFoundException e) {
            try {
                return new FileInputStream(fullString);
            } catch (FileNotFoundException ex) {
                return Rotp.class.getResourceAsStream(n);
            }
        }
    }

    default BufferedReader reader(String n) {
        String fullString = "../rotp/" + n;
        FileInputStream fis = null;
        InputStreamReader in = null;
        InputStream zipStream = null;

        try {
            fis = new FileInputStream(new File(Rotp.jarPath(), n));
        } catch (FileNotFoundException e) {
            try {
                fis = new FileInputStream(fullString);
            } catch (FileNotFoundException ex) {
                zipStream = Rotp.class.getResourceAsStream(n);
            }
        }

        if (fis != null)
            in = new InputStreamReader(fis, StandardCharsets.UTF_8);
        else if (zipStream != null)
            in = new InputStreamReader(zipStream, StandardCharsets.UTF_8);
        else
            err("Base.reader() -- FileNotFoundException:", n);

        if (in == null)
            return null;

        return new BufferedReader(in);
    }

    default PrintWriter writer(String n) {
        String fullString = "src/rotp/" + n;
        try {
            FileOutputStream fout = new FileOutputStream(new File(fullString));
            return new PrintWriter(fout, true);
        } catch (FileNotFoundException e) {
            err("Base.writer -- " + e);
            e.printStackTrace();
            return null;
        }
    }

    default InputStream inputStream(String n) {
        InputStream stream = null;
        File fontFile = new File(n);
        if (fontFile.exists())
            try {
                stream = new FileInputStream(fontFile);
            } catch (FileNotFoundException e) {
                err("Base.fileStream -- FileNotFoundException: " + n);
            }
        else {
            JarFile jarFile = null;
            try {
                jarFile = new JarFile(Rotp.jarFileName);
                ZipEntry ze = jarFile.getEntry(n);
                if (ze != null)
                    stream = jarFile.getInputStream(ze);
            } catch (IOException e) {
                err("Base.fileStream -- IOException: " + n);
            } finally {
                try {
                    if (jarFile != null)
                        jarFile.close();
                } catch (IOException e) {
                }
            }
        }
        return stream;
    }

    default OutputStream outputStream(String s) throws IOException {
        try {
            OutputStream file = new FileOutputStream(s);
            OutputStream buffer = new BufferedOutputStream(file);
            return buffer;
        } catch (IOException e) {
            log("Cannot create output file: ", s);
            log(e.getMessage());
            throw (e);
        }
    }

    static int compare(int a, int b) {
        return a - b;
    }

    static int compare(float a, float b) {
        return Float.compare(a, b);
    }

    default Color newColor(int r, int g, int b) {
        return newColor(r, g, b, 255);
    }

    default Color newColor(int r, int g, int b, int a) {
        //log("Creating color r:"+r+" g:"+g+" b:"+b+" a:"+a);
        return new Color(r, g, b, a);
    }

    default String str(String s) {
        return s == null ? "null" : s;
    }

    default String str(int i) {
        return Integer.toString(i);
    }

    default String str(float i) {
        return Float.toString(i);
    }

    default BufferedImage flip(BufferedImage img) {
        if (img == null)
            return null;
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage flippedImg = newBufferedImage(w, h);
        Graphics g = flippedImg.getGraphics();
        g.drawImage(img, w, 0, 0, h, 0, 0, w, h, null);
        g.dispose();
        return flippedImg;
    }

    default BufferedImage newOpaqueImage(int w, int h) {
        //log("Creating image w:"+w+"  h:"+h);
        return BasePanel.gc().createCompatibleImage(w, h);
    }

    default BufferedImage newBufferedImage(int w, int h) {
        return BasePanel.gc().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
    }

    default BufferedImage asBufferedImage(Image image) {
        if (image == null)
            return null;
        if (image instanceof BufferedImage)
            return (BufferedImage) image;
        return newBufferedImage(image);
    }

    default BufferedImage newOpaqueImage(Image image) {
        BufferedImage bufferedImage = newOpaqueImage(image.getWidth(null), image.getHeight(null));
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(image, null, null);
        return bufferedImage;
    }

    default BufferedImage newBufferedImage(Image image) {
        BufferedImage bufferedImage = newBufferedImage(image.getWidth(null), image.getHeight(null));
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(image, null, null);
        return bufferedImage;
    }

    default Border newLineBorder(Color c, int th) {
        return BorderFactory.createLineBorder(c, scaled(th));
    }

    default Border newEmptyBorder(int top, int left, int bottom, int right) {
        return BorderFactory.createEmptyBorder(scaled(top), scaled(left), scaled(bottom), scaled(right));
    }

    default void drawBorderedString(Graphics g, String str, int x, int y, Color back, Color fore) {
        drawBorderedString(g, str, 1, x, y, back, fore);
    }

    default void drawBorderedString(Graphics g, String str, int th, int x, int y, Color back, Color fore) {
        if (str == null)
            return;
        g.setColor(back);
        int thick = th;
        int start = 0 - thick;
        for (int x0 = start; x0 <= thick; x0++) {
            int x0s = scaled(x0);
            for (int y0 = start; y0 <= thick; y0++) {
                int y0s = scaled(y0);
                g.drawString(str, x + x0s, y + y0s);
            }
        }
        g.setColor(fore);
        g.drawString(str, x, y);
    }

    default void drawShadowedString(Graphics g, String str, int x, int y, Color back, Color fore) {
        drawShadowedString(g, str, 1, x, y, back, fore);
    }

    default void drawShadowedString(Graphics g, String str, int thick, int x, int y, Color back, Color fore) {
        drawShadowedString(g, str, 0, thick, x, y, back, fore);
    }

    default void drawAlphaShadowedString(Graphics2D g, float alpha, String str, int thick, int x, int y, Color back, Color fore) {
        drawAlphaShadowedString(g, alpha, str, 0, thick, x, y, back, fore);
    }

    default void drawShadowedString(Graphics g, String str, int th0, int th1, int x, int y, Color back, Color fore) {
        g.setColor(back);

        int topThick = scaled(th0);
        int thick = th1;
        for (int x0 = (0 - topThick); x0 <= thick; x0++) {
            int x0s = scaled(x0);
            for (int y0 = (0 - topThick); y0 <= thick; y0++) {
                int y0s = scaled(y0);
                g.drawString(str, x + x0s, y + y0s);
            }
        }
        g.setColor(fore);
        g.drawString(str, x, y);
    }

    default void drawShadowedString(Graphics g, String str, int scale, int th0, int th1, int x, int y, Color back, Color fore) {
        g.setColor(back);

        int topThick = scaled(th0);
        int thick = scaled(th1);
        int incr = scaled(scale);
        for (int x0 = (0 - topThick); x0 <= thick; x0 += incr) {
            for (int y0 = (0 - topThick); y0 <= thick; y0 += incr)
                g.drawString(str, x + x0, y + y0);
        }
        g.setColor(fore);
        g.drawString(str, x, y);
    }

    default void drawAlphaShadowedString(Graphics2D g, float alpha, String str, int th0, int th1, int x, int y, Color back, Color fore) {
        if (alpha <= 0)
            return;

        int mult = 2 * (th0 + th1 + 1) * (th0 + th1 + 1);

        Composite c = g.getComposite();

        if (alpha < 1)
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / mult));

        g.setColor(back);

        int topThick = scaled(th0);
        int thick = scaled(th1);
        for (int x0 = (0 - topThick); x0 <= thick; x0++) {
            for (int y0 = (0 - topThick); y0 <= thick; y0++)
                g.drawString(str, x + x0, y + y0);
        }
        if (alpha < 1)
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 2));

        g.setColor(fore);
        g.drawString(str, x, y);
        g.setComposite(c);
    }

    default void drawBoldString(Graphics g, String str, int x, int y) {
        for (int x0 = 0; x0 <= 1; x0++) {
            for (int y0 = 0; y0 <= 1; y0++)
                g.drawString(str, x + x0, y + y0);
        }
    }

    default void drawBoldString(Graphics g, String str, int x, int y, Color fore) {
        g.setColor(fore);

        for (int x0 = 0; x0 <= 1; x0++) {
            for (int y0 = 0; y0 <= 1; y0++)
                g.drawString(str, x + x0, y + y0);
        }
    }

    default boolean isLightColor(Color c) {
        return (c.getRed() + c.getGreen() + c.getBlue()) > 320;
    }

    default void drawShadedPolygon(Graphics g, int[] x, int[] y, Color c0, int offsetX, int offsetY) {
        int x0[] = new int[x.length];
        int y0[] = new int[y.length];

        Color cLight = c0.brighter();
        Color cShade = c0.darker();

        // draw Shade
        for (int i = 0; i < x.length; i++) {
            x0[i] = x[i] - offsetX;
            y0[i] = y[i] - offsetY;
        }
        g.setColor(cShade);
        g.fillPolygon(x0, y0, x0.length);

        // draw Light
        for (int i = 0; i < x.length; i++) {
            x0[i] = x[i] + offsetX;
            y0[i] = y[i] + offsetY;
        }
        g.setColor(cLight);
        g.fillPolygon(x0, y0, x0.length);

        // draw normal
        g.setColor(c0);
        g.fillPolygon(x, y, x.length);
    }

    default List<String> wrappedLines(Graphics g, String text, int maxWidth) {
        return wrappedLines(g, text, maxWidth, 0);
    }

    default List<String> wrappedLines(Graphics g, String text, int maxWidth, int line1Indent) {
        List<String> words = substrings(text, ' ');
        List<String> lines = new ArrayList<>();

        FontMetrics fm = g.getFontMetrics();
        int indent = line1Indent;

        String currentLine = "";
        for (String word : words) {
            String newLine = currentLine;
            if (newLine.isEmpty())
                newLine = newLine + word;
            else
                newLine = newLine + " " + word;
            int newWidth = fm.stringWidth(newLine);
            if (newWidth > (maxWidth - indent)) {
                lines.add(currentLine);
                indent = 0;
                currentLine = word;
            } else
                currentLine = newLine;
        }

        if (!currentLine.isEmpty())
            lines.add(currentLine);

        return lines;
    }

    default List<String> scaledPlainWrappedLines(Graphics g, String text, int maxWidth, int maxLines, int desiredFont, int minFont) {
        int fontSize = desiredFont;
        g.setFont(plainFont(fontSize));
        List<String> wrappedLines = wrappedLines(g, text, maxWidth);
        while ((wrappedLines.size() > maxLines) && (fontSize > minFont)) {
            fontSize--;
            g.setFont(plainFont(fontSize));
            wrappedLines = wrappedLines(g, text, maxWidth);
        }
        return wrappedLines;
    }

    default List<String> scaledNarrowWrappedLines(Graphics g, String text, int maxWidth, int maxLines, int desiredFont, int minFont) {
        int fontSize = desiredFont;
        g.setFont(narrowFont(fontSize));
        List<String> wrappedLines = wrappedLines(g, text, maxWidth);
        while ((wrappedLines.size() > maxLines) && (fontSize > minFont)) {
            fontSize--;
            g.setFont(narrowFont(fontSize));
            wrappedLines = wrappedLines(g, text, maxWidth);
        }
        return wrappedLines;
    }

    default List<String> scaledDialogueWrappedLines(Graphics g, String text, int maxWidth, int maxLines, int desiredFont, int minFont) {
        int fontSize = desiredFont;
        g.setFont(dlgFont(fontSize));
        List<String> wrappedLines = wrappedLines(g, text, maxWidth);
        while ((wrappedLines.size() > maxLines) && (fontSize > minFont)) {
            fontSize--;
            g.setFont(dlgFont(fontSize));
            wrappedLines = wrappedLines(g, text, maxWidth);
        }
        return wrappedLines;
    }

    default List<String> scaledWrappedLines(Graphics g, String text, int maxWidth, int maxLines, int desiredFont, int minFont) {
        int fontSize = desiredFont;
        g.setFont(font(fontSize));
        List<String> wrappedLines = wrappedLines(g, text, maxWidth);
        while ((wrappedLines.size() > maxLines) && (fontSize > minFont)) {
            fontSize--;
            g.setFont(font(fontSize));
            wrappedLines = wrappedLines(g, text, maxWidth);
        }
        return wrappedLines;
    }

    default int scaledFont(Graphics g, String text, int maxWidth, int desiredFont, int minFont) {
        int fontSize = desiredFont;
        g.setFont(narrowFont(fontSize));
        while ((g.getFontMetrics().stringWidth(text) > maxWidth) && (fontSize > minFont)) {
            fontSize--;
            g.setFont(narrowFont(fontSize));
        }
        return fontSize;
    }

    default int scaledLogoFont(Graphics g, String text, int maxWidth, int desiredFont, int minFont) {
        int fontSize = desiredFont;
        g.setFont(logoFont(fontSize));
        while ((g.getFontMetrics().stringWidth(text) > maxWidth) && (fontSize > minFont)) {
            fontSize--;
            g.setFont(logoFont(fontSize));
        }
        return fontSize;
    }

    default void invokeAndWait(Runnable runnable) {
        if (EventQueue.isDispatchThread())
            runnable.run();
        else {
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    default void invokeLater(Runnable runnable) {
        try {
            SwingUtilities.invokeLater(runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default void drawBackgroundStars(BufferedImage img, ImageObserver obs) {
        drawBackgroundStars(img, img.getGraphics(), img.getWidth(obs), img.getHeight(obs), scaled(50), scaled(100));
    }

    default void drawBackgroundStars(BufferedImage img, ImageObserver obs, int minDist, int varDist) {
        drawBackgroundStars(img, img.getGraphics(), img.getWidth(obs), img.getHeight(obs), minDist, varDist);
    }

    default void drawBackgroundStars(Graphics g, int w, int h) {
        drawBackgroundStars(g, w, h, scaled(50), scaled(100));
    }

    default void drawBackgroundStars(Graphics g, int w, int h, int minDist, int varDist) {
        // draw background stars
        g.setColor(newColor(0, 0, 0, 0));
        g.fillRect(0, 0, w, h);
        int count = w * h;
        int p = 0;
        Color dimmest = newColor(32, 32, 32);
        Color dimmer = newColor(48, 48, 48);
        Color dim = newColor(64, 64, 64);
        Color avg = newColor(96, 96, 96);
        Color bright = newColor(144, 144, 144);
        Color brighter = newColor(196, 196, 196);
        Color brightest = newColor(255, 255, 255);

        int s1 = BasePanel.s1;
        int s2 = BasePanel.s2;

        while (p < count) {
            p += (minDist + (int) Math.ceil(random() * varDist));
            int x1 = p % w;
            int y1 = p / h;
            x1 = (int) Math.ceil(random() * w);
            y1 = (int) Math.ceil(random() * h);
            int roll = (int) Math.ceil(random() * 100);
            if (roll <= 50)
                g.setColor(dimmest);
            else if (roll <= 75)
                g.setColor(dimmer);
            else if (roll <= 87)
                g.setColor(dim);
            else if (roll <= 93)
                g.setColor(avg);
            else if (roll <= 97)
                g.setColor(bright);
            else if (roll <= 99)
                g.setColor(brighter);
            else
                g.setColor(brightest);
            if ((roll > 90) && (random() < .2))
                g.fillRoundRect(x1, y1, s2, s2, s2, s2);
            else
                g.fillRect(x1, y1, s1, s1);
        }
    }

    default void drawBackgroundStars(BufferedImage image, Graphics g, int w, int h, int minDist, int varDist) {
        // draw background stars
        g.setColor(newColor(0, 0, 0, 0));
        g.fillRect(0, 0, w, h);
        int count = w * h;
        int p = 0;

        int s1 = BasePanel.s1;
        int s2 = BasePanel.s2;

        while (p < count) {
            p += (minDist + (int) Math.ceil(random() * varDist));
            int x1 = p % w;
            int y1 = p / h;
            x1 = (int) Math.floor(random() * w);
            y1 = (int) Math.floor(random() * h);
            int clr = image.getRGB(x1, y1);
            int red = (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue = clr & 0x000000ff;
            int minPixelValue = 0;
            int roll = (int) Math.ceil(random() * 100);
            if (roll <= 50)
                minPixelValue = 32;
            else if (roll <= 75)
                minPixelValue = 48;
            else if (roll <= 87)
                minPixelValue = 64;
            else if (roll <= 93)
                minPixelValue = 96;
            else if (roll <= 97)
                minPixelValue = 128;
            else if (roll <= 99)
                minPixelValue = 144;
            else
                minPixelValue = 196;
            g.setColor(new Color(max(red, minPixelValue), max(green, minPixelValue), max(blue, minPixelValue)));
            if ((roll > 90) && (random() < .2))
                g.fillRoundRect(x1, y1, s2, s2, s2, s2);
            else
                g.fillRect(x1, y1, s1, s1);
        }
    }

    default BufferedImage makeTransparent(Image img, Color c) {
        colorizer.image(img);
        colorizer.onlySpecificColor(c);
        return colorizer.makeTransparent();
    }

    default String stringAt(List<String> names, int i) {
        if ((i >= names.size()) || names.get(i).isEmpty())
            return names.get(0);
        else
            return names.get(i);
    }

    default List<String> readSystemNames(String filePath) {
        BufferedReader reader = reader(filePath);
        if (reader == null)
            return null;

        List<String> names = new ArrayList<>();
        try {
            List<String> lineValues;
            while ((lineValues = (parsedValues(reader.readLine(), ','))) != null) {
                if (!lineValues.isEmpty())
                    names.add(lineValues.get(0));
            }
        } catch (IOException e) {
            err("Base.readFileLines: ", filePath, " -- IOException: ", e.toString());
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
        return names;
    }
}
