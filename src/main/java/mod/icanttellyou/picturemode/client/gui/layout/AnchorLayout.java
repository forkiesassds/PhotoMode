package mod.icanttellyou.picturemode.client.gui.layout;

import net.minecraft.client.gui.layouts.AbstractLayout;
import net.minecraft.client.gui.layouts.LayoutElement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AnchorLayout extends AbstractLayout {
    private final List<Child> children = new ArrayList<>();

    public AnchorLayout(int x, int y) {
        super(x, y, 0, 0);
    }

    public <T extends LayoutElement> T addChild(Position pos, T child) {
        this.children.add(new Child(child, pos));
        return child;
    }

    @Override
    public void arrangeElements() {
        super.arrangeElements();

        for (Child child : this.children) {
            LayoutElement element = child.element;

            int x = this.getX() + Math.round(child.pos.xPos * (this.width - element.getWidth()));
            int y = this.getY() + Math.round(child.pos.yPos * (this.height - element.getHeight()));

            element.setX(x);
            element.setY(y);
        }
    }

    //? if >=26.2 {
    /*@Override
    public void removeChildren() {
        this.children.clear();
    }
    *///? }

    @Override
    public void visitChildren(Consumer<LayoutElement> visitor) {
        this.children.forEach(child -> visitor.accept(child.element));
    }

    public void updateDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    private record Child(LayoutElement element, Position pos) {}

    public enum Position {
        TOP_LEFT(0.0F, 0.0F),
        TOP_CENTER(0.0F, 0.5F),
        TOP_RIGHT(0.0F, 1.0F),
        MIDDLE_LEFT(0.5F, 0.0F),
        MIDDLE_CENTER(0.5F, 0.5F),
        MIDDLE_RIGHT(0.5F, 1.0F),
        BOTTOM_LEFT(1.0F, 0.0F),
        BOTTOM_CENTER(1.0F, 0.5F),
        BOTTOM_RIGHT(1.0F, 1.0F);

        private final float xPos, yPos;

        Position(float yPos, float xPos) {
            this.xPos = xPos;
            this.yPos = yPos;
        }

        public float getXPos() {
            return this.xPos;
        }

        public float getYPos() {
            return this.yPos;
        }
    }
}
