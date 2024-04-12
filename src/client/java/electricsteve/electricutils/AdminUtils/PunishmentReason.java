package electricsteve.electricutils.AdminUtils;

public class PunishmentReason {
    private String name;
    private String description;
    private PunishmentTypes type;
    private String length;

    public PunishmentReason(String name, String description, PunishmentTypes type, String length) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public PunishmentTypes getType() {
        return type;
    }

    public String getLength() {
        return length;
    }
}
