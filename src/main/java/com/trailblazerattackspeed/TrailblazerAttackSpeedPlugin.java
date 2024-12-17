package com.trailblazerattackspeed;

import com.google.inject.Provides;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.WorldType;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.events.PostAnimation;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
	name = "Leagues Animation Fix",
	tags = {"relic"}
)
public class TrailblazerAttackSpeedPlugin extends Plugin
{
	@Inject private Client client;
	@Inject private ClientThread clientThread;
	@Inject private TrailblazerAttackSpeedConfig config;

	private Set<Integer> animations = null;
	// These animations all stall the player. some must be clipped because they are longer than the cast speed, others are long enough that it makes the character fall behind.
	private Set<Integer> needToBeClipped = null;
	private boolean leaguesWorldsOnly = false;
	private boolean clipAnimations = false;
	private boolean restartAll = false;

	@Provides public TrailblazerAttackSpeedConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(TrailblazerAttackSpeedConfig.class);
	}

	@Override
	protected void startUp() {
		clientThread.invokeLater(this::loadConfig);
	}

	@Subscribe public void onConfigChanged(ConfigChanged e) {
		if (e.getGroup().equals("trailblazerattackspeed")) {
			clientThread.invokeLater(this::loadConfig);
		}
	}

	private void loadConfig()
	{
		try
		{
			leaguesWorldsOnly = !config.normalWorldsToo();
			clipAnimations = config.clippableAnimationsEnabled();
			restartAll = config.restartAll();
			animations = Text.fromCSV(config.restartableAnimations()).stream().map(Integer::parseInt).collect(Collectors.toSet());
			needToBeClipped = Text.fromCSV(config.clippableAnimations()).stream().map(Integer::parseInt).collect(Collectors.toSet());
			client.getAnimationCache().reset();
		} catch (NumberFormatException e) {
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "bla", ColorUtil.wrapWithColorTag("You entered something that wasn't a number in the trailblazer attack speed plugin's config. Please fix it or the plugin will not work.", Color.RED), "bla");
			// prevent NPEs.
			needToBeClipped = Set.of();
			animations = Set.of();
		}
	}

	@Override
	protected void shutDown()
	{
		clientThread.invokeLater(() -> {
			client.getAnimationCache().reset();
			playerDataMap.clear();
			animations = null;
			needToBeClipped = null;
		});
	}

	@Subscribe
	public void onPostAnimation(PostAnimation e)
	{
		if (restartAll || animations.contains(e.getAnimation().getId())) {
			e.getAnimation().setRestartMode(1);
		}
	}
	
	@Data
	private static final class PlayerData {
		WorldPoint lastLocation = null;
		int lastAnimation = -1;
	}

	private final Map<Player, PlayerData> playerDataMap = new HashMap<>();

	@Subscribe
	public void onPlayerDespawned(PlayerDespawned e) {
		playerDataMap.remove(e.getPlayer());
	}

	@Subscribe
	public void onGameTick(GameTick e) {
		if (clipAnimations && (!leaguesWorldsOnly || client.getWorldType().contains(WorldType.SEASONAL)))
		{
			for (Player player : client.getPlayers())
			{
				PlayerData playerData = getPlayerData(player);
				WorldPoint worldPoint = playerData.lastLocation;
				if (!player.getWorldLocation().equals(worldPoint) && playerData.lastAnimation == player.getAnimation() && needToBeClipped.contains(player.getAnimation()))
				{
					player.setAnimation(-1);
				}
				playerData.lastLocation = player.getWorldLocation();
				playerData.lastAnimation = player.getAnimation();
			}
		}
	}

	private PlayerData getPlayerData(Player player)
	{
		PlayerData playerData = playerDataMap.get(player);
		if (playerData == null) {
			playerData = new PlayerData();
			playerDataMap.put(player, playerData);
		}
		return playerData;
	}
}
