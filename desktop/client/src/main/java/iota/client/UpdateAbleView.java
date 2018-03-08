package iota.client;


/**
 * AN interface used by the presenter to update views regardless of UI framework once
 * it detects a refresh is needed. Individual UI kit specific classes should extend
 * the base UI class (or a subclass) and implement this method for updating without
 * destruction and reconstruction.
 */
public interface UpdateAbleView {
     void updateView();
}
