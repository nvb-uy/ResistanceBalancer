package elocindev.resistance_balancer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import elocindev.necronomicon.api.config.v1.NecConfigAPI;
import elocindev.resistance_balancer.config.ResistanceBalancerConfig;

public class ResistanceBalancer implements ModInitializer {
	public static final String MODID = "resistance_balancer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static ResistanceBalancerConfig CONFIG = ResistanceBalancerConfig.INSTANCE;

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success)
		-> {
			NecConfigAPI.registerConfig(ResistanceBalancerConfig.class);
			CONFIG = ResistanceBalancerConfig.INSTANCE;
		});

		NecConfigAPI.registerConfig(ResistanceBalancerConfig.class);
		CONFIG = ResistanceBalancerConfig.INSTANCE;
	}
}