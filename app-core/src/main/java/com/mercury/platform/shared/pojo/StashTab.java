package com.mercury.platform.shared.pojo;

/**
 * Created by Константин on 17.03.2017.
 */
public class StashTab {
    private String title;
    private boolean isQuad;
    private boolean undefined;

    public StashTab(String title, boolean isQuad) {
        this.title = title;
        this.isQuad = isQuad;
        this.undefined = true;
    }

    public StashTab() {
    }

    public boolean isUndefined() {
        return undefined;
    }

    public void setUndefined(boolean undefined) {
        this.undefined = undefined;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isQuad() {
        return isQuad;
    }

    public void setQuad(boolean quad) {
        isQuad = quad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StashTab)) return false;
        StashTab stashTab = (StashTab) o;
        return isQuad() == stashTab.isQuad() && (getTitle() != null ? getTitle().equals(stashTab.getTitle()) : stashTab.getTitle() == null);
    }

    @Override
    public int hashCode() {
        int result = getTitle() != null ? getTitle().hashCode() : 0;
        result = 31 * result + (isQuad() ? 1 : 0);
        return result;
    }
}
