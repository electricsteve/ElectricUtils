package electricsteve.electricutils.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import electricsteve.electricutils.AdminUtils.PunishmentReason;
import electricsteve.electricutils.AdminUtils.PunishmentTypes;
import electricsteve.electricutils.ElectricUtilsClient;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PunishmentReasonsSystem {
    static PunishmentReasonsSystem instance;
    private ArrayList<PunishmentReason> punishmentReasons;
    public Path PUNISHMENT_REASONS_FILE_PATH = FabricLoader.getInstance().getConfigDir().resolve(ElectricUtilsClient.MOD_ID).resolve("punishment_reasons.json");
    private final InputStream i = getClass().getClassLoader().getResourceAsStream("ElectricUtils/punishment_reasons.json");
    private static final List<String> DEFAULT_CONFIG_FILE = List.of();

    public PunishmentReasonsSystem() {
        instance = this;
        punishmentReasons = new ArrayList<>();
        updateReasonsFromFile();
    }

    /**
     * Gets the instance of the PunishmentReasonsSystem.
     *
     * @return The instance of the PunishmentReasonsSystem.
     */
    public static PunishmentReasonsSystem getInstance() {
        return instance;
    }

    /**
     * Adds a punishment reason.
     *
     * @param name        The name of the punishment reason.
     * @param description The description of the punishment reason.
     * @param type        The type of the punishment reason.
     * @param length      The length of the punishment reason.
     */
    public void addReason(String name, String description, PunishmentTypes type, String length) {
        punishmentReasons.add(new PunishmentReason(name, description, type, length));
    }

    /**
     * Gets the punishment reasons.
     *
     * @return ArrayList containing the punishment reasons.
     */
    public ArrayList<PunishmentReason> getPunishmentReasons() {
        return punishmentReasons;
    }

    /**
     * Gets a list of the names of the punishment reasons.
     *
     * @return ArrayList containing the names of the punishment reasons.
     */
    public ArrayList<String> getPunishmentReasonNames() {
        ArrayList<String> names = new ArrayList<>();
        for (PunishmentReason reason : punishmentReasons) {
            names.add(reason.getName());
        }
        return names;
    }

    /**
     * Gets a punishment reason by name.
     *
     * @param name The name of the punishment reason to get.
     * @return The punishment reason. Null if not found.
     */
    public PunishmentReason getPunishmentReason(String name) {
        for (PunishmentReason reason : punishmentReasons) {
            if (reason.getName().equals(name)) {
                return reason;
            }
        }
        return null;
    }

    /**
     * Removes a punishment reason.
     *
     * @param name The name of the punishment reason to remove.
     * @return Whether the removal was successful.
     */
    public boolean removeReason(String name) {
        PunishmentReason reason = getPunishmentReason(name);
        if (reason != null) {
            punishmentReasons.remove(reason);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Clears the punishment reasons.
     */
    public void clearReasons() {
        punishmentReasons.clear();
    }

    /**
     * Sets the punishment reasons.
     *
     * @param reasons ArrayList containing the new punishment reasons.
     */
    public void setReasons(ArrayList<PunishmentReason> reasons) {
        punishmentReasons = reasons;
    }

    /**
     * Sets the punishment reasons.
     *
     * @param reasons Array containing the new punishment reasons.
     */
    public void setReasons(PunishmentReason[] reasons) {
        punishmentReasons = new ArrayList<>(Arrays.asList(reasons));
    }

    public void replaceFileWithDefault() throws IOException{
        Files.copy(i, PUNISHMENT_REASONS_FILE_PATH, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Updates the punishment reasons from the file.
     *
     * @return Whether the update was successful.
     */
    public boolean updateReasonsFromFile() {
        try {
            ElectricUtilsClient.LOGGER.info("Punishment reasons file path: " + PUNISHMENT_REASONS_FILE_PATH);
            if (!Files.exists(PUNISHMENT_REASONS_FILE_PATH)) {
                try {
                    if (!Files.exists(PUNISHMENT_REASONS_FILE_PATH.getParent())) {
                        Files.createDirectory(PUNISHMENT_REASONS_FILE_PATH.getParent());
                    }
                    replaceFileWithDefault();
                } catch (IOException e) {
                    ElectricUtilsClient.LOGGER.error("Failed to create punishment reasons file due to an Input/Output error! Error: " + e);
                    return false;
                } catch (SecurityException e) {
                    ElectricUtilsClient.LOGGER.error("Failed to create punishment reasons file due to a security error! The mod probably couldn't access the file because it doesn't have permissions. Error: " + e);
                    return false;
                }
            }
            String json = Files.readString(PUNISHMENT_REASONS_FILE_PATH);
            JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

            return true;
        } catch (IllegalStateException ise) {
            ElectricUtilsClient.LOGGER.error("The punishment reasons file is not a valid JSON file!");
            ElectricUtilsClient.LOGGER.error("Reset the punishment reasons file with the default one by executing `/electricutils reset_punishment_reasons_file` in the chat." + System.lineSeparator() + "Full error: " + ise);
            return false;
        } catch (JsonSyntaxException jse) {
            ElectricUtilsClient.LOGGER.error("The punishment reasons file is not a valid JSON file!");
            ElectricUtilsClient.LOGGER.error("Reset the punishment reasons file with the default one by executing `/electricutils reset_punishment_reasons_file` in the chat." + System.lineSeparator() + "Full error: " + jse);
            return false;
        } catch (Throwable t) {
            ElectricUtilsClient.LOGGER.error("Failed to update punishment reasons! Error: " + t);
            return false;
        }
    }
}
