package dataManager.support;

/**
 * Created by George on 14.06.2016.
 */
public class Pair<V, T> {
    private V mainValue;
    private T collateralValue;
    boolean marked = false;

    public Pair(V mainValue, T collateralValue) {
        this.mainValue = mainValue;
        this.collateralValue = collateralValue;
    }

    public V getMainValue() {
        return mainValue;
    }

    public T getCollateralValue() {
        return collateralValue;
    }

    public void setMainValue(V mainValue) {
        this.mainValue = mainValue;
    }

    public void setCollateralValue(T collateralValue) {
        this.collateralValue = collateralValue;
    }

    public void setMarked() {
        this.marked = true;
    }

    public boolean isMarked() {
        return marked;
    }
}
