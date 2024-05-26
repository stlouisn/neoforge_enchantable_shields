package me.spzla.beehivetooltip.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BeehiveTooltipConfig {
    public static final BeehiveTooltipConfig INSTANCE = new BeehiveTooltipConfig();

    public final Path configFile = FabricLoader.getInstance().getConfigDir().resolve("beehivetooltip.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public boolean enabled = true;
    public boolean unicodeMode = false;
    public HealthDisplayEnum healthDisplay = HealthDisplayEnum.COMPACT;
    public TooltipDisplayModeEnum displayMode = TooltipDisplayModeEnum.COMPACT;

    public void save() {
        try {
            Files.deleteIfExists(configFile);

            JsonObject json = new JsonObject();
            json.addProperty("enabled", enabled);
            json.addProperty("unicodeMode", unicodeMode);
            json.addProperty("healthDisplay", healthDisplay.toString());
            json.addProperty("displayMode", displayMode.toString());

            Files.writeString(configFile, gson.toJson(json));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            if (Files.notExists(configFile)) {
                save();
                return;
            }

            JsonObject json = gson.fromJson(Files.readString(configFile), JsonObject.class);

            if (json.has("enabled"))
                enabled = json.getAsJsonPrimitive("enabled").getAsBoolean();
            if (json.has("unicodeMode"))
                unicodeMode = json.getAsJsonPrimitive("unicodeMode").getAsBoolean();
            if (json.has("healthDisplay"))
                healthDisplay = HealthDisplayEnum.valueOf(json.getAsJsonPrimitive("healthDisplay").getAsString());
            if (json.has("displayMode"))
                displayMode = TooltipDisplayModeEnum.valueOf(json.getAsJsonPrimitive("displayMode").getAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Screen makeScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("beehivetooltip.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("beehivetooltip.title"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("beehivetooltip.option.enabled"))
                                .description(OptionDescription.of(Text.translatable("beehivetooltip.option.enabled.description")))
                                .binding(
                                        true,
                                        () -> enabled,
                                        value -> enabled = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("beehivetooltip.option.unicodemode"))
                                .description(OptionDescription.of(Text.translatable("beehivetooltip.option.unicodemode.description")))
                                .binding(
                                        false,
                                        () -> unicodeMode,
                                        value -> unicodeMode = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<HealthDisplayEnum>createBuilder()
                                .name(Text.translatable("beehivetooltip.option.healthdisplay"))
                                .description(OptionDescription.of(Text.translatable("beehivetooltip.option.healthdisplay.description")))
                                .binding(
                                        HealthDisplayEnum.COMPACT,
                                        () -> healthDisplay,
                                        value -> healthDisplay = value
                                )
                                .controller(opt -> EnumControllerBuilder.create(opt)
                                        .enumClass(HealthDisplayEnum.class))
                                .build())
                        .option(Option.<TooltipDisplayModeEnum>createBuilder()
                                .name(Text.translatable("beehivetooltip.option.displaymode"))
                                .description(OptionDescription.of(Text.translatable("beehivetooltip.option.displaymode.description")))
                                .binding(
                                        TooltipDisplayModeEnum.COMPACT,
                                        () -> displayMode,
                                        value -> displayMode = value
                                )
                                .controller(opt -> EnumControllerBuilder.create(opt)
                                        .enumClass(TooltipDisplayModeEnum.class))
                                .build())
                        .build())
                .save(this::save)
                .build()
                .generateScreen(parent);
    }

}
