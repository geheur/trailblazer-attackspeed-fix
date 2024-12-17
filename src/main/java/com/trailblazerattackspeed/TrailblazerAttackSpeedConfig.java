package com.trailblazerattackspeed;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("trailblazerattackspeed")
public interface TrailblazerAttackSpeedConfig extends Config
{
	@ConfigItem(
		keyName = "restartableAnimations",
		name = "Restartable animations",
		description = "Which animations to always restart.",
		position = 0
	) default String restartableAnimations() {
		return "10078,10173,10501,1056,1060,1062,1064,1068,1074,1132,1163,1164,1165,1166,1167,1168,1169,1203,1378,1576,1667,1872,2876,2890,3157,3300,391,407,5870,6118,6147,716,717,718,723,724,727,728,729,7512,7514,7515,7556,7638,7640,7642,7855,8291,8292,9171,9173,9848,9961,9962,9963,9964";
	}
	@ConfigItem(
		keyName = "restartAll",
		name = "Restart all",
		description = "",
		position = 1
	) default boolean restartAll() {
		return false;
	}

	@ConfigItem(
		keyName = "clippableAnimationsEnabled",
		name = "Clip these animations when moving",
		description = "",
		position = 100
	) default boolean clippableAnimationsEnabled() {
		return false;
	}
	@ConfigItem(
		keyName = "clippableAnimations",
		name = "",
		description = "",
		position = 101
	) default String clippableAnimations() {
		return "710,711,716,717,718,724,727,728,729,708,1162,1161,1163,1978,1979,4230,5439,1164,1165,1166,1167,1168,1169,1576,2323,3852,6118,7855,8209";
	}
	@ConfigItem(
		keyName = "normalWorldsToo",
		name = "Clip animations on normal worlds",
		description = "If left unchecked, will only clip animations on leagues worlds.",
		position = 102
	) default boolean normalWorldsToo() {
		return false;
	}

	@ConfigItem(
		name = "<html>To find animation ids, use the<br>Identificator plugin on the plugin hub.<br>Remember you can right-click<br>reset config items to restore defaults.</html>", keyName = "n", description = "",
		position = 200
	) default void animationsToReplay() { }
}
