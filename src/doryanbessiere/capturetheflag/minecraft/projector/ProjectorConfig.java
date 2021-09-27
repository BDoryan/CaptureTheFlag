package doryanbessiere.capturetheflag.minecraft.projector;

import org.bukkit.Location;

public class ProjectorConfig {

    private float yaw;
    private float pitch;
    private double power;

    public ProjectorConfig(float yaw, float pitch, double power) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.power = power;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public double getPower() {
        return power;
    }
}
