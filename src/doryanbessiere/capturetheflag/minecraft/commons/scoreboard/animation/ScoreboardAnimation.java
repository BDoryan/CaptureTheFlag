package doryanbessiere.capturetheflag.minecraft.commons.scoreboard.animation;

public abstract class ScoreboardAnimation {

    protected String text;

    public ScoreboardAnimation(String text) {
        this.text = text;
    }

    public abstract void update();

    public String getText() {
        return text;
    }
}
