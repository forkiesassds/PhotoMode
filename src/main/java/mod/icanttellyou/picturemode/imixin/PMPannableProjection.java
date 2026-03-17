//? if >=26.1 {
/*package mod.icanttellyou.picturemode.imixin;

/^*
 * A mixin helper for making pannable projection matrices
 ^/
public interface PMPannableProjection {
    /^*
     * Sets the horizontal pan for the projection.
     *
     * @param panX The horizontal pan to set.
     ^/
    void pictureMode$setPanX(float panX);

    /^*
     * Sets the vertical pan for the projection
     *
     * @param panY The vertical pan to set.
     ^/
    void pictureMode$setPanY(float panY);

    /^*
     * Sets both the horizontal and vertical pan for the projection.
     *
     * @param panX The horizontal pan to set.
     * @param panY The vertical pan to set.
     ^/
    default void pictureMode$setPan(float panX, float panY) {
        this.pictureMode$setPanX(panX);
        this.pictureMode$setPanY(panY);
    }
}
*///? }