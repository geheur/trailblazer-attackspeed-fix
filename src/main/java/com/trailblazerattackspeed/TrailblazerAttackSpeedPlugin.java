package com.trailblazerattackspeed;

import java.util.Set;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.PostAnimation;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Leagues Animation Fix",
	tags = {"relic"}
)
public class TrailblazerAttackSpeedPlugin extends Plugin
{
	@Inject private Client client;
	@Inject private ClientThread clientThread;

	private static final Set<Integer> animations = Set.of(245,376,377,380,381,382,386,390,391,393,395,400,401,406,407,414,419,422,423,426,428,429,438,440,708,723,811,1056,1058,1060,1062,1064,1067,1068,1074,1132,1161,1162,1163,1164,1165,1166,1167,1168,1169,1203,1378,1428,1576,1658,1665,1667,1710,1711,1712,1833,1872,1978,1979,2062,2066,2067,2068,2075,2080,2081,2082,2323,2661,2876,2890,3157,3294,3297,3298,3299,3300,3678,3852,4230,4503,4505,5061,5063,5439,5865,5870,6118,6147,6278,7004,7045,7046,7054,7055,7275,7328,7511,7512,7514,7515,7516,7521,7541,7552,7554,7555,7556,7558,7617,7618,7638,7640,7642,7644,7855,8010,8056,8145,8194,8195,8209,8288,8289,8290,8291,8292,8972,8974,8977,9168,9171,9173,9347,9471,9493,9848,9858,9961,9962,9963,9964,10078,10079,10171,10172,10173,10501);

	@Override
	protected void startUp()
	{
		clientThread.invokeLater(() -> client.getAnimationCache().reset());
	}

	@Override
	protected void shutDown()
	{
		clientThread.invokeLater(() -> client.getAnimationCache().reset());
	}

	@Subscribe
	public void onPostAnimation(PostAnimation e)
	{
		if (animations.contains(e.getAnimation().getId())) {
			e.getAnimation().setRestartMode(1);
		}
	}
}
