package doryanbessiere.capturetheflag.minecraft.commons.logger;

public enum LoggerType {

    INFO("§7[§9INFO§7]§9"),
    FATALERROR("§7[§4FATALERROR§7]§4"),
    ERROR("§7[§cERROR§7]§c"),
    DEBUG("§7[§bDEBUG§7]§b"),
    WARNING("§7[§6WARNING§7]§6");

    private String prefix;

    LoggerType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix+" ";
    }
}
