package elocindev.resistance_balancer.config;

import elocindev.necronomicon.api.config.v1.NecConfigAPI;
import elocindev.necronomicon.config.Comment;
import elocindev.necronomicon.config.NecConfig;

public class ResistanceBalancerConfig {
    @NecConfig
    public static ResistanceBalancerConfig INSTANCE;

    public static String getFile() {
        return NecConfigAPI.getFile("resistance_balancer.json5");
    }

    @Comment("--------------------------------------------------------------------------------------------------------")
	@Comment("                                    Resistance Balancer by ElocinDev")
	@Comment("--------------------------------------------------------------------------------------------------------")
	@Comment(" ")
	@Comment(" This mod allows rebalance the Resistance effect by changing the formula used to calculate the damage")
	@Comment(" mitigation. This is useful to balance the game to your liking.")
	@Comment(" ")
	@Comment("--------------------------------------------------------------------------------------------------------")
	@Comment("Do not touch this")
	public int configVersion = 1;
	@Comment("--------------------------------------------------------------------------------------------------------")
	@Comment("                                            Custom Formula")
	@Comment(" ")
	@Comment(" The result of this formula will be the total damage reduced")
	@Comment(" ")
	@Comment(" Variables:")
	@Comment("   RESISTANCE : The amount of resistance")
	@Comment(" ")
	@Comment(" By the mod's default, the formula is: min(RESISTANCE / 4.0, 1.0) * 0.20")
	@Comment(" This formula will mitigate up to 20% of the damage at Resistance IV")
	@Comment(" ")
	@Comment(" You can define a more complex formula that fits for you, as this was designed for the Prominence modpack.")
	@Comment("--------------------------------------------------------------------------------------------------------")
	public boolean enable_custom_formula = false;
	public String custom_formula = "min(RESISTANCE / 4.0, 1.0) * 0.20";
	@Comment("--------------------------------------------------------------------------------------------------------")
	@Comment(" ")
	@Comment("If custom formula is disabled, it will instead use a simpler logic, simply reducing the damage by a percentage.")
	@Comment(" By default this is 0.05, meaning Resistance would reduce 5% of the damage dealt per level. So at Resistance IV, it would reduce 20% of the damage.")
	public float percentile_reduction_fallback = 0.05F;
}
