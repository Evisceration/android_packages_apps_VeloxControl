package net.openfiretechnologies.veloxcontrol.adapters;

/**
 * Main Drawer Item, used for the MainDrawerAdapter
 */
public class MainDrawerItem {

    public int title;
    public int iconRes;
    public boolean isHeader;

    public MainDrawerItem(int title, int iconRes, boolean header) {
        this.title = title;
        this.iconRes = iconRes;
        this.isHeader = header;
    }

    public MainDrawerItem(int title, int iconRes) {
        this(title, iconRes, false);
    }

}
